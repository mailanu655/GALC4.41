package com.honda.galc.qics.mobile.client.widgets.form;

import com.honda.galc.qics.mobile.client.widgets.MessageList;
import com.smartgwt.mobile.client.widgets.form.fields.SelectItem;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.mobile.client.widgets.form.fields.events.BlurHandler;

public class RequiredIfItemSelectedVerifier extends Verifier {

	/**
	 * The maximum integer value.
	 */
	private SelectItem selectItem;
	private Object selectedValue;
	
	/**
	 * Instantiates a new value verifier.
	 *
	 * @param messageList the message list
	 * @param maxValue the selector to check
	 * @param selectedValue the value that when selected makes this field required.
	 */
	public RequiredIfItemSelectedVerifier(MessageList messageList, SelectItem selectItem, Object selectedValue ) {
		super(messageList);
		assert selectItem != null;
		assert selectedValue != null;
		this.selectItem = selectItem;
		this.selectedValue = selectedValue;
	}

	/* (non-Javadoc)
	 * @see com.honda.hma.qics.mobile.client.widgets.form.Verifier#validate(com.honda.hma.qics.mobile.client.widgets.form.StringItem)
	 */
	@Override
	public boolean validate(Verifiable stringItem) {
		assert stringItem != null;
		boolean valid = true;
		if ( selectItem.getValue() != null && 
				selectItem.getValue().equals( selectedValue )) {
			// The selected value matches so this item is required.
			valid = stringItem.getValueAsString() != null && 
					stringItem.getValueAsString().length() > 0;
			if ( !valid ) {
				addMessage( stringItem, "is required for the selected " + selectItem.getTitle() + " value.");
			}
		}

		return valid;
	}
	
	/**
	 * Gets the blur handler to be added to the SelectedItem .
	 */
	public  BlurHandler getBlurHandlerForSelectItem( final StringItem stringItem) {
		return (new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				removeMessages(stringItem );
				validate(stringItem);
				
			} 
			});
		
	}
	
}
