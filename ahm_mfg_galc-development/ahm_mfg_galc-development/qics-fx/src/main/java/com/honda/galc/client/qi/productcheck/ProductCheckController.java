package com.honda.galc.client.qi.productcheck;

import static com.honda.galc.client.product.action.ProductActionId.KEYBOARD;
import static com.honda.galc.client.product.action.ProductActionId.PRODUCT_CHECK_DONE;
import static com.honda.galc.client.product.action.ProductActionId.SEND_FINAL;
import static com.honda.galc.client.product.action.ProductActionId.UPDATE_REPAIR_AREA;
import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.qi.repairentry.RepairAreaMgmtDialog;
import com.honda.galc.client.qi.repairentry.RepairEntryModel;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qics.DefectDescriptionDao;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectDescriptionId;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.util.ProductCheckType;
import com.honda.galc.util.ProductCheckUtil;

import javafx.scene.control.TreeItem;


/**
* <h3>Class description</h3> <h4>Description</h4>
* <p>
* <code>ProductCheckController</code> is ... .
* </p>
* <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
* <TD>&nbsp;</TD>
* <TD>&nbsp;</TD>
* <TD>0.1</TD>
* <TD>(none)</TD>
* <TD>Initial Realse</TD>
* </TR>
* </TABLE>
* 
* @see
* @ver 0.1
* @author L&T Infotech
*/

public class ProductCheckController extends AbstractQiProcessController<ProductCheckModel, ProductCheckView> implements javafx.event.EventHandler<javafx.event.ActionEvent>{
	
	public ProductCheckController(ProductCheckModel model, ProductCheckView view) {
		super(model, view);
		EventBusUtil.register(this);
	}

	@Override
	public void handle(javafx.event.ActionEvent event) {
		
	}

	@Override
	public void initializeListeners() {
		
	}

	@Override
	public void initEventHandlers() {
		
	}
	public void submitWarnCheckProductState() {
		getLogger().info("excuting submitWarnCheckProductState(productCheckTypes) function");
		Map<String, Object> checkResults = submitCheckProductState(getProductWarnCheckTypes());
		getModel().setProductWarnCheckResults(checkResults);
	}
	
	protected String[] getProductWarnCheckTypes() {
		Map<String, String> checkTypesMap = getModel().getProperty().getProductWarnCheckTypes();
		
		return getProductCheckTypes(checkTypesMap);
	}
	public void submitItemCheckProductState() {
		getLogger().info("excuting submitItemCheckProductState(productCheckTypes) function");
		Map<String, Object> checkResults = submitCheckProductState(getProductCheckTypes());
		getModel().setProductItemCheckResults(checkResults);
	}
	protected String[] getProductCheckTypes() {
		Map<String, String> checkTypesMap = getModel().getProperty().getProductCheckTypes();
		return getProductCheckTypes(checkTypesMap);
	}
	protected String[] getProductCheckTypes(Map<String, String> checkTypesMap) {
		String[] checkTypes = {};
		if (checkTypesMap == null || checkTypesMap.isEmpty()) {
			return checkTypes;
		}
		String key = null;
		if (getProductModel() != null && getProductModel().getProduct() != null) {
			key = getProductModel().getProduct().getModelCode();
		}

		if (StringUtils.isBlank(key) || !checkTypesMap.keySet().contains(key)) {
			key = "*";
		}
		checkTypes = getProductCheckTypes(checkTypesMap, key);
		return checkTypes;
	}
	protected String[] getProductCheckTypes(Map<String, String> checkTypesMap, String key) {
		String[] checkTypes = {};
		if (checkTypesMap == null || checkTypesMap.isEmpty()) {
			return checkTypes;
		}
		String str = checkTypesMap.get(key);
		if (StringUtils.isBlank(str)) {
			return checkTypes;
		}
		checkTypes = StringUtils.trim(str).split(Delimiter.COMMA);
		return checkTypes;
	}
	private Map<String, Object> submitCheckProductState(String[] productCheckTypes) {
		if (productCheckTypes == null || productCheckTypes.length == 0) {
			return new LinkedHashMap<String,Object>();
		}
		return ProductCheckUtil.check(getModel().getProductModel().getProduct(), ApplicationContext.getInstance().getProcessPoint(), productCheckTypes);
	}
	
