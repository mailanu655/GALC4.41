package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.LoadImageEvent;

public interface LoadImageEventHandler extends EventHandler {
  void onLoadImage(LoadImageEvent event);
}