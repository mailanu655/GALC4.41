package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.ICheckPoint;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.observer.AbstractPersistenceManager;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.view.IDataCollectionWidget;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationRevision;

/**
 * @author Subu Kathiresan
 * @param <T>
 * @date Jun 19, 2014
 */
public interface IDataCollectionAction<T extends InputData> extends ICheckPoint<T> {

	public void perform(DataCollectionModel model, DataCollectionEvent event);
	
	public void setPersistenceManager(AbstractPersistenceManager persistenceManager);
	
	public void unregisterCheckPoint();
	
	public MCOperationRevision getOperation();

	public void setOperation(MCOperationRevision operation);
	
	public boolean dispatchReactions(List<CheckResult> checkResults, T inputData);
	
	public DataCollectionModel getModel();
	
	public void setModel(DataCollectionModel model);

	
	     
	      
	      public IDataCollectionWidget<OperationProcessor> getView();
	  	
	  	public void setView(IDataCollectionWidget<OperationProcessor> iDataCollectionWidget);
	  	
	  	
	  	

}
