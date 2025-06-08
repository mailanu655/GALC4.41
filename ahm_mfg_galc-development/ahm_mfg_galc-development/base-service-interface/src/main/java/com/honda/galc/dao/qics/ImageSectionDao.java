package com.honda.galc.dao.qics;

import java.util.List;

import com.honda.galc.entity.qics.ImageSection;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ImageSectionDao Class description</h3>
 * <p> ImageSectionDao description </p>
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
 * Apr 1, 2011
 *
 *
 */
/** * *
* @version 
* @author Gangadhararao Gadde
* @since Jan 15,2015
*/
public interface ImageSectionDao extends IDaoService<ImageSection, Integer> {

	List<ImageSection> findAllByImageName(String imageName);
	
	List<ImageSection> findAllByDescriptionIdPartKindFlag(int descriptionId,short partKindFlag);
	
}
