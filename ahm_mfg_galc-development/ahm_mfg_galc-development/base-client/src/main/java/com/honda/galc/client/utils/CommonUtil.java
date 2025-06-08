package com.honda.galc.client.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.honda.galc.openprotocol.model.LastTighteningResult;


public class CommonUtil {

	public String[] listPackedResources(String path) 
	throws UnsupportedEncodingException, IOException, URISyntaxException{
		URL dirURL = Thread.currentThread().getContextClassLoader().getResource(path);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			/* A file path: easy enough */
			return new File(dirURL.toURI()).list(new FilenameFilter(){public boolean accept(File dir, String name) {
				return !name.startsWith(".");
			};});
		} 

		if (dirURL != null && dirURL.getProtocol().equals("jar")) {
			/* A JAR path */
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
			while(entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith(path) && !name.endsWith("/")) { //filter according to the path
					result.add(name.substring(path.length() +1));
				}
			}
			return result.toArray(new String[result.size()]);
		} 

		throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	}
	
	public static String toString(LastTighteningResult result){
		StringBuilder sb = new StringBuilder();
		if (result != null) {
			sb.append(" tighteningId=").append(result.getTighteningId()).append(",");
			sb.append(" productId=").append(result.getProductId().trim()).append(",");
			sb.append(" torque=").append(result.getTorque()).append(",");
			sb.append(" torqueStatus=").append(result.getTorqueStatus()).append(",");
			sb.append(" angle=").append(result.getAngle()).append(",");
			sb.append(" angleStatus=").append(result.getAngleStatus());
		}
		return sb.toString();
	}
}
