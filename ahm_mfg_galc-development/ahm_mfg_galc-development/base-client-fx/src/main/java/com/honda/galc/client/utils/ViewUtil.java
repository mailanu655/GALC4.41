package com.honda.galc.client.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
/**
 * 
 * <h3>ViewUtil Class description</h3>
 * <p> ViewUtil description </p>
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
 * Jan 12, 2011
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
public class ViewUtil {

	@SuppressWarnings("serial")
	public static void setInsets(JComponent component,final int top,final int left,final int bottom, final int right) {
		component.setBorder(new AbstractBorder() {
			public Insets getBorderInsets(Component c) { 
		        return new Insets(top, left, bottom, right);
		    }
		});
	}
	
	public static void setPreferredWidth(JComponent component, int width) {
		
		component.setPreferredSize(new Dimension(width,component.getPreferredSize().height));
		
	}

	public static void setPreferredHeight(JComponent component, int height) {
		
		component.setPreferredSize(new Dimension(component.getPreferredSize().width,height));
		
	}
	
	public static void setMaxWidth(JComponent component, int width) {
		
		component.setMaximumSize(new Dimension(width,component.getMaximumSize().height));
		
	}

	public static void setMaxHeight(JComponent component, int height) {
		
		component.setMaximumSize(new Dimension(component.getMaximumSize().width,height));
		
	}
	public static JComponent wrap(JComponent comp) {
		JPanel panel = new JPanel();
		panel.add(comp);
		return panel;
	}
	
	public static void setFont(Font font,JComponent... components){
		for(JComponent component : components) {
			component.setFont(font);
		}
	}
	
	public static void setPreferredSize(int x, int y,JComponent... components){
		for(JComponent component : components) {
			component.setPreferredSize(new Dimension(x,y));
		}
	}
	
	public static void setPreferredWidth(int width,JComponent... components) {
		for(JComponent component : components) {
			setPreferredWidth(component, width);
		}
	}
		
	public static void setPreferredHeight(int height,JComponent... components) {
		for(JComponent component : components) {
			setPreferredHeight(component, height);
		}
	}
}
