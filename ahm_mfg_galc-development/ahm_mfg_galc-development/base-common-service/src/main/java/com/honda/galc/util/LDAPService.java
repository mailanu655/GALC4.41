package com.honda.galc.util;
/**
 * 
 * <h3>LDAPService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LDAPService is a service that performs LDAP Authentication. </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by </TH>
 * <TH>Update date </TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH></TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Meghana G
 *   
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.honda.galc.common.exception.LoginException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.UserSecurityGroupId;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.LdapPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class LDAPService {
	private final static int ACCOUNT_DISABLED = 2;

	private static String[] ldapUrls = null;
	private static String adminId = null;
	private static String adminPassword  = null;
	private static SearchControls searchCtls = null; 
	private static String defaultGroup = null;
	private static LDAPService ldapService;
	private static String searchBase = null;

	private int domainIndex;
	private int domainCount;

	public static LDAPService getInstance() {
		if(ldapService == null ) ldapService = new LDAPService();
		return ldapService;
	}
	/**
	 * LDAPService constructor creates and initializes the LDAP Service.
	 * All properties needed to configure LDAP Service are read from DB can be changed to property file. 
	 */
	private LDAPService() {
		super();
		String ldapUrl = getLdapPropertyBean().getLdapUrl();
		ldapUrls = StringUtils.split(ldapUrl, ";");
		adminId = getLdapPropertyBean().getAdminId();
		adminPassword  = getLdapPropertyBean().getAdminPassword();
		defaultGroup = getLdapPropertyBean().getDefaultGroup();
		searchBase = getLdapPropertyBean().getSearchBase();
		searchCtls = new SearchControls();
		String attrTemp = getLdapPropertyBean().getAttr();
		String returnedAtts[]= null;
		if(null != attrTemp){
			StringTokenizer st = new StringTokenizer(attrTemp,",");
			int size = st.countTokens();
			returnedAtts = new String[size];
			int counter = 0;
			while(st.hasMoreElements()){
				returnedAtts[counter++] = st.nextToken();
			}
		}
		int scope = getLdapPropertyBean().getScope();
		searchCtls.setReturningAttributes(returnedAtts);
		searchCtls.setSearchScope(scope);
		
		//Start with a random domain controller to balance load.
		Random randomGenerator = new Random();
		domainIndex = randomGenerator.nextInt(ldapUrls.length);
		domainCount = 0;
		
		
	}

	/**
	 * This method authenticates the user using LDAP Server. It uses userName and password for authentication.
	 * @param userId
	 * @param password
	 * @return
	 * @throws LoginException
	 */
	public List<UserSecurityGroupId> authenticate(String userId,String password)throws LoginException{
		List<UserSecurityGroupId> groups = null;
		String distinguishedName = null;
		String memberOf = null; 
		Long pwdLastSet = null;
		Long accountExpires = null;

		Hashtable<String,String> envGC = getEnvironment(adminId, adminPassword);
		String searchFilter = "(&(cn="+ userId + ")(objectClass=user))";

		try{
			LdapContext ctxGC = new InitialLdapContext(envGC,null);
            //search LDAP Server using search Filter.     
			NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			if (answer.hasMoreElements()) {
				SearchResult result = (SearchResult)answer.next();
				Attributes attrs = result.getAttributes();
				// get all attributes if the user exists
				if (attrs != null) {
					//Check if the User Account has been disabled
					//This is identified by the returned values binary place holder for the decimal value 2 being a 1
					if(null != attrs.get("useraccountcontrol")){
						Attribute userAccountControlAttribute = attrs.get("useraccountcontrol");
						int userAccountControl = userAccountControlAttribute.size() > 0 ? NumberUtils.toInt(userAccountControlAttribute.get(0).toString(), ACCOUNT_DISABLED) : ACCOUNT_DISABLED;
						if((userAccountControl & ACCOUNT_DISABLED) == ACCOUNT_DISABLED){
							throw new LoginException(LoginStatus.AD_DISABLED_ACCOUNT.toString());
						}
					}
					
					//memberOf attribute retrieves the groups to which user belongs.
					//only retrieve group memberships if password is provided
					if(null != attrs.get("memberOf") && (null != password) && (password.trim().length() > 0)){
						memberOf = attrs.get("memberOf").toString();
					}
					groups = getListOfGroups(memberOf, userId);
                    //pwdLastSet retrieves the time when password was last set.
					if(null != attrs.get("pwdLastSet")){
						pwdLastSet = new Long(attrs.get("pwdLastSet").get().toString());
					}
					//accountExpires retrieves the time when account will expire.
					if(null != attrs.get("accountExpires")){
						accountExpires = new Long(attrs.get("accountExpires").get().toString());
						Calendar calendar = Calendar.getInstance();
						calendar.clear();
						calendar.set(1601, 0, 1, 0, 0);
						accountExpires = accountExpires / 10000 + calendar.getTime().getTime();
					}
					//distinguished name retrieves the distinguished name for the user.
					if(null != attrs.get("distinguishedName")){
						distinguishedName = attrs.get("distinguishedName").get().toString();
					}
				}
			}else{
				// if no attributes retrieved then user does not exist.
				throw new LoginException(LoginStatus.USER_NOT_EXIST.toString());
			}
			ctxGC.close();
             
			// verify if account is already expired.
			if ( (null != accountExpires) && (accountExpires.longValue() > 0)) {
				long today = System.currentTimeMillis();
				long expireDay = accountExpires.longValue();
                  
				if ( expireDay < today ) {
					throw new LoginException(LoginStatus.PASSWORD_EXPIRED.toString());
				}
			} 
		} catch (NamingException e) {
			Logger.getLogger().error(e, "Naming Exception occurred");
			if(checkNextDomainController())
				authenticate(userId, password);
			else
				throw new LoginException(LoginStatus.AUTHENTICATION_ERROR.toString());
		}
		if(null != distinguishedName){
			// verify the username and password if password is provided
			if((null != password) && (password.trim().length() > 0)){
				try {
					Hashtable envDC = getEnvironment(distinguishedName,password);
					DirContext ctx = new InitialDirContext(envDC);
					ctx.close();
					return groups;
				}catch (CommunicationException comEx){
					Logger.getLogger().error(comEx, "Communication Exception occurred");
					if(checkNextDomainController())
						return authenticate(userId, password);
					else
						throw new LoginException(LoginStatus.AUTHENTICATION_ERROR.toString());
				}catch (AuthenticationException authEx){
					throw new LoginException(LoginStatus.PASSWORD_INCORRECT.toString());
				}catch (NamingException nameEx){
					Logger.getLogger().error(nameEx, "Naming Exception occurred");
					if(checkNextDomainController())
						return authenticate(userId, password);
					else
						throw new LoginException(LoginStatus.AUTHENTICATION_ERROR.toString());
				}
			}else{
				return groups;
			}
		}else{
			throw new LoginException(LoginStatus.USER_NOT_EXIST.toString());
		}
	}
	
	
	
	/**
	 * This method authenticates the user using LDAP Server. It uses userName for authentication.
	 * @param userId
	 * @param password
	 * @return
	 * @throws LoginException
	 */
	public List<UserSecurityGroupId> authenticate_without_pasword(String userId)throws LoginException{
		List<UserSecurityGroupId> groups = null;
		String distinguishedName = null;
		String memberOf = null; 
		Long pwdLastSet = null;
		Long accountExpires = null;

		Hashtable<String,String> envGC = getEnvironment(adminId, adminPassword);
		String searchFilter = "(&(cn="+ userId + ")(objectClass=user))";

		try{
			LdapContext ctxGC = new InitialLdapContext(envGC,null);
            //search LDAP Server using search Filter.     
			NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			if (answer.hasMoreElements()) {
				SearchResult result = (SearchResult)answer.next();
				Attributes attrs = result.getAttributes();
				// get all attributes if the user exists
				if (attrs != null) {
					//Check if the User Account has been disabled
					//This is identified by the returned values binary place holder for the decimal value 2 being a 1
					if(null != attrs.get("useraccountcontrol")){
						int userAccountControl = NumberUtils.toInt(attrs.get("useraccountcontrol")
							.toString().replaceAll("userAccountControl: ", ""), ACCOUNT_DISABLED);
						if((userAccountControl & ACCOUNT_DISABLED) == ACCOUNT_DISABLED){
							Logger.getLogger().info("User account is disabled: " + userId);
							return null;
						}
					}
					
					//memberOf attribute retrieves the groups to which user belongs.
					if(null != attrs.get("memberOf")){
						memberOf = attrs.get("memberOf").toString();
					}
					groups = getListOfGroups(memberOf, userId);
                    //pwdLastSet retrieves the time when password was last set.
					if(null != attrs.get("pwdLastSet")){
						pwdLastSet = new Long(attrs.get("pwdLastSet").get().toString());
					}
					//accountExpires retrieves the time when account will expire.
					if(null != attrs.get("accountExpires")){
						accountExpires = new Long(attrs.get("accountExpires").get().toString());
						Calendar calendar = Calendar.getInstance();
						calendar.clear();
						calendar.set(1601, 0, 1, 0, 0);
						accountExpires = accountExpires / 10000 + calendar.getTime().getTime();
					}
					//distinguished name retrieves the distinguished name for the user.
					if(null != attrs.get("distinguishedName")){
						distinguishedName = attrs.get("distinguishedName").get().toString();
					}
				}
			}else{
				// if no attributes retrieved then user does not exist.
				throw new LoginException(LoginStatus.USER_NOT_EXIST.toString());
			}
			ctxGC.close();
             
			// verify if account is already expired.
			if ( (null != accountExpires) && (accountExpires.longValue() > 0)) {
				long today = System.currentTimeMillis();
				long expireDay = accountExpires.longValue();
                  
				if ( expireDay < today ) {
					throw new LoginException(LoginStatus.PASSWORD_EXPIRED.toString());
				}
			} 
		} catch (NamingException e) {
			Logger.getLogger().error(e, "Naming Exception occurred");
			if(checkNextDomainController())
				authenticate_without_pasword(userId);
			else
				throw new LoginException(LoginStatus.AUTHENTICATION_ERROR.toString());
		}
		if(null != distinguishedName){
			// verify the username and password if password is provided
			return groups;
		}else{
			throw new LoginException(LoginStatus.USER_NOT_EXIST.toString());
		}
	}
	
	
	private boolean checkNextDomainController()
	{	
		//Check the next domain controller, increase the number we've checked.
		//We keep both a count and an index since we start on a random index to distribute load.
		domainCount++;
		domainIndex++;
		if(domainIndex >= ldapUrls.length)
			domainIndex = 0;
		if(domainCount > ldapUrls.length)
			return false; 
		else
			return true;
	}

	private Hashtable<String,String> getEnvironment(String userId, String password) {
		Hashtable<String,String> envDC = new Hashtable<String,String>();
		envDC.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		envDC.put(Context.SECURITY_AUTHENTICATION,"simple");
		envDC.put(Context.PROVIDER_URL, ldapUrls[domainIndex]);
		envDC.put(Context.SECURITY_PRINCIPAL,userId);
		envDC.put(Context.SECURITY_CREDENTIALS,password);
		return envDC;
	}	
