package com.honda.galc.client.product;

import java.util.Map;

import com.honda.galc.client.schedule.ProductEvent;

/**
 * 
 * 
 * <h3>IProductIdProcessor Class description</h3>
 * <p> IProductIdProcessor description </p>
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
 * Mar 11, 2014
 *
 *
 */
public interface IProductIdProcessor {
	public Map<String,Object> validate(String inputNumber);
	public  String convert(String inputNumber);
	public void processInputNumber(ProductEvent event);
	public boolean validateInputNumber();
	public void productReset(ProductEvent event);
}
