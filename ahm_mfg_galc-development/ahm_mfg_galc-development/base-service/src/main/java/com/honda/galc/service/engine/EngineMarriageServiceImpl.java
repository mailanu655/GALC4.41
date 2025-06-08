package com.honda.galc.service.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.entitypersister.MasterEntityList;
import com.honda.galc.property.EngineMissionAssignPropertyBean;
import com.honda.galc.service.ProductMarriage;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.EngineUtil;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.SubproductUtil;

public class EngineMarriageServiceImpl implements EngineMarriageService{

	private static final String CLASS_NAME = "EngineMarriageService";
	protected EngineMissionAssignPropertyBean assignmentProperty;
	protected ProductCheckUtil productCheckUtil;
	private List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
	private InstalledPart installedPart;
	private String processPointId = null;
	private String applicationId = null;
	private String productType;
	private Logger logger;

	protected SubproductUtil subProductUtil;
	public String missionPartName;
	public String missionTypePartName;
	private MasterEntityList<EntityList<AbstractEntity>> masterEntityList;
	public Engine engine = new Engine();
	public Frame frame = new Frame();
	private EngineUtil engineUtil;

	@Autowired
	private ProductMarriage productMarriage;

	public void init(List<InstalledPart> installedParts, String partName) {
		this.installedParts = trimData(installedParts);
		installedPart = getInstalledPart(installedParts, partName);
		
		PartName part = getPartNameDao().findByKey(partName);
		
		if(part == null) {
			String msg = "Part: " + partName + " does not exist in Part Name table.";
			handleException(msg, new TaskException(msg));
		}
		
		productType = part.getProductTypeName();
		processPointId = installedPart.getProcessPointId();
		masterEntityList = null;

		if(productType.equals(ProductTypeUtil.FRAME.name())) {
			frame = (Frame) productMarriage.getProduct(installedPart.getProductId(), ProductTypeUtil.FRAME.name());
			engine = (Engine) productMarriage.getProduct(installedPart.getPartSerialNumber(), ProductTypeUtil.ENGINE.name());
		}
		else {
			engine = (Engine) productMarriage.getProduct(installedPart.getProductId(), ProductTypeUtil.ENGINE.name());
			frame = (Frame) ((engine.getVin() == null) ? new Frame() : productMarriage.getProduct(engine.getVin(), ProductTypeUtil.FRAME.name()));
		}
	}

	public Engine assignMissionType(List<InstalledPart> installedParts, String processPointId, String applicationId) {
		try {
			this.processPointId = processPointId;
			this.applicationId = applicationId;
			
			getLogger().info("Preparing to assign mission type");
			
			if(installedParts != null && !(installedParts.isEmpty())) 
				init(installedParts,  getEngineUtil().getMissionTypePartName());
			if(installedPart == null) throw new TaskException("Unable to perform marriage, installed part(s) does not contain mission type part name."
					+ " Verify properties are configured with correct part name. Used part name " + getEngineUtil().getMissionTypePartName());

			engine.setActualMissionType(installedPart.getPartSerialNumber());
			if(getEngineUtil().determineMissionStatus(engine, installedPart.getPartName(), 
					getOverallPartOkOrNG(installedPart.getInstalledPartStatus()))) {
				engine.setMissionStatus(InstalledPartStatus.OK.getId());
			}
			if (frame.getProductId()!= null) {
				frame.setActualMissionType(engine.getActualMissionType());
				frame.setEngineStatus(getEngineUtil().determineEngineStatus(engine, installedPart.getPartName(),
						getOverallPartOkOrNG(installedPart.getInstalledPartStatus())));
			}
			updateProducts();
		}catch(PersistenceException e) {
			handleException("Error occured writing data to database", e);
		}catch(TaskException e ) {
			handleException(e.getMessage(), e);
		}catch(Exception e) {
			handleException("Unknown error occured while assigning mission type", e);
		} finally {
			clearData();
		}
		return engine;
	}

