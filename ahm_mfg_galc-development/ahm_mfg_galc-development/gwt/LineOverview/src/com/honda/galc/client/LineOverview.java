package com.honda.galc.client;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.widgets.layout.VLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LineOverview implements EntryPoint {
	
	private static final String WEB_SERVICE_LINE_OVERVIEW = "http://" + Window.Location.getHost() + "/RestWeb/InProcessProductDao/getLineDetail?";
	private static final int REFRESH_TIME = 30000;
	private DataSource dataSource = new DataSource();
	private LineListGrid listGrid;
	private String lineId = "LINE11";
	
	public void onModuleLoad() {
	
		final VLayout layout = new VLayout(10);
		layout.setWidth100();
		layout.setHeight100();
		
		Window.enableScrolling(false);
		if(Window.Location.getQueryString().indexOf("lineId") >= 0)
		{
			lineId = Window.Location.getParameter("lineId");
		}
		
        dataSource.setDataFormat(DSDataFormat.JSON);
	    dataSource.setDataURL(WEB_SERVICE_LINE_OVERVIEW + lineId);   
	    
		listGrid = new LineListGrid();
        listGrid.setDataSource(dataSource); 
		listGrid.setAutoFetchData(true);
		listGrid.setAutoFitData(Autofit.HORIZONTAL);
		listGrid.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);
		listGrid.alignCells(Alignment.CENTER);
		
		listGrid.draw();
		
        startGridAsyncUpdateThread();
	}
	

	private void startGridAsyncUpdateThread() {
		Timer t = new Timer() {
			public void run() {
				try {
					dataSource.fetchData(new Criteria(), new DSCallback() {          

						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							if (response.getStatus() == RPCResponse.STATUS_TRANSPORT_ERROR) {
								return;
							}
							listGrid.invalidateCache();
							listGrid.setData(response.getData());
						}     
					});
				} catch (Exception ex) {
					System.out.println("Unable to retrieve data from server");
				}
			}
		};

		// Schedule the timer to run.
		t.scheduleRepeating(REFRESH_TIME);
	}
	
}
