package com.honda.galc.dao.jpa.conf;


import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.AdminUserDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.AdminUser;

/**
 * 
 * <h3>AdminUserDaoImpl Class description</h3>
 * <p> AdminUserDaoImpl description </p>
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
 * Nov 30, 2010
 *
 *
 */
public class AdminUserDaoImpl extends BaseDaoImpl<AdminUser,String> implements AdminUserDao {
    
	private static String FIND_ALL_WILDCARD = "Select a from AdminUser a where a.userId like ";
	
	private static String UPDATE = "update galadm.GAL600TBX a set a.DISPLAY_NAME = ? , a.USER_DESC = ?, a.PASSWORD = ?  where a.USER_ID = ?";
	private static String UPDATE_PRESERVING_PWD = "update galadm.GAL600TBX a set a.DISPLAY_NAME = ? , a.USER_DESC = ? where a.USER_ID = ?";
	private static String INSERT = "INSERT INTO galadm.GAL600TBX(USER_ID, DISPLAY_NAME, PASSWORD,USER_DESC)	VALUES (?, ?, ?,?)"; 

	public List<AdminUser> findAllMatchUserId(String wildcard) {
		
		return findAllByQuery(FIND_ALL_WILDCARD + "'" + wildcard + "'");
		
	}
	
	@Transactional
	public AdminUser insert(AdminUser user) {
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(INSERT);
			pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getDisplayName());
            pstmt.setBytes(3, encryptPassword(user.getPassword()));
            pstmt.setString(4,user.getUserDesc());
            
            pstmt.executeUpdate();
            Logger.getLogger().check("Executed updated query : " + pstmt.toString());
            
		} catch (SQLException e) {
			
			throw new SystemException("SQL Exception ",e);
		}
		
		return user;

	}
	
	@Transactional
	public AdminUser update(AdminUser user) {
		return update(user, true);
	}
	
	@Transactional
	public AdminUser update(AdminUser user, boolean updatePassword) {

		// have the problem to exceuteNative update due to Password is stored as byte[] on db, use native jdbc instead here
		try {
			PreparedStatement pstmt = getConnection().prepareStatement((updatePassword) ? UPDATE : UPDATE_PRESERVING_PWD);
			int paramIndex = 1;
            pstmt.setString(paramIndex++, user.getDisplayName());
            pstmt.setString(paramIndex++, user.getUserDesc());
            if(updatePassword){
				pstmt.setBytes(paramIndex++, encryptPassword(user.getPassword()));
			}
            pstmt.setString(paramIndex,user.getUserId());
            pstmt.executeUpdate();
            
		} catch (SQLException e) {
			
			throw new SystemException("SQL Exception ",e);
		}
		
		return user;
	}

	private byte[] encryptPassword(String newPass){
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5", "IBMJCE");
			md.reset();
			md.update(newPass.getBytes());
			return md.digest();
        } catch (Exception ex) {
        	throw new SystemException("Error getting MD5 instance",ex);
        }
	}
	
	 
	private Connection getConnection() {
		try {
	       InitialContext context = new InitialContext();
	       DataSource ds = (javax.sql.DataSource)context.lookup("jdbc/galdb-ds5");
	       return ds.getConnection();
	       
		} catch (Exception e) {
	   	   throw new SystemException(e.toString());
		}
	}
}
