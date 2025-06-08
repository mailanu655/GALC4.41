package com.honda.galc.client.teamleader.dunnage;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.dunnage.DunnageUtils;
import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.validator.AlphaNumericValidator;
import com.honda.galc.client.product.validator.PassedDateValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.product.validator.StringTokenValidator;
import com.honda.galc.client.product.validator.StringValidator;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.DunnageContentDao;
import com.honda.galc.dao.product.DunnageDao;
import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Dunnage;
import com.honda.galc.entity.product.DunnageContent;
import com.honda.galc.entity.product.DunnageContentId;
import com.honda.galc.property.DunnagePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.PropertyComparator;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DunnageMaintenanceModel</code> is ... .
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
 * @author Karol Wozniak
 * @created Apr 25, 2013
 */
public class DunnageMaintenanceModel {

	private ApplicationContext applicationContext;
	private List<BroadcastDestination> printers;

	public DunnageMaintenanceModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.printers = new ArrayList<BroadcastDestination>();
		initDunnagePrinters();
	}

	protected void initDunnagePrinters() {
		String dunnagePrinter = StringUtils.trimToEmpty(getProperty().getDunnagePrinter());
		String dunnageForm = StringUtils.trimToEmpty(getProperty().getDunnageForm());
		if (StringUtils.isBlank(dunnagePrinter) || StringUtils.isBlank(dunnageForm)) {
			return;
		}
		List<BroadcastDestination> broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(getApplicationContext().getApplicationId());
		if (broadcastDestinations == null || broadcastDestinations.isEmpty()) {
			return;
		}
		for (BroadcastDestination bd : broadcastDestinations) {
			if (bd.getDestinationId().equals(dunnagePrinter) && dunnageForm.equals(StringUtils.trim(bd.getRequestId()))) {
				getPrinters().add(bd);
			}
		}
	}

	// === dao === //
	public BaseProduct findProduct(String pin) {
		return ProductTypeUtil.getProductDao(getProductType()).findBySn(pin, getProductType());
	}

	@SuppressWarnings("unchecked")
	public List<BaseProduct> selectDunnageProducts(String dunnageNumber) {
		return (List<BaseProduct>) ProductTypeUtil.getProductDao(getProductType()).findAllByDunnage(dunnageNumber);
	}

	public List<Map<String, Object>> selectDunnageProductsData(String dunnageNumber) {

		List<BaseProduct> products = selectDunnageProducts(dunnageNumber);

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (products == null || products.isEmpty()) {
			return data;
		}

		if (products != null && !products.isEmpty()) {
			Collections.sort(products, new PropertyComparator<BaseProduct>(BaseProduct.class, "updateTimestamp"));
			Collections.reverse(products);
		}

		ProductType productType = getProductType();
		return DunnageUtils.mapDunnageMaintData(products, productType, getProperty());
	}

	public int addToDunnage(BaseProduct product, String dunnageNumber) {
		return ProductTypeUtil.getProductDao(getProductType()).updateDunnage(product.getProductId(), dunnageNumber, getDunnageCapacity());
	}
	
	public int addToDunnage(List<String> productIds, String dunnageNumber) {
		return ProductTypeUtil.getProductDao(getProductType()).updateDunnage(productIds, dunnageNumber, getDunnageCapacity());
	}

	public void removeFromDunnage(BaseProduct product) {
		if (getProperty().isInsertDunnageContent()) {
			DunnageContentId dcid = new DunnageContentId();
			dcid.setDunnageId(product.getDunnage());
			dcid.setProductId(product.getProductId());
			ServiceFactory.getDao(DunnageContentDao.class).removeByKey(dcid);
		}
		ProductTypeUtil.getProductDao(getProductType()).removeDunnage(product.getProductId());

	}

	// === validators definitions === //
	public ChainCommand createDunnageNumberValidator() {
		boolean autoGenerated = getProperty().isDunnageNumberAutoGenerated();
		List<Command> validators = DunnageUtils.getValidatorCommands(autoGenerated);
		
		if (autoGenerated) {
			PassedDateValidator pdv = new PassedDateValidator(false, null, true, DunnageUtils.getDatePattern()) {
				@Override
				public Date getDate() {
					return DunnageUtils.getCurrentDate();
				}

				@Override
				public String getDetailedMessage(String propertyName) {
					String date = new SimpleDateFormat("yyyy-MMM-dd").format(getDate());
					String str = String.format("%s (%s)", date, DunnageUtils.getDatePattern());
					return MessageFormat.format(getDetailedMessageTemplate(), propertyName, str);
				}
			};
			int dateIx = DunnageUtils.getDateTokenIx();
			int dateEndIx = DunnageUtils.getDateTokenIx() + DunnageUtils.getDateTokenLength() - 1;
			validators.add(new StringTokenValidator("Date", dateIx, dateEndIx, pdv));
			
			int machineIdLength = getProperty().getDunnageNumberLength() - DunnageUtils.getSequenceTokenLength() - DunnageUtils.getDateTokenLength();
			validators.add(new StringTokenValidator("Machine", 0, machineIdLength - 1, new StringValidator(getProperty().getMachineId())));
		}

		ChainCommand validator = ChainCommand.create(validators, "Dunnage");
		validator.setShortCircuit(true);
		return validator;
	}

	public ChainCommand createProductNumberValidator() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new AlphaNumericValidator());
		ChainCommand validator = ChainCommand.create(validators, "Input Number");
		validator.setShortCircuit(true);
		return validator;
	}

	// === get/set === //
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	private ProductType getProductType() {
		return getApplicationContext().getProductTypeData().getProductType();
	}

	public DunnagePropertyBean getProperty() {
		return PropertyService.getPropertyBean(DunnagePropertyBean.class, getApplicationContext().getApplicationId());
	}

	public int getDunnageCapacity() {
		ProductType type = getProductType();
		if (ProductType.BLOCK.equals(type)) {
			return getProperty().getBlockDunnageCartQuantity();
		} else if (ProductType.HEAD.equals(type)) {
			return getProperty().getHeadDunnageCartQuantity();
		} else
			return getProperty().getDunnageCartQuantity();
	}

	public boolean isShippingSupported() {
		return !StringUtils.isBlank(getProperty().getShippingProcessPointId());
	}

	public boolean isOffConfigured() {
		return getProperty().getOffProcessPointIds() != null && getProperty().getOffProcessPointIds().length > 0;
	}	

	public boolean isPrintSupported() {
		return getPrinters() != null && !getPrinters().isEmpty();
	}

	public void shipProducts(List<BaseProduct> products) {
		String processPointId = getProperty().getShippingProcessPointId();
		if (products == null || products.isEmpty()) {
			return;
		}
		TrackingService service = ServiceFactory.getService(TrackingService.class);
		String msg = "The following products will be shipped to processPointId: " + processPointId + ", products: " + products;
		Logger.getLogger().info(msg);
		for (BaseProduct product : products) {
			service.track(product, processPointId);
		}
	}

	public List<BroadcastDestination> getPrinters() {
		return printers;
	}
	
	public boolean isInsertDunnageContext() {
		return getProperty().isInsertDunnageContent();
	}
	
	public void updateDunnageInfo(String dunnageNumber, String expectedQty, String productSpec)
	{
			Dunnage dunnage = new Dunnage();
			dunnage.setDunnageId(dunnageNumber);
			dunnage.setExpectedQty(Integer.valueOf(expectedQty));
			dunnage.setProductSpecCode(productSpec);
			getDunnageDao().update(dunnage);

	}
	
	public boolean isNewDunnage(String dunnageNumber){
		Dunnage dunnage = getDunnageDao().findByKey(dunnageNumber);
		if(null==dunnage || StringUtils.isEmpty(dunnage.getDunnageId())){
			return true;
		}else return false;
	}
	
	public int updateDunnage(String productType,String productId, String dunnageNumber, String dunnageRow,
			String dunnageColumn, String dunnageLayer) {
		
		getDieCastDao(productType).updateDunnage(productId, dunnageNumber);
		if(isFisrtProductInDunnage(dunnageNumber)){
			Dunnage dunnage = getDunnageDao().findByKey(dunnageNumber);
			dunnage.setProductSpecCode(getDieCastDao(productType).findBySn(productId).getProductSpecCode());
			getDunnageDao().update(dunnage);
		}
		DunnageContentId dunnageContentId = new DunnageContentId(dunnageNumber,productId);
		DunnageContent dunnageContent = new DunnageContent();
		dunnageContent.setId(dunnageContentId);
		dunnageContent.setDunnageColumn(dunnageColumn);
		dunnageContent.setDunnageLayer(dunnageLayer);
		dunnageContent.setDunnageRow(dunnageRow);
		
		getDunnageContentDao().update(dunnageContent);
		return 1;
		
	}
	
	private boolean isFisrtProductInDunnage(String dunnageNumber){
		return getDunnageContentDao().findAllProductsInDunnage(dunnageNumber).size()==0;
	}
		
	private DunnageContentDao getDunnageContentDao() {
		return getDao(DunnageContentDao.class);		
	}
	
	private DunnageDao getDunnageDao() {
		return getDao(DunnageDao.class);		
	}
	
	@SuppressWarnings("unchecked")
	private DiecastDao getDieCastDao(String productType) {
		if(ProductType.BLOCK.toString().equals(productType))
			return getDao(BlockDao.class);
		else if(ProductType.HEAD.toString().equals(productType))
			return getDao(HeadDao.class);
		else if(ProductType.CONROD.toString().equals(productType))
			return getDao(ConrodDao.class);
		else if(ProductType.CRANKSHAFT.toString().equals(productType))
			return getDao(CrankshaftDao.class);
		else return null;
	}
	
	public boolean isDunnageGroupedByModelType(){
		return getProperty().isDunnageGroupedByModelType();
	}
}
