package com.honda.galc.client.qics.device;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.honda.galc.client.datacollection.DeviceDataDispatcher;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerListener;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.ProductId;

/**
 * 
 * <h3>QicsDeviceDataDispatcher</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsDeviceDataDispatcher description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Apr 27, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 27, 2011
 */

public class QicsDeviceDataDispatcher extends DeviceDataDispatcher implements DataContainerListener{

	private QicsFrame frame;
	
	public QicsDeviceDataDispatcher(QicsFrame frame) {
		super();

		this.frame = frame;
		registerListener();
	}

	public QicsDeviceDataDispatcher(QicsFrame frame, boolean isInit) {
		super(isInit);

		this.frame = frame;
		registerListener();
	}

	private void registerListener() {
		registerListener(EiDevice.NAME, this, getProcessData());
	}
	
	
	public DataContainer received(DataContainer dc) {
		Logger.getLogger().info("Receive data container from device:" + dc);
		frame.getIdlePanel().getProductNumberTextField().setText(dc.getString(DataContainerTag.PRODUCT_ID));
		frame.getIdlePanel().getProductNumberTextField().getAction().actionPerformed(null);
		dc.put(DataContainerTag.DATA_COLLECTION_COMPLETE, DataCollectionComplete.OK);
		return dc;
	}

	private void registerListener(String name,
			DeviceListener listener,
			ArrayList<IDeviceData> processData) {
		
		IDevice eiDevice = DeviceManager.getInstance().getDevice(name);
		if(eiDevice != null && eiDevice.isEnabled()){
			((EiDevice)eiDevice).registerDeviceListener(listener, processData);
		}
		
	}

	private ArrayList<IDeviceData>  getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new ProductId());
		list.add(new DataCollectionComplete());
		return list;
	}

	@Override
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		
		Logger.getLogger().info("Receive " + getReceivedLogString(deviceData) + " from device:" + clientId);
		if(deviceData instanceof ProductId){
			if (getFrame().isIdle()) {
				inputProductId(((ProductId)deviceData).getProductId());
				return DataCollectionComplete.OK();
			} else {
				String msg = "Can not process ProductId when not in Idle State.";
				Logger.getLogger().info(msg);
				getFrame().setErrorMessage(msg);
				return DataCollectionComplete.NG();
			}
		} else {
			return super.received(clientId, deviceData);
		}
	}
	
	private void inputProductId(final String productId) {
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run() {
					frame.getIdlePanel().getProductNumberTextField().setText(productId);
				    frame.getIdlePanel().getProductNumberTextField().getAction().actionPerformed(null);
				}
			}
		);
	}

	public QicsFrame getFrame() {
		return frame;
	}

	public void setFrame(QicsFrame frame) {
		this.frame = frame;
	}

}
