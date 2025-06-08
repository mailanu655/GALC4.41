package com.honda.galc.client.teamleader.speccode;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.StatusMessagePane;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameMTOCMasterSpecDao;
import com.honda.galc.dao.product.FrameMTOCPriceMasterSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameMTOCMasterSpec;
import com.honda.galc.entity.product.FrameMTOCMasterSpecId;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpec;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpecId;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.ServiceFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SpecCodeColorMaintDialog  extends FxDialog {

	private FrameSpecDto selectedFrameSpecDto;
	private TextField productIdTextField;
	private TextField productSpecCodeTextField;
	final private String font_style="-fx-font: 15 arial;";
	private Button saveBtn;
	private ObjectTablePane<FrameSpec> colorDetailsList;
	private Label headingLabel;
	private StatusMessagePane statusMessagePane;
	private Stage stage;
	private ObjectTablePane<FrameSpecDto> frameSpecDtoDataList;
	private List<String> productionLots;
	boolean isCompletedLot;
	private MainWindow mainWindow;
	private final static String ERROR_MESSAGE = "Select row to update Color Details";
	private final static String ERROR_MESSAGE1 ="Do you want to update completed lots? Please Confirm";
	private final static String ERROR_MESSAGE2 = "All products from lots ?1 will get updated, Please Confirm";
	private final static String ERROR_MESSAGE3 = "Lots are completed and All products from lots ?1 will get updated, Please Confirm";
	
	
	public SpecCodeColorMaintDialog(MainWindow mainWindow, FrameSpecDto selectedItem, ObjectTablePane<FrameSpecDto> frameSpecDtoDataList, List<String> productionLots, boolean isCompletedLot) {
		super("Product Spec Code Maintainance - Color Options");
		this.selectedFrameSpecDto = selectedItem;
		this.stage = mainWindow.getStage();
		this.mainWindow = mainWindow;
		this.frameSpecDtoDataList = frameSpecDtoDataList;
		this.productionLots = productionLots;
		this.isCompletedLot = isCompletedLot;
		initComponents();
		((BorderPane) this.getScene().getRoot()).setBottom(initStatusMessagePane());
		loadData();
		mapActions();
	}
	
	public Pane initStatusMessagePane() {
		statusMessagePane = new StatusMessagePane(true);
		return statusMessagePane;
	}

	private void mapActions() {
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				FrameSpec selectedItem = getColorDetailsList().getSelectedItem();
				clearErrorMessage();
				if(selectedItem == null) {
					setErrorMessage(ERROR_MESSAGE);
				} else {
					
					String newProductSpecCode = selectedItem.getProductSpecCode().substring(10, selectedItem.getProductSpecCode().length());
					String oldProductSpecCode = selectedFrameSpecDto.getProductSpecCode().substring(10, selectedItem.getProductSpecCode().length());
					String productCodeForUpdate = selectedFrameSpecDto.getProductSpecCode().replace(oldProductSpecCode, newProductSpecCode);
					
					List<Frame> frameList = ServiceFactory.getDao(FrameDao.class).getProductsBySpecCode(selectedFrameSpecDto.getProductSpecCode(), productionLots);

					List<String> productIds = new ArrayList<String>(); 
					for (Frame frame : frameList) {
						if(!productIds.contains(frame.getProductId()))
							productIds.add(frame.getProductId());
					}
					
					if(productionLots.size() > 1 && isCompletedLot) {
						if(!MessageDialog.confirm(stage, ERROR_MESSAGE3.replace("?1", productionLots.toString()), true)){
							return;
						} 
					} else if(productionLots.size() > 1 ) {
						if(!MessageDialog.confirm(stage, ERROR_MESSAGE2.replace("?1", productionLots.toString()), true)){
							return;
						}
					} else if(isCompletedLot) {
						if(!MessageDialog.confirm(stage,ERROR_MESSAGE1 , true)){
							return;
						}
					}
					FrameSpec frameSpec =  ServiceFactory.getDao(FrameSpecDao.class).getFrameDetails(selectedFrameSpecDto);
					
						if(frameSpec!=null) {
							frameSpec.setProductSpecCode(productCodeForUpdate);
							frameSpec.setExtColorCode(selectedItem.getExtColorCode());
							frameSpec.setIntColorCode(selectedItem.getIntColorCode());
							frameSpec.setExtColorDescription(selectedItem.getExtColorDescription());
							frameSpec.setIntColorDescription(selectedItem.getIntColorDescription());
							frameSpec.setSalesExtColorCode(selectedItem.getSalesExtColorCode());
							ServiceFactory.getDao(FrameSpecDao.class).save(frameSpec);
						}
						
						FrameMTOCPriceMasterSpec frameMTOCPriceMasterSpec  = ServiceFactory.getDao(FrameMTOCPriceMasterSpecDao.class).getFrameMTOCPriceMasterSpec(selectedFrameSpecDto);
						if(frameMTOCPriceMasterSpec!= null) {
							FrameMTOCPriceMasterSpecId id = frameMTOCPriceMasterSpec.getId();
							id.setIntColorCode(selectedItem.getIntColorCode());
							id.setExtColorCode(selectedItem.getExtColorCode());
							frameMTOCPriceMasterSpec.setId(id);
							ServiceFactory.getDao(FrameMTOCPriceMasterSpecDao.class).save(frameMTOCPriceMasterSpec);
						}
						FrameMTOCMasterSpec frameMTOCMasterSpec = ServiceFactory.getDao(FrameMTOCMasterSpecDao.class).getFrameMTOCMasterSpec(selectedFrameSpecDto);
						if(frameMTOCMasterSpec!=null) {
							FrameMTOCMasterSpecId id1 = frameMTOCMasterSpec.getId();
							id1.setExtColorCode(selectedItem.getExtColorCode());
							id1.setIntColorCode(selectedItem.getIntColorCode());
							frameMTOCMasterSpec.setId(id1);
							ServiceFactory.getDao(FrameMTOCMasterSpecDao.class).save(frameMTOCMasterSpec);
						}
					
					selectedFrameSpecDto.setUserId(ClientMainFx.getInstance().getApplicationContext().getUserId());
			
					ServiceFactory.getDao(FrameSpecDao.class).updateColorDetails(productCodeForUpdate, selectedFrameSpecDto, productionLots);
					
					mainWindow.getLogger().info("Product Spec code  "+selectedFrameSpecDto.getProductSpecCode()+" updated to "+productCodeForUpdate+" by "+selectedFrameSpecDto.getUserId());
					List<FrameSpecDto> frameSpecDtoList = ServiceFactory.getDao(FrameSpecDao.class).getProductIdDetails(selectedFrameSpecDto.getProductId());
					frameSpecDtoDataList.setData(frameSpecDtoList);
					MessageDialog.showInfo(stage, "Spec code color data updated succesfully.");
				}
				
			}
			
		});
		
	}


	private void loadData() {
		if(selectedFrameSpecDto != null) {
			if(selectedFrameSpecDto.getProductSpecCode() != null) {
				String productSpecCode = selectedFrameSpecDto.getProductSpecCode();
				headingLabel.setText("YMT Color Options ("+productSpecCode.substring(0, 7)+")");
				productIdTextField.setText(selectedFrameSpecDto.getProductId());
				productSpecCodeTextField.setText(selectedFrameSpecDto.getProductSpecCode());
				List<FrameSpec> frameSpecList= ServiceFactory.getDao(FrameSpecDao.class).getColorDetails(productSpecCode.substring(0, 7));
				colorDetailsList.setData(frameSpecList);
			}
		}
	}

	private void initComponents() {
		((BorderPane) this.getScene().getRoot()).setCenter(createMainContainer());
	}

	private Node createMainContainer() {

		HBox hbox = new HBox();
		VBox firstComboBoxContainer = new VBox();
		
		productIdTextField = UiFactory.createTextField("productIdTextField", 17,TextFieldState.EDIT);
		productIdTextField.setMaxWidth(200);
		productIdTextField.setFocusTraversable(true);
		productIdTextField.requestFocus();
		productIdTextField.setStyle(font_style);
		productIdTextField.setPrefSize(200, 30);
		productIdTextField.setDisable(true);
		
		productSpecCodeTextField = UiFactory.createTextField("productSpecCodeTextField", 17,TextFieldState.EDIT);
		productSpecCodeTextField.setMaxWidth(200);
		productSpecCodeTextField.setFocusTraversable(true);
		productSpecCodeTextField.requestFocus();
		productSpecCodeTextField.setStyle(font_style);
		productSpecCodeTextField.setPrefSize(200, 30);
		productSpecCodeTextField.setDisable(true);
		
		Label vinLabel = UiFactory.createLabel("vinLabel", "VIN" , font_style);
		Label originalProductSpecCode = UiFactory.createLabel("specCodeLabel", "Original Product Spec Code" , font_style);
		
		saveBtn = UiFactory.createButton("Save",font_style,true );
		saveBtn.setPrefSize(100, 30);
		
		firstComboBoxContainer.getChildren().addAll(vinLabel, productIdTextField, originalProductSpecCode, productSpecCodeTextField,saveBtn);
		firstComboBoxContainer.setSpacing(10);
		firstComboBoxContainer.setPadding(new Insets(5));

		VBox secondComboBoxContainer= new VBox();
		headingLabel = UiFactory.createLabel("headingLabel", "YMT Color Options (1)" , font_style);
		
		colorDetailsList = createColorDetailsPanels();
		secondComboBoxContainer.getChildren().addAll(headingLabel, colorDetailsList);
		secondComboBoxContainer.setSpacing(10);
		secondComboBoxContainer.setPadding(new Insets(5));
		
		hbox.getChildren().addAll(firstComboBoxContainer, secondComboBoxContainer);

		return hbox;
	}

	private ObjectTablePane<FrameSpec> createColorDetailsPanels() {
		
		ColumnMappingList columnMappingList = ColumnMappingList.with("Ext Color Code", "extColorCode")
				.put("Ext Color Desc", "extColorDescription")
				.put("Int Color Code","intColorCode").put("Int Color Desc", "intColorDescription");

		Double[] columnWidth = new Double[] {
				0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10
		}; 
		
		ObjectTablePane<FrameSpec> panel = new ObjectTablePane<FrameSpec>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;

	}

	public ObjectTablePane<FrameSpec> getColorDetailsList() {
		return colorDetailsList;
	}

	public void setColorDetailsList(ObjectTablePane<FrameSpec> colorDetailsList) {
		this.colorDetailsList = colorDetailsList;
	}
	
	
	public void setErrorMessage(String message) {
		this.getStatusMessagePane().setErrorMessageArea(message);
	}

	public void clearErrorMessage() {
		this.getStatusMessagePane().setStatusMessage(null);
		this.getStatusMessagePane().clearErrorMessageArea();
	}
	
	public StatusMessagePane getStatusMessagePane() {
		return statusMessagePane;
	}

	public void setStatusMessagePane(StatusMessagePane statusMessagePane) {
		this.statusMessagePane = statusMessagePane;
	}
	
}
