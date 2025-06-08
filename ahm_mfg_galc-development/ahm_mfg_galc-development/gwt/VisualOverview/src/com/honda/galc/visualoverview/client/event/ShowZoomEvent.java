package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.ShowZoomEventHandler;
import com.honda.galc.visualoverview.shared.Layer;

public class ShowZoomEvent extends GwtEvent<ShowZoomEventHandler>{
  public static Type<ShowZoomEventHandler> TYPE = new Type<ShowZoomEventHandler>();

  
  
  public ShowZoomEvent() {

  }
  
  @Override
  public Type<ShowZoomEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(ShowZoomEventHandler handler) {
    handler.onShowZoom(this);
  }
}
