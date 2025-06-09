package com.honda.mfg.connection.exceptions;

import org.junit.Test;

/**
 * User: Jeffrey M Lutz Date: 4/13/11
 */
public class UnknownResponseExceptionTest {

	@Test
	public void testAll() {
		UnknownResponseException e = new UnknownResponseException();
		new UnknownResponseException("Hello");
		new UnknownResponseException("Hello", e);
		new UnknownResponseException(e);
	}
}
