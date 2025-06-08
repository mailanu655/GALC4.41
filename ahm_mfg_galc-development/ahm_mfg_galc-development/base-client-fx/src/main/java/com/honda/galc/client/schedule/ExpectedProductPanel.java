package com.honda.galc.client.schedule;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.product.BaseProduct;

/**
 * <h3>Class description</h3> Expected Product Panel used by schedule client.
 * <h4>Description</h4> <h4>Special Notes</h4> <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 */

public class ExpectedProductPanel extends BorderPane {

	public static final int PRODUCT_ID_FIELD_WIDTH = 28;

	private BaseProduct expectedProduct;
	private String expectedProductLabel = "Product ID";

	private Label productIdLabel;
	private TextField productIdTextField;
	private TextField specTextField;
	private Integer reset = null;
	private String processingProductId;
	private boolean checkDuplicateProductId = false;

	public ExpectedProductPanel() {
		super();
		EventBusUtil.register(this);
		initializePanel();
		if (expectedProduct != null) {
			populateData();
		}

		// getProductIdTextField().requestFocusInWindow();
	}

	public ExpectedProductPanel(Integer reset) {
		this();
		this.reset = reset;
	}

	public void populateData() {
		productIdTextField.setText(expectedProduct.getProductId());
		productIdLabel.setText(expectedProductLabel);
	}

	private void initializePanel() {
		productIdLabel = new Label(expectedProductLabel);
		productIdLabel.setFont(Fonts.DIALOG_PLAIN_22);

		productIdTextField = UiFactory.createTextField("productIdTextField", "");
		productIdTextField.setFont(Fonts.DIALOG_PLAIN_24);
		productIdTextField
				.setStyle("-fx-text-fill:WHITE; -fx-background-color:BLUE;");

		HBox hBox = new HBox();
		HBox.setHgrow(productIdTextField, Priority.ALWAYS);
		HBox.setHgrow(productIdLabel, Priority.SOMETIMES);
		hBox.setPadding(new Insets(15, 12, 15, 12));
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(productIdLabel, productIdTextField);
		setCenter(hBox);

		productIdTextField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (isCheckDuplicateProductId()) {
					processProductIdWithDuplicateCheck(e);
				} else {
					processProductId(e);
				}

			}
		});
	}

	private void processProductIdWithDuplicateCheck(ActionEvent e) {
		String productId = StringUtils
				.trimToEmpty(productIdTextField.getText());
		if (StringUtils.equals(processingProductId, productId)) {
			// The processing product id equals to the current product id. So
			// ignore the duplicate request
			e.consume();
			return;
		}
		processingProductId = productId;
		final SchedulingEvent schEvent = new SchedulingEvent(productId,
				SchedulingEventType.PROCESS_PRODUCT);

		EventBusUtil.publish(schEvent);
		if (reset != null)
			resetLaterForCurrentProductId();
		else
			productIdTextField.setText("");
		e.consume();
	}

	private void processProductId(ActionEvent e) {
		final SchedulingEvent schEvent = new SchedulingEvent(
				productIdTextField.getText(),
				SchedulingEventType.PROCESS_PRODUCT);

		EventBusUtil.publish(schEvent);

		if (reset != null)
			resetLater();
		else
			productIdTextField.setText("");
	}

	private void resetLaterForCurrentProductId() {
		final String currentProductId = productIdTextField.getText();
		Thread t = new Thread() {
			public void run() {
				try {
					Thread.sleep(reset);
				} catch (Exception e) {
					
				}
				Platform.runLater(new Runnable() {
					public void run() {
						// Swing Components must be accessed from AWT Event
						// Dispatch Thread
						if (StringUtils.equals(currentProductId,
								productIdTextField.getText())) {
							// only reset the text field if the product id value
							// doesn't changes.
							productIdTextField.setText("");
						}
					}
				});

			}
		};

		t.start();
	}

	public BaseProduct getExpectedProduct() {
		return expectedProduct;
	}

	public void setExpectedProduct(BaseProduct expectedProduct) {
		this.expectedProduct = expectedProduct;
	}

	public String getExpectedProductLabel() {
		return expectedProductLabel;
	}

	public void setExpectedProductLabel(String expectedProductLabel) {
		this.expectedProductLabel = expectedProductLabel;
	}

	public Label getProductIdLabel() {
		return productIdLabel;
	}

	public void setProductIdLabel(Label productIdLabel) {
		this.productIdLabel = productIdLabel;
	}

	public TextField getProductIdTextField() {
		return productIdTextField;
	}

	public void setProductIdTextField(TextField productIdTextField) {
		this.productIdTextField = productIdTextField;
	}

	public TextField getSpecTextField() {
		return specTextField;
	}

	public void setSpecTextField(TextField specTextField) {
		this.specTextField = specTextField;
	}

	private void resetLater() {
		final String currentProductId = productIdTextField.getText();
		Thread t = new Thread() {
			public void run() {
				try {
					Thread.sleep(reset);
				} catch (Exception e) {

				}
				Platform.runLater(new Runnable() {
					public void run() {

						if (StringUtils.equals(currentProductId,
								productIdTextField.getText())) {
							// only reset the text field if the product id value
							// doesn't changes.
							productIdTextField.setText("");
						}
					}
				});

			}
		};

		t.start();

	}

	@Subscribe()
	public void onProductEvent(SchedulingEvent event) {
		if (event == null)
			return;
		if (event.getEventType().equals(SchedulingEventType.PRODUCT_ID_INPUT)) {
			getProductIdTextField().setText(event.getTargetObject().toString());
			getProductIdTextField().fireEvent(new ActionEvent());
		}
	}

	public boolean isCheckDuplicateProductId() {
		return checkDuplicateProductId;
	}

	public void setCheckDuplicateProductId(boolean checkDuplicateProductId) {
		this.checkDuplicateProductId = checkDuplicateProductId;
	}

}
