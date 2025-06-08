 package com.honda.galc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>EngineMasterSpecTask Class description</h3>
 * <p> EngineMasterSpecTask description </p>
 * Engine Master Spec task is an OIF task, which executes every day at 5:00 am.(The setting can be change)
 * It retrieves data from file to get the original priority production. 
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
 * @author Larry Karpov 
 * mostly extract from HMA IF legacy code 
 * (FileUtility and IFFileValidation from GALC_IF project, com.honda.global.galc.system.oif.common)
 * @since Nov 05, 2013
*/

public class OIFFileUtility {

	/**
	 * 
	 * <p>
	 * @param the file that will be processed
	 * @return List of Strings
	 */
	public static List<String> loadRecordsFromFile(String fileName, Logger logger) {
		List<String> recordList = new ArrayList<String>();
		logger.debug("About to load record definition file = " + fileName);
	    RandomAccessFile vFile = null;
	    String vRecord = null;
	    logger.debug("Try to load "+ fileName);
	    try {
	        vFile = new RandomAccessFile(fileName, "r");
	    }
	    catch (java.io.FileNotFoundException e) {
	        logger.error("input file not found " + fileName);
	     	return recordList;   
	    }
		logger.debug("Processing " + fileName);
	    try {
	        vRecord = vFile.readLine();
	        while (vRecord != null) {
				logger.debug("Record is " + vRecord);
				recordList.add(vRecord);
	            vRecord = vFile.readLine();
	        }
	    }
	    catch (java.io.IOException vIOException) {
	        logger.error("Error in reading " + fileName
	                + " " + vIOException.getMessage());
	    }
	    finally
	        {
	        if (vFile != null) {
	          	  logger.info("Closing input file... " + fileName);
	            try {
	                vFile.close();
	            }
	            catch (java.io.IOException vIOException) {
	            	logger.error("Error in closing "
	                        + fileName + " " + vIOException.getMessage());
	            }
	        }
	    }	
		return recordList;
	}

	public static long getRecordCount(String fileName, Logger logger, int bytesPerLine) {
		long recordCount = 0;
		RandomAccessFile vFile = null;
		try {
			vFile = new RandomAccessFile(fileName, "r");
			long fileLength = vFile.length();
			recordCount = fileLength / bytesPerLine;
			vFile.close();
		} catch (java.io.FileNotFoundException e) {
			logger.error("input file not found " + fileName);
		} catch (java.io.IOException vIOException) {
			logger.error("Error in reading " + fileName + " " + vIOException.getMessage());
		} finally {
			if (vFile != null) {
				logger.info("Closing input file... " + fileName);
				try {
					vFile.close();
				} catch (java.io.IOException vIOException) {
					logger.error("Error in closing " + fileName + " " + vIOException.getMessage());
				}
			}
		}
		return recordCount;
	}
	
	public static List<String> loadRecordsFromFile(String fileName,	Logger logger, int bytesPerLine, int recordsPerRead, int readIndex) {
		List<String> recordList = new ArrayList<String>();
		logger.debug("About to load record definition file = " + fileName);
		RandomAccessFile vFile = null;
		String vRecord = null;
		logger.debug("Try to load " + fileName);
		try {
			vFile = new RandomAccessFile(fileName, "r");
			vFile.skipBytes(bytesPerLine * recordsPerRead * readIndex);
		} catch (java.io.FileNotFoundException e) {
			logger.error("input file not found " + fileName);
			return recordList;
		} catch (java.io.IOException vIOException) {
			logger.error("Error in reading " + fileName + " " + vIOException.getMessage());
			return recordList;
		}
		
		logger.debug("Processing " + fileName);
		try {
			int counter = 1;
			vRecord = vFile.readLine();
			while (vRecord != null && counter <= recordsPerRead) {
				logger.debug("Record is " + vRecord);
				recordList.add(vRecord);
				vRecord = vFile.readLine();
				counter++;
			}
		} catch (java.io.IOException vIOException) {
			logger.error("Error in reading " + fileName + " " + vIOException.getMessage());
		} finally {
			if (vFile != null) {
				logger.info("Closing input file... " + fileName);
				try {
					vFile.close();
				} catch (java.io.IOException vIOException) {
					logger.error("Error in closing " + fileName + " " + vIOException.getMessage());
				}
			}
		}
		return recordList;
	}
	
