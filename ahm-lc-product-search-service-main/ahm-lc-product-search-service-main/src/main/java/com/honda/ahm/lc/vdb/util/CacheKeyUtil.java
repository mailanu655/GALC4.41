package com.honda.ahm.lc.vdb.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.ahm.lc.config.TenantContext;
import com.honda.ahm.lc.vdb.web.AddHttpHeaderFilter;

import java.sql.Time;

public class CacheKeyUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheKeyUtil.class);

	private CacheKeyUtil() {

	}

	public static String generateCacheKey(Object... params) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		String combined = Arrays.stream(params).map(param -> {
			if (param == null) {
				return "null";
			} else if (param instanceof java.sql.Date) {
				return dateFormat.format((java.sql.Date) param);
			} else if (param instanceof java.util.Date) {
				return dateFormat.format(param).replaceAll("-", "");
			} else if (param instanceof Time) {
				return timeFormat.format((Time) param);
			} else {
				return param.toString();
			}
		}).collect(Collectors.joining("-"));

		return hashKey(combined + "-" + TenantContext.getCurrentTenant());
	}

	private static String hashKey(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
			return null;
			// throw new RuntimeException("Error generating cache key", e);
		}
	}
}
