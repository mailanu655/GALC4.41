package com.honda.galc.service.utils;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.property.EngineMissionAssignPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.IEngineUtil;
import com.honda.galc.util.ProductCheckUtil;

/**
 * 
 * <h3>EngineUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineUtil description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Apr 27, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 27, 2012
 */
public class EngineUtil extends ServiceUtil implements IEngineUtil{

	private EngineMissionAssignPropertyBean assignmentProperty;
	private ProductCheckUtil productCheckUtil;

	private String processPointId;
	private String enginePartName;
	private String missionPartName;
	private String missionTypePartName;
	
	private FrameSpecDao frameSpecDao;

	public EngineUtil(String processPointId) {
		this.processPointId = processPointId;
	}

	/**
	 * Method to what the value of MISSION_STATUS should be in GAL131TBX.
	 * This value is calculated based on 2 criteria.
	 * 1. Mission Type is in good status based on the result in GAL185TBX, If property MISSION_TYPE_REQUIRED is 
	 *    set to false this check it ignored and will always return true. If the Mission Type is the part that is 
	 *    currently being installed the the <code>status</code> is used to determine the status of the Mission Type.
	 * 2. The Mission is in good status based on the result in GAL185TBX.If the Mission is the part that is 
	 *    currently being installed the the <code>status</code> is used to determine the status of the Mission.
	 * The <code>partName</partName> is used to determine which part is being installed.  The partName is compared
	 * with a property value in EngineMissionAssignPropertyBean which contains a default part name for Engine,
	 * Mission, and Mission Type.
	 * 
	 * @author Bradley Brown, HMA
	 * @param engine
	 * @param partName
	 * @param status
	 * @return boolean
	 */
	public boolean determineMissionStatus(Engine engine, String partName, InstalledPartStatus status) {
		return (isMissionTypeOk(engine, partName, status) &&
				isMissionOk(engine, partName, status));
	}
	
	/**
	 * Method to determine what the value of ENGINE_STAUS should be in GAL143TBX.
	 * This value is calculated based on 3 criteria. 
	 * 1. Mission Type is in good status based on the result in GAL185TBX, If property MISSION_TYPE_REQUIRED is 
	 *    set to false this check it ignored and will always return true.
	 * 2. The Mission is in good status based on the result in GAL185TBX.
	 * 3. The engine is in good status based on the <code>status</code> that is passed into the method.
	 * The <code>partName</partName> is used to determine which part is being installed.  The partName is compared
	 * with a property value in EngineMissionAssignPropertyBean which contains a default part name for Engine,
	 * Mission, and Mission Type.
	 * 	
	 * @author Bradley Brown, HMA
	 * @param engine
	 * @param partName
	 * @param status
	 * @return boolean
	 */
	public boolean determineEngineStatus(Engine engine, String partName, InstalledPartStatus status) {
		return (isMissionTypeOk(engine, partName, status) &&
				isMissionOk(engine, partName, status) &&
				isEngineOk(engine, partName, status));
	}

	private boolean isEngineOk(Engine engine, String partName, InstalledPartStatus status) {
		if(partName.equals(getEnginePartName()) )
			return (status == InstalledPartStatus.OK || status == InstalledPartStatus.REPAIRED) ? true : false;
		else 
			return (getProductCheckUtil().isInstalledPartStatusCheck(engine.getProductId(), getEnginePartName()));
	}

	private boolean isMissionOk(Engine engine, String partName, InstalledPartStatus status) {
		if(partName.equals(getMissionPartName())) {
			return (status == InstalledPartStatus.OK || status == InstalledPartStatus.REPAIRED) ? true : false;
		}else {
			return getProductCheckUtil().isInstalledPartStatusCheck(engine.getProductId(), getMissionPartName());
		}
	}

