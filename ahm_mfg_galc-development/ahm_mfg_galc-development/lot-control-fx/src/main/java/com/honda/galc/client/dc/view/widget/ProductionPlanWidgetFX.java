package com.honda.galc.client.dc.view.widget;

import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.service.ProductionMetricsService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class ProductionPlanWidgetFX extends AbstractWidget {

	private static final long serialVersionUID = 1L;
	private int numberOfRows;

	public ProductionPlanWidgetFX(ProductController productController) {
		super(ViewId.PRODUCTION_PLAN_WIDGETFX, productController);
	}

	@Override
	protected void processProductCancelled(ProductModel productModel) {
		// setExpectedProductId(productModel.getExpectedProductId());
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {

		this.setTop(getProductionMetrics());

	}

	@Override
	protected void initComponents() {
		

	}

	@SuppressWarnings("unchecked")
	private Node getProductionMetrics() {

		// get parameter from property
		PDDAPropertyBean property = PropertyService
				.getPropertyBean(PDDAPropertyBean.class);
		setPadding(new Insets(0, 10, 0, 10));
		String width = property.getProductionPlanWidgetWidth();
		String height = property.getProductionPlanWidgetHeighth();
		String trackingPpt = property.getProductionPlanTrackingProcessPoint();

		String appId = getProductController().getModel().getApplicationContext()
				.getApplicationId();

		String productType = getProductController().getModel().getProductType();

		List<Map<String, String>> metrics = getProductionMetrics(productType, trackingPpt);

		Map<String, String> shiftNumbers = metrics.get(0);
		int shiftActual = Integer.valueOf(shiftNumbers.get("actual"));
		int shiftPlan = Integer.valueOf(shiftNumbers.get("plan"));
		int shiftDeviation = shiftActual - shiftPlan;

		Map<String, String> quarterNumbers = metrics.get(1);
		int quarterActual = Integer.valueOf(quarterNumbers.get("actual"));
		int quarterPlan = Integer.valueOf(quarterNumbers.get("plan"));
		int quarterDeviation = quarterActual - quarterPlan;
	

		HBox hbox = new HBox(10);

		VBox currentQuarterVbox = new VBox();
		currentQuarterVbox.setId("blue-box");
		currentQuarterVbox.setPadding(new Insets(5, 0, 5, 0));
		currentQuarterVbox.setAlignment(Pos.TOP_CENTER);

		Label currentQuarterActualValue = UiFactory.createLabel("currentQuarterActualValue", quarterActual + "");

		Label currentQuarterActualLabel = UiFactory.createLabel("currentQuarterActualLabel", "  Qtr \n Actual");
		currentQuarterVbox.getChildren().addAll(currentQuarterActualValue,
				currentQuarterActualLabel);

		VBox quarterDeviationVbox = new VBox();
		quarterDeviationVbox.setAlignment(Pos.TOP_CENTER);
		if (quarterDeviation >= 0)
			quarterDeviationVbox.setId("green-box");
		else
			quarterDeviationVbox.setId("red-box");
		quarterDeviationVbox.setPadding(new Insets(5, 0, 5, 0));

		Label quarterDeviationValue = UiFactory.createLabel("quarterDeviationValue", quarterDeviation + "");

		Label quarterDeviationLabel = UiFactory.createLabel("quarterDeviationLabel", " Qtr \n Dev");
		quarterDeviationVbox.getChildren().addAll(quarterDeviationValue,
				quarterDeviationLabel);


		VBox currentShiftVbox = new VBox();
		currentShiftVbox.setAlignment(Pos.TOP_CENTER);
		currentShiftVbox.setId("blue-box");
		currentShiftVbox.setPadding(new Insets(5, 0, 5, 0));

		Label currentShiftActualValue = UiFactory.createLabel("currentShiftActualValue", shiftActual + "");

		
		Label currentShiftActualLabel = UiFactory.createLabel("currentShiftActualLabel", "  Shift \n Actual");
		currentShiftVbox.getChildren().addAll(currentShiftActualValue,
				currentShiftActualLabel);

		VBox shiftDeviationVbox = new VBox();
		shiftDeviationVbox.setAlignment(Pos.TOP_CENTER);
		if (shiftDeviation >= 0)
			shiftDeviationVbox.setId("green-box");
		else
			shiftDeviationVbox.setId("red-box");
		shiftDeviationVbox.setPadding(new Insets(5, 0, 5, 0));

		Label shiftDeviationValue = UiFactory.createLabel("shiftDeviationValue", shiftDeviation + "");

		Label shiftDeviationLabel = UiFactory.createLabel("shiftDeviationLabel", " Shift \n  Dev");
		shiftDeviationLabel.setTextFill(Color.GREEN);
		shiftDeviationVbox.getChildren().addAll(shiftDeviationValue,
				shiftDeviationLabel);


		hbox.setPrefWidth(Integer.parseInt(width));
		hbox.setMinWidth(Integer.parseInt(width));
		hbox.setPrefHeight(Integer.parseInt(height));
		hbox.setMinHeight(Integer.parseInt(height));

		
		VBox outerBox = new VBox();
		outerBox.setId("prod-plan-widget");
		Label title = UiFactory.createLabel("productionResults", "Production  Results");
		outerBox.setAlignment(Pos.TOP_CENTER);
		outerBox.getChildren().addAll(title, hbox);
	

		hbox.getStylesheets().add("resource/com/honda/galc/client/dc/view/ProductionPlanWidgetFX.css");
		hbox.setHgrow(currentQuarterVbox, Priority.ALWAYS);
		hbox.setHgrow(quarterDeviationVbox, Priority.ALWAYS);
		hbox.setHgrow(currentShiftVbox, Priority.ALWAYS);
		hbox.setHgrow(shiftDeviationVbox, Priority.ALWAYS);
		
		double maxWidth = Integer.parseInt(width)/4;
		currentQuarterVbox.setMinWidth(maxWidth);
		quarterDeviationVbox.setMinWidth(maxWidth);
		currentShiftVbox.setMinWidth(maxWidth);
		shiftDeviationVbox.setMinWidth(maxWidth);

		hbox.getChildren().addAll(currentQuarterVbox, quarterDeviationVbox,
				currentShiftVbox, shiftDeviationVbox);
		outerBox.setPadding(new Insets(0, 10, 10, 10));
		return outerBox;

	}

	private List<Map<String, String>> getProductionMetrics(String productType, String trackingPpt) {

		List<Map<String, String>> metrics = ServiceFactory.getService(
				ProductionMetricsService.class).getProductionMetrics(
				productType, trackingPpt);

		return metrics;
	}

}
