package com.honda.galc.client.product.entry;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.pane.SearchByProductFilterPane;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.ClientStartUpProgress;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.property.DefaultQiProductPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.MultiLineHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputControl;
import javafx.util.StringConverter;

public class SearchByProductFilterController implements EventHandler<ActionEvent> {
	private SearchByProductFilterPane view;
	private SearchByFilterModel model;
	private String currentApplicationId = "";
	private ProductController productController;
	private MultiLineHelper helper;
	DefaultQiProductPropertyBean qiProp;

	public SearchByProductFilterController(SearchByProductFilterPane searchByProductFilterPane, ApplicationContext applicationContext, ProductController productController) {
		this.view = searchByProductFilterPane;
		this.model = SearchByFilterModel.getInstance(productController.getProductTypeData());
		this.productController = productController;
		currentApplicationId = applicationContext.getApplicationId();
		helper = MultiLineHelper.getInstance(currentApplicationId);
		qiProp = PropertyService.getPropertyBean(DefaultQiProductPropertyBean.class, applicationContext.getProcessPointId());
		initEventHandlers();
		initData();
	}

	private void initEventHandlers() {
		addProcessPointComboBoxListener();
		addTrackingStatusListener();
		view.getSearchBtn().setOnAction(this);
	}

	private void initData() {
		ComboBox<ProcessPoint> ppCombo = view.getProcessPointComboBox();
		ppCombo.setConverter(qiProcessPointConverter());
		ppCombo.getItems().addAll(helper.getAllProcessPoints());
		ppCombo.getSelectionModel().select(0);
		view.getSearchGroup().selectToggle(view.getProdLotRdBtn());
		if(!ProductType.FRAME.equals(productController.getProductTypeData().getProductType()))  {
			view.getVboxSeqRange().setVisible(false);
		}
		if(!ProductType.FRAME.equals(productController.getProductTypeData().getProductType()) && helper.isMultiLine())  {
			view.getVboxProductIdRange().setVisible(false);
		}
	}

	private void addProcessPointComboBoxListener() {
		view.getProcessPointComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProcessPoint>() {
			@Override
			public void changed(
					ObservableValue<? extends ProcessPoint> arg0,
					ProcessPoint oldValue, ProcessPoint newValue) {
				if(newValue != null) {					
					view.getSearchBtn().setDisable(false);
					List<Line> trackingLines=getModel().findAllTrackingStatusByPlant(newValue);
					view.getTrackingStatusComboBox().getItems().clear();
					view.getTrackingStatusComboBox().getItems().addAll(trackingLines);
				}
			}
		});
	}

	private void addTrackingStatusListener() {
		view.getTrackingStatusComboBox().getSelectionModel().selectedItemProperty().addListener(
				(arg0,ov,nv) -> {
					if(nv != null)  {
						view.getSearchGroup().selectToggle(view.getTrackStsRdBtn());
						view.getSearchBtn().setDisable(false);
					}
				}
		);
	}


	private void searchForProducts() {
		QiProgressBar qiProgressBar = null;
		QiCommonUtil.publishClear();
		boolean isValidInput = true;
		List<String> productIds = new ArrayList<String>();
		String plant = view.getProcessPointComboBox().getSelectionModel().getSelectedItem().getPlantName();
		try {
			qiProgressBar = QiProgressBar.getInstance("Start searching...", "Start searching... ",plant,productController.getView().getMainWindow().getStage(),true);	
			qiProgressBar.showMe();
			if (view.getProdLotRdBtn().isSelected()) {
				String prodLot = view.getProdLotTextField().getText();
				if (!validate(prodLot)) {
					focusAndselectText(view.getProdLotTextField());
					return;
				} else {
					productIds = getModel().findProductIdsByProductionLot(prodLot, qiProp.getMaxProductsFetch());
				}
			} else if (view.getSeqRangeRdBtn().isSelected()) {
				String start = view.getSeqRangeTxtField1().getText();
				String end = view.getSeqRangeTxtField2().getText();
				if (!isNumeric(start)) {
					focusAndselectText(view.getSeqRangeTxtField1());
					return;
				} else if (!isNumeric(end)) {
					focusAndselectText(view.getSeqRangeTxtField2());
					return;
				} else {
					if (helper.isMultiLine()) {
						productIds = getModel().findProductIdsBySeqRange(plant, start, end, qiProp.getMaxProductsFetch());
					} else {
						productIds = getModel().findProductIdsBySeqRange("", start, end, qiProp.getMaxProductsFetch());
					}
				}
			}

			else if (view.getProductIdRangeRdBtn().isSelected()) {
				String start = view.getProductIdRangeTxt1().getText();
				String end = view.getProductIdRangeTxt2().getText();
				if (!validate(start)) {
					focusAndselectText(view.getProductIdRangeTxt1());
					return;
				} else if (!validate(end)) {
					focusAndselectText(view.getProductIdRangeTxt2());
					return;
				} else {
					if (helper.isMultiLine()) {
						productIds = getModel().findProductIdsByProductIdRange(plant, start, end, qiProp.getMaxProductsFetch());
					} else {
						productIds = getModel().findProductIdsByProductIdRange("", start, end, qiProp.getMaxProductsFetch());
					}
				}
			}

			else if (view.getTrackStsRdBtn().isSelected()) {
				isValidInput = (view.getTrackingStatusComboBox().getSelectionModel().getSelectedItem() != null);
				if (!isValidInput) {
					QiCommonUtil.publishError("Please select tracking status");
				} else {
					String trStatus = view.getTrackingStatusComboBox().getSelectionModel().getSelectedItem()
							.getLineId();
					productIds = getModel().findProductIdsByTrackingStatus(trStatus, qiProp.getMaxProductsFetch());
				}
			}
			if (productIds == null || productIds.isEmpty()) {
				QiCommonUtil.publishError("No match for search criteria");
			} else {
				EventBusUtil.publishAndWait(new ProductEvent(productIds, ProductEventType.PRODUCT_INPUT_RECIEVED));
			} 
		} finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
	}	

	private void focusAndselectText(TextInputControl textField)  {
		if(textField != null)  {
			textField.requestFocus();
			textField.selectAll();
		}
		
	}
	public boolean validate(String inputString)  {
		if(StringUtils.isBlank(inputString) || QiCommonUtil.hasSpecialCharacters(inputString))  {
			QiCommonUtil.publishError("Please enter alpha numeric characters only");
			return false;
		}
		return true;
	}
	
	public boolean isNumeric(String inputString)  {
		if(StringUtils.isBlank(inputString) || !QiCommonUtil.isNumericInput(inputString))  {
			QiCommonUtil.publishError("Please enter numeric value only");
			return false;
		}
		return true;
	}
	
	private StringConverter<ProcessPoint> qiProcessPointConverter() {
		return new StringConverter<ProcessPoint>() {

			@Override
			public ProcessPoint fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(ProcessPoint arg0) {
				if (arg0 == null) {
					return null;
				} else {
					return arg0.getPlantName();
				}
			}
		};
	}

	/**
	 * @return the model
	 */
	public SearchByFilterModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(SearchByFilterModel model) {
		this.model = model;
	}

	private void setOnTaskExit(Task<?> task, EventHandler<WorkerStateEvent> e) {
		task.setOnSucceeded(e);
		task.setOnFailed(e);
		task.setOnCancelled(e);
	}
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == view.getSearchBtn()) {
			searchForProducts();
			view.getScene().setCursor(Cursor.DEFAULT);
		}	
	}
}
