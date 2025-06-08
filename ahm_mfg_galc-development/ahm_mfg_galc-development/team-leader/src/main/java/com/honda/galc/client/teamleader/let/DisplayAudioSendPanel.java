package com.honda.galc.client.teamleader.let;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMapping;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.LetProgramResultValue;
import com.honda.galc.entity.product.LetProgramResultValueId;
import com.honda.galc.entity.product.LetProgramResultValueOne;
import com.honda.galc.property.NGTDAFeedPropertyBean;
import com.honda.galc.service.LetXmlService;
import com.honda.galc.service.NGTMQMessagingService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>DisplayAudioSendPanel Class description</h3>
 * <p>
 * DisplayAudioSendPanel description
 * </p>
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
 * @author Haohua Xie<br>
 *         Feb 27, 2014
 * 
 * 
 */
public class DisplayAudioSendPanel extends TabbedPanel implements
		ActionListener {

	private static final long serialVersionUID = 1L;

	private JLabel labelProductId;
	private JButton buttonSearch;
	private JButton buttonSend;
	private JButton buttonSendAll;
	private NGTDAFeedPropertyBean dataFeedPropertyBean;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private ObjectTablePane<DisplayAudioFeedEntry> panelDataFeed;
	private LabeledTextField textFieldEndDate;
	private UpperCaseFieldBean textFieldProdId;
	private LabeledTextField textFieldStartDate;

	public DisplayAudioSendPanel(String screenName, int keyEvent) {
		super(screenName, keyEvent);
		initialize();
	}

	public DisplayAudioSendPanel(String screenName, int keyEvent,
			MainWindow mainWindow) {
		super(screenName, keyEvent, mainWindow);
		initialize();
	}

	public DisplayAudioSendPanel(TabbedMainWindow mainWindow) {
		super("Display Audio", KeyEvent.VK_D, mainWindow);
		initialize();
	}

	public void actionPerformed(ActionEvent e) {

	}

	protected Timestamp createEndTimeStamp() throws ParseException {
		Timestamp endTimeStamp = null;
		String endDate = getTextFieldEndDate().getComponent().getText();
		try {
			endTimeStamp = StringUtils.isBlank(endDate) ? null
					: createTimeStamp(endDate);
			endTimeStamp = endTimeStamp == null ? new Timestamp(
					System.currentTimeMillis()) : endTimeStamp;
		} catch (ParseException e) {
			getTextFieldEndDate().getComponent().requestFocusInWindow();
			showPopupMessage(
					"Please enter a correct end date. date format: YYYY-MM-DD hh:mm:ss",
					JOptionPane.ERROR_MESSAGE);
			throw e;
		}
		return endTimeStamp;
	}

	private LetProgramResultValue createLetProgramResultValue(Object[] objs) {
		LetProgramResultValueId id = createLetProgramResultValueId(objs);
		LetProgramResultValue value = new LetProgramResultValueOne();
		value.setId(id);
		value.setInspectionParamValue((String) objs[6]);
		value.setInspectionParamUnit((String) objs[7]);
		value.setCreateTimestamp((Timestamp) objs[8]);
		value.setUpdateTimestamp((Timestamp) objs[9]);
		return value;
	}

	private LetProgramResultValueId createLetProgramResultValueId(Object[] objs) {
		LetProgramResultValueId id = new LetProgramResultValueId();
		id.setEndTimestamp((Timestamp) objs[0]);
		id.setProductId((String) objs[1]);
		id.setTestSeq((Integer) objs[2]);
		id.setInspectionPgmId((Integer) objs[3]);
		id.setInspectionParamId((Integer) objs[4]);
		id.setInspectionParamType((String) objs[5]);
		return id;
	}

	protected Timestamp createStartTimeStamp() throws ParseException {
		Timestamp startTimeStamp = null;
		String startDate = getTextFieldStartDate().getComponent().getText();
		try {
			startTimeStamp = StringUtils.isBlank(startDate) ? null
					: createTimeStamp(startDate);
			startTimeStamp = startTimeStamp == null ? new Timestamp(0)
					: startTimeStamp;
		} catch (ParseException e) {
			getTextFieldStartDate().getComponent().requestFocusInWindow();
			showPopupMessage(
					"Please enter a correct start date. date format: YYYY-MM-DD hh:mm:ss",
					JOptionPane.ERROR_MESSAGE);
			throw e;
		}
		return startTimeStamp;
	}

	private Timestamp createTimeStamp(String content) throws ParseException {
		Date date = dateFormat.parse(content);
		Timestamp ts = new Timestamp(date.getTime());
		return ts;
	}

	public JLabel getLabelProductId() {
		if (labelProductId == null) {
			labelProductId = new javax.swing.JLabel();
			labelProductId.setName("JLabelProductId");
			labelProductId.setText("VIN: ");
			labelProductId.setFont(new java.awt.Font("dialog", 0, 18));
			labelProductId.setIconTextGap(20);
			labelProductId.setPreferredSize(new Dimension(93, 30));
			labelProductId
					.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			labelProductId.setForeground(java.awt.Color.black);
		}
		return labelProductId;
	}

	public JButton getButtonSearch() {
		if (buttonSearch == null) {
			buttonSearch = new javax.swing.JButton();
			buttonSearch.setName("buttonSearch");
			buttonSearch.setText("Search");
			buttonSearch.setFont(new java.awt.Font("dialog", 0, 18));
			buttonSearch.setIconTextGap(40);
			buttonSearch.setPreferredSize(new Dimension(100, 30));
			buttonSearch
					.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			buttonSearch.setForeground(java.awt.Color.black);
			buttonSearch.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					searchDataFeed();
				}
			});
		}
		return buttonSearch;
	}

	public JButton getButtonSend() {
		if (buttonSend == null) {
			buttonSend = new javax.swing.JButton();
			buttonSend.setName("buttonSend");
			buttonSend.setText("Send");
			buttonSend.setFont(new java.awt.Font("dialog", 0, 18));
			buttonSend.setIconTextGap(40);
			buttonSend.setPreferredSize(new Dimension(100, 30));
			buttonSend
					.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			buttonSend.setForeground(java.awt.Color.black);
			buttonSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] rows = getPanelDataFeed().getTable()
							.getSelectedRows();
					List<DisplayAudioFeedEntry> items = getPanelDataFeed()
							.getItems();
					List<DisplayAudioFeedEntry> sendItems = new ArrayList<DisplayAudioFeedEntry>(
							rows.length);
					for (int row : rows) {
						sendItems.add(items.get(row));
					}
					sendDataFeeds(sendItems);
				}
			});
		}
		return buttonSend;
	}

	public JButton getButtonSendAll() {
		if (buttonSendAll == null) {
			buttonSendAll = new javax.swing.JButton();
			buttonSendAll.setName("buttonSend");
			buttonSendAll.setText("Send All");
			buttonSendAll.setFont(new java.awt.Font("dialog", 0, 18));
			buttonSendAll.setIconTextGap(40);
			buttonSendAll.setPreferredSize(new Dimension(100, 30));
			buttonSendAll
					.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			buttonSendAll.setForeground(java.awt.Color.black);
			buttonSendAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					List<DisplayAudioFeedEntry> items = getPanelDataFeed()
							.getItems();
					sendDataFeeds(items);
				}
			});
		}
		return buttonSendAll;
	}

	public ObjectTablePane<DisplayAudioFeedEntry> getPanelDataFeed() {
		if (panelDataFeed == null) {
			List<ColumnMapping> mappings = new ArrayList<ColumnMapping>();
			mappings.add(new ColumnMapping("Test Time", "testTime"));
			mappings.add(new ColumnMapping("Test Seq", "testSeq"));
			mappings.add(new ColumnMapping("AUTO_VIN", "productId"));
			mappings.add(new ColumnMapping("AUTO_MODEL_ID", "modelId"));
			mappings.add(new ColumnMapping("AUTO_MFR_COLOR_CD", "colorCode"));
			mappings.add(new ColumnMapping("AUTO_MODEL_TYPE_CD",
					"modelTypeCode"));
			mappings.add(new ColumnMapping("AUTO_ACCESSORY_CD",
					"modelOptionCode"));
			mappings.add(new ColumnMapping("SUPPLIER_AUDIO_SERIAL",
					"audioSerial"));
			mappings.add(new ColumnMapping("AUDIO_DWG", "audioDwg"));
			mappings.add(new ColumnMapping("AUDIO_VERSION", "audioVersion"));
			panelDataFeed = new ObjectTablePane<DisplayAudioFeedEntry>(mappings);
			panelDataFeed.setBounds(0, 0, 900, 600);
			panelDataFeed.getTable().setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			panelDataFeed.getTable().getSelectionModel()
					.addListSelectionListener(new ListSelectionListener() {

						public void valueChanged(ListSelectionEvent e) {
							refreshSendButtons();

						}
					});
		}
		return panelDataFeed;
	}

	public LabeledTextField getTextFieldEndDate() {
		if (textFieldEndDate == null) {
			textFieldEndDate = new LabeledTextField("End Date:");
			textFieldEndDate.setFont(new java.awt.Font("dialog", 0, 18));
			textFieldEndDate.getComponent().setText("");
			textFieldEndDate.getComponent().setPreferredSize(
					new Dimension(150, 30));
			textFieldEndDate.getComponent().addKeyListener(new KeyListener() {

				public void keyPressed(KeyEvent e) {

				}

				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						searchDataFeed();
					}
				}

				public void keyTyped(KeyEvent e) {

				}
			});
		}
		return textFieldEndDate;
	}

	public UpperCaseFieldBean getTextFieldProdId() {
		if (textFieldProdId == null) {
			textFieldProdId = new UpperCaseFieldBean();
			textFieldProdId.setName("JTextFieldProductID");
			textFieldProdId.setFont(new java.awt.Font("dialog", 0, 24));
			textFieldProdId.setText("");
			textFieldProdId.setFixedLength(true);
			textFieldProdId.setMaximumLength(17);
			textFieldProdId.setColumns(17);
			textFieldProdId.setSelectionColor(new Color(204, 204, 255));
			textFieldProdId.setColor(Color.blue);
			textFieldProdId.setBackground(Color.blue);
			textFieldProdId.setPreferredSize(new Dimension(200, 30));
			textFieldProdId.addKeyListener(new KeyListener() {

				public void keyPressed(KeyEvent e) {

				}

				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						searchDataFeed();
					}
				}

				public void keyTyped(KeyEvent e) {

				}
			});
		}
		return textFieldProdId;
	}

	public LabeledTextField getTextFieldStartDate() {
		if (textFieldStartDate == null) {
			textFieldStartDate = new LabeledTextField("Start Date:");
			textFieldStartDate.setFont(new java.awt.Font("dialog", 0, 18));
			textFieldStartDate.getComponent().setText("");
			textFieldStartDate.getComponent().setPreferredSize(
					new Dimension(150, 30));
			textFieldStartDate.getComponent().addKeyListener(new KeyListener() {

				public void keyPressed(KeyEvent e) {

				}

				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						textFieldEndDate.getComponent().requestFocusInWindow();
					}
				}

				public void keyTyped(KeyEvent e) {

				}
			});
		}
		return textFieldStartDate;
	}

	private void initData() {
		dataFeedPropertyBean = PropertyService.getPropertyBean(
				NGTDAFeedPropertyBean.class, getApplicationId());
		refreshSendButtons();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1024, 768));

		JPanel panelVinSearch = new JPanel();
		panelVinSearch.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panelVinSearch.add(getLabelProductId());
		panelVinSearch.add(getTextFieldProdId());
		panelVinSearch.add(getButtonSearch());
		panelVinSearch.setAlignmentY(Component.LEFT_ALIGNMENT);

		JPanel panelDateSearch = new JPanel();
		panelDateSearch.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panelDateSearch.add(getTextFieldStartDate());
		panelDateSearch.add(getTextFieldEndDate());
		panelDateSearch.setAlignmentY(Component.LEFT_ALIGNMENT);

		JPanel panelSearch = new JPanel();
		panelSearch.setLayout(new MigLayout("wrap 1"));
		panelSearch.add(panelVinSearch);
		panelSearch.add(panelDateSearch);

		add(panelSearch, BorderLayout.PAGE_START);
		add(getPanelDataFeed(), BorderLayout.CENTER);

		JPanel sendPanel = new JPanel();
		sendPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
		sendPanel.add(getButtonSend());
		sendPanel.add(getButtonSendAll());
		add(sendPanel, BorderLayout.PAGE_END);

		initData();
	}

	@Override
	public void onTabSelected() {

	}

	private boolean populateProductData(DisplayAudioFeedEntry entry) {
		BaseProduct product = ProductTypeUtil.FRAME.getProductDao().findBySn(
				entry.getProductId());
		if (product == null) {
			// No VIN is found
			return false;
		}
		FrameSpec productSpec = (FrameSpec) ProductTypeUtil.FRAME
				.getProductSpecDao().findByProductSpecCode(
						product.getProductSpecCode(),
						product.getProductType().name());
		if (productSpec == null) {
			return false;
		}
		entry.setModelId(productSpec.getSalesModelCode());
		// We don't have a SALES_MODEL_OPTION_CODE in our database, since this
		// is an optional field, we just send a blank
		entry.setModelOptionCode("");
		entry.setModelTypeCode(productSpec.getSalesModelTypeCode());
		entry.setColorCode(productSpec.getSalesExtColorCode());
		return true;
	}

	private void refreshSendButtons() {
		if (panelDataFeed.getItems() != null
				&& panelDataFeed.getItems().size() > 0) {
			getButtonSendAll().setEnabled(true);
		} else {
			getButtonSendAll().setEnabled(false);
		}
		if (panelDataFeed.getSelectedItems().size() > 0) {
			getButtonSend().setEnabled(true);
		} else {
			getButtonSend().setEnabled(false);
		}
	}

	private void searchDataFeed() {
		String productId = getTextFieldProdId().getText();
		String startDate = getTextFieldStartDate().getComponent().getText();
		String endDate = getTextFieldEndDate().getComponent().getText();
		panelDataFeed.reloadData(new ArrayList<DisplayAudioFeedEntry>(0));
		try {
			LetXmlService letService = ServiceFactory
					.getService(LetXmlService.class);

			int audioSerialParamId = letService
					.getLetInspectionParamId(dataFeedPropertyBean
							.getAudioSerialParamName());
			int audioDwgParamId = letService
					.getLetInspectionParamId(dataFeedPropertyBean
							.getAudioDwgParamName());
			int audioVersionParamId = letService
					.getLetInspectionParamId(dataFeedPropertyBean
							.getAudioVersionParamName());

			LetResultDao resultDao = ServiceFactory.getDao(LetResultDao.class);
			List<Object[]> values = null;
			try {
				if (StringUtils.isBlank(productId)
						&& StringUtils.isBlank(startDate)
						&& StringUtils.isBlank(endDate)) {
					showPopupMessage("Please input VIN or start/end date",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (StringUtils.isBlank(productId)
						&& (!StringUtils.isBlank(startDate) || !StringUtils
								.isBlank(endDate))) {
					Timestamp startTimeStamp = null;
					Timestamp endTimeStamp = null;
					startTimeStamp = createStartTimeStamp();
					endTimeStamp = createEndTimeStamp();
					values = resultDao
							.getLetProgramResultValueByParamIdEndTime(
									audioSerialParamId, startTimeStamp,
									endTimeStamp);

				} else if (!StringUtils.isBlank(productId)
						&& (!StringUtils.isBlank(startDate) || !StringUtils
								.isBlank(endDate))) {
					if (!validateProductId(productId)) {
						showPopupMessage("Please enter a valid VIN.",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					Timestamp startTimeStamp = null;
					Timestamp endTimeStamp = null;
					startTimeStamp = createStartTimeStamp();
					endTimeStamp = createEndTimeStamp();

					values = resultDao
							.getLetProgramResultValueByProductIdParamIdEndTime(
									productId, audioSerialParamId,
									startTimeStamp, endTimeStamp);

				} else if (!StringUtils.isBlank(productId)
						&& (StringUtils.isBlank(startDate) && StringUtils
								.isBlank(endDate))) {
					if (!validateProductId(productId)) {
						showPopupMessage("Please enter a valid VIN.",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					values = resultDao
							.getLetProgramResultValueByProductIdParamId(
									productId, audioSerialParamId);
				}
			} catch (ParseException e) {
				return;
			}
			if (values == null || values.isEmpty()) {
				showPopupMessage("No Display Audio data is found.",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			List<DisplayAudioFeedEntry> entries = new ArrayList<DisplayAudioFeedEntry>(
					values.size());
			for (Object[] objs : values) {
				DisplayAudioFeedEntry entry = new DisplayAudioFeedEntry();
				// Read DA Serial #
				Object[] objects = objs;
				LetProgramResultValue value = createLetProgramResultValue(objects);
				entry.setProductId(value.getId().getProductId());
				entry.setAudioSerial(value.getInspectionParamValue());

				entry.setTestSeq(String.valueOf(value.getId().getTestSeq()));

				entry.setTestTime(dateFormat.format(value.getId()
						.getEndTimestamp()));
				// Read Audio DWG
				value.getId().setInspectionParamId(audioDwgParamId);
				objects = resultDao.getLetProgramResultValueById(value.getId());
				if (objects == null) {
					entry.setAudioVersion("");
				} else {
					value = createLetProgramResultValue(objects);
					entry.setAudioDwg(value.getInspectionParamValue());
				}
				// Read Audio Version
				value.getId().setInspectionParamId(audioVersionParamId);
				objects = resultDao.getLetProgramResultValueById(value.getId());
				if (objects == null) {
					entry.setAudioVersion("");
				} else {
					value = createLetProgramResultValue(objects);
					entry.setAudioVersion(value.getInspectionParamValue());
				}
				if (populateProductData(entry)) {
					entries.add(entry);
				}
			}

			panelDataFeed.reloadData(entries);
			panelDataFeed.getTable().clearSelection();
			refreshSendButtons();
		} catch (Exception e) {
			e.printStackTrace();
			showPopupMessage("Failed to search for DA Data Feed.",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void sendDataFeed(DisplayAudioFeedEntry entry) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, entry.getProductId());
		dc.put(DataContainerTag.CLIENT_ID,
				dataFeedPropertyBean.getNGTDAFeedClientID());
		dc.put(dataFeedPropertyBean.getAudioSerialTagName(),
				entry.getAudioSerial());
		dc.put(dataFeedPropertyBean.getAudioDwgTagName(), entry.getAudioDwg());
		dc.put(dataFeedPropertyBean.getAudioVersionTagName(),
				entry.getAudioVersion());

		NGTMQMessagingService service = ServiceFactory
				.getService(NGTMQMessagingService.class);
		service.send(dc);
	}

	private void sendDataFeeds(List<DisplayAudioFeedEntry> entries) {
		try {
			for (DisplayAudioFeedEntry entry : entries) {
				sendDataFeed(entry);
			}
			showPopupMessage("The data feed was sent successfully");
		} catch (Exception e) {
			showPopupMessage("Failed to send the data feed.", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void showPopupMessage(String msg) {
		showPopupMessage(msg, JOptionPane.INFORMATION_MESSAGE);
	}

	private void showPopupMessage(String msg, int messageType) {
		JOptionPane.showMessageDialog(this, msg, "", messageType);
	}

	private boolean validateProductId(String productId) {
		if (StringUtils.isBlank(productId)) {
			return false;
		}
		return ProductNumberDef.VIN.isNumberValid(productId);
	}
}
