package com.honda.galc.client.teamleader.common;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.StatusMessagePane;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.util.StringUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SpecMaintDialog extends FxDialog{

	private LoggedButton createBtn;
	private LoggedButton updateBtn;
	private LoggedButton cancelBtn;
	private StatusMessagePane statusMessagePane;
	private LoggedComboBox<String> extColorCodeCombobox;
	private LoggedComboBox<String> intColorCodeCombobox;
	private LoggedComboBox<String> modelYearCodeCombobox;
	private LoggedComboBox<String> modelCodeCombobox;
	private LoggedComboBox<String> modelTypeCodeCombobox;
	private LoggedComboBox<String> modelOptionCodeCombobox;
	private ProductSpecData productSpecData;
	private FrameSpec frameSpec;
	private EngineSpec engineSpec;
	private ProductSpec productSpec;
	private String productType;
	private String title;
	private MainWindow mainWindow;
	private SpecMaintPanel<ProductSpec> specCodePanel;
	
	private static String UPDATE_MESSAGE = "Spec Code updated succesfully.";
	private static String CREATE_MESSAGE = "Spec Code added succesfully.";
	private static String NOCHANGE_MESSAGE = "No change old and new spec code!";
	private static String VALIDATION_MESSAGE ="Please enter field";
	
	
	public SpecMaintDialog(String title,ProductSpec productSpec,String productType, 
			ProductSpecData productSpecData, SpecMaintPanel<ProductSpec> specCodePanel) {
		super(title+" "+productType.toUpperCase()+" SPEC CODE", specCodePanel.getMainWindow().getStage());
	
		this.productSpecData = productSpecData;
		this.productType = productType;
		this.title = title;
		this.mainWindow = specCodePanel.getMainWindow();
		this.specCodePanel = specCodePanel;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		((BorderPane) this.getScene().getRoot()).setBottom(initStatusMessagePane());
		mainWindow.getStage().centerOnScreen();
		this.productSpec = productSpec;
		if(productType.equalsIgnoreCase(ProductType.FRAME.toString())){
			this.frameSpec = (FrameSpec) productSpec;
		} else {
			this.engineSpec = (EngineSpec) productSpec;
		}
		initComponents();
		loadData(title);
		initListeners();
	}


	/**
	 * Adds Listeners to each column in dialog
	 * *Column restrict Length Of TextFields
	 * *Convert types value to uppercase
	 */
	private void initListeners() {
		modelYearCodeCombobox.addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(1));
		modelCodeCombobox.addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(3));
		modelTypeCodeCombobox.addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(3));
		modelOptionCodeCombobox.addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(3));
	
	    TextField modelYearCodeTextField = (TextField)modelYearCodeCombobox.editorProperty().get();
	    convertToUppperCase(modelYearCodeTextField);
	    
	    TextField modelCodeTextField = (TextField)modelCodeCombobox.editorProperty().get();
	    convertToUppperCase(modelCodeTextField);
	    
	    TextField modeltypeCodeTextField = (TextField)modelTypeCodeCombobox.editorProperty().get();
	    convertToUppperCase(modeltypeCodeTextField);
	    
	    TextField modelOptionCodeTextField = (TextField)modelOptionCodeCombobox.editorProperty().get();
	    convertToUppperCase(modelOptionCodeTextField);
	    
	    if(productType.equalsIgnoreCase(ProductType.FRAME.toString())){
			extColorCodeCombobox.addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(10));
			intColorCodeCombobox.addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(2));
		    TextField extColorCodeTextField = (TextField)extColorCodeCombobox.editorProperty().get();
		    convertToUppperCase(extColorCodeTextField);
		    
		    TextField intColorCodeTextField = (TextField)intColorCodeCombobox.editorProperty().get();
		    convertToUppperCase(intColorCodeTextField);
	    }
	}
	/**
	 * Loads Drop down of Model year code, Model codes , Model type code and Model Option Code
	 * For update dialog - sets values to text fields
	 * @param title - CREATE/UPDATE
	 */
	private void loadData(String title) {
		List<String> modelYearCodeList= productSpecData.getModelYearCodes();
		modelYearCodeCombobox.getItems().addAll(modelYearCodeList);
		List<String> modelCodeList = productSpecData.getModelCodes();
		modelCodeCombobox.getItems().addAll(modelCodeList);
		List<String> modelTypeList = productSpecData.getModelTypeCodes();
		modelTypeCodeCombobox.getItems().addAll(modelTypeList);
		List<String> modelOptionList =productSpecData.getModelOptionCodes();
		modelOptionCodeCombobox.getItems().addAll(modelOptionList);
		
		if(productType.equalsIgnoreCase(ProductType.FRAME.toString())){
			extColorCodeCombobox.getItems().addAll(getColorList("extColor"));
			intColorCodeCombobox.getItems().addAll(getColorList("intColor"));
		}
		
		if(title.equalsIgnoreCase(QiConstant.UPDATE)) {
				modelYearCodeCombobox.setValue(this.productSpec.getModelYearCode());
				modelCodeCombobox.setValue(this.productSpec.getModelCode());
				modelTypeCodeCombobox.setValue(this.productSpec.getModelTypeCode());
				modelOptionCodeCombobox.setValue(this.productSpec.getModelOptionCode());
				if(this.frameSpec!=null) {
					extColorCodeCombobox.setValue(this.productSpec.getExtColorCode());
					intColorCodeCombobox.setValue(this.productSpec.getIntColorCode());
				}
			
		}
	}
	
	/**
	 * Get Interior and exterior colorlist
	 * @param colorType
	 * @return
	 */
	private Set<String> getColorList(String colorType) {
		Set<String> colorList = new HashSet<String>();
		if (productSpecData.getProductSpecs() != null) {
			for (ProductSpec mto : productSpecData.getProductSpecs()) {
				if(mto != null) {
					FrameSpec framespec = (FrameSpec) mto;
					if(colorType.equalsIgnoreCase("extColor")) {
						if (framespec.getExtColorCode() != null)
							colorList.add(mto.getExtColorCode());
					} else if(colorType.equalsIgnoreCase("intColor")) {
						if ( framespec.getIntColorCode() != null)
							colorList.add(mto.getIntColorCode());
					}
				}
			}
		}
		return colorList;
	}


	/**
	 * Method to convert textfield value to uppercase
	 * @param textField
	 */
	private void convertToUppperCase(TextField textField) {
		textField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	textField.setText(newValue.toUpperCase());
		    }
		});
		
	}
	/**
	 * create the view box and sets size
	 */
	private void initComponents() {
		VBox outerPane = new VBox();
		outerPane.setSpacing(20);
		outerPane.setPrefWidth(650);
		outerPane.setPrefHeight(180);
		if(productType.equalsIgnoreCase(ProductType.FRAME.toString())){
			outerPane.setPrefWidth(650);
			outerPane.setPrefHeight(220);
		}
		outerPane.getChildren().addAll(createMainContainer());
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
		if(this.title.equalsIgnoreCase(QiConstant.CREATE)) {
			createBtn.setDisable(false);
			updateBtn.setDisable(true);
		} else if(this.title.equalsIgnoreCase(QiConstant.UPDATE)) {
			createBtn.setDisable(true);
			updateBtn.setDisable(false);
		}
		
	}
	
	
	/**
	 * This method is used to create MigPane containing all comboboxes
	 */
	private MigPane createMainContainer(){
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");

		HBox firstComboBoxContainer = new HBox();
		modelYearCodeCombobox = new LoggedComboBox<String>("modelyearCombo");
		modelCodeCombobox = new LoggedComboBox<String>("modelCode");
		firstComboBoxContainer.getChildren().addAll(getcontainer(modelYearCodeCombobox, "Model Year Code"), 
				getcontainer(modelCodeCombobox, "Model Code"));
		firstComboBoxContainer.setSpacing(10);
		firstComboBoxContainer.setPadding(new Insets(5));

		HBox secondComboBoxContainer= new HBox();
		modelTypeCodeCombobox = new LoggedComboBox<String>("modeltypeCombo");
		modelOptionCodeCombobox = new LoggedComboBox<String>("modeloptionCombo");
		secondComboBoxContainer.getChildren().addAll(getcontainer(modelTypeCodeCombobox, "Model Type Code"),
				getcontainer(modelOptionCodeCombobox, "Model Option Code"));
		secondComboBoxContainer.setSpacing(10);
		secondComboBoxContainer.setPadding(new Insets(5));
		
		pane.add(firstComboBoxContainer,"span,wrap");
		pane.add(secondComboBoxContainer,"span,wrap");
		
		if(productType.equalsIgnoreCase(ProductType.FRAME.toString())) {
			HBox thirdComboBoxContainer= new HBox();
			extColorCodeCombobox = new LoggedComboBox<String>("extColorCode");
			intColorCodeCombobox = new LoggedComboBox<String>("intColorCode");
			thirdComboBoxContainer.getChildren().addAll(getcontainer(extColorCodeCombobox, "Ext Color Code"),
					getcontainer(intColorCodeCombobox, "Int Color Code"));
			thirdComboBoxContainer.setSpacing(10);
			thirdComboBoxContainer.setPadding(new Insets(5));
			pane.add(thirdComboBoxContainer,"span,wrap");
		}
		pane.add(createButtonContainer(),"span,wrap");

		return pane;
	}

	private Node getcontainer(LoggedComboBox<String> comboBox, String label) {
		HBox container = new HBox();
		comboBox.getStyleClass().add("combo-box-base");		
		comboBox.setPadding(new Insets(4, 4, 4, 4));
		comboBox.setEditable(true);

		HBox labelBox = new HBox();
		LoggedLabel mainNoLabel = UiFactory.createLabelWithStyle("lable", label, "display-label");
		LoggedLabel asterisk1 = UiFactory.createLabelWithStyle("asterisk1", "*","display-label");
		asterisk1.setStyle("-fx-text-fill: red");
		labelBox.setMaxWidth(130.0);
		labelBox.setPrefWidth(130.0);
		labelBox.setPadding(new Insets(5,5,5,5));
		labelBox.getChildren().addAll(mainNoLabel,asterisk1);

		container.getChildren().addAll(labelBox, comboBox);
		return container;
	}

	/**
	 * Method creates Button container contains CREATE, UPDARE and CANCEL
	 * @return
	 */
	private HBox createButtonContainer() {
		HBox buttonContainer = new HBox();
		createBtn = createBtn(QiConstant.CREATE);
		updateBtn = createBtn(QiConstant.UPDATE);
		cancelBtn = createBtn(QiConstant.CANCEL);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(10, 10, 10, 10));
		buttonContainer.setSpacing(13);
		buttonContainer.getChildren().addAll(createBtn, updateBtn,cancelBtn);
		return buttonContainer;
	}
	/**
	 * Creates the buttons and action on those buttons
	 * @param text - ButtonText
	 * @return
	 */
	public LoggedButton createBtn(String text)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.getStyleClass().add("popup-btn");
		btn.defaultButtonProperty().bind(btn.focusedProperty());
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if(text.equalsIgnoreCase(QiConstant.CANCEL)){
						((Stage)cancelBtn.getScene().getWindow()).close();
					} else {
						if(validateForm()) {
							if(productType.equalsIgnoreCase(ProductType.FRAME.name())) 
								actionFrame(text);
							 else 
								actionEngine(text);
						}}
				}});
		return btn;
	}
	
	
	/**
	 * Method for add and update of Frame spec code 
	 * @param buttonText - CREATE/UPDATE
	 */
	protected void actionEngine(String buttonText) {
		String oldSpecCode = "";
		if(buttonText.equalsIgnoreCase(QiConstant.CREATE)) {
			engineSpec = new EngineSpec();
		} else {
			oldSpecCode = engineSpec.getProductSpecCode();
		}
		engineSpec.setModelYearCode(modelYearCodeCombobox.getSelectionModel().getSelectedItem().toString().toUpperCase());
		engineSpec.setModelCode(modelCodeCombobox.getSelectionModel().getSelectedItem().toString().toUpperCase());
		engineSpec.setModelTypeCode(modelTypeCodeCombobox.getSelectionModel().getSelectedItem().toString().toUpperCase());
		engineSpec.setModelOptionCode(modelOptionCodeCombobox.getSelectionModel().getSelectedItem().toString().toUpperCase());
		String productSpecCode = engineSpec.getModelYearCode()+convertToRequired(engineSpec.getModelCode(), 3)
		+convertToRequired(engineSpec.getModelTypeCode(),3)+convertToRequired(engineSpec.getModelOptionCode(),3);
		
		engineSpec.setProductSpecCode(productSpecCode);
		
		EngineSpec engineSpecObj = getDao(EngineSpecDao.class).findByKey(productSpecCode);
		if(buttonText.equalsIgnoreCase(QiConstant.UPDATE)) {
			if(oldSpecCode.equalsIgnoreCase(productSpecCode.trim())) {
				MessageDialog.showInfo(mainWindow.getStage(), NOCHANGE_MESSAGE);
			}  else {
				if(engineSpecObj != null) {
					MessageDialog.showInfo(mainWindow.getStage(), "Failed to update Engine Spec Code as the Product Spec Code name " +productSpecCode + " already exists!");
					((Stage)updateBtn.getScene().getWindow()).close();	
				} else {
					boolean isExits = getDao(PreProductionLotDao.class).findByProdSpecCode(oldSpecCode);
					if(isExits) {
						isExits = MessageDialog.confirm(mainWindow.getStage(), "Schedule already exists for "+oldSpecCode+", Do you want to update?");
					} else {
						isExits = true;
					}
					
					if(engineSpecObj == null && isExits) {
						getDao(EngineSpecDao.class).updateEngineSpecCode(engineSpec, oldSpecCode);
						MessageDialog.showInfo(mainWindow.getStage(), UPDATE_MESSAGE);
						((Stage)updateBtn.getScene().getWindow()).close();	
					} if(!isExits) {
						((Stage)updateBtn.getScene().getWindow()).close();	
					}
				}
			}
		} else {
			if(engineSpecObj == null) {
				getDao(EngineSpecDao.class).insert(engineSpec);
				MessageDialog.showInfo(mainWindow.getStage(), CREATE_MESSAGE);
			} else {
				MessageDialog.showInfo(mainWindow.getStage(), "Failed to add Engine Spec Code as the Product Spec Code name " +productSpecCode + " already exists!");
			}
			((Stage)createBtn.getScene().getWindow()).close();	
		}
		
	}
	/**
	 * Method for add and update of Engine spec code 
	 * @param buttonText - CREATE/UPDATE
	 */
	protected void actionFrame(String buttonText) {
		String oldSpecCode = "";
		if(buttonText.equalsIgnoreCase(QiConstant.CREATE)) {
			frameSpec = new FrameSpec();
		} else {
			oldSpecCode = frameSpec.getProductSpecCode();
		}
		frameSpec.setModelYearCode(modelYearCodeCombobox.getSelectionModel().getSelectedItem().toString().toUpperCase());
		frameSpec.setModelCode(modelCodeCombobox.getSelectionModel().getSelectedItem().toString().toUpperCase());
		frameSpec.setModelTypeCode(modelTypeCodeCombobox.getSelectionModel().getSelectedItem().toString().toUpperCase());
		frameSpec.setModelOptionCode(modelOptionCodeCombobox.getSelectionModel().getSelectedItem().toString().toUpperCase());
		frameSpec.setExtColorCode(extColorCodeCombobox.getSelectionModel().getSelectedItem().toString().toUpperCase());
		frameSpec.setIntColorCode(intColorCodeCombobox.getSelectionModel().getSelectedItem().toString().toUpperCase());
		
		String productSpecCode = frameSpec.getModelYearCode()+convertToRequired(frameSpec.getModelCode(), 3)
		+convertToRequired(frameSpec.getModelTypeCode(),3)+convertToRequired(frameSpec.getModelOptionCode(),3)
		+convertToRequired(frameSpec.getExtColorCode(),10)+convertToRequired(frameSpec.getIntColorCode(),2);
		frameSpec.setProductSpecCode(productSpecCode);
			
		FrameSpec frameSpecObj = getDao(FrameSpecDao.class).findByKey(productSpecCode);
		if(buttonText.equalsIgnoreCase(QiConstant.UPDATE)) {
			if(oldSpecCode.equalsIgnoreCase(productSpecCode.trim())) {
				MessageDialog.showInfo(mainWindow.getStage(),NOCHANGE_MESSAGE);
			} else {
				if(frameSpecObj != null) {
					MessageDialog.showInfo(mainWindow.getStage(), "Failed to update Frame Spec Code as the Product Spec Code name " +productSpecCode + " already exists!");
					((Stage)updateBtn.getScene().getWindow()).close();	
				} else {
					boolean isExits = getDao(PreProductionLotDao.class).findByProdSpecCode(oldSpecCode);
					if(isExits) {
						isExits = MessageDialog.confirm(mainWindow.getStage(), "Schedule already exists for "+oldSpecCode+", Do you want to update?");
					} else {
						isExits = true;
					}
					if(frameSpecObj == null && isExits) {
						getDao(FrameSpecDao.class).updateFrameSpecCode(frameSpec, oldSpecCode);
						MessageDialog.showInfo(mainWindow.getStage(), UPDATE_MESSAGE);
						((Stage)updateBtn.getScene().getWindow()).close();	
					} if(!isExits) {
						((Stage)updateBtn.getScene().getWindow()).close();	
					}
				}
			}
		} else {
			if(frameSpecObj == null) {
				frameSpec.setBoundaryMarkRequired("");
				getDao(FrameSpecDao.class).insert(frameSpec);
				MessageDialog.showInfo(mainWindow.getStage(), CREATE_MESSAGE);
				
			} else {
				MessageDialog.showInfo(mainWindow.getStage(), "Failed to add Frame Spec Code as the Product Spec Code name " +productSpecCode + " already exists!");
			}
			((Stage)createBtn.getScene().getWindow()).close();	
		}
	  
		
	}
	/**
	 * Converts String to required lenth by appending space
	 * @param text - Actual String
	 * @param requiredSize
	 * @return Output String
	 */
	protected String convertToRequired(String text, int requiredSize) {
		int diff = requiredSize - text.length();
		StringBuffer sb = new StringBuffer();
		sb.append(text);
		for (int i = 1 ;i<=diff; i++) {
			sb.append(Delimiter.SPACE);
		}
		return sb.toString();
	}
	
	/**
	 * Update and Add form validation for Frame and Engine
	 */
	private boolean validateForm() {
		String message = "";
		
		if(productType.equalsIgnoreCase(ProductType.FRAME.toString())) {
			if(StringUtils.isBlank((String)extColorCodeCombobox.getSelectionModel().getSelectedItem())) {
				message = VALIDATION_MESSAGE+" Ext Color Code";
			}else if(StringUtils.isBlank((String)intColorCodeCombobox.getSelectionModel().getSelectedItem())) {
				message = VALIDATION_MESSAGE+" Int Color Code";
			}
		}
		
		if(StringUtils.isBlank((String)modelYearCodeCombobox.getSelectionModel().getSelectedItem())) {
			message = VALIDATION_MESSAGE+" Model Year Code";
		} else if(StringUtils.isBlank((String)modelCodeCombobox.getSelectionModel().getSelectedItem())){
			message = VALIDATION_MESSAGE+" Model Code";
		}else if(StringUtils.isBlank((String)modelTypeCodeCombobox.getSelectionModel().getSelectedItem())) {
			message = VALIDATION_MESSAGE+" Model Type Code";
		}else if(StringUtils.isBlank((String)modelOptionCodeCombobox.getSelectionModel().getSelectedItem())) {
			message = VALIDATION_MESSAGE+" Model Option Code";
		}

		if(!StringUtil.isNullOrEmpty(message)) {
			setErrorMessage(message);
			return false;
		}
		return true;
	}
	
	public void setErrorMessage(String message) {
		this.getStatusMessagePane().setErrorMessageArea(message);
	}
	
	public StatusMessagePane getStatusMessagePane() {
		return statusMessagePane;
	}
	
	public Pane initStatusMessagePane() {
		statusMessagePane = new StatusMessagePane(true);
		return statusMessagePane;
	}
}
