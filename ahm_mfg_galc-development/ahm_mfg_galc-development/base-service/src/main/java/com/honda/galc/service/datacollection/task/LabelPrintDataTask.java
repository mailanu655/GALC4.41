package com.honda.galc.service.datacollection.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>LabelPrintDataTask prepares printer data required by Label Printing. The task is supposebed to be used by ProductOnService</h3> 
 * <h4>Description</h4>
 * <p>
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
 * @author Hale Xie
 * @created Nov 14, 2014
 */
public class LabelPrintDataTask extends BroadcastDataTask {

	/**
	 * Instantiates a new label print data task.
	 *
	 * @param context the context
	 * @param processPointId the process point id
	 */
	public LabelPrintDataTask(HeadlessDataCollectionContext context,
			String processPointId) {
		super(context, processPointId);
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.service.datacollection.task.BroadcastDataTask#prepareBroadcastData(com.honda.galc.data.DataContainer)
	 */
	@Override
	protected void prepareBroadcastData(DataContainer dc) {
		LabelPrintDataPropertyBean propertyBean = PropertyService
				.getPropertyBean(LabelPrintDataPropertyBean.class,
						processPointId);
		BaseProduct product = (BaseProduct) context.get(TagNames.PRODUCT_ID
				.name());
		prepareProductData(dc, propertyBean, product);

		prepareMaunfactureDate(dc, propertyBean, product);

	}

	/**
	 * Prepare product data.
	 *
	 * @param dc the dc
	 * @param propertyBean the property bean
	 * @param product the product
	 */
	protected void prepareProductData(DataContainer dc,
			LabelPrintDataPropertyBean propertyBean, BaseProduct product) {
		ProductionLotDao dao = ServiceFactory.getDao(ProductionLotDao.class);
		ProductionLot lot = dao.findByKey(product.getProductionLot());
		dc.put(ProductionLot.class, lot);
	}

	/**
	 * Prepare maunfacture date.
	 *
	 * @param dc the dc
	 * @param propertyBean the property bean
	 * @param product the product
	 */
	protected void prepareMaunfactureDate(DataContainer dc,
			LabelPrintDataPropertyBean propertyBean, BaseProduct product) {
		// manufactured date
		Date certDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat();
		
		df.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
		dc.put(propertyBean.getPrintTimestampTagName(),
				df.format(certDate));
		
		df.applyPattern("MM/''yy");
		dc.put(propertyBean.getCertManufacturedDateTagName(),
				df.format(certDate));

		df.applyPattern("yy");
		dc.put(propertyBean.getCertYearTagName(), df.format(certDate));

		df.applyPattern("MM");
		dc.put(propertyBean.getCertMonthTagName(), df.format(certDate));

		dc.put(propertyBean.getCertTypeTagName(), product.getProductType()
				.name());
	}

}
