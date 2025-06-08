package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.entity.product.LetDiagResult;
import com.honda.galc.entity.product.LetDiagResultId;
import com.honda.galc.service.IDaoService;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 04, 2013
 */
public interface LetDiagResultDao extends IDaoService<LetDiagResult, LetDiagResultId> 
{
	public List<LetDiagResult> getLetDiagRsltBtwStartEndTmp(Timestamp startTimestamp,Timestamp endTimestamp,String terminalId) ;
	public List<Object[]> getLetDiagNamesWithDetails(String endTimestamp,String terminalId);
	public List<Object[]> getMaxFaultCodeCountWithTestId(String startDate,String endDate,String terminalId);
	public List<Object[]> getLetDiagParamValues(String endTimestamp,String terminalId);
	public List<Object[]> getLetDiagResultTerminalIdList(String startDate,String endDate,String terminalId);
	public List<Object[]> getLetDiagResultNameData(String startDate,String endDate, String terminalId);
	public List<Object[]> getLetDiagRsltView(Timestamp endTimestamp, String terminalId);
	public List<Object[]> getLetDiagColumns(Timestamp startTimestamp, Timestamp endTimestamp, String terminalId);
}