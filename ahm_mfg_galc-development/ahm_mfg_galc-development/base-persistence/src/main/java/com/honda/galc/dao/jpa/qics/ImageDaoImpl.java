package com.honda.galc.dao.jpa.qics;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.util.Base64Coder;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qics.ImageDao;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>ImageDaoImpl Class description</h3>
 * <p> ImageDaoImpl description </p>
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
public class ImageDaoImpl extends BaseDaoImpl<Image,String> implements ImageDao{

	private static final String FIND_ALL_IMAGE_NAMES = "SELECT IMAGE_NAME, BITMAP_FILE_NAME FROM GALADM.GAL173TBX";
	
	public Image findByImageName(String imageName) {

		List<Image> images = findAll(Parameters.with("imageName", imageName));
		return images.isEmpty()? null : images.get(0);
		
	}

	/**
	 * get only image name and bitmap file names
	 */
	public List<Image> findAllImageNames() {
		List<Object[]> imageNames = findAllByNativeQuery(FIND_ALL_IMAGE_NAMES, null, Object[].class);
		List<Image> images = new ArrayList<Image>();
		for (Object[] items : imageNames) {
			Image image = new Image();
			image.setImageName((String)items[0]);
			image.setBitmapFileName((String)items[1]);
			images.add(image);
		}
		return images;
	}

	public String getBase64ByImageName(String imageName) {
		Image image = findFirst(Parameters.with("imageName", imageName));
		if(image != null)
			return new String(Base64Coder.encode(image.getImageData())).replace("\r\n", "");
		else
			return "";
	}
	



}
