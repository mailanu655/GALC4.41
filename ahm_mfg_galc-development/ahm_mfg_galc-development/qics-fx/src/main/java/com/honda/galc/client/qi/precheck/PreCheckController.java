package com.honda.galc.client.qi.precheck;

import static com.honda.galc.client.product.action.ProductActionId.CANCEL;
import static com.honda.galc.client.product.action.ProductActionId.DIRECTPASS;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PreCheckController</code> is the controller class for PreCheck.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by Vivek Bettada</TH>
 * <TH>Update date 2/5/2020</TH>
 * <TH>Version 2.51</TH>
 * <TH>Mark of Update</TH>
 * <TH>RGALCPROD-7302:  Multi-line check for Pre-check screen</TH>
 * </TR>
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class PreCheckController extends AbstractQiProcessController<QiProcessModel, PreCheckView> implements EventHandler<ActionEvent> {

	private boolean refreshProductPreCheckResults = false;

	public PreCheckController(QiProcessModel model, PreCheckView view) {
		super(model, view);
	}

	@Override
	public void handle(ActionEvent arg0) {

	}

	public Map<String, Object> submitPreCheckProductState() {
		getLogger().info("excuting submitPreCheckProductState(productCheckTypes) function");
		String[] productCheckTypes = getProductPreCheckTypes();
		return submitCheckProductState(productCheckTypes);
	}

	private String[] getProductPreCheckTypes() {
		Map<String, String> checkTypesMap = PropertyService.getPropertyBean(QiPropertyBean.class,getCurrentWorkingProcessPointId()).getProductPreCheckTypes();
		return getProductCheckTypes(checkTypesMap);
	}
	
	
	public Map<String, Object> submitPreWarnCheckProductState() {
		getLogger().info("excuting submitPreCheckProductState(productCheckTypes) function");
		String[] productWarnCheckTypes = getProductPreWarnCheckTypes();
		return submitCheckProductState(productWarnCheckTypes);
	}

	private String[] getProductPreWarnCheckTypes() {
		Map<String, String> warnCheckTypesMap = PropertyService.getPropertyBean(QiPropertyBean.class,getCurrentWorkingProcessPointId()).getProductPreWarnCheckTypes();
		return getProductCheckTypes(warnCheckTypesMap);
	}

	private Map<String, Object> submitCheckProductState(String[] productCheckTypes) {
		if (productCheckTypes == null || productCheckTypes.length == 0) {
			return new LinkedHashMap<String,Object>();
		}
		if(getModel().getProductModel() != null)
			return ProductCheckUtil.check(getModel().getProductModel().getProduct(),getModel().getCurrentWorkingProcessPoint(), productCheckTypes);
		return null;
	}

	private String[] getProductCheckTypes(Map<String, String> checkTypesMap) {
		String[] checkTypes = {};
		if (checkTypesMap == null || checkTypesMap.isEmpty()) {
			return checkTypes;
		}
		String key = null;
		if (getModel().getProductModel() != null && getModel().getProductModel().getProduct() != null) {
			key = getModel().getProductModel().getProduct().getModelCode();
		}

		if (StringUtils.isBlank(key) || !checkTypesMap.keySet().contains(key)) {
			key = "*";
		}
		String str = checkTypesMap.get(key);
		if (StringUtils.isBlank(str)) {
			return checkTypes;
		}
		checkTypes = StringUtils.trim(str).split(Delimiter.COMMA);
		return checkTypes;
	}	

	public Map<String, Object> getProductPreCheckResults() {
		Map<String, Object> allResults = new LinkedHashMap<String, Object>();
		Map<String, Object> productChecks = submitPreCheckProductState();
		if (productChecks != null) {
			allResults.putAll(productChecks);
		}
		return allResults;
	}

	public ProductActionId[] getProductActionIds(){
		if(isCancelBtnDisable())  {
			return new ProductActionId[]{DIRECTPASS};
		} else 
			return new ProductActionId[]{CANCEL,DIRECTPASS};
	}

	@Override
	public void initEventHandlers() {

	}

	public boolean isRefreshProductPreCheckResults() {
		return refreshProductPreCheckResults;
	}

	public void setRefreshProductPreCheckResults(boolean refreshProductPreCheckResults) {
		this.refreshProductPreCheckResults = refreshProductPreCheckResults;
	}

	@Override
	public void initializeListeners() {
	}
	
	public void setMultiLine()  {
		getModel().setCurrentWorkingProcessPointId(getProcessPointId());
		setCurrentWorkingProcessPointId(getProcessPointId());
		getModel().setCurrentWorkingEntryDept(getApplicationContext().getEntryDept());
		setCurrentWorkingEntryDept(getApplicationContext().getEntryDept());
		// if multi_line station load station configuration of qics station associated with that line
		if(isMultiLine())  {
			//reset process point/entry dept in both controller and model
			setQicsStation();
		}		
	}
	

}
