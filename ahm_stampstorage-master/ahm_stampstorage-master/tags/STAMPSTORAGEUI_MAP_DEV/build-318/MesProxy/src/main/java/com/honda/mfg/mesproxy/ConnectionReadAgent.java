package com.honda.mfg.mesproxy;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: Adam S. Kendell
 * Date: 6/16/11
 */
public class ConnectionReadAgent implements Runnable {

    private InputStream inputStream;
    private ConnectionAgentManager cam;
    private String messageTerminator;
    private boolean running;

    public ConnectionReadAgent(InputStream in, ConnectionAgentManager cam) {
        MesProxyProperties properties = MesProxyProperties.getInstance();

        this.inputStream = in;
        this.cam = cam;
        this.messageTerminator = properties.getMessageTerminator();
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        running = true;
        while (running) {
            String msg = processMessage();
            cam.offerToInputQueue(msg);
        }
    }

    public void stop(){
        running = false;
    }

    private String processMessage() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                int ch = inputStream.read();
                if (ch == -1) {
                    running = false;
                    cam.agentHasDied();
                    return null;
                }
                sb.append((char) ch);

                if (isMessageComplete(sb.toString())) {
                    return sb.toString() + "\r\n\r\n";
                }
            } catch (IOException e) {
                cam.agentHasDied();
                running = false;
                return null;
            }
        }
    }

    private boolean isMessageComplete(String msg) {
        return msg != null && msg.endsWith(messageTerminator);
    }
}
