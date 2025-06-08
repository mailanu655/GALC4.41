package com.honda.galc.client.product.action;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;

import com.honda.galc.checkers.AbstractBaseChecker;
import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.CheckResultsEvaluator;
import com.honda.galc.checkers.CheckerUtil;
import com.honda.galc.checkers.ExpectedProductChecker;
import com.honda.galc.checkers.ReactionType;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.ProcessEvent;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.process.IProcessController.ProcessState;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.BaseCheckerData;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.entity.conf.MCAppCheckerId;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Subu Kathiresan
 * @date Jul 22, 2015
 */
public class ProductReceivedAction extends AbstractProductDataAction {

	private static String checkPointName = CheckPoints.AFTER_PRODUCT_SCAN.toString();
	private Logger logger;
	private ProductModel model;

	public ProductReceivedAction(ApplicationMainPane appMainPane, ProductModel model) {
		super(appMainPane);
		this.model = model;
		CheckPointsRegistry.getInstance().register(this, checkPointName);
	}

	public boolean executeCheckers(BaseCheckerData ProductData) {

		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		SortedArrayList<MCAppChecker> appCheckers = CheckerUtil.getAppCheckers(ProductData.getCurrentProcessPoint(),
				getCheckPointName());
		if (!model.isBypassExpectedProduct() && model.getProperty().isCheckExpectedProductId()) {
			addDefaultExpectedProductCheckers(appCheckers);
		}
		for (MCAppChecker appChecker : appCheckers) {
			getLogger().info("Executing checker: " + appChecker.getCheckName());

			AbstractBaseChecker<ProductId> checker = CheckerUtil.createChecker(appChecker.getChecker(),
					ProductId.class);
			checker.setReactionType(appChecker.getReactionType());
			List<CheckResult> ckResults = checker.executeCheck(ProductData);
			if (ckResults != null && !ckResults.isEmpty()) {
				ckResults.get(0).setCheckName(appChecker.getCheckName());
				ckResults.get(0).setResult(dispatchReactions(ckResults, ProductData));
				checkResults.addAll(ckResults);
			}

		}

		getLogger().info("Check Results size: " + checkResults.size());
		return CheckResultsEvaluator.evaluate(checkResults);
	}

	private void addDefaultExpectedProductCheckers(SortedArrayList<MCAppChecker> appCheckers) {
		MCAppCheckerId id = new MCAppCheckerId();
		id.setCheckSeq(appCheckers.size());
		MCAppChecker expectedProductChecker = new MCAppChecker();
		expectedProductChecker.setId(id);
		expectedProductChecker.setCheckName("EXPECTED_PRODUCT_CHECK");
		expectedProductChecker.setChecker(ExpectedProductChecker.class.getCanonicalName());
		appCheckers.add(expectedProductChecker);
	}

	public String getCheckPointName() {
		return checkPointName;
	}

	public Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger();
		}
		return logger;
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public boolean dispatchReactions(List<CheckResult> checkResults, BaseCheckerData inputData) {
		for (CheckResult checkResult : checkResults) {
			ReactionType reactionType = checkResult.getReactionType();

			if (reactionType.equals(ReactionType.UNEXPECTED_PRODUCT)) {
				if (!checkResult.isResult()) {
					String product = inputData.getProductId();
					String expectedProduct = model.getExpectedProductId();
					Boolean expectedProductCheck = model.getExpectedProductManager()
							.isProductIdAheadOfExpectedProductId(expectedProduct, product);
					if (expectedProductCheck) {
						String msg = "The product id entered is not the expected product id.\n Do you want to advance to this product id? ";
						displayPopUpForUnexpectedProduct(true, msg);

					} else {
						String msg = "The product id entered is not the expected product id.\n Do you want to process this product id out of sequence? ";
						displayPopUpForUnexpectedProduct(true, msg);
					}
				}
			} else {
				return super.dispatchReactions(checkResults, inputData);
			}

		}
		return true;
	}

	private void displayPopUpForUnexpectedProduct(boolean showText, String msg) {
		final FxDialog dialogStage = new FxDialog("", ClientMainFx.getInstance().getStage());
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		BorderPane border = new BorderPane();
		GridPane pane = new GridPane();
		pane.setStyle("-fx-background-color: RED;");

		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);// padding
		
		// Add generic message to alert
		Label label = new Label(msg);
		label.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		pane.add(label, 0, 1);
		border.setCenter(pane);
		if (showText) {
			HBox hbox1 = new HBox();
			hbox1.setPadding(new Insets(15, 12, 15, 200));
			TextField text = new TextField();
			text.setPrefSize(200, 20);
			
		
			text.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					String val = ((TextField) event.getSource()).getText();
					getLogger().check("received:" + val);

					if (val.equalsIgnoreCase("YES")) {
						dialogStage.close();
					} else if (val.equalsIgnoreCase("NO")) {
						ProcessEvent processEvent = new ProcessEvent();
						processEvent.setProcessState(ProcessState.CANCELLED);
						EventBusUtil.publish(processEvent);
						dialogStage.close();
					}

				}
			});
			hbox1.getChildren().addAll(text);
			pane.add(hbox1, 0, 2);
		}

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 250));
		hbox.setSpacing(30);

		Button yesButton = new Button(" Yes ");
		yesButton.setPrefSize(100, 20);
		
		yesButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dialogStage.close();

			}
		});

		Button noButton = new Button(" No ");
		noButton.setPrefSize(100, 20);
		noButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				ProcessEvent event = new ProcessEvent();
				event.setProcessState(ProcessState.CANCELLED);
				EventBusUtil.publish(event);
				dialogStage.close();

			}
		});
		hbox.getChildren().addAll(yesButton, noButton);
		
		
		border.setBottom(hbox);

		Scene scene = new Scene(border, 700, 200);
		dialogStage.setScene(scene);
		dialogStage.showDialog();
	}

	public ProductModel getModel() {
		return this.model;
	}
}
