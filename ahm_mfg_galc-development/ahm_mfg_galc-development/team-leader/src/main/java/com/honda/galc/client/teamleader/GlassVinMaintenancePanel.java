package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.util.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.glassload.GlassLoadPropertyBean;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

public class GlassVinMaintenancePanel extends ApplicationMainPanel implements ActionListener, ListSelectionListener{
	
	private static String SQL = "SELECT * FROM GTS_CARRIER_TBX WHERE TRACKING_AREA = ?1 order by STATUS ASC";

	List<GtsCarrier> carriers = new ArrayList<GtsCarrier>();
	
	private ObjectTablePane<MultiValueObject<Frame>> vinTablePane;
		
	private JButton assignButton= new JButton("Assign");
	private JButton deassignButton= new JButton("Deassign");

	
	public GlassVinMaintenancePanel(MainWindow window)  {
		super(window);
		initComponents();
		
	}
	
	protected void initComponents() {
		setLayout(new BorderLayout());
		add(createVinPanel(),BorderLayout.CENTER);
		
	}
	
	protected void loadVinData() {
		carriers = fetchGlassCarriers();
		List<MultiValueObject<Frame>> list = new ArrayList<MultiValueObject<Frame>>();
		
		List<Frame> frames = fetchAFonVinList();
		for(Frame frame : frames) {
			GtsCarrier carrier = findCarrier(carriers, frame.getProductId());
			List<Object> values = prepareVinValues(frame, carrier == null ? "" : carrier.getCarrierNumber() );
			MultiValueObject<Frame> valueObject = new MultiValueObject<Frame>(frame, values);
			list.add(valueObject);
		}		
		
		vinTablePane.reloadData(list);
		
		if(vinTablePane.getTable().getSelectedRow() < 0)
			vinTablePane.getTable().getSelectionModel().setSelectionInterval(0, 0);

	}
	
	private GtsCarrier findCarrier(List<GtsCarrier> carriers, String productId) {
		for(GtsCarrier carrier: carriers) {
			if(productId.equals(carrier.getProductId())) return carrier;
		}
		return null;
	}
	
	private GtsCarrier findCarrier(String carrierId) {
		for(GtsCarrier carrier: carriers) {
			if(carrier.getCarrierNumber().equalsIgnoreCase(carrierId)) return carrier;
		}
		return null;
	}
		
