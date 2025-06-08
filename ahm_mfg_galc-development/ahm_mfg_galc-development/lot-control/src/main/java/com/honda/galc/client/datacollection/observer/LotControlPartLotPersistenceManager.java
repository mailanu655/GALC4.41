package com.honda.galc.client.datacollection.observer;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PartLotDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.PartLotStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartLot;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>LotControlPartLotPersistenceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlPartLotPersistenceManager description </p>
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
 * <TD>Jan 31, 2012</TD>
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
 * @since Jan 31, 2012
 */
public class LotControlPartLotPersistenceManager extends
LotControlPersistenceManager {
	
	public LotControlPartLotPersistenceManager(ClientContext context) {
		super(context);
	}
	
	
	@Override
	public void saveCompleteData(ProcessProduct state) {
		super.saveCompleteData(state);
		savePartLot(state);
		
	}
	
	public void savePartLot(ProcessProduct state) {
		try{

			if(state.getProduct() == null /*skip product before product id input*/ || 
					(isSkippedProduct(state) && !context.getProperty().isSaveBuildResultsForSkippedProduct())) return; 
			
			LotControlRule rule;
			List<InstalledPart> partList = state.getProduct().getPartList();
			for(int i = 0; i < partList.size(); i++) {
				rule = getLotControlRule(state, i);
				if(rule.isPartLotScan()){
					processSavePartLot(partList.get(i));
				} if(rule.isPartMaskScan() || rule.isProdLotScan() || rule.isKdLotScan()){
					processSavePartMask(partList.get(i)); 
				}
			}
		}catch(Throwable t){
			Logger.getLogger().error("ERROR:","Failed to save Part Lot in knuckles persistence.");
		}

	}

	private void processSavePartLot(InstalledPart part) {
		if(!part.isSkipped() && part.isValidPartSerialNumber()){
			PartLotDao partLotDao = ServiceFactory.getDao(PartLotDao.class);
			PartLot currentPartLot = partLotDao.findCurrentPartLot(part.getId().getPartName(), context.isRemake());
			currentPartLot.setCurrentQuantity(currentPartLot.getCurrentQuantity() - 1);
			if(currentPartLot.getCurrentQuantity() <= 0)
				currentPartLot.setStatus(PartLotStatus.CLOSED);
			partLotDao.save(currentPartLot);

			if(currentPartLot.getStatus() == PartLotStatus.SAFTYSTOCK)
				partLotDao.updateSaftyStockQuantity(currentPartLot);

			Logger.getLogger().info("Complete Part - update Part Lot:", currentPartLot.getId().getPartSerialNumber(),
					" Part Name:", currentPartLot.getId().getPartName(), " Currnet Quantity:" + currentPartLot.getCurrentQuantity());
		}
	}


	private void processSavePartMask(InstalledPart part) {
		PartLot currentPartLot = findCurrentPartLot(part.getId().getPartName());
		if(currentPartLot != null){
			if(!currentPartLot.getId().getPartNumber().trim().equals(part.getPartSerialNumber().trim())){
				currentPartLot.setStatus(PartLotStatus.OPEN);
				savePartLot(currentPartLot);
				currentPartLot = null;
			} else if(currentPartLot.getStatus() != PartLotStatus.INPROGRESS) {
				currentPartLot.setStatus(PartLotStatus.INPROGRESS);
				savePartLot(currentPartLot);
			}
		}

		if(currentPartLot == null) {
			currentPartLot = new PartLot(part.getPartSerialNumber(), part.getId().getPartName());
			currentPartLot.getId().setPartNumber(part.getPartSerialNumber());
			currentPartLot.setStatus(PartLotStatus.INPROGRESS);
			savePartLot(currentPartLot);
		}
	}
	
	private void savePartLot(PartLot currentPartLot) {
		PartLotDao partLotDao = ServiceFactory.getDao(PartLotDao.class);
		partLotDao.save(currentPartLot);
		
	}

	private LotControlRule getLotControlRule(ProcessProduct state, int i) {
		return state.getLotControlRules().get(i);
	}
	
	protected void sendPartSnRequest(final String partSn) {
		Thread t = new Thread(){
			public void run() {
			
				DataCollectionController.getInstance().received(new PartSerialNumber(partSn));
			}
		};

		t.start();

	}
}
