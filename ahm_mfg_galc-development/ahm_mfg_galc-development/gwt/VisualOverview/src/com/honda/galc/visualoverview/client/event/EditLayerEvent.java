package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.EditLayerEventHandler;

public class EditLayerEvent extends GwtEvent<EditLayerEventHandler>{
  public static Type<EditLayerEventHandler> TYPE = new Type<EditLayerEventHandler>();
  private final String id;
  
  public EditLayerEvent(String id) {
    this.id = id;
  }
  
  public String getId() { return id; }
  
  @Override
  public Type<EditLayerEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(EditLayerEventHandler handler) {
    handler.onEditLayer(this);
  }
}
