package com.honda.galc.qics.mobile.client.widgets.form;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.honda.galc.qics.mobile.client.utils.ElementUtil;
import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.mobile.client.widgets.form.fields.AutoFitTextAreaItem;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;


/**
 * This class provides a form entry item for a String.  This class supports
 * verifiers for validating entries.   This acts as a base class for other 
 * form items that use verifiers.  Verifiers put messages in a MessageList 
 * object.
 * 
 * @author vfc01346
 *
 */
public class StringAreaItem extends AutoFitTextAreaItem implements Verifiable {
    
	/**
	 * This is the list of verifiers that check the form item
	 * on Blur changes and validate() calls.  An item may have zero or 
	 * more verifiers.  Verifiers are checked in the order they are
	 * added to this StringItem.
	 */
	private List<Verifier> verifierList = new ArrayList<Verifier>();
	
	/**
	 * Instantiates a new string item.
	 *
	 * @param name the name, e.g., key
	 * @param title the title, displayed on the form.
	 * @param hint the hint to display when no entry has been made.
	 */
	public StringAreaItem(String name, String title, String hint) {
		super(name, title, hint);
		init();
	}

	/**
	 * Instantiates a new string item.
	 *
	 * @param name the name, e.g., key
	 * @param title the title, displayed on the form.
	 */
	public StringAreaItem(String name, String title) {
		super(name, title);
		init();
	}

	
	/**
	 * Sets this item as auto focus.  There should only be one item
	 * set as auto focus.
	 */
	public void setAutoFocus() {
		this.getElement().setPropertyString("autofocus", "autofocus");
        this.getElement().setAttribute("autofocus", "autofocus");
        this.getElement().setTabIndex(0);
	}
	
	/**
	 * Gives keyboard focus to this element.
	 * Note: Android browser (some?) disables focus.  This trick is
	 * to trigger a click event.
	 * Note:  The trick is it is not possible to know when the widget load 
	 * is completed, in order to use this._getInputElement().focus(). So we 
	 * need to use scheduleDeferred to run this after the browser event 
	 * loop returns.
	 */
	public void focus() {
		
	    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	        	//getElement().focus();
	        	focusImediate();
	        }
	    });
	}
	
	public void focusImediate() {
		//FocusChangedEvent fce = new FocusChangedEvent(true);
		//FocusChangedEvent.fire(this, true);
		
		ElementUtil.mousedownElement(this.getElement());
		ElementUtil.mouseupElement(this.getElement());		
	}	
	
	public void click() {
		ElementUtil.clickElement(this.getElement());
		
	}
	
	public void select() {
		ElementUtil.selectElement(this.getElement());
		forceRepaint();
	}
	
	public void forceRepaint() {
		this.getElement().setAttribute("forceRepaint", "yes");
	}
	
	/**
	 * Add a bunch of verifiers.
	 * 
	 * @param verifiers  one or more verifiers
	 */
	public void addVerifiers( Verifier... verifiers ) {
		for( Verifier v : verifiers ) {
			addVerifier( v );
		}
	}
	
	/**
	 * Add one verifier.  May be called multiple times to add
	 * more verifiers.
	 * @param verifier A object that checks whether the entry is 
	 * valid and produces validation messages.
	 */
	public void addVerifier( Verifier verifier ) {
		assert verifier != null;
		verifierList.add( verifier );
	    addBlurHandler( verifier );
	}
	
	/**
	 * Validate this entry.  Do this by delegating validation to the
	 * verifiers.
	 */
	@Override 
	public boolean validate() {
		// assume valid
		boolean valid = true;
		
		// Use the verifiers to validate
		for ( Verifier v : verifierList ) {
			valid = v.verify( this );
			// exit on first verification failure.  No point in
			// spaming the user with messages.
			if ( !valid ) {
				break;
			}
		}
		return valid;		
	}

	/**
	 * Checks to see if no value is present.
	 * 
	 * @return true if no value, false if some value was entered.
	 */
	public boolean isEmpty() {
		return getValueAsString().isEmpty();
	}
	
	/**
	 * Returns the value as a trimmed uppercase string.  Any null
	 * value will be returned as an empty string.
	 */
	@Override	
	public String getValueAsString() {
		String val = "";
		if ( this.getValue() != null ) {
			val = this.getValue().toString().trim().toUpperCase();
		}
		return val;
	}

	/**
	 * Do initialization of this form item.  
	 */
	protected void init() {
		this.setAutoFit(true);
		// Format the entered value by trimming spaces and making it uppercase.
		super.setEditorValueFormatter(new FormItemValueFormatter() {
			
            @Override
            public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {
                if (value == null) return null;
                final String text = value.toString().trim().toUpperCase();
                return text;
            }
        });
	}


}
