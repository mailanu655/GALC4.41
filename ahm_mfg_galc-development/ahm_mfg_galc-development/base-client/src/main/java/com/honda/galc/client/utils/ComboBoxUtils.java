package com.honda.galc.client.utils;

import java.util.List;

import javax.swing.JComboBox;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Couple useful operations on <code>JComboBox</code>.
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
public class ComboBoxUtils {

	public static <T> void loadComboBox(JComboBox comboBox, List<T> items) {
		if (comboBox == null || items == null) {
			return;
		}
		comboBox.removeAllItems();
		for (T item : items) {
			comboBox.addItem(item);
		}
	}

	public static void addItem(JComboBox comboBox, String item) {
		if (comboBox == null || item == null) {
			return;
		}
		comboBox.addItem(item);
	}

	public static void setSelectedItem(JComboBox comboBox, String item) {
		if (comboBox == null || item == null) {
			return;
		}
		comboBox.setSelectedItem(item);
	}
}
