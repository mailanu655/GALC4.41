package com.honda.galc.client.glassload;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.glassload.GlassLoadController.PlcMode;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.component.ColorTableCellRenderer;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.enumtype.LoginStatus;

import net.miginfocom.swing.MigLayout;

public class GlassLoadView extends ApplicationMainPanel implements ActionListener, ListSelectionListener{
	
	private GlassLoadModel glassLoadModel;
	private GlassLoadController glassLoadController;
	
	private JMenuItem reassignCarriersMenu = new JMenuItem("Reassign Carriers");
	private JMenuItem refreshVehicleListMenu = new JMenuItem("Refresh Vehicle List");
	private JMenuItem skipCurrentVehicleMenu = new JMenuItem("Skip Current Vehicle");
	private JMenuItem carrierMaintenanceMenu = new JMenuItem("Carrier Maintenance");
	
	public LabeledUpperCaseTextField[] vinTextFields  = new LabeledUpperCaseTextField[3];
	protected LabeledUpperCaseTextField[] mtocTextFields = new LabeledUpperCaseTextField[3];
	protected LabeledUpperCaseTextField[] frontGlassTypeTextFields  = new LabeledUpperCaseTextField[3];
	protected LabeledUpperCaseTextField[] rearGlassTypeTextFields  = new LabeledUpperCaseTextField[3];
		
	protected ObjectTablePane<MultiValueObject<Frame>> vinListTablePane;
	
	private Color defaultBackgroundColor;
	
	JPanel productPanel;
	JPanel ppaProductPanel;
	
	
	JPanel[] vinPanels = new JPanel[3];
	JPanel[] ppaVinPanels = new JPanel[3];
	
	public GlassLoadView(DefaultWindow window) {
		
		super(window);
		glassLoadModel = new GlassLoadModel(this);
		glassLoadController = new GlassLoadController(glassLoadModel,this);
		
		initComponents();
		
		loadAllData();
		
	}
	
	public void loadAllData() {
		
		getMainWindow().setWaitCursor();
	
		loadData();
		
		loadProductPanel();
		
		refreshVinListPanel();
		
		setMode();
		
		getMainWindow().setDefaultCursor();

	}
	
    public JMenu initializeMenu() {
        JMenu menu = new JMenu();
        menu.setName("Actions");
        menu.setText("Actions");
        
         
        reassignCarriersMenu.addActionListener(this);
        refreshVehicleListMenu.addActionListener(this);
        skipCurrentVehicleMenu.addActionListener(this);
        carrierMaintenanceMenu.addActionListener(this);
        
        menu.add(reassignCarriersMenu);
        menu.add(refreshVehicleListMenu);
        menu.add(skipCurrentVehicleMenu);
        menu.add(carrierMaintenanceMenu);
        
        
        return menu;
    }
    
    private void initComponents() {
    	
    	JPanel vinListPanel = new JPanel();
    	
    	this.defaultBackgroundColor = vinListPanel.getBackground();
    	
    	createProductTextFields();
    	
    	vinListTablePane = createTablePane();
    	vinListPanel.add(vinListTablePane);
    	setLayout(new BorderLayout());
    	
    	setProductPanel();
    	
   		add(vinListTablePane,BorderLayout.CENTER);
   	 
    }
    
    public void setProductPanel() {
       	if(glassLoadController.plcMode.equals(GlassLoadController.PlcMode.AUTO)) {
       		if(this.ppaProductPanel != null) {
       			remove(this.ppaProductPanel);
       			this.ppaProductPanel = null;
       		}
       	 	this.productPanel = createAutoModeProductPanel();
        	add(productPanel,BorderLayout.NORTH);
       		
       	}else {
       		// PPA or Manual Mode
       		if(this.productPanel != null) {
       			remove(this.productPanel);
       			this.productPanel = null;
       		}
       		if(this.ppaProductPanel != null) {
       			remove(this.ppaProductPanel);
       			this.ppaProductPanel = null;
       		}
        	this.ppaProductPanel = createPPAManualProductPanel();
            add(ppaProductPanel,BorderLayout.NORTH);
       		
       	}
    }
    
