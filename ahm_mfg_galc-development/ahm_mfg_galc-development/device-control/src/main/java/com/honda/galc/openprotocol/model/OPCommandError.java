package com.honda.galc.openprotocol.model;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.honda.galc.openprotocol.OPMessageDefinition;
import com.honda.galc.openprotocol.OPMessageHelper;

/**
 * @author Subu Kathiresan
 * Feb 20, 2009
 */
public enum OPCommandError 
{
	invalidData(1),
	psetNotPresent(2),
	psetCannotBeSet(3),
	psetNotRunning(4),
	vinUploadSubAlreadyExists(6),
	vinUploadSubDoesNotExist(7),
	vinInputSourceNotGranted(8),
	lastTighteningResultSubAlreadyExists(9),
	lastTighteningResultSubDoesNotExist(10),
	alarmSubAlreadyExists(11),
	alarmSubDoesNotExist(12),
	psetSelectionSubAlreadyExists(13),
	psetSelectionSubDoesNotExist(14),
	tighteningIdReqNotFound(15),
	connRejectedProtocolBusy(16),
	jobNumNotPresent(17), 
	jobInfoSubAlreadyExists(18),
	jobInfoSubDoesNotExist(19),
	jobCannotBeSet(20), 
	jobNotRunning(21),
	controllerNotASyncMaster(30),
	msStatusSubAlreadyExists(31),
	msStatusSubDoesNotExist(32),
	msResultSubAlreadyExists(33),
	msResultSubDoesNotExists(34),
	jobLineControlInfoSubAlreadyExists(40),
	jobLineControlInfoSubDoesNotExist(41),
	identifierInputSourceNotGranted(42),
	multipleIdWOSubAlreadyExists(43),
	multipleIdWOSubDoesNotExist(44),
	statusEMISubAlreadyExists(50),
	statusEMISubDoesNotExist(51),
	ioDeviceNotConnected(52),
	faultyIODeviceNumber(53),
	noAlarmPresent(58),
	toolCurrentlyInUse(59),
	noHistogramAvailable(60),
	reserved1(80),
	reserved2(81),
	clientAlreadyConnected(96),
	midRevisionUnSupported(97),
	controllerInternalReqTimeout(98),
	unknownMID(99);
	
	private int _errorCode;
	
	private String _message;
	
	
	public static final String RESOURCE_FILE_NAME = "OPCommandErrors.xml";
	public static final String OP_COMMAND_ERRORS_FILE = OPMessageHelper.RESOURCE_PATH + RESOURCE_FILE_NAME;
	public static final String ERROR_TAG="error";
	public static final String ID_TAG="id";
	public static final String NAME_TAG="name";
	public static final String MESSAGE_TAG="message";
	
	OPCommandError(int errorCode)
	{
		_errorCode = errorCode;
	}
	

	
	static {
		OPMessageDefinition opMessage = new OPMessageDefinition();
		
		try {
			InputStream is = opMessage.getClass().getResourceAsStream(OP_COMMAND_ERRORS_FILE);
			DocumentBuilder builder = null;
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(is);
			NodeList items = doc.getElementsByTagName(ERROR_TAG);
			for(int i = 0; i<items.getLength();i++) {
				Element item = (Element)items.item(i);
				int id = Integer.parseInt(item.getAttribute(ID_TAG));
				OPCommandError opCommandError = get(id);
				if(opCommandError != null) opCommandError.setMessage(item.getAttribute(MESSAGE_TAG));
			}
		}catch(Exception ex) {
			ex.getMessage();
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getErrorCode()
	{
		return _errorCode;
	}
	
	
	
	public String getMessage() {
		return _message;
	}



	public void setMessage(String message) {
		this._message = message;
	}


	public static OPCommandError get(int id) {
		for(OPCommandError t : OPCommandError.class.getEnumConstants()) {
			if(t.getErrorCode() == id) return t;
		}
		return null;
	}
	

}
