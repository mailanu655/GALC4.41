package com.honda.galc.client.dc.observer;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;

public abstract class AbstractPersistenceManager extends AbstractManager implements IPersistenceObserver{

	public AbstractPersistenceManager(DataCollectionController dcController) {
		super(dcController);
	}

	abstract public void saveCollectedData(MCOperationRevision operation);
	
	abstract public void saveInstalledPart(InstalledPart installedPart);
	
	abstract public void deleteInstalledPart(InstalledPartId id);
	
	abstract public void saveMeasurement(Measurement measurement);
	
	abstract public void deleteMeasurement(MeasurementId id);
}
