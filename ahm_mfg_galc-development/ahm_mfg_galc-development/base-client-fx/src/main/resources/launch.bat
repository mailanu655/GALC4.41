:::
:: Description  : QA - Regional GALC Client Launch Script 
:: Author       : Suriya Sena
:: Date Created : 16-APR-2014
:::


SET VERSION=@BuildVersion@
:: ## CHANGE THE JDK AS NEEDED
SET JAVA_HOME=C:\Program Files\Java\jdk1.8.0_102
SET JAVA="%JAVA_HOME%\bin\java"

SET CACHEDIR=c:\ALC_CACHE

:: ## CHANGE THE QA ENVIRONMENT AS NEEDED
SET SERVER=qhma1was
::SET SERVER=qhma2was
::SET SERVER=qhma5was
::SET SERVER=dreg1was
::
:: ## CHANGE THE PORT AS NEEDED
SET PORT=8005

:: ## CHANGE THE TERMINAL AS NEEDED
SET TERMINAL=LCDefaultHostFX
::SET TERMINAL=NAQ_FLOAT_QICS

%JAVA% -noverify -cp galc-client-fx-%VERSION%.jar; com.honda.galc.client.ClientMainFx %CACHEDIR%  http://%SERVER%:%PORT%/BaseWeb/HttpServiceHandler %TERMINAL%
pause