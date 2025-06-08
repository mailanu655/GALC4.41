package com.honda.galc.client.product.entry;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.pane.SearchByProcessPane;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.ClientStartUpProgress;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.dto.SearchByProcessDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.ProductHistory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.StringConverter;

public class SearchByProcessController implements EventHandler<ActionEvent> {
	private SearchByProcessPane view;
	private SearchByProcessModel model;

	public SearchByProcessController(SearchByProcessPane view, ApplicationContext applicationContext) {
		this.view = view;
		this.model = new SearchByProcessModel(applicationContext);
		initEventHandlers();
		initData();
	}

	private void initEventHandlers() {
		addDepartmentComboBoxListener();
		addProcessPointComboBoxListener();
		view.getSearchButton().setOnAction(this);
	}

	private void initData() {
		view.getDepartmentComboBox().getControl().setConverter(qiDivisionConverter());
		view.getProcessPointComboBox().getControl().setConverter(qiProcessPointConverter());
		view.getDepartmentComboBox().setItems(FXCollections.observableArrayList(model.getDepartments()));
	}

	private void addProcessPointComboBoxListener() {
		view.getProcessPointComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProcessPoint>() {
			@Override
			public void changed(
					ObservableValue<? extends ProcessPoint> arg0,
					ProcessPoint oldValue, ProcessPoint newValue) {
				if(newValue != null) {
					
					view.getMachineComboBox().getControl().getItems().clear();
					view.getSearchButton().setDisable(false);
				    view.getMachineComboBox().getControl().getItems().add(null);
	                view.getMachineComboBox().getControl().getItems().addAll(FXCollections.observableArrayList(model.getMachines(newValue.getProcessPointId())));				}
			}
		});
	}

	private void addDepartmentComboBoxListener() {
		view.getDepartmentComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Division>() {
			@Override
			public void changed(
					ObservableValue<? extends Division> arg0,
					Division oldValue, Division newValue) {
				view.getSearchButton().setDisable(true);
				view.getProcessPointComboBox().getControl().getItems().clear();
				view.getMachineComboBox().getControl().getItems().clear();
				if(newValue != null)
					view.getProcessPointComboBox().setItems(FXCollections.observableArrayList(model.getProcessPoints(newValue.getDivisionId())));
			}
		});
	}
	
	private boolean validateTimeRange(Timestamp startTime, Timestamp endTime) {
		long timeRange = endTime.getTime() - startTime.getTime();
		int timeRangeLimit = model.getProductSearchTimeRangeLimit();
		if(timeRange <= 0) {
			EventBusUtil.publish(new ProgressEvent(0,"Hide"));
			MessageDialog.showInfo(view.getProductController().getView().getMainWindow().getStage(),
					"End time must occur after start time.");
			return false;
		}
		timeRange = timeRange / (60 * 60 * 1000);
		if(timeRange > timeRangeLimit) {
			EventBusUtil.publish(new ProgressEvent(0,"Hide"));
			MessageDialog.showInfo(view.getProductController().getView().getMainWindow().getStage(),
					"Time range of " + timeRange + " hours is greater than the allowed " + timeRangeLimit + " hours");
			return false;
		}
		
		return true;
	}

	private void searchForProducts() {
		QiProgressBar qiProgressBar = null;
		try {
			qiProgressBar = QiProgressBar.getInstance("Start searching...", "Start searching... : ",view.getProcessPointComboBox().getControl().getSelectionModel().getSelectedItem().getProcessPointId(),view.getProductController().getView().getMainWindow().getStage(),true);	
			qiProgressBar.showMe();
			List<ProductHistory> productHistory;
			Timestamp startTime = Timestamp.valueOf(view.getStartDatePicker().getDateTimeValue());
			Timestamp endTime = Timestamp.valueOf(view.getEndDatePicker().getDateTimeValue());
			if(validateTimeRange(startTime, endTime)) {
				String processPointId = view.getProcessPointComboBox().getControl().getSelectionModel().getSelectedItem().getProcessPointId();
				if(view.getMachineComboBox().getControl().getSelectionModel().getSelectedIndex() == -1) {
					productHistory = model.getProductHistoyByDateRangeAndProcessPoint(processPointId, startTime, endTime);
				} else {
					String machine = view.getMachineComboBox().getControl().getSelectionModel().getSelectedItem();
					productHistory = model.getProductHistoyByDateRangeAndProcessPoint(processPointId, machine, startTime, endTime);
				}

				if(productHistory.isEmpty()) {
					MessageDialog.showInfo(view.getProductController().getView().getMainWindow().getStage(), "No records found that match the search criteria");
				} else {
					Set<SearchByProcessDto> products = new HashSet<SearchByProcessDto>();
					for(ProductHistory currentProductHistory : productHistory) {
						SearchByProcessDto dto = new SearchByProcessDto();
						dto.setProcessPointId(view.getProcessPointComboBox().getControl().getSelectionModel().getSelectedItem().getProcessPointName());
						dto.setProductId(currentProductHistory.getProductId());
						dto.setTimestamp(currentProductHistory.getActualTimestamp());
						products.add(dto);
					}
					EventBusUtil.publishAndWait(new ProductEvent(new ArrayList<SearchByProcessDto>(products), ProductEventType.PRODUCT_INPUT_RECIEVED));
				}
			}
		}finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
	}

	private StringConverter<Division> qiDivisionConverter() {
		return new StringConverter<Division>() {
			@Override
			public Division fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(Division arg0) {
				if (arg0 == null) {
					return null;
				} else {
					return arg0.getDivisionId() + "-" + arg0.getDivisionName();
				}
			}
		};
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
					return arg0.getProcessPointId() + "-" + arg0.getProcessPointName();
				}
			}
		};
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() instanceof LoggedButton) {
			if(QiConstant.SEARCH.equalsIgnoreCase(view.getSearchButton().getText())) searchForProducts();
		}	
	}
}
