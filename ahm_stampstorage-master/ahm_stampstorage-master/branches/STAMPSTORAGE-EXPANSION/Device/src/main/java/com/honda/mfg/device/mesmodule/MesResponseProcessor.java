package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.NetworkCommunicationException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.mesmodule.messages.GeneralMessage;
import com.honda.mfg.device.mesmodule.messages.MesMessage;
import com.honda.mfg.device.mesmodule.messages.PingMessage;
import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: vcc30690
 * Date: 3/23/11
 */
public class MesResponseProcessor implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MesResponseProcessor.class);
    private MesResponseReader responseReader;
    private volatile boolean connected;

    private volatile boolean runOnce;
    private volatile boolean recordSequence;
    private List<Object> runSequence = new ArrayList<Object>();

    public MesResponseProcessor(MesResponseReader responseReader) {
        this(responseReader, false);
    }

    MesResponseProcessor(MesResponseReader responseReader, boolean runOnce) {
        this(responseReader, runOnce, false);
    }

    MesResponseProcessor(MesResponseReader responseReader, boolean runOnce, boolean recordSequence) {
        this.responseReader = responseReader;
        this.recordSequence = recordSequence;
        this.runOnce = runOnce;
        this.connected = true;
    }

    public void run() {
        LOG.info("Starting response processor. Thread id: " + Thread.currentThread().getId());
        boolean successfulFirstPass = false;
        while (connected) {
            try {
                processResponse();
                if (!successfulFirstPass) {
                    LOG.info("Complete first response processor cycle.");
                    successfulFirstPass = true;
                }
            } catch (ResponseTimeoutException ex) {
                recordItem(ex);
                LOG.trace("Response timeout occurred.  MSG: " + ex.getMessage());
            } catch (CommunicationsException e) {
                recordItem(e);
                LOG.debug("Communications error. MSG: " + e.getMessage());
                connected = false;
            } catch (NetworkCommunicationException e) {
                recordItem(e);
                LOG.debug("Network error. MSG: " + e.getMessage());
                connected = false;
            }catch(Exception e){
                e.printStackTrace();
            }

            if (runOnce) {
                break;
            }
        }
        LOG.debug("Stopped Response Processor since communications failed. Thread ID: " + Thread.currentThread().getId());
    }

    boolean isRunning() {
        return connected;
    }

    List<Object> getSequence() {
        return runSequence;
    }

    private void recordItem(Object item) {
        if (recordSequence) {
            runSequence.add(item);
        }
    }

    void processResponse() {
        String response = responseReader.getResponse();
        MesMessage message;
        if (response.contains("PING")) {
            message = new PingMessage(response);
        } else {
            message = new GeneralMessage(response);
        }
        recordItem(message);
        LOG.debug("Publishing message:  " + message);
        EventBus.publish(message);
    }

    void setConnected(boolean val) {
        this.connected = val;
    }
}
