SET APPLICATION_ROOT=.
SET APPLICATION_LIB=%APPLICATION_ROOT%\lib\*
SET APPLICATION_LOG_ROOT=%APPLICATION_ROOT%\log
SET JARLIB_ROOT=.\lib

java -cp DeviceSimulator.jar;%APPLICATION_LIB% com.honda.galc.device.simulator.client.view.DeviceSimulatorMain