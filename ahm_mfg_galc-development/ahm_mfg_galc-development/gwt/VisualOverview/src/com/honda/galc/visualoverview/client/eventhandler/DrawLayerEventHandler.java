package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.DrawLayerEvent;

public interface DrawLayerEventHandler extends EventHandler {
  void onDrawLayer(DrawLayerEvent event);
}