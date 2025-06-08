package com.honda.galc.client.loader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dto.PartDto;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.service.MfgDataLoaderService;
import com.honda.galc.service.ServiceFactory;

public class MfgDataLoaderPartMaskModel extends AbstractModel {

	public MfgDataLoaderPartMaskModel() {
		super();
	}
	
	public List<MCOperationPartRevision> findAllByOperationName(String operationName) {
		return getDao(MCOperationPartRevisionDao.class).findAllActivePartsByOperationName(operationName);
	}
	
	public void createMfgPart(String operationName, PartDto p, MCOperationPartRevision defaultMFgPart,	MCOperationPartRevision refPart) {
		ServiceFactory.getService(MfgDataLoaderService.class).createMfgPart(operationName, p, defaultMFgPart, refPart);
	}
	
	public List<MCOperationPartMatrix> findByOperationNamePartIdAndRev(String operationName, String partId, int partRevision) {
		return getDao(MCOperationPartMatrixDao.class).findAllSpecCodeForOperationPartIdAndPartRev(operationName, partId, partRevision);
	}
	public List<MCOperationMeasurement> findAllByOperationNamePartIdAndRevision(String operationName, String partId, int partRevision) {
		return getDao(MCOperationMeasurementDao.class).findAllByOperationNamePartIdAndRevision(operationName, partId, partRevision);
	}
	
	public void updatePartMask(PartDto part, MCOperationPartRevision mfgPart) {
		MCOperationPartRevision operationPartRevision =  mfgPart;
		operationPartRevision.setPartMask(part.getPartMask());
		getDao(MCOperationPartRevisionDao.class).update(operationPartRevision);
	}
	
	
	@Override
	public void reset() {
		
	}
}
