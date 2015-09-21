---
type: post
title:  Moving from Hibernate to Space
categories: SBP
parent: data-access-patterns.html
weight: 1100
---



{{% tip %}}
**Summary:**  Moving Spring/Hibernate Application to GigaSpaces <br/>
**Author**: Shay Hassidim, Deputy CTO, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 8.0<br/>
**Last Update:** Feb 2011<br/>


{{% /tip %}}

# Overview
The following example is based on the standard [Spring Hibernate Integration tutorial](http://www.vaannila.com/spring/spring-hibernate-integration-1.html).
In this pattern you will see how to modify an existing simple spring/hibernate application to leverage GigaSpaces as the [in-memory Data-grid](/product_overview/the-in-memory-data-grid.html) and the application server hosting both the [web application]({{%latestjavaurl%}}/web-application-support.html) and the data in-memory. The Hibernate persistency settings will still be leveraged by [GigaSpaces Hibernate External Data Source]({{%latestjavaurl%}}/hibernate-space-persistency.html) storing the data in-memory into a database in an [asynchronous manner]({{%latestjavaurl%}}/asynchronous-persistency-with-the-mirror.html).

Moving Spring/Hibernate application to GigaSpaces involves the following basic steps:

1. Spring bean Configuration file changes
2. [POJO Class]({{%latestjavaurl%}}/pojo-support.html) changes
3. DAO implementation changes
4. Deploying the data-grid and the Spring based web application into GigaSpaces

# Architecture Change

The procedure described below will move a standard Spring/Hibernate application that is using the following architecture:
![Hibernate with EhCache.jpg](/attachment_files/sbp/Hibernate with EhCache.jpg)

To use the following architecture where the Data-Grid placed in-line between the application and the database:
![Hibernate_DataGrid_mirror.jpg](/attachment_files/sbp/Hibernate_DataGrid_mirror.jpg)

# Spring bean Configuration File
The existing application Spring bean Configuration file will be modified to:

- Remove the usage of database at the application layer.
- Use the GigaSpaces DAO implementation instead of the original Hibernate DAO implementation.
- Use Data Grid (space) as the data access layer instead of the database.
- Add a Spring Configuration file for the Data-Grid (Data-Grid PU).
- Add a Spring Configuration file for the Mirror (Mirror PU).

{{%tabs%}}

{{%tab "  Hibernate spring bean configuration file "%}}


```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:p="http://www.springframework.org/schema/p"
 	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
	<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver"
		p:prefix="/WEB-INF/jsp/" p:suffix=".jsp" />

	<bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
		<property name="driverClassName" value="org.hsqldb.jdbcDriver"/>
		<property name="url" value="jdbc:hsqldb:hsql://localhost/xdb"/>
		<property name="username" value="sa"/>
		<property name="password" value=""/>
	</bean>

	<bean id="mySessionFactory" class="org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean">
		<property name="dataSource" ref="myDataSource" />
		<property name="annotatedClasses">
			<list>
				<value>com.vaannila.domain.User</value>
			</list>
		</property>
		<property name="hibernateProperties">
			<props>
				<prop key="hibernate.dialect">org.hibernate.dialect.HSQLDialect</prop>
				<prop key="hibernate.show_sql">true</prop>
				<prop key="hibernate.hbm2ddl.auto">update</prop>
			</props>
		</property>
	</bean>

	<bean id="myUserDAO" class="com.vaannila.dao.UserDAOImpl">
		<property name="sessionFactory" ref="mySessionFactory"/>
	</bean>

	<bean name="/user/*.htm" class="com.vaannila.web.UserController" >
		<property name="userDAO" ref="myUserDAO" />
	</bean>
</beans>
```

{{% /tab %}}

{{%tab "  GigsSpaces spring bean configuration file "%}}

## The Application spring bean configuration file
The {myUserSpaceDAO}} includes the GigaSpaces DAO.


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
	   xmlns:p="http://www.springframework.org/schema/p"

 	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
	       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/core/openspaces-core.xsd">

	<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver"
		p:prefix="/WEB-INF/jsp/" p:suffix=".jsp" />

	<bean name="/user/*.htm" class="com.vaannila.web.UserController" >
		<property name="userDAO" ref="myUserSpaceDAO" />
	</bean>

	<os-core:space-proxy id="space" name="mySpace" />
	<os-core:giga-space id="gigaSpace" space="space"/>

	<bean id="myUserSpaceDAO" class="com.vaannila.dao.UserDAOSpaceImpl">
		<property name="gigaspace" ref="gigaSpace"/>
	</bean>
