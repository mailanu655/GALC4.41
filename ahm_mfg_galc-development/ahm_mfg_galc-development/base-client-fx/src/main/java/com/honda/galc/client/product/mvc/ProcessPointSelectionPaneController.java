package com.honda.galc.client.product.mvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.product.pane.ProcessPointSelectionPane;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.ResetEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.client.ui.event.SelectionEventType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.util.StringConverter;

public class ProcessPointSelectionPaneController {
	private ProcessPointSelectionPane view;
	private ProcessPointSelectionPaneModel model;
	private List<ProcessPoint> kickoutProcessPoints = new ArrayList<ProcessPoint>();

	private boolean isShowDeviceId = false;

	public ProcessPointSelectionPaneController(ProcessPointSelectionPane view) {
		this.view = view;
		model = new ProcessPointSelectionPaneModel();
		EventBusUtil.register(this);
	}

	public void init() {
		initData();
		initListeners();
	}

	private void initData() {
		view.getDepartmentComboBox().getControl().setConverter(divisionConverter());
		view.getLineComboBox().getControl().setConverter(lineConverter());
		view.getProcessPointComboBox().getControl().setConverter(processPointConverter());
		view.getDepartmentComboBox().setItems(FXCollections.observableArrayList(getDivisions()));
	}

	private List<Division> getDivisions() {
		if(!view.isKickoutFilter()) {
			return model.getDivisions();
		}
		Set<String> divisionIds = new HashSet<String>();
		kickoutProcessPoints = model.getProcessPointsWithKickout(null);
		for(ProcessPoint pp : kickoutProcessPoints) {
			divisionIds.add(pp.getDivisionId());
		}
		return model.getDivisions(new ArrayList<String>(divisionIds));
	}
	
	private void initListeners() {
		initDepartmentComboBoxListener();
		initLineComboBoxListener();
		initProcessPointComboBoxListener();
		if(isShowDeviceId) {
			initDeviceIdComboBoxListener();
		}
	}

	private void initDepartmentComboBoxListener() {
		view.getDepartmentComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Division>() {
			@Override
			public void changed(
					ObservableValue<? extends Division> arg0,
					Division oldValue, Division newValue) {
				view.getProcessPointComboBox().getControl().getItems().clear();
				view.getLineComboBox().getControl().getItems().clear();
				if(newValue != null) {
					List<Line> lines = new ArrayList<Line>();
					if(view.isKickoutFilter()) {
						Set<String> kickoutLines = new HashSet<String>();
						for(ProcessPoint pp : kickoutProcessPoints) {
							if(pp.getDivisionId().equalsIgnoreCase(newValue.getDivisionId())) {
								kickoutLines.add(pp.getLineId());
							}
						}
						lines = model.getLines(new ArrayList<String>(kickoutLines));
					} else {
						lines = model.getLines(newValue);
					}
					view.getLineComboBox().getControl().setItems(FXCollections.observableArrayList(lines));
				}
				if(isShowDeviceId) {
					view.getMachineComboBox().getControl().getItems().clear();
				}
				EventBusUtil.publish(new SelectionEvent(view.getPaneId(), newValue, SelectionEventType.DIVISION));
			}
		});		
	}

	private void initLineComboBoxListener() {
		view.getLineComboBox().getControl().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Line>() {
					@Override
					public void changed(ObservableValue<? extends Line> arg0, Line oldValue, Line newValue) {
						if (newValue == null)
							return;
						view.getProcessPointComboBox().getControl().getItems().clear();
						view.getProcessPointComboBox()
								.setItems(FXCollections.observableArrayList(
										view.isKickoutFilter() ? model.getProcessPointsWithKickout(newValue)
												: model.getProcessPoints(newValue)));
						if (isShowDeviceId) {
							view.getMachineComboBox().getControl().getItems().clear();
						}
						EventBusUtil.publish(new SelectionEvent(view.getPaneId(), newValue, SelectionEventType.LINE));
					}
				});
	}

	private void initProcessPointComboBoxListener() {
		view.getProcessPointComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProcessPoint>() {
			@Override
			public void changed(
					ObservableValue<? extends ProcessPoint> arg0,
					ProcessPoint oldValue, ProcessPoint newValue) {
				if(newValue == null) return;
				if(isShowDeviceId) {
					view.getMachineComboBox().getControl().getItems().clear();
				    view.getMachineComboBox().getControl().getItems().add(null);
	                view.getMachineComboBox().getControl().getItems().addAll(FXCollections.observableArrayList(model.getDeviceIdsByProcessPoint(newValue.getProcessPointId())));
				} 
				EventBusUtil.publish(new SelectionEvent(view.getPaneId(), newValue, SelectionEventType.PROCESS_POINT));
			}
		});	
	}

	private void initDeviceIdComboBoxListener() {
		if(isShowDeviceId()) { 
			view.getMachineComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(
						ObservableValue<? extends String> arg0,
						String oldValue, String newValue) {
					if(newValue == null) return;
					EventBusUtil.publish(new SelectionEvent(view.getPaneId(), newValue, SelectionEventType.DEVICE));

				}
			});	
		}
	}

	private StringConverter<Division> divisionConverter() {
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

	private StringConverter<Line> lineConverter() {
		return new StringConverter<Line>() {

			@Override
			public Line fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(Line arg0) {
				if (arg0 == null) {
					return null;
				} else {
					return arg0.getLineId() + "-" + arg0.getLineName();
				}
			}
		};
	}

	private StringConverter<ProcessPoint> processPointConverter() {
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

	@Subscribe
	public void onResetEvent(ResetEvent event) {
		if(event.getFXViewId() instanceof ViewId) {
			if(ViewId.DEFECT_ENTRY == event.getFXViewId()) {
				reset();
			}
		}
	}

	private void reset() {
		if(isShowDeviceId) {
			view.getMachineComboBox().getControl().getItems().clear();
		}
		view.getProcessPointComboBox().getControl().getItems().clear();
		view.getLineComboBox().getControl().getItems().clear();
		view.getDepartmentComboBox().setSelectedIndex(-1);
	}

	public boolean isShowDeviceId() {
		return this.isShowDeviceId;
	}

	public void setShowDeviceId(boolean isShowDeviceId) {
		this.isShowDeviceId = isShowDeviceId;
	}
}
