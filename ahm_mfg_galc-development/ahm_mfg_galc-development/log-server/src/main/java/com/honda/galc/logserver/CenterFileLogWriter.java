package com.honda.galc.logserver;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.LogRecord;

public class CenterFileLogWriter extends FileLogWriter{
	
	private final int MAX_CHARS_PER_LINE = 256;
	
	private List<int[]> rolloverTimes;
	
	private String dateNumber;
	
	public CenterFileLogWriter(String path, String name,List<int[]> rolloverTimes,LogLevel logLevel) {
		super(path, name,logLevel);
		this.rolloverTimes = rolloverTimes;
		maxChars = MAX_CHARS_PER_LINE;
	}
	
	@Override
    public void processItem(LogRecord item) {
		
		if(!needLog(item)) return;
		log(item.getSingleLineMessage(maxChars));
		
    }
	
	private boolean needLog(LogRecord item) {
		return item.isCenterLog();
	}
	
	@Override
    protected void checkForRollover() {
		
		if(isShiftDirectory || rolloverTimes == null || rolloverTimes.size() <=1) {
			super.checkForRollover();
			return;
		}
        
		dateNumber = getDateNumber();
		
		if(!dateNumber.equals(currentFileNumber)) {
			resetFile();
		}
    }
	
	private String getDateNumber() {
		int index = 0;
		GregorianCalendar now = new GregorianCalendar();
		
		for(int[] time : rolloverTimes) {
			GregorianCalendar calendar = (GregorianCalendar)now.clone();
			calendar.set(Calendar.HOUR_OF_DAY,time[0]);
			calendar.set(Calendar.MINUTE,time[1]);
			calendar.set(Calendar.SECOND,00);
			if(now.after(calendar)) index++;
		}
		
		if(index == 0) {
			now.add(Calendar.DATE, -1);
			index = rolloverTimes.size();
		}
		return String.format("%tF",now.getTimeInMillis()) + "_" + index;
	}
	
	public  String getNewFileName() {

		if(isShiftDirectory || rolloverTimes == null || rolloverTimes.size() <=1) 
			return super.getNewFileName();
			
		currentFileNumber = dateNumber;

    	return (getPath() + fileName + "_" + currentFileNumber + ".log");
    }

	
	
	
	

}