</beans>
```

## The Data-Grid PU spring bean configuration file


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:os-events="http://www.openspaces.org/schema/events"
       xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd">

    <!--
        Spring property configurer which allows us to use system properties (such as user.name).
    -->
    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>

    <!--
        Enables the usage of @GigaSpaceContext annotation based injection.
    -->
    <os-core:giga-space-context/>

    <!--
        A JDBC pooled data source that connects to the HSQL server the mirror starts.
    -->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
        <property name="driverClassName" value="org.hsqldb.jdbcDriver"/>
        <property name="url" value="jdbc:hsqldb:hsql://localhost/xdb"/>
        <property name="username" value="sa"/>
        <property name="password" value=""/>
    </bean>

    <!--
        Hibernate SessionFactory bean. Uses the pooled data source to connect to the database.
    -->
    <bean id="sessionFactory" class="org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="annotatedClasses">
            <list>
                <value>com.vaannila.domain.User</value>
            </list>
        </property>
        <property name="hibernateProperties">
            <props>
                <prop key="hibernate.dialect">org.hibernate.dialect.HSQLDialect</prop>
                <prop key="hibernate.cache.provider_class">org.hibernate.cache.NoCacheProvider</prop>
                <prop key="hibernate.cache.use_second_level_cache">false</prop>
                <prop key="hibernate.cache.use_query_cache">false</prop>
                <prop key="hibernate.hbm2ddl.auto">update</prop>
				<prop key="hibernate.show_sql">true</prop>
            </props>
        </property>
    </bean>

    <bean id="hibernateDataSource" class="org.openspaces.persistency.hibernate.DefaultHibernateExternalDataSource">
        <property name="sessionFactory" ref="sessionFactory"/>
    </bean>

    <!--
        A bean representing a space (an IJSpace implementation).

        Note, we do not specify here the cluster topology of the space. It is declared outside of
        the processing unit or within the SLA bean.

        The space is configured to connect to a mirror, and uses the configured external data source
        to perform the initial load operation from the database when the Space starts up.
    -->
    <os-core:embedded-space id="space" name="mySpace" schema="persistent" mirror="true"
                   external-data-source="hibernateDataSource">
        <os-core:properties>
            <props>
                <!-- Use ALL IN CACHE -->
                <prop key="space-config.engine.cache_policy">1</prop>
                <prop key="space-config.external-data-source.usage">read-only</prop>
                <prop key="cluster-config.cache-loader.external-data-source">true</prop>
                <prop key="cluster-config.cache-loader.central-data-source">true</prop>
            </props>
        </os-core:properties>
    </os-core:embedded-space>

    <!--
        Defines a local Jini transaction manager.
    -->
    <os-core:local-tx-manager id="transactionManager" space="space"/>

    <!--
        OpenSpaces simplified space API built on top of IJSpace/JavaSpace.
    -->
    <os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

</beans>
```

## The Mirror PU spring bean configuration file


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:os-events="http://www.openspaces.org/schema/events"
       xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
       xmlns:os-sla="http://www.openspaces.org/schema/sla"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/sla/openspaces-sla.xsd">

    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    </bean>

    <!--
        A JDBC datasource pool that connects to HSQL.
    -->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close" >
        <property name="driverClassName" value="org.hsqldb.jdbcDriver"/>
        <property name="url" value="jdbc:hsqldb:hsql://localhost/xdb"/>
        <property name="username" value="sa"/>
        <property name="password" value=""/>
    </bean>

    <!--
        Hibernate SessionFactory bean. Uses the pooled data source to connect to the database.
    -->
    <bean id="sessionFactory" class="org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="annotatedClasses">
            <list>
                <value>com.vaannila.domain.User</value>
            </list>
        </property>
        <property name="hibernateProperties">
            <props>
                <prop key="hibernate.dialect">org.hibernate.dialect.HSQLDialect</prop>
                <prop key="hibernate.cache.provider_class">org.hibernate.cache.NoCacheProvider</prop>
                <prop key="hibernate.cache.use_second_level_cache">false</prop>
                <prop key="hibernate.cache.use_query_cache">false</prop>
                <prop key="hibernate.hbm2ddl.auto">update</prop>
				<prop key="hibernate.show_sql">true</prop>
            </props>
        </property>
    </bean>

    <!--
        An external data source that will be responsible for persisting changes done on the cluster that
        connects to this mirror using Hibernate.
    -->
	<bean id="hibernateDataSource" class="org.openspaces.persistency.hibernate.DefaultHibernateExternalDataSource">
        <property name="sessionFactory" ref="sessionFactory"/>
    </bean>

    <!--
        The mirror space. Uses the Hiberante external data source. Persists changes done on the Space that
        connects to this mirror space into the database using Hibernate.
    -->
	<os-core:embedded-space id="mirror" name="mirror-service" schema="mirror" external-data-source="hibernateDataSource" >
		<os-core:properties>
			<props>
				<prop key="space-config.mirror-service.cluster.name">mySpace</prop>
			</props>
		</os-core:properties>
	</os-core:embedded-space>

