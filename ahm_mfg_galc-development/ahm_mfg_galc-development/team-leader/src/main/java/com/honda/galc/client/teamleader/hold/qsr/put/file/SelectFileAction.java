package com.honda.galc.client.teamleader.hold.qsr.put.file;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.util.ExtensionFileFilter;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>SelectFileAction</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Mar 29, 2010</TD>
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
public class SelectFileAction extends BaseListener<ImportFilePanel> implements ActionListener {

	public SelectFileAction(ImportFilePanel parentPanel) {
		super(parentPanel);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		getView().getProductPanel().removeData();
		getView().getInputPanel().getFileNameInput().setText("");

		String path = isEmpty(getInputFolderPath()) ? path = "" : getInputFolderPath().trim();
		JFileChooser fc = new JFileChooser(path);

		FileFilter filter = new ExtensionFileFilter("csv", "txt");
		fc.setFileFilter(filter);

		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showOpenDialog((Component) getView());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			getView().getInputPanel().getFileNameInput().setText(fc.getSelectedFile().getAbsolutePath());
			setInputFolderPath(fc.getSelectedFile().getParentFile().getAbsolutePath());
		} else {
			return;
		}
		String fileName = getView().getInputPanel().getFileNameInput().getText();
		Division division = getView().getDivision();
		boolean uploadEnabled = division != null && !isEmpty(fileName);
		getView().getInputPanel().getImportButton().setEnabled(uploadEnabled);

	}

	public String getInputFolderPath() {
		return (String) getView().getCache().get("import.parentFolder");
	}

	public void setInputFolderPath(String parentFolder) {
		getView().getCache().put("import.parentFolder", parentFolder);
	}
}
