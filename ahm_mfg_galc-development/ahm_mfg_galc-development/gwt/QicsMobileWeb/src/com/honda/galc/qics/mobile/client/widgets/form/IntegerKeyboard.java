package com.honda.galc.qics.mobile.client.widgets.form;

import com.google.gwt.user.client.Window;
import com.honda.galc.qics.mobile.client.widgets.ButtonFactory;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.Layout;
import com.smartgwt.mobile.client.widgets.layout.VLayout;


/**
 * The Class IntegerKeyboard creates a numeric keypad for
 * entering an Integer.
 */
public class IntegerKeyboard extends Keyboard {

	/**
	 * Instantiates a new integer keyboard.
	 */
	public IntegerKeyboard() {
		super();
	}
	
	/**
	 * Instantiates a new integer keyboard.
	 *
	 * @param item the item
	 */
	public IntegerKeyboard(FormItem item) {
		super(item);
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.qics.mobile.client.widgets.form.Keyboard#buildLayout()
	 */
	@Override
	protected Layout buildLayout() {
		VLayout layout = new VLayout();

        HLayout row1 = new HLayout();
        row1.addMember(buildCharButton("1"));
        row1.addMember(buildCharButton("2"));
        row1.addMember(buildCharButton("3"));
        layout.addMember(row1);

        HLayout row2 = new HLayout();
        row2.addMember(buildCharButton("4"));
        row2.addMember(buildCharButton("5"));
        row2.addMember(buildCharButton("6"));
        layout.addMember(row2);

        HLayout row3 = new HLayout();
        row3.addMember(buildCharButton("7"));
        row3.addMember(buildCharButton("8"));
        row3.addMember(buildCharButton("9"));
        layout.addMember(row3);

        HLayout row4 = new HLayout();
        row4.addMember(buildCharButton("0"));
        row4.addMember(buildDeleteButton());
        row4.addMember(buildClearButton());
        layout.addMember(row4);
		
		return layout;
		
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.qics.mobile.client.widgets.form.Keyboard#buildButton(java.lang.String)
	 */
	@Override
	protected Button buildButton(String title ) {
		Button b = 	ButtonFactory.buildActionButton(title);
		int w = Window.getClientWidth() / 6;
		b.setWidth(w + "px" );
		return b;
	}


}
