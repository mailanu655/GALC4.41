package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.qi.QiIqsCategoryDao;
import com.honda.galc.dao.qi.QiIqsDao;
import com.honda.galc.dao.qi.QiIqsQuestionDao;
import com.honda.galc.dao.qi.QiIqsVersionDao;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.entity.qi.QiIqsCategory;
import com.honda.galc.entity.qi.QiIqsQuestion;
import com.honda.galc.entity.qi.QiIqsVersion;
import com.honda.galc.entity.qi.QiPartDefectCombination;

/**
 * 
 * <h3>IqsMaintenanceModel Class description</h3>
 * <p>
 * IqsMaintenanceModel is used to maintain IQS data 
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
 * 
 * </TABLE>
 * 
 * @author L&TInfotech<br>
 *        Sep 06, 2016
 * 
 */

public class IqsMaintenanceModel extends QiModel{

	/**
	 * Find All Data of IQS Category
	 */
	public List<QiIqsCategory> findAllIqsCategory() {
		return getDao(QiIqsCategoryDao.class).findAllIqsCategory();
	}

	/**
	 * Create new IQS Category
	 * @param qiIqsCategory
	 */
	public void createIqsCategory(QiIqsCategory qiIqsCategory) {
		getDao(QiIqsCategoryDao.class).insert(qiIqsCategory);
	}

	/**
	 * Update IQS Category
	 * @param qiIqsCategory
	 * @param previousIqsCategory
	 */
	public void updateIqsCategory(String currentIqsCategory, String previousIqsCategory) {
		getDao(QiIqsCategoryDao.class).updateIqsCategory(currentIqsCategory,getUserId(), previousIqsCategory);
	}

	/**
	 * Delete IQS Category
	 * @param qiIqsCategory
	 */
	public void deleteIqsCategory(QiIqsCategory qiIqsCategory) {
		getDao(QiIqsCategoryDao.class).remove(qiIqsCategory);
	}

	/**
	 * Check IQS Category exists
	 * @param 
	 */
	public boolean isIqsCategoryExists(String iqsCategory) {
		return getDao(QiIqsCategoryDao.class).findByKey(iqsCategory) != null;
	}

	/**
	 * Find All Data of IQS Question
	 */
	public List<QiIqsQuestion> findAllIqsQuestion() {
		return getDao(QiIqsQuestionDao.class).findAndSortAllByQuestionNo();
	}

	/**
	 * Create new IQS Question
	 * @param qiIqsQuestion
	 */
	public void createIqsQuestion(QiIqsQuestion qiIqsQuestion) {
		getDao(QiIqsQuestionDao.class).insert(qiIqsQuestion);
	}

	/**
	 * Update IQS Question
	 * @param qiIqsQuestion
	 * @param previousIqsQuestion
	 */
	public void updateIqsQuestion(Integer iqsQuestionNo, String currentIqsQuestion,Integer previousQuestionNo, String previousIqsQuestion) {
		getDao(QiIqsQuestionDao.class).updateIqsQuestion(iqsQuestionNo, currentIqsQuestion, getUserId(),previousQuestionNo, previousIqsQuestion);
	}

	/**
	 * Delete IQS Question
	 * @param qiIqsQuestionList
	 */
	public void deleteIqsQuestion(QiIqsQuestion qiIqsQuestion) {
		getDao(QiIqsQuestionDao.class).deleteIqsQuestion(qiIqsQuestion);
	}

	/**
	 * Check IQS Question exists
	 * @param iqsQuestion
	 */
	public boolean isIqsQuestionExists(int iqsQuestionNo, String iqsQuestion) {
		return getDao(QiIqsQuestionDao.class).isIqsQuestionExists(iqsQuestionNo, iqsQuestion);
	}
	/**
	 * Check IQS Question # exists
	 * @param 
	 */
	public boolean isIqsQuestionNoExists(int iqsQuestionNo) {
		return getDao(QiIqsQuestionDao.class).isIqsQuestionNoExists(iqsQuestionNo);
	}

	/**
	 * Find All Data of IQS Version
	 */
	public List<QiIqsVersion> findAllIqsVersion() {
		return getDao(QiIqsVersionDao.class).findAllIqsVersion();
	}

	/**
	 * Create new IQS Version
	 * @param qiIqsVersion
	 */
	public void createIqsVersion(QiIqsVersion qiIqsVersion) {
		getDao(QiIqsVersionDao.class).insert(qiIqsVersion);
	}

	/**
	 * Update IQS Version
	 * @param qiIqsVersion
	 * @param previousIqsVersion
	 */
	public void updateIqsVersion(String currentIqsVersion, String previousIqsVersion) {
		getDao(QiIqsVersionDao.class).updateIqsVersion(currentIqsVersion, getUserId(), previousIqsVersion);
	}

