package com.honda.galc.entity.enumtype;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;
import com.honda.galc.qi.constant.QiConstant;
/**
 * 
 * <h3>QiEntryStationConfigurationSettings Class description</h3>
 * <p>
 * QiEntryStationConfigurationSettings contains the setting values for an Entry station
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 */
public enum QiEntryStationConfigurationSettings implements IdEnum<QiEntryStationConfigurationSettings> {
	HIGHLIGHT(1,"Highlight","Highlight existing image sections","Yes","Yes","No"),
	DUPLICATE(2,"Duplicate","Duplicate entry warning dialog","Reject duplicate with warning","Accept duplicate entry without warning","Accept duplicate with warning"),
	RESPONSIBILITY(3,"Responsibility","Allow user to overwrite responsibility","No","Yes","No"),
	DEFECTS(4,"Defects","Show all previous defects on image","Yes","Yes","No"),
	REPORTABLE(5,"Reportable","Reportable station (overwrite part defect combination reportable setting)","Yes","Yes","No"),
	SCRAP(6,"Scrap","Need Scrap function for Repair","No","Yes","No"),
	GATHER_ASSOCIATE_ID(7,"Gather Entry","Gather Entry Associate ID","Yes","Yes","No"),
	DEFECT_LIST(8,"Range","Recent Defect List Range (# of hours)","8","10","15"),
	STATION_RESPONSIBILITY(9,"Repair","Actual Problem Responsibility override original Defect Responsibility","Yes","Yes","No"),
	REPAIR_COMMMENT(10,"Repair Comment","Allow to add comments while adding repair methods","Yes","Yes","No"),
	DEFAULT_UPC_QUANTITY(11,"Default Qty","Default quantity for UPC"," ","1","2"),
	REPAIR_PARKING_STATION(12,"Repair Parking Station","Allow print on assigning unit","No","Yes","No"),
	UPDATE_REPAIR_AREA(13,"Update Repair Area","Allow to update repair area","No","Yes","No"),
	CHANGE_TO_FIXED(14,"Change To Fixed","Allow user to change the current defect status from Not Fixed to Fixed","No","Yes","No"),
	SEND_TO_IN_TRANSIT(15,"Send Product In Transit","Allow to send product in transit","No","Yes","No"),
	DISABLE_ACCEPT_DEFECT(16,"Popup Accept Button","Popup Accept button on Defect Entry Screen","No","Yes","No"),
	ENTRY_SCREEN_COLUMN_NUMBER(17,"Entry Screen Column Number","Set number of columns for Entry Screen Panel","2","1","3"),
	SCRAP_REASON(18,"Scrap Reason","Reason for Scrap is required","No","Yes","No"),
	MAX_UPC_QUANTITY(19,"Max UPC quantity ","Maximum Quantity for UPC","100","200","300"),
	ADD_COMMENT_FOR_CHANGING_RESPONSIBILITY(20,"Add comment for defect", "Add comment for changing responsibility of defect","Yes","Yes","No"),
	TRAINING_MODE(21,"Training Mode","Enable Training Mode Button","Yes","Yes","No"),
	DEFAULT_REPAIR_METHOD_FIX(22,"Default Repair Method Fix","Set default value for repair method fix","No","Yes","No"),
	MAX_PROCESSED_LIST_SZ(23, "Max processed products", "Max number of rows to display in processed products", "30", "30", "50"),
	MOST_FREQ_USED_DURATION(24, "Most frequently used duration", "Max frequently used defects over this many hours/days", "0h", "1h", "14d"),
	MAX_MOST_FREQ_USED_SZ(25, "Most frequently used list size", "Max number of items to display in most frequently used list", "10", "1", "10"),
	SHOW_ACCEPT_BUTTON(26,"Do you want to use Accept button on Defect Entry?","No-->Defect automatically accepted when part and defect selected","Yes","Yes","No"),
	REDIRECT_TO_DEFECT_ENTRY(27,"Redirect to defect entry","Redirecting from precheck to defect entry, & flashing precheck for configurable time","-1","20","30"),
	AUTO_PRODUCT_INPUT(28,"Automatic input of product sequence","YES=Automatically enter next in product sequence","No","Yes","No"),
	AUTO_INPUT_POLL_FREQ(29,"Polling frequency in sec automatic input of product sequence","numeric value in sec, 0=no polling","5","1","15"),
	SHOW_L2_L3(30,"Show level 2 and level 3 responsibility?","Allow user to change L2 and L3 responsibility","No","Yes","No"),
	ASSIGN_ACTUAL_PROBLEM_AFTER_SCRAP(31, "Assign Actual Problem After Scrap", "Assign Actual Product to Defect after the part has been scrapped", "No", "Yes", "No"),
	IN_REPAIR_ENTRY_PROMPT_FOR_IS_THIS_CAUSED_DURING_REPAIR(32,"In Repair Entry Prompt for\"Is this caused during Repair\"","Repair station when adding new defect to Turn prompt off or default is on"," ","Off","On"),
	KICKOUT_LOCATION(33,"Kickout Process Location Entry", "Allow input of kickout process location", "No", "Yes", "No"),
	MULTI_SELECT_REPAIRS(34, "Multi-Select Repairs", "Allow user to do the multiple select repairs", "Yes", "Yes", "No"),
	IQS_INPUT(35,"Collect IQS Score at this Station","Add ability to assign an IQS Score to a defect during Defect Entry","No","Yes","No"),
	RESET_TO_DEFAULT_DEFECT_STATUS(36, "Reset to default defect status", "Reset Defect Status panel to default status after defect entry","No","Yes","No"),
	DISABLE_CANCEL(37,"Disable Cancel Button","Yes-->Cancel button will be disable, user has no other option than add defect or direct pass","No","Yes","No"),
	OPEN_EXISTING_DEFECTS(38,"Open Existing Defects view on tab selection","Existing Defects view is opened automatically when Defect Entry tab is selected","No","Yes","No"),
	MBPN_DEFECT_ENTRY_LIMIT_TO_PRODUCT_ID_PART(39,"MBPN Defect Entry Limit to product id Part","Yes=Only MBPN Part scanned part can be entered; No=Any Part on MBPN can be entered","No","Yes","No"),
	IQS_AUDIT_ACTION_INPUT(40,"Collect IQS Audit Action at this Station","Add ability to assign an IQS Audit Action to a defect during Defect Entry (requires IQS Score)","No","Yes","No"),
	DAILY_REFRESH_CACHE_TIME(41, "Daily refresh cache time", "Time of day for automatic full cache refresh", QiConstant.NONE, "00:00", "23:00"),
	SHOW_ACTUAL_PROBLEM_REPAIRED(42, "Enable Repaired Option for Actual Problem", "Allow user to set Repaired status for Actual Problem", "No", "Yes", "No"),
	PLAY_NGSOUND_AT_DEFECT_ENTRY(43, "Play NG sound for bad scan", "Play NG sound if users hit enter or scan a barcode at defect entry", "No", "Yes", "No"),
	NO_PROBLEM_FOUND(44, "No Problem Found", "Allow user to change the current defect status from Not Fixed to No Problem Found", "No", "Yes", "No"),
	GDP_PROCESS_POINT(45, "GDP Process Point", "GDP Process Point/Station", "No", "Yes", "No"),
	TRPU_PROCESS_POINT(46, "TRPU Process Point", "TRPU Process Point/Station", "No", "Yes", "No"),
	LET_DEFECT_CREATION(47,"LET Defect Creation","If enabled the process point will check for LET Defects when a VIN is scanned and create the 1st Defect in the list.","No","Yes","No");
	
