package com.honda.galc.dao.oif;
/**
 * 
 * <h3>EntitySequenceInterface</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EntitySequenceInterface description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Feb 24, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Feb 24, 2015
 */
public interface EntitySequenceInterface {
	/**
	 * Retrieve the max existing sequence number in database for given plan code 
	 * @param planCode
	 * @return
	 */
	public Double findMaxSequence(String planCode);
}
