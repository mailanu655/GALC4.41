package com.honda.galc.client.teamlead.mdrs;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.honda.galc.client.dto.GALCPeriodDTO;
import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.dto.MCRevisionDTO;
import com.honda.galc.client.dto.MdrsQuarterDTO;
import com.honda.galc.client.dto.Selectable;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.dao.conf.MCMdrsManpowerAssignmentDao;
import com.honda.galc.dao.conf.MCMdrsPeriodDao;
import com.honda.galc.dao.conf.MCProcessAssignmentDao;
import com.honda.galc.dao.conf.MCRevisionDao;
import com.honda.galc.dao.mdrs.ManpowerAssignmentDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.conf.MCMdrsPeriod;
import com.honda.galc.entity.conf.MCMdrsPeriodId;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.ServiceFactory;

public class MdrsQuarterMappingPane extends ApplicationMainPane {
/*
	@FXML
	private ChoiceBox<String> deptChoiceBox;
	*/
	@FXML
	private TableView<MdrsQuarterDTO> mdrsQuartersTable;
	@FXML
	private TableColumn<MdrsQuarterDTO, String> mdrsShiftColumn;
	@FXML
	private TableColumn<MdrsQuarterDTO, String> mdrsQuarterColumn;
	@FXML
	private TableColumn<MdrsQuarterDTO, String> mdrsLineColumn;
	@FXML
	private TableColumn<MdrsQuarterDTO, String> mdrsDeptColumn;
	@FXML
	private TableColumn<MdrsQuarterDTO, CheckBox> mdrsQuarterSelectColumn;

	@FXML
	private TableView<GALCPeriodDTO> galcPeriodsTable;
	@FXML
	private TableColumn<GALCPeriodDTO, String> galcDeptColumn;
	@FXML
	private TableColumn<GALCPeriodDTO, String> galcShiftColumn;
	@FXML
	private TableColumn<GALCPeriodDTO, String> galcPeriodColumn;
	@FXML
	private TableColumn<GALCPeriodDTO, String> galcLineColumn;
	@FXML
	private TableColumn<GALCPeriodDTO, String> galcPeriodLabelColumn;
	@FXML
	private TableColumn<GALCPeriodDTO, CheckBox> galcPeriodSelectColumn;

	@FXML
	private Button mapButton;
	
	@FXML
    private AnchorPane outerPane;
	
	@FXML
	private ScrollPane scrollPane;


	MdrsQuarterTable mdrsQuarterTable;
	GALCPeriodTable galcPeriodTable;

	List<MdrsQuarterDTO> selectedMdrsQuarterList = new ArrayList<MdrsQuarterDTO>();
	List<GALCPeriodDTO> selectedGALCPeriodList = new ArrayList<GALCPeriodDTO>();

	public MdrsQuarterMappingPane(MainWindow window) {
		super(window, true);

	}

