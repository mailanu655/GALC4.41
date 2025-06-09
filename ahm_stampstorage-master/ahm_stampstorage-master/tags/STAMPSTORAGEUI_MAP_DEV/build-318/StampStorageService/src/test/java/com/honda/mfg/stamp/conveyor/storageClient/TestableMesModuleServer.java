package com.honda.mfg.stamp.conveyor.storageClient;

import com.honda.mfg.connection.MessageRequestProcessor;
import com.honda.mfg.connection.MessageRequestProcessorImpl;
import com.honda.mfg.connection.messages.MessageRequest;
import com.honda.mfg.connection.processor.ConnectionResponseReader;
import com.honda.mfg.connection.processor.messages.ConnectionRequest;

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
    private BufferedReader stopReader = null;
    private BufferedReader sequenceReader = null;
    BufferedWriter writer = null;

    Socket clientSocket;
    //    String fileName;
//    String stopFileName;
    String PING_RESPONSE = "{\"GeneralMessage\":{\"messageType\":\"PING\"}}" + "{END_OF_MESSAGE}";


    public TestableMesModuleServer(int port) throws IOException {
        this(port, null);
    }

    public TestableMesModuleServer(int port, String fileName) throws IOException {
        this(port, fileName, null);
    }

    public TestableMesModuleServer(int port, String fileName, String stopFileName) throws IOException {
        this.started = false;
        this.connected = false;
        this.port = port;
        initializeReaders(fileName, stopFileName);
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);
        scheduler.execute(this);
    }

    private void initializeReaders(String sequenceFilename, String stopFilename) throws FileNotFoundException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(sequenceFilename);
        this.sequenceReader = new BufferedReader(new InputStreamReader(is));
        InputStream stopIs = this.getClass().getClassLoader().getResourceAsStream(stopFilename);
        this.stopReader = new BufferedReader(new InputStreamReader(stopIs));
    }


    public boolean hasAcceptedClientConnection() {
        return connectedAndListening;
    }

    private String getResponseMessage() throws IOException {
        ConnectionResponseReader mesReader = null;
        LOG.info("##########  Getting reader from socket inputStream");
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        mesReader = new ConnectionResponseReader(reader);
        LOG.info("########## About to read message from client...");
        this.connectedAndListening = true;
        return mesReader.getResponse();
    }

    private void writeMessage(String msg, BufferedWriter writer) throws IOException {
        LOG.info("##########  Getting writer from socket outputStream");
        MessageRequestProcessor requestProcessor = new MessageRequestProcessorImpl(writer);
        MessageRequest request = new ConnectionRequest(msg);
        String stuff = request.getMessageRequest();
        LOG.trace("#########  About to write: " + stuff);
        requestProcessor.sendRequest(request);
    }

    private String getMessage(BufferedReader reader) {
        ConnectionResponseReader mesReader = null;
        mesReader = new ConnectionResponseReader(reader);
        LOG.info("########## About to read message from client...");
        this.connectedAndListening = true;
        return mesReader.getResponse();
    }

    public void run() {
        try {
            LOG.info("Starting server on port:  " + port);
            long delta = System.currentTimeMillis();
            serverSocket = new ServerSocket(port);
            LOG.info("Getting client connection.");
            started = true;
            clientSocket = serverSocket.accept();
            connected = true;
            LOG.info("Accepted client socket connection.");
            // Time to simply echo back what is being sent to the server.
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            pingResponseWhileSleeping(1000L, writer);
            String msg = getMessage(sequenceReader);
            // Loop thru the entire text file and break loop when at end of file (EOF)
            while (msg != null && msg.length() > 0) {
                pingResponseWhileSleeping(1000L, writer);
                if (msg.contains("$StopInfo")) {
                    LOG.info("########## About to read stop info message from client...");
                    msg = getMessage(stopReader);
                    stopReader.close();
                }
                writeMessage(msg, writer);

                if (msg.contains("STORAGE_STATE_VERIFY")) {
                    break;
                }

                LOG.trace("Written message to client:  MSG: " + msg);
                LOG.trace("Read message from client:  MSG: " + msg);
                msg = getMessage(sequenceReader);
                LOG.info("########## Is msg NULL? " + (msg == null));
            }
            pingResponseWhileSleeping(1000L, writer);
            LOG.error("#########  STOPPING NOW!!!!");
            disconnect();


        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }

    }

    private void pingResponseWhileSleeping(long duration, Writer writer) throws IOException {
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < duration) {
            writer.write(PING_RESPONSE);
            writer.flush();
            try {
                Thread.sleep(250L);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
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

    public boolean performingTest() {
        return connected;
    }
}
