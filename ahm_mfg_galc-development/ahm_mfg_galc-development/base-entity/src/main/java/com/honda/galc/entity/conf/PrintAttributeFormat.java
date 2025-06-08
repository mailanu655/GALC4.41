package com.honda.galc.entity.conf;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.PrintAttributeFormatRequiredType;
import com.honda.galc.entity.enumtype.PrintAttributeType;


/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * PrintAttributeFormat represents print of build attribute for particular product spec
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
@Table(name = "GAL258TBX")
public class PrintAttributeFormat extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PrintAttributeFormatId id;

    @Column(name = "\"OFFSET\"")
    private int offset;

    @Column(name = "LENGTH")
    private int length;

    @Column(name = "ATTRIBUTE_TYPE")
    private int attributeTypeId;

    @Column(name = "ATTRIBUTE_VALUE")
    private String attributeValue;

    @Column(name = "SEQUENCE_NUMBER")
    private int sequenceNumber;
    
    @Column(name = "REQUIRED")
    private Integer requiredTypeId;
    
  
	@Transient
    private Map<String,String> attributeMethods = new TreeMap<String,String>();
    
    public PrintAttributeFormat() {
        super();
    }
    public PrintAttributeFormat(String formId, String attribute) {
    	this.id = new PrintAttributeFormatId();
    	id.setFormId(formId);
    	id.setAttribute(attribute);
    }
    
    public PrintAttributeFormatId getId() {
        return this.id;
    }

    public void setId(PrintAttributeFormatId id) {
        this.id = id;
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

    public int getAttributeTypeId() {
        return this.attributeTypeId;
    }

    public void setAttributeTypeId(int attributeTypeId) {
        this.attributeTypeId = attributeTypeId;
    }

    public PrintAttributeType getAttributeType() {
        return PrintAttributeType.getType(attributeTypeId);
    }

    public void setAttributeType(PrintAttributeType type) {
        this.attributeTypeId = type.getId();
    }

    public String getAttributeValue() {
        return StringUtils.trim(this.attributeValue);
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    public String getAttribute() {
    	return id.getAttribute();
    }
    
    public Map<String, String> getAttributeMethods() {
		return attributeMethods;
	}
    
	public void setAttributeMethods(Map<String, String> attributeMethods) {
		this.attributeMethods = attributeMethods;
	}
	        
    public PrintAttributeFormat clone() {
    	PrintAttributeFormat clone = new PrintAttributeFormat(id.getFormId(),id.getAttribute());
    	clone.setAttributeTypeId(this.getAttributeTypeId());
    	clone.setAttributeValue(this.getAttributeValue());
    	clone.setSequenceNumber(this.getSequenceNumber());
    	clone.setOffset(this.getOffset());
    	clone.setLength(this.getLength());
    	clone.setRequiredTypeId(this.getRequiredTypeId());
    	return clone;
    }
	@Override
	public String toString() {
		return toString(id.getAttribute(),id.getFormId());
	}
	
	public Integer getRequiredTypeId() {
		return requiredTypeId;
	}
	
	public void setRequiredTypeId(Integer requiredTypeId) {
		this.requiredTypeId = requiredTypeId;
	}
	
	
	public PrintAttributeFormatRequiredType getRequiredType() {
		return PrintAttributeFormatRequiredType.getType(requiredTypeId == null ? 0 : requiredTypeId);
	}

	

}
