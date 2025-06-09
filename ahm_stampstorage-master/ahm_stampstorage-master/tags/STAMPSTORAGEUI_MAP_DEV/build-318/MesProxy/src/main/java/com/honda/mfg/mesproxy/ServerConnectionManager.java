package com.honda.mfg.mesproxy;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

/**
 * User: Adam S. Kendell
 * Date: 6/16/11
 */
public class ServerConnectionManager implements Runnable {

    private static final String SOCKET_REJECTION_MESSAGE = "Your host is not on the access list for this server.";
    private static final String ACCEPT_ALL_CONNECTIONS = "0.0.0.0";
    private String name;

    private int queueSize;
    private int portNumber = -1;
    private int maxConnections = -1;
    private boolean bufferMessages;
    private String messageTerminator;

    private ServerSocket mainSocket;
    private Vector<Connection> serverConnections = new Vector<Connection>();
    private String[] allowedHosts;
    private Queue<String> inputQueue = new LinkedBlockingQueue<String>();
    private Queue<String> outputQueue = new LinkedBlockingQueue<String>();

    private boolean running;

    public String getMessageTerminator() {
        return messageTerminator;
    }

    public ServerConnectionManager(int portNumber, int maxConnections, String[] allowedHosts) {
        MesProxyProperties properties = MesProxyProperties.getInstance();

        this.portNumber = portNumber;
        this.maxConnections = maxConnections;
        this.allowedHosts = allowedHosts;
        this.queueSize = properties.getMessageQueueSize();
        this.messageTerminator = properties.getMessageTerminator();
        this.bufferMessages = properties.getBufferMessages();
        this.name = "ServerManager: " + portNumber + " - ";
        start();
    }

    public Integer getInputQueueSize() {
        return inputQueue.size();
    }

    public Integer getOutputQueueSize() {
        return outputQueue.size();
    }

    private void start() {
        try {
            SimpleLog.write(name + " starting server on port: " + portNumber);
            mainSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.execute(this);
    }

    public void run() {
        if (queueSize > 0) {
            inputQueue = new LinkedBlockingQueue<String>(queueSize);
            outputQueue = new LinkedBlockingQueue<String>(queueSize);
        }
        running = true;
        while (running) {
            if (serverConnections.size() <= maxConnections || maxConnections == -1) {
                try {
                    Socket newSocket = mainSocket.accept();
                    if ((isAllowedHost(newSocket))) {
                        SimpleLog.write(name + "Received connection request from: " + newSocket.getInetAddress().getHostAddress());

                        new ServerConnection(newSocket, this);
                        drainOutputQueue();
                    } else {
                        disconnectClient(newSocket);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public void stop() {
        SimpleLog.write(name + "disconnecting all " + serverConnections.size() + "clients.");
        running = false;
        disconnectAllClients();
        serverConnections = null;
        try {

            mainSocket.close();
        } catch (IOException e) {
            SimpleLog.write(e.toString());
        }
    }

    private boolean isAllowedHost(Socket socket) {
        InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();

        String ipAddress = address.toString().split("/")[1].split(":")[0];
        String hostName = address.getHostName().split("\\.")[0];

        for (String allowedHost : allowedHosts) {
            if (hostName.equals(allowedHost) || ipAddress.equals(allowedHost) ||
                    allowedHost.equals(ACCEPT_ALL_CONNECTIONS)) return true;
        }
        return false;
    }

    private void disconnectClient(Socket socket) {
        try {
            sendRejectionMessage(socket.getOutputStream());
            InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();
            socket.close();
            SimpleLog.write(name + "rejecting host: " + address.getHostName());
        } catch (IOException e) {
            SimpleLog.write(name + "error disconnecting client." + e.toString());
        }
    }

    private void disconnectAllClients() {
        for (Connection c : serverConnections) {
            c.stop();
        }
    }

    private void sendRejectionMessage(OutputStream outputStream) {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(SOCKET_REJECTION_MESSAGE);
            writer.flush();
        } catch (IOException e) {
            SimpleLog.write(name + "error sending rejection message.");
        }
    }

    public String pollInputQueue() {
        return inputQueue.poll();
    }

    public void writeToAllOutputStreams(String output) {
        if (serverConnections.size() <= 0 && bufferMessages) bufferOutput(output);
//        SimpleLog.write("writing output data: " + output);
        for (Connection c : serverConnections) {
            c.writeToOutputStream(output);
        }
    }

    private void drainOutputQueue() {
        while (outputQueue.size() > 0) {
            writeToAllOutputStreams(outputQueue.poll());
        }
    }

    private void bufferOutput(String output) {
        outputQueue.offer(output);
    }

    public boolean offerToInputQueue(String input) {
//        SimpleLog.write("ServerConnectionManager: received data, offering to inputQueue: " + input);
        return inputQueue.offer(input);
    }

    public void addConnection(Connection c) {
        SimpleLog.write(name + "adding connection " + c.getConnectionName() + " to pool.");
        serverConnections.add(c);
    }

    public void removeConnection(Connection c) {
        SimpleLog.write(name + "removing connection " + c.getConnectionName() + " from pool.");
        serverConnections.remove(c);
    }

    public void connectionError(Connection c, String msg) {
        SimpleLog.write(name + "connection error reported for: " + c.getConnectionName() + ". Message: " + msg);
    }
}
