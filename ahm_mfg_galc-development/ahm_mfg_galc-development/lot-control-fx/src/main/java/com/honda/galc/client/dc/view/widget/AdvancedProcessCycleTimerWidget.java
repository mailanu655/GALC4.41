package com.honda.galc.client.dc.view.widget;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import com.google.common.base.Splitter;
import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.product.AdvanceProcessCycleTimerDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.AdvanceProcessCycleTimer;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.property.PropertyService;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

public class AdvancedProcessCycleTimerWidget extends AbstractWidget {

	
	private Timeline timer;
	private int currentCycleTime = 0;
	private Integer overCycleCount = 0;
	private Integer totalCycleCount = 0;
	private Integer onTargetPerc = 0;
	private Integer accumTimeOverSeconds = 0;
	private Double accumTimeOverMinutes = 0.0;
	private static final int SAMPLE_SIZE = 20;
	private int cycleIndex = 0;
	private Integer maxCycleTime;
	private Integer currentCycleWarnSec;
	private Integer currentCycleErrSec;
	private Integer overCycleWarnUnits;
	private Integer overCycleErrUnits;
	private Integer accumTimeWarnSec;
	private Integer accumTimeErrSec;
	private Integer onTargetWarnPerc;
	private Integer onTargetErrPerc;

	Integer textBoxHeight = 80;
	Integer textBoxWidth = 100;
	String fontSize= "1=80,2=70,3=60,4=50";
	Map<String, String> fontSizeMap = new HashMap<String, String>();
	TextField currentCycleTxt;
	TextField maxCycleTxt;
	TextField totalCyclesTxt;
	TextField onTargetTxt;
	TextField overCyclesTxt;
	TextField accumTimeOverSecTxt;
	TextField accumTimeOverMinTxt;
	GpcsDivision division = new GpcsDivision();
	List<DailyDepartmentSchedule> scheduleList = new ArrayList<DailyDepartmentSchedule>();
	List<String> quarterList = new ArrayList<String>();;
	Map<String, DailyDepartmentSchedule> scheduleMap = new HashMap<String, DailyDepartmentSchedule>();
	String curProdDate = "";
	String curTime = "";

	public AdvancedProcessCycleTimerWidget(ProductController productController) {
		super(ViewId.ADVANCED_PROCESS_CYCLE_TIMER_WIDGET, productController);
		currentCycleTime = this.maxCycleTime;

		ProcessPoint processPoint = ClientMainFx.getInstance()
				.getApplicationContext().getProcessPoint();
		division = getDao(GpcsDivisionDao.class).findByKey(
				processPoint.getDivisionId());
		
		
		String processPointId = processPoint.getProcessPointId();
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class, processPointId);
		this.textBoxHeight  = property.getAdvPrcCycleTimerTxtHeight();
		this.textBoxWidth  = property.getAdvPrcCycleTimerTxtWidth();
		this.fontSize = property.getAdvPrcCycleTimerFontSize();
		getFontSize();
		
