package com.honda.galc.service.msip.handler.inbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.msip.dto.inbound.Gpp307Dto;
import com.honda.galc.service.msip.handler.inbound.PlanCodeBasedMsipHandler;
import com.honda.galc.service.msip.property.inbound.Gpp307PropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Gpp307Handler extends PlanCodeBasedMsipHandler<Gpp307PropertyBean, Gpp307Dto> {
	
	private enum PlanCodeElements {
		SiteId(1),
		LineId(2),
		ProcessLocation(4);
		
		private final int Id;
		
		PlanCodeElements(int Id) {
			this.Id = Id;
		}
	}
	
	public Gpp307Handler() {}
	
	public boolean execute(List<Gpp307Dto> receivedRecords) {
		try {		
			processReleasedAssembly(createMapByPlanCode(receivedRecords));
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	private void processReleasedAssembly(Map<String, ArrayList<Gpp307Dto>> plan307Map) {
		getLogger().info("start to process priority plan");
			try {
				// Process one Plan Code at a time
				for(String planCode : plan307Map.keySet()) {
					final String lineId = parsePlanCode(planCode, PlanCodeElements.LineId);
					PreProductionLot previousPpl = null;
					List<PreProductionLot> modifiedPplList = new ArrayList<PreProductionLot>();
					List<PreProductionLot> waitingPplList = getAllWaitingForPlanCode(planCode);
					
					for(Gpp307Dto plan307 : plan307Map.get(planCode)) {
						PreProductionLot currentPpl = getDao(PreProductionLotDao.class).findByProcessLocationKdLotNumberAndSpecCode(
								parsePlanCode(planCode, PlanCodeElements.ProcessLocation), plan307.getKdLotNumber(), plan307.getProductSpecCode());

						if(currentPpl == null) {
							String message = String.format("Skipping... PreProductionLot not found for Process Location:%s, KdLotNumber:%s, and Product Spec Code:%s", 
									plan307.getProcessLocation(), plan307.getKdLotNumber(), plan307.getProductSpecCode().trim());
							
							getLogger().error(message);
							
							continue;
						}
						
						if(currentPpl.getSendStatus() == PreProductionLotSendStatus.WAITING) {
							if(modifiedPplList.size() == 0) {
								previousPpl = findStartLocation(currentPpl, planCode);
								if(previousPpl == null) {
									continue;
								}
								waitingPplList.remove(previousPpl);
								modifiedPplList.add(previousPpl);
							}

							waitingPplList.remove(currentPpl);
							modifiedPplList.get(modifiedPplList.size() - 1).setNextProductionLot(currentPpl.getProductionLot());
							
							// Lot is moving to a new line so the previous lines schedule needs to be maintained
							if(!currentPpl.getPlanCode().equals(planCode)) {
								if(!currentPpl.getProductionLot().equals(currentPpl.getNextProductionLot()))
									for(PreProductionLot movingPpl : getDao(PreProductionLotDao.class).findLotByNextProductionLot(currentPpl.getProductionLot())) {
										movingPpl.setNextProductionLot(currentPpl.getNextProductionLot());
										modifiedPplList.add(movingPpl);
									}
							
								currentPpl.setPlanCode(planCode);
								currentPpl.setLineNo(lineId);
							}
							
							currentPpl.setNextProductionLot(null);
							currentPpl.setSequence(modifiedPplList.get(modifiedPplList.size() - 1).getSequence() + 1);
							modifiedPplList.add(currentPpl);
						} else {
							String message = String.format("Skipping... PreProductionLot already in process for Process Location:%s, KdLotNumber:%s, and Product Spec Code:%s", 
									currentPpl.getProcessLocation(), currentPpl.getKdLotNumber(), currentPpl.getProductSpecCode().trim());
							
							getLogger().error(message);
						}

						previousPpl = currentPpl;
					}
					
					savePreproductionLot(waitingPplList, modifiedPplList);
					
	
					getLogger().info(String.format("Plan Code %s is processed.", planCode));
				}
			} catch(Exception e) {
				getLogger().error("Exception processing Component Released Assembly data");
			}
	
			getLogger().info("Component Serial Number Release processed.");
	}
	
	private PreProductionLot findStartLocation(PreProductionLot currentPpl, String planCode){
		PreProductionLot previousPpl = null;
		if(previousPpl == null && currentPpl.getPlanCode().equals(planCode) && !currentPpl.getProductionLot().equals(currentPpl.getNextProductionLot()))
			previousPpl = getDao(PreProductionLotDao.class).findParent(currentPpl.getProductionLot());
			
		if(previousPpl == null)
			previousPpl = getLastProcessed(planCode);								

		if(previousPpl == null) {
			String message = String.format("Skipping... Unable to determine starting location for Plan Code: %s", planCode);
			getLogger().error(message);
		}
		return previousPpl;
	}
	
	private void savePreproductionLot(List<PreProductionLot> waitingPplList, List<PreProductionLot> modifiedPplList){
		// Anything that has been removed from the schedule
		if(waitingPplList != null) {
			for(PreProductionLot planCodePpl : waitingPplList) {
				if(!planCodePpl.getProductionLot().equals(planCodePpl.getNextProductionLot())) {
					planCodePpl.setNextProductionLot(planCodePpl.getProductionLot());
					planCodePpl.setSequence(0d);
					modifiedPplList.add(planCodePpl);
				}
			}
		}
		
		if(!modifiedPplList.isEmpty() && validateList(modifiedPplList))
			getDao(PreProductionLotDao.class).saveAll(modifiedPplList);
	}
	
	private boolean validateList(List<PreProductionLot> PplList) {
		Set<String> check4Dups = new HashSet<String>();
		
		// Duplicate record check
		for(PreProductionLot ppl : PplList) {
			if(check4Dups.contains(ppl.getProductionLot())) {
				String DupErr = String.format("Duplicate Production Lot: %s found in import file\nImport of Plan Code cancelled", ppl.getProductionLot());				
				getLogger().error(DupErr);

				return false;
			}
			
			check4Dups.add(ppl.getProductionLot());
		}
		
		return true;
	}

	private String parsePlanCode(String PlanCode, PlanCodeElements Element) {
		Pattern compile = Pattern.compile("^(.{4})(.{2})(.)(.{2})(.*)$");
		Matcher matcher = compile.matcher(PlanCode);
		matcher.find();
		
		return matcher.group(Element.Id);		
	}

	private List<PreProductionLot> getAllWaitingForPlanCode(String PlanCode) {
		List<PreProductionLot> pplList = new ArrayList<PreProductionLot>();
		
		for(PreProductionLot ppl : getDao(PreProductionLotDao.class).findAllBySendStatusAndPlanCode(PreProductionLotSendStatus.WAITING.getId(), PlanCode))
			pplList.add(ppl);
		
		return pplList;
	}
	private PreProductionLot getLastProcessed(String planCode) {
		final boolean isScheduleBySequence = getPropertyBean().getIsScheduleBySequence().equals("TRUE")?true:false;;
		int[] statusesToCheck = {PreProductionLotSendStatus.SENT.getId(), PreProductionLotSendStatus.INPROGRESS.getId(), PreProductionLotSendStatus.DONE.getId()};
		PreProductionLot lastProcessedPpl = null;
		PreProductionLotDao pplDao = getDao(PreProductionLotDao.class);

		for(int sendStatus : statusesToCheck) {
			List<PreProductionLot> productionLots = pplDao.findAllBySendStatusAndPlanCode(sendStatus, planCode);
			
			if(isScheduleBySequence) {
				for(PreProductionLot ppl : productionLots) {
					if(ppl.getSequence() > (lastProcessedPpl == null ? 0 : lastProcessedPpl.getSequence())) {
						lastProcessedPpl = ppl;
					}
				}
				
				if(lastProcessedPpl != null)
					return lastProcessedPpl;
			} else {
				List<String> nextProductionLots = new ArrayList<String>();

				for(PreProductionLot ppl : productionLots)
					nextProductionLots.add(ppl.getNextProductionLot());

				for(PreProductionLot ppl : productionLots) {
					if(!nextProductionLots.contains(ppl.getProductionLot()))
						return ppl;
				}				
			}
		}
		
		return pplDao.findLast(planCode);
	}	

}
