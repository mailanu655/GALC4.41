package com.honda.galc.checkers;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.entity.conf.MCOperationRevision;

/**
 * @author Subu Kathiresan
 * @date Oct 2, 2014
 */
public class CheckersRegistry {

	private ConcurrentHashMap<String, List<MCAppChecker>> appCheckers = new ConcurrentHashMap<String, List<MCAppChecker>>();
	private ConcurrentHashMap<MCOperationRevision, List<MCOperationChecker>> opCheckers = new ConcurrentHashMap<MCOperationRevision, List<MCOperationChecker>>();
	private ConcurrentHashMap<MCOperationRevision, List<MCPartChecker>> partCheckers = new ConcurrentHashMap<MCOperationRevision, List<MCPartChecker>>();
	private ConcurrentHashMap<MCOperationRevision, List<MCMeasurementChecker>> measCheckers = new ConcurrentHashMap<MCOperationRevision, List<MCMeasurementChecker>>();
	
	private static CheckersRegistry instance = null;
	
	private CheckersRegistry() {}
	
	public static CheckersRegistry getInstance() {
		if (instance == null) {
			instance = new CheckersRegistry();
		}
		return instance;
	}

	public ConcurrentHashMap<MCOperationRevision, List<MCOperationChecker>> getOpCheckers() {
		return opCheckers;
	}

	public void setOpCheckers(ConcurrentHashMap<MCOperationRevision, List<MCOperationChecker>> opCheckers) {
		this.opCheckers = opCheckers;
	}

	public ConcurrentHashMap<MCOperationRevision, List<MCPartChecker>> getPartCheckers() {
		return partCheckers;
	}

	public void setPartCheckers(ConcurrentHashMap<MCOperationRevision, List<MCPartChecker>> partCheckers) {
		this.partCheckers = partCheckers;
	}

	public ConcurrentHashMap<MCOperationRevision, List<MCMeasurementChecker>> getMeasurementCheckers() {
		return measCheckers;
	}

	public void setMeasurementCheckers(ConcurrentHashMap<MCOperationRevision, List<MCMeasurementChecker>> measCheckers) {
		this.measCheckers = measCheckers;
	}

	public ConcurrentHashMap<String, List<MCAppChecker>> getAppCheckers() {
		return appCheckers;
	}

	public void setAppCheckers(ConcurrentHashMap<String, List<MCAppChecker>> appCheckers) {
		this.appCheckers = appCheckers;
	}
}
