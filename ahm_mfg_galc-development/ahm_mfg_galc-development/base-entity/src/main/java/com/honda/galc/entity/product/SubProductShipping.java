package com.honda.galc.entity.product;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>SubProductShipping Class description</h3>
 * <p> SubProductShipping description </p>
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
 * @author Jeffray Huang<br>
 * Nov 19, 2010
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="SUB_PRODUCT_SHIPPING_TBX")
public class SubProductShipping extends AuditEntry {
	@EmbeddedId
	private SubProductShippingId id;

	@Column(name="PRODUCT_TYPE")
	private String productType;

	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@Column(name="LINE_NUMBER")
	private String lineNumber;

	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	@Column(name="SEQ_NO")
	private int seqNo;

	@Column(name="STATUS")
	private int status = 0;

	@Column(name="SCH_QUANTITY")
	private int schQuantity;

	@Column(name="ACT_QUANTITY")
	private int actQuantity;

	private static final long serialVersionUID = 1L;
	
	public static final int NOT_STARTED = 0;
	public static final int IN_PROGRESS = 1;
	public static final int SHORT_SHIPPED = 2;
	public static final int SHIPPED = 3;

	public SubProductShipping() {
		super();
	}

	public SubProductShipping(String kdLot,String productionLot) {
		super();
		id = new SubProductShippingId(kdLot,productionLot);
	}
	
	
	public String getKdLotNumber() {
		return id.getKdLotNumber();
	}
	
	public String getProductionLot() {
		return id.getProductionLot();
	}


	public String getProductType() {
		return StringUtils.trim(this.productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductSpecCode() {
		return StringUtils.trim(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getLineNumber() {
		return StringUtils.trim(this.lineNumber) ;
	}

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public int getSeqNo() {
		return this.seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSchQuantity() {
		return this.schQuantity;
	}

	public void setSchQuantity(int schQuantity) {
		this.schQuantity = schQuantity;
	}

	public int getActQuantity() {
		return this.actQuantity;
	}

	public void setActQuantity(int actQuantity) {
		this.actQuantity = actQuantity;
	}
	
	public void setKdLotNumber(String kdLotNumber) {
		if(id == null)
			id = new SubProductShippingId();
		
		this.id.setKdLotNumber(kdLotNumber);
	}

	public void setProductionLot(String productionLot) {
		if(id == null)
			id = new SubProductShippingId();
		
		id.setProductionLot(productionLot);
	}
	
	
	public void incrementActQuantity() {
		this.actQuantity = actQuantity < schQuantity ? actQuantity + 1 : schQuantity;
		if(actQuantity > 0 && actQuantity < schQuantity) this.status = IN_PROGRESS;
		if(actQuantity >= schQuantity) this.status = SHIPPED;
	}
	
	public void decrementActQuantity() {
		this.actQuantity = actQuantity > 0 ? actQuantity - 1 : 0;
		if(this.actQuantity == 0) this.status = NOT_STARTED;
		else if(this.actQuantity > 0) this.status = IN_PROGRESS;
	}
	
	
	public boolean equals(Object obj) {
		if(obj == null || ! (obj instanceof SubProductShipping)) return false; 
		return getProductionLot().equals(((SubProductShipping)obj).getProductionLot());
	}
	
	public boolean isSameKdLot(SubProductShipping shippingLot) {
		
		if( shippingLot == null ) return false;
		return getKdLotNumber().substring(0,getKdLotNumber().length() - 1).equals(
				shippingLot.getKdLotNumber().substring(0,shippingLot.getKdLotNumber().length() -1));
		
	}
	
	/**
	 * create a SubProductShipping object from PreProductionLot object
	 * @param preProductionLot
	 * @param seqNo
	 * @return
	 */
	public static SubProductShipping createKnuckleShipping(PreProductionLot preProductionLot, int seqNo, long time) {
		return createShipping(preProductionLot, seqNo, time, ProductType.KNUCKLE);
	}
	
	public static SubProductShipping createShipping(PreProductionLot preProductionLot, int seqNo, long time, ProductType type) {
		SubProductShipping shippingLot = new SubProductShipping(preProductionLot.getKdLotNumber(),preProductionLot.getProductionLot());
		shippingLot.setProductType(type.toString());
		shippingLot.setProductSpecCode(preProductionLot.getProductSpecCode());
		shippingLot.setLineNumber(preProductionLot.getProductionLot().substring(4, 6));
		shippingLot.setProductionDate(new java.sql.Date(time));
		shippingLot.setSeqNo(seqNo);
		shippingLot.setSchQuantity(preProductionLot.getLotSize() * 2);
		return shippingLot;
	}
	
	
	public String toString() {
		return toString(getKdLotNumber(),getProductionLot());
	}


	public SubProductShippingId getId() {
		return id;
	}


}
