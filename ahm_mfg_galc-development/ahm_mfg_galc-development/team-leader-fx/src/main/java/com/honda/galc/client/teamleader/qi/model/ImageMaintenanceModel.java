package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiImageDao;
import com.honda.galc.dao.qi.QiImageSectionDao;
import com.honda.galc.dao.qi.QiImageSectionPointDao;
import com.honda.galc.dao.qi.QiPartLocationCombinationDao;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiImageSectionPoint;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ImageMaintenanceModel Class description</h3>
 * <p> ImageMaintenanceModel description </p>
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
 * @author L&T Infotech<br>
 * 
 *
 */

public class ImageMaintenanceModel extends QiModel {
	
	public ImageMaintenanceModel() {
		super();
	}
	
	/** 
	 * Filter Data on the basis of Image Name and status
	 */
	public List<QiImageSectionDto> findImageByFilter(String filterValue,List<Short> statusList) {
		return getDao(QiImageDao.class).findImageByFilter(
				filterValue,getProductKind(),statusList);
	}
	/** 
	 * This method gets called when user clicks on Create Image
	 *  
	 */
	public QiImage createImage(QiImage image) {
			return getDao(QiImageDao.class).insert(image);
	}
	
	/**
	 * This method gets called when user clicks on Activate/Inactivate button to change Image Status
	 */
	public void updateImageStatus(String name, short active) {
		getDao(QiImageDao.class).updateImageStatus(name, active,getUserId());
	}
	
	/**
	 * This method gets called when user clicks on Update and Replace Image
	 */
	public QiImage updateImage(QiImage image)  {
			return getDao(QiImageDao.class).save(image);
	}
	
	/**
	 * This method checks whether same imageFileName name exists in the database
	 * @param imageFileName
	 */
	public boolean isImageFileNameExists(String imageFileName) {
		return (getDao(QiImageDao.class).findByKey(imageFileName) != null);
	}
	/**
	 * This method checks whether same imageName name exists in the database
	 * @param imageName
	 */
	public boolean isImageNameExists(String imageName){
		return getDao(QiImageDao.class).isDuplicateImageName(imageName);
	}
	/**
	 * This method is used to fetch the Image info based on the filtered data .
	 */
	public List<QiImageSection> findImageSectionByFilter(String queryString) {
		return getDao(QiImageSectionDao.class).findImageByFilter(
				queryString);
	}
	
	/**
	 * Find filtered Part Location Combinations
	 */
	public List<QiImageSectionDto> getPartLocCombByFilter(String partLocCombFilter, List<Short> statusList) {
		List<QiImageSectionDto> filterDataDtoList = new ArrayList<QiImageSectionDto>();
		List<QiPartLocationCombination> filterDataList = getDao(QiPartLocationCombinationDao.class).findFilteredPartLocComb(partLocCombFilter, getProductKind(), statusList);
		for (QiPartLocationCombination partLocationCombination : filterDataList) {
			filterDataDtoList.add(convertPartLocationToDTO(partLocationCombination));
		}
		return filterDataDtoList;
	}
	
	/**
	 * This method is used to save ImageSection info.
	 * @param qiImageSections
	 * @return
	 */
	public QiImageSection saveImageSection(QiImageSection qiImageSections) {
		 return getDao(QiImageSectionDao.class).save(qiImageSections);
	}

	/**
	 * This method is used to save ImageSectionPoints
	 * @param qiImageSections
	 * @return
	 */
	public void saveImageSectionPoint(List<QiImageSectionPoint> qiImageSections) {
		 getDao(QiImageSectionPointDao.class).saveAll(qiImageSections);
	}
	public List<QiImageSectionPoint> findPolygonPoints(int partLocationId, String imageName) {
		return getDao(QiImageSectionPointDao.class).findPolygonPoints(partLocationId, imageName);
	}
	public String getLineColor(){
		return PropertyService.getPropertyBean(QiPropertyBean.class).getLineColor();
	}
	public int getLineWidth(){
		return PropertyService.getPropertyBean(QiPropertyBean.class).getLineWidth();
	}
	public double getZoomFactor(){
		return PropertyService.getPropertyBean(QiPropertyBean.class).getZoomFactor();
	}
	public void deleteImageSection(QiImageSection imageSection){
		getDao(QiImageSectionDao.class).removeByKey(imageSection.getId());
	}
	public void deleteImageSectionPoint(int imageSectionId){
		getDao(QiImageSectionPointDao.class).deleteAllByImageSetionId(imageSectionId);
	}
	public List<QiImageSectionPoint> showAllImageSection(String imageName) {
		return getDao(QiImageSectionPointDao.class).findAllByImageName(imageName);
	}
	public List<QiImageSection> findAllImageSectionByFilter(String image, int partLocationId) {
		return getDao(QiImageSectionDao.class).findAllImageSectionByImageAndPart(image, partLocationId);
	}
	public List<QiImageSectionPoint> findPolygonPoints(int imageSectionId){
		return getDao(QiImageSectionPointDao.class).findPolygonPoints(imageSectionId);
	}
	public void updateImageSection(int partLocationId, String updateUser, int imageSectionId) {
		 getDao(QiImageSectionDao.class).updateImageSection(partLocationId, updateUser, imageSectionId);
	}
	public void updateImageSectionPoint(List<QiImageSectionPoint> qiImageSections) {
		 getDao(QiImageSectionPointDao.class).updateAll(qiImageSections);
	}
	
