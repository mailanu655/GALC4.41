package com.honda.galc.client.teamleader.fx.dataRecovery;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import net.miginfocom.layout.CC;

/**
 * 
 * <h3>ProductPanel Class description</h3>
 * <p> ProductPanel description </p>
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
 * July 16, 2017
 *
 *
 */
public class ProductPanel extends BorderPane implements EventHandler<ActionEvent> {

	List<ProductNumberDef> productNumberDefs = new ArrayList<ProductNumberDef>();

	private LoggedLabel dcNumberLabel;
	private UpperCaseFieldBean dcNumberTextField;
	private LoggedLabel mcNumberLabel;
	private UpperCaseFieldBean mcNumberTextField;

	protected LoggedLabel defectStatusLabel;
	protected UpperCaseFieldBean defectStatus;

	private LoggedButton searchDCButton;
	private LoggedButton searchMCButton;
	private LoggedButton refreshButton;
	private LoggedButton nextButton;

	private List<ProductNumberDef> dcNumberDefs;
	private List<ProductNumberDef> mcNumberDefs;
	private ProductRecoveryPanel parentPanel;

	private StackPane stackPane1, stackPane2;

	public ProductPanel(ProductRecoveryPanel parentPanel) {
		this.parentPanel = parentPanel;
		initialize();
	}

	protected void initialize() {
		initComponents();
		mapListeners();
		dcNumberDefs = ProductNumberDef.getProductNumberDef(getParentPanel().getProductType(), ProductNumberDef.NumberType.DC);
		mcNumberDefs = ProductNumberDef.getProductNumberDef(getParentPanel().getProductType(), ProductNumberDef.NumberType.MC);
	}

	protected void initComponents() {
		dcNumberLabel = createLabel("DC Number");
		mcNumberLabel = createLabel("MC Number");
		dcNumberTextField = createTextField("dcNumberTextField");
		mcNumberTextField = createTextField("mcNumberTextField");

		defectStatusLabel = createDefectStatusLabel();
		defectStatus = createDefectStatus();

		searchDCButton = Utils.createButton("Search DC", this);
		refreshButton = Utils.createButton("Refresh", this);
		searchMCButton = Utils.createButton("Search MC", this);
		nextButton = Utils.createButton("Next", this);

		refreshButton.setVisible(false);
		nextButton.setVisible(false);
		searchDCButton.setDisable(true);
		searchMCButton.setDisable(true);

		stackPane1 = new StackPane();
		stackPane1.setPadding(new Insets(5));
		stackPane1.setAlignment(Pos.CENTER_RIGHT);
		stackPane2 = new StackPane();
		stackPane2.setPadding(new Insets(5));
		stackPane2.setAlignment(Pos.CENTER_RIGHT);

		stackPane1.getChildren().add(searchDCButton);
		stackPane1.getChildren().add(refreshButton);
		stackPane2.getChildren().add(searchMCButton);
		stackPane2.getChildren().add(nextButton);

		MigPane migPane = new MigPane("insets 5 ", "[]", "");

		migPane.add(dcNumberLabel, new CC().alignX("left").minWidth((Utils.getScreenWidth() * 0.15)+"px"));
		migPane.add(dcNumberTextField,  new CC().alignX("left").minWidth((Utils.getScreenWidth() * 0.73)+"px"));
		migPane.add(stackPane1,  new CC().alignX("right").minWidth((Utils.getScreenWidth() * 0.12)+"px").wrap());
		migPane.add(mcNumberLabel, new CC().alignX("left").minWidth((Utils.getScreenWidth() * 0.12)+"px"));
		migPane.add(mcNumberTextField,  new CC().alignX("left").minWidth((Utils.getScreenWidth() * 0.73)+"px"));
		migPane.add(stackPane2,  new CC().alignX("right").minWidth((Utils.getScreenWidth() * 0.12)+"px").wrap());
		migPane.add(defectStatusLabel,  new CC().alignX("left").minWidth((Utils.getScreenWidth() * 0.12)+"px"));
		migPane.add(defectStatus,  new CC().alignX("left").minWidth((Utils.getScreenWidth() * 0.15)+"px").wrap());
		
		migPane.setPrefWidth(Utils.getScreenWidth() - 50);
		
		this.setCenter(migPane);
		this.setStyle("-fx-border-color: lightGray");
	}

