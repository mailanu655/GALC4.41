package com.honda.mfg.stamp.conveyor.exceptions;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 8/25/14 Time: 1:33 PM To
 * change this template use File | Settings | File Templates.
 */
public class WrongDieInQueueException extends RuntimeException {

	public WrongDieInQueueException(String message) {
		super(message);
	}
}
