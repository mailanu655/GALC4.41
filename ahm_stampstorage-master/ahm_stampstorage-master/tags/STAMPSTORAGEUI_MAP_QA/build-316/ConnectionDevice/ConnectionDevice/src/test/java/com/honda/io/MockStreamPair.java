package com.honda.io;

import com.honda.io.socket.CharSets;
import com.honda.mfg.connection.messages.MessageResponse;
import com.honda.mfg.connection.processor.messages.ConnectionMessage;

import org.bushe.swing.event.EventBus;

import java.io.*;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 9, 2010
 * Time: 1:48:48 PM
 */
public class MockStreamPair implements StreamPair {

    private ByteArrayInputStream input;
    private ByteArrayOutputStream output;
    private BufferedWriter bufOutput;
    private BufferedReader bufIn;
    private ConnectionMessage response;

    public MockStreamPair(ConnectionMessage response) throws UnsupportedEncodingException {

        this.response = response;
        input = new ByteArrayInputStream(response.getMessage().getBytes());
        bufIn = new BufferedReader(new InputStreamReader(input, CharSets.ISO_8859_1));
        output = new ByteArrayOutputStream();
        bufOutput = new BufferedWriter(new OutputStreamWriter(output, CharSets.ISO_8859_1));
    }

    @Override
    public BufferedReader in() {
        EventBus.publish(response);
        return bufIn;
    }

    @Override
    public BufferedWriter out() {
        return bufOutput;
    }

    public String getBytesSentThruOutputStream() {
        String retVal = null;
        try {
            retVal = output.toString(CharSets.ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
        }
        return retVal;
    }
}
