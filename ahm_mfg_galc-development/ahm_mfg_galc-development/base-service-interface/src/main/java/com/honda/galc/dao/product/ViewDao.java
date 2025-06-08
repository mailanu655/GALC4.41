package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.View;
import com.honda.galc.entity.product.ViewId;
import com.honda.galc.service.IDaoService;

/**
 * @author Cody Getz
 * @date Jul 16, 2013 
 * 
 */
public interface ViewDao extends IDaoService<View, ViewId> {
	
	public List<View> getAllViews();
	public List<View> getViewsById(String viewId);
}


