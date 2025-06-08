package com.honda.galc.client.engine.aeon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.EngineBlock;
import com.honda.galc.device.dataformat.MCNumber;
import com.honda.galc.entity.enumtype.BlockLoadStatus;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>BlockLoadController Class description</h3>
 * <p> BlockLoadController description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 26, 2014
 *
 *
 */
public class EngineNumberingController implements ActionListener, DeviceListener{
	private EngineNumberingModel model;
	private EngineNumberingView view;
	
	private final ClientAudioManager audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
	
	private Exception blockException = null;
	private Exception engineException = null;
	private Block block = null;
	private Engine engine = null;
	private List<BlockLoad>  blockLoads = new ArrayList<BlockLoad>();
	
	
	public EngineNumberingController(EngineNumberingModel model,EngineNumberingView view) {
		this.model = model;
		this.view = view;
		registerEiDeviceListener();
	}

	
	public void actionPerformed(ActionEvent e) {
		clearMessage();
		if(e.getSource() == view.refreshButton) refresh();
		else if(e.getSource() == view.mcButton) sendMCNumberToPLC();
	}
	
	private void registerEiDeviceListener() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice != null && eiDevice.isEnabled()){
			eiDevice.registerDeviceListener(this, getDeviceInputDataList());
			DeviceDataConverter.getInstance().registerOutputDeviceData(getDeviceOutputDataList());
		}
	}
	
	private void refresh() {
		view.loadData();
	}
	
	private void sendMCNumberToPLC() {
		BlockLoad blockLoad = view.blockTablePane.getSelectedItem();
		if(blockLoad == null) {
			setErrorMessage("Please select MC number");
			return;
		}
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		try{
			eiDevice.syncSend(new MCNumber(blockLoad.getMcNumber()));
			setMessage("Sent block mc number " + blockLoad.getMcNumber() + " successfully");
		}catch (Exception ex){
			setErrorMessage("Could not send MC number to PLC due to : " + ex.getMessage());
		}
	}

	
	private List<IDeviceData> getDeviceInputDataList() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		//make sure MCNumber is before EngineBlock (with same tag name in both)
		//DeviceDataConverter's bug
		list.add(new MCNumber());
		list.add(new EngineBlock());
		return list;
	}
	
	private List<IDeviceData> getDeviceOutputDataList() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new MCNumber());
		return list;
	}

	public IDeviceData received(String clientId, IDeviceData deviceData) {
		clearMessage();
		if(deviceData instanceof EngineBlock) 
			return processEngineReceived((EngineBlock)deviceData);
		else if(deviceData instanceof MCNumber)
			return processMCNumberReceived((MCNumber)deviceData);
		
		return null;
		
	}
		
	private IDeviceData processEngineReceived(final EngineBlock engineBlock) {
		
		blockException = null;
		engineException = null;
		block = null;
		engine = null;
		BlockLoad blockLoad = null;
		
		getLogger().info("Received engine number and block number: " + engineBlock.getEin() + ", " + engineBlock.getMcNumber());
		view.receivedProductIdField.getComponent().setText(engineBlock.getEin());
		
		try{
			block = model.findBlock(engineBlock.getMcNumber());
			blockLoad = validateBlock(block);
			getLogger().info("block " + block.getMcSerialNumber() + " is validated");
		}catch (Exception ex) {
			blockException = ex;
			getLogger().error("Invalid block " + engineBlock.getMcNumber() + " due to:" + ex.getMessage());
		}
		
		try{
			engine = model.findEngine(engineBlock.getEin());
			validateEngine(engine,block,blockLoad);
			getLogger().info("Engine " + engineBlock.getEin() + " is validated ");
		}catch(Exception ex) {
			engineException = ex;
			getLogger().error("Invalid engine " + engineBlock.getEin() + " due to:" + ex.getMessage());
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				processEngineBlock(engineBlock);
				refreshData(1000);
			}
		});
		
		return new DataCollectionComplete(engineException == null && blockException == null);
	};
	
	private void processEngineBlock(EngineBlock engineBlock){
		if(block == null){
			showErrorDialg(blockException);
			return;
		}else {
			if(blockException != null ) {
				if(!confirm(blockException)) return;
			};	
		}
		
		if(engine == null){
			showErrorDialg(engineException);
			return;
		}else {
			if(engineException != null ) {
				if(!confirm(engineException)) return;
			};	
		}
		
		try{
			model.moveInstalledPartsToEngine(engineBlock.getEin(), block.getMcSerialNumber());
			model.saveInstalledPart(engineBlock.getEin(), block);
			model.updateBlockLoadStatus(block.getMcSerialNumber(), BlockLoadStatus.STAMPED);
			PreProductionLot preProductionLot = model.updateStampCount(engine);
			if(preProductionLot != null && preProductionLot.getStampedCount() == 1) {
				ShippingVanningSchedule schedule = model.createShippingVanningSchedule(preProductionLot);
				if(schedule != null) {
					getLogger().info("Created Shipping Vanning Schedule " + schedule);
				}else {
					// a new lot passed AE ON but no vanning schedule is created. This situation most likes does not happen
				}
			}
			model.updateExpectedProduct(engineBlock.getEin());
			model.invokeTracking(engineBlock.getEin());
			if(preProductionLot != null) model.issueProductOnNotification(preProductionLot);
			setMessage("Processed engine " + engineBlock.getEin() + " and block " + block.getMcSerialNumber() + " successfully");
		}catch(Exception ex) {
			showErrorDialg(ex);
		}
	}
	
	

	private boolean confirm(String message) {
		getLogger().error(message);
		delay(700);
		audioManager.playRepeatedNgSound();
		int result = JOptionPane.showOptionDialog(view.getMainWindow(),message + "\n Do you want to continue?" , "error" , JOptionPane.YES_NO_OPTION,
				JOptionPane.ERROR_MESSAGE, null, null, null);
		boolean aFlag = JOptionPane.YES_OPTION == result;
	 	audioManager.stopRepeatNgSound();
		return aFlag;
	}
	
	private boolean confirm(Exception ex) {
		return confirm(ex.getMessage());
	}
	
	private void showErrorDialg(Exception ex) {
		getLogger().error(ex.getMessage());
		delay(300);
		audioManager.playRepeatedNgSound();
	   	MessageDialog.showError(view.getMainWindow(), ex.getMessage() + "\n Please Check");
	   	audioManager.stopRepeatNgSound();
	}
	
	private IDeviceData processMCNumberReceived(MCNumber mcNumber) {
		getLogger().info("Received block number: " + mcNumber.getMcNumber());
		boolean aFlag = true;
		BlockLoad blockLoad = null;
		Engine nextEngine = null;
		Engine expectedEngine = null;
		

		try{
			Block block = model.findBlock(mcNumber.getMcNumber());
			
			blockLoad = validateBlock(block);
			
			blockLoad.setStatus(BlockLoadStatus.PRE_STAMP);
			model.updateBlockLoad(blockLoad);
			
			blockLoads = model.getLoadedBlocks();
			SwingUtilities.invokeLater(new Runnable() {
				public void run(){
					view.refreshBlockLoadList(blockLoads);
				}
			});

			expectedEngine = model.getExpectedEngine();
			if(expectedEngine != null) 
				nextEngine = model.findNextProduct(expectedEngine.getProductId());

			setMessage("Received MC Number " + mcNumber.getMcNumber() + " is valid");
		}catch(Exception ex){
			setErrorMessage("MC Number " + mcNumber.getMcNumber() + " Received is invalid: " + ex.getMessage());
			aFlag = false;
			return new DataCollectionComplete(aFlag);
		}
		
		if(expectedEngine == null) {
			String msg = "No expected Engine found";
			aFlag = confirm(msg);
			return new DataCollectionComplete(aFlag);
		}
		
        int status = model.checkBlock(expectedEngine, blockLoad);
        
        //blockLoad matches expectedEngine -> OK
        if(status == 1) return new DataCollectionComplete(true);
        
    	// now status > 1
        // blockLoad does not match expectedEngine
        // now check if blockLoad matches next Engine
    	int status2 = model.checkBlock(nextEngine, blockLoad);
    	
   		//check previous block which is supposed to match ExpectedEngine
		BlockLoad previouseBlockLoad = model.findPreviousBlock(blockLoads, blockLoad);

    	if(status2  == 1) {
    		//block matches nextEngine 
     		
    		if(previouseBlockLoad == null) {
    			aFlag = confirm(blockLoad,expectedEngine, status);
    		}else {
    		    
    			if(!previouseBlockLoad.getStatus().equals(BlockLoadStatus.PRE_STAMP)) {
        			// previouseBlockLoad is not PRE_STAMPed 
					String msg = "Block " + previouseBlockLoad.getMcNumber() + " may be at wrong position!\n" + "Please check!";
					aFlag = confirm(msg);
    			} else {
    			
        			int status3 = model.checkBlock(expectedEngine, previouseBlockLoad);
        		
        			aFlag = confirm(previouseBlockLoad,expectedEngine, status3);
    			}
    				
    		}
         }else {
        	 // blockLoad does not match bother expectedEngine and nextEngine
        		if(previouseBlockLoad != null && 
        		   previouseBlockLoad.getStatus().equals(BlockLoadStatus.PRE_STAMP) && 
        		   model.checkBlock(expectedEngine, previouseBlockLoad) == 1) {
        			
        			//confirm blockLoad with nextEngine
        			aFlag = confirm(blockLoad, nextEngine,status2);
        			
        		}else {
        			
        			//confirm blockLoad with expectedEngine
        			aFlag = confirm(blockLoad, expectedEngine, status);
        			
        		}
         }
		
		return new DataCollectionComplete(aFlag);
		
	}
	
	private boolean confirm(BlockLoad blockLoad, Engine engine, int status) {
		boolean aFlag = true;
		
		if(status == 2) {
			
			// production lot does not match
			Block block1 = model.findBlock(blockLoad.getMcNumber());
			try {
				model.checkBlockModelType(engine, block1);
			}catch (Exception ex) {
				// block model type does not match engine 
				showErrorDialg(ex);
				return false;
			}
			String msg = "Block " + blockLoad.getMcNumber() + "'s production lot " + blockLoad.getProductionLot() + " does not match \n" + 
					" engine " + engine.getProductId() + " 's production lot " + engine.getProductionLot();
			aFlag = confirm(msg);
		}else if(status == 3) {
			String msg = "Block " + blockLoad.getMcNumber() + "'s reference number " + blockLoad.getReferenceNumber() + " does not match \n" + 
					" engine " + engine.getProductId() + " 's lot position " + engine.getLotPosition();
			aFlag = confirm(msg);
		}
		
		return aFlag;
	}
	
	private void refreshData(final int delay) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				loadData();
			}
		},
		delay);
	}
	
	private void loadData() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				view.loadData();
			}
		});
	}
	
	public BlockLoad validateBlock(Block block) {
		BlockLoad blockLoad = model.checkBlockLoaded(block);
		model.checkBlockInstalled(block);
		return blockLoad;
	}
	
	public void validateEngine(Engine engine,Block block,BlockLoad blockLoad) {
		model.checkEngineStamped(engine);
		
		model.checkBlockModelType(engine, block);
		model.matchNextExpectedEngine(engine.getProductId());
	}
	
	
	
	private void setErrorMessage(String message) {
		displayErrorMessage(message);
		getLogger().error(message);
	}
	
	private void setMessage(String message) {
		displayMessage(message);
		getLogger().info(message);
	}
	
	private void displayMessage(String message) {
		view.getMainWindow().setMessage(message);
	}
	
	private void displayErrorMessage(String message) {
		view.getMainWindow().setErrorMessage(message);
	}

	private void clearMessage() {
		view.getMainWindow().clearMessage();
	}
	
	private Logger getLogger() {
		return view.getLogger();
	}
	
	private void delay(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
