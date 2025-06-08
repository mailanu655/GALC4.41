package com.honda.galc.qics.mobile.client.widgets.form;

import com.smartgwt.mobile.client.widgets.form.fields.FormItem;
import com.smartgwt.mobile.client.widgets.layout.HLayout;
import com.smartgwt.mobile.client.widgets.layout.Layout;
import com.smartgwt.mobile.client.widgets.layout.VLayout;

public class ClearKeyboard extends Keyboard {

	public ClearKeyboard(){
		super();
	}
	
	public ClearKeyboard(FormItem item) {
		super(item);
	}

	@Override
	protected Layout buildLayout() {
		VLayout layout = new VLayout();

        HLayout row1 = new HLayout();
        row1.addMember(buildClearButton());
        layout.addMember(row1);
		
		return layout;
		
	}

}
