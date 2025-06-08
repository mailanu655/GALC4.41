package com.honda.galc.client.dc.view.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.PushTimeMonitor;
import com.honda.galc.client.dc.common.PushTimerContext;
import com.honda.galc.client.dc.enumtype.ArcColor;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.pane.AbstractWidget;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dto.PddaUnitOfOperation;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.DailyDepartmentScheduleUtil;

/**
 * @author Suriya Sena
 * @date 13 August 2014
 */
public class PushTimerPane extends AbstractWidget {

	/*
	 *  Push Time  : The same as line speed. The vehicle will stay at station for PushTime interval, during that time all units of
	 *               work for that station must be completed. The Push time is configured the same for all stations and is currently
	 *               set to 55 minutes. (See Note 1) 
	 *               
	 *  Process Time : The sum of all unit of work times scheduled for a process, the process time will initially be set to <= Push time.
	 *                 The process time is often significantly smaller than the push time this is because unitProcess time only accounts for time
	 *                 to install parts and not for time to walk around the vehicle  etc.  For example the pushTime is 55 minutes but the sum of 
	 *                 all unit time for a process point could be 40 minutes. 
	 *                 
	 * ** Note 1 **
     * The next Push Time can be greater than the cycle time under the following circumstances
     *  1)  There is not enough time to complete the cycle within the current schedule, in which case the cycle must continue after the break
     *      in this case the break time is added to the push time.
     *  2)  The timer has started before the first work period, this can happen if the associate starts the terminal prior to beginning of the shift. 
     *      In this case the time to the start of the first period will be added to the push time.
	*/

	private Timeline timer;
	private PushTimerWidget pushTimer;
	private ProcessTimerWidget processTimer;
	private long lastUpdate = 0;
	private boolean hasWorkcompleteChanged=true;
	private PushTimeMonitor monitor;

	private static float limitOK = 5.0f; 
	private static float limitWarn = 10.0f;
	private final static int pushTimeMins;
	private static int widgetSize;
	private ArrayList<Date> pushTime;
	private static final String shift = "01";
	private static final boolean isDebug = false;
	private static final int MILLIS = 1000;
    private static final int currentTimeMins = 0;

	
	static {
		pushTimeMins = PropertyService.getPropertyBean(PDDAPropertyBean.class).getPushTimerMins();
		limitOK =  PropertyService.getPropertyBean(PDDAPropertyBean.class).getPushTimerLimitOK();
		limitWarn = PropertyService.getPropertyBean(PDDAPropertyBean.class).getPushTimerLimitWARN();
		widgetSize = PropertyService.getPropertyBean(PDDAPropertyBean.class).getPushTimerWidgetSize();
	}
	

	  
	public PushTimerPane(ProductController productController) {
	   super(ViewId.PUSH_TIMER_WIDGET,productController);
	}
	  
	@Override
	protected void initComponents() {
		monitor = new PushTimeMonitor();
	}
	
	public void init (int size, int maxTimeMins, int currentTimeMins) {
		int initialTime=0;
		int timeToNextPush=0;
		hasWorkcompleteChanged = true;
		String tooltipMessage = null;   // Will show the push schedule or an error message if the schedule cannot be loaded.  
		
		try {
		  tooltipMessage = calculatePushTimes();
		  timeToNextPush = getTimeToNextPush();
		} catch (NullPointerException npe ) {
		  tooltipMessage = "Error failed to load PushTimer calendar. Confirm that the GAL226TBX and GAL238TBX have been populated with the correct data.";
		}

		/* See  Note 1  */
		if (timeToNextPush > maxTimeMins) {
			maxTimeMins = timeToNextPush;
			initialTime = 0;
		} else {
			initialTime = pushTimeMins - timeToNextPush; 
		}
		
		pushTimer = new PushTimerWidget("Push Timer", size, maxTimeMins, initialTime);
		processTimer = new ProcessTimerWidget("Process Timer", size,currentTimeMins);
		
		HBox hbox = new HBox();
		hbox.getChildren().addAll(processTimer,pushTimer);
		
		Tooltip t = new Tooltip(tooltipMessage);
		Tooltip.install(hbox, t);
		
		setCenter(hbox);
	}
	
