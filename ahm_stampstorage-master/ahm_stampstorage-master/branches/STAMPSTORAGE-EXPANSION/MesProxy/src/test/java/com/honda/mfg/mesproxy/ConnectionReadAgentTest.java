package com.honda.mfg.mesproxy;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.Mockito.mock;

/**
 * User: Adam S. Kendell
 * Date: 6/21/11
 */
public class ConnectionReadAgentTest extends TestCase {


    private ConnectionAgentManager cam = mock(ConnectionAgentManager.class);
    private InputStream inputStream = mock(InputStream.class);
    private OutputStream outputStream = mock(OutputStream.class);

    public void testSuccessfullyCreateConnectionReadAgent() throws IOException {

        ConnectionReadAgent connectionReadAgent =
                new ConnectionReadAgent(inputStream, cam);


    }

    //public void test

}
