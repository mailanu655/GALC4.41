package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.FetchLayerFeaturesEventHandler;
import com.honda.galc.visualoverview.shared.Layer;

public class FetchLayerFeaturesEvent extends GwtEvent<FetchLayerFeaturesEventHandler>{
  public static Type<FetchLayerFeaturesEventHandler> TYPE = new Type<FetchLayerFeaturesEventHandler>();
  private final Layer layer;
  private final boolean editable;
  
  
  public FetchLayerFeaturesEvent(Layer layer, boolean editable) {
    this.layer = layer;
    this.editable = editable;
  }
  
  public Layer getLayer() { return layer; }
  
  public boolean isEditable() { return editable; }
  
  @Override
  public Type<FetchLayerFeaturesEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(FetchLayerFeaturesEventHandler handler) {
    handler.onFetchLayerFeatures(this);
  }
}
