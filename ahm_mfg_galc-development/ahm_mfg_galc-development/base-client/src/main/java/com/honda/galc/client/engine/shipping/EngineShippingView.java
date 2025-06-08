package com.honda.galc.client.engine.shipping;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.ColorTableCellRenderer;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;

/**
 * 
 * 
 * <h3>MCShippingView Class description</h3>
 * <p> MCShippingView description </p>
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
 * Sep 10, 2014
 *
 *
 */
public class EngineShippingView extends AbstractView<EngineShippingModel, EngineShippingController>{
	
	private static final long serialVersionUID = 1L;
	
	protected ObjectTablePane<ShippingTrailerInfo> trailerListPane;
	protected ObjectTablePane<ShippingQuorum> quorumListPane;
	protected ObjectTablePane<ShippingQuorum> delayedQuorumListPane;
	protected ObjectTablePane<ShippingQuorumDetail> quorumDetailListPane;
	protected ObjectTablePane<ShippingQuorumDetail> delayedQuorumDetailListPane;
	protected ObjectTablePane<ShippingVanningSchedule> vanningScheduleListPane;
	
	protected JButton assignButton;
	protected JButton deassignButton;
	protected JButton changeTrailerButton;
	protected JButton manualLoadButton;
	protected JButton completeTrailerButton;
	protected JButton printButton;
	protected JButton refreshScheduleButton;
	
	protected JButton loadQuorumButton;
	protected JButton resizeQuorumButton;
	protected JButton repairQuorumButton;
	protected JButton createQuorumButton;
	protected JButton completeQuorumButton;
	protected JButton toDelayButton;
	protected JButton fromDelayBUtton;
	
	public EngineShippingView(DefaultWindow mainWindow) {
		super(mainWindow);
	}
	
	public void prepare() {
		initComponents();
		super.prepare();
	}
	
	public void initComponents() {
		
		setLayout(new GridLayout(1,2));
		createButtons();
		add(createLeftPanel());
		add(createRightPanel());
		setButtonStates();
	}
	
	protected void mapActions() {
		trailerListPane.addListSelectionListener(controller);
		quorumListPane.addListSelectionListener(controller);
		delayedQuorumListPane.addListSelectionListener(controller);
		
		assignButton.addActionListener(controller);
		deassignButton.addActionListener(controller);
		changeTrailerButton.addActionListener(controller);
		manualLoadButton.addActionListener(controller);
		completeTrailerButton.addActionListener(controller);
		printButton.addActionListener(controller);
		loadQuorumButton.addActionListener(controller);
		resizeQuorumButton.addActionListener(controller);
		repairQuorumButton.addActionListener(controller);
		createQuorumButton.addActionListener(controller);
		completeQuorumButton.addActionListener(controller);
		
		toDelayButton.addActionListener(controller);
		fromDelayBUtton.addActionListener(controller);
		
		refreshScheduleButton.addActionListener(controller);
	}
	
	public void reload() {
		model.reloadShippingTrailerInfoList();
		model.reloadActiveQuorums();
		int trailerIndex = Math.max(trailerListPane.getTable().getSelectedRow(),0);
		int scheduleQuorumIndex = Math.max(quorumListPane.getTable().getSelectedRow(),0);
		int delayedQuorumIndex = Math.max(delayedQuorumListPane.getTable().getSelectedRow(),0);
		
		trailerListPane.clearSelection();
		quorumListPane.clearSelection();
		delayedQuorumListPane.clearSelection();
		
		trailerListPane.reloadData(model.getAllShippingTrailers());
		vanningScheduleListPane.reloadData(model.findAllActiveVanningSchedules());
		assignUpcomingLotColors();
		quorumListPane.reloadData(model.findAllScheduledQuorums());
		delayedQuorumListPane.reloadData(model.findAllDelayedQuorums());
		
		trailerListPane.getTable().getSelectionModel().setSelectionInterval(trailerIndex, trailerIndex);
		quorumListPane.getTable().getSelectionModel().setSelectionInterval(scheduleQuorumIndex, scheduleQuorumIndex);
		delayedQuorumListPane.getTable().getSelectionModel().setSelectionInterval(delayedQuorumIndex, delayedQuorumIndex);
	}
	
