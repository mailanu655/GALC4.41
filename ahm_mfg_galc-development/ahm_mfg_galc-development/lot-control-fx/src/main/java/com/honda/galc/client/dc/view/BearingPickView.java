package com.honda.galc.client.dc.view;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.processor.BearingPickProcessor;
import com.honda.galc.client.dc.processor.BearingSelectProcessor;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.event.KeypadEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.MCOperationRevision;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import net.miginfocom.layout.CC;

/**
 * 
 * <h3>BearingPickView</h3>
 * <h3>The class is a operation view to show bearing pick information. </h3>
 * <h4>  </h4>
 * <p> The operation processor for BearingPickView must be {@link BearingPickProcessor}. </p>
 * <p> The bearing pick view shows the information only when bearing select is done. see {@link BearingSelectView} and {@link BearingSelectProcessor}</p>
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
 * @see BearingPickProcessor
 * @see BearingSelectView
 * @see BearingSelectProcessor
 * @author Hale Xie
 * May 30, 2014
 *
 */

public class BearingPickView extends OperationView {
	
	private Map<Integer, TextField> blockMeasurementTextFields;
	private Map<Integer, TextField> crankMainMeasurementTextFields;

	private Map<Integer, TextField> mainUpperBearingTextFields;
	private Map<Integer, TextField> mainLowerBearingTextFields;

	private Map<Integer, TextField> crankConrodMeasurementTextFields;
	private Map<Integer, TextField> conrodMeasurementTextFields;

	private Map<Integer, TextField> conrodUpperBearingTextFields;
	private Map<Integer, TextField> conrodLowerBearingTextFields;
	
	private Button doneButton;
	private MigPane contentPane;
	
	/** The constant DONE_BUTTON_LABEL. */
	public static final String DONE_BUTTON_LABEL = "Done";
	
	/**
	 * Instantiates a new bearing pick view.
	 *
	 * @param processor the operation processor. it must be an instance of {@link BearingPickProcessor}.
	 */
	public BearingPickView(OperationProcessor processor) {
		this((BearingPickProcessor)processor);
	
	}
	
	/**
	 * Instantiates a new bearing pick view.
	 *
	 * @param processor the operation processor
	 */
	public BearingPickView(BearingPickProcessor processor) {
		super(processor);
		ViewControlUtil.cleanUpOldListerners(this);
		EventBusUtil.register(this);
	}
	
	/**
	 * Handle the unit navigator event.
	 *
	 * @param event the event
	 */
	@Subscribe
	public void handleUnitNavigatorEvent(UnitNavigatorEvent event) {
		getLogger().debug("BearingPickView.handleUnitNavigatorEvent: " + event.toString());
		List<MCOperationRevision> structures = this.getProcessor().getController().getModel().getOperations();
		int index = structures.indexOf(this.getProcessor().getOperation());
		if (event.getType().equals(UnitNavigatorEventType.SELECTED) && index == event.getIndex()) {
			//refresh the view when the work unit is selected.
			refreshView();
		}
	}
	
