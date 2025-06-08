package com.honda.galc.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.honda.galc.constant.PartValidity;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class BomPartUtil {
	
	public static PartValidity findPartValidity(String productType, BaseProductSpec productSpec, MCOperationPartRevision operationPart) {
		if(ProductTypeUtil.isMbpnProduct(productType)) {
			//Skipping Part Effective Logic for MBPN Product
			return PartValidity.UNDEFINED;
		}
		else if(productSpec != null && productSpec instanceof ProductSpec) {
			// Product Spec code has YMTOC information.
			// Check Part validity
			if(operationPart!=null && StringUtils.isNotBlank(operationPart.getPartNo())
					&& StringUtils.isNotBlank(operationPart.getPartSectionCode())
					&& StringUtils.isNotBlank(operationPart.getPartItemNo())) {
				//Fetching year, model, type, option, color from product spec code
				ProductSpec ymtoc = ((ProductSpec)productSpec);
				String mtcModel = ymtoc.getModelYearCode()+ymtoc.getModelCode();
				String mtcType = ymtoc.getModelTypeCode();
				
				if(StringUtils.isNotBlank(mtcModel) && StringUtils.isNotBlank(mtcType)) {
					//mtcModel and mtcType are at least required to fetch BOM parts
					String mtcOption = ymtoc.getModelOptionCode();
					mtcOption = (StringUtils.isBlank(mtcOption) || ProductSpec.isWildCard(mtcOption))?"":mtcOption;
					
					String mtcColor = ymtoc.getExtColorCode();
					mtcColor = (StringUtils.isBlank(mtcColor) || ProductSpec.isWildCard(mtcColor))?"":mtcColor;
					
					String intColorCode = ymtoc.getIntColorCode();
					intColorCode = (StringUtils.isBlank(intColorCode) || ProductSpec.isWildCard(intColorCode))?"":intColorCode;
					
					List<Bom> partList = ServiceFactory.getDao(BomDao.class).getPartList(operationPart.getPartNo(), 
							operationPart.getPartSectionCode(), operationPart.getPartItemNo(), mtcModel, mtcType, 
							mtcOption, mtcColor, intColorCode);
					
					return findPartValidity(partList);
				}
			}
		}
		return PartValidity.UNDEFINED;
	}
	
	public static PartValidity findPartValidity(List<Bom> partList) {
		PartValidity partValidity = PartValidity.UNDEFINED;
		if(partList!=null && !partList.isEmpty()) {
			for(Bom bomPart: partList) {
				if(bomPart!=null && bomPart.getId().getEffBegDate()!=null 
						&& bomPart.getEffEndDate()!=null) {
					partValidity = checkValidity(bomPart.getId().getEffBegDate(), bomPart.getEffEndDate());
					if(partValidity.equals(PartValidity.VALID)) {
						//return if any of the BOM part is valid
						return partValidity;
					}
				}
			}
		}
		return partValidity;
	}
	
	public static PartValidity checkValidity(Date beginDate, Date endDate) {
		Date currDate = new Date(System.currentTimeMillis());
		PartValidity partValidity = PartValidity.UNDEFINED;
		//Comparing begin and end dates
		if(DateUtils.truncatedCompareTo(beginDate, endDate, Calendar.DATE) <= 0) {
			//Perform part validity check only if Begin Date is before or same as End Date
			//Comparing begin date
			int compareBegin = DateUtils.truncatedCompareTo(currDate, beginDate, Calendar.DATE);
			//Comparing end date
			int compareEnd = DateUtils.truncatedCompareTo(currDate, endDate, Calendar.DATE);
			
			if(compareBegin>=0 && compareEnd<=0) {
				partValidity = PartValidity.VALID;
			}
			else if(compareEnd > 0) {
				partValidity = PartValidity.EXPIRED;
			}
			else if(compareBegin < 0) {
				partValidity = PartValidity.FUTUREDATED;
			}
		}
		else {
			//Begin Date is after End Date
			partValidity = PartValidity.INVALID;
		}
		return partValidity;
	}
			
}
