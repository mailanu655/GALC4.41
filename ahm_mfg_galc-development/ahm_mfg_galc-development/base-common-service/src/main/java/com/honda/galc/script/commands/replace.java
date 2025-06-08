package com.honda.galc.script.commands;

import bsh.CallStack;
import bsh.Interpreter;

/**
 * 
 * <h3>replace</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> replace description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Apr 12, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 12, 2012
 */
public class replace {
	public static String usage() {
		return "usage: replace( StringBuilder template, String org, String rep )";
	}

	/**
	 * Implement replace command.
	 * @param env
	 * @param callstack
	 */

	public static void invoke( 
			Interpreter env, CallStack callstack, StringBuilder temp, String org, String rep ) 
	{
		temp.replace(temp.indexOf(org), temp.indexOf(org) + org.length(), rep);
	}

	public static void invoke( 
			Interpreter env, CallStack callstack, StringBuffer temp, String org, String rep ) 
	{
		temp.replace(temp.indexOf(org), temp.indexOf(org) + org.length(), rep);
	}
	
}
