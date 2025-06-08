package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.Parameters;



public class TemplateDaoImpl extends BaseDaoImpl<Template,String> implements TemplateDao 
{
	 public static final String FIND_UPDATE_DATE = "select e.updateTimestamp from Template e where e.templateName = :templateName";
	 public static final String NOT_NULL_TEMPLATES = "SELECT  TMPL.TEMPLATE_NAME, TMPL.FORM_ID, TMPL.TEMPLATE_DESCRIPTION, TMPL.TEMPLATE_TYPE, TMPL.REVISION_ID FROM TEMPLATES_TBX TMPL "
	 		+ "WHERE TMPL.TEMPLATE_TYPE IS NOT NULL AND TMPL.TEMPLATE_TYPE !='' AND TMPL.TEMPLATE_TYPE LIKE('%IMAGE%')";
	 
	 final String FIND_MAX_TEMPLATE_NAME = "SELECT MAX(t.templateName) FROM Template t where t.templateName like";
	 String maxTemplateName, templateNameStartsWith;
	  int padLength = 4; 
	 public List<Template> findTemplates() 
	 {
	    	return findAll();
	 }
	 
	 public List<Template> findPrinters(String device)
	 {
		 Parameters parameters = Parameters.with("formId", device);
		 return findAll(parameters);
	 }
	 
	 public Template findTemplateByTemplateName(String templateName)
	 {
		 return findByKey(templateName);
	 }
	 
	
	 
	 public Template maxTemplateName(Template template)
	 {
		
		 maxTemplateName = template.getTemplateName().toString();
		 boolean isRevisionId = maxTemplateName.contains(".REV");
		 if (isRevisionId)
		 {
			 int length = maxTemplateName.length();
			 templateNameStartsWith = maxTemplateName.substring(0, length-padLength);
			 maxTemplateName = entityManager.createQuery(FIND_MAX_TEMPLATE_NAME +"'"+ templateNameStartsWith +"%'").getSingleResult().toString();
		 	 int revId = Integer.parseInt(maxTemplateName.substring(length-padLength))+1;
			 String revIdString = Integer.toString(revId);
			 int padLen = padLength - revIdString.length();
			 StringBuffer strBuf = new StringBuffer(padLength);
			 while(padLen > 0) 
			 { 
				strBuf.append(0); 
				padLen--; 
			 }
			 template.setRevisionId(revId);
			 maxTemplateName = templateNameStartsWith + strBuf.toString() + revIdString;
			 
			
		 }
		 else
		 {
			 maxTemplateName = entityManager.createQuery(FIND_MAX_TEMPLATE_NAME +"'"+ maxTemplateName +"%'").getSingleResult().toString();
			 if(maxTemplateName.contains(".REV"))
			{
				 int length = maxTemplateName.length();
				 templateNameStartsWith = maxTemplateName.substring(0, length-padLength);
				 int revId = Integer.parseInt(maxTemplateName.substring(length-padLength))+1;
				 String revIdString = Integer.toString(revId);
				 int padLen = padLength - revIdString.length();
				 StringBuffer strBuf = new StringBuffer(padLength);
				 while(padLen > 0) 
				 { 
					strBuf.append(0); 
					padLen--; 
				 }
				 template.setRevisionId(revId);
				 maxTemplateName = templateNameStartsWith + strBuf.toString() + revIdString;
				
			}
			else {
			 maxTemplateName = maxTemplateName + ".REV0001";
			 template.setRevisionId(1);
			}
		 }
		 template.setTemplateName(maxTemplateName);
		 return template;
	 }

	public List<Template> findByTemplateType(String type) {
		Parameters params = new Parameters();
		params.put("templateTypeString", type);
		
		return findAll(params);
	}
	
	public Date findUpdateTimestamp(String templateName) {
		return findFirstByQuery(FIND_UPDATE_DATE, Date.class, Parameters.with("templateName", templateName));
	}

	
	public List<Template> findAllTemplates() {
		List<Object[]> availableTemplateLst = findAllByNativeQuery(NOT_NULL_TEMPLATES, null, Object[].class); 
		return constructTemplateLst(availableTemplateLst);
	}
	
	private List<Template> constructTemplateLst(List<Object[]> templateObjLst){
		List<Template> templateLst = null;
		
		if(templateObjLst.size() == 0) return null;
		
		templateLst = new ArrayList<Template>();
		
		for(Object[] temp : templateObjLst) {
			Template template = new Template();
			template.setTemplateName(temp[0].toString());
			
			if(temp[1] != null)
				template.setFormId(temp[1].toString());
			
			if(temp[2] != null)
				template.setTemplateDescription(temp[2].toString());
			
			template.setTemplateTypeString(temp[3].toString());
			
			if(temp[4] != null)
				template.setRevisionId((Integer)temp[4]);
			else
				template.setRevisionId(0);
			
			templateLst.add(template);	
		}
		
		System.out.println("Final Template result " + templateLst.size());
		return templateLst;
	}

	public List<Template> searchTemplates(String templateName, String formId) {
		
		String SEARCH_QUERY, LIKE_STRING = null;
		
		if(templateName.length() > 0 && formId.length() > 0) {
			LIKE_STRING = " AND (TMPL.TEMPLATE_NAME LIKE ('%"+templateName+"%') AND TMPL.FORM_ID LIKE ('%"+formId+"%'))";
		}else if(templateName.length() > 0 && formId.length() == 0) {
			LIKE_STRING = " AND (TMPL.TEMPLATE_NAME LIKE '%"+templateName+"%')";
		}else {
			LIKE_STRING = " AND (TMPL.FORM_ID LIKE ('%"+formId+"%'))";
		}
			
		SEARCH_QUERY = NOT_NULL_TEMPLATES + LIKE_STRING;
		
		System.out.println("Final Query :: " + SEARCH_QUERY);
		
		List<Object[]> searchTemplateResultLst = findAllByNativeQuery(SEARCH_QUERY, null, Object[].class); 
		System.out.println("Final Search result " + searchTemplateResultLst.size());
		return constructTemplateLst(searchTemplateResultLst);
	}

}


