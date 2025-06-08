package com.honda.galc.service;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.entity.product.Feature;

public interface LineVisualizationService extends IService {

	public List<Feature> getLineVisualization(String plantName, String lineId, String featurePrefix, String trackingLayer);
}