	private int id;
	private String settingName;
	private String settingsComment;
	private String defaultPropertyValue;
	private String availablePropertyValue1;
	private String availablePropertyValue2;
	private static int MAX_INT_LIST = 100;

	private QiEntryStationConfigurationSettings(int id,String settingName, String comment, String defaultPropertyValue,String availableropertyValue1,String availableropertyValue2 ){
		this.id=id;
		this.settingName=settingName;
		this.settingsComment=comment;
		this.defaultPropertyValue=defaultPropertyValue;
		this.availablePropertyValue1=availableropertyValue1;
		this.availablePropertyValue2=availableropertyValue2;
	}
	public String getSettingsName() {
		return StringUtils.trimToEmpty(settingName);
	}

	public String getSettingsComment() {
		return StringUtils.trimToEmpty(settingsComment);
	}

	public String getDefaultPropertyValue() {
		return StringUtils.trimToEmpty(defaultPropertyValue);
	}
	public String getAvailablePropertyValue1() {
		return StringUtils.trimToEmpty(availablePropertyValue1);
	}
	public String getAvailablePropertyValue2() {
		return StringUtils.trimToEmpty(availablePropertyValue2);
	}
		
	public boolean isUpdateSettingsPropertyBtnDisabled() {
		switch (this) {
		case HIGHLIGHT:
		case DEFECTS:
		case DEFECT_LIST:
		case STATION_RESPONSIBILITY:
		case REPAIR_COMMMENT:
		case DEFAULT_UPC_QUANTITY:
		case MAX_MOST_FREQ_USED_SZ:
		case IN_REPAIR_ENTRY_PROMPT_FOR_IS_THIS_CAUSED_DURING_REPAIR:
		case MULTI_SELECT_REPAIRS:
			return true;
		default:
			return false;
		}
	}

