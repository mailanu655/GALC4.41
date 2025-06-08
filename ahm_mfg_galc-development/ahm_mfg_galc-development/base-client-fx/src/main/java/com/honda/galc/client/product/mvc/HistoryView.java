package com.honda.galc.client.product.mvc;

import java.util.List;

import com.honda.galc.client.product.process.AbstractProcessView;
import com.honda.galc.client.ui.MainWindow;
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
public class HistoryView extends AbstractProcessView<HistoryModel, HistoryController> {

	private static final long serialVersionUID = 1L;

	public HistoryView(MainWindow window) {
		super(ViewId.HISTORY_VIEW,window);
	}


	@Override
	public void start() {
		List<? extends ProductHistory> list = getProductModel().getProductHistory();
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}
}
