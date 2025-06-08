package com.honda.galc.client.qics.view.fragments;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.JTextComponent;

import com.honda.galc.client.qics.validator.QicsValidator;
import com.honda.galc.client.qics.view.screen.DefectRepairPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.entity.qics.DefectActualProblem;
import com.honda.galc.entity.qics.DefectActualProblemId;
import com.honda.galc.entity.qics.DefectRepairMethod;
import com.honda.galc.entity.qics.DefectRepairMethodId;
import com.honda.galc.entity.qics.DefectResult;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Defect Repair Panel user input elements.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */

public class DefectRepairInputPanel extends JPanel implements ActionListener, DocumentListener, ChangeListener {

	private int associateNumberLength = QicsValidator.MAX_INPUT_ASSOCIATE_NUMBER_LENGTH;

	private static final long serialVersionUID = 1L;
	
	private DefectRepairPanel qicsPanel;
	private JLabel repairMethodLabel;
	private JComboBox repairMethodComboBox;
	private JLabel actualProblemLabel;
	private JComboBox actualProblemComboBox;
	private JLabel timeLabel;
	private JSpinner timeSpinner;
	private JLabel associateNumberLabel;
	private JComboBox associateNumberComboBox;
	private JLabel commentLabel;
	private JTextArea commentInputElement;
	private JScrollPane commentScrollPane;

	public DefectRepairInputPanel(DefectRepairPanel qicsPanel) {
		this(qicsPanel,770, 165);
	}

	public DefectRepairInputPanel(DefectRepairPanel qicsPanel,int width, int height) {
		this.qicsPanel = qicsPanel;
		initialize(width, height);
		mapHandlers();
	}

	protected void initialize(int width, int height) {
		setLayout(null);
		setSize(width, height);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		add(getTimeLabel());
		add(getTimeSpinner());

		add(getAssociateNumberLabel());
		add(getAssociateNumberComboBox());

		add(getActualProblemLabel());
		add(getActualProblemComboBox());

		add(getRepairMethodLabel());
		add(getRepairMethodComboBox());

		commentLabel = createCommentLabel();
		add(getCommentLabel());

		commentInputElement = createCommentInputElement();
		commentScrollPane = createCommentScrollPane();
		add(getCommentScrollPane());
		setComponentZOrder(getCommentScrollPane(), 0);
	}
	
	private void mapHandlers() {
		
		actualProblemComboBox.addActionListener(this);
		repairMethodComboBox.addActionListener(this);
		getTimeSpinner().addChangeListener(this);
		((NumberEditor) getTimeSpinner().getEditor()).getTextField().getDocument().addDocumentListener(this);
		((JTextComponent)getAssociateNumberComboBox().getEditor().getEditorComponent()).getDocument().addDocumentListener(this);
		getCommentInputElement().getDocument().addDocumentListener(this);
			
	}

	
	public void actionPerformed(ActionEvent e) {
		if(qicsPanel.getQicsController().getQicsPropertyBean().isDefectActualProbRepairMethodEnabled())
		{
			if(e.getSource().equals(actualProblemComboBox)) actualProblemChanged();
			else if(e.getSource().equals(repairMethodComboBox)) repairMethodChanged();
		}
	}
	
	
	
	private void actualProblemChanged() {
		
		try {
			qicsPanel.getQicsFrame().setWaitCursor();
			qicsPanel.getQicsFrame().clearMessage();

			getRepairMethodComboBox().setModel(new DefaultComboBoxModel());
			getTimeSpinner().setValue(0);

			DefectActualProblem problem = (DefectActualProblem) getActualProblemComboBox().getSelectedItem();
			List<DefectRepairMethod> repairMethods = null;

			if (problem == null) return;
			
			try {
				repairMethods = qicsPanel.getQicsController().selectRepairMethods(problem);
			} catch (SystemException ex) {
//				String messageId = ex.getMessageID();
//				getQicsFrame().setErrorMessage(messageId);
			}

			if (repairMethods == null) {
				return;
			}

			getRepairMethodComboBox().setModel(new DefaultComboBoxModel(repairMethods.toArray()));

			if (getRepairMethodComboBox().getItemCount() > 0) {
				getRepairMethodComboBox().setSelectedIndex(0);
			}
			repairInputDataChanged();
		} finally {
			qicsPanel.getQicsFrame().setDefaultCursor();
		}
		
	}
	
