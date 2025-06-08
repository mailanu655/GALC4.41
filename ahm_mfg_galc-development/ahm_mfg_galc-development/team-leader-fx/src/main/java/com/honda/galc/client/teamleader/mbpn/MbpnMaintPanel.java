package com.honda.galc.client.teamleader.mbpn;


import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.client.utils.QiConstant;


import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.product.mvc.ViewId;

/**
 * 
 * <h3>MbpnMaintPanel Class description</h3>
 * <p> MbpnMaintPanel description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author L&T Infotech<br>
 *
 *
 */

public class MbpnMaintPanel extends AbstractTabbedView<MbpnModel,MbpnController>{
	private ObjectTablePane<Mbpn> mbpnTablePane;

	public MbpnMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	public void onTabSelected() {
		reload();
		getController().activate();
	}

	public String getScreenName() {
		return "MBPN";
	}

	@Override
	public void reload() {
		mbpnTablePane.setData(getModel().findAllMbpn());
	}

	@Override
	public void start() {}

	/**
	 * This method creates layout of the Mbpn Parent screen
	 */

	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		mbpnTablePane = createMbpnTablePane();
		setSerialNumberColumnProperty();
		this.setCenter(mbpnTablePane);
	}

	/**
	 * Sets the serial number column property.
	 */
	private void setSerialNumberColumnProperty() {
		LoggedTableColumn<Mbpn, Integer> column = new LoggedTableColumn<Mbpn, Integer>();
		createSerialNumber(column);
		column.setText("#");
		column.setResizable(true);
		column.setMaxWidth(100);
		column.setMinWidth(1);
		mbpnTablePane.getTable().getColumns().add(0, column);
	}
	
	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */

	private ObjectTablePane<Mbpn> createMbpnTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Main No", "mainNo")
				.put("Class No", "classNo").put("Prototype Code","prototypeCode")
				.put("Type No", "typeNo").put("Supplementary No", "supplementaryNo")
				.put("Target No","targetNo").put("Hes Color", "hesColor").put("Description", "description");

		Double[] columnWidth = new Double[] {
				0.10, 0.10, 0.10, 0.10, 0.10,0.10, 0.15, 0.16
		}; 
		ObjectTablePane<Mbpn> panel = new ObjectTablePane<Mbpn>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}

	public ObjectTablePane<Mbpn> getMbpnTablePane() {
		return mbpnTablePane;
	}

	public void setMbpnTablePane(ObjectTablePane<Mbpn> mbpnTablePane) {
		this.mbpnTablePane = mbpnTablePane;
	}

}