	public void start() {
		
	}
	
	private JPanel createLeftPanel() {
		JPanel panel = new JPanel(new MigLayout("insets 1", "[grow,fill]"));
		panel.add(createTrailerPanel(),"span 2,wrap");
		panel.add(createScheduledQuorumListPanel(),"span 2,wrap");
		panel.add(toDelayButton,"gapleft 150");
		panel.add(fromDelayBUtton,"gapright 150,wrap");
		delayedQuorumListPane = createQuorumTablePane("Delayed Quorum List");
		panel.add(delayedQuorumListPane,"height 200:200:200,span 2");
		return panel;
	}
	
	private JPanel createRightPanel() {
		JPanel panel = new JPanel(new MigLayout("insets 1", "[grow,fill]"));
		quorumDetailListPane = createQuorumDetailTablePane("Scheduled Quorum Detailed Info");
		delayedQuorumDetailListPane = createQuorumDetailTablePane("Delayed Quorum Detailed Info");
		panel.add(createVanningScheduleListPanel(),"height 280:280:280,wrap");
		panel.add(quorumDetailListPane,"wrap");
		panel.add(delayedQuorumDetailListPane);
		return panel;
	}
	
	private JPanel createTrailerPanel() {
		JPanel panel = new JPanel(new MigLayout("insets 1", "[grow,fill]"));
		panel.setBorder(new TitledBorder("Trailer Info"));
		trailerListPane = createTrailerTablePane();
		panel.add(assignButton);
		panel.add(deassignButton);
		panel.add(changeTrailerButton);
		panel.add(manualLoadButton);
		panel.add(completeTrailerButton);
		panel.add(printButton,"wrap");
		panel.add(trailerListPane,"span");
		ViewUtil.setPreferredHeight(panel, 400);
		return panel;
	}
	
