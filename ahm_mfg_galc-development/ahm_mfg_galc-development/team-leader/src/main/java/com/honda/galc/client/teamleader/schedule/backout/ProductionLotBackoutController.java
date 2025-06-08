package com.honda.galc.client.teamleader.schedule.backout;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.ModelTypeHoldDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.dto.ProductionLotBackout;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.property.ReplicateScheduleProperty;
import com.honda.galc.property.ProductionLotBackoutPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;

public class ProductionLotBackoutController {

	private static final String LOT_DATE_FORMAT = "yyyyMMdd";
	private static final String PRODUCTION_LOT_COLUMN = "PRODUCTION_LOT";
	private static final String MBPN_PRODUCTION_LOT_COLUMN = "CURRENT_ORDER_NO";
	
	private final ProductionLotBackoutPanel panel;
	private final Logger logger;
	private final ClientAudioManager audioManager;
	private final ProductTypeUtil productTypeUtil;
	private final List<String> initialProcessPointIds;
	private PreProductionLotDao preProductionLotDao;
	private ProductionLotDao productionLotDao;
	private List<ProductionLotBackout> productionLotBackouts;
	private HoldResultDao holdResultDao;
	private QsrDao qsrDao;
	private ModelTypeHoldDao modelTypeHoldDao;

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

