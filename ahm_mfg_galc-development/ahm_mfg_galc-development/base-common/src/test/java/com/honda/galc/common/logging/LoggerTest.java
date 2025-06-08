package com.honda.galc.common.logging;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.message.ObjectMessage;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;


public class LoggerTest {
	private Logger logger = Logger.getLogger();
	private static final String TEST_MESSAGE = "This is test message.";

	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Test LogRecord creation from single string.
	 */
	@Test
	public void getLogRecord_confirmStringMessage() throws Exception {
		LogRecord logRecord = Whitebox.<LogRecord> invokeMethod(logger, "getLogRecord", TEST_MESSAGE);
		assertTrue(TEST_MESSAGE.equals(logRecord.getMessage()));
	}

	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Test LogRecord creation from multiple strings.
	 */
	@Test
	public void getLogRecord_comfirmMultipleStringMessage() throws Exception {
		LogRecord logRecord = Whitebox.<LogRecord> invokeMethod(logger, "getLogRecord", TEST_MESSAGE, TEST_MESSAGE);
		assertTrue((TEST_MESSAGE + TEST_MESSAGE).equals(logRecord.getMessage()));
	}

	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Test ObjectMessage creation from single string.
	 */
	@Test
	public void getObjectMessage_confirmStringMessage() throws Exception {
		ObjectMessage message = Whitebox.<ObjectMessage> invokeMethod(logger, "getObjectMessage", TEST_MESSAGE);
		assertTrue(TEST_MESSAGE.equals(message.getFormattedMessage()));
	}

	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Test ObjectMessage creation from multiple strings. 
	 */
	@Test
	public void getObjectMessage_confirmMultipleStringMessage() throws Exception {
		ObjectMessage message = Whitebox.<ObjectMessage> invokeMethod(logger, "getObjectMessage", TEST_MESSAGE, TEST_MESSAGE);
		assertTrue((TEST_MESSAGE + TEST_MESSAGE).equals(message.getFormattedMessage()));
	}
	
	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Test ObjectMessage creation from LogRecord
	 */
	@Test
	public void getObjectMessage_confirmMessageFromLogRecord() throws Exception {
		LogRecord logRecord = new LogRecord(TEST_MESSAGE);
		ObjectMessage message = Whitebox.<ObjectMessage> invokeMethod(logger, "getObjectMessage", logRecord);
		assertTrue(TEST_MESSAGE.equals(message.getFormattedMessage()));
	}
}