    public void completeCurrentVin(boolean curRearTypeConfirmed, boolean nextFrontTypeConfirmed, String currentRearGlassType,String nextFrontGlassType) {
		rearGlassTypeTextFields[0].getComponent().setText(currentRearGlassType);
		Color color = Color.cyan;
		if(!rearGlassTypeTextFields[0].getComponent().getText().isEmpty()) {
		   color = curRearTypeConfirmed ? Color.green : Color.yellow;
		}		
		rearGlassTypeTextFields[0].getComponent().setBackground(color);

		frontGlassTypeTextFields[1].getComponent().setText(nextFrontGlassType);
		color = Color.cyan;
		if(!frontGlassTypeTextFields[1].getComponent().getText().isEmpty()) {
		   color = nextFrontTypeConfirmed ? Color.green : Color.yellow;
		}		
		frontGlassTypeTextFields[1].getComponent().setBackground(color);
		
    }
    
    public void completePPACurrentVin() {
		Frame frame = getNextFrame(vinTextFields[1].getComponent().getText()).getKeyObject();
		
		vinTextFields[0].getComponent().setText(vinTextFields[1].getComponent().getText());
		mtocTextFields[0].getComponent().setText(mtocTextFields[1].getComponent().getText());
		rearGlassTypeTextFields[0].getComponent().setText(rearGlassTypeTextFields[1].getComponent().getText());
	
		rearGlassTypeTextFields[0].getComponent().setBackground(Color.blue);

		vinTextFields[1].getComponent().setText(frame.getProductId());
		mtocTextFields[1].getComponent().setText(frame.getProductSpecCode());
		frontGlassTypeTextFields[1].getComponent().setText("");
		frontGlassTypeTextFields[1].getComponent().setBackground(Color.cyan);
    }
    
    public MultiValueObject<Frame> getFrame(String vin) {
    	List<MultiValueObject<Frame>>  vinList = vinListTablePane.getItems();
    	for(MultiValueObject<Frame> item : vinList) {
    		if(item.getKeyObject().getProductId().equalsIgnoreCase(vin)) {
    			return item; 			
    		}
    	}    	
    	return null;
    }
    
    public MultiValueObject<Frame> getNextFrame(String vin) {
    	List<MultiValueObject<Frame>>  vinList = vinListTablePane.getItems();
    	for(MultiValueObject<Frame> item : vinList) {
    		if(item.getKeyObject().getProductId().equalsIgnoreCase(vin)) {
    			int index = vinList.indexOf(item);
    			if(index < vinList.size()) return vinList.get(index + 1);
    			else return null;  			
    		}
    	}    	
    	return null;
    }
    
    protected void loadData() {
    	List<MultiValueObject<Frame>> items = glassLoadModel.loadVinList();
    	vinListTablePane.reloadData(items);
    	
    }
    
    protected void refreshVinListPanel() {	
    	assignColors();
    	scrollHighlightPosition();
    	vinListTablePane.refresh();
    }
    
    protected void scrollHighlightPosition() {
      	 MultiValueObject<Frame> frame = getFrame(glassLoadModel.getExpectedProduct().getProductId());
    	 int index = vinListTablePane.getItems().indexOf(frame);
    	 vinListTablePane.getTable().scrollRectToVisible(vinListTablePane.getTable().getCellRect(index - glassLoadModel.getPropertyBean().getHighlightPosition(), 0, true));	
    }