		SwingUtilities.invokeLater(new Runnable() {
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
		setModeLotPrefix();
	}

	private void initConnections() {
		this.panel.getLotPrefixTextField().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (!checkLotPrefixChange()) {
					setModeLotPrefix();
				} else {
					getPanel().clearMessage();
					setModeLotDate();
				}
			}
		});
		this.panel.getLotPrefixTextField().addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (!getPanel().getLotPrefixTextField().isEnabled()) {
					getPanel().clearMessage();
					setModeLotPrefix();
				}
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}

		});

		this.panel.getLotDateTextField().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (!checkLotDateChange()) {
					setModeLotDate();
				} else {
					getPanel().clearMessage();
					setModeDelete();
				}
			}
		});
		this.panel.getLotDateTextField().addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (!getPanel().getLotDateTextField().isEnabled() && !getPanel().getLotPrefixTextField().isEnabled()) {
					getPanel().clearMessage();
					setModeLotDate();
				}
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}

		});

		this.panel.getDeleteButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					if (MessageDialog.confirm(getPanel().getMainWindow(), "Are you sure?")) {
						getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						
						String lotMin = getProductionLotBackoutForHold().getLotMin();
						String lotMax = getProductionLotBackoutForHold().getLotMax();
						List<Integer> qsrs = getHoldResultDao().findQsrByProductionLotRange(lotMin,lotMax);
						List<HoldResult> holdResults = getHoldResultDao().findAllByProductionLotRange(lotMin,lotMax);
						getProductionLotDao().backoutProductionLot(getProductionLotBackouts());
						removeHolds(qsrs,holdResults,lotMin,lotMax);
						getPanel().resetComponents();
						getPanel().setCursor(Cursor.getDefaultCursor());
					}
				} catch (Exception e) {
					getPanel().setCursor(Cursor.getDefaultCursor());
					error(e.getMessage(), e);
				}
			}

		
		});
	}

	private void setModeLotPrefix() {
		this.panel.getLotPrefixTextField().setEnabled(true);
		this.panel.getLotPrefixTextField().setText(null);
		this.panel.getLotDateTextField().setEnabled(false);
		this.panel.getLotDateTextField().setText(null);
		this.panel.getDeleteButton().setEnabled(false);
		this.panel.clearProductionLotBackoutTable();
		this.panel.requestFocusOnLotPrefix();
	}
	private void setModeLotDate() {
		this.panel.getLotPrefixTextField().setEnabled(false);
		this.panel.getLotDateTextField().setEnabled(true);
		this.panel.getLotDateTextField().setText(null);
		this.panel.getDeleteButton().setEnabled(false);
		this.panel.clearProductionLotBackoutTable();
		this.panel.requestFocusOnLotDate();
	}
	private void setModeDelete() {
		buildProductionLotBackouts(this.panel.getLotPrefixTextField().getText(), this.panel.getLotDateTextField().getText());
		populateBackoutTable();
		this.panel.getLotPrefixTextField().setEnabled(false);
		this.panel.getLotDateTextField().setEnabled(false);
		if (getRowCountSum() > 0) this.panel.getDeleteButton().setEnabled(true);
	}

	private boolean checkLotPrefixChange() {
		try {
			this.panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			final boolean result;
			final String lotPrefix = this.panel.getLotPrefixTextField().getText();
			final String message = validateLotPrefix(lotPrefix);
			result = (message == null);
			if (!result) error(message);
			this.panel.setCursor(Cursor.getDefaultCursor());
			return result;
		} catch (Exception e) {
			this.panel.setCursor(Cursor.getDefaultCursor());
			error(e.getMessage(), e);
			return false;
		}
	}
	private String validateLotPrefix(String lotPrefix) {
		return (getPreProductionLotDao().isLotPrefixExist(lotPrefix) ? null : "Invalid lot prefix: " + lotPrefix + " is not a valid lot prefix.");
	}

	private boolean checkLotDateChange() {
		try {
			this.panel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			final boolean result;
			final String lotPrefix = this.panel.getLotPrefixTextField().getText();
			final String lotDate = this.panel.getLotDateTextField().getText();
			final String message = validateLotDate(lotPrefix, lotDate);
			result = (message == null);
			if (!result) error(message);
			this.panel.setCursor(Cursor.getDefaultCursor());
			return result;
		} catch (Exception e) {
			this.panel.setCursor(Cursor.getDefaultCursor());
			error(e.getMessage(), e);
			return false;
		}
	}
	private String validateLotDate(String lotPrefix, String lotDate) {
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
	}

	private void buildProductionLotBackouts(final String lotPrefix, final String lotDate) {
		List<ProductionLotBackout> productionLotBackouts = new ArrayList<ProductionLotBackout>();
		if (this.initialProcessPointIds == null) {
			productionLotBackouts.addAll(Arrays.asList(new ProductionLotBackout[] {
					new ProductionLotBackout(getTableNameProduct(), lotPrefix, lotDate),
					new ProductionLotBackout(getTableNameInProcessProduct(), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(PreProductionLot.class), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(ProductStampingSequence.class), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(ProductionLot.class), lotPrefix, lotDate)
			}));
		} else {
			productionLotBackouts.addAll( Arrays.asList(new ProductionLotBackout[] {
					new ProductionLotBackout(getTableNameProduct(), lotPrefix, lotDate),
					new ProductionLotBackout(getTableNameInProcessProduct(), lotPrefix, lotDate),
					new ProductionLotBackout(getTableNameProductHistory(), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(PreProductionLot.class), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(ProductStampingSequence.class), lotPrefix, lotDate),
					new ProductionLotBackout(CommonUtil.getTableName(ProductionLot.class), lotPrefix, lotDate)
			}));
		}
		
		//Add Sub Assembly ProductionLotBackout
				List<String> subLotPrefixes = getSubAssemblyLotPrefix(lotPrefix);
				for(String subLotPrefix: subLotPrefixes) {
					
					if (getProductionLotDao().isTableActiveForProductionDate(MBPN_PRODUCTION_LOT_COLUMN,getTableNameInstalledPart(), subLotPrefix, lotDate, CommonUtil.getTableName(MbpnProduct.class), null)) {
						this.logger.info( "Invalid lot date: active " + InstalledPart.class.getSimpleName() + " (" + getTableNameInstalledPart() + ") records exist for " + lotDate);
						continue;
					}
					if (getProductionLotDao().isTableActiveForProductionDate(MBPN_PRODUCTION_LOT_COLUMN,getTableNameMeasurement(), subLotPrefix, lotDate,CommonUtil.getTableName(MbpnProduct.class), null)) {
						this.logger.info( "Invalid lot date: active " + Measurement.class.getSimpleName() + " (" + getTableNameMeasurement() + ") records exist for " + lotDate);
						continue;
					}
					if(getPreProductionLotDao().isLotStamped(subLotPrefix, lotDate)) {
						this.logger.info( "Stamped Lot records exist for " + lotDate);
						continue;
					}
					this.logger.info( "Adding back out for " + subLotPrefix+lotDate);
					productionLotBackouts.add(new ProductionLotBackout(MBPN_PRODUCTION_LOT_COLUMN,CommonUtil.getTableName(MbpnProduct.class), subLotPrefix, lotDate));
					productionLotBackouts.add(new ProductionLotBackout(CommonUtil.getTableName(ProductStampingSequence.class), subLotPrefix, lotDate));
					productionLotBackouts.add(new ProductionLotBackout(CommonUtil.getTableName(PreProductionLot.class), subLotPrefix, lotDate));
				}
		this.productionLotBackouts = getProductionLotDao().getPopulatedProductionLotBackouts(productionLotBackouts);
	}

	private void populateBackoutTable() {
		List<MultiValueObject<ProductionLotBackout>> data = new ArrayList<MultiValueObject<ProductionLotBackout>>();
		for (ProductionLotBackout productionLotBackout : this.productionLotBackouts) {
			if(productionLotBackout.getRows() > 0) {
				MultiValueObject<ProductionLotBackout> productionLotBackoutMvo;
				productionLotBackoutMvo = new MultiValueObject<ProductionLotBackout>(productionLotBackout, toList(productionLotBackout));
				data.add(productionLotBackoutMvo);
			}
		}
		this.panel.getTable().reloadData(data);
	}

	private List<Object> toList(ProductionLotBackout plb) {
		List<Object> list = new ArrayList<Object>();
		list.add(plb.getTable());
		list.add(plb.getRows());
		list.add(plb.getLotRange());
		return list;
	}

	private int getRowCountSum() {
		int sum = 0;
		for (ProductionLotBackout productionLotBackout : this.productionLotBackouts)
			sum += productionLotBackout.getRows();
		return sum;
	}

	private void error(String message) {
		try { this.audioManager.playNGSound(); } catch (Exception e) { this.logger.error(e); }
		this.panel.displayErrorMessage(message);
		this.logger.error(message);
	}
	private void error(String message, Throwable throwable) {
		try { this.audioManager.playNGSound(); } catch (Exception e) { this.logger.error(e); }
		this.panel.displayErrorMessage(message);
		this.logger.error(throwable, message);
	}
	
	private void removeHolds(List<Integer> qsrs,List<HoldResult> holdResults,String start,String end) {
		if(holdResults != null && holdResults.size() > 0 && allProductionLotsBackedOut(start,end)) {
			this.logger.info("deleting "+holdResults.size() +" hold records");
			getHoldResultDao().removeAll(holdResults);
		
		this.logger.info("deleting Model Type Hold records");
		getModelTypeHoldDao().deleteAllByProductionLotRange(start, end);
	
			for(Integer qsr:qsrs) {
				long count = getHoldResultDao().countByQsr(qsr);
				if(count == 0) {
					this.logger.info("deleting Qsr - "+qsr);
					getQsrDao().removeByKey(qsr);
				}else {
					this.logger.info(count +" hold records still exist cannot delete Qsr - "+qsr);
				}
			}
		}
	}

	
	private boolean allProductionLotsBackedOut(String start,String end) {
		long productSize = getDao(FrameDao.class).count("", "", start, end, "", "", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), "",new ArrayList<String>());
		if(productSize > 0) return false;
		return true;
	}

	private HoldResultDao getHoldResultDao() {
		if (this.holdResultDao == null) {
			this.holdResultDao = getDao(HoldResultDao.class);
		}
		return this.holdResultDao;
	}
	
	private QsrDao getQsrDao() {
		if (this.qsrDao == null) {
			this.qsrDao = getDao(QsrDao.class);
		}
		return this.qsrDao;
	}
	
	private ModelTypeHoldDao getModelTypeHoldDao() {
		if (this.modelTypeHoldDao == null) {
			this.modelTypeHoldDao = getDao(ModelTypeHoldDao.class);
		}
		return this.modelTypeHoldDao;
	}
	
	private ProductionLotBackout getProductionLotBackoutForHold() {
		return new ProductionLotBackout(CommonUtil.getTableName(HoldResult.class), this.panel.getLotPrefixTextField().getText(), this.panel.getLotDateTextField().getText());
	}
	
	private List<String> getSubAssemblyLotPrefix(String lotPrefix) {
		List<String> subAssemblyLotPrefixes = new ArrayList<String>();
		String replicationClassName = "com.honda.galc.oif.task.ReplicateScheduleToLinesTask";
		List<ComponentProperty> properties = PropertyService.getComponentProperty("OIF_SERVICES");
		List<String> oifServiceComponents = new ArrayList<String>();
		for(ComponentProperty property:properties) {
			if(property.getPropertyValue().trim().equalsIgnoreCase(replicationClassName)) {
				oifServiceComponents.add(property.getPropertyKey());
			}
		}
		
		for(String oifServiceComponent:oifServiceComponents) {
			this.logger.info("Loading Properties for - "+oifServiceComponent);
			ReplicateScheduleProperty propBean = PropertyService.getPropertyBean(ReplicateScheduleProperty.class, oifServiceComponent);
			Map<String,String> sourcePlanCodeMap=propBean.getSourcePlanCode();
			Map<String,String> sourceProcessLocationMap=propBean.getSourceProcessLocation();
			Map<String,String> targetPlanCodeMap=propBean.getTargetPlanCode();
			Map<String,String> targetProcessLocationMap=propBean.getTargetProcessLocation();
			if(sourcePlanCodeMap != null) {
				String srcPlCd = "";
				String srcPrLoc = "";
				String tgPlCd  = "";
				String tgPrLoc = "";
				for(String key:sourcePlanCodeMap.keySet()) {
					srcPlCd = sourcePlanCodeMap.get(key);
					srcPrLoc = sourceProcessLocationMap.get(key);
					
					if(StringUtils.isNotEmpty(srcPlCd) && StringUtils.isNotEmpty(srcPrLoc)) {
						String srcLotPrefix = srcPlCd.substring(0, srcPlCd.length()-1)+srcPrLoc;
						if(lotPrefix.equalsIgnoreCase(srcLotPrefix)) {
							tgPlCd = targetPlanCodeMap.get(key);
							tgPrLoc = targetProcessLocationMap.get(key);
							if(StringUtils.isNotEmpty(tgPlCd) && StringUtils.isNotEmpty(tgPrLoc)) {
								String subLotPrefix = srcLotPrefix.replaceAll(srcPrLoc, tgPrLoc);
								if(StringUtils.isNotEmpty(subLotPrefix)) {
									this.logger.info("Adding SubLot Prefix - "+subLotPrefix);
									subAssemblyLotPrefixes.add(subLotPrefix);
								}
								
							}
						}
					}
				}
			}
		}
		return subAssemblyLotPrefixes;
	}
}
