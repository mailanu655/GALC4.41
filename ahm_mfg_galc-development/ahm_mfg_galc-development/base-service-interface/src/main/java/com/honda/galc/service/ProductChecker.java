package com.honda.galc.service;

import com.honda.galc.data.InputData;

/**
 * 
 * <h3>ProductChecker Interface description</h3>
 * <p> ProductChecker description:
 *     Interface for Microservices to access GALC product checks
 * </p>
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
 * @author Paul Chou<br>
 * Apr.5, 2024
 *
 */
public interface ProductChecker extends IService{
	public Object check(InputData data);
}