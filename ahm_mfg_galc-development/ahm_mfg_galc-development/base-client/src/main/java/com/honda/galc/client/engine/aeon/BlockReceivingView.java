package com.honda.galc.client.engine.aeon;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.client.utils.ViewUtil;

/**
 * 
 * 
 * <h3>BlockReceivingView Class description</h3>
 * <p> BlockReceivingView description </p>
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
 * Jan 11, 2016
 *
 *
 */
public class BlockReceivingView extends ApplicationMainPanel {
	private static final long serialVersionUID = 1L;

	protected LabeledTextField blockMCTextField;
	protected List<JTextField> boreTextFields;
	protected List<JTextField> crankTextFields;
	protected JButton okButton;
	protected JButton resetButton;
	
	private BlockReceivingModel blockReceivingModel;
	private BlockReceivingController blockReceivingController;
		
	public BlockReceivingView(DefaultWindow window) {
		super(window);
		blockReceivingModel = new BlockReceivingModel(window.getApplicationContext());
		blockReceivingController = new BlockReceivingController(blockReceivingModel,this);
		initComponents();
		mapActions();
	}
	
	private void initComponents() {
		
		setLayout(new MigLayout("insets 100", "[grow,fill]"));
		
		blockMCTextField = createLabeledTextField("BLOCK MC:",280,getLabelFont(), 18, Color.GREEN,true);
		boreTextFields = createSingleCharTextFields("bore",blockReceivingModel.BORE_MEASUREMENT_COUNT);
		crankTextFields = createSingleCharTextFields("crank",blockReceivingModel.CRANK_JOURNAL_COUNT);
		okButton = createButton("OK");
		resetButton = createButton("RESET");
		
		add(blockMCTextField,"span,wrap");
		add(createMeasurementInputPanel("BORE MEASUREMENT:", boreTextFields,true),"gaptop 50,span,wrap");
		add(createMeasurementInputPanel("CRANK JOURNAL:", crankTextFields,false),"gaptop 50,span,wrap");
		add(okButton,"gaptop 50,gapleft 300");
		add(resetButton,"gapleft 10,gapright 200");
		
	}
	
	private void mapActions() {
	   blockMCTextField.getComponent().addActionListener(blockReceivingController);
	   
	   for(JTextField bean : boreTextFields) {
		   bean.addKeyListener(blockReceivingController);
		   bean.addFocusListener(blockReceivingController);
	   }
	   
	   for(JTextField bean : crankTextFields) {
		   bean.addKeyListener(blockReceivingController);
		   bean.addFocusListener(blockReceivingController);
	   }
	   
	   okButton.addActionListener(blockReceivingController);
	   okButton.addKeyListener(blockReceivingController);
	   resetButton.addActionListener(blockReceivingController);
	   
	   okButton.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "DoClick");
	   okButton.getActionMap().put("DoClick", okButton.getAction());
	}
	
	private List<JTextField> createSingleCharTextFields(String name, int size) {
		List<JTextField> fields = new ArrayList<JTextField>();
		for(int i=0;i<size;i++) {
			fields.add(createSingleCharTextField(name + i));
		}
		return fields;
	}
	
	private JPanel createMeasurementInputPanel(String label,List<JTextField> fields,boolean isAscending) {
		JPanel panel = new JPanel(new MigLayout("insets 0", "[grow,fill]"));
		panel.add(createLabel(label,getLabelFont()),"dock west,gapright 20");
		for(int i = 0;i< fields.size();i++) {
			if(i == fields.size() - 1)
				panel.add(fields.get(i),"wrap,span 3");
			else panel.add(fields.get(i));
		}
		
		for(int i = 1;i<= fields.size();i++) {
			int num = isAscending ? i : fields.size() - i + 1;
			JLabel tmpLabel = createLabel("" + num,new Font("sansserif", 1,30));
			 panel.add(tmpLabel);
		}
		
		return panel;
		
	}
	
	private JTextField createSingleCharTextField(String name) {
		
		JTextField bean = new JTextField();
		bean.setDocument(new UpperCaseDocument(1));
		bean.setName(name);
		bean.setFont(getLabelFont());
		bean.setHorizontalAlignment(JTextField.CENTER);
		TextFieldState.EDIT.setState(bean);
		ViewUtil.setPreferredWidth(bean, 80);
		
		return bean;
	}
	
	private LabeledTextField createLabeledTextField(String label,int labelWidth,Font font,int columnSize,Color backgroundColor,boolean enabled) {
		LabeledTextField labeledTextField =  new LabeledTextField(label);
		labeledTextField.getComponent().setDocument(new UpperCaseDocument(columnSize));
		labeledTextField.setFont(font);
		labeledTextField.setLabelPreferredWidth(labelWidth);
		labeledTextField.getComponent().setHorizontalAlignment(JTextField.RIGHT);
		TextFieldState.EDIT.setState(labeledTextField.getComponent());
		return labeledTextField;
	}
	
	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setName(text);
		button.setFont(new Font("sansserif", 1, 35));
		return button;
	}
	
	private JLabel createLabel(String labelText,Font font) {
		JLabel label = new JLabel(labelText);
		label.setFont(font);
		label.setHorizontalAlignment(JLabel.CENTER);
		return label;
	}
	
	private Font getLabelFont() {
		return new Font("sansserif", 1,40);
	}
	
	public void setFocusForNextValue(JTextField bean) {
		int index = boreTextFields.indexOf(bean);
		if(index >= 0 && index + 1 < boreTextFields.size()){
			boreTextFields.get(index + 1).requestFocusInWindow();
			boreTextFields.get(index + 1).selectAll();
		}	
		else {
			if(index < 0) index = crankTextFields.indexOf(bean);
			else index = -1;
			if(index >= -1 && index + 1 < crankTextFields.size()){
				crankTextFields.get(index + 1).requestFocusInWindow();
				crankTextFields.get(index + 1).selectAll();
			}else {
				okButton.requestFocusInWindow();
			}
		}
	}

	
}
