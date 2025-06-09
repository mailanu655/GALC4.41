package com.honda.io.socket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.IllegalBlockingModeException;

import org.junit.Test;

import com.honda.mfg.connection.exceptions.NetworkCommunicationException;

/**
 * User: Mihail Chirita Date: Sep 28, 2010 Time: 10:21:35 AM
 */
public class SocketStreamPairTest {
	private ByteArrayOutputStream expectedOut;

	private Socket getMockSocket(String inputString) throws IOException {
		InputStream expectedIn = new ByteArrayInputStream(inputString.getBytes());
		expectedOut = new ByteArrayOutputStream();

		Socket socket = mock(Socket.class);
		when(socket.getInputStream()).thenReturn(expectedIn);
		when(socket.getOutputStream()).thenReturn(expectedOut);

		return socket;
	}

	@Test
	public void successfullyReturnsStreamWhenConnected() throws IOException {
// PRE-CONDITIONS
		String expectedMsg = "Hello World!";
		Socket socket = getMockSocket(expectedMsg);

		SocketFactory socketFactory = mock(SocketFactory.class);
		when(socketFactory.createSocket()).thenReturn(socket);

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);

		String actualMsg = streamPair.in().readLine();

		String expectedOutput = "Good bye";
		streamPair.out().write(expectedOutput);
		streamPair.out().flush();

		String actualOutput = expectedOut.toString();

// ASSERT POST_CONDITIONS
		verify(socketFactory, times(1)).createSocket();
		verify(socket, times(1)).getInputStream();
		verify(socket, times(1)).getOutputStream();

		assertEquals(expectedMsg, actualMsg);
		assertEquals(expectedOutput, actualOutput);
	}

	@Test(expected = IOException.class)
	public void throwsExceptionWhenConnectionIsClosed() throws IOException {
// PRE-CONDITIONS
		String line1 = "Line 1";
		String line2 = "Line 2";
		String line3 = "Line 3";
		String expectedMsg = line1 + "\n" + line2 + "\n" + line3 + "\n";
		Socket socket1 = getMockSocket(expectedMsg);
		Socket socket2 = getMockSocket("I should never see this in the test");

		SocketFactory socketFactory = mock(SocketFactory.class);
		when(socketFactory.createSocket()).thenReturn(socket1).thenReturn(socket2);
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);

// PERFORM TEST
		// create new socket that is the socket1 instance
		BufferedReader reader = streamPair.in();
		BufferedWriter writer = streamPair.out();
		String firstRead;
		firstRead = reader.readLine();
		writer.write("Hello");
		assertEquals(line1, firstRead);
		firstRead = reader.readLine();
		assertEquals(line2, firstRead);
		// force stopProcessing to simulate lost connection
		streamPair.close();

		// Attempt to use the reader even though the streampair socket was closed.
		// Should throw an IOException
		writer.write("bye");
		String actualMsg = reader.readLine();
	}

	@Test
	public void reconnectsIfConnectionIsLost() throws IOException {
// PRE-CONDITIONS
		String line1 = "Line 1";
		String line2 = "Line 2";
		String line3 = "Line 3";
		String line4 = "Line 4";
		String expectedMsg = line1 + "\n" + line2 + "\n" + line3 + "\n";
		Socket socket1 = getMockSocket(expectedMsg);
		Socket socket2 = getMockSocket(line4);

		SocketFactory socketFactory = mock(SocketFactory.class);
		when(socketFactory.createSocket()).thenReturn(socket1).thenReturn(socket2);
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);

// PERFORM TEST
		assertEquals(line1, streamPair.in().readLine());
		assertEquals(line2, streamPair.in().readLine());
		streamPair.close(); // force stopProcessing to simulate lost connection

		assertEquals(line4, streamPair.in().readLine());

