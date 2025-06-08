package com.honda.galc.dao.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.dto.ValidAvailableSpecForALtMto;
import com.honda.galc.entity.product.FrameSpec;


/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface FrameSpecDao extends ProductSpecDao<FrameSpec, String> {

    public List<String> findAllModelYearCodes();
    
    public List<String> findAllProductSpecCodes();
    
    public List<FrameSpec> findAllByModelYearCode(String yearCode);
    
    public List<FrameSpec> findAllByProcessPointId(String processPointId);

	public List<FrameSpec> findAllByPrefix(String prefix);
	
	public FrameSpec findTCUData(String productId);
	
	public Map<String, String> findAllModelCodeYear();
	
	public List<FrameSpec> findAllByYMTOCWildCard(String modelYear,String modelCode,String modelType,String modelOption,String extColor,String intColor);
	
	public List<FrameSpec> findAllByMTOCWildCard(String modelCode,String modelType,String modelOption,String extColor,String intColor);
	
	public List<ValidAvailableSpecForALtMto> findAllByAltEngineMto(String altProductSpec);
	
	public List<String> findModelYearCodes();
	
	public List<String> findModelCodes(List<String> modelYearCodes);
	
	public List<FrameSpec> findAllAfterModelYearDescription(String modelYearDescription);
	
	public List<String> findAllColorCodesAfterModelYearDescription(String modelYearDescription);
	
	public List<FrameSpec> findAllActiveProductSpecCodesOnly(String productType);
	
	public List<String> findDistinctFramePrefix();
	
	public List<FrameSpec> findAllBySalesModelTypeExtColor(String salesModelCode, String salesModelTypeCode, String salesExtColorCode);
	
	public List<FrameSpec> findAllBySalesCommonModelTypeExtColor(String commonSalesModelCode, String salesModelTypeCode, String salesExtColorCode);

	public List<FrameSpecDto> getProductIdDetails(String productId);
	
	public List<FrameSpec> getColorDetails(String productSpecCode);

	public FrameSpec getFrameDetails(FrameSpecDto selectedFrameSpecDto);
	
	public void updateColorDetails(String productCodeForUpdate, FrameSpecDto selectedFrameSpecDto, List<String> productionLots);

	public void updateFrameSpecCode(FrameSpec frameSpec, String oldSpecCode);
	
}
