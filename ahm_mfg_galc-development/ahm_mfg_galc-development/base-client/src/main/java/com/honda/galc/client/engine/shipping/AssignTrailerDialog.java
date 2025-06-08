package com.honda.galc.client.engine.shipping;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.product.ShippingTrailer;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.enumtype.LoginStatus;

/**
 * 
 * 
 * <h3>AssignTrailerDialog Class description</h3>
 * <p> AssignTrailerDialog description </p>
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
 * Sep 23, 2014
 *
 *
 */
public class AssignTrailerDialog extends JDialog implements ActionListener, DocumentListener{
	
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_TRAILER_SIZE= 60;
	
	private LabeledComboBox trailerCombox;
	private LabeledTextField quantityTextField;
	
	private ObjectTablePane<ShippingVanningSchedule> vanningScheduleTablePane;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private List<ShippingVanningSchedule> allSchedules;
	
	private ShippingVanningSchedule splitSchedule = null;
	
	private EngineShippingModel model;
	
	private boolean isOK = false;
	
	public AssignTrailerDialog(EngineShippingController controller,List<ShippingVanningSchedule> schedules) {
		super(controller.getView().getMainWindow(),true);
		this.model = controller.getModel();
		this.allSchedules = schedules;
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setName("assign trailer");
		setSize(427, 400);
		setLocationRelativeTo(controller.getView().getMainWindow());
		
		initComponents();
		mapActions();
		loadData();
	}
	
	public ShippingVanningSchedule getSplitSchedule() {
		return splitSchedule;
	}

	public void setSplitSchedule(ShippingVanningSchedule splitSchedule) {
		this.splitSchedule = splitSchedule;
	}

	private void initComponents() {
		JPanel panel = new JPanel(new MigLayout("insets 30 10 30 10", "[grow,fill]"));
		trailerCombox = new LabeledComboBox("Select Trailer");
		trailerCombox.setTextAlignment(JLabel.CENTER);
		trailerCombox.setFont(Fonts.DIALOG_BOLD_16);
		quantityTextField = new LabeledTextField("Enter Quantity");
		quantityTextField.setNumeric(2);
		quantityTextField.setFont(Fonts.DIALOG_BOLD_16);
		
		panel.add(trailerCombox,"span 2,wrap");
		panel.add(quantityTextField,"span 2,wrap");
		panel.add(vanningScheduleTablePane = createVanningScheduleTablePane(),"span 2,wrap");
		panel.add(okButton = createButton("OK"),"gapleft 50");
		panel.add(cancelButton = createButton("CANCEL"),"gapright 50,wrap");
		setContentPane(panel);
		setTitle("Assign Trailer Dialog");
	}
	
	private void mapActions() {
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		quantityTextField.getComponent().getDocument().addDocumentListener(this); 
	       
		trailerCombox.getComponent().addActionListener(this);
	}

	private void loadData() {
		trailerCombox.setModel(new ComboBoxModel<ShippingTrailer>(model.findAvailableTrailers(),"getTrailerNumber"),0);
	}
	
	private ObjectTablePane<ShippingVanningSchedule> createVanningScheduleTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("KD Lot", "kdLot").put("YMTO","ymto").put("Sch","schQty");
		
		ObjectTablePane<ShippingVanningSchedule> tablePane = new ObjectTablePane<ShippingVanningSchedule>(clumnMappings.get(),false);
		tablePane.setBorder(new TitledBorder("Shipping Vanning Schedule"));
		
