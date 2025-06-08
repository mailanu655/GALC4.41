package com.honda.galc.service.printing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.PrintingException;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.property.PrinterQueueSenderPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AbstractVarSubstitutor;
import com.honda.galc.util.DataContainerVarSubstitutor;


/**
 * <h3>Priner interface template queue sender</h3>
 * <h4>Description</h4>
 * Concrete realization of the printer queue sender<br/>
 * Creates output from files template<br/>
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
 * <TD>Dec 07, 2007</TD>
 * <TD>@RL011</TD>
 * <TD>&nbsp;</TD>
 * <TD>Initial Creation</TD>
 * </TR>
 * </TABLE>
 */
public class TemplatePrinterDataAssembler implements IPrinterDataAssembler {
	private static final String TEMPL_SUFFIX = ".tmpl";
	private static final String PRINT_TEMPLATE_PATH = "/resource/com/honda/galc/print/";

	
	private boolean isResourceTemplate = false; 
	
	/**
	 * Constructor
	 * 
	 * @throws BroadcastException 
	 * 
	 */
	public TemplatePrinterDataAssembler(boolean flag) {
		super();
		isResourceTemplate = flag;
	}

	/**
	 * Creates output file by reading from template line-by-line<br/>
	 * and substituting variable placeholders (e.g. ${ENGINE_SN}) with<br/>
	 * their corresponding values<br/>
	 * 
	 * @see com.honda.global.galc.system.broadcast.print.PrinterQueueSender#writeOutputFile(PrintWriter, com.honda.global.galc.common.data.DataContainer)
	 */
	public void	writeOutputFile(OutputStream os,DataContainer dc) {
		
		String formId = getFormId(dc);
		InputStream in = getInputStream(formId);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		//		Create line substitutor
		AbstractVarSubstitutor subst = new DataContainerVarSubstitutor(dc);
		
		String line;
		//	 Loop through template lines:
		try {
			while((line = br.readLine()) != null) {
				
				//      replace patterns with values from data container
				line = subst.substitute(line);
				
				//      write line to output file
				os.write(line.getBytes());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch (IOException e) {
			throw new PrintingException("Could not close template file ");
		}
		
		try {
			os.flush();
			os.close();
		} catch (IOException e) {
			throw new PrintingException("Could not close output stream ", e);
		}
		

			
	}
	
	private InputStream getInputStream(String formId) {
		
		return isResourceTemplate? getInputStreamFromResource(formId) : getInputStreamFromFile(formId);
		
	}
	
	private InputStream getInputStreamFromResource(String formId) {
		
		return  getClass().getResourceAsStream(PRINT_TEMPLATE_PATH  + formId + TEMPL_SUFFIX);
		
	}
	
	private InputStream getInputStreamFromFile(String formId) {
		
		String templateFileName = getTemplateFileName(formId);
		try {
			return new FileInputStream(templateFileName);
		} catch (FileNotFoundException e) {
			throw new PrintingException("Could not find template file : " +  templateFileName);
		}
		
	}

	/**
	 * Returns template file name by mapping FORM_ID to corresponding template<br/>
	 * The format of the template file name:<br/>
	 * <b>&lt;template dir&gt;&lt;FORM_ID&gt;.tmpl</b>
	 * Where:<ul>
	 * <li>template dir - template directory (for all template files)</li>
	 * <li>FORM_ID - form ID (this way each form has one corresponding template)
	 * <li>.tmpl - file extension of the template</li>
	 * </ul>
	 * @param dataContainer - input DataContainer (with FORM_ID)
	 * @return template file name
	 * @throws BroadcastException
	 */
	protected String getTemplateFileName(String formId){
		
		// get template directory
		String templateDir;
		
		PrinterQueueSenderPropertyBean propertyBean = PropertyService.getPropertyBean(PrinterQueueSenderPropertyBean.class);
		
		templateDir = propertyBean.getTemplateDir();
		
		if(StringUtils.isEmpty(templateDir)) {
			throw new PrintingException("Failure to get template dir from property");
		}
		
		// Match FORM_ID to template file name
		StringBuilder templateFileName = new StringBuilder(templateDir);
		templateFileName.append(formId);
		templateFileName.append(TEMPL_SUFFIX);
		
		return templateFileName.toString();
	}
	
	
	protected String getFormId(DataContainer dc) {
	
		Object obj = "";
		try{
			obj = dc.get(DataContainerTag.FORM_ID);
			String formId = (String) obj;
			if(!StringUtils.isEmpty(formId)) return formId;
			else throw new PrintingException("Form ID in the data container is not defined or is empty");

		}catch (Exception ex) {
			throw new PrintingException("Form ID type in the data container is " + obj.getClass().getSimpleName() + " It has to be a String");
		}

	}
	


}
