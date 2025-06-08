package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.DrawNewFeatureEventHandler;
import com.honda.galc.visualoverview.shared.Feature;

public class DrawNewFeatureEvent extends GwtEvent<DrawNewFeatureEventHandler>{
  public static Type<DrawNewFeatureEventHandler> TYPE = new Type<DrawNewFeatureEventHandler>();
  private final String id;
  private final Feature feature;
  
  public DrawNewFeatureEvent(String id, Feature feature) {
    this.id = id;
    this.feature = feature;
  }
  
  public String getId() { return id; }
  
  public Feature getFeature() { return feature; }
  
  @Override
  public Type<DrawNewFeatureEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(DrawNewFeatureEventHandler handler) {
    handler.onDrawNewFeature(this);
  }
}
