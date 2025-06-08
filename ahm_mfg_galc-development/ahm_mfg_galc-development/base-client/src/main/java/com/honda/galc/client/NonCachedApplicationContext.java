package com.honda.galc.client;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.ApplicationByTerminalDao;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ApplicationByTerminal;
import com.honda.galc.entity.conf.ApplicationMenuEntry;
import com.honda.galc.entity.conf.Notification;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ApplicationEnvService;

/**
 * 
 * <h3>NonCachedApplicationContext Class description</h3>
 * <p> NonCachedApplicationContext description </p>
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
 * Aug 2, 2010
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class NonCachedApplicationContext extends ApplicationContext{

	private Terminal terminal;
	
	private List<ProductTypeData> productTypeDataList;
	
	public static ApplicationContext create(Arguments args) {
		applicationContext = new NonCachedApplicationContext(args, getApplicationId(args));
		return applicationContext;
	}
	
	public static ApplicationContext create(Arguments args, String applicationId) {
		applicationContext = new NonCachedApplicationContext(args, applicationId);
		return applicationContext;
	}

	private static String getApplicationId(Arguments args) {
		Terminal terminal = getDao(TerminalDao.class).findByKey(args.getHostName());
		return StringUtils.trimToEmpty(terminal.getLocatedProcessPointId());
	}
	
	public NonCachedApplicationContext(Arguments args, String applicationId) {
		super(args, applicationId);
	}

	@Override
	public Application getDefaultApplication() {
		ApplicationByTerminal appByTerminal = getDao(ApplicationByTerminalDao.class).findDefaultApplication(getHostName());
		Application app = appByTerminal == null ? null :appByTerminal.getApplication();
		replaceScreenClass(app);
		return app;
	}	
	
	@Override
	public Terminal getTerminal() {
	    if(terminal == null) {
	    	terminal = fetchTerminal();
		    	if(terminal == null) {
				// incorrect terminal name
				String message = "Failed to get terminal data for terminal \"" + getHostName() + "\". Please check the terminal name";
				throw new SystemException(message);
		    }
	    }
		return terminal;
	}

	@Override
	public Application getApplication(String appId) {
		Application app = getDao(ApplicationDao.class).findByKey(appId);
		replaceScreenClass(app);
		return app;
	}

	@Override
	public List<ApplicationMenuEntry> getApplicationMenus() {
		return fetchApplicationMenus();
	}

	@Override
	public Notification getNotification(String name) {
	    return fetchNotification(name);
	}

	@Override
	public List<ProductTypeData> getProductTypeDataList() {
		if(productTypeDataList == null) productTypeDataList = fetchProductTypeData();
		return productTypeDataList;
	}

	@Override
	public String getServerBuildLabel() {
		return getService(ApplicationEnvService.class).getBuildLevel();
	}
	
}
