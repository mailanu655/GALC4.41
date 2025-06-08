package com.honda.galc.client.dc.enumtype;

import com.honda.galc.client.dc.action.IDataCollectionAction;
import com.honda.galc.client.dc.action.ManualMeasurementNGAction;
import com.honda.galc.client.dc.action.ManualMeasurementOKAction;
import com.honda.galc.client.dc.action.MeasurementInCompleteAction;
import com.honda.galc.client.dc.action.MeasurementNgAction;
import com.honda.galc.client.dc.action.MeasurementOkAction;
import com.honda.galc.client.dc.action.MeasurementReceivedAction;
import com.honda.galc.client.dc.action.MeasurementRejectAction;
import com.honda.galc.client.dc.action.MeasurementSkipAction;
import com.honda.galc.client.dc.action.OperationCompleteAction;
import com.honda.galc.client.dc.action.PartSnNgAction;
import com.honda.galc.client.dc.action.PartSnOkAction;
import com.honda.galc.client.dc.action.PartSnReceivedAction;
import com.honda.galc.client.dc.action.PartSnRejectAction;
import com.honda.galc.client.dc.action.PartSnSkipAction;
import com.honda.galc.client.dc.action.PddaConfirmAction;
import com.honda.galc.client.dc.action.PddaRejectAction;
import com.honda.galc.client.dc.action.WaitingForMeasurementAction;
import com.honda.galc.client.dc.action.WaitingForPartSnAction;

/**
 * @author Subu Kathiresan
 * @date April 15, 2014
 *
 */
public enum DataCollectionEventType {
	
	WAITING_FOR_PART_SN			(WaitingForPartSnAction.class),
	PART_SN_RECEIVED			(PartSnReceivedAction.class),
	PART_SN_OK					(PartSnOkAction.class),
	PART_SN_NG					(PartSnNgAction.class),
	PART_SN_SKIP				(PartSnSkipAction.class),
	PART_SN_REJECT				(PartSnRejectAction.class),

	WAITING_FOR_MEASUREMENT     (WaitingForMeasurementAction.class),
	MEASUREMENT_RECEIVED		(MeasurementReceivedAction.class),
	MEASUREMENT_OK				(MeasurementOkAction.class),
	MEASUREMENT_NG				(MeasurementNgAction.class),
	MEASUREMENT_SKIP			(MeasurementSkipAction.class),
	MEASUREMENT_REJECT			(MeasurementRejectAction.class),
	MANUAL_MEASUREMENT_OK	    (ManualMeasurementOKAction.class),  
	MANUAL_MEASUREMENT_NG	    (ManualMeasurementNGAction.class),
	OP_INCOMPLETE      			(MeasurementInCompleteAction.class), 
	
	PDDA_CONFIRM				(PddaConfirmAction.class),
	PDDA_REJECT				    (PddaRejectAction.class),
	
	//Event to mark an operation as completed without saving data to DB
	OP_COMPLETE					(OperationCompleteAction.class);	

	private Class<? extends IDataCollectionAction> actionClass; 
	
	private DataCollectionEventType(Class<? extends IDataCollectionAction> actionClass){
		this.actionClass = actionClass;
	}

	public Class<? extends IDataCollectionAction> getActionClass() {
		return actionClass;
	}
	
	public static boolean isValid(String eventName) {
		try {
			if (DataCollectionEventType.valueOf(eventName) != null) {
				return true;
			}
		} catch (Exception ex) {}
		return false;
	}
}
