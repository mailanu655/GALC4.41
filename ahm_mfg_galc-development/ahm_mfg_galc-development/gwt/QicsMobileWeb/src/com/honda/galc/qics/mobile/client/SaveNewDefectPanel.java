package com.honda.galc.qics.mobile.client;


import java.util.HashMap;
import java.util.Map;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.honda.galc.qics.mobile.client.dataservice.DataServiceFactory;
import com.honda.galc.qics.mobile.client.dataservice.DefectResultDataService;
import com.honda.galc.qics.mobile.client.events.DataAccessErrorEvent;
import com.honda.galc.qics.mobile.client.events.DefectsChangedEvent;
import com.honda.galc.qics.mobile.client.events.PubSubBus;
import com.honda.galc.qics.mobile.client.widgets.NewDefectHeader;
import com.honda.galc.qics.mobile.client.widgets.VinInfoPanel;
import com.honda.galc.qics.mobile.client.widgets.WaitPanel;
import com.honda.galc.qics.mobile.shared.entity.AddNewDefectResultRequest;
import com.honda.galc.qics.mobile.shared.entity.DefectResult;
import com.honda.galc.qics.mobile.shared.entity.DefectResultId;
import com.honda.galc.qics.mobile.shared.entity.DefectStatus;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.ActivityIndicator;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.VLayout;

/**
 * This panel allows user to Save a new defect.
 */
public class SaveNewDefectPanel extends ScrollablePanel {

	private final ActivityIndicator activityIndicator;
    private final Button saveButton = new Button("Save New Defect Entry", Button.ButtonType.ACTION_GREEN);

	private VinDefectsModel vinDefectsModel;
	
    public SaveNewDefectPanel( final VinDefectsModel vinDefectsModel ) {
		super("Save New Defect");
	
		this.vinDefectsModel = vinDefectsModel;
		
		VLayout layout = new VLayout();
        layout.setWidth("100%");
        layout.setAlign(Alignment.LEFT);
 
		VinInfoPanel vinInfoPanel = new VinInfoPanel("Vin Info", vinDefectsModel );
        NewDefectHeader header = new NewDefectHeader(vinDefectsModel, "");
        
        activityIndicator = new ActivityIndicator();
        activityIndicator.setVisible(false);
       
	    saveButton.enable();
	    saveButton.setWidth("50%");
	    saveButton.addClickHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
	        	// Disable the button to avoid multiple submits
	        	saveButton.disable();
	        	save();
	        }
	    });
	    
	    layout.addMember(vinInfoPanel);
	    layout.addMember(header);
	    layout.addMember(saveButton);
	    
	    addMember( layout);

 	}

    private void save() {
    	
    	// Build the DefectResult
    	DefectResult d = new DefectResult();
    	
    	DefectResultId id = new DefectResultId();
    	id.setInspectionPartName(vinDefectsModel.getPart());
    	id.setApplicationId(vinDefectsModel.getProcessPoint());
    	id.setDefectTypeName(vinDefectsModel.getPartDefect());
    	id.setInspectionPartLocationName(vinDefectsModel.getPartLocation());
    	id.setProductId(vinDefectsModel.getVin());
    	id.setSecondaryPartName(vinDefectsModel.getOtherPart());
    	id.setTwoPartPairLocation("");
    	id.setTwoPartPairPart("");
   
    	
    	d.setId(id);
    	d.setIsChangeAtRepair(false);
    	d.setIsNewDefect(true);
    	d.setOutstandingFlag(DefectStatus.OUTSTANDING.getId());
    	
    	AddNewDefectResultRequest request = new AddNewDefectResultRequest();
    	request.setProductId(vinDefectsModel.getVin());
    	request.setProcessPointId(vinDefectsModel.getProcessPoint());
    	request.setNewDefectResult(d);
    	
    	Map<String,AddNewDefectResultRequest> addRequest = new HashMap<String,AddNewDefectResultRequest>();
    	addRequest.put("com.honda.galc.dao.qics.vo.AddNewDefectResultRequest", request);
    	
    	DefectResultDataService ds = DataServiceFactory.getInstance().getDefectResultDataService();
		WaitPanel.getInstance().start();

    	ds.addNewDefectResult(addRequest,	new MethodCallback<Void>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				WaitPanel.getInstance().end();
				PubSubBus.EVENT_BUS.fireEvent(new DataAccessErrorEvent( "addNewDefectResult ", exception));
			}

			@Override
			public void onSuccess(Method method, Void response) {
				WaitPanel.getInstance().end();
				PubSubBus.EVENT_BUS.fireEvent(new DefectsChangedEvent( vinDefectsModel.getVin()));
			}		
    	}); 	
     }
}
