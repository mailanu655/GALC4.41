package com.honda.galc.client.product.pane;

import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.action.KeyboardAction;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.ServiceFactory;
import com.sun.glass.ui.Screen;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ProductInputPanel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * @author Karol Wozniak
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
public class ProductInputPane extends AbstractProductInputPane {

	private static final long serialVersionUID = 1L;

	private TextField productIdTextField;

	private TextField expectedProductIdField;

	private Button expectedProductButton;

	public Button productIdButton;
	
	private Button keyboardButton;

	private Label sequenceNumLabel;

	public ProductInputPane(ProductController productController) {
		super(productController);
		initView();
		mapActions();
	}

	public void initView() {

		String label = getProductController().getProductTypeData().getProductIdLabel();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getProductIdField().requestFocus();
			}
		});

		if (getProductController().getModel().getProperty().isManualProductEntryEnabled()) {
			productIdButton = UiFactory.createButton(label, UiFactory.getIdle().getButtonFont(), true);
			getChildren().add(productIdButton);
		} else
			getChildren().add(UiFactory.createLabel("productIdLabel", label, UiFactory.getIdle().getLabelFont()));
		getChildren().add(getProductIdField());
		if (getProductController().getModel().getProperty().isCheckExpectedProductId()) {
			getChildren().add(UiFactory.createLabel("EXPECTED", "EXPECTED:", UiFactory.getIdle().getButtonFont()));
			this.expectedProductIdField = UiFactory.createTextField("expectedProductIdField", getProductIdFieldLength(),
					UiFactory.getIdle().getInputFont(), TextFieldState.READ_ONLY);
			this.expectedProductIdField.setText(getProductController().getModel().getExpectedProductId());
			getChildren().add(expectedProductIdField);
			if (getProductController().getModel().getProperty().isShowProductBypass()) {
				expectedProductButton = UiFactory.createButton("BYPASS", UiFactory.getIdle().getButtonFont(), true,
						"expectedProductButton");
				getChildren().add(expectedProductButton);
			}
		

			// bak - 20150729 - Add showing Product Sequence
			if (getProductController().getModel().getProperty().isShowProductSequence()) {
				getChildren().add(UiFactory.createLabel("productSequenceLabel", " Sequence #",
						UiFactory.getIdle().getLabelFont()));
				sequenceNumLabel = UiFactory.createLabel("productSeqNumLabel");
				double fontSize = Math.ceil(Screen.getMainScreen().getWidth()*0.02);
				sequenceNumLabel.setStyle("-fx-font-size: " + fontSize + "pt; -fx-font-weight: bold;");
				getChildren().add(sequenceNumLabel);
				setProductSequence();
			}
		}
		if (getProductController().getModel().getProperty().isKeyboardButtonVisible()) {
			keyboardButton = UiFactory.createButton("KEYBOARD", UiFactory.getIdle().getButtonFont(), true);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					keyboardButton.setOnAction(new KeyboardAction(getProductController()));
				}
			});
			getChildren().add(keyboardButton);
		}
		// set Etched border
		setStyle("-fx-border-color: white, grey; -fx-border-width: 2, 1; -fx-border-insets: 0, 0 1 1 0");
		
		getProductController().setProductInputPane(this);
	}

	protected void mapActions() {
		if (getProductController().getModel().getProperty().isShowProductBypass()) {
			expectedProductButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if (expectedProductButton.getText().equals("BYPASS")) {
						expectedProductButton.setText("ENABLE");
						getProductController().getModel().setBypassExpectedProduct(true);
					} else {
						expectedProductButton.setText("BYPASS");
						getProductController().getModel().setBypassExpectedProduct(false);
					}
				}

			});
		}
		if (getProductController().getModel().getProperty().isManualProductEntryEnabled()) {

			productIdButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(final ActionEvent arg0) {
					getProductController().setKeyboardPopUpVisible(false);
					ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(
							"Manual Product Entry Dialog", getProductController().getProductTypeData(),getProductController().getView().getMainWindow().getApplicationContext().getApplicationId(),productIdTextField.getText());
					Logger.getLogger().check("Manual Product Entry Dialog Box populated");
					manualProductEntryDialog.showDialog();
					String productId = manualProductEntryDialog.getResultProductId();
					Logger.getLogger().check("Product Id : " + productId + " selected");
					if (!(productId == null || StringUtils.isEmpty(productId))) {
						getProductController().getView().getMainWindow().setWaitCursor();
						setProductId(productId);
						Platform.runLater(new Runnable() {
							@Override
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
	}

	@Override
	public TextField getProductIdField() {
		if (this.productIdTextField == null) {
			this.productIdTextField = (UpperCaseFieldBean)UiFactory.createTextField("productIdTextField", getProductIdFieldLength(),
					UiFactory.getIdle().getInputFont(), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
			
			this.productIdTextField.setMaxWidth(400);
			this.productIdTextField.setFocusTraversable(true);
		}
		return this.productIdTextField;
	}

	public ProductInputPane getProductInputPane() {
		return this;
	}
	
	// === get/set === //
	public TextField getExpectedProductIdField() {
		return expectedProductIdField;
	}

	private int getProductIdFieldLength() {
		List<ProductNumberDef> list = getProductController().getProductTypeData().getProductNumberDefs();
		int length = ProductNumberDef.getMaxLength(list);
		if (length < 1) {
			length = 17;
		}
		return length;
	}

	private String getProductSequence() {
		//2016-03-15 - BAK - Add try block to catch any exceptions and return N/A sequence
		try {
			Integer seq = -1;
			if (getProductController().getModel().getProductType().equals("FRAME")) {
				Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(this.expectedProductIdField.getText());
				
				//2016-03-15 - BAK - Add check for no frame found
				if (frame == null) {
					return "N/A";
				}
				seq = frame.getAfOnSequenceNumber();
			}

			if (seq != null && seq != -1) {
				return seq.toString();
			} else {
				return "N/A";
			}
		}
		catch (Exception ex) {
			return "N/A";
		}
	}
	
	public void setProductSequence() {
		sequenceNumLabel.setText(getProductSequence());
	}
	
	@Override
	public LoggedTextField getQuantityTextField() {
		return null;
	}
}
