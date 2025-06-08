package com.honda.galc.client.script.commands;

import java.io.InputStream;

import bsh.CallStack;
import bsh.Interpreter;

import com.honda.galc.client.audio.TtsSpeaker;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>speak</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> speak description </p>
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
 * <TD>Apr 18, 2012</TD>
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
 * @since Apr 18, 2012
 */
public class speak {
	public static String usage() {
		return "usage: speak(String text | InputStream inputStream )";
	}

	/**
	 * Implement speak command.
	 * @param env
	 * @param callstack
	 */

	public static void invoke(Interpreter env, CallStack callstack, String text ) 
	{
		try {
			TtsSpeaker.speak(text);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed speak:" + text);
		}
	}

	public static void invoke(Interpreter env, CallStack callstack, InputStream inputstream ) 
	{
		try {
			TtsSpeaker.speak(inputstream);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to speak: " + inputstream.hashCode());
		}
	}
	
}
