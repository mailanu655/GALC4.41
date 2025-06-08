package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.SaveFeaturesEventHandler;

public class SaveFeaturesEvent extends GwtEvent<SaveFeaturesEventHandler>{
  public static Type<SaveFeaturesEventHandler> TYPE = new Type<SaveFeaturesEventHandler>();
  private final String id;
  
  public SaveFeaturesEvent(String id) {
    this.id = id;
  }
  
  public String getId() { return id; }
  
  @Override
  public Type<SaveFeaturesEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(SaveFeaturesEventHandler handler) {
    handler.onSaveFeatures(this);
  }
}
