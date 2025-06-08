package com.honda.galc.client.datacollection.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.etcher.ILecLaserDeviceListener;
import com.honda.galc.client.device.etcher.LecLaserSocketDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.constant.DeviceMessageSeverity;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.service.printing.AttributeConvertor;
import com.honda.galc.service.printing.PrintAttributeConvertor;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.GalcStation;

/**
 * @author Subu Kathiresan, Alex Johnson
 * @date Apr 19, 2017
 */
public class LecLaserEtcherProcessor extends ProductIdProcessor implements ILecLaserDeviceListener {

	private static final String lineSeparator = System.getProperty("line.separator"); 
	
	private volatile boolean etchingInProgress = false;
	private volatile String etchingProduct = null;
	private LecLaserSocketDevice device;
	public Logger logger;
	
	public LecLaserEtcherProcessor(ClientContext context) {
		super(context);
	}

	@Override
	public synchronized boolean execute(ProductId productId) {
		if (!super.execute(productId)) return false;

		try {
			getLogger().info("Retrieving laser commands for product " + product.getProductId());
			String[] commandsList = getCommands(product.getProductId());
			if (commandsList.length > 0) {
				getDevice().setCommands(commandsList);
				if (GalcStation.isPingable(getDevice().getHostName())) {
					performEtching(productId);
				} else {
					generateDeviceNotReadyWarning();
					return false;
				}
			} else {
				handleDeviceStatusChange("No plate configured for " + product.getProductId(), DeviceMessageSeverity.info);
			}
		} catch(Exception ex) {
			getLogger().error(ex, "Laser etching failed. " + StringUtils.trimToEmpty(ex.getMessage()));
			getController().getFsm().error(new Message(ex.getMessage()));
			return false;
		}
		return true;
	}

	private void generateDeviceNotReadyWarning() {
		getLogger().warn("Unable to connect to device: " + getDevice().getHostName());
		handleDeviceStatusChange("Etcher is not ready. Please check the connection and retry.", DeviceMessageSeverity.error);
	}

	private void performEtching(ProductId productId) {
		if (!isEtchingInProgress()) {
			etchingProduct = productId.getProductId();
			getDevice().startEtch(etchingProduct);
			handleDeviceStatusChange("Initializing etcher for " + etchingProduct, DeviceMessageSeverity.info);
			monitorEtchProcess();
		} else {
			Message message = new Message("Etching " + etchingProduct + " in progress. Please try again after etching is completed.");
			getController().getFsm().error(message);
		}
	}

	private LecLaserSocketDevice getDevice() {
		if (device == null) {
			for(IDevice iDevice : DeviceManager.getInstance().getDevices().values()){
				if(iDevice instanceof LecLaserSocketDevice && iDevice.isEnabled()) {
					device = (LecLaserSocketDevice) iDevice;
					device.registerListener(this);
					device.activate();
					break;
				}
			}
		}
		return device;
	}
	
	private String[] getCommands(String productId) {
		String formId = getDevice().getFormId();
		BuildAttribute bAttrib = context.getBuildAttribute(getProductSpecCode(productId), formId);
		String templateName = bAttrib.getAttributeValue();

		DataContainer dc = createDataContainer(productId, formId, templateName);
		String[] commandsList = {};
		String printData = getPrintData(dc);
		if (StringUtils.trimToNull(printData) != null) {
			commandsList = getPrintData(dc).split(lineSeparator);
		} 		
		return commandsList;
	}

	private DataContainer createDataContainer(String productId, String formId, String templateName) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.FORM_ID, formId);
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, getProductSpecCode(productId));
		dc.put(DataContainerTag.PRODUCT_ID, productId);
		dc.put(DataContainerTag.TEMPLATE_NAME, templateName);
		
		AttributeConvertor convertor = new AttributeConvertor(getLogger());
		convertor.convertFromPrintAttribute(formId, dc);
		
		return dc;
	}

	private String getPrintData(DataContainer dc) {
		Map<String, String> map = new HashMap<String, String>();
		map.putAll(DataContainerUtil.getAttributeMap(dc));
		dc.put(DataContainerTag.KEY_VALUE_PAIR, map);
		String commandList = PrintAttributeConvertor.getPrintData(dc);
		getLogger().info("Laser commands list   : " + commandList);
		return commandList;
	}

	private void monitorEtchProcess() {
		Runnable etchMonitorRunnable = new Runnable() {
			public void run() {
				etchingInProgress = true;
				long expirationTime = System.currentTimeMillis() + (getDevice().getTimeoutInSecs() * 1000); 
				while ((System.currentTimeMillis() < expirationTime)) {
					try {
						if (!isEtchingInProgress()) {
							return;
						} else {
							Thread.sleep(10);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				setEtchingInProgress(false);
				getController().getFsm().error(new Message("Laser etcher did not finish etching product: "  + product.getProductId()));
			}
		};

		Thread monitor = new Thread(etchMonitorRunnable);
		monitor.start();
	}

	public void etchCompleted(String deviceId, String productId) {
		setEtchingInProgress(false);
		String msg = "completed etching " + productId;
		handleDeviceStatusChange(msg, DeviceMessageSeverity.success);
	}

	public void handleDeviceStatusChange(String msg, DeviceMessageSeverity severity) {
		Message displayMsg = new Message(this.getClass().getSimpleName(), msg);
		switch (severity){
		case success:
		case info:
			displayMsg.setType(MessageType.INFO);
			getController().getFsm().message(displayMsg);
			getLogger().info(msg);
			break;
		case error:
			getController().getFsm().error(displayMsg);
			getLogger().error(msg);
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected BaseProductSpec findProductSpec(String productSpec) {
		@SuppressWarnings("rawtypes")
		BaseProductSpecDao dao = ProductTypeUtil.getProductSpecDao(context.getProperty().getProductType());
		return dao.findByKey(getProductSpecCode(product.getProductId()));
	}
  	
	public Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger(context.getAppContext().getTerminalId());
			logger.getLogContext().setApplicationInfoNeeded(true);
			logger.getLogContext().setMultipleLine(false);
			logger.getLogContext().setCenterLog(false);
		}
		logger.getLogContext().setThreadName(getTerminalId() + "-" + Thread.currentThread().getName());
		return logger;
	}
	
  	public String getTerminalId() {
  		return context.getAppContext().getTerminalId();
  	}
  	
	public String getApplicationName() {
		return context.getAppContext().getApplicationId();
	}
	
	public boolean isEtchingInProgress() {
		return etchingInProgress;
	}
	
	public void setEtchingInProgress(boolean etchingInProgress) {
		this.etchingInProgress = etchingInProgress;
	}

	public String getProductSpecCode(String productId) {
		return product.getProductSpec();
	}

	public String getId() {
		return this.getClass().getSimpleName();
	}
	
	public Integer getDeviceAccessKey(String deviceId) {
		return null;
	}

	public void controlGranted(String deviceId) {}

	public void controlRevoked(String deviceId) {}
}