/**
 * This utility method retrieves the groups from memberOf attribute. The method returns the list of all groups starting with
 * APP-GALC. Group with default access rights is added to the list of groups.
 * @param memberOf
 * @param userId
 * @return
 */
	private List<UserSecurityGroupId> getListOfGroups(String memberOf, String userId) {
		List<UserSecurityGroupId> userScrityGrpList = new ArrayList<UserSecurityGroupId>();
		if(null != memberOf){
			while(memberOf.indexOf("CN=") > 0){
				memberOf = memberOf.substring(memberOf.indexOf("CN=")+3);
				String tmp = memberOf.substring(0,memberOf.indexOf(','));
				if(StringUtils.startsWithAny(tmp, getLdapPropertyBean().getUserSecurityGroupFilter())){
					UserSecurityGroupId groupId = new UserSecurityGroupId(userId, tmp);
					userScrityGrpList.add(groupId);
				}
			}
		}
		UserSecurityGroupId group = new UserSecurityGroupId(userId, defaultGroup);
		if(!userScrityGrpList.contains(group)){
			userScrityGrpList.add(group);
		}
		return userScrityGrpList;
	}
	
	/**
	 * This utility will return Associate Name from Active Directory corresponding to given user id.
	 * @param userId
	 * @return Associate Name
	 */
	public String getAssociateName(String userId) {
		Hashtable<String,String> envGC = getEnvironment(adminId, adminPassword);
		String searchFilter = "(&(cn="+ userId + ")(objectClass=user))";
		String associateName = "";
		try{
			LdapContext ctxGC = new InitialLdapContext(envGC,null);
            //search LDAP Server using search Filter.     
			NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			if (answer.hasMoreElements()) {
				SearchResult result = (SearchResult)answer.next();
				Attributes attrs = result.getAttributes();
				// get all attributes if the user exists
				if (attrs != null) {
					//givenName attribute retrieves Given Name.
					if(null != attrs.get("givenName") ){
						associateName = associateName + attrs.get("givenName").get().toString() + " ";
					}
					
                    //sn retrieves the Surname.
					if(null != attrs.get("sn")){
						associateName += attrs.get("sn").get().toString();
					}
				}
			}
			ctxGC.close();
		} catch (NamingException e) {
			Logger.getLogger().error(e, "Naming Exception occurred while retrieving associate name");
		}
		return associateName;
	}
	
	public String getUseridWithPrefix(String userLogonIdNo){
		String distinguishedName = null;
		String strStartsWithCN = null;
		Hashtable<String,String> envGC = getEnvironment(adminId, adminPassword);
		
		String searchFilter = "(&(employeeID="+ userLogonIdNo + ")(objectClass=user))";
		String useridWithPrefix = null;
		try{
			LdapContext ctxGC = new InitialLdapContext(envGC,null);
            //search LDAP Server using search Filter.     
			NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			if (answer.hasMoreElements()) {
				SearchResult result = (SearchResult)answer.next();
				Attributes attrs = result.getAttributes();
				// get all attributes if the user exists
				if (attrs != null) {
					if(null != attrs.get("distinguishedName")){
						distinguishedName = attrs.get("distinguishedName").get().toString();
						if(distinguishedName != null && distinguishedName.trim().length() >0 && distinguishedName.indexOf("CN=") >=0){
							strStartsWithCN = distinguishedName.substring(distinguishedName.indexOf("CN="));
							useridWithPrefix = strStartsWithCN.substring(strStartsWithCN.indexOf("CN=")+3, strStartsWithCN.indexOf(","));
							strStartsWithCN = null;
						}
					}
				}
			}
			ctxGC.close();
		} catch (NamingException e) {
			Logger.getLogger().error(e, "Naming Exception occurred while retrieving associate name");
		}
		return useridWithPrefix;
	}
	
	public List<UserSecurityGroupId> getMemberOf(String userLogonIdNo){
		String distinguishedName = null;
		String strStartsWithCN = null;
		Hashtable<String,String> envGC = getEnvironment(adminId, adminPassword);
		
		String searchFilter = "(&(cn="+ userLogonIdNo + ")(objectClass=user))";
		List<UserSecurityGroupId> memberOfLst = null;
		try{
			LdapContext ctxGC = new InitialLdapContext(envGC,null);
            //search LDAP Server using search Filter.     
			NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			if (answer.hasMoreElements()) {
				SearchResult result = (SearchResult)answer.next();
				Attributes attrs = result.getAttributes();
				// get all attributes if the user exists
				if (attrs != null) {
					if(null != attrs.get("memberOf")){
						String memberOfString = attrs.toString();
						String memberOf = memberOfString.substring(memberOfString.indexOf("memberof"));
						memberOfLst = getListOfGroups( memberOf, userLogonIdNo);
					}
				}
			}
			ctxGC.close();
		} catch (NamingException e) {
			Logger.getLogger().error(e, "Naming Exception occurred while retrieving associate name");
		}
		return memberOfLst;
	}	
	
	
	public List<String> getMemberList(String userLogonIdNo){
		String distinguishedName = null;
		String strStartsWithCN = null;
		Hashtable<String,String> envGC = getEnvironment(adminId, adminPassword);
		
		String searchFilter = "(&(cn="+ userLogonIdNo + ")(objectClass=user))";
		List<String> memberOfLst = null;
		try{
			LdapContext ctxGC = new InitialLdapContext(envGC,null);
            //search LDAP Server using search Filter.     
			NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			if (answer.hasMoreElements()) {
				SearchResult result = (SearchResult)answer.next();
				Attributes attrs = result.getAttributes();
				// get all attributes if the user exists
				if (attrs != null) {
					if(null != attrs.get("memberOf")){
						String memberOfString = attrs.toString();
						String memberOf = memberOfString.substring(memberOfString.indexOf("memberof"));
						memberOfLst = getListOfGroups( memberOf);
					}
				}
			}
			ctxGC.close();
		} catch (NamingException e) {
			Logger.getLogger().error(e, "Naming Exception occurred while retrieving associate name");
		}
		return memberOfLst;
	}
	
	private List<String> getListOfGroups(String memberOf) {
		List<String> userScrityGrpList = new ArrayList<String>();
		if(null != memberOf){
			while(memberOf.indexOf("CN=") > 0){
				memberOf = memberOf.substring(memberOf.indexOf("CN=")+3);
				String memberString = memberOf.substring(0,memberOf.indexOf(','));
				userScrityGrpList.add(memberString);
			}
		}
		return userScrityGrpList;
	}
	

public String getAssociateEmail(String userId) {
		Hashtable<String,String> envGC = getEnvironment(adminId, adminPassword);
		String searchFilter = "(&(&(objectClass=person)(objectCategory=user))(sAMAccountName=" + userId + "))";
		String email = "";
		try{
			LdapContext ctxGC = new InitialLdapContext(envGC,null);
            //search LDAP Server using search Filter.     
			NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			if (answer.hasMoreElements()) {
				SearchResult result = (SearchResult)answer.next();
				Attributes attrs = result.getAttributes();
				// get all attributes if the user exists
				if (attrs != null) {
					//givenName attribute retrieves Given Name.
					if(null != attrs.get("mail") ){
						email = attrs.get("mail").get().toString();
					}
					
                   
				}
			}
			ctxGC.close();
		} catch (NamingException e) {
			Logger.getLogger().error(e, "Naming Exception occurred while retrieving email");
		}
		return email;
	}
	
	
	private LdapPropertyBean getLdapPropertyBean() {
		return PropertyService.getPropertyBean(LdapPropertyBean.class);
	}
}