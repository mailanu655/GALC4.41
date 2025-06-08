package com.honda.galc.util;

import java.text.SimpleDateFormat;

import com.honda.galc.service.property.PropertyService;

public class OIFConstants {
	public static final String OIF_SYSTEM_PROPERTIES = "OIF_SYSTEM_PROPERTIES";
	public static final String OIF_NOTIFICATION_PROPERTIES = "OIF_NOTIFICATION_PROPERTIES";
	public static final String EMAIL_NOTIFICATION_LEVEL = "EMAIL_NOTIFICATION_LEVEL";
	public static final String EMAIL_SUBJECT = "EMAIL_SUBJECT";
	public static final String EMAIL_DEBUG = "EMAIL_DEBUG";
	public static final String EMAIL_HOST = "EMAIL_HOST";
	public static final String EMAIL_SENDER = "EMAIL_SENDER";
	public static final String EMAIL_DISTRIBUTION_LIST = "EMAIL_DISTRIBUTION_LIST";
	public static final String EMAIL_ID = "EMAIL_ID";
	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
	public static final String MESSAGE_LINE1 = "MESSAGE_LINE1";
	public static final String MESSAGE_LINE2 = "MESSAGE_LINE2";
	public static final String UNDEFINED_PROPERTIES = "Undefined property";
	public static final String OIF_XARG="$ARG$";
	
	public static final String DATE_FORMAT = "DATE_FORMAT";
	public static final String TIME_FORMAT = "TIME_FORMAT";
	public static final String TIMESTAMP_FORMAT = "TIMESTAMP_FORMAT";
	public static final String MQ_CONFIG = "MQ_CONFIG";
	public static final String RESULT = "RESULT";
	public static final String SEND = "SEND";
	public static final String GPCS = "GPCS";
	public static final String STARTING_OFFSET = "STARTING_OFFSET";
	public static final String INTERFACE_ID = "INTERFACE_ID";
	public static final String MESSAGE_LINE_LENGTH = "MESSAGE_LINE_LENGTH";
	public static final String PARSE_LINE_DEFS = "PARSE_LINE_DEFS";
	public static final String OUTPUT_FORMAT_DEFS = "OUTPUT_FORMAT_DEFS";
	
	public static final String OIF_DISTRIBUTION = "OIF_DISTRIBUTION";
	public static final String DISTRIBUTION_PARAM = "DISTRIBUTION_PARAM";
	public static final String DISTRIBUTION_TASK_NAMES_SEP = "\\s*:\\s*";
	public static final String DISTRIBUTION_LINES = "DISTRIBUTION_LINES";
	
	public static final String INITIALIZE_TAIL = "INITIALIZE_TAIL";
	public static final String AUTO_HOLD = "AUTOMATIC_HOLD";
	public static final String NOT_HOLD_DEMANDTYPES = "NOT_HOLD_DEMANDTYPES";
	
	public static final String EXPORT_FILE_PATH = "EXPORT_FILE_PATH";
	public static final String EXPORT_FILE_NAME = "EXPORT_FILE_NAME";
	public static final String MQ_SENDER_FLAG = "MQ_SENDER_FLAG";
	
	public static final String BATCH_PROCESSING_COUNT = "BATCH_PROCESSING_COUNT";
	
	private static final String timeFormat = getFormat(TIME_FORMAT); 
	private static final String dateFormat = getFormat(DATE_FORMAT); 
	private static final String timestampFormat = getFormat(TIMESTAMP_FORMAT); 
	private static String getFormat(String format) {
		String result = PropertyService.getProperty(OIF_SYSTEM_PROPERTIES, format);
		if(result == null) {
			throw new RuntimeException("Property value <" + format + "> is missing for property key <OIF_SYSTEM_PROPERTIES>.");
		}
		return result;
	}
	public static final SimpleDateFormat stf1 = createFormat(timeFormat); 
	public static final SimpleDateFormat sdf1 = createFormat(dateFormat); 
	public static final SimpleDateFormat stsf1 = createFormat(timestampFormat); 
	private static SimpleDateFormat createFormat(String format) {
		try {
			return new SimpleDateFormat(format);
		}catch(final Exception ex){
			throw new RuntimeException("Failed to create <" + format + "> in static block.", ex);
		}
	}

	public enum DEPARTMENT_CODE {
		AE, AF, WE, PA, IA
	};
	
	//part of url to get the services instance
	public static final String HTTP_SERVICE_URL_PART		= "/BaseWeb/HttpServiceHandler";
	//constant to get the name property for active lines
	public static final String ACTIVE_LINES				= "ACTIVE_LINES_URLS";
	//Regular MQ client properties
	public static final String RMQ_HOST_NAME		=	"HOST_NAME";
	public static final String RMQ_PORT				=	"PORT_NUMBER";
	public static final String RMQ_QUEUE_MANAGER	=	"QUEUE_MANAGER_NAME";
	public static final String RMQ_CHANNEL			=	"CHANNEL";
	public static final String RMQ_QUEUE_NAME		=	"QUEUE_NAME";
	public static final String RMQ_USER				=	"USER_NAME";
	public static final String RMQ_PASSWORD			=	"PASSWORD";
	
	//constant to get the type schedule flag (Sequence or next_production_lot)
	public final static String IS_SCHEDULE_BY_SEQUENCE = "IS_SCHEDULE_BY_SEQUENCE";
	//constant with the GPCS NO RECORD message length.
	public final static	Integer NO_RECORD_MESSAGE_LINE_LENGTH	=	67;	
	
	public final static String RECORDS_PER_READ = "RECORDS_PER_READ";
	public final static String BYTES_PER_LINE = "BYTES_PER_LINE";
	public final static String DELETE_OLD_RECORDS_BY_CALENDAR_DAYS="DELETE_OLD_RECORDS_BY_CALENDAR_DAYS";
	public final static String INSERT_LATEST_RECORDS_BY_CALENDAR_DAYS="INSERT_LATEST_RECORDS_BY_CALENDAR_DAYS";
	
	public static final String PROCESS_POINT_MAP = "PROCESS_POINT_MAP";
	public static final String PROCESS_POINT_ID ="PROCESS_POINT_ID";

	public static final String SCHEDULER_USERNAME= "SYSTEM";
	public static final String SAVE_RUN_HISTORY = "SAVE_RUN_HISTORY";

}
