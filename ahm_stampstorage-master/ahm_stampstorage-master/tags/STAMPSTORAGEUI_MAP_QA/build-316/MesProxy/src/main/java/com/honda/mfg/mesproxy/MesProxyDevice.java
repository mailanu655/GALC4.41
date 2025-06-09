package com.honda.mfg.mesproxy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * User: Adam S. Kendell
 * Date: 6/7/11
 */
public class MesProxyDevice implements Runnable {

    private static final String NAME = "MesProxyDevice - ";
    private Integer messagesPerSecond;
    private ServerConnectionManager serverConnectionManager;
    private ServerConnectionManager transportConnectionManager;
    private ClientConnectionManager listenerConnectionManager;

    public MesProxyDevice() {

        if (MesProxyProperties.getPauseBetweenProxyOperations() == 0) {
            messagesPerSecond = 999;
        } else {
            messagesPerSecond = (1000 / MesProxyProperties.getPauseBetweenProxyOperations());
        }

        SimpleLog.write(NAME + "creating new ServerConnectionManager on port: " + MesProxyProperties.getServerPort() +
                " maximum connections: " + MesProxyProperties.getMaximumAllowedServerConnections());
        serverConnectionManager = new ServerConnectionManager(MesProxyProperties.getServerPort(),
                MesProxyProperties.getMaximumAllowedServerConnections(),
                MesProxyProperties.getAllowedTransportHostList());

        SimpleLog.write(NAME + "creating new TransportConnectionManager on port: " + MesProxyProperties.getTransportPort() +
                " maximum connections: " + MesProxyProperties.getMaximumAllowedTransportConnections());
        transportConnectionManager = new ServerConnectionManager(MesProxyProperties.getTransportPort(),
                MesProxyProperties.getMaximumAllowedTransportConnections(),
                MesProxyProperties.getAllowedTransportHostList());

        SimpleLog.write(NAME + "creating new ListenerConnectionManager on port: " + MesProxyProperties.getListenerPort() +
                " connect timeout: " + MesProxyProperties.getListenerConnectTimeout() +
                " pause between retries: " + MesProxyProperties.getListenerPauseBeforeConnectRetry());
        listenerConnectionManager = new ClientConnectionManager(
                MesProxyProperties.getListenerPort(),
                MesProxyProperties.getLocalHostName(),
                MesProxyProperties.getListenerConnectTimeout(),
                MesProxyProperties.getListenerPauseBeforeConnectRetry(),
                MesProxyProperties.getBufferMessages(),
                MesProxyProperties.getMessageQueueSize());

        SimpleLog.write(NAME + "starting new MesProxyDevice. Polling interval set at: " +
                MesProxyProperties.getPauseBetweenProxyOperations() + " ms" + " allows " + messagesPerSecond + " messages per second per channel.");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.execute(this);

    }

    public void run() {

        while (true) {

            try{
            String input = serverConnectionManager.pollInputQueue();
            if (input != null) {
                Integer serverInputQueueSize = serverConnectionManager.getInputQueueSize();
                Integer listenerOutputQueueSize = listenerConnectionManager.getOutputQueueSize();

                if (serverInputQueueSize > messagesPerSecond || listenerOutputQueueSize > messagesPerSecond) {
                    SimpleLog.write(NAME + "listener side queuing detected. Indicates a build-up of application messages.\n\t\t\t\t\t" +
                            "application input queue size: " + serverInputQueueSize + "\n\t\t\t\t\t" +
                            "listener output queue size: " + listenerOutputQueueSize + "\n\t\t\t\t\t" +
                            "estimated time to clear: " + (serverInputQueueSize / messagesPerSecond) * 1000 + "ms");
                }
                listenerConnectionManager.writeToAllOutputStreams(input);
            }

            String output = transportConnectionManager.pollInputQueue();
            if (output != null) {
                Integer transportInputQueueSize = transportConnectionManager.getInputQueueSize();
                Integer serverOutputQueueSize = serverConnectionManager.getOutputQueueSize();

                if (transportInputQueueSize > messagesPerSecond || serverOutputQueueSize > messagesPerSecond) {
                    SimpleLog.write(NAME + "transport side queuing detected. Indicates a build-up of status messages.\n\t\t\t\t\t" +
                            "QPC input queue size: " + transportConnectionManager.getInputQueueSize() + "\n\t\t\t\t\t" +
                            "application output queue size: " + serverConnectionManager.getOutputQueueSize()+ "\n\t\t\t\t\t" +
                            "estimated time to clear: " + (transportInputQueueSize / messagesPerSecond) * 1000 + "ms");
                }
                serverConnectionManager.writeToAllOutputStreams(output);
            }
            pause();    //allows us to dial back CPU usage for use on embedded devices.
            }catch (Exception e){
                SimpleLog.write(e);
            }
        }
    }

    public void stop() {
        SimpleLog.write(NAME + "stopping all managers for this Proxy Device.");
        serverConnectionManager.stop();
        transportConnectionManager.stop();
        listenerConnectionManager.stop();

        serverConnectionManager = null;
        transportConnectionManager = null;
        listenerConnectionManager = null;

    }

    private void pause() {
        try {
            Thread.sleep(MesProxyProperties.getPauseBetweenProxyOperations());
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
