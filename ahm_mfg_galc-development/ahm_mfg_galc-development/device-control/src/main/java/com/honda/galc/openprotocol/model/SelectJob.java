package com.honda.galc.openprotocol.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("OPMessage")
public class SelectJob extends AbstractOPMessage{
	@XStreamAlias("JOB_NUMBER")
	private String jobNumber = "";

	public String getJobNumber() {
		return jobNumber;
	}
}
