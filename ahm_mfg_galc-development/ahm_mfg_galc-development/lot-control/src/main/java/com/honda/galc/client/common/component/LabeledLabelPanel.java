package com.honda.galc.client.common.component;
/**
 * 
 * <h3>LabeledLabelPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LabeledLabelPanel is a GUI component that can be used to display a field and its value as labels. </p>
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
 * <TR>
 * <TD>Meghana G</TD>
 * <TD>Mar 8, 2011</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD>Modified the code to change the color of the value label while setting the new value.</TD>
 * </TR>  
 * </TABLE>
 *   
 * @author Meghana Ghanekar
 * Feb 3, 2011
 *
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabeledLabelPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JLabel nameLabel;
	private JLabel valueLabel;
	private String namelabelName;
	private String valuelabelName;
	private Color originalcolor = null;

	
	
	public LabeledLabelPanel(String namelabelName,String valuelabelName) {
		super();
		this.namelabelName = namelabelName;
		this.valuelabelName = valuelabelName;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout(5, 10));
		setPreferredSize(new Dimension(400, 25));
		createComponents();
		
	}

	public void createComponents() {
		add(getNameLabel(), BorderLayout.WEST);
		add(getValueLabel(), BorderLayout.CENTER);
	}

	public JLabel getNameLabel() {
		if(nameLabel == null){
			nameLabel = new JLabel();
			nameLabel.setPreferredSize(new Dimension(120, 25));
			nameLabel.setText(namelabelName);
    	}
		return nameLabel;
	}
	public JLabel getValueLabel() {
		if(valueLabel == null){
			valueLabel = new JLabel();
			valueLabel.setPreferredSize(new Dimension(120, 25));
			valueLabel.setText(valuelabelName);
			originalcolor = valueLabel.getBackground();
		}
		return valueLabel;
	}
	
	public void setValue(String value,Color color){
		getValueLabel().setText(value);
		Font f = getValueLabel().getFont();
		Font f1 = new Font(f.getName(),Font.BOLD,f.getSize());
		getValueLabel().setFont(f1);
		if(color!=null){
			getValueLabel().setOpaque(true);
			getValueLabel().setBackground(color);
		}else{
			getValueLabel().setOpaque(true);
			getValueLabel().setBackground(originalcolor);
		}
	}
}
