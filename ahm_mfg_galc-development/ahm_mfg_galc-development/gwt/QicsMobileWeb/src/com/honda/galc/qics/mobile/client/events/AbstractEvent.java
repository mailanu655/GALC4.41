package com.honda.galc.qics.mobile.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractEvent<Event extends GwtEvent<?>, EventData>
		extends GwtEvent<AbstractEvent.Handler<Event>> {

	private EventData eventData;

	@SuppressWarnings("unchecked")
	public static Type TYPE = new Type();

	public interface Handler<Event> extends EventHandler {
		void onEvent(Event event);
	}

	@SuppressWarnings("unchecked")
	protected void dispatch(AbstractEvent.Handler<Event> handler) {
		handler.onEvent((Event) this);
	}

	public EventData getEventData() {
		return eventData;

	}

	public void setEventData(EventData eventData) {
		this.eventData = eventData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Type<AbstractEvent.Handler<Event>> getAssociatedType() {
		return TYPE;
	}

	public AbstractEvent() {

	}

	public AbstractEvent(EventData eventData) {
		this.setEventData(eventData);
	}
}
