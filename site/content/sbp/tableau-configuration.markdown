---
type: post
title:  Easy Soft bridge configuration
categories: SBP
parent: tableau.html
weight: 10
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Vitalii Zinchenko    | 12.2 | September 2017|    |     |

# To configure the Tableau-DataGrid integration, complete the following steps:

## Prerequisites

- Run XAP Datagrid
- Make sure you have a space `<SPACE_NAME>` deployed and data loaded into the space.

 
## ODBC bridge installation

- Login/Register on EasySoft official web site: {{%exurl "link""http://www.easysoft.com/cgi-bin/account/login.cgi"%}}
- Download the bridge from here {{%exurl "page""http://www.easysoft.com/products/data_access/odbc_odbc_bridge/index.html"%}}
- Proceed with the installation on your local machine.

### EasySoft license configuration

- Open `Easysoft Data Access License Manager` (you can find it in folder `<EASYSOFT-HOME>\Easysoft\License Manager`)
- Fill your Easysoft account information: `Name`, `E-Mail Address`, `Company`, `Telephone`
- Click on `Request License`  
- Choose the license type:

![](/attachment_files/sbp/easysoft/easysoft_data_license_manager_1.png)

- Choose `Easysoft ODBC-JDBC Gateway (Desktop)(2.5)`

![](/attachment_files/sbp/easysoft/easysoft_data_license_manager_2.png)

 
- Verify the information and press `On-line Request`

![](/attachment_files/sbp/easysoft/easysoft_data_license_manager_3.png)

## ODBC bridge configuration

- Open `ODBC Data Source Administrator`:

![](/attachment_files/sbp/easysoft/odbc_data_source_administrator_1_3.png)

- Press `Add...`
- Choose `Easysoft ODBC-JDBC Gateway`

![](/attachment_files/sbp/easysoft/odbc_data_source_administrator_2_1.png)
  
- `Finish`

## OJDBC-JDBC Gateway DSN Setup

- DSN: `easysoft-xap-odbc-jdbc`
- Driver Class: `com.gigaspaces.jdbc.Driver`
- Class Path: specify absolute path to `<XAL-HOME>\insightedge\lib\xap-jdbc.jar`
- URL: jdbc:xap:url=jini://*/*/<SPACE_NAME>?locators=127.0.0.1
- Check: `Strip Quote`

![](/attachment_files/sbp/easysoft/odbc_data_source_administrator_3_1.png)
 
Test your new connection.

![](/attachment_files/sbp/easysoft/odbc_data_source_administrator_4.png)

- Observe message `OK`, close `ODBC-JDBC Gateway Test` message window
- Save changes of DSN Setup by clicking `OK`

### Tableau configuration

- Open Tableau
- Choose `Other Database (ODBC)` as data source

![](/attachment_files/sbp/easysoft/tableau_1.png)
  
- Choose DSN: `easysoft-xap-odbc-jdbc`
- Press: `Connect`
- Fill Database: `<SPACE_NAME>`
- Press `Sign In`

![](/attachment_files/sbp/easysoft/tableau_2_1.png)

- Make schema search and choose your `<SPACE_NAME>`:

![](/attachment_files/sbp/easysoft/tableau_3.png)

- Make tables search:

![](/attachment_files/sbp/easysoft/tableau_4_1.png)

### Now you can use Tableau to query the XAP Datagrid:

![](/attachment_files/sbp/easysoft/tableau_5_1.png)
