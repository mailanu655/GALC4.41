package com.honda.galc.service.datacollection.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.QicsService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.common.ProductHoldService;
import com.honda.galc.service.datacollection.DataCollectionUtil;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.ProductHoldUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>specCheckTask</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 * @created Sept 23, 2019
 */
/**
 * @author vec15809
 *
 */
public class SpecCheckTask extends CollectorTask{
	ExpectedProductDao expectedProductDao;
	FrameDao frameDao;
	private ProductHoldService holdService;
	private ProductCheckPropertyBean productCheckPropertyBean;
	
	public SpecCheckTask(HeadlessDataCollectionContext context, String processPointId) {
		super(context, processPointId);
	}

	@Override
	public void execute() {
		Frame product = (Frame) context.get("product");
		if(ProductCheckUtil.isSpecChanged(product, context.getProcessPoint().getId(), getLogger())) {
			if(getProductCheckPropertyBean().isHoldProductOnSpecChange())
				holdProduct(product);
			
			if(getProductCheckPropertyBean().isCreateDefectOnSpecChange())
				createDefect();
		}
	}


	private void createDefect() {
		getLogger().info("Spec Changed create defect:", context.getCurrentProductId());
		
		ServiceFactory.getService(QicsService.class).update(getSpecCheckDefectProcessPointId(), context.getProductType(), getBuildResults()); 
		List<ProductBuildResult> buildResults = context.getBuildResults();
		for(Iterator<ProductBuildResult> iter = buildResults.iterator(); iter.hasNext();) {
			ProductBuildResult data = iter.next();
		    if (getProductCheckPropertyBean().getSpecCheckDefectPartName().equals(data.getPartName())) {
		        iter.remove();
		    }
		}
		
	}

	private String getSpecCheckDefectProcessPointId() {
		if (StringUtils.isEmpty(getProductCheckPropertyBean().getSpecCheckDefectProcessPointId()))
			return context.getProcessPointId();
		else 
			return getProductCheckPropertyBean().getSpecCheckDefectProcessPointId();
	}

	private List<ProductBuildResult> getBuildResults() {
		DataCollectionUtil util = new DataCollectionUtil(context);
		ProductBuildResult part = util.addDefectBuildResult(getProductCheckPropertyBean().getSpecCheckDefectPartName(), ""); 
		List<ProductBuildResult> buildResultList = new ArrayList<ProductBuildResult>();
		part.setProcessPointId(getSpecCheckDefectProcessPointId());
		part.setQicsDefect(true);
		buildResultList.add(part);

		return buildResultList;
	}

	private void holdProduct(Frame product) {
		getLogger().info("Spec Changed hold product:", product.getProductId());
		// create QSR hold for spec change
		HoldResultType holdType;
		try {
			holdType = HoldResultType.valueOf(getProductCheckPropertyBean().getSpecCheckHoldType());
		} catch (IllegalArgumentException e) {
			holdType = HoldResultType.HOLD_AT_SHIPPING;
			getLogger().error("SPEC_CHECK_HOLD_TYPE value " + getProductCheckPropertyBean().getSpecCheckHoldType() + " is not valid. "
					+ "Hold type set to HOLD_AT_SHIPPING.");
		}
		
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), product.getProductId());
		dc.put(TagNames.PROCESS_POINT_ID.name(), getSpecCheckDefectProcessPointId());
		dc.put(TagNames.PRODUCT.name(), product);
		dc.put(TagNames.HOLD_REASON.name(), getProductCheckPropertyBean().getSpecCheckHoldReason());
		dc.put(TagNames.ASSOCIATE_ID.name(), getSpecCheckDefectProcessPointId());
		dc.put(TagNames.HOLD_SOURCE.name(), 0);
		dc.put(TagNames.HOLD_RESULT_TYPE.name(), holdType.toString());
		dc.put(TagNames.HOLD_ACCESS_TYPE.name(), ProductHoldUtil.getDefaultAccessTypeByHoldType(getSpecCheckDefectProcessPointId(), holdType));
		dc.put(TagNames.QSR_HOLD.name(), true);
				
		getHoldService().execute(dc);
	}

	public ProductHoldService getHoldService() {
		if(holdService == null)
			holdService = ServiceFactory.getService(ProductHoldService.class);
		return holdService;
	}
	
	public ProductCheckPropertyBean getProductCheckPropertyBean() {
		if(productCheckPropertyBean == null)
			productCheckPropertyBean = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, context.getProcessPointId());
		return productCheckPropertyBean;
	}

	public ExpectedProductDao getExpectedProductDao() {
		if(expectedProductDao == null)
			expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		return expectedProductDao;
	}

	public FrameDao getFrameDao() {
		if(frameDao == null)
			frameDao = ServiceFactory.getDao(FrameDao.class);
		return frameDao;
	}
	
	

}