	public Map<String, Object> getProductItemCheckResults() {
		Map<String, Object> allResults = new LinkedHashMap<String, Object>();
		Map<String, Object> productChecks = getModel().getProductItemCheckResults();
		if (productChecks != null) {
			allResults.putAll(productChecks);
		}
		checkIfUpdatedOutstandingDefects(allResults);
		checkIfNewDefectsHaveFiringFlag(allResults);
		return allResults;
	}
	protected Map<String, Object> checkIfUpdatedOutstandingDefects(Map<String, Object> allResults) {
		String key = ProductCheckType.OUTSTANDING_DEFECTS_CHECK.toString();
		String[] productCheckTypes = getProductCheckTypes();
		if (Arrays.asList(productCheckTypes).contains(key)) {
			List<DefectResult> outstandingDefects = getModel().getOutstandingDefects();
			if (outstandingDefects.isEmpty()) {
				allResults.remove(key);
			} else {
				allResults.put(key, translateDefect(outstandingDefects));
			}
		}
		return allResults;
	}
	protected List<String> translateDefect(List<DefectResult> defects) {

		List<String> list = new ArrayList<String>();
		for (DefectResult defect : defects) {
			if (defect == null) {
				continue;
			}
			list.add(defect.toShortString());
		}
		return list;
	}
	protected Map<String, Object> checkIfNewDefectsHaveFiringFlag(Map<String, Object> allResults) {
		String key = ProductCheckType.ENGINE_FIRING_TEST_CHECK.toString();
		String[] productCheckTypes = getProductCheckTypes();
		if (Arrays.asList(productCheckTypes).contains(key)) {
			Object firingFlag = allResults.get(key);
			if (Boolean.TRUE.equals(firingFlag)) {
				return allResults;
			} else {
				List<DefectResult> newDefects = getModel().getNewDefects();
				if (isDefectsRequireEngineFiring(newDefects)) {
					allResults.put(key, true);
				}
			}
		}
		return allResults;
	}
	protected boolean isDefectsRequireEngineFiring(List<DefectResult> defects) {
		if (defects == null || defects.isEmpty()) {
			return false;
		}
		for (DefectResult defect : defects) {
			if (defect != null) {
				DefectDescription defectDescription = selectDefectDescription(defect);
				if (defectDescription != null && defectDescription.getEngineFiringFlag())
					return true;
			}
		}
		return false;
	}
	
	private DefectDescription selectDefectDescription(DefectResult defect) {
		List<DefectDescription> defectDescriptions = selectDefectDescriptions(defect.getDefectTypeName());
		for (DefectDescription defectDescription : defectDescriptions) {
			DefectDescriptionId id = new DefectDescriptionId();
			id.setInspectionPartName(defect.getInspectionPartName());
			id.setInspectionPartLocationName(defect.getInspectionPartLocationName());
			id.setDefectTypeName(defect.getDefectTypeName());
			id.setTwoPartPairPart(defect.getTwoPartPairPart());
			id.setTwoPartPairLocation(defect.getTwoPartPairLocation());
			id.setSecondaryPartName(defect.getSecondaryPartName());
			id.setPartGroupName(defect.getPartGroupName());
			if(defectDescription.getId().equals(id)) return defectDescription;
		}
		return null;
	}
	public List<DefectDescription> selectDefectDescriptions(String defectTypeName) {

		List<DefectDescription> defectDescriptions = getModel().getDefectDescriptions(defectTypeName);

		if (defectDescriptions == null) {
			defectDescriptions = getDao(DefectDescriptionDao.class).findAllByDefectType(defectTypeName);
			getModel().putDefectDescriptions(defectTypeName, defectDescriptions);
		}
		return defectDescriptions;

	}
	
	public Map<String, Object> getProductWarnCheckResults() {
		Map<String, Object> allResults = new LinkedHashMap<String, Object>();
		Map<String, Object> productChecks = getModel().getProductWarnCheckResults();
		if (productChecks != null) {
			allResults.putAll(productChecks);
		}
		return allResults;
	}
	
	/**
	 * This method is used to create Product Panel Buttons
	 */
	public ProductActionId[] getProductActionIds(){
		if (getModel().getProperty().isSentToFinal()) {
			if (getModel().getProperty().isUpdateRepairArea()) {
				return new ProductActionId[]{UPDATE_REPAIR_AREA,PRODUCT_CHECK_DONE,SEND_FINAL};
			} else {
				return new ProductActionId[]{KEYBOARD,PRODUCT_CHECK_DONE,SEND_FINAL};
			}
		} else {
			if (getModel().getProperty().isUpdateRepairArea()) {
				return new ProductActionId[]{UPDATE_REPAIR_AREA,PRODUCT_CHECK_DONE}; 
			} else {
				return new ProductActionId[]{KEYBOARD,PRODUCT_CHECK_DONE}; 
			}
		}
	}
	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		if (event != null && event.getEventType().equals(ProductEventType.PRODUCT_UPDATE_REPAIR_AREA) 
				&& event.getTargetObject()!=null && event.getTargetObject().toString().equals(ViewId.PRODUCT_CHECK.getViewLabel())){
			List<QiRepairResultDto> defectList = getDao(QiRepairResultDao.class).findAllDefectsByProductId(getProductModel().getProductId());
			if (defectList == null||defectList.size() == 0) return;

			RepairEntryModel repairEntryModel = new RepairEntryModel();
			repairEntryModel.setProductModel(getModel().getProductModel());
			for (QiRepairResultDto qiRepairResultDto : defectList) {
				if (qiRepairResultDto.getCurrentDefectStatus() == DefectStatus.NOT_FIXED.getId())  {
					//get first not fixed defect
					RepairAreaMgmtDialog repairAreaMgmtDialog = new RepairAreaMgmtDialog("Repair Area Management",
							repairEntryModel, getApplicationId(), new TreeItem<QiRepairResultDto>(qiRepairResultDto));
					repairAreaMgmtDialog.show();
					break;
				}
			}
		}
	}

}
