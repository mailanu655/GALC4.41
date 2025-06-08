package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.EditFeatureEventHandler;

public class EditFeatureEvent extends GwtEvent<EditFeatureEventHandler>{
  public static Type<EditFeatureEventHandler> TYPE = new Type<EditFeatureEventHandler>();
  private final String id;
  
  public EditFeatureEvent(String id) {
    this.id = id;
  }
  
  public String getId() { return id; }
  
  @Override
  public Type<EditFeatureEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(EditFeatureEventHandler handler) {
    handler.onEditFeature(this);
  }
}
