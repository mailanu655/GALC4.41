package com.honda.galc.qics.mobile.client.widgets;

import com.allen_sauer.gwt.log.client.Log;
import com.honda.galc.qics.mobile.client.utils.DoneCallback;
import com.honda.galc.qics.mobile.client.widgets.form.StringItem;
import com.honda.galc.qics.mobile.client.widgets.form.Keyboard;
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



public class StringEntryPanel extends Panel {
	
	MessageList messageList;
	MessageListPanel messageListPanel;
	StringItem stringItem;
	DoneCallback<String> callback;
	
	FormItem[] formItemArray;
	private Button submitButton;
	
	public StringEntryPanel( String title, 
			MessageList messageList, 
			StringItem stringItem, 
			DoneCallback<String> callback ) {
		this( title, messageList, stringItem, callback, null );
	}


	
	public StringEntryPanel( String title, MessageList messageList, StringItem stringItem, DoneCallback<String> callback, Keyboard keyboard ) {
		super( title );
		Log.info("inside");
		this.stringItem = stringItem;
		this.callback = callback;
		this.messageList = messageList;
		
		this.messageListPanel = new MessageListPanel(messageList);
		
		formItemArray = new FormItem[] { stringItem };
		DynamicForm dynamicForm = new DynamicForm();
	    dynamicForm.setFields(formItemArray);
	    
	    // Add a listener to enable and disable the submit button. The button
	    // is enabled if the only value is valid and the setting is different 
	    // than the one we have.

	    ItemChangedHandler handler = new ItemChangedHandler(){

			@Override
			public void onItemChanged(ItemChangedEvent event) {
				handleChange( event.getFormItem() );
	 		}	    	
	    };
	    
	    dynamicForm.addItemChangedHandler( handler );  
	    
	    VLayout vlayout = new VLayout();
        vlayout.setWidth("75%");
        vlayout.setAlign(Alignment.CENTER);
	    vlayout.addMember(messageListPanel );
	    vlayout.addMember(dynamicForm);
	    
	    if ( keyboard != null ) {
	    	keyboard.setItem(stringItem);
	    	vlayout.addMember(keyboard.getLayout());
	    }

	    
	    HLayout hlayout = new HLayout();
	    hlayout.addMember(vlayout);
	    hlayout.addMember(buildSubmitButton());

	    addMember(hlayout);
	}



     
	private Button buildSubmitButton() {
    
		this.submitButton = ButtonFactory.buildActionButton("Go");
		submitButton.addClickHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
	  		   Log.debug( "Submitting ");
	  		   callback.onDone( stringItem.getValueAsString());
	        }
	    });
		return this.submitButton;
	}



	

	
	private void handleChange( FormItem intItem) {
		intItem.validate();
		if ( messageList.isEmpty()) {
			submitButton.enable();
		} else {
			submitButton.disable();
		}
	}



}
