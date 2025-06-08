package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
/**
 * 
 * <h3>RecipeDownloadPDAService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipeDownloadPDAService description </p>
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
 * <TD>Jackie</TD>
 * <TD>May 30, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Jackie
 * @since May 30, 2014
 */
public interface RecipeDownloadPDAService extends IoService{

	/**
	 * get the NG part 
	 * @param data
	 * @return
	 */
	public abstract DataContainer getNGPart(DefaultDataContainer data);

	/**
	 * Update the NG part
	 * @param data
	 * @return
	 */
	public abstract DataContainer updateNGPart(DataContainer data);

}