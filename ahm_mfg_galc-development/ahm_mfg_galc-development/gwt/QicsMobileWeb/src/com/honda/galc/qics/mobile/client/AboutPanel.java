package com.honda.galc.qics.mobile.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.honda.galc.qics.mobile.client.utils.PhGap;
import com.honda.galc.qics.mobile.client.utils.Storage;
import com.smartgwt.mobile.client.widgets.Panel;
import com.smartgwt.mobile.client.widgets.ScrollablePanel;


/**
 * The Class AboutPanel displays general information about the application.
 */
public class AboutPanel extends ScrollablePanel {

	/**
	 * Instantiates a new about panel.
	 */
	public AboutPanel () {
		super("About");
		
        Panel aboutInfo = new Panel();
        StringBuilder sb = new StringBuilder();
        sb.append( "<ul>");
        addItem( sb, "Process Point", Settings.getProcessPoint() );
        addItem( sb, "Repair Dept", Settings.getRepairDepartment());
        addItem( sb, "Device Model", PhGap.getDeviceModel());
        addItem( sb, "Device OS Version", PhGap.getDeviceOsVersion());
        addItem( sb, "PhoneGap Version", PhGap.getPhoneGapVersion());
        addItem( sb, "Device UUID", PhGap.getDeviceUuid());
        addItem( sb, "Device Platform", PhGap.getDevicePlatform());
        addItem( sb, "Base Page URL", GWT.getHostPageBaseURL() );
        addItem( sb, "Storage API Available", "" + Storage.isStorageApiSupported() );
        addItem( sb, "Connection Type", "" + PhGap.getConnectionType() );
        addItem( sb, "Screen (Width x Height)", Window.getClientWidth() + " x " + Window.getClientHeight());
        sb.append( "</ul>");
        aboutInfo.setContents(sb.toString());
        aboutInfo.setClassName("sc-rounded-panel");
        aboutInfo.setMargin(10);
        this.addMember(aboutInfo);
	}
	
	private void addItem( StringBuilder sb, String title, String value ) {
		sb.append( "<li>");
		sb.append( title );
		sb.append( ": &nbsp;&nbsp; <b>");
		sb.append( value );
		sb.append( "</b></li>");
	}
	
	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return "(not available)";
	}
	
	
}

