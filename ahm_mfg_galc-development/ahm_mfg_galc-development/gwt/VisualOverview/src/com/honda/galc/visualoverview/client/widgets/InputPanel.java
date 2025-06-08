package com.honda.galc.visualoverview.client.widgets;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class InputPanel extends HorizontalPanel {

	public InputPanel(String label, Widget input)
	{
		Label panelLabel = new Label(label);
		panelLabel.setWordWrap(false);
		setWidth("100%");
		panelLabel.getElement().getStyle().setFontSize(200, Unit.PCT);
		input.getElement().getStyle().setFontSize(200, Unit.PCT);
		setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		add(panelLabel);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		add(input);
	}
}
