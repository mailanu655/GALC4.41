package com.honda.galc.service.recipe;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.property.RecipePropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>FrameRecipeProductSequenceHelper</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FrameRecipeProductSequenceHelper description </p>
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
 * <TD>Dec. 18, 2017</TD>
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
 * @since Dec. 18, 2017
 */
public class FrameRecipeProductSequenceHelper extends RecipeProductSequenceHelper{
	FrameLinePropertyBean frameLinePropertyBean;

	public FrameRecipeProductSequenceHelper(ProductType productType, RecipePropertyBean property, Logger logger, String ppId) {
		super(productType, property, logger, ppId);
	}
	
	
	public Frame findFrameByLineRefNumber(int lineRef){
		return getFrameDao().findFrameByLineRefNumber(lineRef, getFrameLinePropertyBean().getAfOnProcessPointId(), 
				getFrameLinePropertyBean().getLineRefNumberOfDigits());

	}


	private FrameDao getFrameDao() {
		return (FrameDao)productDao;
		
	}


	public FrameLinePropertyBean getFrameLinePropertyBean() {
		if(frameLinePropertyBean ==null)
			frameLinePropertyBean = PropertyService.getPropertyBean(FrameLinePropertyBean.class, processPointId);
		return frameLinePropertyBean;
	}


}
