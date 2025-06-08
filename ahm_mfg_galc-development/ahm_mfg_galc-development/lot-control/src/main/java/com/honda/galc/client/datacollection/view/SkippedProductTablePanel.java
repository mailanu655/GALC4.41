package com.honda.galc.client.datacollection.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.honda.galc.client.datacollection.property.ViewProperty;
import com.honda.galc.client.knuckle.KnuckleLabelPrintingUtil;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.SkippedProductDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.enumtype.SkippedProductStatus;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * <h3>SkippedProductTablePanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SkippedProductTablePanel description </p>
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
 * @author Paul Chou
 * Jan 25, 2011
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class SkippedProductTablePanel extends SingleColumnTablePanel implements ActionListener{
	private static final String PRINT = "REPRINT";
	private static final long serialVersionUID = 1L;
	
	
	public SkippedProductTablePanel(String colname) {
		super(colname);
	}
	
	@Override
	public void initialize(ViewProperty property) {
		propertyBean = PropertyService.getPropertyBean(CommonTlPropertyBean.class, "Default_CommonProperties");
		productListTableCol = new String[]{"Skipped Product"};
		super.initialize(property);
		
		loadSkippedProduct();
	}

	private void loadSkippedProduct() {
		SkippedProductDao dao = ServiceFactory.getDao(SkippedProductDao.class);
		List<String> list = dao.findProductByProcessPointAndStatus(context.getProcessPointId(),SkippedProductStatus.SKIPPED.getId());
		data.addAll(list);
		refresh();
		
	}

	@Override
	protected void addTableListeners() {
		
		if(!isEnablePrintSkipped()) return;
		
		table.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) showPrintPopupMenu(e);
			}			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) showPrintPopupMenu(e);
			}
		} );
	}

	private boolean isEnablePrintSkipped() {
		//Currently print is only enabled for Knuckle product
		return propertyBean.getProductType().equals(propertyBean.getPrintSkippedProductType());
	}

	protected void showPrintPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(PRINT));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());

	}
	
	protected JMenuItem createMenuItem(String name) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.setName(name);
		menuItem.addActionListener(this);
		menuItem.setEnabled(true);
		return menuItem;
	}

	public void actionPerformed(ActionEvent e) {
		 if(e.getSource() instanceof JMenuItem) {
        	 try{
	        	 JMenuItem menuItem = (JMenuItem)e.getSource();
	        	 if(menuItem.getName().equals(PRINT)) printSkippedProduct();
        	 }catch(Exception ex) {
        		 Logger.getLogger().error(ex, "Exception on print skipped product.");
        	 }
         }
		
	}

	private void printSkippedProduct() {
		if((propertyBean.getProductType().equals(propertyBean.getPrintSkippedProductType()))){
			SubProduct selectedProduct = getSelectedSubProduct();
			Logger.getLogger().info("INFO:", "Print skipped Sub products:", selectedProduct.toString());
			new KnuckleLabelPrintingUtil().reprint(selectedProduct);
		}
		
	}

	private SubProduct getSelectedSubProduct() {
		SubProductDao dao = ServiceFactory.getDao(SubProductDao.class);
		String productId = data.get(table.getSelectedRow());
		return dao.findByKey(productId);
		
	}

	@Override
	protected void updateTable(SkippedProduct event) {
		if(event.getStatus() == SkippedProductStatus.COMPLETED){
			data.remove(event.getId().getProductId().trim());
			refresh();
			return;
		} 

		super.updateTable(event);
	}


	
}
