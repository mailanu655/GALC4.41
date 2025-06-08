package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.enumtype.InhouseRecordOrderStatus;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.dto.GPP631DTO;
import com.honda.galc.oif.dto.PreProductionLotDTO;
import com.honda.galc.oif.dto.ProductionLotDTO;
import com.honda.galc.oif.property.InHouseSchedulePropertyBean;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ProductStampingSeqGenService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.StringUtil;

import static com.honda.galc.service.ServiceFactory.getService;

/**
 * 
 * <h3>InHousePriorityPlanGPP631Task Class description</h3>
 * <p> InHousePriorityPlanGPP631Task description </p>
 * In House Priority Plan task is an OIF task for frame and transmission plants, which executes every day 
 * It retrieves data from incoming interface **-GPC#***GAL#GPP631 (EY-GPC#PMCGAL#GPP631 for PMC) 
 * to get the priority production plans. 
 * Every priority plan is converted to production lot in GAL217TBX and preproduction lot in GAL212TBX.  
 * It also builds MBPN_TBX 
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
 * @author Larry Karpov 
 * Jan 29, 2014
 *
 *
 */
/** * * 
* @version 2
* @author Gangadhararao Gadde 
* @since Aug 08, 2017
*/
public class InHousePriorityPlanGPP631Task<T extends GPP631DTO> extends BasePlanCodeTask<GPP631DTO> implements IEventTaskExecutable{
	InHouseSchedulePropertyBean propBean;
	boolean useNextProductionLot = true;
	int txTimeout = 0; //default=300, read from propertyBean
	PreProductionLotDao ppLotDao = getDao(PreProductionLotDao.class);
	ProductionLotDao pLotDao = getDao(ProductionLotDao.class);
	MbpnDao mbpnDao = getDao(MbpnDao.class);
	Timestamp jobTs = null;
	protected ArrayList<GPP631DTO> listOfPlan631 = new ArrayList<GPP631DTO>();
	private int totalRecords = 0, failedRecords=0;
	
	public InHousePriorityPlanGPP631Task(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		logger.info("start to process priority plan");
		
		try{
			initData(GPP631DTO.class);
			
			if(getFilesFromMQ() == 0)
				return;

			propBean = PropertyService.getPropertyBean(InHouseSchedulePropertyBean.class, componentId);
			useNextProductionLot = propBean.getUseNextProductionLot();
			jobTs = getService(GenericDaoService.class).getCurrentDBTime();
			processInHousePriorityPlan();
			
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		}catch(Exception e) {
			logger.emergency(e,"Unexpected exception occured");
			errorsCollector.emergency(e,"Unexpected exception occured");
		} finally {
			setIncomingJobCount(totalRecords-failedRecords, failedRecords, receivedFileList);
			errorsCollector.sendEmail();
		}
	}
	
	protected void processInHousePriorityPlan() {
		if(useNextProductionLot)  {
			Map<String, PreProductionLotDTO> tails = getTailsByPlanCode();
			
			if(tails == null || tails.isEmpty()) {
				setIncomingJobStatus(OifRunStatus.INCORRECT_TAIL_FOR_ATLEAST_ONE_PLAN);
				throw new TaskException(String.format("Tails are not correct for at least one plan code: %s", getPlanCodes()));
			}
		}
		
		for (int count = 0; count < receivedFileList.length; count++) {
			String receivedFile = receivedFileList[count];
			
			if (receivedFile == null || receivedFile.trim().equals(""))
				continue;

			try {
				Map<String, ArrayList<PreProductionLot>> ppLotMap = new HashMap<String, ArrayList<PreProductionLot>>();
				Map<String, ArrayList<ProductionLot>> pLotMap = new HashMap<String, ArrayList<ProductionLot>>();
				Map<String, ArrayList<PreProductionLotDTO>> ppLotDTOMap = new HashMap<String, ArrayList<PreProductionLotDTO>>();
				Map<String, String> processLocationByPlanCode = new HashMap<String, String>();
				
				buildPreProductionLots(receivedFile, ppLotMap, pLotMap, ppLotDTOMap, processLocationByPlanCode);

				if(ppLotMap != null && !ppLotMap.isEmpty()) {
					if(useNextProductionLot)
						saveLots(ppLotMap, pLotMap, ppLotDTOMap, processLocationByPlanCode);
					else
						saveLotsBySequence(ppLotMap, pLotMap, ppLotDTOMap);
				}
			} catch (Exception e) {
				logger.error(e, "Exception processing InHouse priority plan data");
				errorsCollector.error(e,  "Exception processing InHouse priority plan data");
			}

			logger.info(String.format("File %d (%s) of %d total file(s) is processed.", count + 1, receivedFile, receivedFileList.length));
		}
		
		updateLastProcessTimestamp(jobTs);
		logger.info("In House Priority plan data processed.");
	}
	
