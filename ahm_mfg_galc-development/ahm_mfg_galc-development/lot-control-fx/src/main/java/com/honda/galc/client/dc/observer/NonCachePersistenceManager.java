package com.honda.galc.client.dc.observer;

import java.util.Arrays;

import com.honda.galc.client.dc.fsm.AbstractDataCollectionState;
import com.honda.galc.client.dc.fsm.ActionTypes.COMPLETE;
import com.honda.galc.client.dc.fsm.ActionTypes.INIT;
import com.honda.galc.client.dc.fsm.ActionTypes.MESSAGE;
import com.honda.galc.client.dc.fsm.IProcessPart;
import com.honda.galc.client.dc.fsm.IProcessProduct;
import com.honda.galc.client.dc.fsm.IProcessTorque;
import com.honda.galc.client.dc.fsm.ProcessPart;
import com.honda.galc.client.dc.fsm.ProcessProduct;
import com.honda.galc.client.dc.fsm.ProcessTorque;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.fsm.Observer;
import com.honda.galc.fsm.Observers;
import com.honda.galc.service.ServiceFactory;

public class NonCachePersistenceManager extends AbstractBasePersistenceManager{

	public NonCachePersistenceManager(DataCollectionController dcController) {
		super(dcController);
		// TODO Auto-generated constructor stub
	}

	@Observers({
			@Observer(state = IProcessProduct.class, actions = MESSAGE.class),
			@Observer(state = IProcessPart.class, actions = MESSAGE.class),
			@Observer(state = IProcessTorque.class, actions = MESSAGE.class) })
	public void message(AbstractDataCollectionState state) {
		// TODO Auto-generated method stub
		
	}

	@Observer(state = IProcessProduct.class, actions = COMPLETE.class)
	public void saveCompleteData(ProcessProduct state) {
		// TODO Auto-generated method stub
		
	}

	@Observer(state = IProcessPart.class, actions = INIT.class)
	public void initPart(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	@Observer(state = IProcessPart.class, actions = COMPLETE.class)
	public void completePart(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	@Observer(state = IProcessTorque.class, actions = COMPLETE.class)
	public void completeTorque(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveInstalledPart(InstalledPart installedPart) {
		getLogger().info(" Saving Part with out caching ");
		ServiceFactory.getService(InstalledPartDao.class).saveAll(Arrays.asList(installedPart), getApplicationProperty().isSavePartHistory());
		
	}

	@Override
	public void saveMeasurement(Measurement measurement) {
		getLogger().info(" Saving Measurement with out caching ");
		ServiceFactory.getService(MeasurementDao.class).saveAll(Arrays.asList(measurement), getApplicationProperty().isSaveMeasurementHistory());
	}

}