	private void repairMethodChanged() {
		
		try {
			qicsPanel.getQicsFrame().setWaitCursor();
			qicsPanel.getQicsFrame().clearMessage();

			getTimeSpinner().setValue(0);

			DefectRepairMethod repairMethod = (DefectRepairMethod) getRepairMethodComboBox().getSelectedItem();
			if (repairMethod == null) return;

			getTimeSpinner().setValue(repairMethod.getRepairTime());
			repairInputDataChanged();

		} finally {
			qicsPanel.getQicsFrame().setDefaultCursor();
		}

		
	}

	@Override
	public void setEnabled(boolean enabled) {

		getTimeSpinner().setEnabled(enabled);
		getAssociateNumberComboBox().setEnabled(enabled);
		getActualProblemComboBox().setEnabled(enabled);
		getRepairMethodComboBox().setEnabled(enabled);
		getCommentInputElement().setEnabled(enabled);
	}

	public void resetInput() {
		getAssociateNumberComboBox().setModel(new DefaultComboBoxModel());
		getActualProblemComboBox().setModel(new DefaultComboBoxModel());
		getRepairMethodComboBox().setModel(new DefaultComboBoxModel());
		getTimeSpinner().setValue(0);
		getCommentInputElement().setText("");
	}

	public void setInput(DefectResult repairResultData) {
		if (repairResultData == null) {
			return;
		}
		String associateId = repairResultData.getRepairAssociateNo() == null ? "" : repairResultData.getRepairAssociateNo();
//		String problemName = repairResultData.getActualProblem() == null ? "" : repairResultData.getActualProblem();
		String problemName = "";
		String repairMethod = repairResultData.getRepairMethodNamePlan() == null ? "" : repairResultData.getRepairMethodNamePlan();
//		String comment = repairResultData.getComment() == null ? "" : repairResultData.getComment();
		String comment = "";
		if(qicsPanel.getQicsController().getQicsPropertyBean().isDefectActualProbRepairMethodEnabled())
		{
			setActualProblem(problemName);
			setRepairMethod(repairMethod);
		}
		getTimeSpinner().setValue(repairResultData.getRepairTimePlan());
		getAssociateNumberComboBox().setSelectedItem(associateId);
		getCommentInputElement().setText(comment);
	}

	protected void setActualProblem(String problemName) {

		boolean exists = false;
		for (int i = 0; i < getActualProblemComboBox().getItemCount(); i++) {
			DefectActualProblem p = (DefectActualProblem) getActualProblemComboBox().getItemAt(i);
			if (problemName.equals(p.getId().getActualProblemName())) {
				getActualProblemComboBox().setSelectedIndex(i);
				exists = true;
				break;
			}
		}
		if (!exists) {
			DefectActualProblemId id=new DefectActualProblemId();
			DefectActualProblem temp = new DefectActualProblem();
			id.setActualProblemName(problemName);
			temp.setId(id);
			getActualProblemComboBox().addItem(temp);
			getActualProblemComboBox().setSelectedItem(temp);
		}
	}

	protected void addActionListeners(JComboBox comboBox, ActionListener[] actionListeners) {
		if (actionListeners == null || actionListeners.length == 0) {
			return;
		}
		for (ActionListener actionListener : actionListeners) {
			comboBox.addActionListener(actionListener);
		}
	}

	protected void setRepairMethod(String repairMethod) {
		boolean exists = false;
		for (int i = 0; i < getRepairMethodComboBox().getItemCount(); i++) {
			DefectRepairMethod r = (DefectRepairMethod) getRepairMethodComboBox().getItemAt(i);
			if (repairMethod.equals(r.getId().getRepairMethodName())) {
				getRepairMethodComboBox().setSelectedIndex(i);
				exists = true;
				break;
			}
		}
		if (!exists) {
			DefectRepairMethod temp = new DefectRepairMethod();
			DefectRepairMethodId id=new DefectRepairMethodId();
			id.setRepairMethodName(repairMethod);
			temp.setId(id);
			getRepairMethodComboBox().addItem(temp);
			getRepairMethodComboBox().setSelectedItem(temp);
		}
	}