	@PersistenceContext
	public void saveLots(
			Map<String, ArrayList<PreProductionLot>> ppLotMap,
			Map<String, ArrayList<ProductionLot>> prodLotMap, 
			Map<String, ArrayList<PreProductionLotDTO>> ppLotDTOMap,
			Map<String, String> processLocationByPlanCode) {
		
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
			//now, restore the full list of waiting records to be deleted to fix *1 above
			ppLotListWaiting = ppLotDao.findAllWaitingByPlanCode(planCode);
			
			//delete record and corresponding 217/prod-lot record if SEND_STATUS==0
			int deleteCount = 0;

			for(PreProductionLot ppLot : ppLotListWaiting)  {
				if(ppLot == null) 
					continue;
				
				if(!ppLot.getSendStatus().equals(PreProductionLotSendStatus.WAITING))
					continue; //don't do anything if SEND_STATUS != 0
				
				if ( propBean.isAutomaticHold() && (!isNotHoldDemandType(ppLot.getDemandType()))){
					continue;
				}
				ppLotDao.delete(ppLot.getProductionLot());
				pLotDao.delete(ppLot.getProductionLot());
				
				//deleting a lot the system should delete all of the MBPN products already created for the lot
				removeAllMbpnProducts(ppLot.getProductionLot());
				removeAllProductStampingSequence(ppLot.getProductionLot());
				
				deleteCount++;
			}

			logger.info(String.format("Plan code: %s - deleted %d", planCode, deleteCount));
			
			//insert all the new pre-prod-lot records for that plan code
			ArrayList<PreProductionLot> newPPLotList = ppLotMap.get(planCode);
			ArrayList<ProductionLot> newProdLotList = prodLotMap.get(planCode);
			ArrayList<PreProductionLotDTO> newProdLotDTOList = ppLotDTOMap.get(planCode);
			
			for(PreProductionLot newPPLot : newPPLotList)  {
				Mbpn newMbpn = createMBPN(newPPLot);
				mbpnDao.save(newMbpn);
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
			
			if (propBean.isGenerateProductionId() ) {
				for(PreProductionLotDTO newPPLotDTO : newProdLotDTOList)  {
					createMbpnProduct(newPPLotDTO);
					
				}
			}
			
		}
	

	}
	
	private void removeAllProductStampingSequence(String productionLot) {
		try{
			ProductStampingSequenceDao productStampingDao = getDao(ProductStampingSequenceDao.class);
			List<ProductStampingSequence> productStampingSequenceList = productStampingDao.findAllByProductionLot(productionLot);
			 productStampingDao.removeAll(productStampingSequenceList);
		}catch(Exception e){
			logger.info("error deleting ProductStampingSequence records for productionLot"+ productionLot);
		}
	}

	private void removeAllMbpnProducts(String productionLot) {
		try{
				MbpnProductDao mbpnProductDao = getDao(MbpnProductDao.class);
				List<MbpnProduct> mbpnProducts = mbpnProductDao.findAllByOrderNo(productionLot);
				mbpnProductDao.removeAll(mbpnProducts);
			}catch(Exception e){
				logger.info("error deleting MbpnProducts for productionLot"+ productionLot);
			}
	}

