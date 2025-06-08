package com.honda.galc.client.product.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.MfgInstructionLevelType;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.conf.MCProductStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.dao.product.BaseProductStructureDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.entity.conf.BaseMCProductStructure;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPddaUnitRevision;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Nov 26, 2014
 */
public class ProcessModel extends AbstractProcessModel {

	private volatile ConcurrentHashMap<String, MCOperationRevision> opsMap = new ConcurrentHashMap<String, MCOperationRevision>();
	private volatile ConcurrentHashMap<String, Boolean> completedOpsMap = new ConcurrentHashMap<String, Boolean>();
	private volatile ConcurrentHashMap<String, MCOperationRevision> pendingOpsMap = new ConcurrentHashMap<String, MCOperationRevision>();
	private volatile Map<String, Boolean> specialOpsMap = new HashMap<String, Boolean >();
	private volatile ConcurrentHashMap<String, List<MCOperationPartRevision>> partsMap =  new ConcurrentHashMap<String, List<MCOperationPartRevision>>();
	private volatile ConcurrentHashMap<String, Integer> opsTimeMap =  new ConcurrentHashMap<String, Integer>();
	private volatile int currentOperationIndex;
	private MCStructureDao mcStructureDao;
	private MCProductStructureDao mcProductStructureDao;
	private MCProductStructureForProcessPointDao mcProductStructureForProcessPointDao;
	private MfgInstructionLevelType mfgInstructionLevel= MfgInstructionLevelType.ALL;
	private volatile ConcurrentHashMap<String, Boolean> processTrainingMap =  new ConcurrentHashMap<String, Boolean>();
	private StructureCreateMode mode;
	
