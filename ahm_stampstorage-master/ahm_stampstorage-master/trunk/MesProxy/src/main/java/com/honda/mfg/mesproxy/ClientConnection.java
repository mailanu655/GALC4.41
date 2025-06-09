package com.honda.mfg.mesproxy;

import java.io.*;
import java.net.Socket;

/**
 * User: Adam S. Kendell
 * Date: 6/16/11
 */
public class ClientConnection implements Runnable, Connection, ConnectionAgentManager {

    private SimpleLog LOG;
    private Socket srcSocket = null;

    private InputStream srcIn = null;
    private OutputStream srcOut = null;

    private ClientConnectionManager cm = null;

    public String getConnectionName(){
        return srcSocket.getInetAddress().toString();
    }

    public ClientConnection(Socket s, ClientConnectionManager cm) {
        srcSocket = s;
        this.cm = cm;

        try {
            srcIn = s.getInputStream();
            srcOut = s.getOutputStream();

            Thread t = new Thread(this);
            t.start();
        } catch (IOException e) {
            cm.connectionError(this, "" + e);
        }
    }

    public void run() {
        cm.addConnection(this);
    }

    public void stop(){
        cm.removeConnection(this);
    }

    public synchronized void agentHasDied() {
        LOG.write("ClientConnection: " + srcSocket.getInetAddress().toString() + " - Agent has died.");
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
        } catch (Throwable e) {
            e.printStackTrace();
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
