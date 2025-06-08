package com.honda.galc.client.device.etcher;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Subu Kathiresan
 * @date May 17, 2017
 */
public enum LecLaserResponse {
	
	Success(0),
	Idle(1),
	Busy(2),
	NoJob(3),
	InControl(4),
	NotInControl(5),
	LicenseUnavailable(6),
	LicenseAccessDenied(7),
	BadCommand(8),
	BadArg(9),
	ArgOutOfRange(10),
	UnknownTimeZone(11),
	Reserved(12),
	BadConversion(13),
	RegistryError(14),
	TimeZoneFileError(15),
	ResetInterlock(16),
	ListNotOpen(17),
	ListAlreadyOpen(18),
	BadData(19),
	APIException(20),
	JobAborting(21),
	FPGALoadFail(22),
	JobManagerInitFail(23),
	LaserLoadFail(24),
	LensLoadFail(25),
	PMLoadFail(26),
	MotionLoadFail(27),
	HostManagerInitFail(28),
	InvalidIPAddress(29),
	DataUnknown(30),
	BadChecksum(31),
	NetworkShareNotConnected(32),
	NetworkConnectFail(33),
	UnknownNetworkError(34),
	APICommandTimeout(35),
	ExternalProcessFail(36),
	DLLLoadFail(37),
	NoAdapter(38),
	AddIPAddressFailure(39),
	BadAPIResponse(40),
	CannotCreateSocket(41),
	CannotConnectSocket(42),
	CannotGetFPGABufInfo(43),
	CannotGetFPGABuf(44),
	CannotWriteFPGABuf(45),
	FPGAException(46),
	FTPConnectionError(47),
	FileAlreadyExists(48),
	UnknownOS(49),
	SocketException(50),
	ProcessTimeout(51),
	DeviceNotFound(52),
	LoginInProgress(53),
	APIClientInControl(54),
	StreamClientInControl(55),
	CannotConnectToAPI(56),
	ReadFail(57),
	StreamBufferFull(58),
	NoConfigRecord(59),
	OperationCanceled(60),
	NoData(61),
	InitializationError(62),
	FailToCreateServiceThread(63),
	CannotOpenDevice(64),
	SegmentFull(65),
	MarkerLibraryNotInitialized(66),
	RingBufferNotInitialized(67),
	AccessDenied(68),
	RequiresUACElevation(69),
	NotAllowed(70),
	NoLaserConfig(71),
	NoLensConfig(72),
	OutOfMemory(73),
	LensTableNotFound(74),
	HostControlInitError(75),
	NoBytesRead(76),
	WritePending(77),
	NoPen(84),
	NoFilesFound(100),
	NoDrive(101),
	JobOutOfMemory(102),
	TooManyObjects(103),
	NoObject(104),
	JobException(105),
	NotInHostControl(106),
	WrongHostType(107),
	ErrorJobBusy(108),
	NoActiveJob(109),
	ErrorSoftware(110),
	LoadFail(111),
	NoObjects(112),
	WriteFail(113),
	JobFileFormat(114),
	FileException(115),
	UnknownObject(116),
	UnknownType(117),
	NotSupported(118),
	NotAvailable(119),
	FPGADataFail(120),
	FileNotFound(121),
	FileCreationError(122),
	WriteFileFail(123),
	PathNotFound(124),
	NotInCacheMode(125),
	NotWaitingForStartMark(126),
	MotionNotHomed(127),
	No3DModel(128),
	ProjectionError(129),
	NoProperties(200),
	ObjectException(201),
	Abort(202),
	NoFontResource(203),
	NoOverride(204),
	ExternalEnableDenied(205),
	CannotCreatePort(206),
	CannotOpenPort(207),
	PortNotOpen(208),
	PortTimeout(209),
	WrongPortNumber(210),
	WrongObjectType(211),
	AxisNotConfigured(212),
	TextBufferOverrun(213),
	InvBarcodeStringValue(214),
	InvBarcodeStringLength(215),
	InvBarcodeNarrowWidth(216),
	InvBarcodeWidthReduce(217),
	InvBarcodeECC(218),
	BarcodeOutOfMemory(219),
	BarcodeUnknownError(220),
	BarcodeException(221),
	NoVectors(222),
	BadMotionResponse(223),
	MotionDriverNotFound(224),
	AxisNotFound(225),
	EncoderNotFound(226),
	InvStringValue(227),
	MotionControllerNotFound(228),
	MotorNotProvisioned(229),
	RuntimeMotionError(230),
	ObjectOutOfBounds(231),
	InvVersion(232),
	NoOutline(233);
	
	private int responseCode;
	private String message;
	
	public static final String RESOURCE_FILE_NAME = "LecLaserResponses.xml";
	public static final String RESOURCE_PATH = "/resource/com/honda/galc/leclaserprotocol/";
	public static final String LEC_COMMAND_RESPONSE_FILE = RESOURCE_PATH + RESOURCE_FILE_NAME;
	public static final String RESPONSE_TAG = "response";
	public static final String ID_TAG = "id";
	public static final String NAME_TAG = "name";
	public static final String MESSAGE_TAG = "message";
	
	LecLaserResponse(int responseCode) {
		this.responseCode = responseCode;
	}
	
	static {
		String str = new String();
		try {
			InputStream is = str.getClass().getResourceAsStream(LEC_COMMAND_RESPONSE_FILE);
			DocumentBuilder builder = null;
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(is);
			NodeList items = doc.getElementsByTagName(RESPONSE_TAG);
			for(int i = 0; i < items.getLength(); i++) {
				Element item = (Element)items.item(i);
				int id = Integer.parseInt(item.getAttribute(ID_TAG));
				LecLaserResponse lecCommandResponse = get(id);
				if(lecCommandResponse != null) {
					lecCommandResponse.setMessage(item.getAttribute(MESSAGE_TAG));
				}
			}
		} catch(Exception ex) {
			ex.getMessage();
			ex.printStackTrace();
		}
	}
	
	public int getResponseCode() {
		return responseCode;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static LecLaserResponse get(int id) {
		for(LecLaserResponse t : LecLaserResponse.class.getEnumConstants()) {
			if(t.getResponseCode() == id) return t;
		}
		return null;
	}
}