</beans>
```

{{% /tab %}}

{{% /tabs %}}

# The POJO Class
The POJO Class will be modified to include:

- SpaceID field
- Routing field
- Indexed fields
- Other Space class metadata decorations

{{%tabs%}}

{{%tab "  Hibernate POJO Class "%}}


```java
package com.vaannila.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER")
public class User {

	private Long id;  private String name; 	private String password;	private String gender;
	private String country; private String aboutYou; private String[] community; private Boolean mailingList;

	@Id
	@GeneratedValue
	@Column(name="USER_ID")
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@Column(name="USER_NAME")
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	@Column(name="USER_PASSWORD")
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}

	@Column(name="USER_GENDER")
	public String getGender() {return gender;}
	public void setGender(String gender) {this.gender = gender;	}

	@Column(name="USER_COUNTRY")
	public String getCountry() {return country;}
	public void setCountry(String country) {this.country = country;}

	@Column(name="USER_ABOUT_YOU")
	public String getAboutYou() {return aboutYou;}
	public void setAboutYou(String aboutYou) {this.aboutYou = aboutYou;}

	@Column(name="USER_COMMUNITY")
	public String[] getCommunity() {return community;}
	public void setCommunity(String[] community) {this.community = community;}

	@Column(name="USER_MAILING_LIST")
	public Boolean getMailingList() {return mailingList;}
	public void setMailingList(Boolean mailingList) {this.mailingList = mailingList;}
}
```

{{% /tab %}}

{{%tab "  GigsSpaces POJO Class "%}}


```java
package com.vaannila.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

@Entity
@Table(name="USER")
public class User {

	private Long id;	private String name;	private String password;	private String gender;	private String country;
	private String aboutYou; 	private String[] community; 	private Boolean mailingList;

	@Id
	@GeneratedValue
	@Column(name="USER_ID")
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@Column(name="USER_NAME")
	@SpaceId(autoGenerate=false) // <-- ADDED FOR GIGASPACES
	@SpaceRouting				 // <-- ADDED FOR GIGASPACES
	public String getName() {return name;}
	public void setName(String name) {this.name = name;	}

	@Column(name="USER_PASSWORD")
	public String getPassword() {return password;	}
	public void setPassword(String password) {this.password = password;	}

	@Column(name="USER_GENDER")
	public String getGender() {return gender;}
	public void setGender(String gender) {this.gender = gender;	}

	@Column(name="USER_COUNTRY")
	public String getCountry() {return country;	}
	public void setCountry(String country) {this.country = country;}

	@Column(name="USER_ABOUT_YOU")
	public String getAboutYou() {return aboutYou;}
	public void setAboutYou(String aboutYou) {this.aboutYou = aboutYou;	}

	@Column(name="USER_COMMUNITY")
	public String[] getCommunity() {return community;}
	public void setCommunity(String[] community) {this.community = community;}

	@Column(name="USER_MAILING_LIST")
	public Boolean getMailingList() {return mailingList;}
	public void setMailingList(Boolean mailingList) {this.mailingList = mailingList;	}
}
```

{{% /tab %}}

{{% /tabs %}}

# UserDAO interface
The UserDAO interface remains as is:


```java
package com.vaannila.dao;

import java.util.List;
import com.vaannila.domain.User;

public interface UserDAO {
	public void saveUser(User user) ;
	public List<User> listUser() ;
}
```

# The DAO Implementation
The DAO implementation should be modified to use the [GigaSpace interface]({{%latestjavaurl%}}/the-gigaspace-interface.html) to access the data grid instead of using the `HibernateTemplate` that is accessing the database. The `GigaSpace` interface similar methods to the `HibernateTemplate` to write and [Query]({{%latestjavaurl%}}/query-sql.html) for objects.

{{%tabs%}}

{{%tab "  Hibernate DAO Implemenation "%}}


```java
package com.vaannila.dao;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.vaannila.domain.User;

public class UserDAOImpl implements UserDAO {

	private HibernateTemplate hibernateTemplate;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	@Override
	public void saveUser(User user) {hibernateTemplate.saveOrUpdate(user);	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> listUser() {	return hibernateTemplate.find("from User");	}
}
```

{{% /tab %}}

{{%tab "  GigsSpaces DAO Implemenation "%}}


```java
package com.vaannila.dao;

import java.util.ArrayList;
import java.util.List;
import org.openspaces.core.GigaSpace;
import com.vaannila.domain.User;

public class UserDAOSpaceImpl implements UserDAO {

	private GigaSpace gigaspace;

