package com.honda.galc.service.printing;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.honda.galc.common.exception.PrintingException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.service.ServiceFactory;



/**
 * <h3>Priner interface template queue sender</h3>
 * <h4>Description</h4>
 * Concrete realization of the printer queue sender<br/>
 * Creates output as a list of DataContainer tag values:<br/>
 * &lt;Tag1 Value&gt;,&lt;Tag2 Value&gt;,...,&lt;TagN Value&gt;<br/>
 * For Example: <b>ESN0,2PYL- A8,R18A11400001,R18A1-1400001  </b>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Dec 14, 2007</TD>
 * <TD>@RL011</TD>
 * <TD>&nbsp;</TD>
 * <TD>Initial Creation</TD>
 * </TR>
 * </TABLE>
 */
public class ListPrinterDataAssembler implements IPrinterDataAssembler{

	protected static final String ATTRIBUTE_SEPARATOR = ",";
	protected static final int ATTR_SEP_LENGTH = ATTRIBUTE_SEPARATOR.length();
	private static final String PS_ADOBE = "%!PS-Adobe";

	/**
	 * Constructs printer sender object based on queue name
	 * 
	 * @param argQueueName
	 * @throws BroadcastException
	 */
	public ListPrinterDataAssembler(){ 
	}

	public void writeOutputFile(OutputStream os, DataContainer dataContainer) {

		List<String> order = getAttributes(dataContainer);
		StringBuilder buffer = new StringBuilder();
		
		if(!ServiceFactory.isServerSide()) {
			// direct print to network driver, set postscript mode
			
			String queueName = (String)dataContainer.get(DataContainerTag.QUEUE_NAME);
			queueName = queueName.replace("Local", "");
			try {
				os.write(0x04);
				os.write(PS_ADOBE.getBytes());
				os.write(0x0D);
				os.write(("(" + queueName +")run " + queueName).getBytes());
				os.write(0x0D);
				
			} catch (IOException e) {
				throw new PrintingException("Cannot write to output file ",e);
			}
		}
		
		boolean isFirst = true;
		// Write ATTRIBUTE_SEPARATOR separated attribute values
		for (String attrName : order) {
			if(isFirst) isFirst = false;
			else buffer.append(ATTRIBUTE_SEPARATOR);
			String attrValue = String.valueOf(dataContainer.get(attrName));
			buffer.append(attrValue);
		}
		buffer.append(System.getProperty("line.separator"));
		
		Logger.getLogger().info("Print Local: " + buffer.toString());
		
		try {
			os.write(buffer.toString().getBytes());
		} catch (IOException e) {
			throw new PrintingException("Cannot write to output file ",e);
		}
		
		try {
			os.flush();
			os.close();
		} catch (IOException e) {
			throw new PrintingException("Could not close output stream ", e);
		}
	}

	/**
	 * Helper method to isolate warnings caused by casting
	 * 
	 * @param dataContainer - input DataContainer
	 * @return List<String> - list of attributes
	 * @throws BroadcastException
	 */
	@SuppressWarnings("unchecked")
	protected List<String> getAttributes(DataContainer dataContainer) {
		Object orderObject = dataContainer.get(DataContainerTag.TAG_LIST);
		if(orderObject == null) {
			throw new PrintingException("Cannot find " + DataContainerTag.TAG_LIST + " tag in datacontainer");
		}
		
		List<String> order = (List<String>) orderObject;
		return order;
	}

}
