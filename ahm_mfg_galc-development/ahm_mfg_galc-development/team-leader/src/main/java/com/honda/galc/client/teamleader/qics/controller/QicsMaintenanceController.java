package com.honda.galc.client.teamleader.qics.controller;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ClientController;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.teamleader.qics.model.QicsMaintenanceModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ZoneDao;
import com.honda.galc.dao.qics.DefectDescriptionDao;
import com.honda.galc.dao.qics.DefectGroupDao;
import com.honda.galc.dao.qics.DefectTypeDao;
import com.honda.galc.dao.qics.DefectTypeDescriptionDao;
import com.honda.galc.dao.qics.ImageDao;
import com.honda.galc.dao.qics.ImageSectionDao;
import com.honda.galc.dao.qics.InspectionModelDao;
import com.honda.galc.dao.qics.InspectionPartDao;
import com.honda.galc.dao.qics.InspectionPartDescriptionDao;
import com.honda.galc.dao.qics.InspectionPartGroupDao;
import com.honda.galc.dao.qics.InspectionPartLocationDao;
import com.honda.galc.dao.qics.InspectionTwoPartDescriptionDao;
import com.honda.galc.dao.qics.IqsDao;
import com.honda.galc.dao.qics.RegressionDao;
import com.honda.galc.dao.qics.SecondaryPartDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.entity.enumtype.ApplicationType;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.entity.qics.ImageSection;
import com.honda.galc.entity.qics.InspectionModel;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.InspectionPartDescription;
import com.honda.galc.entity.qics.InspectionPartLocation;
import com.honda.galc.entity.qics.InspectionTwoPartDescription;
import com.honda.galc.entity.qics.Iqs;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.entity.qics.Regression;
import com.honda.galc.entity.qics.SecondaryPart;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>QicsMaintenanceController</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Sep 2, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class QicsMaintenanceController  extends ClientController {
	
	protected static final String SAVED = "saved";
	protected static final String UPDATED = "updated";
	protected static final String INSERTED = "inserted";
	protected static final String REMOVED = "removed";

	protected Logger logger = Logger.getLogger();

	private QicsMaintenanceModel clientModel;

	public QicsMaintenanceController(QicsMaintenanceFrame frame) {
		super(frame);
		clientModel = new QicsMaintenanceModel();
	}

	// === server dao api === //
	// === part group === //
	public PartGroup createPartGroup(PartGroup partGroup) {
		PartGroup pg = getDao(InspectionPartGroupDao.class).save(partGroup);
		logUserAction(SAVED, partGroup);
		return pg;
	}

	public void deletePartGroup(PartGroup partGroup) {
		getDao(InspectionPartGroupDao.class).remove(partGroup);
		logUserAction(REMOVED, partGroup);
	}

	public List<PartGroup> selectPartGroups() {
		return getDao(InspectionPartGroupDao.class).findAll();
	}

	// == part === //
	public InspectionPart createPart(InspectionPart part) {
		InspectionPart ip = getDao(InspectionPartDao.class).save(part);
		logUserAction(SAVED, part);
		return ip;
	}

	public void deletePart(InspectionPart part) {
		getDao(InspectionPartDao.class).remove(part);
		logUserAction(REMOVED, part);
	}

	public List<InspectionPart> selectParts() {
		return getDao(InspectionPartDao.class).findAll();
	}

	// === part location === //
	public InspectionPartLocation createPartLocation(InspectionPartLocation partLocation) {
		InspectionPartLocation ipl = getDao(InspectionPartLocationDao.class).save(partLocation);
		logUserAction(SAVED, partLocation);
		return ipl;
	}

	public void deletePartLocation(InspectionPartLocation partLocation) {
		getDao(InspectionPartLocationDao.class).remove(partLocation);
		logUserAction(REMOVED, partLocation);
	}

	public List<InspectionPartLocation> selectPartLocations() {
		return getDao(InspectionPartLocationDao.class).findAll();
	}

	// === part description === //
	public void createPartDescriptions(List<InspectionPartDescription> partDescriptions) {
		getDao(InspectionPartDescriptionDao.class).saveAll(partDescriptions);
		logUserAction(SAVED, partDescriptions);
	}

	public void deletePartDescriptions(List<InspectionPartDescription> partDescriptions) {
		getDao(InspectionPartDescriptionDao.class).removeAll(partDescriptions);
		logUserAction(REMOVED, partDescriptions);
	}

	public List<InspectionPartDescription> selectInspectionPartDescriptions(InspectionPartDescription criteria) {
		return getDao(InspectionPartDescriptionDao.class).findAllByPart(
				criteria.getPartGroupName(), criteria.getInspectionPartName(),criteria.getInspectionPartLocationName());
	}

	public List<InspectionPartDescription> selectInspectionPartDescriptionsByPart(PartGroup partGroup, String partName) {
		return getDao(InspectionPartDescriptionDao.class).findAllByPart(partGroup.getPartGroupName(), partName,"");
	}

	public InspectionPartDescription selectInspectionPartDescription(int descriptionId) {
		List<InspectionPartDescription> partDescriptions =  getDao(InspectionPartDescriptionDao.class).findAllByDescriptionId(descriptionId);
		return partDescriptions.isEmpty() ? null : partDescriptions.get(0);
	}

	// === image === //
	public Image createImage(Image image) {
		Image result = getDao(ImageDao.class).insert(image);
		logUserAction(INSERTED, image);
		return result;
	}

	public Image updateImage(Image image) {
		Image result = getDao(ImageDao.class).save(image);
		logUserAction(SAVED, image);
		return result;
	}

	public void deleteImage(Image image) {
		getDao(ImageDao.class).remove(image);
		logUserAction(REMOVED, image);
	}

	public List<Image> selectImages() {
		return getDao(ImageDao.class).findAllImageNames();
	}
	
	public Image selectImage(String imageName) {
		Image image = getDao(ImageDao.class).findByImageName(imageName);
		getClientModel().putImage(image);
		return image;
	}

	public List<ImageSection> selectImageSections(String imageName) {
		return getDao(ImageSectionDao.class).findAllByImageName(imageName);
	}

	public void saveImageSections(List<ImageSection> createdImageSections, List<ImageSection> updatedImageSections, List<ImageSection> deletedImageSections) {
		if(!createdImageSections.isEmpty()) {
			getDao(ImageSectionDao.class).insertAll(createdImageSections);
			logUserAction(INSERTED, createdImageSections);
		}
		if(!updatedImageSections.isEmpty()) {
			getDao(ImageSectionDao.class).saveAll(updatedImageSections);
			logUserAction(SAVED, updatedImageSections);
		}
		if(!deletedImageSections.isEmpty()) {
			getDao(ImageSectionDao.class).removeAll(deletedImageSections);
			logUserAction(REMOVED, deletedImageSections);
		}
	}

	// === models === //
	public void createInspectionModels(List<InspectionModel> inspectionModels) {
		getDao(InspectionModelDao.class).saveAll(inspectionModels);
		logUserAction(SAVED, inspectionModels);
	}

	public void deleteInspectionModels(List<InspectionModel> inspectionModels) {
		getDao(InspectionModelDao.class).removeAll(inspectionModels);
		logUserAction(REMOVED, inspectionModels);
	}

	public List<InspectionModel> selectInspectionModels(String processPointId) {
		return getDao(InspectionModelDao.class).findAllByApplicationId(processPointId);
	}

	//Find all inspection model on the basis of process point id and model codes.
	public List<InspectionModel> selectInspectionModelsWithModelCodes(String processPointId, List<String> modelCodes) {
		return getDao(InspectionModelDao.class).findAllByApplicationIdAndModelCodes(processPointId, modelCodes);
	}
	
	//Find all part groups on the basis of model codes.
	public List<PartGroup> selectPartGroupsByModelCodes(List<String> modelCodes) {
		return getDao(InspectionPartGroupDao.class).findAllPartGroupsByModelCodes(modelCodes);
	}
	
	//Find all defect groups on the basis of model codes.
	public List<DefectGroup> selectDefectGroupsByModelCodes(List<String> modelCodes) {
		return getDao(DefectGroupDao.class).findAllDefectGroupsByModelCodes(modelCodes);
	}
	
	public List<String> selectModels(String productTypeStr) {
		ProductType productType = ProductTypeCatalog.getProductType(productTypeStr);
		return ProductTypeUtil.getProductSpecDao(productType).findAllModelCodes(productTypeStr); 

	}

	// === defect group === //
	public void createDefectGroup(DefectGroup defectGroup) {
		getDao(DefectGroupDao.class).save(defectGroup);
		logUserAction(SAVED, defectGroup);
	}

	public void deleteDefectGroup(DefectGroup defectGroup) {
		getDao(DefectGroupDao.class).remove(defectGroup);
		logUserAction(REMOVED, defectGroup);
	}

	public List<DefectGroup> selectDefectGroups() {
		return getDao(DefectGroupDao.class).findAll();
	}

	public List<DefectGroup> selectDefectGroups(String imageName) {
		return getDao(DefectGroupDao.class).findAllByImageName(imageName);
	}

	// === defect type === //
	public void createDefectType(DefectType defectType) {
		getDao(DefectTypeDao.class).save(defectType);
		logUserAction(SAVED, defectType);
	}

	public void deleteDefectType(DefectType defectType) {
		getDao(DefectTypeDao.class).remove(defectType);
		logUserAction(REMOVED, defectType);
	}

	public List<DefectType> selectDefectTypes() {
		return getDao(DefectTypeDao.class).findAll();
	}

	// === (defect )secondary part === //
	public void createSecondaryPart(SecondaryPart secondaryPart) {
		getDao(SecondaryPartDao.class).save(secondaryPart);
		logUserAction(SAVED, secondaryPart);
	}

	public void deleteSecondaryPart(SecondaryPart secondaryPart) {
		getDao(SecondaryPartDao.class).remove(secondaryPart);
		logUserAction(REMOVED, secondaryPart);
	}

	public List<SecondaryPart> selectSecondaryParts() {
		return getDao(SecondaryPartDao.class).findAll();
	}

	// === defect type description === //
	public void createDefectTypeDescriptions(List<DefectTypeDescription> defectTypeDescriptions) {
		getDao(DefectTypeDescriptionDao.class).saveAll(defectTypeDescriptions);
		logUserAction(SAVED, defectTypeDescriptions);
	}

	public void deleteDefectTypeDescriptions(List<DefectTypeDescription> defectTypeDescriptions) {
		getDao(DefectTypeDescriptionDao.class).removeAll(defectTypeDescriptions);
		logUserAction(REMOVED, defectTypeDescriptions);
	}

	public List<DefectTypeDescription> selectDefectTypeDescriptions(DefectTypeDescription criteria) {
		return getDao(DefectTypeDescriptionDao.class).findAllBy(
				criteria.getDefectGroupName(), criteria.getDefectTypeName(),criteria.getSecondaryPartName());
	}

	// === defect description === //
	public void createDefectDescriptions(List<DefectDescription> defectDescriptions) {
		getDao(DefectDescriptionDao.class).saveAll(defectDescriptions);
		logUserAction(SAVED, defectDescriptions);
	}

	public void deleteDefectDescriptions(List<DefectDescription> defectDescriptions) {
		getDao(DefectDescriptionDao.class).removeAll(defectDescriptions);
		logUserAction(REMOVED, defectDescriptions);
	}

	public List<DefectDescription> selectDefectDescriptions(List<InspectionPartDescription> inspectionPartDescriptions) {
		List<DefectDescription>  totalDefectDescriptions = new ArrayList<DefectDescription>();

		for(InspectionPartDescription item : inspectionPartDescriptions) {
			List<DefectDescription> defectDescriptions  = getDao(DefectDescriptionDao.class)
					.findAllBy(item.getPartGroupName(), item.getInspectionPartName(),item.getInspectionPartLocationName()); 
			totalDefectDescriptions.addAll(defectDescriptions);
		}
		return totalDefectDescriptions;
	}

	public List<DefectDescription> selectDefectDescriptions(List<InspectionPartDescription> inspectionPartDescriptions, DefectGroup defectGroup) {
		List<DefectDescription>  totalDefectDescriptions = new ArrayList<DefectDescription>();
		
		for(InspectionPartDescription item : inspectionPartDescriptions) {
			List<DefectDescription> defectDescriptions  = getDao(DefectDescriptionDao.class)
			.findAllByDefectGroup(item.getPartGroupName(), item.getInspectionPartName(),
					              item.getInspectionPartLocationName(),defectGroup.getDefectGroupName()); 
			totalDefectDescriptions.addAll(defectDescriptions);
		}
		return totalDefectDescriptions;
	}
	
	public List<DefectDescription> selectDefectDescriptionsByDefectProperties(
			String responsibleDept, String responsibleZone,
			String iqsCategoryName, String iqsItemName, String regressionCode) {
		return getDao(DefectDescriptionDao.class).findAllByDefectProperties(
				responsibleDept, responsibleZone, iqsCategoryName, iqsItemName, regressionCode);
	}

	public List<DefectDescription> selectDefectDescriptionsByDefectTypeDescriptions(List<DefectTypeDescription> defectTypeDescriptions) {
		List<DefectDescription>  totalDefectDescriptions = new ArrayList<DefectDescription>();
		
		for(DefectTypeDescription item : defectTypeDescriptions) {
			List<DefectDescription> defectDescriptions  = 
				getDao(DefectDescriptionDao.class).findAllByDefectType(item.getDefectTypeName(), item.getSecondaryPartName());
			totalDefectDescriptions.addAll(defectDescriptions);
		}
		return totalDefectDescriptions;
	}

	// === general selecctors === //
	public List<Division> selectDepartments() {
		return getDao(DivisionDao.class).findById(getClientModel().getSiteName());
	}

	public List<Line> selectLines(Division division) {
		return getDao(LineDao.class).findAllByDivisionId(division, false);
	}

	public List<Zone> selectZones(String divisionId) {
		return getDao(ZoneDao.class).findAllByDivisionId(divisionId);
	}
	
	public List<Line> selectLines() {
		return getDao(LineDao.class).findAll();
	}

	public List<Zone> selectZones() {
		return getDao(ZoneDao.class).findAll();
	}

	public List<ProcessPoint> selectProcessPoints(Division division) {
		List<ProcessPoint> processPoints = getDao(ProcessPointDao.class).findAllByApplicationType(ApplicationType.getQicsApplicationTypes(), division.getDivisionId());
		return processPoints;
	}
	
	public List<Iqs> selectIqs() {
		return getDao(IqsDao.class).findAll();
	}

	public List<Regression> selectRegression() {
		return getDao(RegressionDao.class).findAll();
	}


	public QicsMaintenanceModel getClientModel() {
		return clientModel;
	}

	public void setClientModel(QicsMaintenanceModel clientModel) {
		this.clientModel = clientModel;
	}
	
	public List<Iqs> selectDistinctIqs(){
		return getDao(IqsDao.class).findAllIqsByDistinct();
	}
	
	public List<Iqs> selectDistinctIqsItems(){
		return getDao(IqsDao.class).findAllIqsItemsByDistinct();
	}
	
	public List<Iqs> findAllByCategoryName(String categoryName) {
		return getDao(IqsDao.class).findAllByCategoryName(categoryName);
	}
	
	public List<InspectionTwoPartDescription> selectInspecTwoPartDescriptions(List<InspectionPartDescription> inspectionPartDescriptions,String partGroupName) {
		List<InspectionTwoPartDescription>  totalInspTwoPartDescriptions = new ArrayList<InspectionTwoPartDescription>();
		for(InspectionPartDescription inspPartDesc:inspectionPartDescriptions)
		{
			totalInspTwoPartDescriptions.addAll(getDao(InspectionTwoPartDescriptionDao.class).findByPartGroupInspLocPartName(partGroupName, inspPartDesc.getInspectionPartLocationName(), inspPartDesc.getInspectionPartName()));
		}
		return totalInspTwoPartDescriptions;
	}
	
	protected void logUserAction(String message, Object object) {
		if(object != null) {
			getLogger().info(getUserInfo(), message, " ", object.getClass().getSimpleName(), ": ", object.toString());
		}
	}

	protected void logUserAction(String message, List<?> items) {
		for(Object object : items) {
			logUserAction(message, object);
		}
	}
	
	protected String getUserInfo() {
		return "User " + ClientMain.getInstance().getAccessControlManager().getUserName() + " ";
	}

	public Logger getLogger() {
		return logger;
	}
}
