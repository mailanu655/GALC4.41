package com.honda.galc.common;

import honda.jp.hm.gpc.gco.cm.mqft.MqftCoreClient;
import honda.jp.hm.gpc.gco.cm.mqft.MqftFileTransferClient;
import honda.jp.hm.gpc.gco.cm.mqft.common.MqftMode;
import honda.jp.hm.gpc.gco.cm.mqft.err.MqftException;
import honda.jp.hm.gpc.gco.cm.mqft.err.MqftSevereException;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.honda.galc.oif.task.OifAbstractTask;
import com.honda.galc.service.property.PropertyHelper;
import com.honda.galc.util.OIFFileUtility;

public class MQUtility extends OIFObject
{
    /**
     * InstanceID Array
     */
    public java.lang.String[] instanceID = null;

    /**
     * InterfaceID for Status Key
     */
    private static String STS_IF_ID = "STS_IF_ID";

    /**
     * MQUtility Property file instance"
     */
	private static PropertyHelper propertyHelper;
	
	/**
	 * Constructor
	 */
	public MQUtility(OifAbstractTask aParentHandler)
	{
	    super(aParentHandler);
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (10/22/01 12:23:46 PM)
	 * @return java.lang.String
	 */
	public static String createNoDataString(int num) {
		// Parameter num is number of space code that fills end of String.
		// String to return is sent to MQ in case of No data in DB.
		String strRet = new String();
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	
		strRet = "GPCS NORECORD  "
			   + df.format(date) + " ";
		for (int idx = 0; idx < num; idx++) {
			strRet += ' ';
		}
		return strRet;
	}

	/**
	 * createNoDataMessage - adds spaces to the end of hardcoded message
	 * Creation date: (02/06/2015)
	 * @param int messageLength - length of the string to be created
	 * 		Should be longer than strRet length, not trimmed otherwise
	 * @return java.lang.String
	 */
	public static String createNoDataMessage(int messageLength) {
		// Parameter num is number of space code that fills end of String.
		// String to return is sent to MQ in case of No data in DB.
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	
		StringBuffer strRet = new StringBuffer("GPCS NORECORD  " + df.format(date) + " ");
		for (int idx = strRet.length(); idx < messageLength; idx++) {
			strRet.append(" ");
		}
		return strRet.toString();
	}

	/**
	 * Put sending file to MQ.
	 * <p>
	 * @param interfaceID InterfaceID
	 * @param sendFileName Sending File Name
	 * @exception MQUtilityException Fail to put to MQ.
	 */
	public void executeMQSendAPI(String interfaceID, String configFile, String sendFileName)
	    throws MQUtilityException {
        MqftFileTransferClient file_transfer_client = null;
	    try {
	        // construct the instance
	        file_transfer_client =
	            new MqftFileTransferClient(configFile);
	
	        // open MQFT
	        file_transfer_client.open(interfaceID, MqftMode.SEND);
	
	        String instansID = interfaceID + "-" + getTimestamp();
	
	        // read file
	        file_transfer_client.storeFile(instansID, sendFileName);
	
	        logger.info(
	            "MQUtility.executeMQSendAPI() for interfaceID: "
	                + interfaceID
	                + " sendFileName: "
	                + sendFileName
	                + " appears to have worked correctly.");
	
	    }
	    catch (MqftException e) {
	        logger.error(e, 
	            "MqftException: "
	                + e
	                + " caught in MQUtility.executeMQSendAPI() for interface: "
	                + interfaceID
	                + " fileName: "
	                + sendFileName);
	        throw new MQUtilityException();
	    }
	    catch (RuntimeException e) {
	        logger.error(e, 
	            "RuntimeException: "
	                + e
	                + " caught in MQUtility.executeMQSendAPI() for interface: "
	                + interfaceID
	                + " fileName: "
	                + sendFileName);
	        throw new MQUtilityException();
	    } finally {
	    	closeClient(file_transfer_client, interfaceID);
	    }
	
	}

	/**
	 * Receive the file from MQ.<br>
	 * These files are created on directory for receiving.
	 * <p>
	 * @return Receiving file Name
	 * @param interfaceID InterfaceID
	 * @param dirPath Directory name to save 
	 * @exception MQUtilityException Fail to get File from MQ
	 */
	public String[] getReceiveFile(String interfaceID, String mqProperties, String dirPath)
	    throws MQUtilityException
	{
        MqftFileTransferClient file_transfer_client = null;
	    try
	        {
	        // Create the instance
	        file_transfer_client = new MqftFileTransferClient(mqProperties);
	
	        // open 
	        file_transfer_client.open(interfaceID, MqftMode.RECEIVE);
	
	        Vector<String> tmpFileList = new Vector<String>();
	        Vector<String> tmpInsIDList = new Vector<String>();
	
	        int count = 1;
	        while (true)
	            {
	            String recFileName = dirPath + interfaceID;
	            StringBuffer buf = new StringBuffer(recFileName);
	            // read file
	            String instance_id = file_transfer_client.retrieveFile(buf);
	            if (instance_id == null) {
	                break;
	            }
//	            Change the file name
	            File newFile = new File(recFileName);
			    File destFile = File.createTempFile(interfaceID,"_"+getTimestamp()+"_"+count, new File(dirPath));
			    boolean copyResult = OIFFileUtility.renameFile(newFile, destFile);
			    logger.info("File " + newFile + " renamed to " + destFile + ": " + copyResult);
	
	            tmpInsIDList.addElement(instance_id);
	            tmpFileList.addElement(destFile.getName());
	            count++;
	        }
	        //Save to String array File Created by MQ 
	        int fileSize = tmpFileList.size();
	        String fileList[] = new String[fileSize];
	        instanceID = new String[fileSize];
	        for (int i = 0; i < fileSize; i++)
	            {
	            fileList[i] = (String) tmpFileList.elementAt(i);
	            instanceID[i] = (String) tmpInsIDList.elementAt(i);
	        }
	        return fileList;
	
	    }
	    catch (MqftException e) {
	        logger.error(e, 
	            "MqftException: "
	                + e.getMessage()
	                + " caught in MQUtility.getReceivedFile() for interface: "
	                + interfaceID
	                + " dirPath: "
	                + dirPath);
	        throw new MQUtilityException();
	    }
	    catch (RuntimeException e) {
	        logger.error(e,
	            "RuntimeException: "
	                + e.getMessage()
	                + " caught in MQUtility.getReceivedFile() for interface: "
	                + interfaceID
	                + " dirPath: "
	                + dirPath);
	        throw new MQUtilityException("OSICN206");
	    }
	    catch (Exception e) {
	        logger.error(e,
	            "Exception: "
	                + e.getMessage()
	                + " caught in MQUtility.getReceivedFile() for interface: "
	                + interfaceID
	                + " dirPath: "
	                + dirPath);
	        throw new MQUtilityException("OSICN206");
	    } finally {
	    	closeClient(file_transfer_client, interfaceID);
	    }
	}

	/**
	 * Receive the record from MQ.
	 * <p>
	 * @return The record got from MQ
	 * @param InterfaceID InterfaceID
	 * @exception MQUtilityException Fail to get record from MQ
	 */
	public String[] getReceiveRecord(String interfaceID) throws MQUtilityException
	{
        MqftCoreClient coreClient = null;
	    try {
	        coreClient =
	            new MqftCoreClient(propertyHelper.getProperty("INTERFACE_ID"));
	        coreClient.open(interfaceID, MqftMode.RECEIVE);
	        // begin to receiving
	        StringBuffer fth_extension_type = new StringBuffer();
	        byte[] fth_extension = new byte[256];
	        String instance_id =
	            coreClient.beginReceiving(fth_extension_type, fth_extension);
	        if (instance_id == null) return null; // no FTU arraived in the queue
	
	        // open file
	        Vector<String> vec = new Vector<String>();
	        // transfer!
	        while (true) {
	            // read from MQ
	            byte[] buffer = coreClient.retrieveRecordAsByteArray();
	
	            if (buffer != null) {
	                String st = new String(buffer);
	                vec.addElement(st);
	                logger.info("MQUtility.getReciveRecord() str :" + st);
	            }
	            else
	                if (buffer == null)
	                    break;
	        }
	
	        coreClient.completeReceiving();
	
	        int size = vec.size();
	        String[] resRec = new String[size];
	        for (int i = 0; i < size; i++) {
	            resRec[i] = new String((String) vec.elementAt(i));
	        }
	        return resRec;
	
	    }
	    catch (MqftException excp) {
	        logger.error(excp,
	            "MqftException: "
	                + excp
	                + " caught in MQUtility.getReciveRecord() for interface: "
	                + interfaceID);
	
	        throw new MQUtilityException("OSICN204");
	    }
	    catch (RuntimeException e) {
	        logger.error(e,
	            "RuntimeException: "
	                + e
	                + " caught in MQUtility.getReciveRecord() for interface: "
	                + interfaceID);
	        throw new MQUtilityException("OSICN206");
	    } finally {
		    // close MQFT
		    try {
		    	if(coreClient != null) {
		    		coreClient.close();
		    	}
			} catch (MqftSevereException e) {
		        logger.error(e,
			            "Exception: "
			                + e
			                + " caught in MQUtility.getReceivedFile() for interface: "
			                + interfaceID 
			                + " on close() method of the instance of MqftFileTransferClient." );
			        throw new MQUtilityException("OSICN206");
			}
	    }
	}
	
	/**
	 * Get timestamp string 
	 * <p>
	 * @return Timestamp string
	 * @param aTimestamp Timestamp
	 */
	private String getTimestamp() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = format.format(new Timestamp(new Date().getTime()));
		return timestamp;
	}
	
