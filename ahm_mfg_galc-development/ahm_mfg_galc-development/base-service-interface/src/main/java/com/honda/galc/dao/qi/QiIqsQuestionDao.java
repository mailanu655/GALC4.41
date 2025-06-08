package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiIqsQuestion;
import com.honda.galc.entity.qi.QiIqsQuestionId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QIIqsQuestionDao Class description</h3>
 * <p> QIIqsQuestionDao description </p>
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
 *
 * </TABLE>
 *   
 * @author L&T Infotech<br>
 * July 4 2016
 *
 *
 */

public interface QiIqsQuestionDao extends IDaoService<QiIqsQuestion, QiIqsQuestionId> {
	
	public void updateIqsQuestion(Integer iqsQuestionNo, String currentIqsQuestion, String userId,Integer previousQuestionNo, String previousIqsQuestion);
	public boolean isIqsQuestionExists(int iqsQuestionNo,String iqsQuestion);
	public boolean isIqsQuestionNoExists(int iqsQuestionNo);
	public List<QiIqsQuestion> findAndSortAllByQuestionNo();
	public List<QiIqsQuestion> findAllByQuestionId(List<Integer> questionNumbers);
	public void deleteIqsQuestion(QiIqsQuestion qiIqsQuestion);
}
