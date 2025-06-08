package com.honda.galc.openprotocol.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Subu Kathiresan
 * Feb 11, 2009
 */

@XStreamAlias("OPMessage")
public class CommandError extends AbstractOPMessage 
{
	@XStreamAlias("MSG_ID")
	private String _failedCommandId = "";
	
	@XStreamAlias("ERROR_CODE")
	private String _errorCode = "";
	
	private OPCommandError _commandError = null;
	
	/**
	 * 
	 * @return
	 */
	public String getFailedCommandId()
	{
		return _failedCommandId;
	}	
	
	/**
	 * 
	 * @return
	 */
	public String getErrorCode()
	{
		return _errorCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public OPCommandError getCommandError()
	{
		return _commandError;
	}
	
	/**
	 * 
	 * @param commandError
	 */
	public void setCommandError(OPCommandError commandError)
	{
		_commandError = commandError;
	}
}