	/**
	 * Send status to sender with using MQ API
	 * <p>
	 * @param interfaceID InterfaceID
	 * @param instanceID InstanceID
	 * @param statusCode Status
	 * @exception MQUtilityException Fail to send status
	 */
	public void returnStatusToSender(
	    String interfaceID,
	    String instanceID,
	    int statusCode)
	    throws MQUtilityException {
        MqftFileTransferClient file_transfer_client = null;
	    try {
	        String statusInterfaceID = propertyHelper.getProperty(STS_IF_ID); 
	        // construct the instance
	
	        file_transfer_client =
	            new MqftFileTransferClient(propertyHelper.getProperty(statusInterfaceID));
	
	        // open MQFT
	        file_transfer_client.open(statusInterfaceID, MqftMode.SEND);
	        file_transfer_client.sendStatus(interfaceID, instanceID, statusCode);
	        file_transfer_client.close();
	    }
	    catch (MqftException e) {
	        logger.error(e, 
	            "MqftException: "
	                + e
	                + " caught in MQUtility.returnStatusToSender(String, String, int) for interface: "
	                + interfaceID
	                + " instance: "
	                + instanceID
	                + " statusCode: "
	                + statusCode);
	
	        //  Saves message that is error
	        throw new MQUtilityException("OSICN205");
	    }
	    catch (RuntimeException e) {
	        logger.error(e, 
	            "RuntimeException: "
	                + e
	                + " caught in MQUtility.returnStatusToSender(String, String, int) for interface: "
	                + interfaceID
	                + " instance: "
	                + instanceID
	                + " statusCode: "
	                + statusCode);
	        throw new MQUtilityException("OSICN021");
	    } finally {
	    	closeClient(file_transfer_client, interfaceID);
	    }
	
	}
	
