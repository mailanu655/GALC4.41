package com.honda.galc.entity.product;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>RearIpuCase</code> is ... .
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
 * @author Kamlesh Maharjan
 * @created April 25, 2024
 */

@Entity
@DiscriminatorValue(value = "RIPUCASE")
public class RearIpuCase extends Case {

	private static final long serialVersionUID = 1L;

	public RearIpuCase() {
		super(ProductType.RIPUCASE);
	}

	public RearIpuCase(String productId) {
		super(ProductType.RIPUCASE, productId);
	}

	@Override
	public ProductNumberDef getProductNumberDef() {
		return ProductNumberDef.DCRIPUCASE;
	}
}
