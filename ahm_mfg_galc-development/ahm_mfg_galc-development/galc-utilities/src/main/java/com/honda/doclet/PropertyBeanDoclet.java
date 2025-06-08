package com.honda.doclet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.DocErrorReporter;

/**
 * 
 * <h3>PropertyBeanDoclet Class description</h3>
 * <p> PropertyBeanDoclet description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Suriya Sena<br>
 * May 24, 2014
 *
 */

/**
 * Main entry point
 */
public class PropertyBeanDoclet {

	private static final String OUTPUTPATHARG = "-outputpath";
	private static final String IPROPERTY = "IProperty"; 
	private static String outputpath;
	private ClassDoc iPropertyClassDoc; // Parent of PropertyBean in the Javadoc world

	/*
	 * Main entry point
	 */
	public static boolean start(RootDoc root) {
		outputpath = getOutputpath(root.options());
		return (new PropertyBeanDoclet()).generateDocumentation(root);
	}

	private static String getOutputpath(String[][] options) {
		for (int i = 0; i < options.length; i++) {
			String[] opt = options[i];
			if (opt[0].equals(OUTPUTPATHARG)) {
				outputpath = opt[1];
			}
		}
		return outputpath;
	}

	public static int optionLength(String option) {
		if (option.equals(OUTPUTPATHARG)) {
			return 2;
		}
		return 0;
	}

	public static boolean validOptions(String options[][],
			DocErrorReporter reporter) {
		boolean foundTagOption = false;
		for (int i = 0; i < options.length; i++) {
			String[] opt = options[i];
			if (opt[0].equals(OUTPUTPATHARG)) {
				if (foundTagOption) {
					reporter.printError(String.format(
							"Only one %s option allowed.", OUTPUTPATHARG));
					return false;
				} else {
					foundTagOption = true;
				}
			}
		}

		if (!foundTagOption) {
			reporter.printError(String
					.format("Usage: javadoc %s pathtopropertyfile.html -doclet blah blah blah...",
							OUTPUTPATHARG));
		}
		return foundTagOption;
	}

	/*
	 * Run as a stand alone app, useful for debugging in eclipse
	 */

	
	public static void main(String[] args) {

		String params[] = { "-doclet", PropertyBeanDoclet.class.getName(),
				"-sourcepath", "../BaseClientFx/src;../BaseCommon/src;../BaseCommonService/src;../BaseEntity/src;;../BasePersistence/src;../BaseService/src;../BaseServiceInterface/src;../BaseWeb/src;../BaseWebStart/src;../ConfigWeb/src;../DeviceControl/src;../GALC_QICS/src;../JcaAdapter/src/;JcaSocketEJB/src;../LotControlFx/src;../OifConfigWeb/src;../OifServiceWeb/src;../RestWeb/src;../TeamLeaderFx/src",
				"-classpath", "../BaseClientFx/bin;../BaseCommon/bin;../BaseCommonService/bin;../BaseEntity/bin;../BaseService/bin;../BaseServiceInterface/bin",
				"-subpackages", "com.honda",
				"-outputpath", "c:\\properties.html"	
		};
		

		com.sun.tools.javadoc.Main.execute("Program", params);
	}

	private boolean generateDocumentation(RootDoc root) {
		List<PropertyBeanStruct> beanList = buildPropertyBeanStructList(root);
		if (beanList == null) {
			return false;
		}

		beanList = sort(beanList);
		try {
			printBeanListAsHTML(beanList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}

	public List<PropertyBeanStruct> sort(List<PropertyBeanStruct> list) {
		Collections.sort(list, new Comparator<PropertyBeanStruct>() {
			@Override
			public int compare(PropertyBeanStruct bean1,PropertyBeanStruct bean2) {
				return bean1.classname.compareTo(bean2.classname);
			}
		});
		return list;
	}

	public void printBeanListAsText(List<PropertyBeanStruct> beanList) {
		for (PropertyBeanStruct propertyBean : beanList) {
			System.out.println(propertyBean.toString());
		}
		System.out.println("propertyBean Count " + beanList.size());
	}

	public void printBeanListAsHTML(List<PropertyBeanStruct> beanList) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File(outputpath));
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<style TYPE='text/css'>");
		out.println(" body {font-family: 'Arial'}");
		out.println("tr:nth-child(even) {background: #E7FCEE}");
		out.println("  tr:nth-child(odd) {background: #FFF}");
		out.println("  table{font-family:'Arial';}");
		out.println("  table { margin: 1em; border-collapse: collapse; }");
		out.println("  td, th { padding: .4em; border: 1px #ccc solid; }");
		out.println("</style>");
		out.println("</HEAD>");
		out.println("<BODY>");
		out.println(String.format("<p>Generated documentation for %d beans on %s by %s.</p>\n",beanList.size(), new Date(), this.getClass().getName()));
		out.println(String.format("<h1>PropertyBean Quick Reference</h1>\n"));

		out.println(PropertyBeanStruct.toHtmlSummary(beanList));
		
		for (PropertyBeanStruct propertyBean : beanList) {
			out.println(propertyBean.toHtml());
		}
		out.println("</BODY>");
		out.println("</HTML>");
		out.close();

	}

