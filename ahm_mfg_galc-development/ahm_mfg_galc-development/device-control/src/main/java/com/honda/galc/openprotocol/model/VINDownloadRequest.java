package com.honda.galc.openprotocol.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("OPMessage")
public class VINDownloadRequest extends AbstractOPMessage{
	@XStreamAlias("VIN")
	private String vin = "";

	public String getVin() {
		return vin;
	}
}
