package com.honda.galc.client.dc.observer;

import com.honda.galc.client.dc.mvc.AbstractDataCollectionView;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.fsm.AbstractObserver;

/**
 * 
 * <h3>AbstractViewManager Class description</h3>
 * <p> AbstractViewManager description </p>
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
 * Feb 25, 2014
 *
 *
 */
public abstract class AbstractManager extends AbstractObserver{
	
	private DataCollectionController dcController;
	
	public AbstractManager(DataCollectionController dcController) {
		this.dcController = dcController;
		init();
	}

	protected void init() {
		
	}

	public DataCollectionController getController() {
		return dcController;
	}
	
	protected AbstractDataCollectionView getView() {
		return getController().getView();
	}
	
	protected DataCollectionModel getModel() {
		return getController().getModel();
	}
	
	public Logger getLogger() {
		return getController().getView().getLogger();
	}


}
