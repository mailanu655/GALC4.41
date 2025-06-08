package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
/**
 * 
 * <h3>ImagePropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ImagePropertyBean description </p>
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
 * @author Paul Chou
 * Apr 23, 2010
 *
 */
@PropertyBean(componentId ="Default_LotControl_Image")
public interface ImagePropertyBean extends IProperty{
	String getOkImage();
	String getNgImage();
	String getBlankImage();

}
