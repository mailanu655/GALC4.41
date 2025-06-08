package com.honda.galc.entity.conf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.data.PrintAttribute;
import com.honda.galc.device.dataformat.PlcBoolean;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductionLot;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * DeviceFormat data format entry for one tag of the device
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
@Table(name = "GAL257TBX")
public class DeviceFormat extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private DeviceFormatId id;

    @Column(name = "SEQUENCE_NUMBER")
    private int sequenceNumber;
    
    @Column(name = "\"OFFSET\"")
    private int offset;

    @Column(name = "LENGTH")
    private int length;

    @Column(name = "TAG_TYPE")
    private int tagType;

    @Column(name = "TAG_VALUE")
    private String tagValue;
    
    @Column(name = "DATA_TYPE")
    private int dataType;
    
    @Column(name = "TAG_NAME")
    private String tagName;

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "CLIENT_ID", referencedColumnName = "CLIENT_ID",
                    unique = true, nullable = false, insertable = false, updatable = false)
    })

    private Device device;

    @SuppressWarnings("unused")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "CLIENT_ID", referencedColumnName = "REPLY_CLIENT_ID",
                    unique = true, nullable = false, insertable = false, updatable = false)
    })
    private Device device1;
    
    @Transient
    private Object value;
    
    @Transient
    private String exception = null;

 	@Transient
    private Map<String,String> attributeMethods = new TreeMap<String,String>();
    
    public DeviceFormat() {
        super();
    }
    
    public DeviceFormat(String clientId, String tag) {
    	this.id = new DeviceFormatId();
    	id.setClientId(clientId);
    	id.setTag(tag);
    }
    
    public DeviceFormat(String clientId, String tag, String value) {
    	this.id = new DeviceFormatId();
    	id.setClientId(clientId);
    	id.setTag(tag);
    	this.value = value;
    }

    public DeviceFormat(String clientId, String tag, String msg, DeviceTagType type) {
		this(clientId, tag, msg);
		this.setTagType(type.getId());
	}

    public DeviceFormatId getId() {
        return this.id;
    }

    public void setId(DeviceFormatId id) {
        this.id = id;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getTagType() {
        return this.tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public DeviceTagType getDeviceTagType() {
        return DeviceTagType.getType(tagType);
    }

    public void setDeviceTageType(DeviceTagType type) {
        this.tagType = type.getId();
    }
    
    public boolean isStaticType(){
        return getDeviceTagType() == DeviceTagType.STATIC;
    }

    public boolean isAttrType(){
    	if (getDeviceTagType() == DeviceTagType.ATTR_BY_ENGINE_SPEC ||
    			getDeviceTagType() == DeviceTagType.ATTR_BY_FRAME_SPEC ||
    			getDeviceTagType() == DeviceTagType.ATTR_BY_ENGINE ||
    			getDeviceTagType() == DeviceTagType.ATTR_BY_FRAME ||
    			getDeviceTagType() == DeviceTagType.ATTR_BY_PROD_LOT||
    			getDeviceTagType() == DeviceTagType.ATTR_BY_MTOC||
    			getDeviceTagType() == DeviceTagType.ATTR_BY_TRACK ) return true;
    	return false;
    }

    public String getTagValue() {
        return StringUtils.trim(tagValue);
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Object getValue() {
    	if(isStaticType() || isAttrType()) {
    		return getTagValue();
    	}
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public String getTag() {
    	return id != null ? id.getTag() : "";
    }
    
    public void setTag(String tag) {
    	if(id != null)
    		this.id.setTag(tag);
    }

    public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

    public void setDataType(int dataType) {
    	this.dataType = dataType;
    }
    
    public int getDataType() {
    	return this.dataType;
    }

    public DeviceDataType getDeviceDataType() {
    	return DeviceDataType.getType(dataType);
    }

    public void setDeviceDataType(DeviceDataType type) {
    	this.dataType = type.getId();
    }
    
    public String getException() {
 		return exception;
 	}

 	public void setException(String exception) {
 		this.exception = exception;
 	}

    public DeviceFormat clone() {
    	DeviceFormat clone = new DeviceFormat(id.getClientId(),id.getTag());
    	clone.setSequenceNumber(this.getSequenceNumber());
    	clone.setLength(this.getLength());
    	clone.setOffset(this.getOffset());
    	clone.setTagType(this.getTagType());
    	clone.setTagValue(this.getTagValue());
    	clone.setDataType(this.getDataType());
    	clone.setValue(this.getValue());
    	return clone;
    }
    
    public Object convert(Object object) {
    	if(object == null) return object;
    	try {
			if (this.getDeviceDataType() == DeviceDataType.FLOAT) {
				return Float.valueOf(object.toString());
			} else if (this.getDeviceDataType() == DeviceDataType.INTEGER) {
				return Integer.valueOf(object.toString());
			} else if (this.getDeviceDataType() == DeviceDataType.SHORT) {
				return Short.valueOf(object.toString());
			} else if (this.getDeviceDataType() == DeviceDataType.DOUBLE) {
				return Double.valueOf(object.toString());
			} 
    	} catch (Exception e) {
			Logger.getLogger().warn("Invalid data:", object.toString(), e.getMessage());
			exception = e.getMessage();
			return null;
		}
    	
    	if (this.getDeviceDataType() == DeviceDataType.BOOLEAN) {
			return convertToBoolean(object.toString());
		} else if (this.getDeviceDataType() == DeviceDataType.STRING) {
    		return (object instanceof String) ? object : object.toString();
    	} else if (this.getDeviceDataType() == DeviceDataType.PLC_BOOLEAN) {
			return PlcBoolean.parseBoolean(object.toString());
		} else if (this.getDeviceDataType() == DeviceDataType.TIMESTAMP) {
			return Timestamp.valueOf(object.toString()); //Supported Format: yyyy-[m]m-[d]d hh:mm:ss[.f...]
		} 
	
    	
		throw new TaskException("Unsupported data type:" + this.getDataType());
    }
    
	private Boolean convertToBoolean(String value) {
		if("OK".equals(value.trim()) || "1".equals(value.trim()))
			return true;
		else
			return Boolean.valueOf(value);
	}
	
	@SuppressWarnings("unchecked")
	public Map getAttributeMethods() {
    		
        switch(getDeviceTagType())	{
	        case ATTR_BY_ENGINE:
	        	return findAttributes(Engine.class);
	        case ATTR_BY_FRAME:
	        	return findAttributes(Frame.class);
	        case ATTR_BY_ENGINE_SPEC:
	        	return findAttributes(EngineSpec.class);
	        case ATTR_BY_FRAME_SPEC:
	        	return findAttributes(FrameSpec.class);
	        case ATTR_BY_PROD_LOT:
	        	return findAttributes(ProductionLot.class);
	        case ATTR_BY_TRACK:
	        	return findAttributes(Product.class);
        }
    	return new TreeMap<String,String>();
    }
	
	@SuppressWarnings("unchecked")
	private Map findAttributes(Class<?> attributeClass) {
    	Map<String,String> map = new TreeMap<String,String>();
    	Method[] methods = attributeClass.getMethods();
    	for(Method method : methods) {
    		Annotation annotation = method.getAnnotation(PrintAttribute.class);
    		if(annotation != null) {
    			map.put(method.getName(), method.getName());
    		}
    	}
    	
    	return map;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("tagName:").append(getTagName()).append(",");
		sb.append("tagType:").append(getDeviceTagType()).append(",");
		sb.append("tagValue:").append(getTagValue()).append(",");
		sb.append("dataType:").append(getDataType()).append(",");
		sb.append("dataValue:").append(getValue());
		return sb.toString();
	}
    
    public boolean isOk(){
    	if(DeviceDataType.STRING == getDeviceDataType())
    		return "OK".equals(getValue().toString().trim()) ||
    		        "1".equals(getValue().toString().trim());
    	else if(DeviceDataType.BOOLEAN == getDeviceDataType())
    		return (Boolean)getValue();
    	else if(DeviceDataType.SHORT == getDeviceDataType() || DeviceDataType.INTEGER == getDeviceDataType())
    		return ((Integer)getValue()) == 1;
    	else //invalid data type
    		Logger.getLogger().error("Invalid data ready data type:" + getDataType());
    	
    	return false;
    }

	public void setStaticValue() {
		setValue(convert(getTagValue()));
	}
	public Object getDefaultValue() {
		switch(getDeviceDataType()){
		case STRING:
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < getLength(); i++)
				sb.append(Delimiter.SPACE);

			return sb.toString();
		case SHORT:
			return new Short((short)0);
		case INTEGER:
			return 0;
		case FLOAT:
			return 0.0;
		case BOOLEAN:
			return false;
		default:
			return null;
		}
		
	}
}
