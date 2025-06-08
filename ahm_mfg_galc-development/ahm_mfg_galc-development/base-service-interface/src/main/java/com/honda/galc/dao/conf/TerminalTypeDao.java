package com.honda.galc.dao.conf;

import com.honda.galc.entity.conf.TerminalType;
import com.honda.galc.service.IDaoService;

/** 
* @author Subu Kathiresan 
* @since Sep 09, 2022
*/
public interface TerminalTypeDao extends IDaoService<TerminalType, String> {
	
	public TerminalType findById(int terminalFlag);
}