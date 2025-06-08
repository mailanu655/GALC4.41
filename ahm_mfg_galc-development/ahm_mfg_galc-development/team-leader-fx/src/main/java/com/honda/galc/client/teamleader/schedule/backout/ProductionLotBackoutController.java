package com.honda.galc.client.teamleader.schedule.backout;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dto.ProductionLotBackout;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.property.ProductionLotBackoutPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;

public class ProductionLotBackoutController {

	private static final String LOT_DATE_FORMAT = "yyyyMMdd";
	private static final String PRODUCTION_LOT_COLUMN = "PRODUCTION_LOT";
	
	private final ProductionLotBackoutPanel panel;
	private final Logger logger;
	private final ClientAudioManager audioManager;
	private final ProductTypeUtil productTypeUtil;
	private final List<String> initialProcessPointIds;
	private PreProductionLotDao preProductionLotDao;
	private ProductionLotDao productionLotDao;
	private List<ProductionLotBackout> productionLotBackouts;

	public ProductionLotBackoutController(ProductionLotBackoutPanel panel) {
		this.panel = panel;
		this.logger = panel.getLogger();
		this.audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
		this.productTypeUtil = ProductTypeUtil.getTypeUtil(panel.getMainWindow().getProductType());

		final String[] initialProcessPointIdsArray = PropertyService.getPropertyBean(ProductionLotBackoutPropertyBean.class, panel.getMainWindow().getApplicationContext().getApplicationId()).getInitialProcessPointIds();
		if (initialProcessPointIdsArray == null || initialProcessPointIdsArray.length == 0) {
			this.initialProcessPointIds = null;
		} else {
			this.initialProcessPointIds = Arrays.asList(initialProcessPointIdsArray);
		}

		Platform.runLater(new Runnable() {
			public void run() { initialize(); }
		});
	}

	private ProductionLotBackoutPanel getPanel() { return this.panel; }
	private List<ProductionLotBackout> getProductionLotBackouts() { return this.productionLotBackouts; }
	private String getTableNameProduct() { return CommonUtil.getTableName(this.productTypeUtil.getProductClass()); }
	private String getTableNameProductHistory() { return CommonUtil.getTableName(this.productTypeUtil.getProductHistoryClass()); }
	private String getTableNameInProcessProduct() { return CommonUtil.getTableName(InProcessProduct.class); }
	private String getTableNameInstalledPart() { return CommonUtil.getTableName(InstalledPart.class); }
	private String getTableNameMeasurement() { return CommonUtil.getTableName(Measurement.class); }

	private PreProductionLotDao getPreProductionLotDao() {
		if (this.preProductionLotDao == null) {
			this.preProductionLotDao = getDao(PreProductionLotDao.class);
		}
		return this.preProductionLotDao;
	}
	private ProductionLotDao getProductionLotDao() {
		if (this.productionLotDao == null) {
			this.productionLotDao = getDao(ProductionLotDao.class);
		}
		return this.productionLotDao;
	}

	private void initialize() {
		initConnections();
		this.panel.resetComponents();
		this.panel.requestFocusOnLotPrefix();
	}

