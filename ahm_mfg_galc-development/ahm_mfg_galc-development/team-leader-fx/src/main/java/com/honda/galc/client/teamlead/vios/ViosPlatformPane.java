package com.honda.galc.client.teamlead.vios;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.ServiceFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

public abstract class ViosPlatformPane extends TitledPane {
	
	private IViosPanel panel;
	
	private LabeledComboBox<String> plantCombobox;
	private LabeledComboBox<String> deptCombobox;
	private LabeledComboBox<String> modelYearCombobox;
	private LabeledComboBox<String> prodRateCombobox;
	private LabeledComboBox<String> lineNoCombobox;
	private LabeledComboBox<String> vmcCombobox;

	public ViosPlatformPane(IViosPanel panel) {
		super();
		this.panel = panel;
		createPlatformPane();
		addComboBoxListener();
		loadPlantComboBox();
	}
	
	private void createPlatformPane() {
		HBox platformContainer = new HBox();
		platformContainer.setAlignment(Pos.CENTER);
		platformContainer.setPadding(new Insets(10));
		platformContainer.setSpacing(20);
		
		plantCombobox = createLabeledComboBox("plantCombobox", ViosConstants.PLANT, false, true, false);
		deptCombobox = createLabeledComboBox("deptCombobox", ViosConstants.DEPARTMENT, false, true, false);
		modelYearCombobox = createLabeledComboBox("modelYearCombobox", ViosConstants.MODEL_YEAR, false, true, false);
		prodRateCombobox = createLabeledComboBox("prodRateCombobox", ViosConstants.PRODUCTION_RATE, false, true, false);
		lineNoCombobox = createLabeledComboBox("lineNoCombobox", ViosConstants.LINE_NUMBER, false, true, false);
		vmcCombobox = createLabeledComboBox("vmcCombobox", ViosConstants.VEHICLE_MODEL_CODE, false, true, false);

		platformContainer.getChildren().addAll(plantCombobox, deptCombobox, modelYearCombobox, prodRateCombobox, lineNoCombobox, vmcCombobox);

		this.setText("Select Platform");
		this.setFont(Font.font(StringUtils.EMPTY, FontWeight.BOLD, 12));
		this.setContent(platformContainer);
		this.setCollapsible(false);
		this.setContentDisplay(ContentDisplay.CENTER);
	}
	
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(30);
		comboBox.getControl().setPrefWidth(200);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		comboBox.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.009 * getScreenWidth())));
		comboBox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				panel.clearErrorMessage();
			}
		});
		return comboBox;
	}
	
	private void loadPlantComboBox() {
		plantCombobox.getControl().getItems().clear();
		plantCombobox.getControl().setPromptText(ViosConstants.SELECT);
		plantCombobox.getControl().getItems().addAll(getMCViosMasterPlatformDao().findAllPlants());
	}

	private void addPlantComboBoxListener() {
		plantCombobox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) {
				if(newValue != null) {
					loadDeptComboBox(newValue);
					panel.clearErrorMessage();
				}
			} 
		});
	}

	private void loadDeptComboBox(String plant) {
		deptCombobox.getControl().getItems().clear();
		modelYearCombobox.getControl().getItems().clear();
		modelYearCombobox.getControl().setValue(null);
		prodRateCombobox.getControl().getItems().clear();
		prodRateCombobox.getControl().setValue(null);
		lineNoCombobox.getControl().getItems().clear();
		vmcCombobox.getControl().getItems().clear();
		lineNoCombobox.getControl().setValue(null);
		vmcCombobox.getControl().setValue(null);
		deptCombobox.getControl().setPromptText(ViosConstants.SELECT);
		deptCombobox.getControl().getItems().addAll(getMCViosMasterPlatformDao().findAllDeptBy(plant));
		if(deptCombobox.getControl().getItems().size() == 1) {
			deptCombobox.getControl().getSelectionModel().selectFirst();
		}
	}

	private void addDeptComboBoxListener() {
		deptCombobox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				if(newValue != null) {
					loadModelYearComboBox(newValue);
					panel.clearErrorMessage();
				}
			} 
		});
	}

	private void loadModelYearComboBox(String dept) {
		modelYearCombobox.getControl().getItems().clear();
		modelYearCombobox.getControl().setValue(null);
		prodRateCombobox.getControl().getItems().clear();
		prodRateCombobox.getControl().setValue(null);
		lineNoCombobox.getControl().getItems().clear();
		vmcCombobox.getControl().getItems().clear();
		lineNoCombobox.getControl().setValue(null);
		vmcCombobox.getControl().setValue(null);
		modelYearCombobox.getControl().setPromptText(ViosConstants.SELECT);
		String plant = plantCombobox.getControl() != null ? plantCombobox.getControl().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		for (BigDecimal modelYear : getMCViosMasterPlatformDao().findAllModelYearBy(plant, dept)) {
			modelYearCombobox.getControl().getItems().add(modelYear.toString());
		}
		if(modelYearCombobox.getControl().getItems().size() == 1) {
			modelYearCombobox.getControl().getSelectionModel().selectFirst();
		}
	}

	private void addModelYearComboBoxListener() {
		modelYearCombobox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				if(newValue != null) {
					loadProdRateComboBox(newValue);
					panel.clearErrorMessage();
				}
			} 
		});
	}

	private void loadProdRateComboBox(String modelYear) {
		prodRateCombobox.getControl().getItems().clear();
		prodRateCombobox.getControl().setValue(null);
		lineNoCombobox.getControl().getItems().clear();
		vmcCombobox.getControl().getItems().clear();
		lineNoCombobox.getControl().setValue(null);
		vmcCombobox.getControl().setValue(null);
		prodRateCombobox.getControl().setPromptText(ViosConstants.SELECT);
		String plant = plantCombobox.getControl() != null ? plantCombobox.getControl().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		String dept = deptCombobox.getControl() != null ? deptCombobox.getControl().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		for (BigDecimal l : getMCViosMasterPlatformDao().findAllProdQtyBy(plant, dept, 
				new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)))) {
			prodRateCombobox.getControl().getItems().add(l.toString());
		}
		if(prodRateCombobox.getControl().getItems().size() == 1) {
			prodRateCombobox.getControl().getSelectionModel().selectFirst();
		}
	}

	private void addProdRateComboBoxListener() {
		prodRateCombobox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) {
				if(newValue != null) {
					loadLineNoComboBox(newValue);
					panel.clearErrorMessage();
				}
			} 
		});
	}

	private void loadLineNoComboBox(String productionRate) {
		lineNoCombobox.getControl().getItems().clear();
		vmcCombobox.getControl().getItems().clear();
		lineNoCombobox.getControl().setValue(null);
		vmcCombobox.getControl().setValue(null);
		lineNoCombobox.getControl().setPromptText(ViosConstants.SELECT);
		vmcCombobox.getControl().getItems().clear();
		String plant = plantCombobox.getControl() != null  && deptCombobox.getControl().getSelectionModel().getSelectedItem() != null ? 
				plantCombobox.getControl().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
				String dept = deptCombobox.getControl() != null && deptCombobox.getControl().getSelectionModel().getSelectedItem() != null ? 
						deptCombobox.getControl().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
						String modelYear = modelYearCombobox.getControl() != null && modelYearCombobox.getControl().getSelectionModel().getSelectedItem() != null ? 
								modelYearCombobox.getControl().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
								if(!StringUtils.isBlank(modelYear))
									lineNoCombobox.getControl().getItems().addAll(getMCViosMasterPlatformDao().findAllLineNoBy(plant, dept, 
											new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)), new BigDecimal(productionRate.replaceAll(",", StringUtils.EMPTY))));
								if(lineNoCombobox.getControl().getItems().size() == 1) {
									lineNoCombobox.getControl().getSelectionModel().selectFirst();
								}
	}

	private void addLineNoComboBoxListener() {
		lineNoCombobox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) {
				if(newValue != null) {
					loadVMCComboBox(newValue);
					panel.clearErrorMessage();
				}
			} 
		});
	}

	private void loadVMCComboBox(String lineNo) {
		vmcCombobox.getControl().getItems().clear();
		vmcCombobox.getControl().setValue(null);
		vmcCombobox.getControl().setPromptText(ViosConstants.SELECT);
		String plant = plantCombobox.getControl() != null  && plantCombobox.getControl().getSelectionModel().getSelectedItem() != null ? 
				plantCombobox.getControl().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
				String dept = deptCombobox.getControl() != null  && deptCombobox.getControl().getSelectionModel().getSelectedItem() != null ? 
						deptCombobox.getControl().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
						String modelYear = modelYearCombobox.getControl() != null  && modelYearCombobox.getControl().getSelectionModel().getSelectedItem() != null ? 
								modelYearCombobox.getControl().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
								String productionRate = prodRateCombobox.getControl() != null  && prodRateCombobox.getControl().getSelectionModel().getSelectedItem() != null ? 
										prodRateCombobox.getControl().getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
										if(!StringUtils.isBlank(modelYear) && !StringUtils.isBlank(productionRate))
											vmcCombobox.getControl().getItems().addAll(getMCViosMasterPlatformDao().findAllVMCBy(plant, dept, 
													new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)), new BigDecimal(productionRate.replaceAll(",", StringUtils.EMPTY)), lineNo));
										if(vmcCombobox.getControl().getItems().size() == 1) {
											vmcCombobox.getControl().getSelectionModel().selectFirst();
										}
	}

	private void addVMCComboBoxListener() {
		vmcCombobox.getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				panel.clearErrorMessage();
				vmcComboboxListener(newValue);
			} 
		});
	}
	
	private void addComboBoxListener() {
		addPlantComboBoxListener();
		addDeptComboBoxListener();
		addModelYearComboBoxListener();
		addProdRateComboBoxListener();
		addLineNoComboBoxListener();
		addVMCComboBoxListener();
	}
	
	public void selectPlatform(MCViosMasterPlatform selectedPlatform) {
		getPlantCombobox().getSelectionModel().select(selectedPlatform.getPlantLocCode());
		getDeptCombobox().getSelectionModel().select(selectedPlatform.getDeptCode());
		getModelYearCombobox().getSelectionModel().select(selectedPlatform.getModelYearDate().toPlainString());
		getProdRateCombobox().getSelectionModel().select(selectedPlatform.getProdSchQty().toPlainString());
		getLineNoCombobox().getSelectionModel().select(selectedPlatform.getProdAsmLineNo());
		getVmcCombobox().getSelectionModel().select(selectedPlatform.getVehicleModelCode());
	}
	
	private MCViosMasterPlatformDao getMCViosMasterPlatformDao() {
		return ServiceFactory.getDao(MCViosMasterPlatformDao.class);
	}
	
	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
	
	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

	public ComboBox<String> getPlantCombobox() {
		return plantCombobox.getControl();
	}

	public ComboBox<String> getDeptCombobox() {
		return deptCombobox.getControl();
	}

	public ComboBox<String> getModelYearCombobox() {
		return modelYearCombobox.getControl();
	}

	public ComboBox<String> getProdRateCombobox() {
		return prodRateCombobox.getControl();
	}

	public ComboBox<String> getLineNoCombobox() {
		return lineNoCombobox.getControl();
	}

	public ComboBox<String> getVmcCombobox() {
		return vmcCombobox.getControl();
	}
	
	public abstract void vmcComboboxListener(String value);

}
