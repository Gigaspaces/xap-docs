---
type: post
title:  Safe Grid Shutdown
categories: SBP
parent: production.html
weight: 1200
---

{{% ssummary page %}}This best practice illustrates an approach that can be used to perform a clean shutdown mechanism by waiting for all asynchronous persistence to finish before killing Grid Service Containers.{{% /ssummary %}}


{{% tip %}}
**Author**: Ali Hodroj, Senior Solutions Architect, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 9.6.0<br/>


{{% /tip %}}

# Problem
Shutting down an entire cluster in GigaSpaces XAP is usually done through the "gsa shutdown" command in the [gsa - GigaSpaces CLI]({{%latestadmurl%}}/command-line-interface.html#gsa). However, in cases of a space asynchronously replicating to a persistent store ([Asynchronous Persistency with the Mirror]({{%latestjavaurl%}}/asynchronous-persistency-with-the-mirror.html)) or a remote grid ([Multi-Site Replication over the WAN]({{%latestjavaurl%}}/multi-site-replication-over-the-wan.html)), the gsa shutdown workflow does not wait for replication redo logs to completely flush before killing the child GSC processes. Since replication redo logs are almost always stored in memory, this could lead to a situation where pending space changes do not make it to an external data store or cluster.

# Solution
To ensure that no pending asynchronous replication data is lost during shutdown, we utilize the [Admin API]({{%latestjavaurl%}}/administration-and-monitoring-api.html) to ensure that the shutdown process does not kill all processes until all replication operations have been committed (redo log size is 0). This mechanism is achieved through the following orderly steps:
1.	Wait until the redo log size for mirrors is 0
2.	Wait until the redo log size for all backups is 0
If the redo logs are not flushed after a specific timeout, an E-mail alert is sent as a warning and the shutdown process is cancelled.

# Application
The sample code below is meant to illustrate how the Admin API can be used to discover Grid Service Containers, Spaces, and Mirrors in order to check the replication statistics.

{{% note %}}
 This example uses multicast to discover the service grid components. For unicast discovery, you can use the  [addLocator()](http://www.gigaspaces.com/docs/JavaDoc{{%latestxaprelease %}}/org/openspaces/admin/AdminFactory.html#addLocator(java.lang.String)) method with a LUS address.
{{% /note %}}

{{%tabs%}}

{{%tab "  Grid Shutdown Code "%}}


```java
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.openspaces.admin.gsa.GridServiceAgent;
import org.openspaces.admin.space.Space;
import org.openspaces.admin.space.SpaceInstance;

import com.gigaspaces.cluster.replication.async.mirror.MirrorStatistics;
import com.j_spaces.core.filters.ReplicationStatistics;
import com.j_spaces.core.filters.ReplicationStatistics.OutgoingReplication;

public class GridShutdown {

	public static void main(String[] args) throws Exception {

		long startTime = System.currentTimeMillis();

		Properties props = loadProperties();
		String group = props.getProperty("lookupgroups");
		String mailDomain = props.getProperty("mailDomain");
		String toMail = props.getProperty("toMail");
		String mailSubject = props.getProperty("mailSubject");
		String fromMail = props.getProperty("fromMail");
		int numberOfAgentsToShutdown = Integer.parseInt(props.getProperty("numberOfGSAgents"));
		int transactionTimeout = Integer.parseInt(props.getProperty("transactionTimeout"));;

		if ((group == null || group.length() == 0)
				|| (mailDomain == null || mailDomain.length() == 0)
				|| (toMail == null || toMail.length() == 0)
				|| (mailSubject == null || mailSubject.length() == 0)
				|| (fromMail == null || fromMail.length() == 0)) {
			throw new Exception("Invalid Input parameters ");
		}

		Scanner keyboard = new Scanner(System.in);
		System.out.println("Are you sure you want to shut down the entire service grid?");
		String next = keyboard.next();

		if (! next.equals("Y"))
		{
			System.exit(0);
		}

		System.out.println("Starting Shutdown of " + numberOfAgentsToShutdown + " agents");
		Admin admin = new AdminFactory().addGroup(group).createAdmin();

		Thread.sleep(3000);
		//admin.getGridServiceManagers().waitForAtLeastOne();
		admin.getGridServiceAgents().waitFor(numberOfAgentsToShutdown);

		System.out.println("Wait for transactions to complete");
		Thread.sleep(transactionTimeout);

		System.out.println("Testing mirrors");
		// Test the mirrors for shutdown
		List<SpaceInstance> mirrors = findAllMirrors(admin);
		List<String> pending = testMirrorsForShutDown(mirrors);

		if (!pending.isEmpty()) {
			System.out
					.println("Some of the mirrors are not finished flushing the content");

			for (String name : pending) {
				System.out.println("Mirror :" + name
						+ " needs to flush entries");
			}

			sendMail(toMail,
					mailSubject,
					"Some of the mirrors are not finished flushing the content",
					fromMail, mailDomain);

			System.exit(0);
		}

		System.out.println("Testing Redo logs");
		// Test if the replication is done
		List<SpaceInstance> redos = findRedoLogs(admin);
		pending = testReplicationForShutDown(redos);

		if (!pending.isEmpty()) {
			System.out
					.println("Some of the spaces have not finished replicating the content");

			for (String name : pending) {
				System.out.println("Space :" + name
						+ " needs to replicate entries");
			}

			sendMail(
					toMail,
					mailSubject,
					"Some of the spaces have not finished replicating the content",
					fromMail, mailDomain);

			System.exit(0);
		}

		System.out.println("Shutting down grid service agents");
		// Now we are ready to shutdown the agents
		for (GridServiceAgent agent : admin.getGridServiceAgents().getAgents()) {
			shutdownAgent(agent);
		}

		System.out.println("Shutdown completed in : "
				+ (System.currentTimeMillis() - startTime) / 1000 + " seconds");
		System.exit(0);
	}

	public static void shutdownAgent(GridServiceAgent agent) {

		System.out.println("Shutting down " + agent + " on "+ agent.getMachine().getHostName());
		agent.shutdown();

	}

	public static List<String> testMirrorsForShutDown(
			List<SpaceInstance> mirrors) {

		List<String> list = new ArrayList<String>();

		for (SpaceInstance spaceInstance : mirrors) {
			MirrorStatistics mirrorStat = spaceInstance.getStatistics()
					.getMirrorStatistics();
			System.out.println("	Mirror Stats:"
					+ spaceInstance.getSpace().getName());
			System.out.println("		total operation count:"
					+ mirrorStat.getOperationCount());
			System.out.println("		successful operation count:"
					+ mirrorStat.getSuccessfulOperationCount());
			System.out.println("		failed operation count:"
					+ mirrorStat.getFailedOperationCount());
			System.out.println("		in progress operation count:"
					+ mirrorStat.getInProgressOperationCount());

			if (mirrorStat.getInProgressOperationCount() > 0) {
				System.out
						.println("***** WARNING: Mirror in progress count > 0");
				System.out.println("Mirror :"
						+ spaceInstance.getSpace().getName() + "  has "
						+ mirrorStat.getInProgressOperationCount()
						+ "  pending Operations");

				list.add(spaceInstance.getSpace().getName());
			}
		}
		return list;
	}

	public static ArrayList<SpaceInstance> findAllMirrors(Admin admin) {
		ArrayList<SpaceInstance> mirrors = new ArrayList<SpaceInstance>();

		for (Space space : admin.getSpaces()) {
			for (SpaceInstance spaceInstance : space) {

				MirrorStatistics mirrorStat = spaceInstance.getStatistics()
						.getMirrorStatistics();
				// check if this instance is mirror
				if (mirrorStat != null) {
					mirrors.add(spaceInstance);
				}
			}
		}
		return mirrors;
	}

	private static List<String> testReplicationForShutDown(
			List<SpaceInstance> spaces) {

		List<String> list = new ArrayList<String>();

		for (SpaceInstance spaceInstance : spaces) {
			ReplicationStatistics rs = spaceInstance.getStatistics()
					.getReplicationStatistics();

			if (rs != null) {
				OutgoingReplication repStats = spaceInstance.getStatistics()
						.getReplicationStatistics().getOutgoingReplication();

				System.out.println("	Outgoing Replication:"
						+ spaceInstance.getSpace().getName());
				System.out.println("		Redo log size:"
						+ repStats.getRedoLogSize());
				System.out.println("		memory packet count:"
						+ repStats.getRedoLogMemoryPacketCount());
				System.out.println("		external storage packet count:"
						+ repStats.getRedoLogExternalStoragePacketCount());
				System.out.println("		extenral storage space used:"
						+ repStats.getRedoLogExternalStorageSpaceUsed());

				if (repStats.getRedoLogSize() > 0) {
					System.out
							.println("***** WARNING: Replication in progress count > 0");
					System.out.println("Mirror :"
							+ spaceInstance.getSpace().getName() + "  has "
							+ repStats.getRedoLogSize()
							+ "  pending RedoLogSize");

					list.add(spaceInstance.getSpace().getName());
				}
			}
		}
		return list;
	}

	public static List<SpaceInstance> findRedoLogs(Admin admin) {
		List<SpaceInstance> list = new ArrayList<SpaceInstance>();

		for (Space space : admin.getSpaces()) {
			for (SpaceInstance spaceInstance : space) {
				ReplicationStatistics rs = spaceInstance.getStatistics()
						.getReplicationStatistics();

				if (rs != null) {
					list.add(spaceInstance);
				}
			}
		}
		return list;
	}

	public static void sendMail(String recipient, String subject, String body,
			String fromMail, String mailDomain) {

		Properties props = new Properties();

		props.put("mail.smtp.host", mailDomain);

		Session session = Session.getInstance(props);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromMail));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipient));
			message.setSubject(subject);
			message.setText(body);

			Transport transport = session.getTransport("smtp");
			transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private static Properties loadProperties() throws Exception {
		try {
			Properties prop = new Properties();
			InputStream in = new FileInputStream("shutdown.properties");
			prop.load(in);
			in.close();

			return prop;

		} catch (IOException e) {
			throw new Exception("Config file 'shutdown.properties' not found");
		}
	}
}
```

{{% /tab %}}

{{%tab "  shutdown.properties "%}}


```xml
lookupgroups = MyGroup

mailDomain=smtp.server.com

toMail=email@email.com

fromMail=email@email.com

mailSubject=Grid Shutdown Failure

numberOfGSAgents = 1

transactionTimeout = 1000
```

{{% /tab %}}

{{% /tabs %}}

