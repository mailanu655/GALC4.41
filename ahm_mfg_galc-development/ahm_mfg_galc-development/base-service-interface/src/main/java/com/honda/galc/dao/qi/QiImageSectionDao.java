package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiImageSectionId;
import com.honda.galc.service.IDaoService;

/*
 * @author L&T Infotech
 */
public interface QiImageSectionDao extends IDaoService<QiImageSection, QiImageSectionId>{
	public List<QiImageSection> findImageByFilter(String filterData);
	public List<QiImageSection> findPartLocationIdInImageSection(List<Integer> partLocationIdList);
	public List<QiImageSection> findAllImageSectionByImageAndPart(String image, int partLocationId);
	public void updateImageSection(int partLocationId, String updateUser, int imageSectionId);
	public int getMaxSectionId();
	public int getImageSectionCount(int imageSectionId);
	public int deleteImageSectionById(QiImageSection imageSection);
	public List<QiImageSection> findImageSectionByImageSectionId(int imageSectionId);
	public int deleteAllByImageSetionId(int imageSectionId);
	public void updateImageName(String newImageName, String updateUser, String oldImageName);
	public List<String> findAllByImageName(String imageName);
	public int getEntryScreenCountByImageAndPartLocation(String imageName, List<Integer> partLocationId);
	public int getEntryScreenCountByImageAndSectionId(String imageName, int sectionId, List<Integer> partLocationId);
	public List<String> findAllImageNameBySection();
	public List<Integer> findAllPartLocationIdsWithValidSectionByImageName(String entryScreen, String imageName);
}
