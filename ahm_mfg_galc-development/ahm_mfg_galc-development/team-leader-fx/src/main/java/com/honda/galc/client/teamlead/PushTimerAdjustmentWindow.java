package com.honda.galc.client.teamlead;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dto.PushTimerDTO;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.DailyDepartmentScheduleUtil;

public class PushTimerAdjustmentWindow extends MainWindow {
	private VBox pushTimerContainer;
	private Map<Integer, PushTimerDTO> pushTimerData;
	private Map<Integer, HBox> pushTimerMap;
	private int counter;
	private Stage stage;
	private ComponentProperty property;
	private ComponentPropertyDao dao;
	private Label errorMsgField = UiFactory.createLabel("errorMsgField");
	private StringConverter<LocalDate> converter;
	private final String COMPONENT_ID = "DEFAULT_VIOS";
	private final String PROPERTY_KEY = "PUSH_TIMER_ADJUSTMENT";
	private final ComponentPropertyId id = new ComponentPropertyId(COMPONENT_ID, PROPERTY_KEY);
	private final String ADD = "Add";
	private final String EDIT = "Edit";
	private final String DATE_PATTERN = "MM/dd/yy";

	public PushTimerAdjustmentWindow(ApplicationContext appContext,
			Application application) {
		super(appContext, application, true);
		init();
	}

	public void init() {
		pushTimerData = new LinkedHashMap<Integer, PushTimerDTO>();
		pushTimerMap = new HashMap<Integer, HBox>();
		stage = ClientMainFx.getInstance().getStage();
		
		converter= new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}
			@Override
			public LocalDate fromString(String string) {
				if (StringUtils.isNotBlank(string)) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		};
		
		dao = ServiceFactory.getDao(ComponentPropertyDao.class);
		property = dao.findByKey(id);

		//Throwing system exception if property in not present in database
		if(property == null) {
			throw new SystemException("Property does not exist [Composite Id: "+"COMPONENT_ID"+", Property Key: "+"PROPERTY_KEY"+"]");
		}

		//PDDAPropertyBean propertyBean = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		//String propertyValue = "09/15/14 4:00 5, 09/15/14 12:15 10, 09/14/14 09:3 09, 9/9/14 08:04 40,";
		String propertyValue = (property.getPropertyValue()!=null)?property.getPropertyValue().trim():"";
		populatePushTimerData(propertyValue);

		//Header
		StackPane heading = new StackPane();
		Text headingText = UiFactory.createText("Push Timer Adustment");
		headingText.setStyle(" -fx-font-size: 2em; -fx-font-weight: bold; ");
		heading.setStyle("-fx-padding: 3em 0px 2em 0px;");
		heading.getChildren().add(headingText);

		//Creation of V box for multiple rows
		pushTimerContainer = new VBox();
		pushTimerContainer.setAlignment(Pos.TOP_CENTER);
		pushTimerContainer.setSpacing(10);

		//Header
		HBox hbox = getHBox();
		hbox.getChildren().add(getLabel("Date", "Date (mm/dd/yy)"));
		hbox.getChildren().add(getLabel("Time", "Time (hh:mm)"));
		hbox.getChildren().add(getLabel("Delay", "Delay (in minutes)"));
		hbox.getChildren().add(getLabel("Actions", "Actions"));
		hbox.setStyle("-fx-font-weight: bold");
		pushTimerContainer.getChildren().addAll(hbox,getSeparator());

		if(pushTimerData.size() > 0) {
			for(final int i: pushTimerData.keySet()) {
				addPushTimerDetails(stage, i);
			}
		}

