package com.honda.galc.client.dc.view;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.pane.AbstractProductInputPane;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;

import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;




public class PartShipmentInputPane  extends AbstractProductInputPane {

	
	private TextField productIdTextField;
	private Button productIdButton;
	private LoggedTextField trailerNoTextField;
	
	private Label siteValue, partCntVal;

	private Map<String, String> sitePartCountMap;
	private Map<String, String>  siteTrailerMaskMap;
	private boolean isScanTrailerMask;
	private String trailerNumber, site;
	private String trailerNumberLabel;

	
	public PartShipmentInputPane(ProductController productController) {
		super(productController);
		initComponents();
		mapActions();
	}
	

	protected void initComponents() {
		loadProperties();
		VBox vBox = new VBox();
		
		if(isScanTrailerMask)vBox.getChildren().add(createTrailerPanel());
		vBox.getChildren().add(createProductPanel());
		vBox.setPadding(new Insets(50));
	
		getChildren().add(vBox);

	}

	private void loadProperties() {
		trailerNumberLabel = getProductController().getModel().getProperty().getTrailerNumberLabel();
		isScanTrailerMask = getProductController().getModel().getProperty().isScanTrailerFirst();
		if(isScanTrailerMask){
			sitePartCountMap =   getProductController().getModel().getProperty().getSitePartCount();
			siteTrailerMaskMap =  getProductController().getModel().getProperty().getSiteTrailerMask();
		}
	}

	
	private Node createProductPanel() {
		
		HBox hbox1 = new HBox();
		hbox1.setPadding(new Insets(5, 10, 5, 10));
		
		String label = getProductController().getProductTypeData().getProductIdLabel();

		

		if (getProductController().getModel().getProperty().isManualProductEntryEnabled()) {
			productIdButton = UiFactory.createButton(label, UiFactory.getIdle().getButtonFont(), true);
			hbox1.getChildren().add(productIdButton);
		} else
			hbox1.getChildren().add(UiFactory.createLabel("productIdLabel", label, UiFactory.getIdle().getLabelFont()));
			hbox1.getChildren().add(getProductIdField());
			
		return hbox1;
	}
	
		
	private Node createTrailerPanel() {
		
		VBox vBox = new VBox();
		
		HBox hbox1 = new HBox();
		hbox1.setPadding(new Insets(5, 10, 5, 10));
				
		Label trailerNoLabel = UiFactory.createLabel("TrailerLabel", trailerNumberLabel,UiFactory.getIdle().getLabelFont(),150);
		
		HBox hbox2 = new HBox();
		hbox2.setPadding(new Insets(5, 10, 5, 10));
	
		Label siteLabel = UiFactory.createLabel("siteLabel", "Site ",UiFactory.getIdle().getLabelFont(),150);
		siteValue = UiFactory.createLabel("siteValue", "",UiFactory.getIdle().getLabelFont(),150);
		
		HBox hbox3 = new HBox();
		hbox3.setPadding(new Insets(5, 10, 25, 10));
		
		Label partCountLabel = UiFactory.createLabel("partCountLabel", "Part Count ",UiFactory.getIdle().getLabelFont(),150);
		partCntVal = UiFactory.createLabel("PartCount", " ",UiFactory.getIdle().getLabelFont(),150);
		
		hbox1.getChildren().add(trailerNoLabel);
		hbox1.getChildren().add(getTrailerNoField());
		hbox2.getChildren().add(siteLabel);
		hbox2.getChildren().add(siteValue);
		hbox3.getChildren().add(partCountLabel);
		hbox3.getChildren().add(partCntVal);
		
		
		vBox.getChildren().add(hbox1);
		vBox.getChildren().add(hbox2);
		vBox.getChildren().add(hbox3);
		
		return vBox;
	}
	
