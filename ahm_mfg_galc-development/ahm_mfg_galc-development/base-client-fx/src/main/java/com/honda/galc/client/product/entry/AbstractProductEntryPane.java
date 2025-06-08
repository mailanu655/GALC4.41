package com.honda.galc.client.product.entry;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;

import javafx.scene.control.TabPane;

public abstract class AbstractProductEntryPane extends TabPane {
	protected TabbedPanel parentView;
	
	private ApplicationContext applicationContext;
	private double paneHeight;
	private double paneWidth;

	public AbstractProductEntryPane(TabbedPanel parentView) {
		this.parentView = parentView;
		this.applicationContext = parentView.getMainWindow().getApplicationContext();
		setPaneHeight(parentView.getPrefHeight() * 0.145);
		setPaneWidth(parentView.getPrefWidth() * 0.90);
	}

	public TabbedPanel getParentView() {
		return this.parentView;
	}
	
	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}
	
	public MainWindow getMainWindow() {
		return this.parentView.getMainWindow();
	}
	
	public double getPaneHeight() {
		return this.paneHeight;
	}
	
	public void setPaneHeight(double height) {
		this.paneHeight = height;
		setPrefHeight(height);
	}
	
	public double getPaneWidth() {
		return this.paneWidth;
	}
	
	public void setPaneWidth(double width) {
		this.paneWidth = width;
		setPrefWidth(width);
	}
}
