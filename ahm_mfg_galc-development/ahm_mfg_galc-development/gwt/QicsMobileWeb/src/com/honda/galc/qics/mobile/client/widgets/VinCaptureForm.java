package com.honda.galc.qics.mobile.client.widgets;



import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.honda.galc.qics.mobile.client.VinDefectsModel;
import com.honda.galc.qics.mobile.client.utils.DoneCallback;
import com.honda.galc.qics.mobile.client.widgets.form.StringItem;
import com.honda.galc.qics.mobile.client.widgets.form.VinVerifier;
import com.smartgwt.mobile.client.types.Alignment;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.mobile.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.VLayout;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;
import com.honda.galc.qics.mobile.client.widgets.form.ClearKeyboard;

/**
 * This panel is used to Scan a VIN.  It is composed of a panel containing
 * 1) Dynamic Form with one Field for the VIN
 * 2) A MessageListPanel for displaying invalid VIN scans
 * 3) A toobar with Submit and Clear buttons.
 * 
 * When the submit button is pressed, a VinScannedEvent is published.
 * 
 * @author vfc01346
 *
 */
public class VinCaptureForm extends Panel {
	
	private boolean DEV = true;
	
    final Button submitButton = ButtonFactory.buildActionButton("GO");
     
    final boolean REQUIRED = true;

    DynamicForm dynamicForm = new DynamicForm();
    
    StringItem vinItem;
    
    
    // holder of messages
    final MessageList messageList ;
    
    // displays the messages
    final MessageListPanel messageListPanel;
    
    public VinCaptureForm(final VinDefectsModel vinDefectsModel, final DoneCallback<String> callback ) {
        super("Vin Capture");
        setWidth("100%");
        
        messageList = new MessageList();
        messageListPanel = new MessageListPanel( messageList );
        
        // Add a listener to enable and disable the submit button. The button
        // is enabled if the VIN is valid 
        ItemChangedHandler handler = new ItemChangedHandler(){

    		@Override
    		public void onItemChanged(ItemChangedEvent event) {
    			// run verifiers on the form item, enable the submit 
    			// button if it is valid
    			handleChange( (StringItem) event.getFormItem() ) ;
    		}        	
        };

	    
        dynamicForm.addItemChangedHandler( handler );
        
        vinItem = new StringItem("vin", "VIN", "Scan a VIN");
        vinItem.addVerifier( new VinVerifier( messageList ));
        vinItem.setAutoFocus();
        vinItem.focus();
        
        dynamicForm.setFields(new FormItem[]{vinItem});
        
        submitButton.disable();
        submitButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	callback.onDone(vinItem.getValueAsString());

            }
        });
        
        
	    VLayout vlayout = new VLayout();
        vlayout.setWidth("75%");
        vlayout.setAlign(Alignment.CENTER);
	    vlayout.addMember(messageListPanel );
	    vlayout.addMember(dynamicForm);
	    
	    HLayout hlayout = new HLayout();
	    hlayout.addMember(vlayout);
	    hlayout.addMember(submitButton);
	    
	    ClearKeyboard keyboard = new ClearKeyboard( vinItem );

	    addMember( hlayout );
	    addMember( keyboard.getLayout());
               
        if (DEV ) {
        	addMember(buildDevToolbar());       
        }
        
        // When a VIN is cleared, the model is changed.  We need to capture that
        // and update this widget. If it set to null, we need to disable submit.
        vinDefectsModel.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ( "vin".equals(evt.getPropertyName())) {
					vinItem.setValue( evt.getNewValue() );
					if ( evt.getNewValue() == null ) {
						submitButton.disable();
					}
				}
			}
        });
        
       this.vinItem.focus();
     }
    
    /**
     * Handle an entry change by the user.
     * @param stringItem
     */
    private void handleChange( StringItem stringItem ) {
		// run verifiers on the form item, enable the submit 
		// button if it is valid
		if ( stringItem.validate() ) {
			submitButton.enable();
		} else {
			submitButton.disable();
		}

    }
    
    private  ToolStrip buildDevToolbar() {
		ToolStrip toolbar = new ToolStrip(); 
		ToolStripButton devScanButton = new ToolStripButton("Dev Scan");
		ToolStripButton qaScanButton = new ToolStripButton("QA Scan");
		devScanButton.addClickHandler(new ClickHandler() {
		        @Override
		        public void onClick(ClickEvent event) {
		        	// set vin to test vehicle
		    	vinItem.setValue("5FNRL38215B000006");
		     	messageListPanel.clearMessage();
		     	handleChange( vinItem );
		    }
		});
        
        qaScanButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	// set vin to test vehicle
            	vinItem.setValue("5FPYK1F73DB008082");
	         	messageListPanel.clearMessage();
	         	handleChange( vinItem);
            }
        });
        
        // add buttons to toolstrip
        toolbar.addButton(devScanButton);
        toolbar.addButton(qaScanButton);
    	return toolbar;
    	
    }

}


