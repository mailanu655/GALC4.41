package com.honda.galc.service.schedule.backout;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.ProductionLotBackout;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.property.ReplicateScheduleProperty;
import com.honda.galc.property.ProductionLotBackoutPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;

public class ProductionLotBackoutServiceImpl implements ProductionLotBackoutService {

	private static final String LOT_DATE_FORMAT = "yyyyMMdd";
	
	private static final String PRODUCTION_LOT_COLUMN = "PRODUCTION_LOT";
	private Logger logger;
	private ProductTypeUtil productTypeUtil;
	private String tableNameProduct;
	private String tableNameProductHistory;
	private String tableNameInProcessProduct;
	private String tableNameInstalledPart;
	private String tableNameMeasurement;
	private List<String> initialProcessPointIds;

	@Autowired
	public PreProductionLotDao preProductionLotDao;

	@Autowired
	public ProductionLotDao productionLotDao;
	

	private Logger getLogger() {
		if (this.logger == null) {
			this.logger = Logger.getLogger();
		}
		return this.logger;
	}

	private ProductTypeUtil getProductTypeUtil() {
		if (this.productTypeUtil == null) {
			ProductType productType = getDao(ProductTypeDao.class).findByKey(PropertyService.getSystemPropertyBean().getProductType()).getProductType();
			this.productTypeUtil = ProductTypeUtil.getTypeUtil(productType);
		}
		return this.productTypeUtil;
	}

	private String getTableNameProduct() {
		if (this.tableNameProduct == null) {
			this.tableNameProduct = CommonUtil.getTableName(getProductTypeUtil().getProductClass());
		}
		return this.tableNameProduct;
	}

	private String getTableNameProductHistory() {
		if (this.tableNameProductHistory == null) {
			this.tableNameProductHistory = CommonUtil.getTableName(getProductTypeUtil().getProductHistoryClass());
		}
		return this.tableNameProductHistory;
	}

	private String getTableNameInProcessProduct() {
		if (this.tableNameInProcessProduct == null) {
			this.tableNameInProcessProduct = CommonUtil.getTableName(InProcessProduct.class);
		}
		return this.tableNameInProcessProduct;
	}

	private String getTableNameInstalledPart() {
		if (this.tableNameInstalledPart == null) {
			this.tableNameInstalledPart = CommonUtil.getTableName(InstalledPart.class);
		}
		return this.tableNameInstalledPart;
	}

	private String getTableNameMeasurement() {
		if (this.tableNameMeasurement == null) {
			this.tableNameMeasurement = CommonUtil.getTableName(Measurement.class);
		}
		return this.tableNameMeasurement;
	}

	private List<String> getInitialProcessPointIds() {
		if (this.initialProcessPointIds == null) {
			final String[] initialProcessPointIdsArray = PropertyService.getPropertyBean(ProductionLotBackoutPropertyBean.class).getInitialProcessPointIds();
			if (initialProcessPointIdsArray == null || initialProcessPointIdsArray.length == 0) {
				this.initialProcessPointIds = new java.util.ArrayList<String>(0);
			} else {
				this.initialProcessPointIds = Arrays.asList(initialProcessPointIdsArray);
			}
		}
		return this.initialProcessPointIds;
	}

	@Override
	public Device execute(Device device) { return null; }

	@Override
	public DataContainer execute(DataContainer data) { return null; }

	@Override
	public DataContainer backoutProductionLot(DataContainer input) {
		try {
			String lotPrefix = (String) input.get(DataContainerTag.LOT_PREFIX);
			String lotDate = (String) input.get(DataContainerTag.LOT_DATE);
			return backoutProductionLot(lotPrefix, lotDate);
		} catch (Exception e) {
			getLogger().error(e);
			return buildOutput(InfoCodes.NG.getId(), new StringBuilder().append("ERROR: ").append(e.getMessage()).toString());
		}
	}

