package com.honda.galc.client.ui.component;


/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Apr 22, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140422</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class TextInputRender extends DefaultFieldRender {

	public void inputMode() {
		this.field.setColor(VIEW_COLOR_CURRENT);
		this.field.setBackground(VIEW_COLOR_CURRENT);
		this.field.setDisabledTextColor(VIEW_COLOR_FONT);
		this.field.setForeground(VIEW_COLOR_FONT);
		this.field.setEditable(true);
		this.field.setEnabled(true);
	}
	
	public void errorCorrectionMode() {
		this.field.setDisabledTextColor(VIEW_COLOR_FONT);
		this.field.setForeground(VIEW_COLOR_FONT);
		this.field.setEditable(true);
		this.field.setEnabled(true);
		this.field.setColor(VIEW_COLOR_NG);
		this.field.setBackground(VIEW_COLOR_NG);
		this.field.selectAll();
		this.field.requestFocus();
	}
	
	public void disableMode() {
		this.field.setColor(VIEW_COLOR_INPUT);
		this.field.setBackground(VIEW_COLOR_INPUT);
		this.field.setDisabledTextColor(VIEW_COLOR_FONT);
		this.field.setForeground(VIEW_COLOR_FONT);
		this.field.setEditable(false);
		this.field.setEnabled(false);
	}
	
	public void clear() {
		this.field.setText("");
	}
}
