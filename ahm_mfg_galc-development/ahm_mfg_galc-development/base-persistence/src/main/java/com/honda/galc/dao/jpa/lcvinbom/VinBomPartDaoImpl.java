package com.honda.galc.dao.jpa.lcvinbom;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.lcvinbom.VinBomPartDao;
import com.honda.galc.dto.lcvinbom.VinBomPartDto;
import com.honda.galc.entity.lcvinbom.VinBomPart;
import com.honda.galc.entity.lcvinbom.VinBomPartId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.ProductSpecUtil;
import com.honda.galc.util.StringUtil;

public class VinBomPartDaoImpl extends BaseDaoImpl<VinBomPart, VinBomPartId> 
implements VinBomPartDao {

	private final String FIND_ALL_BY_FILTER = "select p from VinBomPart p where "
			+ "UPPER(p.id.productSpecCode) like :specCodePrefix "
			+ "and UPPER(p.id.letSystemName) like :letSysName "
			+ "and UPPER(p.id.dcPartNumber) like :dcPartNo";

	private final String FIND_ALL_BY_FILTER_NATIVE = "select p.*, substr(p.PRODUCT_SPEC_CODE, 1, 1) as MODEL_YEAR_CODE, "
			+ "substr(p.PRODUCT_SPEC_CODE, 2, 3) as MODEL_CODE, substr(p.PRODUCT_SPEC_CODE, 5, 3) as MODEL_TYPE_CODE, "
			+ "CASE " + 
			"   WHEN m.ACTIVE = 1 THEN 'ACTIVE' " + 
			"   WHEN m.ACTIVE = 0 THEN 'INACTIVE' " + 
			"   WHEN m.ACTIVE = 2 THEN 'DEPRECATED' " + 
			"   ELSE '*NOT SET*' " + 
			"  END AS ACTIVE from lcvinbom.PART p "
			+ "left outer join lcvinbom.MODEL_PART m "
			+ "on p.PRODUCT_SPEC_CODE = m.PRODUCT_SPEC_WILDCARD "
			+ "and p.LET_SYSTEM_NAME = m.LET_SYSTEM_NAME "
			+ "and substr(p.DC_PART_NUMBER, 1, 15) = m.DC_PART_NUMBER "
			+ "where UPPER(p.PRODUCT_SPEC_CODE) like ?1 "
			+ "and UPPER(p.LET_SYSTEM_NAME) like ?2 "
			+ "and UPPER(p.DC_PART_NUMBER) like ?3";

	
	private final String FIND_SYS_NAME_BY_PART_NO = "select distinct p from VinBomPart p " +
			"where p.id.dcPartNumber like :dcPartNo and p.id.productSpecCode like :productSpecCode " + 
			"ORDER BY p.effectiveBeginDate DESC";

	private final String FIND_DIST_LET_SYS_NAME = "select distinct p.id.letSystemName from VinBomPart p ORDER BY p.id.letSystemName";
	
	private static final String FIND_SYSTEM_NAMES_BY_PRODUCTSPEC_PARTNUMBER = "select p.id.letSystemName from VinBomPart p where UPPER(p.id.productSpecCode) like :specCodePrefix and p.id.dcPartNumber = :dcPartNo @PART_NOT_LIKE@";
	
	private static final String FIND_SYSTEM_NAMES_BY_PRODUCTSPEC = "select distinct p.id.letSystemName from VinBomPart p where UPPER(p.id.productSpecCode) like :specCodePrefix ";
	
	private static final String FIND_SHIP_STATUS_BY_PRODUCTID =" WITH TEMP1 (let_system_name, shipping_status) AS (select distinct a.let_system_name,"
			+" CASE WHEN (select count(*) from lcvinbom.VIN_PART b where a.PRODUCT_ID=b.PRODUCT_ID and a.LET_SYSTEM_NAME=b.LET_SYSTEM_NAME and b.SHIP_STATUS = 1)>0 THEN 'Y'"
			+" ELSE 'N' END AS SHIPPING_STATUS from lcvinbom.VIN_PART a where a.PRODUCT_ID = ?1)"
			+" select let_system_name from TEMP1 WHERE shipping_status = 'N'"; 

	@Override
	public List<VinBomPartDto> findAllByFilter(VinBomPartDto vinBomPartDtoFilter) {
		if(vinBomPartDtoFilter!=null) {
			String paramProductSpecCode = 
					(StringUtils.isNotBlank(vinBomPartDtoFilter.getModelYearCode()))
					?StringUtils.upperCase(StringUtils.trim(vinBomPartDtoFilter.getModelYearCode()))
							:"_";

					paramProductSpecCode += (StringUtils.isNotBlank(vinBomPartDtoFilter.getModelCode()))
							?StringUtils.upperCase(StringUtils.stripEnd(vinBomPartDtoFilter.getModelCode(), null))
									:"___";

							paramProductSpecCode += (StringUtils.isNotBlank(vinBomPartDtoFilter.getModelTypeCode()))
									?StringUtils.upperCase(StringUtils.stripEnd(vinBomPartDtoFilter.getModelTypeCode(), null))
											:"___";

									paramProductSpecCode += "%";

									String paramLetSystemName = "%"+StringUtils.upperCase(StringUtils.trim(vinBomPartDtoFilter.getLetSystemName()))+"%";
									String paramDcPartNumber = "%"+StringUtils.upperCase(StringUtils.trim(vinBomPartDtoFilter.getDcPartNumber()))+"%";

									Parameters params = Parameters.with("specCodePrefix", paramProductSpecCode).put("letSysName", paramLetSystemName)
											.put("dcPartNo", paramDcPartNumber);
									return constructVinBomPartDto(findAllByQuery(FIND_ALL_BY_FILTER, params));
		}
		return constructVinBomPartDto(findAll());
	}

	@Override
	public String getSystemNameByPartNumber(String dcPartNumber,String productSpecCode) {
		Parameters params = Parameters.with("dcPartNo", "%"+dcPartNumber+"%");
		params.put("productSpecCode", "%"+productSpecCode+"%");
		VinBomPart vinBomPart = (VinBomPart)findFirstByQuery(FIND_SYS_NAME_BY_PART_NO, params);
		return (vinBomPart != null) ? vinBomPart.getId().getLetSystemName() : "";
	}

	@Override
	public List<VinBomPart> findDistinctPartNumberBySystemName(String letSystemName) {
		return findAll(Parameters.with("id.letSystemName", letSystemName));
	}

	@Override
	public List<String> findDistinctLetSystemName() {
		return findByQuery(FIND_DIST_LET_SYS_NAME, String.class);
	}

	private List<VinBomPartDto> constructVinBomPartDto(List<VinBomPart> vinBomPartList) {
		List<VinBomPartDto> vinBomPartDtoList = new ArrayList<VinBomPartDto>();
		if (vinBomPartList!=null && !vinBomPartList.isEmpty()) {
			for(VinBomPart vinBomPart: vinBomPartList) {
				vinBomPartDtoList.add(constructVinBomPartDto(vinBomPart));
			}
		}
		return vinBomPartDtoList;
	}

	private VinBomPartDto constructVinBomPartDto(VinBomPart vinBomPart) {
		VinBomPartDto vinBomPartDto = new VinBomPartDto();
		if(vinBomPartDto!=null) {
			vinBomPartDto.setProductSpecCode(vinBomPart.getId().getProductSpecCode());
			vinBomPartDto.setLetSystemName(vinBomPart.getId().getLetSystemName());
			vinBomPartDto.setDcPartNumber(vinBomPart.getId().getDcPartNumber());;
			vinBomPartDto.setModelYearCode(ProductSpecUtil.extractModelYearCode(vinBomPart.getId().getProductSpecCode()));
			vinBomPartDto.setModelCode(ProductSpecUtil.extractModelCode(vinBomPart.getId().getProductSpecCode()));
			vinBomPartDto.setModelTypeCode(ProductSpecUtil.extractModelTypeCode(vinBomPart.getId().getProductSpecCode()));
			vinBomPartDto.setBasePartNumber(vinBomPart.getBasePartNumber());
		
			vinBomPartDto.setDescription(vinBomPart.getDescription());
			vinBomPartDto.setEffectiveBeginDate(vinBomPart.getEffectiveBeginDate());
			vinBomPartDto.setEffectiveEndDate(vinBomPart.getEffectiveEndDate());
		}
		return vinBomPartDto;
	}
	

	
	public List<String> getSystemNamesByProductSpecPartNumber(String productSpec,String dcPartNumber,List<String> sysNameToExclude) {
		Parameters parameters = Parameters.with("specCodePrefix", productSpec+"%");
		parameters.put("dcPartNo", dcPartNumber);
		
		StringBuilder sb = new StringBuilder();
		for (String s : sysNameToExclude) {
			if (!StringUtil.isNullOrEmpty(s)) {
				sb.append(" AND ");
				sb.append("p.id.letSystemName" + " NOT LIKE ");
				sb.append("'%");
				sb.append(StringUtils.trim(s));
				sb.append("%'");
			}
		}
		
		String sql = FIND_SYSTEM_NAMES_BY_PRODUCTSPEC_PARTNUMBER.replace("@PART_NOT_LIKE@", sb.toString());
		
		return findResultListByQuery(sql, parameters);
	}
	
	public List<String> getSystemNamesByProductSpec(String productSpec) {
		Parameters parameters = Parameters.with("specCodePrefix", productSpec+"%");
		return findResultListByQuery(FIND_SYSTEM_NAMES_BY_PRODUCTSPEC, parameters);
	}
	
	@Override
	public List<String> getShipStatusByProductId(String productId) {
		Parameters parameters = Parameters.with("1", productId);
		return findResultListByNativeQuery(FIND_SHIP_STATUS_BY_PRODUCTID, parameters);
	}
	
	public List<VinBomPart> findDistinctPartNumberBySystemNameAndProductSpec(String letSystemName, String productSpec) {
		Parameters parameters = Parameters.with("id.letSystemName", letSystemName);
		parameters.put("id.productSpecCode", productSpec);
		return findAll(parameters);
	}

	@Override
	public List<VinBomPartDto> findAllByFilterNative(VinBomPartDto vinBomPartDtoFilter) {
		if(vinBomPartDtoFilter!=null) {
			String paramProductSpecCode = 
					(StringUtils.isNotBlank(vinBomPartDtoFilter.getModelYearCode()))
					?StringUtils.upperCase(StringUtils.trim(vinBomPartDtoFilter.getModelYearCode()))
							:"_";

					paramProductSpecCode += (StringUtils.isNotBlank(vinBomPartDtoFilter.getModelCode()))
							?StringUtils.upperCase(StringUtils.stripEnd(vinBomPartDtoFilter.getModelCode(), null))
									:"___";

							paramProductSpecCode += (StringUtils.isNotBlank(vinBomPartDtoFilter.getModelTypeCode()))
									?StringUtils.upperCase(StringUtils.stripEnd(vinBomPartDtoFilter.getModelTypeCode(), null))
											:"___";

									paramProductSpecCode += "%";

									String paramLetSystemName = "%"+StringUtils.upperCase(StringUtils.trim(vinBomPartDtoFilter.getLetSystemName()))+"%";
									String paramDcPartNumber = "%"+StringUtils.upperCase(StringUtils.trim(vinBomPartDtoFilter.getDcPartNumber()))+"%";

									Parameters params = Parameters.with("1", paramProductSpecCode).put("2", paramLetSystemName)
											.put("3", paramDcPartNumber);
									return findAllByNativeQuery(FIND_ALL_BY_FILTER_NATIVE, params,VinBomPartDto.class);
		}
		return constructVinBomPartDto(findAll());
	}
}