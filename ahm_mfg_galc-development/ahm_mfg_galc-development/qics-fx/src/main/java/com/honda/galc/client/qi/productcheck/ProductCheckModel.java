package com.honda.galc.client.qi.productcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectResult;

/**
* <h3>Class description</h3> <h4>Description</h4>
* <p>
* <code>ProductCheckModel</code> is ... .
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
* @author L&T Infotech
*/

public class ProductCheckModel extends QiProcessModel{
	private BaseProduct product;
	
	private List<DefectResult> defects = new ArrayList<DefectResult>();
	private List<DefectResult> newDefects = new ArrayList<DefectResult>();
	private Map<String, Object> productWarnCheckResults;
	private Map<String, List<DefectDescription>> allDefectDescriptions = new HashMap<String,List<DefectDescription>>();

	public BaseProduct getProduct() {
		return product;
	}
	
	public String getProductId() {
		return product == null ? null : product.getProductId();
	}
	
	public void setProduct(BaseProduct product) {
		this.product = product;
	}
	
	public Map<String, Object> getProductItemCheckResults() {
		return productModel.getProductItemCheckResults();
	}

	public void setProductItemCheckResults(Map<String, Object> productItemChecks) {
		productModel.setProductItemCheckResults(productItemChecks);
	}
	
	public List<DefectResult> getOutstandingDefects() {
		List<DefectResult> outstandingDefects = new ArrayList<DefectResult>();
		for (DefectResult defect : getDefects()) {
			if (!defect.isRepairedStatus()) {
				outstandingDefects.add(defect);
			}
		}
		return outstandingDefects;
	}
	public List<DefectResult> getDefects() {
		return defects;
	}

	protected void setDefects(List<DefectResult> defects) {
		this.defects = defects;
	}
	public List<DefectResult> getNewDefects() {
		return newDefects;
	}

	protected void setNewDefects(List<DefectResult> newDefects) {
		this.newDefects = newDefects;
	}
	
	public List<DefectDescription> getDefectDescriptions(String defectTypeName) {
		return allDefectDescriptions.get(defectTypeName);
	}
	
	public void putDefectDescriptions(String defectTypeName, List<DefectDescription> defectDescriptions) {
		allDefectDescriptions.put(defectTypeName, defectDescriptions);
	}
	
	public Map<String, Object> getProductWarnCheckResults() {
		return productWarnCheckResults;
	}

	public void setProductWarnCheckResults(Map<String, Object> productWarnChecks) {
		this.productWarnCheckResults = productWarnChecks;
	}

	
}

