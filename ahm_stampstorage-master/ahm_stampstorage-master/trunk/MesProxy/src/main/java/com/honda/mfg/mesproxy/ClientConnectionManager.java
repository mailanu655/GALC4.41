package com.honda.mfg.mesproxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.IllegalBlockingModeException;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

/**
 * User: Adam S. Kendell
 * Date: 6/16/11
 */
public class ClientConnectionManager implements Runnable {
    static final int MIN_PORT_NUMBER = 1;
    static final int MAX_PORT_NUMBER = 65535;
    private String name;

    private int queueSize;
    private int portNumber = -1;
    private int socketConnectTimeout = 500;
    private int retryConnectionRate = 5000;
    private String hostName;
    private boolean bufferMessages;

    private Socket socket;
    private Queue<String> inputQueue = new LinkedBlockingQueue<String>();
    private Queue<String> outputQueue = new LinkedBlockingQueue<String>();
    private Connection clientConnection;

    public Integer getInputQueueSize(){
        return inputQueue.size();
    }

    public Integer getOutputQueueSize(){
        return outputQueue.size();
    }

    public ClientConnectionManager(int portNumber, String hostName, int socketConnectTimeout,
                                   int retryConnectionRate, boolean bufferMessages, int queueSize) {

        if (portNumber < MIN_PORT_NUMBER || portNumber > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid port number: " + portNumber +
                    " (valid range: " + MIN_PORT_NUMBER + " - " + MAX_PORT_NUMBER + ")");
        }

        this.portNumber = portNumber;
        this.hostName = hostName;
        this.socketConnectTimeout = socketConnectTimeout;
        this.retryConnectionRate = retryConnectionRate;
        this.bufferMessages = bufferMessages;
        this.queueSize = queueSize;
        this.name = "ClientConnectionManager: " + portNumber + " - ";
        start();
    }

    private void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.execute(this);
    }

    public void run() {
        if (queueSize > 0) {
            inputQueue = new LinkedBlockingQueue<String>(queueSize);
            outputQueue = new LinkedBlockingQueue<String>(queueSize);
        }
        connectIfNotConnected();
    }

    public void stop() {
        clientConnection.stop();
        clientConnection = null;
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
        }
    }

    private void connectIfNotConnected() {
        while (socket == null) {
            try {
                SimpleLog.write(name + "attempting to connect. Creating socket: " + hostName + ":" + portNumber);
                socket = createSocket();
                clientConnection = new ClientConnection(socket, this);
                while (!outputQueue.isEmpty() && !socket.isConnected()) {
                    writeToAllOutputStreams(outputQueue.poll());
                }
                break;
            } catch (UnknownHostException e) {
                pause();
            } catch (IllegalArgumentException e) {
                pause();
            } catch (SocketTimeoutException e) {
                pause();
            } catch (IllegalBlockingModeException e) {
                pause();
            } catch (SecurityException e) {
                pause();
            } catch (IOException e) {
                pause();
            }
        }
    }

    private void pause() {
        try {
            //LOG.write(name + "Retrying Connection in " + retryConnectionRate + " ms.");
            Thread.sleep(retryConnectionRate);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    private Socket createSocket()
            throws IOException, SecurityException,
            IllegalArgumentException, IllegalBlockingModeException {

        Socket socket;
        InetSocketAddress inetSocketAddress = getAddress();
        socket = getNewSocket();
        bindSocket(socket);
        connectSocket(socket, inetSocketAddress);

        return socket;
    }

    private void connectSocket(Socket socket, InetSocketAddress address)
            throws IllegalArgumentException, IllegalBlockingModeException,
            IOException {
        socket.connect(address, socketConnectTimeout);
    }

    private void bindSocket(Socket socket)
            throws IOException, IllegalArgumentException {
        socket.bind(null);
    }

    private Socket getNewSocket()
            throws IllegalArgumentException, SecurityException {
        return constructNewSocket();
    }

    private InetSocketAddress getAddress()
            throws IllegalArgumentException, SecurityException {
        return constructNewAddress();
    }

    private InetSocketAddress constructNewAddress() {
        return new InetSocketAddress(hostName, portNumber);
    }

    private Socket constructNewSocket() {
        return new Socket();
    }

    public String pollInputQueue() {
        return inputQueue.poll();
    }

    public boolean writeToAllOutputStreams(String output) {
        connectIfNotConnected();
        try {
            clientConnection.writeToOutputStream(output);
            return true;
        } catch (NullPointerException e) {
            //LOG.write(name + "buffering message to OutputQueue.");
            if (bufferMessages) bufferToOutputQueue(output);
            return true;
        }
    }

    private void bufferToOutputQueue(String output) {
        outputQueue.offer(output);
    }

    public boolean offerToInputQueue(String input) {
        boolean retVal = false;
        retVal = inputQueue.offer(input);
        return retVal;
    }

    public void addConnection(Connection c) {
        SimpleLog.write(name + "adding connection " + c.getConnectionName() + " to pool.");
        clientConnection = c;
    }

    public void removeConnection(Connection c) {
        SimpleLog.write(name + "removing connection " + c.getConnectionName() + " from pool.");
        clientConnection = null;
        socket = null;
        connectIfNotConnected();
    }

    public void connectionError(Connection c, String msg) {
        SimpleLog.write(name + "connection error reported for: " + c.getConnectionName() + ". Message: " + msg);
        clientConnection = null;
        connectIfNotConnected();
    }
}