	private String calculatePushTimes() {
		ProcessPoint processPoint =  ClientMainFx.getInstance().getApplicationContext().getProcessPoint();
		DailyDepartmentScheduleUtil scheduleUtil = new DailyDepartmentScheduleUtil(processPoint);
		
		DailyDepartmentSchedule current = scheduleUtil.getFirstWorkPeriodStartTime(shift);
		DailyDepartmentSchedule last = scheduleUtil.getLastWorkPeriodStartTime(shift);
		
		pushTime = new ArrayList<Date>();
		SortedMap<Date,Integer>  scheduleAdjustmentMap = loadAdjustmentList();
	    int adjustmentMinutes=0;
		
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(current.getStartTime());

		
		while (current != null && calendar.getTime().before(last.getEndTime())) {
        	if (isDebug) {
        		System.out.printf("Current: period=%d, label=%s, startTime=%tR, endTime=%tR%n",current.getId().getPeriod(),current.getPeriodLabel(),current.getStartTime(),current.getEndTime());
        	}
			
        	adjustmentMinutes = calculateCumulativeAdjustment(current, scheduleAdjustmentMap);
        	
            calendar.add(Calendar.MINUTE,pushTimeMins);
            calendar.add(Calendar.MINUTE,adjustmentMinutes);
            

        	// if the calculated Push time is after the end of the current schedule, skip over work schedule items
        	// and if the work schedule is a break i.e. plan = N, delay the push time by the break time.
            while ((calendar.getTime().after(current.getEndTime()) || current.getPlan().equals("N")) && calendar.getTime().before(last.getEndTime()) ) {
            	current  = scheduleUtil.getScheduleFollowing(current);
            	
            	if (current == null ) {
            	  break;	
            	}

            	if (isDebug) {
            	   System.out.printf("%cNext: period=%d, label=%s, startTime=%tR, endTime=%tR%n",'\t',current.getId().getPeriod(),current.getPeriodLabel(),current.getStartTime(),current.getEndTime());
            	}            	
            	if (current.getPlan().equals("N")) {
            		int timeAmt = (int) (current.getEndTime().getTime() -  current.getStartTime().getTime());
            		calendar.add(Calendar.MILLISECOND, timeAmt);
            		if (isDebug) {
                	  System.out.printf("%cScheduled non-working time add %d mins to push time%n",'\t',timeAmt  / (1000 * 60));
            		}
            	}
            }
            
        	if (isDebug) {
        	   System.out.printf("%n%cPush Time = %tR [%2$tc]%n%n",'\t',calendar.getTime());
        	}
        	pushTime.add(calendar.getTime());
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Push Times%n");
		for ( Date date : pushTime) {
			sb.append(String.format("%tR%n",date));
		}
		
		return sb.toString();
 	}
	
	private int calculateCumulativeAdjustment(DailyDepartmentSchedule current,SortedMap<Date, Integer> adjustmentMap) {
		int adjustmentTime=0;
		
		for (Date date : adjustmentMap.keySet()) {
			if (DailyDepartmentScheduleUtil.isToday(date) &&
                toScheduleTime(date).compareTo(toScheduleTime(current.getStartTime())) >= 0 && 
				toScheduleTime(date).compareTo(toScheduleTime(current.getEndTime())) <= 0 ) {
				int minutes = adjustmentMap.get(date);
				adjustmentTime+=minutes;
				adjustmentMap.put(date, 0);
	        	if (isDebug) {
	               System.out.printf("%cAdj:  time=%tR, adj=%d,  cumulativeAdj=%d%n",'\t',date,minutes,adjustmentTime);
	         	}				
			}
		}
		
		return adjustmentTime;
	}
	
	private static Comparator<Date> dateComparator = new Comparator<Date>() {
	    public int compare( Date date1, Date date2) {
	    	return  date1.compareTo(date2);
	    }
	};
	
	private SortedMap<Date, Integer> loadAdjustmentList() {
		
		SortedMap<Date,Integer>  adjustments = new TreeMap<Date, Integer>(dateComparator);
		String adjustmentList = PropertyService.getPropertyBean(PDDAPropertyBean.class).getPushTimerAdjustment();
		
		String dateFormat = "MM/d/yy HH:mm";
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);

		for (String adj : adjustmentList.split(",")) {
			adj = adj.trim();
			try {
				Date start = df.parse(adj);
				int mins = Integer.parseInt(adj.substring(dateFormat.length() + 1).trim());
				if (mins > 0  && mins < 60) {
					adjustments.put(start, mins);
				} else {
					getLogger().warn("Adjustment out of range! Value ignored");
				}
			} catch (ParseException e) {
				getLogger().warn("Invalid Date value ignored!" + e.getMessage());
			}
		}
		return adjustments;
	}
	
