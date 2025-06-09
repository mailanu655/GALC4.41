package com.honda.mfg.stamp.conveyor.processor.messagebuilders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.processor.messagebuilders.json.JSonResponseParser;

/**
 * User: vcc30690 Date: 4/25/11
 */
public class MesMessageBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(MesMessageBuilder.class);

	public MesMessageBuilder() {
	}

	public GeneralMessage buildMesMessage(Message deviceMessage, Class clazz) {
		GeneralMessage mesMessage;
		JSonResponseParser parser = new JSonResponseParser();
		String msg = parser.toJson(deviceMessage, clazz);
		mesMessage = new GeneralMessage(msg);
		LOG.trace("Built mes message: " + mesMessage.getMessage());
		return mesMessage;
	}
}
