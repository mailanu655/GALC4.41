package com.honda.galc.service.engine;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.entity.product.DieCast;

/**
 * 
 * <h3>DcMcOn</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DcMcOn description </p>
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
 * <TD>A.Gawarla</TD>
 * <TD>Aug 25, 2015</TD>
 * <TD>1.42</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Ambica Gawarla
 * @since Aug 25, 2015
 */

public class DcMcOn<T extends DieCast> extends DcMcOnBase implements DcMcOnService{
	public static final String department = "DCMC";
	
	private DiecastOn<T> dieCastOn;
	private MachiningOn<T> machiningOn;
	
	DcMcOn(DiecastOn<T> dieCastOn,MachiningOn<T> machiningOn){
		this.dieCastOn = dieCastOn;
		this.machiningOn = machiningOn;
	}
	
	@Override
	public void collectData() {
		dieCastOn.collectData();
		machiningOn.collectData();
	}

	@Override
	protected String getDepartment() {
		return department;
	}

	@Override
	protected boolean isCreateDefect(boolean barCodeStatus) {
			return !dieCastOn.isPreheat() && !barCodeStatus;
	}

}
