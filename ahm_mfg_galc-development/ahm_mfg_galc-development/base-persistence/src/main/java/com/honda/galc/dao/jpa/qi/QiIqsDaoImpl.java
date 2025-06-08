package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiIqsDao;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QIIqsDaoImpl Class description</h3>
 * <p> QIIqsDaoImpl description </p>
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
 * July 7 2016
 *
 *
 */

public class QiIqsDaoImpl extends BaseDaoImpl<QiIqs, Integer> implements QiIqsDao {

	private static String FIND_ALL_IQS_ASSOCIATED_DATA = "SELECT distinct qiIqs.IQS_VERSION,qiIqs.IQS_CATEGORY,qiIqs.IQS_QUESTION_NO,qiIqs.IQS_QUESTION,qiIqs.ACTIVE, qiIqs.IQS_ID " +
			"FROM GALADM.QI_IQS_TBX qiIqs where qiIqs.ACTIVE in(?1,?2)";

	private static String FIND_ALL_CATEGORIES_BY_VERSION = "SELECT distinct e.iqsCategory FROM QiIqs e, QiIqsCategory c WHERE e.iqsCategory = c.iqsCategory and e.active = 1 and e.iqsVersion = :iqsVersion  order by e.iqsCategory";

	/**
	 * To Update IQS Status
	 * @param iqsId
	 * @param active
	 * @param userId 
	 */
	@Transactional
	public void updateIqsStatus(Integer iqsId, short active, String userId) {
		Parameters params = Parameters.with("active", active)
				.put("updateUser", userId);
		Parameters whereParams = Parameters.with("iqsId", iqsId);
		update(params, whereParams);

	}
	/**
	 * To check if IQS Association Exist
	 * @param iqsId
	 * @param active
	 * @param userId 
	 */
	public boolean isIqsAssociationExists(QiIqs qiIqs){
		Parameters params= Parameters.with("iqsVersion", qiIqs.getIqsVersion()).put("iqsCategory", qiIqs.getIqsCategory()).put("iqsQuestionNo", qiIqs.getIqsQuestionNo());
		return (!findAll(params).isEmpty());
	}

	/**
	 * To Find All IQS Associated Data
	 * @param statusList
	 */
	public List<QiIqs> findAllIqsAssociatedData(List<Short> statusList){
		Parameters params = Parameters.with("1", statusList.get(0)).put("2", statusList.get(1));
		StringBuilder findAssociationForSelectedValue = new StringBuilder(FIND_ALL_IQS_ASSOCIATED_DATA);
		findAssociationForSelectedValue.append(" ORDER BY qiIqs.IQS_VERSION");
		return (findAllByNativeQuery(findAssociationForSelectedValue.toString(), params));
	}

	/**
	 * To Find All IQS Associated Data
	 * @param qiIqs
	 * @param statusList
	 */
	public List<QiIqs> findAllAssociationForSelectedValue(QiIqs qiIqs ,List<Short> statusList){
		Parameters params = Parameters.with("1", statusList.get(0)).put("2", statusList.get(1));
		StringBuilder findAssociationForSelectedValue = new StringBuilder(FIND_ALL_IQS_ASSOCIATED_DATA);
		if(!StringUtils.isBlank(qiIqs.getIqsVersion())){
			params.put("3", qiIqs.getIqsVersion());
			findAssociationForSelectedValue.append(" and qiIqs.IQS_VERSION=?3");
			if(!StringUtils.isBlank(qiIqs.getIqsCategory())){
				params.put("4", qiIqs.getIqsCategory());
				findAssociationForSelectedValue.append(" and qiIqs.IQS_CATEGORY=?4");
			}
			if(qiIqs.getIqsQuestionNo()!=0){
				params.put("5", qiIqs.getIqsQuestionNo()).put("6", qiIqs.getIqsQuestion());
				findAssociationForSelectedValue.append(" and qiIqs.IQS_QUESTION_NO=?5 and qiIqs.IQS_QUESTION=?6");
			}
		}else if(!StringUtils.isBlank(qiIqs.getIqsCategory())){
			params.put("4", qiIqs.getIqsCategory());
			findAssociationForSelectedValue.append(" and qiIqs.IQS_CATEGORY=?4");
			if(qiIqs.getIqsQuestionNo()!=0){
				params.put("5", qiIqs.getIqsQuestionNo()).put("6", qiIqs.getIqsQuestion());
				findAssociationForSelectedValue.append(" and qiIqs.IQS_QUESTION_NO=?5 and qiIqs.IQS_QUESTION=?6");
			}
		}else if(qiIqs.getIqsQuestionNo()!=0){
			params.put("5", qiIqs.getIqsQuestionNo()).put("6", qiIqs.getIqsQuestion());
			findAssociationForSelectedValue.append(" and qiIqs.IQS_QUESTION_NO=?5 and qiIqs.IQS_QUESTION=?6");
		}
		findAssociationForSelectedValue.append(" ORDER BY qiIqs.IQS_VERSION");

		return (findAllByNativeQuery(findAssociationForSelectedValue.toString(), params));

	}

