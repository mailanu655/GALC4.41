package com.honda.galc.entity.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.enumtype.DeviceType;
import com.honda.galc.util.CommonUtil;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Device represents peripheral device (PLC, Torque controller, etc.) entry
 * <p/>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Aug 31, 2009</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL253TBX")
public class Device extends AuditEntry {
    private static final long serialVersionUID = 1L;

    public static String PRINTER_PP = "PP";
	public static String PRINTER_BSX = "BSX";
	
    @Id
    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "ALIAS_NAME")
    private String aliasName;

    @Column(name = "DEVICE_TYPE")
    private int deviceTypeId;

    @Column(name = "DEVICE_DESCRIPTION")
    private String deviceDescription;

    @Column(name = "EIF_IP_ADDRESS")
    private String eifIpAddress;

    @Column(name = "EIF_PORT")
    private int eifPort;

    @Column(name = "IO_PROCESS_POINT_ID")
    private String ioProcessPointId;

    @Column(name = "DIVISION_ID")
    private String divisionId;

    @Column(name = "REPLY_CLIENT_ID")
    private String replyClientId;

    @OneToMany(targetEntity = DeviceFormat.class, mappedBy = "device", cascade = {}, fetch = FetchType.EAGER)
    @OrderBy("sequenceNumber ASC")
    private List<DeviceFormat> deviceDataFormats;

    @OneToMany(targetEntity = DeviceFormat.class, mappedBy = "device1", cascade = {}, fetch = FetchType.EAGER)
    @OrderBy("sequenceNumber ASC")
    private List<DeviceFormat> replyDeviceDataFormats;

    public Device() {
        super();
    }

    public String getClientId() {
        return StringUtils.trim(this.clientId);
    }
    
    public String getId() {
    	return getClientId();
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAliasName() {
        return StringUtils.trim(aliasName);
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public int getDeviceTypeId() {
        return this.deviceTypeId;
    }

    public void setDeviceTypeId(int deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public DeviceType getDeviceType() {
        return DeviceType.getType(deviceTypeId);
    }
    
    public boolean isOPCType(){
        return getDeviceType() == DeviceType.OPC;
    }
    
    public boolean isDeviceWiseType(){
        return getDeviceType() == DeviceType.DEVICE_WISE;
    }

    public boolean isDataFromTagValue(){
    	return getDeviceType() == DeviceType.DEVICE_WISE || getDeviceType() == DeviceType.OPC;
    }
    
    public void setDeviceType(DeviceType type) {
        this.deviceTypeId = type.getId();
    }

    public String getDeviceDescription() {
        return StringUtils.trimToEmpty(deviceDescription);
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    public String getEifIpAddress() {
        return StringUtils.trimToEmpty(eifIpAddress);
    }

    public void setEifIpAddress(String eifIpAddress) {
        this.eifIpAddress = eifIpAddress;
    }
 
    public int getEifPort() {
        return this.eifPort;
    }

    public void setEifPort(int eifPort) {
        this.eifPort = eifPort;
    }

    public String getIoProcessPointId() {
        return StringUtils.trim(ioProcessPointId);
    }

    public void setIoProcessPointId(String ioProcessPointId) {
        this.ioProcessPointId = ioProcessPointId;
    }

    public String getDivisionId() {
        return StringUtils.trim(divisionId);
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getReplyClientId() {
        return StringUtils.trim(replyClientId);
    }

    public void setReplyClientId(String replyClientId) {
        this.replyClientId = replyClientId;
    }

    public List<DeviceFormat> getDeviceDataFormats() {
        return deviceDataFormats;
    }

    public void setDeviceDataFormats(List<DeviceFormat> deviceDataFormats) {
        this.deviceDataFormats = deviceDataFormats;
    }

    public List<DeviceFormat> getReplyDeviceDataFormats() {
        return replyDeviceDataFormats;
    }
    
    public List<DeviceFormat> getChangedDeviceDataFormats() {
    	List<DeviceFormat> deviceFormats = new ArrayList<DeviceFormat>();
    	for(DeviceFormat deviceDataFormat :deviceDataFormats) {
    		if(deviceDataFormat.getValue() != null) deviceFormats.add(deviceDataFormat);
    	}
    	return deviceFormats;
    }
    
    public List<String> getTagNameList() {
		 List<String> tagNameList = new ArrayList<String>();
		 for(DeviceFormat deviceDataFormat :deviceDataFormats) {
			 if(deviceDataFormat.getDeviceTagType() != DeviceTagType.NONE)
				 tagNameList.add(deviceDataFormat.getTag());
		 }
		 return tagNameList;
	}
	
	public List<String> getTagValueList() {
		 List<String> tagValueList = new ArrayList<String>();
		 for(DeviceFormat deviceDataFormat :deviceDataFormats) {
			 if(deviceDataFormat.getDeviceTagType() != DeviceTagType.NONE)
				 tagValueList.add(deviceDataFormat.getTag());
		 }
		 return tagValueList;
	}
    
    public List<String> getReplyTagNameList() {
		 List<String> tagNameList = new ArrayList<String>();
		 for(DeviceFormat deviceDataFormat :replyDeviceDataFormats) {
			 if(deviceDataFormat.getDeviceTagType() != DeviceTagType.NONE)
				 tagNameList.add(deviceDataFormat.getTag());
		 }
		 return tagNameList;
	}
	
	public List<String> getReplyTagValueList() {
		 List<String> tagValueList = new ArrayList<String>();
		 for(DeviceFormat deviceDataFormat :replyDeviceDataFormats) {
			 if(deviceDataFormat.getDeviceTagType() != DeviceTagType.NONE)
				 tagValueList.add(deviceDataFormat.getTag());
		 }
		 return tagValueList;
	}
	
    public void setReplyDeviceDataFormats(List<DeviceFormat> replyDeviceDataFormats) {
        this.replyDeviceDataFormats = replyDeviceDataFormats;
    }
    
    public void resetDeviceDataFormatValues() {
        resetDataFormats();
        resetReplyFormats();
    }

	public void resetReplyFormats() {
		if(replyDeviceDataFormats != null){
			for(DeviceFormat deviceData : replyDeviceDataFormats) {
				if(deviceData.getDeviceTagType() == DeviceTagType.DEVICE || 
						deviceData.getDeviceTagType() == DeviceTagType.TAG)
                deviceData.setValue(null);
            }
        }
	}

	private void resetDataFormats() {
		if(deviceDataFormats != null) {
            for(DeviceFormat deviceData : deviceDataFormats) {
                deviceData.setValue(null);
            }
        }
	}
    

    public DeviceFormat getDeviceFormat(String tagName){
    	 if(deviceDataFormats != null) {
             for(DeviceFormat deviceFormat : deviceDataFormats) {
                 if(deviceFormat.getId().getTag().equals(tagName))
                	 return deviceFormat;
             }
         }
    	 
    	 return null;
    }
    
    
    public DeviceFormat getReplyDeviceFormat(String tagName) {
    	 if(replyDeviceDataFormats != null) {
             for(DeviceFormat deviceFormat : replyDeviceDataFormats) {
                 if(getReplyTag(deviceFormat).equals(tagName))
                	 return deviceFormat;
             }
         }
    	 
    	 return null;
		
	}

	public String getReplyTag(DeviceFormat deviceFormat) {
		if(isOPCType() || isDeviceWiseType()) return deviceFormat.getTagValue();
		else return deviceFormat.getId().getTag();
	}
    
 
	public String toString() {
		
		return toString(getClientId(), 
		                getReplyClientId(),
		                getIoProcessPointId());
		
	}

	public boolean isAddressDefined() {
		//A simple quick validation
		return !StringUtils.isBlank(eifIpAddress) && eifPort != 0;
	}
	
	public Device clone(){
		return new Device(this);
	}

	public Device(Device dev) {
		super();
		this.clientId = dev.getClientId();
		this.aliasName = dev.getAliasName();
		this.deviceTypeId = dev.getDeviceTypeId();
		this.deviceDescription = dev.getDeviceDescription();
		this.eifIpAddress = dev.getEifIpAddress();
		this.eifPort = dev.getEifPort();
		this.ioProcessPointId = dev.getIoProcessPointId();
		this.divisionId = dev.getDivisionId();
		this.replyClientId = dev.getReplyClientId();
		
		this.deviceDataFormats = new ArrayList<DeviceFormat>();
		for(DeviceFormat format : dev.getDeviceDataFormats())
			deviceDataFormats.add(format.clone());
		
		this.replyDeviceDataFormats = new ArrayList<DeviceFormat>();
		for(DeviceFormat format : dev.getReplyDeviceDataFormats())
			replyDeviceDataFormats.add(format.clone());
	}

	public void populate(Map<Object, Object> data) {
		for(DeviceFormat format: deviceDataFormats){
			if(getDeviceType() == DeviceType.OPC||getDeviceType() == DeviceType.DEVICE_WISE)
			    format.setValue(format.convert(data.get(format.getTagValue())));
			else
				format.setValue(format.convert(data.get(format.getTag())));
		}
	}
	
	public void populateUnsolicitedData(DataContainer dc) {
		for(Entry<Object,Object> entry : dc.entrySet()) {
			String key = ObjectUtils.toString(entry.getKey());
			if(!dc.getClientID().equals(key)) {
				DeviceFormat format = getDeviceFormat(key);
				if(format != null) {
					Object obj = format.convert(dc.get(format.getTag()));
					format.setValue(obj == null ? format.getDeviceDataType().getDefaultValue() : obj);
				}
			}
		}
	}
	
	public void populateReply(Map<Object, Object> data) {
	
		resetReplyFormats();
		
		for(DeviceFormat format: replyDeviceDataFormats){
			if(getDeviceType() == DeviceType.OPC||getDeviceType() == DeviceType.DEVICE_WISE){
				if(data.containsKey(format.getTagValue()))
					format.setValue(data.get(format.getTagValue()));
			} else {
				if(data.containsKey(format.getTag()))
					format.setValue(data.get(format.getTag()));
			}
		}
	}
	
	public DataContainer toDataContainer() {
		DataContainer dc = new DefaultDataContainer();
		for(DeviceFormat format: deviceDataFormats){
			dc.put(format.getTag(), format.getValue());
		}
		return dc;
	}
	
	
	public DataContainer toReplyDataContainer(boolean isString) {
		DataContainer dc = new DefaultDataContainer();
		List<String> taglist = new ArrayList<String>(); 
		
		adjustDataFormats();
		
		for(DeviceFormat format: replyDeviceDataFormats){
			if(format.getDeviceTagType() == DeviceTagType.DATA_FORMATTER) continue;
			dc.put(format.getTag(), getTagValue(isString, format));
			if(!(format.getDeviceTagType() == DeviceTagType.NONE)){
				taglist.add(format.getTag());
			}
		}
		
		if(taglist.size() > 0 && !isDeviceWiseType())
			dc.put(DataContainerTag.TAG_LIST, taglist);
		
		dc.put(DataContainerTag.PROCESS_POINT_ID, getIoProcessPointId());
		dc.put(DataContainerTag.CLIENT_ID, getReplyClientId());
		
		return dc;
	}

	private void adjustDataFormats() {
		try {
			for(DeviceFormat df: replyDeviceDataFormats)
				if(df.getDeviceTagType() == DeviceTagType.DATA_FORMATTER) {
					List<Object> formatterArguments = CommonUtil.parseFormatterArguments(df.getTagValue(), getReplyMap());
					String format = (String)formatterArguments.get(0);
					formatterArguments.remove(0);
					String formattedOut = String.format(format, formatterArguments.toArray());

					DeviceFormat targetDeviceFormat = getDeviceFormat(replyDeviceDataFormats, df.getTagName());
					targetDeviceFormat.setValue(formattedOut);
				}
		} catch(Exception e) { // should not stop sending data
			Logger.getLogger().error("ERROR: Exception on processing data formatter." + e.getCause());
		}
	}

	private DeviceFormat getDeviceFormat(List<DeviceFormat> formatList, String tagName) {
		for(DeviceFormat df : formatList)
			if(tagName.equals(df.getTag()) || tagName.equals(df.getTagValue()))
				return df;
		return null;
	}

	private Map<Object, Object> getReplyMap() {
		Map<Object, Object> map = new HashMap<Object, Object>();

		for(DeviceFormat format : replyDeviceDataFormats) {
			if(this.getDeviceType()==DeviceType.OPC)
				map.put(format.getTagValue(), format.getValue());
			else 
				map.put(format.getTag(), format.getValue());
		}

		return map;
	}

	private Object getTagValue(boolean isString, DeviceFormat format) {
		
		 if(isString)
			 return format.getValue() == null ? "" : format.getValue().toString();
	     else 
	    	 return format.getValue() == null ? "" : format.getValue();
	}

	public void dataCollectionStatusOk() {
		for(DeviceFormat format : replyDeviceDataFormats){
			if(DataContainerTag.DATA_COLLECTION_STATUS.equals(getGalcStatusTag(format)))
				format.setValue("1");//make it compatible with GALC 
		}
		
	}
	
	public void dataCollectionStatusNg() {
		for(DeviceFormat format : replyDeviceDataFormats){
			if(DataContainerTag.DATA_COLLECTION_STATUS.equals(getGalcStatusTag(format)))
				format.setValue("0");//make it compatible with GALC
		}
		
	}
	
	private String getGalcStatusTag(DeviceFormat format) {
		return (getDeviceType() == DeviceType.EQUIPMENT) ? format.getTag() : format.getTagValue();
	}
	
	public Object getInputValue(String tag){
		DeviceFormat format = getDeviceFormatByTagName(tag, deviceDataFormats);
		return format == null ? null : format.getValue();
	}
	
	public Object getReplyValue(String tag){
		DeviceFormat format = getDeviceFormatByTagName(tag, replyDeviceDataFormats);
		return format == null ? null : format.getValue();
	}

	public static DeviceFormat getDeviceFormatByTagName(String tag, List<DeviceFormat> dataFormats) {
		for(DeviceFormat format : dataFormats){
			if(tag.equals(format.getTag()))
				return format;
		}

		return null;
	}

	public Map<Object, Object> getInputMap() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		for(DeviceFormat format : deviceDataFormats)
			map.put(format.getTag(), format.getValue());
		
		return map;
	}

	public String toInputString(){
		return toLogString(true);
	}
	
	public String toReplyString(){
		return toLogString(false);
	}
	
	public String toLogString(boolean isInput) {
		StringBuilder sb = new StringBuilder();
		sb.append("clientId:").append(getClientId()).append(",");
		sb.append("replyClientId:").append(getReplyClientId()).append(",");
		sb.append("deviceType:").append(getDeviceType()).append(",");
		sb.append(System.getProperty("line.separator")).append("{").append(System.getProperty("line.separator"));;
		
		if(isInput)
			addDataFormats(sb, deviceDataFormats);
		else
			addDataFormats(sb, replyDeviceDataFormats);
		
		sb.append("}").append(System.getProperty("line.separator"));
		return sb.toString();
	}

	private void addDataFormats(StringBuilder sb, List<DeviceFormat> deviceFormats) {
		if(deviceFormats == null) return;
		for(DeviceFormat format : deviceFormats){
			if(format.getDeviceTagType() == DeviceTagType.STATIC)
				sb.append(format.getTag()).append(":").append(format.getTagValue());
			else {
				sb.append(format.getTag()).append(":").append(format.getValue() == null ? "null" : format.getValue());
				sb.append(System.getProperty("line.separator"));
			}
			
		}
	}

	public void copyReplyDeviceDataFormats(List<DeviceFormat> formats) {
		resetReplyFormats();
		
		for(DeviceFormat format: formats)
			if(format.getValue() != null) getDeviceFormat(format.getTag()).setValue(format.getValue());
		
		
	}

	public DeviceFormat getInputDeviceFormat(String tag){
		return getDeviceFormatByTagName(tag, deviceDataFormats);
	}
	
	public Object getInputObjectValue(String tag) {
		DeviceFormat format = getDeviceFormatByTagName(tag, deviceDataFormats);
		if(format == null) return null;
		return format.convert(format.getValue());
		
	}

	public void addException(String msg) {
		getReplyDeviceDataFormats().add(new DeviceFormat(getClientId(), TagNames.EXCEPTION.name(), msg, DeviceTagType.NONE));
		
	}

	public String getException(){
		return getReplyDeviceFormat(TagNames.EXCEPTION.name()) == null? null : (String)getReplyDeviceFormat(TagNames.EXCEPTION.name()).getValue();
	}
	
	public void addMessage(String msg) {
		getReplyDeviceDataFormats().add(new DeviceFormat(getClientId(), TagNames.MESSAGE.name(), msg, DeviceTagType.NONE));
		
	}
	
	public String getMessage(){
		return getReplyDeviceFormat(TagNames.MESSAGE.name()) == null? null : (String)getReplyDeviceFormat(TagNames.MESSAGE.name()).getValue();
	}

	public void setReplyValue(String tagName, Object value) {
		getReplyDeviceFormat(tagName).setValue(value);
		
	}
	
	public DataContainer convertDeviceTag(DataContainer dc){
		populate(dc);
		return toDataContainer();
	}
	
	public DataContainer toRequestDataContainer() {
		DataContainer dc = new DefaultDataContainer();
		for(DeviceFormat format: deviceDataFormats){
			if(isDataFromTagValue())
				dc.put(format.getTagValue(), format.getValue());
			else
				dc.put(format.getTag(), format.getValue());
		}
		dc.put(TagNames.PROCESS_POINT_ID.name(), getIoProcessPointId());
		dc.put(TagNames.CLIENT_ID.name(), getClientId());
		dc.put(TagNames.APPLICATION_ID.name(), getIoProcessPointId());
		
		return dc;
	}
	
	
	
}