	// === ui elements factory methods === //
	protected LoggedLabel createProductNumberLabel() {
		LoggedLabel label = new LoggedLabel();
		label.setText(getProductName() + " DC Number");
		label.setFont(Utils.getMainLabelFont());
		return label;
	}

	protected LoggedLabel createLabel(String text) {
		LoggedLabel label = new LoggedLabel(text);
		label.setText(text);
		label.setAlignment(Pos.CENTER);
		label.setFont(Utils.getMainLabelFont());
		return label;
	}

	protected UpperCaseFieldBean createTextField(String id){

		UpperCaseFieldBean textField = UiFactory.createUpperCaseFieldBean(id, getProductNumberLength(),
				Utils.getPlainFont(0.03), TextFieldState.EDIT, Pos.TOP_LEFT);
		textField.setOnAction(this);
		textField.setPrefSize(Utils.getScreenWidth() * 0.70 , Utils.getScreenHeight() * 0.055);
		textField.requestFocus();
		addTextChangeListener(textField);
		return textField;
	}


	protected LoggedLabel createDefectStatusLabel() {
		LoggedLabel label = new LoggedLabel();
		label.setText("Defect Status");
		label.setAlignment(Pos.CENTER);
		label.setFont(Utils.getMainLabelFont());
		label.setMinSize(Utils.getScreenWidth() * 0.15, Utils.getScreenHeight() * 0.05);
		return label;
	}

	protected UpperCaseFieldBean createDefectStatus() {
		UpperCaseFieldBean field = UiFactory.createUpperCaseFieldBean("defectStatusTextField", 15, Utils.getBoldFont(0.03), TextFieldState.READ_ONLY, Pos.TOP_LEFT);
		field.setPrefSize(Utils.getScreenWidth() * 0.15, Utils.getScreenHeight() * 0.055);
		field.requestFocus();
		field.setEditable(false);
		field.setAlignment(Pos.CENTER);
		return field;
	}


	// === get/set === //
	protected String getProductName() {
		return getParentPanel().getProductType().getProductName();
	}

	public int getProductNumberLength() {
		return ProductNumberDef.getMaxLength(getProductNumberDefs());
	}


	protected LoggedLabel getDefectStatusLabel() {
		return defectStatusLabel;
	}

	public UpperCaseFieldBean getDefectStatus() {
		return defectStatus;
	}

	protected ProductRecoveryPanel getParentPanel() {
		return parentPanel;
	}

	protected void setParentPanel(ProductRecoveryPanel parentPanel) {
		this.parentPanel = parentPanel;
	}

	// === state control === //
	protected BaseProduct getProduct() {	
		return getParentPanel().getController().getProduct();
	}

	public void setIdleMode() {

		dcNumberTextField.setText("");
		Utils.setTextFieldEditable(dcNumberTextField);
		dcNumberTextField.requestFocus();
		mcNumberTextField.setText("");
		Utils.setTextFieldEditable(mcNumberTextField);
		mcNumberTextField.requestFocus();

		getDefectStatus().setText("");
		Utils.setIdleColors(getDefectStatus());
		toggleButton();
	}

	public void setInputMode(ActionEvent actionEvent) {
		getController().getMainWindow().clearMessage();
		getController().getMainWindow().clearStatusMessage();

		BaseProduct product = getProduct();

		Utils.setInputReadOnlyColors(dcNumberTextField);
		dcNumberTextField.setText(product.getProductId());
		Utils.setInputReadOnlyColors(mcNumberTextField);

		if (product instanceof DieCast) {
			DieCast dieCast = (DieCast) product;
			mcNumberTextField.setText(dieCast.getMcSerialNumber());
		}

		if (product.getDefectStatus() == null || product.isRepairedStatus()) {
			Utils.setInputReadOnlyColors(getDefectStatus());
			getDefectStatus().setText("OK");
		} else {
			getDefectStatus().setText(product.getDefectStatus().getName());
			Utils.setErrorColors(getDefectStatus());
		}

		getDefectStatus().getParent().requestFocus();

		if (actionEvent.getSource() != refreshButton)
			toggleButton();
	}


	protected void setProductNumberValue(BaseProduct product) {
		dcNumberTextField.setText(product.getProductId());
	}

	// === mapping === //
	public void mapListeners() {
		addTextChangeListener(dcNumberTextField, searchDCButton);
		addTextChangeListener(mcNumberTextField, searchMCButton);
	}

