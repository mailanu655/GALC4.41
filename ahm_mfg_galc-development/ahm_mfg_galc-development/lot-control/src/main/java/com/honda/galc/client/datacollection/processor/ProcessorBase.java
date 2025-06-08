package com.honda.galc.client.datacollection.processor;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.CellAction;
import com.honda.galc.client.common.LotControlAccessControlManager;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.CommonPropertyBean;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.CellBasedProductId;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SkippedProductId;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ProcessorBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProcessorBase description </p>
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
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
  /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public abstract class ProcessorBase {
	private static final String INVOKE_FSM = "INVOKE_FSM";
	protected ClientContext context;
	private String authorizedUser;
	
	
	public ProcessorBase(ClientContext context) {
		super();
		this.context = context;
	}

	public IDeviceData execute(Object processor, IDeviceData deviceData) {
		try {
			Method method = processor.getClass().getMethod("execute", deviceData.getClass());
			if(checkAndHandleCellAction(deviceData)) {
				boolean result = (Boolean)method.invoke(processor, deviceData);
				return result ? DataCollectionComplete.OK() : DataCollectionComplete.NG();
			}
		} catch (NoSuchMethodException e) {
			String userMsg = getUnexpectedResultMessage(deviceData);
			DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getFsm().error(new Message("CLIENT", userMsg));
			Logger.getLogger().error(e, userMsg);
		} catch (Throwable t){
			DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getFsm().error(new Message("CLIENT", t.getMessage())); 
			Logger.getLogger().error(t, t.getMessage());
		}
		
		return DataCollectionComplete.NG();
	}
	
	private boolean checkAndHandleCellAction(IDeviceData deviceData) {
		if(context.getCellAction() == CellAction.IGNORE) return true;
	
		if(deviceData instanceof ProductId && ((ProductId) deviceData).isInputData()) {
			ProductId productId = (ProductId)deviceData;
			if(shouldHandleCellOutForProduct(productId))
				return handleCellOutAction(productId);
			else if(shouldHandleCellInForProduct(productId)) 
				return handleCellInProduct(productId);
			else {
				StringBuilder sb = new StringBuilder("Ignored Product id: " + productId. getProductId());
				if (productId instanceof CellBasedProductId)
					sb.append(" from Cell " + ((CellBasedProductId)productId).getCellType());
				Logger.getLogger().info(sb.toString());
				return false;
			}
		}
		return true;
	}

	private boolean shouldHandleCellInForProduct(ProductId productId) {
		return isBetweenProducts() && (!(productId instanceof CellBasedProductId) || ((CellBasedProductId)productId).isCellInProduct());
	}

	private boolean shouldHandleCellOutForProduct(ProductId productId) {
		return isCollectingData() && (!(productId instanceof CellBasedProductId) || ((CellBasedProductId)productId).isCellOutProduct());
	}

	private boolean isBetweenProducts() {
		return getController().getStateName().equals("ProcessProduct");
	}

	private boolean isCollectingData() {
		return getController().getStateName().equals("ProcessTorque") ||
				getController().getStateName().equals("ProcessPart");
	}

	private boolean handleCellInProduct(ProductId productId) {
		StringBuilder sb = new StringBuilder("Received ProductId:").append(productId).append(" from device ").append(" in state of ProcessProduct.");
		boolean actionResult = true;
		switch(context.getCellAction()){
		
		case SKIP:
			sb.append(" CELL ACTION: SKIP ");
			if(productId.getProductId().equals(getController().getState().getExpectedProductId())){
				if(getController().getState().getStateBean().isPreviousProductSkppedByCellOut()) {
					sb.append(" The previoud product was skipped ");
					if(context.getProperty().isForceManualScanAfterSkippedByCellOut()) {
						sb.append(" need hand scan for the this product.");
						actionResult = false;
					} else {
						sb.append(" but configured to auto populate product after skipped by CellOut.");
					}
				} 
			}else if(isLastProductId(productId)){//silent ignore cell out
				sb.append(" ignore input VIN.");
				actionResult = false;
			}
			
			break;
		case CANCEL:
			sb.append(" CELL ACTION: CANCEL");
			break;
		}
		
		Logger.getLogger().info(sb.toString());
		return actionResult;
		
	}

	private boolean isLastProductId(ProductId productId) {
		try {
		return StringUtils.equals(productId.getProductId(), context.getCurrentViewManager().getView().getTextFieldLastPid().getText());
		} catch (Exception e) {
			return false;
		}
	}

	private boolean handleCellOutAction(ProductId productId) {
		StringBuilder sb = buildLogMessage(productId);
		boolean actionResult = true;
		
		if (!getController().getState().getProductId().equals(productId.getProductId())) {
			//This product id is not for the current in process product id
			Logger.getLogger().info(sb.toString());
			return false;
		}
		
		switch(context.getCellAction()){
		
		case CANCEL:
			sb.append(" CELL ACTION: CANCEL");
			getController().getFsm().cancel();
			actionResult = false;
			break;
		case SKIP:
			sb.append(" CELL ACTION: SKIP");
			EventBus.publish(new SkippedProduct(new SkippedProductId(
					getController().getState().getProductId(), context.getProcessPointId())));
			
			getController().getState().getProduct().setSkippedByCellOut(true); //force to save next expected for cell out
			getController().getFsm().skipProduct();
			actionResult = false;
			break;
		default:
			break;
		}
		
		Logger.getLogger().info(sb.toString());
		return actionResult;
	}

	private StringBuilder buildLogMessage(ProductId prodId) {
		StringBuilder sb = new StringBuilder("Received Product Id:").append(prodId.getProductId());
		sb.append(" at the state: ").append(getController().getInstance().getStateName());
		sb.append(" when process product id:").append(getController().getState().getProductId());
		return sb;
	}

	public String getUnexpectedResultMessage(IDeviceData deviceData){
		return "Unexpected " + deviceData.getClass().getSimpleName() + " received in " + DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getStateName() + " state" ;
	}
	
	public IDeviceData processReceived(IDeviceData deviceData) {
		return execute(this, deviceData);
	}

	protected void registerListener(String deviceName, DeviceListener listener, List<IDeviceData> dataList) {
		IDevice eiDevice = DeviceManager.getInstance().getDevice(deviceName);
		if(eiDevice != null && eiDevice.isEnabled()){
			((EiDevice)eiDevice).registerDeviceListener(listener, dataList);
		}
	}
	
	public <T extends ProductSpec> ProductSpec findProductSpec(List<T> productSpecs,
			String productSpecCode) {
		for(ProductSpec spec: productSpecs){
			if(productSpecCode.equals(spec.getProductSpecCode()))
				return spec;
		}
		
		return null;
	}
	
	public synchronized boolean execute(ProductId productId) {
		return true;
	}

	public synchronized boolean execute(CellBasedProductId productId) {
		return execute((ProductId)productId);
	}
	
	/**
	 * Invoke Fsm state change in a separate thread to improve performance
	 * 
	 * @param methodName
	 * @param object
	 */
	protected void invokeFsmAsync(final String methodName, final Object object){
		Thread t = new Thread(){
			public void run() {
				try {
					Method method = DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getFsm().getClass().getMethod(
							methodName,	new Class[] { object.getClass() });
					
					method.invoke(DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getFsm(), new Object[]{object});
				} catch (Exception e) {
					e.printStackTrace();
					DataCollectionController.getInstance(context.getAppContext().getApplicationId().trim()).getFsm().error(new Message(INVOKE_FSM, e.getMessage()));
				}
			}
		};
		
		t.start();
	}
	
	protected boolean login() {
		if(LotControlAccessControlManager.getInstance().login()){
			authorizedUser = LotControlAccessControlManager.getInstance().getAuthorizedUser();
			Logger.getLogger().info("User:", authorizedUser, " logged in.");
			return true;
		} else {
			authorizedUser = null;
			return false;
		}
	}
	
	protected String getUserId() {
		return authorizedUser == null ? context.getUserId() : authorizedUser;
		
	}
	
	public DataCollectionController getController() {
		return DataCollectionController.getInstance(context.getAppContext().getApplicationId());
	}
	
	protected CommonPropertyBean getCommonPropertyBean() {
		return PropertyService.getPropertyBean(CommonPropertyBean.class);
	}
	
	protected FrameLinePropertyBean getFrameLinePropertyBean() {
		return PropertyService.getPropertyBean(FrameLinePropertyBean.class, context.getAppContext().getApplicationId());
	}
}
