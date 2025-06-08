package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiImageSectionPoint;
import com.honda.galc.entity.qi.QiImageSectionPointId;
import com.honda.galc.service.IDaoService;
/** * *
 * @version 0.1
* @author Abhishek Garg
* @since May 18, 2016
*/
public interface QiImageSectionPointDao extends IDaoService<QiImageSectionPoint, QiImageSectionPointId>{
	public List<QiImageSectionPoint> findPolygonPoints(int partLocationId, String imageName);
	public int deleteAllByImageSetionId(int imageSectionId);
	public List<QiImageSectionPoint> findAllByImageName(String imageName);
	public List<QiImageSectionPoint> findPolygonPoints(int imageSectionId);
	public List<QiImageSectionPoint> findAllByDefectFilter(String productKind, String entryScreen, String imageName, String mtcModel, String processPointId, String productType, String mainPartNo);
}
