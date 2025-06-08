package com.honda.galc.client.qi.repairentry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.data.QiCommonDefectResult;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.defect.ScrapService;
import com.honda.galc.service.QiClearParking;
import com.honda.galc.dao.qi.QiRepairAreaSpaceDao;
import com.honda.galc.data.ProductType;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

/**
 * <h3>ScrapDialogController Class description</h3>
 * <p>
 * ScrapDialogController description
 * </p>
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
 *         Dec 16, 2016
 *
 *
 */
public class ScrapDialogController extends AbstractScrapDialogController {
	/**
	 * @param model
	 * @param view
	 */
	public ScrapDialogController(RepairEntryModel model, ScrapDialog view, String associateId) {
		super(model, view, associateId);
	}

	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton button = (LoggedButton) actionEvent.getSource();
			if ("Scrap".equals(button.getText()))
				scrapProduct();
			else
				cancelBtnAction();
		}
	}

	/**
	 * This method will be used to scrap product. It will perform two action
	 * mentioned below- <br>
	 * 1- Change status of all the defects under selected product/VIN <br>
	 * 2- Make an scrap entry in GAL136TBX table.
	 * 
	 */
	protected void scrapProduct() {
		if (!getDialog().getDefectToScrapLabel().getText().isEmpty()) {

			if (getModel().isScrapReasonRequired()
					&& StringUtils.isBlank(getDialog().getScrapReasonTextArea().getText())) {
				publishErrorMessage(QiConstant.SCRAP_REASON);
				return;
			}
			if (RepairEntryView.getParentCachedDefectList().isEmpty()) {
				if (MessageDialog.confirm(getDialog(), "You are about to Scrap Product. Do you wish to continue?",
						false)) {

					// check current tracking status again
					if (getModel().isPreviousLineInvalid()) {
						publishProductPreviousLineInvalidEvent();
						return;
					}

					QiRepairResultDto qiDefectResultDto = null;
					QiRepairResultDto qiRepairResultDto = null;
					ScrapDialog myView = (ScrapDialog)getDialog();
					//if selected defect is main defect
					if(myView.getParentTableView().getSelectionModel().getSelectedItem().getParent() == myView.getParentTableView().getRoot())  {
						qiDefectResultDto = myView.getParentTableView().getSelectionModel().getSelectedItem().getValue();
						qiRepairResultDto = getNewAcutalProblem(getDialog().getDefectToScrap());
						List<QiRepairResultDto> repairResults = new ArrayList<QiRepairResultDto>();
						repairResults.add(qiRepairResultDto);
						ObservableList<TreeItem<QiRepairResultDto>> childNodes = myView.getParentTableView().getSelectionModel().getSelectedItem().getChildren();
						if(childNodes != null && !childNodes.isEmpty())  {
							for(TreeItem<QiRepairResultDto> myNode : childNodes)  {
								if(myNode != null)  {
									repairResults.add(myNode.getValue());
								}
							}
						}
						if(!repairResults.isEmpty())  {
							qiDefectResultDto.setChildRepairResultList(repairResults);
						}
					}
					else  {  //selected item is repair result
						qiRepairResultDto = myView.getParentTableView().getSelectionModel().getSelectedItem().getValue();
						qiDefectResultDto = myView.getParentTableView().getSelectionModel().getSelectedItem().getParent().getValue();
					}
					if (!getModel().getProductModel().isTrainingMode()) {
						updateQiRepairResult(qiRepairResultDto);
						updateQiDefectResult(qiDefectResultDto);

					} else {
						updateCacheforTrainingMode(qiDefectResultDto);
					}

					ScrapService scrapService = ServiceFactory.getService(ScrapService.class);
					scrapService.scrapProduct(createRequestDc(qiRepairResultDto,
							new ArrayList<String>(Arrays.asList(qiRepairResultDto.getProductId().toString()))));
					
					getDialog().setUnitScraped(true);
					QiClearParking clearParked = ServiceFactory.getService(QiClearParking.class);
					clearParked.removeVinFromQicsParking(getModel().getProduct().getProductId());
					((Stage) getDialog().getScrapBtn().getScene().getWindow()).close();
				}
			} else {
				getDialog().displayValidationMessage(getDialog().getMsgLabel(),
						"Defect can not be scrapped in training mode", "error-message");
			}
		} else {
			getDialog().displayValidationMessage(getDialog().getMsgLabel(), "Please select a defect to scrap",
					"error-message");
		}
	}
 
	
	/**
	 * When user clicks on close button in the popup screen closeBtnAction
	 * method gets called.
	 */
	private void cancelBtnAction() {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured during cancel action ", "Failed to perform cancel action", e);
		}
	}

	@Override
	public void initListeners() {		
	}
}