	private void initConnections() {
		this.panel.getLotPrefixTextField().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (!checkLotPrefixChange()) {
					setModeLotPrefix();
				} else {
					getPanel().clearMessage();
					setModeLotDate();
				}
			}
		});
		this.panel.getLotPrefixTextFieldHolder().setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (getPanel().getLotPrefixTextField().isDisabled()) {
					getPanel().clearMessage();
					setModeLotPrefix();
				}
			}
		});

		this.panel.getLotDateTextField().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (!checkLotDateChange()) {
					setModeLotDate();
				} else {
					getPanel().clearMessage();
					setModeDelete();
				}
			}
		});
		this.panel.getLotDateTextFieldHolder().setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (getPanel().getLotDateTextField().isDisabled() && getPanel().getLotPrefixTextField().isDisabled()) {
					getPanel().clearMessage();
					setModeLotDate();
				}
			}
		});

		this.panel.getDeleteButton().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					if (MessageDialog.confirm(ClientMainFx.getInstance().getStage(),"Are you sure?")) {
						getPanel().setCursor(Cursor.WAIT);
						getProductionLotDao().backoutProductionLot(getProductionLotBackouts());
						getPanel().resetComponents();
						getPanel().setCursor(Cursor.DEFAULT);
					}
				} catch (Exception e) {
					getPanel().setCursor(Cursor.DEFAULT);
					error(e.getMessage(), e);
				}
			}
		});
	}

	private void setModeLotPrefix() {
		this.panel.getLotPrefixTextField().setDisable(false);
		this.panel.getLotPrefixTextField().setText(null);
		this.panel.getLotDateTextField().setDisable(true);
		this.panel.getLotDateTextField().setText(null);
		this.panel.getDeleteButton().setDisable(true);
		this.panel.clearTable();
		this.panel.requestFocusOnLotPrefix();
	}
	private void setModeLotDate() {
		this.panel.getLotPrefixTextField().setDisable(true);
		this.panel.getLotDateTextField().setDisable(false);
		this.panel.getLotDateTextField().setText(null);
		this.panel.getDeleteButton().setDisable(true);
		this.panel.clearTable();
		this.panel.requestFocusOnLotDate();
	}
	private void setModeDelete() {
		buildProductionLotBackouts(this.panel.getLotPrefixTextField().getText(), this.panel.getLotDateTextField().getText());
		populateBackoutTable();
		this.panel.getLotPrefixTextField().setDisable(true);
		this.panel.getLotDateTextField().setDisable(true);
		if (getRowCountSum() > 0) this.panel.getDeleteButton().setDisable(false);
	}

	private boolean checkLotPrefixChange() {
		try {
			this.panel.setCursor(Cursor.WAIT);
			final boolean result;
			final String lotPrefix = this.panel.getLotPrefixTextField().getText();
			final String message = validateLotPrefix(lotPrefix);
			result = (message == null);
			if (!result) error(message);
			this.panel.setCursor(Cursor.DEFAULT);
			return result;
		} catch (Exception e) {
			this.panel.setCursor(Cursor.DEFAULT);
			error(e.getMessage(), e);
			return false;
		}
	}
	private String validateLotPrefix(String lotPrefix) {
		try {
			return (getPreProductionLotDao().isLotPrefixExist(lotPrefix) ? null : "Invalid lot prefix: " + lotPrefix + " is not a valid lot prefix.");
		} catch (Exception e) {
			error(e.getMessage(), e);
			return e.getMessage();
		}
	}

	private boolean checkLotDateChange() {
		try {
			this.panel.setCursor(Cursor.WAIT);
			final boolean result;
			final String lotPrefix = this.panel.getLotPrefixTextField().getText();
			final String lotDate = this.panel.getLotDateTextField().getText();
			final String message = validateLotDate(lotPrefix, lotDate);
			result = (message == null);
			if (!result) error(message);
			this.panel.setCursor(Cursor.DEFAULT);
			return result;
		} catch (Exception e) {
			this.panel.setCursor(Cursor.DEFAULT);
			error(e.getMessage(), e);
			return false;
		}
	}
	private String validateLotDate(String lotPrefix, String lotDate) {
		try {
			if (lotDate.length() != 8) {
				return "Invalid lot date: " + lotDate + " is not a valid Date in the format " + LOT_DATE_FORMAT;
			}
			SimpleDateFormat lotDateFormat = new SimpleDateFormat(LOT_DATE_FORMAT);
			try {
				lotDateFormat.parse(lotDate);
			} catch (ParseException pe) {
				return "Invalid lot date: " + lotDate + " is not a valid Date in the format " + LOT_DATE_FORMAT;
			}
			if (!getPreProductionLotDao().isLastProductionDate(lotPrefix, lotDate)) {
				return "Invalid lot date: " + lotDate + " is not the last production date";
			}
			if (this.productTypeUtil.getProductDao().isProductActiveForProductionDate(lotPrefix, lotDate, this.initialProcessPointIds)) {
				return "Invalid lot date: active " + this.productTypeUtil.getProductClass().getSimpleName() + " (" + getTableNameProduct() + ") records exist for " + lotDate;
			}
			if (getProductionLotDao().isTableActiveForProductionDate(PRODUCTION_LOT_COLUMN,getTableNameProductHistory(), lotPrefix, lotDate, getTableNameProduct(), this.initialProcessPointIds)) {
				return "Invalid lot date: active " + this.productTypeUtil.getProductHistoryClass().getSimpleName() + " (" + getTableNameProductHistory() + ") records exist for " + lotDate;
			}
			if (getProductionLotDao().isTableActiveForProductionDate(PRODUCTION_LOT_COLUMN,getTableNameInstalledPart(), lotPrefix, lotDate, getTableNameProduct(), null)) {
				return "Invalid lot date: active " + InstalledPart.class.getSimpleName() + " (" + getTableNameInstalledPart() + ") records exist for " + lotDate;
			}
			if (getProductionLotDao().isTableActiveForProductionDate(PRODUCTION_LOT_COLUMN,getTableNameMeasurement(), lotPrefix, lotDate, getTableNameProduct(), null)) {
				return "Invalid lot date: active " + Measurement.class.getSimpleName() + " (" + getTableNameMeasurement() + ") records exist for " + lotDate;
			}
			return null;
		} catch (Exception e) {
			error(e.getMessage(), e);
			return e.getMessage();
		}
	}

	private void buildProductionLotBackouts(final String lotPrefix, final String lotDate) {
		List<ProductionLotBackout> productionLotBackouts;
		if (this.initialProcessPointIds == null) {
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
		this.productionLotBackouts = getProductionLotDao().getPopulatedProductionLotBackouts(productionLotBackouts);
	}

	private void populateBackoutTable() {
		this.panel.clearTable();
		for (ProductionLotBackout productionLotBackout : this.productionLotBackouts) {
			ProductionLotBackoutDTO productionLotBackoutDto = new ProductionLotBackoutDTO(productionLotBackout.getTable(), productionLotBackout.getRows(), productionLotBackout.getLotRange());
			this.panel.getTableItems().add(productionLotBackoutDto);
		}
		this.panel.getTable().setVisible(true);
	}

	private int getRowCountSum() {
		int sum = 0;
		for (ProductionLotBackout productionLotBackout : this.productionLotBackouts)
			sum += productionLotBackout.getRows();
		return sum;
	}

	private void error(String message) {
		try { this.audioManager.playNGSound(); } catch (Exception e) { this.logger.error(e); }
		this.panel.displayMessage(message, MessageType.ERROR);
		this.logger.error(message);
	}
	private void error(String message, Throwable throwable) {
		try { this.audioManager.playNGSound(); } catch (Exception e) { this.logger.error(e); }
		this.panel.displayMessage(message, MessageType.ERROR);
		this.logger.error(throwable, message);
	}
}
