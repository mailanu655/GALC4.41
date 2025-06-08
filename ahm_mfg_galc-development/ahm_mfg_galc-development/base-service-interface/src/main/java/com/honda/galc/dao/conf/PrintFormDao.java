/**
 * 
 */
package com.honda.galc.dao.conf;

import java.util.List;


import com.honda.galc.entity.conf.PrintForm;
import com.honda.galc.entity.conf.PrintFormId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * Sep 12, 2011
 */
public interface PrintFormDao extends IDaoService<PrintForm, PrintFormId> {

	public List<PrintForm> findByDesitnationId(String destination);
	
	public List<PrintForm> findByFormId(String formId);
	
	public List<String> findDistinctForms();
	
	public int updateDestinationId(String formId,String oldDestinationId,String destinationId);
}
