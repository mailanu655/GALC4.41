package com.honda.galc.data;

public enum JasperReportPageOrientation {
	PORTRAIT(0),
	LANDSCAPE(3);
	
	private int postScriptValue = 0;

	private JasperReportPageOrientation(int postScriptValue) {
		this.postScriptValue = postScriptValue;
	}

	public int getPostScriptValue() {
		return postScriptValue;
	}
}