		//Add button to create Push Timer adjustments
		Button addButton = UiFactory.createButton("Add");
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				showPushTimerDialog(stage, ++counter, ADD);
			}
		});

		//Complete screen layout
		GridPane layout = new GridPane();
		layout.setAlignment(Pos.TOP_CENTER);
		layout.setVgap(30);
		layout.addRow(0, heading);
		layout.addRow(1, pushTimerContainer);
		layout.addRow(2, addButton);

		setClientPane(layout);
	}

	private void populatePushTimerData(String propertyValue) {
		if(propertyValue!=null) {
			String[] pushTimerValues = propertyValue.split(",");
			if(pushTimerValues!=null) {
				for(String pushTimerValue : pushTimerValues) {
					StringTokenizer st_data = new StringTokenizer(pushTimerValue.trim(), " ");

					if(st_data.countTokens() == 3) {
						String date = st_data.nextToken().trim();

						SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
						try {
							date = df.format(df.parse(date));
						} catch (ParseException e) {
							getLogger().error(e, "Parse Exception occurred while parsing date '"+date+"'");
							continue;
						}

						String time = st_data.nextToken().trim();
						String hh = "00";
						String mm = "00";
						StringTokenizer st_time = new StringTokenizer(time, ":");
						if(st_time.countTokens() == 2) {
							hh = String.format("%02d", Integer.parseInt(st_time.nextToken().trim()));
							mm = String.format("%02d", Integer.parseInt(st_time.nextToken().trim()));
						}

						String delay =  String.format("%02d", Integer.parseInt(st_data.nextToken().trim()));
						pushTimerData.put(++counter, new PushTimerDTO(date, hh, mm, delay));
					}
				}
			}
		}
	}

	private void addPushTimerDetails(final Stage stage, final int key) {
		PushTimerDTO data = pushTimerData.get(key);
		//Creation of H box for push timer
		HBox hbox = getHBox();
		final Separator separator = getSeparator();

		Label dateLabel = getLabel("dateLabel", null);
		dateLabel.textProperty().bind(data.dateProperty());
		hbox.getChildren().add(dateLabel);

		Label timeLabel = getLabel("timeLabel", null);
		timeLabel.textProperty().bind(data.timeProperty());
		hbox.getChildren().add(timeLabel);

		Label delayLabel = getLabel("delayLabel", null);
		delayLabel.textProperty().bind(data.delayProperty());
		hbox.getChildren().add(delayLabel);

		HBox buttonBox = new HBox(20);
		buttonBox.setMinWidth(200);
		Button editButton = UiFactory.createButton("Edit");
		editButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				showPushTimerDialog(stage, key, EDIT);
			}
		});
		buttonBox.getChildren().add(editButton);
		Button removeButton = UiFactory.createButton("Remove");
		removeButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				pushTimerData.remove(key);

				pushTimerContainer.getChildren().remove(pushTimerMap.get(key));
				pushTimerContainer.getChildren().remove(separator);

				pushTimerMap.remove(key);
				updatePropertyValue();
			}
		});
		buttonBox.getChildren().add(removeButton);
		hbox.getChildren().add(buttonBox);

		pushTimerMap.put(key, hbox);
		pushTimerContainer.getChildren().addAll(hbox, separator);
	}

	private HBox getHBox() {
		HBox hbox = new HBox(20);
		hbox.setAlignment(Pos.CENTER);
		return hbox;
	}

	private Label getLabel(String id, String text) {

		Label label;
		if(StringUtils.isNotBlank(text)) {
			label = UiFactory.createLabel(id, text);
		}
		else {
			label = UiFactory.createLabel(id);
		}
		label.setMinWidth(200);
		return label;
	}

	private Separator getSeparator() {
		//Horizontal separator
		Separator separator = new Separator();
		return separator;
	}

	private void showPushTimerDialog(final Stage parent,final int key, final String buttonLabel) {
		// initialize the dialog.
		final Stage dialog = new Stage();
		dialog.setTitle("Add/Modify Push Timer");
		dialog.initOwner(parent);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initStyle(StageStyle.UTILITY);
		dialog.setWidth(550);
		dialog.setHeight(400);
		dialog.centerOnScreen();

		//create a grid for the data entry.
		GridPane grid = new GridPane();
		final DatePicker datePicker = new DatePicker();

		datePicker.setConverter(converter);

		final Callback<DatePicker, DateCell> dayCellFactory = 
				new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isBefore(LocalDate.now())) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						}   
					}
				};
			}
		};
		datePicker.setDayCellFactory(dayCellFactory);


		final ChoiceBox<String> hh = new ChoiceBox<String>();
		List<String> hhList = new ArrayList<String>();
		for(int i=0; i<=23; i++) 
			hhList.add(String.format("%02d", i));
		hh.setItems(FXCollections.observableList(hhList));
		hh.getSelectionModel().selectFirst();
		final ChoiceBox<String> mm = new ChoiceBox<String>();
		List<String> mmList = new ArrayList<String>();
		for(int i=0; i<=60; i++) 
			mmList.add(String.format("%02d", i));
		mm.setItems(FXCollections.observableList(mmList));
		mm.getSelectionModel().selectFirst();

		final ChoiceBox<String> delay = new ChoiceBox<String>();
		delay.setItems(FXCollections.observableList(mmList));
		delay.getSelectionModel().selectFirst();

		grid.addRow(0, UiFactory.createLabel("date", "Date"), UiFactory.createLabel("hours", "Hours"), UiFactory.createLabel("minutes", "Minutes"), UiFactory.createLabel("delay", "Delay (in minutes)"));
		grid.addRow(1, datePicker, hh, mm, delay);
		grid.setHgap(15);
		grid.setVgap(10);
		ColumnConstraints col1 = new ColumnConstraints(150);
		ColumnConstraints col2 = new ColumnConstraints(50);
		col2.setHgrow(Priority.ALWAYS);
		ColumnConstraints col3 = new ColumnConstraints(60);
		col3.setHgrow(Priority.ALWAYS);
		ColumnConstraints col4 = new ColumnConstraints(120);
		grid.getColumnConstraints().addAll(col1,col2,col3,col4);

		//Data Population in case of Edit
		if(buttonLabel!=null && buttonLabel.equals(EDIT)) {
			PushTimerDTO data = pushTimerData.get(key);
			//Setting Date
			datePicker.setValue(converter.fromString(data.getDate()));
			//Setting Time
			hh.getSelectionModel().select(data.getHh());
			mm.getSelectionModel().select(data.getMm());
			//Setting delay
			delay.getSelectionModel().select(data.getDelay());
		}
		else {
			//Setting Date
			datePicker.setValue(LocalDate.now());
		}
		errorMsgField.setText("");

		// create action buttons for the dialog.
		Button ok = UiFactory.createButton(buttonLabel);
		ok.setDefaultButton(true);
		Button cancel = UiFactory.createButton("Cancel");
		cancel.setCancelButton(true);

		// add action handlers for the dialog buttons.
		ok.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				if(buttonLabel!=null) {
					String selectedDate = converter.toString(datePicker.getValue());
					String selectedHh = hh.getSelectionModel().getSelectedItem();
					String selectedMm = mm.getSelectionModel().getSelectedItem();
					String selectedDelay = delay.getSelectionModel().getSelectedItem();

					boolean isValid = performValidation(selectedDate, selectedHh, selectedMm, selectedDelay);

					if(isValid) {
						if(buttonLabel.equals(ADD)) {
							//Add Push timer details
							pushTimerData.put(key, new PushTimerDTO(selectedDate, selectedHh, selectedMm, selectedDelay));
							addPushTimerDetails(parent, key);
							updatePropertyValue();
						} else if (buttonLabel.equals(EDIT)) {
							PushTimerDTO pushTimerDetail = pushTimerData.get(key);
							pushTimerDetail.setDate(selectedDate);
							pushTimerDetail.setHh(selectedHh);
							pushTimerDetail.setMm(selectedMm);
							pushTimerDetail.setTime(pushTimerDetail.getTime());
							pushTimerDetail.setDelay(selectedDelay);
							updatePropertyValue();
						}
						dialog.close();
					}
				}
			}
		});
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				dialog.close();
			}
		});

		// layout the dialog.
		HBox buttons = new HBox();
		buttons.setSpacing(10);
		buttons.getChildren().addAll(ok, cancel);
		buttons.setAlignment(Pos.CENTER_LEFT);

		//Error Message
		errorMsgField.setAlignment(Pos.CENTER_LEFT);
		errorMsgField.setWrapText(true);
		errorMsgField.setStyle("-fx-text-fill: red");

		VBox layout = new VBox(10);
		layout.getChildren().addAll(grid, buttons, errorMsgField);
		layout.setPadding(new Insets(5));
		dialog.setScene(new Scene(layout));
		dialog.show();
	}

	private boolean performValidation(String date,String hh, String mm, 
			String delay) {
		String errorMsg = "";
		if(StringUtils.isBlank(date)) {
			errorMsg += "Please select a date." + System.getProperty("line.separator");
		}
		else if(converter.fromString(date).isBefore(LocalDate.now())) {
			errorMsg += "Please select a date that is greater than or equal to today." + System.getProperty("line.separator");
		}
		else if(StringUtils.isBlank(hh)) {
			errorMsg += "Please select hour." + System.getProperty("line.separator");
		}
		else if(StringUtils.isBlank(mm)) {
			errorMsg += "Please select minutes." + System.getProperty("line.separator");
		}
		else if(StringUtils.isBlank(delay)) {
			errorMsg += "Please select delay." + System.getProperty("line.separator");
		}
		else {
			//schedule check
			String processPointId = getPushTimerProcessPointId();
			if(StringUtils.isBlank(processPointId)) {
				errorMsg += "Process Point does not found to validate schedule." + System.getProperty("line.separator");
			}
			else {
				//Get Process Point 
				ProcessPointDao processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
				ProcessPoint processPoint = processPointDao.findById(processPointId);
				processPoint.setProcessPointId(processPointId);
				//User Date conversion
				SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
				try {
					//Getting schedule
					DailyDepartmentScheduleUtil scheduleUtil = new DailyDepartmentScheduleUtil(processPoint, new Date(df.parse(date).getTime()));
					List<DailyDepartmentSchedule> schedules = scheduleUtil.getSchedules();
					
					String timeFormat = "HH:mm:ss";
					SimpleDateFormat tf = new SimpleDateFormat(timeFormat);
					
					if(schedules!=null && !schedules.isEmpty()) {
						boolean isTimeValid = false;
						List<String> workingTime = new ArrayList<String>();
						for(DailyDepartmentSchedule schedule : schedules) {
							String plan = schedule.getPlan();
							
							if(plan!=null && plan.equalsIgnoreCase("Y")) {
								Time startTime = schedule.getStartTime();
								Time endTime = schedule.getEndTime();
								
							    String startTimeStr = tf.format(startTime);
							    java.util.Date start = tf.parse(startTimeStr);

							    String endTimeStr = tf.format(endTime);
							    java.util.Date end = tf.parse(endTimeStr);
							    
							    workingTime.add(startTimeStr + " - " + endTimeStr);

							    java.util.Date time = tf.parse(hh + ":" + mm + ":00");
								if(isTimeBetween(start, end, time)) {
									isTimeValid = true;
									break;
								}
							}
						}
						if(!isTimeValid) {
							String msg = "Time falls in break time. Working slots for date '"+date+"' are: ";
							for(String slot: workingTime) {
								msg += System.getProperty("line.separator") + "    " +slot;
							}
							errorMsg += msg + System.getProperty("line.separator");
						}
					}
					else {
						errorMsg += "No schedule found for date '"+date+"'" + System.getProperty("line.separator");
					}
				} catch (ParseException e) {
					errorMsg += "Parse Exception occurred while parsing date: "+e.getMessage()+"" + System.getProperty("line.separator");
				}
			}
		}

		if(errorMsg.equals("")) {
			return true;
		}
		else {
			errorMsgField.setText("Error!"+System.getProperty("line.separator")+errorMsg);
			return false;
		}

	}
	
	private boolean isTimeBetween(java.util.Date start, java.util.Date end, java.util.Date time) {
	    if ((start.before(time)||start.equals(time)) && (end.after(time)||end.equals(time))) {
	        return true;
	     }
	    else {
	    	return false;
	    }
	}

	private String getPushTimerProcessPointId() {
		String processPointId =  "";
		ComponentPropertyDao componentPropertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		List<ComponentProperty>  componentPropertyList = componentPropertyDao.findAllByPropertyKey("WIDGETS");
		for (ComponentProperty componentProperty : componentPropertyList)  {
			if (componentProperty.getPropertyValue().indexOf("PUSH_TIMER_WIDGET") >= 0) {
				if(StringUtils.isNotBlank(componentProperty.getId().getComponentId())) {
					processPointId = componentProperty.getId().getComponentId().trim();
					break;
				}
			}
		}
		return processPointId;
	}

	private void updatePropertyValue() {
		String value = "";
		for(int i: pushTimerData.keySet()) {
			PushTimerDTO data = pushTimerData.get(i);
			if(data!=null && data.isValid()) {
				if(value.equals("")) {
					value += data.toPropertyValue();
				}
				else {
					value +=  "," + data.toPropertyValue();
				}
			}
		}
		//Update property value
		property.setPropertyValue(value);
		dao.update(property);
		PropertyService.updateProperty(COMPONENT_ID, PROPERTY_KEY, value);
		PropertyService.refreshComponentProperties(COMPONENT_ID);
		getLogger().info("Property updated ==> Component Id: "+COMPONENT_ID+", Property Key: "+PROPERTY_KEY+", and Property value: "+value);
	}
}
