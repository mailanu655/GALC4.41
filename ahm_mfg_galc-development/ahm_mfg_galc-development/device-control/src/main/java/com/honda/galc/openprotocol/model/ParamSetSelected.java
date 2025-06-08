package com.honda.galc.openprotocol.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Subu Kathiresan
 * Jan 30, 2009
 */

@XStreamAlias("OPMessage")
public class ParamSetSelected extends AbstractOPMessage
{
	@XStreamAlias("PSET_NUMBER")
	private String _psetNumber = "";
	
	@XStreamAlias("DATE_LAST_CHANGED")
	private String _dateLastChanged = "";
	
	public String getPsetNumber()
	{
		return _psetNumber;
	}
	
	public String getDateLastChanged()
	{
		return _dateLastChanged;
	}
}
