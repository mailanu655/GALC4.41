package com.honda.galc.dao.jpa.product;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.EngineFiringResultDao;
import com.honda.galc.entity.product.EngineFiringResult;
import com.honda.galc.entity.product.EngineFiringResultId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>EngineFiringResultDaoImpl Class description</h3>
 * <p> EngineFiringResultDaoImpl description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Jul 28, 2011
 *
 *
 */
public class EngineFiringResultDaoImpl extends BaseDaoImpl<EngineFiringResult,EngineFiringResultId> implements EngineFiringResultDao {

    private static final long serialVersionUID = 1L;
    private static final String INERT_FIRING_RESULT = "insert into gal141tbx (engine_serial_no,firing_test_type, associate_no, firing_notes, firing_status, firing_bench_no, actual_timestamp, result_id) " +
    		" values (?1, ?2, ?3, ?4, ?5, ?6, current timestamp,(select coalesce(max(result_id),0)  + 1 from gal141tbx where engine_serial_no = ?1))";

	public List<EngineFiringResult> findAllByProductId(String productId) {
		
		return findAll(Parameters.with("id.engineSerialNo",productId));
		
	}
	
	@Transactional
	public int insert(String productId, String firingTestType, String associateId, String notes, 
			boolean fireResult, int benchId) {
		Parameters params = Parameters.with("1", productId);
		params.put("2", firingTestType);
		params.put("3", associateId);
		params.put("4", notes);
		params.put("5", fireResult ? 1 : 0);
		params.put("6", benchId);
		return executeNativeUpdate(INERT_FIRING_RESULT, params);
	}

}
