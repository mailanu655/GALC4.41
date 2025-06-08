package com.honda.galc.dao.qics;

import java.util.List;

import com.honda.galc.entity.qics.Image;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>ImageDao Class description</h3>
 * <p> ImageDao description </p>
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
public interface ImageDao extends IDaoService<Image, String> {

	public Image findByImageName(String imageName);
	
	public String getBase64ByImageName(String imageName);
	
	public List<Image> findAllImageNames();
	
}
