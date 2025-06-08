package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.Layer;
import com.honda.galc.service.IDaoService;

/**
 * @author Cody Getz
 * @date Jul 16, 2013 
 * 
 */
public interface LayerDao extends IDaoService<Layer, String> {
	
	public List<Layer> getAllLayers();
	public List<Layer> getLayersByType(String layerType);
	public Layer getLayerById(String layerId);
	public List<Layer> getLayerByViewId(String viewId);
}
