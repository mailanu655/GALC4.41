package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.EditLayerCancelledEvent;

public interface EditLayerCancelledEventHandler extends EventHandler {
  void onEditLayerCancelled(EditLayerCancelledEvent event);
}