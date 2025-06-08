package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.DeleteFeatureEventHandler;
import com.honda.galc.visualoverview.shared.Feature;

public class DeleteFeatureEvent extends GwtEvent<DeleteFeatureEventHandler>{
  public static Type<DeleteFeatureEventHandler> TYPE = new Type<DeleteFeatureEventHandler>();
  private final String id;
  private final Feature feature;
  
  public DeleteFeatureEvent(String id, Feature feature) {
    this.id = id;
    this.feature = feature;
  }
  
  public String getId() { return id; }
  
  public Feature getFeature() { return feature; }
  
  @Override
  public Type<DeleteFeatureEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(DeleteFeatureEventHandler handler) {
    handler.onDeleteFeature(this);
  }
}
