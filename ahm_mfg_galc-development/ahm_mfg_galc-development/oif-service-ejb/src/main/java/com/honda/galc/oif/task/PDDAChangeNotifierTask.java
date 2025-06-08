package com.honda.galc.oif.task;

import java.sql.SQLException;

import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.dao.product.BaseProductStructureDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.BaseMCProductStructure;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifServiceEMailHandler;


public class PDDAChangeNotifierTask extends OifAbstractTask implements IEventTaskExecutable {

	private final static String emailSubject = "GALC PDDA Change notification";
	private final static String emailMessageTemplate = "The following PDDA changes require your attention:\n\n" +
	                                           "   Change forms          %d\n" +
	                                           "   Change form units     %d\n" +
	                                           "   Change form processes %d\n" +
	                                           "   Unmapped MBPNs        %d\n" +
	                                           "   Unmapped Engines      %d\n" +
	                                           "   Unmapped VINs         %d\n\n" +
	                                           "Please login to the GALC team leader screen and take the appropriate actions.\n";

	private int changeFormCount;
	private int changeFormUnitCount;
	private int changeFormProcessCount;
	private int unmappedMBPNCount;
	private int unmappedEngineCount;
	private int unmappedFrameCount;
	
	public PDDAChangeNotifierTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		try {
			if (loadChanges()) {
				sendEmailNotification();
			}
		} catch (SQLException e) {
			logger.error(e,getName());
		}
	}


	private void sendEmailNotification() {
		OifServiceEMailHandler emailHandler = new OifServiceEMailHandler(getName());
		String emailMessage = String.format(emailMessageTemplate,changeFormCount,changeFormUnitCount,changeFormProcessCount, unmappedMBPNCount,unmappedEngineCount,unmappedFrameCount);
	    emailHandler.delivery(emailSubject,emailMessage);
	}

	
	private boolean loadChanges() throws SQLException {
		ChangeFormDao changeFormDao = ServiceFactory.getDao(ChangeFormDao.class);
		ChangeFormUnitDao changeFormUnitDao = ServiceFactory.getDao(ChangeFormUnitDao.class);
		BaseProductStructureDao<? extends BaseMCProductStructure, ?> dao = ServiceFactory.getDao(getStructureCreateMode().getProductStructureDaoClass());
		
		changeFormCount = changeFormDao.getChangeFormCount();
		changeFormUnitCount = changeFormUnitDao.getUnmappedChangeFormUnitCount();
		changeFormProcessCount = changeFormUnitDao.getUnmappedChangeFormProcessCount();
		unmappedMBPNCount = dao.getUnmappedProductCount(ProductType.MBPN);
		unmappedEngineCount = dao.getUnmappedProductCount(ProductType.ENGINE);
		unmappedFrameCount = dao.getUnmappedProductCount(ProductType.FRAME);
		
		logger.debug(String.format("PDDA change summary : changeFormCount=%d, changeFormUnitCount=%d, changeFormProcessCount=%d, unmappedMBPMCount=%d, unmappedEngineCount=%d,  unmappedFrameCount=%d",
						changeFormCount, changeFormUnitCount,
						changeFormProcessCount, unmappedMBPNCount, 
						unmappedEngineCount,unmappedFrameCount));

		return changeFormCount > 0 || changeFormUnitCount > 0
				|| changeFormProcessCount > 0 || unmappedMBPNCount > 0 
				|| unmappedEngineCount > 0 || unmappedFrameCount > 0;
	}
	
	public static StructureCreateMode getStructureCreateMode() {
		String structureMode = PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString());
		return structureMode == null ? StructureCreateMode.DIVISION_MODE : StructureCreateMode.get(structureMode);
	}

}
