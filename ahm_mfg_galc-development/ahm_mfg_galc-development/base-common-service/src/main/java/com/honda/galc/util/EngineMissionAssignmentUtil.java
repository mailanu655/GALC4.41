package com.honda.galc.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.AfbDataDao;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.enumtype.HoldStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.property.EngineMissionAssignPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class EngineMissionAssignmentUtil {
	
	protected InProcessProductDao inProcessProductDao = null;
	protected AfbDataDao afbDataDao = null;
	protected DeviceFormatDao deviceFormatDao = null;
	protected FrameSpecDao frameSpecDao = null;
	protected FrameDao frameDao = null;
	protected EngineDao engineDao = null;
	protected HoldResultDao holdResultDao = null;
	protected HoldResult holdResult = null;
	protected QsrDao qsrDao = null;
	/**
	 * update the Engine product spec with expected MTO used in Engine
	 * uncommanization process.
	 * 
	 */
	public void updateEngineSpec(String vin, Engine engine) {
		try {
			Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(vin);
			FrameSpec frameSpec = getFrameSpecDao().findByKey(frame.getProductSpecCode());
			String vinEngineMto = StringUtils.trimToEmpty(frameSpec.getEngineMto());
			String altEngineMto = StringUtils.trimToEmpty(frameSpec.getAltEngineMto());
			String engineProductSpecCode = StringUtils.trimToEmpty(engine.getProductSpecCode());
			if (altEngineMto != null 
					&& altEngineMto.equals(engineProductSpecCode)
					&& !altEngineMto.equals(vinEngineMto)) {
				String previousSpec = engine.getProductSpecCode();
				//calculating to make sure engine going to be updated has all latest values. 
				engine = ServiceFactory.getDao(EngineDao.class).findByKey(engine.getId());
				engine.setProductSpecCode(vinEngineMto);
				ServiceFactory.getDao(EngineDao.class).update(engine);
				Logger.getLogger().info(
						"EngineLoadUtility:: Engine spec updated for "
								+ engine.getProductId()+" from "+ previousSpec+ " to "+ engine.getProductSpecCode());
			} else {
				Logger.getLogger().info(
						"EngineLoadUtility:: Use existing product spec for engine "
								+ engine.getProductId() + " spec code " + engine.getProductSpecCode());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(
					"EngineLoadUtility:: Could not update engine spec"
							+ engine.getProductId());
		}
	}
	
	/**
	 * Check if engine is assigned to a frame
	 * 
	 * @param engineSn
	 * @return
	 */
	public boolean isEngineAssignedToFrame(Engine engine) {
		return engine.getVin() != null;
	}
	
	public boolean isEngineOnHold(String engineSn) {
		List<HoldResult> holdResults = getHoldResultDao().findAllByProductId(engineSn);
		if (this.isEngineOnShipHold(engineSn, holdResults)) return true;
		if (this.isEngineOnDepartmentHold(engineSn, holdResults)) return true;
		return false;
	}
	
	public boolean isEngineOnShipHold(String engineSn) {
		List<HoldResult> holdResults = getHoldResultDao().findAllByProductId(engineSn);
		return this.isEngineOnShipHold(engineSn, holdResults);
	}
	
	public boolean isEngineOnDepartmentHold(String engineSn) {
		List<HoldResult> holdResults = getHoldResultDao().findAllByProductId(engineSn);
		return this.isEngineOnDepartmentHold(engineSn, holdResults);
	}
	
	private boolean isEngineOnShipHold(String engineSn, List<HoldResult> holdResults) {
		return this.isEngineOnHold(engineSn, holdResults, HoldResultType.HOLD_AT_SHIPPING);
	}

	private boolean isEngineOnDepartmentHold(String engineSn, List<HoldResult> holdResults) {
		return this.isEngineOnHold(engineSn, holdResults, HoldResultType.HOLD_NOW);
	}
	
	private boolean isEngineOnHold(String engineSn, List<HoldResult> holdResults, HoldResultType holdType) {	
		this.holdResult = null;
		for (HoldResult holdResult: holdResults) {
			if (holdResult.getId().getHoldType() == holdType.getId() && holdResult.getReleaseFlag() == HoldStatus.ON_HOLD.getId()) {
				this.holdResult = holdResult;
				return true;
			}
		}
		return false;
	}
	
	public boolean isEngineOnExternalHold(Engine engine, ProcessPoint processPoint) {
		String sites = "";
		try {
			sites = PropertyService.getPropertyBean(EngineMissionAssignPropertyBean.class, processPoint.getId()).getExternalHoldCheckSites();
		} catch (Exception e) {
			Logger.getLogger().info("EXTERNAL_HOLD_CHECK_SITES property not set. External holds check will be skipped.");
			return false;
		}
		ProductCheckUtil checkUtil = new ProductCheckUtil(engine, processPoint);
		List<String> extHoldChkResults = checkUtil.externalProductOnHoldCheck();
		if (StringUtils.isBlank(sites)) return !extHoldChkResults.isEmpty();
		
		List<String> siteList = Arrays.asList(sites.split(","));
		for (String extHoldChkResult : extHoldChkResults) {
			if (extHoldChkResult.contains("is on hold at site:")) {
				String site = StringUtils.substringAfterLast(extHoldChkResult, ":");
				if (siteList.contains(site.trim())) return true;
			}
		}
		return false;
	}
	
	/**
	 * returns the FrameSpec for the provided product
	 * @return
	 */
	protected FrameSpec getFrameSpec(String vin) {
		Frame frame = getFrameDao().findByKey(vin);
		return getFrameSpecDao().findByKey(frame.getProductSpecCode());
	}	
	
	public HoldResult getHoldResult() {
		return this.holdResult;
	}
	
	public FrameDao getFrameDao() {
		if (frameDao == null)
			frameDao = ServiceFactory.getDao(FrameDao.class);
		return frameDao;
	}
	
	public EngineDao getEngineDao() {
		if (engineDao == null)
			engineDao = ServiceFactory.getDao(EngineDao.class);
		return engineDao;
	}
	
	public FrameSpecDao getFrameSpecDao() {
		if (frameSpecDao == null)
			frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		return frameSpecDao;
	}
	
	public AfbDataDao getAfbDataDao() {
		if (afbDataDao == null)
			afbDataDao = ServiceFactory.getDao(AfbDataDao.class);
		return afbDataDao;
	}

	public DeviceFormatDao getDeviceFormatDao() {
		if (deviceFormatDao == null)
			deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		return deviceFormatDao;
	}
	
	public InProcessProductDao getInProcessProductDao() {
		if (inProcessProductDao == null)
			inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		return inProcessProductDao;
	}
	
	public HoldResultDao getHoldResultDao() {
		if (holdResultDao == null)
			holdResultDao = ServiceFactory.getDao(HoldResultDao.class);
		return holdResultDao;
	}
	
	public QsrDao getQsrDao() {
		if (qsrDao == null)
			qsrDao = ServiceFactory.getDao(QsrDao.class);
		return qsrDao;
	}
}