	private JPanel createVinPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(vinTablePane = createVinTablePane(),BorderLayout.CENTER);
		panel.add(createButtonPanel(),BorderLayout.SOUTH);
		return panel;
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 30, 10));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.add(Box.createHorizontalStrut(100));
		buttonPanel.add(assignButton);
		buttonPanel.add(deassignButton);
		buttonPanel.add(Box.createHorizontalStrut(100));
		
		assignButton.addActionListener(this);
		deassignButton.addActionListener(this);
		return buttonPanel;

	}
	
	private ObjectTablePane<MultiValueObject<Frame>> createVinTablePane()  {
		ColumnMappings columnMappings = ColumnMappings.with(new String[] {"VIN", "Ref #", "Carrier #","Production Lot","MTOC", "Process Point"});
		ObjectTablePane<MultiValueObject<Frame>> pane = new ObjectTablePane<MultiValueObject<Frame>>(
				"",columnMappings.get());
		
		pane.getTable().getTableHeader().setFont(Fonts.DIALOG_BOLD(getPropertyBean().getHeaderFontSize()));
		pane.getTable().setFont(new Font("sansserif", 1, getPropertyBean().getFontSize()));
		pane.getTable().setRowHeight(getPropertyBean().getItemHeight());
		pane.setAlignment(SwingConstants.CENTER);
		
		pane.getTable().getSelectionModel().addListSelectionListener(this);
				
		return pane;
	}
	
	public List<GtsCarrier> fetchGlassCarriers() {
		
		List<GtsCarrier> carriers = getService(GenericDaoService.class).findAll(SQL, Parameters.with("1",  "GLASS_LOAD"), GtsCarrier.class);
		
		return carriers;
	}
	
	public Frame fetchFrame(String productId) {
		if(StringUtils.isEmpty(productId))  return null;
		else {
			return getDao(FrameDao.class).findByKey(productId);
		}
	}
		
	private List<Object> prepareVinValues(Frame frame,String carrier) {
		List<Object> values = new ArrayList<Object>();
		values.add(frame.getProductId());
		values.add(frame.getAfOnSequenceNumber());
		values.add(carrier);
		values.add(frame.getProductionLot());
		values.add(frame.getProductSpecCode());
		values.add(frame.getLastPassingProcessPointId());
		return values;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(assignButton)) assignCarrier();
		else if(e.getSource().equals(deassignButton)) deassignCarrier();			
	}
	
	private void assignCarrier() {
		int selectionIndex = this.vinTablePane.getTable().getSelectedRow();

		MultiValueObject<Frame> item = vinTablePane.getSelectedItem();
		String carrierId = MessageDialog.showInputDialog(this, "Assign Carrier", "Input Carrier Number \nfor VIN " + item.getKeyObject().getProductId(), 10, true);
		if(carrierId == null) return;
		
		GtsCarrier carrier = findCarrier(carrierId);
		
		if(carrier == null) {
			MessageDialog.showError(this, "Invalid Carrier Number " + carrierId);
			return;
		}
		
		if(!StringUtils.isEmpty(carrier.getProductId()))  {
			MessageDialog.showError(this, "Carrier # " + carrierId + " is assigned to VIN " + carrier.getProductId() + "\nPlease choose another carrier");
			return;
		}
		
		carrier.setProductId(item.getKeyObject().getProductId());
		
		updateCarrier(carrier);
		
	    getMainWindow().getLogger().info("VIN " + item.getKeyObject().getProductId() + " is assigned to carrier number " + carrier.getCarrierNumber());
	    
	    loadVinData();
	    vinTablePane.clearSelection();
		vinTablePane.getTable().getSelectionModel().setSelectionInterval(selectionIndex, selectionIndex);

	}
	
	private void deassignCarrier() {
		
		int selectionIndex = this.vinTablePane.getTable().getSelectedRow();

		MultiValueObject<Frame> item = vinTablePane.getSelectedItem();
		
		GtsCarrier carrier = findCarrier(carriers,item.getKeyObject().getProductId());
		
		if(carrier == null) return;
		
		boolean flag = MessageDialog.confirm(this, "Are you sure to deassign VIN " + carrier.getProductId() + " From Carrier " + carrier.getCarrierNumber() + " ? ");
		
		if (!flag) return ;
		
		carrier.setProductId(null);
		updateCarrier(carrier);
		
	    getMainWindow().getLogger().info("VIN " + item.getKeyObject().getProductId() + " is de-assigned from carrier number " + carrier.getCarrierNumber());

	    loadVinData();

	    vinTablePane.clearSelection();

		vinTablePane.getTable().getSelectionModel().setSelectionInterval(selectionIndex, selectionIndex);

	}
	
	public void updateCarrier(GtsCarrier carrier) {
		getDao(GtsCarrierDao.class).save(carrier);
	}

	public List<Frame> fetchAFonVinList() {
		List<Frame> frames = getDao(FrameDao.class).findByTrackingStatus(getPropertyBean().getAfOnTrackingStatus());
		
		List<Frame> filteredFrames = new ArrayList<Frame>();
		
		for(Frame frame : frames) {
			if(frame.getAfOnSequenceNumber() != null) filteredFrames.add(frame);
		}
		
		List<Frame> items = null ;
		try {
			items =  new SortedArrayList<Frame>(filteredFrames, "getAfOnSequenceNumber");
			
		}catch(Throwable ex) {
			ex.printStackTrace();
		}
		
		return items;
	}
	
	public GlassLoadPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(GlassLoadPropertyBean.class, getApplicationId());
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		
		Frame frame = vinTablePane.getSelectedItem().getKeyObject();
		
		GtsCarrier carrier = findCarrier(carriers,frame.getProductId());
		
		if(carrier == null) {
			assignButton.setEnabled(true);
			deassignButton.setEnabled(false);
		} else if(!StringUtils.isEmpty(carrier.getProductId())) {
			assignButton.setEnabled(false);
			deassignButton.setEnabled(true);	
		}
	}


}
