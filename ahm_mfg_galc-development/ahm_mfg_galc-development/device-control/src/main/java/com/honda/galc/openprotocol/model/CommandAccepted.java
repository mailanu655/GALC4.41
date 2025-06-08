package com.honda.galc.openprotocol.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Subu Kathiresan
 * Jan 30, 2009
 */

@XStreamAlias("OPMessage")
public class CommandAccepted extends AbstractOPMessage
{
	@XStreamAlias("MSG_ID")
	private String _ackMsgId = "";
	
	public String getAckMsgId()
	{
		return _ackMsgId;
	}
}

