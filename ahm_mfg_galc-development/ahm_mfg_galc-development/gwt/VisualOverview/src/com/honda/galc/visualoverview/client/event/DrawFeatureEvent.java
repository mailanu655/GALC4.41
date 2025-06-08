package com.honda.galc.visualoverview.client.event;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.DrawFeatureEventHandler;

public class DrawFeatureEvent extends GwtEvent<DrawFeatureEventHandler>{
  public static Type<DrawFeatureEventHandler> TYPE = new Type<DrawFeatureEventHandler>();
  private final Vector layer;
  private final VectorFeature vectorFeature;
  
  public DrawFeatureEvent(Vector layer, VectorFeature vectorFeature) {
    this.layer = layer;
    this.vectorFeature = vectorFeature;
  }
  
  public Vector getLayer() { return layer; }
  
  public VectorFeature getVectorFeature() { return vectorFeature; }
  
  @Override
  public Type<DrawFeatureEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(DrawFeatureEventHandler handler) {
    handler.onDrawFeature(this);
  }
}
