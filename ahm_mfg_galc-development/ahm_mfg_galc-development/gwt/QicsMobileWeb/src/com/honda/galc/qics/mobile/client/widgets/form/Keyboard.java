package com.honda.galc.qics.mobile.client.widgets.form;

import com.honda.galc.qics.mobile.client.widgets.ButtonFactory;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.layout.Layout;

/**
 * The Class Keyboard is the base class for application keyboards.  A keyboard operates
 * on a FormField; typically a text based form field.   The use of keyboard was added
 * to avoid the annoying built in on that is on the ET-1 tablets.
 */
public abstract class Keyboard {

	// FormItem must be set before any button is pressed.
	/** The item. */
	FormItem item;
	
	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public FormItem getItem() {
		return item;
	}

	/**
	 * Sets the item.
	 *
	 * @param item the new item
	 */
	public void setItem(FormItem item) {
		this.item = item;
	}

	/**
	 * Gets the layout.
	 *
	 * @return the layout
	 */
	public Layout getLayout() {
		if ( layout == null ) {
			layout = buildLayout();
		}
		return layout;
	}

	/**
	 * This returns the layout of the keyboard.  
	 * 
	 * @return layout containing all the keys.
	 */
	abstract Layout buildLayout();
	
	/**
	 * Sets the layout.
	 *
	 * @param layout the new layout
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	/** The layout. */
	Layout layout = null;
	
	/**
	 * Instantiates a new keyboard.
	 *
	 * @param item the item
	 */
	public Keyboard( FormItem item ) {
		this.item = item;
	}
	
	/**
	 * Instantiates a new keyboard.
	 */
	public Keyboard() {
	}

	
	/**
	 * Builds the char button.  This button will addend
	 * the char to the FormItem's value.
	 *
	 * @param ch the a string to be appended to the input
	 * @return the button
	 */
	protected Button buildCharButton( final String ch ) {
		Button b = buildButton(ch);
		b.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				String s = getEntry();
				setEntry(s + ch );				
			}});
		return b;
	}

	/**
	 * Builds the space button.  Pressing it appends a space to
	 * the FormItems value.
	 *
	 * @return the button
	 */
	protected Button buildSpaceButton( ) {
		Button b = buildButton("SP");
		b.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				String s = getEntry();
				setEntry(s + " " );
				
			}});
		return b;
	}

	
	/**
	 * Builds the clear button which sets the FormItem's value to "".
	 *
	 * @return the button
	 */
	protected Button buildClearButton( ) {
		Button b = buildButton("CLR");
		b.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				setEntry("" );

			}});
		return b;
	}
	
	/**
	 * Builds the delete button.  Pressing it removes the
	 * last character of the FormItem's value
	 *
	 * @return the button
	 */
	protected Button buildDeleteButton(  ) {
		Button b = buildButton("DEL");
		b.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				String s = getEntry();
				if( s.length() > 0 ) {
					s = s.substring(0,s.length()-1);
				}
				setEntry(s );
				
			}});
		return b;
	}
    
	/**
	 * Builds the button.
	 *
	 * @param title the title
	 * @return the button
	 */
	protected Button buildButton(String title ) {
		Button b = ButtonFactory.buildActionButton( title );
		return b;
	}
	
	/**
	 * Gets the entry.
	 *
	 * @return the entry
	 */
	protected String getEntry() {
		if ( item == null ) {
			throw new IllegalStateException("Keyboard in not bound to a FormItem");
		}
		Object obj = item.getValue();
		if ( item.getValue() == null ) {
			return "";
		} else {
			return (String ) obj;
		}
		
	}
	
	/**
	 * Sets the entry.
	 *
	 * @param s the new entry
	 */
	protected void setEntry(String s ) {
		if ( item == null ) {
			throw new IllegalStateException("Keyboard in not bound to a FormItem");
		}
		item.setValue(s);
		
	}
}
