package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.StationUserDao;
import com.honda.galc.entity.product.StationUser;
import com.honda.galc.entity.product.StationUserId;
import com.honda.galc.service.Parameters;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiStationRepairMethodDaoImpl</code> is an implementation class for QiStationRepairMethodDao interface.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>15/06/2016</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */

public class StationUserDaoImpl extends BaseDaoImpl<StationUser, StationUserId> implements StationUserDao{

	
	private static final String UPDATE_STATION_USER = "UPDATE StationUser E SET E.loginTimestamp= :timestamp WHERE E.id.hostName= :hostName AND E.id.user= :user";

	private static final String DELETE_STATION_USER = "DELETE from StationUser E  WHERE E.id.hostName= :hostName AND E.id.user= :user";

	private static final String FIND_RECENT_USERS_BY_HOST_NAME= "Select e from StationUser e where e.id.hostName= :hostName and e.loginTimestamp >= :loginTimestamp order by e.loginTimestamp desc ";

	private static final String FIND_ALL_RECENT_USERS = "select station.USER,station.LOGIN_TIMESTAMP,station.HOST_NAME from GALADM.STATION_USER_TBX station where  station.HOST_NAME=?1 order by LOGIN_TIMESTAMP";
	
	
	public List<StationUser> findAllStationUsersByHost(String hostName) {
            return findAll(Parameters.with("id.hostName", hostName),new String[]{"id.user"},true);
	}

	public List<StationUser> findAllStationUser(String hostName) {
		Parameters params = Parameters.with("1", hostName);
		return findAllByNativeQuery(FIND_ALL_RECENT_USERS,params);
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NESTED)
	public void updateTimeStampByStationUser(StationUser qiStationUser) {
		Parameters params = Parameters.with("hostName", qiStationUser.getId().getHostName()).put("user", qiStationUser.getId().getUser()).put("timestamp", qiStationUser.getLoginTimestamp());
		executeUpdate(UPDATE_STATION_USER, params);
		
	}

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NESTED)
	public void removeUser(StationUserId qiStationUserId) {
		Parameters params = Parameters.with("hostName", qiStationUserId.getHostName()).put("user", qiStationUserId.getUser());
		executeUpdate(DELETE_STATION_USER, params);
	}
	
	/**
	 * This method finds all logged in user within a time period for a station.
	 * @return List<QiStationUser>
	 */
	public List<StationUser> findAllRecentUsersByHostName(String hostName ,Timestamp loginTimeStamp, int maxNoOfUser) {
		Parameters params = Parameters.with("hostName", hostName).put("loginTimestamp", loginTimeStamp);
		return findAllByQuery(FIND_RECENT_USERS_BY_HOST_NAME, params, maxNoOfUser);
	}
	
	

	
}
