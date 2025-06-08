package com.honda.galc.service.printing;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.product.Template;

/**
 * @author Alex Johnson
 * @date Aug 11, 2016
 */
public class CompiledJasperPrintAttributeConvertor extends JasperPrintAttributeConvertor {

	public CompiledJasperPrintAttributeConvertor(Logger logger) {
		super(logger);
		logger.debug("inside the CompiledJasperPrintAttributeConvertor");
	}

	public CompiledJasperPrintAttributeConvertor(List<PrintAttributeFormat> printAttributes) {
		super(printAttributes);

	}

	public List<String> parseTemplateData(Template template) {
		ArrayList<String> templateAttributes = new ArrayList<String>();
		JasperReport jasperReport = null;
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(new ByteArrayInputStream(template.getTemplateDataBytes()));
		} catch (JRException e) {
			e.printStackTrace();
			logger.error(e, e.getMessage());
		}
		JRParameter[] params = jasperReport.getParameters();
		for (JRParameter param : params) {
			if (!param.isSystemDefined()) {
				templateAttributes.add(param.getName());
			}
		}
		return templateAttributes;
	}
}
