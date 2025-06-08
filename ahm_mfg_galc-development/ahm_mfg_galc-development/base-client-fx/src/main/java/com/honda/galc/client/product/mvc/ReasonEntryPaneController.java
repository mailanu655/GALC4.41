package com.honda.galc.client.product.mvc;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.product.pane.ReasonEntryPane;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.ProcessCompleteEvent;
import com.honda.galc.client.ui.event.ProductResultEvent;
import com.honda.galc.client.ui.event.TextFieldEvent;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ReasonEntryPaneController implements ChangeListener<String> {
	ReasonEntryPane view;
	
	public ReasonEntryPaneController(ReasonEntryPane view) {
		this.view = view;
		initListeners();
		EventBusUtil.register(this);
	}

	private void initListeners() {
		view.getReasonTextFieldControl().textProperty().addListener(this);
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		EventBusUtil.publish(new TextFieldEvent(view.getId(), newValue));
	}
	
	@Subscribe
	public void onProductResultEvent(ProductResultEvent event) {
		if(event == null) return;
		if(event instanceof ProcessCompleteEvent) {
			view.getReasonTextField().clear();
		}
	}
}
