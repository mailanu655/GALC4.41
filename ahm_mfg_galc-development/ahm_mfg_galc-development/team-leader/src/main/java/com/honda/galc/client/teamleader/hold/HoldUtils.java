package com.honda.galc.client.teamleader.hold;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.util.ProductHoldUtil;

/**
 * 
 * 
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HoldUtils</code> is ... .
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
public class HoldUtils extends ProductHoldUtil {

	@SuppressWarnings("unchecked")
	public static List<HoldResult> extractHoldResults(List<Map<String, Object>> list) {

		List<HoldResult> holdResults = new ArrayList<HoldResult>();
		if (list == null || list.isEmpty()) {
			return holdResults;
		}
		for (Object o : list) {
			Map<String, Object> e = (Map<String, Object>) o;

			HoldResult hr = (HoldResult) e.get("holdResult");
			holdResults.add(hr);
		}
		return holdResults;
	}

	public static boolean equals(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		if (str1 == null) {
			return false;
		}
		if (str2 == null) {
			return false;
		}
		return str1.trim().equals(str2.trim());
	}

	public static GridBagConstraints createHorizontalConstraint(double weight, int x) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = weight;
		c.gridx = x;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		return c;
	}

	public static String getCallerInfo() {
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		int ix = 3;
		String str = String.format("%s.%s()", stes[ix].getClassName(), stes[ix].getMethodName());
		return str;
	}
	
	public static File popupSaveDialog(Component parent, String currentDirectoryPath, String fileName) {
		if (currentDirectoryPath == null) {
			currentDirectoryPath = "";
		}
		JFileChooser fc = new JFileChooser(currentDirectoryPath);
		File file = new File(fileName);
		fc.setSelectedFile(file);

		int returnVal = fc.showSaveDialog(parent);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
		} else {
			return null;
		}
		return file;
	}
}
