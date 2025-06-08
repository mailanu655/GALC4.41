package com.honda.galc.visualoverview.client.eventhandler;

import com.google.gwt.event.shared.EventHandler;
import com.honda.galc.visualoverview.client.event.EditViewEvent;

public interface EditViewEventHandler extends EventHandler {
  void onEditView(EditViewEvent event);
}