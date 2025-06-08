package com.honda.galc.client.ui;

import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * <h3>DefaultWindow Class description</h3>
 * <p> DefaultWindow description </p>
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
 * Jan 8, 2013
 *
 *
 */
public class DefaultWindow extends MainWindow{

	
	private static final long serialVersionUID = 1L;
	
	private ApplicationMainPanel panel;

	public DefaultWindow(ApplicationContext appContext,Application application) {
		this(appContext,application,true);
	}
	
	public DefaultWindow(ApplicationContext appContext,Application application,boolean statusFlag) {
		super(appContext,application,statusFlag);
		init();
		setSize(1024,768);
	}
	
	protected void init() {
		String panelClassName = getMainPanelName();
		if(!StringUtils.isEmpty(panelClassName)){
			panel = createApplicationPanel(panelClassName);
			this.setClientPanel(panel);
		}
	}
	
	protected void notifyWindowOpened() {
		if(panel != null)panel.panelRealized();
	}
	private String getMainPanelName(){
		return getApplicationProperty("PANEL_CLASS_NAME");
	}
	
	@SuppressWarnings("unchecked")
	protected ApplicationMainPanel createApplicationPanel(String panelClassName) {
		if(StringUtils.isEmpty(panelClassName)) throw new TaskException("non existent panel class " + panelClassName);
		Class<?> clazz;
		try{
			clazz = Class.forName(panelClassName);
		}catch(Exception e) {
			throw new SystemException("Unable to get panel class from the class name", e);
		}
		if(!JPanel.class.isAssignableFrom(clazz)) 
			throw new TaskException("class " + clazz.getSimpleName() + " is not a JPanel");
		Object[] params = new Object[]{(MainWindow)this};
		return ReflectionUtils.createInstance((Class<? extends ApplicationMainPanel>)clazz, (MainWindow)this);
	}

	public ApplicationMainPanel getPanel() {
		return panel;
	}
	
}
