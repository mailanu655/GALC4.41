/**
 * 
 */
package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.HoldReason;
import com.honda.galc.service.IDaoService;

/**
 * @author hat0926
 *
 */
public interface HoldReasonDao extends IDaoService<HoldReason, Integer>{
	public List<HoldReason> findAllByDivisionId(String reasonId);
	public HoldReason findByHoldReason(String holdReason);
	public HoldReason findByReasonId(String reasonId);
}
