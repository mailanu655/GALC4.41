package com.honda.galc.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.IEntity;

/**
 * 
 * <h3>CommonUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CommonUtil description </p>
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
 * @author Paul Chou
 * Apr 12, 2010
 *
 */
public class CommonUtil {

	static final String[] TimestampFormats= {"yyyy-MM-dd-HH.mm.ss.SSS","yyyy-MM-dd-HH.mm.ss"};
	static final String[] dateFormats= {"yyyy-MM-dd","yyyy/MM/dd","yyyyMMdd"};
	private static final String START_DELIMITER ="{";
	private static final String END_DELIMITER ="}";
	
	public static String format(Date date) {
		if(date == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SS");
		return df.format(date);
	}
	
	public static String format(Timestamp timestamp) {
		if(timestamp == null) return "";
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SS");
		return df.format(timestamp);
	}

	public static String convertNull(String string) {
		return string == null ? "" : string;
	}
	
	public static Timestamp getTimestampNow() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * get Date from current date with offset
	 * @param offset
	 * @return
	 */
	public static java.sql.Date getDate(int offset) {
		Date date = new Date(System.currentTimeMillis());
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, offset);
		date.setTime(c.getTime().getTime());
		return new java.sql.Date(date.getTime());
	}
	
	/**
	 * split a string with default delimiter ","
	 * @param str
	 * @return list of String
	 */
	public static List<String> splitStringList(String str){
		return splitStringList(str, ",");
	}
	
	/**
	 * Check if the string in the comma separated string list
	 * @param token
	 * @param list
	 * @return
	 */
	public static boolean isInList(String token, String list){
		if(token  == null || list == null) return false;
		
		List<String> splitStringList = splitStringList(list);
		for(String s : splitStringList)
			if(token.equals(s.trim())) return true;
		
		return false;
	}
	
	/**
	 * split a string with specified delimiter into a list of String
	 * @param str
	 * @return
	 */
	public static List<String> splitStringList(String str, String delimiter) {
		List<String> list = new ArrayList<String>();
		if(StringUtils.isEmpty(str)) return list;
		
		String[] split = str.split(delimiter);
		for(int i = 0; i < split.length; i++){
			list.add(split[i].trim());
		}
		
		return list;
	}
	
	/**
	 * returns the table name associated with an annotated JPA entity class
	 * @param entityClass
	 * @return
	 */
	public static String getTableName(Class<? extends IEntity> entityClass) {
		if(entityClass.getAnnotation(Table.class) != null) {
			Table table = entityClass.getAnnotation(Table.class);
			return table.name();
		}
		return "";
	}
	
