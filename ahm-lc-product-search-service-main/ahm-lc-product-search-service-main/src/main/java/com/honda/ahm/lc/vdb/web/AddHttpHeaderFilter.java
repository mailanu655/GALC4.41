package com.honda.ahm.lc.vdb.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.honda.ahm.lc.config.AsyncConfiguration;
import com.honda.ahm.lc.config.TenantContext;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>AddHttpHeaderFilter</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Apr 22, 2021
 */
@Component
@WebFilter(urlPatterns = { "/*" })
public class AddHttpHeaderFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddHttpHeaderFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
		httpServletResponse.setHeader("*", "*");
		try {

			String requestURI = httpRequest.getRequestURI();
			String[] pathParts = requestURI.split("/");
			if (pathParts.length >= 3) {
				String tenantId = pathParts[1];
				TenantContext.setCurrentTenant(tenantId);
			}
			chain.doFilter(request, response);

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//e.printStackTrace();
		} finally {
			TenantContext.clear();
		}
	}
}
