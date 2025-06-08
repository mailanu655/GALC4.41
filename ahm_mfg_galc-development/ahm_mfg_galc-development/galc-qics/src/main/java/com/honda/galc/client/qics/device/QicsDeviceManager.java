package com.honda.galc.client.qics.device;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.observer.DeviceManagerBase;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.ProductDeviceData;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;

/**
 * 
 * <h3>QicsDeviceManager</h3> <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * QicsDeviceManager description
 * </p>
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

public class QicsDeviceManager extends DeviceManagerBase {

	QicsFrame frame;

	public QicsDeviceManager(QicsFrame frame) {
		super();
		this.frame = frame;
		if (getEiDevice().containOutputDeviceData(DataCollectionComplete.class)) {
			getEiDevice().reqisterDeviceData(new DataCollectionComplete());
		}
		if (getEiDevice().containOutputDeviceData(ProductDeviceData.class)) {
			getEiDevice().reqisterDeviceData(new ProductDeviceData());
		}
	}

	@Override
	protected void handleException(IDeviceData deviceData, Throwable e) {
		String msg = "Failed to send " + deviceData.getClass().getSimpleName() + " to Ei Device.";
		Logger.getLogger().error(e, msg);
		frame.setErrorMessage(msg);
	}

	public void send(IDeviceData deviceData) {
		getEiDevice().send(deviceData);
	}

	@Override
	public void sendDataCollectionComplete() {
		super.sendDataCollectionComplete();
		if (getEiDevice().containOutputDeviceData(ProductDeviceData.class)) {
			if (DeviceDataConverter.getInstance().getDeviceDataObject(new ProductDeviceData()) != null) {
				ProductDeviceData deviceData = assembleProductDeviceData(getProduct());
				sendDeviceData(deviceData);
			}
		}
	}

	private BaseProduct getProduct() {
		//get updated product from database for device reply
		return getFrame().getQicsController().findProduct(getFrame().getQicsController().getProductModel().getProduct().getProductId());
	}

	protected ProductDeviceData assembleProductDeviceData(BaseProduct product) {
		ProductDeviceData deviceData = new ProductDeviceData();
		deviceData.setDataCollectionComplete(DataCollectionComplete.OK);
		deviceData.setProductId(product.getProductId());
		deviceData.setDefectStatus(product.getDefectStatusValue() == null ? "" : product.getDefectStatusValue().toString());
		deviceData.setRepairedFlag(product.getDefectStatusValue() == null || product.isRepairedStatus());
		deviceData.setHoldStatus(product.getHoldStatus());
		deviceData.setProductSpecCode(StringUtils.trimToEmpty(product.getProductSpecCode()));
		assembleProductSpecCodeData(product, deviceData);
		return deviceData;
	}

	protected void assembleProductSpecCodeData(BaseProduct product, ProductDeviceData deviceData) {
		if (product instanceof Product) {
			assembleProductSpecCodeData((Product) product, deviceData);
		} else if (product instanceof DieCast) {
			assembleProductSpecCodeData((DieCast) product, deviceData);
		}
	}

	protected void assembleProductSpecCodeData(Product product, ProductDeviceData deviceData) {
		String productSpecCode = deviceData.getProductSpecCode();
		deviceData.setModelYearCode(ProductSpec.extractModelYearCode(productSpecCode));
		deviceData.setModelCode(ProductSpec.extractModelCode(productSpecCode));
		deviceData.setModelTypeCode(ProductSpec.extractModelTypeCode(productSpecCode));
		deviceData.setModelOptionCode(ProductSpec.extractModelOptionCode(productSpecCode));
		StringBuilder ymCode = new StringBuilder();
		ymCode.append(deviceData.getModelYearCode());
		ymCode.append(deviceData.getModelCode());
		deviceData.setModelYearModelCode(ymCode.toString());
	}

	protected void assembleProductSpecCodeData(DieCast product, ProductDeviceData deviceData) {
		deviceData.setModelCode(StringUtils.trimToEmpty(product.getModelCode()));
	}

	// === get/set === //
	protected QicsFrame getFrame() {
		return frame;
	}

	protected void setFrame(QicsFrame frame) {
		this.frame = frame;
	}
}
