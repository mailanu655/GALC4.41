package com.honda.galc.let.message;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.LetReply;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.LetSpool;
import com.honda.galc.let.util.LetUtil;
import com.honda.galc.letxml.model.UnitInTest;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Apr 10, 2015
 */
public class LetProcessItem {
	
	private static final String VALID_MSG_FILE_EXTENSION = ".xml";
	private static final String INVALID_MSG_FILE_EXTENSION = ".msg";
	private static Logger logger;
	
	private StringBuilder receivedMsg = new StringBuilder();
	private String fileLocation = "";
	private String msgKey = "";
	private long msgId = -1;
	private boolean isValid = false;
	private LetReply reply = LetReply.NOT_SENT;
	private LetSpool spool = null;
	private Timestamp actualTimeStamp;
	private String msgType;
	private String terminalId;
	private UnitInTest unitInTest;
	
	
	public LetProcessItem(StringBuilder receivedMsg, String msgKey, LetSpool spool,String msgType, String terminalId) {
		this.receivedMsg = receivedMsg;
		this.spool = spool;
		this.msgKey = msgKey;
		this.msgType = msgType;
		this.terminalId = terminalId;
	}
	
	public LetProcessItem(StringBuilder receivedMsg, String msgKey, LetSpool spool,boolean isValid, String msgType, String terminalId) {
		this(receivedMsg, msgKey, spool, msgType, terminalId);
		this.isValid = isValid;
		
	}
	
	public LetProcessItem(StringBuilder receivedMsg, String msgKey, String fileLocation, boolean isValid, String msgType, String terminalId) {
		this.fileLocation = fileLocation;
	}
		
	public StringBuilder getReceivedMsg() {
		return receivedMsg;
	}
	
	public void setReceivedMsg(StringBuilder receivedMsg) {
		this.receivedMsg = receivedMsg;
	}
	
	public String getFileLocation() {
		if (StringUtils.trimToNull(fileLocation) == null) {
			try {
				String saveLocation = LetUtil.getXmlSaveLocation(new Date(), isValid(), spool, receivedMsg);
				LetUtil.createDirs(saveLocation);
				fileLocation = saveLocation + getMsgKey() + getFileExtension();
			} catch(Exception ex) {
				ex.printStackTrace();
				getLogger().error(ex, "Unable to get file location for request " + getMsgKey() + ": " + ex.getLocalizedMessage());
			}
		}
		return fileLocation;
	}
	
	public String getFileExtension() {
		if (isValid()) {
			return VALID_MSG_FILE_EXTENSION;
		} else {
			return INVALID_MSG_FILE_EXTENSION;
		}
	}
	
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	public String getMsgKey() {
		return msgKey;
	}
	
	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	public LetReply getReply() {
		return reply;
	}

	public void setReply(LetReply reply) {
		this.reply = reply;
	}
	
	public Timestamp getActualTimeStamp() {
		return actualTimeStamp;
	}

	public void setActualTimeStamp(Timestamp actualTimeStamp) {
		this.actualTimeStamp = actualTimeStamp;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public static Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger("JcaAdaptor");
		}
		return logger;
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

    public UnitInTest getUnitInTest() {
        return unitInTest;
    }

    public void setUnitInTest(UnitInTest unitInTest) {
        this.unitInTest = unitInTest;
    }

	

	
}
