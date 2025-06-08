/**
 * 
 */
package com.honda.galc.client.device.ipuqatester.model;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.IDeviceData;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author Subu Kathiresan
 * @date Feb 2, 2012
 */
@XStreamAlias("UNIT_IN_TEST")
public class UnitInTest implements IDeviceData {
	
	@XStreamAlias("VIN")
	@XStreamAsAttribute
	private String _productId = "";
	
	@XStreamAlias("BuildCode")
	@XStreamAsAttribute
	private String _buildCode = "";
	
	@XStreamAlias("TestID")
	@XStreamAsAttribute
	private String _testId = "";
	
	@XStreamAlias("SeqStepFile")
	@XStreamAsAttribute
	private String _seqStepFile = "";
	
	@XStreamAlias("ContStepFile")
	@XStreamAsAttribute
	private String _contStepFile = "";
	
	@XStreamAlias("SEQData")
	@XStreamAsAttribute
	private String _seqData = "";
	
	@XStreamAlias("MACAddress")
	@XStreamAsAttribute
	private String _macAddress = "";
	
	@XStreamAlias("MfgID")
	@XStreamAsAttribute
	private String _mfgId = "";
	
	@XStreamAlias("Production")
	@XStreamAsAttribute
	private String _production = "";
	
	@XStreamAlias("Generation")
	@XStreamAsAttribute
	private String _generation = "";
	
	@XStreamAlias("TotalStatus")
	@XStreamAsAttribute
	private String _status = "";

	@XStreamImplicit(itemFieldName="PROCESS")
	private ArrayList<Process> _processes = new ArrayList<Process>();
	
	/** Need to allow bean to be created via reflection */
	public UnitInTest() {}

	public String getProductId() {
		return StringUtils.trimToEmpty(_productId);
	}

	public void setProductId(String productId) {
		_productId = StringUtils.trimToEmpty(productId);
	}

	public String getBuildCode() {
		return _buildCode;
	}

	public void setBuildCode(String buildCode) {
		_buildCode = buildCode;
	}

	public String getTestId() {
		return _testId;
	}

	public void setTestId(String testId) {
		_testId = testId;
	}

	public String getSeqStepFile() {
		return _seqStepFile;
	}

	public void setSeqStepFile(String seqStepFile) {
		_seqStepFile = seqStepFile;
	}

	public String getContStepFile() {
		return _contStepFile;
	}

	public void setContStepFile(String contStepFile) {
		_contStepFile = contStepFile;
	}

	public String getSeqData() {
		return _seqData;
	}

	public void setSeqData(String seqData) {
		_seqData = seqData;
	}

	public String getMacAddress() {
		return _macAddress;
	}

	public void setMacAddress(String macAddress) {
		_macAddress = macAddress;
	}

	public String getMfgId() {
		return _mfgId;
	}

	public void setMfgId(String mfgId) {
		_mfgId = mfgId;
	}

	public String getProduction() {
		return _production;
	}

	public void setProduction(String production) {
		_production = production;
	}

	public String getGeneration() {
		return _generation;
	}

	public void setGeneration(String generation) {
		_generation = generation;
	}

	public boolean isTotalStatusPass() {
		return _status.equalsIgnoreCase("Pass") ? true : false;
	}

	public void setStatus(String status) {
		_status = status;
	}

	public ArrayList<Process> getProcesses() {
		return _processes;
	}

	public void setProcesses(ArrayList<Process> processes) {
		_processes = processes;
	}	
	
	public String getProductSpecCode() {
		String[] buildCodeArr = getBuildCode().split("-");
		return buildCodeArr[0] + buildCodeArr[1];
	}
}
