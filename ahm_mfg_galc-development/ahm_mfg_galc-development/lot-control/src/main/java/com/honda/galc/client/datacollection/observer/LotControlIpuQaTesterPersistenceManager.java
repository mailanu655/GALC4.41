package com.honda.galc.client.datacollection.observer;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.dao.product.MeasurementSpecDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.MeasurementSpecId;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Gangadhararao Gadde
 * @author Subu Kathiresan
 * @Date Apr 27, 2012
 *
 */
public class LotControlIpuQaTesterPersistenceManager extends LotControlSubProductPersistenceManager {
	
	private MeasurementSpecDao _measurementSpecDao;
	
	public LotControlIpuQaTesterPersistenceManager(ClientContext context) {
		super(context);
	}
	
	@Override
	protected <T extends DataCollectionState> List<InstalledPart> prepareForSave(T state) {
		ProductBean product = state.getProduct();
		CopyOnWriteArrayList<InstalledPart> cloneList = new CopyOnWriteArrayList<InstalledPart>(product.getPartList());
		for (InstalledPart part : cloneList) {
			if (part.getInstalledPartStatus() == InstalledPartStatus.MISSING) {
				product.getPartList().remove(part);
			} else {
				validateMeasurements(part);
			}
		}
		return product.getPartList();
	}
	
	/**
	 * validates all measurements in the installed part
	 * and set measurement status
	 * 
	 * @param part
	 */
	private void validateMeasurements(InstalledPart part) {
		for (Measurement measurement : part.getMeasurements()) {
			MeasurementSpecId mSpecId = new MeasurementSpecId(part.getPartName(), part.getPartId(), 1);
			MeasurementSpec mSpec = getMeasurementSpecDao().findByKey(mSpecId);
			if (measurement.getMeasurementValue() > mSpec.getMaximumLimit() || measurement.getMeasurementValue() < mSpec.getMinimumLimit()) {
				measurement.setMeasurementStatus(MeasurementStatus.NG);
			} else {
				measurement.setMeasurementStatus(MeasurementStatus.OK);
			}
		}
	}
	
	public MeasurementSpecDao getMeasurementSpecDao() {
		if (_measurementSpecDao == null) {
			_measurementSpecDao = ServiceFactory.getDao(MeasurementSpecDao.class);
		}
		return _measurementSpecDao;
	}
}
