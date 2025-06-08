package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.WaitEvent;

public interface WaitEventHandler extends EventHandler {
  void onWait(WaitEvent event);
}