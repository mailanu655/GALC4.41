package com.honda.galc.device.simulator.client.ui;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * This class replaces DefaultHandler When GalcStatusBar parsing
 * menu definitions from XML documents. It will throw SAXParseException
 * if there are errors or warnings when parsing the document.
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
 * <TD>Guang Yang</TD>
 * <TD>Jun 22, 2007</TD>
 * <TD>1.0</TD>
 * <TD>20070622</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Guang Yang
 */
public class GalcXmlErrorHandler extends DefaultHandler {
    
    public void error(SAXParseException e) throws SAXParseException {
        throw e;
    }

    public void warning(SAXParseException e) throws SAXParseException {
    	throw e;
    }

    public void fatalError(SAXParseException e) throws SAXException {
        throw e;
    }
}

