package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.LoadImageEventHandler;

public class LoadImageEvent extends GwtEvent<LoadImageEventHandler>{
  public static Type<LoadImageEventHandler> TYPE = new Type<LoadImageEventHandler>();
  private final String layerId;
  private final String featureId;
  
  public LoadImageEvent(String layerId, String featureId) {
    this.layerId = layerId;
    this.featureId = featureId;
  }
  
  public String getLayerId() { return layerId; }
  
  public String getFeatureId() { return featureId; }
    
  @Override
  public Type<LoadImageEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(LoadImageEventHandler handler) {
    handler.onLoadImage(this);
  }
}
