package com.honda.galc.client.datacollection.observer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.task.AsyncTaskExecutorService;
/**
 * 
 * <h3>LotControlVQShipPersistentManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlVQShipPersistentManager description </p>
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
 * <TD>Apr 17, 2019</TD>
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
 * @since Apr 17, 2019
 */
public class LotControlVQShipPersistentManager extends LotControlPartLotPersistenceManager{
    private FrameLinePropertyBean frameLinePropertyBean;
	private boolean keyNoExist = false;
	private static final String OIF_SHIP_TRANSACTION_TASK = "OIF_SHIP_TRANSACTION_TASK";


	public LotControlVQShipPersistentManager(ClientContext context) {
		super(context);
	}
	
	public void initPart(ProcessPart state) {
		super.initPart(state);

		if(isProcessKeyNo(state))
			retrieveKeyNoAndSendToProcessor(state);
		
	}

	private void retrieveKeyNoAndSendToProcessor(ProcessPart state) {
		keyNoExist = false;
		InstalledPart keyNo = ServiceFactory.getDao(InstalledPartDao.class).findByKey(new InstalledPartId(state.getProductId(), state.getCurrentPartName()));
		
		//keyNo == null should never happen - guaranteed by required part check
		if(keyNo != null) {
			sendPartSnRequest(keyNo.getPartSerialNumber());
			keyNoExist  = true;
		}else {
			state.message(new Message("","Failed to find installed Key No", MessageType.ERROR));
		    Logger.getLogger().error("Error: failed to find Key No.");
		}
			
	}
	
	private boolean isProcessKeyNo(ProcessPart state) {
		if(StringUtils.isEmpty(getFrameLinePropertyBean().getKeyNoPartName())) 
			return false;
		
		return getFrameLinePropertyBean().getKeyNoPartName().contains(state.getCurrentPartName());
	}

	public FrameLinePropertyBean getFrameLinePropertyBean() {
		if(frameLinePropertyBean == null)
			frameLinePropertyBean = PropertyService.getPropertyBean(FrameLinePropertyBean.class, context.getProcessPointId());
		return frameLinePropertyBean;
	}
	
	protected <T extends DataCollectionState> List<InstalledPart> prepareForSave(T state) {
		List<InstalledPart> saveList = super.prepareForSave(state);
		if(keyNoExist) {//don't save Key No again.
			Iterator<InstalledPart> iterator = saveList.iterator();
			while(iterator.hasNext()) {
	          
	            if (getFrameLinePropertyBean().getKeyNoPartName().equals(iterator.next().getId().getPartName())) {
	               iterator.remove();
	               break;
	            }
	         }
			
		}
		return saveList;
	}
	
	public void saveCompleteData(ProcessProduct state) {
		super.saveCompleteData(state);

		if(getFrameLinePropertyBean().isInvokeShippingTransactionService())	{
			Map<String, String> clientArgs = new HashMap<String, String>();
			
			if(!StringUtils.isEmpty(getSendLocation()))
				clientArgs.put(TagNames.SEND_LOCATION.name(), getSendLocation());
			
			getAsyncTaskService().execute(OIF_SHIP_TRANSACTION_TASK, clientArgs, null, "");
		}
	}

	private String getSendLocation() {
		FrameLinePropertyBean propertyByTerminal = PropertyService.getPropertyBean(FrameLinePropertyBean.class, context.getAppContext().getTerminalId());
		return propertyByTerminal.getSendLocation();
	}

	private AsyncTaskExecutorService getAsyncTaskService() {
		return ServiceFactory.getService(AsyncTaskExecutorService.class);
		
	}
}