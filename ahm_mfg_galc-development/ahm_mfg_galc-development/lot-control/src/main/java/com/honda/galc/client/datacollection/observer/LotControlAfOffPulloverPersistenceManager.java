package com.honda.galc.client.datacollection.observer;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.entity.enumtype.StrategyType;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
/**
 * 
 * <h3>LotControlAfOffPersistenceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlAfOffPersistenceManager description </p>
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
 * <TD>Jul 15, 2019</TD>
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
 * @since Jul 15, 2019
 */
public class LotControlAfOffPulloverPersistenceManager extends LotControlFramePersistenceManager {

	private boolean isPulloverProduct = false;
	public LotControlAfOffPulloverPersistenceManager(ClientContext context) {
		super(context);
		
	}
	
	public void saveCompleteData(ProcessProduct state) {
		isPulloverProduct = isPulloverProduct(state);
		super.saveCompleteData(state);
	}
	
	public boolean isPulloverProduct(ProcessProduct state) {
		if(StringUtils.isEmpty(context.getFrameLinePropertyBean().getPulloverProcessPointId())) return false;
		return !ServiceFactory.getDao(ProductResultDao.class).findAllByProductAndProcessPoint(state.getProductId(), context.getFrameLinePropertyBean().getPulloverProcessPointId()).isEmpty();
	}

	protected void trackProduct(ProcessProduct state) {
		if(isPulloverProduct) {
			Logger.getLogger().info("process pullover product:", state.getProductId(), " no tracking.");
			return; 
		}
		
		List<String> pullOverProducts = state.getStateBean().getPullOverProductList();
		if(pullOverProducts !=null && pullOverProducts.size() > 0) {
			for(String vin : pullOverProducts) {
				ProductHistory pulloverHistory = ProductTypeUtil.createProductHistory(vin, context.getFrameLinePropertyBean().getPulloverProcessPointId(), context.getProductType());
				track(vin, pulloverHistory);
				
				ProductHistory offHistory = ProductTypeUtil.createProductHistory(vin, context.getProcessPointId(), context.getProductType());
				if(!vin.equals(state.getProductId())) //current product will be tracked later; avoid double track.
					track(vin, offHistory);
			}
		} 
		
		super.trackProduct(state);
	}

	private void track(String vin, ProductHistory history) {
		if (history != null) {
			history.setAssociateNo(context.getUserId());
			getTrackingService().track(context.getProductType(), history);
			Logger.getLogger().info(" track pull over product:", vin, " at process point:", history.getProcessPointId(), " completed.");
		} else {
			Logger.getLogger().warn("WARN:", " failed to track pull over product:", vin);
		}
	}
	
	public void initPart(ProcessPart state) {
		super.initPart(state);

		if(isConfirmEngine(state))
			retrieveEngineAndSendToProcessor(state);
		
	}

	private boolean isConfirmEngine(ProcessPart state) {
		if(StringUtils.isEmpty(context.getProperty().getEnginePartName()))
				return false;
			
		return StrategyType.ENGINE_VIN_CONFIRMATION == StrategyType.valueOf(state.getCurrentLotControlRule().getStrategy()); 
	}

	private void retrieveEngineAndSendToProcessor(ProcessPart state) {
		String enginePartName=getProperty().getEnginePartName();
		InstalledPart installedEngine = ServiceFactory.getDao(InstalledPartDao.class).findByKey(new InstalledPartId(state.getProductId(), enginePartName));
		
		if(installedEngine != null && isEngineLoaded(state)) {
			sendPartSnRequest(installedEngine.getPartSerialNumber());
		}else if(installedEngine == null) {
			sendPartSnRequest(((Frame)state.getProduct().getBaseProduct()).getEngineSerialNo());
		    Logger.getLogger().warn("Warn: failed to find installed Engine.");
		} else {
			sendPartSnRequest("");
			state.message(new Message("","Failed to find Engine associated with vehicle.", MessageType.ERROR));
		    Logger.getLogger().error("Error: Failed to find Engine associated with vehicle.");
		}
			
	}

	private boolean isEngineLoaded(ProcessPart state) {
		return !StringUtils.isEmpty(((Frame)state.getProduct().getBaseProduct()).getEngineSerialNo());
	}
	
}
