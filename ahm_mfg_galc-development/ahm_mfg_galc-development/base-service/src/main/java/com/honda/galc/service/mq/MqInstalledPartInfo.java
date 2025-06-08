package com.honda.galc.service.mq;

import java.io.Serializable;
import java.lang.reflect.Field;

public class MqInstalledPartInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String partName;
	private String partSerialNumber;
	private String partStatus;
	private String timestamp;
	
	public MqInstalledPartInfo(){
		
	}
	public MqInstalledPartInfo(String partName, String partSerialNumber,
			String partStatus, String timestamp) {
	this.partName=partName;	
	this.partSerialNumber=partSerialNumber;
	this.partStatus=partStatus;
	this.timestamp=timestamp;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getPartSerialNumber() {
		return partSerialNumber;
	}
	public void setPartSerialNumber(String partSerialNumber) {
		this.partSerialNumber = partSerialNumber;
	}
	public String getPartStatus() {
		return partStatus;
	}
	public void setPartStatus(String partStatus) {
		this.partStatus = partStatus;
	}
	/*
	public String toString(){
		return "partname= "+partName+",partSerialNumber= "+partSerialNumber+",partStatus= "+partStatus+",timestamp= "+timestamp;
		
	}
	public String toString1() {
		  StringBuilder result = new StringBuilder();
		  result.append( "{" );
		  //determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();
		  int i = 1;
		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    try {
		      result.append( field.getName() );
		      result.append(":");
		      //requires access to private field:
		      result.append( field.get(this) );
		      if (i++ != fields.length) result.append( "," );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		  }
		  result.append("}");

		  return result.toString();
	}
	*/

}
