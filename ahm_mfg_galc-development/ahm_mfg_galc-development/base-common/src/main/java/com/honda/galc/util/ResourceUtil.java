package com.honda.galc.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceUtil {
	public static String[] listPackedResources(String path) 
	throws UnsupportedEncodingException, IOException, URISyntaxException{
		URL dirURL = Thread.currentThread().getContextClassLoader().getResource(path);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			/* A file path: easy enough */
			return new File(dirURL.toURI()).list(new FilenameFilter(){public boolean accept(File dir, String name) {
				return !name.startsWith(".");
			};});
		} 

		if (dirURL.getProtocol().equals("jar")) {
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
	
	public static String[] listResourcesFromFile(String path) 
	throws UnsupportedEncodingException, IOException, URISyntaxException{
		
		List<String> list = new ArrayList<String>();
		URL dirURL = Thread.currentThread().getContextClassLoader().getResource(path);
		Scanner scanner = new Scanner(dirURL.openStream());
		try {
			
			while (scanner.hasNextLine()){
				list.add(scanner.nextLine().trim());
			}
		} catch(Exception e){
			throw new UnsupportedOperationException("Cannot list files for URL "+path);
		}
		finally{
			scanner.close();
		}
		
		return list.toArray(new String[0]);

		
	}

}
