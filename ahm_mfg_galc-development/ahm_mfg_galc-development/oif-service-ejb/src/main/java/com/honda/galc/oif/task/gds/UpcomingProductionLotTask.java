package com.honda.galc.oif.task.gds;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductSpec;

/**
 * 
 * <h3>UpcomingProductionLotTask Class description</h3>
 * <p> UpcomingProductionLotTask description </p>
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
 * @author Jeffray Huang<br>
 * Nov 21, 2012
 *
 *
 */
public class UpcomingProductionLotTask extends AbstractDataCalculationTask{

	public UpcomingProductionLotTask(String name) {
		super(name);
	}

	@Override
	public void processCalculation() {
		int count = getPropertyInt("NUMBER_OF_LOTS", 3);
		List<PreProductionLot> prodLots = getDao(PreProductionLotDao.class).findUpcomingPreProductionLots(count);
		
		if(prodLots.isEmpty()) return;
		PreProductionLot prodLot = prodLots.get(0);
		String deptName = prodLot.getProcessLocation();
		int startIndex = 0;
		if(prodLot.getStampedCount() != 0) {
			processCurrentProductionLot(prodLot);
			startIndex = 1;
		}
		for(int i = startIndex;i<count;i++) {
			PreProductionLot item = i >= prodLots.size() ? null : prodLots.get(i);
			String name = deptName + "\\Upcoming Lots\\KD Lot " + (i + 1) + "\\";
			updateValue(name + "KD Lot", item == null  ? "-" : item.getKdLot());
			updateValue(name + "Prod Lot", item == null  ? "-" : item.getProductionLot());
			updateValue(name + "Year", item == null  ? "-" : ProductSpec.extractModelYearCode(item.getProductSpecCode()));
			updateValue(name + "Model", item == null  ? "-" : ProductSpec.extractModelCode(item.getProductSpecCode()));
			updateValue(name + "Type", item == null  ? "-" : ProductSpec.extractModelTypeCode(item.getProductSpecCode()));
			updateValue(name + "Option", item == null  ? "-" : ProductSpec.extractModelOptionCode(item.getProductSpecCode()));
			updateValue(name + "Lot Size", item == null  ? 0 : item.getLotSize());
		}
	}
	
	private void processCurrentProductionLot(PreProductionLot prodLot) {
		List<PreProductionLot> prodLots = getDao(PreProductionLotDao.class).findAllWithSameKdLot(prodLot.getKdLot());
		
		int kdLotSize = 0;
		int kdLotProgress = 0;
		for(PreProductionLot item : prodLots) {
			kdLotSize += item.getLotSize();
			kdLotProgress += item.getStampedCount();
		}
		String name = prodLot.getProcessLocation()+ "\\" + getOnProcessName() + "\\";
		updateValue(name + "KD LOT",prodLot.getKdLot());
		updateValue(name + "KD LOT Size",kdLotSize);
		updateValue(name + "KD LOT Progress",kdLotProgress);
		
		updateValue(name + "Prod Lot",prodLot.getProductionLot());
		updateValue(name + "Prod Lot Size",prodLot.getLotSize());
		updateValue(name + "Prod Lot Progress",prodLot.getStampedCount());
		updateValue(name + "Model Code",ProductSpec.extractModelCode(prodLot.getProductSpecCode()));
		updateValue(name + "Model Type Code",ProductSpec.extractModelTypeCode(prodLot.getProductSpecCode()));
		
	}
	
	private String getOnProcessName(){
		String ppId = getProperty("LOT_PROGRESS_PROCESS_POINT");
		if(StringUtils.isEmpty(ppId)) return "NULL";
		ProcessPoint processPoint = getDao(ProcessPointDao.class).findByKey(ppId);
		return processPoint == null ? "NULL" : processPoint.getProcessPointName();
	}

}