	/**
	 * To Update Question # in all Associated Data for selected value
	 * @param iqsId
	 * @param quesNo
	 * @param user
	 */
	@Transactional
	public void updateQuestionNoAndQuestion(Integer iqsId, Integer quesNo, String user, String ques) {
		Parameters params = Parameters.with("iqsQuestionNo", quesNo)
				.put("updateUser", user).put("iqsQuestion", ques);
		Parameters whereParams = Parameters.with("iqsId", iqsId);
		update(params, whereParams);

	}

	/**
	 * To Find All Associated Data for selected version
	 * @param qiIqsVersion
	 */
	public List<QiIqs> findAllByVersion(String qiIqsVersion) {
		Parameters params= Parameters.with("iqsVersion",qiIqsVersion);
		return findAll(params);
	}

	/**
	 * To Find All Associated Data for selected category
	 * @param qiIqsCategory
	 */
	public List<QiIqs> findAllByCategory(String qiIqsCategory) {
		Parameters params= Parameters.with("iqsCategory",qiIqsCategory);
		return findAll(params);
	}

	/**
	 * To Find All Associated Data for selected question no
	 * @param qiIqsQuestionNo
	 */
	public List<QiIqs> findAllByQuestionNoAndQuestion(Integer qiIqsQuestionNo, String iqsQuestion) {
		Parameters params= Parameters.with("iqsQuestionNo",qiIqsQuestionNo).put("iqsQuestion", iqsQuestion);
		return findAll(params);
	}
	
	/**
	 * To Update Version in all Associated Data for selected value
	 * @param iqsId
	 * @param version
	 * @param user
	 */
	@Transactional
	public void updateVersion(Integer iqsId, String version, String user) {
		Parameters params = Parameters.with("iqsVersion", version)
				.put("updateUser", user);
		Parameters whereParams = Parameters.with("iqsId", iqsId);
		update(params, whereParams);

	}
	
	/**
	 * To Update Category in all Associated Data for selected value
	 * @param iqsId
	 * @param category
	 * @param user
	 */
	@Transactional
	public void updateCategory(Integer iqsId, String category, String user) {
		Parameters params = Parameters.with("iqsCategory", category)
				.put("updateUser", user);
		Parameters whereParams = Parameters.with("iqsId", iqsId);
		update(params, whereParams);
	}
	
	/**
	 * Find all category by version
	 * @param iqsVersion
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllCategoriesByVersion(String iqsVersion) {
		Parameters params = Parameters.with("iqsVersion", iqsVersion);
		return findResultListByQuery(FIND_ALL_CATEGORIES_BY_VERSION, params);
	}
	
	/**
	 * Find iqsid
	 * @param iqs
	 */
	public QiIqs find(QiIqs iqs) {
		Parameters params = Parameters.with("iqsCategory", iqs.getIqsCategory())
				.put("iqsVersion", iqs.getIqsVersion()).put("iqsQuestionNo", iqs.getIqsQuestionNo())
				.put("iqsQuestion", iqs.getIqsQuestion());
		return findFirst(params);
	}
	/**
	 * Find all iqs question numbers by version and category
	 * @param iqsCategory
	 */
	public List<QiIqs> findAllByVersionAndCategory(String iqsVersion, String iqsCategory) {
		return findAll(Parameters.with("iqsVersion", iqsVersion).put("iqsCategory", iqsCategory), new String[]{"iqsQuestion"}, true);
	}
}
