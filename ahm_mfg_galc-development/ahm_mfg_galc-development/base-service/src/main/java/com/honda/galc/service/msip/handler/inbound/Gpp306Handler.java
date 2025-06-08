package com.honda.galc.service.msip.handler.inbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.msip.dto.inbound.Gpp306Dto;
import com.honda.galc.service.msip.dto.inbound.PreProductionLotDto;
import com.honda.galc.service.msip.dto.inbound.ProductionLotDto;
import com.honda.galc.service.msip.handler.inbound.PlanCodeBasedMsipHandler;
import com.honda.galc.service.msip.property.inbound.Gpp306PropertyBean;
import com.honda.galc.service.msip.util.MsipUtil;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Gpp306Handler extends PlanCodeBasedMsipHandler<Gpp306PropertyBean, Gpp306Dto> {
	
	private final static String AE_PROCESS_LOCATION	=	"AE";
	private final static String AM_PROCESS_LOCATION	=	"AM";
	
	public Gpp306Handler() {}
	
	public boolean execute(List<Gpp306Dto> receivedRecords) {
		try {		
				processComponentSerialNumberRelease(createMapByPlanCode(receivedRecords));
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	private void processComponentSerialNumberRelease(Map<String, ArrayList<Gpp306Dto>> plan306Map) {
		getLogger().info("start to process priority plan");
		
			try {
				
				// Create the starting record for the plan code if it doesn't exist
				if(getPropertyBean().getInitializeTail().equals("false"))
					initTails(plan306Map);
	
				// Verify that each Plan Code has one and only one tail
				Map<String, PreProductionLotDto> tailsByPlanCode = getTailsByPlanCode();
				if(tailsByPlanCode == null || tailsByPlanCode.isEmpty())
					throw new TaskException(String.format("Tails are not correct for at least one plan code: %s", StringUtils.join(getPropertyBean().getPlanCodes(), ",")));
	
				// Process one Plan Code at a time
				for(String planCode : plan306Map.keySet()) {
					PreProductionLotDto tail = tailsByPlanCode.get(planCode);
					List<PreProductionLotDto> newPplList = new ArrayList<PreProductionLotDto>();
					List<ProductionLotDto> newPlList = new ArrayList<ProductionLotDto>();
					Set<String> changedLotNumbers = new HashSet<String>();
					
					// Start working with the individual record from the file
					for(Gpp306Dto plan306 : plan306Map.get(planCode)) {
						if(getDao(PreProductionLotDao.class).findByStartProductId(plan306.getStartProductId()) != null) {
							String Message = String.format("Skipping... record with same starting product Id already exists: %s",  plan306.getStartProductId());
							getLogger().error(Message);

							continue;				
						}
						
						while(!changedLotNumbers.add(plan306.getLotNumber()) || getDao(PreProductionLotDao.class).findByProductionLotAndPlanCode(plan306.createProductionLot(), plan306.getPlanCode()) != null)
							plan306.setLotNumber(plan306.getLotNumber().substring(0, 8).concat(String.format("%04d", Integer.parseInt(plan306.getLotNumber().substring(8)) + 1)));

						plan306.setProductionLot(plan306.createProductionLot());
	
						// Pre Production Lot
						PreProductionLotDto pplDto = new PreProductionLotDto(plan306);
	
						tail.setNextProductionLot(pplDto.getProductionLot());
						pplDto.setSequence(tail.getSequence() + 1);
						
						newPplList.add(tail);
						
						tail = pplDto;
						
						tailsByPlanCode.remove(planCode);
						tailsByPlanCode.put(planCode, tail);
	
						// Production Lot
						newPlList.add(getProductionLot(plan306));
					}
	
					// Add the new tail to the list
					newPplList.add(tail);
	
					// Create the PreProductionLot, ProductionLot, & Engines
					serializeData(newPplList, newPlList);
	
					getLogger().info(String.format("Plan Code %s is processed.", planCode));
				}
			} catch(Exception e) {
				getLogger().error("Exception processing Component Serial Number Release data");
			}
	
			getLogger().info("Component Serial Number Release processed.");
	}
	
	private void serializeData(List<PreProductionLotDto> ppldtoList, List<ProductionLotDto> pldtoList) {
		// 	Testing data integrity
		Set<String> productionLotSet = new HashSet<String>();
		
		for(PreProductionLotDto ppldto : ppldtoList) {
			PreProductionLot ppl = ppldto.derivePreProductionLot();
			
			if(ppl == null || ppl.getProductionLot() == null)
				throw new TaskException("PreProductionLot is undefined.");
			
			String strProductionLot = ppl.getProductionLot();

			if(productionLotSet.contains(strProductionLot))
				throw new TaskException("Duplicate PreProductionLot.");

			productionLotSet.add(strProductionLot);
		}

		productionLotSet.clear();
		
		for(ProductionLotDto pldto : pldtoList) {
			ProductionLot pl = deriveProductionLot(pldto);
			
			if(pl == null || pl.getProductionLot() == null)
				throw new TaskException("ProductionLot is undefined.");
			
			String strProductionLot = pl.getProductionLot();

			if(productionLotSet.contains(strProductionLot))
				throw new TaskException("Duplicate ProductionLot.");

			productionLotSet.add(strProductionLot);
		}
		
		saveData(ppldtoList, pldtoList);
	}
	
	private void saveData(List<PreProductionLotDto> ppldtoList, List<ProductionLotDto> pldtoList){
//	 	serialize data		
		//	PreProductionLot
		for(PreProductionLotDto ppldto : ppldtoList)
			getDao(PreProductionLotDao.class).save(ppldto.derivePreProductionLot());
		
		//	ProductionLot
		for(ProductionLotDto pldto : pldtoList) {
			ProductionLot pl = deriveProductionLot(pldto);
			getDao(ProductionLotDao.class).insert(pl);
			
			if(pl.getProcessLocation().equals(AE_PROCESS_LOCATION))
				createEngines(pl);
			else if(pl.getProcessLocation().equals(AM_PROCESS_LOCATION))
				insertTransmissions(pldto);
		}
	}
	
	private void createEngines(ProductionLot pl) {
		List<Engine> engines = new ArrayList<Engine>();
		int lotSize = pl.getLotSize();
		String engineType = pl.getStartProductId().substring(0,5);
		int serialNumber = Integer.parseInt(pl.getStartProductId().substring(5));
		getLogger().info("Creating " + lotSize + " EINs. Starting from " + pl.getStartProductId());
		for(int i = 0; i < lotSize; i++) {
			String productId = engineType + String.format("%07d", serialNumber); 
			serialNumber++;
			Engine engine = createEngine(pl, productId);
			engines.add(engine);
		}
		getDao(EngineDao.class).saveAll(engines);
		getLogger().info("" + lotSize + " EINs are created");
	}
	
//	Search incoming message and use appropriate data as initial record for PreProductionSchedule 
	private void initTails(Map<String, ArrayList<Gpp306Dto>> RecordsByPlanCode) {
		for(String planCode : RecordsByPlanCode.keySet()) {
			if(getDao(PreProductionLotDao.class).countByPlanCode(planCode) == 0) {
				Gpp306Dto plan306 = RecordsByPlanCode.get(planCode).get(0);
				
				PreProductionLotDto ppldto = new PreProductionLotDto(plan306);
				PreProductionLot ppl = ppldto.derivePreProductionLot();
				ppl.setSequence(1);
				getDao(PreProductionLotDao.class).save(ppl);
				
				ProductionLotDto pldto = getProductionLot(plan306);
				String strProductionLot = ppldto.getProductionLot();
				pldto.setProductionLot(strProductionLot);
				pldto.setProdLotKd(strProductionLot);
				ProductionLot pl = deriveProductionLot(pldto);
				
				createEngines(pl);

				getDao(ProductionLotDao.class).save(pl);
				
				RecordsByPlanCode.get(planCode).remove(0);
			}
		}
	}
	
    public ProductionLotDto getProductionLot(Gpp306Dto dto306) {
    	ProductionLotDto productionLotDto = new ProductionLotDto();
    	productionLotDto.setProductionLot(dto306.getProductionLot());
    	productionLotDto.setPlanCode(dto306.getPlanCode());
    	productionLotDto.setLotSize(dto306.getLotSize());
    	productionLotDto.setStartProductId(dto306.getStartProductId());
    	productionLotDto.setLotNumber(dto306.getLotNumber());
    	productionLotDto.setProcessLocation(dto306.getProcessLocation());
    	productionLotDto.setProductSpecCode(dto306.getProductSpecCode());
    	productionLotDto.setKdLotNumber(dto306.getKdLotNumber());
    	String planCode = dto306.getPlanCode();
    	if(planCode != null) {
        	if(planCode.length() > 3) {
        		String plantCode = planCode.substring(0, 3);
        		productionLotDto.setPlantCode(plantCode);
        	}
        	if(planCode.length() > 6) {
        		String strLineNo = planCode.substring(4, 6);
        		productionLotDto.setLineNo(strLineNo);
        	}
    	}
    	if(productionLotDto.getLotNumber() != null && productionLotDto.getLotNumber().length() > 8) {
    		try {
    			productionLotDto.setPlanOffDate(new Date(MsipUtil.sdf1.parse(productionLotDto.getLotNumber().substring(0,8)).getTime()));
    			productionLotDto.setProductionDate(productionLotDto.getPlanOffDate());
			} catch (ParseException e) {
				getLogger().error("Parsing date error from: " + productionLotDto.getLotNumber());
			}
    	}
    	return productionLotDto;
    }
}
