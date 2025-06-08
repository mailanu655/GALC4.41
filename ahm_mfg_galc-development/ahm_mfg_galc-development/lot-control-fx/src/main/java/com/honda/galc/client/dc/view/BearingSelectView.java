package com.honda.galc.client.dc.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.processor.BearingSelectProcessor;
import com.honda.galc.client.dc.processor.BearingSelectUiState;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.validator.BearingSelectValidator;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.KeypadEvent;
import com.honda.galc.client.ui.event.ProductStartedEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.entity.conf.MCOperationRevision;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextAlignment;
import net.miginfocom.layout.CC;

/**
 * <h3>BearingSelectView</h3>
 * <h3>The class is the  operation view for the bearing select  </h3>
 * <h4>  </h4>
 * <p> The operation processor for BearingPickView must be {@link BearingSelectProcessor}.</p>
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
 * </TABLE>.
 *
 * @see BearingSelectProcessor
 * @author Hale Xie
 * May 30, 2014
 */
public class BearingSelectView extends OperationView {

	/**
	 * The listener interface for receiving bearing select events.
	 * The class that is interested in processing a bearing select
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addBearingSelectListener<code> method. When
	 * the bearing select event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see BearingSelectEvent
	 */
	
	private BearingSelectUiState uiState = new BearingSelectUiState();
	
	protected static class BearingSelectListener implements
			EventHandler<ActionEvent> {
		
		/** The view. */
		private BearingSelectView view;

		/**
		 * Instantiates a new bearing select listener.
		 *
		 * @param view the view
		 */
		public BearingSelectListener(BearingSelectView view) {
			this.view = view;
		}

		/**
		 * Edits the measurements.
		 *
		 * @param fields the fields
		 * @param button the button
		 */
		protected void editMeasurements(List<TextField> fields, Button button) {
			if (fields == null) {
				return;
			}
			UiUtils.setState(fields, TextFieldState.EDIT);
			if (button != null) {
				button.setDisable(true);
			}
			getView().getProcessor().getController()
					.setFocusComponent(fields.get(0));
		}

		/**
		 * Gets the view.
		 *
		 * @return the view
		 */
		public BearingSelectView getView() {
			return view;
		}

		/* (non-Javadoc)
		 * @see javafx.event.EventHandler#handle(javafx.event.Event)
		 */
		public void handle(ActionEvent event) {

			getView().getProcessor().preExecute();

			if (getView().getBlockMeasurementsEditButton().equals(
					event.getSource())) {
				editMeasurements(getView()
						.getBlockMeasurementTextFieldsAsList(), getView()
						.getBlockMeasurementsEditButton());
			} else if (getView().getCrankMainMeasurementsEditButton().equals(
					event.getSource())) {
				editMeasurements(getView()
						.getCrankMainMeasurementTextFieldsAsList(), getView()
						.getCrankMainMeasurementsEditButton());
			} else if (getView().getCrankConrodMeasurementsEditButton().equals(
					event.getSource())) {
				editMeasurements(getView()
						.getCrankConrodMeasurementTextFieldsAsList(), getView()
						.getCrankConrodMeasurementsEditButton());
			} else if (getView().getConrodMeasurementsEditButton().equals(
					event.getSource())) {
				editMeasurements(getView()
						.getConrodMeasurementTextFieldsAsList(), getView()
						.getConrodMeasurementsEditButton());
			}

			getView().getProcessor().postExecute();
			event.consume();
		}

	}

	/**
	 * The listener interface for receiving measurementChange events.
	 * The class that is interested in processing a measurementChange
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMeasurementChangeListener<code> method. When
	 * the measurementChange event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MeasurementChangeEvent
	 */
	protected static class MeasurementChangeListener implements EventHandler<KeyEvent>, ChangeListener<String> {
		
		/** The view. */
		private BearingSelectView view;
		
		/** The text field. */
		private TextField textField;
		
		/** The button. */
		private Button button;
		
		/** The completed. */
		private boolean completed;
		
		private int maxLength = 1;
		
		/**
		 * Checks if is completed.
		 *
		 * @return true, if is completed
		 */
		public boolean isCompleted() {
			return completed;
		}
		
		/**
		 * Sets the completed.
		 *
		 * @param completed the new completed
		 */
		public void setCompleted(boolean completed) {
			this.completed = completed;
		}
		
