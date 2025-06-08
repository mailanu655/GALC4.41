package com.honda.galc.openprotocol.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Subu Kathiresan
 * Jan 30, 2009
 */

@XStreamAlias("OPMessage")
public class CommunicationStartAck extends AbstractOPMessage
{
	@XStreamAlias("CELL_ID")
	private String _cellId = "";
	
	@XStreamAlias("CHANNEL_ID")
	private String _channelId = "";
	
	@XStreamAlias("CONTROLLER_NAME")
	private String _controllerName = "";
	
	@XStreamAlias("SUPPLIER_CODE")
	private String _supplierCode = "";
	
	public String getCellId() {
		return _cellId;
	}

	public void setCellId(String id) {
		_cellId = id;
	}

	public String getChannelId() {
		return _channelId;
	}

	public void setChannelId(String id) {
		_channelId = id;
	}

	public String getControllerName() {
		return _controllerName;
	}

	public void setControllerName(String name) {
		_controllerName = name;
	}
	
	public String getSupplierCode() {
		return _supplierCode;
	}

	public void setSupplierCode(String name) {
		_supplierCode = name;
	}
}
