package com.honda.galc.dao.jpa.conf;


import java.util.List;


import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.User;
import com.honda.galc.service.Parameters;

public class UserDaoImpl extends BaseDaoImpl<User,String> implements UserDao {

    
	 private static final String  FIND_USER = "select * from galadm.GAL105TBX  where UPPER(USER_ID) like UPPER(?1) or UPPER(USER_NAME) like UPPER(?1) order by USER_ID";
	 
	 private static final String SORTED_USER = "select trim(USER_NAME)||' ('||trim(USER_ID)||')' as userId from galadm.GAL105TBX order by USER_NAME";
    		
    /**
	 * This method is to find Users By Filter with sorted 
	 */
    public List<User> findAllUsersByFilter(String filterData) {
			 Parameters params = Parameters.with("1", "%"+filterData+"%");
			return findAllByNativeQuery(FIND_USER, params);
	}
   
	/**
   	 * This method is to find and sort all users 
   	 */
	public List<User> findAllSortedByUserId(){
		return findAll(null,new String[]{"userId"},true);
	}
	
	/**
   	 * This method is to find and sort all users 
   	 */
	public List<String> findAllUser(){
		return findAllByNativeQuery(SORTED_USER, null,String.class);
	}
}
