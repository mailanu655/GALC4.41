/**
 * 
 */
package com.honda.galc.qics.mobile.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.UmbrellaException;
import com.honda.galc.qics.mobile.client.dataservice.DataServiceFactory;
import com.honda.galc.qics.mobile.client.dataservice.DefectDescriptionDataService;
import com.honda.galc.qics.mobile.client.dataservice.DefectResultDataService;
import com.honda.galc.qics.mobile.client.dataservice.FrameDataService;
import com.honda.galc.qics.mobile.client.dataservice.InspectionModelDataService;
import com.honda.galc.qics.mobile.client.events.CancelDefectRequestEvent;
import com.honda.galc.qics.mobile.client.events.ClearVinEvent;
import com.honda.galc.qics.mobile.client.events.CreateDefectRequestEvent;
import com.honda.galc.qics.mobile.client.events.DataAccessErrorEvent;
import com.honda.galc.qics.mobile.client.events.DefectResultsReceivedEvent;
import com.honda.galc.qics.mobile.client.events.DefectSelectedEvent;
import com.honda.galc.qics.mobile.client.events.DefectsChangedEvent;
import com.honda.galc.qics.mobile.client.events.ExceptionEvent;
import com.honda.galc.qics.mobile.client.events.OtherPartSelectedEvent;
import com.honda.galc.qics.mobile.client.events.PartDefectSelectedEvent;
import com.honda.galc.qics.mobile.client.events.PartGroupSelectedEvent;
import com.honda.galc.qics.mobile.client.events.PartLocationSelectedEvent;
import com.honda.galc.qics.mobile.client.events.PartSelectedEvent;
import com.honda.galc.qics.mobile.client.events.PubSubBus;
import com.honda.galc.qics.mobile.client.events.SettingsChangedEvent;
import com.honda.galc.qics.mobile.client.events.SettingsChangedEventData;
import com.honda.galc.qics.mobile.client.events.VinNotFoundEvent;
import com.honda.galc.qics.mobile.client.events.VinScannedEvent;
import com.honda.galc.qics.mobile.client.utils.DoneCallback;
import com.honda.galc.qics.mobile.client.utils.PhGap;
import com.honda.galc.qics.mobile.client.widgets.TimedDialog;
import com.honda.galc.qics.mobile.client.widgets.VinCaptureForm;
import com.honda.galc.qics.mobile.client.widgets.WaitPanel;
import com.honda.galc.qics.mobile.shared.entity.AddNewDefectRepairResultRequest;
import com.honda.galc.qics.mobile.shared.entity.DefectResult;
import com.honda.galc.qics.mobile.shared.entity.Frame;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.util.BooleanCallback;
import com.smartgwt.mobile.client.util.SC;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.icons.IconResources;
import com.smartgwt.mobile.client.widgets.layout.NavStack;
import com.smartgwt.mobile.client.widgets.layout.VLayout;


/**
 * The Class Defects is the controller for the majority of the application.  It works by registering
 * event handlers and responding to the events.
 */
public class QicsNavStack extends NavStack {

  
	private VinDefectsModel vinDefectsModel;
	
	// These are the panels we use on the nav stack
	private ScrollablePanel vinCapturePanel;
	private VinIssueListPanel vinIssueListPanel;
	private DefectViewerPanel defectViewerPanel;
	private DefectCreationSelectPanel partGroupSelectPanel;
	private DefectCreationSelectPanel partSelectPanel;
	private DefectCreationSelectPanel partLocationSelectPanel;
	private DefectCreationSelectPanel partDefectSelectPanel;
	private DefectCreationSelectPanel otherPartSelectPanel;
	private SaveNewDefectPanel saveNewDefectPanel;
	
    final NavStack navStack = this;

