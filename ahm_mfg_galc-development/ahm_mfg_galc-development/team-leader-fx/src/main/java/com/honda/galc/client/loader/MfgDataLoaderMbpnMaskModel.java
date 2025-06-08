package com.honda.galc.client.loader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;
import java.util.Set;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.service.MfgDataLoaderService;
import com.honda.galc.service.ServiceFactory;

public class MfgDataLoaderMbpnMaskModel extends AbstractModel {

	public MfgDataLoaderMbpnMaskModel() {
		super();
	}
	
	public List<McOperationDataDto> findAllOperationMatrixByDeptCodeAndAsmProc(String deptCode, String asmProcNumber, String modelCode) {
		return getDao(MCOperationMatrixDao.class).findAllByDeptCodeAndAsmProc(deptCode, asmProcNumber, modelCode);
	}
	
	public List<McOperationDataDto> findAllPartMatrixByDeptCodeAndAsmProc(String deptCode, String asmProcNumber, String modelCode) {
		return getDao(MCOperationPartMatrixDao.class).findAllByDeptCodeAndAsmProc(deptCode, asmProcNumber, modelCode);
	}
	
	public int getOperationMatrixCountBy(String operationName, int opRev, int pddaPltformId, String specCodeType, String mbpnMask) {
		return getDao(MCOperationMatrixDao.class).getMatrixCountBy(operationName, opRev, pddaPltformId, specCodeType, mbpnMask);
	}
	
	public int getPartMatrixCountBy(String operationName, String partId, int partRev, String specCodeType, String mbpnMask) {
		return getDao(MCOperationPartMatrixDao.class).getMatrixCountBy(operationName, partId, partRev, specCodeType, mbpnMask);
	}
	
	public void saveMatrix(Set<MCOperationMatrix> opMatrixSet, Set<MCOperationPartMatrix> partMatrixSet) {
		ServiceFactory.getService(MfgDataLoaderService.class).saveMatrix(opMatrixSet, partMatrixSet);
	}
	
	@Override
	public void reset() {
		
	}
}
