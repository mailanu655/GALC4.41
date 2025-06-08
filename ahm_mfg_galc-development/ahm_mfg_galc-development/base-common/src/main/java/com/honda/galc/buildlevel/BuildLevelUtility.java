package com.honda.galc.buildlevel;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * @author martinek
 */
public class BuildLevelUtility implements Serializable {

	private static final long serialVersionUID = 1L;
	private static BuildLevelUtility util;
	private Properties buildInfo;

	private BuildLevelUtility() {
		ClassLoader cl = this.getClass().getClassLoader();
		buildInfo = new Properties();
		try {
			InputStream is = cl.getResourceAsStream("com/honda/galc/buildlevel/BuildLevelUtility.properties");
			buildInfo.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BuildLevelUtility getInstance() {
		if (util == null) {
			util = new BuildLevelUtility();
		}
		return util;
	}

	private String getProperty(String key) {
		return buildInfo.getProperty(key);
	}

	public static void main(String[] args) {
		util = getInstance();
		System.out.println("Build Comment: " + getBuildComment());
		System.out.println("Build Tag: " + getCvstag());
		System.out.println("Build Timestamp: " + getBuildTimestamp());
	}

	/**
	 * Returns the buildComment.
	 * 
	 * @return String
	 */
	public static String getBuildComment() {
		return getInstance().getProperty("build.comment");
	}

	/**
	 * Returns the buildTimestamp.
	 * 
	 * @return String
	 */
	public static String getBuildTimestamp() {
		return getInstance().getProperty("build.timestamp");
	}

	/**
	 * Returns the cvstag.
	 * 
	 * @return String
	 */
	public static String getCvstag() {
		return getInstance().getProperty("build.cvstag");
	}
}
