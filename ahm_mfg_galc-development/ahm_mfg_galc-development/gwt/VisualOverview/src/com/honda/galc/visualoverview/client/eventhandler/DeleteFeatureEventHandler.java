package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.DeleteFeatureEvent;

public interface DeleteFeatureEventHandler extends EventHandler {
  void onDeleteFeature(DeleteFeatureEvent event);
}