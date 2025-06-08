package com.honda.galc.let.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.LetSpool;
import com.honda.galc.let.message.LetDataQueueProcessor;
import com.honda.galc.let.message.LetProcessItem;
import com.honda.galc.letxml.model.FaultCode;
import com.honda.galc.letxml.model.Process;
import com.honda.galc.letxml.model.ReFaultCode;
import com.honda.galc.letxml.model.ReTest;
import com.honda.galc.letxml.model.ReTestAttribute;
import com.honda.galc.letxml.model.ReTestParam;
import com.honda.galc.letxml.model.Test;
import com.honda.galc.letxml.model.TestAttrib;
import com.honda.galc.letxml.model.TestParam;
import com.honda.galc.letxml.model.UnitInTest;
import com.honda.galc.mail.MailContext;
import com.honda.galc.mail.MailSender;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.thoughtworks.xstream.XStream;

/**
 * @author Subu Kathiresan
 * @date Apr 9, 2015
 */
public class LetUtil {

    private static Logger logger;
    private static String PARSE_KEY_PRODUCT_ID      = "VIN=\"";
    private static String PARSE_KEY_TEST_END_TIME   = "TestEndTime=\"";
    private static String PARSE_KEY_CELL            = "Cell=\"";
    private static String PARSE_KEY_BUILD_CODE      = "BuildCode=\"";
    private static String PARSE_KEY_TOTAL_STATUS    = "TotalStatus=\"";
    private static String PARSE_KEY_PRODUCTION      = "Production=\"";
    private static String PARSE_KEY_CONT_STEP_FILE = "ContStepFile=\"";
    private static String ZONE = "ZONE";
    private static String NO_ZONE_FOLDER = "No_Zone";
    
    private static final String UNDERSCORE = "_";
    public static final int HEADER_LENGTH = 16;
    
    private static final String LET_ALERT = "LET Alert";
    
    private static XStream unitInTestXmlConvertor;
    
    static {
        unitInTestXmlConvertor = createUnitInTestXmlConvertor();
    }
    
    /**
     * provides the location to save the xml file 
     * 
     * @param date
     * @param validXml
     * @return
     */
    public static String getXmlSaveLocation(Date date, boolean validXml, LetSpool spool, StringBuilder msg){
        if(validXml){
            SimpleDateFormat ymdfmt = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat ymfmt =  new SimpleDateFormat("yyyyMM");
            SimpleDateFormat yfmt =  new SimpleDateFormat("yyyy");
        
            String xmlSaveLocationSuffix = File.separator + spool.getEnvName() + File.separator + spool.getLineName() + File.separator;
            
            if (getLetPropertyBean().isGroupFilesByZone()) {
                String contStepFile = getAttribVal(msg, PARSE_KEY_CONT_STEP_FILE);
                String zone = parseZone(contStepFile);
                xmlSaveLocationSuffix = xmlSaveLocationSuffix + (StringUtils.isEmpty(zone) ? NO_ZONE_FOLDER : zone) +  File.separator;
            }
            
            return spool.getXmlSaveLocation() + yfmt.format(date) + File.separator + ymfmt.format(date) + File.separator + ymdfmt.format(date)+ xmlSaveLocationSuffix;
        }
        return spool.getExceptionSaveLocation();
    }
    
    /**
     * converts a UnitInTest xml to an UnitInTest object using XStream parser
     * 
     * @param receivedXml
     * @param msgKey
     * @return
     */
    public static UnitInTest convertToUnitInTest(String receivedXml, String msgKey) {   
        
        long startTime = System.currentTimeMillis();
        
        UnitInTest unitInTest = null;
        
        try {
            unitInTest = (UnitInTest) getUnitInTestXmlConvertor().fromXML(receivedXml);
        } catch (Exception ex) {
            ex.printStackTrace();
            getLogger().error(ex, "Error converting LET message to UnitInTest");
            return null;
        } 
        
        getLogger().info("Converting to UnitInTest for msg " + msgKey + " took " + (System.currentTimeMillis() - startTime) + " milliseconds");
        return unitInTest;
    }   
    
    public static LetPropertyBean getLetPropertyBean() {
        return PropertyService.getPropertyBean(LetPropertyBean.class);
    }
    
