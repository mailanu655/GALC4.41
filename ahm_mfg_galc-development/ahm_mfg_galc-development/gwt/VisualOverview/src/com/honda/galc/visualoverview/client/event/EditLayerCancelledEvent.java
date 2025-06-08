package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.EditLayerCancelledEventHandler;

public class EditLayerCancelledEvent extends GwtEvent<EditLayerCancelledEventHandler>{
  public static Type<EditLayerCancelledEventHandler> TYPE = new Type<EditLayerCancelledEventHandler>();
  
  @Override
  public Type<EditLayerCancelledEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(EditLayerCancelledEventHandler handler) {
    handler.onEditLayerCancelled(this);
  }
}
