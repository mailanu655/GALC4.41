package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.ConfigEventHandler;

public class ConfigEvent extends GwtEvent<ConfigEventHandler>{
  public static Type<ConfigEventHandler> TYPE = new Type<ConfigEventHandler>();
  
  public ConfigEvent() {
  }
  
  
  @Override
  public Type<ConfigEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(ConfigEventHandler handler) {
    handler.onConfig(this);
  }
}
