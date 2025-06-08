package com.honda.galc.qics.mobile.client.widgets.form;

import com.google.gwt.user.client.Window;
import com.honda.galc.qics.mobile.client.widgets.ButtonFactory;
import com.smartgwt.mobile.client.widgets.Button;
import com.smartgwt.mobile.client.widgets.events.ClickEvent;
import com.smartgwt.mobile.client.widgets.events.ClickHandler;
import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.Layout;
import com.smartgwt.mobile.client.widgets.layout.VLayout;

/**
 * The Class AlphaKeyboard builds an alpha-numeric keyboard
 * attached to a form item.
 */
public class AlphaKeyboard extends Keyboard {
	
	boolean showAlpha = true;
	Layout alphaLayout = null;
	Layout numericLayout = null;

	public AlphaKeyboard() {
		super();
	}
	
	public AlphaKeyboard(FormItem item) {
		super(item);
	}

	@Override
	protected Layout buildLayout() {
		VLayout layout = new VLayout();
		if ( isScreenOrientationLandscape() ) {
			alphaLayout = buildWideAlphaLayout();
			numericLayout = buildWideNumericSymbolLayout();
		} else {
			alphaLayout = buildNarrowAlphaLayout();
			numericLayout = buildNarrowNumericSymbolLayout();
		}
		layout.addMember(alphaLayout);
		layout.addMember(numericLayout);
		chooseKeyboard();
		return layout;
		
	}
	
	/**
	 * Pick which keyboard should be visible
	 */
	protected void chooseKeyboard() {
		alphaLayout.setVisible(showAlpha);
		numericLayout.setVisible(!showAlpha);
	}
	
	protected Layout buildWideAlphaLayout() {
		VLayout layout = new VLayout();

        HLayout row1 = new HLayout();
        row1.addMember(buildCharButton("A"));
        row1.addMember(buildCharButton("B"));
        row1.addMember(buildCharButton("C"));
        row1.addMember(buildCharButton("D"));
        row1.addMember(buildCharButton("E"));
        row1.addMember(buildCharButton("F"));
        row1.addMember(buildCharButton("G"));
        row1.addMember(buildCharButton("H"));
        layout.addMember(row1);

        HLayout row2 = new HLayout();
        row2.addMember(buildCharButton("I"));
        row2.addMember(buildCharButton("J"));
        row2.addMember(buildCharButton("K"));
        row2.addMember(buildCharButton("L"));
        row2.addMember(buildCharButton("M"));
        row2.addMember(buildCharButton("N"));
        row2.addMember(buildCharButton("O"));
        row2.addMember(buildCharButton("P"));
        layout.addMember(row2);

        HLayout row3 = new HLayout();
        row3.addMember(buildCharButton("Q"));
        row3.addMember(buildCharButton("R"));
        row3.addMember(buildCharButton("S"));
        row3.addMember(buildCharButton("T"));
        row3.addMember(buildCharButton("U"));
        row3.addMember(buildCharButton("V"));
        row3.addMember(buildCharButton("W"));
        row3.addMember(buildCharButton("X"));
        layout.addMember(row3);

        HLayout row4 = new HLayout();
        row4.addMember(buildCharButton("Y"));
        row4.addMember(buildCharButton("Z"));
        row4.addMember(buildCharButton("."));
        row4.addMember(buildCharButton(","));
        row4.addMember(buildSpaceButton());
        row4.addMember(buildDeleteButton());
        row4.addMember(buildClearButton());
        row4.addMember(buildToggleKeyboard("@123"));
        
        layout.addMember(row4);
		
		return layout;
		
	}

