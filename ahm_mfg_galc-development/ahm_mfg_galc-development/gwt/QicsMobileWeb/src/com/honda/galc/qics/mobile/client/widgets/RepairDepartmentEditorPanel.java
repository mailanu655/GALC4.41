package com.honda.galc.qics.mobile.client.widgets;

import com.honda.galc.qics.mobile.client.Settings;
import com.honda.galc.qics.mobile.client.widgets.form.RepairDepartmentVerifier;
import com.honda.galc.qics.mobile.client.widgets.form.Verifier;

public class RepairDepartmentEditorPanel extends SettingEditorPanel{

	public RepairDepartmentEditorPanel(String settingKey, String defaultValue,
			String settingTitle, Verifier verifier) {
		super(settingKey, defaultValue, settingTitle, verifier);
	}

	public RepairDepartmentEditorPanel() {
		super(Settings.REPAIR_DEPARTMENT_KEY, 
				Settings.REPAIR_DEPARTMENT_DEFAULT_VALUE, 
				"Repair Department",  
				new RepairDepartmentVerifier());
	}



}