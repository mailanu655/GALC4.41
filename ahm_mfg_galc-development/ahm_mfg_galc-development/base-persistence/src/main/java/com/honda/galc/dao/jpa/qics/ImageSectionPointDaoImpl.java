package com.honda.galc.dao.jpa.qics;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.ImageSectionPointDao;
import com.honda.galc.entity.qics.ImageSectionPoint;
import com.honda.galc.entity.qics.ImageSectionPointId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>ImageSectionPointDaoImpl Class description</h3>
 * <p> ImageSectionPointDaoImpl description </p>
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
public class ImageSectionPointDaoImpl extends BaseDaoImpl<ImageSectionPoint, ImageSectionPointId> implements ImageSectionPointDao {

	public int deleteAllByImageSetionId(int imageSectionId) {
		return delete(Parameters.with("id.imageSectionId",imageSectionId));
	}

	
	
}
