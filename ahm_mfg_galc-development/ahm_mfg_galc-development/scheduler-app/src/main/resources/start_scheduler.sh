#!/bin/sh

##
# Sample shell script to run the scheduler
# Author Suriya Sena
# Date   3/30/2016
##

##
# NOTE
# The maximum number of concurrent jobs that can be started by the scheduler is defined in the quartz properties (org.quartz.threadPool.threadCount). Suppose 
# org.quartz.threadPool.threadCount=5, in the case where more than 5 tasks were scheduler to run at this time only the first 5 would be started, no error is reported.
#
# The server side executor pool size is configured in the Spring config DaoWebsphere.xml for the bean OifTaskExecutor
# maxPoolSize determines the maximum number of jobs that can run on the server
# queueCapacity determines the how many tasks to queue if all threads in the pool are used
#:
# Installation Checklist
#  1) Verify you have a JRE 7 or above installed, and then change the JAVA environment var to refer to it.
#  2) Change the CLASSPATH to refer to the correct release build jar.  You should have only unpacked the start_scheduler.sh and README.md
#  3) Create a log directory for the scheduler log and then change the LOGDIR to refer to it.
#  4) Change the GALC_SERVER url to refer to a GALC instance that you want the scheduler to scheduler tasks on.
#  5) Create the Quartz tables in the GALC database 
#  6) Change the JDBC_URL to refer to the GALC database with the Quartz tables.
#  
##


##
# Path to Java 7 or above java executable 
# 
##
JAVA=/usr/bin/java

##
# Classpath to the GALC scheduler application 
##
HOME=.
CLASSPATH=$HOME/lib/spring-jdbc-4.2.5.RELEASE.jar:$HOME/lib/aopalliance-1.0.jar:$HOME/lib/commons-dbcp-1.2.2.jar:$HOME/lib/commons-logging-1.1.1.jar:$HOME/lib/commons-pool-1.5.4.jar:$HOME/lib/db2jcc4.jar:$HOME/lib/jetty-all-9.2.10.v20150310.jar:$HOME/lib/log4j-1.2.15.jar:$HOME/lib/quartz-2.2.2.jar:$HOME/lib/servlet-api-3.1.jar:$HOME/lib/slf4j-api-1.6.1.jar:$HOME/lib/slf4j-log4j12-1.6.1.jar:$HOME/lib/spring-aop-4.2.5.RELEASE.jar:$HOME/lib/spring-beans-4.2.5.RELEASE.jar:$HOME/lib/spring-context-4.2.5.RELEASE.jar:$HOME/lib/spring-context-support-4.2.5.RELEASE.jar:$HOME/lib/spring-core-4.2.5.RELEASE.jar:$HOME/lib/spring-expression-4.2.5.RELEASE.jar:$HOME/lib/spring-tx-4.2.5.RELEASE.jar:$HOME/release_j5build_maj_min_rev_bld_scheduler.jar

##
# Directory the scheduler log file. A new file is created for each day of the week, files rotate after 7 days.
##
LOGDIR=$HOME/log


##
#  The hostname of the machine running the scheduler, the name must be reachable from the GALC server. 
##
HOSTNAME=changeme


##
#  The TCP/IP Listener port for the scheduler web console 
##
PORT=8110


##
#  HTTP client timeout in seconds. When the job trigger fires, i.e. when its time to run a job the application will open a http client connection to the 
#  GALC application server and send message to start the job asynchronously. The connection + job initiation must complete within this timeout value. 
##

TIMEOUT_SECS=30


##
# Scheduler synchronization interval in minutes. The frequency at which the Quartz scheduler should refresh its job table from the GALC jobs table (GAL489TBX)
##

SYNC_INTERVAL=15

##
# GALC Server URL specifies the url of the server that the OIF task will run on. The scheduler will append addition parameters such as the task name to the URL 
##
GALC_SERVER=http://qhma1was:8005/RestWeb/AsyncTaskExecutorService/execute

##
# GALC Database connection parameters.
# 1) The Scheduler maintains schedule information in database QRTZ_* tables,the create table DDL is in quartz-2.2.2-distribution.tar.gz and 
#    BaseLibrary\lib\quartz\tables_db2_v95.sql
# 2) Secondly it reads scheduling and task information from the GAL489TBX tables
##
JOBSTORE=DB
JDBC_URL=jdbc:db2://dhma1db1:60012/GAL1DBQA
DBUSERNAME=galadm
DBPASSWORD=galadm





##
# MAIN
##

if [ ! -d $LOGDIR ]
then
 echo "Scheduler : LOGDIR does not exist !"
 exit 1
fi


if [ ! -x $JAVA ]
then
 echo "Scheduler : JAVA must refer to a Java 7 or greater JRE !!!"
 exit 1
fi

jver=`$JAVA -version 2>&1|grep version|awk '{print $3}'|cut -f2 -d.`
if [[ $jver -lt "6" ]]
then
    echo "Scheduler : JAVA must refer to a Java 7 or greater JRE ...yours is " $jver 
    exit 1
fi


schedpid=`ps ax|grep scheduler.jar|grep -v grep|awk '{print $1}'`
if ps ax | grep -v grep | grep scheduler.jar > /dev/null
   then
       echo "scheduler is running killing process $schedpid ...."
       kill -9 $schedpid
       sleep 5
fi




nohup $JAVA -server -Dlog.dir=$LOGDIR \
              -Dsocket.listener.hostname=$HOSTNAME \
              -Dsocket.listener.port=$PORT \
              -Dgalc.server.url=$GALC_SERVER \
              -Dhttpclient.timeout=$TIMEOUT_SECS \
              -Dscheduler.sync.interval=$SYNC_INTERVAL \
              -Dscheduler.jobstore=$JOBSTORE \
              -Djdbc.datasource.url=$JDBC_URL \
              -Djdbc.datasource.username=$DBUSERNAME \
              -Djdbc.datasource.password=$DBPASSWORD \
              -cp $CLASSPATH com.honda.scheduler.AppLauncher 2>&1 >  $LOGDIR/stdout.txt &