	public boolean isSettingTypeNumber(){
		switch (this) {
		case AUTO_INPUT_POLL_FREQ:
		case DAILY_REFRESH_CACHE_TIME:
		case DEFAULT_UPC_QUANTITY:
		case DEFECT_LIST:
		case MAX_MOST_FREQ_USED_SZ:
		case MOST_FREQ_USED_DURATION:
		case REDIRECT_TO_DEFECT_ENTRY:
			return true;
		default:
			return false;
		}
	}
	public boolean isMostFrequentlyUsedDuration(){		
		return (this.getId() == MOST_FREQ_USED_DURATION.id);
	}
	public boolean isMostFreqListSize(){		
		return (this.getId() == MAX_MOST_FREQ_USED_SZ.id);
	}

	public boolean isAutoInputPollFreq(){		
		return (this.getId() == AUTO_INPUT_POLL_FREQ.id);
	}

	public boolean isRedirectToDefectEntrySize(){		
		return (this.getId() == REDIRECT_TO_DEFECT_ENTRY.id);
	}

	public boolean isReportableSetting(){
		return  this.getId()==5;
	}

	public  Set<String> getPropertyValueByString(){
		Set<String> propertyValueSet = new HashSet<String>();
		propertyValueSet.add(getDefaultPropertyValue());
		propertyValueSet.add(getAvailablePropertyValue1());
		propertyValueSet.add(getAvailablePropertyValue2());
		return propertyValueSet;
	}
	public  List<String> getPropertyValueByNumber(){
		if(isMostFrequentlyUsedDuration())  {
			return getFrequentlyUsedRange();
		} else if (this.getId() == DAILY_REFRESH_CACHE_TIME.id) {
			return getDailyRefreshCacheTimeRange();
		}
		int maxVal = MAX_INT_LIST; //100
		List<String> propertyValueSet = new ArrayList<String>();
		int minVal=0;
		if(isMostFreqListSize())  {
			maxVal = 11;
		} else if(isRedirectToDefectEntrySize())  {
			minVal = -1;
			maxVal = 31;
		}

		else if(isAutoInputPollFreq())  {
			maxVal = 16;
		}

		for (Integer index = minVal; index < maxVal; index++)
		{
			propertyValueSet.add(index.toString());
		}
		if(isRedirectToDefectEntrySize())  {
			propertyValueSet.add(2, "0.5");
		}
		return propertyValueSet;
	}



	public List<String> getFrequentlyUsedRange()  {
		List<String> propertyValueSet = new ArrayList<String>();
		//show range from 1 hour to 24 hours to 14 days (1-24h + 2-14d)
		for (int index = 0; index < 25; index++)
		{
			String val = index + "h";
			propertyValueSet.add(String.valueOf(val));
		}
		for(int days = 2; days < 15; days++)  {
			String val = days + "d";
			propertyValueSet.add(String.valueOf(val));	
		}
		return propertyValueSet;
	}

	public List<String> getDailyRefreshCacheTimeRange() {
		List<String> propertyValueSet = new ArrayList<String>();
		propertyValueSet.add(QiConstant.NONE);
		// show hourly range from 00:00 to 23:00
		for (int index = 0; index < 24; index++) {
			String val = StringUtils.leftPad(String.valueOf(index), 2, '0') + ":00";
			propertyValueSet.add(val);
		}
		return propertyValueSet;
	}



	public int getId() {
		return id;
	}
	public static QiEntryStationConfigurationSettings getType(int id) {
		return EnumUtil.getType(QiEntryStationConfigurationSettings.class, id);
	}

}