	public int getTimeToNextPush() {
		int timeToNextPush = 0;

        Date currentTime = toScheduleTime(new Date());
        
		for (Date nextPushTime : pushTime) {
//		System.out.println(" nextPushTime " + nextPushTime + " currentTime  " +  currentTime.getTime());
	       if (!nextPushTime.before(currentTime)) {
	    	   timeToNextPush = (int)(nextPushTime.getTime()- currentTime.getTime()) /(1000 * 60);
	    	   if (isDebug) {
	           	   System.out.printf("%n%cgetTimeToNextPush in minutes = %d%n%n",'\t',timeToNextPush);
	    	   }
	    	   return timeToNextPush ;
	       }
	    }
		return timeToNextPush;
	}

	private Date toScheduleTime(Date date) {
	  // GAL226TBX schedule start and end times are set for the day 01 JAN 1970 so we need to convert our current date fields accordingly

      Calendar sourceDate = new GregorianCalendar();
      sourceDate.setTime(date);
		
	  Calendar targetTime = new GregorianCalendar();
	  targetTime.setLenient(false);
	  targetTime.set(1970,Calendar.JANUARY,1,sourceDate.get(Calendar.HOUR_OF_DAY), sourceDate.get(Calendar.MINUTE),sourceDate.get(Calendar.SECOND));
	  
	  return targetTime.getTime();
	}

	public void setProcessTimer(float timeMins) {
		if (processTimer != null) {
		   processTimer.setProcessTime(timeMins);
		}
	}
	
	public void advanceProcessTimer(float timeMins) {
		if (processTimer != null) {
		   processTimer.advanceProcessTimer(timeMins);
		}
	}
	
	private void update() {
		long deltaTime = getDeltaTimeMs();
		pushTimer.update(deltaTime);
		processTimer.update(deltaTime);
	}
	
	private long getDeltaTimeMs() {
		long deltaTime;
		long now = System.currentTimeMillis();
		
		if (lastUpdate == 0) {
			lastUpdate = now;
		}
		deltaTime = now - lastUpdate;
		lastUpdate = now;
		return deltaTime;
	}
	
