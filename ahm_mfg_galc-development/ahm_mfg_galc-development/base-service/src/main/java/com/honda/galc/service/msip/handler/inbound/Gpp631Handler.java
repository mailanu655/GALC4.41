package com.honda.galc.service.msip.handler.inbound;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.enumtype.InhouseRecordOrderStatus;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductStampingSequenceId;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.msip.dto.inbound.Gpp631Dto;
import com.honda.galc.service.msip.dto.inbound.PreProductionLotDto;
import com.honda.galc.service.msip.dto.inbound.ProductionLotDto;
import com.honda.galc.service.msip.property.inbound.Gpp631PropertyBean;
import com.honda.galc.service.msip.util.MsipUtil;
import com.honda.galc.util.StringUtil;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public class Gpp631Handler extends PlanCodeBasedMsipHandler<Gpp631PropertyBean, Gpp631Dto> {
	
	boolean useNextProductionLot = true;
	int txTimeout = 0; //default=300, read from propertyBean
	
	Timestamp jobTs = null;
	protected ArrayList<Gpp631Dto> listOfPlan631 = new ArrayList<Gpp631Dto>();
	PreProductionLotDao ppLotDao;
	ProductionLotDao pLotDao;
	MbpnDao mbpnDao;
	
	public Gpp631Handler() {}
	
	public boolean execute(List<Gpp631Dto> receivedRecords) {
		try {		
			//processReleasedAssembly(createMapByPlanCode(receivedRecords));
			useNextProductionLot = getPropertyBean().getUseNextProductionLot();
			jobTs = getService(GenericDaoService.class).getCurrentDBTime();
			processInHousePriorityPlan(receivedRecords);
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	private void processInHousePriorityPlan(List<Gpp631Dto> plan631List) {
		if(useNextProductionLot)  {
			Map<String, PreProductionLotDto> tails = getTailsByPlanCode();
			if(tails == null || tails.isEmpty())
				throw new TaskException(String.format("Tails are not correct for at least one plan code: %s", StringUtils.join(getPropertyBean().getPlanCodes(), ",")));
		}

		try {
			Map<String, ArrayList<PreProductionLot>> ppLotMap = new HashMap<String, ArrayList<PreProductionLot>>();
			Map<String, ArrayList<ProductionLot>> pLotMap = new HashMap<String, ArrayList<ProductionLot>>();
			Map<String, ArrayList<PreProductionLotDto>> ppLotDTOMap = new HashMap<String, ArrayList<PreProductionLotDto>>();
			Map<String, String> processLocationByPlanCode = new HashMap<String, String>();
			
			buildPreProductionLots(plan631List, ppLotMap, pLotMap, ppLotDTOMap, processLocationByPlanCode);

			if(ppLotMap != null && !ppLotMap.isEmpty()) {
				if(useNextProductionLot)
					saveLots(ppLotMap, pLotMap, ppLotDTOMap, processLocationByPlanCode);
				else
					saveLotsBySequence(ppLotMap, pLotMap, ppLotDTOMap);
			}
		} catch (Exception e) {
			getLogger().error(e, "Exception processing InHouse priority plan data");
		}
		updateLastProcessTimestamp(jobTs);
		getLogger().info("In House Priority plan data processed.");
	}
	
	public void saveLots(
			Map<String, ArrayList<PreProductionLot>> ppLotMap,
			Map<String, ArrayList<ProductionLot>> prodLotMap, 
			Map<String, ArrayList<PreProductionLotDto>> ppLotDTOMap,
			Map<String, String> processLocationByPlanCode) {
		ppLotDao = getDao(PreProductionLotDao.class);
		pLotDao = getDao(ProductionLotDao.class);
		MbpnDao mbpnDao = getDao(MbpnDao.class);
		ArrayList<Mbpn> mbpnList = new ArrayList<Mbpn>();
		
		if(ppLotMap == null || ppLotMap.isEmpty())  
			return;
		
		//first, delete existing lots which have a send status = WAITING/0
		for(String planCode : ppLotMap.keySet())  {
			PreProductionLot attachAt = null;
			//for each plan code in 631 file
			String procLoc = processLocationByPlanCode.get(planCode); //get process location
			//get a sorted list of pp lots with send status=0, last element in list is tail record
			//*1: in case there is send_status != 0 in the middle, then this will be a partial list (the last set
			//of consecutive send_status=0 records, that is OK, we will fix it later
			List<PreProductionLot> ppLotListWaiting = ppLotDao.findAllPreProductionLotsByProcessLocation(procLoc);
			
			if(ppLotListWaiting == null || ppLotListWaiting.isEmpty())  {
				//this usually means a tail record could not be found with send status=0
				//then find tail record with any send status
				attachAt = ppLotDao.findLastLot(procLoc);
			}
			else  {
				PreProductionLot ppLot = ppLotListWaiting.get(0);
				
				if(ppLot != null)  {
					attachAt = ppLotDao.findParent(ppLot.getProductionLot());
				}
			}
			
			deleteRecord(ppLotListWaiting, planCode);
			
			//insert all the new pre-prod-lot records for that plan code
			mbpnList = savePreProductionLot(ppLotMap, prodLotMap,ppLotDTOMap, planCode, attachAt);
			
		}
		
		for(Mbpn newMbpn : mbpnList)  {
			mbpnDao.save(newMbpn);
		}

	}
	
	private ArrayList<Mbpn> savePreProductionLot(Map<String, ArrayList<PreProductionLot>> ppLotMap,
			Map<String, ArrayList<ProductionLot>> prodLotMap, 
			Map<String, ArrayList<PreProductionLotDto>> ppLotDTOMap,
			String planCode, PreProductionLot attachAt){
		ArrayList<Mbpn> mbpnList = new ArrayList<Mbpn>();
		ArrayList<PreProductionLot> newPPLotList = ppLotMap.get(planCode);
		ArrayList<ProductionLot> newProdLotList = prodLotMap.get(planCode);
		ArrayList<PreProductionLotDto> newProdLotDTOList = ppLotDTOMap.get(planCode);
		
		for(PreProductionLot newPPLot : newPPLotList)  {
			Mbpn newMbpn = createMBPN(newPPLot);
			mbpnList.add(newMbpn);
		}
		if(newPPLotList != null && !newPPLotList.isEmpty())  {
			if(attachAt != null && newPPLotList.get(0) != null)  {					
				attachAt.setNextProductionLot(newPPLotList.get(0).getProductionLot());
				ppLotDao.save(attachAt);
			}
			ppLotDao.saveAll(newPPLotList);
			if(newProdLotList != null && !newProdLotList.isEmpty())
				pLotDao.saveAll(newProdLotList);
		}
		if (getPropertyBean().getIsGenerateProductionId()) {
			for(PreProductionLotDto newPPLotDTO : newProdLotDTOList)  {
				createMbpnProduct(newPPLotDTO);
			}
		}
		return mbpnList;
	}
	public void deleteRecord(List<PreProductionLot> ppLotListWaiting, String planCode){
		//now, restore the full list of waiting records to be deleted to fix *1 above
		ppLotListWaiting = ppLotDao.findAllWaitingByPlanCode(planCode);
		
		//delete record and corresponding 217/prod-lot record if SEND_STATUS==0
		int deleteCount = 0;

		for(PreProductionLot ppLot : ppLotListWaiting)  {
			if(ppLot == null) 
				continue;
			
			if(!ppLot.getSendStatus().equals(PreProductionLotSendStatus.WAITING))
				continue; //don't do anything if SEND_STATUS != 0
			
			if ( getPropertyBean().getIsAutomaticHold().equals("true") && (!isNotHoldDemandType(ppLot.getDemandType()))){
				continue;
			}
			ppLotDao.delete(ppLot.getProductionLot());
			pLotDao.delete(ppLot.getProductionLot());
			deleteCount++;
		}

		getLogger().info(String.format("Plan code: %s - deleted %d", planCode, deleteCount));
	}
	
	public void saveLotsBySequence(
			Map<String, ArrayList<PreProductionLot>> ppLotMap,
			Map<String, ArrayList<ProductionLot>> prodLotMap, 
			Map<String, ArrayList<PreProductionLotDto>> ppLotDTOMap) {
		
		ArrayList<Mbpn> mbpnList = new ArrayList<Mbpn>();
		PreProductionLotDao ppLotDao = getDao(PreProductionLotDao.class);
		ProductionLotDao pLotDao = getDao(ProductionLotDao.class);
		MbpnDao mbpnDao = getDao(MbpnDao.class);
		
		if(ppLotMap == null || ppLotMap.isEmpty())
			return;
		
		for(String planCode : ppLotMap.keySet())  {
			//delete record and corresponding 217/prod-lot record if SEND_STATUS==0
			int deleteCount = 0;

			if ( getPropertyBean().getIsAutomaticHold().equals("true")){
				// only delete not hold demand types that are in waiting status
				String demandTypes = getPropertyBean().getNotHoldDemandTypes();
				getDao(PreProductionLotDao.class).deleteByPlanCodeSendStatusNotHoldDemandType(planCode, PreProductionLotSendStatus.WAITING.getId(), demandTypes);	
				
			} else {
				deleteCount = pLotDao.deleteByPlanCodeSendStatus(planCode, PreProductionLotSendStatus.WAITING.getId());
				deleteCount = ppLotDao.deleteByPlanCodeSendStatus(planCode, PreProductionLotSendStatus.WAITING.getId());			
				
				getLogger().info(String.format("Plan code: %s - deleted %d", planCode, deleteCount));
				
			}
			
			//insert all the new pre-prod-lot records for that plan code
			ArrayList<PreProductionLot> newPPLotList = ppLotMap.get(planCode);
			ArrayList<ProductionLot> newProdLotList = prodLotMap.get(planCode);
			ArrayList<PreProductionLotDto> newProdLotDTOList = ppLotDTOMap.get(planCode);
			
			for(PreProductionLot newPPLot : newPPLotList)  {
				Mbpn newMbpn = createMBPN(newPPLot);
				mbpnList.add(newMbpn);	
			}

			if(newPPLotList != null && !newPPLotList.isEmpty())  {
				//even though next production lot chaining is not used here, attempt to fix the chaining
				//to keep it consistent
				PreProductionLot attachAt = ppLotDao.findLastLotByPlanCode(planCode);
				
				if(attachAt != null && newPPLotList.get(0) != null)  {
					attachAt.setNextProductionLot(newPPLotList.get(0).getProductionLot());
					ppLotDao.save(attachAt);
				}

				ppLotDao.saveAll(newPPLotList);
				
				if(newProdLotList != null && !newProdLotList.isEmpty())
					pLotDao.saveAll(newProdLotList);
			}

			if (getPropertyBean().getIsGenerateProductionId()) {
				for(PreProductionLotDto newPPLotDTO : newProdLotDTOList)
					createMbpnProduct(newPPLotDTO);
			}
		}
		
		for(Mbpn newMbpn : mbpnList)  {
			mbpnDao.save(newMbpn);
		}
	}
	
	private void buildPreProductionLots(
			List<Gpp631Dto> planList, 
			Map<String, ArrayList<PreProductionLot>> ppLotMap,
			Map<String, ArrayList<ProductionLot>> pLotMap, 
			Map<String, ArrayList<PreProductionLotDto>> ppLotDTOMap,
			Map<String, String> processLocationByPlanCode) throws IllegalAccessException, InstantiationException {
		
		Map<String, PreProductionLot> previousPplByPlanCode = new HashMap<String, PreProductionLot>();
		PreProductionLotDao ppLotDao = getDao(PreProductionLotDao.class);

		Map<String, ArrayList<Gpp631Dto>> receivedRecordsByPlanCode = createMapByPlanCode(planList);
		
		if (receivedRecordsByPlanCode.isEmpty())
			return;
		
		Map<String, Double> sequences = new HashMap<String, Double>();

		for(String planCode : receivedRecordsByPlanCode.keySet()) {
			for(Gpp631Dto plan631 : receivedRecordsByPlanCode.get(planCode)) {
				PreProductionLot ppLot = null;
				PreProductionLotDto ppldto = new PreProductionLotDto(plan631, useNextProductionLot);
				
				if(plan631.getCurrentOrderStatus() == InhouseRecordOrderStatus.PLANNED.getId() ||
						plan631.getCurrentOrderStatus() == InhouseRecordOrderStatus.CONFIRMED.getId() ||
						plan631.getCurrentOrderStatus() == InhouseRecordOrderStatus.COMPLETED.getId())
					continue;
				
				if(ppldto != null)
					ppLot = ppldto.derivePreProductionLot();
				
				if(ppLot != null)  {
					//if this production lot is already there in GALC, then check send status
					PreProductionLot existingPpl = ppLotDao.findByKey(ppLot.getProductionLot());
	
					// if ppl exists and its send status is not 0, then ignore this.
					if(existingPpl != null && !existingPpl.getSendStatus().equals(PreProductionLotSendStatus.WAITING))
						continue;
	
					if(!processLocationByPlanCode.containsKey(planCode))
						processLocationByPlanCode.put(planCode, ppLot.getProcessLocation());

					PreProductionLot previousPpl = previousPplByPlanCode.get(planCode);
				
					Double maxSequence = findSequenceByNotSendStatus(planCode, sequences);
					ppLot.setSequence(maxSequence);
					sequences.put(planCode, maxSequence);
	
					if(!ppLotMap.containsKey(planCode))  {
						ppLotMap.put(planCode, new ArrayList<PreProductionLot>());
					}
					
					if(!pLotMap.containsKey(planCode))  {
						pLotMap.put(planCode, new ArrayList<ProductionLot>());
					}
					
					if(!ppLotDTOMap.containsKey(planCode))  {
						ppLotDTOMap.put(planCode, new ArrayList<PreProductionLotDto>());
					}
					
					// Check if lot to be placed on hold based on demand type
					if ( getPropertyBean().getIsAutomaticHold().equals("true") && (!isNotHoldDemandType(ppLot.getDemandType()))){
						ppLot.setHoldStatus(0);
						ppLot.setNextProductionLot(null);
						// save the lot without tying to linked list
						ppLotDao.save(ppLot);
					} else {
						ppLot.setHoldStatus(1);
						// Only add to linked list if lot is not on hold
						if(previousPpl != null)
							previousPpl.setNextProductionLot(ppLot.getProductionLot());
						
						previousPplByPlanCode.put(planCode, ppLot);
						previousPpl = ppLot;
						
						ppLotMap.get(planCode).add(ppLot);
						ppLotDTOMap.get(planCode).add(ppldto);
					}

					ProductionLot pLot = null;
	
					ProductionLotDto pldto = createPLDTO(plan631);
					listOfPlan631.add(plan631);
					if(pldto != null)  {
						pLot = deriveProductionLot(pldto);
						pLotMap.get(planCode).add(pLot);
					}
				}
			}
		}
	}

//	Create MBPN from 
	private Mbpn createMBPN(PreProductionLot ppl) {
		Mbpn mbpn = null;
		String mbpnString = ppl.getMbpn();
		if(mbpnString != null && ppl.getHesColor() != null) {
			String productSpecCode = mbpnString + ppl.getHesColor();
			mbpn = (getDao(MbpnDao.class)).findByKey(productSpecCode);
			if(mbpn == null) {
				if(productSpecCode.length() < 18) {
					mbpnString = StringUtil.padRight(productSpecCode, 18, ' ', false);
				}
				mbpn = new Mbpn();
				mbpn.setMbpn(mbpnString);
				mbpn.setHesColor(ppl.getHesColor());
				mbpn.setMainNo(mbpnString.substring(0, 5));
				mbpn.setClassNo(mbpnString.substring(5, 8));
				mbpn.setPrototypeCode(mbpnString.substring(8, 9));
				mbpn.setTypeNo(mbpnString.substring(9, 13));
				mbpn.setSupplementaryNo(mbpnString.substring(13, 15));
				mbpn.setTargetNo(mbpnString.substring(15, 17));
				mbpn.setProductSpecCode(productSpecCode);
			}
		} else {
			getLogger().error("Can not get product Spec Code from " +  ppl);
		}
		return mbpn;
	}


	private ProductionLotDto createPLDTO(Gpp631Dto plan631) { 
		ProductionLotDto pldto = getProductionLot(plan631);
		String productionLot;
		if(useNextProductionLot) {
			productionLot = plan631.generateProductionLot(true);
		} else {
			pldto.setLotSize(plan631.getProdOrderQty());
			productionLot = plan631.generateProductionLot(false);
		}
		pldto.setProductionLot(productionLot);
		return pldto;
	}

	

	private Double findSequenceByNotSendStatus(String planCode, Map<String, Double> sequences) { 
		Double maxSequence = 0d;
		if(sequences.containsKey(planCode)) {
			maxSequence = sequences.get(planCode);
			maxSequence += 1;
		} else {
			maxSequence = getDao(PreProductionLotDao.class)
					.findMaxSequenceWhereNotSendStatus(planCode, PreProductionLotSendStatus.WAITING.getId());
			if(maxSequence == null) {
				maxSequence = 0d;  
			} else {
				maxSequence += 1;
			}
		}
		BigDecimal big = new BigDecimal(Double.toString(maxSequence));
		big = big.setScale(4, RoundingMode.HALF_UP);
		return Double.valueOf(big.toString());
	}
	
	private void createMbpnProduct(PreProductionLotDto ppldto) {

		String startProductIdPrefix = getproductIdPrefix(ppldto);
		if (StringUtils.isEmpty(startProductIdPrefix)) return;
		
		int lotSize = ppldto.getLotSize();
		int lastSeq = findMBPNSequence(startProductIdPrefix);
		String start_producty_id = "";
		for(int i=0; i<lotSize; i++) {
			String product_id = startProductIdPrefix+StringUtils.leftPad(lastSeq+"", getPropertyBean().getSeqLength(),"0");
			if (i == 0) start_producty_id = product_id; 
			MbpnProduct mbpnProduct = new MbpnProduct();
			mbpnProduct.setProductId(product_id);
			mbpnProduct.setCurrentProductSpecCode(ppldto.getMbpn()+ppldto.getHesColor());
			mbpnProduct.setCurrentOrderNo(ppldto.getProductionLot());
			getDao(MbpnProductDao.class).save(mbpnProduct);
			
			createProductStampingSequence(product_id,ppldto.getProductionLot(),i+1);
			lastSeq++;
		}
		//update 212,217's start_producty_id
		getDao(PreProductionLotDao.class).updateStartProductId(ppldto.getProductionLot(),start_producty_id);
		getDao(ProductionLotDao.class).updateStartProductId(ppldto.getProductionLot(),start_producty_id);
	}
	
	private String getproductIdPrefix(PreProductionLotDto ppldto) {
		//get SUB_ASSEMBLE_ID_RULE
		Map<String, String> subAssembleItRuleMap = new HashMap<String, String>();

		String partname = getMBPNPartName(ppldto.getMbpn());
		if (StringUtils.isEmpty(partname)) return "";
		subAssembleItRuleMap = getPropertyBean().getSubAssembleIdRule(String.class);
		
		String startProductIdPrefix = "";
		if (subAssembleItRuleMap.containsKey(partname)){  
			String[] subAssembleItRule = subAssembleItRuleMap.get(partname).split(",");
			for(int i = 0;i <subAssembleItRule.length; i++) {
			 	if (subAssembleItRule[i].equals("partName3"))	startProductIdPrefix = startProductIdPrefix+partname;
			 	if (subAssembleItRule[i].equals("LineNum1"))	startProductIdPrefix = startProductIdPrefix+ppldto.getLineNo().substring(1, 2);
			 	if (subAssembleItRule[i].equals("LineNum2"))	startProductIdPrefix = startProductIdPrefix+ppldto.getLineNo();
			 	if (subAssembleItRule[i].contains("mbpnMain"))   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,ppldto.getMbpn().substring(0, 5),subAssembleItRule[i]);  //			"HTZ6Y"E500 NH603PX   F         
			 	if (subAssembleItRule[i].contains("mbpnClass"))   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,ppldto.getMbpn().substring(5, 8),subAssembleItRule[i]); //		 	 HTZ6Y"E50"0 NH603PX   F         
			 	if (subAssembleItRule[i].equals("mbpnPrototype1"))   startProductIdPrefix = startProductIdPrefix+ppldto.getMbpn().substring(8, 9); //	 	 HTZ6YE50"0" NH603PX   F         
			 	if (subAssembleItRule[i].contains("mbpnType"))	startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,ppldto.getMbpn().substring(9, 13),subAssembleItRule[i]); //		 	 HTZ6YE500" NH6"03PX   F         			 	
			 	if (subAssembleItRule[i].contains("mbpnSupplementary"))   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,ppldto.getMbpn().substring(13, 15),subAssembleItRule[i]); //HTZ6YE500 NH6"03"PX   F         
			 	if (subAssembleItRule[i].contains("mbpnTarget"))   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,ppldto.getMbpn().substring(15, 17),subAssembleItRule[i]); //		 HTZ6YE500 NH603"PX"   F         
			 	if (subAssembleItRule[i].contains("mbpnHesColor"))   startProductIdPrefix = getMbpnSubstring(startProductIdPrefix,ppldto.getHesColor(),subAssembleItRule[i]); //		 HTZ6YE500 NH603"PX"   F
			 	if (subAssembleItRule[i].equals("productionYear2"))	startProductIdPrefix = startProductIdPrefix+ppldto.getProductionDate().substring(2, 4);
			 	if (subAssembleItRule[i].equals("productionYear4"))	startProductIdPrefix = startProductIdPrefix+ppldto.getProductionDate().substring(0, 4);
			 	if (subAssembleItRule[i].equals("productionDate5"))	startProductIdPrefix = startProductIdPrefix+getProductionDate(ppldto.getProductionDate(),true);
			 	if (subAssembleItRule[i].equals("productionDate6"))	startProductIdPrefix = startProductIdPrefix+getProductionDate(ppldto.getProductionDate(),false);
			}
		}
		return startProductIdPrefix;
	}

	private void createProductStampingSequence(String Product_id,String productLot,int seq) {
		//creating the product stamping sequence (table gal216tbx)
		//create Id
		ProductStampingSequenceId productStampingSequeneId	=	new ProductStampingSequenceId ();
		productStampingSequeneId.setProductID(Product_id);
		productStampingSequeneId.setProductionLot(productLot);
		//create the product sequence
		ProductStampingSequence	productStampingSequence = new ProductStampingSequence ();
		productStampingSequence.setId(productStampingSequeneId);
		productStampingSequence.setSendStatus(0);
		productStampingSequence.setStampingSequenceNumber(seq);
		
		getDao( ProductStampingSequenceDao.class ).save(productStampingSequence);
	}

	
	private String getProductionDate(String productiondate,boolean shortDate) {
		String strmonth = productiondate.substring(5, 6);
		if (shortDate) {
			if ( productiondate.substring(4, 6).equals("10")) strmonth = "A";
			if ( productiondate.substring(4, 6).equals("11")) strmonth = "B";
			if ( productiondate.substring(4, 6).equals("12")) strmonth = "C";
		} else return productiondate.substring(2,8);
		
		return productiondate.substring(2, 4)+strmonth+productiondate.substring(6, 8);
	}

	private String getMBPNPartName(String mbpnString) {
		String MBPNMainNo = mbpnString.substring(0, 5);
		String[] MbpnPartEnum = getPropertyBean().getMbpnPartEnum().split(",");
		Map<String, String> mainNoMap = new HashMap<String, String>();
		
		if (null == MbpnPartEnum || MbpnPartEnum.length == 0) return "";
		for (int i = 0;i<MbpnPartEnum.length;i++) {
			mainNoMap = getPropertyBean().getMbpnPartPrefix(String.class);
					if ( null == mainNoMap || mainNoMap.size() == 0) return "";
					if (mainNoMap.containsKey(MbpnPartEnum[i])) {
						String MBPNMainNoLst = mainNoMap.get(MbpnPartEnum[i]);
						if (MBPNMainNoLst.indexOf(MBPNMainNo) != -1 ) return  MbpnPartEnum[i];
					}
		}
		return "";
	}

	private int findMBPNSequence(String productIdPrefix) { 
		int maxSequence = 1;
		String lastProductId = getDao(MbpnProductDao.class).findLastProductId(productIdPrefix+"%");
		if (!StringUtils.isBlank(lastProductId)) maxSequence =(int)Integer.parseInt(lastProductId.substring(lastProductId.length()-getPropertyBean().getSeqLength(), lastProductId.length()))+1;
		return maxSequence;
	}
	
	/**
	 * Gets the mbpn substring based on sub assembly rule configuration.
	 *
	 * @param startProductIdPrefix the start product id prefix
	 * @param targetSpecCode the target spec code
	 * @param ruleName the rule name
	 * @return the mbpn substring
	 */
	private String getMbpnSubstring(String startProductIdPrefix, String targetSpecCode, String ruleName ){
		if (StringUtils.isEmpty(targetSpecCode)) return "";
		
		String[] ruleTokens = ruleName.split(":");
		if (ruleTokens.length ==2){
			int pos = Integer.parseInt(ruleTokens[0].substring(ruleTokens[0].length()-1));
			int len = Integer.parseInt(ruleTokens[1]);
			if (StringUtils.isEmpty(targetSpecCode)) 
				return StringUtils.leftPad("", len, " "); //if empty then return spaces for that length
			startProductIdPrefix = startProductIdPrefix+targetSpecCode.substring(pos,len);
		} else {
			startProductIdPrefix = startProductIdPrefix+targetSpecCode;
		}		
		return startProductIdPrefix;
		
	}
	// Check if current demand type matches with any of the configured Hold Demand Types
	private boolean isNotHoldDemandType(String currentLotDemandType) {
		
		String demandTypesProperty = getPropertyBean().getNotHoldDemandTypes();
		if (StringUtils.isEmpty(demandTypesProperty)) return true;
		
		String[] demandTypes = demandTypesProperty.split(":");
		
		for(String demandType:demandTypes){
			if(demandType.trim().equalsIgnoreCase(currentLotDemandType)) return true;
		}
		return false;
	}

	public ProductionLotDto getProductionLot(Gpp631Dto dto631) {
		ProductionLotDto productionLotDto = new ProductionLotDto();
		productionLotDto.setProductionLot(dto631.getProductionLot());
		productionLotDto.setPlanCode(dto631.getPlanCode());
		productionLotDto.setPlantCode(dto631.getPlantCode());
		productionLotDto.setLotNumber(dto631.getLotNumber());
		productionLotDto.setLineNo(dto631.getLineNo());
		productionLotDto.setProcessLocation(dto631.getProcessLocation());
		productionLotDto.setProductSpecCode(dto631.getProductSpecCode());
		productionLotDto.setDemandType(dto631.getDemandType());
    	//productionLotDto.lotSize(dto631.getProdOrderQty());
    	String prodDate = dto631.getProductionDate();
    	if(prodDate != null && prodDate.length() == 8) {
    		try {
    			productionLotDto.setProductionDate(new Date(MsipUtil.sdf1.parse(prodDate).getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	return productionLotDto;
    }
}
