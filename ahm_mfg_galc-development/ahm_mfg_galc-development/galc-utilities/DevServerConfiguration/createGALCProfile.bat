::::
:: Description  : Create and Configure Legacy GALC profile
:: Author       : Suriya Sena
:: Date Created : 4-2-2014
:: 
:: Author       : John Martinek
:: Updated      : 8-22-2015
:: Description  : Add command line processing feature with command line argument validation
::                Add custom registry setup feature
::                Add feature to launch the server configuration script
::                  
::
:: createConfigureGALCProfile.bat d:/sdp9 RGALCTrunk D:/Workspaces/JJM_HMA_WAS7_Migration "C:\Program Files (x86)\IBM\SQLLIB" GAL1DBQA dhma1db1 60012
::
::
::   If only the RAD root and Profile Name root is specified, then the server
::   configuration script will not be processed.
::
::   Arguments:
::      RAD root - Base install of rational application developer
::      Profile Name root - Used to build profile and related names
::      Workspace root - Used to locate GALC property files. If the server configuration
::                       script is not to be invoked, specify DEFAULT or FILE to control
::                       port settings.
::      DB2 root - base directory of DB2 install
::      db name - Name of GALC database to connect to
::      db server name - Name of GALC database server
::      db port - port of GALC database server
::      [DEFAULT | FILE ] - Optional if, FILE is specified, GALCPorts.txt file will be used
::          
::::
echo OFF

:: Check for required parameters
set SDP_ROOT=%1
if "%SDP_ROOT%"=="" goto displayhelp
set WAS_HOME=%SDP_ROOT%/runtimes/base_v7

echo "WAS_HOME=%WAS_HOME%"

set NAME_ROOT=%2
if "%NAME_ROOT%"=="" goto displayhelp

set PROFILE_NAME=%NAME_ROOT%
set CELL_NAME=%NAME_ROOT%Cell
set NODE_NAME=%NAME_ROOT%Node
set SERVER_NAME=%NAME_ROOT%Server
set TEMPLATE_NAME=%WAS_HOME%/profileTemplates/default


:: Check for optional parameters
set WORKSPACE_ROOT=%3
set DB2_ROOT=%4
set DB_NAME=%5
set DB_SERVER=%6
set DB_PORT=%7
set SERVER_PORTS=%8

:: Determine port option to use
if "%SERVER_PORTS%"=="" (
set SERVER_PORTS=DEFAULT
)
set PORTSCMD=
if "%WORKSPACE_ROOT%"=="FILE" (
set PORTSCMD=-portsFile GALCPorts.txt
set WORKSPACE_ROOT=
)
if "%SERVER_PORTS%"=="FILE" (
set PORTSCMD=-portsFile GALCPorts.txt
)
if "%SERVER_PORTS%"=="file" (
set PORTSCMD=-portsFile GALCPorts.txt
)

:: Now call manageprofiles
echo Calling manageprofiles
echo ON
call %WAS_HOME%/bin/manageprofiles -validateAndUpdateRegistry
call %WAS_HOME%/bin/manageprofiles -create -templatePath %TEMPLATE_NAME% -profileName %PROFILE_NAME% -cellName %CELL_NAME% -nodeName %NODE_NAME%  -adminUserName GALCAdmin1 -adminPassword g2lc -serverName %SERVER_NAME% -winserviceCheck false %PORTSCMD% -enableAdminSecurity true -isDeveloperServer -personalCertValidityPeriod 15 -signingCertValidityPeriod 15
if "%ERRORLEVEL%"=="0" goto registrysetup 
echo OFF
echo Error creating profile %PROFILE_NAME%
goto endscript

:registrysetup

:: Now Handle GALC Custom Registry files
set targetfile="%WAS_HOME%/profiles/%PROFILE_NAME%/properties/GALCRegistry.properties"
echo Building %targetfile%
set targetfile2=%targetfile:/=\%
echo copying to %targetfile2%
copy config\HMA_GALCRegistry.properties %targetfile2%
echo DBURL=jdbc:db2://%DB_SERVER%:%DB_PORT%/%DB_NAME% >> %WAS_HOME%/profiles/%PROFILE_NAME%/properties/GALCRegistry.properties

set targetdir="%WAS_HOME%/lib/ext"
set targetdir2=%targetdir:/=\%

if exist %WAS_HOME%/lib/ext/GALCRegistry.jar (
   echo GALCRegistry.jar is already in place
) else (
   echo Installing GALCRegistry.jar to %targetdir2%
   copy config\GALCRegistry.jar %targetdir2%
)

:: Check to see if we call wsadmin configuration script
::                        
if "%WORKSPACE_ROOT%"=="" goto displayports

:: Start server in order to run wsadmin script
echo "Starting Server %SERVER_NAME%"
call %WAS_HOME%/profiles/%PROFILE_NAME%/bin/startServer.bat %SERVER_NAME%

:: Now do server and resource configuration
echo Calling GALCTestNodeConfig.py to perform server and resource configuration
call %WAS_HOME%/profiles/%PROFILE_NAME%/bin/wsadmin.bat -lang jython -user GALCAdmin1 -password g2lc -f GALCTestnodeConfig.py %WORKSPACE_ROOT% %DB2_ROOT% %DB_NAME% %DB_SERVER% %DB_PORT%

if "%ERRORLEVEL%"=="0" goto restartserver   
echo " "

echo Stopping server after wsadmin script error
call %WAS_HOME%/profiles/%PROFILE_NAME%/bin/stopServer.bat %SERVER_NAME% -user GALCAdmin1 -password g2lc
goto endscript
 
:restartserver
echo Restarting Server %SERVER_NAME%
call %WAS_HOME%/profiles/%PROFILE_NAME%/bin/stopServer.bat %SERVER_NAME% -user GALCAdmin1 -password g2lc
call %WAS_HOME%/profiles/%PROFILE_NAME%/bin/startServer.bat %SERVER_NAME%

:displayports

echo " "
echo Server port information...
set targetfile=%WAS_HOME%/profiles/%PROFILE_NAME%/logs/AboutThisProfile.txt
set targetfile2=%targetfile:/=\%
if exist %targetfile2% (
 type %targetfile2%
) else (
 type GALCPorts.txt
)

echo " "
echo Script startup is complete


goto endscript
               


:displayhelp
echo "This script requires the following command line parameters:"
echo "   SDP_ROOT  (e.g., d:/SDP9)"
echo "   NAME_ROOT (e.g, GALCLegacy - used to build profile, cell, node, and server names"
echo "   [WORKSPACE_ROOT DB2_ROOT DB_NAME DB_SERVER DB_PORT] Optional parms"
echo " "


:endscript
