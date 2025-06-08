package com.honda.galc.client.ui.component;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * 
 * <h3>LabeledNumberSpinner Class description</h3>
 * <p> LabeledNumberSpinner description </p>
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
 * Apr 20, 2011
 *
 *
 */
public class LabeledNumberSpinner extends LabeledComponent<JSpinner> {

	private static final long serialVersionUID = 1L;

	
	public LabeledNumberSpinner(String labelName) {
		this(labelName,true);
	}
	
	public LabeledNumberSpinner(String labelName, boolean isHorizontal) {
		super(labelName, new JSpinner(new SpinnerNumberModel()),isHorizontal);
		this.setInsets(10, 10, 10, 10);
	}
	
	public LabeledNumberSpinner(String labelName, boolean isHorizontal,int min,int max) {
		super(labelName, new JSpinner(new SpinnerNumberModel(min,min,max,1)),isHorizontal);
		this.setInsets(10, 10, 10, 10);
	}
	
	public void setRange(int min,int max) {
		
		setRange(min,min,max);
		
	}
	
	public void setRange(int value, int min, int max) {
		
		setRange(value,min,max, 1);
		
	}
	
	public void setRange(int value, int min, int max, int stepSize) {
		
		getModel().setValue(value);
		getModel().setMinimum(min);
		getModel().setMaximum(max);
		getModel().setStepSize(stepSize);
		
	}
	
	public SpinnerNumberModel getModel() {
		
		return (SpinnerNumberModel)getComponent().getModel();
		
	}
	
	public int getValue() {
		
		return (Integer)getComponent().getValue();
		
	}
}