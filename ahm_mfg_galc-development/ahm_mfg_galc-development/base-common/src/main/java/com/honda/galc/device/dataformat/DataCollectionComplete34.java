package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;

/**
 * 
 * <h3>DataCollectionComplete12 Class description</h3>
 * <p> DataCollectionComplete12 description </p>
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
 * @author Jeffray Huang<br>
 * Jul 13, 2011
 *
 *
 */
public class DataCollectionComplete34 implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	public static final String OK = "1";
	public static final String NG = "0";
	
	@Tag(name="DATA_COLLECTION_COMPLETE_1")
	private String dcComplete1;

	@Tag(name="DATA_COLLECTION_COMPLETE_2")
	private String dcComplete2;
	
	public DataCollectionComplete34() {
	}
	
	public DataCollectionComplete34(String flag1,String flag2) {
		this.dcComplete1 = flag1;
		this.dcComplete2 = flag2;
	}
	
	public String getDcComplete1() {
		return dcComplete1;
	}

	public void setDcComplete1(String dcComplete1) {
		this.dcComplete1 = dcComplete1;
	}

	public String getDcComplete2() {
		return dcComplete2;
	}

	public void setDcComplete2(String dcComplete2) {
		this.dcComplete2 = dcComplete2;
	}

	public static DataCollectionComplete34 OK() {
		return new DataCollectionComplete34(OK,OK);
	}
	
	public static DataCollectionComplete34 NG() {
		return new DataCollectionComplete34(NG,NG);
	}
}
