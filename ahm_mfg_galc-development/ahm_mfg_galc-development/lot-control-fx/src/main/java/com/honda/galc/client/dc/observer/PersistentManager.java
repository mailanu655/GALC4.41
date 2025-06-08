
package com.honda.galc.client.dc.observer;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.dc.fsm.AbstractDataCollectionState;
import com.honda.galc.client.dc.fsm.ActionTypes.COMPLETE;
import com.honda.galc.client.dc.fsm.IProcessProduct;
import com.honda.galc.client.dc.fsm.ProcessPart;
import com.honda.galc.client.dc.fsm.ProcessProduct;
import com.honda.galc.client.dc.fsm.ProcessTorque;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.data.cache.ResultsCacheDispatcher;
import com.honda.galc.data.cache.ResultsCacheItem;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.fsm.Observer;

/**
 * 
 * <h3>PerisistentManager Class description</h3>
 * <p>
 * PerisistentManager description
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
 * @author Jeffray Huang<br>
 *         Feb 25, 2014
 * 
 * 
 */
public class PersistentManager extends AbstractBasePersistenceManager {

	private static final String SAVE_ALL = "saveAll";
	
	public PersistentManager(DataCollectionController dcController) {
		super(dcController);
	}

	public void completePart(ProcessPart state) {
		// TODO Auto-generated method stub

	}

	public void completeTorque(ProcessTorque state) {
		// TODO Auto-generated method stub

	}

	public void initPart(ProcessPart state) {
		// TODO Auto-generated method stub

	}

	public void message(AbstractDataCollectionState state) {
		// TODO Auto-generated method stub

	}

	/**
	 * Checks if the current product is skipped .
	 *
	 * @return true, if the product is skipped 
	 */
	protected boolean isSkippedProduct() {
		return getController().getProductModel().isSkipped();
	}

	
	
	public void saveInstalledPart(InstalledPart installedPart) {
		getLogger().info(" Saving Part using cache ");
		if (getModel().getProductModel().isTrainingMode()) return;
		String serverUrl = getModel().getProductModel().getServerUrl();
		
		List<InstalledPart> parts = new ArrayList<InstalledPart>();
		parts.add(installedPart);
		ResultsCacheItem cacheParts = new ResultsCacheItem(serverUrl, InstalledPartDao.class.getCanonicalName(), SAVE_ALL, 
				parts, getApplicationProperty().isSavePartHistory());
		ResultsCacheDispatcher.getInstance().addToCache(cacheParts);
	}
	
	

	public void saveMeasurement(Measurement measurement) {
		getLogger().info(" Saving Measurement using cache ");
		if (getModel().getProductModel().isTrainingMode()) return;
		String serverUrl = getModel().getProductModel().getServerUrl();
		
		List<Measurement> measurements = new ArrayList<Measurement>();
		measurements.add(measurement);
		ResultsCacheItem cacheMeasurements = new ResultsCacheItem(serverUrl, MeasurementDao.class.getCanonicalName(), SAVE_ALL, 
				measurements, getApplicationProperty().isSaveMeasurementHistory());
		ResultsCacheDispatcher.getInstance().addToCache(cacheMeasurements);
	}
	
	
	
	@Observer(state = IProcessProduct.class, actions = COMPLETE.class)
	public void saveCompleteData(ProcessProduct state) {
		// TODO Auto-generated method stub
	}
	
}
