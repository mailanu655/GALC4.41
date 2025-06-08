package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.DrawFeatureEvent;

public interface DrawFeatureEventHandler extends EventHandler {
  void onDrawFeature(DrawFeatureEvent event);
}