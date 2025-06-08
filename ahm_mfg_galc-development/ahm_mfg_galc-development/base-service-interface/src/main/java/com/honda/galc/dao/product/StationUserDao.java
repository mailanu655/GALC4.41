package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.entity.product.StationUser;
import com.honda.galc.entity.product.StationUserId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiBomPartDao Class description</h3>
 * <p>
 * QiBomPartDao is used to declare the methods required for the operation on Bom Part table on screen
 * </p>
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
 *        MAY 06, 2016
 * 
 */
public interface StationUserDao extends IDaoService<StationUser, StationUserId> {


	List<StationUser> findAllStationUser(String hostName);

	List<StationUser> findAllStationUsersByHost(String hostName);

	void updateTimeStampByStationUser(StationUser qiStationUser);

	void removeUser(StationUserId qiStationUserid);

	public List<StationUser> findAllRecentUsersByHostName(String hostName, Timestamp loginTimeStamp, int maxNoOfUser) ;
	
	
			
}