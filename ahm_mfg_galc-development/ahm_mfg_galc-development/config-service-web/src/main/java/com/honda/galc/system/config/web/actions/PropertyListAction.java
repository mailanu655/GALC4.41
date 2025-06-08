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

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.system.config.web.forms.PropertyListForm;

/**
 * @version 	1.0
 * @author
 */
public class PropertyListAction extends ConfigurationAction

{

    /**
     * Constructor
     */
    public PropertyListAction() {
	super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	ActionErrors errors = new ActionErrors();
	
	PropertyListForm propertyListForm = (PropertyListForm)form;

	try {

	    List<ComponentProperty> result = getDao(ComponentPropertyDao.class).findAllByComponentId(propertyListForm.getComponentID());
	    propertyListForm.setPropertyDataList(result);

	} catch (Exception e) {

	    // Report the error using the appropriate name and ID.
	    errors.add("name", new ActionError("id"));

	}

    // forward failure or success depending on errors
    return forward(mapping,request,errors);

    }
}
