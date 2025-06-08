package com.honda.galc.client.ui;

import javafx.util.Callback;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;

/**
 * 
 * <h3>FXMLController Class description</h3>
 * <p>
 * This class is to store Application ID and Product Kind for fxml screen
 * controller. Application ID will be set after fxml is loaded and then
 * controller can retrieve property by application ID.
 * </p>
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
 * @author Justin Jiang May 4, 2016
 */

public abstract class FXMLController {

	abstract public void refreshData();
	
	abstract public void setApplicationContext(ApplicationContext applicationContext);
	
	/**
	 * This method is used to clear the errorMessage while getting error on the screen
	 */
	public void clearMessage(){
		EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
	}
	/**
	 * This method is used to show error on the screen while getting exception
	 */
	public void errorMessage(String errorMessage){
		EventBusUtil.publish(new StatusMessageEvent(errorMessage, StatusMessageEventType.ERROR));
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
