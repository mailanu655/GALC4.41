package com.honda.galc.client.qi.productcheck;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javafx.scene.web.WebView;
import net.miginfocom.layout.CC;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.NavigateTabEvent;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.ProductCheckType;


/**
* <h3>Class description</h3> <h4>Description</h4>
* <p>
* <code>ProductCheckView</code> is ... .
* </p>
* <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
* <TD>&nbsp;</TD>
* <TD>&nbsp;</TD>
* <TD>0.1</TD>
* <TD>(none)</TD>
* <TD>Initial Realse</TD>
* </TR>
* </TABLE>
* 
* @see
* @ver 0.1
* @author L&T Infotech
*/

public class ProductCheckView extends AbstractQiProcessView<ProductCheckModel, ProductCheckController>{

	Map<String, Object> productItemCheckResults;
	Map<String, Object> productWarnCheckResults;
	
	private WebView webView;
	
	public ProductCheckView(MainWindow window) {
		super(ViewId.PRODUCT_CHECK, window);
	}

	@Override
	public void reload() {
		
		getController().submitWarnCheckProductState();
		productWarnCheckResults = getController().getProductWarnCheckResults();

		getController().submitItemCheckProductState();
		productItemCheckResults = getController().getProductItemCheckResults();
		
		reloadProductCheckData();
	}

	@Override
	public void start() {
		
	}

	@Override
	public void initView() {

		MigPane migPane = new MigPane("insets 0", "[max, fill]");
		webView= new WebView();
		migPane.add(webView, new CC().grow());
		this.setTop(migPane);
		
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
    
    protected String generateProductWarnCheckContent(Map<String, Object> mProductCheckResults) {
		Logger.getLogger().check("Product Warn Check Results: " + mProductCheckResults);
		return generateProductCheckContent(mProductCheckResults, "#c7c7c7");
	}
	
	protected String generateProductItemCheckContent(Map<String, Object> mProductCheckResults) {
		Logger.getLogger().check("Product Item Check Results: " + mProductCheckResults);
		return generateProductCheckContent(mProductCheckResults, "#c7c7c7");
	}
	
	private void setWebView(String content){
		webView.getEngine().loadContent(content);
	}

	public void reloadProductCheckData() {
		QiProgressBar progressBarUtility = null;
		try {
			progressBarUtility = QiProgressBar.getInstance("Loading Product Check View.","Loading Product Check View.",
					getModel().getProductId(),getStage(),true);
			progressBarUtility.showMe();
			if (productItemCheckResults == null && productWarnCheckResults == null) {
				return;
			}
			StringBuffer htmlContent = new StringBuffer();
			if (getProductWarnCheckResults() != null && !getProductWarnCheckResults().isEmpty()) {
				htmlContent.append("<div style='padding:2px; background-color:FF8000; font-size: 17px; font-weight: bold; font-family: dialog;'>Product Check - Warning</div>");
				htmlContent.append(generateProductWarnCheckContent(getProductWarnCheckResults()));
			}
			if (getProductItemCheckResults() != null && !getProductItemCheckResults().isEmpty()) {
				htmlContent.append("<div style='padding:2px; background-color:RED; font-size: 17px; font-weight: bold; font-family: dialog;'>Product Check - Failure</div>");
				htmlContent.append(generateProductItemCheckContent(getProductItemCheckResults()));
			}
		
			if(htmlContent.toString().isEmpty()){
	
				NavigateTabEvent processEvent = new NavigateTabEvent();
				processEvent.setNavigateToTab(ViewId.DUNNAGE.getViewLabel());
				EventBusUtil.publish(processEvent);
			}
			setWebView(htmlContent.toString());
		}
		finally {
			if(progressBarUtility != null)  {
				progressBarUtility.closeMe();
			}
		}
	}

	
	/**
	 * This method is used to display the data into table format
	 */
	private void generateProductCheckContent(String message, Collection<Object> result, StringBuilder content) {
		Iterator<Object> i = result.iterator();
		int count = 0;
		while (i.hasNext()) {
			count++;
			Object name = i.next();
			String color = "#ededed";
			if (count % 2 == 0) {
				color = "#ffffff";
			}
			content.append("<tr style='font-size: 13px; background: ").append(color).append("'><td>&nbsp;</td>").append("<td width='10%' style='text-align: right;'>").append(count)
			.append(")</td>").append("<td>").append(name).append("</td></tr>");
		}
	}

	/**
	 * This method is used to append the style and property to the table data
	 */
	private StringBuilder generateProductCheckContent(String message, Object result, int rowCount, StringBuilder content, String color) {
		if (rowCount % 2 == 0) {
			color = "#bababa";
		}
		content.append("<tr style='background: ").append(color).append(";'><td width='10%' style='text-align: right;'>").append(rowCount).append(".</td><td colspan='2'>").append(
				message).append("</td></tr>");
		return content;
	}

	/**
	 * This method is used to generate product pre check content
	 */
	@SuppressWarnings("unchecked")
	private String generateProductCheckContent(Map<String, Object> mProductCheckResults, String color) {
		if (mProductCheckResults == null || mProductCheckResults.isEmpty()) {
			return "";
		}

		StringBuilder content = new StringBuilder();

		int rowCount = 0;
		content.append("<table width='100%' border='0' cellspacing='0' cellpadding='2' style='font-size: 15px; font-weight: bold; font-family: dialog; '>");
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
				handleException(e);
			}

			content = generateProductCheckContent(message, value, rowCount, content, color);

			Collection<Object> collectionValue = null;
			if (value instanceof Collection) {
				collectionValue = (Collection<Object>) value;
			} else if (value.getClass().isArray()) {
				Object[] arrayValue = (Object[]) value;
				collectionValue = Arrays.asList(arrayValue);
			} else if (value instanceof Map) {
				@SuppressWarnings("rawtypes")
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
	

}
