package com.honda.galc.device;

import java.util.List;

/**
 * 
 * <h3>DevicePlcData</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DevicePlcData description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Nov 5, 2010
 *
 */
public class DevicePlcData implements IPlcData{
	String id;
	List<PlcDataBlock> dataList;
	boolean changedOnly;
	
	public DevicePlcData() {
		super();
	}

	public DevicePlcData(String id, List<PlcDataBlock> dataList, boolean changedOnly) {
		super();
		this.id = id;
		this.dataList = dataList;
		this.changedOnly = changedOnly;
	}
	
	public DevicePlcData(String id, List<PlcDataBlock> dataList) {
		this(id, dataList, false);
	}

	
	//Getters && Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PlcDataBlock> getDataList() {
		return dataList;
	}

	public void setDataList(List<PlcDataBlock> datList) {
		this.dataList = datList;
	}

	public boolean isChangedOnly() {
		return changedOnly;
	}

	public void setChangedOnly(boolean changedOnly) {
		this.changedOnly = changedOnly;
	}

}
