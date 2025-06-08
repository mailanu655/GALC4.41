package com.honda.galc.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.honda.galc.service.property.PropertyService;

/**
 * <h3>PropertyRefreshServlet/h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>This servlet is called by the RefreshComponentProperties.jsp to do the refreshing of 
 * the specified component's properties.</p>
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
 * <TD>Apr 28, 2004</TD>
 * <TD>insert version</TD>
 * <TD>@OIM282.3</TD>
 * <TD>Created in order to support a factory pattern for 
 * property refresh helper objects.</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Jul 14, 2004</TD>
 * <TD>1.0</TD>
 * <TD>OIM71</TD>
 * <TD>Added server identifier information to display, additional functions</TD>
 * </TR>
 * </TABLE>
 * 
 * @author martinek
 *
 * 
 */
public class RefreshPropertiesServlet extends HttpServlet
{
	
	private static final long serialVersionUID = 1L;
	
	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		processRequest(req, resp);
	}

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		processRequest(req, resp);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void processRequest(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		String componentID = req.getParameter("componentID");
		PrintWriter out = new PrintWriter(resp.getOutputStream()); 
		String messageText=null;
		
		if(componentID != null) {
			PropertyService.refreshComponentProperties(componentID);
			messageText="componentID=" + componentID + " refresh successful.";
		} else {
			messageText="componentID=" + componentID + " refresh failed!";
			messageText += "Please that the verify request url is correct. e.g http://<host>:<port>/Baseweb/RefreshProperties&componentID=PP10240";
		}
		
		String response=String.format("<HTML><HEAD></HEAD><BODY><CODE>%s</CODE></BODY></HTML>",messageText);
		out.println(response);
		out.flush();

	}

	/**
	* @see javax.servlet.GenericServlet#void ()
	*/
	public void init(ServletConfig config) throws ServletException
	{

		super.init(config);

	}

}