	public Engine assignMission(List<InstalledPart> installedParts, String processPointId, String applicationId) {
		try {
			this.processPointId = processPointId;
			this.applicationId = applicationId;
			
			getLogger().info("Preparing to assign mission");
			
			init(installedParts, getEngineUtil().getMissionPartName());
			engine.setMissionSerialNo(installedPart.getPartSerialNumber());
			if(getEngineUtil().determineMissionStatus(engine, installedPart.getPartName(),
					getOverallPartOkOrNG(installedPart.getInstalledPartStatus()))) {
				engine.setMissionStatus(getEngineUtil().determineMissionStatus(engine,
						installedPart.getPartName(), getOverallPartOkOrNG(installedPart.getInstalledPartStatus())));
			}
			if(frame.getProductId() != null) {
				frame.setMissionSerialNo(engine.getMissionSerialNo());
				frame.setEngineStatus(getEngineUtil().determineEngineStatus(engine, installedPart.getPartName(),
						getOverallPartOkOrNG(installedPart.getInstalledPartStatus())));
			}
			updateProducts();
			getLogger().info("Mission has been successfully assigned");
		}
		catch(PersistenceException e) {
			handleException("Error occured writing data to database", e);
		}
		catch(Exception e) {
			handleException("Unknown error occured while assigning mission", e);
		} finally {
			clearData();
		}
		return engine;
	}

	public Engine deassignMission(List<InstalledPart> installedParts, String processPointId, String applicationId) {
		try {
			this.processPointId = processPointId;
			this.applicationId = applicationId;
		
			getLogger().info("Preparing to deassign mission");

			init(installedParts, getEngineUtil().getMissionPartName());
			engine.setMissionSerialNo(null);
			engine.setActualMissionType(null);
			engine.setMissionStatus(InstalledPartStatus.NG.getId());
			if(frame.getProductId() != null) {
				frame.setActualMissionType(null);
				frame.setMissionSerialNo(null);
				frame.setEngineStatus(InstalledPartStatus.NG.getId());
			}
			updateProducts();
			getLogger().info("Mission has been sucessfully deassigned");
		}
		catch(PersistenceException e) {
			handleException("Error occured while writing data to database", e);
		}
		catch(Exception e) {
			handleException("Unknown error occured while deassigning mission", e);
		} finally {
			clearData();
		}
		return engine;
	}
	
	public Engine deassignMissionType(List<InstalledPart> installedParts, String processPointId, String applicationId) {
		try {
			this.processPointId = processPointId;
			this.applicationId = applicationId;
		
			getLogger().info("Preparing to deassign mission");

			init(installedParts, getEngineUtil().getMissionTypePartName());
			engine.setMissionSerialNo(null);
			engine.setActualMissionType(null);
			engine.setMissionStatus(InstalledPartStatus.NG.getId());
			if(frame.getProductId() != null) {
				frame.setActualMissionType(null);
				frame.setMissionSerialNo(null);
				frame.setEngineStatus(InstalledPartStatus.NG.getId());
			}
			updateProducts();
			getLogger().info("Mission has been sucessfully deassigned");
		}
		catch(PersistenceException e) {
			handleException("Error occured while writing data to database", e);
		}
		catch(Exception e) {
			handleException("Unknown error occured while deassigning mission", e);
		} finally {
			clearData();
		}
		return engine;
	}

	public void assignEngineAndFrame(List<InstalledPart> installedParts) {
		try {
			init(installedParts, installedParts.get(0).getPartName());
			getLogger().info("Preparing to assign engine and frame");
			
			engine.setVin(frame.getProductId());
			if(frame.getProductId() != null){
				frame.setEngineSerialNo(engine.getProductId());
				frame.setMissionSerialNo(engine.getMissionSerialNo());
				frame.setActualMissionType(engine.getActualMissionType());
				frame.setEngineStatus(getEngineUtil().determineEngineStatus(engine,
						installedPart.getPartName(), getOverallPartOkOrNG(installedPart.getInstalledPartStatus())));
			}
			engine = getEngineUtil().updateEngineSpec(frame, engine);
			updateProducts();
			getLogger().info("Engine and frame have been sucessfully associated");
		}
		catch(PersistenceException e) {
			handleException("Error occured writing data to database", e);
		}
		catch(Exception e) {
			handleException("Unknown error occured assigning engine and frame", e);
		} finally {
			clearData();
		}
	}

	public void updateEngineAndFrame(Engine engine , Frame frame, ProductBuildResult result) {
		try {
			InstalledPart part = (InstalledPart)result;
			processPointId = part.getProcessPointId();
			this.engine = engine;
			this.frame = frame;
			part.setInstalledPartStatus(InstalledPartStatus.OK);
			installedParts.add(part);

			assignEngineAndFrame(installedParts);
			installedParts.clear();
		} catch(Exception e) {
			handleException("Error occured updating products", e);
		}
	}

