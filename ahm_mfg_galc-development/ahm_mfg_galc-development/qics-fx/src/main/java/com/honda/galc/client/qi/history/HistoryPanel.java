package com.honda.galc.client.qi.history;

import java.util.List;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dto.ProductHistoryDisplayDto;
import com.honda.galc.entity.product.ProductHistory;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>HistoryPanel</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Mar 31, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class HistoryPanel extends AbstractQiProcessView<HistoryModel, HistoryPanelController>{	

	private ObjectTablePane<ProductHistoryDisplayDto> productHistoryPane;

	public HistoryPanel(MainWindow mainWindow) {
		super(ViewId.PRODUCT_HISTORY,mainWindow);
		initView();
	}

	private ObjectTablePane<ProductHistoryDisplayDto> createHistoryPane() {

		ColumnMappingList columnMappingList = ColumnMappingList.with("Process Point ID", "processPointId")
				.put("Process Point Name", "processPointName").put("Actual Timestamp", "actualTimestamp").put("Carrier Id", "carrierId")
				;

		Double[] columnWidth = new Double[] {0.25,0.25,0.25,0.25};
		ObjectTablePane<ProductHistoryDisplayDto> panel = new ObjectTablePane<ProductHistoryDisplayDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}


	@Override
	public void reload() {
		@SuppressWarnings("unchecked")
		List<ProductHistory> list =  (List<ProductHistory>) getProductModel().getProductHistory();
		List<ProductHistoryDisplayDto> productHistoryDisplayDto = getController().getProductHistoryList(list);
		getProductModel().setProductHistory(list);
		productHistoryPane.setDataWithPreserveSort(productHistoryDisplayDto);
	}

	@Override
	public void start() {

	}

	@Override
	public void initView() {

		productHistoryPane = createHistoryPane();
		LoggedTableColumn<ProductHistoryDisplayDto, Integer> column = new LoggedTableColumn<ProductHistoryDisplayDto, Integer>();
		createSerialNumber(column);
		productHistoryPane.getTable().getColumns().add(0, column);
		productHistoryPane.getTable().getColumns().get(0).setText("#");
		productHistoryPane.getTable().getColumns().get(0).setResizable(true);
		productHistoryPane.getTable().getColumns().get(0).setMaxWidth(50);
		productHistoryPane.getTable().getColumns().get(0).setMinWidth(15);
		this.setCenter(productHistoryPane);
	}

	public ObjectTablePane<ProductHistoryDisplayDto> getHistoryPane() {
		return productHistoryPane;
	}

	public void setHistoryPane(ObjectTablePane<ProductHistoryDisplayDto> historyPane) {
		this.productHistoryPane = historyPane;
	}

}
