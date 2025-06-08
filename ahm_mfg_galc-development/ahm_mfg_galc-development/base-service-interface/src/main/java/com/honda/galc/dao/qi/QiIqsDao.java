package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QIIqsDao Class description</h3>
 * <p>
 * QIIqsDao description
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
 * @author L&T Infotech<br>
 *         July 4 2016
 * 
 * 
 */

public interface QiIqsDao extends IDaoService<QiIqs, Integer> {

	public void updateIqsStatus(Integer iqsId, short active, String user);

	public List<QiIqs> findAllIqsAssociatedData(List<Short> statusList);

	public List<QiIqs> findAllAssociationForSelectedValue(QiIqs qiIqs, List<Short> statusList);

	public boolean isIqsAssociationExists(QiIqs iqs);

	public void updateQuestionNoAndQuestion(Integer iqsId, Integer quesNo, String user, String ques);

	public List<QiIqs> findAllByVersion(String qiIqsVersion);

	public List<QiIqs> findAllByCategory(String qiIqsCategory);

	public List<QiIqs> findAllByQuestionNoAndQuestion(Integer qiIqsQuestionNo, String iqsQuestion);

	public void updateVersion(Integer iqsId, String version, String user);

	public void updateCategory(Integer iqsId, String category, String user);

	public List<String> findAllCategoriesByVersion(String iqsVersion);

	public QiIqs find(QiIqs iqs);

    public List<QiIqs> findAllByVersionAndCategory(String iqsVersion, String iqsCategory);

}