	public boolean isInputDirty(DefectResult repairResultData) {
		boolean dirty = false;
		String problemName="";
		String methodName="";

		String associateNumber = ((JTextComponent) getAssociateNumberComboBox().getEditor().getEditorComponent()).getText();
		if (associateNumber == null) {
			associateNumber = "";
		}

		associateNumber = associateNumber == null ? "" : associateNumber;
        if(qicsPanel.getQicsController().getQicsPropertyBean().isDefectActualProbRepairMethodEnabled())
        {
    		DefectActualProblem actualProblem = (DefectActualProblem) getActualProblemComboBox().getSelectedItem();
    		DefectRepairMethod repairMethod = (DefectRepairMethod) getRepairMethodComboBox().getSelectedItem();
    		problemName = actualProblem == null ? "" : actualProblem.getId().getActualProblemName();
    		methodName = repairMethod == null ? "" : repairMethod.getId().getRepairMethodName();
        }else
        {
    		String actualProblem = (String) getActualProblemComboBox().getSelectedItem();
    		String repairMethod = (String) getRepairMethodComboBox().getSelectedItem();
    		problemName = actualProblem == null ? "" : actualProblem.toString();
    		methodName = repairMethod == null ? "" : repairMethod.toString();
        }
		int time = getTimeSpinner().getValue() == null ? 0 : Integer.valueOf(getTimeSpinner().getValue().toString());
		String comment = getCommentInputElement().getText().trim();

//		boolean problemNameUpdated = !problemName.trim().equals(repairResultData.getActualProblem());
		boolean problemNameUpdated = false;
		boolean methodNameUpdated = !methodName.trim().equals(repairResultData.getRepairMethodNamePlan());
		boolean timeUpdated = time != repairResultData.getRepairTimePlan();
		boolean repairAssociateUpdated = !associateNumber.equals(repairResultData.getRepairAssociateNo());
//		boolean commentUpdated = !StringUtils.equals(comment, repairResultData.getComment());
		boolean commentUpdated = false;
		dirty = timeUpdated || problemNameUpdated || methodNameUpdated || repairAssociateUpdated || commentUpdated;
		return dirty;
	}

	protected javax.swing.JLabel getActualProblemLabel() {
		if (actualProblemLabel == null) {
			actualProblemLabel =  createLabel("Actual Problem", Color.black, 130,10,15);
		}
		return actualProblemLabel;
	}
	
	private JLabel createLabel(String name,Color color, int width, int posx, int posy) {
		JLabel label = new JLabel(name);
		label.setFont(Fonts.DIALOG_PLAIN_18);
		label.setForeground(color);
		label.setSize(width,30);
		label.setLocation(posx, posy);
		return label;
	}

	public JComboBox getActualProblemComboBox() {
		if (actualProblemComboBox == null) {
			actualProblemComboBox = new JComboBox();
			actualProblemComboBox.setFont(Fonts.DIALOG_PLAIN_18);
			actualProblemComboBox.setName("actualProblemComboBox");
			actualProblemComboBox.setLocation(getActualProblemLabel().getX() + getActualProblemLabel().getWidth(), getActualProblemLabel().getY());
			int width = 360;
			actualProblemComboBox.setSize(width, 30);
			if(!qicsPanel.getQicsController().getQicsPropertyBean().isDefectActualProbRepairMethodEnabled())
			{
				actualProblemComboBox.setEditable(true);
			}

		}
		return actualProblemComboBox;
	}

	protected javax.swing.JLabel getRepairMethodLabel() {
		if (repairMethodLabel == null) {

			Component base = getActualProblemLabel();
			repairMethodLabel = createLabel("Repair Method",Color.black,130,base.getX(), base.getY() + base.getHeight() + 5);
		}
		return repairMethodLabel;
	}

	public JComboBox getRepairMethodComboBox() {
		if (repairMethodComboBox == null) {
			repairMethodComboBox = new JComboBox();
			repairMethodComboBox.setFont(Fonts.DIALOG_PLAIN_18);
			repairMethodComboBox.setName("repairMethodComboBox");
			
			repairMethodComboBox.setSize(getActualProblemComboBox().getWidth(), getActualProblemComboBox().getHeight());
			Component base = getRepairMethodLabel();
			repairMethodComboBox.setLocation(base.getX() + base.getWidth(), base.getY());
			if(!qicsPanel.getQicsController().getQicsPropertyBean().isDefectActualProbRepairMethodEnabled())
			{
			   repairMethodComboBox.setEditable(true);
			}
		}
		return repairMethodComboBox;
	}

