package com.honda.galc.dao.jpa.oif;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.oif.FifCodeChoicesDao;
import com.honda.galc.entity.fif.FifCodeChoices;
import com.honda.galc.entity.fif.FifCodeChoicesId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>FifCodeChoicesDaoImpl.java</h3> <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * AnnualCalendarDaoImpl.java description
 * </p>
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
 * <TD>KM</TD>
 * <TD>Feb 19, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * 
 * </TABLE>
 * 
 * @version 0.1
 * @author Xiaomei Ma
 * @created Feb 19, 2015
 */
public class FifCodeChoicesDaoImpl extends
		BaseDaoImpl<FifCodeChoices, FifCodeChoicesId> implements
		FifCodeChoicesDao {
	
	public List<FifCodeChoices> findFifCodeChoices( String plantCd,String modelYear,String modelCd,String devSeqCd,String fifCode,String fifType,String groupCd ){
		
		Parameters parameters = Parameters.with("id.plantCd", plantCd);
		parameters.put("id.modelYear", modelYear);
		parameters.put("id.modelCd", modelCd);
		parameters.put("id.devSeqCd", devSeqCd);
		parameters.put("id.fifCode", fifCode);
		parameters.put("id.fifType", fifType);
		parameters.put("id.groupCd", groupCd);
		return findAll(parameters);
		
	}

}
