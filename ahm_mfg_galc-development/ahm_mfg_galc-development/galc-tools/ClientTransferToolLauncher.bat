SET APPLICATION_ROOT=.
SET APPLICATION_LIB=%APPLICATION_ROOT%\lib\*
SET APPLICATION_LOG_ROOT=%APPLICATION_ROOT%\log
SET JARLIB_ROOT=.\lib

java -cp ClientTransferTool.jar;%APPLICATION_LIB% com.honda.galc.tools.client.ClientToolsWindow ServerList.properties