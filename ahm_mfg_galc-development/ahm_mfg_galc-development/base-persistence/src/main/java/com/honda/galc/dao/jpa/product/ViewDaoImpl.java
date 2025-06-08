package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ViewDao;
import com.honda.galc.entity.product.Layer;
import com.honda.galc.entity.product.View;
import com.honda.galc.entity.product.ViewId;
import com.honda.galc.service.Parameters;

public class ViewDaoImpl extends BaseDaoImpl<View, ViewId>
	implements ViewDao {
	
	private static final String FIND_ALL_ORDERED_SQL = "select distinct v from View v order by v.id.viewId";
	private static final String FIND_BY_VIEW_ID = "select distinct v from View v where v.id.viewId = :viewId order by v.id.layerId";
	
	public List<View> getAllViews() {
		return findAllByQuery(FIND_ALL_ORDERED_SQL);
	}
	
	public List<View> getViewsById(String viewId)
	{
		Parameters params = Parameters.with("viewId", viewId);
		List<View> objects = findAllByQuery(FIND_BY_VIEW_ID, params);
		return objects == null ? null : objects;
	}

}
