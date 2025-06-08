rem SET GALC_JDK=C:/HcmGalcClient/AppClient_1.4/java/jre
rem SET PATH=C:/HcmGalcClient/AppClient_1.4/java/jre\bin;%PATH%

SET DATE=April 14 2008
SET VERSION=HCM_Release_20080414_2
SET SERVER_URL=http://localhost/BaseWeb/HttpServiceHandler
REM SET SERVER_URL=http://207.130.147.18/BaseWeb/HttpServiceHandler
REM SET SERVER_URL=http://192.168.7.209/BaseWeb/HttpServiceHandler
SET APPLICATION_ROOT=.
SET APPLICATION_LOG_ROOT=%APPLICATION_ROOT%\log
rem SET WAS_LIB_ROOT=C:\HcmGalcClient\AppClient_1.4\lib
SET JARLIB_ROOT=.\lib

SET CLASSPATH=
SET CLASSPATH=ClientMain.jar;.;%JARLIB_ROOT%\spring-2.5.6.jar;%JARLIB_ROOT%\commons-lang-2.4.jar;%JARLIB_ROOT%\commons-logging-1.1.1.jar;%JARLIB_ROOT%\ehcache-core-1.7.0.jar;%JARLIB_ROOT%\log4j-1.2.15.jar;%JARLIB_ROOT%\eventbus-1.3.jar;%JARLIB_ROOT%\jsr173_api.jar;%JARLIB_ROOT%\sjsxp.jar;%JARLIB_ROOT%\xpp3_min-1.1.4c.jar;%JARLIB_ROOT%\xstream-1.3.1.jar
SET CLASSPATH=ClientMain.jar;.;%JARLIB_ROOT%\*;
REM javaw com.honda.global.galc.client.common.ClientMainClass %APPLICATION_ROOT% %SERVER_URL% %3 %4 2>%APPLICATION_ROOT%\logs\stdout.txt
java com.honda.galc.client.ClientMain  %APPLICATION_LOG_ROOT% %SERVER_URL% AE0EN14501 > %APPLICATION_LOG_ROOT%\stdout.txt
