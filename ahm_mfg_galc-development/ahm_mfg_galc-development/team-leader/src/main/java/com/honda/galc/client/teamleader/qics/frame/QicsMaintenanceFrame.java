package com.honda.galc.client.teamleader.qics.frame;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.teamleader.qics.controller.QicsMaintenanceController;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.InspectionPartLocation;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.entity.qics.SecondaryPart;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>QicsMaintenanceFrame Class description</h3>
 * <p> QicsMaintenanceFrame description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Oct 31, 2011
 *
 *
 */
public class QicsMaintenanceFrame extends TabbedMainWindow {

	private static final long serialVersionUID = 1L;

	private QicsMaintenanceController controller;

	public QicsMaintenanceFrame(ApplicationContext appContext, Application application) throws SystemException {
		super(appContext, application);
	}

	@Override
	protected void init() {
		controller = createController();
		super.init();
	}
	
	@Override
	protected List<String> loadPanelConfigs() {
		List<String> panelIds = new ArrayList<String>();
		panelIds.add("INSPECTION_PART");
		panelIds.add("DEFECT");
		panelIds.add("PART_DEFECT_COMB");
		panelIds.add("IMAGE");
		panelIds.add("IMAGE_SECTION");
		panelIds.add("MODEL_ASSIGNMENT");
		panelIds.add("ACTUAL_PROBLEM_REPAIR_METHOD");
		panelIds.add("IQS_REGRESSION_CODE_MAINTENANCE");
		panelIds.add("TWO_PART_PAIR_MAINTENANCE");
		panelIds.add("TWO_PART_DEFECT_COMB_MAINTENANCE");
		return panelIds;
	}
	
		protected QicsMaintenanceController createController() {
		QicsMaintenanceController controller = new QicsMaintenanceController(this);
		try {
			controller.getClientModel().setSiteName(PropertyService.getSiteName());

			List<Division> departments = controller.selectDepartments();
			List<PartGroup> partGroups = controller.selectPartGroups();
			List<InspectionPart> parts = controller.selectParts();
			List<InspectionPartLocation> partLocations = controller.selectPartLocations();

			List<Image> images = controller.selectImages();
			List<DefectGroup> defectGroups = controller.selectDefectGroups();
			List<DefectType> defectTypes = controller.selectDefectTypes();
			List<SecondaryPart> secondaryParts = controller.selectSecondaryParts();

			String productType = ProductTypeCatalog.getProductType(getApplicationPropertyBean().getProductType()).toString();
			List<String> models = controller.selectModels(productType);

			controller.getClientModel().setDepartments(departments);
			controller.getClientModel().setPartGroups(partGroups);
			controller.getClientModel().setParts(parts);
			controller.getClientModel().setPartLocations(partLocations);

			controller.getClientModel().setImages(images);
			controller.getClientModel().setDefectGroups(defectGroups);
			controller.getClientModel().setDefectTypes(defectTypes);
			controller.getClientModel().setSecondaryParts(secondaryParts);

			controller.getClientModel().setModels(models);

		} catch (PropertyException e) {
			throw new RuntimeException(e);
		}
		return controller;
	}

	// === get/set === //
	public QicsMaintenanceController getController() {
		return controller;
	}

	protected void setController(QicsMaintenanceController controller) {
		this.controller = controller;
	}


}
