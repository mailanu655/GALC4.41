package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.FrameEngineModelMapDao;
import com.honda.galc.entity.product.FrameEngineModelMap;
import com.honda.galc.entity.product.FrameEngineModelMapId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.Parameters;

public class FrameEngineModelMapDaoImpl extends BaseDaoImpl<FrameEngineModelMap, FrameEngineModelMapId> implements FrameEngineModelMapDao{
	public static final String FRM_YEAR = "id.frmModelYearCode";
	public static final String FRM_MODEL = "id.frmModelCode";
	public static final String FRM_TYPE = "id.frmModelTypeCode";
	public static final String FRM_OPTION = "id.frmModelOptionCode";
	public static final String ENG_YEAR = "id.engModelYearCode";
	public static final String ENG_MODEL = "id.engModelCode";
	public static final String ENG_TYPE = "id.engModelTypeCode";
	public static final String ENG_OPTION = "id.engModelOptionCode";
	
	public List<FrameEngineModelMap> findAll() {		
		return this.findAll(null, new String[] {FRM_YEAR, FRM_MODEL, FRM_TYPE, FRM_OPTION,
												ENG_YEAR, ENG_MODEL, ENG_TYPE, ENG_OPTION}, true);
	}
	
	public List<FrameEngineModelMap> findAllByFrameYmto(String frmYmto){
		if(StringUtils.isBlank(frmYmto)) return new ArrayList<FrameEngineModelMap>();
		String year = ProductSpec.extractModelYearCode(frmYmto);
		String model = ProductSpec.extractModelCode(frmYmto);
		String type = ProductSpec.extractModelTypeCode(frmYmto);
		String option = ProductSpec.extractModelOptionCode(frmYmto);
		return findAllByFrameYmto(year, model,type, option);
	}
	
	public List<FrameEngineModelMap> findAllByFrameYmto(String frmYear, String frmModel, String frmType, String frmOption){
		Parameters params = new Parameters(); 
		if(StringUtils.isBlank(frmYear) || StringUtils.isBlank(frmModel) || StringUtils.isBlank(frmType) || StringUtils.isBlank(frmOption)) return new ArrayList<FrameEngineModelMap>();
		params.put(FRM_YEAR, frmYear);
		params.put(FRM_MODEL, frmModel);
		params.put(FRM_TYPE, frmType);
		params.put(FRM_OPTION, frmOption);
		String[] orderBy = {FRM_YEAR, FRM_MODEL, FRM_TYPE, FRM_OPTION};
		return findAll(params, orderBy, true);
	}
}
