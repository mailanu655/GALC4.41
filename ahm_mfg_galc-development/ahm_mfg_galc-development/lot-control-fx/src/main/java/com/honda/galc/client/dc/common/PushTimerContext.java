package com.honda.galc.client.dc.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.dto.PddaUnitOfOperation;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Sep 28, 2015
 */
public class PushTimerContext {
	
	private static PushTimerContext instance = null;
	
	private List<PddaUnitOfOperation> unitOfOperationList = null;
	private String productId = "";
	private String processPointId = "";
	
	private PushTimerContext() {}
	
	public static PushTimerContext getInstance() {
		if (instance == null) {
			instance = new PushTimerContext();
		}
		return instance;
	}
	
	public List<PddaUnitOfOperation> getUnitOfOperationList(String productId, String processPointId) {
		if (unitOfOperationList == null ||
				!this.productId.equals(productId.trim()) || 
				!this.processPointId.equals(processPointId.trim())) {
		   //  Get a list of units to be installed at this process point and sort by operation name ascending
		   MCStructureDao mcStructureDao = ServiceFactory.getDao(MCStructureDao.class);
		   if(PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString()).equalsIgnoreCase(StructureCreateMode.DIVISION_MODE.toString())){
			   this.unitOfOperationList = mcStructureDao.getAllOperationsForProcessPointAndProduct(productId, processPointId);
		   }else if(PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString()).equalsIgnoreCase(StructureCreateMode.PROCESS_POINT_MODE.toString())){
			   this.unitOfOperationList = mcStructureDao.getAllOperationsForProcessPointAndProductPPMode(productId, processPointId);
		   }
		   this.productId = productId.trim();
		   this.processPointId = processPointId.trim();
		}
		return unitOfOperationList;
	}

	public PddaUnitOfOperation getPddaUnitOfOperation(String partName, String productId, String processPointId) {
		PddaUnitOfOperation unit = null;
		PddaUnitOfOperation key = new PddaUnitOfOperation();
		key.setOperationName(partName);
		int index = Collections.binarySearch(getUnitOfOperationList(productId, processPointId), key, pddaUnitOfOperationComparator);
		if (index > -1) {
			unit = getUnitOfOperationList(productId, processPointId).get(index);
		}
		return unit;
	}
	
	public static Comparator<PddaUnitOfOperation> pddaUnitOfOperationComparator = new Comparator<PddaUnitOfOperation>() {
	    public int compare(PddaUnitOfOperation operation1, PddaUnitOfOperation operation2) {
	    	return operation1.getOperationName().trim().compareTo(operation2.getOperationName().trim());
	    }
	};
}
