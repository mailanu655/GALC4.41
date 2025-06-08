package com.honda.galc.vios.client;

import com.google.gwt.user.client.Window;
import com.honda.galc.vios.shared.WidgetBase;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.rpc.RPCManager;

public class ProductionScheduleUnitViewWidget extends WidgetBase {

	private DataSource dataSource = new DataSource();

	private String WS_URL2 =

	//	 production
	"http://" + Window.Location.getHost()
			+ "/RestWeb/InProcessProductDao/getVinSequenceVIOS?"
			+ getProductId() + "&(int)" + getNumberofRecords();
	

	ListGrid grid = new ListGrid() {

		protected String getCellCSSText(ListGridRecord record, int rowNum,
				int colNum) {
			String style = super.getCellCSSText(record, rowNum, colNum);

			if (record.getAttribute("color").equals("green")) {

				style = "color:green;font-weight:bold;font-size:"
						+ getFontSize() + "%";

			} else if (record.getAttribute("color").equals("#2EFE2E")) {
				style = "color:#2EFE2E;font-weight:bold;font-size:"
						+ getFontSize() + "%";
			} else if (record.getAttribute("color").equals("yellow")) {
				style = "color:yellow;font-weight:bold;font-size:"
						+ getFontSize() + "%";
			} else if (record.getAttribute("color").equals("black")) {
				style = "background-color:#F5DA81;color:black;font-weight:bold;font-size:"
						+ getFontSize() + "%";
			}

			return style;
		}

	};

	@Override
	public void onModuleLoad() {
		
		RPCManager.setAllowCrossDomainCalls(true);
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(WS_URL2);

		DataSourceTextField serialField = new DataSourceTextField("productId",
				"Serial");
		DataSourceTextField mtoField = new DataSourceTextField("MTO", "MTO");

		DataSourceTextField colorField = new DataSourceTextField("color");
		colorField.setHidden(true);

		dataSource.setFields(serialField, mtoField, colorField);
		grid.setDataSource(dataSource);
		grid.setAlign(Alignment.CENTER);

		grid.setAutoFitData(Autofit.HORIZONTAL);
		grid.setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);

		grid.setAutoFetchData(true);
		grid.setWidth(getWidgetWidth());

		grid.setHeight(getWidgetHeight());

		grid.draw();

	}

}
