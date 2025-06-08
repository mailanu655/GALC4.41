package com.honda.galc.client.dc.view;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.observer.AbstractManager;
import com.honda.galc.client.dc.observer.IViewObserver;
import com.honda.galc.client.dc.processor.MeasurementProcessor;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.processor.PartProcessor;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationRevision;

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
public abstract class AbstractViewManager extends AbstractManager implements IViewObserver{
	
	protected Map<MCOperationRevision,PartView> partViews = new HashMap<MCOperationRevision, PartView>();
	protected Map<MCOperationMeasurement,MeasurementView> measurementViews= new HashMap<MCOperationMeasurement,MeasurementView>();
	protected Map<MCOperationRevision,OperationView> operationViews = new HashMap<MCOperationRevision, OperationView>();
	
	public AbstractViewManager(DataCollectionController dcController) {
		super(dcController);
	}
	
	public Map<MCOperationRevision, PartView> getPartViews() {
		return partViews;
	}

	protected void setPartViews(Map<MCOperationRevision, PartView> partViews) {
		this.partViews = partViews;
	}

	public Map<MCOperationMeasurement, MeasurementView> getMeasurementViews() {
		return measurementViews;
	}

	protected void setMeasurementViews(
			Map<MCOperationMeasurement, MeasurementView> measurementViews) {
		this.measurementViews = measurementViews;
	}

	public Map<MCOperationRevision, OperationView> getOperationViews() {
		return operationViews;
	}
	
	public PartProcessor getPartProcessor(MCOperationRevision structure) {
		PartView partView = partViews.get(structure);
		return partView == null ? null : partView.getProcessor();
	}
	
	public MeasurementProcessor getMeasurementProcessor(MCOperationMeasurement measurement) {
		MeasurementView view = measurementViews.get(measurement);
		return view == null ? null : view.getProcessor();
	}

	protected void setOperationViews(Map<MCOperationRevision, OperationView> operationViews) {
		this.operationViews = operationViews;
	}
	
	@SuppressWarnings("unchecked")
	protected MeasurementProcessor createMeasurementProcessor(MCOperationRevision structure, MCOperationMeasurement measurement) {
		String measurementProcessor = measurement.getProcessor();
		if(!StringUtils.isEmpty(measurementProcessor)){ 
			try {
				Class clazz = Class.forName(measurementProcessor);
				if(ClassUtils.isAssignable(clazz,MeasurementProcessor.class )){
					Class[] parameterTypes = {getController().getClass(),MCOperationRevision.class,MCOperationMeasurement.class};
					Object[] parameters = {getController(),structure,measurement};
					Constructor constructor = clazz.getConstructor(parameterTypes);
					return (MeasurementProcessor)constructor.newInstance(parameters);
				}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		return new MeasurementProcessor(getController(),structure,measurement);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected MeasurementView createMeasurementView(MeasurementProcessor measurementProcessor,MCOperationMeasurement measurement) {
		String viewClass = measurement.getView();
		MeasurementView measurementView = null;
		if(!StringUtils.isEmpty(viewClass)){ 
			try {
				Class clazz = Class.forName(viewClass);
				if(ClassUtils.isAssignable(clazz,MeasurementView.class)){
					Class[] parameterTypes = {MeasurementProcessor.class};
					Object[] parameters = {measurementProcessor};
					Constructor constructor = clazz.getConstructor(parameterTypes);
					return (MeasurementView)constructor.newInstance(parameters);
				}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else measurementView = new MeasurementView(measurementProcessor);
		measurementViews.put(measurement,measurementView);
		return measurementView;
	}
	
	@SuppressWarnings("unchecked")
	protected OperationView createOperationView(OperationProcessor operationProcessor, MCOperationRevision operation) {
		String viewClass = operation.getView();
		OperationView operationView = null;
		if(!StringUtils.isEmpty(viewClass)){ 
			try {
				Class clazz = Class.forName(viewClass);
				if(ClassUtils.isAssignable(clazz,OperationView.class)){
					Class[] parameterTypes = {OperationProcessor.class};
					Object[] parameters = {operationProcessor};
					Constructor constructor = clazz.getConstructor(parameterTypes);
					return (OperationView)constructor.newInstance(parameters);
				}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else operationView = new OperationView(operationProcessor);
		operationViews.put(operation, operationView);
		return operationView;
	}
}
