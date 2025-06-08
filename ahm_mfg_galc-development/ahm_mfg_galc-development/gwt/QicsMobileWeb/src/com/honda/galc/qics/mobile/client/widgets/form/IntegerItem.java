package com.honda.galc.qics.mobile.client.widgets.form;

import com.smartgwt.mobile.client.data.Record;
import com.smartgwt.mobile.client.widgets.form.DynamicForm;
import com.smartgwt.mobile.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;


/**
 * The Class IntegerItem is used to capture an integer value.  It is used within 
 * a DynamicForm.
 *
 * @author vfc01346
 */
public class IntegerItem extends StringItem {

   
	/**
	 * Instantiates a new integer item.
	 *
	 * @param name the name
	 * @param title the title
	 * @param hint the hint
	 */
	public IntegerItem(String name, String title, String hint) {
		super(name, title, hint);
		init();
	}

	/**
	 * Instantiates a new integer item.
	 *
	 * @param name the name
	 * @param title the title
	 */
	public IntegerItem(String name, String title) {
		super(name, title);
		init();
	}

	/**
	 * Instantiates a new integer item.
	 *
	 * @param name the name
	 */
	public IntegerItem(String name) {
		super(name);
		init();
	}
	

	/**
	 * Gets the value as integer.  May return null.
	 *
	 * @return the value as integer, returns null if the value is
	 * not convertible to an integer.
	 */
	public Integer getValueAsInteger() {
		Integer val = null;
		if ( ! this.getValueAsString().isEmpty() ) {
			try {
				val = Integer.parseInt(this.getValueAsString());
			} catch (NumberFormatException e) {
				val = null;
			}
		}
		return val;
	}

	public Long getValueAsLong() {
		Long val = null;
		if ( ! this.getValueAsString().isEmpty() ) {
			try {
				val = Long.parseLong(this.getValueAsString());
			} catch (NumberFormatException e) {
				val = null;
			}
		}
		return val;
	}

	@Override
	protected void init() {

		// Format the entered value by removing all the non-digits.
		// Eliminate leading 0's if there are more than 1 digits
		super.setEditorValueFormatter(new FormItemValueFormatter() {
			
            @Override
            public String formatValue(Object value, Record record, DynamicForm form, FormItem item) {
                if (value == null) return null;
                // get rid of non digits
                String text = value.toString().replaceAll("[^\\d]", "");
                // remove leading 0s
                while( text.length() > 1 && text.startsWith("0")) {
                	text = text.substring(1);
                }
                final String str = text;
                return str;
            }
        });
	}

}
