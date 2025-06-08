package com.honda.galc.logserver;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.AbstractPropertyConfig;

/**
 * 
 * <h3>LogServerConfig Class description</h3>
 * <p> LogServerConfig description </p>
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
 * @author Jeffray Huang<br>
 * May 17, 2010
 *
 */
public class LogServerConfig extends AbstractPropertyConfig{
    
    private static final int DEFAULT_PORT = 55500;
    private static final String SERVER_PORT = "SERVER_PORT";
    private static final String CONFIG_RESOURCE_PATH="logserver.properties";
    private static final String CENTER_LOGGING = "CENTER_LOGGING";
    private static final String CENTER_LOG_NAME ="CENTER_LOG_NAME";
    private static final String CENTER_LOG_LEVEL ="CENTER_LOG_LEVEL";
    private static final String CENTER_LOG_ROLLOVER ="CENTER_LOG_ROLLOVER";
    
    private static final String TIVOLI_LOG_NAME ="TIVOLI_LOG_NAME";
    private static final String TIVOLI_LOG_LEVEL ="TIVOLI_LOG_LEVEL";
    
    private static final String LOG_FILE_PATH ="LOG_FILE_PATH";
    private static final String LOG_PER_APP ="LOG_PER_APP";
    private static final String SHIFT_DIR ="SHIFT_DIR";
    private static final String SHIFT_START_TIMES ="SHIFT_START_TIMES";
    
    
    /**
     * This constant is the name of the System property that is used as the "preferred suffix"
     * for retrieving the component-list and PropertyServices properties.
     */
    private static String SYSTEM_PROPERTY_INIT_PREFERRED_SUFFIX = 
    	"PropertyServices.InitialPreferredSuffix";

    
    public LogServerConfig() {
        
    	super(CONFIG_RESOURCE_PATH,SYSTEM_PROPERTY_INIT_PREFERRED_SUFFIX);
        
    }
    
    public int getServerPort(){
        return getPropertyInt(SERVER_PORT, DEFAULT_PORT);
    }
    
    public String getCenterLogName() {
        return getProperty(CENTER_LOG_NAME,"GCL");
    }
    
    public String getCenterLogLevel() {
        return getProperty(CENTER_LOG_LEVEL, "Trace");
    }
    
    public String getTivoliLogName() {
        return getProperty(TIVOLI_LOG_NAME,"TVL");
    }
    
    
    public String getTivoliLogLevel() {
        return getProperty(TIVOLI_LOG_LEVEL, "Emergency");
    }
    
    public String getLogFilePath(){
        return getProperty(LOG_FILE_PATH,".");
    }
    
    public boolean isLogPerApp(){
        return getPropertyBoolean(LOG_PER_APP, true);
    }
    
    public boolean isShiftDir(){
        return getPropertyBoolean(SHIFT_DIR, false);
    }

    public int[] getShiftStartTimes() {
    	String value = getProperty(SHIFT_START_TIMES,"");
    	if (value.matches("[0-9]+:[0-9]+,[0-9]+:[0-9]+") != true)  {
    		return null;
    	} else {
    	   String part[] = value.split("[,:]");
    	   int time []  = new int[4];
    	   time[0] = Integer.parseInt(part[0]);
    	   time[1] = Integer.parseInt(part[1]);
    	   time[2] = Integer.parseInt(part[2]);
    	   time[3] = Integer.parseInt(part[3]);
    	   return time;
    	}
    }
    
    public boolean isCenterLogging(){
    	return getPropertyBoolean(CENTER_LOGGING, false);
    }
    
    public List<int[]> getCenterLogRolloverTimes() {
    	List<int[]> times = new ArrayList<int[]>();
    	String value = getProperty(CENTER_LOG_ROLLOVER,"");
    	if(StringUtils.isEmpty(value)) return times;
    	String[] values = value.split(",");
    	for(String tmpValue :values) {
    		String[] tmpValues = tmpValue.split(":");
    		int[] time = new int[2];
    		if(tmpValues.length <=1) return new ArrayList<int[]>();
    		time[0] = Integer.parseInt(tmpValues[0]);
    		time[1] = Integer.parseInt(tmpValues[1]);
    		times.add(time);
    	}
    	return times;
    	
    }
 
}
