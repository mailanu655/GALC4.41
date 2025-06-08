::::
:: Description  : QA - Regional GALC Client Launch Script 
:: Author       : Suriya Sena
:: Date Created : 08-AUG-2013
::::


:: ## CHANGE THE JDK AS NEEDED
SET JAVA_HOME=C:\Program Files\Java\jdk1.8.0
::SET JAVA_HOME=c:\Program Files\Java\jdk1.7.0_25
SET JAVAWS="%JAVA_HOME%\bin\javaws.exe"

:: ## CHANGE THE PORT AS NEEDED
SET PORT=8005

:: ## CHANGE THE QA ENVIRONMENT AS NEEDED
SET SERVER=qhma1was
::SET SERVER=qhma2was
::SET SERVER=qhma5was
::SET SERVER=dreg1was

:: ## CHANGE THE TERMINAL AS NEEDED
SET TERMINAL=1-HEADLIGHT


%JAVAWS%  http://%SERVER%:%PORT%/BaseWebStart/%TERMINAL%/LotControl/Production.jnlp
