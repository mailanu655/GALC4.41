package com.honda.galc.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>ClientProgress</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ClientProgress description </p>
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
 * Dec 28, 2010
 *
 */

public class ClientStartUpProgress extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	String title;
	JPanel progressPanel;
	JProgressBar progressBar;
	JLabel descriptionLabel;
	
	public ClientStartUpProgress(String title) throws HeadlessException {
		super(title);
		this.title = title;
		
		intialize();
	}

	public ClientStartUpProgress() {
		
	}

	private void intialize() {
		
		AnnotationProcessor.process(this);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getProgressPanel());
		
		pack();
		this.setLocationRelativeTo(null);
		
	}

	public JPanel getProgressPanel() {
		if(progressPanel == null){
			progressPanel = new JPanel();
			progressPanel.setLayout(new BorderLayout());
			progressPanel.setOpaque(true);
			progressPanel.add(getDescriptionLabel(), BorderLayout.NORTH);
			progressPanel.add(getProgressBar(), BorderLayout.CENTER);
			progressPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
		}
		return progressPanel;
	}

	private JProgressBar getProgressBar() {
		if(progressBar == null){
			progressBar = new JProgressBar(0, 100);
			progressBar.setPreferredSize(new Dimension(500, 35));
			progressBar.setValue(0);
			progressBar.setIndeterminate(true);
			progressBar.setStringPainted(true);
		}

		return progressBar;
	}
	
	public JLabel getDescriptionLabel() {
		if(descriptionLabel == null)
			descriptionLabel = new JLabel();
		
		return descriptionLabel;
	}

	public void setDescriptionLabel(JLabel descriptionLabel) {
		this.descriptionLabel = descriptionLabel;
	}

	@EventSubscriber(eventClass = ProgressEvent.class)
	public void processProgressEvent(ProgressEvent event){
		getProgressBar().setValue(event.getProgress());
		getProgressBar().setIndeterminate(false);
		getDescriptionLabel().setText(event.getDescription());
		
		Logger.getLogger().info("Received Progress Event :", event.getDescription() + event.getProgress());
		
		if(event.getProgress() == 100)
			this.dispose();
	}

	public void run() {
		this.setVisible(true);
		
	}
	
	public void start(){
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed to start client start up progress monitor.");
		}		
	}

}
