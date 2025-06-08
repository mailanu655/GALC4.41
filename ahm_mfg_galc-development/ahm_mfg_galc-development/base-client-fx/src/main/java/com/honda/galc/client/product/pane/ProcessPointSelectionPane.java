package com.honda.galc.client.product.pane;

import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.product.mvc.ProcessPointSelectionPaneController;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedLabeledComboBox;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ProcessPointSelectionPane extends AbstractGenericEntryPane{
	private ProcessPointSelectionPaneController controller;

	private Pane selectionPane;

	private LabeledComboBox<ProcessPoint> processPointComboBox;
	private LabeledComboBox<Line> lineComboBox;
	private LabeledComboBox<Division> departmentComboBox;
	private LabeledComboBox<String> machineComboBox;

	private boolean isComboBoxHorizontal;
	private boolean isLabelHorizontal;
	private boolean isKickoutFilter;
	
	public ProcessPointSelectionPane(Pane parentView, boolean isShowDeviceId, boolean isComboBoxHorizontal,
			boolean isLabelHorizontal, boolean isKickoutFilter) {
		super(PaneId.PROCESS_POINT_SELECT_PANE);
		this.isComboBoxHorizontal = isComboBoxHorizontal;
		this.isLabelHorizontal = isLabelHorizontal;
		this.isKickoutFilter = isKickoutFilter;
		controller = new ProcessPointSelectionPaneController(this);
		controller.setShowDeviceId(isShowDeviceId);
		init();
		controller.init();
	}
	
	public ProcessPointSelectionPane(TabPane parentView, boolean isShowDeviceId, boolean isComboBoxHorizontal,
			boolean isLabelHorizontal, boolean isKickoutFilter) {
		super(PaneId.PROCESS_POINT_SELECT_PANE);
		this.isComboBoxHorizontal = isComboBoxHorizontal;
		this.isLabelHorizontal = isLabelHorizontal;
		this.isKickoutFilter = isKickoutFilter;
		controller = new ProcessPointSelectionPaneController(this);
		controller.setShowDeviceId(isShowDeviceId);
		init();
		controller.init();
	}

	public ProcessPointSelectionPane(AbstractGenericEntryPane parentView, PaneId paneId) {
		super(paneId);
		this.isComboBoxHorizontal = true;
		this.isLabelHorizontal = false;
		this.isKickoutFilter = false;
		controller = new ProcessPointSelectionPaneController(this);
		controller.setShowDeviceId(true);
		init();
		controller.init();
	}
	
	private void init() {
		this.getChildren().add(createSelectionPane());
	}

	private Pane createSelectionPane() {
		if(isComboBoxHorizontal) {
			selectionPane = new HBox();
		} else {
			selectionPane = new VBox();
		}
		selectionPane.prefWidthProperty().bind(width);

		selectionPane.getChildren().addAll(createDepartmentComboBox(), createLineComboBox(), createProcessPointComboBox());

		if(controller.isShowDeviceId()) {		
			selectionPane.getChildren().add(createMachineComboBox());
		}
		return selectionPane;
	}

	private LabeledComboBox<Division> createDepartmentComboBox() {
		departmentComboBox = new LabeledComboBox<Division>("Department", isLabelHorizontal,new Insets(0, 9, 0, 0), true, false);
		if(isComboBoxHorizontal) {
			if(isLabelHorizontal) {
				departmentComboBox.getLabel().fontProperty().bind(hfont);
				departmentComboBox.getControl().styleProperty().bind(hComboBoxSyle);
				departmentComboBox.getLabel().setPadding(new Insets(0, 10, 0, 0));
			} else {
				departmentComboBox.getLabel().fontProperty().bind(hComboBoxVLabelFont);
				departmentComboBox.getControl().styleProperty().bind(hCbVLableComboBoxStyle);
			}
			departmentComboBox.getLabel().setAlignment(Pos.CENTER_RIGHT);
			departmentComboBox.getControl().prefWidthProperty().bind(widthProperty());
			departmentComboBox.getControl().maxWidthProperty().bind(widthProperty().multiply(0.75));
			departmentComboBox.getControl().prefHeightProperty().bind(heightProperty().multiply(0.70));
		} else {
			if(isLabelHorizontal) {
				departmentComboBox.getControl().minHeightProperty().bind(heightProperty().multiply(0.27));
				departmentComboBox.getControl().prefHeightProperty().bind(heightProperty().multiply(0.31));
				departmentComboBox.getControl().maxWidthProperty().bind(widthProperty().multiply(0.55));
				departmentComboBox.getLabel().fontProperty().bind(vComboBoxHLabelFont);
				departmentComboBox.getControl().styleProperty().bind(vCbHLableComboBoxStyle);
			}
		}
		departmentComboBox.getLabel().setAlignment(Pos.CENTER_RIGHT);
		departmentComboBox.getControl().prefWidthProperty().bind(widthProperty());
		return departmentComboBox;
	}

	private LabeledComboBox<Line> createLineComboBox() {
		lineComboBox = new LabeledComboBox<Line>("Line", isLabelHorizontal, new Insets(0, 9, 0, 0), true, false);
		if(isComboBoxHorizontal) {
			if(isLabelHorizontal) {
				lineComboBox.getLabel().fontProperty().bind(hfont);
				lineComboBox.getControl().styleProperty().bind(hComboBoxSyle);
				lineComboBox.getLabel().setPadding(new Insets(0, 10, 0, 0));
			} else {
				lineComboBox.getLabel().fontProperty().bind(hComboBoxVLabelFont);
				lineComboBox.getControl().styleProperty().bind(hCbVLableComboBoxStyle);
			}
			lineComboBox.getLabel().setAlignment(Pos.CENTER_RIGHT);
			lineComboBox.getControl().prefWidthProperty().bind(widthProperty());
			lineComboBox.getControl().maxWidthProperty().bind(widthProperty().multiply(0.75));
			lineComboBox.getControl().prefHeightProperty().bind(heightProperty().multiply(0.70));
		} else {
			if(isLabelHorizontal) {
				lineComboBox.getControl().minHeightProperty().bind(heightProperty().multiply(0.27));
				lineComboBox.getControl().prefHeightProperty().bind(heightProperty().multiply(0.31));
				lineComboBox.getControl().maxWidthProperty().bind(widthProperty().multiply(0.55));
				lineComboBox.getLabel().fontProperty().bind(vComboBoxHLabelFont);
				lineComboBox.getControl().styleProperty().bind(vCbHLableComboBoxStyle);
			}
		}
		lineComboBox.getLabel().setAlignment(Pos.CENTER_RIGHT);
		lineComboBox.getControl().prefWidthProperty().bind(widthProperty());
		return lineComboBox;
	}

	private LabeledComboBox<ProcessPoint> createProcessPointComboBox() {
		processPointComboBox = new LabeledComboBox<ProcessPoint>("Process Point", isLabelHorizontal, new Insets(0, 9, 0, 0), true, false);
		if(isComboBoxHorizontal) {
			if(isLabelHorizontal) {
				processPointComboBox.getLabel().fontProperty().bind(hfont);
				processPointComboBox.getControl().styleProperty().bind(hComboBoxSyle);
				processPointComboBox.getLabel().setPadding(new Insets(0, 10, 0, 0));
			} else {
				processPointComboBox.getLabel().fontProperty().bind(hComboBoxVLabelFont);
				processPointComboBox.getControl().styleProperty().bind(hCbVLableComboBoxStyle);
			}
			processPointComboBox.getLabel().setAlignment(Pos.CENTER_RIGHT);
			processPointComboBox.getControl().prefWidthProperty().bind(widthProperty());
			processPointComboBox.getControl().maxWidthProperty().bind(widthProperty().multiply(0.75));
			processPointComboBox.getControl().prefHeightProperty().bind(heightProperty().multiply(0.70));
		} else {
			if(isLabelHorizontal) {
				processPointComboBox.getControl().minHeightProperty().bind(heightProperty().multiply(0.27));
				processPointComboBox.getControl().prefHeightProperty().bind(heightProperty().multiply(0.31));
				processPointComboBox.getControl().maxWidthProperty().bind(widthProperty().multiply(0.55));
				processPointComboBox.getLabel().fontProperty().bind(vComboBoxHLabelFont);
				processPointComboBox.getControl().styleProperty().bind(vCbHLableComboBoxStyle);
			}
		}
		processPointComboBox.getLabel().setAlignment(Pos.CENTER_RIGHT);
		processPointComboBox.getControl().prefWidthProperty().bind(widthProperty());
		return processPointComboBox;
	}

	private LabeledComboBox<String> createMachineComboBox() {
		if(controller.isShowDeviceId()) {
			machineComboBox = new LabeledComboBox<String>("Machine", isLabelHorizontal, new Insets(0, 9, 0, 0), true, false);
			if(isComboBoxHorizontal) {
				if(isLabelHorizontal) {
					machineComboBox.getLabel().fontProperty().bind(hfont);
					machineComboBox.getControl().styleProperty().bind(hComboBoxSyle);
					machineComboBox.getLabel().setPadding(new Insets(0, 10, 0, 0));
				} else {
					machineComboBox.getLabel().fontProperty().bind(hComboBoxVLabelFont);
					machineComboBox.getControl().styleProperty().bind(hCbVLableComboBoxStyle);
				}
				machineComboBox.getLabel().setAlignment(Pos.CENTER_RIGHT);
				machineComboBox.getControl().prefWidthProperty().bind(widthProperty().multiply(0.55));
				machineComboBox.getControl().maxWidthProperty().bind(widthProperty().multiply(0.55));
				machineComboBox.getControl().prefHeightProperty().bind(heightProperty().multiply(0.70));
			} else {
				if(isLabelHorizontal) {
					machineComboBox.getControl().minHeightProperty().bind(heightProperty().multiply(0.35));
					machineComboBox.getControl().maxWidthProperty().bind(widthProperty().multiply(0.55));
					machineComboBox.getLabel().fontProperty().bind(vComboBoxHLabelFont);
					machineComboBox.getControl().styleProperty().bind(vCbHLableComboBoxStyle);
				}
			}
			machineComboBox.getLabel().setAlignment(Pos.CENTER_RIGHT);
			machineComboBox.getControl().prefWidthProperty().bind(widthProperty());
		}
		return machineComboBox;
	}

	public LabeledComboBox<ProcessPoint> getProcessPointComboBox() {
		return this.processPointComboBox;
	}

	public LabeledComboBox<Line> getLineComboBox() {
		return this.lineComboBox;
	}

	public LabeledComboBox<Division> getDepartmentComboBox() {
		return this.departmentComboBox;
	}

	public LabeledComboBox<String> getMachineComboBox() {
		return machineComboBox;
	}

	public void setMachineComboBox(LoggedLabeledComboBox<String> machineComboBox) {
		this.machineComboBox = machineComboBox;
	}

	@Override
	public String getPaneLabel() {
		return "Process Point Selection";
	}
	
	public boolean isKickoutFilter() {
		return isKickoutFilter;
	}

	public void setKickoutFilter(boolean isKickoutFilter) {
		this.isKickoutFilter = isKickoutFilter;
	}
}
