#!/bin/ksh

export JAVA_HOME=/usr/java6_64

if [ -z "$1" ]
  then
    echo "No argument supplied: Usage:\n \
	run_stamp_service.sh <application_id>\n \
	eg. run_stamp_service.sh stampstorageservice_map_dev\n \
	eg. run_stamp_service.sh stampstorageservice_map_qa"
    exit
fi

mkdir -p ./logs
$JAVA_HOME/jre/bin/java -Dfile.encoding=UTF-8 -Dapp.id=$1  -classpath ./lib/StampStorageService-1.0.jar:./lib/ConnectionDevice-1.0.jar:./lib/StampStorageManager-1.0.jar:./lib/StampStorageDomain-1.0.jar:./lib/antlr-2.7.6.jar:./lib/aopalliance-1.0.jar:./lib/aspectjrt-1.6.11.jar:./lib/aspectjweaver-1.6.11.jar:./lib/cglib-nodep-2.2.jar:./lib/commons-beanutils-1.8.0.jar:./lib/commons-codec-1.4.jar:./lib/commons-collections-3.1.jar:./lib/commons-dbcp-1.3.jar:./lib/commons-digester-2.0.jar:./lib/commons-fileupload-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging-1.0.4.jar:./lib/commons-pool-1.5.4.jar:./lib/db2jcc4-db2jcc4.jar:./lib/db2jcc_license_cu-db2jcc_license_cu.jar:./lib/derby-10.8.1.2.jar:./lib/derbyclient-10.8.1.2.jar:./lib/dom4j-1.6.1.jar:./lib/eventbus-1.4.jar:./lib/flexjson-2.1.jar:./lib/google-collections-1.0.jar:./lib/hamcrest-core-1.1.jar:./lib/hibernate-commons-annotations-3.2.0.Final.jar:./lib/hibernate-core-3.6.3.Final.jar:./lib/hibernate-entitymanager-3.6.3.Final.jar:./lib/hibernate-jpa-2.0-api-1.0.0.Final.jar:./lib/hibernate-validator-4.1.0.Final.jar:./lib/javassist-3.12.0.GA.jar:./lib/jcl-over-slf4j-1.6.1.jar:./lib/jettison-1.2.jar:./lib/joda-time-1.6.jar:./lib/jstl-1.2.jar:./lib/jta-1.1.jar:./lib/junit-4.13.2.jar:./lib/log4j-1.2.16.jar:./lib/mail-1.4.jar:./lib/mockito-all-5.3.0.jar:./lib/slf4j-api-1.6.1.jar:./lib/slf4j-log4j12-1.6.1.jar:./lib/spring-aop-3.0.5.RELEASE.jar:./lib/spring-asm-3.0.5.RELEASE.jar:./lib/spring-aspects-3.0.5.RELEASE.jar:./lib/spring-beans-3.0.5.RELEASE.jar:./lib/spring-context-3.0.5.RELEASE.jar:./lib/spring-context-support-3.0.5.RELEASE.jar:./lib/spring-core-3.0.5.RELEASE.jar:./lib/spring-expression-3.0.5.RELEASE.jar:./lib/spring-jdbc-3.0.5.RELEASE.jar:./lib/spring-js-resources-2.2.1.RELEASE.jar:./lib/spring-ldap-core-1.3.0.RELEASE.jar:./lib/spring-ldap-core-tiger-1.3.0.RELEASE.jar:./lib/spring-orm-3.0.5.RELEASE.jar:./lib/spring-security-acl-3.0.5.RELEASE.jar:./lib/spring-security-config-3.0.5.RELEASE.jar:./lib/spring-security-core-3.0.5.RELEASE.jar:./lib/spring-security-ldap-3.0.1.RELEASE.jar:./lib/spring-security-taglibs-3.0.5.RELEASE.jar:./lib/spring-security-web-3.0.5.RELEASE.jar:./lib/spring-tx-3.0.5.RELEASE.jar:./lib/spring-web-3.0.5.RELEASE.jar:./lib/spring-webmvc-3.0.5.RELEASE.jar:./lib/stax-api-1.0.1.jar:./lib/tiles-api-2.2.1.jar:./lib/tiles-core-2.2.1.jar:./lib/tiles-jsp-2.2.1.jar:./lib/tiles-servlet-2.2.1.jar:./lib/tiles-template-2.2.1.jar:./lib/validation-api-1.0.0.GA.jar:./lib/xpp3_min-1.1.4c.jar:./lib/xstream-1.3.1.jar com.honda.mfg.stamp.conveyor.service.StorageServiceMain start&
ps -f -u ohcvadev,ohcvaqa,ohcvaprd -opid,args|grep $1.*StampStorageService|grep -v grep|awk '{print $1}' | xargs -I'{pid}' nohup -p '{pid}'
