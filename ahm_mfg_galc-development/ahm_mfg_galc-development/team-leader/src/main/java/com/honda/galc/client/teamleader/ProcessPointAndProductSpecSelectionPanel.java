package com.honda.galc.client.teamleader;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ProcessPointSelectiontPanel;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.MissionSpecDao;
import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ProcessPointAndProductSpecSelectionPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProcessPointAndProductSpecSelectionPanel description </p>
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
 * <TD>P.Chou</TD>
 * <TD>May 13, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 13, 2011
 */

public class ProcessPointAndProductSpecSelectionPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JPanel productSpecBox;
	private JButton specButton;
	private JComboBox specComBoBox;
	private String productType;
	private ProcessPointSelectiontPanel processPointSelectionPanel;
	
	public ProcessPointAndProductSpecSelectionPanel(String productType) {
		super();
		this.productType = productType;
		init();
	}

	private void init() {
		initComponent();
		specComBoBox.addActionListener(this);
		specButton.addActionListener(this);
		
	}


	private void initComponent() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(getProcessPointSelectionPanel());
		add(getProductSpecBox());
	}


	public void actionPerformed(ActionEvent e) {

		if(e.getSource().equals(specComBoBox)) productSpecChanged();
		else if(e.getSource().equals(specButton)) loadProductSpecCode();

	}


	private void loadProductSpecCode() {
		String prefix = JOptionPane.showInputDialog(this, "Product Spec Code prefix:");
		if(StringUtils.isEmpty(prefix)) return;
		prefix = prefix.toUpperCase();
		List<? extends BaseProductSpec> productSpecs = loadProductSpecCode(prefix);
		ComboBoxModel<Object> model = new ComboBoxModel<Object>(getProductSpecStringList(productSpecs));
		specComBoBox.setModel(model);
		specComBoBox.setRenderer(model);
		specComBoBox.setSelectedIndex(-1);
	}
	
	
	private List<? extends BaseProductSpec> loadProductSpecCode(String  prefix) {
		String selectedProductType = getSelectedProductType();
		ProductType prodType = ProductType.getType(selectedProductType);
		List<? extends BaseProductSpec> list;
		switch(ProductType.getType(selectedProductType)) {
		case FRAME :
		case KNUCKLE :
			list = ServiceFactory.getDao(FrameSpecDao.class).findAllByPrefix(prefix);
			break;
		case ENGINE :
			list = ServiceFactory.getDao(EngineSpecDao.class).findAllByPrefix(prefix);
			break;
		case MISSION :
			list = ServiceFactory.getDao(MissionSpecDao.class).findAllByPrefix(prefix);		
			break;
		case MBPN:
			list =  ServiceFactory.getDao(MbpnDao.class).findAllByPrefix(prefix);
			break;	
		default :
			list = ServiceFactory.getDao(ProductSpecCodeDao.class).findAllByProductTypeAndPrefix(selectedProductType, prefix);
		}
		return list;
	}

	public String getSelectedProductType() {
		return processPointSelectionPanel.selectedProductType();
	}

	private List<Object> getProductSpecStringList(List<? extends BaseProductSpec> productSpecs) {
		Set<String> set = new HashSet<String>();
		for(BaseProductSpec spec : productSpecs){
			set.add(spec.getProductSpecCode());
		}
		Object[] array = set.toArray();
		Arrays.sort(array);
		return Arrays.asList(array);
	}

	private void productSpecChanged() {
		EventBus.publish(new ProductSpecSelectionEvent(this,SelectionEvent.SELECTED));
		
	}

	public JPanel getProductSpecBox() {
		if(productSpecBox == null){
			productSpecBox = new JPanel();
			specButton = new JButton("Product Spec Code");
			specButton.setFont(Fonts.DIALOG_BOLD_14);
			specComBoBox = new JComboBox();
			specComBoBox.setPreferredSize(new Dimension(200,26));
			specComBoBox.setFont(Fonts.DIALOG_BOLD_14);
			
			productSpecBox.setLayout(new FlowLayout());
			productSpecBox.add(specButton);
			productSpecBox.add(Box.createHorizontalStrut(10));
			productSpecBox.add(specComBoBox);
			productSpecBox.add(Box.createHorizontalStrut(200));
			
		}
		return productSpecBox;
	}

	public JComboBox getSpecComBoBox() {
		return specComBoBox;
	}

	public ProcessPointSelectiontPanel getProcessPointSelectionPanel() {
		if(processPointSelectionPanel == null){
			String siteName = PropertyService.getSiteName();
			if(StringUtils.isEmpty(siteName)){
				MessageDialog.showError(this, "SITE_NAME is not defined in the System_Info property");
			}
			processPointSelectionPanel = new LotControlProcessPoinSelectiontPanel(siteName);
		}
		
		return processPointSelectionPanel;
	}
	
}
