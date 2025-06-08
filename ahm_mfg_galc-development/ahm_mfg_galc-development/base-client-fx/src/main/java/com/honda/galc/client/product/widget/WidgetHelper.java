package com.honda.galc.client.product.widget;

/**
 * @author Suriya Sena
 * @date 24 Oct 2013
 */
import java.io.IOException;
import java.net.URL;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class WidgetHelper {

	private static final String resourcePath = "%s.fxml";
	private static final WidgetHelper instance = new WidgetHelper();

	private WidgetHelper() {
	}

	public static WidgetHelper getInstance() {
		return instance;
	}

	public Node loadFXML(Node widget) {
		URL resource = getViewURL(widget);
		FXMLLoader loader = new FXMLLoader(resource);
		loader.setRoot(widget);
		loader.setController(widget);
		try {
			Node node = (Node) loader.load();
			return node;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getViewPath(Node widget) {
		return String.format(resourcePath, widget.getClass().getSimpleName());
	}

	public URL getViewURL(Node widget) {
		return widget.getClass().getResource(getViewPath(widget));
	}


}
