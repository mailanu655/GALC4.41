package com.honda.galc.client.teamleader;


import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.common.logging.Logger;

import com.honda.galc.dao.product.SequenceDao;

import com.honda.galc.entity.product.Sequence;

import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;


public class ResetSequencePanel extends TabbedPanel implements  ActionListener,ItemListener {

	private JPanel buttonPanel;
	private JButton saveButton;
	private JButton cancelButton;
	
	private LabeledComboBox sequenceComboBox;
	private LabeledTextField currentSeqText;
	private LabeledTextField newSeqText;
	private LabeledTextField minMaxText;
	private LabeledTextField descriptionText;
	

	public ResetSequencePanel() {
		super("Reset Sequence", KeyEvent.VK_L);
	}

	public ResetSequencePanel(TabbedMainWindow mainWindow) {
		super("Reset Sequence", KeyEvent.VK_L, mainWindow);
	}

	private void initComponents() {
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
		
		
		Box box = Box.createVerticalBox();
		

		box.add(getSequenceComboBox());
		box.add(getDescription());
		box.add(getMinMax());
		box.add(getCurrentSeq());
		box.add(getNewSeq());
		box.add(getButtonPanel());
		
		add(box);
		
		populateSequences();
	}
	

	private Component getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
			buttonPanel.add(getSaveButton());
			buttonPanel.add(getCancelButton());
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
			saveButton.setEnabled(false);
		}
		return saveButton;
	}
	

	private void populateSequences() {
		List<String> sequenceNames = PropertyService.getPropertyList(getMainWindow().getApplication().getApplicationId(), "SEQUENCE_NAMES");
		getSequenceComboBox().setModel(sequenceNames, -1);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getSequenceComboBox().getComponent()) {
			String sequenceName = (String)getSequenceComboBox().getComponent().getSelectedItem();
			if(StringUtils.isNotEmpty(sequenceName)) populateSeqInfo(sequenceName);
		}else if (e.getSource() == getCancelButton()) {
			resetSeq();
		} else if ( e.getSource() == getSaveButton()) {
			String sequenceName = (String)getSequenceComboBox().getComponent().getSelectedItem();
			String desc = getDescription().getComponent().getText();
			resetSequence(sequenceName,desc);
			if(StringUtils.isNotEmpty(sequenceName)) populateSeqInfo(sequenceName);
		}
		
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == getSequenceComboBox().getComponent()) {
			String sequenceName = (String)getSequenceComboBox().getComponent().getSelectedItem();
			clearErrorMessage();
			if(StringUtils.isNotEmpty(sequenceName))populateSeqInfo(sequenceName);
		}
		
	}

	private void populateSeqInfo(String sequenceName) {
		SequenceDao seqDao = getSequenceDao();
		
		Sequence  seq = seqDao.findByKey(sequenceName);
		
		if(seq != null) {
			String currSeq = seqDao.getNextSequence(sequenceName).getCurrentSeq().toString();
			getCurrentSeq().getComponent().setText(currSeq);
			getMinMax().getComponent().setText(seq.getStartSeq().toString() +" / " +seq.getEndSeq().toString());
			getDescription().getComponent().setText(seq.getDescription());
			getNewSeq().getComponent().setText("");
			getSaveButton().setEnabled(true);
		}
		
	}

	private void resetSeq() {
		getNewSeq().getComponent().setText("");
		clearErrorMessage();
	}

	private boolean resetSequence(String sequenceName,String desc) {
		SequenceDao seqDao = getSequenceDao();
		Sequence  seq = seqDao.findByKey(sequenceName);
		seq.setDescription(desc);
		String newSequence = getNewSeq().getComponent().getText();
		try{
		Integer seqNumber = Integer.parseInt(newSequence);
		if(seqNumber < seq.getStartSeq() || seqNumber > seq.getEndSeq()){
			Logger.getLogger().info(" received invalid sequence -"+ newSequence);
			setErrorMessage(" received invalid sequence -"+ newSequence);
		}else{
			clearErrorMessage();
			if(seqNumber-seq.getIncrementValue() < seq.getStartSeq()) seq.setCurrentSeq(seq.getEndSeq());
			else seq.setCurrentSeq(seqNumber-seq.getIncrementValue());
			Logger.getLogger().info(" resetting -"+sequenceName+" to new Sequence - "+ newSequence );
			seqDao.save(seq);
			setMessage(" Updated - "+sequenceName+" to new Sequence - "+ newSequence);
			
			logUserAction(SAVED, seq);
			AuditLoggerUtil.logAuditInfo(null, seq, "save", getScreenName(), getUserName().toUpperCase(),"GALC","GALC_Maintenance");
			return true;
		}
		
		}catch(Exception e){
			Logger.getLogger().info(" received invalid sequence -"+ newSequence);
			setErrorMessage(" received invalid sequence -"+ newSequence);
		}
		return false;
	}

	private SequenceDao getSequenceDao(){
		return ServiceFactory.getDao(SequenceDao.class);
	}
	

	@Override
	public void onTabSelected() {
		if (isInitialized) {
			return;
		}
		initComponents();
		isInitialized = true;
	}

	public LabeledComboBox getSequenceComboBox() {
		if(sequenceComboBox == null) {
			sequenceComboBox = new LabeledComboBox("  Seq Name : ");
			sequenceComboBox.setFont(new java.awt.Font("", 0, 20));
			sequenceComboBox.getLabel().setHorizontalAlignment(SwingConstants.RIGHT);
			sequenceComboBox.getComponent().addActionListener(this);
			sequenceComboBox.getComponent().addItemListener(this);
		}
		return sequenceComboBox;
	}

	public LabeledTextField getCurrentSeq() {
		if(currentSeqText == null) {
			currentSeqText = new LabeledTextField(" Current Seq :");
			currentSeqText.setFont(new java.awt.Font("", 0, 20));
			currentSeqText.getLabel().setHorizontalAlignment(SwingConstants.RIGHT);
			currentSeqText.setEnabled(false);
		}
		return currentSeqText;
	}

	public LabeledTextField getNewSeq() {
		if(newSeqText == null) {
			newSeqText = new LabeledTextField("    New Seq : ");
			newSeqText.setFont(new java.awt.Font("", 0, 20));
			newSeqText.getLabel().setHorizontalAlignment(SwingConstants.RIGHT);
			newSeqText.setEnabled(true);
		}
		return newSeqText;
	}

	public LabeledTextField getMinMax() {
		if(minMaxText == null) {
			minMaxText = new LabeledTextField("   Min / Max : ");
			minMaxText.setFont(new java.awt.Font("", 0, 20));
			minMaxText.getLabel().setHorizontalAlignment(SwingConstants.RIGHT);
			minMaxText.setEnabled(false);
		}
		return minMaxText;
	}
	
	public LabeledTextField getDescription() {
		if(descriptionText == null) {
			descriptionText = new LabeledTextField("   Description : ");
			descriptionText.setFont(new java.awt.Font("", 0, 20));
			descriptionText.getLabel().setHorizontalAlignment(SwingConstants.RIGHT);
			descriptionText.setEnabled(true);
		}
		return descriptionText;
	}


	
	
	
}
