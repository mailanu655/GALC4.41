package com.honda.galc.client.ui.listener;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import com.honda.galc.client.ui.MainWindow;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 8, 2014
 */
public class BaseListener<T extends BorderPane> {

	protected T view;

	public BaseListener(T view) {
		this.view = view;
	}

	public T getView() {
		return view;
	}

	protected void preExecute() {
		clearMessage();
	}

	protected void postExecute() {
	}

	protected void clearMessage() {
		MainWindow mainWindow = getMainWindow();
		if (mainWindow != null) {
			mainWindow.clearMessage();
		}
	}

	protected MainWindow getMainWindow() {
		Parent parent = view.getParent();
		while (null != parent && !(parent instanceof MainWindow)) {
			parent = view.getParent();
		}
		return (MainWindow) parent;
	}

}
