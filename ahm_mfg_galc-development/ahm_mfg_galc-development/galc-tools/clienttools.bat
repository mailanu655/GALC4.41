
SET SERVER_URL=http://10.167.200.200/BaseWeb/HttpServiceHandler
SET APPLICATION_ROOT=.
SET APPLICATION_LOG_ROOT=%APPLICATION_ROOT%\log
SET JARLIB_ROOT=.

SET CLASSPATH=
SET CLASSPATH=clienttools.jar;.;%JARLIB_ROOT%\spring-2.5.6.jar;%JARLIB_ROOT%\commons-lang-2.4.jar;%JARLIB_ROOT%\commons-logging-1.1.1.jar;%JARLIB_ROOT%\ehcache-core-1.7.0.jar;%JARLIB_ROOT%\log4j-1.2.15.jar;%JARLIB_ROOT%\eventbus-1.3.jar;%JARLIB_ROOT%\jsr173_api.jar;%JARLIB_ROOT%\sjsxp.jar;%JARLIB_ROOT%\xpp3_min-1.1.4c.jar;%JARLIB_ROOT%\xstream-1.3.1.jar
SET CLASSPATH=clienttools.jar;.;%JARLIB_ROOT%\*;
java com.honda.galc.tools.client.ClientToolsWindow