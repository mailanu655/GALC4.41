package com.honda.galc.client.ui;

import javafx.scene.layout.Pane;

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
 * @author Suriya Sena
 * Jan 23, 2014 JavaFx migration
 *
 *
 */
public class DefaultWindow extends MainWindow{

	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private ApplicationMainPane pane;

	public DefaultWindow(ApplicationContext appContext,Application application) {
		super(appContext,application,true);
		init();
	}
	
	protected void init() {
		String paneClassName = getMainPaneName();
		if(!StringUtils.isEmpty(paneClassName)){
			pane = createApplicationPane(paneClassName);
			this.setClientPane(pane);
		}
	}
	
	protected void notifyWindowOpened() {
		if(pane != null)pane.panelRealized();
	}
	private String getMainPaneName(){
		return getApplicationProperty("FX_PANE_CLASS_NAME");
//		return getApplicationProperty("PANEL_CLASS_NAME");
	}
	
	@SuppressWarnings("unchecked")
	protected ApplicationMainPane createApplicationPane(String paneClassName) {
		if(StringUtils.isEmpty(paneClassName)) throw new TaskException("non existent pane class " + paneClassName);
		Class<?> clazz;
		try{
			clazz = Class.forName(paneClassName);
		}catch(Exception e) {
			throw new SystemException("Unable to get pane class from the class name", e);
		}
		if(!Pane.class.isAssignableFrom(clazz)) {
			throw new TaskException("class " + clazz.getSimpleName() + " is not a javafx.scene.layout.Pane");
		}
		Object[] params = new Object[]{(MainWindow)this};
		return ReflectionUtils.createInstance((Class<? extends ApplicationMainPane>)clazz, (MainWindow)this);
	}

	public ApplicationMainPane getPanel() {
		return pane;
	}
	
}
