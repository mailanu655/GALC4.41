package com.honda.vios.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.enumtype.StructureCompareStatus;
import com.honda.galc.service.vios.CompareStructureService;
import com.honda.galc.vios.dto.StructureCompareDto;

public class CompareStructureServiceImpl implements CompareStructureService {


	

	public List<StructureCompareDto>  compareStructuresAtProdSpecLevel(List<MCStructure> newStructure, List<MCStructure> oldStructure){
		
		List<StructureCompareDto> structureCompareResult = new ArrayList<StructureCompareDto>();
		
		/* Get common & added / delete process points from both list */
		HashSet<String> availableProcessPt = new HashSet<String>();
		availableProcessPt.addAll(getProcessPoints(newStructure));
		availableProcessPt.addAll(getProcessPoints(oldStructure));
		
		Iterator<String> availableProcessPtItr = availableProcessPt.iterator();
		while (availableProcessPtItr.hasNext()) {
			String processPoint = (String) availableProcessPtItr.next();
			
			/* Get all the operations related to the process point from both list*/
			List<MCStructure> newOperLstForProcessPt = new ArrayList<MCStructure>(getAllOperationsForProcessPt(processPoint, newStructure));
			List<MCStructure> oldOperLstForProcessPt = new ArrayList<MCStructure>(getAllOperationsForProcessPt(processPoint, oldStructure));
			
			if(newOperLstForProcessPt.size() == 0 || oldOperLstForProcessPt.size() == 0){
				/*Check for Added or Deleted process points*/
				structureCompareResult.addAll(checkForProcessPointAddedOrDeleted(newOperLstForProcessPt, oldOperLstForProcessPt));
			}else{
				structureCompareResult.addAll(compareStructureForProcessPoint(newOperLstForProcessPt, oldOperLstForProcessPt));
				structureCompareResult.addAll(checkForOperationDeletion(oldOperLstForProcessPt, newOperLstForProcessPt));
			}
			
			newOperLstForProcessPt = null;
			oldOperLstForProcessPt = null;
		}
		
		return structureCompareResult;
	}
	
	private List<StructureCompareDto> compareStructureForProcessPoint(List<MCStructure> newStructureLst, List<MCStructure> oldStructureLst){
		List<StructureCompareDto> compareResultLst = new ArrayList<StructureCompareDto>();
		boolean opNameFound;
		boolean partIdFound;
		boolean checkForPart;
		StructureCompareDto compareDto = null;
		
		for(MCStructure newMcStructure : newStructureLst){
			opNameFound = false;
			partIdFound = false;
			checkForPart = false;
			compareDto = null;
			
			compareDto = loadStructureObject(newMcStructure);
			compareDto.setProcessPtCompareSts(StructureCompareStatus.SAME);
			
			for(MCStructure oldMcStructure : oldStructureLst){
				
				if(newMcStructure.getId().getOperationName().equals(oldMcStructure.getId().getOperationName()) && (newMcStructure.getId().getPartId().trim().length() == 0)){
					/* Operation name matched,but not associated with parts*/
					opNameFound = true;
					compareDto.setOpNameCompareSts(StructureCompareStatus.SAME);
					if(newMcStructure.getId().getOperationRevision() == oldMcStructure.getId().getOperationRevision()){
						compareDto.setOpRevCompareSts(StructureCompareStatus.SAME);
					}else{
						compareDto.setOpRevCompareSts(StructureCompareStatus.UPDATED);
					}
					
					/*  Checks whether division id modified with in the process point & operation or not */ 
					if(newMcStructure.getId().getDivisionId().equals(oldMcStructure.getId().getDivisionId()))
						compareDto.setDivIdCompareSts(StructureCompareStatus.SAME);
					else
						compareDto.setDivIdCompareSts(StructureCompareStatus.UPDATED);
					
				}else if(newMcStructure.getId().getOperationName().equals(oldMcStructure.getId().getOperationName()) && (newMcStructure.getId().getPartId().trim().length() > 0)){
					/* Operation name matched and has parts*/
					opNameFound = true;
					checkForPart = true;
					compareDto.setOpNameCompareSts(StructureCompareStatus.SAME);
					
					if(newMcStructure.getId().getOperationRevision() == oldMcStructure.getId().getOperationRevision()){
						compareDto.setOpRevCompareSts(StructureCompareStatus.SAME);
					}else{
						compareDto.setOpRevCompareSts(StructureCompareStatus.UPDATED);
					}
					
					/*  Checks whether division id modified with in the process point, operation & part  or not */ 
					if(newMcStructure.getId().getDivisionId().equals(oldMcStructure.getId().getDivisionId()))
						compareDto.setDivIdCompareSts(StructureCompareStatus.SAME);
					else
						compareDto.setDivIdCompareSts(StructureCompareStatus.UPDATED);
					
					if(newMcStructure.getId().getPartId().equals(oldMcStructure.getId().getPartId())){
						if(newMcStructure.getId().getPartId().equals(oldMcStructure.getId().getPartId())){
							partIdFound = true;
							compareDto.setPartIdCompareSts(StructureCompareStatus.SAME);
							if(newMcStructure.getId().getPartRevision() == oldMcStructure.getId().getPartRevision()){
								compareDto.setPartRevCompareSts(StructureCompareStatus.SAME);
							}else{
								compareDto.setPartRevCompareSts(StructureCompareStatus.UPDATED);
							}
						}else{
							partIdFound = false;
						}
						break;
					}
				}
			}
			
			if(!opNameFound){
				compareDto.setOpNameCompareSts(StructureCompareStatus.ADDED);
			}
			if(!partIdFound && checkForPart){
				compareDto.setPartIdCompareSts(StructureCompareStatus.ADDED);
			}
			compareResultLst.add(compareDto);
		}
		
		
		compareResultLst.addAll(checkForOperationAndPartDeletion(oldStructureLst, newStructureLst));
		return compareResultLst;
	}
	
