package com.honda.galc.client.ui.component;

import java.awt.Color;

import javax.swing.JTextField;

import com.honda.galc.common.logging.Logger;
/**
 * 
 * <h3>DefaultFieldRender</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DefaultFieldRender description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Aug 18, 2010
 *
 */
public class DefaultFieldRender implements ITextFieldRender{
	LengthFieldBean field;
	public final static Color VIEW_COLOR_INPUT = Color.white;
	public final static Color VIEW_COLOR_OUTPUT = new Color(204,204,204);
	public final static Color VIEW_COLOR_CURRENT = Color.blue;
	public final static Color VIEW_COLOR_OK = Color.green;
	public final static Color VIEW_COLOR_NG = Color.red;
	public final static Color VIEW_COLOR_FONT = Color.black;
	public final static Color VIEW_COLOR_PROMPT = new Color(204, 204, 255);
	public final static Color VIEW_COLOR_SKIP = new Color(204, 204, 255);

	public void renderField(Text text) {
		if(text.getValue() != null && text.getValue().equals("")){
			init();
		} else if(text.isStatus()){
			ok();
		} else if(!text.isStatus()){
			ng();
		}
	}
	
	public void renderField(boolean status) {
		if(status) 
			ok();
		else
			ng();
	}

	private void ng() {
		this.field.setColor(VIEW_COLOR_NG);
		this.field.setBackground(VIEW_COLOR_NG);
		this.field.setSelectionStart(0);
		this.field.setSelectionEnd(this.field.getText().length());
		this.field.setEnabled(true);
		this.field.requestFocus();
	}

	private void ok() {
		this.field.setColor(VIEW_COLOR_OK);
		this.field.setBackground(VIEW_COLOR_OK);
		this.field.setDisabledTextColor(VIEW_COLOR_FONT);
		this.field.setForeground(VIEW_COLOR_FONT);
		this.field.setEditable(false);
		this.field.setEnabled(false);
	}

	public void init() {
		this.field.setColor(VIEW_COLOR_CURRENT);
		this.field.setBackground(VIEW_COLOR_CURRENT);
		this.field.setDisabledTextColor(VIEW_COLOR_FONT);
		this.field.setForeground(VIEW_COLOR_INPUT);
		this.field.setSelectionColor(VIEW_COLOR_PROMPT);
		this.field.setEnabled(true);
		this.field.setEditable(true);
		this.field.setVisible(true);
		this.field.requestFocus();
	}

	public void setField(JTextField field) {
		if(field instanceof LengthFieldBean)
			this.field = (LengthFieldBean)field;
		else 
			Logger.getLogger().error("Field type error: DefaultFieldRender for LengthFieldBean only."); 
		
	}

	
}
