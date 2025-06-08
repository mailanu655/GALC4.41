package com.honda.galc.client.gts.view;

import java.awt.Point;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>Preference</code> is ...
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
 * <TD>Apr 3, 2008</TD>
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

public class Preference {
    
    public static String name;
    
    private static Preferences prefs;
    
    
    public static void init(String name) {
        
        prefs = Preferences.userRoot().node("GTS_" + name);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            prefs = null;
        }
    }
    
    public static void setDrawingViewOption(int type) {
        
        if(prefs != null) 
            prefs.putInt("Drawing View Option", type);
        
    }
    
    public static boolean isAutoFitAll(){
        
        return prefs == null ? false : prefs.getInt("Drawing View Option", 0) == 1;
        
    }
    
    public static boolean isCustomizedSize(){
        
        return prefs == null ? false : prefs.getInt("Drawing View Option", 0) == 2;

    }
    
    public static void setWindowMaximized(boolean aFlag) {
        
        if(prefs != null) 
            prefs.putBoolean("Maximize Window", aFlag);
        
    }
    
    public static boolean isWindowMaximized() {
        
        return prefs == null ? false : prefs.getBoolean("Maximize Window", false);
        
    }
    
    public static void setConfirmMessage(boolean aFlag) {
    	if(prefs != null) 
             prefs.putBoolean("Manual Mode Confirm Message", aFlag);
    }
    
    public static boolean isConfirmMessage() {
        return prefs == null ? true : prefs.getBoolean("Manual Mode Confirm Message", true);
    }
    
    public static void setCarrierDisplayType(String type){
        
        if(prefs != null) 
            prefs.put("Carrier Display Type", type);
            
    }
    
    public static String getCarrierDisplayType(){
        
        return prefs == null ? "CARRIER" : prefs.get("Carrier Display Type", "CARRIER");
        
    }
    
    
    public static void setDrawingScaleFactor(double scale){
        
        if(prefs != null) 
            prefs.putDouble("Drawing Scale Factor", scale);
        
    }
    
    public static double getDrawingScaleFactor() {
        
        return prefs == null ? 1.0 : prefs.getDouble("Drawing Scale Factor", 1.0d);
        
    }
    
    public static void setLocation(int x, int y){
        
        if(prefs == null) return;
        prefs.putInt("Location X", x);
        prefs.putInt("Location Y", y);
        
    }
    
    public static void setLocation(Point p) {
        
        setLocation(p.x, p.y);
        
    }
    
    
    public static Point getLocation(){
        

        return prefs == null ? new Point(0,0) :
                new Point(prefs.getInt("Location X", 0),
                         prefs.getInt("Location Y", 0));
        
    }

}
