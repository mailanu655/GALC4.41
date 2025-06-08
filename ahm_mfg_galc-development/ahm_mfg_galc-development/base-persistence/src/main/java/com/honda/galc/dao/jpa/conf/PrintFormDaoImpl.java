/**
 * 
 */
package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.PrintFormDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.PrintForm;
import com.honda.galc.entity.conf.PrintFormId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * Sep 12, 2011
 */
public class PrintFormDaoImpl extends BaseDaoImpl<PrintForm, PrintFormId> implements PrintFormDao {

	private static final long serialVersionUID = 1L;
	
	private final String FIND_DISTINCT_FORMS = "select distinct a.id.formId from PrintForm a order by a.id.formId";

    private final String UPDATE_DESTINATION_ID = 
    	"update PrintForm a set a.id.destinationId = :destinationId where a.id.formId = :formId and a.id.destinationId = :oldDestinationId";
	/**
	 * 
	 */
	public List<PrintForm> findByDesitnationId(String destination) {
		return findAll(Parameters.with("id.destinationId", destination));
	}

	/**
	 * 
	 */
	public List<PrintForm> findByFormId(String formId) {
		return findAll(Parameters.with("id.formId", formId));
	}

	public List<String> findDistinctForms() {
		return findByQuery(FIND_DISTINCT_FORMS, String.class);
	}

	@Transactional
	public int updateDestinationId(String formId, String oldDestinationId,String destinationId) {
		Parameters params = Parameters.with("formId", formId)
			.put("oldDestinationId", oldDestinationId)
			.put("destinationId", destinationId);
		
		return executeUpdate(UPDATE_DESTINATION_ID, params);
	}
}
