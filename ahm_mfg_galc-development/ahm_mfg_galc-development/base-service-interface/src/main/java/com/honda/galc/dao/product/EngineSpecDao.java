package com.honda.galc.dao.product;

import java.util.List;
import java.util.Map;

import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>EngineSpecDao Class description</h3>
 * <p> EngineSpecDao description </p>
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
 * Jun 19, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface EngineSpecDao extends ProductSpecDao<EngineSpec, String> {

    public List<String> findAllModelYearCodes();
    
    public List<String> findAllProductSpecCodes();
    
    /**
     * only populate the product spec code and individual year code, type code etc
     * to save query time
     * @return
     */
    public List<EngineSpec> findAllProductSpecCodesOnly(String productType);
    
    public List<EngineSpec> findAllByModelYearCode(String yearCode);
    
    public List<EngineSpec> findAllByProcessPointId(String processPointId);

	public List<EngineSpec> findAllByPrefix(String prefix);
	
	public List<Map<String, String>> findAllYearModelCodes();
	
	public List<String> findModelTypeCodes(String modelYearCode, String modelCode);
	
	public Map<String, String> findAllModelCodeYear();
	
	public List<Object[]> findEngineHostMto(int start, int end);
	public void updateEngineSpecCode(EngineSpec engineSpec, String oldSpecCode);
}