	/**
	 * This method is used for Text Field Listener
	 */
	protected void addTextChangeListener(final UpperCaseFieldBean TextField, final LoggedButton button){
		TextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				button.setDisable(false);

			}
		});
	}

	public List<ProductNumberDef> getProductNumberDefs() {
		return productNumberDefs;
	}

	public void setProductNumberDefs(List<ProductNumberDef> productNumberDefs) {
		this.productNumberDefs = productNumberDefs;
	}

	private void dcNumberInputed(ActionEvent actionEvent) {
		dcNumberInputed(dcNumberTextField.getText(), actionEvent);
	}

	private boolean dcNumberInputed(String productId, ActionEvent actionEvent) {
		getController().resetModel();
		if(StringUtils.isEmpty(productId)){
			parentPanel.getMainWindow().setErrorMessage("DC number not entered.");
			return false;
		}

		if (!ProductNumberDef.isNumberValid(productId, dcNumberDefs)) {
			dcNumberTextField.selectAll();
			Utils.setEditableErrorColors(dcNumberTextField);
			parentPanel.getMainWindow().setErrorMessage("Invalid DC number.");
			dcNumberTextField.requestFocus();
			return false;
		}

		getController().findProductByDc(productId);
		if (parentPanel.getController().getProduct() == null) {
			dcNumberTextField.selectAll();
			Utils.setEditableErrorColors(dcNumberTextField);
			parentPanel.getMainWindow().setErrorMessage("DC number does not exist!");
			dcNumberTextField.requestFocus();
			return false;
		}

		getParentPanel().resetDataPanelElements();
		getController().findProductBuildResults(getProduct().getProductId(), getParentPanel().getDataPanel().getPartNames());
		parentPanel.setInputMode(actionEvent);
		return true;
	}

	private void mcNumberInputed(ActionEvent actionEvent) {
		mcNumberInputed(mcNumberTextField.getText(), actionEvent);
	}

	private boolean mcNumberInputed(String productId, ActionEvent actionEvent) {
		getController().resetModel();
		if(StringUtils.isEmpty(productId)) {
			parentPanel.getMainWindow().setErrorMessage("MC number not entered.");
			return false;
		}

		if (!ProductNumberDef.isNumberValid(productId, mcNumberDefs)) {
			mcNumberTextField.selectAll();
			Utils.setEditableErrorColors(mcNumberTextField);
			parentPanel.getMainWindow().setErrorMessage("Invalid MC number.");

			mcNumberTextField.requestFocus();
			return false;
		}

		getController().findProductByMc(productId);
		if (parentPanel.getController().getProduct() == null) {
			mcNumberTextField.selectAll();
			Utils.setEditableErrorColors(mcNumberTextField);
			parentPanel.getMainWindow().setErrorMessage("MC number does not exist!");
			mcNumberTextField.requestFocus();
			return false;
		}
		getParentPanel().resetDataPanelElements();
		getController().findProductBuildResults(getProduct().getProductId(), getParentPanel().getDataPanel().getPartNames());
		parentPanel.setInputMode(actionEvent);
		return true;
	}

	private void refreshButtonClicked(ActionEvent actionEvent)  {
		dcNumberInputed(getController().getProduct().getProductId(), actionEvent);
	}

	private void nextButtonClicked(){
		parentPanel.getController().resetModel();
		parentPanel.setIdleMode();
	}

	private DataRecoveryController getController() {
		return parentPanel.getController();
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() == searchDCButton || actionEvent.getSource() == dcNumberTextField) {
			dcNumberInputed(actionEvent);
		} else if (actionEvent.getSource() == refreshButton) {
			refreshButtonClicked(actionEvent);
		} else if (actionEvent.getSource() == searchMCButton || actionEvent.getSource() == mcNumberTextField) {
			mcNumberInputed(actionEvent);
		} else if (actionEvent.getSource() == nextButton) {
			nextButtonClicked();
		}
	}


	private void toggleButton() {
		toggleButton(stackPane1);
		toggleButton(stackPane2);
	}

	private void toggleButton(StackPane stackPane) {
		ObservableList<Node> childs = stackPane.getChildren();
		if (childs.size() > 1) {
			Node topButton = childs.get(childs.size()-1);
			Node newTopButton = childs.get(childs.size()-2);
			topButton.setVisible(false);
			topButton.toBack();
			newTopButton.setVisible(true);
		}
	}

	

	/**
	 * This method is used for Text Field Listener
	 */
	protected <T extends TextField>  void addTextChangeListener(final T textField){
		textField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				getController().getMainWindow().clearMessage();
				
			}
		});
	}

}
