package com.honda.galc.system.config.web.forms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.OpcConfigEntry;

/**
 * Form bean for a Struts application.
 * @version 	1.0
 * @author
 */
public class OpcSettingsForm extends ActionForm
{
	private static final long serialVersionUID = 1L;
	private long id = -1;
	private String dataReadyTag = null;
	private String opcInstanceName = null;
	private boolean enabled = false;
	private String processPointID = null;
	private String deviceID = null;
	private String replyDeviceID = null;
	private String replyDataReadyTag = null;
	private String serverURL = null;
	private int serverClientType = 1;
	private int serverClientMode = 1;
	private String listenerClass = null;
	private boolean needsListener = false;
	private short readSource = 1;
	private String processCompleteTag = null;
	
	private String load = null;
	private String apply = null;
	private String delete = null;
	private String selectDevice = null;
	
	private boolean newInstance = false;
	private boolean editor = false;
	
	boolean deleteConfirmed = false;
	
	private List<Device> deviceList = null;
	
    public void reset(ActionMapping mapping, HttpServletRequest request) {

	// Reset field values here.

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

	public String getDataReadyTag() {
		return dataReadyTag;
	}

	public void setDataReadyTag(String dataReadyTag) {
		this.dataReadyTag = dataReadyTag;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getListenerClass() {
		return listenerClass;
	}

	public void setListenerClass(String listenerClass) {
		this.listenerClass = listenerClass;
	}

	public String getOpcInstanceName() {
		return opcInstanceName;
	}

	public void setOpcInstanceName(String opcInstanceName) {
		this.opcInstanceName = opcInstanceName;
	}

	public String getProcessPointID() {
		return processPointID;
	}

	public void setProcessPointID(String processPointID) {
		this.processPointID = processPointID;
	}

	public String getReplyDataReadyTag() {
		return replyDataReadyTag;
	}

	public void setReplyDataReadyTag(String replyDataReadyTag) {
		this.replyDataReadyTag = replyDataReadyTag;
	}

	public String getReplyDeviceID() {
		return replyDeviceID;
	}

	public void setReplyDeviceID(String replyDeviceID) {
		this.replyDeviceID = replyDeviceID;
	}

	public int getServerClientType() {
		return serverClientType;
	}

	public void setServerClientType(int serverClientType) {
		this.serverClientType = serverClientType;
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public boolean isEditor() {
		return editor;
	}

	public void setEditor(boolean editor) {
		this.editor = editor;
	}

	public boolean isNewInstance() {
		return newInstance;
	}

	public void setNewInstance(boolean newInstance) {
		this.newInstance = newInstance;
	}
	
	public void setOpcConfigurationData(OpcConfigEntry data,Device device) {
		setDataReadyTag(data.getDataReadyTag());
		setDeviceID(data.getDeviceId());
		setEnabled(data.getEnabled()== 1);
		setId(data.getId());
		setListenerClass(data.getListenerClass());
		setOpcInstanceName(data.getOpcInstanceName());
		setProcessPointID((device!=null) ? device.getIoProcessPointId() : "");
		setReplyDataReadyTag(data.getReplyDataReadyTag());
		setReplyDeviceID((device!=null)? device.getReplyClientId() : "");
		setServerClientType(data.getServerClientTypeId());
		setServerClientMode(data.getServerClientModeId());
		setServerURL(data.getServerUrl());
		setNeedsListener(data.getNeedsListener() == 1);
		setReadSource(data.getReadSourceId());
		setProcessCompleteTag(data.getProcessCompleteTag());
		
		
	}
	
	public OpcConfigEntry getOpcConfigurationData() {
		OpcConfigEntry data = new OpcConfigEntry();
		data.setDataReadyTag(getDataReadyTag());
		data.setDeviceId(getDeviceID());
		data.setEnabled(isEnabled()? 1: 0);
		data.setId(getId());
		data.setListenerClass(getListenerClass());
		data.setOpcInstanceName(getOpcInstanceName());
		data.setReplyDataReadyTag(getReplyDataReadyTag());
		data.setServerClientTypeId(getServerClientType());
		data.setServerClientModeId(getServerClientMode());
		data.setServerUrl(getServerURL());
		data.setNeedsListener(isNeedsListener()? 1: 0);
		data.setReadSourceId(getReadSource());
		data.setProcessCompleteTag(getProcessCompleteTag());
		return data;
		
	}

	public boolean isDeleteConfirmed() {
		return deleteConfirmed;
	}

	public void setDeleteConfirmed(boolean deleteConfirmed) {
		this.deleteConfirmed = deleteConfirmed;
	}

	public String getLoad() {
		return load;
	}

	public void setLoad(String load) {
		this.load = load;
	}

	public boolean isNeedsListener() {
		return needsListener;
	}

	public void setNeedsListener(boolean needsListener) {
		this.needsListener = needsListener;
	}

	public int getServerClientMode() {
		return serverClientMode;
	}

	public void setServerClientMode(int serverClientMode) {
		this.serverClientMode = serverClientMode;
	}

	public short getReadSource() {
		return readSource;
	}

	public void setReadSource(short readSource) {
		this.readSource = readSource;
	}

	public String getSelectDevice() {
		return selectDevice;
	}

	public void setSelectDevice(String selectDevice) {
		this.selectDevice = selectDevice;
	}

	public List<Device> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}

	public String getProcessCompleteTag() {
		return processCompleteTag;
	}

	public void setProcessCompleteTag(String processCompleteTag) {
		this.processCompleteTag = processCompleteTag;
	}
	
}
