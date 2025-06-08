package com.honda.galc.client.dc.mvc;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.dc.control.SearchCriteriaPanel;
import com.honda.galc.client.dc.property.DataCollectionPropertyBean;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.product.BaseProductStructureDao;
import com.honda.galc.entity.conf.BaseMCProductStructure;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.entity.conf.MCProductStructureForProcessPointId;
import com.honda.galc.entity.conf.MCProductStructureId;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class DataCollectionRepairClientModel extends DataCollectionModel {
	
	private DataCollectionPropertyBean dataCollectionPropertyBean;
	
	@Override
	public void addOperationsToProduct() {
		BaseMCProductStructure productStruct = null;
		StructureCreateMode mode = getStructureCreateMode();
		String id = (mode.equals(StructureCreateMode.DIVISION_MODE)) ? getProductModel().getDivisionId() : getProductModel().getProcessPointId();
		BaseProductStructureDao<? extends BaseMCProductStructure, ?> dao = ServiceFactory.getDao(mode.getProductStructureDaoClass());
		
		productStruct = dao.findByKey(getProductModel().getProductId(), id, getProductModel().getProductSpec().getProductSpecCode());
		
		if (productStruct != null) {
			List<MCOperationRevision> opsInSequence = new ArrayList<MCOperationRevision>();
			dataCollectionPropertyBean = PropertyService.getPropertyBean(DataCollectionPropertyBean.class, getProductModel().getProcessPointId());
			String  processPointList[] = dataCollectionPropertyBean.getMultiProcessPoints();
			for(String processPointId : processPointList){
				setOperationsMap(productStruct, processPointId);
				opsInSequence.addAll(fetchOperationSeq(productStruct, processPointId));
				setOperationParts(productStruct, processPointId);
			}
			getProductModel().getProduct().setOperations(opsInSequence);
		}
	}
	
	public static StructureCreateMode getStructureCreateMode() {
		String structureMode = PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString());
		return structureMode == null ? StructureCreateMode.DIVISION_MODE : StructureCreateMode.get(structureMode);
	}
	
	@Override
	public boolean isDataCollectionComplete() {
		for(MCOperationRevision operation: SearchCriteriaPanel.getInstance().getAllOperations()){
			if(!isOperationComplete(operation.getId().getOperationName())){
				return false;
			}
		}
		return true;
	}
}