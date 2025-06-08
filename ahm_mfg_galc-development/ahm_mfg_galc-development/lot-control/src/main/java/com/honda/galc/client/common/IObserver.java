package com.honda.galc.client.common;

public interface IObserver {
	void update(Observable observable, Object data);

	void cleanUp();
}
