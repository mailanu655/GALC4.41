package com.honda.galc.visualoverview.client.event;

import org.gwtopenmaps.openlayers.client.layer.Image;

import com.google.gwt.event.shared.GwtEvent;
import com.honda.galc.visualoverview.client.eventhandler.DrawImageLayerEventHandler;

public class DrawImageLayerEvent extends GwtEvent<DrawImageLayerEventHandler>{
  public static Type<DrawImageLayerEventHandler> TYPE = new Type<DrawImageLayerEventHandler>();
  private final Image image;
  
  public DrawImageLayerEvent(Image image) {
    this.image = image;;
  }
  
  public Image getImage() { return image; }
    
  @Override
  public Type<DrawImageLayerEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(DrawImageLayerEventHandler handler) {
    handler.onDrawImage(this);
  }
}
