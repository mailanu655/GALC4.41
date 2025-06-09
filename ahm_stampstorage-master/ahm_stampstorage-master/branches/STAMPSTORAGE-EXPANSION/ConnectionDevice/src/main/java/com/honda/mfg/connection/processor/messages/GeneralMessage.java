package com.honda.mfg.connection.processor.messages;

/**
 * User: vcc30690 Date: 4/12/11
 */
public class GeneralMessage extends ConnectionMessageBase {

	// usage: new GeneralMessage("GeneralMessage{ a:'{b:'{c:'hi'}'}' }")
	// usage: new GeneralMessage("...")
	public GeneralMessage(String message) {
		super(message);
	}
}
