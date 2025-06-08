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
public class DataCollectionComplete12 implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	public static final String OK = "1";
	public static final String NG = "0";
	
	@Tag(name="DATA_COLLECTION_COMPLETE_3")
	private String dcComplete3;

	@Tag(name="DATA_COLLECTION_COMPLETE_4")
	private String dcComplete4;
	
	public DataCollectionComplete12() {
	}
	
	public DataCollectionComplete12(String flag1,String flag2) {
		this.dcComplete3 = flag1;
		this.dcComplete4 = flag2;
	}
	
	public String getDcComplete3() {
		return dcComplete3;
	}

	public void setDcComplete3(String dcComplete3) {
		this.dcComplete3 = dcComplete3;
	}

	public String getDcComplete4() {
		return dcComplete4;
	}

	public void setDcComplete4(String dcComplete4) {
		this.dcComplete4 = dcComplete4;
	}

	public static DataCollectionComplete12 OK() {
		return new DataCollectionComplete12(OK,OK);
	}
	
	public static DataCollectionComplete12 NG() {
		return new DataCollectionComplete12(NG,NG);
	}
}
