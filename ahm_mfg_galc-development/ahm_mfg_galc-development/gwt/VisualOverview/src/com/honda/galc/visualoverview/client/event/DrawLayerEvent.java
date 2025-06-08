package com.honda.galc.visualoverview.client.event;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.DrawLayerEventHandler;
import com.honda.galc.visualoverview.shared.Feature;
import com.honda.galc.visualoverview.shared.Layer;
import com.honda.galc.visualoverview.shared.PrintAttributeFormat;

public class DrawLayerEvent extends GwtEvent<DrawLayerEventHandler>{
  public static Type<DrawLayerEventHandler> TYPE = new Type<DrawLayerEventHandler>();
  private final Layer layer;
  private final List<Feature> features;
  private final boolean editable;
  private final Map<String, List<PrintAttributeFormat>> attributeMap;
  
  
  public DrawLayerEvent(Layer layer, List<Feature> features, final Map<String, List<PrintAttributeFormat>> attributeMap, boolean editable) {
    this.layer = layer;
    this.features = features;
    this.attributeMap = attributeMap;
    this.editable = editable;
  }
  
  public Layer getLayer() { return layer; }
  
  public List<Feature> getFeatures() { return features; }
  
  public Map<String, List<PrintAttributeFormat>> getAttributeMap() { return attributeMap; }
  
  public boolean isEditable() { return editable; }
  
  @Override
  public Type<DrawLayerEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(DrawLayerEventHandler handler) {
    handler.onDrawLayer(this);
  }
}