    /**
     * creates a new LetProcessItem based on the message 
     * 
     * @param strB
     * @param nodeName
     * @return
     */
    public static LetProcessItem createProcessRequest(StringBuilder strB, String nodeName, LetSpool spool) {
        long startTime = System.currentTimeMillis();
        StringBuilder msgKey = new StringBuilder();
        ArrayList<String> missingAttribs = new ArrayList<String>();
        String msgType = strB.toString().substring(9, 13);
        String terminalId = strB.toString().substring(6, 9);    
        if (addVal(msgKey, getAttribVal(strB, PARSE_KEY_PRODUCT_ID, missingAttribs))
                & addVal(msgKey, getAttribVal(strB, PARSE_KEY_TEST_END_TIME, missingAttribs)
                        .replace("\"", "")
                        .replace("-", "")
                        .replace(":", "")
                        .replace("T", "_"))
                & addVal(msgKey, getAttribVal(strB, PARSE_KEY_CELL, missingAttribs))
                & addVal(msgKey, getAttribVal(strB, PARSE_KEY_BUILD_CODE, missingAttribs))
                & addVal(msgKey, getAttribVal(strB, PARSE_KEY_TOTAL_STATUS, missingAttribs))
                & addVal(msgKey, getAttribVal(strB, PARSE_KEY_PRODUCTION, missingAttribs))) {
            
            msgKey.append("1");
            
            String contStepFile = getAttribVal(strB, PARSE_KEY_CONT_STEP_FILE);
            String zoneId = parseZoneId(contStepFile);
            msgKey.append(UNDERSCORE);
            if (StringUtils.isBlank(zoneId)) {
                zoneId = "0";
            }
            msgKey.append(zoneId);

            getLogger().check("Creating message key " + msgKey + " took " + (System.currentTimeMillis() - startTime) + " milliseconds");
            
            return new LetProcessItem(strB, msgKey.toString(), spool, true, msgType, terminalId);
        }
        // if parsing is not successful, use date stamp based msgKey
        LetProcessItem item = new LetProcessItem(strB, (new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date()) + LetUtil.UNDERSCORE + nodeName), spool,msgType, terminalId);
        LetUtil.sendAlertEmail("Missing attributes " + missingAttribs + " in request " + item.getFileLocation());
        return item;
    }   
    
    public static long getConvertedTimestamp(String oldDateString) {
        Date oldDate = null;
        String newDateString = null;
        Calendar calendar = Calendar.getInstance();
        final String oldFormat = "yyyy-MM-dd HH:mm:ss";
        final String newFormat = "yyyy-MM-dd HH:mm:ss.SSSSSS";
        DateFormat formatter = new SimpleDateFormat(oldFormat);
        
        try {
            oldDate = formatter.parse(oldDateString);
            ((SimpleDateFormat) formatter).applyPattern(newFormat);
            newDateString = formatter.format(oldDate);
            Date newDate = formatter.parse(newDateString);
            calendar.setTime(newDate);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return calendar.getTimeInMillis();
    }

    /**
     * retrieves attribute value for the provided key 
     * from the raw string message
     * 
     * @param recdStrB
     * @param key
     * @param missingAttribs
     * @return
     */
    public static String getAttribVal(StringBuilder recdStrB, String key, ArrayList<String> missingAttribs) {
        String value = getAttribVal(recdStrB, key);
        if (value == null) {
            missingAttribs.add(key.replace("=\"", ""));
        }
        return value;
    }
    
   public static String getAttribVal(StringBuilder recdStrB, String key) {
        int indexOfKey = recdStrB.indexOf(key);
        if (indexOfKey != -1) {
            StringBuilder tempStrB = new StringBuilder(recdStrB.substring(indexOfKey + key.length()));
            return StringUtils.trimToEmpty(tempStrB.substring(0, tempStrB.indexOf("\"")));
        } else {
            return null;
        }
    }
    
    /**
     * adds attribute value to the message key
     * 
     * @param msgKey
     * @param attribVal
     * @return
     */
    public static boolean addVal(StringBuilder msgKey, String attribVal) {
        if (attribVal != null) {
            msgKey.append(attribVal + UNDERSCORE);
            return true;
        } 
        return false;
    }
    
    /**
     * saves the received message to the file system
     * 1 If xml is valid, saves just the xml part of the message (without the header) with a .xml extension
     * 2 If xml is NOT valid, saves the entire message to a file with a .msg extension
     * 
     * @param processItem
     */
    public static void saveLetXmlAsFile(LetProcessItem processItem) {
        
        long startTime = System.currentTimeMillis();
        FileOutputStream fos = null;
        
        try {
            fos = new FileOutputStream(processItem.getFileLocation());
            if (processItem.isValid()) {
                fos.write(processItem.getReceivedMsg().substring(HEADER_LENGTH).trim().getBytes());
            } else {
                fos.write(processItem.getReceivedMsg().toString().getBytes());
            }
            fos.flush();
            getLogger().check("Received xml saved at: " + processItem.getFileLocation());
            getLogger().check("Saving file " + processItem.getFileLocation() + " took " + (System.currentTimeMillis() - startTime) + " milliseconds");
        } catch(Exception ex){
            ex.printStackTrace();
            getLogger().error(ex, "Unable to write LET xml to file " + processItem.getFileLocation() + ": " + ex.getMessage());
        } finally{
            try {
                fos.close();
            } catch (IOException ex) {}
        }
    }

    /**
     * adds message to the asynchronous process queue
     * 
     * @param processItem
     */
    public static void addMsgToProcessQueue(LetProcessItem processItem) {
        getLogger().info("Attempting to add " + processItem.getMsgKey() + " to LetDataQueueProcessor"); 
        long startTime = System.currentTimeMillis();
        
        LetDataQueueProcessor.getInstance().enqueue(processItem);
        getLogger().info("Adding " + processItem.getMsgKey() + " to LetDataQueueProcessor took " + (System.currentTimeMillis() - startTime) + " milliseconds");
    }
    
    /**
     * creates directories in the xml file save location path, if they
     * don't exist already
     * 
     * @param letXmlSavePath
     */
    public static void createDirs(String letXmlSavePath){
        File serverPath = new File(letXmlSavePath);
        if(!serverPath.exists()){
            if(serverPath.mkdirs())
                getLogger().info("Created directories " + letXmlSavePath);
            else
                getLogger().error("Unable to create directories " + letXmlSavePath);
        }
    }
    
    /**
     * sends alert email asynchronously with exception details
     * 
     * @param alertMessage
     * @param ex
     */
    public static void sendAlertEmail(String alertMessage, Exception ex) {
        sendAlertEmail(alertMessage + "\n\n" + ExceptionUtils.getStackTrace(ex));   
    }
    
    /**
     * sends alert email to the configured group asynchronously
     * 
     * @param alertMessage
     */
    public static void sendAlertEmail(String alertMessage) {
        try {
            MailContext mailContext = new MailContext();
            mailContext.setMessage(alertMessage);
            mailContext.setSubject(LET_ALERT);
            mailContext.setRecipients(getLetPropertyBean().getAlertMailRecipients());
        
            MailSender.sendAsync(mailContext); 
        } catch(Exception ex) {
            ex.printStackTrace();
            getLogger().error(ex, "Unable to send " + LET_ALERT + " email: " + alertMessage );
        }
    }
    
    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger("JcaAdaptor");
        }
        return logger;
    }

    public static double calculateTimeDiffInMins(long startTimeMillis, long endTimeMillis) {
        double timeDiff = endTimeMillis - startTimeMillis; 
        double timeDiffInMin = timeDiff / (60 * 1000);
        timeDiffInMin = Math.round(timeDiffInMin * 100.0);
        timeDiffInMin = timeDiffInMin / 100.0;
        return timeDiffInMin;
    }

   public static String parseZone(String fileName) {
        String zone = "";
        if (StringUtils.isBlank(fileName)) {
            return zone;
        }
        int startIx = fileName.indexOf(ZONE);
        if (startIx < 0) {
            return zone;
        }
        zone = StringUtils.substring(fileName, startIx, startIx + ZONE.length() + 1);
        return zone;
    }
    
    public static String parseZoneId(String fileName) {
        String zoneId = "";
        if (StringUtils.isBlank(fileName)) {
            return zoneId;
        }
        String zoneName = parseZone(fileName);
        if (StringUtils.isBlank(zoneName) || zoneName.length() < ZONE.length() + 1) {
            return zoneId;
        }
        zoneId = StringUtils.substring(zoneName, ZONE.length(), ZONE.length() + 1);
        return zoneId;
    }
    
    private static XStream createUnitInTestXmlConvertor() { 
        XStream xs = new XStream();
        xs.processAnnotations(UnitInTest.class);
        xs.processAnnotations(Process.class);
        xs.processAnnotations(Test.class);
        xs.processAnnotations(TestParam.class);
        xs.processAnnotations(TestAttrib.class);
        xs.processAnnotations(FaultCode.class);
        xs.processAnnotations(ReTest.class);
        xs.processAnnotations(ReTestParam.class);
        xs.processAnnotations(ReTestAttribute.class);
        xs.processAnnotations(ReFaultCode.class);
        return xs;
    }
    
    public static XStream getUnitInTestXmlConvertor() {
        return unitInTestXmlConvertor;
    }
}
