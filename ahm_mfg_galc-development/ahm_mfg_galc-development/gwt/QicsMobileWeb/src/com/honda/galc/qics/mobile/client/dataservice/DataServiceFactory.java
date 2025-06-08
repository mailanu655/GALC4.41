package com.honda.galc.qics.mobile.client.dataservice;

import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.GWT;
import com.honda.galc.qics.mobile.client.Settings;

/**
 * A factory for creating DataService objects.
 */
public class DataServiceFactory {

	/** The service factory. */
	static private DataServiceFactory serviceFactory = null;
	
	/**
	 * Gets the single instance of DataServiceFactory.
	 *
	 * @return single instance of DataServiceFactory
	 */
	public static DataServiceFactory getInstance() {
		if ( serviceFactory == null ) {
			serviceFactory = new DataServiceFactory();
		}
		return serviceFactory;
	}
	
	/**
	 * Gets the REST Service base address.
	 *
	 * @return the base
	 */
	private String getBase() {
		return Settings.getRestWeb();
	}
	
	/**
	 * Gets the defect result data service.
	 *
	 * @return the defect result data service
	 */
	public DefectResultDataService getDefectResultDataService() {
		Resource resource = new Resource( getBase() );
		DefectResultDataService service = GWT.create(DefectResultDataService.class);
		((RestServiceProxy)service).setResource(resource);
		return service;
	}
	
	/**
	 * Gets the frame data service.
	 *
	 * @return the frame data service
	 */
	public FrameDataService getFrameDataService() {
		Resource resource = new Resource( getBase() );
		FrameDataService service = GWT.create(FrameDataService.class);
		((RestServiceProxy)service).setResource(resource);
		return service;
	}
	
	
	/**
	 * Gets the inspection model data service.
	 *
	 * @return the inspection model data service
	 */
	public InspectionModelDataService getInspectionModelDataService() {
		Resource resource = new Resource( getBase() );
		InspectionModelDataService service = GWT.create(InspectionModelDataService.class);
		((RestServiceProxy)service).setResource(resource);
		return service;
	}
	
	/**
	 * Gets the defect description data service.
	 *
	 * @return the defect description data service
	 */
	public DefectDescriptionDataService getDefectDescriptionDataService() {
		Resource resource = new Resource( getBase() );
		DefectDescriptionDataService service = GWT.create(DefectDescriptionDataService.class);
		((RestServiceProxy)service).setResource(resource);
		return service;
	}
	
	/**
	 * Gets the process point data service.
	 *
	 * @return the process point data service
	 */
	public ProcessPointDataService getProcessPointDataService() {
		Resource resource = new Resource( getBase() );
		ProcessPointDataService service = GWT.create(ProcessPointDataService.class);
		((RestServiceProxy)service).setResource(resource);
		return service;
	}
}