	public Frame deassignEngineAndFrame(List<InstalledPart> installedParts, String processPointId, String applicationId) {
		try {
			
			this.processPointId = processPointId;
			this.applicationId = applicationId;
			
			getLogger().info("Preparing to deassign engine and frame.");
			
			init(installedParts, installedParts.get(0).getPartName());
			engine.setVin(null);
			frame.setEngineSerialNo(null);
			frame.setMissionSerialNo(null);
			frame.setActualMissionType(null);
			frame.setEngineStatus(InstalledPartStatus.NG.getId());
			updateProducts();
			getLogger().info("Engine and frame have been sucessfully disassociated");
		}
		catch(PersistenceException e) {
			handleException("Error occured writing data to database", e);
		}
		catch(Exception e) {
			handleException("Unknown error occured deassigning engine and frame", e);
		} finally {
			clearData();
		}
		return frame;
	}

	private void updateProducts() {
		prepareEntityList();
		productMarriage.marryProduct(getMasterEntityList());
	}

	private void prepareEntityList() {
		if(installedParts != null && !(installedParts.isEmpty()))
			getMasterEntityList().push(productMarriage.addEntity(installedParts, productType));
		if(engine.getProductId() != null)
			getMasterEntityList().push(productMarriage.addProductEntity(engine));
		if(frame.getProductId() !=null)
			getMasterEntityList().push(productMarriage.addProductEntity(frame));
	}

	private List<InstalledPart> trimData(List<InstalledPart> installedParts) {
		if(installedParts == null || installedParts.isEmpty()) return installedParts;
		List<InstalledPart> listParts = new ArrayList<InstalledPart>();
		for(InstalledPart part : installedParts) {
			part.setProductId(StringUtils.trim(part.getProductId()));
			part.setProductType(StringUtils.trim(part.getProductType()));
			part.setProcessPointId(StringUtils.trim(part.getProcessPointId()));
			listParts.add(part);
		}
		return listParts;
	}

	private InstalledPart getInstalledPart(List<InstalledPart> installedParts, String partName) {
		for(InstalledPart installedPart : installedParts) {
			if(installedPart.getPartName().equals(partName)) {
				return installedPart;
			}
		}
		getLogger().warn("Could not find installed part for " + partName);
		return null;
	}

	private void clearData() {
		masterEntityList = null;
		installedPart = null;
	}

	public Logger getLogger() {
		if(logger == null) {
			logger = Logger.getLogger(getLoggerName());
		}
		return logger;
	}

	private String getLoggerName() {
		if(applicationId == null) {
			if(processPointId == null) {
				return CLASS_NAME;
			}else {
				return processPointId;
			}
		} else {
			return applicationId;
		}
	}
	
	/**
	 * 
	 * @author Bradley Brown, HMA
	 * @date Aug 14, 2018
	 * @param status
	 * @return
	 * Checks if the InstalledPartStatus is in a acceptable status to ship a product.
	 * InstalledPartStatus.OK will be returned if the InstalledPartStatus is OK,
	 * REPAIRED, or ACCEPT. Otherwise return InstalledPartStatus.NG
	 */
	private InstalledPartStatus getOverallPartOkOrNG(InstalledPartStatus status) {
		if(status == null) return InstalledPartStatus.NC;

		if(status != InstalledPartStatus.getType(InstalledPartStatus.OK.getId())) {
			return (status == InstalledPartStatus.getType(InstalledPartStatus.REPAIRED.getId()) ||
					status == InstalledPartStatus.getType(InstalledPartStatus.ACCEPT.getId())) ?

							InstalledPartStatus.OK : InstalledPartStatus.NG;
		}
		return InstalledPartStatus.OK;
	}


	public EngineUtil getEngineUtil() {
		return (engineUtil == null) ? engineUtil = new EngineUtil(processPointId) : engineUtil;
	}

	public void handleException(String message, Exception e) {
		getLogger().error(e, message);
		throw new TaskException(message, CLASS_NAME);
	}

	public ProductCheckUtil getProductCheckUtil() {
		return (productCheckUtil == null) ? productCheckUtil = new ProductCheckUtil() : productCheckUtil;
	}

	public MasterEntityList<EntityList<AbstractEntity>> getMasterEntityList() {
		return (masterEntityList == null) ? masterEntityList = new MasterEntityList<EntityList<AbstractEntity>>() : masterEntityList;
	}
	
	public PartNameDao getPartNameDao() {
		return ServiceFactory.getDao(PartNameDao.class);
	}
}