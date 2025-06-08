package com.honda.galc.visualoverview.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.EditViewEventHandler;

public class EditViewEvent extends GwtEvent<EditViewEventHandler>{
  public static Type<EditViewEventHandler> TYPE = new Type<EditViewEventHandler>();
  private final String id;
  
  public EditViewEvent(String id) {
    this.id = id;
  }
  
  public String getId() { return id; }
  
  @Override
  public Type<EditViewEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(EditViewEventHandler handler) {
    handler.onEditView(this);
  }
}
