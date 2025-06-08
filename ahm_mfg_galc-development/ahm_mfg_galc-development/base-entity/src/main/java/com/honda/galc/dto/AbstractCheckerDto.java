package com.honda.galc.dto;
/**
 * 
 * <h3>AbstractCheckerDto Class description</h3>
 * <p> AbstractCheckerDto: Abstract Class for Checker Dto used for Checker Comparison</p>
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
 * </TABLE>
 *   
 * @author Hemant Kumar<br>
 * Apr 30, 2018
 *
 */
public abstract class AbstractCheckerDto implements IDto{
	
	private static final long serialVersionUID = 1L;

	public abstract int getCheckSeq();
	
	public abstract String getCheckPoint();
	
	public abstract String getCheckSeqAsString();
	
	public abstract String getCheckName();
	
	public abstract String getChecker();
	
	public abstract String getReactionType();

	public abstract String getOperationName();
	
	public abstract String getKey();
	
}
