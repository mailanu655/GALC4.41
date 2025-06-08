package com.honda.vios.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.MCOrderStructureDao;
import com.honda.galc.dao.conf.MCOrderStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.conf.BaseMCOrderStructure;
import com.honda.galc.entity.conf.MCOrderStructure;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPoint;
import com.honda.galc.entity.conf.MCOrderStructureForProcessPointId;
import com.honda.galc.entity.conf.MCOrderStructureId;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.enumtype.StructureCompareStatus;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.CompareStructureService;
import com.honda.galc.service.vios.StructureCreateService;
import com.honda.galc.vios.dto.PddaPlatformDto;
import com.honda.galc.vios.dto.StructureCompareDto;

public class StructureCreateServiceImpl implements StructureCreateService {
	
	@Autowired
	private MCOrderStructureDao mCOrderStructureDao;
	
	@Autowired
	private MCOrderStructureForProcessPointDao mcOrderStructureForProcessPointDao;
	
	@Autowired
	private MCStructureDao mCStructureDao;

	
	@Override
	public MCOrderStructure findOrCreateOrderStructure(String orderNo,
			String divisionId, PddaPlatformDto pddaPlatform) throws Exception {
		MCOrderStructure orderStructure = null;
		orderStructure = mCOrderStructureDao.findByKey(new MCOrderStructureId(orderNo, divisionId));
		if(orderStructure == null){
			try {
				PreProductionLot preProdLot = ServiceFactory.getDao(PreProductionLotDao.class).findByKey(orderNo);
				return (MCOrderStructure)structureCreate(orderNo, preProdLot.getProductSpecCode(), divisionId, pddaPlatform);			
			} catch (Exception e) {
				if(e.getMessage()!=null && e.getMessage().equalsIgnoreCase("No Structure for Division Id")){
					throw new Exception("No Structure for Division Id");
				}else
					throw e;
			}
		}else{
			return orderStructure;
		}
	}
	
	@Override
	public MCOrderStructureForProcessPoint findOrCreateOrderStructureForProcessPoint(String orderNo,
			String divisionId, String processPointId, String mode, PddaPlatformDto pddaPlatform) throws Exception {
		MCOrderStructureForProcessPoint orderStructure = null;
		orderStructure = mcOrderStructureForProcessPointDao.findByKey(new MCOrderStructureForProcessPointId(orderNo, processPointId));
		if(orderStructure == null){
			try {
				PreProductionLot preProdLot = ServiceFactory.getDao(PreProductionLotDao.class).findByKey(orderNo);
				return (MCOrderStructureForProcessPoint)structureCreate(orderNo, preProdLot.getProductSpecCode(), divisionId, processPointId, mode, pddaPlatform);			
			} catch (Exception e) {
				if(e.getMessage()!=null && e.getMessage().equalsIgnoreCase("No Structure for Division Id")){
					throw new Exception("No Structure for Division Id");
				}else
					throw e;
			}
		}else{
			return orderStructure;
		}
	}
	
	@Override
	public MCStructure findOrCreateStructure(String productSpecCode,
			String divisionId, PddaPlatformDto pddaPlatform) throws Exception {		
		return (MCStructure)findOrCreateStructure(productSpecCode, divisionId, null, StructureCreateMode.DIVISION_MODE.toString(), pddaPlatform);
	}
	
	@Override
	public MCStructure findOrCreateStructure(String productSpecCode,
			String divisionId, String processPointId, String mode, PddaPlatformDto pddaPlatform) throws Exception {		
			try {				
				return (MCStructure)structureCreate(StringUtils.EMPTY, productSpecCode, divisionId, processPointId, mode, pddaPlatform);			
			} catch (Exception e) {
				if(e.getMessage()!=null && e.getMessage().equalsIgnoreCase("No Structure for Division Id")){
					throw new Exception("No Structure for Division Id");
				}else
					throw e;
			}	
	}
	
	public Object structureCreate(String orderNo, String productSpecCode, String divisionId, PddaPlatformDto pddaPlatform) throws Exception{
		return structureCreate(orderNo, productSpecCode, divisionId, null, StructureCreateMode.DIVISION_MODE.toString(), pddaPlatform);
	}
	
