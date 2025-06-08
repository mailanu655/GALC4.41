package com.honda.galc.dao.qi;
/**
 * 
 * 
 * <h3>QiInspectionLocationDao Class description</h3>
 * <p>
 * QiInspectionLocationDao contains methods for 'filter the data based on the all properties of Location' and update the 'status' of the Location and 'updateUser'
 *  </p>
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
 * @author LnTInfotech<br>
 *        
 * 
 */
import java.util.List;

import com.honda.galc.entity.qi.QiInspectionLocation;
import com.honda.galc.service.IDaoService;

public interface QiInspectionLocationDao extends IDaoService<QiInspectionLocation, String>{
	/**
	 * This method is used to filter the data based on the all properties of Location and Status
	 * @param filterValue- Input from the user in filter
	 * @param productKind- Product Kind of the Inspection Location 
	 * @return the number of rows based on filtered value
	 */
	public List<QiInspectionLocation> findLocationByFilter(String filterValue,String productKind);
	/**
	 * This method is used to update the 'status' of the Location and 'updateUser'
	 * @param name - Location Name
	 * @param active- 1=active, 0 =inactive
	 * @param user- User Id
	 */
	public void updateLocationStatus(String name, short active, String user);
	/**
	 * This method is used to inActivate associated Part location Combination .
	 * @param locationName- Location Name 
	 */
	public void inactivateLocation(String locationName,String userName);
	/**
	 * This method is used to find active Inspection Locations.
	 * @param productKind- Product Kind of the Inspection Location
	 * @return the number of rows having active Inspection Locations based on product kind
	 */
	public List<QiInspectionLocation> findActiveInspectionLocationsByProductKind(String productKind);
	/**
	 * This method is used to find filtered active Inspection Locations.
	 * @param filterData- Input from the user in filter
	 * @param productKind- Product Kind of the Inspection Location 
	 * @return the number of rows which are having active Inspection Locations based on filtered value 
	 */
	public List<QiInspectionLocation> findFilteredActiveInspectionLocations(String filterData, String productKind);
	/**
	 * This method is used to find active and primary Inspection Locations.
	 * @param productKind- Product Kind of the Inspection Location
	 * @return the number of rows which are having active and primary Inspection Locations
	 */
	public List<QiInspectionLocation> findActivePrimaryInspectionLocationsByProductKind(String productKind);
	/**
	 * This method is used to find filtered active and primary Inspection Locations.
	 * @param filterData- Input from the user in filter
	 * @param productKind- Product Kind of the Inspection Location 
	 * @return the number of rows based on filtered value
	 */
	public List<QiInspectionLocation> findFilteredActivePrimaryInspectionLocations(String filterData, String productKind);
	/**
	 * To Update Location along with Location name
	 * @param qiInspectionLocation
	 * @param oldLocName
	 */
	public void updateLocation(QiInspectionLocation qiInspectionLocation, String oldLocName);
	/**
	 * This method is used to filter the data based on the all properties of Location and Status
	 * @param filterValue- Input from the user in filter
	 * @param productKind- Product Kind of the Inspection Location 
	 * @return the number of rows based on filtered value
	 */
	public List<QiInspectionLocation> findLocationByFilter(String filterValue, String status ,String productKind);
	
}