	@FXML
	void initialize() {

		
	//	deptChoiceBox.setItems(FXCollections.observableList(getDeptCodes()));
		
		Rectangle2D parentBounds = Screen.getPrimary().getVisualBounds();
		setPaneSize(outerPane, parentBounds.getWidth(), parentBounds.getHeight());
		// Setting proper screen size with respect to resolution
		
				
				double scrollPaneWidth = parentBounds.getWidth() ;
				double scrollPaneHeight = parentBounds.getHeight() - 30;
				setPaneSize(scrollPane, scrollPaneWidth, scrollPaneHeight);
		

		Map<String, TableColumn<MdrsQuarterDTO, String>> mdrsQuarterColumnmap = new HashMap<String, TableColumn<MdrsQuarterDTO, String>>();

		mdrsQuarterColumnmap.put("line", mdrsLineColumn);
		mdrsQuarterColumnmap.put("shift", mdrsShiftColumn);
		mdrsQuarterColumnmap.put("dept", mdrsDeptColumn);
		mdrsQuarterColumnmap.put("quarter", mdrsQuarterColumn);

		mdrsQuarterSelectColumn
				.setCellValueFactory(new PropertyValueFactory<MdrsQuarterDTO, CheckBox>(
						"selected"));

		mdrsQuarterTable = new MdrsQuarterTable(mdrsQuartersTable,
				mdrsQuarterColumnmap);
		mdrsQuarterTable.init(getUnmappedQuarters());

		Map<String, TableColumn<GALCPeriodDTO, String>> galcPeriodColumnmap = new HashMap<String, TableColumn<GALCPeriodDTO, String>>();

		galcPeriodColumnmap.put("line", galcLineColumn);
		galcPeriodColumnmap.put("shift", galcShiftColumn);
		galcPeriodColumnmap.put("dept", galcDeptColumn);
		galcPeriodColumnmap.put("periodLabel", galcPeriodLabelColumn);
		galcPeriodColumnmap.put("period", galcPeriodColumn);

		galcPeriodTable = new GALCPeriodTable(galcPeriodsTable,
				galcPeriodColumnmap);

		galcPeriodSelectColumn
				.setCellValueFactory(new PropertyValueFactory<GALCPeriodDTO, CheckBox>(
						"selected"));
		galcPeriodTable.init(getGALCPeriods());

		mapButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {

				
				if (!validate()) {
					errorMessagePopup();
				} else {
					insertMcmdrsPeriod();
				}
			}

		});
	}

	private boolean validate() {
		
		boolean selectionsOk = false;
		boolean periodSelected = false;

		boolean quarterSelected = isSelected(mdrsQuarterTable.tblView.getItems(), selectedMdrsQuarterList);
		
		 periodSelected = isSelected(galcPeriodTable.tblView.getItems(), selectedGALCPeriodList);

		return quarterSelected && periodSelected;
	}

	private <T> boolean isSelected(List<? extends Selectable> list, List<T> selectedList) {	

		boolean selected = false;
		
		for (Selectable  item : list) {
			if (item.isSelected())  {
				selected = true;
			
				selectedList.add((T)item);
			}
		}
		return selected;
	}

	private List<String> getDeptCodes() {
		return ServiceFactory.getDao(ManpowerAssignmentDao.class).findDepts();
	}

	private List<MdrsQuarterDTO> getUnmappedQuarters() {
		List<MdrsQuarterDTO> quarterList = new ArrayList<MdrsQuarterDTO>();

		List<Object[]> list = ServiceFactory
				.getDao(ManpowerAssignmentDao.class).getUnmappedQuarters();
		for (Object[] quarter : list) {
			MdrsQuarterDTO rec = new MdrsQuarterDTO();
			rec.setPlantLocCode(quarter[0].toString());
			rec.setDept(quarter[2].toString());
			rec.setLine(quarter[1].toString());
			rec.setQuarter(quarter[4].toString());
			rec.setShiftId(quarter[3].toString());
			rec.setSelected(new CheckBox());
			quarterList.add(rec);

		}
		return quarterList;

	}

	private List<GALCPeriodDTO> getGALCPeriods() {
		List<GALCPeriodDTO> periodList = new ArrayList<GALCPeriodDTO>();

		 java.sql.Date date = new
		 Date(Calendar.getInstance().getTimeInMillis());
	
		List<DailyDepartmentSchedule> list = ServiceFactory.getDao(
				DailyDepartmentScheduleDao.class).getAllFutureByProductionDate(
				date);

		for (DailyDepartmentSchedule period : list) {
			if (!period.getId().getShift().equals("01"))
				continue;
			GALCPeriodDTO rec = new GALCPeriodDTO();
			rec.setDept(period.getId().getProcessLocation());
			rec.setLine(period.getId().getLineNo());
			rec.setPeriod(String.valueOf(period.getId().getPeriod()));
			rec.setShift(period.getId().getShift());
			rec.setPeriodLabel(period.getPeriodLabel());
			rec.setPlantCode(period.getId().getPlantCode());
			rec.setSelected(new CheckBox());
			periodList.add(rec);
		}
		return periodList;
	}

	private void insertMcmdrsPeriod() {

		MCMdrsPeriodDao mcMdrsPeriodDao = ServiceFactory
				.getDao(MCMdrsPeriodDao.class);

		MdrsQuarterDTO quarter = selectedMdrsQuarterList.get(0);
		for (GALCPeriodDTO period : selectedGALCPeriodList) {

			MCMdrsPeriod mcMdrsPeriod = new MCMdrsPeriod();
			MCMdrsPeriodId mcMdrsPeriodId = new MCMdrsPeriodId();
			mcMdrsPeriodId.setDeptCode(quarter.getDept());
			mcMdrsPeriodId.setLineNo(quarter.getLine());
			mcMdrsPeriodId.setPeriod(Integer.parseInt(period.getPeriod()));
			mcMdrsPeriodId.setPlantLocCode(quarter.getPlantLocCode());
			mcMdrsPeriodId.setPlantCode(period.getPlantCode());
			mcMdrsPeriodId.setProcessLocation(period.getDept());
			mcMdrsPeriodId.setQuarter(Short.parseShort(quarter.getQuarter()));
			mcMdrsPeriodId.setShift(period.getShift());
			mcMdrsPeriodId.setShiftId(Short.parseShort(quarter.getShiftId()));
			mcMdrsPeriod.setId(mcMdrsPeriodId);

			mcMdrsPeriodDao.save(mcMdrsPeriod);

		}
		
		selectedGALCPeriodList.clear();
		selectedMdrsQuarterList.clear();

	}

	private void errorMessagePopup() {

		Label errorLabel = new Label(
				"One MDRS quarter and one or more GALC periods needs to be selected  ");
		Button okButton = new Button("OK");

		final Stage dialog = new Stage();

		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initStyle(StageStyle.DECORATED);

		dialog.setHeight(200);
		dialog.setWidth(500);

		okButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {

				dialog.close();
			}
		});

		GridPane grid = new GridPane();

		grid.setVgap(10);
		grid.setPadding(new Insets(30, 30, 30, 30));

		grid.add(errorLabel, 0, 1);
		grid.add(okButton, 0, 2);
		Scene scene = new Scene(grid);
		dialog.setScene(scene);

		dialog.centerOnScreen();

		dialog.toFront();
		dialog.showAndWait();

	}
	
	private void setPaneSize(Region pane, double width, double height) {
		pane.setMinSize(width, height);
		pane.setPrefSize(width, height);
		pane.setMaxSize(width, height);
	}

}
