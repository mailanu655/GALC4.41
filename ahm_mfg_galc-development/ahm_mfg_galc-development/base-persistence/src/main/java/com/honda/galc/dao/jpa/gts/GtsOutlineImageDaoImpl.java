package com.honda.galc.dao.jpa.gts;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.gts.GtsOutlineImageDao;
import com.honda.galc.dao.gts.GtsOutlineMapDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.gts.GtsOutlineImage;
import com.honda.galc.entity.gts.GtsOutlineMap;
import com.honda.galc.entity.gts.GtsOutlineMapId;
import com.honda.galc.service.Parameters;

/**
 * 
 * 
 * <h3>GtsColorMapDaoImpl Class description</h3>
 * <p> GtsColorMapDaoImpl description </p>
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
 * May 21, 2015
 *
 *
 */
public class GtsOutlineImageDaoImpl extends BaseDaoImpl<GtsOutlineImage,Integer> implements GtsOutlineImageDao{
	
	
	public Integer getNextId(){
		Integer maxSeq = max("imageId", Integer.class, null);
		return maxSeq == null ? 1 : maxSeq + 1;
	}
	
	@Transactional
	public GtsOutlineImage insert(GtsOutlineImage outlineImage){
		outlineImage.setImageId(getNextId());
		GtsOutlineImage image = super.insert(outlineImage);
//		GtsNotificationSender.getNotificationService(node.getId().getTrackingArea()).shapeCreated(node);
		return image;
	}
}