	/**
	 * Delete IQS Version
	 * @param iqsVersion
	 */
	public void deleteIqsVersion(QiIqsVersion iqsVersion) {
		getDao(QiIqsVersionDao.class).remove(iqsVersion);
	}

	/**
	 * Check IQS Version exists
	 * @param 
	 */
	public boolean isIqsVersionExists(String iqsVersion) {
		return getDao(QiIqsVersionDao.class).findByKey(iqsVersion) != null;
	}

	/**
	 * Update IQS Status
	 * @param qiIqsVersionList
	 */
	public void updateIqsStatus(Integer iqsId, short active){
		getDao(QiIqsDao.class).updateIqsStatus(iqsId, active, getUserId());
	}

	/**
	 * Associate IQS Data
	 * @param qiIqs
	 */
	public void AssociateIqsData(QiIqs qiIqs){
		qiIqs.setCreateUser(getUserId());
		getDao(QiIqsDao.class).save(qiIqs);
	}

	/**
	 * Find All Data of IQS Association
	 */
	public List<QiIqs> findAllIqsAssociatedData(List<Short> statusList) {
		return getDao(QiIqsDao.class).findAllIqsAssociatedData(statusList);
	}

	/**
	 * check Association For selected items
	 * @param iqsVersion
	 * @return
	 */
	public boolean isIqsAssociationExists(QiIqs iqs){
		return getDao(QiIqsDao.class).isIqsAssociationExists(iqs);
	}

	/**
	 * Update Iqs Association Status
	 * @param key
	 * @return
	 */
	public void updateIqsAssociationStatus(Integer iqsId, short active)
	{
		getDao(QiIqsDao.class).updateIqsStatus(iqsId, active, getUserId());
	}

	/**
	 * find Association For Version
	 * @param iqsVersion
	 * @return
	 */
	public List<QiIqs> findAssociationForSelectedValue(QiIqs qiIqs ,List<Short> statusList)
	{
		return getDao(QiIqsDao.class).findAllAssociationForSelectedValue(qiIqs ,statusList);
	}

	/**
	 * Update Question No in Iqs Association
	 * @param key
	 * @return
	 */
	public void updateQuestionNoAndQuestion(Integer iqsId, Integer quesNo,String ques)
	{
		getDao(QiIqsDao.class).updateQuestionNoAndQuestion(iqsId, quesNo, getUserId(),ques);
	}

	/**
	 * Fetch Association For selected version to be deleted
	 * @param iqsVersion
	 * @return
	 */
	public List<QiIqs> getIqsAssociationForSelectedVersion(String iqsVersion){
		return getDao(QiIqsDao.class).findAllByVersion(iqsVersion);
	}

	/**
	 * Fetch Association For selected category to be deleted
	 * @param iqsCategory
	 * @return
	 */
	public List<QiIqs> getIqsAssociationForSelectedCategory(String iqsCategory){
		return getDao(QiIqsDao.class).findAllByCategory(iqsCategory);
	}

	/**
	 * Fetch Association For selected question no to be deleted
	 * @param iqsQNo
	 * @return
	 */
	public List<QiIqs> getIqsAssociationForSelectedQuestionNo(Integer iqsQNo,String iqsQuestion){
		return getDao(QiIqsDao.class).findAllByQuestionNoAndQuestion(iqsQNo, iqsQuestion);
	}

	/**
	 * Update Version in Iqs Association
	 * @param key
	 * @return
	 */
	public void updateIqsAssociationVersion(Integer iqsId, String version)
	{
		getDao(QiIqsDao.class).updateVersion(iqsId, version, getUserId());
	}

	/**
	 * Update Category in Iqs Association
	 * @param key
	 * @return
	 */
	public void updateIqsAssociationCategory(Integer iqsId, String category)
	{
		getDao(QiIqsDao.class).updateCategory(iqsId, category, getUserId());
	}
	/**
	 * Find QiPartDefectCombination in Iqs Association
	 * @param qiIqs
	 * @return
	 */
	public List<QiPartDefectCombination> findAllByIqsId(Integer qiIqs){
		return getDao(QiPartDefectCombinationDao.class).findAllByIqsId(qiIqs);
	}
	/**
	 * delete Iqs Association
	 * @param qiIqs
	 * @return
	 */
	public void deleteIqsAssociation(Integer iqsId)
	{
		getDao(QiIqsDao.class).removeByKey(iqsId);
	}
	/**
	 * Update QiPartDefectCombination due to deletion of IQS Association
	 * @param pdcList
	 * @return
	 */
	public void updatePartDefectCombination(List<QiPartDefectCombination> pdcList){
		getDao(QiPartDefectCombinationDao.class).updateAll(pdcList);
	}
}