	@Override
	public void saveUser(User user) {
		gigaspace.write(user);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> listUser() {
		List<User> users = new ArrayList<User>();
		User usersArry[] = gigaspace.readMultiple(new User(), Integer.MAX_VALUE);
		for (User user : usersArry) {
			users.add(user);
		}
		return users;
	}

	public GigaSpace getGigaspace() {return gigaspace;}
	public void setGigaspace(GigaSpace gigaspace) {this.gigaspace = gigaspace;}
}
```

{{% /tab %}}

{{% /tabs %}}

# Deploying the Data-Grid and the Application
To deploy the Data-Grid and the web Application into the [GigaSpaces runtime environment]({{%latestadmurl%}}/the-runtime-environment.html) perform the following:

- Download the [3rd party libraries](/attachment_files/sbp/3rd_party_libraries.zip) package, and extract it into the `\gigaspaces-xap\lib\optional\pu-common` folder.
- Download the [application.war](/attachment_files/sbp/application.war) , [myDataGrid.jar](/attachment_files/sbp/myDataGrid.jar) and the [myMirror.jar](/attachment_files/sbp/myMirror.jar).
- Start the [GigaSpaces agent](/product_overview/service-grid.html#gsa):
On windows run the following command:


```java
\gigaspaces-xap\bin\gs-agent.bat
```

On linux run the following command:


```java
\gigaspaces-xap\bin\gs-agent.sh
```

- Start the HSQLDB database:


```java
java -cp ../lib/platform/jdbc/hsqldb.jar org.hsqldb.Server -database.0 file:mydb -dbname.0 xdb
```

Once the agent is up and running call the deploy commands.

- Deploy the Data-grid using:


```java
gs.sh deploy myDataGrid.jar
```

You should see the following:


```java
Found 1 GSMs
Deploying [myDataGrid.jar] with name [myDataGrid] under groups [gigaspaces-8.0.0-XAPPremium-ga] and locators []
Uploading [myDataGrid] to [http://192.168.1.101:3354/]
Waiting for [2] processing unit instances to be deployed...
[myDataGrid.1] [1] deployed successfully on [192.168.1.101]
[myDataGrid.1] [2] deployed successfully on [192.168.1.101]
Finished deploying [2] processing unit instances
```

- Deploy the Mirror using:


```java
gs.sh deploy myMirror.jar
```

You should see the following:


```java
Found 1 GSMs
Deploying [myMirror.jar] with name [myMirror] under groups [gigaspaces-8.0.0-XAPPremium-ga] and locators []
Uploading [myMirror] to [http://192.168.1.101:3354/]
SLA Not Found in PU.  Using Default SLA.
Waiting for [1] processing unit instances to be deployed...
[myMirror] [1] deployed successfully on [192.168.1.101]
Finished deploying [1] processing unit instances
```

- Deploy the web Application using:


```java
gs.sh deploy application.war
```

You should see the following:


```java
Found 1 GSMs
Deploying [application.war] with name [application] under groups [gigaspaces-8.0.0-XAPPremium-ga] and locators []
Uploading [application] to [http://192.168.1.101:3354/]
Waiting for [1] processing unit instances to be deployed...
[application] [1] deployed successfully on [192.168.1.101]
Finished deploying [1] processing unit instances
```

- To view the deployed application and data-grid start the GS-UI:
On windows run the following command:


```java
\gigaspaces-xap\bin\gs-ui.bat
```

On linux run the following command:


```java
\gigaspaces-xap\bin\gs-ui.sh
```

- Once the Data-Grid, Mirror and the application will be deployed you should see the following within the GS-UI:

{{% indent %}}
![hib2space1.jpg](/attachment_files/sbp/hib2space1.jpg)
{{% /indent %}}

- You can start the web application and register users:

{{% indent %}}
![hib2space3.jpg](/attachment_files/sbp/hib2space3.jpg)
{{% /indent %}}

Each registered user data will be stored within the space and also be persist into the database.

- To view the data within the space click the Data Types , select the User class and click Query:

{{% indent %}}
![hib2space2.jpg](/attachment_files/sbp/hib2space2.jpg)
{{% /indent %}}

This will display the Query view with the User objects data stored within the space:

{{% indent %}}
![hib2space5.jpg](/attachment_files/sbp/hib2space5.jpg)
{{% /indent %}}

- To view the data within the database, start the database UI :


```java
java -cp  ../lib/platform/jdbc/hsqldb.jar  org.hsqldb.util.DatabaseManager
```

And query the database:

{{% indent %}}
![hib2space4.jpg](/attachment_files/sbp/hib2space4.jpg)
{{% /indent %}}

{{% exclamation %}} You may download the full source code of the application [here](/attachment_files/sbp/HibernateToSpace.zip).