    public QicsNavStack() {
    	super("Mobile QICS");
 
        vinDefectsModel = new VinDefectsModel();
        vinDefectsModel.setProcessPoint(Settings.getProcessPoint());
 
    	this.registerHandlers();

		setWidth("100%");
		VLayout layout = new VLayout();
        layout.setWidth("100%");
        layout.setAlign(Alignment.CENTER);
        
        
        // add the Vin Capture which is our home panel
        vinCapturePanel = new ScrollablePanel("Mobile QICS", IconResources.INSTANCE.home());
        layout.addMember( buildVinCaptureForm());
        vinCapturePanel.addMember(layout);
        
        push( vinCapturePanel );
        

    }
    
    private void discardAllChildPanels() {
    	discardDefectEntryPanels();
    	discardPanel( vinIssueListPanel );
    	discardPanel( defectViewerPanel );
    	
    	vinIssueListPanel = null;
    	defectViewerPanel = null;
    }
    
    private void discardDefectEntryPanels() {
    	discardPanel(partGroupSelectPanel);
    	discardPanel(partSelectPanel);
    	discardPanel(partLocationSelectPanel);
    	discardPanel(partDefectSelectPanel);
    	discardPanel(otherPartSelectPanel);
    	discardPanel(saveNewDefectPanel);
    	
    	partGroupSelectPanel = null;
    	partSelectPanel = null;
    	partLocationSelectPanel = null;
    	partDefectSelectPanel = null;
    	otherPartSelectPanel = null;
    	saveNewDefectPanel = null;
    }
    
