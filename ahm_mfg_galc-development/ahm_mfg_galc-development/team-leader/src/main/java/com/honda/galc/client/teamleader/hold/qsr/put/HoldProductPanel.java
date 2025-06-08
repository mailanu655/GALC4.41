package com.honda.galc.client.teamleader.hold.qsr.put;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JPopupMenu;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.HoldPanel;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.put.listener.ExportAction;
import com.honda.galc.client.teamleader.hold.qsr.put.listener.PopupHoldDialogAction;
import com.honda.galc.client.teamleader.hold.qsr.put.listener.PopupMassScrapDialogAction;
import com.honda.galc.client.teamleader.hold.qsr.put.listener.RemoveSelectedProductsAction;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HoldProductPanel</code> is ... .
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
 */
public abstract class HoldProductPanel extends HoldPanel {

	private static final long serialVersionUID = 1L;

	private String datePattern;
	private DateFormat dateFormat;
	private QsrMaintenancePropertyBean qsrMaintPropertyBean;

	public HoldProductPanel(String screenName, int keyEvent, QsrMaintenanceFrame mainWindow) {
		super(screenName, keyEvent, mainWindow);
	}

	protected void mapActions() {
		super.mapActions();
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopupMenu(e);
			}

			protected void showPopupMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					boolean productsSelected = getProductPanel().getSelectedValue() != null;
					boolean productsListed = getProductPanel().getTable().getRowCount() > 0;
					getProductPopupMenu().getSubElements()[0].getComponent().setEnabled(productsSelected);
					getProductPopupMenu().getSubElements()[1].getComponent().setEnabled(productsSelected);
					getProductPopupMenu().getSubElements()[2].getComponent().setEnabled(productsListed);
					if(disableScrapMenuItem())
					{
						getProductPopupMenu().getSubElements()[3].getComponent().setEnabled(false);
					}else
					{
						getProductPopupMenu().getSubElements()[3].getComponent().setEnabled(productsSelected);						
					}
					if (isScrap()) {
						getProductPopupMenu().getSubElements()[4].getComponent().setEnabled(productsSelected);
						getProductPopupMenu().getSubElements()[5].getComponent().setEnabled(productsListed);
						
					} else {
						getProductPopupMenu().getSubElements()[4].getComponent().setEnabled(productsListed);
					}
					getProductPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};

		getProductPanel().addMouseListener(mouseListener);
		getProductPanel().getTable().addMouseListener(mouseListener);
	}

	protected void initModel() {
		setDatePattern("yyyy-MM-dd HH:mm:ss");
		setDateFormat(new SimpleDateFormat(getDatePattern()));
	}

	// === factory methods === //
	@Override
	protected JPopupMenu createProductPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		popup.add("Remove Selected Products").addActionListener(new RemoveSelectedProductsAction(this));
		popup.addSeparator();
		popup.add("Hold Selected Products").addActionListener(new PopupHoldDialogAction(this, false));
		popup.add("Hold All Listed Products").addActionListener(new PopupHoldDialogAction(this, true));
		popup.addSeparator();
		if (isScrap()) {
			popup.add("Scrap Selected Products").addActionListener(new PopupMassScrapDialogAction(this));
			popup.addSeparator();
		}
		popup.add("Export Selected Products").addActionListener(new ExportAction(this, false));
		popup.add("Export All Listed Products").addActionListener(new ExportAction(this, true));
		return popup;
	}

	// === get/set === //
	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public QsrMaintenancePropertyBean getQsrMaintPropertyBean() {
		if (qsrMaintPropertyBean == null) {
			qsrMaintPropertyBean = PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getApplicationId());
		}
		return qsrMaintPropertyBean;
	}
	
	private boolean isScrap() {
		return getQsrMaintPropertyBean().isMassScrapEnabled();
	}
	
	public List<String> getShipLines(){	
		String shipLines = getQsrMaintPropertyBean().getShipLineId();
		return Arrays.asList(shipLines.split(Delimiter.COMMA));
	}
	
	public List<BaseProduct> extractProducts(List<Map<String, Object>> list) {
		List<BaseProduct> products = new ArrayList<BaseProduct>();
		if (list == null || list.isEmpty()) {
			return products;
		}
		for (Map<String, Object> o : list) {
			BaseProduct p = (BaseProduct) o.get("product");
			products.add(p);
		}
		return products;
	}
	
	public void updateProductRecords() {
		List<Map<String, Object>> currentRecordList = getProductPanel().getItems();
		List<Map<String, Object>> updatedRecordList = new ArrayList<Map<String, Object>>();
		List<BaseProduct> currentProductList = extractProducts(currentRecordList);
		List<BaseProduct> updatedProductList = updateProducts(currentProductList);
		Map<String,BaseProduct> updatedProductMap = updatedProductList.stream().collect(
				Collectors.toMap(BaseProduct::getProductId, product -> product));
		
		Set<String> lastProcPointIdList = updatedProductList.stream().map(BaseProduct::getLastPassingProcessPointId).collect(Collectors.toSet());
		List<ProcessPoint> lastProcPointList = ServiceFactory.getDao(ProcessPointDao.class).findAllByIds(new ArrayList<String>(lastProcPointIdList));
		Map<String,ProcessPoint> lastProcPointMap = lastProcPointList.stream().collect(
				Collectors.toMap(ProcessPoint::getProcessPointId, processPoint -> processPoint));
		
		for (Map<String, Object> productRecord : currentRecordList) {
			BaseProduct currentProduct = ((BaseProduct)productRecord.get("product"));
			if (!updatedProductMap.containsKey(currentProduct.getProductId())) continue;
			BaseProduct updatedProduct = updatedProductMap.get(currentProduct.getProductId());
			productRecord.put("product", updatedProduct);
			String lastProcPointId = updatedProduct.getLastPassingProcessPointId();
			if (StringUtils.isNotBlank(lastProcPointId) && lastProcPointMap.containsKey(lastProcPointId))
				productRecord.put("lastProcessPointName", lastProcPointMap.get(lastProcPointId).getProcessPointName());
			productRecord.put("ship", getShipLines().contains(updatedProduct.getTrackingStatus()));
			updatedRecordList.add(productRecord);
		}
		
		getProductPanel().reloadData(updatedRecordList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BaseProduct> updateProducts(List<BaseProduct> productList){
		ProductDao productDao = ProductTypeUtil.getProductDao(getProductType());
		List<String> productIdList = productList.stream().map(BaseProduct::getProductId).collect(Collectors.toList());
		List<BaseProduct> updatedProductList = productDao.findProducts(productIdList, 0, productList.size());
		return updatedProductList;
	}
}