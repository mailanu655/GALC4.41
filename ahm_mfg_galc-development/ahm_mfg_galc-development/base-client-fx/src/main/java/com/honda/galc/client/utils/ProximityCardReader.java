package com.honda.galc.client.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.common.logging.Logger;
import com.sun.jna.Library;
import com.sun.jna.Native;

/*
 * Update for ICLASS cards Nov, 2015 - Deshane Joseph
 * To read the ICLASS cards a C++ based DLL is loaded using JNA
 * and the readCard() method is invoked. The DLL code is 
 * adapted from  code sample provided by HID Global. The DLL is created and built
 * using Visual Studio 2015. The zip file for the Visual studio Solution is checked into
 * BaseLibrary/lib/java/jna/viosbadgereaderdll. The DLL is packaged as jar to satisfy webstart requirements
 * 
 * This code is invoked only if the 489tbx property SCANNER_ACTIVE is set to true
 * and the PROXIMITY_CARD_READER_NAME property is set to "HID OMNIKEY 5427 CK CL 0"
 * or "OMNIKEY CardMan 5x25-CL 0"
 * 
 *
 * 
 */

public class ProximityCardReader {
	private static final String OMNIKEY_CARD_MAN_5X25_CL_0 = "OMNIKEY CardMan 5x25-CL 0";

	private static final String HID_OMNIKEY_5427_CK_CL_0 = "HID OMNIKEY 5427 CK CL 0";

	int cardNumber = 0;

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static String[] staticLookup = new String[] { "0000", "0001",
			"0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001",
			"1010", "1011", "1100", "1101", "1110", "1111" };
	private String cardName;
	private String proxRowFormat = "00";
	private String proxH10302Format = "02";// 11
	private String proxCorp1000Format = "64";// 08
	private String proxH10320Format = "14";// 08
	private File dllFile;
	private String tempDirectory = System.getProperty("java.io.tmpdir");

	public interface hidDLL extends Library {
		hidDLL INSTANCE = (hidDLL) Native.loadLibrary(ClientConstants.VIOS_BADGE_READER_DLL, hidDLL.class);

		int readCard(String tempDirWatchFile);
		
		void loggedInManually();
	}

	public ProximityCardReader(String cardName) {
		this.cardName = cardName;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ApplicationPropertyBean appBean = PropertyService
		// .getPropertyBean(ApplicationPropertyBean.class);
		ProximityCardReader rdr = new ProximityCardReader(
				"HID OMNIKEY 5427 CK CL 0");
		//rdr.readCard();
		
		String s = "0306059FC45849A0";
		
		//extractCardNumber(s);
		

	}

	public void loggedInManually() {
		hidDLL sdll = hidDLL.INSTANCE;
		Logger.getLogger().info("User Logged in manually... before calling C++ function..." );
		sdll.loggedInManually();
		Logger.getLogger().info("User Logged in manually... After calling C++ function..." );
		
		sdll = null;		
	}

	public void loadJarDll(String name) throws IOException {
		Logger.getLogger().info("before inputstream " );
		InputStream in = ProximityCardReader.class.getResourceAsStream("/"
				+ name);
		byte[] buffer = new byte[1024];
		int read = -1;
        if (in == null ) return;
        Logger.getLogger().info("after inputstream " );
		dllFile = new File(new File(tempDirectory), name);
		FileOutputStream fos = new FileOutputStream(dllFile);

		while ((read = in.read(buffer)) != -1) {
			fos.write(buffer, 0, read);
		}
		fos.close();
		in.close();
		System.setProperty("jna.library.path", dllFile.getParent());

	}

	public String readCard() {
		String cardNum = null;
		Logger.getLogger().info("cardname: " + cardName);
	
		if (cardName.equals("OMNIKEY CardMan 5x25-CL 0"))
			cardNum = readProxcard();
		else if (cardName.equals("HID OMNIKEY 5427 CK CL 0"))
			cardNum = readICLASSCard();
		return cardNum;
	}

	public String readICLASSCard() {

	//	String  cnum = null;
		
		int  cnum = 0;
		Logger.getLogger().info("card name: " + cardName);

		try {
			loadJarDll(ClientConstants.VIOS_BADGE_READER_DLL + ".dll");
			
			Logger.getLogger().info("after loadjardll " );
		} catch (IOException e1) {
			Logger.getLogger().info("Unable to Load ICLASS Card Scanner DLL");
			e1.printStackTrace();
		}
		hidDLL sdll = hidDLL.INSTANCE;
		Logger.getLogger().info("before readcard " );
		cnum = sdll.readCard(ClientConstants.TEMP_LOGIN_WATCH_FILE_PATH);
		Logger.getLogger().info("after readcard. Card Number: " + cnum );
		
		dllFile.delete();
		sdll = null;		
	
		
		return String.valueOf(cnum);
		
	}

	public String readProxcard() {
		// show the list of available terminals
		TerminalFactory factory = TerminalFactory.getDefault();
		int cardNumber = 0;
		CardTerminals tls = factory.terminals();

		try {
			CardTerminal terminal = tls.getTerminal(cardName);
			Logger.getLogger().info("card name: " + cardName);

			terminal.waitForCardPresent(0);
			Card card = terminal.connect("T=0");

			ATR atr = card.getATR();
			String historicalBytes = bytesToHex(atr.getHistoricalBytes());
			if (!StringUtils.isBlank(historicalBytes)
					&& historicalBytes.length() > 2) {
				String proxFormat = historicalBytes.substring(0, 2);
				if (proxFormat.equals(proxRowFormat)) {
					// Get card number for row format
					String binString = HexToBinary(historicalBytes);
					int len = binString.length();
					if (historicalBytes.length() == 10) {
						cardNumber = Integer.parseInt(
								binString.substring(len - 15, len - 1), 2);
					} else if (historicalBytes.length() == 12) {
						cardNumber = Integer.parseInt(
								binString.substring(len - 19, len - 1), 2);
					}
				} else {
					// Get card number for any other format directly from
					// historical data
					int cardNumBytes = 6;
					if (proxFormat.equals(proxH10302Format)) {
						cardNumBytes = 11;
					} else if (proxFormat.equals(proxCorp1000Format)
							|| proxFormat.equals(proxH10320Format)) {
						cardNumBytes = 8;
					}
					cardNumber = Integer
							.parseInt(historicalBytes.substring(historicalBytes
									.length() - cardNumBytes));
				}
			}

			Logger.getLogger().info(
					"Card detected - card number: " + cardNumber);
			card.disconnect(false);

		} catch (CardException e) {

			e.printStackTrace();
		}
		return String.valueOf(cardNumber);
	}

	String bytesToHex(byte[] bytes) {

		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String HexToBinary(String hexString) {
		char hex = 0;

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hexString.length(); i++) {
			hex = hexString.charAt(i);
			sb = sb.append(staticLookup[Integer.parseInt(
					Character.toString(hex), 16)]);
		}
		return sb.toString();
	}

	public static String getUserId(String cardString, String cardName ) {
		try {
			String hexToBinary = HexToBinary(cardString);
			if(HID_OMNIKEY_5427_CK_CL_0.equals(cardName)) {
				int cardNumber = Integer.parseInt(hexToBinary.substring(15, 35), 2);
				return ClientMainFx.getInstance().getAccessControlManager().mapProxCardNumber(cardNumber);
			} 
		} catch (NumberFormatException e) {
			;//ok;
		}
		return null;
		
		
	}
}
