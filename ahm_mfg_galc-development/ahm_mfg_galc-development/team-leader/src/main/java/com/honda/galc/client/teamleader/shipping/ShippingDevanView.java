package com.honda.galc.client.teamleader.shipping;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.ShippingTrailerDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.dao.product.ShippingTrailerRackDao;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.entity.enumtype.ShippingTrailerInfoStatus;
import com.honda.galc.entity.enumtype.TrailerStatus;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingTrailerRack;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * 
 * <h3>ShippingDevanView Class description</h3>
 * <p> ShippingDevanView description </p>
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
 * @author Jeffray Huang<br>
 * Jan 27, 2015
 *
 *
 */
public class ShippingDevanView extends ApplicationMainPanel implements ActionListener, ListSelectionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ObjectTablePane<MultiValueObject<ShippingTrailerInfo>> trailerPane;
	private List<LabeledTextField> textFields = new ArrayList<LabeledTextField>();
	private JButton refreshButton = new JButton("Refresh");
	private JButton devanButton = new JButton("Devan");
	
	public ShippingDevanView(DefaultWindow window) {
		super(window);
		initComponents();
		loadData();
		addListeners();
	}
	
	private void initComponents(){
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		add(Box.createHorizontalStrut(200));
		JPanel mainPanel = new JPanel();
		add(mainPanel);
		add(Box.createHorizontalStrut(200));
		
		JLabel label = new JLabel("Select Trailer Number to be Devanned:");
		label.setFont(new Font("sansserif", 1, 18));
		
		mainPanel.setLayout(new MigLayout("insets 20", "[grow,fill]"));
		mainPanel.add(label,"wrap");
		mainPanel.add(trailerPane = createTrailerPane(),"wrap");
		mainPanel.add(createShippingRackFields(),"gapleft 150,gapright 150,wrap");
		mainPanel.add(createButtonPanel());
	}
	
	private void loadData(){
		List<ShippingTrailerInfo> trailers = 
			ServiceFactory.getDao(ShippingTrailerInfoDao.class).findAllCompleteTrailers();
		List<ShippingVanningSchedule> vanningSchedules = 
			ServiceFactory.getDao(ShippingVanningScheduleDao.class).findAllCompleteVanningSchedules();
		
		List<MultiValueObject<ShippingTrailerInfo>> trailerList = new ArrayList<MultiValueObject<ShippingTrailerInfo>>();
		
		for(ShippingTrailerInfo trailerInfo :trailers) {
			List<ShippingVanningSchedule> schedules = findVanningSchedules(trailerInfo.getTrailerId(), vanningSchedules);
			if(schedules.isEmpty()) {
				getLogger().error("");
			}else {
				List<String> values = new ArrayList<String>();
				values.add(trailerInfo.getTrailerNumber());
				values.add(schedules.get(0).getKdLot());
				values.add(schedules.get(schedules.size() -1).getKdLot());
				MultiValueObject<ShippingTrailerInfo> valueObject = new MultiValueObject<ShippingTrailerInfo>(trailerInfo,values);
				valueObject.setKeyObject(trailerInfo);
				trailerList.add(valueObject);
			}
		}
		trailerPane.clearSelection();
		trailerPane.reloadData(trailerList);
		trailerPane.getTable().getSelectionModel().setSelectionInterval(0, 0);
	}
	
	private List<ShippingVanningSchedule> findVanningSchedules(int trailerId, List<ShippingVanningSchedule> allSchedules) {
		List<ShippingVanningSchedule> schedules = new ArrayList<ShippingVanningSchedule>();
		for(ShippingVanningSchedule schedule : allSchedules){
			if(trailerId == schedule.getTrailerId()) schedules.add(schedule);
		}
		return schedules;
	}
	
	private void addListeners() {
		trailerPane.addListSelectionListener(this);
		refreshButton.addActionListener(this);
		devanButton.addActionListener(this);
	}
	
	private void refresh(){
		loadData();
	}
	
	private void devan() {
		MultiValueObject<ShippingTrailerInfo> selectedTrailer = trailerPane.getSelectedItem();
		if(selectedTrailer == null) return;
		ShippingTrailerInfo trailerInfo = selectedTrailer.getKeyObject();
		if(!MessageDialog.confirm(this, "Are You Sure to Devan Trailer " + trailerInfo.getTrailerNumber() + " ? ")) return;
		
		trailerInfo.setStatus(ShippingTrailerInfoStatus.DEVANNED);
		List<ShippingTrailerRack> racks = getTrailerRackList(trailerInfo.getTrailerNumber());
		ServiceFactory.getDao(ShippingTrailerRackDao.class).saveAll(racks);
		ServiceFactory.getDao(ShippingTrailerInfoDao.class).update(trailerInfo);
		ServiceFactory.getDao(ShippingTrailerDao.class).updateStatus(trailerInfo.getTrailerNumber(), TrailerStatus.AVAILABLE);
		String message = "Devanned Trailer " + trailerInfo + " Successfully";
		getLogger().info(message);
		setMessage(message);
		loadData();
	}
	
	private ObjectTablePane<MultiValueObject<ShippingTrailerInfo>> createTrailerPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Trailer Number").put("KD Min").put("KD Max");
	
		ObjectTablePane<MultiValueObject<ShippingTrailerInfo>> pane = new ObjectTablePane<MultiValueObject<ShippingTrailerInfo>>(clumnMappings.get(),false);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		return pane;
	}

	public void actionPerformed(ActionEvent e) {
		getMainWindow().clearMessage();
		try{
			if(e.getSource().equals(refreshButton)) refresh();
			else if(e.getSource().equals(devanButton)) devan();
		}catch(Exception ex) {
			handleException(ex);
		}
		
	}
	
	private JPanel createButtonPanel() {
		refreshButton.setFont(new Font("sansserif", 1, 18));
		devanButton.setFont(new Font("sansserif", 1, 18));
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(200));
		panel.add(refreshButton);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(devanButton);
		panel.add(Box.createHorizontalStrut(200));
		return panel;
	}
	
	private JPanel createShippingRackFields() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		for(ShippingTrailerRack.RackType rackType: ShippingTrailerRack.RackType.values()) {
			LabeledTextField textField = new LabeledTextField(rackType.toString());
			textField.setNumeric(2);
			textField.setLabelPreferredWidth(100);
			textField.getComponent().setHorizontalAlignment(JLabel.CENTER);
			textField.setFont(new Font("sansserif", 1, 18));
			textFields.add(textField);
			panel.add(textField);
		}
		return panel;
	}
	
	private List<ShippingTrailerRack> getTrailerRackList(String trailerNumber) {
		List<ShippingTrailerRack> rackList = new ArrayList<ShippingTrailerRack>();
		for(LabeledTextField rackTextField : textFields) {
			ShippingTrailerRack.RackType rackType = ShippingTrailerRack.RackType.getType(rackTextField.getLabel().getText());
			if(rackType == null) continue;
			ShippingTrailerRack rack = new ShippingTrailerRack(trailerNumber,rackType);
			rack.setDescription(rackType.getDescription());
			rack.setQuantity(new Integer(rackTextField.getComponent().getText()));
			rackList.add(rack);
		}
		return rackList;
	}
	
	private String getRackQuantity(String rackType,List<ShippingTrailerRack> racks) {
		for(ShippingTrailerRack rack :racks) {
			if(rack.getRackType().equals(rackType)) return Integer.toString(rack.getQuantity());
		}
		return "0";
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		MultiValueObject<ShippingTrailerInfo> selectedTrailer = trailerPane.getSelectedItem();
		if(selectedTrailer == null) return;
		
		List<ShippingTrailerRack> racks = ServiceFactory.getDao(ShippingTrailerRackDao.class).
			findAllByTrailerNumber(selectedTrailer.getKeyObject().getTrailerNumber());
		for(LabeledTextField rackTextField :textFields) {
			rackTextField.getComponent().setText(getRackQuantity(rackTextField.getLabel().getText(), racks));
		}
		
	}
	
}