	protected JLabel getTimeLabel() {
		if (timeLabel == null) {
			Component base = getActualProblemComboBox();
			timeLabel = createLabel("Time[min]",Color.black,100,base.getX() + base.getWidth() + 10, base.getY());
		}
		return timeLabel;
	}

	public JSpinner getTimeSpinner() {
		if (timeSpinner == null) {
			Component base = getTimeLabel();
			int maxValue = 9999;
			timeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, maxValue, 1));
			timeSpinner.setEnabled(false);
			timeSpinner.setFont(Fonts.DIALOG_PLAIN_18);
			((NumberEditor) timeSpinner.getEditor()).getTextField().setEditable(true);
			((NumberEditor) timeSpinner.getEditor()).getTextField().setBackground(Color.white);
			((DefaultFormatter) ((NumberEditor) timeSpinner.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);
			int width = getWidth() - base.getX() - base.getWidth() - 10;
			timeSpinner.setSize(width, 30);
			timeSpinner.setLocation(getTimeLabel().getWidth() + getTimeLabel().getX(), getTimeLabel().getY());
		}
		return timeSpinner;
	}

	protected JLabel getAssociateNumberLabel() {
		if (associateNumberLabel == null) {
			Component base = getTimeLabel();
			associateNumberLabel = createLabel("Associate ID",Color.black,100,base.getX(), base.getY() + base.getHeight() + 5);
		}
		return associateNumberLabel;
	}

	public JComboBox getAssociateNumberComboBox() {
		if (associateNumberComboBox == null) {
			Component base = getAssociateNumberLabel();
			associateNumberComboBox = new JComboBox();
			associateNumberComboBox.setName("associateNumberComboBox");
			associateNumberComboBox.setFont(Fonts.DIALOG_PLAIN_18);
			int width = getTimeSpinner().getWidth();
			associateNumberComboBox.setSize(width, 30);
			associateNumberComboBox.setEditable(true);

			associateNumberComboBox.setLocation(base.getX() + base.getWidth(), base.getY());
			JTextComponent editor = (JTextComponent) associateNumberComboBox.getEditor().getEditorComponent();
			editor.setDocument(new LimitedLengthPlainDocument(getAssociateNumberLength()));
		}
		return associateNumberComboBox;
	}

	protected JLabel createCommentLabel() {
		JLabel label = new JLabel("Comment", JLabel.LEFT);
		label.setFont(Fonts.DIALOG_PLAIN_18);
		label.setForeground(Color.black);
		label.setSize(130, 30);
		Component base = getRepairMethodLabel();
		label.setLocation(base.getX(), base.getY() + base.getHeight() + 5);
		return label;
	}

	public JScrollPane createCommentScrollPane() {

		int width = getCommentScrollPaneWidth();
		int h = getCommentScrollPaneHeight();

		JScrollPane editorScrollPane = new JScrollPane(getCommentInputElement());
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editorScrollPane.setSize(width, h);
		editorScrollPane.setLocation(getCommentLabel().getX() + getCommentLabel().getWidth(), getCommentLabel().getY());
		return editorScrollPane;
	}

	public JTextArea createCommentInputElement() {
		final JTextArea field = new JTextArea();
		field.setFont(Fonts.DIALOG_PLAIN_18);
		field.setDocument(new LimitedLengthPlainDocument(240));
		field.setWrapStyleWord(true);
		field.setLineWrap(true);
		return field;
	}

	// === get/set === //
	protected int getAssociateNumberLength() {
		return associateNumberLength;
	}

	protected int getCommentScrollPaneWidth() {
		return getWidth() - getCommentLabel().getX() - getCommentLabel().getWidth() - 10;
	}

	protected int getCommentScrollPaneHeight() {
		return 70;
	}

	public JLabel getCommentLabel() {
		return commentLabel;
	}

	public JTextArea getCommentInputElement() {
		return commentInputElement;
	}

	public JScrollPane getCommentScrollPane() {
		return commentScrollPane;
	}

	public void changedUpdate(DocumentEvent e) {
		repairInputDataChanged();
	}

	public void insertUpdate(DocumentEvent e) {
		repairInputDataChanged();
	}

	public void removeUpdate(DocumentEvent e) {
		repairInputDataChanged();
	}
	
	private void repairInputDataChanged() {
		
		qicsPanel.setAcceptButtonEnabled(qicsPanel.isInputDirty());
	}

	public void stateChanged(ChangeEvent e) {
		repairInputDataChanged();
	}


}
