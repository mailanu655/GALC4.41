package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.ShowZoomEvent;

public interface ShowZoomEventHandler extends EventHandler {
  void onShowZoom(ShowZoomEvent event);
}