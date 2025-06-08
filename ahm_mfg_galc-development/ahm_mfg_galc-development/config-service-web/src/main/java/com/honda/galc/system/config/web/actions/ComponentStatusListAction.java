package com.honda.galc.system.config.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.system.config.web.forms.ComponentStatusListForm;

public class ComponentStatusListAction extends ConfigurationAction {

	public ComponentStatusListAction() {
		super();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionErrors errors = new ActionErrors();
		ComponentStatusListForm componentStatusListForm = (ComponentStatusListForm) form;

		try {
			List<ComponentStatus> result = getDao(ComponentStatusDao.class).findAllByComponentId(componentStatusListForm.getComponentID());
			componentStatusListForm.setComponentStatusDataList(result);
		} catch (Exception e) {
			// Report the error using the appropriate name and ID.
			errors.add("name", new ActionError("id"));
		}
		// forward failure or success depending on errors
		return forward(mapping,request,errors);
	}
}
