README.md
=========
// This file is in markdown format.

The scheduler keeps track of scheduled and running Jobs by saving the information to a JobStore. The RAM Jobstore is useful for testing the application in an environment
where you don't have a database or there are multiple developers sharing the same database. The JDBC Jobstore is the recommended method to schedule jobs in production. 


1. How do I use the DB jobstore?
>  Run the SQL to create the scheduler tables BaseLibrary\lib\quartz\tables_db2_v95.sql
>  Edit the start_scheduler.sh and change the JOBSTORE environment variable to DB

1. How do I clear the DB jobstore?
>
>  DELETE FROM QRTZ_FIRED_TRIGGERS;
>  DELETE FROM QRTZ_PAUSED_TRIGGER_GRPS;
>  DELETE FROM QRTZ_SCHEDULER_STATE;
>  DELETE FROM QRTZ_LOCKS;
>  DELETE FROM QRTZ_SIMPLE_TRIGGERS;
>  DELETE FROM QRTZ_SIMPROP_TRIGGERS;
>  DELETE FROM QRTZ_CRON_TRIGGERS;
>  DELETE FROM QRTZ_TRIGGERS;
>  DELETE FROM QRTZ_JOB_DETAILS;
>  DELETE FROM QRTZ_CALENDARS;
>  DELETE FROM QRTZ_BLOB_TRIGGERS;   

1. I want to use a different database schema name or table prefix for my Quartz tables.
>  Unpack the quartzDB.properties file and add a new prefix entry to the file, then add the quartzDB.properties file back to the jar.
>  jar xvf  release_j5build_maj_min_rev_bld_scheduler.jar  quartzDB.properties
>  Edit quartzDB.properties 
>  Add line org.quartz.jobStore.tablePrefix="yourschema.yourprefix"
>  jar uvf release_j5build_maj_min_rev_bld_scheduler.jar  quartzDB.properties

   
1.  I see the error during the application launch
>   13-04-17 08:01:41  [INFO] XmlBeanDefinitionReader Loading XML bean definitions from class path resource [application.xml]
>   13-04-17 08:02:44  [WARN] SimpleSaxErrorHandler Ignored XML validation warning
>   org.xml.sax.SAXParseException; lineNumber: 8; columnNumber: 131; schema_reference.4: Failed to read schema document 'http://www.springframework.org/schema/beans/spring-beans-4.2.xsd', because 1) could not find the document; 2) the document could not be read; 3) the root element of the document is not <xsd:schema>.
>
>   This error occurs when the application is unable to retrieve xsd files for validation. The following woraround will correct the problem.
>   Edit start_scheduler.sh
>   Change the CLASSPATH to add spring-beans-4.2.5.RELEASE.jar
>   e.g CLASSPATH= spring-beans-4.2.5.RELEASE.jar:/appl/scheduler/release_j7build_maj_min_rev_bld_scheduler.jar
>
>   Delete the lines that check the classpath is file, (now its a list of files and will fail) e.g if [ ! -f $CLASSPATH ]
>
