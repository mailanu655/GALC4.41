package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;


/**
 * 
 * <h3>Block Class description</h3>
 * <p> Block description </p>
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
 * Feb 28, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="BLOCK_TBX")
public class Block extends DieCast {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BLOCK_ID")
    private String blockId;
	
	public Block(){
		super();
	}
	
	public Block(String blockId) {
		super();
		this.blockId = blockId;
		setDcSerialNumber(blockId);
	}



	public String getBlockId() {
		return blockId;
	}
	
	public String getProductId() {
		return getBlockId();
	}
	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getId() {
		return getProductId();
	}

	@Override
	public ProductType getProductType() {
		
		return ProductType.BLOCK;
		
	}
	
	@Override
	public ProductNumberDef getProductNumberDef() {
		return ProductNumberDef.DCB;
	}
	
	
}
