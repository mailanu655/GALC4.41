package com.honda.galc.visualoverview.shared.constants;

import org.gwtopenmaps.openlayers.client.Projection;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Window;

public interface VisualizationConstants extends Constants{
	String WEB_SERVICE_LOCATION = "http://" + Window.Location.getHost() + "/RestWeb/";
	Projection DEFAULT_PROJECTION = new Projection("EPSG:4326");
	int PLOT_REFRESH = 30000;

}
