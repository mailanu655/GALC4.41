package com.honda.galc.servlet;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FeatureDao;
import com.honda.galc.entity.product.Feature;

/**
 * Servlet implementation class AjaxRequestServlet
 */
public class AjaxRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Logger logger;
	
	private static final String WEB_START = "Web Start";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxRequestServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String featureType = request.getParameter("feature_type");
		String featureId = "";
		List<String> featureIdList = new ArrayList<String>();
    	List<Feature> featureIds= getDao(FeatureDao.class).findAllFeatures(featureType);
    	for(Feature feature : featureIds) {
    		featureIdList.add(feature.getFeatureId());
    	}
    	featureIdList.add(0, "NONE");
    	for(String f : featureIdList) {
    		featureId = featureId + "|||" + f;
    	}
    	featureId = featureId +"|||";
    	response.setContentType("text/plain");
    	response.getWriter().write(featureId);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("add_client.jsp");
		if (dispatcher != null) {
			try {
				dispatcher.include(request, response);
			} catch (ServletException e) {
				getLogger().error(e, "Exception in forwardTo "  + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				getLogger().error(e, "Exception in forwardTo "  + e.getMessage());
				e.printStackTrace();
			}
		} else {
			try {
				response.sendRedirect("web_start_home.jsp");
			} catch (IOException e) {
				getLogger().error(e, "Unable to redirect response to web_start_home.jsp");
			}
		}
		
		
	}

	
	private Logger getLogger() {
		if(logger == null) logger = Logger.getLogger(WEB_START);
		return logger;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
