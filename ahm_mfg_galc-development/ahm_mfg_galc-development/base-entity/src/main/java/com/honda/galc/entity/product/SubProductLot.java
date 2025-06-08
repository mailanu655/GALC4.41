package com.honda.galc.entity.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ReflectionUtils;
/**
 * 
 * <h3>SubProductLot</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SubProductLot description </p>
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
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Dec 21, 2010
 *
 */

public class SubProductLot implements Serializable, Comparable<SubProductLot>{
	private static final long serialVersionUID = 1L;
	public final static int NOT_PROCESSED = -999;
	public final static int INIT_PRINTING = -1;

	private PreProductionLot preProductionLot;
	private String subId;
	private int startProductSn;
	private BuildAttribute buildAttribute;
	
	private int smallLotIndex = -1;
	private int labelPrintingStartPosition = NOT_PROCESSED; //not set
	
	public SubProductLot() {
		super();
	}
	
	public SubProductLot(PreProductionLot preProductionLot, String subId, BuildAttribute buildAttribute) {
		super();
		this.preProductionLot = preProductionLot;
		this.subId = subId;
		this.buildAttribute = buildAttribute;
		
		initialize();
	}



	private void initialize() {
		startProductSn = ProductionLotHelper.getStartProductSn(preProductionLot.getStartProductId(), subId);
	}



	public boolean isInLot(String productId, int productNoPrefixLength) {
		return ProductionLotHelper.isInLot(productId, getStartProductId(), getProductNoPrefixLength(), getSize());

	}

	public String getStartProductId(){
		return buildAttribute.getAttributeValue() + buildAttribute.getModelYearCode() + 
		formatSequenceNumber(startProductSn);
	}
	
	public String getEndProductId() {
		return buildAttribute.getAttributeValue() + buildAttribute.getModelYearCode() + 
		formatSequenceNumber(startProductSn + preProductionLot.getLotSize() -1);
	}
	
	public int getSize() {
		return preProductionLot.getLotSize();
	}

	private int getProductNoPrefixLength() {
		//attribute value + 1 chars : year code
		return buildAttribute.getAttributeValue() == null ? 0 + 1 : buildAttribute.getAttributeValue() .length() + 1;
	}
	
	
	//Getters & Setters
	public String getAttribute() {
		return StringUtils.trim(buildAttribute.getId().getAttribute());
	}


	public String getAttributeValue() {
		return StringUtils.trim(buildAttribute.getAttributeValue() );
	}

	public <T extends SubProduct> List<SubProduct> generatePrintLabelList(Class<T> clazz) {	
		List<SubProduct> list = new ArrayList<SubProduct>();
		list.add(createSubProduct(clazz, getStartProductId()));
		
		SubProduct nextProduct = nextProductId(list.get(0));
		while(nextProduct != null){
			list.add(nextProduct);
			nextProduct = nextProductId(nextProduct);
		}
		
		return list;
	}
	
	public <T extends SubProduct> SubProduct createSubProduct(Class<T> clazz, String productId){
		SubProduct product = (SubProduct) ReflectionUtils.createInstance(clazz);
		product.setProductId(productId);
		product.setProductSpecCode(getProductSpecCode());
		product.setKdLotNumber(preProductionLot.getKdLot());
		product.setSubId(getSubId());
		
		return product;
	}
	
	private SubProduct nextProductId(SubProduct product) {	
		if(SubProduct.getProductSerialNumber(product.getProductId()) < (startProductSn + preProductionLot.getLotSize() -1))
			return createSubProduct(product.getClass() ,nextProductId(product.getProductId()));
		
		return null;
	}

	private String nextProductId(String productId) {
		String prefix = productId.substring(0, productId.length() - 6);
		int sequenceNumber = Integer.parseInt(productId.substring(productId.length() - 6)) +1;
		
		return prefix + formatSequenceNumber(sequenceNumber);
	}
	
	private String formatSequenceNumber(int sequenceNumber){
		String format ="%1$06d";
		return String.format(format, sequenceNumber);
	}

	public int getSmallLotIndex() {
		return smallLotIndex;
	}

	public void setSmallLotIndex(int smallLotIndex) {
		this.smallLotIndex = smallLotIndex;
	}

	public int compareTo(SubProductLot o) {
		return getAttribute().compareTo(o.getAttribute());
	}

	public int getLabelPrintingStartPosition() {
		return labelPrintingStartPosition;
	}

	public void setLabelPrintingStartPosition(int labelPrintingStartPosition) {
		this.labelPrintingStartPosition = labelPrintingStartPosition;
	}

	public boolean isLabelPrintingStartPositionSet() {
		return labelPrintingStartPosition != NOT_PROCESSED;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getProductionLot() {
		return preProductionLot.getProductionLot();
	}

	public String getProductSpecCode() {
		return preProductionLot.getProductSpecCode();
	}

	public PreProductionLot getPreProductionLot() {
		return preProductionLot;
	}


	public boolean isInLot(String productId) {

		return ProductionLotHelper.isInLot(productId, getStartProductId(), 
				getProductNoPrefixLength(), preProductionLot.getLotSize());
	}
	
	
}
