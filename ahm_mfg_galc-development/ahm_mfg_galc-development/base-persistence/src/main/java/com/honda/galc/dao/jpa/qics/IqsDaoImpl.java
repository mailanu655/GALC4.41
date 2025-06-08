package com.honda.galc.dao.jpa.qics;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.IqsDao;
import com.honda.galc.entity.qics.Iqs;
import com.honda.galc.entity.qics.IqsId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>IqsDaoImpl Class description</h3>
 * <p> IqsDaoImpl description </p>
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
 * @author Jeffray Huang<br>
 * Feb 28, 2011
 *
 *
 */
public class IqsDaoImpl extends BaseDaoImpl<Iqs,IqsId> implements IqsDao{
	
	private static final String FIND_ALL_DISTINCT = "Select e.id.iqsCategoryName, min(e.id.iqsItemName) from Iqs e GROUP BY e.id.iqsCategoryName";
	
	private static final String FIND_ALL_DISTINCT_IQS_ITEMS = "Select min(e.id.iqsCategoryName), e.id.iqsItemName from Iqs e GROUP BY e.id.iqsItemName";
	
	public List<Iqs> findAllIqsByDistinct() {
		List<?> iqsData = findAllByQuery(FIND_ALL_DISTINCT);
		List<Iqs> iqsList = new ArrayList<Iqs>();
		for (Object object : iqsData) {
			Object[] arr = (Object[])object;
			IqsId id = new IqsId(arr[0].toString(), arr[1].toString());
			Iqs iqs = new Iqs();
			iqs.setid(id);
			iqsList.add(iqs);
		}
		return iqsList;
	}
	
	public List<Iqs> findAllIqsItemsByDistinct() {
		List<?> iqsData = findAllByQuery(FIND_ALL_DISTINCT_IQS_ITEMS);
		List<Iqs> iqsList = new ArrayList<Iqs>();
		for (Object object : iqsData) {
			Object[] arr = (Object[])object;
			IqsId id = new IqsId(arr[0].toString(), arr[1].toString());
			Iqs iqs = new Iqs();
			iqs.setid(id);
			iqsList.add(iqs);
		}
		return iqsList;
	}
	
	public List<Iqs> findAllByCategoryName(String categoryName) {
        Parameters params = Parameters.with("id.iqsCategoryName", categoryName);
        return findAll(params);
  }

}
