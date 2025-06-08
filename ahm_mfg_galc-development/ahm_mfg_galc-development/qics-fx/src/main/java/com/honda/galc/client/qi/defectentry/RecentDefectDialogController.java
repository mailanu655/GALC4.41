package com.honda.galc.client.qi.defectentry;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.qi.base.AbstractQiDialogController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.QiInspectionUtils;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.qi.QiResponsibilityMappingDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.qi.QiRecentDefectDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiResponsibilityMapping;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.service.ServiceFactory;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * <h3>RecentDefectDialogController Class description</h3>
 * <p> RecentDefectDialogController description </p>
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
 * @author L&T Infotech<br>
 * Nov 22, 2016
 *
 *
 */

public class RecentDefectDialogController extends AbstractQiDialogController<DefectEntryModel, RecentDefectDialog> {
	
	private static final String DUPLICATE_PART_DEFECT_ACCEPT_MSG = "Selected Part Defect Comb is already associated with the product.Do you wish to continue?";
	
	public RecentDefectDialogController(DefectEntryModel model,
			RecentDefectDialog recentDefectDialog) {
		super();
		setModel(model);
		setDialog(recentDefectDialog);
	}
 
	
	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			List<QiRecentDefectDto> dtoList = getDialog().getDefectResultTablePane().getSelectedItems();
			LoggedButton button = (LoggedButton) actionEvent.getSource();
			if (QiConstant.APPLY.equals(button.getText()) || QiConstant.ACCEPT.equals(button.getText())) {
				for(QiRecentDefectDto dto : dtoList)  {
					addRecentDefect(actionEvent, QiDefectResult.createInstance(dto), getDialog().getDefectStatus(), getDialog().getWriteUpDept(), dto.getDefectResultId());
				}
			} else {
				cancelBtnAction(actionEvent);
			}
		} 
	}

	@Override
	public void initListeners() {}

	/**
	 * When user clicks on close button in the popup screen closeBtnAction method gets called.
	 */
	private void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			getModel().setDefectResult(null);
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}
	
	/**
	 * This method is used to add PDC which user wants to save
	 * @param qiDefectResult
	 */
	private void addRecentDefect(ActionEvent event, QiDefectResult qiDefectResult, String defectStatus, String writeUpDept, long defectResultId){
		updateDefectResult(qiDefectResult);
		QiStationConfiguration dupWarnSetting=getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.DUPLICATE.getSettingsName());
		String allowDuplicate = QiEntryStationConfigurationSettings.DUPLICATE.getDefaultPropertyValue();
		if(dupWarnSetting != null && dupWarnSetting.getPropertyValue() != null)	allowDuplicate = dupWarnSetting.getPropertyValue();
		
		QiStationConfiguration disableAcceptSetting =getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.DISABLE_ACCEPT_DEFECT.getSettingsName());
		String disableAccept = QiEntryStationConfigurationSettings.DISABLE_ACCEPT_DEFECT.getDefaultPropertyValue();
		if(disableAcceptSetting != null && disableAcceptSetting.getPropertyValue() != null)	disableAccept = disableAcceptSetting.getPropertyValue();
		
		if(getDialog().getDefectResultTablePane().getSelectedItem()!=null){
			List<QiDefectResult> resultList = getModel().findAllByProductId(new ArrayList<Short>());
			Stage stage = (Stage) getDialog().getApplyBtn().getScene().getWindow();
			for(QiDefectResult result : resultList){
				if(	qiDefectResult.getPartDefectDesc().equals(result.getPartDefectDesc())){
					if (allowDuplicate.equalsIgnoreCase(QiEntryStationConfigurationSettings.DUPLICATE.getAvailablePropertyValue1()) ||
						(allowDuplicate.equalsIgnoreCase(QiEntryStationConfigurationSettings.DUPLICATE.getAvailablePropertyValue2()) &&
						MessageDialog.confirm(stage, DUPLICATE_PART_DEFECT_ACCEPT_MSG))) break;
					else if (allowDuplicate.equalsIgnoreCase(QiEntryStationConfigurationSettings.DUPLICATE.getDefaultPropertyValue())) {
						getDialog().displayValidationMessage(getDialog().getMsgLabel(),"Selected Part Defect Combination is already assigned.", "error-message");
						return;
					} else return;
				}
			}
			setDefectResult(qiDefectResult, defectStatus, writeUpDept, defectResultId);
			stage.close();
		} else {
			getDialog().displayValidationMessage(getDialog().getMsgLabel(),"Please select a defect to add", "error-message");
		}
	}
	
	private void updateDefectResult(QiDefectResult qiDefectResult) {
		if(StringUtils.isEmpty(qiDefectResult.getProductId())) qiDefectResult.setProductId(getModel().getProductId());
		if(StringUtils.isEmpty(qiDefectResult.getApplicationId())) qiDefectResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());

		QiDefectResult defectResult = getModel().findDefect(qiDefectResult, getModel().getProcessPointId());
		if(defectResult != null) {
			qiDefectResult.setEntryModel(defectResult.getEntryModel());
			qiDefectResult.setEntryScreen(defectResult.getEntryScreen());
			qiDefectResult.setEntrySiteName(defectResult.getEntrySiteName());
			qiDefectResult.setEntryPlantName(defectResult.getEntryPlantName());
			qiDefectResult.setEntryProdLineNo(defectResult.getEntryProdLineNo());
			qiDefectResult.setEntryDept(defectResult.getEntryDept());
			qiDefectResult.setProductType(defectResult.getProductType());
			qiDefectResult.setTerminalName(defectResult.getTerminalName());
			
			qiDefectResult.setThemeName(defectResult.getThemeName());
			qiDefectResult.setLocalTheme(defectResult.getLocalTheme());
			qiDefectResult.setDefectCategoryName(defectResult.getDefectCategoryName());
			qiDefectResult.setBomMainPartNo(defectResult.getBomMainPartNo());
			
			qiDefectResult.setIqsCategoryName(defectResult.getIqsCategoryName());
			qiDefectResult.setIqsScore(defectResult.getIqsScore());
			qiDefectResult.setIqsVersion(defectResult.getIqsVersion());
			qiDefectResult.setIqsQuestionNo(defectResult.getIqsQuestionNo());
			qiDefectResult.setIqsQuestion(defectResult.getIqsQuestion());
			
			
			if(StringUtils.isNotEmpty(defectResult.getImageName())) {
				qiDefectResult.setImageName(defectResult.getImageName());
				qiDefectResult.setPointX(defectResult.getPointX());
				qiDefectResult.setPointY(defectResult.getPointY());
			}
		}
	}

	/**
	 * This method is used to save Defect
	 * @param qiDefectResult
	 */
	private void setDefectResult(QiDefectResult qiDefectResult, String defectStatus, String writeUpDept, long defectResultId) {
		qiDefectResult.setCreateTimestamp(null);
		qiDefectResult.setUpdateTimestamp(null);
		qiDefectResult.setCreateUser(getModel().getUserId().toUpperCase());
		qiDefectResult.setUpdateUser(null);
		qiDefectResult.setProductId(getModel().getProductId());
		qiDefectResult.setDefectResultId(0);
		qiDefectResult.setProductionLot(getModel().getProductionLot());
		qiDefectResult.setProductSpecCode(getModel().getProductSpecCode());
		
		if (isFrameQicsEngineSource()) {
			String respLevel = checkEngineSource(qiDefectResult);
			if (respLevel != null)
				qiDefectResult.setResponsibleLevel1(respLevel);
		}
		
		if(getModel().getProductType().equalsIgnoreCase(ProductType.FRAME.getProductName()) || 
				getModel().getProductType().equalsIgnoreCase(ProductType.ENGINE.getProductName())) {
			/**
			 * If Product Type is FRAME or ENGINE
			 */
			Product product = (Product)getModel().getProduct();
			if (product != null) {
				qiDefectResult.setAfOnSequenceNumber(product.getAfOnSequenceNumber()!=null ? product.getAfOnSequenceNumber().intValue() : 0);
				qiDefectResult.setKdLotNumber(product.getKdLotNumber());
				qiDefectResult.setProductionDate(product.getProductionDate());
			}
		}
		
		List<QiDefectResult> defectResultList = getModel().getCachedDefectResultList();
		if(defectResultList != null && !defectResultList.isEmpty()) {
			QiDefectResult lastDefectEntered = defectResultList.get(defectResultList.size() - 1);
			if(lastDefectEntered.getOriginalDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()
					&& lastDefectEntered.getCurrentDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()) {
				MessageDialog.showError(QiConstant.POST_SCRAP_ACCEPT_DEFECT_ERROR_MSG);
				return;
			}
		}
		
		qiDefectResult.setDefectStatus(defectStatus);
		qiDefectResult.setWriteUpDept(writeUpDept);
		qiDefectResult.setGdpDefect((QiInspectionUtils.isGdpProcessPoint(getModel().getCurrentWorkingProcessPointId()) && qiDefectResult.getCurrentDefectStatus()==(short)DefectStatus.NOT_FIXED.getId())?(short)1:(short)0);			
		qiDefectResult.setTrpuDefect((QiInspectionUtils.isTrpuProcessPoint(getModel().getCurrentWorkingProcessPointId())) ? (short) 1 : (short) 0);

		if(QiInspectionUtils.isGlobalGdpEnabled() 
				&& QiInspectionUtils.isGdpProcessPoint(getModel().getCurrentWorkingProcessPointId())
				&& QiInspectionUtils.isGlobalGdpWriteUpDept(qiDefectResult.getWriteUpDept())  
				&& (qiDefectResult.getTrpuDefect() == (short) 1)) {
			 		qiDefectResult.setGdpDefect((short) 1);
		 		}

		qiDefectResult.setDefaultResponsibleLevel1(getModel().findDefaultResponsibleLevel1(defectResultId));
		
		getModel().getCachedDefectResultList().add(qiDefectResult);
		EventBusUtil.publish(new StatusMessageEvent("Part Defect Comb successfully accepted", StatusMessageEventType.INFO));
		EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_DEFECT_ACCEPT));
	}

	/**
	 * This method is used to get all recent defect list
	 * @param dbResult
	 * @param recentDefectRange
	 * @param entryDept 
	 */
	public List<QiRecentDefectDto> getRecentDefectList(List<QiDefectResult> dbResult,
			Integer recentDefectRange) {
		
		String entryDept;
		String entryModel = getModel().findByMtcModel().getId().getEntryModel();
		List<String> mtcModelList = getModel().findAllMtcModelByEntryModel(entryModel);
		if(!getModel().getProperty().isUpcStation()){
			entryDept = getModel().getCurrentWorkingEntryDept();
		}else{
			entryDept = getModel().getEntryDept();
		}
		
		StringBuilder sb = new StringBuilder(); 
		for (String str : mtcModelList){
			sb.append("'"+str+"',"); 
			} 
		sb.deleteCharAt(sb.length() -1); 
		return getModel().findAllByMtcModelAndEntryDept(sb.toString(),entryDept,recentDefectRange,getModel().getCurrentWorkingProcessPointId());		
	}
	
	private String checkEngineSource(QiDefectResult qiDefectResult) {
		List<QiResponsibilityMapping> listRespLevel = ServiceFactory.getDao(QiResponsibilityMappingDao.class).findAll();
		if (listRespLevel.isEmpty()) return null;
		QiResponsibleLevel respLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class)
				.findBySitePlantDepartmentAndLevelName(qiDefectResult.getResponsibleSite(),
						qiDefectResult.getResponsiblePlant(), qiDefectResult.getResponsibleDept(),
						qiDefectResult.getResponsibleLevel1());
		if (respLevel == null) return null;
		Product product = (Product)getModel().getProduct();
		Frame vehicle = (Frame) product;
		if (vehicle.getEngineSerialNo() == null || vehicle.getEngineSerialNo().isEmpty()) return null;
		Engine engine = ServiceFactory.getDao(EngineDao.class).findByKey(vehicle.getEngineSerialNo());
		if (engine == null) return null;
		for (QiResponsibilityMapping each : listRespLevel) {
			if (respLevel.getResponsibleLevelId().equals(each.getDefaultRespLevel())
					&& each.getPCode().equals(engine.getPlantCode())) {
				QiResponsibleLevel altRespLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class)
						.findByResponsibleLevelId(each.getAlternateDefault());
				if (altRespLevel == null) break;
				return altRespLevel.getResponsibleLevelName();
			}
		}
		return null;
	}
}
