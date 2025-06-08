package com.honda.galc.client.ui.component;

import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.honda.galc.util.BeanUtils;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>PropertyComboBoxRenderer</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Sep 2, 2008</TD>
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
public class PropertyComboBoxRenderer<T> extends BasicComboBoxRenderer {

	private static final long serialVersionUID = 1L;

	private Class<T> typeClass;
	private Method propertyAccessor;

	public PropertyComboBoxRenderer(Class<T> typeClass, String propertyName) {
		if (typeClass == null) {
			return;
		}

		this.typeClass = typeClass;

		if (propertyName == null) {
			return;
		}

		propertyAccessor = BeanUtils.getPropertyAccessor(typeClass, propertyName);

	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		// setHorizontalAlignment(JLabel.CENTER);
		if (getTypeClass() != null && value != null) {
			if (getTypeClass().isAssignableFrom(value.getClass())) {
				if (getPropertyAccessor() != null) {
					Object propertyValue = BeanUtils.getPropertyValue(value, getPropertyAccessor());
					if (propertyValue != null) {
						value = propertyValue.toString().trim();
					} else {
						value = propertyValue;
					}
				}
			}
		}
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}

	protected Method getPropertyAccessor() {
		return propertyAccessor;
	}

	protected Class<T> getTypeClass() {
		return typeClass;
	}
}
