package com.honda.galc.device.simulator.client.view.cfg;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.honda.galc.device.simulator.client.view.data.ConfigValueObject;


/**
 * <h3>AppearenceVarPanel</h3>
 * <h4> </h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Sep. 23, 2008</TD>
 * <TD>Version 0.2</TD>
 * <TD></TD>
 * <TD>Initial version.</TD>
 * </TR>
 * </TABLE>
 */
public class AppearenceVarPanel extends SimulatorVarPanel {
	private static final long serialVersionUID = -2998737759433530295L;
	private JPanel varReplyPanel = null;
	private JPanel varSenderPanel = null;
	private ButtonGroup senderButtonGroup;  //  @jve:decl-index=0:
	private ButtonGroup replyButtonGroup;  //  @jve:decl-index=0:
	private JRadioButton showTagRadioButton = null;
	private JRadioButton showReplyTagRadioButton = null;
	private JRadioButton showReplyTagValueRadioButton = null;
	private JRadioButton showTagValueRadioButton = null;
	private boolean senderShowTag;
	private boolean replyShowTag;

	public AppearenceVarPanel() {
		super();

		setLayout(new GridBagLayout());
		setName("Simulator UI");
		
		initialize();
	}

	private void initialize() {
		GridBagConstraints c = getConstraint();
		c.gridy = 0;
		c.insets = new Insets(20, 40, 10, 40);
		add(getSenderVarPanel(), c);
		c.gridy = 1;
		add(getReplyVarPanel(), c);
		
		getShowTagRadioButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionOnSenderShowTagGroup(true);
			}
		});
		getShowTagValueRadioButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionOnSenderShowTagGroup(false);
			}
		});
		
		getShowReplyTagRadioButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionOnReplyShowTagGroup(true);
			}
		});
		getShowReplyTagValueRadioButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionOnReplyShowTagGroup(false);
			}
		});

	}
	
	/**
	 * This method initializes sender Variable Panel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSenderVarPanel() {
		if (varSenderPanel == null) {
			varSenderPanel = new JPanel();
			TitledBorder border = new TitledBorder("Sender Panel");
			varSenderPanel.setBorder(border);	
			varSenderPanel.setLayout(new BorderLayout());
			varSenderPanel.add(getShowTagRadioButton(), BorderLayout.NORTH);
			varSenderPanel.add(getShowTagValueRadioButton(), BorderLayout.SOUTH);
			
			senderButtonGroup = new ButtonGroup();
			senderButtonGroup.add(getShowTagRadioButton());
			senderButtonGroup.add(getShowTagValueRadioButton());
		}
		return varSenderPanel;
	}
	
	/**
	 * This method initializes varReplyPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getReplyVarPanel() {
		if (varReplyPanel == null) {
			
			TitledBorder replyBorder = new TitledBorder("Reply Panel");
			varReplyPanel = new JPanel();
			varReplyPanel.setLayout(new BorderLayout());
			varReplyPanel.setBorder(replyBorder);
			varReplyPanel.add(getShowReplyTagRadioButton(), BorderLayout.NORTH);
			varReplyPanel.add(getShowReplyTagValueRadioButton(), BorderLayout.SOUTH);
			
			replyButtonGroup = new ButtonGroup();
			replyButtonGroup.add(getShowReplyTagRadioButton());
			replyButtonGroup.add(getShowReplyTagValueRadioButton());
		}
		return varReplyPanel;
	}
	
	/**
	 * This method initializes showTagRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getShowTagRadioButton() {
		if (showTagRadioButton == null) {
			showTagRadioButton = new JRadioButton();
			showTagRadioButton.setText("Show Data Format Tag");
			showTagRadioButton.setFont(getTextFont());
			
		}
		return showTagRadioButton;
	}
	
	/**
	 * This method initializes showReplyTagRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getShowReplyTagRadioButton() {
		if (showReplyTagRadioButton == null) {
			showReplyTagRadioButton = new JRadioButton();
			showReplyTagRadioButton.setFont(getFont());
			showReplyTagRadioButton.setText("Show Data Format Tag");
		}
		return showReplyTagRadioButton;
	}
	
	/**
	 * This method initializes showReplyTagValueRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getShowReplyTagValueRadioButton() {
		if (showReplyTagValueRadioButton == null) {
			showReplyTagValueRadioButton = new JRadioButton();
			showReplyTagValueRadioButton.setFont(getFont());
			showReplyTagValueRadioButton.setText("Show Data Format Tag Value");
		}
		return showReplyTagValueRadioButton;
	}
	
	/**
	 * This method initializes showTagValueRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getShowTagValueRadioButton() {
		if (showTagValueRadioButton == null) {
			showTagValueRadioButton = new JRadioButton();
			showTagValueRadioButton.setText("Show Data Format Tag Value");
			showTagValueRadioButton.setFont(getFont());
		}
		return showTagValueRadioButton;
	}
	
	protected void actionOnReplyShowTagGroup(boolean b) {
		replyShowTag = b;
	}

	protected void actionOnSenderShowTagGroup(boolean showTag) {
		senderShowTag = showTag;
		
	}

	/**
	 * @return the replyShowTag
	 */
	public boolean isReplyShowTag() {
		return replyShowTag;
	}

	/**
	 * @param replyShowTag the replyShowTag to set
	 */
	public void setReplyShowTag(boolean replyShowTag) {
		this.replyShowTag = replyShowTag;
	}

	/**
	 * @return the senderShowTag
	 */
	public boolean isSenderShowTag() {
		return senderShowTag;
	}

	/**
	 * @param senderShowTag the senderShowTag to set
	 */
	public void setSenderShowTag(boolean senderShowTag) {
		this.senderShowTag = senderShowTag;
	}
	
	public void setShowTagRadioButton(boolean b)
	{
		getShowTagRadioButton().setSelected(b);
		getShowTagValueRadioButton().setSelected(!b);
	}
	
	public void setShowReplyTagRadioButton(boolean b)
	{
		getShowReplyTagRadioButton().setSelected(b);
		getShowReplyTagValueRadioButton().setSelected(!b);
	}

	protected void fromValueObj(ConfigValueObject vo) {
		senderShowTag = vo.isSenderShowTag();
		replyShowTag = vo.isReplyShowTag();
		setShowTagRadioButton(vo.isSenderShowTag());
		setShowReplyTagRadioButton(vo.isReplyShowTag());
	}

	protected void saveToValueObj(ConfigValueObject vo) {
		vo.setSenderShowTag(isSenderShowTag());
		vo.setReplyShowTag(isReplyShowTag());
		
	}

	

}