	public ProcessModel() {
		super();
		String structureMode = PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString());
		mode = structureMode == null ? StructureCreateMode.DIVISION_MODE : StructureCreateMode.get(structureMode);
	}
	
	public ConcurrentHashMap<String, MCOperationRevision> getPendingOpsMap() {
		return pendingOpsMap;
	}

	public void setPendingOpsMap(ConcurrentHashMap<String, MCOperationRevision> pendingOpsMap) {
		this.pendingOpsMap = pendingOpsMap;
	}

	public ConcurrentHashMap<String, Boolean> getCompletedOpsMap() {
		return completedOpsMap;
	}

	public void setCompletedOpsMap(ConcurrentHashMap<String, Boolean> completedOpsMap) {
		this.completedOpsMap = completedOpsMap;
	}
	
	

	public Map<String, Boolean> getSpecialOps() {
		return specialOpsMap;
	}

	public void setSpecialOps(Map<String, Boolean> specialOpsMap) {
		this.specialOpsMap = specialOpsMap;
	}

	public ConcurrentHashMap<String, MCOperationRevision> getOperationsMap() {
		return opsMap;
	}

	public void setOperationsMap(ConcurrentHashMap<String, MCOperationRevision> operationsMap) {
		this.opsMap = operationsMap;
	}
	
	public int getCurrentOperationIndex() {
		return currentOperationIndex;
	}

	public void setCurrentOperationIndex(int currentOperationIndex) {
		this.currentOperationIndex = currentOperationIndex;
	}
	
	public MCOperationRevision getCurrentOperation() {
		return getOperations().get(getCurrentOperationIndex());
	}
		
	public String getCurrentOperationName() {
		return getOperations().get(getCurrentOperationIndex()).getId().getOperationName();
	}
		
	/**
	 * Checks if the current operation is marked as completed.
	 *
	 * @return true, if the current operation is marked as completed
	 */
	public boolean isCurrentOperationComplete(){
		return isOperationComplete(getCurrentOperationName());
	}
	
	/**
	 * Checks if the operation is marked as completed.
	 *
	 * @param operationName the operationName
	 * @return true, if the operation is marked as completed.
	 */
	public boolean isOperationComplete(String operationName) {
		Boolean result = completedOpsMap.get(operationName);
		return result == null ? false : result;
	}
	
	/**
	 * Checks if the operation is marked as pending.
	 *
	 * @param operationName the operationName
	 */
	public boolean isOperationPending(String operationName) {
		MCOperationRevision result = pendingOpsMap.get(operationName);
		return result != null;
	}
	
	/**
	 * Mark an operation as completed.
	 *
	 * @param operationName theoperationName
	 * @param completed the completed
	 */
	public void markComplete(String operationName, boolean completed){
		//Remove the operation from pending map to make it as complete
		pendingOpsMap.remove(operationName);
		completedOpsMap.put(operationName, completed);
	}
	
	/**
	 * Mark an operation as pending.
	 *
	 * @param operationName theoperationName
	 */
	public void markPending(String operationName, MCOperationRevision operation){
		pendingOpsMap.put(operationName, operation);
	}
	
	public void addOperationsToProduct() {
		String id = (mode.equals(StructureCreateMode.DIVISION_MODE)) ? getProductModel().getDivisionId() : getProductModel().getProcessPointId();
		BaseProductStructureDao<? extends BaseMCProductStructure, ?> dao = ServiceFactory.getDao(mode.getProductStructureDaoClass());
		BaseMCProductStructure productStruct = dao.findByKey(getProductModel().getProductId(), id, 
				getProductModel().getProductSpec().getProductSpecCode());
		
		if (productStruct != null) {
			String processPointId = getProductModel().getProcessPointId(); 
			setOperationsMap(productStruct, processPointId);
			getProductModel().getProduct().setOperations(fetchOperationSeq(productStruct, processPointId));
			setOperationParts(productStruct, processPointId);
			//check if we need to display special operations
			if(getProductModel().getProperty().isHighlightSpecialOpInUnitNavigator() && getOperationsMap() != null && getOperationsMap().size()>0){
				
				InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
				setSpecialOps(installedPartDao.findSpecialUnitsInProcessPoint(processPointId, 
																				 getProductModel().getApplicationContext().getUserId(),
																				 getProductModel().getProperty().getSpecialOpCheckInDays(), 
																				 new ArrayList<MCOperationRevision>(getOperationsMap().values())));
			}
		}
	}
	
	public List<MCOperationRevision> getOperations() {
		return productModel.getProduct().getOperations();
	}
	
	protected void setOperationParts(BaseMCProductStructure productStruct, String processPointId) {
		for(MCOperationPartRevision part: fetchParts(productStruct, processPointId)) {
			MCOperationRevision op = getOperationsMap().get(part.getId().getOperationName());
			if ((op == null || op.getType() == OperationType.INSTRUCTION) && !mfgInstructionLevel.equals(MfgInstructionLevelType.ALL)) {
				continue;
			}
			
			if (!getPartsMap().containsKey(part.getId().getOperationName())) {
				List<MCOperationPartRevision> partsList = new ArrayList<MCOperationPartRevision>();
				partsList.add(part);
				getPartsMap().put(part.getId().getOperationName(), partsList);
			} else {
				getPartsMap().get(part.getId().getOperationName()).add(part);
			}
			getOperationsMap().get(part.getId().getOperationName()).getParts().add(part);
		}
		validateFifMfgParts();
	}

	/**
	 * If Multi-MFG part check flag is set,
	 * then remove default MFG part if the operation has multiple MFG parts
	 */
	protected void validateFifMfgParts() {
		if(getProductModel().getProperty().isCheckMultiMfgPart()) {
			for( String operationName: getOperationsMap().keySet()) {
				MCOperationRevision operation = getOperationsMap().get(operationName);
				if(operation!=null) {
					List<MCOperationPartRevision> mfgPartList = operation.getManufacturingMFGPartList();
					//perform validation only when there are multiple MFG parts
					if(mfgPartList.size() > 1) {
						MCOperationPartRevision defaultMfgPart = null;
						for (MCOperationPartRevision mfgPart: mfgPartList) {
							//Getting Default Manufacturing part
							if(mfgPart.getId().getPartId().equalsIgnoreCase(ApplicationConstants.DEFAULT_MFG_PART_ID)) {
								defaultMfgPart = mfgPart;
							}
						}
						//Removing default Manufacturing part if the operation has multiple manufacturing parts,
						if(defaultMfgPart!=null) {
							getPartsMap().get(operationName).remove(defaultMfgPart);
							getOperationsMap().get(operationName).getParts().remove(defaultMfgPart);
						}
					}
				}
			}
		}
	}

	protected List<MCOperationPartRevision> fetchParts(
			BaseMCProductStructure productStruct, String processPointId) {
		List<MCOperationPartRevision> operationPartsList = ServiceFactory
				.getDao(MCOperationPartRevisionDao.class).findAllParts(productStruct.getProductSpecCode(),
						productStruct.getStructureRevision(), processPointId);
		return operationPartsList;
	}

	protected void setOperationsMap(BaseMCProductStructure productStruct, String processPointId) {
		List<MCStructure> structureList = getMCStructureDao().getStructures(
				productStruct.getProductSpecCode(), processPointId,
				productStruct.getStructureRevision());
		
		if(structureList!=null) {
			for(MCStructure structure : structureList) {
				MCOperationRevision operation = structure.getMcOperationRevision();
				if (operation.getType() == OperationType.INSTRUCTION && !mfgInstructionLevel.equals(MfgInstructionLevelType.ALL)){
					continue;
				}
				operation.setStructure(structure);
				if(structure.getPddaUnitRevisions()!=null && structure.getPddaUnitRevisions().size()>0) {
					MCPddaUnitRevision pddaUnitRevision = structure.getPddaUnitRevisions().iterator().next();
					operation.setUnitNo(pddaUnitRevision.getId().getUnitNo());
					operation.setApprovedUnitMaintenanceId(pddaUnitRevision.getApprovedUnitMaintenanceId());
				}
				getOperationsMap().put(operation.getId().getOperationName(), operation);
			}
		}
	}

	protected List<MCOperationRevision> fetchOperationSeq(BaseMCProductStructure productStruct, String processPointId) {
		List<Object[]> operationsSeq = ServiceFactory.getDao(
				MCOperationRevisionDao.class).getOperationSeq(processPointId,
				productStruct.getProductSpecCode(),
				productStruct.getStructureRevision());

		List<MCOperationRevision> opsInSequence = new ArrayList<MCOperationRevision>();
		for (Object[] opName: operationsSeq) {
			MCOperationRevision op = getOperationsMap().get(opName[0].toString().trim());
			if ((op != null && op.getType() != OperationType.INSTRUCTION) || mfgInstructionLevel.equals(MfgInstructionLevelType.ALL)) {
				opsInSequence.add(op);
				opsTimeMap.put(op.getId().getOperationName().trim(), (Integer) opName[3]);
			}
		}
		return opsInSequence;
	}

	@Override
	public void reset(){
		super.reset();
		currentOperationIndex = 0;
		completedOpsMap.clear();
		pendingOpsMap.clear();
		specialOpsMap.clear();
		opsMap.clear();
		opsTimeMap.clear();
	}
	
	public MCProductStructureDao getMCProductStructureDao() {
		if(mcProductStructureDao == null)
			mcProductStructureDao = ServiceFactory.getDao(MCProductStructureDao.class);
		return mcProductStructureDao;
	}
	
	public MCProductStructureForProcessPointDao getMcProductStructureForProcessPointDao() {
		if(mcProductStructureForProcessPointDao == null)
			mcProductStructureForProcessPointDao = ServiceFactory.getDao(MCProductStructureForProcessPointDao.class);
		return mcProductStructureForProcessPointDao;
	}

	public MCStructureDao getMCStructureDao() {
		if(mcStructureDao == null)
			mcStructureDao = ServiceFactory.getDao(MCStructureDao.class);
		return mcStructureDao;
	}
	
	public ConcurrentHashMap<String, List<MCOperationPartRevision>> getPartsMap() {
		return partsMap;
	}

	public void setPartsMap(ConcurrentHashMap<String, List<MCOperationPartRevision>> partsMap) {
		this.partsMap = partsMap;
	}

	public ConcurrentHashMap<String, Integer> getOpsTimeMap() {
		return opsTimeMap;
	}

	public void setOpsTimeMap(ConcurrentHashMap<String, Integer> opsTimeMap) {
		this.opsTimeMap = opsTimeMap;
	}
	
	public void setMfgInstructionLevel(MfgInstructionLevelType mfgInstructionLevel) {
		this.mfgInstructionLevel = mfgInstructionLevel;
	}

	public ConcurrentHashMap<String, Boolean> getProcessTrainingMap() {
		return processTrainingMap;
	}

	public void setProcessTrainingMap(
			ConcurrentHashMap<String, Boolean> processTrainingMap) {
		this.processTrainingMap = processTrainingMap;
	}
	
	
}
