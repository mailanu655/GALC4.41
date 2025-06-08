package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.SaveFeaturesEvent;

public interface SaveFeaturesEventHandler extends EventHandler {
  void onSaveFeatures(SaveFeaturesEvent event);
}