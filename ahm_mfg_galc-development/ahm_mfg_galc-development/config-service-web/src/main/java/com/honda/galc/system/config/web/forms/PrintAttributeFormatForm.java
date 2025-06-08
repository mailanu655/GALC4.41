package com.honda.galc.system.config.web.forms;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.honda.galc.data.PrintAttribute;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.enumtype.PrintAttributeType;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.printing.PrintAttributeServiceUtil;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
/**
 * @version 2.0
 * @author Gangadhararao Gadde
 * @date Sept 20, 2016
 */
public class PrintAttributeFormatForm extends ActionForm

{
    
    private static final long serialVersionUID = 1L;
	private String operation;
    private String formID;
    private String newFormID;

	private int[] length;
	private int[] offset;
	private int[] sequenceNumber;
	private int[] attributeTypeId;
	private String[] attributeValue;
	private String[] attribute;
	private Integer[] requiredTypeId;
	
	private Map requiredTypes;
	
	private Map attributeTypes; 

	private boolean editor;
	
	private String clone;
	
	private boolean deleteConfirmed = false;
	
	private int changedAttributeTypeIndex = -1;
	
	private List<LabelValueBean> forms = Collections.emptyList();
	
    /**
     * @return Returns the deleteConfirmed.
     */
    public boolean isDeleteConfirmed() {
        return deleteConfirmed;
    }
    /**
     * @param deleteConfirmed The deleteConfirmed to set.
     */
    public void setDeleteConfirmed(boolean deleteConfirmed) {
        this.deleteConfirmed = deleteConfirmed;
    }
    
    public int getChangedAttributeTypeIndex() {
		return changedAttributeTypeIndex;
	}
	public void setChangedAttributeTypeIndex(int changedAttributeTypeIndex) {
		this.changedAttributeTypeIndex = changedAttributeTypeIndex;
	}
	/**
     * @return Returns the clone.
     */
    public String getClone() {
        return clone;
    }
    /**
     * @param clone The clone to set.
     */
    public void setClone(String clone) {
        this.clone = clone;
    }
    /**
     * @return Returns the editor.
     */
    public boolean isEditor() {
        return editor;
    }
    /**
     * @param editor The editor to set.
     */
    public void setEditor(boolean editor) {
        this.editor = editor;
    }
    /**
     * @return Returns the attribute.
     */
    public String[] getAttribute() {
        return attribute;
    }
    /**
     * @param attribute The attribute to set.
     */
    public void setAttribute(String[] attribute) {
        this.attribute = attribute;
    }
    /**
     * @return Returns the attributeType.
     */
    public int[] getAttributeTypeId() {
        return attributeTypeId;
    }
    /**
     * @param attributeType The attributeType to set.
     */
    public void setAttributeTypeId(int[] attributeType) {
        this.attributeTypeId = attributeType;
    }
    /**
     * @return Returns the attributeTypes.
     */
    public Map getAttributeTypes() {
        return attributeTypes;
    }
    /**
     * @param attributeTypes The attributeTypes to set.
     */
    public void setAttributeTypes(Map attributeTypes) {
        this.attributeTypes = attributeTypes;
    }
    /**
     * @return Returns the attributeValue.
     */
    public String[] getAttributeValue() {
        return attributeValue;
    }
    /**
     * @param attributeValue The attributeValue to set.
     */
    public void setAttributeValue(String[] attributeValue) {
        this.attributeValue = attributeValue;
    }
    /**
     * @return Returns the formID.
     */
    public String getFormID() {
        return formID;
    }
    /**
     * @param formID The formID to set.
     */
    public void setFormID(String formID) {
        this.formID = formID;
    }
    /**
     * @return Returns the length.
     */
    public int[] getLength() {
        return length;
    }
    /**
     * @param length The length to set.
     */
    public void setLength(int[] length) {
        this.length = length;
    }
    /**
     * @return Returns the newFormID.
     */
    public String getNewFormID() {
        return newFormID;
    }
    /**
     * @param newFormID The newFormID to set.
     */
    public void setNewFormID(String newFormID) {
        this.newFormID = newFormID;
    }
    /**
     * @return Returns the offset.
     */
    public int[] getOffset() {
        return offset;
    }
    /**
     * @param offset The offset to set.
     */
    public void setOffset(int[] offset) {
        this.offset = offset;
    }
    /**
     * @return Returns the operation.
     */
    public String getOperation() {
        return operation;
    }
    /**
     * @param operation The operation to set.
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
    /**
     * @return Returns the sequence.
     */
    public int[] getSequenceNumber() {
        return sequenceNumber;
    }
    /**
     * @param sequence The sequence to set.
     */
    public void setSequenceNumber(int[] sequence) {
        this.sequenceNumber = sequence;
    }
    
