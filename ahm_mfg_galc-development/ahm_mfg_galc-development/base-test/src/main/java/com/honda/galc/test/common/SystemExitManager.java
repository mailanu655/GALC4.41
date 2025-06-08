package com.honda.galc.test.common;

import java.security.Permission;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>SystemExitManager</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Jeffray Huang</TD>
 * <TD>Feb 5, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class SystemExitManager extends SecurityManager{
    
    private static SystemExitManager securityManager;
    private static boolean allowExit = false;
    private static boolean exited = false;
    private static int count = 0;
    public static void forbidSystemExitCall() {
        if(securityManager == null) {
            securityManager = new SystemExitManager();
            System.setSecurityManager(securityManager);
        }
    }
    
    public static void allowExit() {
        allowExit = true;
        count ++;
    }
    
    public void checkExit(int status) {
        
    	
    	System.out.println("check exit - allow exit =" + allowExit + " stauts=" + status + " count=" + count );
    	if((allowExit && status > 0) || exited){
    		System.out.println("allow exit!");
    		exited = true;
    	}else {
    		throw new SecurityException();
    	}
        
    }
    
    public void checkPermission(Permission permission) {
        
    }
}


