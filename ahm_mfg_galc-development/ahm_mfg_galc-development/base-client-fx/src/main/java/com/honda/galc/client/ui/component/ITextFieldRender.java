package com.honda.galc.client.ui.component;


import javafx.scene.control.TextField;

//import javax.swing.JTextField;

/**
 * 
 * <h3>ITextFieldRender</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ITextFieldRender description </p>
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
public interface ITextFieldRender {
	public void renderField(Text text);
	public void renderField(boolean status);
	public void setField(TextField field);
	public void init();
}
