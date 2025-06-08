package com.honda.galc.qics.mobile.client;

import com.honda.galc.qics.mobile.client.widgets.SettingEditorPanel;
import com.honda.galc.qics.mobile.client.widgets.form.ProcessPointDbVerifier;
import com.honda.galc.qics.mobile.client.widgets.form.Verifier;


/**
 * The Class ProcessPointEditorPanel.
 */
public class ProcessPointEditorPanel extends SettingEditorPanel{

	/**
	 * Instantiates a new process point editor panel.
	 *
	 * @param settingKey the setting key
	 * @param defaultValue the default value
	 * @param settingTitle the setting title
	 * @param verifier the verifier
	 */
	public ProcessPointEditorPanel(String settingKey, String defaultValue,
			String settingTitle, Verifier verifier) {
		super(settingKey, defaultValue, settingTitle, verifier);
	}

	/**
	 * Instantiates a new process point editor panel.
	 */
	public ProcessPointEditorPanel() {
		super(Settings.PROCESS_POINT_KEY, 
				Settings.PROCESS_POINT_DEFAULT_VALUE, 
				"Process Point",  
				new ProcessPointDbVerifier());
	}



}
