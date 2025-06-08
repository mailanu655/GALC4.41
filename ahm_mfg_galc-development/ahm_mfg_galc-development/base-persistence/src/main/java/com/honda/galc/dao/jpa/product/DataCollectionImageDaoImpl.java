package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DataCollectionImageDao;
import com.honda.galc.entity.product.DataCollectionImage;

/**
 * <h3>Class description</h3>
 * 
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 12, 2017</TD>
 * <TD>1.0</TD>
 * <TD>20170912</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */
public class DataCollectionImageDaoImpl extends BaseDaoImpl<DataCollectionImage, Integer> implements DataCollectionImageDao {
	
	private static final String FIND_ALL_WITHOUT_IMAGE_DATA = "select p.image_id, p.image_name, p.image_description, p.active from galadm.data_collection_image_tbx p order by p.image_name";
	private static final String FIND_ALL_ACTIVE_WITHOUT_IMAGE_DATA = "select p.image_id, p.image_name, p.image_description, p.active from galadm.data_collection_image_tbx p where p.active=1 order by p.image_name";

	public List<DataCollectionImage> findAllWithoutImageData() {
		return findAllByNativeQuery(FIND_ALL_WITHOUT_IMAGE_DATA, null, DataCollectionImage.class);
	}

	public List<DataCollectionImage> findAllActiveWithoutImageData() {
		return findAllByNativeQuery(FIND_ALL_ACTIVE_WITHOUT_IMAGE_DATA, null, DataCollectionImage.class);
	}
}
