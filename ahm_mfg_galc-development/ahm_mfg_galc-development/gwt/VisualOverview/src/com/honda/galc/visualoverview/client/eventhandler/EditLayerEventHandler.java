package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.EditLayerEvent;

public interface EditLayerEventHandler extends EventHandler {
  void onEditLayer(EditLayerEvent event);
}