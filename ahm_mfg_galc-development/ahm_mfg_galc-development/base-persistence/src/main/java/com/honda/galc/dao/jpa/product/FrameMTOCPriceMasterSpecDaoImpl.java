package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.FrameMTOCPriceMasterSpecDao;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpec;
import com.honda.galc.entity.product.FrameMTOCPriceMasterSpecId;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>FrameMTOCPriceMasterSpecDaoImpl.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FrameMTOCPriceMasterSpecDaoImpl.java description </p>
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
 * <TD>PB</TD>
 * <TD>August 1, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Kenneth Gibson
 * @created August 1, 2014
 */
public class FrameMTOCPriceMasterSpecDaoImpl extends BaseDaoImpl<FrameMTOCPriceMasterSpec,FrameMTOCPriceMasterSpecId> 
implements FrameMTOCPriceMasterSpecDao {

	private static final String SELECT_VIN_PRICE =
			"select p.* from GAL268TBX p, GAL143TBX f where "
					+ " f.product_Id = ?1 and "
					+ " (Select TO_CHAR(MIN(ACTUAL_TIMESTAMP),'YYYYMMDD') "
					+ "  From GAL215TBX tracking "
					+ "  Where f.product_id = tracking.product_id "
					+ "  AND tracking.Process_Point_ID = ?2 ) "
					+ " >= p.EFFECTIVE_DATE and "
					+ " f.Product_Spec_Code =  concat(p.MODEL_YEAR_CODE, "
					+						 " concat(p.MODEL_CODE, "
					+						 " concat(p.MODEL_TYPE_CODE, "
					+ 						 " concat(p.MODEL_OPTION_CODE, "
					+ 						 " concat(p.EXT_COLOR_CODE,p.INT_COLOR_CODE))))) "
					+ " order by p.EFFECTIVE_DATE desc ";

	private static final String GET_MTOC_PRICE =
			"SELECT PRICE FROM GAL268TBX "
					+ "join GAL144TBX gal144tbx on GAL268TBX.PLANT_CODE_FRAME = gal144tbx.PLANT_CODE_GPCS "
					+ "AND GAL268tbx.MODEL_YEAR_CODE = gal144tbx.MODEL_YEAR_CODE "
					+ "AND GAL268tbx.MODEL_CODE = gal144tbx.MODEL_CODE "
					+ "AND GAL268tbx.MODEL_TYPE_CODE = gal144tbx.MODEL_TYPE_CODE "
					+ "AND GAL268tbx.MODEL_OPTION_CODE = gal144tbx.MODEL_OPTION_CODE "
					+ "AND GAL268tbx.EXT_COLOR_CODE = gal144tbx.EXT_COLOR_CODE "
					+ "AND GAL268tbx.INT_COLOR_CODE = gal144tbx.INT_COLOR_CODE "
					+ "AND GAL268tbx.EFFECTIVE_DATE  <= ?2 "
					+ "AND gal144tbx.PRODUCT_SPEC_CODE LIKE ?1 "
					+ "ORDER BY EFFECTIVE_DATE DESC FETCH FIRST 1 ROWS ONLY ";

	private static final String GET_PRICE_ON_PRODUCTION_DATE =
			"SELECT PRICE FROM GAL268TBX "
					+ "join GAL144TBX gal144tbx on GAL268TBX.PLANT_CODE_FRAME = gal144tbx.PLANT_CODE_GPCS "
					+ "AND GAL268tbx.MODEL_YEAR_CODE = gal144tbx.MODEL_YEAR_CODE "
					+ "AND GAL268tbx.MODEL_CODE = gal144tbx.MODEL_CODE "
					+ "AND GAL268tbx.MODEL_TYPE_CODE = gal144tbx.MODEL_TYPE_CODE "
					+ "AND GAL268tbx.MODEL_OPTION_CODE = gal144tbx.MODEL_OPTION_CODE "
					+ "AND GAL268tbx.EXT_COLOR_CODE = gal144tbx.EXT_COLOR_CODE "
					+ "AND GAL268tbx.INT_COLOR_CODE = gal144tbx.INT_COLOR_CODE "
					+ "AND GAL268tbx.EFFECTIVE_DATE  <= ?2 "
					+ "AND gal144tbx.PRODUCT_SPEC_CODE = ?1 "
					+ "ORDER BY EFFECTIVE_DATE DESC FETCH FIRST 1 ROWS ONLY ";

	@Transactional
	public FrameMTOCPriceMasterSpec findByProductIdAndSpecCode(String vin, String processPoint60A) {
		Parameters p = Parameters.with("1", vin);
		p.put("2", processPoint60A);
		FrameMTOCPriceMasterSpec price = findFirstByNativeQuery(SELECT_VIN_PRICE, p);
		return price;
	}

	/**
	 * @author Vidya Chitta
	 * @date Nov 14,2018
	 * 
	 * Queries the Database with a query to get the last updated price of a product 
	 * 
	 * @return		the last updated price of the product
	 */
	@Override
	public String getMTOCPrice( String mtoc, Integer effectiveDate) {
		Parameters parameters = Parameters.with("1",mtoc + "%").put("2", effectiveDate);
		@SuppressWarnings("unchecked")
		List<String> prices = findResultListByNativeQuery(GET_MTOC_PRICE, parameters,1);
		return prices != null && prices.size() > 0 ? prices.get(0) : null;
	}

	@Override
	public String getPriceForProductionDate(String productSpecCode, String dateStr) {
		Parameters parameters = Parameters.with("1", productSpecCode);
		parameters.put("2", dateStr);
		return findFirstByNativeQuery(GET_PRICE_ON_PRODUCTION_DATE,parameters,String.class);
	}
	
	public FrameMTOCPriceMasterSpec getFrameMTOCPriceMasterSpec(FrameSpecDto selectedFrameSpecDto) {
		
		Parameters parameters = Parameters.with("id.modelYearCode", selectedFrameSpecDto.getModelYearCode()).put("id.modelCode", selectedFrameSpecDto.getModelCode())
				.put("id.modelTypeCode", selectedFrameSpecDto.getModelTypeCode()).put("id.modelOptionCode", selectedFrameSpecDto.getModelOptionCode())
				.put("id.extColorCode", selectedFrameSpecDto.getExtColorCode());
		
		List<FrameMTOCPriceMasterSpec> frameMTOCProceMasterSpecList  = findAll(parameters);
		if(frameMTOCProceMasterSpecList.size()>=1) {
			return frameMTOCProceMasterSpecList.get(0);
		}
		return null;
	}
}
