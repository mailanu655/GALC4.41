package com.honda.galc.client.teamleader;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.teamleader.model.PartResultTableModel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductResultUtil;
/**
 * 
 * @author vec15809
 *
 */
public class ManualLotControlGroupRepairController extends ManualLotControlRepairController<BaseProduct, ProductBuildResult> 
implements ItemListener{
	
	private String currentGroupId;
	List<PartResult> groupedPartResultData = new ArrayList<PartResult>();
	public ManualLotControlGroupRepairController(MainWindow mainWin, ManualLotControlGroupRepairPanel repairPanel,
			IManualLtCtrResultEnterViewManager resultEnterViewManager) {
		super(mainWin, repairPanel, resultEnterViewManager);
		
		addConnections();
		updateGroupIdPanel();
		
		
	}
	
	private void addConnections() {
		getGroupIdPanel().getComponent().addItemListener(this);
		getView().getSetResultNgButton().addActionListener(this);
	}

	private LabeledComboBox getGroupIdPanel() {
		return ((ManualLotControlGroupRepairPanel)getView()).getGroupIdPanel();
	}
	
	private void updateGroupIdPanel() {
		
		if( getProductType() != null) {
			updateGroupIdModel( getProductType().name());

		}
	}

	private void updateGroupIdModel(String productType) {
		List<String> groupIdsByProductType = ServiceFactory.getDao(LotControlRuleDao.class).findAllGroupIdsByProductType(productType);
		List<String> groupIdList = new ArrayList<String>();
		groupIdList.add("");
		for(String groupId : groupIdsByProductType){
			groupId = StringUtils.trimToEmpty(groupId);
			if(!groupIdList.contains(groupId)) groupIdList.add(groupId);
		}
		
		getGroupIdPanel().getComponent().setModel(new DefaultComboBoxModel(groupIdList.toArray(new String[]{})));
		getGroupIdPanel().getComponent().setSelectedIndex(0);
		if(true) return;
	}
	
	
	@Override
	protected BaseProduct checkProductOnServer(String productId) {
		try {
			return ProductTypeUtil.getTypeUtil(getProductType()).findProduct(productId);
		} catch (Exception e) {
			String msg = "failed to load " + getProductType().name() +	": " + productId;
			Logger.getLogger().warn(e, msg);
			throw new TaskException(msg);
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	protected void loadProductBuildResults() {
		this.getGroupIdPanel().setEnabled(true);
		
		productBuildResulits = (List<ProductBuildResult>) ProductTypeUtil.getTypeUtil(getProductType()).getProductBuildResultDao().findAllByProductId(product.getProductId());
		loadProductBuildResults(productBuildResulits);
	}
	
	

	public void itemStateChanged(ItemEvent e) {
		boolean groupChanged = false;
		if(e.getStateChange() == ItemEvent.SELECTED){
			currentGroupId = StringUtils.trim(e.getItem().toString());
			groupChanged = true;
		}
		
		if(groupChanged) loadProductBuildResultStatus(false);
	}
	
	protected void reset() {
		super.reset();
		this.getGroupIdPanel().setEnabled(false);
	}
	

	protected void renderProductBuildResult() {
		groupedPartResultData.clear();
		if(!StringUtils.isEmpty(currentGroupId)){
			for(PartResult pr : lotControlPartResultData){
				if(currentGroupId.equals(pr.getLotControlRule().getGroupId()))
					groupedPartResultData.add(pr);

			}
		}
		
		new PartResultTableModel(groupedPartResultData,getColumns(), getView().getPartStatusTable());
		renderProductSpecField();
		renderSeqField();
		getView().getSetResultNgButton().setEnabled(groupedPartResultData.size() > 0);
		enableButtonsAfterRenderBuildResults();
		
	}
	
	public void setResultNg(){
		try {
			List<InstalledPart> resultList = new ArrayList<InstalledPart>();
			for(PartResult pr : groupedPartResultData){

				if(pr.getBuildResult() == null) {
					ProductBuildResult buildResult = ProductTypeUtil.getTypeUtil(getProductType()).createBuildResult(product.getProductId(),pr.getPartName());
					pr.setBuildResult(buildResult);
				} 
				//set installed part status NG - currently support installed part only
				pr.getBuildResult().setInstalledPartStatus(InstalledPartStatus.NG);
				pr.getInstalledPart().setMeasurementsNg(getMeasurementCount(pr));

				resultList.add((InstalledPart)pr.getBuildResult());
				ProductResultUtil.saveAll(appContext.getApplicationId(),resultList);
			}		
		} catch (Exception e) {
			MessageDialog.showError(getView(), "Failed to delete data:" + e.getMessage());
		}
		
		PartResultTableModel model = (PartResultTableModel)getView().getPartStatusTable().getModel();
		model.fireTableDataChanged();
	}
	
}
