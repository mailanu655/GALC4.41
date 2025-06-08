package com.honda.galc.client.qics.view.fragments;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.ProductCheckType;


/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>CheckResultsPane</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Apr 17, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class CheckResultsPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	Map<String, Object> productPreCheckResults;
	Map<String, Object> productItemCheckResults;
	Map<String, Object> productWarnCheckResults;
	private JEditorPane htmlContentPanel;
	
	public CheckResultsPane() {
		init();
	}
	
	public void reloadPreCheckData() {
		StringBuffer htmlContent = new StringBuffer(); 
		if (getProductWarnCheckResults() != null) {
			htmlContent.append(generateProductWarnCheckHTML());
		}
		if (getProductPreCheckResults() != null) {
			htmlContent.append(generateProductPreCheckHTML());
		}
		getHtmlContentPanel().setText(htmlContent.toString());
	}
	
	public void reloadProductCheckData() {
		if (productItemCheckResults == null && productWarnCheckResults == null) {
			return;
		}
		StringBuffer htmlContent = new StringBuffer(); 
		htmlContent.append(generateProductWarnCheckHTML());
		if (getProductItemCheckResults() != null && !getProductItemCheckResults().isEmpty()){
			htmlContent.append("<div style='padding:2px; background-color:RED; font-size: 10px; font-weight: bold; font-family: dialog;'>Product Check - Failure</div>");
			htmlContent.append(generateProductItemCheckContent(getProductItemCheckResults()));
		}
		getHtmlContentPanel().setText(htmlContent.toString());
	}
	
	protected String generateProductWarnCheckHTML(){
		StringBuffer htmlContent = new StringBuffer();
		if (getProductWarnCheckResults() != null && !getProductWarnCheckResults().isEmpty()){
			htmlContent.append("<div style='padding:2px; background-color:#FF8000; font-size: 10px; font-weight: bold; font-family: dialog;'>Product Check - Warning</div>");
			htmlContent.append(generateProductWarnCheckContent(getProductWarnCheckResults()));
		}
		return htmlContent.toString();
	}
	
	protected String generateProductPreCheckHTML(){
		StringBuffer htmlContent = new StringBuffer();
		if (getProductPreCheckResults() != null && !getProductPreCheckResults().isEmpty()){
			htmlContent.append("<div style='padding:2px; background-color:WHITE; font-size: 10px; font-weight: bold; font-family: dialog;'>Product Pre Check</div>");
			htmlContent.append(generateProductPreCheckContent(getProductPreCheckResults()));
		}
		return htmlContent.toString();
	}
	
	protected void init() {
		
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		
		setViewportView(getHtmlContentPanel());
		if (getVerticalScrollBar().isVisible()) {
			getVerticalScrollBar().setValue(0);
			getHtmlContentPanel().setCaretPosition(0);
		}
	}
	
	
	private JEditorPane getHtmlContentPanel() {
		if (htmlContentPanel == null) {
			htmlContentPanel = new JEditorPane();
			htmlContentPanel.setEditable(false);
			htmlContentPanel.setContentType("text/html");
		}
		return htmlContentPanel;
	}
	
	protected String generateProductPreCheckContent(Map<String, Object> mProductCheckResults) {
		Logger.getLogger().check("Product Pre Check Results: " + mProductCheckResults);
		return generateProductCheckContent(mProductCheckResults, "#c7c7c7");
	}
	
	protected String generateProductWarnCheckContent(Map<String, Object> mProductCheckResults) {
		Logger.getLogger().check("Product Warn Check Results: " + mProductCheckResults);
		return generateProductCheckContent(mProductCheckResults, "#c7c7c7");
	}
	
	protected String generateProductItemCheckContent(Map<String, Object> mProductCheckResults) {
		Logger.getLogger().check("Product Item Check Results: " + mProductCheckResults);
		return generateProductCheckContent(mProductCheckResults, "#c7c7c7");
	}
		
	@SuppressWarnings("unchecked")
	protected String generateProductCheckContent(Map<String, Object> mProductCheckResults, String color) {
		if (mProductCheckResults == null || mProductCheckResults.isEmpty()) {
			return "";
		}

		StringBuilder content = new StringBuilder();

		int rowCount = 0;
		content.append("<table width='100%' border='0' cellspacing='0' cellpadding='2' style='font-size: 10px; font-weight: bold; font-family: dialog; '>");
		for (Map.Entry<String, Object> entry : mProductCheckResults.entrySet()) {
			rowCount++;
			String message = entry.getKey();
			Object value = entry.getValue();

			if (message == null || value == null) {
				continue;
			}

			try {
				ProductCheckType checkType =  ProductCheckType.valueOf(message);
				message = checkType.getName();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			content = generateProductCheckContent(message, value, rowCount, content, color);
			
			Collection<Object> collectionValue = null;
			if (value instanceof Collection) {
				collectionValue = (Collection<Object>) value;
			} else if (value.getClass().isArray()) {
				Object[] arrayValue = (Object[]) value;
				collectionValue = Arrays.asList(arrayValue);
			} else if (value instanceof Map) {
				Map mapValue = (Map) value;
				collectionValue = mapValue.entrySet();
			}
			
			if (collectionValue != null) {
				generateProductCheckContent(message, collectionValue, content);				
			}
		}
		content.append("</table>");
		return content.toString();
	}

	public StringBuilder generateProductCheckContent(String message, Object result, int rowCount, StringBuilder content, String color) {
		if (rowCount % 2 == 0) {
			color = "#bababa";
		}
		content.append("<tr style='background: ").append(color).append(";'><td width='5%' style='text-align: right;'>").append(rowCount).append(".</td><td colspan='2'>").append(
				message).append("</td></tr>");
		return content;
	}

	public void generateProductCheckContent(String message, Collection<Object> result, StringBuilder content) {
		Iterator<Object> i = result.iterator();
		int ix = 0;
		while (i.hasNext()) {
			ix++;
			Object name = i.next();
			String color = "#ededed";
			if (ix % 2 == 0) {
				color = "#ffffff";
			}
			content.append("<tr style='font-size: 9px; background: ").append(color).append("'><td>&nbsp;</td>").append("<td width='5%' style='text-align: right;'>").append(ix)
					.append(")</td>").append("<td>").append(name).append("</td></tr>");

		}
	}
	public Map<String, Object> getProductItemCheckResults() {
		return productItemCheckResults;
	}


	public void setProductItemCheckResults(Map<String, Object> productItemCheckResults) {
		this.productItemCheckResults = productItemCheckResults;
	}

	/**
     * @return the productWarnCheckResults
     */
    public Map<String, Object> getProductWarnCheckResults() {
    	return productWarnCheckResults;
    }

	/**
     * @param productWarnCheckResults the productWarnCheckResults to set
     */
    public void setProductWarnCheckResults(Map<String, Object> productWarnCheckResults) {
    	this.productWarnCheckResults = productWarnCheckResults;
    }

	/**
     * @return the productPreCheckresults
     */
    public Map<String, Object> getProductPreCheckResults() {
    	return productPreCheckResults;
    }

	/**
     * @param productPreCheckresults the productPreCheckresults to set
     */
    public void setProductPreCheckResults(Map<String, Object> productPreCheckResults) {
    	this.productPreCheckResults = productPreCheckResults;
    }
	
}
