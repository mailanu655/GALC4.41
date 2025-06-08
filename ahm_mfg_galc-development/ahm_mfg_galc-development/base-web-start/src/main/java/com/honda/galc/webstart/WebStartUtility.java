package com.honda.galc.webstart;

import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.conf.TerminalTypeDao;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.conf.TerminalType;
import com.honda.galc.service.ServiceFactory;
import com.ibm.ejs.ras.RasHelper;

/**
 * @author Subu Kathiresan
 * @date Sep 21, 2012
 *
 */
public class WebStartUtility {

	public static String getCellName() {
		String cellName = null;
		try {
			cellName = RasHelper.getServerName().split("\\\\", 2)[0];
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Cannot determine cell name");
		}
		return cellName;
	}
	
	public static TerminalType getTerminalType(String hostname) {
		TerminalDao terminalDao  = ServiceFactory.getDao(TerminalDao.class);
		Terminal terminal = terminalDao.findByKey(hostname);
		
		TerminalTypeDao terminalTypeDao  = ServiceFactory.getDao(TerminalTypeDao.class);
		return terminalTypeDao.findById(terminal.getAfTerminalFlag());
	}
}