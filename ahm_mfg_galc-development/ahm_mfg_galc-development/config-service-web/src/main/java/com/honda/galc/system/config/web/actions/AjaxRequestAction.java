package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.product.FeatureDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.product.Feature;
import com.honda.galc.system.config.web.forms.DivisionForm;
import com.honda.galc.system.config.web.forms.ProcessPointForm;

public class AjaxRequestAction extends ConfigurationDispatchAction {

	public ActionForward populateFeatureId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Ajax had called this method....");
		String featureId = "";
		ProcessPointForm processPointForm = (ProcessPointForm) form;
		String selectedFeatureType = processPointForm.getFeatureType();
		List<String> featureIdList = new ArrayList<String>();
		List<Feature> featureIds= getDao(FeatureDao.class).findAllFeatures(selectedFeatureType);
		for(Feature feature : featureIds) {
			featureIdList.add(feature.getFeatureId());
		}
		featureIdList.add(0, "NONE");

		for(String f : featureIdList) {
			featureId = featureId + "|||" + f;
		}
		featureId = featureId +"|||";
		response.setContentType("text");
		final PrintWriter out = response.getWriter();
		out.println(featureId);
		out.flush();
		return mapping.findForward(null);
	}

	public ActionForward checkDivisionIdExists(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		DivisionForm divisionForm = (DivisionForm) form;
		String selectedDivisionID = divisionForm.getDivisionID();
		Division existingDivision = getDao(DivisionDao.class).findByKey(selectedDivisionID);
		response.setContentType("text");
		final PrintWriter out = response.getWriter();
		if (existingDivision != null) {
			out.println("true," + existingDivision.getDivisionId() + "," + existingDivision.getSiteName() + "," + existingDivision.getPlantName() + "," + divisionForm.getSiteName() + "," + divisionForm.getPlantName());
		} else {
			out.println("false");
		}
		out.flush();
		return mapping.findForward(null);
	}

}
