package com.honda.galc.service.wds;
/**
 * 
 * <h3>IWdsClient Class description</h3>
 * <p> IWdsClient description </p>
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
 * Dec 19, 2012
 *
 *
 */
public interface IWdsClient {
    
    public void updateValue(String category, String name, float value);
    public void updateValue(String category,String name,int value);
    public void updateValue(String category,String name,String value);
    
}