	/**
	 * returns the column name associated with the Id of an annotated JPA entity class, or null if it does not exist
	 */
	public static String getIdColumnName(Class<? extends IEntity> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				Column column = field.getAnnotation(Column.class);
				if (column != null) {
					return column.name();
				}
			}
		}
		return null;
	}
	
	public static Timestamp convertTimeStamp(String str){
		if(str != null){
			for(String ts : TimestampFormats){
				try {
					Date pdate = new SimpleDateFormat(ts).parse(str);
					return new Timestamp(pdate.getTime());
				} catch (Exception e) {}
			}
		}
		return null;
	}
	
	public static java.sql.Date convertDate(String str){
		if(str != null){
			for(String df : dateFormats){
				try {
					Date date = new SimpleDateFormat(df).parse(str);
					return new java.sql.Date(date.getTime());
				} catch (Exception e) {} 
			}
		}
		return null;
	
	}
	
	
	
	public static List<String> toList(String string) {
		List<String> list = new ArrayList<String>();
		if(!StringUtils.isBlank(string)){
			for(String s : string.split(Delimiter.COMMA))
				list.add(StringUtils.trim(s));
		}
		return list;
	}

	public static String toInString(String planCode) {
		StringBuilder sb = new StringBuilder();
		for(String s : planCode.split(Delimiter.COMMA)){
			if(sb.length() > 0) sb.append(Delimiter.COMMA);
			sb.append("\'");
			sb.append(StringUtils.trim(s));
			sb.append("\'");
		}
		
		return sb.toString();
	}
	
	public static Integer stringToInteger(String str) {
		return StringUtils.isEmpty(str) ? null : Integer.valueOf(str);
	}
	
	/**
	 * This method is used to format the date in 'yyyy-MM-dd-HH.mm.ss'
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		if(date == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
		return df.format(date);
	}
	
	public static Map<String, Integer> sortMapByValue(Map<String, Integer> map){
		return sortMapByValue(map, true);
	}
	
	public static Map<String, Integer> sortMapByValue(Map<String, Integer> map, final boolean desc) {
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
	    {
	        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
	        {
	        	
	            return desc ? ( o2.getValue() ).compareTo( o1.getValue() ) : ( o1.getValue() ).compareTo( o2.getValue() );
	        }
	    } );

	    Map<String, Integer> result = new LinkedHashMap<String, Integer>();
	    for (Map.Entry<String, Integer> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;

	}
	
	public static String removeChars(String userId){
		String tempUserid = userId.replaceAll("\\pL+","");
		if(!tempUserid.trim().equalsIgnoreCase("")){
			return String.format("%05d", Integer.parseInt(tempUserid));
		}else{
			return userId;
		}
		
	}
	
	public static <T> List<T> removeNullItems(List<T> list) {
		if (list == null || list.isEmpty()) {
			return list;
		}
		Iterator<T> i = list.iterator();
		while (i.hasNext()) {
			T next = i.next();
			if (next == null) {
				i.remove();
			}
		}
		return list;
	}
	
	public static Boolean convertToBoolean(String value) {
		if("OK".equals(value.toString().trim()) || "1".equals(value.toString().trim()))
			return true;
		else
			return Boolean.valueOf(value.toString());
	}

	public static String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}
	
	/**
	 * parse and replace the variables in the formatter configuration with the data in the DataContainer
	 * Each variable is enclosed by { and } 
	 * example select * from galadm.gal131tbx where product_id = {PRODUCT_ID} where PRODUCT_ID is variable
	 * @param specifierSkelton - Original Formatter Configuration
	 * @param aData - Data container containing values of the variables
	 * @return converted Formatter specifiers
	 */
	public static List<Object> parseFormatterArguments(String configString, Map<Object, Object> map) {

		String[] split = configString.split(Delimiter.COMMA);
		List<Object> resultList = new ArrayList<Object>();

		for (int i = 0; i < split.length; i++) {
			String token = split[i];
			int indexS = token.indexOf(START_DELIMITER);
			if(indexS < 0) {
				resultList.add(token);
				continue;
			}
			int indexE = token.indexOf(END_DELIMITER);
			if(indexE < 0) throw new DataConversionException("Invalid formatter configuration : " + token + ". It does not have ending }");
			String dcTag = token.substring(indexS + 1, indexE);
			if(StringUtils.isEmpty(dcTag))
				throw new DataConversionException("Invalid formatter variable. Empty variable in formatter statetment : " + token);

			if(!map.containsKey(dcTag))
				throw new DataConversionException("No tag is defined in data container for formatter varialbe : " + dcTag);

			Object value = map.get(dcTag);
			if(value == null) value = "";
			resultList.add(value);
		}

		return resultList;

	}
	
	public static List<String> toList(String inputStr, String semiColon) {
		List<String> list = new ArrayList<String>();
		if(!StringUtils.isBlank(inputStr)){
			for(String s : inputStr.split(semiColon))
				list.add(StringUtils.trim(s));
		}
		return list;
	}
	
	public static List<String> objectArrayToStringList(Object[] years){
		List<String> defaultList = new ArrayList<String>();
	
		if( years!= null && years.length > 0) {
			String[] yearsArray = new String[years.length];
			System.arraycopy(years, 0, yearsArray, 0,  years.length);
			List<String> yearsList = Arrays.asList(yearsArray);
			return yearsList;
		}
		return defaultList;
	}
	
}
