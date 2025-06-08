package com.honda.galc.web;

import java.io.IOException;

import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.management.AdminService;
import com.ibm.websphere.management.AdminServiceFactory;


/**
 * <h3>JMXStatusServlet</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>This servlet is used to display configuration status information.</p>
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
 * <TD>martinek</TD>
 * <TD>Oct 5, 2005</TD>
 * <TD>insert version</TD>
 * <TD>@JM081</TD>
 * <TD>Add support for looking up the database information.</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Feb 22, 2006</TD>
 * <TD></TD>
 * <TD>@JM093</TD>
 * <TD>Only access database data when required</TD>
 * </TR>
 * </TABLE>
 *
 */
public class JMXStatusServlet extends HttpServlet
{
	
	private static final long serialVersionUID = 1L;
   
	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		processRequest(req,resp);
	}

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		processRequest(req,resp);
	}

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String errorMessage = null;
		String resultMessage = null;
		String cellName = null;
		String serverName = null;
		String nodeName = null;
		String processName = null;
		
		String outputPage = "default";
		
		outputPage = req.getParameter("outputPage");
		if (outputPage == null) outputPage = "default";
		
		String delayParm = req.getParameter("delay");
		if (delayParm != null)
		{
			try {
				long delay = Long.parseLong(delayParm);
				Thread.sleep(delay);
			}
			catch (Exception e)
			{
				
			}
		}
		
		try {
		
		   AdminService adminService = AdminServiceFactory.getAdminService();
		   
		   // Store the cell name
		   cellName = adminService.getCellName();
           
		   ObjectName localServerObjectName = adminService.getLocalServer();
		   
		   // Store the server name and node name
		   serverName = localServerObjectName.getKeyProperty("name");
		   nodeName = localServerObjectName.getKeyProperty("node"); 
		   processName = localServerObjectName.getKeyProperty("process");
		   
		   resultMessage = "Server properties retrieved from "+localServerObjectName.getKeyProperty("mbeanIdentifier");
		}
		catch (Exception e)
		{
			errorMessage = e.toString();
		}
		
		if (errorMessage != null) req.setAttribute("jmxstatus.errorMessage",errorMessage);
		
		if (resultMessage != null) req.setAttribute("jmxstatus.resultMessage",resultMessage);
		
		if (cellName != null) req.setAttribute("jmxstatus.cellName",cellName);
		
		if (nodeName != null) req.setAttribute("jmxstatus.nodeName",nodeName);
		
		if (serverName != null) req.setAttribute("jmxstatus.serverName",serverName);
		
		if (processName != null) req.setAttribute("jmxstatus.processName",processName);
		
		if (outputPage != null && outputPage.equalsIgnoreCase("cellname"))
			req.getRequestDispatcher("/JMXCellName.jsp").forward(req,resp);
		else req.getRequestDispatcher("/JMXStatus.jsp").forward(req,resp);
		
		return;
	}


}
