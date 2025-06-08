package com.honda.galc.dao.product;
import java.util.List;

import com.honda.galc.entity.product.EngineManifest;
import com.honda.galc.entity.product.EngineManifestId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>EngineManifestDao</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineManifestDao description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Mar 17, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 17, 2017
 */
public interface EngineManifestDao extends IDaoService<EngineManifest, EngineManifestId> {

	/**
	 * find all Engine Manifest by engine numbers and plant
	 * @param plant
	 * @param engines
	 * @param status
	 */
	public List<EngineManifest> findAllByEngineList(String plant, List<String> engines);

}
