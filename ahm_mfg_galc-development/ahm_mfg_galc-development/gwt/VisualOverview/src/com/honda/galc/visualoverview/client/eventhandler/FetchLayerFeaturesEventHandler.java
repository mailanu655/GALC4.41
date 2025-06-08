package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.FetchLayerFeaturesEvent;

public interface FetchLayerFeaturesEventHandler extends EventHandler {
  void onFetchLayerFeatures(FetchLayerFeaturesEvent event);
}