// ASSERT POST_CONDITIONS
		verify(socketFactory, times(2)).createSocket();
		verify(socket1, times(1)).getInputStream();
		verify(socket1, times(1)).getOutputStream();
		verify(socket2, times(1)).getInputStream();
		verify(socket2, times(1)).getOutputStream();
	}

	@Test(expected = NetworkCommunicationException.class)
	public void throwsUnknownHostExceptionEstablishingConnection() {
// PRE-CONDITIONS
		SocketFactory socketFactory = mock(SocketFactory.class);
		try {
			when(socketFactory.createSocket()).thenThrow(new UnknownHostException("Unknown host!"));
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
		streamPair.in();
	}

	@Test(expected = NetworkCommunicationException.class)
	public void throwsIllegalArgumentExceptionEstablishingConnection() {
// PRE-CONDITIONS
		SocketFactory socketFactory = mock(SocketFactory.class);
		try {
			when(socketFactory.createSocket()).thenThrow(new IllegalArgumentException("Unknown host!"));
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
		streamPair.in();
	}

	@Test(expected = NetworkCommunicationException.class)
	public void throwsSocketTimeoutExceptionEstablishingConnection() {
// PRE-CONDITIONS
		SocketFactory socketFactory = mock(SocketFactory.class);
		try {
			when(socketFactory.createSocket()).thenThrow(new SocketTimeoutException("Unknown host!"));
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
		streamPair.in();
	}

	@Test(expected = NetworkCommunicationException.class)
	public void throwsIllegalBlockingModeExceptionEstablishingConnection() {
// PRE-CONDITIONS
		SocketFactory socketFactory = mock(SocketFactory.class);
		try {
			when(socketFactory.createSocket()).thenThrow(new IllegalBlockingModeException());
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
		streamPair.in();
	}

	@Test(expected = NetworkCommunicationException.class)
	public void throwsSecurityExceptionEstablishingConnection() {
// PRE-CONDITIONS
		SocketFactory socketFactory = mock(SocketFactory.class);
		try {
			when(socketFactory.createSocket()).thenThrow(new SecurityException("Unknown host!"));
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
		streamPair.in();
	}

	@Test(expected = NetworkCommunicationException.class)
	public void throwsIOExceptionEstablishingConnection() {
// PRE-CONDITIONS
		SocketFactory socketFactory = mock(SocketFactory.class);
		try {
			when(socketFactory.createSocket()).thenThrow(new IOException("Unknown host!"));
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
		streamPair.in();
	}

	@Test // (expected = NetworkCommunicationException.class)
	public void throwsExceptionEstablishingConnectionToUnreachableDevice() {
// PRE-CONDITIONS
		SocketFactory socketFactory = mock(SocketFactory.class);
		try {
			when(socketFactory.createSocket())
					.thenThrow(new NetworkCommunicationException("Failed to connect to device"));
		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
		streamPair.in();
	}

	@Test // (expected = NetworkCommunicationException.class)
	public void throwsExceptionWhenItFailsToReconnect() throws IOException {
// PRE-CONDITIONS
		String expectedMsg = "Hello World!";
		Socket socket1 = getMockSocket("Some bogus message");

		SocketFactory socketFactory = mock(SocketFactory.class);
		when(socketFactory.createSocket()).thenReturn(socket1)
				.thenThrow(new NetworkCommunicationException("Failed to connect to device"));

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);

		streamPair.in(); // create new socket that is the socket1 instance
		streamPair.close(); // force stopProcessing to simulate lost connection

		verify(socketFactory, times(1)).createSocket();
		verify(socket1, times(1)).getInputStream();
		verify(socket1, times(1)).getOutputStream();

		// Throw exception in trying to establish second connection
		streamPair.in();
	}

	@Test(expected = NetworkCommunicationException.class)
	public void throwsExceptionWhenItFailsToGetInputStream() throws IOException {
// PRE-CONDITIONS
		String expectedMsg = "Hello World!";
		Socket socket = getMockSocket(expectedMsg);

		when(socket.getInputStream()).thenThrow(new IOException("I cannot do that, Dave"));

		SocketFactory socketFactory = mock(SocketFactory.class);
		when(socketFactory.createSocket()).thenReturn(socket);

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);

		streamPair.in().readLine();
	}

	@Test(expected = NetworkCommunicationException.class)
	public void throwsExceptionWhenItFailsToGetOutputStream() throws IOException {
// PRE-CONDITIONS
		String expectedMsg = "Hello World!";
		Socket socket = getMockSocket(expectedMsg);

		when(socket.getOutputStream()).thenThrow(new IOException("I cannot do that, Dave"));

		SocketFactory socketFactory = mock(SocketFactory.class);
		when(socketFactory.createSocket()).thenReturn(socket);

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);

		streamPair.out().write("Daisy... Daisy...");
	}

	@Test
	public void throwsExceptionTryingToCloseAlreadyClosedStreamPair() throws IOException {
// PRE-CONDITIONS
		String expectedMsg = "Hello World!";
		Socket socket1 = getMockSocket("Some bogus message");

		doThrow(new IOException("Failed to close")).when(socket1).close();

		SocketFactory socketFactory = mock(SocketFactory.class);
		when(socketFactory.createSocket()).thenReturn(socket1)
				.thenThrow(new NetworkCommunicationException("Failed to connect to device"));

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);

		streamPair.in(); // create new socket that is the socket1 instance
		streamPair.close(); // force stopProcessing to simulate lost connection

		verify(socketFactory, times(1)).createSocket();
		verify(socket1, times(1)).getInputStream();
		verify(socket1, times(1)).getOutputStream();

	}

	@Test(expected = IOException.class)
	public void throwsExceptionTryingToReadFromClosedStreamPair() throws IOException {
// PRE-CONDITIONS
		String expectedMsg = "Hello World!";
		Socket socket1 = getMockSocket("Some bogus message");
		ByteArrayInputStream inputStream = new ByteArrayInputStream("hello world".getBytes());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		when(socket1.getInputStream()).thenReturn(inputStream);
		when(socket1.getOutputStream()).thenReturn(outputStream);

		SocketFactory socketFactory = mock(SocketFactory.class);
		when(socketFactory.createSocket()).thenReturn(socket1);

// PERFORM TEST
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);

		BufferedReader reader = streamPair.in(); // create new socket that is the socket1 instance
		BufferedWriter writer = streamPair.out();
		streamPair.close(); // force stopProcessing to simulate lost connection

		String actualMsg = reader.readLine();
	}
}
