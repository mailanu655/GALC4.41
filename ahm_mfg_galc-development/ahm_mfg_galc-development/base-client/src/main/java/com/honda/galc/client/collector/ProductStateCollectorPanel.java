package com.honda.galc.client.collector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.ProductStateProductPanel;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.ProductCheckDto;

public class ProductStateCollectorPanel extends CollectorPanel {

	private static final long serialVersionUID = 1L;
	private ObjectTablePane<ProductCheckDto> productStatePane;
	private JSplitPane splitPane;
	private ProductStateProductPanel productPane;

	public ProductStateCollectorPanel(ProductStateCollectorClientPropertyBean property,	MainWindow mainWin) {
		super(property, mainWin);
	}
	
	public void refresh(){
		getProductPane().refresh();
		getCancelButton().setVisible(false);
		getProductPane().getStatusText().setText("");
		getProductPane().getStatusText().setBackground(Color.decode("#f2f2f2"));
		getProductPane().getProductIdField().requestFocus();
	}
	
	protected void init() {
		setLayout(new BorderLayout());
		if(getProperty().isShowProductId())
			add(getProductPane(),BorderLayout.NORTH);
		
		add(productResultCheck(), BorderLayout.CENTER);
		getSplitPane().setVisible(false);
		add(getButtonPane(), BorderLayout.SOUTH);
	}
	
	public ProductStateProductPanel getProductPane() {
		if(productPane == null)
			productPane = new ProductStateProductPanel(mainWin, mainWin.getApplicationContext().getProductTypeData());
		
		return productPane;
	}
	
	private  JSplitPane productResultCheck(){
		splitPane = new JSplitPane();
		productStatePane = createProductStatePane();
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				productStatePane, getResultScrollPane());
		splitPane.setDividerLocation(getProperty().getProductStateDividerLocation());
		return splitPane;
	}
	
	protected ObjectTablePane<ProductCheckDto> createProductStatePane() {
		ColumnMappings columnMappings;
		if (getProductPane().getProductType().equals(ProductType.FRAME)) 
			columnMappings = ColumnMappings
					.with("Product Id", "productId").put("AF On Seq", "afOnSeqNo").put("Status", "status")
					.put("Reason", "reason").put("Date", "date");
		else 
			columnMappings = ColumnMappings
					.with("Product Id", "productId").put("Status", "status")
					.put("Reason", "reason").put("Date", "date");
		
		ObjectTablePane<ProductCheckDto> pane = new ObjectTablePane<ProductCheckDto>(
				columnMappings.get(), true);

		pane.setSize(1000, 400);
		pane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setColumnSize(pane);
		return pane;
	}
	
	protected void setColumnSize(ObjectTablePane<ProductCheckDto> pane) {
		Map<String, Integer> columnWidths = new HashMap<String, Integer>();
		columnWidths.put("Product Id", 150);
		columnWidths.put("AF On Sequence", 150);
		columnWidths.put("Status", 100);
		columnWidths.put("Reason", 200);
		columnWidths.put("Date", 200);		
	}

	protected ProductStateCollectorClientPropertyBean getProperty() {
		return (ProductStateCollectorClientPropertyBean)this.property;
	}
	
	public ObjectTablePane<ProductCheckDto> getProductStatePane() {
		return productStatePane;
	}

	public void setProductStatePane(
			ObjectTablePane<ProductCheckDto> productStatePane) {
		this.productStatePane = productStatePane;
	}

	public JSplitPane getSplitPane() {
		if(splitPane == null)
			splitPane = new JSplitPane();
		return splitPane;
	}

	public void setSplitPane(JSplitPane splitPane) {
		this.splitPane = splitPane;
	}
	
	
}