		configureTablePane(tablePane);
		return tablePane;
	}
	
	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.setName(label);
		button.setMargin(new Insets(0,0,0,0));
		button.setFont(Fonts.DIALOG_BOLD_12);
		return button;
	}
	
	private void configureTablePane(ObjectTablePane<?> tablePane) {
		tablePane.getTable().setRowHeight(22);
		tablePane.getTable().setFont(Fonts.FONT_BOLD("sansserif",14));
		tablePane.setAlignment(JLabel.CENTER);
		tablePane.getTable().setSelectionBackground(Color.green);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(okButton)) okButtonClicked();
		else if(e.getSource().equals(cancelButton)) cancelButtonClicked();
		else if(e.getSource().equals(trailerCombox.getComponent())) trailerChanged();
	}

	private void okButtonClicked() {
		ShippingVanningSchedule schedule = getSplitSchedule();
		if(schedule != null) {
			if(LoginDialog.login() != LoginStatus.OK) return; 

			if (!ClientMain.getInstance().getAccessControlManager().isAuthorized(model.getPropertyBean().getAuthorizationGroup())) {
				JOptionPane.showMessageDialog(null, "You have no access permission to create trailer with split vanning schedule", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		this.isOK = true;
		this.setVisible(false);
	}

	private void cancelButtonClicked() {
		this.setVisible(false);
	}
	
	private void trailerSizeChanged() {
		String trailerSizeStr = quantityTextField.getComponent().getText();
		if(StringUtils.isEmpty(trailerSizeStr)) return;
		int trailerSize = Integer.valueOf(trailerSizeStr);
		if(trailerSize <= 0) trailerSize = DEFAULT_TRAILER_SIZE;
		vanningScheduleTablePane.reloadData(getVanningSchedule(allSchedules, trailerSize));
	}
	
	private void trailerChanged() {
		ShippingTrailer trailer = (ShippingTrailer)trailerCombox.getComponent().getSelectedItem();
		if(trailer != null){
			quantityTextField.getComponent().setText(Integer.toString(trailer.getTrailerCapacity()));
			trailerSizeChanged();
		}
	}
	
	public List<ShippingVanningSchedule> getSelectedVanningSchedules() {
		return isOK ? vanningScheduleTablePane.getItems() : null;
	}
	
	public String getSelectedTrailerNumber() {
		ShippingTrailer trailer = (ShippingTrailer)trailerCombox.getComponent().getSelectedItem();
		return trailer == null ? "" : trailer.getTrailerNumber();
	}
	
	public List<ShippingVanningSchedule> getVanningSchedule(List<ShippingVanningSchedule> allSchedules , int trailerSize) {
		
		List<ShippingVanningSchedule> schedules = new ArrayList<ShippingVanningSchedule>();
		
		splitSchedule = null;
		int size = 0;
		ShippingVanningSchedule firstSchedule = null;
		
		List<ShippingVanningSchedule> availableVanningSchedules = model.findNotAssignedVanningSchedules(allSchedules); 
		
		for(int i = 0; i < availableVanningSchedules.size();i++) {
			ShippingVanningSchedule schedule = availableVanningSchedules.get(i);
			if(firstSchedule == null) firstSchedule = schedule;
			if(!schedule.getPlantCode().equalsIgnoreCase(firstSchedule.getPlantCode())) continue;
			ShippingVanningSchedule newSchedule = schedule;
			size += schedule.getSchQty();
			if(size > trailerSize) {
				newSchedule = schedule.clone();
				newSchedule.setSchQty(schedule.getSchQty()-size + trailerSize);
				splitSchedule = schedule.clone();
				splitSchedule.setSchQty(schedule.getSchQty() - newSchedule.getSchQty());
				ShippingVanningSchedule nextSchedule =
					i < availableVanningSchedules.size() -1 ? availableVanningSchedules.get(i+1) : null;
				int vanningSeq = calculateVanningSeq(newSchedule, nextSchedule);
				splitSchedule.getId().setVanningSeq(vanningSeq);
			}
			schedules.add(newSchedule);
			if(size >= trailerSize) break;
		}
		return schedules;
	}
	
	private int calculateVanningSeq(ShippingVanningSchedule schedule,ShippingVanningSchedule nextSchedule){
		if(nextSchedule == null || 
		   nextSchedule.getId().getProductionDate().after(schedule.getId().getProductionDate())){ 
				return	schedule.getId().getVanningSeq() + ShippingVanningSchedule.VANNING_SEQ_INTERVAL;
		}else {
			return (schedule.getId().getVanningSeq() + nextSchedule.getId().getVanningSeq()) / 2;
		}
	}

	public void changedUpdate(DocumentEvent e) {
		trailerSizeChanged();
	}

	public void insertUpdate(DocumentEvent e) {
		trailerSizeChanged();
	}

	public void removeUpdate(DocumentEvent e) {
		trailerSizeChanged();
	}
	
	
	
}