	public void startClock() {
		if (timer!=null) {
			timer.stop();
		}
		
	    timer =  new Timeline(new KeyFrame(Duration.seconds(1),new EventHandler<javafx.event.ActionEvent>() {
		  public void handle(javafx.event.ActionEvent event) {
			  update();
		  };
		}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}
	
	public void stopClock() {
		timer.stop();
	}
		
	static class PushTimerWidget extends BorderPane{
	   	private final int ANGLE_ORIGIN = 90;  // The 12 o'clock position
		private String caption;               // title above dial
		private int size;                     // size of the dial
		protected float maxTimeMins;          // the maximum value the dial can show
   		private float pushTimeRemainingMins;  // The amount of time remaining before the next push
		protected float currentTimeMins;
		protected long deltaTimeMs;           // the amount of time that has passed since the last gui update.
	   	
	   	public PushTimerWidget(String caption, int size, float maxTimeMins, int currentTimeMins) {
	   		this.caption = caption;
	   		this.size = size;
	   		this.maxTimeMins = maxTimeMins;
	   		this.currentTimeMins = currentTimeMins;
	   	}
	   	
	   	public float getPushTimeRemainingMins() {
	   		return pushTimeRemainingMins;
	   	}
	   	
	   	protected double getArcAngle() {
	   		double angle = Math.min ((360.0 / (maxTimeMins )) * currentTimeMins, 360);
	   		return angle;
	   	}
	   	
	   	protected ArcColor getArcColor() {
	   	    return ArcColor.TIMER;	
	   	}
	   	
	   	protected String getLabelCaption() {
	   		return String.format("%d",(int)Math.ceil(pushTimeRemainingMins));
	   	}
	
		protected void update(long deltaTimeMs) {
			int dialRadius = size / 2;
			float innerCircleRadius = dialRadius * 0.50f;
			int fontSize = size / 4;
			
			this.deltaTimeMs = deltaTimeMs;
			if (deltaTimeMs > 0) {
			   currentTimeMins += ((float)deltaTimeMs / (MILLIS * 60));
			   currentTimeMins = Math.max(0,currentTimeMins);
			}
			
			pushTimeRemainingMins = Math.max(maxTimeMins - currentTimeMins,0.0f);

			VBox vbox = new VBox();
			Text title = UiFactory.createText();
			title.setText(caption);
			title.setFont(new Font(size * 0.15));
			title.setWrappingWidth(size);
			title.setTextAlignment(TextAlignment.CENTER);
			title.setX((size - title.getLayoutBounds().getWidth() / 2));
			vbox.setAlignment(Pos.CENTER);

			Group root = new Group();

			Circle outline = new Circle();
			outline.setRadius(dialRadius);
			outline.setCenterX(dialRadius);
			outline.setCenterY(dialRadius);
			outline.setStroke(Color.DARKGRAY);
			outline.setFill(getArcColor().bg());

			Arc arc = new Arc();
			arc.setFill(getArcColor().fg());
			arc.setRadiusX(dialRadius);
			arc.setRadiusY(dialRadius);
			/*
			 * JavaFx draws and arc in anti-clockwise direction so we have to draw
			 * the arc from the end position to the 12 o'clock position/.
			 */
			double angle = getArcAngle();
			arc.setStartAngle(ANGLE_ORIGIN - angle);
			arc.setLength(angle);
			arc.setCenterX(dialRadius);
			arc.setCenterY(dialRadius);
			arc.setStroke(Color.DARKGRAY);
			arc.setType(ArcType.ROUND);

			Text currentValueText = UiFactory.createText();
			currentValueText.setFont(new Font(30));
			currentValueText.setWrappingWidth(200);
			currentValueText.setTextAlignment(TextAlignment.CENTER);
		
			currentValueText.setText(getLabelCaption());
			currentValueText.setFont(Font.font("Verdana", fontSize));
			double height = currentValueText.getLayoutBounds().getHeight();
			double width = currentValueText.getLayoutBounds().getWidth();
			currentValueText.setX((size - width) / 2);
			currentValueText.setY((size + (height * 0.75 )) / 2);

			Circle circle = new Circle();
			circle.setRadius(innerCircleRadius);
			circle.setCenterX(dialRadius);
			circle.setCenterY(dialRadius);
			circle.setFill(Color.LIGHTGRAY);
			circle.setStroke(Color.DARKGRAY);
			root.getChildren().addAll(outline, arc, circle, currentValueText);
			vbox.getChildren().addAll(root,title);
			setCenter(vbox);
		}
	}
	
	class ProcessTimerWidget extends PushTimerWidget {
		private float processTimeRemainingMins;
		
	   	public  ProcessTimerWidget(String caption, int size, int currentTimeMins) {
	   		super(caption, size,0, currentTimeMins);
	   	}
	   	
	   	protected double getArcAngle() {
	   		double angle = Math.min ((360.0 / maxTimeMins) * (maxTimeMins - processTimeRemainingMins), 360);
	   		return angle;
	   	}
	   	
	   	private float calculatePcntDifference() {
	   		float pcntDifference=-1;
	   		
			/* See  Note 1 :  The push timer PushTime may be greater than the ProcessTime PushTime*/
	   		if (pushTimer.getPushTimeRemainingMins() <= PushTimerPane.pushTimeMins) {
	   		  pcntDifference = ((processTimeRemainingMins -  pushTimer.getPushTimeRemainingMins())/maxTimeMins) * 100.0f;
	   		} 
	   		
	   	//	System.out.printf("percent diff %2.2f %n", pcntDifference);
	   		return pcntDifference;
	   	}
	   	
	   	protected ArcColor getArcColor() {
	   		float pcntDifference =  calculatePcntDifference();
	   		if (pcntDifference >= 0) {
	   			pcntDifference = Math.abs(pcntDifference);
	   			if ((pushTimer.getPushTimeRemainingMins() == 0.0 && pcntDifference > 0.0)  || pcntDifference > limitWarn ) {
	   				return ArcColor.LATE;
	   			} else if (pcntDifference < limitOK) {
	   				return ArcColor.ONTIME;
				} else {
	   				return ArcColor.DELAYED;
				}
	   		} else {
   				return ArcColor.ONTIME;
	   		}
	   	}
	   	
	   	protected String getLabelCaption() {
	   		return String.format("%d",(int)processTimeRemainingMins);
	   	}
	   	
	   	public void advanceProcessTimer(float timeMins) {
        	if (isDebug) {
	   		   System.out.printf("## processTimeRemaining - timeMins. %f - %f%n",  this.processTimeRemainingMins, timeMins );
        	}
	   		this.processTimeRemainingMins = Math.max(0, this.processTimeRemainingMins- timeMins) ;

        	if (isDebug) {
	   		   System.out.printf("## processTimeRemaining=%f %n", this.processTimeRemainingMins);
        	}
	   	}
	   	
	   	public void setProcessTime( float timeMins) {
	   	   	if (isDebug) {
	   	   		System.out.printf("## setProcessTimer(%f)%n", timeMins );
	        }
	   		maxTimeMins = processTimeRemainingMins = timeMins;
	   	}
	   	
		@Override
		protected void  update(long deltaTime) {
			super.update(deltaTime);
			Platform.runLater(new Runnable() {
				public void run() {
					try {
					calculateWorkComplete();
					} catch (Exception ex) {}
				}
			});			
		}
	}

	@Override
	protected void processProductStarted(ProductModel productModel) {
		init(widgetSize, pushTimeMins,currentTimeMins);
		startClock();
	}

	private void calculateWorkComplete() {
		if (hasWorkcompleteChanged) {
		   hasWorkcompleteChanged = false;

		   String productId = getProductController().getModel().getProductId();
		   String processPointId = ApplicationContext.getInstance().getApplicationId();
		   float  workCompleted = 0.0f;

	       monitor.setOpsPlanned(getUnitOfOperationList(productId, processPointId).size());
	       setProcessTimer(getTotalUnitTimeMins(getUnitOfOperationList(productId, processPointId)));
	    
	       Collections.sort(getUnitOfOperationList(productId, processPointId), getComparator());
	    
  	       // Get a list of parts that are already installed at the process point
	       InstalledPartDao installedPartDao  =  ServiceFactory.getDao(InstalledPartDao.class);
	       List<InstalledPart> installedPartList = installedPartDao.findAllByProductIdAndProcessPoint(productId, processPointId);
	       monitor.setOpsCompleted(installedPartList.size());
	       
 	       // For each installed part add up the installation times.
	       for (InstalledPart part : installedPartList) {
	    	  if (part.isStatusOk()) {
	    		  PddaUnitOfOperation unit = getPddaUnitOfOperation(part.getPartName(), productId, processPointId);
	    		  if (unit != null) {
	    			  workCompleted += unit.getUnitTotalTime();
	    	  	  }
	    	  }
	       }
	       workCompleted /= 60;
		   advanceProcessTimer(workCompleted);
		}
		long progress = (long) ((pushTimer.getPushTimeRemainingMins() - processTimer.processTimeRemainingMins) * 60);
		monitor.updateStatus(getProductController().getModel().getProductId(), progress);
	}

	private float getTotalUnitTimeSecs(List<PddaUnitOfOperation> unitOfOperationList) {
	    float totalTimeinSecs = 0.0f;
	    
	    for (PddaUnitOfOperation p : unitOfOperationList) {
	    	totalTimeinSecs += p.getUnitTotalTime();
	    }
	    return totalTimeinSecs;
	}
	
	private float getTotalUnitTimeMins(List<PddaUnitOfOperation> unitOfOperationList) {
	    return getTotalUnitTimeSecs(unitOfOperationList)/60;
	}
	
	@Override
	protected void processProductCancelled(ProductModel productModel) {
		stopClock();
	}

	@Override
	protected void processProductFinished(ProductModel productModel) {
		stopClock();
	}
		
	@Subscribe
	public void received(DataCollectionResultEvent event) {
		if (event.getType().equals(DataCollectionResultEventType.DC_COMPLETED_FOR_PART)      ||
			event.getType().equals(DataCollectionResultEventType.DC_REJECTED_FOR_PART)       ||
			event.getType().equals(DataCollectionResultEventType.VALID_MEASUREMENT_RECEIVED) ||
			event.getType().equals(DataCollectionResultEventType.REJECT_MEASUREMENT_RECEIVED)) {
		
			hasWorkcompleteChanged = true;
		}
	} 
	
	public List<PddaUnitOfOperation> getUnitOfOperationList(String productId, String processPointId) {
		return PushTimerContext.getInstance().getUnitOfOperationList(productId, processPointId);
	}
	
	public PddaUnitOfOperation getPddaUnitOfOperation(String partName, String productId, String processPointId) {
		return PushTimerContext.getInstance().getPddaUnitOfOperation(partName, productId, processPointId);
	}
	
	public Comparator<PddaUnitOfOperation> getComparator() {
		return PushTimerContext.pddaUnitOfOperationComparator;
	}
}
