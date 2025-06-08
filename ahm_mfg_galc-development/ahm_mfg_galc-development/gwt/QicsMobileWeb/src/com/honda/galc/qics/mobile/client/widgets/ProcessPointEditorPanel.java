package com.honda.galc.qics.mobile.client.widgets;

import com.honda.galc.qics.mobile.client.Settings;
import com.honda.galc.qics.mobile.client.widgets.form.ProcessPointDbVerifier;
import com.honda.galc.qics.mobile.client.widgets.form.Verifier;

public class ProcessPointEditorPanel extends SettingEditorPanel{

	public ProcessPointEditorPanel(String settingKey, String defaultValue,
			String settingTitle, Verifier verifier) {
		super(settingKey, defaultValue, settingTitle, verifier);
	}

	public ProcessPointEditorPanel() {
		super(Settings.PROCESS_POINT_KEY, 
				Settings.PROCESS_POINT_DEFAULT_VALUE, 
				"Process Point",  
				new ProcessPointDbVerifier());
	}



}
