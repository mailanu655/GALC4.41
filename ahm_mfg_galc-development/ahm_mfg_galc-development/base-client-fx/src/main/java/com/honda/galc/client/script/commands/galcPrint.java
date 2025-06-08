package com.honda.galc.client.script.commands;

import java.util.ArrayList;
import java.util.List;

import bsh.CallStack;
import bsh.Interpreter;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.printing.PrintingUtil;

/**
 * 
 * <h3>galcPrint</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> galcPrint description </p>
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
 * <TD>Apr 13, 2012</TD>
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
 * @since Apr 13, 2012
 */
public class galcPrint {
	public static String usage() {
		return "usage: galcPrint(String clientId, String formId, DataContainer | Product | ProductSpec )";
	}

	/**
	 * Implement galcPrint command.
	 * @param env
	 * @param callstack
	 */

	public static void invoke(Interpreter env, CallStack callstack, String clientId, String formId, ProductSpec productSpec ) 
	{
		try {
			PrintingUtil printUtil = new PrintingUtil(clientId, formId);
			printUtil.print(productSpec);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to print " + clientId + " formId:" + formId);
		}
	}

	public static void invoke(Interpreter env, CallStack callstack, String clientId, String formId, DataContainer dc ) 
	{
		try {
			PrintingUtil printUtil = new PrintingUtil(clientId, formId);
			printUtil.print(dc);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to print " + clientId + " formId:" + formId);
		}
	}
	
	public static void invoke(Interpreter env, CallStack callstack, String clientId, String formId, BaseProduct product ) 
	{
		try {
			PrintingUtil printUtil = new PrintingUtil(clientId, formId);
			List<BaseProduct> list = new ArrayList<BaseProduct>();
			list.add(product);
			printUtil.print(list);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to print " + clientId + " formId:" + formId);
		}
	}
	
	public static void invoke(Interpreter env, CallStack callstack, String clientId, String formId, List<BaseProduct> productList ) 
	{
		try {
			PrintingUtil printUtil = new PrintingUtil(clientId, formId);
			printUtil.print(productList);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to print " + clientId + " formId:" + formId);
		}
	}
}
