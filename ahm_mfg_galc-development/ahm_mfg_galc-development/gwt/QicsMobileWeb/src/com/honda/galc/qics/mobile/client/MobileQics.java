package com.honda.galc.qics.mobile.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.honda.galc.qics.mobile.client.events.ExceptionEvent;
import com.honda.galc.qics.mobile.client.events.PubSubBus;
import com.honda.galc.qics.mobile.client.utils.PhGap;
import com.honda.galc.qics.mobile.client.widgets.LocalLogger;
import com.smartgwt.mobile.client.Version;
import com.smartgwt.mobile.client.cordova.CordovaEntryPoint;
import com.smartgwt.mobile.client.widgets.tab.Tab;
import com.smartgwt.mobile.client.widgets.tab.TabSet;

/**
 * Entry point classes define <code>onModuleLoad()</code>.  This class 
 * creates a defects tab and a menu tab.  It also initializes logging and 
 * phonegap.
 */
public class MobileQics extends CordovaEntryPoint {
	
	
    private Tab defectsTab, menuTab;
    private QicsNavStack defects;
    
    private Menu menu;
 
    public MobileQics() {
    	super();
		Log.addLogger(LocalLogger.getInstance() );
		
		GWT.setUncaughtExceptionHandler(new   
			      GWT.UncaughtExceptionHandler() {  
			      public void onUncaughtException(Throwable e) {  
			        PubSubBus.EVENT_BUS.fireEvent(new ExceptionEvent(e)) ;
			    }  
		});
				
    	// Initialize PhoneGap
    	PhGap.initialize();
    }

    /**
     * This is the entry point  for the Mobile QICS application.  This class just
     * creates the initial layout.  The real work is done in the QicsNavStack class.
     * 
     */

	   @Override
	    public void onDeviceReady() {
		   Log.info( "SmartGWT.mobile build date = " + Version.getBuildDate() );
		   Log.info( "GWT version = " + GWT.getVersion());
		   Log.info( "HostPageBaseURL = " + GWT.getHostPageBaseURL()  );
		   Log.info( "GWT.getModuleBaseURL() = " + GWT.getModuleBaseURL()  );
		   
	       RootLayoutPanel.get().add(buildTabSet());
	        
	   }
	   
	   
	   private TabSet buildTabSet() {
	        TabSet tabSet = new TabSet();
	      
	        defects = new QicsNavStack();
	        defectsTab = new Tab("Defects");
	        defectsTab.setPane( defects );
	        tabSet.addTab(defectsTab);
	        
	        menu = new Menu("Menu");
	        menuTab = new Tab( "Menu");
	        menuTab.setPane( menu );
	        tabSet.addTab( menuTab );

	        return tabSet;
	   }
	   

	   
}
