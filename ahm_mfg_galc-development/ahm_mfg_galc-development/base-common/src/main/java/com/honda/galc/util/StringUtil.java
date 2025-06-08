/**
 * 
 */
package com.honda.galc.util;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.honda.galc.constant.Delimiter;

/**
 * @author Subu Kathiresan
 * Oct 31, 2011
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 25, 2013
 */
/**
 * @author vf036360
 *
 */
public class StringUtil {
	
	/**
	 * returns current date time in the provided format
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Converts a Date to String
	 *
	 * @param date 			The date to convert
	 * @param dateFormat	Date format to use for conversion
	 * @return 				A string representing the date
	 */
	public static String dateToString(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date stringToDate(String dateString, String format)	{
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			ParsePosition parsePos = new ParsePosition(0);
			return dateFormat.parse(dateString, parsePos);
		} catch (Exception ex) {
	    	getLogger().error(ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the time of the given date in string format
	 *
	 * @param date 			The date to convert
	 * @return 				A string representing the date
	 */
	public static String getTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		return sdf.format(date);
	}

	public static final String padLeft(String stringToPad, int length, char padChar) {
		return padLeft(stringToPad, length, padChar, true);
	}
	
	/**
	 * Pads a string with the provided char, to the left
	 * 
	 * @param stringToPad	String to pad
	 * @param length		length of the padded string
	 * @param padChar		Char to use for padding
	 * @param truncate		Indicates whether or not to truncate if the orginal string exceeds the pad length 
	 * @return
	 */
	public static final String padLeft(String stringToPad, int length, char padChar, boolean truncate) {
		return pad(new StringBuilder(stringToPad), length, padChar, truncate, false).toString();
	}
	
	public static final StringBuilder padLeft(StringBuilder stringToPad, int length, char padChar) {
		return padLeft(stringToPad, length, padChar, true);
	}
	
	/**
	 * pads a StringBuilder with the provided char, to the left
	 * 
	 * @param stringToPad
	 * @param length
	 * @param padChar
	 * @param truncate
	 * @return
	 */
	public static final StringBuilder padLeft(StringBuilder stringToPad, int length, char padChar, boolean truncate) {
		return pad(stringToPad, length, padChar, truncate, false);
	}
	
	/**
	 * Pads a string with the provided char, to the right
	 * 
	 * @param stringToPad	String to pad
	 * @param length		length of the padded string
	 * @param padChar		Char to use for padding
	 * @param truncate		Indicates whether or not to truncate if the orginal string exceeds the pad length 
	 * @return
	 */
	public static final String padRight(String stringToPad, int length, char padChar, boolean truncate) {
		return pad(new StringBuilder(stringToPad), length, padChar, truncate, true).toString();
	}
	
	/**
	 * pads a StringBuilder with the provided char, to the right
	 * 
	 * @param stringToPad
	 * @param length
	 * @param padChar
	 * @param truncate
	 * @return
	 */
	public static final StringBuilder padRight(StringBuilder stringToPad, int length, char padChar, boolean truncate) {
		return pad(stringToPad, length, padChar, truncate, true);
	}
	
	/**
	 * Pads a string with the provided char, to the right or left side
	 * 
	 * @param stringToPad	String to pad
	 * @param length		length of the padded string
	 * @param padChar		Char to use for padding
	 * @param truncate		Indicates whether or not to truncate if the orginal string exceeds the pad length 
	 * @param right			Indicates whether to pad right or left
	 * @return
	 */
	public static final StringBuilder pad(StringBuilder stringToPad, int length, char padChar, boolean truncate, boolean right) {
		if(stringToPad == null) 
			stringToPad = new StringBuilder("");

		// Nothing to do
		if((stringToPad.length() == length) || (length < 1))
			return stringToPad;
		
		// truncate
		if(stringToPad.length() > length) 
			return truncate ? new StringBuilder(stringToPad.substring(0, length)) : stringToPad;

		// pad
		int padLen = length - stringToPad.length();
		StringBuilder strBld = new StringBuilder(length);
		
		if (right)
			strBld.append((CharSequence) stringToPad);
		
		while(padLen > 0) { 
			strBld.append(padChar); 
			padLen--; 
		}
		
		if (!right)
			strBld.append((CharSequence) stringToPad);
		
		return strBld;
	}
	
	/**
	 * replace mask char '#' with random numbers
	 * 
	 * @param mask
	 * @return
	 */
	public static String replaceMaskWithRandomNumber(String mask) {
		StringBuffer strBuf = new StringBuffer();
		Random rndGenerator = new Random();
		
		for(char replaceChar: mask.toCharArray()) {
			if (replaceChar == '#')
				strBuf.append(new Integer(rndGenerator.nextInt(10)).toString());
			else
				strBuf.append(replaceChar);				
		}
		return strBuf.toString();
	}
	
	/**
	 * packs the bit array(string) value to an array of characters representing the binary equivalent. 
	 * Input--> "0100001001000011" Output--> "BC"
	 * Input--> "12301010"         Output--> NumberFormatException
	 * 
	 * @param binaryStr
	 * @return
	 */
	public static StringBuilder bitArrayToChars(String binaryStr) {
		
		StringBuilder strB = new StringBuilder();
		if (binaryStr == null)
			return strB;
		
		binaryStr = binaryStr.trim();
		
		for(Character c: binaryStr.toCharArray()) {
			if (c != '0' && c != '1') {
				throw new NumberFormatException(binaryStr + " is not binary");
			}
		}
		
		int xtraBitsLen = binaryStr.length() % 8;
		if (xtraBitsLen > 0) {
			// extra characters are found.  zeros will be left padded to the highest order byte
			binaryStr = padLeft(binaryStr, 8 - xtraBitsLen + binaryStr.length(), '0');
		}
		// pack one byte at a time starting with the higher order byte.  
		Matcher m = Pattern.compile(".{1,8}").matcher(binaryStr);
		int numOfBytes = binaryStr.length()/8;
		char[] charArray = new char[numOfBytes];
		for (int i = 0; i < numOfBytes; i++ ) {
			// convert a single byte to its ASCII character equivalent
			charArray[i] = (char) Integer.parseInt(m.find() ? binaryStr.substring(m.start(), m.end()) : "", 2);
		}
		return strB.append(charArray);
	}
	
	/**
	 * converts an integer to string (Every byte is represented by a character)
	 * Int		Hex		Binary      			Output
	 * 123		7D 		0111 1100				{
	 * 127		7F 		0111 1111				DEL
	 * 9876		2694	0010 0110 1001 0100		SUB^
	 * 65535	FFFF	1111 1111 1111 1111
	 * 
	 * @param intValue
	 * @return
	 */
	public static StringBuilder intToChars(Integer intValue) {
		return bitArrayToChars(Integer.toBinaryString(intValue));
	}
	
	/**
	 * returns a string representing the binary equivalent of the input characters. 
	 * Input--> "BC" Output--> "0100001001000011"
	 * @param inputStr
	 * @return
	 */
	public static String stringToBitArray(StringBuilder inputStr) {
		StringBuffer strBuf = new StringBuffer();
		for(char character: toCharArray(inputStr)) {
			strBuf.append(padLeft(Integer.toBinaryString(character), 8, '0', true));
		}
		
		return strBuf.toString().trim();
	}
	
	
	public static String reverse(String source) {
		return reverse(new StringBuilder(source)).toString();
	 }
	
	/**
	 * reverses the input string
	 * 
	 * @param source
	 * @return
	 */
	public static StringBuilder reverse(StringBuilder source) {
		int i, len = source.length();
		StringBuilder dest = new StringBuilder(len);

		for (i = (len - 1); i >= 0; i--) {
			dest.append(source.charAt(i));
		}
		return dest;
	 }

	/**
	 * returns an array of characters representing the
	 * passed in StringBuilder object
	 * @param strBuilder
	 * @return
	 */
	public static char[] toCharArray(StringBuilder strBuilder) {
		char[] retVal = new char[strBuilder.length()];
		for(int index =0; index < strBuilder.length(); index++) {
			retVal[index] = strBuilder.charAt(index);
		}
		return retVal;
	}
	
	/**
	 * converts integer to Hexadecimal and pads result
	 * 
	 * @param val
	 * @return
	 */
	public static String zeroPadHex(int val) {
		String hexValue = Integer.toHexString(val);
		 while (hexValue.length() < 2) {
		      hexValue = 0 + hexValue;
		    }
		 return hexValue;
	}

	/**
	 * converts byte array to Hexadecimal string and zero pads 
	 * 
	 * @param val
	 * @return
	 */
	public static String zeroPadHex(byte[] val) {
		BigInteger bi = new BigInteger(val); 
		String s = bi.toString(16);			// 120ff0
		if (s.length() % 2 != 0) {
		    s = "0" + s;					// Pad with 0
		}
		return s;
	}
	
	/**
	 * returns the hexadecimal representation of the provided
	 * StringBuilder with each byte separated by a space
	 * @param message
	 * @return
	 */
	public static String toHexString(StringBuilder message) {
		StringBuilder newStrBld = new StringBuilder("HEX: ");
		try {
			for(char c: StringUtil.toCharArray(message)) {
				String hexValue = Integer.toHexString(c);
				hexValue = hexValue.length() % 2 > 0? "0" + hexValue : hexValue; 
				while (hexValue.length() >= 2) {
					newStrBld.append(hexValue.substring(0, 2));
					hexValue = hexValue.substring(2);
					if (hexValue.length() > 2)
						newStrBld.append(" ");
				}
				newStrBld.append(hexValue + " ");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return newStrBld.toString().toUpperCase();  	
	}
	
	public static boolean isNumeric(String str) {   
		return str.trim().matches("[+-]?\\d*(\\.\\d+)?"); 
	}
		
	public static boolean isNullOrEmpty(String str) {
		return str == null || StringUtils.isEmpty(str);
	}
	
	/**
	 * This method is used to get the getter name
	 */
	public static String getterName(String fieldName){
		return getMethodName(fieldName, "get");
	}
	
	/**
	 * This method is used to get the setter name
	 */
	public static String setterName(String fieldName){
		return getMethodName(fieldName, "set");
	}
	
	/**
	 * pass in the accessor prefix and get the method name
	 * for the provided field name
	 * 
	 * @param fieldName
	 * @param accessorPrefix
	 * @return
	 */
	public static String getMethodName(String fieldName, String accessorPrefix) {
		if (fieldName == null)
			return "";
		
		try {
			// remove leading Underscores
			if (fieldName.startsWith("_")) {
				fieldName = fieldName.substring(1);
			}
			char[] charArray = fieldName.toCharArray();
			charArray[0] = Character.toUpperCase(charArray[0]);
			return accessorPrefix + new String(charArray).trim();
		}catch(Exception ex) {}
		return "";
	}
	
    /**
     * replaces all non-printable characters with a hexadecimal string equivalent
     * @param message
     * @return
     */
	public static String replaceNonPrintableCharacters(StringBuilder strBuilder) {
		if (strBuilder == null)
			return "";
		
		StringBuilder strB = new StringBuilder();
		for (int i = 0; i < strBuilder.length(); i++) {
			char singleChar = strBuilder.charAt(i);
        	if (singleChar < 32 || singleChar > 126)		// ASCII values between 32 and 126 are printable
        		strB.append("[" + Integer.toHexString(singleChar)+ "]");
        	else
        		strB.append(singleChar);
        }
		return strB.toString();
	}

	/**
	 * converts a byte array to a String in UTF-8 format
	 * 
	 * @param bytes
	 * @return
	 */
	public static String convertToString(byte[] bytes) {
		try {
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			ByteBuffer srcBuffer = ByteBuffer.wrap(bytes);
			return decoder.decode(srcBuffer).toString();
		} catch (CharacterCodingException ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to convert byte array to String: " + bytes);
		}
		
		return "";
	}
	
	/**
	 * Adds quotes around the string and also escapes double quotes within the string to make sure we can view the data as csv. 
	 * @param str
	 * @return
	 */
	public static String csvCommaCheckAddQuotes(String str) {
		try {
			if (str == null) {
				return null;
			}
			if (str.indexOf(",") != -1) {
				if (str.indexOf("\"") != -1) {
					str = str.replaceAll("\"", "\"\"");
				}
				str = "\"" + str + "\"";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "An error occurred while performing commacheck " );
		}
		return str;
	}

	/**
	 * convert the String with first character uppercased format
	 * @param String
	 * @return 
	 */
	public static String properCase(String inputVal){
	    if (inputVal.length() == 0) return "";
	    if (inputVal.length() == 1) return inputVal.toUpperCase();
	    return inputVal.substring(0,1).toUpperCase()
	        + inputVal.substring(1).toLowerCase();
	}
	
	/**
	 * concatenates and returns toString for 
	 * all the passed in objects
	 * @param className
	 * @param objects
	 * @return
	 */
	public static String toString(String className, Object... objects ) {
		String str = className + "(";
		boolean isFirst = true;
		for(Object item : objects) {
			if(!isFirst) str += ",";
			else isFirst = false;
			str += item == null ? "null" : item.toString();
		}
		str +=")";
		return str;
	}
	
	public static String toSqlInString(List<String> params) {
		StringBuilder sb = new StringBuilder();
		for(String s : params){
			if(sb.length() > 0) sb.append(Delimiter.COMMA);
			sb.append("\'");
			sb.append(StringUtils.trim(s));
			sb.append("\'");
		}
		if(sb.length() == 0) sb.append("\'\'");
		
		return sb.toString();
	}
	
	public static String toSqlInString(String paramString) {
		return toSqlInString(paramString.split(Delimiter.COMMA));
	}
	
	public static String toSqlInString(String[] paramList) {
		return toSqlInString(Arrays.asList(paramList));
	}
	
	public static int[] toIntArray(String[] strArray) {
		if (strArray == null || strArray.length == 0) {
			return new int[]{};
		}
		int[] intArray = new int[strArray.length];
		for (int i = 0 ; i < strArray.length; i++) {
			intArray[i] = Integer.parseInt(strArray[i]);
		}
		return intArray;
	}
	
	public static String toSqlLikeString(List<String> params, String columnName) {
		StringBuilder sb = new StringBuilder();
		for(String s : params){
			if(sb.length() > 0) sb.append(" OR ");
			sb.append(columnName + " LIKE ");
			sb.append("\'%");
			sb.append(StringUtils.trim(s));
			sb.append("%\'");
		}

		return sb.toString();
	}
	
	public static String toSqlNotLikeString(List<String> params, String columnName) {
    StringBuilder sb = new StringBuilder();
    for(String s : params) {
        if(sb.length() > 0) sb.append(" AND ");
        sb.append(columnName + " NOT LIKE ");
        sb.append("'%");
        sb.append(StringUtils.trim(s));
        sb.append("%'");
    }
    return sb.toString();
    }
	
	public static Integer toInteger(String param) {
		return NumberUtils.isNumber(param) ? NumberUtils.toInt(param) : null; 
	}
	
	public static Double toDouble(String param) {
		return NumberUtils.isNumber(param) ? NumberUtils.toDouble(param) : null; 
	}
	
	/**
	 * This method will be used to trim the string value in List collection
	 * object.
	 * 
	 * @param stringList
	 * @return
	 */
	public static List<String> trimStringList(List<String> stringList) {
		if (stringList != null && !stringList.isEmpty()) {
			List<String> trimmedList = new ArrayList<String>(stringList.size());
			for (String string : stringList) {
				trimmedList.add(string.trim());
			}
			return trimmedList;
		}
		return stringList;
	}
	
	public static StringBuilder appendIfNotEmpty(StringBuilder sb, String str) {
		if (sb == null) {
			return sb;
		}
		if (sb.length() > 0) {
			sb.append(str);
		}
		return sb;
	}
	
	/**
	 * This method is used to convert a given string to camel case with delimiter as "_" by default
	 * @param str
	 * @param capitalizeFirstLetter
	 * @return
	 */
	public static String toCamelCase(String str, final boolean capitalizeFirstLetter) {
		return toCamelCase(str, capitalizeFirstLetter, Delimiter.UNDERSCORE.toCharArray());
	}
	
	/**
	 * This method is used to convert a given string to camel case (i.e. "This is String" => "ThisIsString" or "thisIsString")
	 * @param str
	 * @param capitalizeFirstLetter
	 * @param delimiters
	 * @return
	 */
	public static String toCamelCase(String str, final boolean capitalizeFirstLetter, final char... delimiters) {

		if (StringUtils.isEmpty(str)) {
			return str;
		}
		str = str.toLowerCase();
		final int strLen = str.length();
		final int[] newCodePoints = new int[strLen];
		int outOffset = 0;
		final Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
		boolean capitalizeNext = false;
		if (capitalizeFirstLetter) {
			capitalizeNext = true;
		}
		for (int index = 0; index < strLen;) {
			final int codePoint = str.codePointAt(index);
			if (delimiterSet.contains(codePoint)) {
				capitalizeNext = true;
				if (outOffset == 0) {
					capitalizeNext = false;
				}
				index += Character.charCount(codePoint);
			} else if (capitalizeNext || outOffset == 0 && capitalizeFirstLetter) {
				final int titleCaseCodePoint = Character.toTitleCase(codePoint);
				newCodePoints[outOffset++] = titleCaseCodePoint;
				index += Character.charCount(titleCaseCodePoint);
				capitalizeNext = false;
			} else {
				newCodePoints[outOffset++] = codePoint;
				index += Character.charCount(codePoint);
			}
		}
		if (outOffset != 0) {
			return new String(newCodePoints, 0, outOffset);
		}
		return str;
	}

	private static Set<Integer> generateDelimiterSet(final char[] delimiters) {
		final Set<Integer> delimiterHashSet = new HashSet<Integer>();
		delimiterHashSet.add(Character.codePointAt(new char[]{' '}, 0));
		if (delimiters == null || delimiters.length == 0) {
			return delimiterHashSet;
		}
		for (int index = 0; index < delimiters.length; index++) {
			delimiterHashSet.add(Character.codePointAt(delimiters, index));
		}
		return delimiterHashSet;
	}
	
	/**
	 * Method to remove all special Characters other than a-z or A-Z or 0-9 in String
	 * @param source
	 * @return
	 */
	public static String removeAllSpecialCharacters(String source) {
	    return source.replaceAll("[^a-zA-Z0-9]", "");
	}
	
	/**
	 * Method to remove one or more special Characters other than a-z or A-Z or 0-9 
	 * at start of the String
	 * @param source
	 * @return
	 */
	public static String trimSpecialCharacterAtFrontOnly(String source) {
		return source.replaceAll("^[^a-zA-Z0-9]*", "");
	}
	
	/**
	 * Method to remove one or more special Characters other than a-z or A-Z or 0-9 
	 * at end of the String
	 * @param source
	 * @return
	 */
	public static String trimSpecialCharacterAtEndOnly(String source) {
		return source.replaceAll("([^a-zA-Z0-9])*\\z", "");
	}
	
	/**
	 * Method to remove one or more special Characters other than a-z or A-Z or 0-9 
	 * at start and end of the String
	 * @param source
	 * @return
	 */
	public static String trimSpecialCharacterFrontAndEnd(String source) {
		return trimSpecialCharacterAtEndOnly(trimSpecialCharacterAtFrontOnly(source));
	}
	
	/**
	 * Returns true if both strings are either null or empty, or if both strings are equal
	 */
	public static boolean emptyOrEqual(String str1, String str2) {
		return (StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)) || ObjectUtils.equals(str1, str2);
	}
	
	public static String removeControlCharacters(String str) {
		StringBuilder builder = new StringBuilder();
		int pos = 0;
		try {
			for(int i = 0; i <  Character.codePointCount(str, 0, str.length()); i++) {
				int codePoint = str.codePointAt(pos);
				
				char[] chs = Character.toChars(codePoint);
				pos += chs.length;

				if(!isControlCharacter(codePoint)) {
					builder.append(chs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	public static boolean isControlCharacter(int codePoint) {
		boolean result = false;
			
		switch(Character.getType(codePoint)) {
			case Character.CONTROL :
			case Character.UNASSIGNED :
			case Character.PRIVATE_USE :
			case Character.COMBINING_SPACING_MARK : 
				result = true;
				break;
			default :
				result = false;
		}

		return result && (10 != codePoint); // code point 10 is a control character but it is needed
	}
}
