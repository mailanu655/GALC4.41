package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceDataType;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class DeviceDataFormatForm extends ActionForm

{

	private static final long serialVersionUID = 1L;
	private String operation;
	private String clientID;
	private String newClientID;

	private int[] length;
	private int[] offset;
	private int[] sequenceNumber;
	private int[] tagType;
	private int[] dataType;
	private String[] tagValue;
	private String[] tagName;
	private String[] tag;

	private Map<String,String> deviceTagTypes; 

	private String clone;

	private boolean deleteConfirmed = false;


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

	private boolean editor;

	/**
	 * @return Returns the newClientID.
	 */
	public String getNewClientID() {
		return newClientID;
	}
	/**
	 * @param newClientID The newClientID to set.
	 */
	public void setNewClientID(String newClientID) {
		this.newClientID = newClientID;
	}

	public Map<String,String> getDataTypes() {

		Map<String,String> dataTypes = new HashMap<String,String>();

		for(DeviceDataType type: DeviceDataType.values()) 
			dataTypes.put(Integer.toString(type.getId()), type.toString());
		return dataTypes;
	}
	/**
	 * @return Returns the clientID.
	 */
	public String getClientID() {
		return clientID;
	}
	/**
	 * @param clientID The clientID to set.
	 */
	public void setClientID(String clientID) {
		this.clientID = clientID;
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
	public void setSequenceNumber(int[] sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	/**
	 * @return Returns the tag.
	 */
	public String[] getTag() {
		return tag;
	}
	/**
	 * @param tag The tag to set.
	 */
	public void setTag(String[] tag) {
		this.tag = tag;
	}
	/**
	 * @return Returns the tagType.
	 */
	public int[] getTagType() {
		return tagType;
	}
	/**
	 * @param tagType The tagType to set.
	 */
	public void setTagType(int[] tagType) {
		this.tagType = tagType;
	}

	public Map<String, String> getDeviceTagTypes() {
		return deviceTagTypes;
	}
	public void setDeviceTagTypes(Map<String, String> deviceTagTypes) {
		this.deviceTagTypes = deviceTagTypes;
	}

	public int[] getDataType() {
		return dataType;
	}

	public void setDataType(int[] dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return Returns the tagValue.
	 */
	public String[] getTagValue() {
		return tagValue;
	}
	/**
	 * @param tagValue The tagValue to set.
	 */
	public void setTagValue(String[] tagValue) {
		this.tagValue = tagValue;
	}

	/**
	 * @return Returns the tagName.
	 */
	public String[] getTagName() {
		return tagName;
	}
	/**
	 * @param tagName The tagName to set.
	 */
	public void setTagName(String[] tagName) {
		this.tagName = tagName;
	}

	public List<DeviceFormat> getData() {
		List<DeviceFormat> data = new ArrayList<DeviceFormat>();

		if (length != null && length.length >0) {
			DeviceFormat item = null;
			for (int i=0; i<length.length; i++) {
				item = new DeviceFormat(clientID,tag[i]);
				item.setSequenceNumber(sequenceNumber[i]);
				item.setTagType(tagType[i]);
				item.setDataType(dataType[i]);
				item.setTagValue(tagValue[i]);
				item.setTagName(tagName[i]);
				item.setLength(length[i]);
				item.setOffset(offset[i]);
				data.add(item);
			}
		}
		return data;
	}

	public void setData(List<DeviceFormat> data) {
		if (data != null && data.size() > 0) {
			DeviceFormat item = null;
			//initialize the data size
			int size = data.size();
			length = new int[size];
			offset = new int[size];
			sequenceNumber = new int[size];
			tagType = new int[size];
			dataType = new int[size];
			tagValue = new String[size];
			tagName = new String[size];
			tag = new String[size];
			//copy the data from the list to form
			for(int i=0; i<size; i++) {
				item = data.get(i);
				clientID = item.getId().getClientId();
				sequenceNumber[i] = item.getSequenceNumber();
				tag[i] = item.getId().getTag();
				tagType[i] = item.getTagType();
				dataType[i] = item.getDataType();
				tagValue[i] = item.getTagValue();
				tagName[i] = item.getTagName();
				length[i] = item.getLength();
				offset[i] = item.getOffset();
			}
		}
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		operation = null;
		clientID = null;
		length = null;
		offset = null;
		sequenceNumber = null;
		tagType = null;
		dataType = null;
		tagValue = null;
		tagName = null;
		tag = null;
		newClientID = null;
		editor = false;
		clone = null;
		//tagTypes are constants, should not be reset to null
		//tagTypes = null;
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
}
