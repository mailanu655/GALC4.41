package com.honda.galc.qics.mobile.client.widgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.google.gwt.user.client.Window;
import com.honda.galc.qics.mobile.client.VinDefectsModel;
import com.honda.galc.qics.mobile.client.events.ClearVinEvent;
import com.honda.galc.qics.mobile.client.events.PubSubBus;
import com.honda.galc.qics.mobile.client.utils.PhGap;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.BaseButton.ButtonType;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Label;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.VLayout;


/**
 * The Class VinInfoPanel displays basic information about the VIN.  It includes:
 * VIN, MTO, Production Lot.   Values are pull from the VinDefectsModel.  Listeners
 * update the UI on model changes.
 * 
 * A clear VIN button is provided.  It fires the ClearVinEvent.
 */
public class VinInfoPanel extends Panel {

	private static String HEIGHT = "32px";
	private static int MARGIN = 10;
    
    final Label vinLabel  = new Label("VIN:");
    final Label mtoLabel  = new Label("MTO:");
    final Label productionLotLabel  = new Label("Prod Lot:");
    
    // These are not really used as buttons.  They are used to get the background.
    final Button vinValue  = new Button("",Button.ButtonType.ACTION_CANCEL);
    final Button mtoValue  = new Button("",Button.ButtonType.ACTION_CANCEL);
    final Button productionLotValue  = new  Button("",Button.ButtonType.ACTION_CANCEL);;
     
    
	/**
	 * Instantiates a new vin info panel.
	 *
	 * @param title the title
	 * @param vinDefectsModel the vin defects model
	 */
	public VinInfoPanel(String title, VinDefectsModel vinDefectsModel ) {
        super(title);

        vinLabel.setHeight(HEIGHT);
        vinLabel.setMargin( MARGIN );

        mtoLabel.setHeight(HEIGHT);
        mtoLabel.setMargin( MARGIN );
        
        productionLotLabel.setHeight(HEIGHT);
        productionLotLabel.setMargin( MARGIN );
        
        vinValue.setTitle( vinDefectsModel.getVin());
        vinValue.disable();
        vinValue.setHeight(HEIGHT);
        
        mtoValue.setTitle(  vinDefectsModel.getMto());
        mtoValue.disable();
        mtoValue.setHeight(HEIGHT);
        
        productionLotValue.setTitle( vinDefectsModel.getProductionLot());
        productionLotValue.disable();
        productionLotValue.setHeight(HEIGHT);
        
        Button clearVinButton = new Button("Clear VIN", Button.ButtonType.ACTION_DEFAULT );
        clearVinButton.setHeight(HEIGHT);
        clearVinButton.setMargin( MARGIN );
        clearVinButton.setButtonType(ButtonType.BORDERED);
        
        clearVinButton.addClickHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
        		PhGap.doTactalFeedback();
	        	PubSubBus.EVENT_BUS.fireEvent(new ClearVinEvent());
	        }
	    });
 
        boolean isWideScreen = Window.getClientWidth() > 720;
        
        HLayout hlayout = new HLayout();
        hlayout.setAlign( Alignment.CENTER);
        if ( isWideScreen) hlayout.addMember(vinLabel);
        hlayout.addMember(vinValue);
        if ( isWideScreen) hlayout.addMember(mtoLabel);
        hlayout.addMember(mtoValue);
        if ( isWideScreen) hlayout.addMember(productionLotLabel);
        hlayout.addMember(productionLotValue);
        hlayout.addMember( clearVinButton);
        
        // Vertical layout added to center labels and values on a center line
        VLayout vlayout = new VLayout();
        vlayout.setAlign( Alignment.CENTER);
        vlayout.addMember( hlayout );
        
        addMember( vlayout);

        
        vinDefectsModel.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ( "vin".equals(evt.getPropertyName())) {
					vinValue.setTitle( (String) evt.getNewValue());
				} else if ( "mto".equals(evt.getPropertyName())) {
					mtoValue.setTitle( (String) evt.getNewValue());
				} else if ( "productionLot".equals(evt.getPropertyName())) {
					productionLotValue.setTitle( (String) evt.getNewValue() );
				}
			}
        	
        });
		
	}
	
	
}
