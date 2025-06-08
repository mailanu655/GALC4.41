package com.honda.galc.client.teamleader.hold.qsr.put.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.QsrAction;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.client.teamleader.hold.qsr.put.dialog.HoldDialog;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PopupHoldDialogAction</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 7, 2010</TD>
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
public class PopupHoldDialogAction extends QsrAction<HoldProductPanel> implements ActionListener {

	private boolean allSelected;
	private Map<String,ArrayList<BaseProduct>> rejectMap;
	final static String SHIPPED_PROD_GRP = "poduct shipped";
	final static String SHP_ASSOC_ENG_GRP = "associated engine shipped";
	final static String SHP_ASSOC_FRM_GRP = "associated VIN shipped";

	public PopupHoldDialogAction(HoldProductPanel panel, boolean allSelected) {
		super(panel);
		this.allSelected = allSelected;
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {	
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();
		Map<BaseProduct,Map<String, Object>> unshippedFrames = new HashMap<BaseProduct,Map<String, Object>>();
		Map<BaseProduct,Map<String, Object>> unshippedEngines = new HashMap<BaseProduct,Map<String, Object>>();
		rejectMap = new HashMap<String,ArrayList<BaseProduct>>();
		rejectMap.put(SHIPPED_PROD_GRP, new ArrayList<BaseProduct>());
		rejectMap.put(SHP_ASSOC_ENG_GRP, new ArrayList<BaseProduct>());
		rejectMap.put(SHP_ASSOC_FRM_GRP, new ArrayList<BaseProduct>());
		
		if (isAllSelected()) {
			list = getView().getProductPanel().getItems();
		} else {
			list = getView().getProductPanel().getSelectedItems();
		}
		
		for(Map<String, Object> item : list){
			if(Boolean.TRUE.equals(item.get("ship"))){
				ArrayList<BaseProduct> shippedProduts = rejectMap.get(SHIPPED_PROD_GRP);
				shippedProduts.add((BaseProduct)item.get("product"));
				rejectMap.put(SHIPPED_PROD_GRP, shippedProduts);
			}else{
				if (((	BaseProduct)item.get("product")).getProductType() == ProductType.FRAME &&
						!Config.getProperty().isAllowHoldVinWithShippedEng()) {
					Frame frame = (Frame)item.get("product");
					if (StringUtils.isNotBlank(frame.getEngineSerialNo())) {
						unshippedFrames.put(frame, item);
						continue;
					}
				} else if (((BaseProduct)item.get("product")).getProductType() == ProductType.ENGINE &&
						!Config.getProperty().isAllowHoldEngWithShippedVin()) {
					Engine engine = (Engine)item.get("product");
					if (StringUtils.isNotBlank(engine.getVin())) {
						unshippedEngines.put(engine, item);
						continue;
					}
				} 
				finalList.add(item);
			}
		}
		
		if (!unshippedFrames.isEmpty())
			finalList.addAll(updateRejectMap(unshippedFrames, ProductType.FRAME));
		
		if (!unshippedEngines.isEmpty())
			finalList.addAll(updateRejectMap(unshippedEngines, ProductType.ENGINE));
		
		getView().getProductPanel().getSelectedItems();
		if (finalList.size() > Config.getProperty().getMaxHoldBatchSize()) {
			StringBuilder sb = new StringBuilder();
			sb.append("The total number of Products to hold exceeds maximum allowed ").append(Config.getProperty().getMaxHoldBatchSize());
			JOptionPane.showMessageDialog(getView(), sb, "Invalid Input", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Boolean rejectionsExist = false;
		for (ArrayList<BaseProduct> rejectGroup : rejectMap.values()) {
			if (rejectionsExist = !rejectGroup.isEmpty()) break;
		}
		
		if (rejectionsExist) {
			StringBuilder message = new StringBuilder();		
			message.append("The following products will be skipped.\n");		
			for(BaseProduct product : rejectMap.get(SHIPPED_PROD_GRP)){
				message.append("\n" + product.getProductId() + " - " + SHIPPED_PROD_GRP);
			}
			for(BaseProduct product : rejectMap.get(SHP_ASSOC_ENG_GRP)){
				message.append("\n" + product.getProductId() + " - engine [" + ((Frame)product).getEngineSerialNo() + "] shipped");
			}
			for(BaseProduct product : rejectMap.get(SHP_ASSOC_FRM_GRP)){
				message.append("\n" + product.getProductId() + " - VIN [" + ((Engine)product).getVin() + "] shipped");
			}
			this.showScrollDialog(message.toString());
		}
		
		if(Config.isDisableProductIdCheck(this.getView().getProductType().toString()) && !finalList.isEmpty()){
			List<String> productIds = new ArrayList<String>();
			for (Map<String, Object> item : finalList) {
				productIds.add(((BaseProduct)item.get("product")).getProductId().trim());
			}
			@SuppressWarnings("unchecked")
			List<BaseProduct> productList = (List<BaseProduct>) getProductDao(getView().getProductType()).findProducts(productIds, 0, productIds.size());
			for (BaseProduct product : productList) {
				if (productIds.contains(product.getProductId().trim())) productIds.remove(product.getProductId().trim());
			}
			if (!productIds.isEmpty()) {
				StringBuilder message = new StringBuilder();
				message.append(	"Manifests missing for the following product IDs.\nWould you like to put them on hold anyway?\n");
				for (String productId : productIds) {
					message.append("\n" + productId.trim());
				}
				if (this.showConfirmDialog(message.toString()) != JOptionPane.OK_OPTION) return;
			}
		}
		
		if (finalList.isEmpty()) return;
		
		List<Qsr> qsrs = selectQsrs();
		final HoldDialog dialog = new HoldDialog(getView(), "Hold Products", finalList, qsrs);
		dialog.setLocationRelativeTo(getView().getMainWindow());
		dialog.setVisible(true);
	}
	
	private List<Map<String, Object>> updateRejectMap(Map<BaseProduct, Map<String, Object>> productMap, ProductType productType){
		ArrayList<String> unitsWithShippedAssocProds = this.filterShippedAssocProducts(new ArrayList<BaseProduct>(productMap.keySet()));
		List<Map<String, Object>> acceptedProducts = new ArrayList<Map<String, Object>>();
		ArrayList<BaseProduct> rejectedProducts = new ArrayList<BaseProduct>();
		for (BaseProduct product : productMap.keySet()) {
			if (unitsWithShippedAssocProds.contains(product.getProductId()))
				rejectedProducts.add(product);
			else 
				acceptedProducts.add(productMap.get(product));
		}
		String rejectKey = (productType == ProductType.ENGINE) ? SHP_ASSOC_FRM_GRP : SHP_ASSOC_ENG_GRP;
		rejectMap.put(rejectKey, rejectedProducts);
		
		return acceptedProducts;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ArrayList<String> filterShippedAssocProducts(List<BaseProduct> productList) {
		ArrayList<String> shippedAssocProducts = new ArrayList<String>();
		if (productList.isEmpty()) return shippedAssocProducts;
		
		List<String> assocProductIds = new ArrayList<String>();
		ProductType productType = productList.get(0).getProductType();

		for (BaseProduct product : productList) {
			if (productType == ProductType.ENGINE) {
				assocProductIds.add(((Engine)product).getVin());
			} else if (productType == ProductType.FRAME) {
				assocProductIds.add(((Frame)product).getEngineSerialNo());
			} else {
				return shippedAssocProducts;
			}
		}
		ProductDao assocProductDao = (productType == ProductType.ENGINE) ? ServiceFactory.getDao(FrameDao.class) : ServiceFactory.getDao(EngineDao.class);
		List<BaseProduct> assocProducts = assocProductDao.findProducts(assocProductIds, 0, assocProductIds.size());
		for (BaseProduct assocProduct : assocProducts) {
			if (StringUtils.isNotBlank(assocProduct.getTrackingStatus()) && this.getShipLines().contains(assocProduct.getTrackingStatus())){
				String shippedAssocProduct = (productType == ProductType.ENGINE) ?
						((Frame)assocProduct).getEngineSerialNo() : ((Engine)assocProduct).getVin();
				shippedAssocProducts.add(shippedAssocProduct);
			}
		}
		return shippedAssocProducts;
	}

	protected List<Qsr> selectQsrs() {
		
		Division division = getView().getDivision();
		ProductType productType = getView().getProductType();
		if (division == null || productType == null) {
			return new ArrayList<Qsr>();
		}
		List<Qsr> list = getQsrDao().findAll(division.getDivisionId(), productType.name(), QsrStatus.ACTIVE.getIntValue());
		if (list == null) {
			return new ArrayList<Qsr>();
		}
		Comparator<Qsr> c = new Comparator<Qsr>() {
			public int compare(Qsr o1, Qsr o2) {
				return -o1.getId().compareTo(o2.getId());
			}
		};
		Collections.sort(list, c);
		return list;
	}

	public boolean isAllSelected() {
		return allSelected;
	}
	
	
}