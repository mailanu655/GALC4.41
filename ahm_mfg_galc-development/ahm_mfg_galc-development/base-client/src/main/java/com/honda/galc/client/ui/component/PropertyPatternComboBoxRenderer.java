package com.honda.galc.client.ui.component;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.honda.galc.util.BeanUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PropertyPatternComboBoxRenderer</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class PropertyPatternComboBoxRenderer<T> extends BasicComboBoxRenderer {

	private static final long serialVersionUID = 1L;

	private Class<T> typeClass;
	private String pattern;
	private List<Method> propertyAccessors;

	public PropertyPatternComboBoxRenderer(Class<T> typeClass, String pattern, String... propertyNames) {
		this.propertyAccessors = new ArrayList<Method>();
		this.typeClass = typeClass;
		this.pattern = pattern;
		for (String propertyName : propertyNames) {
			propertyAccessors.add(BeanUtils.getPropertyAccessor(typeClass, propertyName));
		}
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		// setHorizontalAlignment(JLabel.CENTER);
		if (getTypeClass() != null && value != null) {
			if (getTypeClass().isAssignableFrom(value.getClass())) {
				if (getPropertyAccessors() != null) {
					Object[] values = new Object[getPropertyAccessors().size()];
					for (int i = 0; i < getPropertyAccessors().size(); i++) {
						values[i] = BeanUtils.getPropertyValue(value, getPropertyAccessors().get(i));
						if (values[i] == null) {
							values[i] = "";
						} else {
							values[i] = values[i].toString().trim();							
						}
					}
					value = String.format(getPattern(), values);
				}
			}
		}
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}

	protected List<Method> getPropertyAccessors() {
		return propertyAccessors;
	}

	protected Class<T> getTypeClass() {
		return typeClass;
	}

	protected String getPattern() {
		return pattern;
	}
}
