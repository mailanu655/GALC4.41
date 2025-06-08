package com.honda.galc.client.qics.view.frame;


import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.qics.device.QicsDeviceManager;
import com.honda.galc.client.qics.view.screen.LpdcIdlePanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.device.dataformat.DataCollectionComplete12;
import com.honda.galc.device.dataformat.DataCollectionComplete34;
import com.honda.galc.entity.conf.Application;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>QicsLpdcFrame</code>
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Pankaj Gopal</TD>
 * <TD>Apr 14, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Pankaj Gopal
 */

public class QicsLpdcFrame extends QicsFrame{

	private static final long serialVersionUID = 1L;
	private boolean processsingMultipleProduct = false;
	private LpdcIdlePanel idlePanel;
	private DataContainer dataContainer = new DefaultDataContainer();
	
	public QicsLpdcFrame(ApplicationContext appContext, Application application) {
		super(appContext,application);
	}	

	@Override
	protected void initDeviceManager() {
		
		if(isPlcSupported()){
			deviceManager = new QicsDeviceManager(this);
			registerDeviceListener();
		}
	}
	
	private void registerDeviceListener() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice != null && eiDevice.isEnabled()){
			eiDevice.registerDataContainerListener(getLpdcIdlePanel());
		}
	}

	public LpdcIdlePanel getLpdcIdlePanel() {
		return idlePanel;
	}


	@Override
	public void startFrame() {
		idlePanel = new LpdcIdlePanel(this);

		displayIdleView();

//		getDisplayPanel().add(getCurrentPanel(), "Center");
		getCurrentPanel().requestFocus();
	}

	@Override
	public void displayIdleView() {
		displayView(getLpdcIdlePanel());
	}

	@Override
	public boolean isUserLoggedIn() {
		return true;
	}

	@Override
	public void updateUserLogedin() {
	}

	@Override
	public boolean isIdle() {
		return true;
	}

	@Override
	public void resetMenuForCurrentScreen(QicsPanel panel) {
		getSwitchUserMenu().setEnabled(true);
		getCloseMenu().setEnabled(true);
		getExitMenu().setEnabled(true);
	}


	public void createDataCollectionCompleteDataContainer() {
		createDataCollectionCompleteDataContainer(1);
		createDataCollectionCompleteDataContainer(2);
		createDataCollectionCompleteDataContainer(3);
		createDataCollectionCompleteDataContainer(4);
	}

	@Override
	public void createDataCollectionCompleteDataContainer(int id) {

		dataContainer.put(DataContainerTag.DATA_COLLECTION_COMPLETE + id, LineSideContainerValue.COMPLETE);
		try {
			processDataCollectionComplete(id);
		} catch (Exception e) {
			getLogger().warn(e, " Exception to process data collection complete:" + id);
		}

	}

	protected void processDataCollectionComplete(int id) {
		
		if(!isPlcSupported()) return;
		
		if(id == 2 || id == 4) id--;
		String str1 = dataContainer.getString(DataContainerTag.DATA_COLLECTION_COMPLETE + id);
		String str2 = dataContainer.getString(DataContainerTag.DATA_COLLECTION_COMPLETE + id + 1);

		if(!StringUtils.isEmpty(str1) && !StringUtils.isEmpty(str2)) {
			if(id == 1) getDeviceManager().send(DataCollectionComplete12.OK());
			else getDeviceManager().send(DataCollectionComplete34.OK());
			dataContainer.remove(DataContainerTag.DATA_COLLECTION_COMPLETE + id);
			dataContainer.remove(DataContainerTag.DATA_COLLECTION_COMPLETE + id + 1);
		}
	}
	
	
	
	public boolean isProcesssingMultipleProduct() {
		return processsingMultipleProduct;
	}
	
	public void setProcesssingMultipleProduct(boolean processsingMultipleProduct) {
		this.processsingMultipleProduct = processsingMultipleProduct;
	}

	public void clearMessage(){
		if(!processsingMultipleProduct)
			this.getStatusMessagePanel().setErrorMessageArea(null);
	}


}