	private List<PropertyBeanStruct> buildPropertyBeanStructList(RootDoc root) {
		ArrayList<PropertyBeanStruct> beanList = new ArrayList<PropertyBeanStruct>();
		iPropertyClassDoc = getClassDoc(root.classes(), IPROPERTY);
		if (iPropertyClassDoc != null) {
			for (ClassDoc classDoc : root.classes()) {
				if (classDoc.subclassOf(iPropertyClassDoc)) {
					beanList.add(getPropertyBeanStruct(classDoc));
				}
			}
		} else {
			System.err.println("Failed to find IProperty class information. Perhaps the sourcepath or classpath is not correct.");
		}

		return beanList;
	}

	private PropertyBeanStruct getPropertyBeanStruct(ClassDoc classDoc) {
		PropertyBeanStruct propertyBeanStruct = new PropertyBeanStruct(
				classDoc.name());

		// Get PropertyBean Annotation info
		for (AnnotationDesc annotationDesc : classDoc.annotations()) {
			if (annotationDesc.annotationType().name().equals("PropertyBean")) {
				for (AnnotationDesc.ElementValuePair element : annotationDesc.elementValues()) {
					if (element.element().name().equals("componentId")) {
						propertyBeanStruct.componentId = element.value().toString().replace("\"", "");
					} else if (element.element().name().equals("prefix")) {
						propertyBeanStruct.prefix = element.value().toString();
					} else if (element.element().name().equals("defaultValue")) {
						propertyBeanStruct.defaultValue = element.value().toString();
					}
				}
			}
		}
		// Get PropertyBeanAttribute info
		for (MethodDoc methodDoc : classDoc.methods()) {
			PropertyBeanAttributeStruct propertyBeanAttribStruct = getPropertyBeanAttributeStruct(methodDoc);
			propertyBeanStruct.propertyList.add(propertyBeanAttribStruct);
		}
		// If the property bean is not a direct descendant of IProperty handle
		// super interface(s)/class
		if (getClassDoc(classDoc.interfaces(), IPROPERTY) == null) {
			for (ClassDoc classDocInf : classDoc.interfaces()) {
				if (classDocInf.subclassOf(iPropertyClassDoc)) {
					PropertyBeanStruct myParentBean = getPropertyBeanStruct(classDocInf);
					propertyBeanStruct.parentBeanList.add(myParentBean);
				}
			}
		}
		return propertyBeanStruct;
	}

	private PropertyBeanAttributeStruct getPropertyBeanAttributeStruct(
			MethodDoc methodDoc) {
		PropertyBeanAttributeStruct propertyBeanAttributeStruct = new PropertyBeanAttributeStruct(
				methodDoc.name());

		propertyBeanAttributeStruct.commentText = methodDoc.commentText();
		for (AnnotationDesc annotationDesc : methodDoc.annotations()) {
			if (annotationDesc.annotationType().name()
					.equals("PropertyBeanAttribute")) {
				for (AnnotationDesc.ElementValuePair element : annotationDesc
						.elementValues()) {
					if (element.element().name().equals("propertyKey")) {
						propertyBeanAttributeStruct.propertyKey = element.value().toString().replace("\"", "");
					} else if (element.element().name().equals("defaultValue")) {
						propertyBeanAttributeStruct.defaultValue = element.value().toString();
					}
				}
			}

		}
		return propertyBeanAttributeStruct;
	}

	private ClassDoc getClassDoc(ClassDoc[] classDocArr, String name) {
		for (ClassDoc classDoc : classDocArr) {
			if (classDoc.name().equals(name)) {
				return classDoc;
			}
		}
		return null;
	}

}
