package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiImageDao;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiImageDaoImpl Class description</h3>
 * <p> QiImageDaoImpl description </p>
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

public class QiImageDaoImpl extends BaseDaoImpl<QiImage, String> implements QiImageDao {
	private static String UPDATE_IMAGE_STATUS = "update QI_IMAGE_TBX  set ACTIVE = ?1 , UPDATE_USER = ?2 where BITMAP_FILE_NAME= ?3";
	
	private static String FIND_IMAGE_BY_FILTER= "select e.IMAGE_NAME,e.ACTIVE from QI_IMAGE_TBX e where (" +
			"e.IMAGE_NAME like ?1 or " +
			"e.ACTIVE = ?2) and e.ACTIVE in (?3,?4) and e.PRODUCT_KIND=?5 order by e.IMAGE_NAME";
	
	private static String CHECK_DUPLICATE_IMAGE_NAME = "select e from QiImage e where e.imageName= :image";
	
	private static String FIND_ALL_BY_PRODUCT_KIND = "select e.IMAGE_NAME,e.ACTIVE from QI_IMAGE_TBX e where " +
			"e.IMAGE_NAME like ?1 and e.PRODUCT_KIND=?2 and e.ACTIVE = 1 order by e.IMAGE_NAME";
	
	/**
	 * To Filter the table data.
	 * @param filterValue- Input from the user in filter
	 * @param productKind- Product Kind of the image
	 * @param statusList- Status of the image
	 * @return the the number of rows based on filtered value
	 */
	public List<QiImageSectionDto> findImageByFilter(String filterValue, String productKind,List<Short> statusList) {
		Parameters params = Parameters.with("1", "%" +filterValue+ "%")
				.put("2",((filterValue.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(filterValue.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2))
				.put("3", statusList.get(0))
				.put("4", statusList.get(1))
				.put("5", productKind);
		return findAllByNativeQuery(FIND_IMAGE_BY_FILTER, params,QiImageSectionDto.class);
	}
	
	/**
	 * To Update Image Status.
	 * @param bitmapFileName- the bitmap file name of image
	 * @param active- 1=active, 0 =inactive
	 * @param user User Id
	 */
	@Transactional
	public void updateImageStatus(String bitmapFileName, short active, String user) {
		Parameters params = Parameters.with("1", active)
				.put("2", user).put("3",bitmapFileName);
		executeNativeUpdate(UPDATE_IMAGE_STATUS, params);
	}
	
	/**
	 * To check whether Image Name exists.
	 * @param imageName- the image name  
	 * @return true, if is duplicate image name
	 */
	public boolean isDuplicateImageName(String imageName)
	{
		Parameters params = Parameters.with("image", imageName);
		return !findAllByQuery(CHECK_DUPLICATE_IMAGE_NAME,params).isEmpty();
	}
	/**
	 * To get Image based on Image Name.
	 * @param imageName- the bitmap file name of image
	 */
	public QiImage findImageByImageName(String imageName) {
		return findFirst(Parameters.with("imageName", imageName));
	}
	
	public List<QiImage> findAllByImageName(List<String> imageNames) {
		return findAll(Parameters.with("imageName", imageNames));
	}
	
	public List<QiImageSectionDto> findAllByProductKind(String filterValue, String productKind) {
		Parameters params = Parameters.with("1", "%" +filterValue+ "%")
				.put("2", productKind);
		return findAllByNativeQuery(FIND_ALL_BY_PRODUCT_KIND, params,QiImageSectionDto.class);
	}

	@Transactional
	public void updateImage(String imageName, String imageDescription, String updateUser, String bitmapFileName) {

		update(Parameters.with("imageName", imageName).put("imageDescription", imageDescription).put("updateUser", updateUser), Parameters.with("bitmapFileName", bitmapFileName));
	}

	@Transactional
	public void updateImage(byte[] imageData, String updateUser, String bitmapFileName) {

		update(Parameters.with("imageData", imageData).put("updateUser", updateUser), Parameters.with("bitmapFileName", bitmapFileName));
	}
}
