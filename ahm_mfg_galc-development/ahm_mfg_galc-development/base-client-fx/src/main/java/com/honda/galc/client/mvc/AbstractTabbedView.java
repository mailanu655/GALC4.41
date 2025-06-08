package com.honda.galc.client.mvc;

import javafx.util.Callback;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.ITabbedPanel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;


/**
 * 
 * <h3>AbstractView Class description</h3>
 * <p> AbstractView description </p>
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
 * @author Jeffray Huang<br>
 * Feb 24, 2014
 *
 *
 */
public abstract class AbstractTabbedView<M extends IModel,C extends AbstractController<?,?>> extends AbstractView<M, C> implements ITabbedPanel{

	public AbstractTabbedView(ViewId viewId, TabbedMainWindow mainWindow){
		super(viewId,mainWindow);	
	}
	/**
	 *  This is used to create the serial number dynamically on the TableView's '#' column
	 * @param rowIndex
	 */
	public void createSerialNumber(LoggedTableColumn rowIndex){
		rowIndex.setCellFactory( new Callback<LoggedTableColumn, LoggedTableCell>()
				{
			public LoggedTableCell call(LoggedTableColumn p)
			{
				return new LoggedTableCell()
				{
					@Override
					public void updateItem( Object item, boolean empty )
					{
						super.updateItem( item, empty );
						setText( empty ? null : getIndex() + 1 + "" );
					}
				};
			}
				});
	}
}
