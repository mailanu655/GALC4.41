package com.honda.galc.qics.mobile.client.widgets.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageResources extends ClientBundle {

    public static ImageResources INSTANCE = GWT.create(ImageResources.class);

    
    @Source("attention.png")
    ImageResource attention();
    
    @Source("check.png")
    ImageResource check();
    
    @Source("qics.jpg")
    ImageResource qics();
    
    @Source("loader.gif")
    ImageResource loader();
    
    @Source("settings.png")
    ImageResource settings();
}