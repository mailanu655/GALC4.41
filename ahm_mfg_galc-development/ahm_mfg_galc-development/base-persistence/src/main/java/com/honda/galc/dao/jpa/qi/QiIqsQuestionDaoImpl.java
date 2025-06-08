package com.honda.galc.dao.jpa.qi;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiIqsQuestionDao;
import com.honda.galc.entity.qi.QiExternalSystemDataId;
import com.honda.galc.entity.qi.QiIqsQuestion;
import com.honda.galc.entity.qi.QiIqsQuestionId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QIIqsQuestionDaoImpl Class description</h3>
 * <p> QIIqsQuestionDaoImpl description </p>
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

public class QiIqsQuestionDaoImpl extends BaseDaoImpl<QiIqsQuestion, QiIqsQuestionId> implements QiIqsQuestionDao {

	private static String UPDATE_IQS_QUESTION = "update QI_IQS_QUESTION_TBX  set IQS_QUESTION_NO=?1 ,IQS_QUESTION= ?2 , " +
			"UPDATE_USER = ?3 where IQS_QUESTION_NO= ?4 and IQS_QUESTION=?5";
	private final String GET_ALL_BY_QUESTION_NUMBERS= "SELECT e FROM QiIqsQuestion e WHERE e.id.iqsQuestionNo in ( :questionNumbers ) order by e.iqsQuestion";

	/**
	 * To Update IQS Question 
	 * @param currentIqsQuestion
	 * @param userId
	 * @param previousIqsQuestion
	 */
	@Transactional
	public void updateIqsQuestion(Integer currentIqsQuestionNo, String currentIqsQuestion, String userId, Integer previousQuestionNo, String previousIqsQuestion) {
		Parameters params = Parameters.with("1", currentIqsQuestionNo).put("2", currentIqsQuestion)
				.put("3", userId).put("4",previousQuestionNo).put("5", previousIqsQuestion);
		executeNativeUpdate(UPDATE_IQS_QUESTION, params);
	}
	/**
	 * To Check IQS Question Exists in DB
	 * @param iqsQuestion
	 */
	public boolean isIqsQuestionExists(int iqsQuestionNo, String iqsQuestion){
		Parameters params = Parameters.with("id.iqsQuestionNo", iqsQuestionNo).put("id.iqsQuestion", iqsQuestion);
		return !findAll(params).isEmpty();
	}
	/**
	 * To Check IQS Question No Exists in DB
	 * @param iqsQuestionNo
	 */
	public boolean isIqsQuestionNoExists(int iqsQuestionNo){
		return !findAll(Parameters.with("id.iqsQuestionNo", iqsQuestionNo)).isEmpty();
	}
	
	public List<QiIqsQuestion> findAndSortAllByQuestionNo(){
		return findAll(null,new String[]{"id.iqsQuestionNo"},true);
	}
	
	public List<QiIqsQuestion> findAllByQuestionId(List<Integer> questionNumbers){
		return findAllByQuery(GET_ALL_BY_QUESTION_NUMBERS, Parameters.with("id.questionNumbers", questionNumbers));
	}
	/**
	 * To delete Iqs Question
	 * @param qiIqsQuestion
	 */
	@Transactional
	public void deleteIqsQuestion(QiIqsQuestion qiIqsQuestion) {
		if(qiIqsQuestion == null)  return;
		QiIqsQuestionId id = qiIqsQuestion.getId();
		Parameters idParams = Parameters.with("id.iqsQuestion", id.getIqsQuestion())
				.put("id.iqsQuestionNo", id.getIqsQuestionNo());
		delete(idParams);
	}
}