	@Transactional
	public Object structureCreate(String orderNo, String productSpecCode, String divisionId, String processPointId, String mode, PddaPlatformDto pddaPlatform) throws Exception{
		String PRODUCT_SPEC_CODE_TYPE = "PRODUCT";
		/* Structure is created on 2 cases. 1: Only PDDA and PDDA + MBPN 2: PDDA with FIF.
		 * A special query is fired against BOM_TBX to check whether the given prod spec code has tied with any FIF code or not..
		 * If it has tied then new structure will collect all FIF related operations for the product spec code. 
		 * if it has not tied then we should consider as non fif spec code (Only PDDA) or MBPN spec code. 
		 * */

		// Get max rev structure for spec code and division id
		Set<MCStructure> currentStructureLst = null;
		Set<MCStructure> newStructures = null;
		if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.DIVISION_MODE.toString())){
			currentStructureLst = mCStructureDao.getMaxRevStrucForSpecCodeAndDivId(productSpecCode, divisionId);
		}else if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.PROCESS_POINT_MODE.toString())){
			currentStructureLst = mCStructureDao.getMaxRevStrucForSpecCodeAndProcessPointId(productSpecCode, processPointId);
		}
		List<String> specCodeLst = new ArrayList<String>();
		//Adding product spec code
		specCodeLst.add(productSpecCode);
		List<String> fifSpecCodeLst = mCStructureDao.getMatchingFIFSpecCode(productSpecCode);
		
		if(fifSpecCodeLst != null && fifSpecCodeLst.size()> 0){

			/* for a given product spec code, if there are any FIF code found in BOM_TBX then structure will be created for all attached FIF spec codes
			 * There are chances that for a given spec code more then one FIF spec code mapped to it*/
			
			/* Very rare case, GPCS would have exported order details in GAL212TBX, but PDDA wouldn't have exported model details to VIOS or the exported model had not been configured via Team leader screen,  then basic structure query will return null
			 * We don't need to proceed structure create for FIF  */
			
			specCodeLst.addAll(fifSpecCodeLst);
		} 
		

		//Create delimited string for spec code list
		String fifSpecCodes = Delimiter.VERTICAL_BAR + StringUtils.join(specCodeLst, Delimiter.VERTICAL_BAR);
		// Create new structure for spec code
		if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.DIVISION_MODE.toString())){
			newStructures = mCStructureDao.createStructureForProdSpecAndDivisionId(productSpecCode, fifSpecCodes, 
					divisionId, mCStructureDao.getMaxRevision()+1, PRODUCT_SPEC_CODE_TYPE, pddaPlatform);
		}else if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.PROCESS_POINT_MODE.toString())){
			newStructures = mCStructureDao.createStructureForProdSpecAndProcessPoint(productSpecCode, fifSpecCodes, 
					mCStructureDao.getMaxRevision()+1, PRODUCT_SPEC_CODE_TYPE, processPointId, pddaPlatform);
		}
		if(newStructures == null || newStructures.size() == 0){
			throw new Exception("No Structure for Division Id");
		}	
		if((currentStructureLst == null || currentStructureLst.size()==0) && (newStructures == null || newStructures.size() == 0)){
			throw new Exception("No Structure for Division Id");
		}else{
			MCStructure struct = null;
			BaseMCOrderStructure baseOrderStruct = null;
			if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.DIVISION_MODE.toString())){
				MCOrderStructure orderStruct;
				if(currentStructureLst == null || currentStructureLst.size() == 0){
					orderStruct = saveStructure(new ArrayList<MCStructure>(newStructures), orderNo);
					struct = new ArrayList<MCStructure>(newStructures).iterator().next();
				}else{
					List<StructureCompareDto> comparedResultDto = ServiceFactory.getService(CompareStructureService.class).compareStructuresAtProdSpecLevel(new ArrayList<MCStructure>(newStructures), new ArrayList<MCStructure>(currentStructureLst));
							
					if(!isStructureSame(comparedResultDto)){
						orderStruct = saveStructure(new ArrayList<MCStructure>(newStructures), orderNo);
						struct = new ArrayList<MCStructure>(newStructures).iterator().next();
					}else{
						orderStruct = saveOrderStructure(orderNo, currentStructureLst.iterator().next());
						struct = currentStructureLst.iterator().next();
					}
				}
				baseOrderStruct = orderStruct;
			}else if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.PROCESS_POINT_MODE.toString())){
				MCOrderStructureForProcessPoint orderStruct;
				
				if(currentStructureLst == null || currentStructureLst.size() == 0){
					orderStruct = saveStructureProcessPoint(new ArrayList<MCStructure>(newStructures), orderNo);
					struct = new ArrayList<MCStructure>(newStructures).iterator().next();
				}else{
					List<StructureCompareDto> comparedResultDto = ServiceFactory.getService(CompareStructureService.class).compareStructuresAtProdSpecLevel(new ArrayList<MCStructure>(newStructures), new ArrayList<MCStructure>(currentStructureLst));
							
					if(!isStructureSame(comparedResultDto)){
						orderStruct = saveStructureProcessPoint(new ArrayList<MCStructure>(newStructures), orderNo);
						struct = new ArrayList<MCStructure>(newStructures).iterator().next();
					}else{
						orderStruct = saveOrderStructureProcessPoint(orderNo, currentStructureLst.iterator().next());
						struct = currentStructureLst.iterator().next();
					}
				}
				baseOrderStruct = orderStruct;
			}
			if (StringUtils.isBlank(orderNo)) {
				return struct;
			}
			else {
				return baseOrderStruct;
			}
		}
	}
	
	private boolean isStructureSame(List<StructureCompareDto> comparedResult){
		boolean isStructureSame = true;
		
		try {
			List<StructureCompareDto> comparedStructureResult = comparedResult;
			
			for(StructureCompareDto StructureCompareDto : comparedStructureResult) {
				
				if(!StructureCompareStatus.SAME.equals(StructureCompareDto.getProcessPtCompareSts()) || !StructureCompareStatus.SAME.equals(StructureCompareDto.getDivIdCompareSts()) || !StructureCompareStatus.SAME.equals(StructureCompareDto.getOpNameCompareSts()) 
						|| !StructureCompareStatus.SAME.equals(StructureCompareDto.getOpRevCompareSts()) || (StructureCompareDto.getPartIdCompareSts() != null && !StructureCompareStatus.SAME.equals(StructureCompareDto.getPartIdCompareSts()) )
						|| (StructureCompareDto.getPartRevCompareSts()!= null && !StructureCompareStatus.SAME.equals(StructureCompareDto.getPartRevCompareSts()))){
					isStructureSame = false;
				}
				
				if(!isStructureSame) break;
			}
		} catch (Exception e) {
			Logger.getLogger().error("Exception caught while processing record in compareMCStructures :: " + e.getMessage());
		}
		
		return isStructureSame;
	}
	
	@Transactional
	public MCOrderStructure saveStructure(List<MCStructure> structureList, String orderNo) {
		structureList = mCStructureDao.saveAll(new ArrayList<MCStructure>(structureList));
		MCStructure structure = structureList.iterator().next();
		
		if (!StringUtils.isBlank(orderNo)) {
			return saveOrderStructure(orderNo, structure);		
		}
		else {
			return null;
		}
	}
	
	@Transactional
	public MCOrderStructureForProcessPoint saveStructureProcessPoint(List<MCStructure> structureList, String orderNo) {
		structureList = mCStructureDao.saveAll(new ArrayList<MCStructure>(structureList));
		MCStructure structure = structureList.iterator().next();
		
		if (!StringUtils.isBlank(orderNo)) {
			return saveOrderStructureProcessPoint(orderNo, structure);		
		}
		else {
			return null;
		}
	}
	
	public MCOrderStructure saveOrderStructure(String orderNo, MCStructure structure) {
		MCOrderStructure mcOrdStru = new MCOrderStructure();
		MCOrderStructureId mcOrdStruId = new MCOrderStructureId(orderNo, structure.getId().getDivisionId());
		mcOrdStru.setProductSpecCode(structure.getId().getProductSpecCode());
		mcOrdStru.setStructureRevision(structure.getId().getRevision());
		mcOrdStru.setId(mcOrdStruId);
		return mCOrderStructureDao.save(mcOrdStru);
	}
	
	public MCOrderStructureForProcessPoint saveOrderStructureProcessPoint(String orderNo, MCStructure structure) {
		MCOrderStructureForProcessPoint mcOrdStru = new MCOrderStructureForProcessPoint();
		MCOrderStructureForProcessPointId mcOrdStruId = new MCOrderStructureForProcessPointId(orderNo, structure.getId().getProcessPointId());
		mcOrdStru.setProductSpecCode(structure.getId().getProductSpecCode());
		mcOrdStru.setStructureRevision(structure.getId().getRevision());
		mcOrdStru.setDivisionId(structure.getId().getDivisionId());
		mcOrdStru.setId(mcOrdStruId);
		return mcOrderStructureForProcessPointDao.save(mcOrdStru);
	}
}
