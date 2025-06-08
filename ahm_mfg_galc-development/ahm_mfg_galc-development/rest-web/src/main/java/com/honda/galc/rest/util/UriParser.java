package com.honda.galc.rest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;
import java.util.StringTokenizer;

import com.honda.galc.util.Primitive;

/**
 * @author Subu Kathiresan
 * @date Apr 25, 2013
 */
public class UriParser {
	
	public static Stack<String> parse(String uri) {
		Stack<String> stack = new Stack<String>();
		StringTokenizer tokens = new StringTokenizer(uri, "/");
		while (tokens.hasMoreTokens()){
			stack.add(tokens.nextElement().toString());
		}
		return stack;
	}
	
	public static void addQueryStringParam(ArrayList<Object> parameters, String parameterPair) {
		String key = "";
		String val = parameterPair;
		if (parameterPair.startsWith("(short)") ||
				parameterPair.startsWith("(int)") ||
				parameterPair.startsWith("(long)") ||
				parameterPair.startsWith("(double)") ||
				parameterPair.startsWith("(float)") ||
				parameterPair.startsWith("(boolean)") ||
				parameterPair.startsWith("(char)") ||
				parameterPair.startsWith("(date)")	) {
			String[] tokens = parameterPair.split("\\)");
			key = tokens[0].replaceFirst("\\(", "");
			val = tokens[1];
		}

		if (key.equals("short"))
			parameters.add(new Primitive(Short.parseShort(val)));
		else if (key.equals("int"))
			parameters.add(new Primitive(Integer.parseInt(val)));
		else if (key.equals("long"))
			parameters.add(new Primitive(Long.parseLong(val)));
		else if (key.equals("double"))
			parameters.add(new Primitive(Double.parseDouble(val)));
		else if (key.equals("float"))
			parameters.add(new Primitive(Float.parseFloat(val)));
		else if (key.equals("boolean"))
			parameters.add(new Primitive(Boolean.parseBoolean(val)));
		else if (key.equals("char"))
			parameters.add(new Primitive(val.toCharArray()[0]));
		else if (key.equals("date"))
			try {
				parameters.add(new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z").parse(val).getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
				parameters.add(new Date(1L));
			}
		else 
			parameters.add(new String(val));
	}

}
