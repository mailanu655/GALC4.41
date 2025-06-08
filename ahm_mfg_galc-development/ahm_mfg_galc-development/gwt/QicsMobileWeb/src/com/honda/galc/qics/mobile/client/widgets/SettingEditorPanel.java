package com.honda.galc.qics.mobile.client.widgets;


import com.honda.galc.qics.mobile.client.Settings;
import com.honda.galc.qics.mobile.client.widgets.form.StringItem;
import com.honda.galc.qics.mobile.client.widgets.form.Verifier;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.mobile.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStrip;
import com.smartgwt.mobile.client.widgets.toolbar.ToolStripButton;

/**
 * This panel is used to Alter a single setting.  It is composed of a panel containing
 * 1) Dynamic Form with one Field for the Setting
 * 2) A MessageListPanel for displaying invalid Setting messages
 * 3) A toolbar with Submit and Cancel buttons.
 * 
 * When the submit button is pressed, a Verifier is checked. It passes the 
 * setting is changed.  The Setting takes care of publishing the change.
 * 
 * When Cancel pressed, the current value of the setting is put in the 
 * form.
 * 
 * @author vfc01346
 *
 */
public class SettingEditorPanel extends Panel {
	
    final ToolStripButton submitButton = new ToolStripButton("Submit");
    final ToolStripButton cancelButton = new ToolStripButton("Cancel");
    final ToolStripButton resetButton = new ToolStripButton("Default Value");
    
    final boolean REQUIRED = true;

 	private String settingKey;
    private String settingDefaultValue;

    DynamicForm dynamicForm = new DynamicForm();
 
    StringItem settingItem;
    
    ToolStrip toolbar = new ToolStrip();
    
    // holder of messages
    final MessageList messageList ;
    
    // displays the messages
    final MessageListPanel messageListPanel;
    
    public SettingEditorPanel( final String settingKey, 
    						final String defaultValue,
    		                final String settingTitle, 
    		                final Verifier verifier ) {
        super(settingTitle + " Editor");
        this.settingDefaultValue = defaultValue;
        this.settingKey = settingKey;
        
        // create holder for messages and a panel to display them
        messageList = new MessageList();
        messageListPanel = new MessageListPanel( messageList );
        
        settingItem = new StringItem(settingKey,  settingTitle, "");
        settingItem.setValue( Settings.getProperty(settingKey, defaultValue));
        settingItem.addVerifier( verifier);
        verifier.setMessageList(this.messageList);
        
        dynamicForm.setFields(new FormItem[]{settingItem});
 
        // Add a listener to enable and disable the submit button. The button
        // is enabled if the only value is valid and the setting is different 
        // than the one we have.

        ItemChangedHandler handler = new ItemChangedHandler(){

    		@Override
    		public void onItemChanged(ItemChangedEvent event) {
    			handleChange((StringItem) event.getFormItem() );
     		}
        	
        };
        
      dynamicForm.addItemChangedHandler( handler );
        

        
        submitButton.disable();
        submitButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	if( settingItem.validate() ) {
            		String v = settingItem.getValueAsString();
            		Settings.setProperty( settingKey, v );
            		TimedDialog.timedNotification( settingTitle + " set.", 3 );
            		submitButton.disable();
            	}
            }
        });
        
        // Cancel means restore the current value
        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	// get the current value and put it in the form
            	String v = Settings.getProperty( settingKey, defaultValue);
            	settingItem.setValue( v );
            	messageListPanel.clearMessage();     
            	submitButton.disable();
            }
        });
        
        // Reset means restore the default value
        resetButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            	// get the current value and put it in the form
            	settingItem.setValue(defaultValue);
            	messageListPanel.clearMessage(); 
            	// do this to have submit button enabled/disabled properly
            	handleChange( settingItem );
            }
        });
       
        toolbar.addButton(submitButton);
        toolbar.addButton( cancelButton );
        toolbar.addButton( resetButton );
        addMember( messageListPanel );
        addMember(dynamicForm);  
        addMember(toolbar);       
        

        
     }
    
    
    public void handleChange( StringItem stringItem) {
		boolean vld = stringItem.validate();
		if ( vld && ! stringItem.getValueAsString().equals( Settings.getProperty(this.settingKey, this.settingDefaultValue))) {
			submitButton.enable();
		} else {
			submitButton.disable();
		}  	
    }
    protected MessageList getMessageList() {
    	return this.messageList;
    }
}
