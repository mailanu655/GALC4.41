package com.honda.galc.system.config.web.forms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.conf.RegionalCode;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jul. 10, 2018</TD>
 * <TD>1.0</TD>
 * <TD>20180710</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 *
 */

public class RegionalCodeListForm extends ActionForm {
    
	private static final long serialVersionUID = 4452974667920026257L;

	private List<RegionalCode> regionalCodes = null;
    private String searchText = null; 
    
  
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return new ActionErrors();
    }
    
    public String getSearchText() {
        return searchText;
    }
    
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    
	public List<RegionalCode> getRegionalCodes() {
		return regionalCodes;
	}

	public void setRegionalCodes(List<RegionalCode> regionalCodes) {
		this.regionalCodes = regionalCodes;
	}
}