	protected Layout buildNarrowAlphaLayout() {
		VLayout layout = new VLayout();

        HLayout row1 = new HLayout();
        row1.addMember(buildCharButton("A"));
        row1.addMember(buildCharButton("B"));
        row1.addMember(buildCharButton("C"));
        row1.addMember(buildCharButton("D"));
        row1.addMember(buildCharButton("E"));
        layout.addMember(row1);
        
        HLayout row2 = new HLayout();
        row2.addMember(buildCharButton("F"));
        row2.addMember(buildCharButton("G"));
        row2.addMember(buildCharButton("H"));
        row2.addMember(buildCharButton("I"));
        row2.addMember(buildCharButton("J"));
        layout.addMember(row2);
        
        HLayout row3 = new HLayout();
        row3.addMember(buildCharButton("K"));
        row3.addMember(buildCharButton("L"));
        row3.addMember(buildCharButton("M"));
        row3.addMember(buildCharButton("N"));
        row3.addMember(buildCharButton("O"));
        layout.addMember(row3);
        
        HLayout row4 = new HLayout();
        row4.addMember(buildCharButton("P"));
        row4.addMember(buildCharButton("Q"));
        row4.addMember(buildCharButton("R"));
        row4.addMember(buildCharButton("S"));
        row4.addMember(buildCharButton("T"));
        layout.addMember(row4);
        
        HLayout row5 = new HLayout();
        row5.addMember(buildCharButton("U"));
        row5.addMember(buildCharButton("V"));
        row5.addMember(buildCharButton("W"));
        row5.addMember(buildCharButton("X"));
        row5.addMember(buildCharButton("Y"));
        layout.addMember(row5);
      
        HLayout row6 = new HLayout();
        row6.addMember(buildCharButton("Z"));
        row6.addMember(buildCharButton("."));
        row6.addMember(buildCharButton(","));
        row6.addMember(buildSpaceButton());
        row6.addMember(buildDeleteButton());
        layout.addMember(row6);
        
        HLayout row7 = new HLayout();
        row7.addMember(buildClearButton());
        row7.addMember(buildToggleKeyboard("@123"));
        layout.addMember(row7);
        
		
		return layout;
		
	}

	
	protected Layout buildWideNumericSymbolLayout() {
		VLayout layout = new VLayout();

        HLayout row1 = new HLayout();
        row1.addMember(buildCharButton("0"));
        row1.addMember(buildCharButton("1"));
        row1.addMember(buildCharButton("2"));
        row1.addMember(buildCharButton("3"));
        row1.addMember(buildCharButton("4"));
        layout.addMember(row1);

        HLayout row2 = new HLayout();
        row2.addMember(buildCharButton("5"));
        row2.addMember(buildCharButton("6"));
        row2.addMember(buildCharButton("7"));
        row2.addMember(buildCharButton("8"));
        row2.addMember(buildCharButton("9"));
        layout.addMember(row2);

        HLayout row3 = new HLayout();
        row3.addMember(buildCharButton("."));
        row3.addMember(buildCharButton(","));
        row3.addMember(buildCharButton("+"));
        row3.addMember(buildCharButton("-"));
        row3.addMember(buildCharButton("#"));
        row3.addMember(buildCharButton("@"));
        row3.addMember(buildCharButton("("));
        row3.addMember(buildCharButton(")"));
        layout.addMember(row3);

        HLayout row4 = new HLayout();
        row4.addMember(buildCharButton("/"));
        row4.addMember(buildCharButton("*"));
        row4.addMember(buildCharButton("|"));
        row4.addMember(buildCharButton("="));
        row4.addMember(buildSpaceButton());
        row4.addMember(buildDeleteButton());
        row4.addMember(buildClearButton());
        row4.addMember(buildToggleKeyboard("ABC"));
       layout.addMember(row4);
		
		return layout;
		
	}
	
	protected Layout buildNarrowNumericSymbolLayout() {
		VLayout layout = new VLayout();

        HLayout row1 = new HLayout();
        row1.addMember(buildCharButton("0"));
        row1.addMember(buildCharButton("1"));
        row1.addMember(buildCharButton("2"));
        row1.addMember(buildCharButton("3"));
        row1.addMember(buildCharButton("4"));
        layout.addMember(row1);

        HLayout row2 = new HLayout();
        row2.addMember(buildCharButton("5"));
        row2.addMember(buildCharButton("6"));
        row2.addMember(buildCharButton("7"));
        row2.addMember(buildCharButton("8"));
        row2.addMember(buildCharButton("9"));
        layout.addMember(row2);

        HLayout row3 = new HLayout();
        row3.addMember(buildCharButton("."));
        row3.addMember(buildCharButton(","));
        row3.addMember(buildCharButton("+"));
        row3.addMember(buildCharButton("-"));
        row3.addMember(buildCharButton("#"));
        layout.addMember(row3);
        
        HLayout row4 = new HLayout();
        row4.addMember(buildCharButton("@"));
        row4.addMember(buildCharButton("("));
        row4.addMember(buildCharButton(")"));
        row4.addMember(buildCharButton("/"));
        row4.addMember(buildCharButton("*"));
        layout.addMember(row4);
        
        HLayout row5 = new HLayout();
        row5.addMember(buildCharButton("|"));
        row5.addMember(buildCharButton("="));
        row5.addMember(buildSpaceButton());
        row5.addMember(buildDeleteButton());
        row5.addMember(buildClearButton());
        layout.addMember(row5);
        
        HLayout row6 = new HLayout();
        row6.addMember(buildToggleKeyboard("ABC"));
        layout.addMember(row6);
		
		return layout;
	}
	
	protected Button buildToggleKeyboard( String title ) {
		Button b = buildButton( title );
		b.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				showAlpha = !showAlpha;
				chooseKeyboard();
			}});
		return b;
	}		
	
	protected boolean isScreenOrientationLandscape() {
		return Window.getClientWidth() > Window.getClientHeight();
	}
	

	@Override
	protected Button buildButton(String title ) {
		Button b = 	ButtonFactory.buildActionButton(title);
		int w = Window.getClientWidth() / 12;
		b.setWidth(w + "px" );
		return b;
	}

}
