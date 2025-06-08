package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LayerDao;
import com.honda.galc.entity.product.Layer;
import com.honda.galc.service.Parameters;

public class LayerDaoImpl extends BaseDaoImpl<Layer, String>
	implements LayerDao {
	
	private static final String FIND_ALL_ORDERED_SQL = "select l from Layer l order by l.layerId";
	private static final String FIND_TYPE_SQL = "select l from Layer l where l.layerType = :layerType order by l.layerId";
	private static final String FIND_LAYER_BY_ID = "select l from Layer l where l.layerId = :layerId";
	private static final String FIND_LAYER_BY_VIEW = "select distinct l from Layer l, View v where l.layerId = v.id.layerId and v.id.viewId =:viewId";
	
	public List<Layer> getAllLayers() {
		return findAllByQuery(FIND_ALL_ORDERED_SQL);
	}
	
	public List<Layer> getLayerByViewId(String viewId)
	{
		Parameters parameters = Parameters.with("viewId", viewId.trim());
		return findAllByQuery(FIND_LAYER_BY_VIEW, parameters);
	}
	
	public Layer getLayerById(String layerId)
	{
		Parameters parameters = Parameters.with("layerId", layerId.trim());
		return findFirstByQuery(FIND_LAYER_BY_ID, parameters);
	}
	
	public List<Layer> getLayersByType(String layerType)
	{
		Parameters params = Parameters.with("layerType", layerType);
		List<Layer> objects = findAllByQuery(FIND_TYPE_SQL, params);
		return objects == null ? null : objects;
	}

}
