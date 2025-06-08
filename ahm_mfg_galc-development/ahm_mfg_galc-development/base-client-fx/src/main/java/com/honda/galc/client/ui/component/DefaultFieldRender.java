package com.honda.galc.client.ui.component;

import com.honda.galc.common.logging.Logger;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
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
	public final static Color VIEW_COLOR_INPUT = Color.WHITE;
	public final static Color VIEW_COLOR_OUTPUT = new Color(0.8,0.8,0.8, 0.5);
	public final static Color VIEW_COLOR_CURRENT = Color.BLUE;
	public final static Color VIEW_COLOR_OK = Color.GREEN;
	public final static Color VIEW_COLOR_NG = Color.RED;
	public final static Color VIEW_COLOR_FONT = Color.BLACK;
	public final static Color VIEW_COLOR_PROMPT = new Color(0.8, 0.8, 1.0, 0.5);
	public final static Color VIEW_COLOR_SKIP = new Color(0.8, 0.8, 1.0,0.5);

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
		this.field.setColor(VIEW_COLOR_INPUT);
		this.field.setStyle("-fx-text-fill:white; -fx-background-color:#FF0000; -fx-font: 20 arial; -fx-font-weight: bold;");
		this.field.setEditable(true);
		this.field.requestFocus();
		this.field.selectAll();
	}

	private void ok() {
		this.field.setColor(VIEW_COLOR_INPUT);
		this.field.setStyle("-fx-text-fill:black; -fx-background-color:#00FF00; -fx-font: 20 arial; -fx-font-weight: bold;");
		this.field.setEditable(false);
		this.field.requestFocus();
	}

	public void init() {
		this.field.setColor(VIEW_COLOR_INPUT);
		this.field.setStyle("-fx-text-fill:white; -fx-background-color:#0000FF;  -fx-font: 20 arial; -fx-font-weight: bold;");
		this.field.setEditable(true);
		this.field.setVisible(true);
		this.field.requestFocus();
	}

	public void setField(TextField field) {
		if(field instanceof LengthFieldBean)
			this.field = (LengthFieldBean)field;
		else 
			Logger.getLogger().error("Field type error: DefaultFieldRender for LengthFieldBean only."); 
		
	}

	
}
