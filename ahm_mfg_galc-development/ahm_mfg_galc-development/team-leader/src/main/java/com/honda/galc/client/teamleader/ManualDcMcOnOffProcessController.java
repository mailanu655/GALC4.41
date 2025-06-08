package com.honda.galc.client.teamleader;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClipPlayManager;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.net.Request;
import com.honda.galc.notification.service.IProductReceivedNotification;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
/**
 * 
 * <h3>ManualDcMcOnOffProcessController</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualDcMcOnOffProcessController description </p>
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
 * <TD>hat0926</TD>
 * <TD>Mar 20, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Prasanna Parvathaneni
 * @since Jan 31 2017
 */
@SuppressWarnings("serial")
public class ManualDcMcOnOffProcessController extends
		ManualDcMcOnOffView implements IProductReceivedNotification{
	private static final String ON = "ON"; 
	private static final String OFF = "OFF"; 
	private static final String DC = "DC"; 
	//, DeviceListener 
	public ManualDcMcOnOffProcessController(ApplicationContext appContext, Application application) {
		super(appContext, application);
	}
	
	@SuppressWarnings({ "unchecked" })
	protected void initData() {
		super.initData();
		// populate custom data
		getProcessTypeComboBox().setModel(new DefaultComboBoxModel(getPropertyBean().getProcess()));
		getProcessPointComboBox().setModel(new DefaultComboBoxModel(getPropertyBean().getOnProcessPoints()));
		getNoneHomeProductCheckBox().setVisible(false);
		// Check above and get corresponding property entry for process point
		if (Arrays.asList(getPropertyBean().getProcess()).get(0).equals(ON))
				initProcessPoints(getOnProcessPoints(), getPropertyBean().getOnProcessPoints(), ON);
		else if (Arrays.asList(getPropertyBean().getProcess()).get(0).equals(OFF))
			initProcessPoints(getOffProcessPoints(), getPropertyBean().getOffProcessPoints(), OFF);
	}
	protected void initState() {
		initSelection(getProductTypeComboBox());

		// Display the configured properties as read only
		getProcessPointComboBox().setEditable(false);
		getProcessTypeComboBox().setEditable(false);
		getProductTypeComboBox().setEditable(false);
		getDeptComboBox().setEditable(false);
		getModelCodeComboBox().setEditable(false);
		
		TextFieldState.DISABLED.setState(getMcNumberTextField());
		TextFieldState.DISABLED.setState(getDcNumberTextField());
		
		getProcessPointComboBox().setEnabled(false);
		getProcessTypeComboBox().setEnabled(false);
		getProductTypeComboBox().setEnabled(false);
		getDeptComboBox().setEnabled(false);
		getModelCodeComboBox().setEnabled(false);

	}

			// === Actionlistener === //
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == getProductIdTextField()) {
				String department = (String) getDeptComboBox().getSelectedItem();
				String processType = (String) getProcessTypeComboBox().getSelectedItem();
				 if (DC.equals(department) && ON.equals(processType)) {
					 //
				 } else {
					 TextFieldState.EDIT.setState(getMcNumberTextField());
				 }
				processProductIdInput();
			} else {
				super.actionPerformed(e);
			}
		} catch (TaskException ex) {
			Logger.getLogger().error(ex);
			setErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			Logger.getLogger().error(ex);
			setErrorMessage("Exception occured: \n" + ex.getMessage());
		}
	}
	protected boolean processOn() {
		boolean success = super.processOn();
		return success;
	}
	
	protected boolean processOff() {
		boolean success = false;
		try {
			ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
			String productId = getProductIdTextField().getText();
			setProduct(findProduct(getDcNumberTextField(), productType, NumberType.DC, productId));
			if(getProduct() == null) {
				Logger.getLogger().error("Product " + productId + " does not exist");
				setErrorMessage("Product " + productId + " does not exist");
			}else {
				ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBox().getSelectedItem();
				ServiceFactory.getService(TrackingService.class).track(productType, productId, processPoint.getProcessPointId());
				success = true;
			}
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception uccured when executing Tracking");
			setErrorMessage("Exception uccured when executing Tracking: \n" + e);
		}
		if (success) {	
			// send stopper release signal
			doBroadcast();
			resetScreen();
		}
		return success;
	}

	protected void enableFields() {
		TextFieldState.EDIT.setState(getMcNumberTextField());
		TextFieldState.EDIT.setState(getDcNumberTextField());
		getModelCodeComboBox().setEnabled(true);
		getModelCodeComboBox().setEditable(true);
	}
	
	protected void enableDCFields() {
		TextFieldState.EDIT.setState(getDcNumberTextField());
		getModelCodeComboBox().setEnabled(false);
		getModelCodeComboBox().setEditable(false);
	}
	/**
	 * notification about last passed product at a process Point
	 * @param event
	 * @param request
	 */
	@EventTopicSubscriber(topic="IProductReceivedNotification")
	public void onProductReceivedEvent(String event, Request request) {
        try {
			request.invoke(this);
		} catch (SecurityException e) {
			e.printStackTrace();
			playSound(false);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			playSound(false);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			playSound(false);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			playSound(false);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			playSound(false);
		}catch (Exception e) {
			e.printStackTrace();
			playSound(false);
		}
   }
	
	public void execute(String productId, String mcNumber, String message, String detail) {
		if (StringUtils.isBlank(message)|| StringUtils.equalsIgnoreCase(message.trim(), LineSideContainerValue.CHECK_NG)) {
			getMcNumberTextField().setText(StringUtils.trim(mcNumber));
			getDcNumberTextField().setText(StringUtils.trim(productId));
			getDcNumberTextField().selectAll();
			getDcNumberTextField().requestFocus();
			if (StringUtils.isNotBlank(detail)) 
				setErrorMessage(detail);
			else
				setErrorMessage("Data Processing error");
			enableFields();
		} else {
			initState();
			clearMessage();	
		}
	}
	// DC
	public void execute(String productId, String message, String detail) {
		if (StringUtils.isBlank(message) || StringUtils.equalsIgnoreCase(message.trim(), LineSideContainerValue.CHECK_NG)) {
			
			getDcNumberTextField().setText(StringUtils.trim(productId));
			getDcNumberTextField().selectAll();
			getDcNumberTextField().requestFocus();
			if (StringUtils.isNotBlank(detail)) 
				setErrorMessage(detail);
			else
				setErrorMessage("Data Processing error");
			enableDCFields();
		} else {
			initState();
			clearMessage();	
		}
	}
	private void playSound(boolean isOk) {
		try {
			if(isOk)ClipPlayManager.playOkSound();
			else ClipPlayManager.playNgSound();
		} catch (Exception e) {
			getLogger().error(e,"Could not play sound");
		}
	}
	private void doBroadcast() {
		String broadcastDestinations = getPropertyBean().getStopperRelease();
		String processPointId = getApplicationContext().getProcessPointId();
		if (broadcastDestinations != null && broadcastDestinations != "") {
			try {
				Logger.getLogger().info("Beginning broadcast from ManualDCMC Controller");
				
				List<String> broadcastDestinationList = java.util.Arrays.asList(broadcastDestinations.split(","));
				
				DataContainer dataContainer = new DefaultDataContainer();
				dataContainer.put(DataContainerTag.DATA_COLLECTION_COMPLETE, LineSideContainerValue.COMPLETE);
				dataContainer.put(DataContainerTag.MODEL_CODE, getProduct().getModelCode());
				dataContainer.put(DataContainerTag.PRODUCT_ID, getProduct().getProductId());
				dataContainer.put("MC_SERIAL_NUMBER", getProduct().getMcSerialNumber());
				dataContainer.put("CC_SERIAL_NUMBER", getProduct().getDcSerialNumber());
	
				for(BroadcastDestination destination: ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(processPointId, false)) {
					if (broadcastDestinationList.contains(destination.getDestinationId())) {
						Logger.getLogger().info(String.format("Broadcasting %s at %s", destination.getDestinationId(), processPointId));
						ServiceFactory.getService(BroadcastService.class).broadcast(destination, processPointId, dataContainer);
					}
				}

			} catch (Exception e) {
				setErrorMessage(e.toString());
				Logger.getLogger().error(e, "Failed to broadcast from ManualDCMC Controller: " + e.toString());			
			} finally {
				Logger.getLogger().info("Completed broadcast from ManualDCMC Controller");
			}
		}
	}

	
}
