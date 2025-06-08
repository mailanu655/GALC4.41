
SITE=your_site_and_env_name_go_here
JAVA=/usr/java6/bin/java

kill -9 `ps -efla  | grep ${SITE} | awk '{print $4}'`
${JAVA} -DPropertyServices.InitialPreferredSuffix=${SITE} -jar /appl/logserver/LogServer.jar &