		/**
		 * Checks if is button toggable.
		 *
		 * @return true, if is button toggable
		 */
		public boolean isButtonToggable() {
			return buttonToggable;
		}
		
		/** The button toggable. */
		private boolean buttonToggable;
		
		/**
		 * Instantiates a new measurement change listener.
		 *
		 * @param view the view
		 * @param textField the text field
		 * @param button the button
		 * @param buttonToggable the button toggable
		 */
		public MeasurementChangeListener(BearingSelectView view,
				TextField textField, Button button, boolean buttonToggable, int maxLength) {
			super();
			this.view = view;
			this.textField = textField;
			this.button = button;
			this.buttonToggable = buttonToggable;
			this.maxLength = maxLength;
		}
		
		/**
		 * Gets the button.
		 *
		 * @return the button
		 */
		public Button getButton() {
			return button;
		}
		
		/**
		 * Gets the view.
		 *
		 * @return the view
		 */
		public BearingSelectView getView() {
			return view;
		}
		
		/**
		 * Gets the text field.
		 *
		 * @return the text field
		 */
		public TextField getTextField() {
			return textField;
		}
		
		/* (non-Javadoc)
		 * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
		 */
		public void changed(ObservableValue<? extends String> observable,
				String oldValue, String newValue) {
			getView().getProcessor().preExecute();
			
			if ((newValue != null && newValue.length() > 0)|| (oldValue!=null && oldValue.length()>0)) {
				if (newValue.length() == this.maxLength) {
					processInput();
					getView().getProcessor().postExecute();	
				}
			} else {
				getView().getProcessor().postExecute();
			}
		}
		
		/* (non-Javadoc)
		 * @see javafx.event.EventHandler#handle(javafx.event.Event)
		 */
		public void handle(KeyEvent event) {
			KeyCode kc = event.getCode();
			if((StringUtils.equalsIgnoreCase(event.getText(), getTextField().getText()) && getTextField().getText().length() >= this.maxLength) 
					|| kc==KeyCode.ENTER || kc == KeyCode.TAB){
				getView().getProcessor().preExecute();
				
				processInput();
				getView().getProcessor().postExecute();	
				
				event.consume();
			}
		}
		
		/**
		 * Process input.
		 */
		public void processInput() {
			if (completed) {
				return;
			}
			getView().getProcessor().validate(getTextField());
			if (getView().getProcessor().getController().isErrorExists()) {
				getView().getProcessor().getController().setFocusComponent(getTextField());
			} else {
				TextFieldState.READ_ONLY.setState(getTextField());
				getView()
						.getProcessor()
						.getController()
						.setFocusComponent(getView().getProcessor().getNextFocusableTextField(getTextField()));
				if (getButton() != null 
						&& isButtonToggable() 
						&& getButton().isDisable()) {
					getButton().setDisable(false);
				}
				completed=true;
			}
		}
	}
	
	/**
	 * The listener interface for receiving measurement events.
	 * The class that is interested in processing a measurement
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMeasurementListener<code> method. When
	 * the measurement event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MeasurementEvent
	 */
	protected static class MeasurementListener implements
			 ChangeListener<Boolean> {
		
		/** The view. */
		private BearingSelectView view;
		
		/** The text field. */
		private TextField textField;
		
		/** The change listener. */
		private MeasurementChangeListener changeListener;
		
		/**
		 * Gets the change listener.
		 *
		 * @return the change listener
		 */
		public MeasurementChangeListener getChangeListener() {
			return changeListener;
		}

		/**
		 * Instantiates a new measurement listener.
		 *
		 * @param view the view
		 * @param textField the text field
		 * @param button the button
		 * @param buttonToggable the button toggable
		 */
		public MeasurementListener(BearingSelectView view,
				TextField textField, Button button, boolean buttonToggable, int maxLength) {
			this.view = view;
			this.textField = textField;
			changeListener=new MeasurementChangeListener(view, textField, button, buttonToggable, maxLength);
		}

		/* (non-Javadoc)
		 * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
		 */
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			if (newValue) {
				getTextField().selectAll();
				getChangeListener().setCompleted(false);
				getTextField().setOnKeyPressed(getChangeListener());
				getTextField().textProperty().addListener(getChangeListener());
			} else {
				getTextField().setOnKeyPressed(null);
				getTextField().textProperty().removeListener(getChangeListener());
			}
		}

