package com.honda.galc.openprotocol.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Brandon Kroeger
 * September 16, 2015
 */

@XStreamAlias("OPMessage")
public class SetTime extends AbstractOPMessage
{
	@XStreamAlias("TIME")
	private String _time = "";
	
	public String getTime()
	{
		return _time;
	}	
	
}