	private List<StructureCompareDto> checkForOperationAndPartDeletion(List<MCStructure> oldStructureLst, List<MCStructure> newStructureLst){
		List<StructureCompareDto> deleteOperLst = new ArrayList<StructureCompareDto>();
		boolean partFound = false;
		
		for(MCStructure oldMcStructure : oldStructureLst){
			partFound = false;
			if(oldMcStructure.getId().getPartId().trim().length()>0){
				for(MCStructure newMcStructure : newStructureLst){
					if(oldMcStructure.getId().getOperationName().equals(newMcStructure.getId().getOperationName()) && oldMcStructure.getId().getPartId().equals(newMcStructure.getId().getPartId())){
						partFound = true;
					}
				}
				if(!partFound){
					StructureCompareDto compareDto = loadStructureObject(oldMcStructure);
					compareDto.setProcessPtCompareSts(StructureCompareStatus.SAME);
					compareDto.setOpNameCompareSts(StructureCompareStatus.SAME);
					compareDto.setPartIdCompareSts(StructureCompareStatus.DELETED);
					deleteOperLst.add(compareDto);
				}
			}
		}
		
		
		return deleteOperLst;
	}
	
	private List<StructureCompareDto> checkForOperationDeletion(List<MCStructure> oldStructureLst, List<MCStructure> newStructureLst){
		List<StructureCompareDto> deleteOperLst = new ArrayList<StructureCompareDto>();
		
		boolean operationFound = false;
		
		for(MCStructure oldMcStructure : oldStructureLst){
			operationFound = false;
			
			for(MCStructure newMcStructure : newStructureLst){
				if(oldMcStructure.getId().getOperationName().equals(newMcStructure.getId().getOperationName()))
					operationFound = true;
			}
			if(!operationFound){
				StructureCompareDto compareDto = loadStructureObject(oldMcStructure);
				compareDto.setProcessPtCompareSts(StructureCompareStatus.SAME);
				compareDto.setOpNameCompareSts(StructureCompareStatus.DELETED);
				deleteOperLst.add(compareDto);
			}
			
		}
		
		return deleteOperLst;
	}
	
	private List<StructureCompareDto> checkForProcessPointAddedOrDeleted(List<MCStructure> newStructure, List<MCStructure> oldStructure){
		List<StructureCompareDto> resultStructure = new ArrayList<StructureCompareDto>();
		
		if(oldStructure.size() == 0){
			for(MCStructure structure : newStructure){
				StructureCompareDto struCompDto = loadStructureObject(structure);
				struCompDto.setProcessPtCompareSts(StructureCompareStatus.ADDED);
				resultStructure.add(struCompDto);
			}
		}
		
		if(newStructure.size() == 0){
			for(MCStructure structure : oldStructure){
				StructureCompareDto struCompDto = loadStructureObject(structure);
				struCompDto.setProcessPtCompareSts(StructureCompareStatus.DELETED);
				resultStructure.add(struCompDto);
			}
		}
		
		return resultStructure;
	}
	
	private List<MCStructure> getAllOperationsForProcessPt(String processPoint, List<MCStructure> structureLst){
		List<MCStructure> availableOperations = new ArrayList<MCStructure>();
		
		for(MCStructure mcStructure : structureLst){
			if(processPoint.equals(mcStructure.getId().getProcessPointId()))
				availableOperations.add(mcStructure);
		}
		
		return availableOperations;
	}
	
	private HashSet<String> getProcessPoints(List<MCStructure> structureLst){
		HashSet<String> availableProcessPts = new HashSet<String>();
		
		for(MCStructure mcStructure : structureLst){
			availableProcessPts.add(mcStructure.getId().getProcessPointId());
		}
		return availableProcessPts;
	}
	
	private StructureCompareDto loadStructureObject(MCStructure structure){
		return new StructureCompareDto(structure.getId().getProductSpecCode(), structure.getId().getRevision(), structure.getId().getProcessPointId(), structure.getId().getOperationName(), structure.getId().getOperationRevision(), structure.getId().getPartId(), structure.getId().getPartRevision(), structure.getId().getDivisionId());
	}



}
