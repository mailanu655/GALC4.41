package com.honda.galc.oif.values;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public class OifServiceConfigValue {

	private String interfaceName;
	private String messageDefinitionsKey;
	private String distributionName;
	
	private List<LabelValueBean> recordDefs = new ArrayList<LabelValueBean>();

	/**
	 * @param name
	 */
	public OifServiceConfigValue(String name) {
		this.interfaceName = name;
	}

	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * @return the recordDefs
	 */
	public List<LabelValueBean> getRecordDefs() {
		return recordDefs;
	}

	/**
	 * @param recDefName
	 * @param tableName
	 * @see java.util.List#add(java.lang.Object)
	 */
	public void addRecordDef(String recDefName, String tableName) {
		recordDefs.add(new LabelValueBean(recDefName, tableName));
	}

	public void setMessageDefinitionsKey(String messageDefinitionsKey) {
		this.messageDefinitionsKey = messageDefinitionsKey;
	}

	public String getMessageDefinitionsKey() {
		return messageDefinitionsKey;
	}

	public void setDistributionName(String distributionName) {
		this.distributionName = distributionName;
	}

	public String getDistributionName() {
		return distributionName;
	}
	
}
