package com.honda.galc.client.ui.component;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * 
 * <h3>LabeledComboBox Class description</h3>
 * <p> LabeledComboBox description </p>
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
 * @author Jeffray Huang<br>
 * May 17, 2010
 *
 *
 */
public class LabeledComboBox extends LabeledComponent<JComboBox> {

	private static final long serialVersionUID = 1L;

	private int textAlignment = JLabel.LEFT;
	
	public LabeledComboBox(String labelName) {
		this(labelName,true);
	}
	
	public LabeledComboBox(String labelName, boolean isHorizontal) {
		super(labelName, new JComboBox(),isHorizontal);
		this.setInsets(10, 10, 10, 10);
	}
	
	public int getTextAlignment() {
		return textAlignment;
	}

	public void setTextAlignment(int textAlignment) {
		this.textAlignment = textAlignment;
	}	
	
	@SuppressWarnings("unchecked")
	public <T> void setModel(List<T> values,int selectionIndex) {
		setModel(values.toArray((T[])new Object[]{}),selectionIndex);
	}
	
	public <T> void setModel(T[] values,int selectionIndex) {
		setModel(new ComboBoxModel<T>(values),selectionIndex);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void setModel(ComboBoxModel<T> model, int selectionIndex) {	
		model.setTextAlignment(textAlignment);
		getComponent().setModel(model);
		getComponent().setRenderer(model);
		getComponent().setSelectedIndex(selectionIndex < model.getSize()  ? selectionIndex : model.getSize() -1);
	}
}