package com.honda.galc.service.printing;

import java.io.File;
import java.io.IOException;

import com.honda.galc.data.DataContainer;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author Subu Kathiresan
 * @date Oct 10, 2016
 */
public interface IJasperExporter {
	
	public File exportToFile(JasperPrint jasperPrint, DataContainer dc) throws IOException;
	
	public byte[] export(JasperPrint jasperPrint, DataContainer dc);
	
	public String getPrintCommand(DataContainer dc, File file);
}