	/**
	 * The format of the file is checked
	 * <p>
	 * @return The executive result 
	 * @param readFilePath File name to do check.
	 * @columnLength Column length
	 * @startLine Start line number
	 * @exception OIFFileUtilityException Exception occured
	 */
	public static boolean isValidFileFormat(String readFilePath, int[] columnLength, int startLine, 
			Logger logger) throws OIFFileUtilityException
	{

	    try {
	        int recordLength = 0;

	        //	Record Length is acquired
	        for (int cnt = 0; cnt < columnLength.length; cnt++) {
	            recordLength += columnLength[cnt];
	        }

	        //	The existence of the check file is looked up
	        File checkFile = new File(readFilePath);
	        if (checkFile.isFile() == false) {
	            logger.error(
	                "Bad file: "
	                    + readFilePath
	                    + " passed into IFFileValidation.isValidFileFormat().");
	            throw new OIFFileUtilityException();
	        }
	        if (checkFile.length() == 0) {
	            logger.error("The file is empty: " + readFilePath);
	            return false;
	        }

	        //	A check file is opened
	        FileReader fileR = new FileReader(readFilePath);
	        BufferedReader in = new BufferedReader(fileR);

	        //	A file is compared with reading RecordLength one line
	        String str;
	        for (int lineCnt = 0;; lineCnt++) {
	            //	Reading does a file one line
	            str = in.readLine();

	            if (startLine - 1 < lineCnt) {
	                if (str == null) {
	                    break;
	                }
	                //	It is compared with str which read it, and RecordLength
	                if (recordLength == str.length()) {
	                    continue;
	                } else {
	                    fileR.close();
	                    in.close();
	                    //  It save message that is error
	                    logger.error(
	                        "Problem in IFFileValidation.isValidFileFormat() with recordLength : "
	                            + recordLength
	                            + "  Length : "
	                            + str.length());
	                    throw new OIFFileUtilityException();
	                }
	            }
	            else {
	                if (str == null) {
	                    fileR.close();
	                    in.close();
	                    //  It save message that is error
	                    logger.error(
	                        "Problem in IFFileValidation.isValidFileFormat() with readFilePath: "
	                            + readFilePath + ". Record has 0 length" );

	                    throw new OIFFileUtilityException();
	                }
	            }
	        }
	        //	A check file is closed
	        fileR.close();
	        in.close();
	    }
	    catch (IOException e)
	        {
	        //  It save message that is error

	        logger.error(
	            "IOException"
	                + e
	                + " caught in IFFileValidation.isValidFileFormat() for readFilePath: "
	                + readFilePath);

	        throw new OIFFileUtilityException();
	    }

	    return true;
	}

	/**
	 * rename file modification from File.renameTo 
	 * to deal with Windows issues
	 * <p>
	 * @return boolean
	 * @param File
	 * @param File
	 * @exception IOException
	 */
	public static boolean renameFile(File srcFile, File destFile) throws IOException {
	    boolean bSucceeded = false;
	    try {
	        if (destFile.exists()) {
	            if (!destFile.delete()) {
	                throw new IOException("" + srcFile + " was not successfully renamed to " + destFile); 
	            }
	        }
	        if (!srcFile.renameTo(destFile))        {
	            throw new IOException("" + srcFile + " was not successfully renamed to " + destFile); 
	        } else {
	                bSucceeded = true;
	        }
	    } finally {
	          if (bSucceeded) {
	                srcFile.delete();
	          }
	    }
	    return bSucceeded;
	}

	public static void writeToFile(List<?> printList, String aFilePath) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(aFilePath)));
		for (Object output : printList) {
			bufferedWriter.write(output.toString());
			bufferedWriter.newLine();
		}
		bufferedWriter.close();
	}

	public static void createFile(String aFilePath) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(aFilePath)));
		bufferedWriter.close();
	}

	public static void addToFile(List<?> printList, String aFilePath) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(aFilePath), true));
		for (Object output : printList) {
			bufferedWriter.write(output.toString());
			bufferedWriter.newLine();
		}
		bufferedWriter.close();
	}

	public static List<String> locateFiles(String resultPath, final String search, Logger logger) {
		File[] inventoryFiles = null;
		List<String> inventoryFileNames = new ArrayList<String>();
		File inventoryDirectory = new File(resultPath);
		inventoryFiles = 
			inventoryDirectory.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().startsWith(search);
			}
		});
		if (inventoryFiles == null || inventoryFiles.length == 0) {
			logger.error("InventoryFileList has no entry");
		} else {
			logger.info("InventoryFileList has " + inventoryFiles.length + " files.");
			for(File f : inventoryFiles) {
				inventoryFileNames.add(f.getName());
			}
		}
		return inventoryFileNames;
	}

}