	/**
	 * Send status to sender with using MQ API
	 * <p>
	 * @param interfaceID InterfaceID
	 * @param instanceID InstanceID
	 * @param statusCode status
	 * @param aComment  Comment
	 * @exception MQUtilityException Fail to send status
	 */
	public void returnStatusToSender(
	    String interfaceID,
	    String instanceID,
	    int statusCode,
	    String aComment)
	    throws MQUtilityException {
        MqftFileTransferClient file_transfer_client = null;
	    try {
	        String statusInterfaceID = propertyHelper.getProperty(STS_IF_ID);
	        // construct the instance
	
	        file_transfer_client =
	            new MqftFileTransferClient(propertyHelper.getProperty(statusInterfaceID));
	
	        // open MQFT
	        file_transfer_client.open(statusInterfaceID, MqftMode.SEND);
	        file_transfer_client.sendStatus(interfaceID, instanceID, statusCode, aComment);
	        file_transfer_client.close();
	    }
	    catch (MqftException e) {
	        //  It save message that is error
	        logger.error(e,
	            "MqftException: "
	                + e
	                + " caught in MQUtility.returnStatusToSender(String, String, int, String) for interface: "
	                + interfaceID
	                + " instance: "
	                + instanceID
	                + " statusCode: "
	                + statusCode
	                + " comment: "
	                + aComment);
	        //	MQUtilityException object is formed and thrown
	        throw new MQUtilityException("OSICN205");
	    }
	    catch (RuntimeException e) {
	        logger.error(e,
	            "RuntimeException: "
	                + e
	                + " caught in MQUtility.returnStatusToSender(String, String, int, String) for interface: "
	                + interfaceID
	                + " instance: "
	                + instanceID
	                + " statusCode: "
	                + statusCode
	                + " comment: "
	                + aComment);
	
	        throw new MQUtilityException("OSICN021");
	    } finally {
	    	closeClient(file_transfer_client, interfaceID);
	    }
	}
	
	private void closeClient(MqftFileTransferClient file_transfer_client, String interfaceID)
	    throws MQUtilityException {
	    // close MQFT
	    try {
	    	if(file_transfer_client != null) {
	    		file_transfer_client.close();
	    	}
		} catch (MqftSevereException e) {
	        logger.error(e,
		            "Exception: "
		                + e.getMessage()
		                + " caught in MQUtility.getReceivedFile() for interface: "
		                + interfaceID 
		                + " on close() method of the instance of MqftFileTransferClient." );
		        throw new MQUtilityException("OSICN206");
		}
	}
}