	private boolean isMissionTypeOk(Engine engine, String partName, InstalledPartStatus status) {
		if(partName.equals(getMissionPartName())) {
			return (getAssignmentProperty().isMissionTypeRequired()) ?
					getProductCheckUtil().isInstalledPartStatusCheck(engine.getProductId(), getMissionTypePartName()) : true;
		}else if(partName.equals(getMissionTypePartName())) {
			return (getAssignmentProperty().isMissionTypeRequired() && (status == InstalledPartStatus.OK || status == InstalledPartStatus.REPAIRED));
		}else if (partName.equals(getEnginePartName()))
			return (getAssignmentProperty().isMissionTypeRequired()) ?
					getProductCheckUtil().isInstalledPartStatusCheck(engine.getProductId(), getMissionTypePartName()) : true;
					return false;
	}
	
	/**
	 * update the Engine product spec with expected MTO used in Engine
	 * uncommanization process.
	 * 
	 */
	public Engine updateEngineSpec(Frame frame, Engine engine) {
		try {
			FrameSpec frameSpec = getFrameSpecDao().findByKey(frame.getProductSpecCode());
			String vinEngineMto = StringUtils.trimToEmpty(frameSpec.getEngineMto());
			String altEngineMto = StringUtils.trimToEmpty(frameSpec.getAltEngineMto());
			String engineProductSpecCode = StringUtils.trimToEmpty(engine.getProductSpecCode());
			if (altEngineMto != null 
					&& altEngineMto.equals(engineProductSpecCode)
					&& !altEngineMto.equals(vinEngineMto)) {
				String previousSpec = engine.getProductSpecCode();
				Logger.getLogger().info(
						"EngineLoad:: Engine spec updated for "
								+ engine.getProductId()+" from "+ previousSpec+ " to "+ engine.getProductSpecCode());
			} else {
				Logger.getLogger().info(
						"EngineLoad:: Use existing product spec for engine "
								+ engine.getProductId() + " spec code " + engine.getProductSpecCode());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(
					"EngineLoadUtility:: Could not update engine spec"
							+ engine.getProductId());
		}
		return engine;
	}
	
	/**
	 * checks if the engine tracking status is valid
	 * 
	 * @return
	 */
	public boolean checkValidPreviousEngineLine(Engine engine) {
		String validPreviousLines = "";
		
		// return true if no previous lines are specified
		if (getAssignmentProperty().getValidEngineLoadPreviousLines() == null || 
				getAssignmentProperty().getValidEngineLoadPreviousLines().trim().equals(""))
			return true;

		// if previous lines are specified in the property, perform the check
		validPreviousLines = getAssignmentProperty().getValidEngineLoadPreviousLines();
		boolean isValidPreviousLine = false;

		for(String validLine: validPreviousLines.split(Delimiter.COMMA)) {
			if (engine.getTrackingStatus().equals(validLine)) {
				isValidPreviousLine = true;
				break;
			}
		}
		return isValidPreviousLine;
	}

	private EngineMissionAssignPropertyBean getAssignmentProperty() {
		return (assignmentProperty == null) ? assignmentProperty = 
				PropertyService.getPropertyBean(EngineMissionAssignPropertyBean.class, processPointId) : assignmentProperty;
	}
	
	public String getEnginePartName() {
		return (enginePartName == null) ? enginePartName = getAssignmentProperty().getEnginePartName() : enginePartName;
	}

	public String getMissionPartName() {
		return (missionPartName == null) ? missionPartName = getAssignmentProperty().getMissionPartName() : missionPartName;
	}

	public String getMissionTypePartName() {
		return (missionTypePartName == null) ? missionTypePartName = getAssignmentProperty().getMissionTypePartName() : missionTypePartName;
	}

	public FrameSpecDao getFrameSpecDao() {
		return (frameSpecDao == null) ? frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class) : frameSpecDao;
	}
	
	private ProductCheckUtil getProductCheckUtil() {
		return (productCheckUtil == null) ? productCheckUtil = new ProductCheckUtil() : productCheckUtil;
	}
}