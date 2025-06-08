package com.honda.galc.client.qi.defectentry;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.data.QiCommonDefectResult;
import com.honda.galc.client.enumtype.ObservableListChangeEventType;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.qi.base.AbstractQiDialogController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.ObservableListChangeEvent;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.MediaUtils;
import com.honda.galc.client.utils.MicroserviceUtils;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.QiInspectionUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.qi.QiAppliedRepairMethodDao;
import com.honda.galc.dao.qi.QiDefectResultHistDao;
import com.honda.galc.dao.qi.QiDefectResultImageDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiResponsibilityMappingDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dto.Auditable;
import com.honda.galc.dto.ExistingDefectDto;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.dto.qi.QiStationResponsibilityDto;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.enumtype.QiEntryStationDefaultStatus;
import com.honda.galc.entity.enumtype.QiReportable;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.qi.QiAppliedRepairMethod;
import com.honda.galc.entity.qi.QiAppliedRepairMethodId;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.entity.qi.QiDefectResultImage;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiResponsibilityMapping;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.KeyValue;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * <h3>ExistingDefectDialogController Class description</h3>
 * <p> ExistingDefectDialogController description </p>
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
 * Nov 18, 2016
 *
 *
 */
/**
 * 
 * 
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017 Simulation changes
 */

public class ExistingDefectDialogController extends AbstractQiDialogController<DefectEntryModel, ExistingDefectDialog> {

	private static final String CHANGED_TO_FIXED = "Changed to Fixed from Defect Entry Existing Defects Screen";
	private static final String CHANGED_TO_NOT_FIXED = "Changed to Not Fixed from Defect Entry Existing Defects Screen";
	private static final String PRODUCT_SHIPPED_MSG = "Product has already been shipped. Failed to Save Defect Results.";
	private static final String IN_LINE_REPAIR_METHOD = "IN LINE REPAIR";
	private static final String NO_PROBLEM_FOUND = "NO PROBLEM FOUND";
	private ProductModel productModel;
	private String quantity;
	private Set<Integer> selectedList = new HashSet<Integer>();
	private List<QiDefectResult> finalResultList = new ArrayList<QiDefectResult>();
	private List<QiStationResponsibilityDto> assignedStationResponsibilities;
	boolean isAllowOverwriteResponsibility = false;
	ResponsibleLevelController respController = null;

	public ExistingDefectDialogController(DefectEntryModel model, ProductModel productModel,
			ExistingDefectDialog existingDefectDialog, String quantity) {
		super();
		this.productModel = productModel;
		setModel(model);
		this.quantity = quantity;
		setDialog(existingDefectDialog);
		EventBusUtil.register(this);
		isAllowOverwriteResponsibility = isAllowOverwriteResponsibility();
		assignedStationResponsibilities = new ArrayList<QiStationResponsibilityDto>(
				getModel().findAllAssignedStationResponsibilitiesByProcessPoint());
	}