	@Override
	public DataContainer backoutProductionLot(String lotPrefix, String lotDate) {
		try {
			DataContainer output;
			output = validateLotPrefix(lotPrefix);
			if ((Integer) output.get(DataContainerTag.INFO_CODE) != InfoCodes.OK.getId()) return output;
			output = validateLotDate(lotPrefix, lotDate);
			if ((Integer) output.get(DataContainerTag.INFO_CODE) != InfoCodes.OK.getId()) return output;
			output = doProductionLotBackout(lotPrefix, lotDate);
			return output;
		} catch (Exception e) {
			getLogger().error(e);
			return buildOutput(InfoCodes.NG.getId(), new StringBuilder().append("ERROR: ").append(e.getMessage()).toString());
		}
	}

	private DataContainer validateLotPrefix(DataContainer input) {
		try {
			String lotPrefix = (String) input.get(DataContainerTag.LOT_PREFIX);
			return validateLotPrefix(lotPrefix);
		} catch (Exception e) {
			getLogger().error(e);
			return buildOutput(InfoCodes.NG.getId(), new StringBuilder().append("ERROR: ").append(e.getMessage()).toString());
		}
	}

	private DataContainer validateLotPrefix(String lotPrefix) {
		try {
			if (!preProductionLotDao.isLotPrefixExist(lotPrefix)) {
				return buildOutput(InfoCodes.INVALID_LOT_PREFIX.getId(), new StringBuilder().append("Invalid lot prefix: ").append(lotPrefix).append(" is not a valid lot prefix.").toString());
			}
			return buildOutput(InfoCodes.OK.getId(), "");
		} catch (Exception e) {
			getLogger().error(e);
			return buildOutput(InfoCodes.NG.getId(), new StringBuilder().append("ERROR: ").append(e.getMessage()).toString());
		}
	}

	private DataContainer validateLotDate(DataContainer input) { // TODO: take INITIAL_PPIDS into account
		try {
			String lotPrefix = (String) input.get(DataContainerTag.LOT_PREFIX);
			String lotDate = (String) input.get(DataContainerTag.LOT_DATE);
			return validateLotDate(lotPrefix, lotDate);
		} catch (Exception e) {
			getLogger().error(e);
			return buildOutput(InfoCodes.NG.getId(), new StringBuilder().append("ERROR: ").append(e.getMessage()).toString());
		}
	}

	private DataContainer validateLotDate(String lotPrefix, String lotDate) {
		try {
			if (lotDate.length() != 8) {
				return buildOutput(InfoCodes.INVALID_LOT_DATE.getId(), new StringBuilder().append("Invalid lot date: ").append(lotDate).append(" is not a valid Date in the format ").append(LOT_DATE_FORMAT).toString());
			}
			try {
				SimpleDateFormat lotDateFormat = new SimpleDateFormat(LOT_DATE_FORMAT);
				lotDateFormat.parse(lotDate);
			} catch (ParseException pe) {
				return buildOutput(InfoCodes.INVALID_LOT_DATE.getId(), new StringBuilder().append("Invalid lot date: ").append(lotDate).append(" is not a valid Date in the format ").append(LOT_DATE_FORMAT).toString());
			}
			if (!preProductionLotDao.isLastProductionDate(lotPrefix, lotDate)) {
				return buildOutput(InfoCodes.INVALID_LOT_DATE_NOT_LAST_DATE.getId(), new StringBuilder().append("Invalid lot date: ").append(lotDate).append(" is not the last production date").toString());
			}
			if (getProductTypeUtil().getProductDao().isProductActiveForProductionDate(lotPrefix, lotDate, getInitialProcessPointIds())) {
				return buildOutput(InfoCodes.INVALID_LOT_DATE_ACTIVE_PRODUCTS_EXIST.getId(), new StringBuilder().append("Invalid lot date: active ").append(getProductTypeUtil().getProductClass().getSimpleName()).append(" (").append(getTableNameProduct()).append(") records exist for ").append(lotDate).toString());
			}
			if (productionLotDao.isTableActiveForProductionDate(PRODUCTION_LOT_COLUMN ,getTableNameProductHistory(), lotPrefix, lotDate, getTableNameProduct(), getInitialProcessPointIds())) {
				return buildOutput(InfoCodes.INVALID_LOT_DATE_ACTIVE_PRODUCT_RESULTS_EXIST.getId(), new StringBuilder().append("Invalid lot date: active ").append(getProductTypeUtil().getProductHistoryClass().getSimpleName()).append(" (").append(getTableNameProductHistory()).append(") records exist for ").append(lotDate).toString());
			}
			if (productionLotDao.isTableActiveForProductionDate(PRODUCTION_LOT_COLUMN ,getTableNameInstalledPart(), lotPrefix, lotDate, getTableNameProduct(), null)) {
				return buildOutput(InfoCodes.INVALID_LOT_DATE_ACTIVE_INSTALLED_PARTS_EXIST.getId(), new StringBuilder().append("Invalid lot date: active ").append(InstalledPart.class.getSimpleName()).append(" (").append(getTableNameInstalledPart()).append(") records exist for ").append(lotDate).toString());
			}
			if (productionLotDao.isTableActiveForProductionDate(PRODUCTION_LOT_COLUMN ,getTableNameMeasurement(), lotPrefix, lotDate, getTableNameProduct(), null)) {
				return buildOutput(InfoCodes.INVALID_LOT_DATE_ACTIVE_MEASUREMENTS_EXIST.getId(), new StringBuilder().append("Invalid lot date: active ").append(Measurement.class.getSimpleName()).append(" (").append(getTableNameMeasurement()).append(") records exist for ").append(lotDate).toString());
			}
			return buildOutput(InfoCodes.OK.getId(), "");
		} catch (Exception e) {
			getLogger().error(e);
			return buildOutput(InfoCodes.NG.getId(), new StringBuilder().append("ERROR: ").append(e.getMessage()).toString());
		}
	}

