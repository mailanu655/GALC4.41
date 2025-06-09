package com.honda.mfg.connection.processor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import com.honda.mfg.connection.exceptions.CommunicationsException;
import com.honda.mfg.connection.exceptions.ResponseTimeoutException;

/**
 * User: vcc30690 Date: 4/13/11
 */
public class ConnectionResponseReaderTest {

	@Test
	public void createsCompleteResponse() {
		String junkString = "\"hi\"::\"bye\":{lie}";
		String expectedMsg1 = "{ \"GeNeRaLMeSsAgE\" : {Hello}}";
		String expectedMsg2 = "{ \"GeNeRaLMeSsAgE\" : {Ignore me!}}";
		String SEP = "" + ConnectionMessageSeparators.DEFAULT.getSeparator();
		String msg = junkString + expectedMsg1 + SEP + expectedMsg2 + SEP;
		StringReader sr = new StringReader(msg);
		BufferedReader reader = new BufferedReader(sr);
		ConnectionResponseReader mesReader = new ConnectionResponseReader(reader);
		// Perform Test
		String actualMsg1 = mesReader.getResponse();
		String actualMsg2 = mesReader.getResponse();
		// Post-condition / assertions
		Assert.assertEquals(expectedMsg1, actualMsg1);
		Assert.assertEquals(expectedMsg2, actualMsg2);
	}

	@Test(expected = ResponseTimeoutException.class)
	public void throwsExceptionAttemptingToReadAnInvalidMessage() {
		String junkString = "\"hi\"::\"bye\":{lie}";
		String expectedMsg1 = "{a \"GeNeRaLMeSsAgE\" : {Ignore me!}}";
		String SEP = "" + ConnectionMessageSeparators.DEFAULT.getSeparator();
		StringReader sr = new StringReader(junkString + expectedMsg1 + SEP);
		BufferedReader reader = new BufferedReader(sr);
		ConnectionResponseReader mesReader = new ConnectionResponseReader(reader);
		// Perform Test
		String actualMsg1 = mesReader.getResponse();
		String actualMsg2 = mesReader.getResponse();
		String actualMsg3 = mesReader.getResponse();
		// Post-condition / assertions
	}

	@Test(expected = ResponseTimeoutException.class)
	public void timesOutWhenResponseIsIncomplete() throws IOException {
		BufferedReader mockedBufferedReader = mock(BufferedReader.class);
		when(mockedBufferedReader.ready()).thenThrow(new ResponseTimeoutException());
		ConnectionResponseReader mesReader = new ConnectionResponseReader(mockedBufferedReader);
		mesReader.getResponse();
	}

	@Test(expected = CommunicationsException.class)
	public void throwsExceptionWhenThereIsAnInputStreamProblem() throws IOException {
		BufferedReader mockedBufferedReader = mock(BufferedReader.class);
		when(mockedBufferedReader.ready()).thenThrow(new IOException("Mocked IO Exception"));
		ConnectionResponseReader mesReader = new ConnectionResponseReader(mockedBufferedReader);
		mesReader.getResponse();
	}

}