	@PersistenceContext
	public void saveLotsBySequence(
			Map<String, ArrayList<PreProductionLot>> ppLotMap,
			Map<String, ArrayList<ProductionLot>> prodLotMap, 
			Map<String, ArrayList<PreProductionLotDTO>> ppLotDTOMap) {
		
		ArrayList<Mbpn> mbpnList = new ArrayList<Mbpn>();
		
		if(ppLotMap == null || ppLotMap.isEmpty())
			return;
		
		for(String planCode : ppLotMap.keySet())  {
			//delete record and corresponding 217/prod-lot record if SEND_STATUS==0
			int deleteCount = 0;

			if ( propBean.isAutomaticHold()){
				// only delete not hold demand types that are in waiting status
				String demandTypes = propBean.getNotHoldDemandTypes();
				getDao(PreProductionLotDao.class).deleteByPlanCodeSendStatusNotHoldDemandType(planCode, PreProductionLotSendStatus.WAITING.getId(), demandTypes);	
				
			} else {
				List<PreProductionLot> ppLotListWaiting = ppLotDao.findAllWaitingByPlanCode(planCode);
				deleteCount = pLotDao.deleteByPlanCodeSendStatus(planCode, PreProductionLotSendStatus.WAITING.getId());
				deleteCount = ppLotDao.deleteByPlanCodeSendStatus(planCode, PreProductionLotSendStatus.WAITING.getId());		
				
				
				logger.info(String.format("Plan code: %s - deleted %d", planCode, deleteCount));
				
				for(PreProductionLot ppLot : ppLotListWaiting)  {
					//deleting a lot the system should delete all of the MBPN products already created for the lot
					removeAllMbpnProducts(ppLot.getProductionLot());
					removeAllProductStampingSequence(ppLot.getProductionLot());
				}
				
			}
			
			//insert all the new pre-prod-lot records for that plan code
			ArrayList<PreProductionLot> newPPLotList = ppLotMap.get(planCode);
			ArrayList<ProductionLot> newProdLotList = prodLotMap.get(planCode);
			ArrayList<PreProductionLotDTO> newProdLotDTOList = ppLotDTOMap.get(planCode);
			
			for(PreProductionLot newPPLot : newPPLotList)  {
				Mbpn newMbpn = createMBPN(newPPLot);
				mbpnDao.save(newMbpn);
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

			if (propBean.isGenerateProductionId() ) {
				for(PreProductionLotDTO newPPLotDTO : newProdLotDTOList)
					createMbpnProduct(newPPLotDTO);
			}
		}
		
	}
	
	private void buildPreProductionLots(
			String receivedFile, 
			Map<String, ArrayList<PreProductionLot>> ppLotMap,
			Map<String, ArrayList<ProductionLot>> pLotMap, 
			Map<String, ArrayList<PreProductionLotDTO>> ppLotDTOMap,
			Map<String, String> processLocationByPlanCode) throws IllegalAccessException, InstantiationException {

		Map<String, ArrayList<GPP631DTO>> receivedRecordsByPlanCode = getRecordsByPlanCode(receivedFile);
		
		if (receivedRecordsByPlanCode.isEmpty())
			return;
		
		for(String planCode : receivedRecordsByPlanCode.keySet()) {
			ArrayList<PreProductionLot> ppLotList = new ArrayList<PreProductionLot>();
			ArrayList<ProductionLot> pLotList = new ArrayList<ProductionLot>(); 
			ArrayList<PreProductionLotDTO> ppLotDTOList = new ArrayList<PreProductionLotDTO>();
			String processLocation = new String();
			
			if (buildPreProductionLotsByPlanCode(
					receivedRecordsByPlanCode.get(planCode),
					planCode,
					ppLotList,
					pLotList,
					ppLotDTOList,
					processLocation)) {
				ppLotMap.put(planCode, ppLotList);
				pLotMap.put(planCode, pLotList);
				ppLotDTOMap.put(planCode, ppLotDTOList);
				processLocationByPlanCode.put(planCode, processLocation);
			}
		}
	}

	private Boolean buildPreProductionLotsByPlanCode(
			ArrayList<GPP631DTO> receivedRecordsByPlanCode,
			String planCode,
			ArrayList<PreProductionLot> ppLotList,
			ArrayList<ProductionLot> pLotList, 
			ArrayList<PreProductionLotDTO> ppLotDTOList,
			String processLocation
			) {
		Set<String> dupCheck = new HashSet<String>();
		Map<String, PreProductionLot> previousPplByPlanCode = new HashMap<String, PreProductionLot>();
		Map<String, Double> sequences = new HashMap<String, Double>();

		totalRecords = receivedRecordsByPlanCode.size();
		for(GPP631DTO plan631 : receivedRecordsByPlanCode) {
			PreProductionLot ppLot = null;
			PreProductionLotDTO ppldto = new PreProductionLotDTO(plan631, useNextProductionLot);
			
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

				if(!dupCheck.add(ppLot.getLotNumber())) {
					String errorMessage = String.format("Duplicate Order Number (%s) found in import file for plan code %s", ppLot.getLotNumber(), planCode);
					
					logger.error(errorMessage);
					errorsCollector.emergency(errorMessage);
					setIncomingJobStatus(OifRunStatus.DUPLICATE_ORDER_FOUND_IN_FILE);
					failedRecords++;
					return false;
				}
				
				processLocation= ppLot.getProcessLocation();

				PreProductionLot previousPpl = previousPplByPlanCode.get(planCode);
			
				Double maxSequence = findSequenceByNotSendStatus(planCode, sequences);
				ppLot.setSequence(maxSequence);
				sequences.put(planCode, maxSequence);
				ppldto.setSequence(maxSequence);
				
				// Check if lot to be placed on hold based on demand type
				if ( propBean.isAutomaticHold() && (!isNotHoldDemandType(ppLot.getDemandType()))){
					ppLot.setHoldStatus(0);
					ppldto.setHoldStatus(0);
					ppLot.setNextProductionLot(null);
					ppldto.setNextProductionLot(null);
					// save the lot without tying to linked list
					ppLotDao.save(ppLot);
				} else {
					ppLot.setHoldStatus(1);
					ppldto.setHoldStatus(1);
					// Only add to linked list if lot is not on hold
					if(previousPpl != null){
						previousPpl.setNextProductionLot(ppLot.getProductionLot());
						ppLotDTOList.get(ppLotDTOList.size()-1).setNextProductionLot(ppLot.getProductionLot());
					}
					
					previousPplByPlanCode.put(planCode, ppLot);
					previousPpl = ppLot;
					
					ppLotList.add(ppLot);
					ppLotDTOList.add(ppldto);
				}

				ProductionLot pLot = null;

				ProductionLotDTO pldto = createPLDTO(plan631);
				listOfPlan631.add(plan631);
				if(pldto != null)  {
					pLot = pldto.deriveProductionLot();
					pLotList.add(pLot);
				}
			}
		}
		
		return true;
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
				
				//look for an already existing MBPN (first 11 characters) if a match is found the mask id should be copied to the new MBPN_TBX record otherwise it will be blank.
				mbpn.setMaskId(getMaskId(mbpnString));
			}
		} else {
			logger.error("Can not get product Spec Code from " +  ppl);
		}
		return mbpn;
	}
	
	private String getMaskId(String mbpnString){
		List<Mbpn> mbpns = (getDao(MbpnDao.class)).findAllDescByMainNoAndClassNo(mbpnString.substring(0, 5), mbpnString.substring(5, 8));
		String maskId = "";
		for(Mbpn tempMbpn: mbpns){
			if((tempMbpn.getMbpn().substring(0, 11)).equalsIgnoreCase(mbpnString.substring(0, 11))){
				maskId = tempMbpn.getMaskId();
				break;
			}
		}
		
		return maskId;
	}

	private ProductionLotDTO createPLDTO(GPP631DTO plan631) { 
		ProductionLotDTO pldto = new ProductionLotDTO(plan631);
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
	
	private void createMbpnProduct(PreProductionLotDTO ppldto) {

		List<ProductStampingSequence> targetProductStampingSequenceList = ServiceFactory.getService(ProductStampingSeqGenService.class).createStampingSequenceList(ppldto.getProductSpecCode(), ppldto.getProcessLocation(), ppldto.derivePreProductionLot(),componentId);	
		getDao( ProductStampingSequenceDao.class ).saveAll(targetProductStampingSequenceList);
		ServiceFactory.getService(ProductStampingSeqGenService.class).updateTargetPreProdLot(ppldto.derivePreProductionLot(), targetProductStampingSequenceList.get(0).getId().getProductID(), ppldto.getProductSpecCode(), ppldto.getProcessLocation());
	}
	
	

	// Check if current demand type matches with any of the configured Hold Demand Types
	private boolean isNotHoldDemandType(String currentLotDemandType) {
		
		String demandTypesProperty = propBean.getNotHoldDemandTypes();
		if (StringUtils.isEmpty(demandTypesProperty)) return true;
		
		String[] demandTypes = demandTypesProperty.split(":");
		
		for(String demandType:demandTypes){
			if(demandType.trim().equalsIgnoreCase(currentLotDemandType)) return true;
		}
		return false;
	}
}