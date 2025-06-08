package com.honda.galc.client.datacollection.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.common.component.RowColorTableModelCellRenderer;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.ViewProperty;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.VinDisplayValue;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;


public class SingleColumnTablePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	protected List<String> data = new ArrayList<String>();
	protected String[] productListTableCol = {"Skipped Product"};
	private JScrollPane scrollPaneListTable = null;
	protected JTable table = null;
	private int panelwidth = 210;
	private int panelhight = 116;
	private int fontSize = 18;
	private FrameDao frameDao;
	protected CommonTlPropertyBean propertyBean;
	protected ClientContext context;

	public SingleColumnTablePanel(String colname) {
		
		super();
		this.productListTableCol[0] = colname;
	}
	
	public void initialize(ViewProperty property)
	{
		try {
			
			AnnotationProcessor.process(this);
			context = DataCollectionController.getInstance().getClientContext();
			panelwidth = property.getSkippedProductMonitorPanelWidth();
			fontSize = property.getSkippedProductMonitorPanelFontSize();
			panelhight = property.getSkippedProductMonitorPanelHeight();
			setLayout(new BorderLayout());
			setSize(getPanelwidth(), getPanelhight());
			setBounds(2, 2, getPanelwidth(), getPanelhight());
			add(getScrollTablePanel());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	private JScrollPane getScrollTablePanel() {
		if(scrollPaneListTable == null)
		{
			table = getTable();
		    scrollPaneListTable = new JScrollPane();		    
		    scrollPaneListTable.getViewport().add(table);
		    scrollPaneListTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    scrollPaneListTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		    scrollPaneListTable.setPreferredSize(new Dimension(getPanelwidth(), getPanelhight()));
		}
		
		return scrollPaneListTable;
		
	}

	protected JTable getTable() {
		if (table == null) {
			try {
				createTable();
				addTableListeners();

			} catch (java.lang.Throwable ex) {
				handleException(ex);
			}
		}
		return table;

	}

	protected void addTableListeners() {
		table.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e)
			{
				JTable table = (JTable)e.getSource();
				Point p = e.getPoint();
				if(e.getClickCount() == 2)
				{
					int row = table.rowAtPoint(p);
					//int col = table.columnAtPoint(p);
					//String value = (String)table.getValueAt(row,col);

					data.remove(row);
					DefaultTableModel tm = new DefaultTableModel(createTableData(), productListTableCol);
					table.setModel(tm);
				}
			}

		} );
	}

	@SuppressWarnings("serial")
	protected JTable createTable() {
		table = new JTable(){
			public boolean isCellEditable(int rowIndex, int vColIndex) {
		        return false;
		    }
		};
		//Initialize the product list
		RowColorTableModelCellRenderer tm = getTableModel();

		table.setModel(tm);
		table.setDefaultRenderer(Object.class, tm);
		table.getColumnModel().getColumn(0).setWidth(getPanelwidth());
		table.setFont(new Font("sansserif", 1, fontSize));
		table.setRowHeight(22);
		return table;
	}

	private RowColorTableModelCellRenderer getTableModel() {
		RowColorTableModelCellRenderer tm = new RowColorTableModelCellRenderer(createTableData(), productListTableCol);
		
		for(int i = 0; i < tm.getRowCount(); i++)
			tm.setRowColor(i, Color.red);
		
		return tm;
	}

	private void handleException(java.lang.Throwable e) {
		e.printStackTrace();
	}
	
	public Object[][] createTableData()
	{
		Object [][] tdata;
		if (data != null && data.size() > 0) {
			tdata = new Object[data.size()][];
			for(int i = 0; i < data.size(); i++)
			{
				tdata[i] = new Object[1];
				tdata[i][0] = data.get(i);
			}
		} else {
			tdata = new Object[0][];
		}	
		return tdata;
	}

	//getter && setter
	public List<String> getData() {
	    return data;
	}

	public void refresh() {
	    RowColorTableModelCellRenderer tm = getTableModel();
	    table.setModel(tm);
	    table.setDefaultRenderer(Object.class, tm);

	}
	
	public int getPanelhight() {
		return panelhight;
	}

	public void setPanelhight(int panelhight) {
		this.panelhight = panelhight;
	}

	public int getPanelwidth() {
		return panelwidth;
	}

	public void setPanelwidth(int panelwidth) {
		this.panelwidth = panelwidth;
	}

	public String getProductListTableCol() {
		return productListTableCol[0];
	}

	public void setProductListTableCol(String engineListTableCol) {
		productListTableCol[0] = engineListTableCol;
		RowColorTableModelCellRenderer tm = getTableModel();
    	table.setModel(tm);
    	table.setDefaultRenderer(Object.class, tm);
	}

	@EventSubscriber(eventClass = SkippedProduct.class)
	public void add(SkippedProduct event){
		updateTable(event);
	}

	protected void updateTable(SkippedProduct event) {
		if(!StringUtils.isEmpty(event.getId().getProductId())){

			if(ProductType.FRAME.name().equals(getPropertyBean().getProductType())) {
				VinDisplayValue displayValue = VinDisplayValue.valueOf(propertyBean.getSkippedProductDisplayValue());
				if(!event.getId().getProductId().contains(Delimiter.HYPHEN)) {
					updateAndRefreshSkipSingleProduct(event, displayValue);
				} else {
					updateAndRefreshSkipMultipleProducts(event, displayValue);
				}

			} else
				updateAndRefresh(event.getId().getProductId().trim()); 
		}
		
	}

	private void updateAndRefreshSkipSingleProduct(SkippedProduct event, VinDisplayValue displayValue) {
		Frame frame = getFrameDao().findByKey(event.getId().getProductId().trim());

		if(frame == null) {
			Logger.getLogger().error("Invalid Skipped Product Id:" + event.getId().getProductId());
			return;
		}

		updateAndRefresh(frame.getDiplayValue(displayValue, propertyBean.getLineRefNumberOfDigits()));
	}
	
	private void updateAndRefreshSkipMultipleProducts(SkippedProduct event, VinDisplayValue displayValue) {
		String[] prodIds = event.getId().getProductId().split(Delimiter.HYPHEN);
		Frame frame1 = getFrameDao().findByKey(prodIds[0].trim());
		StringBuilder sb = new StringBuilder(frame1.getDiplayValue(displayValue, propertyBean.getLineRefNumberOfDigits()));
		sb.append(Delimiter.HYPHEN);
		Frame frame2 = getFrameDao().findByKey(prodIds[1].trim());
		
		if(frame2 != null) {
			String vin2 = frame2.getDiplayValue(displayValue, propertyBean.getLineRefNumberOfDigits());
			if(displayValue == VinDisplayValue.LINE_REF)
				sb.append(vin2);
			else {
				
				sb.append(vin2.substring(vin2.length() - getPropertyBean().getSkippedProductLastNumberOfDigits()));
			}
		}
		
		updateAndRefresh(sb.toString());
		
	}

	public CommonTlPropertyBean getPropertyBean() {
		if(propertyBean == null)
			propertyBean = PropertyService.getPropertyBean(CommonTlPropertyBean.class, context.getProcessPointId());
		
		return propertyBean;
	}

	private FrameDao getFrameDao() {
		if(frameDao == null)
			frameDao = ServiceFactory.getDao(FrameDao.class);
		
		return frameDao;
		
	}
	
	private void updateAndRefresh(String displayId) {
		if(!data.contains(displayId)){
			data.add(displayId);
			refresh();
		}
	}

}
