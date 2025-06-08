package com.honda.galc.checkers;

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.MCAppCheckerDao;
import com.honda.galc.dao.conf.MCMeasurementCheckerDao;
import com.honda.galc.dao.conf.MCPartCheckerDao;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Subu Kathiresan
 * @date Oct 2, 2014
 */
public class CheckerUtil {

	private static Logger logger = null;
	
	public static List<MCPartChecker> getCheckers(String appId, String checkPointName) {
		String checkerKey = (StringUtils.isBlank(checkPointName))?appId:appId+Delimiter.COLON+checkPointName;
		if (!CheckersRegistry.getInstance().getAppCheckers().containsKey(checkerKey)) {
			CheckerUtil.loadAppCheckers(appId, checkPointName);
		}
		return CheckersRegistry.getInstance().getPartCheckers().get(checkerKey);
	}
	
	public static SortedArrayList<MCAppChecker> getAppCheckers(String appId, String checkPointName) {
		String checkerKey = (StringUtils.isBlank(checkPointName))?appId:appId+Delimiter.COLON+checkPointName;
		if (!CheckersRegistry.getInstance().getAppCheckers().containsKey(checkerKey)) {
			CheckerUtil.loadAppCheckers(appId, checkPointName);
		}
		return new SortedArrayList<MCAppChecker>(CheckersRegistry.getInstance().getAppCheckers().get(checkerKey), "getCheckSeq");
	}
	
	public static SortedArrayList<MCPartChecker> getPartCheckers(MCOperationRevision operation, String checkPointName,String partId) {
		
		// TODO consolidate checkers defined at Operation and Application levels
		if (!CheckersRegistry.getInstance().getPartCheckers().containsKey(operation)) {
			CheckerUtil.loadPartCheckers(operation, checkPointName,partId);
		}

		return new SortedArrayList<MCPartChecker>(CheckersRegistry.getInstance().getPartCheckers().get(operation), "getCheckSeq");
	}
	
	public static SortedArrayList<MCMeasurementChecker> getMeasurementCheckers(MCOperationRevision operation, String checkPointName) {
		
		// TODO consolidate checkers defined at Operation and Application levels
		if (!CheckersRegistry.getInstance().getMeasurementCheckers().containsKey(operation)) {
			CheckerUtil.loadMeasurementCheckers(operation, checkPointName);
		}

		return new SortedArrayList<MCMeasurementChecker>(CheckersRegistry.getInstance().getMeasurementCheckers().get(operation), "getCheckSeq");
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends InputData> AbstractBaseChecker<T> createChecker(String iChecker, Class<T> inputDataClass) {
		Class<?> clazz;
		AbstractBaseChecker<T> checker = null;
		try {
			clazz = Class.forName(iChecker);
			if(!AbstractBaseChecker.class.isAssignableFrom(clazz))
				getLogger().error(iChecker + " is not a Checker that implements IChecker");
			Constructor<?> constructor = clazz.getConstructor();
			checker = (AbstractBaseChecker<T>) constructor.newInstance();
		} catch (Throwable e) {
			getLogger().error("Failed to create Checker:" + iChecker);
		}
		return checker;
	}
	
	public static void loadAppCheckers(String appId) {
		List<MCAppChecker> appCheckers = ServiceFactory.getDao(MCAppCheckerDao.class).findAllByApplicationId(appId);
		CheckersRegistry.getInstance().getAppCheckers().put(appId, appCheckers);
	}
	
	public static void loadAppCheckers(String appId, String checkPointName) {
		if(StringUtils.isBlank(checkPointName)) {
			loadAppCheckers(appId);
			return;
		}
		List<MCAppChecker> appCheckers = ServiceFactory.getDao(MCAppCheckerDao.class).findAllBy(appId, checkPointName);
		CheckersRegistry.getInstance().getAppCheckers().put(appId+Delimiter.COLON+checkPointName, appCheckers);
	}
	
	public static void loadPartCheckers(MCOperationRevision operation, String checkPointName ,String partId) {
		List<MCPartChecker> partCheckers = ServiceFactory.getDao(MCPartCheckerDao.class).
				findAllByOperation(operation.getId().getOperationName(), 
						partId, 
						operation.getId().getOperationRevision(),
						checkPointName);
		CheckersRegistry.getInstance().getPartCheckers().put(operation, partCheckers);
	}
	
	public static void loadMeasurementCheckers(MCOperationRevision operation, String checkPointName) {
		List<MCMeasurementChecker> measCheckers = ServiceFactory.getDao(MCMeasurementCheckerDao.class).
				findAllByOperation(operation.getId().getOperationName(), 
						operation.getSelectedPart().getId().getPartId(), 
						operation.getId().getOperationRevision(),
						checkPointName);
		CheckersRegistry.getInstance().getMeasurementCheckers().put(operation, measCheckers);
	}
	
	public static Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger();
		}
		return logger;
	}
}
