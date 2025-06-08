package com.honda.galc.client.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.util.StringUtil;
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

	/**
	 * Adds the child to the parent with the given configuration.
	 * @param parent - component to which child is added
	 * @param child - component which is added to parent
	 * @param configuration - csv configuration for GridBagConstraints
	 */
	public static void setGridBagConstraints(Container parent, Component child, String configuration) {
		Integer gridx = null;
		Integer gridy = null;
		Integer gridwidth = null;
		Integer gridheight = null;
		Integer fill = null;
		Integer ipadx = null;
		Integer ipady = null;
		Insets insets = null;
		Integer anchor = null;
		Double weightx = null;
		Double weighty = null;

		if (!"DEFAULT".equals(configuration)) {
			String[] configs = configuration.split(Delimiter.COMMA);
			for (String config : configs) {
				String key;
				String value;
				{
					String[] info = config.split(Delimiter.COLON);
					if (info.length != 2) continue;
					key = info[0];
					value = info[1];
				}
				if ("gridx".equals(key)) {
					if ("RELATIVE".equals(value)) gridx = GridBagConstraints.RELATIVE;
					else gridx = StringUtil.toInteger(value);
				}
				else if ("gridy".equals(key)) {
					if ("RELATIVE".equals(value)) gridy = GridBagConstraints.RELATIVE;
					else gridy = StringUtil.toInteger(value);
				}
				else if ("gridwidth".equals(key)) {
					if ("RELATIVE".equals(value)) gridwidth = GridBagConstraints.RELATIVE;
					else if ("REMAINDER".equals(value)) gridwidth = GridBagConstraints.REMAINDER;
					else gridwidth = StringUtil.toInteger(value);
				}
				else if ("gridheight".equals(key)) {
					if ("RELATIVE".equals(value)) gridheight = GridBagConstraints.RELATIVE;
					else if ("REMAINDER".equals(value)) gridheight = GridBagConstraints.REMAINDER;
					else gridheight = StringUtil.toInteger(value);
				}
				else if ("fill".equals(key)) {
					if ("NONE".equals(value)) fill = GridBagConstraints.NONE;
					else if ("HORIZONTAL".equals(value)) fill = GridBagConstraints.HORIZONTAL;
					else if ("VERTICAL".equals(value)) fill = GridBagConstraints.VERTICAL;
					else if ("BOTH".equals(value)) fill = GridBagConstraints.BOTH;
				}
				else if ("ipadx".equals(key)) {
					ipadx = StringUtil.toInteger(value);
				}
				else if ("ipady".equals(key)) {
					ipady = StringUtil.toInteger(value);
				}
				else if ("insets".equals(key)) { // Example of insets format - insets:top=12 left=34 bottom=56 right=78
					int top = 0;
					int left = 0;
					int bottom = 0;
					int right = 0;
					String[] insetConfigs = value.split(Delimiter.SPACE);
					for (String insetConfig : insetConfigs) {
						String insetKey;
						Integer insetValue;
						{
							String[] info = insetConfig.split(Delimiter.EQUALS_SIGN);
							if (info.length != 2) continue;
							insetValue = StringUtil.toInteger(info[1]);
							if (insetValue == null) continue;
							insetKey = info[0];
						}
						if ("top".equals(insetKey)) top = insetValue;
						else if ("left".equals(insetKey)) left = insetValue;
						else if ("bottom".equals(insetKey)) bottom = insetValue;
						else if ("right".equals(insetKey)) right = insetValue;
					}
					insets = new Insets(top,left,bottom,right);
				}
				else if ("anchor".equals(key)) {
					if ("CENTER".equals(value)) anchor = GridBagConstraints.CENTER;
					else if ("NORTH".equals(value)) anchor = GridBagConstraints.NORTH;
					else if ("NORTHEAST".equals(value)) anchor = GridBagConstraints.NORTHEAST;
					else if ("EAST".equals(value)) anchor = GridBagConstraints.EAST;
					else if ("SOUTHEAST".equals(value)) anchor = GridBagConstraints.SOUTHEAST;
					else if ("SOUTH".equals(value)) anchor = GridBagConstraints.SOUTH;
					else if ("SOUTHWEST".equals(value)) anchor = GridBagConstraints.SOUTHWEST;
					else if ("WEST".equals(value)) anchor = GridBagConstraints.WEST;
					else if ("NORTHWEST".equals(value)) anchor = GridBagConstraints.NORTHWEST;
					else if ("PAGE_START".equals(value)) anchor = GridBagConstraints.PAGE_START;
					else if ("PAGE_END".equals(value)) anchor = GridBagConstraints.PAGE_END;
					else if ("LINE_START".equals(value)) anchor = GridBagConstraints.LINE_START;
					else if ("LINE_END".equals(value)) anchor = GridBagConstraints.LINE_END;
					else if ("FIRST_LINE_START".equals(value)) anchor = GridBagConstraints.FIRST_LINE_START;
					else if ("FIRST_LINE_END".equals(value)) anchor = GridBagConstraints.FIRST_LINE_END;
					else if ("LAST_LINE_START".equals(value)) anchor = GridBagConstraints.LAST_LINE_START;
					else if ("LAST_LINE_END".equals(value)) anchor = GridBagConstraints.LAST_LINE_END;
				}
				else if ("weightx".equals(key)) {
					weightx = StringUtil.toDouble(value);
				}
				else if ("weighty".equals(key)) {
					weighty = StringUtil.toDouble(value);
				}
			}
		}

		setGridBagConstraints(parent, child, gridx,gridy,gridwidth,gridheight,fill,ipadx,ipady,insets,anchor,weightx,weighty);
	}

	public static void setGridBagConstraints(Container parent, Component child, Integer gridx, Integer gridy, Integer gridwidth, Integer gridheight, Integer fill, Integer ipadx, Integer ipady, Insets insets, Integer anchor, Double weightx, Double weighty) {
		GridBagConstraints constraints = new GridBagConstraints();
		if (gridx != null) constraints.gridx = gridx;
		if (gridy != null) constraints.gridy = gridy;
		if (gridwidth != null) constraints.gridwidth = gridwidth;
		if (gridheight != null) constraints.gridheight = gridheight;
		if (fill != null) constraints.fill = fill;
		if (ipadx != null) constraints.ipadx = ipadx;
		if (ipady != null) constraints.ipady = ipady;
		if (insets != null) constraints.insets = insets;
		if (anchor != null) constraints.anchor = anchor;
		if (weightx != null) constraints.weightx = weightx;
		if (weighty != null) constraints.weighty = weighty;
		parent.add(child, constraints);
	}

}
