package com.honda.galc.qics.mobile.client.events;

import com.google.gwt.core.client.GWT;
//import com.google.gwt.event.shared.EventBus;
//import com.google.gwt.event.shared.SimpleEventBus;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/** 
 * This is the applications EventBus.  It is used publish and subscribe to application events.   It
 * is intended to de-couple requestors and consumers.  This simplifies communication between application
 * components.
 * 
 * @author vfc01346
 *
 */
public class PubSubBus {
	public static EventBus EVENT_BUS = GWT.create( SimpleEventBus.class );

}