    private void discardPanel( Panel panel ) {
    	if ( panel != null ) {
    		panel.destroy();
    	}
    }
    
   
	@SuppressWarnings("unchecked")
	public void registerHandlers() {
	
		
		
		   // Fired when VIN barcode is scanned
		   // Action is to create the vin issue list panel
	       PubSubBus.EVENT_BUS.addHandler( VinScannedEvent.TYPE,  new VinScannedEvent.Handler<VinScannedEvent>() {
	           @Override
	           public void onEvent(VinScannedEvent vinScannedEvent) {
	        	    Log.info("VIN SCANNED IS " + vinScannedEvent.getEventData() );
		        	vinDefectsModel.setVin(vinScannedEvent.getEventData());
		        	vinDefectsModel.setMto(null);
		        	vinDefectsModel.setProcessPoint(Settings.getProcessPoint());
		        	vinDefectsModel.setDefectRecord(null);
		        	
		        	openVinIssuesListPanel( vinDefectsModel );
	           }

	       });
	       
	       
	       
	       // Fired when Defect Results are received
	       PubSubBus.EVENT_BUS.addHandler(DefectResultsReceivedEvent.TYPE, new DefectResultsReceivedEvent.Handler<DefectResultsReceivedEvent>()     {
	           @Override
	           public void onEvent(DefectResultsReceivedEvent defectResultsReceivedEvent) {
	        	   List<DefectResult> vsList = defectResultsReceivedEvent.getEventData();
	        	   vinDefectsModel.setDefectResultList(vsList);
	        	   vinDefectsModel.setSelectedDefectResultId(null);
	           }
	       });	  
	       
	       // Fired when the VIN is looked up and not found
	       // Action Display a message and go prepare to scan another VIN
	       //
	       // Note: The code to popTo the defectsPanel is put in the say callback.
	       // If this is not done, the navstack gets goofed up when you are displaying
	       // the say dialog and popTo at the same time. This display is also goofed if
	       // you move the say to after the discardAllChildPanels().
	       PubSubBus.EVENT_BUS.addHandler(VinNotFoundEvent.TYPE, new VinNotFoundEvent.Handler<VinNotFoundEvent>() {

				@Override
				public void onEvent(VinNotFoundEvent vinNotFoundEvent) {

					SC.say( "VIN " + vinNotFoundEvent.getEventData() + " was not found.",
							new BooleanCallback(){

								@Override
								public void execute(Boolean value) {
									vinDefectsModel.setVin(null);
									
					           	   // pop to the top panel
					        	   popTo( vinCapturePanel );
					
					        	   // Get rid of the useless panels so they are
					        	   // garbage collected.
					        	  discardAllChildPanels();									
								}});
					
				}

	       });	     
	       
	       // When a user selects a defect
	       // If it is closed, just display the defect info
	       // If it is open, display the edit form
	       PubSubBus.EVENT_BUS.addHandler(DefectSelectedEvent.TYPE, new DefectSelectedEvent.Handler<DefectSelectedEvent>()     {
	           @Override
	           public void onEvent(DefectSelectedEvent defectSelectedEvent) {
	        	   // get the status
	        	   Record record = defectSelectedEvent.getEventData();
	        	   Integer selectedId = record.getAttributeAsInt("id");
	        	   vinDefectsModel.setSelectedDefectResultId(selectedId);
	        	   vinDefectsModel.setDefectRecord(record);
	        	   String status = record.getAttribute("status");
	        	   if ( "Open".equals( status )) {
	        		   
	        		   RepairWizard repairWizard = new RepairWizard( navStack, vinDefectsModel, new DoneCallback<AddNewDefectRepairResultRequest>(){
						@Override
						public void onDone(	AddNewDefectRepairResultRequest addNewDefectRepairResultRequest) {
		        			   saveDefectRepairResult( addNewDefectRepairResultRequest );
						}});
	        		   repairWizard.start();
	        		   
	        		   
	        	   } else {
	        		   // Open viewer
	        		   defectViewerPanel = new DefectViewerPanel( vinDefectsModel);
	        		   push( defectViewerPanel );
	        	   }
	           }
	       });	     
	       
	       
	       // Fired when User wants to create a new defect
	       // Action is to display the Part Group Selection panel
	       PubSubBus.EVENT_BUS.addHandler(CreateDefectRequestEvent.TYPE, new CreateDefectRequestEvent.Handler<CreateDefectRequestEvent>()     {
	           @Override
	           public void onEvent(CreateDefectRequestEvent event) {
	        	   
	        	   vinDefectsModel.setPartGroup( null );
	        	   vinDefectsModel.setPart( null );
	        	   vinDefectsModel.setPartLocation(null);
	        	   vinDefectsModel.setPartDefect(null);
	        	   vinDefectsModel.setOtherPart(null);
	        	   
	        	   openPartGroupSelectionPanel( vinDefectsModel );

	           }
	       });	     
	       
	       
	       // Fired when User selects a part group while creating a new defect
	       // Action is to display the Part Location Selection panel
	       PubSubBus.EVENT_BUS.addHandler(PartGroupSelectedEvent.TYPE, new PartGroupSelectedEvent.Handler<PartGroupSelectedEvent>()     {
	           @Override
	           public void onEvent(PartGroupSelectedEvent event) {
	        	   // Add the part group to the model
	        	   vinDefectsModel.setPartGroup(event.getEventData());
	        	   
	        	   // Clear dependent choices
	        	   vinDefectsModel.setPart( null );
	        	   vinDefectsModel.setPartLocation(null);
	        	   vinDefectsModel.setPartDefect(null);
	        	   vinDefectsModel.setOtherPart(null);
	        	   
	        	   // Display panel to select part location
	        	   openPartSelectionPanel( vinDefectsModel);
	        	   
	           }
	       });	    
	       
	       
	       // Fired when User selects a part while creating a new defect
	       // Action is to display the Part Location Selection panel
	       PubSubBus.EVENT_BUS.addHandler(PartSelectedEvent.TYPE, new PartSelectedEvent.Handler<PartSelectedEvent>()     {
	           @Override
	           public void onEvent(PartSelectedEvent event) {
	        	   // Add the part group to the model
	        	   vinDefectsModel.setPart(event.getEventData());
	        	   
	        	   // Clear dependent choices
	        	   vinDefectsModel.setPartLocation(null);
	        	   vinDefectsModel.setPartDefect(null);
	        	   vinDefectsModel.setOtherPart(null);
	
	        	   openPartLocationSelectionPanel( vinDefectsModel );

	           }
	       });	 
		       
		       
	       // Fired when User selects a part location while creating a new defect
	       // Action is to display the Part Location Selection panel
	       PubSubBus.EVENT_BUS.addHandler(PartLocationSelectedEvent.TYPE, new PartLocationSelectedEvent.Handler<PartLocationSelectedEvent>()     {
	           @Override
	           public void onEvent(PartLocationSelectedEvent event) {
	        	   // Add the part group to the model
	        	   vinDefectsModel.setPartLocation(event.getEventData());
	        	   
	        	   // Clear dependent choices
	        	   vinDefectsModel.setPartDefect(null);
	        	   vinDefectsModel.setOtherPart(null);
	
	        	   openPartDefectSelectionPanel(vinDefectsModel);

	           }
	       });	 
		   
		   
	       // Fired when User selects a defect while creating a new defect
	       // Action is to display the Other Part Selection panel if there are multiple other parts
		   // If there are no other parts, don't make the user do it.    
	       PubSubBus.EVENT_BUS.addHandler(PartDefectSelectedEvent.TYPE, new PartDefectSelectedEvent.Handler<PartDefectSelectedEvent>()     {
	           @Override
	           public void onEvent(PartDefectSelectedEvent event) {
	        	   // Add the part group to the model
	        	   vinDefectsModel.setPartDefect(event.getEventData());
	        	   // Clear dependent choices
	         	   vinDefectsModel.setOtherPart(null);
	
	        	   // Display panel to select part location
	         	  openOtherPartSelectionPanel( vinDefectsModel );
	           }
	       });	 
	   
	
			// Fired when User selects an other part while creating a new defect
			// Action is to display the Part Location Selection panel
			PubSubBus.EVENT_BUS.addHandler(OtherPartSelectedEvent.TYPE, new OtherPartSelectedEvent.Handler<OtherPartSelectedEvent>()     {
			    @Override
			    public void onEvent(OtherPartSelectedEvent event) {
			 	   // Add the part group to the model
			 	   vinDefectsModel.setOtherPart(event.getEventData());
			 	   // Display the save panel
			 	   
			 	   saveNewDefectPanel = new SaveNewDefectPanel(vinDefectsModel);
			 	   push(saveNewDefectPanel );
			    }
			});	 
			
	
		   
			// Fired when User cancels the entry of a new defect
			// Action is go back to the vin issues list
			PubSubBus.EVENT_BUS.addHandler(CancelDefectRequestEvent.TYPE, new CancelDefectRequestEvent.Handler<CancelDefectRequestEvent>()     {
			    @Override
			    public void onEvent(CancelDefectRequestEvent event) {
			    	// Clean up the model
	        	   vinDefectsModel.setPartGroup( null );
	        	   vinDefectsModel.setPart( null );
	        	   vinDefectsModel.setPartLocation(null);
	        	   vinDefectsModel.setPartDefect(null);
	        	   vinDefectsModel.setOtherPart(null);
	
	        	   // pop to show the issues
	        	   popTo( vinIssueListPanel );
	
	        	   // The following defect entry panels are useless so let them get 
	        	   // garbage collected.
	        	   discardDefectEntryPanels();
	        	   
			    }
			});	 
			
			
			// Fired when User clicks clear the VIN
			// Action is go back to the vin issues list
			PubSubBus.EVENT_BUS.addHandler(ClearVinEvent.TYPE, new ClearVinEvent.Handler<ClearVinEvent>()     {
			    @Override
			    public void onEvent(ClearVinEvent event) {
	 	        	   
	        	   // Clean up the model
			       vinDefectsModel.setVin(null);
			       vinDefectsModel.setMto(null);
			       vinDefectsModel.setProductionLot(null);
	        	   vinDefectsModel.setPartGroup( null );
	        	   vinDefectsModel.setPart( null );
	        	   vinDefectsModel.setPartLocation(null);
	        	   vinDefectsModel.setPartDefect(null);
	        	   vinDefectsModel.setOtherPart(null);
	        	   
	           	   // pop to the top panel
	        	   popTo( vinCapturePanel );
	
	
	        	   // Get rid of the useless so let them get 
	        	   // garbage collected.
	        	   discardAllChildPanels();
	        	   
			    }
			});	 
			
			
			// Fired when some data access problem occurred
			// Action is tell the user
			PubSubBus.EVENT_BUS.addHandler(DataAccessErrorEvent.TYPE, new DataAccessErrorEvent.Handler<DataAccessErrorEvent>()     {
			    @Override
			    public void onEvent(DataAccessErrorEvent event) {
	 	        	   
			       // Build a message to display to the user.
			    	
			       String msg = "<p> An error occurred communicating with the server.";
			       msg += "  There are many possible causes which include: ";
			       msg += "<ul>";
			       msg += "<li> There is no WIFI available at your current location.  You may move to a covered location and retry.";
			       msg += "<li> The data contains an error.  Please check that data and make corrections, if necessary.";
			       msg += "<li> There may be a an applicaton error.  Contact you support personnel.";
			       msg += "</ul></p>";
			       if ( event.getEventData().getMessage() != null ) {
			    	   msg += "<p> Detailed Message: " + event.getEventData().getMessage() + "</p>";
			       }
	
			       if ( event.getEventData().getException() != null ) {
			    	   msg += "<p> Detailed Exception: " + event.getEventData().getException().getMessage() + "</p>";
			    	   
			    	   // for developer assistance print stack trace
			    	   event.getEventData().getException().printStackTrace();
			       }
			       
			       Log.error( event.getEventData().getMessage(), event.getEventData().getException());
			       // Show the user the message, no callback is needed
			       SC.say("Error", divLeftAlign(msg));
	       	   
			    }
			});	
			
			// Fired a defect is added or updated
			// Action is to clean up some of the panels and pretend like the vin was rescanned.
			PubSubBus.EVENT_BUS.addHandler(DefectsChangedEvent.TYPE, new DefectsChangedEvent.Handler<DefectsChangedEvent>()     {
			    @Override
			    public void onEvent(DefectsChangedEvent event) {
			    	
			    	
			    	// Re-process the VIN
		        	vinDefectsModel.setVin(event.getEventData());
		        	vinDefectsModel.setMto(null);
		        	vinDefectsModel.setProcessPoint(Settings.getProcessPoint());
		        	vinDefectsModel.setDefectRecord(null);
		        	
		        	// pop to top to clear navigation stack
		        	popTo( vinCapturePanel );
		        	
		        	// discard un-needed panels
		        	discardAllChildPanels();
		        	
		        	// create panel for issues and retrieve them
		        	openVinIssuesListPanel( vinDefectsModel );
			    }
			});
			
			// Setting changed
			// Action is to record change in the log
			PubSubBus.EVENT_BUS.addHandler(SettingsChangedEvent.TYPE, new SettingsChangedEvent.Handler<SettingsChangedEvent>()     {
			    @Override
			    public void onEvent(SettingsChangedEvent event) {
			    	SettingsChangedEventData data = event.getEventData();
			    	
			    	Log.info("Setting " + quoteForMessage( data.getKey() ) + 
			    			" changed from " + quoteForMessage(data.getOldValue()) + 
			    			" to " + quoteForMessage(data.getNewValue()) );
			    }
			});
			
			
			// Exception Event, intended for uncaught exceptions
			// Action is to display a message record  in the log
			// UmbrellaExceptions are ignored
			PubSubBus.EVENT_BUS.addHandler(ExceptionEvent.TYPE, new ExceptionEvent.Handler<ExceptionEvent>()     {
			    @Override
			    public void onEvent(ExceptionEvent event) {
			    	Throwable ex = event.getEventData();
			    	
			    	// The event bus generates an UmbrellaException every time, it tests
			    	// to see if an event can be handled by a handler.  Current solution 
			    	// is to ignore UmbrellaExcetions.
			    	if (! (ex instanceof UmbrellaException) ) {
				    	String msg = buildExceptionMessage( ex, "<li>", "</li>");
				    	Log.info(msg);
				    	SC.say("Error", divLeftAlign( msg ));
			    	}
			    }
			});


	}  // End of Registering Events
	
