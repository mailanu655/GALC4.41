package com.honda.galc.client.teamleader.history;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.product.validator.AlphaNumericValidator;
import com.honda.galc.client.product.validator.DateValidator;
import com.honda.galc.client.product.validator.RegExpValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.entity.product.ProductHistory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductHistoryMaintenancePanel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * @created Jul 17, 2013
 */
public class ProductHistoryMaintenancePanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	private JTextField productIdTextField;

	private ObjectTablePane<ProductHistory> productHistoryPane;

	private JTextField processPointTextField;
	private JTextField actualTimestampTextField;
	private JTextField productionDateTextField;

	private JButton updateButton;
	private JButton deleteButton;
	private JButton resetButton;
	private JButton newEntryButton;

	private String datePattern;
	private String dateTimePattern;

	private DateFormat dateFormat;
	private DateFormat dateTimeFormat;
	private String dateTimeFormatRegex;


	public ProductHistoryMaintenancePanel() {
		super("Product History", KeyEvent.VK_H);
		this.dateTimePattern = "yyyy-MM-dd HH:mm:ss.SSSSSS";
		this.dateTimeFormatRegex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{1,6}";
		this.datePattern = "yyyy-MM-dd";
		this.dateTimeFormat = new SimpleDateFormat(getDateTimePattern());
		this.dateFormat = new SimpleDateFormat(getDatePattern());
		getDateTimeFormat().setLenient(false);
		getDateFormat().setLenient(false);
	}

	public ProductHistoryMaintenancePanel(TabbedMainWindow mainWindow) {
		super("Product History", KeyEvent.VK_H, mainWindow);
		this.dateTimePattern = "yyyy-MM-dd HH:mm:ss.SSSSSS";
		this.dateTimeFormatRegex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{1,6}";
		this.datePattern = "yyyy-MM-dd";
		this.dateTimeFormat = new SimpleDateFormat(getDateTimePattern());
		this.dateFormat = new SimpleDateFormat(getDatePattern());
		getDateTimeFormat().setLenient(false);
		getDateFormat().setLenient(false);

		initView();
		mapActions();
		mapValidators();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getProductIdTextField().requestFocusInWindow();
			}
		});
	}

	@Override
	public void onTabSelected() {

	}

	public void actionPerformed(ActionEvent arg0) {
	}

	// === initialization ===//
	protected void initView() {

		String productIdLabel = getMainWindow().getApplicationContext().getProductTypeData().getProductIdLabel();
		int inputLength = getMainWindow().getProductType().getProductIdLength();

		UiFactory factory = UiFactory.getInfoSmall();
		createProductIdTextField(inputLength);
		createProductHistoryPane();

		this.processPointTextField = UiFactory.getInfo().createTextField(TextFieldState.DISABLED);
		this.productionDateTextField = createDateTextField(UiFactory.getInfo(), getDatePattern());
		createActualTimestampTextField();

		this.deleteButton = factory.createButton("Delete");
		this.updateButton = factory.createButton("Update");
		this.resetButton = factory.createButton("Next Product");
		this.newEntryButton = factory.createButton("Add New Entry");

		setName(getClass().getSimpleName());
		StringBuilder sb = new StringBuilder("20[]10[][][]20[]20");
		setLayout(new MigLayout("insets 10 5 0 5", sb.toString(), ""));

		add(UiFactory.getIdle().createLabel(productIdLabel));
		add(getProductIdTextField(), "span 4, width max, wrap 20");
		add(getProductHistoryPane(), new CC().span(5).width("max").wrap("20"));

		add(factory.createLabel("Process Point Name"), "span 2");
		add(getProcessPointTextField(), "span 2, width max");
		add(getResetButton(), "width 150!, height 40!,wrap 5");

		add(factory.createLabel("Actual Timestamp"), "span 1");
		add(createSmallLabel(String.format(" (%s)", getDateTimePattern())));
		add(getActualTimestampTextField(), "span 2, width max");
		add(getDeleteButton(), "width 150!, height 40!, wrap");

		add(factory.createLabel("Production Date"), "span 1");
		add(createSmallLabel(String.format(" (%s)", getDatePattern())));
		add(getProductionDateTextField(), "span 2, width 250!");
		add(getUpdateButton(), "width 150!, height 40!, wrap 10");
		add(getNewEntryButton(), new CC().span(5).alignX("right").height("40").width("20"));
	}

	public JTextField createActualTimestampTextField() {
		this.actualTimestampTextField = createDateTextField(UiFactory.getInfo(), getDateTimePattern());
		this.actualTimestampTextField.setName("actualTimestampTextField");
		return this.actualTimestampTextField;
	}

	public ObjectTablePane<ProductHistory> createProductHistoryPane() {
		this.productHistoryPane = createHistoryPane();
		this.productHistoryPane.getTable().setName("productHistoryTable");
		return this.productHistoryPane;
	}

	public JTextField createProductIdTextField(int inputLength) {
		this.productIdTextField = UiFactory.getIdle().createTextField(inputLength, TextFieldState.EDIT);
		this.productIdTextField.setName("productIdTextField");
		return this.productIdTextField;
	}

	protected void mapActions() {
		ProductHistoryMaintenanceController listner = new ProductHistoryMaintenanceController(this);
		getProductIdTextField().addActionListener(listner);
		getProductIdTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getProductIdTextField()));

		getActualTimestampTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getActualTimestampTextField()));
		getProductionDateTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getProductionDateTextField()));

		getResetButton().addActionListener(listner);
		getUpdateButton().addActionListener(listner);
		getNewEntryButton().addActionListener(listner);
		getDeleteButton().addActionListener(listner);

		getProductHistoryPane().getTable().getSelectionModel().addListSelectionListener(listner);
	}

	public void mapValidators() {

		RequiredValidator requiredValidator = new RequiredValidator();
		AlphaNumericValidator alphaNumericValidator = new AlphaNumericValidator();

		DateValidator dateTimeValidator = new DateValidator(false, getDateTimePattern()) {
			@Override
			public String getMessage(String propertyName) {
				return MessageFormat.format(getMessageTemplate(), propertyName, getPatternsAsString());
			}
		};
		DateValidator dateValidator = new DateValidator(false, getDatePattern()) {
			@Override
			public String getMessage(String propertyName) {
				return MessageFormat.format(getMessageTemplate(), propertyName, getPatternsAsString());
			}
		};
		RegExpValidator dateTimeRegExpValidator = new RegExpValidator(UiUtils.dateTimePatternToRegExpPattern(getDateTimePattern())) {
			@Override
			public String getMessage(String propertyName) {
				return MessageFormat.format(getMessageTemplate(), propertyName, getDateTimePattern());
			}
		};
		RegExpValidator dateRegExpValidator = new RegExpValidator(UiUtils.dateTimePatternToRegExpPattern(getDatePattern())) {
			@Override
			public String getMessage(String propertyName) {
				return MessageFormat.format(getMessageTemplate(), propertyName, getDatePattern());
			}
		};
		UiUtils.mapValidator(getProductIdTextField(), ChainCommand.create("Input Number", requiredValidator, alphaNumericValidator));
		UiUtils.mapValidator(getActualTimestampTextField(), ChainCommand.create("Actual Timestamp", requiredValidator, dateTimeRegExpValidator, dateTimeValidator));
		UiUtils.mapValidator(getProductionDateTextField(), ChainCommand.create("Production Date", requiredValidator, dateRegExpValidator, dateValidator));
	}

	// === factory methods === //
	protected JTextField createDateTextField(UiFactory factory, final String pattern) {
		JTextField component = new JTextField();
		component.setColumns(StringUtils.trim(pattern).length());
		TextFieldState.DISABLED.setState(component);
		LimitedLengthPlainDocument document = new LimitedLengthPlainDocument(pattern.length());
		component.setDocument(document);
		component.setFont(factory.getInputFont());
		return component;
	}

	protected Timestamp getActualTimestampValue() {
		String text = getActualTimestampTextField().getText();
		Timestamp date = null;
		try {
			if(!text.matches(dateTimeFormatRegex))return null;
			date = new Timestamp(getDateTimeFormat().parse(text).getTime());
		} catch (ParseException e) {
			getMainWindow().getLogger().error(e, "Error trying to parse Actual Timestamp");
		}
		return date;
	}

	protected void setActualTimestampValue(Date date) {
		String text = "";
		if (date != null) {
			text = getDateTimeFormat().format(date);
		}
		getActualTimestampTextField().setText(text);
	}

	protected Date getProductionDateValue() {
		String text = getProductionDateTextField().getText();
		Date date = null;
		try {
			date = getDateFormat().parse(text);
		} catch (ParseException e) {
			//
		}
		return date;
	}

	protected void setProductionDateValue(Date date) {
		String text = "";
		if (date != null) {
			text = getDateFormat().format(date);
		}
		getProductionDateTextField().setText(text);
	}

	protected JLabel createSmallLabel(String text) {
		JLabel component = new JLabel(text);
		component.setFont(getFont().deriveFont(Font.PLAIN));
		return component;
	}

	protected ObjectTablePane<ProductHistory> createHistoryPane() {
		PropertiesMapping columnMappings = new PropertiesMapping();
		columnMappings.put("#", "row");
		columnMappings.put("Process Point Id", "processPointId");
		columnMappings.put("Process Point Name", "processPointName");
		columnMappings.put("Actual Timestamp", "actualTimestamp", new SimpleDateFormat(getDateTimePattern()));
		columnMappings.put("Production Date", "productionDate", new SimpleDateFormat(getDatePattern()));
		ObjectTablePane<ProductHistory> panel = new ObjectTablePane<ProductHistory>(columnMappings.get(), true, true);
		panel.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return panel;
	}

	// === get/set === //
	protected JTextField getProductIdTextField() {
		return productIdTextField;
	}

	protected ObjectTablePane<ProductHistory> getProductHistoryPane() {
		return productHistoryPane;
	}

	protected JTextField getActualTimestampTextField() {
		return actualTimestampTextField;
	}

	protected JTextField getProductionDateTextField() {
		return productionDateTextField;
	}

	protected JButton getUpdateButton() {
		return updateButton;
	}

	protected JButton getNewEntryButton(){
		return newEntryButton;
	}
	
	protected JButton getDeleteButton() {
		return deleteButton;
	}

	protected JButton getResetButton() {
		return resetButton;
	}

	protected JTextField getProcessPointTextField() {
		return processPointTextField;
	}

	protected String getDatePattern() {
		return datePattern;
	}

	protected String getDateTimePattern() {
		return dateTimePattern;
	}

	protected DateFormat getDateFormat() {
		return dateFormat;
	}

	protected DateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}
}
