package com.honda.galc.tools.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

/**
 * <h3>Class description</h3>
 * This is the class to handle client configuration files.
 * .
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Aug 17, 2012</TD>
 * <TD>1.0</TD>
 * <TD>20120817</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */
public class FileHandler {
	private static final String SERVER_FILE_NAME = "servers.txt";
	private String path = ".";
	private String filename = "process_point.xml";
	
	public FileHandler(String path, String filename) {
		this.path = path;
		this.filename = filename;
	}

	public static boolean isFileExist(String path, String filename) {
		File file = new File(path, filename);
		return file.exists() && file.isFile();
	}
	
	public void write(String content)  {
		FileWriter writer = null;
        try {
        	File file = new File(path, filename);
        	writer = new FileWriter(file);
            writer.write(content);
            System.out.println("Process point was saved to " + filename);
        } catch (IOException e) {
			System.out.println("Unable to write to file " + filename);
			e.printStackTrace();
        } finally {
			try {
				if(writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (IOException e) {
				System.out.println("Unable to close file " + filename);
				e.printStackTrace();
			}
        }
    }

	public GalcClientConfiguration readConfiguration() {
		GalcClientConfiguration config = null;
		FileReader reader = null;
		try {
			reader = new FileReader(filename);
			XStream xStream = new XStream();
			config = (GalcClientConfiguration) xStream.fromXML(reader);
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist.");
			e.printStackTrace();
		} catch (Exception ex) {
			System.out.println("Exception during file operation. " + ex.getMessage());
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return config;
	}
	
	public GalcSpecConfiguration readSpecs() {
		GalcSpecConfiguration config = null;
		FileReader reader = null;
		try {
			reader = new FileReader(filename);
			XStream xStream = new XStream();
			config = (GalcSpecConfiguration) xStream.fromXML(reader);
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist.");
			e.printStackTrace();
		} catch (Exception ex) {
			System.out.println("Exception during file operation. " + ex.getMessage());
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return config;
	}

	public List<String> readServerFile() {
		List<String> servers = new ArrayList<String>();
		FileInputStream stream;
		try {
			stream = new FileInputStream(SERVER_FILE_NAME);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line;
			while((line = reader.readLine()) != null) {
				servers.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return servers;
	}
}
