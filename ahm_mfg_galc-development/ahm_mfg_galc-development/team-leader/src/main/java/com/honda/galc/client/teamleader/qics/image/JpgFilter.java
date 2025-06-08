package com.honda.galc.client.teamleader.qics.image;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.filechooser.FileFilter;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>JpgFilter</code> is ...
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
 * <TD>Dec 19, 2008</TD>
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
public class JpgFilter extends FileFilter {

	private static Set<String> EXTENSIONS = new LinkedHashSet<String>();

	static {
		EXTENSIONS.add("jpg");
		EXTENSIONS.add("jpeg");
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = getExtension(f);
		if (extension != null) {
			if (EXTENSIONS.contains(extension)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	public String getDescription() {
		StringBuilder filter = new StringBuilder();
		for (String str : EXTENSIONS) {
			if (filter.length() > 0) {
				filter.append(", ");
			}
			filter.append(str);
		}
		return filter.toString();
	}
}
