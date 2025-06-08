package com.honda.galc.client.collector;

import java.lang.reflect.Constructor;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>CollectorMain</h3>
 * <h3> Class description</h3>
 * <h4> This is intend to be a general purpose client for headless data collector
 * </h4>
 * <p> CollectorMain description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Dec 2, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Dec 2, 2011
 */

public class CollectorMain extends MainWindow{
	private static final long serialVersionUID = 1L;
	protected CollectorPanel collectorPanel;
	protected CollectorClientController controller;
	private CollectorClientPropertyBean property;
	private Logger logger;
	
	public CollectorMain(ApplicationContext appContext,Application application) {
		super(appContext,application, true);
		init();
	}
	
	private void init() {
		setFrameProperties();
		initConnections();
	}
	
	private void initConnections() {
		controller = createController(getProperty().getControllerClass());
	}

	protected void setFrameProperties() {
		try {
			setName("LotControlMain");
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setResizable(true); 
			setSize(1024, 768);
			
	        setClientPanel(getClientPanel());
			
		} catch (java.lang.Throwable t) {
			Logger.getLogger().error(t, this.getClass().getSimpleName());
		}
	}
	
	@SuppressWarnings("unchecked")
	public CollectorClientController createController(String clazzName) {
		Class claz;
		try {
			claz = Class.forName(clazzName);
			Class[] parameterTypes = {this.getClass()};
			Object[] parameters = {this};
			Constructor constructor = claz.getConstructor(parameterTypes);
			return (CollectorClientController) constructor.newInstance(parameters);
		} catch (Throwable e) {
			throw new TaskException("Failed to create controller:" + clazzName);
		}
	}

	private CollectorPanel getClientPanel() {
		return getCollectorPanel();
	}

	public CollectorPanel getCollectorPanel() {
		if(collectorPanel == null)
			collectorPanel = new CollectorPanel(getProperty(), this);
		
		return collectorPanel;
	}

	public void setCollectorPanel(CollectorPanel collectorPanel) {
		this.collectorPanel = collectorPanel;
	}

	public CollectorClientController getController() {
		return controller;
	}

	public void setController(CollectorClientController controller) {
		this.controller = controller;
	}
	
	public CollectorClientPropertyBean getProperty() {
		if(property == null)
			property = PropertyService.getPropertyBean(CollectorClientPropertyBean.class, getApplicationContext().getProcessPointId());
		
		return property;
	}
	
	public Logger getLogger(){
		if(logger == null){
		   logger = Logger.getLogger(applicationContext.getProcessPointId() + getProperty().getNewLogSuffix());
		}
		
		return logger;
	}

}