		/**
		 * Gets the text field.
		 *
		 * @return the text field
		 */
		public TextField getTextField() {
			return textField;
		}

		// === get/set === //
		/**
		 * Gets the validator.
		 *
		 * @return the validator
		 */
		protected BearingSelectValidator getValidator() {
			return getView().getProcessor().getValidator();
		}

		/**
		 * Gets the view.
		 *
		 * @return the view
		 */
		public BearingSelectView getView() {
			return view;
		}

		
	}

	/** The Constant EDIT_ACTION_LABEL. */
	public static final String EDIT_ACTION_LABEL = "Edit";
	
	/** The Constant RECOVER_ACTION_LABEL. */
	public static final String RECOVER_ACTION_LABEL = "Recover";
	
	/** The Constant EDIT_ACTION_COMMAND. */
	public static final String EDIT_ACTION_COMMAND = "edit";
	
	/** The Constant RECOVER_ACTION_COMMAND. */
	public static final String RECOVER_ACTION_COMMAND = "recover";
	
	/** The constant DONE_BUTTON_LABEL. */
	public static final String DONE_BUTTON_LABEL = "Done";

	/** The block measurements edit button. */
	private Button blockMeasurementsEditButton;
	
	/** The crank main measurements edit button. */
	private Button crankMainMeasurementsEditButton;
	
	/** The crank conrod measurements edit button. */
	private Button crankConrodMeasurementsEditButton;
	
	/** The conrod measurements edit button. */
	private Button conrodMeasurementsEditButton;

	/** The done button. */
	private Button doneButton;
	
	/** The content pane. */
	private MigPane contentPane;

	/** The block measurement text fields. */
	private Map<Integer, TextField> blockMeasurementTextFields;
	
	/** The crank main measurement text fields. */
	private Map<Integer, TextField> crankMainMeasurementTextFields;

	/** The crank conrod measurement text fields. */
	private Map<Integer, TextField> crankConrodMeasurementTextFields;
	
	/** The conrod measurement text fields. */
	private Map<Integer, TextField> conrodMeasurementTextFields;

	/**
	 * Instantiates a new bearing select view.
	 *
	 * @param processor the processor
	 */
	public BearingSelectView(BearingSelectProcessor processor) {
		super(processor);
		BearingSelectValidator validator = new BearingSelectValidator(this);
		processor.setValidator(validator);
		mapActions();
		ViewControlUtil.cleanUpOldListerners(this);
		EventBusUtil.register(this);
	}
	
	/**
	 * Start to process bearing select.
	 */
	protected void startProcess(){
		getProcessor().start();
	}
	
	
	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.view.AbstractDataCollectionWidget#refreshView()
	 */
	@Override
	public void refreshView(){
		startProcess();
		Platform.runLater(new Runnable() {
			public void run() {
				contentPane.layout();
			}
		});
	}
	
	@Override
	public void resetView(){
		uiState.clear(this);
		uiState.reset(this);
		getProcessor().reset();
	}
	
	/**
	 * Instantiates a new bearing select view.
	 *
	 * @param processor the processor
	 */
	public BearingSelectView(OperationProcessor processor) {
		this((BearingSelectProcessor) processor);
	}
	
	/**
	 * Handle unit navigator event.
	 *
	 * @param event the event
	 */
	@Subscribe
	public void handleUnitNavigatorEvent(UnitNavigatorEvent event) {
		getLogger().debug("BearingSelectView.handleUnitNavigatorEvent: " + event.toString());
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
	
	/**
	 * Handle product started event.
	 *
	 * @param event the event
	 */
	@Subscribe
	public void handleProductStartedEvent(ProductStartedEvent event){
		//Reset the view when a new product id is entered;
		resetView();
	}

	/**
	 * Adds the block measurements.
	 *
	 * @param panel the panel
	 * @param startColIx the start col ix
	 * @param sectionStartRowIx the section start row ix
	 * @param layoutConstraints the layout constraints
	 */
	protected void addBlockMeasurements(MigPane panel, int startColIx,
			int sectionStartRowIx, String layoutConstraints) {
		List<Integer> blockIx = getProcessor().getBlockMainIxDisplaySequence();
		panel.add(
				UiFactory.getInfoSmall().createLabel("Block", "Block",
						TextAlignment.CENTER), new CC().cell(0, 1).span(2, 1)
						.height("max"));
		for (int i = 0; i < blockIx.size(); i++) {
			int bearingIx = blockIx.get(i);
			int colIx = startColIx + 2 * i;
			int rowIx = sectionStartRowIx;
			Label mainIxLabel = UiFactory.getDefault().createLabel("mainIxLabel", String.valueOf(bearingIx), TextAlignment.CENTER);
			TextField blockMain = UiFactory.createBearingMeasurementTextField("blockMain", getProcessor().getConrodCount());
			blockMain.setId("Block Rank "+ i);
			panel.add(mainIxLabel, new CC().cell(colIx, rowIx++).span(2, 1)
					.alignX("center"));
			panel.add(blockMain, new CC().cell(colIx, rowIx++).span(2, 1)
					.height("40:40:40"));
			getBlockMeasurementTextFields().put(bearingIx, blockMain);
		}
		panel.add(
				getBlockMeasurementsEditButton(),
				new CC().cell(
						startColIx + getProcessor().getMainBearingCount() * 2,
						sectionStartRowIx + 1).grow().wrap());
	}
	
	private void createDoneButton() {
		if(getProcessor().isAlreadyProcessed()) {
			this.doneButton = UiFactory.getInfo().createButton("Reject", 
					getProcessor().getImageView(getProcessor().getRejectImage()));
		}
		else {
			this.doneButton = UiFactory.getInfo().createButton(DONE_BUTTON_LABEL, 
					getProcessor().getImageView(getProcessor().getDoneImage()));
		}
	}

	/**
	 * Adds the conrod measurements.
	 *
	 * @param panel the panel
	 * @param sectionStartRowIx the section start row ix
	 * @param layoutConstraints the layout constraints
	 * @param layoutConrod the layout conrod
	 */
	protected void addConrodMeasurements(MigPane panel, int sectionStartRowIx,
			String layoutConstraints, String layoutConrod) {
		this.conrodMeasurementsEditButton = UiFactory.getInput().createButton("Edit", "conrodMeasurementsEditButton");
		createDoneButton();
		
		this.doneButton.defaultButtonProperty().bind(this.doneButton.focusedProperty());
		List<Integer> conrodIx = getProcessor().getConrodIxDisplaySequence();
		List<Integer> firingIx = getProcessor().getFiringIx();
		panel.add(
				UiFactory.getInfoSmall().createLabel("Conrod", "Conrod", TextAlignment.CENTER),
				new CC().cell(0, sectionStartRowIx + 0).span(2, 1));
		MigPane conrodPanel = new MigPane(layoutConstraints, layoutConrod, "");
		MigPane conrodLabelPanel = new MigPane(layoutConstraints, layoutConrod,
				"");
		for (int i = 0; i < conrodIx.size(); i++) {
			int bearingIx = conrodIx.get(i);
			int cylFireIx = firingIx.get(i);
			TextField conrod = UiFactory.createBearingMeasurementTextField("conrod", getProcessor().getConrodCount());
			conrod.setId("ConRod Rank "+ i);
			Label conrodIxLabel = UiFactory.getDefault().createLabel("conrodIxLabel", String.valueOf(cylFireIx), TextAlignment.CENTER);
			conrodPanel.add(conrod, new CC().cell(i, 0).height("40:40:40"));
			conrodLabelPanel.add(conrodIxLabel,
					new CC().cell(i, 0).alignX("center"));
			getConrodMeasurementTextFields().put(bearingIx, conrod);
		}

		panel.add(conrodLabelPanel,
				new CC().span(getProcessor().getMainBearingCount() * 2 - 2, 1)
						.cell(3, sectionStartRowIx + 1));
		panel.add(conrodPanel,
				new CC().span(getProcessor().getMainBearingCount() * 2 - 2, 1)
						.cell(3, sectionStartRowIx + 0));

		panel.add(
				getConrodMeasurementsEditButton(),
				new CC().cell(getProcessor().getMainBearingCount() * 2 + 2,
						sectionStartRowIx + 0).grow().height("max"));
		panel.add(getDoneButton(), new CC().cell(getProcessor()
				.getMainBearingCount() * 2 + 2, sectionStartRowIx + 1));
	}

	/**
	 * Adds the crank conrod measurements.
	 *
	 * @param panel the panel
	 * @param sectionStartRowIx the section start row ix
	 * @param layoutConstraints the layout constraints
	 * @param layoutConrod the layout conrod
	 */
	protected void addCrankConrodMeasurements(MigPane panel,
			int sectionStartRowIx, String layoutConstraints, String layoutConrod) {
		List<Integer> crankConrodIx = getProcessor().getCrankConrodIxDisplaySequence();
		List<Integer> firingIx = getProcessor().getFiringIx();
		MigPane crankConPanel = new MigPane(layoutConstraints,
				layoutConrod.toString(), "");
		MigPane crankConLabelPanel = new MigPane(layoutConstraints,
				layoutConrod.toString(), "");
		for (int i = 0; i < crankConrodIx.size(); i++) {
			int bearingIx = crankConrodIx.get(i);
			int cylFireIx = firingIx.get(i);
			TextField crankCon = UiFactory.createBearingMeasurementTextField("crankCon", getProcessor().getConrodCount());
			crankCon.setId("Crank ConRod Rank "+i);
			Label crankConIxLabel = UiFactory.getDefault().createLabel("crankConIxLabel", 
					String.valueOf(cylFireIx), TextAlignment.CENTER);
			crankConPanel.add(crankCon, new CC().cell(i, 0).height("40:40:40"));
			crankConLabelPanel.add(crankConIxLabel,
					new CC().cell(i, 0).height("max").alignX("center"));
			getCrankConrodMeasurementTextFields().put(bearingIx, crankCon);
		}

		panel.add(crankConPanel,
				new CC().span(getProcessor().getMainBearingCount() * 2 - 2, 1)
						.cell(3, sectionStartRowIx + 2));
		panel.add(
				getCrankConrodMeasurementsEditButton(),
				new CC().cell(getProcessor().getMainBearingCount() * 2 + 2,
						sectionStartRowIx + 2).grow());
		panel.add(crankConLabelPanel,
				new CC().span(getProcessor().getMainBearingCount() * 2 - 2, 1)
						.cell(3, sectionStartRowIx + 3).wrap());
	}

	/**
	 * Adds the crank journal measurements.
	 *
	 * @param panel the panel
	 * @param startColIx the start col ix
	 * @param sectionStartRowIx the section start row ix
	 * @param layoutConstraints the layout constraints
	 */
	protected void addCrankJournalMeasurements(MigPane panel, int startColIx,
			int sectionStartRowIx, String layoutConstraints) {
		this.crankMainMeasurementsEditButton = UiFactory.getInput().createButton("Edit", "crankMainMeasurementsEditButton");
		this.crankConrodMeasurementsEditButton = UiFactory.getInput().createButton("Edit", "crankConrodMeasurementsEditButton");

		List<Integer> crankMainIx = getProcessor()
				.getCrankMainIxDisplaySequence();

		panel.add(
				UiFactory.getInfoSmall().createLabel("Crank", "Crank",
						TextAlignment.CENTER),
				new CC().cell(0, sectionStartRowIx + 1).span(1, 2));
		panel.add(
				UiFactory.getInput().createLabel("Journal", "Journal",
						TextAlignment.CENTER),
				new CC().cell(1, sectionStartRowIx + 1).height("max"));
		panel.add(
				UiFactory.getInput().createLabel("Cons", "Cons", TextAlignment.CENTER),
				new CC().cell(1, sectionStartRowIx + 2).height("max")
						.alignX("center"));

		for (int i = 0; i < crankMainIx.size(); i++) {
			int bearingIx = crankMainIx.get(i);
			int colIx = startColIx + 2 * i;
			int rowIx = sectionStartRowIx;
			Label mainIxLabel = UiFactory.getDefault().createLabel("mainIxLabel", 
					String.valueOf(bearingIx), TextAlignment.CENTER);
			TextField crankMain = UiFactory.createBearingMeasurementTextField("crankMain", getProcessor().getConrodCount());
			crankMain.setId("Crank Rank "+i);
			panel.add(mainIxLabel, new CC().cell(colIx, rowIx++).span(2, 1)
					.alignX("center"));
			panel.add(crankMain, new CC().cell(colIx, rowIx++).span(2, 1)
					.height("40:40:40"));
			getCrankMainMeasurementTextFields().put(bearingIx, crankMain);
		}
		panel.add(
				getCrankMainMeasurementsEditButton(),
				new CC().cell(
						startColIx + getProcessor().getMainBearingCount() * 2,
						sectionStartRowIx + 1).grow().wrap());

	}

	/**
	 * Creates the measurement section.
	 */
	protected void createMeasurementSection() {

		this.blockMeasurementsEditButton = UiFactory.getInput().createButton("Edit", "blockMeasurementsEditButton");

		UiFactory.addSeparator(contentPane, "Rankings");
		String constraintToken = "[max,fill]";
		StringBuilder mainLayout = new StringBuilder();
		for (int i = 0; i < getProcessor().getMainBearingCount(); i++) {
			mainLayout.append(constraintToken);
		}
		String str = mainLayout.toString();
		StringBuilder doubleMainLayout = new StringBuilder(str).append(str);
		int w = getFixedColumnWidth() / 2;

		String layoutConstraints = "insets 0 0 0 0";
		String columnConstraints = String.format(
				"[%s!,fill][%s!,fill]%s[%s!,fill]5", w, w, doubleMainLayout,
				getFixedColumnWidth());

		StringBuilder layoutConrod = new StringBuilder();
		for (int i = 0; i < getProcessor().getConrodCount(); i++) {
			layoutConrod.append(constraintToken);
		}

		MigPane panel = new MigPane(layoutConstraints, columnConstraints, "");

		int startColIx = 2;
		int sectionStartRowIx = 0;

		addBlockMeasurements(panel, startColIx, sectionStartRowIx,
				layoutConstraints);
		UiFactory.addSeparator(panel, "");

		sectionStartRowIx = 3;
		addCrankJournalMeasurements(panel, startColIx, sectionStartRowIx,
				layoutConstraints);

		addCrankConrodMeasurements(panel, sectionStartRowIx, layoutConstraints,
				layoutConrod.toString());
		UiFactory.addSeparator(panel, "");

		sectionStartRowIx = 8;
		addConrodMeasurements(panel, sectionStartRowIx, layoutConstraints,
				layoutConrod.toString());

		contentPane.add(panel, "wrap 5");
	}

	/**
	 * Gets the block measurements edit button.
	 *
	 * @return the block measurements edit button
	 */
	public Button getBlockMeasurementsEditButton() {
		return blockMeasurementsEditButton;
	}

	/**
	 * Gets the block measurement text fields.
	 *
	 * @return the block measurement text fields
	 */
	public Map<Integer, TextField> getBlockMeasurementTextFields() {
		return blockMeasurementTextFields;
	}

	/**
	 * Gets the block measurement text fields as list.
	 *
	 * @return the block measurement text fields as list
	 */
	public List<TextField> getBlockMeasurementTextFieldsAsList() {
		return UiUtils.toList(getProcessor().getBlockMainIxDisplaySequence(),
				getBlockMeasurementTextFields());
	}

	/**
	 * Gets the conrod measurements edit button.
	 *
	 * @return the conrod measurements edit button
	 */
	public Button getConrodMeasurementsEditButton() {
		return conrodMeasurementsEditButton;
	}

	/**
	 * Gets the conrod measurement text fields.
	 *
	 * @return the conrod measurement text fields
	 */
	public Map<Integer, TextField> getConrodMeasurementTextFields() {
		return conrodMeasurementTextFields;
	}

	/**
	 * Gets the conrod measurement text fields as list.
	 *
	 * @return the conrod measurement text fields as list
	 */
	public List<TextField> getConrodMeasurementTextFieldsAsList() {
		return UiUtils.toList(getProcessor().getConrodIxDisplaySequence(),
				getConrodMeasurementTextFields());
	}

	/**
	 * Gets the content pane.
	 *
	 * @return the content pane
	 */
	public MigPane getContentPane() {
		return contentPane;
	}

	/**
	 * Gets the crank conrod measurements edit button.
	 *
	 * @return the crank conrod measurements edit button
	 */
	public Button getCrankConrodMeasurementsEditButton() {
		return crankConrodMeasurementsEditButton;
	}

	/**
	 * Gets the crank conrod measurement text fields.
	 *
	 * @return the crank conrod measurement text fields
	 */
	public Map<Integer, TextField> getCrankConrodMeasurementTextFields() {
		return crankConrodMeasurementTextFields;
	}

	/**
	 * Gets the crank conrod measurement text fields as list.
	 *
	 * @return the crank conrod measurement text fields as list
	 */
	public List<TextField> getCrankConrodMeasurementTextFieldsAsList() {
		return UiUtils.toList(getProcessor().getCrankConrodIxDisplaySequence(),
				getCrankConrodMeasurementTextFields());
	}

	/**
	 * Gets the crank main measurements edit button.
	 *
	 * @return the crank main measurements edit button
	 */
	public Button getCrankMainMeasurementsEditButton() {
		return crankMainMeasurementsEditButton;
	}

	/**
	 * Gets the crank main measurement text fields.
	 *
	 * @return the crank main measurement text fields
	 */
	public Map<Integer, TextField> getCrankMainMeasurementTextFields() {
		return crankMainMeasurementTextFields;
	}

	/**
	 * Gets the crank main measurement text fields as list.
	 *
	 * @return the crank main measurement text fields as list
	 */
	public List<TextField> getCrankMainMeasurementTextFieldsAsList() {
		return UiUtils.toList(getProcessor().getCrankMainIxDisplaySequence(),
				getCrankMainMeasurementTextFields());
	}

	/**
	 * Gets the done button.
	 *
	 * @return the done button
	 */
	public Button getDoneButton() {
		return doneButton;
	}

	/**
	 * Gets the fixed column width.
	 *
	 * @return the fixed column width
	 */
	protected int getFixedColumnWidth() {
		return 115;
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.view.AbstractDataCollectionWidget#getProcessor()
	 */
	public BearingSelectProcessor getProcessor() {
		return (BearingSelectProcessor) super.getProcessor();
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.view.OperationView#initComponents()
	 */
	@Override
	public void initComponents() {
		getProcessor().setView(this);
		this.blockMeasurementTextFields = new HashMap<Integer, TextField>();
		this.crankMainMeasurementTextFields = new HashMap<Integer, TextField>();
		this.crankConrodMeasurementTextFields = new HashMap<Integer, TextField>();
		this.conrodMeasurementTextFields = new HashMap<Integer, TextField>();
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

		createMeasurementSection();
		Platform.runLater(new Runnable() {
			public void run() {
				refreshView();
			}
		});
	}

	/**
	 * Map UI actions to their handlers
	 */
	public void mapActions() {

		final BearingSelectListener actionListener = new BearingSelectListener(
				this);

		for (TextField tf : getBlockMeasurementTextFields().values()) {
			MeasurementListener ml = new MeasurementListener(this, tf,
					getBlockMeasurementsEditButton(), getProcessor()
							.isBlockMeasurementsEditable(), 
							getProcessor().getProperty().getBlockMeasurementMaxLength());
			tf.focusedProperty().addListener(ml);
		}
		for (TextField tf : getCrankMainMeasurementTextFields().values()) {
			MeasurementListener ml = new MeasurementListener(this, tf,
					getCrankMainMeasurementsEditButton(), getProcessor()
							.isCrankMainMeasurementsEditable(),
							getProcessor().getProperty().getCrankMainMeasurementMaxLength());
			tf.focusedProperty().addListener(ml);
		}
		for (TextField tf : getCrankConrodMeasurementTextFields().values()) {
			MeasurementListener ml = new MeasurementListener(this, tf,
					getCrankConrodMeasurementsEditButton(), getProcessor()
							.isCrankConrodMeasurementsEditable(),
							getProcessor().getProperty().getCrankConrodMeasurementMaxLength());
			tf.focusedProperty().addListener(ml);
		}
		for (TextField tf : getConrodMeasurementTextFields().values()) {
			MeasurementListener ml = new MeasurementListener(this, tf,
					getConrodMeasurementsEditButton(), getProcessor()
							.isConrodMeasurementsEditable(),
							getProcessor().getProperty().getConrodMeasurementMaxLength());
			tf.focusedProperty().addListener(ml);
		}

		getBlockMeasurementsEditButton().setOnAction(actionListener);
		getCrankMainMeasurementsEditButton().setOnAction(actionListener);
		getCrankConrodMeasurementsEditButton().setOnAction(actionListener);
		getConrodMeasurementsEditButton().setOnAction(actionListener);

		EventHandler<ActionEvent> doneAction = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				getProcessor().finish();
			}
		};
		getDoneButton().setOnAction(doneAction);
	}
}