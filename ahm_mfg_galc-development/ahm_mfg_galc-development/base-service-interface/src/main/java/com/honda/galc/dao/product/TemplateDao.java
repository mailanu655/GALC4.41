package com.honda.galc.dao.product;

import java.util.Date;
import java.util.List;

import com.honda.galc.entity.product.Template;
import com.honda.galc.service.IDaoService;

public interface TemplateDao extends IDaoService<Template, String> 
{
	public Template findTemplateByTemplateName(String templateName);
	public List<Template> findTemplates();
	public Template maxTemplateName(Template template);
	public List<Template> findPrinters(String device);
	public List<Template> findByTemplateType(String type);
	public Date findUpdateTimestamp(String templateName);
	public List<Template> findAllTemplates();
	public List<Template> searchTemplates(String templateName, String formId);
}
