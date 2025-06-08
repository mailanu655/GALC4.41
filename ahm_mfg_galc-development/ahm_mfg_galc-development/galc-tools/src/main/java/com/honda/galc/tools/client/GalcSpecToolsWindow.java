package com.honda.galc.tools.client;

import java.io.File;


/**
 * 
 * @author vec15809
 * Oct. 11 - 2017
 *
 */
public class GalcSpecToolsWindow extends ClientToolsWindow {
	private static final long serialVersionUID = 1L;
	protected SpecsUtil specsUtil;
	
	public void openToolsWindow(String[] args) {
		headers = new Object[] {"Name", "Description", "URL"};
		servers = new Object[][]{{"GALC Spec Transfer Tool", "Change IP : Port in URL column", "http://hostname:port/BaseWeb/HttpServiceHandler"}};
		specsUtil = new SpecsUtil();
		super.openToolsWindow(args);
	}
	
	protected void setTitle() {
		this.setTitle("GALC Spec Transfer Tool Ver." + GalcSpecConfiguration.CURRENT_VERSION);
	}
	
	protected void enabledButtonsAfterLoadProcessPoints() {
		super.enabledButtonsAfterLoadProcessPoints();
		cookbookButton.setEnabled(false);
	}

	public static void main(String[] args) {
		GalcSpecToolsWindow tools = new GalcSpecToolsWindow();
		tools.openToolsWindow(args);
	}
	
	@Override
	protected void doBackUp() {
		specsUtil.backupSpecs(selectedProcessPointIds());
	}

	@Override
	protected void doRemove() {
		specsUtil.removeSpecs(selectedProcessPointIds());
	}

	@Override
	protected void doRestore(File[] files) {
		specsUtil.restoreSpecsFromFiles(files);
	}
	
	
	protected void setSelectedWorkingDirectory() {
		super.setSelectedWorkingDirectory();
		specsUtil.setWorkingDirectory(workingDirectory);
	}
	

}