	/**
	 * Handle Clicker Complete event.
	 *
	 * @param event the KeypadEvent
	 */
	@Subscribe
	public void handle(KeypadEvent event) {
	
		switch (event.getEventType()) {
		    // Don't let the user reject an unit via the clicker
			case KEY_COMPLETE:
				if(getDoneButton()!=null && 
					getDoneButton().getText().equals(DONE_BUTTON_LABEL) &&
					getDoneButton().isDisabled() == false) {
					getDoneButton().fire();
				}
				break;
			default:
				break;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.view.AbstractDataCollectionWidget#refreshView()
	 */
	@Override
	public void refreshView(){
		getProcessor().start();
		Platform.runLater(new Runnable() {
			public void run() {
				contentPane.layout();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.view.OperationView#initComponents()
	 */
	@Override
	public void initComponents() {
		getProcessor().setView(this);
		this.blockMeasurementTextFields = new LinkedHashMap<Integer, TextField>();
		this.crankMainMeasurementTextFields = new LinkedHashMap<Integer, TextField>();
		this.crankConrodMeasurementTextFields = new LinkedHashMap<Integer, TextField>();
		this.conrodMeasurementTextFields = new LinkedHashMap<Integer, TextField>();

		this.mainUpperBearingTextFields = new LinkedHashMap<Integer, TextField>();
		this.mainLowerBearingTextFields = new LinkedHashMap<Integer, TextField>();
		this.conrodUpperBearingTextFields = new LinkedHashMap<Integer, TextField>();
		this.conrodLowerBearingTextFields = new LinkedHashMap<Integer, TextField>();
		int FONT_SIZE = getProcessor().getProperty().getFontSize();
		String title = StringUtils
				.isNotBlank(getProcessor().getProperty().getOperationTitles().get(getOperation().getCommonName()))
						? getProcessor().getProperty().getOperationTitles().get(getOperation().getCommonName())
						: getProcessor().getProperty().getOperationTitles()
								.get(getOperation().getId().getOperationName());
		title = StringUtils.trimToEmpty(title);
		contentPane = new MigPane("insets 14 5 0 5", "[center,grow,fill]", "");
		contentPane.add(UiFactory.createLabel("MainTitle", title, Fonts.SS_DIALOG_BOLD((int)(FONT_SIZE*1.2))), "wrap");
		this.setCenter(contentPane);
		createBearingSection();
		
		mapActions();
		Platform.runLater(new Runnable() {
			public void run() {
				refreshView();
			}
		});
	}
	
	/**
	 * Map UI actions to event handlers.
	 */
	public void mapActions() {
		
		EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				getProcessor().finish();
			}
		};
		getDoneButton().setOnAction(handler);
	}
	
	/**
	 * Gets the column width.
	 *
	 * @return the fixed column width
	 */
	protected int getFixedColumnWidth() {
		return 115;
	}
	
	/**
	 * Creates the bearing section.
	 */
	protected void createBearingSection() {
		UiFactory.addSeparator(getContentPane(), "Rankings");
		String constraintToken = "[max,fill]";
		StringBuilder mainLayout = new StringBuilder();

		List<Integer> mainBearingIxs = getProcessor().getMainBearingIxDisplaySequence();
		for (int i = 0; i < mainBearingIxs.size(); i++) {
			mainLayout.append(constraintToken);
		}
		String str = mainLayout.toString();
		StringBuilder doubleMainLayout = new StringBuilder(str).append(str);
		int w = getFixedColumnWidth() / 2;

		String layoutConstraints = "insets 0 0 0 0";
		String columnConstraints = String.format("[%s!,fill][%s!,fill]%s5", w, w, doubleMainLayout);

		StringBuilder layoutConrod = new StringBuilder();
		for (int i = 0; i < getProcessor().getConrodCount(); i++) {
			layoutConrod.append(constraintToken);
		}

		MigPane panel = new MigPane(layoutConstraints, columnConstraints, "");
		int startColIx = 2;
		int sectionStartRowIx = 0;
		addMainBearings(panel, startColIx, sectionStartRowIx, layoutConstraints);

		UiFactory.addSeparator(panel, "");
		sectionStartRowIx = 6;
		addConrodBearings(panel, startColIx, sectionStartRowIx, layoutConstraints, layoutConrod.toString());

		getContentPane().add(panel, "wrap 5");
	}

	public Map<Integer, TextField> getBlockMeasurementTextFields() {
		return blockMeasurementTextFields;
	}

	public Map<Integer, TextField> getCrankMainMeasurementTextFields() {
		return crankMainMeasurementTextFields;
	}

	public Map<Integer, TextField> getMainUpperBearingTextFields() {
		return mainUpperBearingTextFields;
	}

	public Map<Integer, TextField> getMainLowerBearingTextFields() {
		return mainLowerBearingTextFields;
	}

	public Map<Integer, TextField> getCrankConrodMeasurementTextFields() {
		return crankConrodMeasurementTextFields;
	}

	public Map<Integer, TextField> getConrodMeasurementTextFields() {
		return conrodMeasurementTextFields;
	}

	public Map<Integer, TextField> getConrodUpperBearingTextFields() {
		return conrodUpperBearingTextFields;
	}

	public Map<Integer, TextField> getConrodLowerBearingTextFields() {
		return conrodLowerBearingTextFields;
	}

	public Button getDoneButton() {
		return doneButton;
	}

	public MigPane getContentPane() {
		return contentPane;
	}
	
	private void createDoneButton() {
		if(getProcessor().getController().getModel().isCurrentOperationComplete()) {
			this.doneButton = UiFactory.getInfo().createButton("Reject", 
					getProcessor().getImageView(getProcessor().getRejectImage()));
		}
		else {
			this.doneButton = UiFactory.getInfo().createButton(DONE_BUTTON_LABEL, 
					getProcessor().getImageView(getProcessor().getDoneImage()));
		}
	}
	
	/**
	 * Adds the bearing ranks of UI components for Conrod to the screen.
	 *
	 * @param panel the panel
	 * @param startColIx the start col ix
	 * @param sectionStartRowIx the section start row ix
	 * @param layoutConstraints the layout constraints
	 * @param layoutConrod the layout conrod
	 */
	protected void addConrodBearings(MigPane panel, int startColIx, int sectionStartRowIx, String layoutConstraints, String layoutConrod) {

		createDoneButton();
		this.doneButton.defaultButtonProperty().bind(this.doneButton.focusedProperty());

		List<Integer> conrodIx = getProcessor().getConrodIxDisplaySequence();
		List<Integer> firingIx = getProcessor().getFiringIx();
		
		
		MigPane crankConPanel = new MigPane(layoutConstraints, layoutConrod.toString(), "");
		MigPane conrodPanel = new MigPane(layoutConstraints, layoutConrod.toString(), "");
		MigPane labelPanel = new MigPane(layoutConstraints, layoutConrod.toString(), "");
		MigPane lowerBearingPanel = new MigPane(layoutConstraints, layoutConrod.toString(), "");
		MigPane upperBearingPanel = new MigPane(layoutConstraints, layoutConrod.toString(), "");

		panel.add(UiFactory.getInfoSmall().createLabel("Conrod", "Conrod", TextAlignment.CENTER), new CC().cell(0, sectionStartRowIx).span(1, 2));
		panel.add(UiFactory.getDefault().createLabel("Crank", "Crank", TextAlignment.CENTER), new CC().cell(1, sectionStartRowIx));
		panel.add(UiFactory.getDefault().createLabel("Cons", "Cons", TextAlignment.CENTER), new CC().cell(1, sectionStartRowIx + 1));

		panel.add(UiFactory.getInfoSmall().createLabel("Bearing", "Bearing", TextAlignment.CENTER), new CC().cell(0, sectionStartRowIx + 3).span(1, 2));
		panel.add(UiFactory.getInput().createLabel("Rods", "Rods", TextAlignment.CENTER), new CC().cell(1, sectionStartRowIx + 3));
		panel.add(UiFactory.getInput().createLabel("Caps", "Caps", TextAlignment.CENTER), new CC().cell(1, sectionStartRowIx + 4));
		

		for (int i = 0; i < conrodIx.size(); i++) {
			int bearingIx = conrodIx.get(i);
			int cylFireIx = firingIx.get(i);
			TextField crankCon = UiFactory.createBearingMeasurementTextField("crankCon", getProcessor().getConrodCount());
			TextField conrod = UiFactory.createBearingMeasurementTextField("conrod", getProcessor().getConrodCount());

			Label ixLabel = UiFactory.getDefault().createLabel("ixLabel", String.valueOf(cylFireIx), TextAlignment.CENTER);

			TextField upper = UiFactory.createBearingColorTextField("upper", getProcessor().getConrodCount());
			TextField lower = UiFactory.createBearingColorTextField("lower", getProcessor().getConrodCount());

			crankConPanel.add(crankCon, new CC().cell(i, 0).height("max"));
			conrodPanel.add(conrod, new CC().cell(i, 0).height("max"));

			labelPanel.add(ixLabel, new CC().cell(i, 0).alignX("center"));

			upperBearingPanel.add(upper, new CC().cell(i, 0).height("max"));
			lowerBearingPanel.add(lower, new CC().cell(i, 0).height("max"));

			getCrankConrodMeasurementTextFields().put(bearingIx, crankCon);
			getConrodMeasurementTextFields().put(bearingIx, conrod);
			getConrodUpperBearingTextFields().put(bearingIx, upper);
			getConrodLowerBearingTextFields().put(bearingIx, lower);
		}
		int rowIx = sectionStartRowIx;
		panel.add(crankConPanel, new CC().span(getProcessor().getMainBearingCount() * 2 - 2, 1).cell(3, rowIx++));
		panel.add(conrodPanel, new CC().span(getProcessor().getMainBearingCount() * 2 - 2, 1).cell(3, rowIx++));
		panel.add(labelPanel, new CC().span(getProcessor().getMainBearingCount() * 2 - 2, 1).cell(3, rowIx++));
		panel.add(upperBearingPanel, new CC().span(getProcessor().getMainBearingCount() * 2 - 2, 1).cell(3, rowIx++));
		panel.add(lowerBearingPanel, new CC().span(getProcessor().getMainBearingCount() * 2 - 2, 1).cell(3, rowIx++));
		

		panel.add(getDoneButton(), new CC().cell(startColIx + getProcessor().getMainBearingCount() * 2 - 1, sectionStartRowIx + 5).span(1));
	}
	
	/**
	 *Adds the bearing ranks of UI components for Main Bearing to the screen.
	 *
	 * @param panel the panel
	 * @param startColIx the start col ix
	 * @param sectionStartRowIx the section start row ix
	 * @param layoutConstraints the layout constraints
	 */
	protected void addMainBearings(MigPane panel, int startColIx, int sectionStartRowIx, String layoutConstraints) {
		List<Integer> mainBearingIx = getProcessor().getMainBearingIxDisplaySequence();

		panel.add(UiFactory.getInfoSmall().createLabel("Journal", "Journal", TextAlignment.CENTER), new CC().cell(0, 0).span(1, 2));
		panel.add(UiFactory.getInput().createLabel("Block", "Block", TextAlignment.CENTER), "cell 1 0, height max");
		panel.add(UiFactory.getInput().createLabel("Crank", "Crank", TextAlignment.CENTER), "cell 1 1, height max");

		panel.add(UiFactory.getInfoSmall().createLabel("Bearing", "Bearing", TextAlignment.CENTER), new CC().cell(0, 3).span(1, 2));
		panel.add(UiFactory.getInput().createLabel("Upper", "Upper", TextAlignment.CENTER), "cell 1 3, height max");
		panel.add(UiFactory.getInput().createLabel("Lower", "Lower", TextAlignment.CENTER), "cell 1 4, height max");
		

		for (int i = 0; i < mainBearingIx.size(); i++) {
			int bearingIx = mainBearingIx.get(i);
			int colIx = startColIx + 2 * i;
			int rowIx = sectionStartRowIx;

			TextField mainBlock = UiFactory.createBearingMeasurementTextField("mainBlockTF", getProcessor().getConrodCount());
			TextField mainCrank = UiFactory.createBearingMeasurementTextField("mainCrankTF", getProcessor().getConrodCount());
			Label crankMainIxLabel = UiFactory.getDefault().createLabel("crankMainIxLabel", String.valueOf(bearingIx), TextAlignment.CENTER);
			
			TextField upper = UiFactory.createBearingColorTextField("upperConrodCountTF", getProcessor().getConrodCount());
			TextField lower = UiFactory.createBearingColorTextField("lowerConrodCountTF", getProcessor().getConrodCount());
			
			panel.add(mainBlock, new CC().cell(colIx, rowIx++).height("max").span(2, 1));
			panel.add(mainCrank, new CC().cell(colIx, rowIx++).height("max").span(2, 1));

			panel.add(crankMainIxLabel, new CC().cell(colIx, rowIx++).span(2, 1).alignX("center"));

			panel.add(upper, new CC().cell(colIx, rowIx++).height("max").span(2, 1));
			panel.add(lower, new CC().cell(colIx, rowIx++).height("max").span(2, 1).wrap());

			getBlockMeasurementTextFields().put(bearingIx, mainBlock);
			getCrankMainMeasurementTextFields().put(bearingIx, mainCrank);
			getMainUpperBearingTextFields().put(bearingIx, upper);
			getMainLowerBearingTextFields().put(bearingIx, lower);
		}
	}
	
	@Override
	public BearingPickProcessor getProcessor(){
		return (BearingPickProcessor) super.getProcessor();
	}
}