	@Override
	public void close() {
		super.close();
		EventBusUtil.unregister(this);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		List<ExistingDefectDto> existingDefectDtoList = getDialog().getDefectResultTablePane().getTable()
				.getSelectionModel().getSelectedItems();
		List<QiDefectResult> qiDefectResultList = existingDefectDtoList.stream().map(d -> d.getQiDefectResult())
				.collect(Collectors.toList());

		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton button = (LoggedButton) actionEvent.getSource();
			if (getDialog().getDefectResultTablePane().getTable().getSelectionModel().getSelectedItem() == null) {
				if (QiConstant.VOID_LAST.equals(button.getText()))
					voidLastButtonAction(actionEvent);
				else if (QiConstant.VOID_ALL.equals(button.getText()))
					voidAllBtnAction(actionEvent);
				else
					closeBtnAction(actionEvent);
			} else {
				QiDefectResult qiDefectResult = getDialog().getDefectResultTablePane().getTable().getSelectionModel()
						.getSelectedItem().getQiDefectResult();

				if (QiConstant.APPLY.equals(button.getText()))
					updateResponsibility(actionEvent, qiDefectResult);
				else if (QiConstant.VOID_LAST.equals(button.getText()))
					voidLastButtonAction(actionEvent);
				else if (QiConstant.VOID_ALL.equals(button.getText()))
					voidAllBtnAction(actionEvent);
				else if (QiConstant.VOID.equals(button.getText()))
					voidDefect(actionEvent, qiDefectResult);
				else if (QiConstant.NO_PROBLEM_FOUND.equals(button.getText()))
					updateNoProblemFoundForDefects(qiDefectResultList, true);
				else if (QiConstant.CHANGE_TO_FIXED.equals(button.getText()))
					changeCurrentDefectStatus(qiDefectResultList, true, false);
				else if (QiConstant.CHANGE_TO_NOT_FIXED.equals(button.getText()))
					changeCurrentDefectStatus(qiDefectResultList, false, false);
				else if (QiConstant.UPLOAD_IMAGE_VIDEO.equals(button.getText()))
					uploadFile(qiDefectResult);
				else
					closeBtnAction(actionEvent);
			}
		}
	}

	private void uploadFile(QiDefectResult qiDefectResult) {
		File sourceFile = MediaUtils.browseMediaFile();
		if (sourceFile != null) {

			QiDefectResultImage defectResultImage = new QiDefectResultImage(qiDefectResult.getDefectResultId(), null);
			defectResultImage.setApplicationId(getModel().getApplicationId());
			defectResultImage.setCreateUser(getUserId());

			if (qiDefectResult.getDefectResultId() > 0) {
				String url = MicroserviceUtils.getInstance().postFile(sourceFile);
				if (url != null) {
					defectResultImage.getId().setImageUrl(url);
					ServiceFactory.getDao(QiDefectResultImageDao.class).save(defectResultImage);
				} else {
					String msg = "Failed to upload media file: " + sourceFile.getName();
					displayErrorMessage(msg, msg);
				}
			} else {
				defectResultImage.setFile(sourceFile);
				qiDefectResult.getDefectResultImages().add(defectResultImage);
			}

		}
	}

	@Override
	public void initListeners() {
		addDefectTableListener();
		addSiteComboBoxListener();
		addPlantComboBoxListener();
		addDeptComboBoxListener();
		enableChangeToFixedButton(new ArrayList<QiDefectResult>());
		enableNoProblemFoundButton(new ArrayList<QiDefectResult>());
		if (getRespController().isShowL2L3()) {
			getRespController().setAssignedStationResponsibilities(assignedStationResponsibilities);
			getRespController().addListener();
		}
	}

	@Subscribe()
	public void onObjectTablePaneEvent(ObservableListChangeEvent event) {
		if (event == null)
			return;

		if (event.getEventType().equals(ObservableListChangeEventType.ADD)) {
			getDialog().getDefectResultTablePane().getTable().getSelectionModel().clearSelection();
			for (Integer item : selectedList) {
				getDialog().getDefectResultTablePane().getTable().getSelectionModel().select(item);
				Logger.getLogger().check("Defect Result Table row : " + item.intValue() + " selected");
			}

			if (selectedList.size() == 1) {
				QiDefectResult defectResult = getDialog().getDefectResultTablePane().getTable().getSelectionModel()
						.getSelectedItem().getQiDefectResult();
				if (defectResult.getActualTimestamp() == null) { // cached defect
					getDialog().getVoidButton().setDisable(false);
					changeDefectStatus(defectResult);
					changeResponsible(defectResult, isAllowOverwriteResponsibility);
				} else {
					disableComponents(true);
				}
			} else {
				disableComponents(true);
			}
		}
	}

	/**
	 * This method is for Defect Table Listeners
	 */
	private void addDefectTableListener() {
		getDialog().getDefectResultTablePane().getTable().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<ExistingDefectDto>() {
					public void changed(ObservableValue<? extends ExistingDefectDto> observableValue,
							ExistingDefectDto oldValue, ExistingDefectDto newValue) {
						if (null != newValue) {
							clearDisplayMessage();
						}
						List<ExistingDefectDto> selectedDefectResultList = getDialog().getDefectResultTablePane()
								.getSelectedItems();

						if (!isAllowOverwriteResponsibility && selectedDefectResultList.isEmpty()) {
							disableComponents(true);
						} else if (newValue != null) {
							getDialog().getApplyBtn().setDisable(false);
							if (newValue.getQiDefectResult().getActualTimestamp() != null) { // saved defect
								disableComponents(true);
							} else { // cached defect
								changeDefectStatus(newValue.getQiDefectResult());
								changeResponsible(newValue.getQiDefectResult(), isAllowOverwriteResponsibility);
								getDialog().getVoidButton().setDisable(false);
							}
						}

						if (selectedDefectResultList.isEmpty()) {
							getDialog().getApplyBtn().setDisable(true);
						} else {
							for (ExistingDefectDto defectResult : selectedDefectResultList) {
								if (defectResult.getQiDefectResult().getActualTimestamp() != null) { // saved defect
									getDialog().getApplyBtn().setDisable(true);
									break;
								}
							}
						}
						getDialog().getUploadImageVideoButton().setDisable((selectedDefectResultList.size() != 1)
								|| !MicroserviceUtils.getInstance().isServiceAvailable());
						enableChangeToFixedButton(selectedDefectResultList.stream().map(d -> d.getQiDefectResult())
								.collect(Collectors.toList()));
						enableNoProblemFoundButton(selectedDefectResultList.stream().map(d -> d.getQiDefectResult())
								.collect(Collectors.toList()));
					}
				});

		getDialog().getDefectResultTablePane().getTable()
				.setRowFactory(new Callback<TableView<ExistingDefectDto>, TableRow<ExistingDefectDto>>() {
					public TableRow<ExistingDefectDto> call(TableView<ExistingDefectDto> tableView) {
						final TableRow<ExistingDefectDto> row = new TableRow<ExistingDefectDto>() {
							@Override
							protected void updateItem(ExistingDefectDto qiDefectResult, boolean paramBoolean) {
								super.updateItem(qiDefectResult, paramBoolean);
								this.getStyleClass().add("row-style");
								if (qiDefectResult != null
										&& qiDefectResult.getQiDefectResult().getActualTimestamp() != null
										&& qiDefectResult.getQiDefectResult().getProductId()
												.equalsIgnoreCase(getModel().getProductId())
										&& (qiDefectResult.getQiDefectResult()
												.getCurrentDefectStatus() == (short) DefectStatus.FIXED.getId()
												|| qiDefectResult.getQiDefectResult()
														.getCurrentDefectStatus() == (short) DefectStatus.NON_REPAIRABLE
																.getId())) {
									this.setDisable(false);
									this.setStyle("-fx-text-background-color: green;");
									this.setOpacity(1);
								} else if (qiDefectResult != null) {
									this.setDisable(false);
									if (qiDefectResult.getQiDefectResult()
											.getCurrentDefectStatus() == (short) DefectStatus.NOT_FIXED.getId()
											&& qiDefectResult.getQiDefectResult().getActualTimestamp() != null)
										this.setStyle("-fx-text-background-color: red;");
									else
										this.setStyle("-fx-text-background-color: black;");
									this.setOpacity(1);
								}
							}
						};
						row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
							public void handle(MouseEvent event) {
								if (event.getButton() == MouseButton.PRIMARY) {
									final int index = row.getIndex();
									if (index >= 0
											&& index < getDialog().getDefectResultTablePane().getTable().getItems()
													.size()
											&& getDialog().getDefectResultTablePane().getTable().getSelectionModel()
													.isSelected(index)) {
										selectedList.remove(index);
										event.consume();
									} else if (getDialog().getDefectResultTablePane().getTable().getSelectionModel()
											.isSelected(index)
											|| index < getDialog().getDefectResultTablePane().getTable().getItems()
													.size()) {
										selectedList.add(index);
									}
									EventBusUtil.publish(
											new ObservableListChangeEvent("", ObservableListChangeEventType.ADD));

								}
							}
						});
						return row;
					}
				});

	}

	/**
	 * This method is used to disable components
	 * 
	 * @param disable
	 */
	private void disableComponents(boolean disable) {
		disableResponsibilityPane(disable);
		getDialog().getStatusPane().setDisable(disable);
		getDialog().getVoidButton().setDisable(disable);
	}

	private void disableResponsibilityPane(boolean disable) {
		getDialog().getRespSite().setDisable(disable);
		getDialog().getRespPlant().setDisable(disable);
		getDialog().getRespDept().setDisable(disable);
		if (!getRespController().isShowL2L3()) {
			getDialog().getRespLevel().setDisable(disable);
		} else {
			getRespController().disableComboBoxes(disable);
		}
	}

	/**
	 * This method is event listener for siteComboBox
	 */
	private void addSiteComboBoxListener() {
		getDialog().getRespSite().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				clearDisplayMessage();
				getDialog().getRespPlant().getSelectionModel().select(null);
				getDialog().getRespDept().getSelectionModel().select(null);
				getDialog().getRespLevel().getSelectionModel().select(null);

				getDialog().getRespPlant().getItems().clear();
				getDialog().getRespDept().getItems().clear();
				getDialog().getRespLevel().getItems().clear();

				if (!assignedStationResponsibilities.isEmpty()) {
					getDialog().getRespPlant().getItems().addAll(getModel().getDefectEntryCacheUtil()
							.getPlantListFromResponsibilities(assignedStationResponsibilities, new_val));
				} else {
					getDialog().getRespPlant().getItems().addAll(getModel().findAllPlantBySite(new_val));
				}
				Collections.sort(getDialog().getRespPlant().getItems());
				if (getDialog().getRespPlant().getItems().size() == 1) {
					getDialog().getRespPlant().getSelectionModel().select(0);
				}
			}
		});
	}

	/**
	 * This method is event listener for plantComboBox
	 */
	private void addPlantComboBoxListener() {
		getDialog().getRespPlant().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				clearDisplayMessage();
				String site = (String) getDialog().getRespSite().getSelectionModel().getSelectedItem();
				getDialog().getRespDept().getSelectionModel().select(null);
				getDialog().getRespLevel().getSelectionModel().select(null);
				getDialog().getRespDept().getItems().clear();
				getDialog().getRespLevel().getItems().clear();
				if (!assignedStationResponsibilities.isEmpty()) {
					getDialog().getRespDept().getItems().addAll(getModel().getDefectEntryCacheUtil()
							.getDeptListFromResponsibilities(assignedStationResponsibilities, site, new_val));
				} else {
					getDialog().getRespDept().getItems()
							.addAll(getModel().findAllDepartmentBySiteAndPlant(site, new_val));
				}
				Collections.sort(getDialog().getRespDept().getItems());
				if (getDialog().getRespDept().getItems().size() == 1) {
					getDialog().getRespDept().getSelectionModel().select(0);
				}
			}
		});
	}

	/**
	 * This method is event listener for DeptComboBox
	 */
	private void addDeptComboBoxListener() {
		getDialog().getRespDept().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				clearDisplayMessage();
				getDialog().getRespLevel().getSelectionModel().select(null);
				getDialog().getRespLevel().getItems().clear();

				if (new_val == null)
					return;
				if (respController.isShowL2L3()) {
					getRespController().loadRespComboBoxesWithAllValues(new_val);
					return;
				}

				String site = (String) getDialog().getRespSite().getSelectionModel().getSelectedItem();
				String plant = (String) getDialog().getRespPlant().getSelectionModel().getSelectedItem();

				List<String> lvlNames = null;
				List<QiResponsibleLevel> responsibleLevel1List = null;
				List<KeyValue<String, Integer>> listOfL1 = null;
				if (assignedStationResponsibilities != null && !assignedStationResponsibilities.isEmpty()) {
					lvlNames = getModel().getDefectEntryCacheUtil()
							.getLevel1ListFromResponsibilities(assignedStationResponsibilities, site, plant, new_val);
					listOfL1 = ResponsibleLevelController.getUniqueListOfNames(lvlNames);
				} else {
					responsibleLevel1List = getModel().findAllBySitePlantDepartmentLevel(site, plant, new_val,
							(short) 1);
					listOfL1 = ResponsibleLevelController.getUniqueListOfResponsibleLevelNames(responsibleLevel1List);
				}
				if (listOfL1 != null && !listOfL1.isEmpty()) {
					Collections.sort(listOfL1, ResponsibleLevelController.getKVComparator());
					getDialog().getRespLevel().getItems().addAll(listOfL1);
					if (getDialog().getRespLevel().getItems().size() == 1) {
						getDialog().getRespLevel().getSelectionModel().select(0);
					}
				}

			}
		});
	}

	/**
	 * This method is used to void Defect
	 */
	private void voidDefect(ActionEvent actionEvent, QiDefectResult qiDefect) {
		clearDisplayMessage();
		boolean isVoidOk = true;
		try {
			if (getModel().getProperty().isUpcStation()) {
				isVoidOk = MessageDialog.confirm(getDialog(),
						"Defects for all " + quantity + " UPC's will be voided. Do you wish to continue?");
				if (isVoidOk) {
					String partDefectDesc = qiDefect.getPartDefectDesc();
					List<QiDefectResult> defectResults = getModel().getCachedDefectResultList();
					List<QiDefectResult> deleteList = new ArrayList<QiDefectResult>();
					for (QiDefectResult cachedDefectResult : defectResults) {
						if (cachedDefectResult.getPartDefectDesc().equalsIgnoreCase(partDefectDesc)) {
							deleteList.add(cachedDefectResult);
						}
					}
					if (!deleteList.isEmpty()) {
						getModel().getCachedDefectResultList().removeAll(deleteList);
						selectedList.clear();
						disableComponents(true);
					}

					getDialog().reload();
				} else {
					return;
				}
			} else {
				if (isVoidOk) {
					getModel().getCachedDefectResultList().remove(qiDefect);
					selectedList.clear();
					disableComponents(true);
					getDialog().reload();
				} else {
					return;
				}
			}

			if (getModel().getCachedDefectResultList().size() == 0) {
				getDialog().getVoidAllButton().setDisable(true);
				getDialog().getVoidLastButton().setDisable(true);
			}

		} catch (Exception e) {
			handleException("An error occurred in createDefect method ", "Failed to open Create Defect popup ", e);
		}
	}

	/**
	 * This method is used to Change Responsible Site/Plant/Department/Level1
	 * 
	 * @param qiDefect
	 */
	private void changeResponsible(QiDefectResult qiDefect, boolean isAccessible) {
		clearDisplayMessage();
		try {

			if (isAccessible)
				disableResponsibilityPane(false);

			getDialog().getRespSite().getItems().clear();
			getDialog().getRespPlant().getItems().clear();
			getDialog().getRespDept().getItems().clear();
			getDialog().getRespLevel().getItems().clear();

			List<String> lvlNames = null;
			List<QiResponsibleLevel> responsibleLevel1List = null;
			List<KeyValue<String, Integer>> listOfL1 = null;
			if (!assignedStationResponsibilities.isEmpty()) {
				getDialog().getRespSite().getItems().addAll(QiCommonUtil.getUniqueArrayList(Lists
						.transform(assignedStationResponsibilities, new Function<QiStationResponsibilityDto, String>() {
							@Override
							public String apply(final QiStationResponsibilityDto entity) {
								return entity.getSite();
							}
						})));
				getDialog().getRespPlant().getItems()
						.addAll(getModel().getDefectEntryCacheUtil().getPlantListFromResponsibilities(
								assignedStationResponsibilities, qiDefect.getResponsibleSite()));
				getDialog().getRespDept().getItems()
						.addAll(getModel().getDefectEntryCacheUtil().getDeptListFromResponsibilities(
								assignedStationResponsibilities, qiDefect.getResponsibleSite(),
								qiDefect.getResponsiblePlant()));
				lvlNames = getModel().getDefectEntryCacheUtil().getLevel1ListFromResponsibilities(
						assignedStationResponsibilities, qiDefect.getResponsibleSite(), qiDefect.getResponsiblePlant(),
						qiDefect.getResponsibleDept());
				listOfL1 = ResponsibleLevelController.getUniqueListOfNames(lvlNames);
			} else {
				getDialog().getRespSite().getItems().addAll(getModel().findAllSite());
				getDialog().getRespPlant().getItems()
						.addAll(getModel().findAllPlantBySite(qiDefect.getResponsibleSite()));
				getDialog().getRespDept().getItems().addAll(getModel().findAllDepartmentBySiteAndPlant(
						qiDefect.getResponsibleSite(), qiDefect.getResponsiblePlant()));
				responsibleLevel1List = getModel().findAllBySitePlantDepartmentLevel(qiDefect.getResponsibleSite(),
						qiDefect.getResponsiblePlant(), qiDefect.getResponsibleDept(), (short) 1);
				listOfL1 = ResponsibleLevelController.getUniqueListOfResponsibleLevelNames(responsibleLevel1List);
			}

			if (listOfL1 != null && !listOfL1.isEmpty()) {
				Collections.sort(listOfL1, ResponsibleLevelController.getKVComparator());
				getDialog().getRespLevel().getItems().addAll(listOfL1);
			}
			getDialog().getRespSite().getSelectionModel().select(qiDefect.getResponsibleSite());
			getDialog().getRespPlant().getSelectionModel().select(qiDefect.getResponsiblePlant());
			getDialog().getRespDept().getSelectionModel().select(qiDefect.getResponsibleDept());
			KeyValue<String, Integer> kv1 = ResponsibleLevelController.getKeyValue(qiDefect.getResponsibleLevel1());
			getDialog().getRespLevel().getSelectionModel().select(kv1);
			if (getRespController().isShowL2L3()) {
				KeyValue<String, Integer> kv2 = null, kv3 = null;
				if (!StringUtils.isEmpty(qiDefect.getResponsibleLevel2())) {
					kv2 = ResponsibleLevelController.getKeyValue(qiDefect.getResponsibleLevel2());
					getRespController().getResponsiblePanel().getResponsibleLevel2ComboBox().getControl()
							.getSelectionModel().select(kv2);
				}
				if (!StringUtils.isEmpty(qiDefect.getResponsibleLevel3())) {
					kv3 = ResponsibleLevelController.getKeyValue(qiDefect.getResponsibleLevel3());
					getRespController().getResponsiblePanel().getResponsibleLevel3ComboBox().getControl()
							.getSelectionModel().select(kv3);
				}
				QiResponsibleLevel l1 = getRespController().findResponsibleLevel1ByLevelNames(
						qiDefect.getResponsibleSite(), qiDefect.getResponsiblePlant(), qiDefect.getResponsibleDept(),
						kv1, kv2, kv3);
				getRespController().setOriginalResponsibleLevel(l1);
			}

		} catch (Exception e) {
			handleException("An error occurred in changeResponsible method ", "Failed to update Responsibility ", e);
		}
	}

	/**
	 * This method is used to update responsibility
	 * 
	 * @param qiDefectResult
	 */
	private void updateResponsibility(ActionEvent event, QiDefectResult qiDefectResult) {

		boolean isUpdateResponsibilityOk = true;
		String respSite = StringUtils
				.trimToEmpty((String) getDialog().getRespSite().getSelectionModel().getSelectedItem());
		String respPlant = StringUtils
				.trimToEmpty((String) getDialog().getRespPlant().getSelectionModel().getSelectedItem());
		String respDept = StringUtils
				.trimToEmpty((String) getDialog().getRespDept().getSelectionModel().getSelectedItem());
		String respLevel1 = "", respLevel2 = "", respLevel3 = "";
		ComboBox<KeyValue<String, Integer>> l1ComboBox = getDialog().getRespLevel();
		if (l1ComboBox.getSelectionModel().getSelectedItem() != null) {
			respLevel1 = StringUtils.trimToEmpty(l1ComboBox.getSelectionModel().getSelectedItem().toString());
		}

		if (getRespController().isShowL2L3()) {
			ComboBox<KeyValue<String, Integer>> l2ComboBox = getRespController().getResponsiblePanel()
					.getResponsibleLevel2ComboBox().getControl();
			if (l2ComboBox.getSelectionModel().getSelectedItem() != null) {
				respLevel2 = StringUtils.trimToEmpty(l2ComboBox.getSelectionModel().getSelectedItem().toString());
			}

			ComboBox<KeyValue<String, Integer>> l3ComboBox = getRespController().getResponsiblePanel()
					.getResponsibleLevel3ComboBox().getControl();
			if (l3ComboBox.getSelectionModel().getSelectedItem() != null) {
				respLevel3 = StringUtils.trimToEmpty(l3ComboBox.getSelectionModel().getSelectedItem().toString());
			}
		} else {
			List<QiDefectResultDto> levelResultList = getModel().findLevel2andLevel3ByLevel1(respSite, respPlant,
					respDept, respLevel1);
			if (levelResultList != null && !levelResultList.isEmpty() && levelResultList.get(0) != null) {
				respLevel2 = levelResultList.get(0).getLevelTwo();
				respLevel3 = levelResultList.get(0).getLevelThree();
			}
		}

		// if L2/L3 have been set to null or spaces, make them blank string
		if (StringUtils.isBlank(respLevel2)) {
			respLevel2 = "";
		}
		if (StringUtils.isBlank(respLevel3)) {
			respLevel3 = "";
		}

		if (StringUtils.isBlank(respSite)) {
			displayErrorMessage("Mandatory field is empty", "Please enter Responsible Site");
			return;
		} else if (StringUtils.isBlank(respPlant)) {
			displayErrorMessage("Mandatory field is empty", "Please enter Responsible Plant");
			return;
		} else if (StringUtils.isBlank(respDept)) {
			displayErrorMessage("Mandatory field is empty", "Please enter Responsible Dept");
			return;
		} else if (StringUtils.isBlank(respLevel1)) {
			displayErrorMessage("Mandatory field is empty", "Please enter Responsible Level 1");
			return;
		} else if (respController.isShowL2L3()) {
			if (!respController.isValidateResponsibleLevel()) {
				displayErrorMessage("Mandatory field is empty",
						DefectEntryController.SELECT_RESPONSIBLE_LEVEL2_ERROR_MSG);
				return;
			}
		}
		this.clearDisplayMessage();

		QiStationConfiguration qiStationConfiguration = getModel().findPropertyKeyValueByProcessPoint(
				QiEntryStationConfigurationSettings.ADD_COMMENT_FOR_CHANGING_RESPONSIBILITY.getSettingsName());

		String comment = StringUtils.EMPTY;
		// Since L2/L3 wire set to empty strings when null or spaces, it is safe to just
		// do an equals check
		boolean isL2Same = respLevel2.equalsIgnoreCase(qiDefectResult.getResponsibleLevel2());
		boolean isL3Same = respLevel3.equalsIgnoreCase(qiDefectResult.getResponsibleLevel3());
		if (qiStationConfiguration != null && !(qiDefectResult.getResponsibleLevel1().equals(respLevel1) && isL2Same
				&& isL3Same && qiDefectResult.getResponsibleSite().equals(respSite)
				&& qiDefectResult.getResponsiblePlant().equals(respPlant)
				&& qiDefectResult.getResponsibleDept().equals(respDept))) {
			if (StringUtils.trimToEmpty(qiStationConfiguration.getPropertyValue()).equalsIgnoreCase(QiConstant.YES)) {
				comment = getReasonForChangingResponsibility();
				if (!StringUtils.isNotBlank(comment)) {
					return;
				}
			}
		}

		if (isFrameQicsEngineSource()) {
			List<QiResponsibilityMapping> listRespLevel = ServiceFactory.getDao(QiResponsibilityMappingDao.class)
					.findAll();
			QiResponsibleLevel respLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class)
					.findBySitePlantDepartmentAndLevelName(respSite, respPlant, respDept, respLevel1);
			Product product = (Product) getModel().getProduct();
			Frame vehicle = (Frame) product;
			if (!(vehicle.getEngineSerialNo() == null || vehicle.getEngineSerialNo().isEmpty())) {
				Engine engine = ServiceFactory.getDao(EngineDao.class).findByKey(vehicle.getEngineSerialNo());
				if (engine != null) {
					for (QiResponsibilityMapping each : listRespLevel) {
						if (respLevel.getResponsibleLevelId().equals(each.getDefaultRespLevel())
								&& each.getPCode().equals(engine.getPlantCode())) {
							QiResponsibleLevel altRespLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class)
									.findByResponsibleLevelId(each.getAlternateDefault());
							if (altRespLevel == null)
								break;
							respLevel1 = altRespLevel.getResponsibleLevelName();
							break;
						}
					}
				}
			}
		}

		if (getModel().getProperty().isUpcStation()) {

			isUpdateResponsibilityOk = MessageDialog.confirm(getDialog(), "Responsibility/Defect Status for all "
					+ quantity + " UPCs will be changed. Do you wish to continue?");
			if (isUpdateResponsibilityOk) {

				changeResponsibilityForAllUpc(comment);

			} else {
				return;
			}
		} else {

			if (StringUtils.isNotBlank(comment))
				qiDefectResult.setComment(comment);
			getModel().getCachedDefectResultList().remove(qiDefectResult);
			qiDefectResult.setResponsibleSite(respSite);
			qiDefectResult.setResponsiblePlant(respPlant);
			qiDefectResult.setResponsibleDept(respDept);
			qiDefectResult.setResponsibleLevel1(respLevel1);
			qiDefectResult.setResponsibleLevel2(respLevel2);
			qiDefectResult.setResponsibleLevel3(respLevel3);
			getModel().getCachedDefectResultList().add(qiDefectResult);
			disableResponsibilityPane(true);
			updateDefectStatus(qiDefectResult);
			getDialog().getApplyBtn().setDisable(true);
			getDialog().getVoidButton().setDisable(true);
			selectedList.clear();
			getDialog().reload();
		}

	}

	private void changeResponsibilityForAllUpc(String comment) {
		List<QiDefectResult> defectResultList = new ArrayList<QiDefectResult>();
		List<QiDefectResult> cachedDefectResultList = getModel().getCachedDefectResultList();
		String respSite = StringUtils
				.trimToEmpty((String) getDialog().getRespSite().getSelectionModel().getSelectedItem());
		String respPlant = StringUtils
				.trimToEmpty((String) getDialog().getRespPlant().getSelectionModel().getSelectedItem());
		String respDept = StringUtils
				.trimToEmpty((String) getDialog().getRespDept().getSelectionModel().getSelectedItem());
		String respLevel1 = StringUtils
				.trimToEmpty((String) getDialog().getRespLevel().getSelectionModel().getSelectedItem().toString());
		String partDefectDesc = getDialog().getDefectResultTablePane().getSelectedItem().getQiDefectResult()
				.getPartDefectDesc();
		for (QiDefectResult qiDefectResult : cachedDefectResultList) {
			if (qiDefectResult.getPartDefectDesc().equalsIgnoreCase(partDefectDesc)) {
				if (StringUtils.isNotBlank(comment))
					qiDefectResult.setComment(comment);
				qiDefectResult.setResponsibleSite(respSite);
				qiDefectResult.setResponsiblePlant(respPlant);
				qiDefectResult.setResponsibleDept(respDept);
				qiDefectResult.setResponsibleLevel1(respLevel1);

			}
			defectResultList.add(qiDefectResult);
		}

		updateDefectStatusForAllUPC();

		getModel().getCachedDefectResultList().clear();
		getModel().getCachedDefectResultList().addAll(defectResultList);
		disableResponsibilityPane(true);
		getDialog().getStatusPane().setDisable(true);
		getDialog().getApplyBtn().setDisable(true);

		getDialog().reload();

	}

	/**
	 * When user clicks on Void All button cache list gets cleared
	 */
	private void voidAllBtnAction(ActionEvent event) {
		getModel().getCachedDefectResultList().clear();
		selectedList.clear();
		disableComponents(true);
		getDialog().getVoidAllButton().setDisable(true);
		getDialog().getVoidLastButton().setDisable(true);
		getDialog().reload();
	}

	/**
	 * When user clicks on close button in the popup screen closeBtnAction method
	 * gets called.
	 */
	private void closeBtnAction(ActionEvent event) {
		LoggedButton closeBtn = getDialog().getDoneBtn();
		try {
			Stage stage = (Stage) closeBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}

	/**
	 * When user clicks on Void Last button, Last entry from cache list gets cleared
	 */
	private void voidLastButtonAction(ActionEvent event) {
		List<QiDefectResult> cacheList = getModel().getCachedDefectResultList();
		if (getModel().getProperty().isUpcStation()) {
			if (null != cacheList && !cacheList.isEmpty()) {
				for (int i = 0; i < Integer.parseInt(quantity); i++) {
					cacheList.remove(cacheList.size() - 1);
				}
			}

		} else {
			if (cacheList.size() > 0) {
				cacheList.remove(cacheList.size() - 1);
			}
		}

		selectedList.clear();
		disableComponents(true);
		getDialog().reload();
		if (cacheList.size() == 0) {
			getDialog().getVoidAllButton().setDisable(true);
			getDialog().getVoidLastButton().setDisable(true);
		}
	}

	/**
	 * This method is used to Change Responsible Site/Plant/Department/Level1
	 * 
	 * @param qiDefect
	 */
	private void changeDefectStatus(QiDefectResult qiDefect) {
		clearDisplayMessage();
		try {
			getDialog().getStatusPane().setDisable(false);
			QiStationConfiguration qiEntryStationConfigManagement = getModel()
					.findPropertyKeyValueByProcessPoint(QiConstant.ENTRY_STATION_AVAILABLE_DEFECT_STATUS);
			String availableDefectStatus = StringUtils.EMPTY;
			if (qiEntryStationConfigManagement != null) {
				availableDefectStatus = qiEntryStationConfigManagement.getPropertyValue();
				String[] availableDefectStatusList = availableDefectStatus.split(",");

				List<String> defectStatusList = getDialog().getDefectEntryStatusList();

				if (defectStatusList != null && defectStatusList.size() > 0) {
					availableDefectStatusList = String.join(",", defectStatusList).split(",");
				}

				for (String defectStatus : availableDefectStatusList) {
					if (defectStatus.equalsIgnoreCase(QiEntryStationDefaultStatus.REPAIRED.getName())) {
						getDialog().getRepairedRadioBtn().setVisible(true);
						if (DefectStatus.getType(qiDefect.getOriginalDefectStatus()).equals(DefectStatus.REPAIRED))
							getDialog().getRepairedRadioBtn().setSelected(true);

					} else if (defectStatus.equalsIgnoreCase(QiEntryStationDefaultStatus.NOT_REPAIRED.getName())) {
						getDialog().getNotRepairedRadioBtn().setVisible(true);
						if (DefectStatus.getType(qiDefect.getOriginalDefectStatus()).equals(DefectStatus.NOT_REPAIRED))
							getDialog().getNotRepairedRadioBtn().setSelected(true);

					} else if (defectStatus
							.equalsIgnoreCase(QiEntryStationDefaultStatus.NON_REPAIRABLE_SCRAP.getName())) {
						getDialog().getNonRepairableRadioBtn().setVisible(true);
						if (DefectStatus.getType(qiDefect.getOriginalDefectStatus())
								.equals(DefectStatus.NON_REPAIRABLE))
							getDialog().getNonRepairableRadioBtn().setSelected(true);
					}
				}
			}

		} catch (Exception e) {
			handleException("An error occurred in changeDefectStatus method ", "Failed to update Defect Status ", e);
		}
	}

	/**
	 * This method is used to update defect status
	 * 
	 * @param event
	 * @param qiDefectResult
	 */
	private void updateDefectStatus(QiDefectResult qiDefectResult) {
		getModel().getCachedDefectResultList().remove(qiDefectResult);
		if (getDialog().getRepairedRadioBtn().isSelected())
			qiDefectResult.setDefectStatus(DefectStatus.REPAIRED.getName());
		else if (getDialog().getNotRepairedRadioBtn().isSelected())
			qiDefectResult.setDefectStatus(DefectStatus.NOT_REPAIRED.getName());
		else if (getDialog().getNonRepairableRadioBtn().isSelected())
			qiDefectResult.setDefectStatus(DefectStatus.NON_REPAIRABLE.getName());
		
		if(!QiInspectionUtils.isGlobalGdpEnabled() && QiInspectionUtils.isGdpProcessPoint(getModel().getProcessPointId())) {
			qiDefectResult.setGdpDefect(qiDefectResult.getCurrentDefectStatus() == (short) DefectStatus.NOT_FIXED.getId() ? (short) 1 : (short) 0);
		}

		qiDefectResult.setTrpuDefect(QiInspectionUtils.isTrpuProcessPoint(getModel().getProcessPointId()) ? (short) 1 : (short) 0);
		getModel().getCachedDefectResultList().add(qiDefectResult);
		getDialog().getStatusPane().setDisable(true);
	}

	/**
	 * 
	 * When the user selects a single defect or multiple defect and opts for No
	 * Problem Found button The same feature as No Problem Found button in the
	 * Repair Entry Screen.
	 * 
	 */
	private void updateNoProblemFoundForDefects(List<QiDefectResult> qiDefectResultList, boolean isFixed) {
		// Update the defect as FIXED
		// and its repair result as NO PROBLEM FOUND
		changeCurrentDefectStatus(qiDefectResultList, true, true);
	}

	/**
	 * When user click on change to fixed button, current defect status of defect
	 * result and repair result table gets changed to fixed
	 */
	private void changeCurrentDefectStatus(List<QiDefectResult> qiDefectResultList, boolean isFixed,
			boolean isNoProblemFound) {
		boolean isNPFChangedToNotFixed=false;
		try {
			if (productModel.isBulkProcess() && productModel.getProcessedProducts().size() > 1) {
				Set<QiCommonDefectResult> selectedCommonDefects = new HashSet<QiCommonDefectResult>();
				for (QiDefectResult qiDefectResult : qiDefectResultList) { // get all the common defects that are
																			// currently selected
					selectedCommonDefects.add(new QiCommonDefectResult(qiDefectResult));
				}
				qiDefectResultList = new ArrayList<QiDefectResult>();
				List<QiRepairResultDto> commonDefects = ProductSearchResult.getDefectsProcessing(); // get common
																									// defects
				for (QiRepairResultDto commonDefect : commonDefects) { // get the selected common defects for all
																		// processed products
					QiDefectResult qiDefectResult = getModel().findDefectResultById(commonDefect.getDefectResultId());
					if (selectedCommonDefects.contains(new QiCommonDefectResult(qiDefectResult))) {
						qiDefectResultList.add(qiDefectResult);
					}
				}
			}

			// check current tracking status before saving defect/repair results
			if (getModel().isPreviousLineInvalid()) {
				publishProductPreviousLineInvalidEvent();
				return;
			}

			@Table(name = "QI_DEFECT_RESULT_TBX")
			class DefectStatusAuditEntry {
				DefectStatusAuditEntry(String status, long id) {
					originalDefectStatus = status;
					defectResultId = id;
				}

				@Id
				Long defectResultId = 0L;
				@Auditable
				@Column(name = "ORIGINAL_DEFECT_STATUS")
				private String originalDefectStatus = "";
			}

			List<QiRepairResult> finalRepairList = new ArrayList<QiRepairResult>();
			List<QiDefectResultHist> defectResultHistList = new ArrayList<QiDefectResultHist>();

			for (QiDefectResult defectResult : qiDefectResultList) {
				if (defectResult.getActualTimestamp() == null)
					continue;

				// @NAQICS-627 if change from fixed to not-fixed, create a defect result history
				// record
				if (!isFixed) {
					QiDefectResultHist hist = new QiDefectResultHist(defectResult);
					hist.setChangeUser(getUserId());
					defectResultHistList.add(hist);
				}

				defectResult.setCurrentDefectStatus(
						isFixed ? (short) DefectStatus.FIXED.getId() : (short) DefectStatus.NOT_FIXED.getId());
				int originalStatus = defectResult.getOriginalDefectStatus();
				if (originalStatus == DefectStatus.REPAIRED.getId() && !isFixed) {
					defectResult.setOriginalDefectStatus((short) (DefectStatus.NOT_REPAIRED.getId()));
					// if original status was REPAIRED and user changed it to NOT_REPAIRED --> Audit
					// Log
					DefectStatusAuditEntry audit1 = new DefectStatusAuditEntry(DefectStatus.REPAIRED.getName(),
							defectResult.getDefectResultId());
					DefectStatusAuditEntry audit2 = new DefectStatusAuditEntry(DefectStatus.NOT_REPAIRED.getName(),
							defectResult.getDefectResultId());
					StringBuilder sb = new StringBuilder();
					sb.append(defectResult.getProductId()).append(": ")
							.append("User changed Repaired/Fixed to Not_Fixed");
					AuditLoggerUtil.logAuditInfo(audit1, audit2, sb.toString(), "ExistingDefects",
							String.valueOf(defectResult.getDefectResultId()), getUserId());
				}

				if (QiInspectionUtils.isGdpProcessPoint(getModel().getProcessPointId())) {
					// currently, if original status was not_repaired, GDP is not set even if it is
					// a GDP station
					// this will set the GDP flag regardless of original defect status
					if (!isVqGdpProcessPoint() || (isVqGdpProcessPoint() && !isOverHourSinceOriginalScan())) {
						if (isFixed) {
							QiDefectResultHist hist = ServiceFactory.getDao(QiDefectResultHistDao.class)
									.findFirstDefectResultHistory(defectResult.getDefectResultId());

							if (hist != null
									&& getModel().getCurrentWorkingEntryDept().equalsIgnoreCase(hist.getEntryDept())) {
								defectResult.setGdpDefect(hist.getGdpDefect()); // If a defect was flipped to not fixed
																				// in the same dept and fixed again,
																				// restore its previous GDP flag
							}
						} else {
							defectResult.setGdpDefect((short) 1);
						}
					}
				}
				
				defectResult.setUpdateUser(getModel().getUserId());

				List<QiRepairResult> repairResultList = getModel()
						.findAllRepairResultByDefectResultId(defectResult.getDefectResultId());
				
				if(!isFixed && defectResult.getReportable()==QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId())
					isNPFChangedToNotFixed=true;
					

				// Updating history for defect result history when marking from
				// Not Fixed to No Problem Found
				if (isNoProblemFound) {

					QiDefectResultHist defectHist = new QiDefectResultHist(defectResult);
					defectHist.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
					defectHist.setComment(NO_PROBLEM_FOUND);
					defectHist.setChangeUser(getUserId());
					ServiceFactory.getDao(QiDefectResultHistDao.class).insert(defectHist);
					// Marking NPF in main defect
					defectResult.setReportable((short) QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId());

					if (repairResultList.isEmpty()) {
						QiRepairResult repairResult = new QiRepairResult(defectResult);
						repairResult.setEntrySiteName(getModel().getCurrentWorkingProcessPoint().getSiteName());
						repairResult.setEntryPlantName(getModel().getCurrentWorkingProcessPoint().getPlantName());
						repairResult.setEntryProdLineNo(getModel().getEntryProdLineNo());
						repairResult.setEntryDept(getModel().getCurrentWorkingEntryDept());
						repairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
						repairResult.setTerminalName(getModel().getTerminalName());
						repairResult.setActualProblemSeq((short) 1);
						repairResult.setCreateUser(getModel().getUserId());

						repairResult.setDefectTypeName(NO_PROBLEM_FOUND);
						repairResult.setDefectTypeName2("");
						repairResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
						repairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
						repairResult.setReportable((short) QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId());
						repairResult.setProductionDate(getModel().getProductionDate());

						finalRepairList.add(repairResult);
					} else {
						for (QiRepairResult repairResult : repairResultList) {
							if(repairResult.getCurrentDefectStatus()!= DefectStatus.FIXED.getId()) {
								repairResult.setDefectTypeName(NO_PROBLEM_FOUND);
								repairResult.setDefectTypeName2("");
								repairResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
								repairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
								repairResult.setReportable((short) QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId());
								repairResult.setProductionDate(getModel().getProductionDate());
								repairResult.setUpdateUser(getModel().getUserId());
								repairResult.setTerminalName(getModel().getTerminalName());
							}

							finalRepairList.addAll(repairResultList);
						}
					}
				} else {

					if (repairResultList.isEmpty()) {
						QiRepairResult repairResult = new QiRepairResult(defectResult);
						repairResult.setEntrySiteName(getModel().getCurrentWorkingProcessPoint().getSiteName());
						repairResult.setEntryPlantName(getModel().getCurrentWorkingProcessPoint().getPlantName());
						repairResult.setEntryProdLineNo(getModel().getEntryProdLineNo());
						repairResult.setEntryDept(getModel().getCurrentWorkingEntryDept());
						repairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
						repairResult.setTerminalName(getModel().getTerminalName());
						repairResult.setActualProblemSeq((short) 1);
						repairResult.setCreateUser(getModel().getUserId());
						repairResult.setProductionDate(getModel().getProductionDate());

						finalRepairList.add(repairResult);
					} else {
							for (QiRepairResult repairResult : repairResultList) {
								repairResult.setCurrentDefectStatus(isFixed ? (short) DefectStatus.FIXED.getId()
										: (short) DefectStatus.NOT_FIXED.getId());
								repairResult.setUpdateUser(getModel().getUserId());
								repairResult.setTerminalName(getModel().getTerminalName());
							}
						
						finalRepairList.addAll(repairResultList);
					}
					if(isNPFChangedToNotFixed) {
						//updating reportable to the defect which is marked as Not Fixed after marking 
						//as NBF
						defectResult.setReportable((short)QiReportable.REPORTABLE.getId());
						//deleting actual problem and the applied repair method
						for (QiRepairResult repairResult : repairResultList) {
							getDao(QiAppliedRepairMethodDao.class)
								.deleteAppliedRepairMethodByRepairIdNPF(repairResult.getRepairId(),NO_PROBLEM_FOUND);
							getDao(QiRepairResultDao.class)
								.deleteRepairResultByDefectResultIdNPF(defectResult.getDefectResultId(),NO_PROBLEM_FOUND);
						}
					}
				}
				finalResultList.add(defectResult);
			}
			if (!getDialog().isTrainingModeOn()) {
				if(isNPFChangedToNotFixed) {
					getModel().updateAllDefectResults(finalResultList);
					// Update main defect in cache
					ProductSearchResult.updateDefectsProcessingMap(finalResultList);

					if (!isFixed) {
						for (QiDefectResultHist hist : defectResultHistList) {
							getDao(QiDefectResultHistDao.class).insert(hist);
						}
					}
				}else {
				List<QiRepairResult> savedRepairResultList = getModel().saveAllRepairResults(finalRepairList);
				savedRepairResultList = savedRepairResultList.stream().distinct().collect(Collectors.toList());
				List<QiAppliedRepairMethod> savedAppliedRepairMethodList = createRepairMethod(savedRepairResultList,isNoProblemFound);
				savedAppliedRepairMethodList =savedAppliedRepairMethodList.stream().distinct().collect(Collectors.toList());
				
				if (!isFixed) {
					// update QI_APPLIED_REPAIR_METHOD_TBX to set IS_COMPLETELY_FIXED=0
					updateNotCompletelyFixed(savedRepairResultList);
				}

				// @KM : Check if the product is already shipped by time of finishing product
				if (isProductShipped(getModel().getProductId())) {
					EventBusUtil.publishAndWait(
							new StatusMessageEvent(PRODUCT_SHIPPED_MSG, StatusMessageEventType.DIALOG_ERROR));
					EventBusUtil.publishAndWait(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_SHIPPED));
					return;
				}
				getModel().updateAllDefectResults(finalResultList);
				// Update main defect in cache
				ProductSearchResult.updateDefectsProcessingMap(finalResultList);

				if (!isFixed) {
					for (QiDefectResultHist hist : defectResultHistList) {
						getDao(QiDefectResultHistDao.class).insert(hist);
					}
				}

				// replicate updated defect result to GAL125TBX and update repair result to
				// GAL222TBX
				if (PropertyService.getPropertyBean(QiPropertyBean.class, getModel().getCurrentWorkingProcessPointId())
						.isReplicateDefectRepairResult()) {
					replicateCurrentDefectStatus(qiDefectResultList, savedRepairResultList,
							savedAppliedRepairMethodList, isFixed);
				}

				if (isFixed) {
					QiDefectResult lastDefectResult = finalResultList.get(finalResultList.size() - 1);
					String message = getModel().addUnitToConfiguredRepairArea(lastDefectResult.getDefectResultId());
					if (!StringUtils.isEmpty(message)) {
						EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.DIALOG_INFO));
					}
				}
			}
			}
			selectedList.clear();
			disableComponents(true);
			getDialog().reload();
		} catch (Exception e) {
			handleException("An error occurred in changeCurrentDefectStatus method ",
					"Failed to update current Defect Status ", e);
		}
	}

	/**
	 * checker to check if the product has been shipped or not.
	 * 
	 * @param productId
	 * @return boolean
	 */
	private boolean isProductShipped(String productId) {
		ShippingStatus shippingStatus = ServiceFactory.getDao(ShippingStatusDao.class).findByKey(productId);
		if (shippingStatus == null || ShippingStatusEnum.S90A
				.equals(ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus())))
			return false;
		else
			return true;
	}

	/**
	 * Replicate updated defect result to GAL125TBX and update repair result to
	 * GAL222TBX
	 */
	private void replicateCurrentDefectStatus(List<QiDefectResult> qiDefectResultList,
			List<QiRepairResult> qiRepairResultList, List<QiAppliedRepairMethod> qiAppliedRepairMethodList,
			boolean isFixed) {
		if (getDialog().isTrainingModeOn()) {
			return;
		}
		try {
			if (isFixed) {
				// create record in GAL222TBX
				for (QiAppliedRepairMethod qiAppliedRepairMethod : qiAppliedRepairMethodList) {
					QiAppliedRepairMethodDto appliedRepairMethodDto = new QiAppliedRepairMethodDto();
					appliedRepairMethodDto.setComment(qiAppliedRepairMethod.getComment());
					appliedRepairMethodDto.setCreateUser(qiAppliedRepairMethod.getCreateUser());
					appliedRepairMethodDto.setEntryDept(getModel().getCurrentWorkingEntryDept());
					appliedRepairMethodDto.setIsCompletelyFixed(qiAppliedRepairMethod.getIsCompletelyFixed());
					appliedRepairMethodDto.setRepairId(qiAppliedRepairMethod.getId().getRepairId());
					appliedRepairMethodDto.setRepairMethod("");
					appliedRepairMethodDto.setRepairMethodSeq(qiAppliedRepairMethod.getId().getRepairMethodSeq());
					appliedRepairMethodDto.setRepairTime(qiAppliedRepairMethod.getRepairTime());
					appliedRepairMethodDto.setRepairTimestamp(qiAppliedRepairMethod.getRepairTimestamp());

					String partDefectDesc = "";
					for (QiRepairResult repairResult : qiRepairResultList) {
						if (repairResult.getRepairId() == qiAppliedRepairMethod.getId().getRepairId()) {
							partDefectDesc = repairResult.getPartDefectDesc();
							break;
						}
					}
					getModel().replicateRepairResult(appliedRepairMethodDto, partDefectDesc,
							getModel().getCurrentWorkingProcessPointId());
				}
			} else {
				for (QiDefectResult defectResult : qiDefectResultList) {
					if (defectResult.getActualTimestamp() == null) {
						// invalid QiDefectResult
						continue;
					}
					// update defect status to outstanding in GAL125TBX
					getModel().updateOldDefectStatus(defectResult.getDefectResultId(), DefectStatus.OUTSTANDING.getId(),
							null, "");
				}

				for (QiRepairResult repairResult : qiRepairResultList) {
					// delete record from GAL222TBX
					getModel().deleteOldRepairResult(repairResult.getRepairId());
				}
			}
		} catch (Exception e) {
			handleException("An error occurred in replicateCurrentDefectStatus method ",
					"Failed to replicate Current Defect Status ", e);
		}
	}

	/**
	 * This method is used to create repair method
	 * 
	 * @param repairTimestamp
	 * @param savedResult
	 */
	private List<QiAppliedRepairMethod> createRepairMethod(List<QiRepairResult> savedResultList,boolean isNoProblemFound) {
		List<QiAppliedRepairMethod> finalRepairMethodList = new ArrayList<QiAppliedRepairMethod>();
		for (QiRepairResult savedResult : savedResultList) {
			boolean isFixed = savedResult.getCurrentDefectStatus() == DefectStatus.FIXED.getId();
			Integer count = getModel().findMaxSequenceValueByRepairId(savedResult.getRepairId());
			count = count == null ? 0 : count;
			count++;
			QiAppliedRepairMethod qiAppliedRepairMethod = new QiAppliedRepairMethod();
			QiAppliedRepairMethodId id = new QiAppliedRepairMethodId();
			qiAppliedRepairMethod.setId(id);
			qiAppliedRepairMethod.getId().setRepairMethodSeq(count);
			qiAppliedRepairMethod.getId().setRepairId(savedResult.getRepairId());
			qiAppliedRepairMethod.setRepairMethod(isNoProblemFound ? NO_PROBLEM_FOUND : IN_LINE_REPAIR_METHOD);
			qiAppliedRepairMethod.setApplicationId(getModel().getCurrentWorkingProcessPointId());
			qiAppliedRepairMethod.setCreateUser(getModel().getUserId());
			qiAppliedRepairMethod.setRepairTime(1);
			qiAppliedRepairMethod.setComment(isFixed ? CHANGED_TO_FIXED : CHANGED_TO_NOT_FIXED);
			qiAppliedRepairMethod.setIsCompletelyFixed(isFixed ? 1 : 0);
			finalRepairMethodList.add(qiAppliedRepairMethod);
		}
		return getModel().saveAllRepairMethods(finalRepairMethodList);		
	}

	/**
	 * This method is used to enable the No Problem Found button if it is configured
	 * from Station Config Screen
	 */
	private void enableNoProblemFoundButton(List<QiDefectResult> defectResultList) {
		QiStationConfiguration qiEntryStationConfigManagement = getModel()
				.findPropertyKeyValueByProcessPoint(QiConstant.NO_PROBLEM_FOUND);
		if (null != qiEntryStationConfigManagement
				&& qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.YES)
				&& defectResultList.size() > 0) {
			if (defectResultList.size() == 1) {
				QiDefectResult qiDefectResult = defectResultList.get(0);
				if (qiDefectResult.getActualTimestamp() != null) {
					getDialog().getNoProblemFoundButton().setDisable(
							!(qiDefectResult.getCurrentDefectStatus() == (short) DefectStatus.NOT_FIXED.getId()));
				} else {
					getDialog().getNoProblemFoundButton().setDisable(true);
				}
			} else {
				boolean isFixed = false;
				boolean isNotFixed = false;
				for (QiDefectResult defectResult : defectResultList) {
					if (defectResult.getActualTimestamp() != null) {
						if (defectResult.getCurrentDefectStatus() == (short) DefectStatus.FIXED.getId()) {
							isFixed = true;
						}
						if (defectResult.getCurrentDefectStatus() == (short) DefectStatus.NOT_FIXED.getId()) {
							isNotFixed = true;
						}
					} else {
						isFixed = true;
						isNotFixed = true;
					}
				}
				if (isFixed && isNotFixed) {
					getDialog().getNoProblemFoundButton().setDisable(true);
				} else if (isNotFixed) {
					getDialog().getNoProblemFoundButton().setDisable(false);
				} else {
					getDialog().getNoProblemFoundButton().setDisable(true);
				}

			}
		} else {
			getDialog().getNoProblemFoundButton().setDisable(true);
		}
	}

	/**
	 * This method is used to enable Changed to Fixed button if it is configured
	 * from Station Config Screen
	 */
	private void enableChangeToFixedButton(List<QiDefectResult> defectResultList) {
		QiStationConfiguration qiEntryStationConfigManagement = getModel()
				.findPropertyKeyValueByProcessPoint(QiConstant.CHANGE_TO_FIXED);
		if (null != qiEntryStationConfigManagement
				&& qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.YES)
				&& defectResultList.size() > 0) {
			if (defectResultList.size() == 1) {
				QiDefectResult qiDefectResult = defectResultList.get(0);
				if (qiDefectResult.getActualTimestamp() != null) {
					getDialog().getChangeToFixedButton()
							.setDisable(qiDefectResult.getCurrentDefectStatus() == (short) DefectStatus.FIXED.getId());
					getDialog().getChangeToNotFixedButton().setDisable(
							qiDefectResult.getCurrentDefectStatus() == (short) DefectStatus.NOT_FIXED.getId());

				} else {
					getDialog().getChangeToFixedButton().setDisable(true);
					getDialog().getChangeToNotFixedButton().setDisable(true);
				}
			} else {
				boolean isFixed = false;
				boolean isNotFixed = false;
				for (QiDefectResult defectResult : defectResultList) {
					if (defectResult.getActualTimestamp() != null) {
						if (defectResult.getCurrentDefectStatus() == (short) DefectStatus.FIXED.getId()) {
							isFixed = true;
						}
						if (defectResult.getCurrentDefectStatus() == (short) DefectStatus.NOT_FIXED.getId()) {
							isNotFixed = true;
						}
					} else {
						isFixed = true;
						isNotFixed = true;
					}
				}
				getDialog().getChangeToFixedButton().setDisable(isFixed);
				getDialog().getChangeToNotFixedButton().setDisable(isNotFixed);
			}
		} else {
			getDialog().getChangeToFixedButton().setDisable(true);
			getDialog().getChangeToNotFixedButton().setDisable(true);
		}
	}

	/**
	 * This method is used to update Defect Status for all UPC
	 */
	private void updateDefectStatusForAllUPC() {
		List<QiDefectResult> defectResultList = new ArrayList<QiDefectResult>();
		String partDefectDesc = getDialog().getDefectResultTablePane().getSelectedItem().getQiDefectResult()
				.getPartDefectDesc();
		for (QiDefectResult qiDefectResult : getModel().getCachedDefectResultList()) {
			if (qiDefectResult.getPartDefectDesc().equalsIgnoreCase(partDefectDesc)) {
				if (getDialog().getRepairedRadioBtn().isSelected())
					qiDefectResult.setDefectStatus(DefectStatus.REPAIRED.getName());
				else if (getDialog().getNotRepairedRadioBtn().isSelected())
					qiDefectResult.setDefectStatus(DefectStatus.NOT_REPAIRED.getName());
				else if (getDialog().getNonRepairableRadioBtn().isSelected())
					qiDefectResult.setDefectStatus(DefectStatus.NON_REPAIRABLE.getName());
			}
			defectResultList.add(qiDefectResult);
		}
	}

	private String getReasonForChangingResponsibility() {
		ReasonForChangeDialog dialog = new ReasonForChangeDialog(getDialog());
		if (dialog.showReasonForChangeDialog(null)) {
			return dialog.getReasonForChangeText();
		}
		return StringUtils.EMPTY;
	}

	public List<QiDefectResult> getFinalResultList() {
		return finalResultList;
	}

	public void setFinalResultList(List<QiDefectResult> finalResultList) {
		this.finalResultList = finalResultList;
	}

	private boolean isAllowOverwriteResponsibility() {
		boolean isAllowOverwriteResponsibility = false;
		QiStationConfiguration qiEntryStationConfigManagement = getModel().findPropertyKeyValueByProcessPoint(
				QiEntryStationConfigurationSettings.RESPONSIBILITY.getSettingsName());
		if ((qiEntryStationConfigManagement != null
				&& qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.YES))
				|| (qiEntryStationConfigManagement == null && QiEntryStationConfigurationSettings.RESPONSIBILITY
						.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.YES))) {
			isAllowOverwriteResponsibility = true;
		}
		return isAllowOverwriteResponsibility;
	}

	/**
	 * @return the respController
	 */
	public ResponsibleLevelController getRespController() {
		return respController;
	}

	/**
	 * @param respController the respController to set
	 */
	public void setRespController(ResponsibleLevelController respController) {
		this.respController = respController;
	}

	public void updateNotCompletelyFixed(List<QiRepairResult> repairResultList) {
		String repairIdsString = "";
		for (int i = 0; i < repairResultList.size(); i++) {
			if (i == 0) {
				repairIdsString = "(" + repairResultList.get(0).getRepairId();
			} else {
				repairIdsString += "," + repairResultList.get(i).getRepairId();
			}
		}
		repairIdsString += ")";
		getModel().updateNotCompletelyFixed(repairIdsString);
	}

	public boolean isVqGdpProcessPoint() {
		String[] vqGdpProcessPointIdArray = PropertyService.getPropertyBean(QiPropertyBean.class)
				.getVqGdpProcessPointId();
		boolean isProcessPoint = false;
		if (vqGdpProcessPointIdArray.length > 0) {
			String vqGdpProcessPointIds = "'" + vqGdpProcessPointIdArray[0];
			for (int i = 1; i < vqGdpProcessPointIdArray.length; i++) {
				vqGdpProcessPointIds += "', '" + vqGdpProcessPointIdArray[i];
			}
			vqGdpProcessPointIds += "'";
			if (vqGdpProcessPointIds.contains(getModel().getProcessPointId())) {
				isProcessPoint = true;
			} else {
				isProcessPoint = false;
			}
		}
		return isProcessPoint;
	}

	public boolean isOverHourSinceOriginalScan() {
		Date oneHourAfterInitialScan = null;
		boolean hourSince = false;
		Date currentTime = new Date();
		String[] vqGdpProcessPointIdArray = getModel().getProperty().getVqGdpProcessPointId();

		if (vqGdpProcessPointIdArray.length > 0) {
			List<String> vqGdpProcessPointIdList = Arrays.asList(vqGdpProcessPointIdArray);
			if (vqGdpProcessPointIdList.contains(getModel().getProcessPointId()) && QiInspectionUtils.isGdpProcessPoint(getModel().getProcessPointId())) {
				Timestamp initialVqGdpTimestamp = getModel().getInitialVqGdpTimestamp(getModel().getProductId(),
						vqGdpProcessPointIdList);

				if (initialVqGdpTimestamp != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(initialVqGdpTimestamp);
					calendar.add(Calendar.HOUR_OF_DAY, 1);
					oneHourAfterInitialScan = calendar.getTime();
					if (currentTime.after(oneHourAfterInitialScan)) {
						hourSince = true;
					} else {
						hourSince = false;
					}
				}
			}
		}
		return hourSince;
	}

}
