package com.honda.mfg.mesproxy;

import java.io.*;
import java.net.Socket;

/**
 * User: Adam S. Kendell
 * Date: 6/16/11
 */
public class ServerConnection implements Runnable, Connection, ConnectionAgentManager {

    private Socket srcSocket = null;

    private InputStream srcIn = null;
    private OutputStream srcOut = null;

    private ServerConnectionManager cm = null;
    private ConnectionReadAgent connectionReadAgent;

    private String messageTerminator;

    public ServerConnection(Socket s, ServerConnectionManager cm) {
        srcSocket = s;
        this.cm = cm;
        this.messageTerminator = cm.getMessageTerminator();

        try {
            srcIn = s.getInputStream();
            srcOut = s.getOutputStream();

            Thread t = new Thread(this);
            t.start();
        } catch (IOException e) {
            cm.connectionError(this, "" + e);
        }
    }

    public String getConnectionName(){
        return srcSocket.getInetAddress().toString() + " : " + Integer.toString(srcSocket.getLocalPort());
    }

    public String getMessageTerminator() {
        return messageTerminator;
    }

    public void run() {
        cm.addConnection(this);
        connectionReadAgent = new ConnectionReadAgent(srcIn, this);
    }

    public void stop() {
        connectionReadAgent.stop();
        connectionReadAgent = null;
        agentHasDied();
    }

    public synchronized void agentHasDied() {
        SimpleLog.write("ServerConnection: " + srcSocket.getInetAddress().toString() + " - Agent has died.");
        closeSrc();
        cm.removeConnection(this);
    }

    public boolean offerToInputQueue(String input) {
        boolean retVal = false;
        if (input != null) {
            retVal = cm.offerToInputQueue(input);
        }
        return retVal;
    }

    public void writeToOutputStream(String out) {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(srcOut));
            writer.write(out);
            writer.flush();
        } catch (IOException e) {
            this.agentHasDied();
        }
    }

    private void closeSrc() {
        try {
            srcIn.close();
            srcOut.close();
            srcSocket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