	private JPanel createScheduledQuorumListPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder("Scheduled Quorum List"));
		quorumListPane = createQuorumTablePane(null);
		
		JPanel buttonPanel = new JPanel(new GridLayout(5,1,2,2));
		buttonPanel.add(loadQuorumButton);
		buttonPanel.add(resizeQuorumButton);
		buttonPanel.add(repairQuorumButton);
		buttonPanel.add(createQuorumButton);
		buttonPanel.add(completeQuorumButton);
		
		panel.add(quorumListPane,BorderLayout.CENTER);
		panel.add(buttonPanel,BorderLayout.EAST);
		
		return panel;
	}
	
	private JPanel createVanningScheduleListPanel() {
		JPanel panel = new JPanel(new MigLayout("insets 1", "[grow,fill]"));
		vanningScheduleListPane = createVanningScheduleTablePane();
		panel.add(vanningScheduleListPane,"wrap");
		panel.add(refreshScheduleButton,"gapleft 150,gapright 150");
		return panel;
	}
	
	private ObjectTablePane<ShippingTrailerInfo> createTrailerTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Trailer Id", "trailerId")
			.put("Trailer #", "trailerNumber").put("Sch Qty","schQty").put("Act Qty","actQty")
			.put("Status","status");
		
		ObjectTablePane<ShippingTrailerInfo> tablePane = new ObjectTablePane<ShippingTrailerInfo>(clumnMappings.get(),false);
		
		configureTablePane(tablePane);
		return tablePane;
	}
	
	private ObjectTablePane<ShippingQuorum> createQuorumTablePane(String title) {
		ColumnMappings clumnMappings = ColumnMappings.with("Trailer Id", "trailerId")
			.put("Trailer #","trailerNumber").put("Row", "trailerRow").put("Size","quorumSize")
			.put("Status","status");
		
		ObjectTablePane<ShippingQuorum> tablePane = new ObjectTablePane<ShippingQuorum>(clumnMappings.get(),false);
		
		if(title != null) tablePane.setBorder(new TitledBorder(title));
		
		configureTablePane(tablePane);
		return tablePane;
	}
	
	private ObjectTablePane<ShippingQuorumDetail> createQuorumDetailTablePane(String title) {
		ColumnMappings clumnMappings = ColumnMappings.with("Seq", "quorumSeq")
			.put("KD Lot Number", "kdLot").put("YMTO","ymto").put("Engine Number","engineNumber");
		
		ObjectTablePane<ShippingQuorumDetail> tablePane = new ObjectTablePane<ShippingQuorumDetail>(clumnMappings.get(),false);
		
		tablePane.setBorder(new TitledBorder(title));
		configureTablePane(tablePane);
		return tablePane;
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
	
	private ObjectTablePane<ShippingVanningSchedule> createVanningScheduleTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Plant", "plantCode")
			.put("KD Lot", "kdLot").put("YMTO","ymto").put("Sch","schQty")
			.put("Act","actQty").put("Trailer #","trailerNumber");
		
		ObjectTablePane<ShippingVanningSchedule> tablePane = new ObjectTablePane<ShippingVanningSchedule>(clumnMappings.get(),false);
		tablePane.setBorder(new TitledBorder("Shipping Vanning Schedule"));
		
		configureTablePane(tablePane);
		tablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return tablePane;
	}
	
	private void assignUpcomingLotColors() {
		JTable table =  vanningScheduleListPane.getTable();
		ColorTableCellRenderer renderer = new ColorTableCellRenderer(new DefaultTableCellRenderer(),
				table.getRowCount(),table.getColumnCount());
		
		for(int row = 0;row<table.getRowCount();row++){
			ShippingVanningSchedule schedule = vanningScheduleListPane.getItems().get(row);
			Color color = model.getPropertyBean().getColors(Color.class).get(schedule.getPlant());
			renderer.setColor(color, row, 0); // Plant column
		}
		
		table.getColumnModel().getColumn(0).setCellRenderer(renderer);	
	}

	
	private void createButtons() {
		assignButton =createButton("Assign",Fonts.DIALOG_BOLD_12);
		deassignButton =createButton("Deassign",Fonts.DIALOG_BOLD_12);
		changeTrailerButton =createButton("Change Trailer",Fonts.DIALOG_BOLD_12);
		manualLoadButton=createButton("Manual Load",Fonts.DIALOG_BOLD_12);
		completeTrailerButton=createButton("Complete",Fonts.DIALOG_BOLD_12);
		printButton=createButton("Print",Fonts.DIALOG_BOLD_12);
		refreshScheduleButton=createButton("Refresh Schedule",Fonts.DIALOG_BOLD_12);
		
		loadQuorumButton=createButton("Load",Fonts.DIALOG_BOLD_12);
		resizeQuorumButton=createButton("Resize",Fonts.DIALOG_BOLD_12);
		repairQuorumButton=createButton("Repair",Fonts.DIALOG_BOLD_12);
		createQuorumButton=createButton("Create",Fonts.DIALOG_BOLD_12);
		completeQuorumButton=createButton("Complete",Fonts.DIALOG_BOLD_12);
		completeQuorumButton.setName("complete Quorum");
		toDelayButton=createButton("To Delay",Fonts.DIALOG_BOLD_12);;
		fromDelayBUtton=createButton("From Delay",Fonts.DIALOG_BOLD_12);;
	}
	
	public void setButtonStates() {
		ShippingQuorum quorum = quorumListPane.getSelectedItem();
		if(quorum == null) return;
		boolean isFirstQuorumSelected = quorumListPane.getTable().getSelectedRow() == 0;
		boolean isQuorumSelected = quorum != null;
		boolean isDelayedQuorumSelected = quorumListPane.getTable().getSelectedRow() >= 0;
			
		loadQuorumButton.setEnabled(isFirstQuorumSelected && ShippingQuorumStatus.ALLOCATED.equals(quorum.getStatus()));
		resizeQuorumButton.setEnabled(isQuorumSelected && !ShippingQuorumStatus.LOADING.equals(quorum.getStatus()));
		completeQuorumButton.setEnabled(isFirstQuorumSelected);
		toDelayButton.setEnabled(isQuorumSelected && !ShippingQuorumStatus.LOADING.equals(quorum.getStatus()));
		fromDelayBUtton.setEnabled(isDelayedQuorumSelected && isQuorumSelected);
	}

}
