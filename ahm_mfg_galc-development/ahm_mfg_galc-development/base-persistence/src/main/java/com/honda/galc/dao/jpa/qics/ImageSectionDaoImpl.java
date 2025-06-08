package com.honda.galc.dao.jpa.qics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.ImageSectionDao;
import com.honda.galc.dao.qics.ImageSectionPointDao;
import com.honda.galc.entity.qics.ImageSection;
import com.honda.galc.entity.qics.ImageSectionPoint;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>ImageSectionDaoImpl Class description</h3>
 * <p> ImageSectionDaoImpl description </p>
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
 * Feb 28, 2011
 *
 *
 */
/** * *
* @version 
* @author Gangadhararao Gadde
* @since Jan 15,2015
*/
public class ImageSectionDaoImpl extends BaseDaoImpl<ImageSection,Integer> implements ImageSectionDao{

	@Autowired
	private ImageSectionPointDao imageSectionPointDao;
	
	public List<ImageSection> findAllByImageName(String imageName) {
		
		return findAll(Parameters.with("imageName", imageName));
		
	}
	
	@Transactional
	public void insertAll(List<ImageSection> imageSections) {
		
		for(ImageSection imageSection : imageSections) {
			List<ImageSectionPoint> imageSectionPoints = imageSection.getImageSectionPoints();
			imageSection.setImageSectionPoints(new ArrayList<ImageSectionPoint>());
			imageSection.setImageSectionId(getMaxSectionId() + 1);
			imageSection.setOverlayNo(getMaxOverlayNo() + 1);
			save(imageSection);
			for(ImageSectionPoint imageSectionPoint : imageSectionPoints) {
				imageSectionPoint.setImageSectionId(imageSection.getImageSectionId());
				imageSectionPointDao.save(imageSectionPoint);
			}
		}
	}
	
	@Transactional
	public List<ImageSection> saveAll(List<ImageSection> imageSections) {
		for(ImageSection imageSection : imageSections) {
			List<ImageSectionPoint> imageSectionPoints = imageSection.getImageSectionPoints();
			imageSection.setImageSectionPoints(new ArrayList<ImageSectionPoint>());
			save(imageSection);
			for(ImageSectionPoint imageSectionPoint : imageSectionPoints) {
				imageSectionPoint.setImageSectionId(imageSection.getImageSectionId());
				imageSectionPointDao.save(imageSectionPoint);
			}
		}
		return imageSections;
	}
	
	@Transactional
	public void removeAll(List<ImageSection> imageSections) {
		for(ImageSection imageSection : imageSections) {
			imageSection.setImageSectionPoints(new ArrayList<ImageSectionPoint>());
			imageSectionPointDao.deleteAllByImageSetionId(imageSection.getImageSectionId());
			remove(imageSection);
		}
	}
	
	private int getMaxSectionId(){
		Integer maxId = max("imageSectionId",Integer.class); 
		if (maxId == null) {
			maxId = 0;
		}
		return maxId;
	}
	
	private int getMaxOverlayNo(){
		Integer maxId = max("overlayNo",Integer.class);
		if (maxId == null) {
			maxId = 0;
		}
		return maxId;
	}
	
	public List<ImageSection> findAllByDescriptionIdPartKindFlag(int descriptionId,short partKindFlag)
	{
		return findAll(Parameters.with("descriptionId", descriptionId).put("partKindFlag",partKindFlag));
	}



}
