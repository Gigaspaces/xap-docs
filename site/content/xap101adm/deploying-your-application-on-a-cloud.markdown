---
type: post101
title:  Deploying on a Cloud
categories: XAP101ADM
weight: 450
parent: none
---



XAP 10 Comes with a built-in Cloudify recipe feature that allows the deployment and management of an elastic Data Grid onto any cloud. For more information on supported Cloud and Cloud Drivers see the [Cloudify User Guide](http://www.cloudifysource.org/guide/). The following sections will demonstrate how to compose a recipe for deploying and managing your XAP Elastic Data Grid on the Cloud.

The following XAP types of services are supported:

- In memory elastic data grid

# Using an Elastic Data Grid


```java
	service {
		name = "xap-management"		
	}

	service {
		name = "xap-datagrid"
		dependsOn = ["xap-management"]
	}
```


# Packing Your Recipe

In order to have a mixture of services (XAP EDG and non XAP) you need to prepare the following structure:
![recipe_folder.png](/attachment_files/recipe_folder.png)
The application recipe should describe the different services and the dependencies between services:


```java
application {
	name="myApp"

	service {
		name = "xap-management"		
	}

	service {
		name = "xap-datagrid"
		dependsOn = ["xap-management"]
	}

	service {
		name = "feeder"
		dependsOn = ["xap-datagrid"]
	}
	service {
		name = "processor"
		dependsOn = ["xap-datagrid"]
	}
}
```

You can find a working example that includes a stateful and a stateless PU [here](https://github.com/CloudifySource/cloudify-recipes/tree/master/apps/streaming-bigdata).

# Deploying Your Recipe

{{% info title="Useful Information "%}}
In order to use your license on the cloud environment you should perform the following:

- cd to `<XAP installation root>/tools/cli/plugins/ecs/<cloud name>/upload`
- create directory cloudify-overrides
- copy your license(<XAP installation root>/gslicense.xml) to the above directory.
{{% /info %}}

### Testing on a `localcloud`

XAP 9.0 comes with a cloud emulator called [`localcloud`](http://getcloudify.org/guide/{{%latestcloudifyrelease%}}/bootstrapping/bootstrapping_localcloud) that allows you to test the recipe execution

To start the localcloud environment:

- cd to `<XAP installation root>/bin/`
- Type `cloudify.sh/bat`
- In the Cloudify shell, type `bootstrap-localcloud` to start the localcloud services. This might take a minute or two to complete.
- Deploy the application using the `install-application` command:


```java
install-application c:/rt-app
```

You can track the progress in the shell and in the Web Management Console at localhost:8099.
For more information refer to [the cloudify application deployment guide](http://getcloudify.org/guide/{{%latestcloudifyrelease%}}/deploying/deploying_apps.html).

### Running on Any Cloud

To deploy your recipe to one a cloud environment, follow the below steps:

- Obtain your cloud's credentials and [configure the cloud driver](http://getcloudify.org/guide/{{%latestcloudifyrelease%}}/setup/post_installation_configuration.html))
- Bootstrap the cloud using [`bootstrap-cloud` command](http://getcloudify.org/guide/{{%latestcloudifyrelease%}}/bootstrapping/bootstrapping_process.html).
- [Install the application](http://getcloudify.org/guide/{{%latestcloudifyrelease%}}/deploying/deploying_apps.html) using the `install-application` command, as described in the previous section.
