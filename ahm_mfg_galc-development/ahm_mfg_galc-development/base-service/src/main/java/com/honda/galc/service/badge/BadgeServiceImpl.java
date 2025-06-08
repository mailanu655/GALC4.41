package com.honda.galc.service.badge;

import java.net.HttpURLConnection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.BadgeInfoDto;
import com.honda.galc.net.HttpClient;
import com.honda.galc.property.BadgeInfoPropertyBean;
import com.honda.galc.service.BadgeService;
import com.honda.galc.service.property.PropertyService;

public class BadgeServiceImpl implements BadgeService {
	private Logger logger = Logger.getLogger("BadgeServiceImpl");
	BadgeInfoPropertyBean badgeInfoPropertyBean;

	public BadgeInfoDto getUserByBadge(int badgeId) {
		try {
			String responseJson = null;
			if (StringUtils.isNotEmpty(getBadgeInfoPropertyBean().getRestUrl())) {
				String url = getBadgeInfoPropertyBean().getRestUrl() + "/msip-badge-ldap/findLdapUserByBadge";
				String data = "{\"BadgeId\": " + badgeId + "}";

				responseJson = HttpClient.post(url, data, HttpURLConnection.HTTP_OK, getRequestHeaders());
				logger.info("REST call - URL: " + url + "; Result: " + responseJson);
				
				Gson gson = new Gson();
				BadgeInfoDto[] badgeInfoDtos = gson.fromJson(responseJson, BadgeInfoDto[].class);
				for (BadgeInfoDto badgeInfoDto : badgeInfoDtos) {
					badgeInfoDto.setUser_id(removePrefix(badgeInfoDto.getUser_id()));
					return badgeInfoDto;
				}
			} else {
				logger.info("Rest Url is not configured");
			}

		} catch (Exception e) {
			logger.error(e, "Failed to execute BadgeService REST call");
		}
		return null;
	}

	public BadgeInfoPropertyBean getBadgeInfoPropertyBean() {
		if (badgeInfoPropertyBean == null) {
			badgeInfoPropertyBean = PropertyService.getPropertyBean(BadgeInfoPropertyBean.class);
		}
		return badgeInfoPropertyBean;
	}

	private String removePrefix(String userid) {
		String[] prefixes = getBadgeInfoPropertyBean().getAssociatePrefixes();

		for (String prefix : prefixes) {
			if (userid.toLowerCase().startsWith(prefix.toLowerCase())) {
				userid = "" + userid.substring(prefix.length());
				break;
			}
		}
		return userid;
	}

	private Map<String, String> getRequestHeaders() {
		Map<String, String> headers = getBadgeInfoPropertyBean().getRequestHeader();
		return headers;
	}
}
