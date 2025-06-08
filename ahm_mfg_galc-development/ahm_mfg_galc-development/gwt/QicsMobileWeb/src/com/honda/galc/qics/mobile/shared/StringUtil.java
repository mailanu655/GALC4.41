package com.honda.galc.qics.mobile.shared;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;

/**
 * The Class StringFormatter.bbThis exists because
 * GWT does not support String.format(...)
 */
public class StringUtil {
	
	
	/**
	 * Format a string by embedding arguments in the string.  This exists because
	 * GWT does not support String.format(...).  Example:
	 * 
	 * String s = StringFormatter.format( "The %a brown %b ", "quick", "fox" );
	 * 
	 *
	 * @param format the format
	 * @param args the args
	 * @return the string
	 */
	public static String format(final String format, final Object... args) {
		final RegExp regex = RegExp.compile("%[a-z]");
		final SplitResult split = regex.split(format);
		final StringBuffer msg = new StringBuffer();
		for (int pos = 0; pos < split.length() - 1; ++pos) {
			msg.append(split.get(pos));
			msg.append(args[pos].toString());
		}
		msg.append(split.get(split.length() - 1));
		return msg.toString();
	}
}