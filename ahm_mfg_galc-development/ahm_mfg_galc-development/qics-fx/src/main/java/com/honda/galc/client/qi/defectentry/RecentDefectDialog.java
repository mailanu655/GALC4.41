package com.honda.galc.client.qi.defectentry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.enumtype.ObservableListChangeEventType;
import com.honda.galc.client.qi.base.QiFxDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.event.ObservableListChangeEvent;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiRecentDefectDto;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiStationConfiguration;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * <h3>RecentDefectDialog Class description</h3>
 * <p> RecentDefectDialog description </p>
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
 * Nov 22, 2016
 *
 *
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class RecentDefectDialog extends QiFxDialog<DefectEntryModel>{

	private ObjectTablePane<QiRecentDefectDto> defectResultTablePane;
	private RecentDefectDialogController controller;
	private LoggedButton applyBtn;
	private LoggedButton cancelBtn;
	private LoggedLabel msgLabel;
	private LoggedLabel defectStatusLabel;
	private LoggedLabel writeUpDeptLabel;
	private String defectStatus;
	private String writeUpDept;
	private Set<Integer> selectedList = new HashSet<Integer>();

	public RecentDefectDialog(DefectEntryModel model, String applicationId, String defectStatus, String writeUpDept) {
		super("Recent Defects",applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.setController(new RecentDefectDialogController(model,this));
				
		this.defectStatus = defectStatus;
		this.writeUpDept = writeUpDept;
		
		initComponents();
		reload();
	}

	private BorderPane getRootBorderPane() {
		return (BorderPane) getScene().getRoot();
	}

	private void initComponents() {
		
		VBox outerPane = new VBox();
		outerPane.setPrefHeight(400);
		HBox buttonContainer = createButtonPane();
		HBox tableContainer = createRecentDefectTableBox();

		HBox msgBox = createMsgLabel();
		VBox defectStatusWriteUpDeptContainer = createDefectStatusWriteUpDeptContainer();
		outerPane.getChildren().addAll(tableContainer,defectStatusWriteUpDeptContainer,buttonContainer,msgBox);
		getRootBorderPane().setCenter(outerPane);
	}

	/**
	 * This method is used to create a Button HBox and all components inside it.
	 * @return
	 */
	private HBox createButtonPane() {
		HBox buttonContainer = new HBox();
		applyBtn = createBtn(QiConstant.ACCEPT, getController());
		cancelBtn = createBtn(QiConstant.CANCEL, getController());
		buttonContainer.setAlignment(Pos.CENTER_RIGHT);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(20);
		buttonContainer.getChildren().addAll(applyBtn, cancelBtn);
		return buttonContainer;
	}

	/**
	 * This method is used to create a validationResult HBox and all components inside it.
	 * @return
	 */
	private HBox createRecentDefectTableBox() {
		HBox validationResultTableContainer = new HBox();
		HBox TableBox = new HBox();
		defectResultTablePane = createRecentDefectTablePane();
		defectResultTablePane.setId("defectResultTablePane");
		TableBox.getChildren().addAll(defectResultTablePane);
		TitledPane responsibilityPane = new TitledPane("Recent Defects", TableBox);
		validationResultTableContainer.getChildren().addAll(responsibilityPane);
		return validationResultTableContainer;
	}

	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiRecentDefectDto> createRecentDefectTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Part Defect Comb Desc", "PartDefectDesc")
				.put("Responsibility", "ResponsibilityDesc")
				.put("Last used timestamp", "ActualTimestampForDisplay")
				.put("Defect Qty", "Count", Integer.class);

		Double[] columnWidth = new Double[] {0.25, 0.1, 0.1, 0.05};
		final ObjectTablePane<QiRecentDefectDto> panel = new ObjectTablePane<QiRecentDefectDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(true);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		panel.getTable().setRowFactory(new Callback<TableView<QiRecentDefectDto>, TableRow<QiRecentDefectDto>>() {
			public TableRow<QiRecentDefectDto> call(TableView<QiRecentDefectDto> tableView) {
				final TableRow<QiRecentDefectDto> row = new TableRow<QiRecentDefectDto>();
				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						if (event.getButton() == MouseButton.PRIMARY) {
							final int index = row.getIndex();
							if (index >= 0
									&& index < getDefectResultTablePane().getTable().getItems().size()
									&& getDefectResultTablePane().getTable().getSelectionModel().isSelected(index)) {
								selectedList.remove(index);
								  panel.getTable().getSelectionModel().clearSelection(index);
							event.consume();
							} else if(getDefectResultTablePane().getTable().getSelectionModel().isSelected(index) || 
									index < getDefectResultTablePane().getTable().getItems().size()) {
								selectedList.add(index);
							}
							EventBusUtil.publish(new ObservableListChangeEvent("", ObservableListChangeEventType.ADD));
							
						}
					}
				});
				return row;
			}
		});

		panel.getTable().getSelectionModel().selectedItemProperty().addListener( new ChangeListener<QiRecentDefectDto>() {

			@Override
			public void changed(ObservableValue<? extends QiRecentDefectDto> arg0,
					QiRecentDefectDto arg1, QiRecentDefectDto arg2) {
				if(arg0 != null && arg0.getValue() != null)
					Logger.getLogger().check("Selected Row : " + arg0.getValue().getPartDefectDesc());
			}
		} );
		return panel;
	}

	@Subscribe()
	public void onObjectTablePaneEvent(ObservableListChangeEvent event) {
		if (event == null)
			return;

		if (event.getEventType().equals(ObservableListChangeEventType.ADD)) {
			getDefectResultTablePane().getTable().getSelectionModel().clearSelection();
			for (Integer item : selectedList) {
				getDefectResultTablePane().getTable().getSelectionModel().select(item);
				Logger.getLogger().check("Defect Result Table row : " + item.intValue() + " selected");
			}
		}
	}
	
	/**
	 * This method is to create error label
	 * @return
	 */
	private HBox createMsgLabel() {
		HBox msgBox= new HBox();
		msgBox.setPadding(new Insets(10, 10, 10, 10));
		msgLabel = UiFactory.createLabel("messageLabel");
		msgLabel.setWrapText(true);
		msgLabel.setPrefWidth(400);
		msgLabel.setAlignment(Pos.CENTER);
		msgBox.getChildren().add(msgLabel);
		msgBox.setAlignment(Pos.CENTER);
		return msgBox;
	}
	
	private VBox createDefectStatusWriteUpDeptContainer() {
		VBox defectStatusWriteUpDeptContainer = new VBox();
		defectStatusWriteUpDeptContainer.setPadding(new Insets(20, 20, 20, 20));
		defectStatusLabel = UiFactory.createLabel("defectStatusLabel");
		defectStatusLabel.setWrapText(true);
		defectStatusLabel.setPrefWidth(400);
		defectStatusLabel.setAlignment(Pos.CENTER_LEFT);
		defectStatusLabel.getStyleClass().add("display-label-blue");
		defectStatusLabel.setPadding(new Insets(0, 0, 10, 0));
		defectStatusLabel.setText("Overwrite Defect Status to:                 " + defectStatus);
		
		writeUpDeptLabel = UiFactory.createLabel("writeUpDeptLabel");
		writeUpDeptLabel.setWrapText(true);
		writeUpDeptLabel.setPrefWidth(400);
		writeUpDeptLabel.setAlignment(Pos.CENTER_LEFT);
		writeUpDeptLabel.getStyleClass().add("display-label-blue");
		writeUpDeptLabel.setText("Overwrite Write Up Department to:   " + writeUpDept);
				
		defectStatusWriteUpDeptContainer.getChildren().addAll(defectStatusLabel, writeUpDeptLabel);
		defectStatusWriteUpDeptContainer.setAlignment(Pos.CENTER_LEFT);
		return defectStatusWriteUpDeptContainer;
	}

	public void reload(){
		List<QiDefectResult> dbResult = new ArrayList<QiDefectResult>();
		Integer recentDefectRange;
		QiStationConfiguration entryStation = getModel().findEntryStationById();
		if(null!=entryStation)
			recentDefectRange = Integer.parseInt(entryStation.getPropertyValue());
		else
			recentDefectRange = Integer.parseInt(QiEntryStationConfigurationSettings.DEFECT_LIST.getDefaultPropertyValue());
		List<QiRecentDefectDto> defectList = getController().getRecentDefectList(dbResult, recentDefectRange);
		defectResultTablePane.setData(defectList);
	}

	public RecentDefectDialogController getController() {
		return controller;
	}

	public void setController(RecentDefectDialogController controller) {
		this.controller = controller;
	}

	public LoggedButton getApplyBtn() {
		return applyBtn;
	}

	public void setApplyBtn(LoggedButton applyBtn) {
		this.applyBtn = applyBtn;
	}
	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

	public ObjectTablePane<QiRecentDefectDto> getDefectResultTablePane() {
		return defectResultTablePane;
	}

	public LoggedLabel getMsgLabel() {
		return msgLabel;
	}

	public void setMsgLabel(LoggedLabel msgLabel) {
		this.msgLabel = msgLabel;
	}

	public void setDefectResultTablePane(
			ObjectTablePane<QiRecentDefectDto> defectResultTablePane) {
		this.defectResultTablePane = defectResultTablePane;
	}

	public String getDefectStatus() {
		return defectStatus;
	}

	public String getWriteUpDept() {
		return writeUpDept;
	}
}