		try {
			curProdDate = CommonUtil.getCurrentDate();
			curTime = CommonUtil.getCurrentTime();
		} catch (ParseException e) {
			curProdDate = "";
			getLogger().error(e);
		}

	}

	@Override
	protected void initComponents() {
		EventBusUtil.register(this);
		getTimers();
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {
		this.setTop(getProductionMetrics());
		Date d1 = new Date();
		if (!scheduleMap.containsKey(curProdDate)) {
			List<String> quarterList = new ArrayList<String>();
			quarterList.add("1STQ");
			quarterList.add("2NDQ");
			quarterList.add("3RDQ");
			quarterList.add("4THQ");
			scheduleList = getDao(DailyDepartmentScheduleDao.class)
					.findByCurDateProcLocAndQuart(division.getGpcsLineNo(),
							division.getGpcsProcessLocation(), quarterList);
			for (DailyDepartmentSchedule schedule : scheduleList) {
				scheduleMap.put(curProdDate + "|" + schedule.getId().getShift() + "|"
						+ schedule.getPeriodLabel(), schedule);
			}
		}

		Date currentDateTime = new Date();
		for (DailyDepartmentSchedule schedule : scheduleList) {
			
			if (curProdDate.equals(schedule.getId().getProductionDate()
					.toString())) {
				Date startDateTime = CommonUtil.appendDateTime(schedule.getId()
						.getProductionDate().toString(), schedule
						.getStartTime().toString());
				Date endDateTime = CommonUtil.appendDateTime(schedule.getId()
						.getProductionDate().toString(), schedule.getEndTime()
						.toString());
				if (schedule.getEndTime().toString().startsWith("00")) {
					Calendar c = Calendar.getInstance();
					c.setTime(endDateTime);
					c.add(Calendar.DATE, 1);
					endDateTime = c.getTime();
				}
				if (startDateTime.compareTo(currentDateTime)
						* currentDateTime.compareTo(endDateTime) > 0) {
					String dateShiftQuarter = schedule.getId()
							.getProductionDate()
							+ "|"
							+ schedule.getId().getShift()
							+ "|"
							+ schedule.getPeriodLabel();
					if (!quarterList.contains(dateShiftQuarter)) {
						totalCycleCount = 0;
						overCycleCount = 0;
						accumTimeOverMinutes = 0.0;
						accumTimeOverSeconds = 0;
						onTargetPerc = 0;
						totalCyclesTxt.setText("0");
						overCyclesTxt.setText("0");
						accumTimeOverSecTxt.setText("0");
						accumTimeOverMinTxt.setText("0");
						onTargetTxt.setText("0");

						quarterList.add(dateShiftQuarter);
					}
				}
			}
		}

		startClock();
	}

	@Override
	protected void processProductCancelled(ProductModel productModel) {
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
		stopClock();
	}


	private Node getProductionMetrics() {

		VBox finalVbox = new VBox();
		HBox allColHbox = new HBox();

		VBox vboxFirstCol = new VBox();
		vboxFirstCol.setSpacing(5);
		vboxFirstCol.setAlignment(Pos.CENTER_RIGHT);
		Label title = UiFactory.createLabel("processCycleTimer",
				"Process Cycle Timer");

		HBox maxCycleTimeBox = new HBox();
		maxCycleTimeBox.setAlignment(Pos.CENTER_RIGHT);
		maxCycleTimeBox.getChildren().add(
				UiFactory.createLabel("maxCycleTimeLabel", "Max Cycle Time: ",
						"-fx-font-weight: bold;"));
		maxCycleTxt = createTextBox(textBoxHeight, textBoxWidth);
		maxCycleTxt.setText(maxCycleTime + "");
		maxCycleTimeBox.getChildren().add(maxCycleTxt);
		maxCycleTimeBox.getChildren().add(
				UiFactory.createLabel("maxCycleTimeLabel2", " sec",
						"-fx-font-weight: bold;"));

		HBox toalCyclesBox = new HBox();
		toalCyclesBox.setAlignment(Pos.CENTER_RIGHT);
		toalCyclesBox.getChildren().add(
				UiFactory.createLabel("toalCyclesLabel", "  Total  Cycles :  ",
						"-fx-font-weight: bold;"));
		totalCyclesTxt = createTextBox(textBoxHeight, textBoxWidth);
		totalCyclesTxt.setText(totalCycleCount + "");
		toalCyclesBox.getChildren().add(totalCyclesTxt);
		toalCyclesBox.getChildren().add(
				UiFactory.createLabel("toalCyclesLabel2", "unit",
						"-fx-font-weight: bold;"));

		vboxFirstCol.getChildren().addAll(maxCycleTimeBox, toalCyclesBox);

		VBox vboxSecondCol = new VBox();
		vboxSecondCol.setSpacing(5);
		vboxSecondCol.setAlignment(Pos.CENTER_RIGHT);

		HBox currentCycleBox = new HBox();
		currentCycleBox.setAlignment(Pos.CENTER_RIGHT);
		currentCycleBox.getChildren().add(
				UiFactory.createLabel("currentCycleLabel", "Current Cycle: ",
						"-fx-font-weight: bold;"));
		currentCycleTxt = createTextBox(textBoxHeight, textBoxWidth);
		currentCycleTxt.setText(maxCycleTime + "");
		setPadding(currentCycleTxt, 1);
		currentCycleTxt.setBackground(new Background(
				new BackgroundFill(Color.LAWNGREEN,
						CornerRadii.EMPTY, Insets.EMPTY)));
		currentCycleBox.getChildren().add(currentCycleTxt);
		currentCycleBox.getChildren().add(
				UiFactory.createLabel("currentCycleLabel2", " sec",
						"-fx-font-weight: bold;"));

		HBox overCyclesBox = new HBox();
		overCyclesBox.setAlignment(Pos.CENTER_RIGHT);
		overCyclesBox.getChildren().add(
				UiFactory.createLabel("overCyclesLabel", "Over  Cycles : ",
						"-fx-font-weight: bold;"));
		overCyclesTxt = createTextBox(textBoxHeight, textBoxWidth);
		overCyclesTxt.setText(overCycleCount.toString());
		overCyclesBox.getChildren().add(overCyclesTxt);
		overCyclesBox.getChildren().add(
				UiFactory.createLabel("overCyclesLabel2", "unit",
						"-fx-font-weight: bold;"));

		vboxSecondCol.getChildren().addAll(currentCycleBox, overCyclesBox);

		VBox vboxThirdCol = new VBox();
		vboxThirdCol.setSpacing(5);
		vboxThirdCol.setAlignment(Pos.CENTER_RIGHT);

		HBox accumTimeOverSecBox = new HBox();
		accumTimeOverSecBox.setAlignment(Pos.CENTER_RIGHT);
		accumTimeOverSecBox.getChildren().add(
				UiFactory.createLabel("accumTimeOverSecLabel",
						"Accum. Time Over: ", "-fx-font-weight: bold;"));
		accumTimeOverSecTxt = createTextBox(textBoxHeight, textBoxWidth);
		accumTimeOverSecTxt.setText(accumTimeOverSeconds.toString());
		accumTimeOverSecBox.getChildren().add(accumTimeOverSecTxt);
		accumTimeOverSecBox.getChildren().add(
				UiFactory.createLabel("accumTimeOverSecLabel2", " sec",
						"-fx-font-weight: bold;"));

		HBox accumTimeOverMinBox = new HBox();
		accumTimeOverMinBox.setAlignment(Pos.CENTER_RIGHT);
		accumTimeOverMinBox.getChildren().add(
				UiFactory.createLabel("accumTimeOverLabel",
						"Accum. Time Over: ", "-fx-font-weight: bold;"));
		accumTimeOverMinTxt = createTextBox(textBoxHeight, textBoxWidth);
		if(accumTimeOverMinutes.toString().equals("0.0")) {
			accumTimeOverMinTxt.setText("0");
		} else {
			accumTimeOverMinTxt.setText(accumTimeOverMinutes.toString());
		}
		accumTimeOverMinBox.getChildren().add(accumTimeOverMinTxt);
		accumTimeOverMinBox.getChildren().add(
				UiFactory.createLabel("accumTimeOverLabel2", " min",
						"-fx-font-weight: bold;"));
		
		vboxThirdCol.getChildren().addAll(accumTimeOverSecBox, accumTimeOverMinBox);

		VBox vboxFourthCol = new VBox();
		vboxFourthCol.setAlignment(Pos.CENTER_RIGHT);
		HBox onTargetBox = new HBox();
		onTargetBox.getChildren().add(
				UiFactory.createLabel("accumTimeOverLabel", "On Target: ",
						"-fx-font-weight: bold;"));
		onTargetTxt = createTextBox(textBoxHeight*2, textBoxWidth*2);
		onTargetTxt.setText(onTargetPerc + "");
		onTargetTxt.setPrefWidth(textBoxHeight*3);
		onTargetTxt.setMinSize(textBoxHeight*3, textBoxHeight*2);
		onTargetTxt.setMaxSize(textBoxHeight*3, textBoxHeight*2);
		onTargetBox.getChildren().add(onTargetTxt);
		onTargetBox.getChildren().add(
				UiFactory.createLabel("accumTimeOverLabel2", " %",
						"-fx-font-size: 24pt;"));
		vboxFourthCol.getChildren().addAll(onTargetBox);

		allColHbox.setSpacing(20);
		allColHbox
				.setStyle("-fx-border-color: silver; -fx-border-width: 1px;  -fx-border-radius: 1px;");
		allColHbox.getChildren().addAll(vboxFirstCol, vboxSecondCol, vboxThirdCol,
				vboxFourthCol);

		finalVbox.getChildren().addAll(title, allColHbox);

		return finalVbox;
	}

	private void getTimers() {
		ProcessPoint processPoint = ClientMainFx.getInstance()
				.getApplicationContext().getProcessPoint();
		String lineId = processPoint.getLineId();
		List<AdvanceProcessCycleTimer> advanceProcessCycleTimerList = getDao(
				AdvanceProcessCycleTimerDao.class).findByLineId(lineId);
		if (null != advanceProcessCycleTimerList
				&& advanceProcessCycleTimerList.size() > 1) {
			for (AdvanceProcessCycleTimer advanceProcessCycleTimer : advanceProcessCycleTimerList) {
				if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.MAX_CYCLE_TIME.equals(advanceProcessCycleTimer.getCycleTimer())) {
					this.maxCycleTime = advanceProcessCycleTimer
							.getWarningValue();
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.CURRENT_CYCLE_TIME.equals(advanceProcessCycleTimer.getCycleTimer())) {
					currentCycleWarnSec = advanceProcessCycleTimer
							.getWarningValue();
					currentCycleErrSec = advanceProcessCycleTimer
							.getErrorValue();
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.OVER_CYCLE.equals(advanceProcessCycleTimer.getCycleTimer())) {
					overCycleWarnUnits = advanceProcessCycleTimer
							.getWarningValue();
					overCycleErrUnits = advanceProcessCycleTimer
							.getErrorValue();
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.ACCUMULATION_TIME.equals(advanceProcessCycleTimer.getCycleTimer())) {
					accumTimeWarnSec = advanceProcessCycleTimer
							.getWarningValue();
					accumTimeErrSec = advanceProcessCycleTimer.getErrorValue();
				} else if(advanceProcessCycleTimer.getCycleTimer() != null && ClientConstants.ON_TARGET.equals(advanceProcessCycleTimer.getCycleTimer())) {
					onTargetWarnPerc = advanceProcessCycleTimer
							.getWarningValue();
					onTargetErrPerc = advanceProcessCycleTimer.getErrorValue();
				} else {
					maxCycleTime = 0;
					currentCycleWarnSec = 0;
					currentCycleErrSec = 0;
					overCycleWarnUnits = 0;
					overCycleErrUnits = 0;
					accumTimeWarnSec = 0;
					accumTimeErrSec = 0;
					onTargetWarnPerc = 0;
					onTargetErrPerc = 0;
				}
			}
		}
	}

	public void startClock() {

		if (timer != null) {
			cycleIndex = (cycleIndex + 1) % SAMPLE_SIZE;
			currentCycleTime = this.maxCycleTime;
			timer.stop();
		}
		timer = new Timeline(new KeyFrame(Duration.seconds(1),
				new EventHandler<javafx.event.ActionEvent>() {
					public void handle(javafx.event.ActionEvent event) {

						currentCycleTime = currentCycleTime - 1;
						// Change background color of Current Cycle
						if (currentCycleTime <= currentCycleErrSec) {
							currentCycleTxt.setBackground(new Background(
									new BackgroundFill(Color.RED,
											CornerRadii.EMPTY, Insets.EMPTY)));
						} else if (currentCycleTime <= currentCycleWarnSec) {
							currentCycleTxt.setBackground(new Background(
									new BackgroundFill(Color.YELLOW,
											CornerRadii.EMPTY, Insets.EMPTY)));
						} else {
							currentCycleTxt.setBackground(new Background(
									new BackgroundFill(Color.LAWNGREEN,
											CornerRadii.EMPTY, Insets.EMPTY)));
						}

						
						setCurrentCycleValue(currentCycleTime);

						// Change background color of On Target
						if (totalCycleCount > 0) {
							if (onTargetPerc <= onTargetErrPerc) {
								onTargetTxt
										.setBackground(new Background(
												new BackgroundFill(
														Color.RED,
														CornerRadii.EMPTY,
														Insets.EMPTY)));
							} else if (onTargetPerc <= onTargetWarnPerc) {
								onTargetTxt
										.setBackground(new Background(
												new BackgroundFill(Color.YELLOW,
														CornerRadii.EMPTY,
														Insets.EMPTY)));
							} else {
								onTargetTxt
								.setBackground(new Background(
										new BackgroundFill(Color.LAWNGREEN,
												CornerRadii.EMPTY,
												Insets.EMPTY)));
							}
						}
							// Change background color of Over Cycles
							if (overCycleCount >= overCycleErrUnits) {
								overCyclesTxt
										.setBackground(new Background(
												new BackgroundFill(
														Color.RED,
														CornerRadii.EMPTY,
														Insets.EMPTY)));
							} else if (overCycleCount >= overCycleWarnUnits && overCycleCount < overCycleErrUnits) {
								overCyclesTxt
										.setBackground(new Background(
												new BackgroundFill(Color.YELLOW,
														CornerRadii.EMPTY,
														Insets.EMPTY)));
							} else {
								overCyclesTxt
									.setBackground(new Background(
										new BackgroundFill(Color.LAWNGREEN,
												CornerRadii.EMPTY,
												Insets.EMPTY)));
							}
							
							// Change background color of Accumulation Time Over Seconds
							if (accumTimeOverSeconds >= accumTimeErrSec) {
								accumTimeOverSecTxt
										.setBackground(new Background(
												new BackgroundFill(
														Color.RED,
														CornerRadii.EMPTY,
														Insets.EMPTY)));
								accumTimeOverMinTxt
										.setBackground(new Background(
												new BackgroundFill(
														Color.RED,
														CornerRadii.EMPTY,
														Insets.EMPTY)));
							} else if (accumTimeOverSeconds >= accumTimeWarnSec && accumTimeOverSeconds < accumTimeErrSec) {
								accumTimeOverSecTxt
										.setBackground(new Background(
												new BackgroundFill(Color.YELLOW,
														CornerRadii.EMPTY,
														Insets.EMPTY)));
								accumTimeOverMinTxt
										.setBackground(new Background(
												new BackgroundFill(Color.YELLOW,
														CornerRadii.EMPTY,
														Insets.EMPTY)));
							} else {
								accumTimeOverSecTxt
										.setBackground(new Background(
											new BackgroundFill(Color.LAWNGREEN,
													CornerRadii.EMPTY,
													Insets.EMPTY)));
								accumTimeOverMinTxt
										.setBackground(new Background(
											new BackgroundFill(Color.LAWNGREEN,
													CornerRadii.EMPTY,
													Insets.EMPTY)));
							}
					};
				}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}

	public String getCurrentCycleValue() {
		return this.currentCycleTime + "";
	}

	private TextField createTextBox(Integer height, Integer width) {
		TextField textField = new TextField("");
		textField.setDisable(true);
		textField.setPrefWidth(width);
		textField.setMinSize(width, height);
		textField.setMaxSize(width, height);
		return textField;
	}

	private void setCurrentCycleValue(int value) {
		currentCycleTime = value;
		currentCycleTxt.setText(currentCycleTime + "");
		
		setPadding(currentCycleTxt, 1);
		setPadding(overCyclesTxt, 1);
		setPadding(accumTimeOverSecTxt, 1);
		setPadding(accumTimeOverMinTxt, 1);
		setPadding(onTargetTxt, 2);
		setPadding(maxCycleTxt, 1);
		setPadding(totalCyclesTxt, 1);
	}
	
	private void setPadding (TextField textField, Integer size) {
		Integer textLength = textField.getText().length();
		Integer fontSize;
		switch (textLength) {
        	case 1:  
        		fontSize = new Integer(fontSizeMap.get("1"));
                break;
        	case 2:  
        		fontSize = new Integer(fontSizeMap.get("2"));
                break;
        	case 3:  
        		fontSize = new Integer(fontSizeMap.get("3"));
                break;
        	case 4:  
        		fontSize = new Integer(fontSizeMap.get("4"));
                break;
            default:
            	fontSize = new Integer(fontSizeMap.get("4"))-30;
            break;
		}
		FontMetrics metrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(textField.getFont());
		textField.setPadding(new Insets(-metrics.getDescent(), 0, -metrics.getDescent(), 0));
		textField.setStyle("-fx-opacity: 1.0; -fx-padding: -10 0 10 0px; -fx-font-size: " + (fontSize*size) + "px;");
	}

	public void stopClock() {
		timer.stop();
		if (currentCycleTime < 0) {
			overCycleCount = overCycleCount + 1;
			accumTimeOverSeconds = accumTimeOverSeconds
					+ Math.abs(currentCycleTime);
			Double min = new Double(accumTimeOverSeconds) / 60;
			accumTimeOverMinutes = Math.round(min * 100.0) / 100.0;
		}
		totalCycleCount = totalCycleCount + 1;
		totalCyclesTxt.setText(totalCycleCount + "");

		double diff = totalCycleCount - overCycleCount;
		Double perc = diff / totalCycleCount;
		perc = perc * 100;
		onTargetPerc = perc.intValue();
	}

	private int getCountStartSecond(String processPointId) {
		return PropertyService.getPropertyBean(PDDAPropertyBean.class,
				processPointId).getCountStartSecond();
	}
	
	private void getFontSize() {
		this.fontSizeMap = Splitter.on(',')
			    .trimResults()
			    .withKeyValueSeparator(
			        Splitter.on('=')
			            .limit(2)
			            .trimResults())
			    .split(fontSize);
	}

}
