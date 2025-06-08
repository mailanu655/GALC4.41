package com.honda.galc.client.product.process.engine.bearing.pick.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.process.engine.bearing.pick.controller.BearingPickController;
import com.honda.galc.client.product.process.engine.bearing.pick.model.BearingPickModel;
import com.honda.galc.client.product.process.view.ProcessView;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>BearingPickPanel</code> is ... .
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
public class BearingPickPanel extends ApplicationMainPanel implements ProcessView {

	private static final long serialVersionUID = 1L;

	private BearingPickController controller;

	private Map<Integer, JTextField> blockMeasurementTextFields;
	private Map<Integer, JTextField> crankMainMeasurementTextFields;

	private Map<Integer, JTextField> mainUpperBearingTextFields;
	private Map<Integer, JTextField> mainLowerBearingTextFields;

	private Map<Integer, JTextField> crankConrodMeasurementTextFields;
	private Map<Integer, JTextField> conrodMeasurementTextFields;

	private Map<Integer, JTextField> conrodUpperBearingTextFields;
	private Map<Integer, JTextField> conrodLowerBearingTextFields;

	private JButton doneButton;

	public BearingPickPanel(MainWindow window) {
		super(window);

		window.getApplicationContext().getProcessPointId();
		window.getApplication().getApplicationId();

		this.blockMeasurementTextFields = new LinkedHashMap<Integer, JTextField>();
		this.crankMainMeasurementTextFields = new LinkedHashMap<Integer, JTextField>();
		this.crankConrodMeasurementTextFields = new LinkedHashMap<Integer, JTextField>();
		this.conrodMeasurementTextFields = new LinkedHashMap<Integer, JTextField>();

		this.mainUpperBearingTextFields = new LinkedHashMap<Integer, JTextField>();
		this.mainLowerBearingTextFields = new LinkedHashMap<Integer, JTextField>();
		this.conrodUpperBearingTextFields = new LinkedHashMap<Integer, JTextField>();
		this.conrodLowerBearingTextFields = new LinkedHashMap<Integer, JTextField>();
		this.controller = new BearingPickController(this);

		if (!this.controller.getModel().getProperty().getUseBearingMatrixForBearingPick()) {
			 initView(); //The Bearing Pick Client needs the ability to dynamically switch between L4 or V6 depending on the engine model.
			 mapActions();
		}
	}

	public void initView() {

		setName("BearingPickPanel");
		setLayout(new MigLayout("insets 0 5 0 5", "[grow,fill]", ""));
		createBearingSection();
	}

