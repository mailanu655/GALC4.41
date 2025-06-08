package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.DataCollectionImage;
import com.honda.galc.service.IDaoService;

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
public interface DataCollectionImageDao extends IDaoService<DataCollectionImage, Integer> {
	public List<DataCollectionImage> findAllWithoutImageData();
	public List<DataCollectionImage> findAllActiveWithoutImageData();

}
