package com.honda.galc.rest.security;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.LoginException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.conf.UserSecurityGroupId;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.LdapPropertyBean;
import com.honda.galc.property.RestServicePropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.rest.util.RestUtils;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.LDAPService;

/**
 * @author Subu Kathiresan
 * @date Mar 18, 2017
 * 
 * HTTP Basic Authentication filter for JAX-RS
 */
@Provider
public class BasicAuthFilter implements ContainerRequestFilter {

	public static final int CACHE_TIME_MINUTES = 15;
	public static final int MILLISECS_PER_MIN = 60000;
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final int USER_ID_INDEX = 0;
	public static final int PWD_INDEX = 1;
	public static final int AUTH_INFO_LENGTH = 2;
	public static final int DB_USER_ID_LENGTH = 11;
	
	private static ConcurrentHashMap<String, String> authenticatedUsers = new ConcurrentHashMap<String, String>();
	private static ConcurrentHashMap<String, Date> loginExpiration = new ConcurrentHashMap<String, Date>();
	
	public BasicAuthFilter() {}
	
	public void filter(ContainerRequestContext requestContext) throws WebApplicationException {
		try {
			if (!PropertyService.getPropertyBean(RestServicePropertyBean.class).isUserAuthenticationEnabled()) {
				return;
			}
			String auth = requestContext.getHeaderString(AUTHORIZATION_HEADER);
			String[] authInfo = BasicAuthDecoder.decode(auth);

			if (authInfo.length != AUTH_INFO_LENGTH) {
				logParamError(authInfo);
			} else {
				getLogger().info(requestContext.getUriInfo().getPath() 
						+ (requestContext.getUriInfo().getQueryParameters().isEmpty() ? "" : "?" 
						+ requestContext.getUriInfo().getQueryParameters())
						+ " requested by " + authInfo[BasicAuthFilter.USER_ID_INDEX]);
				if (authenticateUser(authInfo[USER_ID_INDEX], authInfo[PWD_INDEX])){
					return;
				}
			} 
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Authorization failed " + StringUtils.trimToEmpty(ex.getMessage()));
		}
		throw new WebApplicationException(Status.UNAUTHORIZED);
	}
	
	public boolean authenticateUser(String userId, String password) {
		if (isAlreadyAuthenticated(userId, password)) {
			return true;
		} 
		
		if (authenticate(userId, password).equals(LoginStatus.OK)) {
			addToAuthUsersCache(userId, password);
			return true;
		}	
		return false;
	}

	private void addToAuthUsersCache(String userId, String password) {
		getAuthenticatedUsers().put(userId, password);
		getLoginExpiration().put(userId, new Date(new Date().getTime() + (CACHE_TIME_MINUTES * MILLISECS_PER_MIN)));
	}

	private boolean isAlreadyAuthenticated(String userId, String password) {
		// return true only if already authenticated, password matches, and not expired
		if (getAuthenticatedUsers().containsKey(userId) 
			&& password.equals(getAuthenticatedUsers().get(userId))
			&& getLoginExpiration().get(userId).after(new Date())) {
				getLogger().info(userId + " already authenticated");
				return true;
		} 
		return false;
	}
	
	private LoginStatus authenticate(String userId, String password) {
		if (isLdap()) {
			return authenticateUsingLdap(userId, password);
		} else {
			return authenticateUsingDb(userId, password);
		}
	}

	private LoginStatus authenticateUsingLdap(String userId, String password) {
		try {
			List<UserSecurityGroupId> userGroups = LDAPService.getInstance().authenticate(userId, password);
			if (!userGroups.isEmpty()) {
				String defaultGroup = PropertyService.getPropertyBean(LdapPropertyBean.class).getDefaultGroup();
				UserSecurityGroupId group = new UserSecurityGroupId(userId, defaultGroup);
				
				//if the user has only default group, authentication fails
				if (userGroups.size() == 1 && userGroups.contains(group)) {
					return LoginStatus.AUTHENTICATION_ERROR;
				}
				
				getLogger().info(userId + " authenticated successfully");
				return LoginStatus.OK;
			}
		} catch (LoginException ex) {
			ex.printStackTrace();
			return Enum.valueOf(LoginStatus.class, ex.getMessage());
		}
		return LoginStatus.AUTHENTICATION_ERROR;
	}
	
	private LoginStatus authenticateUsingDb(String userId, String password) {
		if (StringUtils.trim(userId).length() > DB_USER_ID_LENGTH) {
			return LoginStatus.USER_NOT_EXIST;
		}

		User user = findUser(userId);
		if(user == null) return LoginStatus.USER_NOT_EXIST;
		if(password != null && password.equals(user.getPasswd())) {
			if(user.isPasswordExpired()) {
				return LoginStatus.PASSWORD_EXPIRED;
			}
			getLogger().info(userId + " authenticated successfully");
			return LoginStatus.OK;
		} else {
			return LoginStatus.PASSWORD_INCORRECT;
		}
	}

	private void logParamError(String[] authInfo) {
		if (StringUtils.trimToEmpty(authInfo[USER_ID_INDEX]).equals("")) {
			getLogger().warn("Authorization failed: User name not provided");
		}
		if (StringUtils.trimToEmpty(authInfo[PWD_INDEX]).equals("")) {
			getLogger().warn("Authorization failed: Password not provided");
		}
	}

	private User findUser(String userName) {
		return ServiceFactory.getDao(UserDao.class).findByKey(userName);
	}

	private boolean isLdap(){
		return PropertyService.getPropertyBean(SystemPropertyBean.class).isLdap();
	}
	
	public static Logger getLogger(){
		return Logger.getLogger(RestUtils.LOGGER_ID);
	}

	public ConcurrentHashMap<String, Date> getLoginExpiration() {
		return loginExpiration;
	}

	public ConcurrentHashMap<String, String> getAuthenticatedUsers() {
		return authenticatedUsers;
	}
}