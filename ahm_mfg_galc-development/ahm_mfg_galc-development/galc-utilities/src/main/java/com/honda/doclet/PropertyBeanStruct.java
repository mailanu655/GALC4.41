package com.honda.doclet;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.service.property.PropertyService;

public class PropertyBeanStruct {
    String componentId;
    String classname;
	String prefix;
	String defaultValue;
	
	ArrayList<PropertyBeanStruct>           parentBeanList = new ArrayList<PropertyBeanStruct> ();
	ArrayList<PropertyBeanAttributeStruct>  propertyList = new ArrayList<PropertyBeanAttributeStruct>();
	
	PropertyBeanStruct(String classname) {
		this.classname=	 classname;
		this.componentId = PropertyService.getComponentByBeanName(classname);
		this.prefix="";
		this.defaultValue="";
	}
	
	public String toString() {
        return toStringExt(this, true);
	}
	
	
	private String toStringExt(PropertyBeanStruct bean, boolean isRoot) {
		StringBuilder sb = new StringBuilder();
		
		if (isRoot) {
		  sb.append(String.format("componentId=%s, prefix=%s, defaultValue=%s\n", bean.componentId, bean.prefix, bean.defaultValue));
		}
		
		for (PropertyBeanStruct parent :  bean.parentBeanList) {
			sb.append(String.format("parent %s \n",parent.componentId));
			
		    sb.append(toStringExt(parent,false));
		}
		
		for (PropertyBeanAttributeStruct property : bean.propertyList) {
			sb.append(String.format("%s  %s %s \n",bean.componentId, bean.classname, property.toString()));
			
		}
		return sb.toString();
	}
	
	public String toHtml() {
        return  toHtmlExt(this, true);
	}
	
	public static String toHtmlSummary( List<PropertyBeanStruct> beanList ) {
		StringBuilder sb = new StringBuilder();
		int colCount=1;

		sb.append("<a id='index'><H1>Index</H1></a><table>");
		sb.append("<tr>\n");
		for (PropertyBeanStruct propertyBean : beanList) {
			sb.append(String.format("<!-- %d -->\n",colCount));
			sb.append(String.format("<td><a href='#%1$s'>%1$s</a></td>",propertyBean.classname));
		    if ((colCount++ % 4) == 0) {
				sb.append("\n</tr>\n<tr>\n");
		    }
		}
		
		if ((colCount % 4 ) != 0) {
		    sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		
		return sb.toString();
	}
	
	private String toHtmlExt(PropertyBeanStruct bean, boolean isRoot) {
		StringBuffer sb =new StringBuffer();
		 
		if (isRoot) {
		  sb.append(String.format("<a id='%1$s'><h2>%1$s</h2></a>\n", classname));
		  sb.append("<P>\n");
		  sb.append("<table>");		  
	      sb.append(String.format("<tr><td width=30%%>ComponentID</td><td>%s</td></tr>\n", componentId));
	      sb.append(String.format("<tr><td width=30%%>Classname</td><td>%s</td></tr>\n", classname));
	      sb.append(String.format("<tr><td width=30%%>Prefix</td><td>%s</td></tr>\n", prefix));
	      sb.append(String.format("<tr><td width=30%%>DefaultValue</td><td>%s</td></tr>\n",defaultValue));
		  for (String parentInfo : buildParentList(bean,null)) {
				sb.append(String.format("<tr><td>Inherits</td><td>%s</td></tr>\n",parentInfo));
	  	  }
		  sb.append("</table>\n");
		  sb.append("</P>\n");
		  sb.append("<table  border=1 width=100%>\n");
		  sb.append(String.format("<tr><th width=15%%>ComponentId</th><th width=20%%>PropertyKey</th><th width=25%%>Default</th><th width=30%%>Description</th></tr>\n"));
		}
		
		for (PropertyBeanStruct parent :  bean.parentBeanList) {
			sb.append(toHtmlExt(parent,false));
		}

		for (PropertyBeanAttributeStruct property : bean.propertyList) {
			sb.append(String.format("<tr><td>%s</td>%s</tr>\n",bean.componentId, property.toHtml()));
		}
		
		if (isRoot) {
		  sb.append("</table><a href='#index'><p>Index</p></a>\n");
		}

		return sb.toString();
	}
	
	public List<String>  buildParentList( PropertyBeanStruct bean, List<String> myParents) {
		if (myParents == null) {
			myParents = new ArrayList<String>();
		}
		
		for ( PropertyBeanStruct parent : bean.parentBeanList) {
		  myParents.add(String.format("%s (%s)", parent.componentId, parent.classname));
		  buildParentList(parent, myParents);
		}
		
		return myParents;
	}

}
