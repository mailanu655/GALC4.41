package com.honda.galc.client.datacollection.observer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.PlcForceLotControlPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.IDeviceDataInput;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.PlcDataString;
import com.honda.galc.entity.MeasurementList;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * <h3>PlcForceDeviceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PlcForceDeviceManager description </p>
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
 * @author Paul Chou
 * Mar.5, 2019
 *
 */
public class PlcForceDeviceManager extends LotControlJobTorqueManager {
	protected String productId;
	private PlcForceLotControlPropertyBean plcPropertyBean;
	

	public PlcForceDeviceManager(ClientContext context) {
		super(context);
	}

	public IDeviceData processDeviceData(IDeviceDataInput deviceData) {
		try {
			doProcessDeviceData(deviceData);
			return DataCollectionComplete.OK();
		} catch (Exception e) {
			Logger.getLogger().error(e.getCause());
		}
		return DataCollectionComplete.NG();

	}

	protected void doProcessDeviceData(IDeviceDataInput deviceData) {
		if(deviceData instanceof PlcDataString) {
			PlcDataString plcData = (PlcDataString)deviceData;
			String str = plcData.getPlcDataString();
			Logger.getLogger().info("PLC data string received:" + str);
			if(str.length() < 3) {
				Logger.getLogger().warn("Invalid Message:", str, " received for Givens Engineering protocol.");
			}

			if(str.length() > context.getProductType().getProductIdLength()) {
				processForcesPackage(str);
			}
		}
	}

	protected void processForcesPackage(String str) {
		productId = str.substring(0, ProductType.FRAME.getProductIdLength());
		MeasurementList mlist = new MeasurementList();
		int currentPosition = ProductType.FRAME.getProductIdLength() +1;
		int torqueIndex = 1;
		int torqueDataLength = getPlcPropertyBean().getTorqueDataLength();//length in input data package
		try {
			while ((str.length() - currentPosition) >= torqueDataLength) {
				Measurement measurement = new Measurement(productId, DataCollectionController.getInstance().getState().getCurrentPartName(), torqueIndex);
				measurement.setMeasurementStatus(MeasurementStatus.OK);//default to OK
				double force = Double.parseDouble(StringUtils.trim(str.substring(currentPosition, currentPosition + torqueDataLength)));
				measurement.setMeasurementValue(force/Math.pow(10, getPlcPropertyBean().getForceDecimalPosition()));

				mlist.getMeasurements().add(measurement);
				currentPosition += (torqueDataLength+1);
				torqueIndex++;
			} 
		} catch (Exception e) {
			Logger.getLogger().error("Failed to parse force data frpm PLC:" + e.getCause());

		}

		//now post to process data
		DataCollectionController.getInstance().received(mlist);
	}

	public PlcForceLotControlPropertyBean getPlcPropertyBean() {
		if(plcPropertyBean == null)
			plcPropertyBean = PropertyService.getPropertyBean(PlcForceLotControlPropertyBean.class, context.getProcessPointId());
		return plcPropertyBean;
	}

	public static void main(String[] args) {
		String a = "2HGFC2F87KH508654 0001000";
		PlcForceDeviceManager aa = new PlcForceDeviceManager(null);
		aa.processForcesPackage(a);
	}
	

}
