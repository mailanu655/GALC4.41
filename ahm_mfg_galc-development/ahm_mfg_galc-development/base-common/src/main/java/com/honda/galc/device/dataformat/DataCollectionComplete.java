package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class DataCollectionComplete implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	public static final String OK = "1";
	public static final String NG = "0";
	
	@Tag(name="DATA_COLLECTION_COMPLETE")
	private String completeFlag;

	public DataCollectionComplete() {
	}
	
	public DataCollectionComplete(String text) {
		this.completeFlag = text;
	}
	
	public DataCollectionComplete(boolean isOK) {
		this.completeFlag = isOK? OK : NG;
	}
	
	public String getCompleteFlag() {
		return completeFlag;
	}



	public void setCompleteFlag(String completeFlag) {
		this.completeFlag = completeFlag;
	}

	public String toString() {
		return getClass().getSimpleName() + "(" + getCompleteFlag() + ")";
	}


	public static DataCollectionComplete OK() {
		return new DataCollectionComplete(OK);
	}
	
	public static DataCollectionComplete NG() {
		return new DataCollectionComplete(NG);
	}
	
	public boolean isOk(){
		return OK.equals(completeFlag);
	}
}
