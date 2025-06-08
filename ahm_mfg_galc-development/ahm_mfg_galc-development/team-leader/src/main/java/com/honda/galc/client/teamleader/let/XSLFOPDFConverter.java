
package com.honda.galc.client.teamleader.let;

import org.apache.avalon.framework.logger.NullLogger;
import org.apache.fop.apps.Driver;
import org.apache.fop.messaging.MessageHandler;
import org.xml.sax.InputSource;
import com.honda.galc.common.logging.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Dec 03, 2013
 */
public class XSLFOPDFConverter {

	public static byte[] generatePDF(String xslfo)  
	{
		org.apache.avalon.framework.logger.Logger log = new NullLogger();
		MessageHandler.setScreenLogger(log);
		ByteArrayOutputStream bytOut = null;
		BufferedOutputStream bufOut = null;
		byte[] pdfbinary=null;
		try {
			InputSource source = new InputSource(new ByteArrayInputStream(xslfo.getBytes()));
			bytOut = new ByteArrayOutputStream();
			bufOut = new BufferedOutputStream(bytOut);
			Driver driver = new Driver(source, bufOut);
			driver.setLogger(log);
			driver.setRenderer(Driver.RENDER_PDF);
			driver.run();
			driver = null;
			bufOut.flush();
			bufOut.close();
			bufOut = null;
			source = null;
			pdfbinary= bytOut.toByteArray();
			bytOut.close();
			bytOut = null;

		}catch (Exception e)
		{
			e.printStackTrace();
			Logger.getLogger().error("An error Occurred while processing the XSL-FO conversion to PDF");
		}
		finally {
			try {
				if (bufOut != null) {
					bufOut.close();
					bufOut = null;
				}
				if (bytOut != null) {
					bytOut.close();
					bytOut = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.getLogger().error("An error Occurred while processing the XSL-FO conversion to PDF");
			}
		}
		return pdfbinary;
	}

	public static byte[] gzipUncompress(byte[] in) 
	{
		byte[] rtn = null;
		BufferedInputStream bufIn = null;
		ByteArrayOutputStream bytOut = null;
		BufferedOutputStream bufOut = null;
		try {
			bufIn = new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(in)));
			bytOut = new ByteArrayOutputStream();
			bufOut = new BufferedOutputStream(bytOut, 1024000);
			int chr;
			while ((chr = bufIn.read()) != -1) {
				bufOut.write((byte) chr);
			}
			bufOut.flush();
			bufOut.close();
			rtn = bytOut.toByteArray();
			bytOut.close();
			bufOut = null;
			bytOut = null;
			bufIn.close();
			bufIn = null;

		}catch(Exception e)
		{
			e.printStackTrace();
			Logger.getLogger().error("An error Occurred while processing the XSL-FO conversion to PDF");
		}
		finally {
			try {
				if (bufOut != null) {				
					bufOut.close();				
					bufOut = null;
				}
				if (bytOut != null) {
					bytOut.close();
					bytOut = null;
				}
				if (bufIn != null) {
					bufIn.close();
					bufIn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.getLogger().error("An error Occurred while processing the XSL-FO conversion to PDF");
			}
		}
		return rtn;
	}
}
