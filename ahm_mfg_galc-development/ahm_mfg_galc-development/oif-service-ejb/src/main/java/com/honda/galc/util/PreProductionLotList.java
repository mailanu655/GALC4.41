package com.honda.galc.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.oif.dto.GPP307DTO;

import static com.honda.galc.service.ServiceFactory.getDao;

public class PreProductionLotList extends ArrayList<PreProductionLot> {
	private static final long serialVersionUID = 1L;
	private PreProductionLotDao dao = null; 
	private Logger logger = null;
	
	public PreProductionLotList() {
		dao = getDao(PreProductionLotDao.class);
		this.logger = Logger.getLogger(this.getClass().getName());
	}
	public PreProductionLotList(Logger logger) {
		dao = getDao(PreProductionLotDao.class);
		this.logger = logger;
	}
	public int indexOfProductionLotNumber(PreProductionLot ProductionLot) {
		return indexOfProductionLotNumber(ProductionLot.getProductionLot());
	}
	
	public int indexOfProductionLotNumber(String ProductionLotNumber) {
		for (int x = 0; x < this.size(); x++) {
			if (this.get(x).getProductionLot().equals(ProductionLotNumber))
				return x;
		}
		
		return -1;
	}

	public int indexOfKdLotNumberSpecCode(GPP307DTO gpp307) {
		return indexOfKdLotNumberSpecCode(gpp307.getKdLotNumber(), gpp307.getProductSpecCode());
	}

	public int indexOfKdLotNumberSpecCode(PreProductionLot preProductionLot) {
		return indexOfKdLotNumberSpecCode(preProductionLot.getKdLotNumber(), preProductionLot.getProductSpecCode());
	}
	
	public int indexOfKdLotNumberSpecCode(String kdLotNumber, String specCode) {
		for (int x = 0; x < this.size(); x++) {
			if (StringUtils.trim(this.get(x).getKdLotNumber()).equals(StringUtils.trim(kdLotNumber)) && 
					StringUtils.trim(this.get(x).getProductSpecCode()).equals(StringUtils.trim(specCode)))
				return x;
		}
		
		return -1;
	}
	
	public boolean containsProductionLotNumber(String ProductionLotNumber) {
		return indexOfProductionLotNumber(ProductionLotNumber) >= 0;
	}

	public boolean containsKdLotNumberSpecCode(String kdLotNumber, String specCode) {
		return indexOfKdLotNumberSpecCode(kdLotNumber, specCode) >= -1;
	}
	public void SetNextProductionLotSequence() {
		if (this.size() == 0) return;
		logger.info("Setting the Next prod lot and sequence ");
		double sequence = this.get(0).getSequence();
		if(sequence ==0) sequence = 1;
		
		for (int x = 0; x < this.size(); x++) {
			if (x > 0)
				this.get(x - 1).setNextProductionLot(this.get(x).getProductionLot());
					
			this.get(x).setSequence(sequence++);
		}
		
		this.get(this.size() - 1).setNextProductionLot(null);
	}
	
	public PreProductionLotList MoveProductionLots() {
		logger.info("Moving production lots to new plan codes and fixing the tails");
		PreProductionLotList pplReturn = new PreProductionLotList();
		
		for (PreProductionLot thisPpl : this)
			pplReturn.addAll(dao.findLotByNextProductionLot(thisPpl.getProductionLot()));

		for (int iOuter = 0; iOuter < pplReturn.size() - 1; iOuter++) {
			boolean cont = true;
			
			for (int iInner = iOuter + 1; iInner < pplReturn.size(); iInner++) {
				if (pplReturn.get(iInner).getProductionLot().equals(pplReturn.get(iOuter).getNextProductionLot())) {
					pplReturn.get(iOuter).setNextProductionLot(this.get(this.indexOfProductionLotNumber(pplReturn.remove(iInner--).getNextProductionLot())).getNextProductionLot());

					cont = false;
				}
			}
			
			if (cont)
				pplReturn.get(iOuter).setNextProductionLot(this.get(this.indexOfProductionLotNumber(pplReturn.get(iOuter).getNextProductionLot())).getNextProductionLot());
		}
		
		return pplReturn;
	}
	
	public PreProductionLotList RemoveFromSchedule() {
		logger.info("Remove lots that are not present in the file from DB ");
		PreProductionLotList ppll = new PreProductionLotList();
		
		// Add any lots that currently point to the ones being removed
		for (PreProductionLot ppl : this)
			ppll.addAll(dao.findLotByNextProductionLot(ppl.getProductionLot()));
		
		// Remove lots that are being taken out of the schedule
		ppll.removeAll(this);

		// Correct the linked list
		for (PreProductionLot ppl : ppll) {
			int i = -1;
			
			while ((i = this.indexOfProductionLotNumber(ppl.getNextProductionLot())) >= 0)
				ppl.setNextProductionLot(this.get(i).getNextProductionLot());
		}
		
		// Add the lots being taken out of the schedule back in 
		for (PreProductionLot ppl : this) {
			logger.info("Logically removing KD Lot :"+ppl.getKdLotNumber() +" from the DB ");
			ppl.setNextProductionLot(ppl.getProductionLot());
			ppl.setSequence(0);
			ppll.add(ppl);
		}
		
		return ppll;
	}
	
	public PreProductionLotList AllActive() {
		logger.info("Finding active lots with sequence > 0 ");
		PreProductionLotList ppll = new PreProductionLotList();
		
		for (PreProductionLot ppl : this) {
			if (ppl.getSequence() > 0)
				ppll.add(ppl);
		}
		
		return ppll;
	}
	
	public void findAllBySendStatusAndPlanCodes(PreProductionLotSendStatus sendStatus, String planCodes) {
		findAllBySendStatusAndPlanCodes(sendStatus, planCodes, ",;");
	}
	
	public void findAllBySendStatusAndPlanCodes(PreProductionLotSendStatus sendStatus, String planCodes, String delimeters) {
		findAllBySendStatusAndPlanCodes(sendStatus, StringUtils.split(planCodes, delimeters));
	}
	
	public void findAllBySendStatusAndPlanCodes(PreProductionLotSendStatus sendStatus, List<String> planCodes) {
		findAllBySendStatusAndPlanCodes(sendStatus, (String[]) planCodes.toArray());
	}
	
	public void findAllBySendStatusAndPlanCodes(PreProductionLotSendStatus sendStatus, String... planCodes) {
		this.clear();
		
		for (String planCode : planCodes)
			this.addAll(dao.findAllBySendStatusAndPlanCode(sendStatus.getId(), StringUtils.trim(planCode)));
	}

	public void findAllByPlanCode(String PlanCode) {
		this.clear();
		
		this.addAll(dao.findAllByPlanCode(PlanCode));
	}
	
	public void saveAll() {
		dao.saveAll(this);
	}
}
