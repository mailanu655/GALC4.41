package com.honda.galc.util;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.filechooser.FileFilter;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ExtensionFileFilter</code> is ... .
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
public class ExtensionFileFilter extends FileFilter {

	private Set<String> extensions = new LinkedHashSet<String>();

	public ExtensionFileFilter(String... extensions) {
		if (extensions != null) {
			getExtensions().addAll(Arrays.asList(extensions));
		}
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = getExtension(f);
		if (extension != null) {
			if (getExtensions().contains(extension)) {
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
		for (String str : getExtensions()) {
			if (filter.length() > 0) {
				filter.append(", ");
			}
			filter.append(str);
		}
		return filter.toString();
	}

	public Set<String> getExtensions() {
		return extensions;
	}
}