	private DataContainer doProductionLotBackout(String lotPrefix, String lotDate) {
		int totalExpectedDeleteCount = 0;
		List<ProductionLotBackout> productionLotBackouts = buildProductionLotBackouts(lotPrefix, lotDate);
		for (ProductionLotBackout productionLotBackout : productionLotBackouts) {
			totalExpectedDeleteCount += productionLotBackout.getRows();
		}
		if (totalExpectedDeleteCount == 0) {
			return buildOutput(InfoCodes.CANNOT_BACKOUT.getId(), "Cannot backout: no records to delete");
		}
		try {
			productionLotDao.backoutProductionLot(productionLotBackouts);
		} catch (Exception e) {
			getLogger().error(e);
			return buildOutput(InfoCodes.CANNOT_BACKOUT.getId(), new StringBuilder().append("Cannot backout: ").append(e.getMessage()).toString());
		}
		return buildOutput(InfoCodes.OK.getId(), "");
	}

	private List<ProductionLotBackout> buildProductionLotBackouts(final String lotPrefix, final String lotDate) {
		List<ProductionLotBackout> productionLotBackouts;
		if (getInitialProcessPointIds() == null || getInitialProcessPointIds().isEmpty()) {
			productionLotBackouts = Arrays.asList(new ProductionLotBackout[] {
					new ProductionLotBackout(getTableNameProduct(), lotPrefix, lotDate),
					new ProductionLotBackout(getTableNameInProcessProduct(), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(PreProductionLot.class), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(ProductStampingSequence.class), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(ProductionLot.class), lotPrefix, lotDate)
			});
		} else {
			productionLotBackouts = Arrays.asList(new ProductionLotBackout[] {
					new ProductionLotBackout(getTableNameProduct(), lotPrefix, lotDate),
					new ProductionLotBackout(getTableNameInProcessProduct(), lotPrefix, lotDate),
					new ProductionLotBackout(getTableNameProductHistory(), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(PreProductionLot.class), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(ProductStampingSequence.class), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(ProductionLot.class), lotPrefix, lotDate)
			});
		}
		return productionLotDao.getPopulatedProductionLotBackouts(productionLotBackouts);
	}

	private DataContainer buildOutput(int infoCode, String infoMessage) {
		DataContainer output = new DefaultDataContainer();
		output.put(DataContainerTag.INFO_CODE, infoCode);
		output.put(DataContainerTag.INFO_MESSAGE, infoMessage);
		getLogger().info(new StringBuilder().append(infoCode).append(" - ").append(infoMessage).toString());
		return output;
	}
}
