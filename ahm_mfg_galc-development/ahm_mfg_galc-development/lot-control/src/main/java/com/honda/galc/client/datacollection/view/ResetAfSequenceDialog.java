package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.SequenceDao;
import com.honda.galc.entity.product.Sequence;
import com.honda.galc.service.ServiceFactory;

public class ResetAfSequenceDialog extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private JPanel buttonPanel, messagePanel;
	private JButton saveButton;
	private JButton cancelButton;
	private UpperCaseFieldBean newSeqTextField;
	private JLabel msgLabel;
	private String msg;
	public ResetAfSequenceDialog(JFrame owner){
		super(owner, true);
		setTitle("Reset Sequence");
		setLocationRelativeTo(owner);
		
		initComponents();
	}
	
	private void initComponents() {
			
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1,10,10));
		panel.setBackground(Color.YELLOW);
				
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		panel1.setBackground(Color.YELLOW);
		
		JLabel  currentSeqLabel = new JLabel();
		currentSeqLabel.setFont(new java.awt.Font("", 0, 40));
		currentSeqLabel.setText("Current Seq :");
		currentSeqLabel.setBackground(Color.YELLOW);
		
		JLabel currSeqValueLabel = new JLabel();
		currSeqValueLabel.setFont(new java.awt.Font("", 0, 40));
		currSeqValueLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		currSeqValueLabel.setText(String.valueOf(getCurrentSequence()));
		currSeqValueLabel.setBackground(Color.YELLOW);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		panel2.setBackground(Color.YELLOW);
		
		JLabel newSeqLabel = new JLabel();
		newSeqLabel.setFont(new java.awt.Font("", 0, 40));
		newSeqLabel.setText("New Sequence :");
		newSeqLabel.setBackground(Color.YELLOW);
						
		panel1.add(currentSeqLabel);
		panel1.add(currSeqValueLabel);
		panel2.add(newSeqLabel);
		panel2.add(getNewSequenceTextField());
		
		panel.add(panel1);
		panel.add(panel2);
		panel.add(getMessagePanel());
		panel.add(getButtonPanel());
		
		setContentPane(panel);
		setSize(600, 400);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setBackground(Color.YELLOW);
		
		 WindowAdapter adapter = new WindowAdapter() {
	            private boolean gotFocus = false;
	            public void windowClosing(WindowEvent we) {
	              
	            }
	            public void windowGainedFocus(WindowEvent we) {
	                // Once window gets focus, set initial focus
	                if (!gotFocus) {
	                   gotFocus = true;
	                
	                   getNewSequenceTextField().requestFocus();
	                  
	                }
	            }
	        };
	        addWindowListener(adapter);
	        addWindowFocusListener(adapter);
	}
	
	private UpperCaseFieldBean getNewSequenceTextField(){
		if(newSeqTextField == null){
			newSeqTextField = new UpperCaseFieldBean();
			newSeqTextField.setColumns(5);
			newSeqTextField.setMaximumLength(5);
			newSeqTextField.setFont(new java.awt.Font("", 0, 25));
			newSeqTextField.setPreferredSize(new Dimension(100, 30));
		}
		
		return newSeqTextField;
	}
	
	private JLabel getMessageLabel(){
		if(msgLabel == null){
			msgLabel = new JLabel();
			msgLabel.setFont(new java.awt.Font("", 0, 25));
		}
		return msgLabel;
	}
	
	private Component getMessagePanel() {
		if (messagePanel == null) {
			messagePanel = new JPanel();
			messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
			messagePanel.add(getMessageLabel());
			messagePanel.setBackground(Color.YELLOW);
		}
		return messagePanel;
	}
	
	private Component getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
			buttonPanel.add(getSaveButton());
			buttonPanel.add(getCancelButton());
			buttonPanel.setBackground(Color.YELLOW);
		}
		return buttonPanel;
	}

	public JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton("Cancel");
			cancelButton.setName("Cancel");
			cancelButton.addActionListener(this);
		}
		return cancelButton;
	}

	public JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton("Save");
			saveButton.setName("Save");
			saveButton.addActionListener(this);
		}
		return saveButton;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getCancelButton()) {
			this.dispose();
		} else if ( e.getSource() == getSaveButton()) {
			if(resetSequence())	this.dispose();
		}
		
	}

	private boolean resetSequence() {
		SequenceDao seqDao = getSequenceDao();
		Sequence  seq = seqDao.findByKey(getSequenceName());
		String newSequence = getNewSequenceTextField().getText();
		try{
		Integer seqNumber = Integer.parseInt(newSequence);
		if(seqNumber < 1 || seqNumber > 99999){
			Logger.getLogger().info(" received invalid sequence -"+ newSequence);
			setErrorMessage();
		}else{
			getMessageLabel().setText("");
			seq.setCurrentSeq(seqNumber-seq.getIncrementValue());
			Logger.getLogger().info(" resetting -"+getSequenceName()+" to new Sequence - "+ newSequence);
			seqDao.save(seq);
			return true;
		}
		
		}catch(Exception e){
			Logger.getLogger().info(" received invalid sequence -"+ newSequence);
			setErrorMessage();
		}
		return false;
	}

	private String getSequenceName(){
		return DataCollectionController.getInstance().getProperty().getSequenceName();
	}
	
	private Integer getCurrentSequence(){
		Sequence  seq = getSequenceDao().getNextSequence(getSequenceName());
		return seq.getCurrentSeq();
	}
		
	private SequenceDao getSequenceDao(){
		return ServiceFactory.getDao(SequenceDao.class);
	}
	
	private void setErrorMessage(){
		msg="invalid Sequence Number";
		getMessageLabel().setText(msg);
		
		getNewSequenceTextField().setSelectionStart(0);
		getNewSequenceTextField().setSelectionEnd(getNewSequenceTextField().getText().length());
		getNewSequenceTextField().requestFocus();
	}
}
