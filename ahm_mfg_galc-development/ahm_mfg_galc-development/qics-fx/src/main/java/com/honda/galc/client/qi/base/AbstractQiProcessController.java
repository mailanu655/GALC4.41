package com.honda.galc.client.qi.base;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.product.process.AbstractProcessController;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.MultiLineHelper;
/**
 * <h3>AbstractQiProcessController description</h3> <h4>Description</h4>
 * <p>
 * <code>AbstractQiProcessController</code> is model for Defect Entry Screen
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>26/11/2016</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
public abstract class AbstractQiProcessController<M extends QiProcessModel, V extends AbstractQiProcessView<?, ?>> extends AbstractProcessController<M, V> {
	
	protected String currentWorkingEntryDept = "";
	protected String currentWorkingProcessPointId = new String("");
	protected ClientAudioManager audioManager = null;
	public AbstractQiProcessController(M model,	V view) {
		super(model, view);
		//initialize working process point
		currentWorkingProcessPointId = getProcessPointId();
		setCurrentWorkingEntryDept(getApplicationContext().getEntryDept());
		try  {
			AudioPropertyBean property = PropertyService.getPropertyBean(AudioPropertyBean.class,
					getModel().getProcessPointId());
			setAudioManager(new ClientAudioManager(property));
		}
		catch(Exception ex)  {
			getLogger().error("Error creating audo manager");
		}
	}
	
	/**
	 * This method is used for exception handling.
	 */
	public void handleException(String loggerMsg, String errMsg, Exception e) {
		getLogger().error(e, new LogRecord(errMsg));
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.ERROR));
	}
	
	protected void setQicsStation()  {
		
		getModel().setCurrentWorkingProcessPointId(getProcessPointId());
		getModel().setCurrentWorkingEntryDept(getApplicationContext().getEntryDept());
		MultiLineHelper multiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
		ProcessPoint newPP = multiLineHelper.getProcessPointToUse(getProductModel().getProduct());
		//couldn't determine Qics station configuration, just use the current station
		if(newPP == null || newPP.getProcessPointId().trim().equalsIgnoreCase(getProcessPointId().trim()))  {
			return;
		}
		//currently, the design is only for 2 lines.  If not this station, it must be the other station
		setCurrentWorkingProcessPointId(newPP.getProcessPointId());
		setCurrentWorkingEntryDept(getApplicationContext().getEntryDept2());
		getModel().setCurrentWorkingProcessPointId(newPP.getProcessPointId());
		getModel().setCurrentWorkingEntryDept(getApplicationContext().getEntryDept2());
		
	}
	
	public boolean isMultiLine()  {
		return MultiLineHelper.getInstance(getProcessPointId()).isMultiLine();
	}
	
	public String getCurrentWorkingEntryDept() {
		return currentWorkingEntryDept;
	}

	public void setCurrentWorkingEntryDept(String currentWorkingEntryDept) {
		this.currentWorkingEntryDept = currentWorkingEntryDept;
	}

	public String getCurrentWorkingProcessPointId() {
		return currentWorkingProcessPointId;
	}

	public void setCurrentWorkingProcessPointId(String currentWorkingProcessPointId) {
		this.currentWorkingProcessPointId = currentWorkingProcessPointId;
	}

	public abstract void initializeListeners();
	/**
	 * This method will be used to read station configuration for cancel button.
	 * <br>
	 * If no configuration present then it will read default value.
	 * 
	 */
	public boolean isCancelBtnDisable() {
		boolean isCancelBtnDisable;
		QiStationConfiguration entryStation = getModel().findEntryStationConfigById("Disable Cancel Button");
		if (entryStation != null) {
			isCancelBtnDisable = "Yes".equalsIgnoreCase(entryStation.getPropertyValue()) ? true : false;
		} else {
			isCancelBtnDisable = QiEntryStationConfigurationSettings.DISABLE_CANCEL.getDefaultPropertyValue()
					.equalsIgnoreCase("Yes") ? true : false;
		}
		return isCancelBtnDisable;
	}
	
	public ApplicationContext getApplicationContext() {
		return getView().getMainWindow().getApplicationContext();
	}

	public ClientAudioManager getAudioManager() {
		return audioManager;
	}

	public void setAudioManager(ClientAudioManager audioManager) {
		this.audioManager = audioManager;
	}
	
	public QiProgressBar getQiProgressBar(String labelText, String processName) {
		QiProgressBar qiProgressBar = QiProgressBar.getInstance(labelText, processName, getModel().getProductId(),getView().getStage(),true);
		return qiProgressBar;
	}
}
