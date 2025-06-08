package com.honda.galc.dao.product;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.product.BearingSelectResult;
import com.honda.galc.entity.product.BearingSelectResultId;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>IPPTagDao Class description</h3>
 * <p> IPPTagDao description </p>
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
 * Jun 3, 2012
 *
 *
 */
public interface BearingSelectResultDao extends IDaoService<BearingSelectResult, BearingSelectResultId> {

	public List<BearingSelectResult> findAllByProductId(String productId);
	public BearingSelectResult findByProductId(String productId);
	public void save(BearingSelectResult bearingSelectResult, List<InstalledPart> installedParts);
	public List<HashMap<String, Object>> getBearingUsageData(DefaultDataContainer dc) throws ParseException;
}
