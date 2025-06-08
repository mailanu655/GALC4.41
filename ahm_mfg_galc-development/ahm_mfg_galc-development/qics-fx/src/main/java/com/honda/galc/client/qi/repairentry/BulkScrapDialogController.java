package com.honda.galc.client.qi.repairentry;


import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.data.QiCommonDefectResult;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.defect.ScrapService;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class BulkScrapDialogController extends AbstractScrapDialogController {

	public BulkScrapDialogController(RepairEntryModel model, AbstractScrapDialog nonRepaiableDialog, String associateId) {
		super(model, nonRepaiableDialog, associateId);
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
			if (AbstractRepairEntryView.getParentCachedDefectList().isEmpty()) {
				if (MessageDialog.confirm(getDialog(), "You are about to Scrap Product. Do you wish to continue?",
						false)) {

					// check current tracking status again
					if (getModel().isPreviousLineInvalid()) {
						publishProductPreviousLineInvalidEvent();
						return;
					}

					List<String> naqScrappedProductList = new ArrayList<String>();

					QiCommonDefectResult selectedCommonDefect = new QiCommonDefectResult(
							getDialog().getDefectToScrap());
					for (QiRepairResultDto currentDefect : ProductSearchResult.getDefectsProcessing()) {
						QiCommonDefectResult currentCommonDefect = new QiCommonDefectResult(currentDefect);
						if (selectedCommonDefect.equals(currentCommonDefect)
								&& !isChildDefect(getDialog().getDefectToScrap())) {
							//scrap when main defect is selected
							QiRepairResultDto qiDefectResultDto = ProductSearchResult
									.getDefectResult(selectedCommonDefect, currentDefect.getProductId());
							QiRepairResultDto qiRepairResultDto = createAcutalProblem(currentDefect);
							if (!getModel().getProductModel().isTrainingMode()) {
								updateQiRepairResult(qiRepairResultDto);
								updateQiDefectResult(qiDefectResultDto);
								naqScrappedProductList.add(qiRepairResultDto.getProductId());
							} else {
								updateCacheforTrainingMode(qiDefectResultDto);

							}
						} else if(isChildDefect(getDialog().getDefectToScrap())) {
							//scrap when actual problem is selected
							QiRepairResultDto qiDefectResultDto = ProductSearchResult
									.getParentProductDefect(selectedCommonDefect, currentDefect.getProductId());
							QiCommonDefectResult currentCommonParentDefect = new QiCommonDefectResult(
									qiDefectResultDto);
							if (currentCommonDefect.equals(currentCommonParentDefect)) {
								QiRepairResultDto qiRepairResultDto = ProductSearchResult
										.getChildDefect(qiDefectResultDto, selectedCommonDefect);
								if (!getModel().getProductModel().isTrainingMode()) {
									updateQiRepairResult(qiRepairResultDto);
									updateQiDefectResult(qiDefectResultDto);
									naqScrappedProductList.add(qiRepairResultDto.getProductId());
								} else {
									updateCacheforTrainingMode(qiDefectResultDto);

								}
							}

						}
					}

					if (!naqScrappedProductList.isEmpty() && !getModel().getProductModel().isTrainingMode()) {
						ScrapService scrapService = ServiceFactory.getService(ScrapService.class);
						scrapService
								.scrapProduct(createRequestDc(getDialog().getDefectToScrap(), naqScrappedProductList));
					}

					getDialog().setUnitScraped(true);

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
	
	
	@Override
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
