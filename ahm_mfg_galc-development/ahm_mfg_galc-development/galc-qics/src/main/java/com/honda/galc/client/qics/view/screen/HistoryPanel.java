package com.honda.galc.client.qics.view.screen;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
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
public class HistoryPanel extends QicsPanel {

	private static final long serialVersionUID = 1L;

	private ObjectTablePane<ProductHistory> historyPane;

	public HistoryPanel(QicsFrame frame) {
		super(frame);
		initialize();
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.HISTORY;
	}

	protected void initialize() {
		setLayout(new GridLayout(1, 1));
		historyPane = createHistoryPane();
		add(getHistoryPane());
	}

	@Override
	public void startPanel() {
		List<ProductHistory> list = getProductModel().getProductHistory();
		if (list == null) {
			list = new ArrayList<ProductHistory>(getQicsController().selectProductHistory());
			
			for(ProductHistory item : list) {
				item.setProcessPointName(getClientModel().getProcessPointName(item.getProcessPointId()));
			}
			getProductModel().setProductHistory(list);
		}

		getHistoryPane().reloadData(list);
		setButtonsState();
	}

	@Override
	public void setButtonsState() {
		super.setButtonsState();
		getQicsFrame().getMainPanel().setButtonsState();
	}

	public ObjectTablePane<ProductHistory> getHistoryPane() {
		return historyPane;
	}

	public void setHistoryPane(ObjectTablePane<ProductHistory> historyPane) {
		this.historyPane = historyPane;
	}

	// === ui factory methods === //
	protected ObjectTablePane<ProductHistory> createHistoryPane() {

		ColumnMappings columnMappings = 
			ColumnMappings.with("#", "row").put("Process Point ID", "processPointId")
						  .put("Process Point Name", "processPointName").put("Actual Timestamp", "actualTimestamp");
		

		ObjectTablePane<ProductHistory> panel = new ObjectTablePane<ProductHistory>(columnMappings.get());

		return panel;
	}
}
