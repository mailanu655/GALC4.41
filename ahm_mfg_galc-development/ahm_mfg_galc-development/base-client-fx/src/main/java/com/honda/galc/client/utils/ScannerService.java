package com.honda.galc.client.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ScannerService extends Service<String> {
	private StringProperty url = new SimpleStringProperty();

	private String cardName;

	public ScannerService(String cardName) {
		this.cardName = cardName;
	}

	public final StringProperty urlProperty() {
		return url;
	}

	protected Task<String> createTask() {

		return new Task<String>() {
			@Override
			public String call() {

				ProximityCardReader reader = new ProximityCardReader(cardName);
				String cardNumber = reader.readCard();

				return cardNumber;
			}

		};
	}
}
