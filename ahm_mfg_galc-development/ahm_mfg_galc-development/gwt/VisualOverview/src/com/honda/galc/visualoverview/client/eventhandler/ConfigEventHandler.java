package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.ConfigEvent;

public interface ConfigEventHandler extends EventHandler {
  void onConfig(ConfigEvent event);
}