    protected void assignColors() {
    	
    	int rowCount = vinListTablePane.getTable().getRowCount();
		int columnCount = vinListTablePane.getTable().getColumnCount();
	
    	for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			TableCellRenderer renderer = vinListTablePane.getTable().getColumnModel().getColumn(columnIndex).getCellRenderer();
			if (renderer instanceof ColorTableCellRenderer) {
				renderer =  ((ColorTableCellRenderer)renderer).getTargetCellRenderer();
			} else if (columnIndex == 0) {
				renderer = new BooleanRenderer(JLabel.CENTER);
			} else {
				renderer = new DefaultTableCellRenderer();
			}
			vinListTablePane.getTable().getColumnModel().getColumn(columnIndex).setCellRenderer(
					new ColorTableCellRenderer(renderer, glassLoadController.prepareBackgroudColors(), glassLoadController.prepareTextColor(), rowCount, columnCount));
		}
    }
    
    protected void loadProductPanel() {
    	
    	try {
	    	ExpectedProduct expectedProduct = glassLoadModel.getExpectedProduct();
	    	if(expectedProduct == null) return;
	    	
	    	String productId = expectedProduct.getProductId();
	    	
	    	Frame lastFrame = getFrame(productId).getKeyObject();
	    	Frame currentFrame = getNextFrame(lastFrame.getProductId()).getKeyObject();
	    	Frame nextFrame = getNextFrame(currentFrame.getProductId()).getKeyObject();
	    	
			vinTextFields[0].getComponent().setText(lastFrame.getProductId());
			mtocTextFields[0].getComponent().setText(lastFrame.getProductSpecCode());
			rearGlassTypeTextFields[0].getComponent().setText("");
			if(glassLoadController.plcMode.equals(PlcMode.PPA)) {
				rearGlassTypeTextFields[0].getComponent().setBackground(Color.blue);
			}else {
				rearGlassTypeTextFields[0].getComponent().setBackground(Color.cyan);
			}
			vinTextFields[1].getComponent().setText(currentFrame.getProductId());
			mtocTextFields[1].getComponent().setText(currentFrame.getProductSpecCode());
			frontGlassTypeTextFields[1].getComponent().setText("");
			frontGlassTypeTextFields[1].getComponent().setBackground(Color.cyan);
			rearGlassTypeTextFields[1].getComponent().setText("");
			rearGlassTypeTextFields[1].getComponent().setBackground(Color.cyan);
	
			vinTextFields[2].getComponent().setText(nextFrame.getProductId());
			mtocTextFields[2].getComponent().setText(nextFrame.getProductSpecCode());
			frontGlassTypeTextFields[2].getComponent().setText("");
			frontGlassTypeTextFields[2].getComponent().setBackground(Color.cyan);
    	} catch (Exception ex) {
    		
    	}
    }
    
    private JPanel createAutoModeProductPanel() {
    	JPanel productPanel = new JPanel();
    	productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
    	
    	productPanel.add(vinPanels[0] = createVinPanel(0));
       	productPanel.add(vinPanels[1] = createVinPanel(1));
       	productPanel.add(vinPanels[2] = createVinPanel(2));
                
        return productPanel;
    }
    
    private JPanel createPPAManualProductPanel() {
    	JPanel productPanel = new JPanel();
    	productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
    	productPanel.add(ppaVinPanels[0] = createPPAVinPanel(0));
       	productPanel.add(ppaVinPanels[1] = createPPAVinPanel(1));
       	return productPanel;
    }
    
    public void setMode() {
    	Color color = defaultBackgroundColor;
    	if(glassLoadController.plcMode.equals(GlassLoadController.PlcMode.PPA)) color = Color.yellow;
    	if(glassLoadController.plcMode.equals(GlassLoadController.PlcMode.MANUAL)) color = Color.red;
    	
    	if(glassLoadController.plcMode.equals(GlassLoadController.PlcMode.AUTO)) {
    		this.vinPanels[0].setBackground(color);
    		this.vinPanels[1].setBackground(color);
    		this.vinPanels[2].setBackground(color);
    	}else {
    		this.ppaVinPanels[0].setBackground(color);
    		this.ppaVinPanels[1].setBackground(color);
    	}
       	
       	((GlassLoadWindow)getMainWindow()).setTitle(glassLoadController.plcMode);
    }
    
    private void createProductTextFields() {
       	createProductTextFields(0, "P VIN");
       	createProductTextFields(1, "C VIN");
       	createProductTextFields(2, "N VIN");
    }
    
    private void createProductTextFields(int index, String label) {
       	vinTextFields[index] = createLabeledTextField(label,60,getLabelFont(), 20, Color.cyan,false);
    	mtocTextFields[index] = createLabeledTextField("",0,getLabelFont(), 22, Color.cyan,false);;
    	
      	frontGlassTypeTextFields[index] = createLabeledTextField("Front",60,getLabelFont(), 6, Color.cyan,false);
      	rearGlassTypeTextFields[index] = createLabeledTextField("Rear",50,getLabelFont(), 6, Color.cyan,false);;
    }
    
    
    private JPanel createVinPanel(int index) {
      	
      	JPanel panel = new JPanel();
      	panel.setLayout(new MigLayout("insets 10", ""));
      	panel.add(vinTextFields[index],"width 330:330:330");
      	panel.add(mtocTextFields[index],"width 290:290:290");
      	if(index >0) panel.add(frontGlassTypeTextFields[index],"width 180:180:180");
      	if(index == 0 ) panel.add(rearGlassTypeTextFields[index],"gapleft 182, width 170:170:170,align right");
      	else if(index == 1 ) panel.add(rearGlassTypeTextFields[index],"width 170:170:170,align right");
      	
      	return panel;

    }
    
    private JPanel createPPAVinPanel(int index) {
      	
      	JPanel panel = new JPanel();
      	panel.setLayout(new MigLayout("insets 10", ""));
      	panel.add(vinTextFields[index],"width 330:330:330");
      	panel.add(mtocTextFields[index],"width 290:290:290");
       	if(index == 0) panel.add(rearGlassTypeTextFields[index],"gapleft 182, width 170:170:170,align right");
      	if(index == 1) panel.add(frontGlassTypeTextFields[index],"width 170:170:170,align right");
        
      	return panel;

    }
    
    
	private LabeledUpperCaseTextField createLabeledTextField(String label,int labelWidth,Font font,int columnSize,Color backgroundColor,boolean enabled) {
		LabeledUpperCaseTextField labeledTextField =  new LabeledUpperCaseTextField(label);
		labeledTextField.setFont(font);
		labeledTextField.setLabelPreferredWidth(labelWidth);
		labeledTextField.getComponent().setMaximumLength(columnSize);
		labeledTextField.setInsets(0, 10, 0, 10);
		labeledTextField.getComponent().setFixedLength(false);
		labeledTextField.getComponent().setHorizontalAlignment(JTextField.CENTER);
		labeledTextField.getComponent().setBackground(backgroundColor);
		labeledTextField.getComponent().setEnabled(enabled);
		labeledTextField.getComponent().setDisabledTextColor(Color.black);
		return labeledTextField;
	}
	
	private Font getLabelFont() {
		return new Font("sansserif", 1,22);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(carrierMaintenanceMenu)) carrierMaintenanceMenuClicked();
		else if(e.getSource().equals(reassignCarriersMenu)) reassignMenuClicked();
		else if(e.getSource().equals(refreshVehicleListMenu)) refreshVehicleList();
		else if(e.getSource().equals(skipCurrentVehicleMenu)) skipCurrentVehicle();
	}
	
	private void refreshVehicleList() {
		if(!login("Refresh Vehicle List")) return;
		
		loadAllData();
		
		glassLoadController.setMessage("User refreshed vehcile list");

	}
	
	private void skipCurrentVehicle() {
		if(!login("Skip Current Vehicle")) return;
		
		
		 glassLoadController.skipCurrentVehicle();
	}
	
	private void  carrierMaintenanceMenuClicked() {
		if(!login("Maintain Glass Load Carrier")) return;
		GlassCarrierMaintenaceDialog dialog = new GlassCarrierMaintenaceDialog(glassLoadController);
		dialog.setVisible(true);
		
		if(dialog.isDirty) {
			loadAllData();
		}
		
		glassLoadController.setMessage("User finished carrier maintenance");

	}
	
	private void reassignMenuClicked() {
		if(!login("Reassign Glass Load Carrier")) return;
		GlassReassignDialog dialog = new GlassReassignDialog(glassLoadController);
		dialog.setVisible(true);
		
		glassLoadController.setMessage("User finished reassigning carriers");
	}
	
	private ObjectTablePane<MultiValueObject<Frame>> createTablePane()  {
		ColumnMappings columnMappings = ColumnMappings.with(new String[] {"","Carrier", "Ref #", "VIN","MTOC", "Glass Type", "KD Lot","Lot Pos", "Lot Size"});
		ObjectTablePane<MultiValueObject<Frame>> pane = new ObjectTablePane<MultiValueObject<Frame>>(
				"",columnMappings.get());
		
		pane.getTable().getTableHeader().setFont(Fonts.DIALOG_BOLD(glassLoadModel.getPropertyBean().getHeaderFontSize()));
		pane.getTable().setFont(new Font("sansserif", 1, glassLoadModel.getPropertyBean().getFontSize()));
		pane.getTable().setRowHeight(glassLoadModel.getPropertyBean().getItemHeight());
		pane.setAlignment(SwingConstants.CENTER);
		
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pane.getTable().getSelectionModel().addListSelectionListener(this);
		
		return pane;
	}
	
	private boolean login(String message) {
		if(LoginDialog.login() != LoginStatus.OK) return false; 
	
		if (!ClientMain.getInstance().getAccessControlManager().isAuthorized(glassLoadModel.getPropertyBean().getAuthorizationGroup())) {
			JOptionPane.showMessageDialog(null, "You have no access permission to " + message, "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}


	private static class BooleanRenderer extends JCheckBox implements TableCellRenderer, UIResource
	{
		private static final long serialVersionUID = 1L;
		private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

		public BooleanRenderer(final int alignment) {
			super();
			setHorizontalAlignment(alignment);
			setBorderPainted(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			}
			else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setSelected((value != null && ((Boolean)value).booleanValue()));

			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			} else {
				setBorder(noFocusBorder);
			}

			return this;
		}
	}
}
