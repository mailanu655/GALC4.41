package com.honda.galc.client.datacollection.strategy;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.processor.IDataCollectionTaskProcessor;
import com.honda.galc.client.datacollection.processor.PartSnProcessorDuplicateCheckLocal;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.openprotocol.model.LastTighteningResult;

/**
 * 
 * <h3>PartSerialNumberDuplicateCheck</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartSerialNumberDuplicateCheck description </p>
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
 * <TD>David Jensen</TD>
 * <TD>07/24/13</TD>
 * <TD>0</TD>
 * <TD>Other</TD>
 * <TD>Fix Duplicate Part Serial # Check using new strategy, 'PartSerialNumberDupCheckValidation'.
 *     This fix will prevent the entry of the same part serial #s into consecutive text boxes.</TD>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Oct 17, 2013</TD>
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
 * @since Oct 17, 2013
 */
public class PartSerialNumberDuplicateValidation implements IDataCollectionTaskProcessor<InputData>{
	private DataCollectionController controller;
	private ClientContext context;

	public PartSerialNumberDuplicateValidation(ClientContext context) {
		super();
		this.context = context;
		controller = DataCollectionController.getInstance();
	}

	public boolean execute(InputData data) {
		IDeviceData result = processReceived(data); 
		return ((DataCollectionComplete)result).isOk();
	}

	public void init() {
	}

	@SuppressWarnings("unchecked")
	public IDeviceData processReceived(IDeviceData data) {
	
		boolean result = true;
		if(data instanceof PartSerialNumber){
			
			result = getProcessor(PartSnProcessorDuplicateCheckLocal.class).execute(data);
		    
		} else if(data instanceof LastTighteningResult)
			result = getProcessor(ProcessTorque.class).execute(data);
		else 
			Logger.getLogger().warn("unexpected data received:" + data.getClass().getSimpleName() + data.toString());
	
		return result ? DataCollectionComplete.OK() : DataCollectionComplete.NG();		
	}
	


	@SuppressWarnings("unchecked")
	private IDataCollectionTaskProcessor getProcessor(Class<?> claz) {
		 IDataCollectionTaskProcessor processor = controller.getProcessor(claz);
		 if(processor == null){
			 processor = getController().createProcessor(claz.getName());
			 getController().getProcessors().put(claz, processor);
		 }
		 
		 processor.init();
		 return processor;
	}

	public void registerDeviceListener(DeviceListener listener) {
		// TODO Auto-generated method stub
	}

	public DataCollectionController getController() {
		return controller;
	}

	public ClientContext getContext() {
		return context;
	}



}