	private void loadSiteData(String trailerNo) {
		this.trailerNumber = trailerNo;
		this.site = getSite(trailerNo);
		if(StringUtils.isEmpty(this.site)){
			String msg = "Failed to find Matching site for Trailer, Please verify Configuration - ";
			for(String key: siteTrailerMaskMap.keySet()){
				msg = msg + key +"{ "+ siteTrailerMaskMap.get(key) +" }, ";
			}
			this.trailerNoTextField.selectAll();
			getProductController().getView().setErrorMessage(msg);
			getProductController().getAudioManager().playRepeatedNgSound();
		}else{
			getProductController().getAudioManager().playOKSound();
			getProductController().getView().setErrorMessage("");
			siteValue.setText(site);
			String partCount = sitePartCountMap.get(site);
			partCntVal.setText(partCount);
			this.getProductIdField().setDisable(false);
			getProductIdField().requestFocus();
			
		}
	}
		

	public String getName() {
		return "Part Shipment Input";
	}
	
	
	
	protected String getSite(String text) {
		String site = null;
		String partMaskFormat = PropertyService.getPartMaskWildcardFormat();
		if(siteTrailerMaskMap != null){
			Collection<String> values = siteTrailerMaskMap.values();
			for(String key: siteTrailerMaskMap.keySet()){
				String value = siteTrailerMaskMap.get(key);
				String[] tempMasks = value.split(",");
				for(String temp:tempMasks){
					if(CommonPartUtility.verification(text,temp, partMaskFormat)){
						site = key;
						break;
					}
					
				}
				if(!StringUtils.isEmpty(site)){
					break;
				}
			}
		}
	
		return site;
	}

	
	public LoggedTextField getTrailerNoField() {
		if (this.trailerNoTextField == null) {
			this.trailerNoTextField = UiFactory.createTextField("trailerNoTextField",getProductIdFieldLength(), UiFactory.getIdle().getInputFont(), TextFieldState.EDIT, Pos.CENTER, true);
			this.trailerNoTextField.setFocusTraversable(true);
		}
		return this.trailerNoTextField;
	}
	

	@Override
	public TextField getProductIdField() {
		if (this.productIdTextField == null) {
			this.productIdTextField = UiFactory.createTextField("productIdTextField", getProductIdFieldLength(),
					UiFactory.getIdle().getInputFont(), TextFieldState.EDIT, Pos.CENTER, true);
			this.productIdTextField.setMaxWidth(400);
			this.productIdTextField.setFocusTraversable(true);
		}
		return this.productIdTextField;
	}
	
	private int getProductIdFieldLength() {
		List<ProductNumberDef> list = getProductController().getProductTypeData().getProductNumberDefs();
		int length = ProductNumberDef.getMaxLength(list);
		if (length < 1) {
			length = 17;
		}
		return length;
	}

	@Override
	public TextInputControl getExpectedProductIdField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProductSequence() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LoggedTextField getQuantityTextField() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void mapActions() {
		
		if (getProductController().getModel().getProperty().isManualProductEntryEnabled()) {

			productIdButton.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(final ActionEvent arg0) {
					getProductController().setKeyboardPopUpVisible(false);
					ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(
							"Manual Product Entry Dialog", getProductController().getProductTypeData(),getProductController().getView().getMainWindow().getApplicationContext().getApplicationId());
					Logger.getLogger().check("Manual Product Entry Dialog Box populated");
					manualProductEntryDialog.showDialog();
					String productId = manualProductEntryDialog.getResultProductId();
					Logger.getLogger().check("Product Id : " + productId + " selected");
					if (!(productId == null || StringUtils.isEmpty(productId))) {
						getProductController().getView().getMainWindow().setWaitCursor();
						setProductId(productId);
						Platform.runLater(new Runnable() {
							public void run() {
								if (getProductController().getModel().getProperty().isAutoEnteredManualProductInput()) {
									getProductIdField().fireEvent(arg0);
                                    Logger.getLogger().check("Selected Product Id Auto Entered");
								}
								getProductController().getView().getMainWindow().setDefaultCursor();
							}
						});
					}
				}

			});

		}
		
		getTrailerNoField().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				loadSiteData(getTrailerNoField().getText());
			}

			
		});
		
	}
	
	public String getTrailerNumber(){
		return this.trailerNumber;
	}
	
	public String getSite(){
		return this.site;
	}
	
	public void resetView(){
		this.trailerNoTextField.setText("");
		this.siteValue.setText("");
		this.partCntVal.setText("");
		this.trailerNoTextField.requestFocus();
		this.getProductIdField().setDisable(true);
		getProductController().getView().setErrorMessage("");
	}
}
