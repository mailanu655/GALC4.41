package com.honda.galc.entity.product;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.honda.galc.data.ProductType;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PulleyShaftDrive</code> is ... .
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
 * @created Jun 29, 2015
 */
@Entity
@DiscriminatorValue(value = "PSDR      ")
public class PulleyShaftDrive extends SubProduct {

	private static final long serialVersionUID = 1L;

	public PulleyShaftDrive() {
		super(ProductType.PSDR);
	}

	public PulleyShaftDrive(String productId) {
		super(ProductType.PSDR, productId);
	}
}