package com.honda.galc.client.qics.view.screen;

import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.entity.conf.Line;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */
public class RepairInPanel extends QicsPanel  {

	private static final long serialVersionUID = 1L;
	private JLabel repairLineLabel;
	private JComboBox repairLineComboBox;

	public RepairInPanel(QicsFrame frame) {
		super(frame);
		initialize();
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.REPAIR_IN;
	}

	@Override
	public void setButtonsState() {
		getQicsFrame().getMainPanel().setButtonsState();
	}

	protected void initialize() {
		initComponents();
		setLayout(null);
		setSize(getTabPaneWidth(), getTabPaneHeight());
		mapActions();
	}

	protected void initComponents() {
		JPanel panel = new JPanel();
		repairLineLabel = getRepairLineLabel();
		repairLineComboBox = getRepairLineComboBox();
		panel.add(getRepairLineLabel());
		panel.add(getRepairLineComboBox());
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.setLayout(null);
		panel.setLocation(225, 50);
		panel.setSize(500, 400);
		add(panel);
	}

	protected JLabel getRepairLineLabel() {
		if (repairLineLabel == null) {
			repairLineLabel = createLabel("Repair Line:", Color.black, 100, 50, 175);
		}
		return repairLineLabel;
	}

	public JComboBox getRepairLineComboBox() {
		if (repairLineComboBox == null) {
			repairLineComboBox = new JComboBox();
			repairLineComboBox.setName("repairLineComboBox");
			repairLineComboBox.setFont(Fonts.DIALOG_PLAIN_18);
			repairLineComboBox.setSize(250, 30);
			repairLineComboBox.setEditable(false);
			repairLineComboBox.setLocation(175, 175);
		}
		return repairLineComboBox;
	}

	private JLabel createLabel(String name, Color color, int width, int posx, int posy) {
		JLabel label = new JLabel(name);
		label.setFont(Fonts.DIALOG_PLAIN_18);
		label.setForeground(color);
		label.setSize(width, 30);
		label.setLocation(posx, posy);
		return label;
	}

	protected JLabel createCommentLabel() {
		JLabel label = new JLabel("Comments:", JLabel.LEFT);
		label.setFont(Fonts.DIALOG_PLAIN_18);
		label.setForeground(Color.black);
		label.setSize(130, 30);
		label.setLocation(50, 150);
		return label;
	}

	public JTextArea createCommentTextArea() {
		final JTextArea field = new JTextArea();
		field.setFont(Fonts.DIALOG_PLAIN_18);
		field.setWrapStyleWord(true);
		field.setLineWrap(true);
		return field;
	}

	@Override
	public void startPanel() {
		List<Line> lines = this.getQicsController().findEligibleRepairLines();
		getRepairLineComboBox().removeAllItems();
		getRepairLineComboBox().addItem("Select");
		for (Line line : lines) {
			getRepairLineComboBox().addItem(line.getLineName().trim().concat("(").concat(line.getLineId().trim()).concat(")"));
		}
		getRepairLineComboBox().setSelectedIndex(0);
	}

}
