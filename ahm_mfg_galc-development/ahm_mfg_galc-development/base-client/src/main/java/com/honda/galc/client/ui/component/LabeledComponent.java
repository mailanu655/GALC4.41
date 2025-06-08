package com.honda.galc.client.ui.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.AbstractBorder;

/**
 * 
 * <h3>LabeledComponent Class description</h3>
 * <p> LabeledComponent description </p>
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
 * Apr 21, 2011
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class LabeledComponent<T extends JComponent> extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel label = new JLabel();
	private JScrollPane scrollPane;
	private T component;
	
	public LabeledComponent(String labelName, T component){
	    this(labelName,component,true,false);
	}
	public LabeledComponent(String labelName,T component, boolean isHorizontal) {
	    this(labelName,component,isHorizontal,false);
	}
	
	public LabeledComponent(String labelName, T component,boolean isHorizontal, boolean isScrollable){

		// set horizontal and vertical gap between components as 10
		this.setLayout(new BorderLayout(10,10));
        
        label.setText(labelName);
        label.setName(labelName.toLowerCase()+" Label");
        component.setName(labelName.toLowerCase()+" Field");
        this.component = component;
        
        if(isScrollable){
            scrollPane =  new JScrollPane(component);
            scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        }
        
        if(isHorizontal) {
            this.add(label,BorderLayout.WEST);
            this.add(Box.createHorizontalStrut(10));
            this.add(isScrollable ? scrollPane:component,BorderLayout.CENTER);
        }else{
            this.add(label,BorderLayout.NORTH);
            this.add(Box.createVerticalStrut(10));
            this.add(isScrollable ? scrollPane:component,BorderLayout.CENTER);
        }
        setWidth(100);
	}
	
	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public T getComponent() {
		return component;
	}

	public void setComponent(T component) {
		this.component = component;
	}
	
	public void setFont(Font font) {
		if(label != null) label.setFont(font);
		if(component != null) component.setFont(font);
	}
	
	public void setWidth(int width) {
		component.setSize(width,component.getSize().height);
	}
	
	public void setHeight(int height) {
		component.setSize(component.getSize().width,height);
	}
	
	public void setMaxHight(int height) {
		setMaximumSize(new Dimension(getMaximumSize().width,height));
	}
	
	public void setMaxWidth(int width) {
		setMaximumSize(new Dimension(width,getMaximumSize().height));
	}
	
	public void setPreferredHeight(int height) {
		this.component.setPreferredSize(new Dimension(component.getPreferredSize().width,height));
	}
	
	public void setPreferredWidth(int width) {
		this.component.setPreferredSize(new Dimension(width,component.getPreferredSize().height));
	}
	
	public void setLabelPreferredHeight(int height) {
		this.label.setPreferredSize(new Dimension(label.getPreferredSize().width,height));
	}
	
	public void setLabelPreferredWidth(int width) {
		this.label.setPreferredSize(new Dimension(width,label.getPreferredSize().height));
	}

	@SuppressWarnings("serial")
	public void setInsets(final int top,final int left,final int bottom, final int right) {
		this.setBorder(new AbstractBorder() {
			public Insets getBorderInsets(Component c) { 
		        return new Insets(top, left, bottom, right);
		    }
		});
	}
	
	
	@Override
	public void setEnabled(boolean flag) {
		
		label.setEnabled(flag);
		component.setEnabled(flag);
		
	}
	
}
