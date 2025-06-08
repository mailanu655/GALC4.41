package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PartLotDao;
import com.honda.galc.entity.enumtype.PartLotStatus;
import com.honda.galc.entity.product.PartLot;
/**
 * 
 * <h3>PartLotPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartLotPanel description </p>
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
 * Dec 7, 2010
 *
 */
public class PartLotPanel extends TabbedPanel implements ListSelectionListener, ActionListener{
	private static final long serialVersionUID = 1L;
	private final static String SHOW_ALL = "Show All";
	private TablePane partLotTablePane;
	private int midPanelHeight = 500;
	private int startX = -10;
	private int startY = 0;
	private PartLotTableModel partLotTableModel;
	private static String SAVE_PART_LOT = "SAVE PART LOT";
	private Color defaultSelectBackground;
	private JButton refreshButton;
	private JButton showAll;
	private boolean showAllPartLots = false;
	
	
	public PartLotPanel() {
		super("Part Lot", KeyEvent.VK_O);
	}

	@Override
	public void onTabSelected() {
		if(isInitialized) return;
		
		initComponents();
		addListeners();
		isInitialized = true;
		defaultSelectBackground = partLotTablePane.getTable().getSelectionBackground();
		
		showPartLot();
	}

	private void addListeners() {
		partLotTablePane.getTable().addMouseListener(createPartLotListener());
		refreshButton.addActionListener(this);
		showAll.addActionListener(this);
		
	}


	private MouseListener createPartLotListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showEditPartLotPopupMenu(e);
			}
		 }); 
	}


	protected void showEditPartLotPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		partLotTableModel = (PartLotTableModel)partLotTablePane.getTable().getModel();
		popupMenu.add(createMenuItem(SAVE_PART_LOT, isSelected() && isChangedRow()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private boolean isChangedRow() {
		
		return partLotTableModel.isSelectedChangedItem();
	}

	private boolean isSelected() {
		return partLotTablePane.getTable().getSelectedRow() >= 0;
	}

	private void initComponents() {
		setLayout(null);

		add(createPartLotTablePane());
		add(createRefreshButton());
		add(createShowAllButton());
		
	}


	private JButton createRefreshButton() {
		if(refreshButton == null){
			refreshButton = new JButton("Refresh");
			refreshButton.setFont(new Font("Dialog", Font.PLAIN, 18));
			refreshButton.setBounds(380, midPanelHeight +40, 120, 30);
		}
		return refreshButton;
	}
	
	
	private JButton createShowAllButton() {
		if(showAll == null){
			showAll = new JButton(SHOW_ALL);
			showAll.setFont(new Font("Dialog", Font.PLAIN, 18));
			showAll.setBounds(550, midPanelHeight +40, 150, 30);
		}
		return showAll;
	}
	

	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void actionPerformed(ActionEvent e) {

		if(e.getSource() instanceof JMenuItem) {
			try{
				JMenuItem menuItem = (JMenuItem)e.getSource();
				if(menuItem.getName().equals(SAVE_PART_LOT)) savePartLot();
			}catch(Exception ex) {
				handleException(ex);
			}
		} else if(e.getSource() == refreshButton){
			partLotTableModel.clearChanged();
			showPartLot();
		} else if(e.getSource() == showAll){
			showAll();
		}
		
	}

	public void showAll(){
		if(showAll.getText().equals(SHOW_ALL)){
			setShowAllPartLots(true);
			showAll.setText("Show Active");
			showPartLot();
		} else {
			setShowAllPartLots(false);
			showAll.setText(SHOW_ALL);
			showPartLot();
		}
	}
	
	
	private void savePartLot() {
		PartLot selectedPartLot = partLotTableModel.getSelectedItem();
		PartLot changedPartLot = partLotTableModel.getOriginalPartLot(selectedPartLot.getId());
		
		if(changedPartLot == null) return;
		
		//must add a comment to change part lot
		if(StringUtils.isEmpty(selectedPartLot.getComment())){
			showErrorMsg("Missing comment for part lot");
			return;
		} else if(changedPartLot.getComment() != null && 
				selectedPartLot.getComment().trim().equals(changedPartLot.getComment().trim())){
			
			showErrorMsg("Please update comment for part lot");
			return;
		}
		
		if(partLotTableModel.isUpdatingPrimaryKey(selectedPartLot.getId())) {
			PartLot original = partLotTableModel.getOriginalPartLot(selectedPartLot.getId());
			getDao(PartLotDao.class).remove(original);
			logUserAction(REMOVED, original);
		}
		
		getDao(PartLotDao.class).update(selectedPartLot);
		logUserAction(UPDATED, selectedPartLot);
		
		partLotTablePane.getTable().setSelectionBackground(defaultSelectBackground);
		partLotTableModel.deleteOriginal(selectedPartLot.getId());
		partLotTableModel.selectItem(partLotTableModel.findPartLot(selectedPartLot));
		
	}

	private void showErrorMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}


	private TablePane createPartLotTablePane() {
		partLotTablePane = new TablePane("Part Lot");
		partLotTablePane.setSize(1024,midPanelHeight);
		//partLotTablePane.getTable().setRowHeight(20);
		//partLotTablePane.getTable().setFont(new Font("Dialog", Font.PLAIN, 18));
		partLotTablePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		partLotTablePane.setLocation(startX, startY);
		return partLotTablePane;
	}

	private void showPartLot() {
		Exception exception = null;
		try{
			PartLot selectedPartLot = partLotTableModel == null ? null : partLotTableModel.getSelectedItem();
			
			List<PartLot> partLots = getPartLots();
			
			partLotTablePane.getTable().setSelectionBackground(defaultSelectBackground);
			partLotTableModel = new PartLotTableModel(partLotTablePane.getTable(),partLots);
			//partLotTableModel.pack();
			partLotTablePane.addListSelectionListener(this); 

			selectedPartLot = findPartLot(partLots, selectedPartLot);
			if(selectedPartLot != null) {
				
				partLotTableModel.selectItem(partLotTableModel.findPartLot(selectedPartLot));
			}
		}catch(Exception ex) {
			exception = ex;
		}
		handleException(exception);
	}
	
	private PartLot findPartLot(List<PartLot> partLots, PartLot selectedPartLot) {
		if(selectedPartLot == null) return null;
		
		for(PartLot partLot : partLots){
			if(partLot.getId().equals(selectedPartLot.getId()))
				return partLot;
		}
		return null;
	}

	private List<PartLot> getPartLots(){
		List<PartLot> list = getDao(PartLotDao.class).findAll();
		List<PartLot> showList = new ArrayList<PartLot>();
		for(PartLot lot : list){
			if(isShowAllPartLots() || lot.getStatus() != PartLotStatus.CLOSED)
				showList.add(lot);
		}
		
		return showList;
	}
	

	public boolean isShowAllPartLots() {
		return showAllPartLots;
	}

	public void setShowAllPartLots(boolean showAllPartLots) {
		this.showAllPartLots = showAllPartLots;
	}

}