	public List<QiImageSectionDto> findPartNameByImageSectionId(int imageSectionId){
		List<QiImageSectionDto> partLocationDtoList = new ArrayList<QiImageSectionDto>();
		List<QiImageSection> qiImageSections = getDao(QiImageSectionDao.class).findImageSectionByImageSectionId(imageSectionId);
		 for(QiImageSection imageSection : qiImageSections){
			 QiPartLocationCombination partLocation = getDao(QiPartLocationCombinationDao.class).findByKey(imageSection.getPartLocationId());
			 QiImageSectionDto dto = convertPartLocationToDTO(partLocation);
			 dto.setImageSectionId(imageSectionId);
			 partLocationDtoList.add(dto);
		 }
		 return partLocationDtoList;
	}
	
	private QiImageSectionDto convertPartLocationToDTO(QiPartLocationCombination partLocationCombination) {
		QiImageSectionDto dto = new QiImageSectionDto();
		dto.setPartLocationId(partLocationCombination.getPartLocationId());
		dto.setInspectionPartName(partLocationCombination.getInspectionPartName());
		dto.setInspectionPartLocationName(partLocationCombination.getInspectionPartLocationName());
		dto.setInspectionPartLocation2Name(partLocationCombination.getInspectionPartLocation2Name());
		dto.setInspectionPart2Name(partLocationCombination.getInspectionPart2Name());
		dto.setInspectionPart2LocationName(partLocationCombination.getInspectionPart2LocationName());
		dto.setInspectionPart2Location2Name(partLocationCombination.getInspectionPart2Location2Name());
		dto.setInspectionPart3Name(partLocationCombination.getInspectionPart3Name());
		return dto;
	}
	
	public int getMaxSectionId() {
		return getDao(QiImageSectionDao.class).getMaxSectionId();
	}
	
	public int getImageSectionCount(int imageSectionId) {
		return getDao(QiImageSectionDao.class).getImageSectionCount(imageSectionId);
	}
	
	public void deleteImageSectionById(QiImageSection imageSection){
		getDao(QiImageSectionDao.class).deleteImageSectionById(imageSection);
	}
	
	public void saveAllImageSections(List<QiImageSection> qiImageSections) {
		 getDao(QiImageSectionDao.class).saveAll(qiImageSections);
	}
	public void deleteImageSection(int imageSectionId){
		getDao(QiImageSectionDao.class).deleteAllByImageSetionId(imageSectionId);
	}
	

	public void updateImageNameForImageSection(String newImageName, String updateUser, String oldImageName) {
		getDao(QiImageSectionDao.class).updateImageName(newImageName, updateUser, oldImageName);
	}

	public List<String> findAllImageSectionsByImageName(String imageName) {
		return getDao(QiImageSectionDao.class).findAllByImageName(imageName);
	}
	
	public void updateImageNameForEntryScreen(String newImageName, String updateUser, String oldImageName) {
		getDao(QiEntryScreenDao.class).updateImageName(newImageName, updateUser, oldImageName);
	}
	
	public Long findEntryScreensCountByImageName(String imageName) {
		return getDao(QiEntryScreenDao.class).findCountByImageName(imageName);
	}

	public List<String> findAllEntryScreensByEntryScreenDefect(String imageName) {
		return getDao(QiEntryScreenDao.class).findAllByEntryScreenDefect(imageName);
	}

	public List<String> findAllEntryScreensByStationEntry(String imageName) {
		return getDao(QiEntryScreenDao.class).findAllByStationEntry(imageName);
	}

	public void updateImage(String imageName, String imageDescription, String updateUser, String bitmapFileName) {
		getDao(QiImageDao.class).updateImage(imageName, imageDescription, updateUser, bitmapFileName);
	}

	public void updateImage(byte[] imageData, String updateUser, String bitmapFileName) {
		getDao(QiImageDao.class).updateImage(imageData, updateUser, bitmapFileName);
	}
	
	/**
	 * Find Image on the basis of imageName
	 *
	 * @param imageName
	 * @return
	 */
	public QiImage findQiImageByImageName(String imageName) {
		return getDao(QiImageDao.class).findImageByImageName(imageName);
	}
	
	public int getEntryScreenCountByImageAndPartLocation(String imageName, List<Integer> partLocationId) {
		return getDao(QiImageSectionDao.class).getEntryScreenCountByImageAndPartLocation(imageName, partLocationId);
	}
	
	public int getEntryScreenCountByImageAndSectionId(String imageName, int sectionId, List<Integer> partLocationId) {
		return getDao(QiImageSectionDao.class).getEntryScreenCountByImageAndSectionId(imageName, sectionId, partLocationId);
	}
	
	/**
	 *  This method will return list of image name having the section
	 */
	public List<String> findAllImageNameBySection() {
		return getDao(QiImageSectionDao.class).findAllImageNameBySection();
	}
}
