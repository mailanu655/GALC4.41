package com.honda.galc.service;

import com.honda.galc.common.logging.Logger;
import com.ibm.websphere.management.AdminService;
import com.ibm.websphere.management.AdminServiceFactory;
import com.ibm.ejs.ras.RasHelper;
import com.honda.galc.buildlevel.BuildLevelUtility;

public class ApplicationEnvServiceImpl implements ApplicationEnvService {

	private static Logger logger = null;

	public String getCellName() {
		AdminService adminService = AdminServiceFactory.getAdminService();
		String cellName = adminService.getCellName();
		return cellName;
	}

	public String getCurrentCellName() {
	    String cellName = null;

        try {
            cellName = RasHelper.getServerName().split("\\\\", 2)[0];
            getLogger().info("The cell name is: " + cellName);
        } catch (Exception e) {
            getLogger().error(e, "Cannot determine the cell: ");
        }

        return cellName;
	}

	public String getBuildLevel() {
		return BuildLevelUtility.getBuildComment();
	}
	
	public String getBuildNumber() {
		String buildComment = BuildLevelUtility.getBuildComment().split("\\_\\_\\_")[1];
		return buildComment;
	}

	public String getBuildDate() {
		return BuildLevelUtility.getBuildTimestamp();
	}

	public String getBuildType() {
		String buildComment = BuildLevelUtility.getBuildComment().split("\\_\\_\\_")[0];
		return buildComment;
	}

	public static Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger();
		}
		return logger;
	}

}
