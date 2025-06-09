package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.MessageRequestProcessor;
import com.honda.mfg.device.MessageRequestProcessorImpl;
import com.honda.mfg.device.mesmodule.messages.MesRequest;
import com.honda.mfg.device.messages.MessageRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * User: vcc30690
 * Date: 4/19/11
 */
class TestableMesModuleServer implements Runnable {
    final Logger LOG = LoggerFactory.getLogger(TestableMesModuleServer.class);

    private int port;
    private long connectWaitTimeout = 10000L; // Wait 0.5 seconds
    private ServerSocket serverSocket;
    private volatile boolean started;
    private boolean connected;
    private boolean connectedAndListening;
    Socket clientSocket;

   public TestableMesModuleServer(int port) throws IOException {
        this.started = false;
        this.connected = false;
        this.connectedAndListening = false;
        this.port = port;
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);
        scheduler.execute(this);
        LOG.debug("Waiting.....  TIME: " + System.currentTimeMillis());
        while (!started) {
        }
        LOG.debug("Done..........TIME: " + System.currentTimeMillis());
    }

    public boolean hasAcceptedClientConnection() {
        return connectedAndListening;
    }

    private String getMessage(Reader reader) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                int ch = reader.read();
                LOG.trace("CHAR:int  " + ch);
                sb.append((char) ch);
                LOG.trace("Current SB: " + sb);
                if (isMessageComplete(sb.toString())) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return sb.toString();
    }

    private String getResponseMessage() throws IOException {
        MesResponseReader mesReader = null;
        LOG.info("##########  Getting reader from socket inputStream");
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        mesReader = new MesResponseReader(reader);
        LOG.info("########## About to read message from client...");
        this.connectedAndListening = true;
        return mesReader.getResponse();
    }

    private void writeMessage(String msg) throws IOException {
        LOG.info("##########  Getting writer from socket outputStream");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        MessageRequestProcessor requestProcessor = new MessageRequestProcessorImpl(writer);
        MessageRequest request = new MesRequest(msg);
        String stuff = request.getMessageRequest();
        LOG.trace("#########  About to write: " + stuff);
        requestProcessor.sendRequest(request);
    }

    public void run() {
        try {
            LOG.info("Starting server on port:  " + port);
            serverSocket = new ServerSocket(port);
            LOG.info("Getting client connection.");
            started = true;
            clientSocket = serverSocket.accept();
            connected = true;
            LOG.info("Accepted client socket connection.");

            // Time to simply echo back what is being sent to the server.
            while (connected) {
                String msg = getResponseMessage();
                LOG.trace("Read message from client:  MSG: " + msg);
                writeMessage(msg);
                LOG.trace("Written message to client:  MSG: " + msg);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            connected = false;
        }
    }

    public void disconnect() {
        try {
            clientSocket.close();
        } catch (Throwable e) {
        }
        try {
            serverSocket.close();
        } catch (Throwable e) {
        }
        connected = false;
    }

    private boolean isMessageComplete(String message) {

        int openCurly = 0;
        int closedCurly = 0;
        for (char c : message.toCharArray()) {
            if (c == '{') {
                openCurly++;
            }
            if (c == '}') {
                closedCurly++;
            }
        }
        return (openCurly != 0 || closedCurly != 0) && openCurly == closedCurly;
    }
   
}