	public void mapActions() {
		Action action = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			{
				putValue(NAME, "Done");
				putValue(MNEMONIC_KEY, KeyEvent.VK_D);
			}

			public void actionPerformed(ActionEvent e) {
				getController().finish();
			}
		};
		getDoneButton().setAction(action);
		getDoneButton().getInputMap().put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "done");
		getDoneButton().getActionMap().put("done", getDoneButton().getAction());
	}

	protected void createBearingSection() {
		UiFactory.addSeparator(this, "Rankings");
		String constraintToken = "[max,fill]";
		StringBuilder mainLayout = new StringBuilder();

		List<Integer> mainBearingIxs = getModel().getMainBearingIxDisplaySequence(controller.getBearingMatrix());
		for (int i = 0; i < mainBearingIxs.size(); i++) {
			mainLayout.append(constraintToken);
		}
		String str = mainLayout.toString();
		StringBuilder doubleMainLayout = new StringBuilder(str).append(str);
		int w = getFixedColumnWidth() / 2;

		String layoutConstraints = "insets 0 0 0 0";
		String columnConstraints = String.format("[%s!,fill][%s!,fill]%s5", w, w, doubleMainLayout);

		StringBuilder layoutConrod = new StringBuilder();
		for (int i = 0; i < getModel().getConrodCount(controller.getBearingMatrix()); i++) {
			layoutConrod.append(constraintToken);
		}

		MigLayout layout = new MigLayout(layoutConstraints, columnConstraints, "");
		JPanel panel = new JPanel(layout);

		int startColIx = 2;
		int sectionStartRowIx = 0;
		addMainBearings(panel, startColIx, sectionStartRowIx, layoutConstraints);

		UiFactory.addSeparator(panel, "");
		sectionStartRowIx = 6;
		addConrodBearings(panel, startColIx, sectionStartRowIx, layoutConstraints, layoutConrod.toString());

		add(panel, "wrap 5");
	}

	protected void addMainBearings(JPanel panel, int startColIx, int sectionStartRowIx, String layoutConstraints) {
		List<Integer> mainBearingIx = getModel().getMainBearingIxDisplaySequence(controller.getBearingMatrix());

		panel.add(UiFactory.getInfoSmall().createLabel("Journal", JLabel.CENTER), new CC().cell(0, 0).span(1, 2));
		panel.add(UiFactory.getInput().createLabel("Block", JLabel.CENTER), "cell 1 0, height max");
		panel.add(UiFactory.getInput().createLabel("Crank", JLabel.CENTER), "cell 1 1, height max");

		panel.add(UiFactory.getInfoSmall().createLabel("Bearing", JLabel.CENTER), new CC().cell(0, 3).span(1, 2));
		panel.add(UiFactory.getInput().createLabel("Lower", JLabel.CENTER), "cell 1 3, height max");
		panel.add(UiFactory.getInput().createLabel("Upper", JLabel.CENTER), "cell 1 4, height max");

		for (int i = 0; i < mainBearingIx.size(); i++) {
			int bearingIx = mainBearingIx.get(i);
			int colIx = startColIx + 2 * i;
			int rowIx = sectionStartRowIx;

			JTextField mainBlock = UiFactory
					.createBearingMeasurementTextField(getModel().getConrodCount(controller.getBearingMatrix()));
			JTextField mainCrank = UiFactory
					.createBearingMeasurementTextField(getModel().getConrodCount(controller.getBearingMatrix()));
			JLabel crankMainIxLabel = UiFactory.getDefault().createLabel(String.valueOf(bearingIx), JLabel.CENTER);

			JTextField upper = UiFactory
					.createBearingColorTextField(getModel().getConrodCount(controller.getBearingMatrix()));
			JTextField lower = UiFactory
					.createBearingColorTextField(getModel().getConrodCount(controller.getBearingMatrix()));

			panel.add(mainBlock, new CC().cell(colIx, rowIx++).height("max").span(2, 1));
			panel.add(mainCrank, new CC().cell(colIx, rowIx++).height("max").span(2, 1));

			panel.add(crankMainIxLabel, new CC().cell(colIx, rowIx++).span(2, 1));

			panel.add(lower, new CC().cell(colIx, rowIx++).height("max").span(2, 1));
			panel.add(upper, new CC().cell(colIx, rowIx++).height("max").span(2, 1).wrap());

			getBlockMeasurementTextFields().put(bearingIx, mainBlock);
			getCrankMainMeasurementTextFields().put(bearingIx, mainCrank);
			getMainUpperBearingTextFields().put(bearingIx, upper);
			getMainLowerBearingTextFields().put(bearingIx, lower);
		}
	}

	protected void addConrodBearings(JPanel panel, int startColIx, int sectionStartRowIx, String layoutConstraints,
			String layoutConrod) {

		this.doneButton = UiFactory.getInfo().createButton("");

		List<Integer> conrodIx = getModel().getConrodIxDisplaySequence(controller.getBearingMatrix());

		JPanel crankConPanel = new JPanel(new MigLayout(layoutConstraints, layoutConrod.toString(), ""));
		JPanel conrodPanel = new JPanel(new MigLayout(layoutConstraints, layoutConrod.toString(), ""));
		JPanel labelPanel = new JPanel(new MigLayout(layoutConstraints, layoutConrod.toString(), ""));
		JPanel lowerBearingPanel = new JPanel(new MigLayout(layoutConstraints, layoutConrod.toString(), ""));
		JPanel upperBearingPanel = new JPanel(new MigLayout(layoutConstraints, layoutConrod.toString(), ""));

		panel.add(UiFactory.getInfoSmall().createLabel("Conrod", JLabel.CENTER),
				new CC().cell(0, sectionStartRowIx).span(1, 2));
		panel.add(UiFactory.getDefault().createLabel("Crank", JLabel.CENTER), new CC().cell(1, sectionStartRowIx));
		panel.add(UiFactory.getDefault().createLabel("Cons", JLabel.CENTER), new CC().cell(1, sectionStartRowIx + 1));

		panel.add(UiFactory.getInfoSmall().createLabel("Bearing", JLabel.CENTER),
				new CC().cell(0, sectionStartRowIx + 3).span(1, 2));
		panel.add(UiFactory.getInput().createLabel("Caps", JLabel.CENTER), new CC().cell(1, sectionStartRowIx + 3));
		panel.add(UiFactory.getInput().createLabel("Rods", JLabel.CENTER), new CC().cell(1, sectionStartRowIx + 4));

		for (int i = 0; i < conrodIx.size(); i++) {
			int bearingIx = conrodIx.get(i);

			JTextField crankCon = UiFactory
					.createBearingMeasurementTextField(getModel().getConrodCount(controller.getBearingMatrix()));
			JTextField conrod = UiFactory
					.createBearingMeasurementTextField(getModel().getConrodCount(controller.getBearingMatrix()));

			JLabel ixLabel = UiFactory.getDefault().createLabel(String.valueOf(bearingIx), JLabel.CENTER);

			JTextField upper = UiFactory
					.createBearingColorTextField(getModel().getConrodCount(controller.getBearingMatrix()));
			JTextField lower = UiFactory
					.createBearingColorTextField(getModel().getConrodCount(controller.getBearingMatrix()));

			crankConPanel.add(crankCon, new CC().cell(i, 0).height("max"));
			conrodPanel.add(conrod, new CC().cell(i, 0).height("max"));

			labelPanel.add(ixLabel, new CC().cell(i, 0));

			lowerBearingPanel.add(lower, new CC().cell(i, 0).height("max"));
			upperBearingPanel.add(upper, new CC().cell(i, 0).height("max"));

			getCrankConrodMeasurementTextFields().put(bearingIx, crankCon);
			getConrodMeasurementTextFields().put(bearingIx, conrod);
			getConrodUpperBearingTextFields().put(bearingIx, upper);
			getConrodLowerBearingTextFields().put(bearingIx, lower);
		}
		int rowIx = sectionStartRowIx;
		panel.add(crankConPanel, new CC().span(getModel().getMainBearingCount(controller.getBearingMatrix()) * 2 - 2, 1)
				.cell(3, rowIx++));
		panel.add(conrodPanel, new CC().span(getModel().getMainBearingCount(controller.getBearingMatrix()) * 2 - 2, 1)
				.cell(3, rowIx++));
		panel.add(labelPanel, new CC().span(getModel().getMainBearingCount(controller.getBearingMatrix()) * 2 - 2, 1)
				.cell(3, rowIx++));
		panel.add(lowerBearingPanel, new CC()
				.span(getModel().getMainBearingCount(controller.getBearingMatrix()) * 2 - 2, 1).cell(3, rowIx++));
		panel.add(upperBearingPanel, new CC()
				.span(getModel().getMainBearingCount(controller.getBearingMatrix()) * 2 - 2, 1).cell(3, rowIx++));

		panel.add(getDoneButton(),
				new CC().cell(startColIx + getModel().getMainBearingCount(controller.getBearingMatrix()) * 2 - 1,
						sectionStartRowIx + 5).span(1));
	}

	// === get/set === //
	public BearingPickController getController() {
		return controller;
	}

	public Map<Integer, JTextField> getBlockMeasurementTextFields() {
		return blockMeasurementTextFields;
	}

	public Map<Integer, JTextField> getCrankMainMeasurementTextFields() {
		return crankMainMeasurementTextFields;
	}

	public Map<Integer, JTextField> getCrankConrodMeasurementTextFields() {
		return crankConrodMeasurementTextFields;
	}

	public Map<Integer, JTextField> getConrodMeasurementTextFields() {
		return conrodMeasurementTextFields;
	}

	protected int getFixedColumnWidth() {
		return 115;
	}

	public Map<Integer, JTextField> getMainUpperBearingTextFields() {
		return mainUpperBearingTextFields;
	}

	public Map<Integer, JTextField> getMainLowerBearingTextFields() {
		return mainLowerBearingTextFields;
	}

	public Map<Integer, JTextField> getConrodUpperBearingTextFields() {
		return conrodUpperBearingTextFields;
	}

	public Map<Integer, JTextField> getConrodLowerBearingTextFields() {
		return conrodLowerBearingTextFields;
	}

	protected BearingPickModel getModel() {
		return getController().getModel();
	}

	public JButton getDoneButton() {
		return doneButton;
	}
}
