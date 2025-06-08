package com.honda.galc.client.teamlead;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.product.AdvanceProcessCycleTimerDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.product.AdvanceProcessCycleTimer;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class AdvanceProcessCycleTimerWindow extends MainWindow {
	private VBox cycleTimerContainer;
	private AdvanceProcessCycleTimerDao dao;
	private String selectedPlant;
	Map<String, Division> divisionMap = new HashMap<String, Division>();
	String lineId;
	List<AdvanceProcessCycleTimer> advanceProcessCycleTimerList = new ArrayList<AdvanceProcessCycleTimer>();
	Label messageLbl;
	ComboBox lineComboBox = new ComboBox();
	TextField maxCycleTimeTxt;
	TextField currentCycleWarnTxt;
	TextField overCycleWarnTxt;
	TextField accumTimeWarnTxt;
	TextField onTargetWarnTxt;
	TextField currentCycleErrTxt;
	TextField overCycleErrTxt;
	TextField accumTimeErrTxt;
	TextField onTargetErrTxt;

	public AdvanceProcessCycleTimerWindow(ApplicationContext appContext,
			Application application) {
		super(appContext, application, true);
		init();
	}

	public void init() {
		String siteName = findSiteName();
		dao = ServiceFactory.getDao(AdvanceProcessCycleTimerDao.class);

		//Header
		StackPane heading = new StackPane();
		Text headingText = UiFactory.createText("Advance Process Cycle Timer Configuration");
		headingText.setUnderline(true);
		headingText.setStyle(" -fx-font-size: 2em; -fx-font-weight: bold; ");
		heading.setStyle("-fx-padding: 3em 0px 2em 0px;");
		heading.getChildren().add(headingText);

		//Creation of V box for multiple rows
		cycleTimerContainer = new VBox();
		cycleTimerContainer.setAlignment(Pos.TOP_CENTER);
		cycleTimerContainer.setSpacing(10);

		VBox vBox = new VBox();
		
		//Filter
		HBox filterHbox = getHBox();
		
		HBox siteHbox = getHBox();
		Label siteLbl = UiFactory.createLabel("siteLbl", "Site: ");
		siteHbox.getChildren().add(siteLbl);
		final ComboBox siteComboBox = new ComboBox();
		siteComboBox.getItems().addAll(
				siteName
        );
		siteComboBox.setValue(siteName);
		siteHbox.getChildren().add(siteComboBox);
		
		HBox plantHbox = getHBox();
		Label plantLbl = UiFactory.createLabel("plantLbl", "Plant: ");
		plantHbox.getChildren().add(plantLbl);
		
		
		ObservableList<String> plantOptions = 
			    FXCollections.observableArrayList(findPlantsBySiteName(siteName));
		final ComboBox plantComboBox = new ComboBox();
		plantComboBox.setItems(plantOptions);
		
		plantHbox.getChildren().add(plantComboBox);
		
		HBox deptHbox = getHBox();
		Label deptLbl = UiFactory.createLabel("deptLbl", "Department: ");
		deptHbox.getChildren().add(deptLbl);
		final ComboBox deptComboBox = new ComboBox();
		deptHbox.getChildren().add(deptComboBox);
		
		HBox lineHbox = getHBox();
		Label lineLbl = UiFactory.createLabel("plantLbl", "Line: ");
		lineHbox.getChildren().add(lineLbl);
		
		
		lineHbox.getChildren().add(lineComboBox);
		
		filterHbox.getChildren().addAll(siteHbox, plantHbox, deptHbox, lineHbox);
		filterHbox.setStyle("-fx-font-weight: bold");
		
		HBox messageHbox = getHBox();
		messageLbl = UiFactory.createLabel("");
		messageHbox.getChildren().add(messageLbl);
		messageHbox.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-padding: 20 0 0 0;");
		
		//Max Cycle Timer
		HBox maxCycleTimeHbox = getHBox();
		Label maxCycleTimeLbl = UiFactory.createLabel("maxCycleTimeLbl", "Max Cycle Time: ");
		maxCycleTimeHbox.getChildren().add(maxCycleTimeLbl);
		maxCycleTimeTxt = createTextBox();
		maxCycleTimeHbox.getChildren().add(maxCycleTimeTxt);
		maxCycleTimeHbox.setStyle("-fx-font-weight: bold; -fx-padding: 40 0 0 0;");
		
		
		
		// Set Warning and Error
		VBox warnErrorVbox = new VBox();
		
		HBox warnErrorHeaderHbox = getHBox();
		Label blankLabel = UiFactory.createLabel("blankLabel", "    		              ");
		Label warningLabel = UiFactory.createLabel("warningLabel", "Warning");
		warningLabel.setUnderline(true);
		Label blankLabel11 = UiFactory.createLabel("currentCycleSec1Lbl", "                	");
		Label errorLabel = UiFactory.createLabel("errorLabel", "Error");
		Label blankLabel12 = UiFactory.createLabel("currentCycleSec1Lbl", " 		  ");
		errorLabel.setUnderline(true);
		warnErrorHeaderHbox.getChildren().addAll(blankLabel, warningLabel, blankLabel11, blankLabel12, errorLabel);
		
		//Current Cycle Time
		HBox currentCycleHbox = getHBox();
		Label currentCycleLbl = UiFactory.createLabel("currentCycle", "Current Cycle Time: ");
		currentCycleWarnTxt = createTextBox();
		Label currentCycleSec1Lbl = UiFactory.createLabel("currentCycleSec1Lbl", "Sec		");
		currentCycleErrTxt = createTextBox();
		Label currentCycleSec2Lbl = UiFactory.createLabel("currentCycleSec2Lbl", "Sec");
		currentCycleWarnTxt.setStyle("-fx-font-weight: bold; -fx-control-inner-background: YELLOW;");
		currentCycleErrTxt.setStyle("-fx-font-weight: bold; -fx-control-inner-background: TOMATO;");
		currentCycleHbox.getChildren().addAll(currentCycleLbl, currentCycleWarnTxt, currentCycleSec1Lbl, currentCycleErrTxt, currentCycleSec2Lbl);
		currentCycleHbox.setStyle("-fx-padding: 5 0 0 0;");
		
		// Over Cycle
		HBox overCycleHbox = getHBox();
		Label overCycleLbl = UiFactory.createLabel("overCycle", "                 Over Cycle: ");
		overCycleWarnTxt = createTextBox();
		Label overCycleSec1Lbl = UiFactory.createLabel("overCycleSec1Lbl", "Units       ");
		overCycleErrTxt = createTextBox();
		Label overCycleSec2Lbl = UiFactory.createLabel("overCycleSec2Lbl", "Units");
		overCycleWarnTxt.setStyle("-fx-font-weight: bold; -fx-control-inner-background: YELLOW;");
		overCycleErrTxt.setStyle("-fx-font-weight: bold; -fx-control-inner-background: TOMATO;");
		overCycleHbox.getChildren().addAll(overCycleLbl, overCycleWarnTxt, overCycleSec1Lbl, overCycleErrTxt, overCycleSec2Lbl);
		overCycleHbox.setStyle("-fx-padding: 5 0 0 0;");
		
		// Accumulation Time
		HBox accumTimeHbox = getHBox();
		Label accumTimeLbl = UiFactory.createLabel("accumTime", "Accumulation Time: ");
		accumTimeWarnTxt = createTextBox();
		Label accumTime1Lbl = UiFactory.createLabel("accumTime1Lbl", "Sec		");
		accumTimeErrTxt = createTextBox();
		Label accumTime2Lbl = UiFactory.createLabel("accumTime2Lbl", "Sec");
		accumTimeWarnTxt.setStyle("-fx-font-weight: bold; -fx-control-inner-background: YELLOW;");
		accumTimeErrTxt.setStyle("-fx-font-weight: bold; -fx-control-inner-background: TOMATO;");
		accumTimeHbox.getChildren().addAll(accumTimeLbl, accumTimeWarnTxt, accumTime1Lbl, accumTimeErrTxt, accumTime2Lbl);
		accumTimeHbox.setStyle("-fx-padding: 5 0 0 0;");
		
		// On Target
		HBox onTargetHbox = getHBox();
		Label onTargetLbl = UiFactory.createLabel("onTarget", "              On Target: ");
		 onTargetWarnTxt = createTextBox();
		Label onTarget1Lbl = UiFactory.createLabel("onTarget1Lbl", "%		");
		onTargetErrTxt = createTextBox();
		Label onTarget2Lbl = UiFactory.createLabel("onTarget2Lbl", "%");
		onTargetWarnTxt.setStyle("-fx-font-weight: bold; -fx-control-inner-background: YELLOW;");
		onTargetErrTxt.setStyle("-fx-font-weight: bold; -fx-control-inner-background: TOMATO;");
		onTargetHbox.getChildren().addAll(onTargetLbl, onTargetWarnTxt, onTarget1Lbl, onTargetErrTxt, onTarget2Lbl);
		onTargetHbox.setStyle("-fx-padding: 5 0 0 0;");
		
		warnErrorVbox.getChildren().addAll(warnErrorHeaderHbox, currentCycleHbox, overCycleHbox, accumTimeHbox, onTargetHbox);
		warnErrorVbox.setStyle("-fx-font-weight: bold; -fx-padding: 30 0 0 0;");
		
		vBox.getChildren().addAll(filterHbox, messageHbox, maxCycleTimeHbox, warnErrorVbox);
		cycleTimerContainer.getChildren().addAll(vBox, getSeparator());

		//Add button to create Push Timer adjustments
		HBox actionButtonsHbox = getHBox();
		Button saveButton = UiFactory.createButton(" Save ");
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	String validationMessage = validateForm().trim();
                if(validationMessage.equals("")) {
                	saveOrUpdate();
                	populateTextFieldValues(lineComboBox.getValue().toString());
                } else {
                	messageLbl.setText(validationMessage);
                }
            }
        });
		Button deleteButton = UiFactory.createButton("Delete");
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String msg = validateDataForLine();
            	if(msg.equals("")) {
                	delete();
                } else {
                	messageLbl.setText(msg);
                }
            }
        });
		actionButtonsHbox.getChildren().addAll(saveButton, deleteButton);
		
		//Complete screen layout
		GridPane layout = new GridPane();
		layout.setAlignment(Pos.TOP_CENTER);
		layout.setVgap(30);
		layout.addRow(0, heading);
		layout.addRow(1, cycleTimerContainer);
		layout.addRow(2, actionButtonsHbox);

		setClientPane(layout);
		
		plantComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object selectedPlant) {
            	setSelectedPlant(selectedPlant.toString());
                ObservableList deptComboOptions = FXCollections.observableArrayList(findAllDivisionBySiteAndPlant(findSiteName(), selectedPlant));
                deptComboBox.setItems(deptComboOptions);
                lineComboBox.getItems().clear();
                clearTimers();
            }
        });
		
		deptComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object selectedDept) {
                ObservableList lineComboOptions = FXCollections.observableArrayList(findAllLinesBySiteAndDept(findSiteName(), selectedDept));
                lineComboBox.setItems(lineComboOptions);
                clearTimers();
            }
        });
		
		lineComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object selectedLineName) {
            	populateTextFieldValues(selectedLineName.toString());
            }
        });
	}
	
	private String validateDataForLine() {
		if(lineId == null || lineId.trim().length() == 0) {
			return "Please select Plant, Department and Line";
		} else {
			if(advanceProcessCycleTimerList == null && advanceProcessCycleTimerList.size() == 0) {
				return "No data present for selected Line";
			} else {
				return "";
			}
		}
	}
	
	private void clearTimers() {
		maxCycleTimeTxt.setText("");
		currentCycleWarnTxt.setText("");
		currentCycleErrTxt.setText("");
		overCycleWarnTxt.setText("");
		overCycleErrTxt.setText("");
		accumTimeWarnTxt.setText("");
		accumTimeErrTxt.setText("");
		onTargetWarnTxt.setText("");
		onTargetErrTxt.setText("");
	}

	
	private void delete() {
		for(AdvanceProcessCycleTimer advanceProcessCycleTimer : advanceProcessCycleTimerList) {
			dao.remove(advanceProcessCycleTimer);
		}
		messageLbl.setText("Timer data deleted for selected Line");
		clearTimers();
	}
	
	private void saveOrUpdate() {
		String userid = ApplicationContext.getInstance().getUserId();
		if(null != advanceProcessCycleTimerList && advanceProcessCycleTimerList.size() > 1) {
			for(AdvanceProcessCycleTimer advanceProcessCycleTimer : advanceProcessCycleTimerList) {
				advanceProcessCycleTimer.setUpdatedBy(userid);
				advanceProcessCycleTimer.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
				if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.MAX_CYCLE_TIME.equals(advanceProcessCycleTimer.getCycleTimer())) {
					advanceProcessCycleTimer.setWarningValue(new Integer(maxCycleTimeTxt.getText()));
					advanceProcessCycleTimer.setErrorValue(new Integer(maxCycleTimeTxt.getText()));
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.CURRENT_CYCLE_TIME.equals(advanceProcessCycleTimer.getCycleTimer())) {
					advanceProcessCycleTimer.setWarningValue(new Integer(currentCycleWarnTxt.getText()));
					advanceProcessCycleTimer.setErrorValue(new Integer(currentCycleErrTxt.getText()));
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.OVER_CYCLE.equals(advanceProcessCycleTimer.getCycleTimer())) {
					advanceProcessCycleTimer.setWarningValue(new Integer(overCycleWarnTxt.getText()));
					advanceProcessCycleTimer.setErrorValue(new Integer(overCycleErrTxt.getText()));
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.ACCUMULATION_TIME.equals(advanceProcessCycleTimer.getCycleTimer())) {
					advanceProcessCycleTimer.setWarningValue(new Integer(accumTimeWarnTxt.getText()));
					advanceProcessCycleTimer.setErrorValue(new Integer(accumTimeErrTxt.getText()));
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.ON_TARGET.equals(advanceProcessCycleTimer.getCycleTimer())) {
					advanceProcessCycleTimer.setWarningValue(new Integer(onTargetWarnTxt.getText()));
					advanceProcessCycleTimer.setErrorValue(new Integer(onTargetErrTxt.getText()));
				} else {
					throw new IllegalArgumentException("Invalid entries in ADVANCE_PROCESS_CYCLE_TIMER_TBX table. It must have entries for MAX_CYCLE_TIME, CURRENT_CYCLE_TIME, OVER_CYCLE, ACCUMULATION_TIME, ON_TARGET");
				}
			}
			for(AdvanceProcessCycleTimer entity : advanceProcessCycleTimerList) {
				getDao(AdvanceProcessCycleTimerDao.class).update(entity);
			}
			messageLbl.setText("Data updated succesfully");
		} else {
			Integer nextId = getDao(AdvanceProcessCycleTimerDao.class).getNextId();
			List<AdvanceProcessCycleTimer> advanceProcessCycleTimerCreateList = new ArrayList<AdvanceProcessCycleTimer>();
			
			AdvanceProcessCycleTimer maxCycleTimerObj = new AdvanceProcessCycleTimer();
			maxCycleTimerObj.setCycleTimer(ClientConstants.MAX_CYCLE_TIME);
			maxCycleTimerObj.setWarningValue(new Integer(maxCycleTimeTxt.getText()));
			maxCycleTimerObj.setErrorValue(new Integer(maxCycleTimeTxt.getText()));
			advanceProcessCycleTimerCreateList.add(maxCycleTimerObj);
			
			AdvanceProcessCycleTimer currentCycleTimerObj = new AdvanceProcessCycleTimer();
			currentCycleTimerObj.setCycleTimer(ClientConstants.CURRENT_CYCLE_TIME);
			currentCycleTimerObj.setWarningValue(new Integer(currentCycleWarnTxt.getText()));
			currentCycleTimerObj.setErrorValue(new Integer(currentCycleErrTxt.getText()));
			advanceProcessCycleTimerCreateList.add(currentCycleTimerObj);
			
			AdvanceProcessCycleTimer overCycleTimerObj = new AdvanceProcessCycleTimer();
			overCycleTimerObj.setCycleTimer(ClientConstants.OVER_CYCLE);
			overCycleTimerObj.setWarningValue(new Integer(overCycleWarnTxt.getText()));
			overCycleTimerObj.setErrorValue(new Integer(overCycleErrTxt.getText()));
			advanceProcessCycleTimerCreateList.add(overCycleTimerObj);
			
			AdvanceProcessCycleTimer accumTimerObj = new AdvanceProcessCycleTimer();
			accumTimerObj.setCycleTimer(ClientConstants.ACCUMULATION_TIME);
			accumTimerObj.setWarningValue(new Integer(accumTimeWarnTxt.getText()));
			accumTimerObj.setErrorValue(new Integer(accumTimeErrTxt.getText()));
			advanceProcessCycleTimerCreateList.add(accumTimerObj);
			
			AdvanceProcessCycleTimer onTargetTimerObj = new AdvanceProcessCycleTimer();
			onTargetTimerObj.setCycleTimer(ClientConstants.ON_TARGET);
			onTargetTimerObj.setWarningValue(new Integer(onTargetWarnTxt.getText()));
			onTargetTimerObj.setErrorValue(new Integer(onTargetErrTxt.getText()));
			advanceProcessCycleTimerCreateList.add(onTargetTimerObj);
			
			for(AdvanceProcessCycleTimer advanceProcessCycleTimer : advanceProcessCycleTimerCreateList) {
				advanceProcessCycleTimer.setId(nextId);
				advanceProcessCycleTimer.setLineId(lineId);
				advanceProcessCycleTimer.setCreatedBy(userid);
				advanceProcessCycleTimer.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
				getDao(AdvanceProcessCycleTimerDao.class).save(advanceProcessCycleTimer);
				
				nextId =nextId + 1;
			}
			messageLbl.setText("Data inserted succesfully");
			
		}
	}
	
	private String validateForm() {
		String message = ""; 
		if(isNumeric(maxCycleTimeTxt.getText())
				&& isNumeric(currentCycleWarnTxt.getText())
				&& isNumeric(currentCycleErrTxt.getText())
				&& isNumeric(overCycleWarnTxt.getText())
				&& isNumeric(accumTimeWarnTxt.getText())
				&& isNumeric(accumTimeErrTxt.getText())
				&& isNumeric(onTargetWarnTxt.getText())
				&& isNumeric(onTargetErrTxt.getText())
				&& lineId != null && lineId.trim().length() > 0) {
			
			//Current Cycle Time
			if(new Integer(currentCycleWarnTxt.getText().toString().trim()) <= new Integer(currentCycleErrTxt.getText().toString().trim())) {
				message = "Current Cycle Time Warning value can't be less or equal to Error value";
			} else if(new Integer(overCycleWarnTxt.getText().toString().trim()) >= new Integer(overCycleErrTxt.getText().toString().trim())) {
				message = "Over Cycle Units Warning value can't be more or equal to Error value";
			} else if(new Integer(accumTimeWarnTxt.getText().toString().trim()) >= new Integer(accumTimeErrTxt.getText().toString().trim())) {
				message = "Accumulation Time Warning value can't be more or equal to Error value";
			} else if(new Integer(onTargetWarnTxt.getText().toString().trim()) <= new Integer(onTargetErrTxt.getText().toString().trim())) {
				message = "On Target Percentage Warning value can't be less or equal to Error value";
			} else {
				message = "";
			}
		} else {
			message = "Please enter Numeric data in all timer fields";
		}
		return message;
	}

	private static Boolean isNumeric(String s) {
		return s != null && !s.trim().isEmpty() && StringUtils.isNumeric(s);
	}
	
	private void setSelectedPlant(String s) {
		this.selectedPlant = s;
	}
	
	private String getSelectedPlant() {
		return this.selectedPlant;
	}
	
	private HBox getHBox() {
		HBox hbox = new HBox(20);
		hbox.setAlignment(Pos.CENTER);
		return hbox;
	}

	private TextField createTextBox() {
		final TextField textField = new TextField("");
		textField.setStyle("-fx-opacity: 1.0;");
		
		textField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		            textField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		return textField;
	}

	private Separator getSeparator() {
		//Horizontal separator
		Separator separator = new Separator();
		return separator;
	}
	
	private String findSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}

	/** 
	 * This method is used to find all plant by site name
	 * @param siteName
	 * @return list of plant
	 */
	public List<String> findPlantsBySiteName(String siteName) {
		List<Plant> plants =  getDao(PlantDao.class).findAllBySite(siteName);
		List<String> plantList = new ArrayList<String>();
		for(Plant plant : plants) {
			plantList.add(plant.getPlantName());
		}
		return plantList;
	}

	/**
	 * This method is used to find  division based on Site and Plant
	 */
	public List<String> findAllDivisionBySiteAndPlant(String siteName, Object plantName) {
		List<Division> divisions =  getDao(DivisionDao.class).findById(siteName,plantName.toString());
		List<String> divisionList = new ArrayList<String>();
		for(Division division : divisions) {
			divisionList.add(division.getDivisionName());
			divisionMap.put(division.getDivisionName(), division);
		}
		return divisionList;
    }
	
	/**
	 * This method is used to find EntryDept by process point
	 */
	public List<String> findAllLinesBySiteAndDept(String siteName, Object deptName){
		List<Line> lines =  getDao(LineDao.class).findAllByDivisionId(divisionMap.get(deptName), false);
		List<String> lineList = new ArrayList<String>();
		for(Line line : lines) {
			lineList.add(line.getLineName());
		}
		
		return lineList;
	}
	
	public void populateTextFieldValues(String selectedLineName) {
		Line line = getDao(LineDao.class).findByLineName(selectedLineName);
		lineId = line.getLineId();
		advanceProcessCycleTimerList =  getDao(AdvanceProcessCycleTimerDao.class).findByLineId(lineId);
		if(null != advanceProcessCycleTimerList && advanceProcessCycleTimerList.size() > 1) {
			messageLbl.setText("");
			for(AdvanceProcessCycleTimer advanceProcessCycleTimer : advanceProcessCycleTimerList) {
				if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.MAX_CYCLE_TIME.equals(advanceProcessCycleTimer.getCycleTimer())) {
					maxCycleTimeTxt.setText(advanceProcessCycleTimer.getWarningValue().toString());
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.CURRENT_CYCLE_TIME.equals(advanceProcessCycleTimer.getCycleTimer())) {
					currentCycleWarnTxt.setText(advanceProcessCycleTimer.getWarningValue().toString());
					currentCycleErrTxt.setText(advanceProcessCycleTimer.getErrorValue().toString());
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.OVER_CYCLE.equals(advanceProcessCycleTimer.getCycleTimer())) {
					overCycleWarnTxt.setText(advanceProcessCycleTimer.getWarningValue().toString());
					overCycleErrTxt.setText(advanceProcessCycleTimer.getErrorValue().toString());
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.ACCUMULATION_TIME.equals(advanceProcessCycleTimer.getCycleTimer())) {
					accumTimeWarnTxt.setText(advanceProcessCycleTimer.getWarningValue().toString());
					accumTimeErrTxt.setText(advanceProcessCycleTimer.getErrorValue().toString());
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.ON_TARGET.equals(advanceProcessCycleTimer.getCycleTimer())) {
					onTargetWarnTxt.setText(advanceProcessCycleTimer.getWarningValue().toString());
					onTargetErrTxt.setText(advanceProcessCycleTimer.getErrorValue().toString());
				} else {
					throw new IllegalArgumentException("Invalid entries in ADVANCE_PROCESS_CYCLE_TIMER_TBX table. It must have entries for MAX_CYCLE_TIME, CURRENT_CYCLE_TIME, OVER_CYCLE, ACCUMULATION_TIME, ON_TARGET");
				}
			}
		} else {
			messageLbl.setText("Process Cycle Timer has not been configured for this line. Please configure.");
			maxCycleTimeTxt.setText("");
			currentCycleWarnTxt.setText("");
			currentCycleErrTxt.setText("");
			overCycleWarnTxt.setText("");
			overCycleErrTxt.setText("");
			accumTimeWarnTxt.setText("");
			accumTimeErrTxt.setText("");
			onTargetWarnTxt.setText("");
			onTargetErrTxt.setText("");
		}
	}
}
