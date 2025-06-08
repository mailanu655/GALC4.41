package com.honda.galc.web;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.honda.galc.common.exception.BaseException;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.logging.LogContext;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.logging.ServerSideLoggerConfig;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.property.JpaServerPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>HttpServiceHandler Class description</h3>
 * <p> HttpServiceHandler description </p>
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
 * Oct 24, 2010
 *
 *
 */

public abstract class AbstractHttpServiceHandler extends HttpServlet{
	private static final long serialVersionUID = 1L;
	protected JpaServerPropertyBean propertyBean;

	protected abstract String getHandlerId();

	public void init(javax.servlet.ServletConfig config) throws ServletException {

		super.init(config);

		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		ApplicationContextProvider.setApplicationContext(ctx); 

		//allow log4j to load the log4j.property dynamically based on the machine host name or preferred suffix
		ServerSideLoggerConfig.configLog4j();

		propertyBean = getPropertyBean();

		initLogContext();

		getLogger().info("Request execution time warning threshold is set to " + propertyBean.getExecutionTimeWarningThreshold() + "ms");

		getLogger().info("Request result list warning threshold is set to " + propertyBean.getResultListWarningThreshold());

	}

	protected void initLogContext(){
		LogContext context = LogContext.getContext();
		context.setApplicationName(getHandlerId());
		context.setApplicationLogLevel(getLogLevel());
		context.setMultipleLine(propertyBean.isLogMultipleLine());
		System.out.println("Current Log Level for Base Web " + context.getApplicationLogLevel());
	}

	protected JpaServerPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(JpaServerPropertyBean.class, getHandlerId());
	}

	protected LogLevel getLogLevel() {

		return LogLevel.valueOf(propertyBean.getLogLevel());

	}

	protected Throwable translateException(Exception e) {
		Throwable obj = e;
		if(e instanceof InvocationTargetException) {
			Throwable throwable = e.getCause();
			// directly send out application defined exception
			// translate all other exceptions (system, jpa etc)
			if(throwable instanceof BaseException) obj = throwable;
			else {
				obj = new ServiceInvocationException(throwable.toString(), throwable);
				obj.setStackTrace(throwable.getStackTrace());
			}
		}

		return obj;
	}

	protected Logger getLogger(){
		return Logger.getLogger(getHandlerId());
	}


}