	// Retreive MTO and Lot, then update the model
	private void fillInMtoAndLot(final VinDefectsModel vinDefectsModel ) {
		
		FrameDataService service = DataServiceFactory.getInstance().getFrameDataService();
		service.getFrame(vinDefectsModel.getVin(), new MethodCallback<Frame>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "getFrame ", exception ));
			}

			@Override
			public void onSuccess(Method method, Frame frame) {
				if ( frame == null ) {
					PubSubBus.EVENT_BUS.fireEvent(new VinNotFoundEvent(vinDefectsModel.getVin()) );
				} else {
					vinDefectsModel.setMto( frame.getMto());	
					vinDefectsModel.setProductionLot(frame.getProductionLot());
				}
			} });
		
	}
	
	// Retrieve the defects for the vin and open the panel to display them
	// Trigger retrieval of MTO and production Lot
	private void openVinIssuesListPanel( final VinDefectsModel vinDefectsModel ) {
    	// Retrieve the issues 
		WaitPanel.getInstance().start();
        DefectResultDataService service = DataServiceFactory.getInstance().getDefectResultDataService();
        service.getDefects(vinDefectsModel.getVin(), new MethodCallback<List<DefectResult>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				WaitPanel.getInstance().end();
				PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "getDefects ", exception));
				
			}

			@Override
			public void onSuccess(Method method, List<DefectResult> defectResultList) {
				WaitPanel.getInstance().end();
				if ( defectResultList == null ) {
					PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "getDefects null "));
				} else {
		        	vinIssueListPanel = new VinIssueListPanel(vinDefectsModel, defectResultList );
		    		
	        	    // make the panel visible
		           	push(vinIssueListPanel );
		           	
		           	// retrieve and set the MTO and Lot
		           	fillInMtoAndLot(vinDefectsModel );
				}
			}
        });
	}
	
	// Retrieve part groups and display a panel to select one
	private void openPartGroupSelectionPanel( final VinDefectsModel vinDefectsModel  )  {
        InspectionModelDataService service = DataServiceFactory.getInstance().getInspectionModelDataService();
        WaitPanel.getInstance().start();
        service.findPartGroupNamesByApplicationIdAndModelCode(
        		vinDefectsModel.getProcessPoint(), 
        		vinDefectsModel.getModelCode(),
        		new MethodCallback<List<String>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				WaitPanel.getInstance().end();
				PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "findPartGroupNamesByApplicationIdAndModelCode  ", exception ));
			}

			@Override
			public void onSuccess(Method method, List<String> partGroupNames) {
				WaitPanel.getInstance().end();
				if ( partGroupNames == null ) {
					PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "findPartGroupNamesByApplicationIdAndModelCode null "));
				} else {
		        	   partGroupSelectPanel = new DefectCreationSelectPanel( "Part Group Select",
		        			   "Select a part group", vinDefectsModel, partGroupNames, 
		        			   new DoneCallback<String>(){

								@Override
								public void onDone(String result) {
									PubSubBus.EVENT_BUS.fireEvent(new PartGroupSelectedEvent( result ));
								}} );
		        	   push(partGroupSelectPanel );
				}
			}
        });
	}
	private void openPartSelectionPanel( final VinDefectsModel vinDefectsModel  )  {
        DefectDescriptionDataService service = DataServiceFactory.getInstance().getDefectDescriptionDataService();
        WaitPanel.getInstance().end();
        service.findInspectionPartNamesByPartGroupName(
        		vinDefectsModel.getPartGroup(), 
        		new MethodCallback<List<String>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				WaitPanel.getInstance().end();
				PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "findInspectionPartNamesByPartGroupName ", exception));
			}

			@Override
			public void onSuccess(Method method, List<String> partNames) {
				WaitPanel.getInstance().end();
				if ( partNames == null ) {
					PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "findInspectionPartNamesByPartGroupName null "));
				} else {
		        	   partSelectPanel = new DefectCreationSelectPanel( "Part Select",
		        			   "Select a part", vinDefectsModel, partNames, 
		        			   new DoneCallback<String>(){

								@Override
								public void onDone(String result) {
									PubSubBus.EVENT_BUS.fireEvent(new PartSelectedEvent( result ));
								}} );
		        	   
		        	   push(partSelectPanel );
				}
			}
        });

	}
	private void openPartLocationSelectionPanel( final VinDefectsModel vinDefectsModel  )  {
		// Get the locations
        DefectDescriptionDataService service = DataServiceFactory.getInstance().getDefectDescriptionDataService();
        WaitPanel.getInstance().start();
        service.findInspectionPartLocationNamesByPartGroupNameAndInspectionPartName(
        		vinDefectsModel.getPartGroup(), 
        		vinDefectsModel.getPart(),
        		new MethodCallback<List<String>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				WaitPanel.getInstance().end();
				PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "findInspectionPartLocationNamesByPartGroupNameAndInspectionPartName  ", exception ));
			}

			@Override
			public void onSuccess(Method method, List<String> partLocationNames) {
				WaitPanel.getInstance().end();
				if ( partLocationNames == null ) {
					PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "findInspectionPartLocationNamesByPartGroupNameAndInspectionPartName is null  "));
				} else if ( partLocationNames.size() == 1 ) {
					// Pick this for the user
					PubSubBus.EVENT_BUS.fireEvent(new PartLocationSelectedEvent( partLocationNames.get(0) ));
				} else {
					// make the user select one
					partLocationSelectPanel = new DefectCreationSelectPanel( "Part Location Select",
		        			   "Select a part location", vinDefectsModel, partLocationNames, 
		        			   new DoneCallback<String>(){

								@Override
								public void onDone(String result) {
									PubSubBus.EVENT_BUS.fireEvent(new PartLocationSelectedEvent( result ));
								}} );
					push(partLocationSelectPanel );
				}
			}
        });		

	}
	private void openPartDefectSelectionPanel( final VinDefectsModel vinDefectsModel  )  {
		// Get the locations
        DefectDescriptionDataService service = DataServiceFactory.getInstance().getDefectDescriptionDataService();
        WaitPanel.getInstance().start();
        service.findPartDefectTypeNames(
        		vinDefectsModel.getPartGroup(), 
        		vinDefectsModel.getPart(),
        		vinDefectsModel.getPartLocation(),
        		new MethodCallback<List<String>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				WaitPanel.getInstance().end();
				PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "findPartDefectTypeNames ", exception));
			}

			@Override
			public void onSuccess(Method method, List<String> partDefectTypeNames) {
				WaitPanel.getInstance().end();
				if ( partDefectTypeNames == null ) {
					PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "findPartDefectTypeNames  null "));
				} else if ( partDefectTypeNames.size() == 1 ) {
					// Pick this for the user
					PubSubBus.EVENT_BUS.fireEvent(new PartDefectSelectedEvent( partDefectTypeNames.get(0) ));
				} else {
					// make the user select one
					partDefectSelectPanel = new DefectCreationSelectPanel( "Defect Select",
		        			   "Select a part defect", vinDefectsModel, partDefectTypeNames, 
		        			   new DoneCallback<String>(){

								@Override
								public void onDone(String result) {
									PubSubBus.EVENT_BUS.fireEvent(new PartDefectSelectedEvent( result ));
								}} );
 				    push( partDefectSelectPanel );
				}
			}
        });		 	   
		
	}
	
	private void openOtherPartSelectionPanel( final VinDefectsModel vinDefectsModel  )  {
		// Get the locations
        DefectDescriptionDataService service = DataServiceFactory.getInstance().getDefectDescriptionDataService();
        WaitPanel.getInstance().start();
        service.findSecondaryPartNames(
        		vinDefectsModel.getPartGroup(), 
        		vinDefectsModel.getPart(),
        		vinDefectsModel.getPartLocation(),
        		vinDefectsModel.getPartDefect(),
        		new MethodCallback<List<String>>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				WaitPanel.getInstance().end();
				PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "findSecondaryPartNames ", exception));
			}

			@Override
			public void onSuccess(Method method, List<String> secondaryPartNames) {
				WaitPanel.getInstance().end();
				if ( secondaryPartNames == null ) {
					PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "findSecondaryPartNames response was null "));
				} else if ( secondaryPartNames.size() == 1 ) {
					// Pick this for the user
					PubSubBus.EVENT_BUS.fireEvent(new OtherPartSelectedEvent( secondaryPartNames.get(0) ));
				} else {
					// make the user select one
					otherPartSelectPanel = new DefectCreationSelectPanel( "Other Part Select",
		        			   "Select a part defect", vinDefectsModel, secondaryPartNames, 
		        			   new DoneCallback<String>(){

								@Override
								public void onDone(String result) {
									PubSubBus.EVENT_BUS.fireEvent(new OtherPartSelectedEvent( result ));
								}} ); 				   
					push( otherPartSelectPanel );
				}
			}
        });		 	 
        
				
	}
	
	private Panel buildVinCaptureForm() {
		Panel panel = new VinCaptureForm( vinDefectsModel, new DoneCallback<String>(){

			@Override
			public void onDone(String result) {
            	// Check for network connectivity
            	if( PhGap.isConnected()) {
            		PhGap.doTactalFeedback();
            		PubSubBus.EVENT_BUS.fireEvent( new VinScannedEvent(result));
            	} else {
            		TimedDialog.timedNotification("Device is not currently connected to a network.", 3);
            	}
			}});
		return panel;
	}
	
	private void saveDefectRepairResult( AddNewDefectRepairResultRequest addNewDefectRepairResultRequest ) {
		DefectResultDataService service = DataServiceFactory.getInstance().getDefectResultDataService();
		WaitPanel.getInstance().start();
		Map<String,AddNewDefectRepairResultRequest> req = new HashMap<String,AddNewDefectRepairResultRequest>();
		req.put("com.honda.galc.dao.qics.vo.AddNewDefectRepairResultRequest", addNewDefectRepairResultRequest);
		service.addNewDefectRepairResult( req, new MethodCallback<Void>(){

			@Override
			public void onFailure(Method method, Throwable exception) {
				WaitPanel.getInstance().end();
				PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "addNewDefectRepairResult Error saving the Repair Result  " , exception));
			}

			@Override
			public void onSuccess(Method method, Void response) {
				WaitPanel.getInstance().end();
				PubSubBus.EVENT_BUS.fireEvent(new DefectsChangedEvent( vinDefectsModel.getVin() ));
			}});
	}
	

	private String quoteForMessage( Object obj ) {
		String m = "NULL";
		if ( obj != null ) {
			m = "'" + obj.toString() + "'";
		}
		
		return m;
	}
	
	private String divLeftAlign( String s ) {
		String out = "<div style=\"text-align:left\">" + s + "</div>";
		return out;
	}
	
	private String buildExceptionMessage( Throwable ex, String prefix,  String suffix ) {
		String msg = "";
		if ( ex == null ) {
			return msg;
		}
		if ( ex instanceof UmbrellaException) {
			UmbrellaException ue = (UmbrellaException) ex;
			for( Throwable c : ue.getCauses()) {
				msg += buildExceptionMessage( c, prefix, suffix );
			}
		} else {
			msg += prefix;
			msg += ex.getMessage();
			msg += suffix;
			msg += buildExceptionMessage( ex.getCause(), prefix, suffix );		
		}
		return msg;
	}
	

	
	public Throwable unwrap(Throwable e) {
		if (e instanceof UmbrellaException) {
			UmbrellaException ue = (UmbrellaException) e;
			if (ue.getCauses().size() == 1) {
				return unwrap(ue.getCauses().iterator().next());
			}
		}
		return e;
	}
}

