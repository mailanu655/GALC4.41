package com.honda.galc.qics.mobile.client;

import com.allen_sauer.gwt.log.client.Log;
import com.honda.galc.qics.mobile.client.tables.LogMessageTable;
import com.honda.galc.qics.mobile.client.utils.PhGap;
import com.honda.galc.qics.mobile.client.widgets.ContentViewPanel;
import com.honda.galc.qics.mobile.client.widgets.LocalLogger;
import com.honda.galc.qics.mobile.client.widgets.TimedDialog;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickEvent;
import com.smartgwt.mobile.client.widgets.tableview.events.RecordNavigationClickHandler;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;


/**
 * The Class LogViewPanel displays log messages.  Allows user to set log level.
 */
public class LogViewPanel extends ScrollablePanel {

	private NavStack navStack;

	private final ContentViewPanel logRecordViewPanel = new ContentViewPanel("Log Message", p("Hello"));
	/**
	 * Instantiates a new panel to view log messages.
	 */
	public LogViewPanel (NavStack nav) {
		
		super( "Log Messages");
		navStack = nav;
		
		LogMessageTable logMessageTable = new LogMessageTable();
		logMessageTable.addRecordNavigationClickHandler(new RecordNavigationClickHandler() {
            @Override
            public void onRecordNavigationClick(RecordNavigationClickEvent event) {
            	PhGap.doTactalFeedback();
            	displayLogMessage( event.getRecord() );
            }
        });
		
	    ToolStrip toolstrip = new ToolStrip();
	    ToolStripButton clearButton = new ToolStripButton("Clear");
	    ToolStripButton debugButton = new ToolStripButton("Debug");
	    ToolStripButton infoButton = new ToolStripButton("Info");
	    ToolStripButton warnButton = new ToolStripButton("Warn");
	    ToolStripButton errorButton = new ToolStripButton("Error");
	    ToolStripButton fatalButton = new ToolStripButton("Fatal");
	    ToolStripButton offButton = new ToolStripButton("Off");

		VLayout layout = new VLayout();
        layout.setWidth("100%");
        layout.setAlign(Alignment.LEFT);

		  toolstrip.addButton( clearButton );
		  toolstrip.addSpacer();
		  toolstrip.addButton( debugButton );
		  toolstrip.addButton( warnButton );
		  toolstrip.addButton( infoButton );
		  toolstrip.addButton( errorButton );
		  toolstrip.addButton( fatalButton );
		  toolstrip.addButton( offButton );
		  
		  clearButton.enable();
		  clearButton.addClickHandler(new ClickHandler() {
		        @Override
		        public void onClick(ClickEvent event) {
		        	LocalLogger.getInstance().getLogMessageRecordList().clear();
		        }
		  });	  
		  
		  debugButton.enable();
		  debugButton.addClickHandler(new ClickHandler() {
		        @Override
		        public void onClick(ClickEvent event) {
		        	setLogLevel("DEBUG", Log.LOG_LEVEL_DEBUG);
		        }
		  });

		  infoButton.enable();
		  infoButton.addClickHandler(new ClickHandler() {
		        @Override
		        public void onClick(ClickEvent event) {
		        	setLogLevel("INFO", Log.LOG_LEVEL_INFO);
		        }
		  });
		  
		  warnButton.enable();
		  warnButton.addClickHandler(new ClickHandler() {
		        @Override
		        public void onClick(ClickEvent event) {
		        	setLogLevel("WARN", Log.LOG_LEVEL_WARN);
		        }
		  });
		  
		  errorButton.enable();
		  errorButton.addClickHandler(new ClickHandler() {
		        @Override
		        public void onClick(ClickEvent event) {
		        	setLogLevel("ERROR", Log.LOG_LEVEL_ERROR);
		        }
		  });
		  
		  fatalButton.enable();
		  fatalButton.addClickHandler(new ClickHandler() {
		        @Override
		        public void onClick(ClickEvent event) {
		        	setLogLevel("FATAL", Log.LOG_LEVEL_FATAL);
		        }
		  });
		  
		  offButton.enable();
		  offButton.addClickHandler(new ClickHandler() {
		        @Override
		        public void onClick(ClickEvent event) {
		        	setLogLevel("OFF", Log.LOG_LEVEL_OFF);
		        }
		  });

		  logMessageTable.setData( LocalLogger.getInstance().getLogMessageRecordList());
		  layout.addMember(toolstrip);
		  layout.addMember(logMessageTable);
		  
		  this.addMember(layout);
	  }
	
	  private void setLogLevel( String stringLevel, int level ) {
		  Log.setCurrentLogLevel(level);
		  TimedDialog.timedNotification("Log level changed to " + stringLevel, 3);
	  }
	  
	  public void displayLogMessage( Record record ) {
		  logRecordViewPanel.setInfo( p( record.getAttribute("title") ));
		  navStack.push(logRecordViewPanel);
 	   }
	  
	  public String p( String text ) {
		  return "<p>" + text + "</p>";
	  }

	}
