package com.honda.galc.entity.product;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import javax.persistence.*;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.TemplateType;

@Entity
@Table(name="TEMPLATES_TBX")
public class Template extends AuditEntry  {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TEMPLATE_NAME")
	private String templateName;
	 
	@Column(name = "TEMPLATE_DESCRIPTION")
	private String templateDescription;

	@Column(name = "TEMPLATE_TYPE")
	private String templateTypeString;

	@Column(name = "TEMPLATE_DATA")
	@Lob
	private byte[] templateDataBytes;
	
	@Column(name = "FORM_ID")
	private String formId;
	
	@Column(name = "REVISION_ID")
	private int revisionId;

	@Transient
    private int revId;

	public Template() {
		super();
	}
	
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public int getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(int revisionId) {
		this.revisionId = revisionId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getTemplateDescription() {
		return templateDescription;
	}
	
	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}
	
	public TemplateType getTemplateType() {
		return TemplateType.valueOf(templateTypeString);
	}

	public void setTemplateTypeString(String templateTypeString) {
		this.templateTypeString = templateTypeString;
	}
	
	public String getTemplateTypeString() {
		return templateTypeString;
	}
	
	public byte[] getTemplateDataBytes() {
		return templateDataBytes;
	}

	public void setTemplateData(byte[] templateDataBytes)
	{
		this.templateDataBytes = templateDataBytes;
	}
	
	public void setTemplateData(String templateData)
	{
		byte[] theByteArray = templateData.getBytes();
		this.templateDataBytes = theByteArray;
	}
	
	public String getTemplateDataString() {
		return new String(templateDataBytes);
	}
		
	public Set<String> getTemplateVariables() {
		TemplateType templateType = getTemplateType();
		Matcher matcher = templateType.getVariablePattern().matcher(getTemplateDataString());
		Set<String> templateVariables = new HashSet<String>();

		while (matcher.find()) {
			templateVariables.add(matcher.group().replaceAll(
					templateType.getVariableDelimiter(), "").replaceAll(
					templateType.getVariableDelimiter(), ""));
		}
		return templateVariables;
	}
	
	public String getPopulatedTemplate(Map<String, String> replacementValues) {
		String populatedTemplate = getTemplateDataString();
		TemplateType templateType = getTemplateType();

		for (String variable : getTemplateVariables())
			populatedTemplate = populatedTemplate.replaceAll(templateType.getVariableDelimiter()
					+ variable 
					+ templateType.getVariableDelimiter(),
					replacementValues.get(variable));

		return populatedTemplate;
	}

	public Object getId() {
		return getTemplateName();
	}

	public int getRevId() {
		return revId;
	}

	public void setRevId(int revId) {
		this.revId = revId;
	}
}


