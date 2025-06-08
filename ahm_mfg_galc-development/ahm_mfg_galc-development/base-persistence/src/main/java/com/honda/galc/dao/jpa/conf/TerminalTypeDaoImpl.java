package com.honda.galc.dao.jpa.conf;

import com.honda.galc.dao.conf.TerminalTypeDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.TerminalType;
import com.honda.galc.service.Parameters;

/** 
* @author Subu Kathiresan 
* @since Sep 09, 2022
*/
public class TerminalTypeDaoImpl extends BaseDaoImpl<TerminalType, String> implements TerminalTypeDao {

	public static final String FIND_BY_ID = "select t from TerminalType t where t.terminalFlag = :terminalFlag";
	
	@Override
	public TerminalType findById(int terminalFlag) {
    	return findFirstByQuery(FIND_BY_ID, Parameters.with("terminalFlag", terminalFlag));
	}
}