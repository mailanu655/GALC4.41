package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.WaitEventHandler;

public class WaitEvent extends GwtEvent<WaitEventHandler>{
  public static Type<WaitEventHandler> TYPE = new Type<WaitEventHandler>();
  
  public WaitEvent() {
  }
  
  
  @Override
  public Type<WaitEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(WaitEventHandler handler) {
    handler.onWait(this);
  }
}