    public List<PrintAttributeFormat> getData() {
        
    	 List<PrintAttributeFormat> items = null;
        
        if (length != null && length.length >0) {
        	items = new ArrayList<PrintAttributeFormat>();
	        for (int i=0; i<length.length; i++) {
		        PrintAttributeFormat item = new PrintAttributeFormat(formID,attribute[i]);
		        item.setSequenceNumber(sequenceNumber[i]);
		        item.setAttributeTypeId(attributeTypeId[i]);
		        item.setAttributeMethods(getAttributeMethods(attributeTypeId[i]));
		        item.setAttributeValue(attributeValue[i]);
		        item.setLength(length[i]);
		        item.setOffset(offset[i]);
		        item.setRequiredTypeId(requiredTypeId[i]);
		        items.add(item);
            }
        }
        return items;
    	
    }
    
	@SuppressWarnings("unchecked")
	private Map<String,String> getAttributeMethods(int attributeTypeId) {
       	PrintAttributeType type = PrintAttributeType.getType(attributeTypeId);
       	if(type == null)return new TreeMap<String,String>();
       	if(type.equals(PrintAttributeType.AttributeByProduct)) 
   		return findAttributes(Product.class);
   		else if(type.equals(PrintAttributeType.AttributeByEngine))
   			return findAttributes(Engine.class);
   		else if(type.equals(PrintAttributeType.AttributeByFrame))
   			return findAttributes(Frame.class);
   		else if(type.equals(PrintAttributeType.AttributeByEngineSpec))
   			return findAttributes(EngineSpec.class);
   		else if(type.equals(PrintAttributeType.AttributeByFrameSpec))
   			return findAttributes(FrameSpec.class);
   		else if(type.equals(PrintAttributeType.AttributeByProductionLot))
   			return findAttributes(ProductionLot.class);
   		else if(type.equals(PrintAttributeType.AttributeByPartSpec))
       		return findAttributes(PartSpec.class);
   		else if(type.equals(PrintAttributeType.AttributeByService))
   			return findAttributes(PrintAttributeServiceUtil.class);
   		else return new TreeMap<String,String>();
    }
       
    private Map findAttributes(Class<?> attributeClass) {
       	Map<String,String> map = new TreeMap<String,String>();
       	map.put("", "");
       	Method[] methods = attributeClass.getMethods();
       	for(Method method : methods) {
       		Annotation annotation = method.getAnnotation(PrintAttribute.class);
       		if(annotation != null) {
       			map.put(method.getName(), method.getName());
       		}
       	}
       	return map;
    }
       
    public void setData(List<PrintAttributeFormat> items) {
    	
    	if (items != null && items.size() > 0) {
            //initialize the data size
            int size = items.size();
            length = new int[size];
            offset = new int[size];
            sequenceNumber = new int[size];
            attributeTypeId = new int[size];
            attributeValue = new String[size];
            attribute = new String[size];
            requiredTypeId = new Integer[size];
            //copy the data from the list to form
            int i =0;
            for( PrintAttributeFormat item : items) {
                formID = item.getId().getFormId();
                sequenceNumber[i] = item.getSequenceNumber();
                attribute[i] = item.getAttribute();
                attributeTypeId[i] = item.getAttributeTypeId();
                attributeValue[i] = item.getAttributeValue();
                length[i] = item.getLength();
                offset[i] = item.getOffset();
                requiredTypeId[i]=item.getRequiredTypeId();
                i++;
            }
        }
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        operation = null;
        formID = null;
        length = null;
        offset = null;
        sequenceNumber = null;
        attributeTypeId = null;
        requiredTypeId = null;
        attributeValue = null;
        attribute = null;
        newFormID = null;
        clone = null;
        //attributeTypes are constants, should not be reset to null
        //attributeTypes = null;
        deleteConfirmed = false;
    }
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        // Validate the fields in your form, adding
        // adding each error to this.errors as found, e.g.

        // if ((field == null) || (field.length() == 0)) {
        //   errors.add("field", new org.apache.struts.action.ActionError("error.field.required"));
        // }
        return errors;

    }
	/**
	 * @return the forms
	 */
	public List<LabelValueBean> getForms() {
		return forms;
	}
	/**
	 * @param forms the forms to set
	 */
	public void setForms(List<LabelValueBean> forms) {
		this.forms = forms;
	}
	
	
	public Integer[] getRequiredTypeId() {
		return requiredTypeId;
	}
	
	public void setRequiredTypeId(Integer[] requiredTypeId) {
		this.requiredTypeId = requiredTypeId;
	}
	
	public Map getRequiredTypes() {
		return requiredTypes;
	}
	
	public void setRequiredTypes(Map requiredTypes) {
		this.requiredTypes = requiredTypes;
	}
	
}
