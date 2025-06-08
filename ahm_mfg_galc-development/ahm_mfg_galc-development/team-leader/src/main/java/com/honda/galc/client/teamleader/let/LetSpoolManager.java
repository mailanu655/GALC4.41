package com.honda.galc.client.teamleader.let;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.honda.galc.client.enumtype.LetColumnNames;
import com.honda.galc.client.enumtype.LetTotalStatus;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetMessageDao;
import com.honda.galc.dao.product.LetSpoolDao;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.device.let.HandHeldDeviceXmlSendThread;
import com.honda.galc.entity.enumtype.UploadStatusType;
import com.honda.galc.entity.product.LetMessage;
import com.honda.galc.entity.product.LetSpool;
import com.honda.galc.message.Message;
import com.honda.galc.net.TCPSocketFactory;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author Shweta kadav
 * @date Mar 04, 2016
 */
@SuppressWarnings(value = { "all" })
public class LetSpoolManager extends TabbedPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel jMainPanel = null;

	private JLabel jHourLbl = null;
	private JLabel jMinLbl = null;
	private JLabel jSecLbl = null;
	private JLabel jMseclbl = null;
	private JLabel jYearLbl = null;
	private JLabel jMonthLbl = null;
	private JLabel jDayLbl = null;
	private JLabel jDeviceNameLbl = null;
	private JLabel jFilterByProductLbl = null;
	private JTable jLetMessageTable = null;

	private JComboBox jDeviceNameCombo = null;
	private JScrollPane jLetMessageScrollPane = null;

	private JButton jDeleteBtn = null;
	private JButton jUploadBtn = null;
	private JButton jRestartBtn = null;
	private JButton jDownLoadBtn = null;
	private JButton jLoadSearchBtn = null;
	private JButton jLoadPointerBtn = null;
	private JButton jSetCurrentTime = null;

	private JRadioButton jTextRadioBtn = null;
	private JRadioButton jXmlRadioBtn = null;

	private LabeledComboBox statusElement;

	private JTextField jFilterByProductTxt = null;

	private JSpinner jHourChooser = null;
	private JSpinner jMinChooser = null;
	private JSpinner jSecChooser = null;
	private JSpinner jMsecChooser = null;

	private JCheckBoxMenuItem ipAddressChkBox;
	private JCheckBoxMenuItem productIdChkBox;
	private JCheckBoxMenuItem timestampChkBox;
	private JCheckBoxMenuItem processingTimeChkBox;
	private JCheckBoxMenuItem buildCodeChkBox;
	private JCheckBoxMenuItem macAddrChkBox;
	private JCheckBoxMenuItem msgHeaderChkBox;
	private JCheckBoxMenuItem msgTypeChkBox;
	private JCheckBoxMenuItem msgRplyChkBox;

	private Socket _socket = null;

	private List<String> deviceNameList = new ArrayList<String>();
	private List<String> columnList = new ArrayList<String>();

	private String searchStr; 
	private JDatePickerImpl jDatePicker;
	
	private JRadioButton jAutoRefreshOn = null;
	private JRadioButton jAutoRefreshOff = null;
	private JLabel jAutoRefreshLbl = null;
	private final long ITEMS_PER_PAGE = 25;
	private long startIndex = 1;
	private long endIndex = ITEMS_PER_PAGE;
	private long totalMsgCount;
	private JLabel currentPage = new JLabel();
	private JLabel totalPageCount = new JLabel();
	private long maxPageIndex;

	private boolean isCurrentTimeButtonHit;
	private HashMap<String, TableColumn> columnsMap = new HashMap<String, TableColumn>();
	int selectedRow = 0;

	public LetSpoolManager(TabbedMainWindow mainWindow) {
		super("MessageSpoolManager", KeyEvent.VK_S, mainWindow);
		initialize();
	}

	public LetSpoolManager(String screenName, int keyEvent,
			TabbedMainWindow mainWindow) {
		super(screenName, keyEvent, mainWindow);
	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		getLogger().info("MessageSpoolManager Panel is selected");
	}

	private void initialize() {
		try {
			setLayout(new BorderLayout());
			add(getMainPanel(), BorderLayout.CENTER);
			getLetMessageData((LetSpool) jDeviceNameCombo.getSelectedItem());

		} catch (Exception e) {
			getLogger().error(e, "An error Occurred while processing the MessageSpoolManager screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the MessageSpoolManager screen");
		}
	}

	private void setInitialColumnModel() {
		TableColumnModel tableColumnModel = jLetMessageTable.getColumnModel();
		Enumeration<TableColumn> enumeration = tableColumnModel.getColumns();
		while (enumeration.hasMoreElements()) {
			TableColumn tableColumn = enumeration.nextElement();
			columnsMap.put((String) tableColumn.getIdentifier(), tableColumn);
		}

		if(!ipAddressChkBox.isSelected()){
			jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(LetColumnNames.IP_ADDRESS.toString()));
		}if(!productIdChkBox.isSelected()){
			jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(LetColumnNames.PRODUCT_ID.toString()));
		}if(!timestampChkBox.isSelected()){
			jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(LetColumnNames.ACTUAL_TIMESTAMP.toString()));
		}if(!processingTimeChkBox.isSelected()){
			jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(LetColumnNames.PROCESSING_TIME.toString()));
		}if(!buildCodeChkBox.isSelected()){
			jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(LetColumnNames.BUILD_CODE.toString()));
		}if(!macAddrChkBox.isSelected()){
			jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(LetColumnNames.MAC_ADDRESS.toString()));
		}if(!msgHeaderChkBox.isSelected()){
			jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(LetColumnNames.MESSAGE_HEADER.toString()));
		}if(!msgRplyChkBox.isSelected()){
			jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(LetColumnNames.MESSAGE_REPLY.toString()));
		}if(!msgTypeChkBox.isSelected()){
			jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(LetColumnNames.MESSAGE_TYPE.toString()));
		}
		jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(""));
	}

	private JPanel getMainPanel() {
		if (jMainPanel == null) {
			ButtonGroup downloadRadioBtnGrp = new ButtonGroup();
			jMainPanel = new JPanel();
			jMainPanel.setLayout(null);

			jMainPanel.add(getDeviceNameLabel(), null);
			jMainPanel.add(getFilterLabel(), null);
			jMainPanel.add(getDeviceNameDropDown(), null);
			jMainPanel.add(getFilterbyProductText(), null);

			jMainPanel.add(getLetMessagePane(), null);
			jMainPanel.add(getButtonPane(), null);
			jMainPanel.add(getAutoRefreshPane(), null);
			jMainPanel.add(getUpLoadButton(), null);

			jMainPanel.add(getYearLbl(), null);
			jMainPanel.add(getDateChooser(), null);
			jMainPanel.add(setCurrentTimeButton(), null);
			jMainPanel.add(getHourLbl(), null);
			jMainPanel.add(getHourChooser(), null);
			jMainPanel.add(getMinLbl(), null);
			jMainPanel.add(getMinChooser(), null);
			jMainPanel.add(getSecLbl(), null);
			jMainPanel.add(getSecChooser(), null);
			jMainPanel.add(getmSecLbl(), null);
			jMainPanel.add(getMsecChooser(), null);

			jMainPanel.add(getRestartButton(), null);
			jMainPanel.add(getDeleteButton(), null);
			jMainPanel.add(getDownloadButton(), null);

			jTextRadioBtn = getTextRadioButton();
			jXmlRadioBtn = getXmlRadioButton();

			downloadRadioBtnGrp.add(jXmlRadioBtn);
			downloadRadioBtnGrp.add(jTextRadioBtn);

			jMainPanel.add(jXmlRadioBtn, null);
			jMainPanel.add(jTextRadioBtn, null);
			
			jMainPanel.add(getStatusFilterPane(), null);
		}
		return jMainPanel;
	}

	private JLabel getDeviceNameLabel() {
		if (jDeviceNameLbl == null) {
			jDeviceNameLbl = new JLabel();
			jDeviceNameLbl.setSize(250, 35);
			jDeviceNameLbl.setFont(new Font("Dialog", Font.BOLD, 15));

			jDeviceNameLbl.setText(Message.get("DEVICE_NAME"));
			jDeviceNameLbl.setLocation(30, 60);
		}
		return jDeviceNameLbl;
	}

	private JComboBox getDeviceNameDropDown() {
		if (jDeviceNameCombo == null) {
			jDeviceNameCombo = new JComboBox();
			List<LetSpool> letSpools = getDao(LetSpoolDao.class).findAll();
			jDeviceNameCombo.setModel(new DefaultComboBoxModel(letSpools.toArray()));

			jDeviceNameCombo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onDeviceNameChange();
				}
			});
			jDeviceNameCombo.setSize(250, 35);
			jDeviceNameCombo.setLocation(150, 60);
			jDeviceNameCombo.setBackground(Color.WHITE);
		}
		return jDeviceNameCombo;
	}

	protected void onDeviceNameChange() {
		getLetMessageData((LetSpool) jDeviceNameCombo.getSelectedItem());
	}

	private JLabel getFilterLabel() {
		if (jFilterByProductLbl == null) {
			jFilterByProductLbl = new JLabel();
			jFilterByProductLbl.setSize(150, 35);
			jFilterByProductLbl.setFont(new Font("Dialog", Font.BOLD, 15));
			jFilterByProductLbl.setText(Message.get("filterByProduct"));
			jFilterByProductLbl.setLocation(30, 10);
		}
		return jFilterByProductLbl;
	}

	private JTextField getFilterbyProductText() {

		if (jFilterByProductTxt == null) {
			jFilterByProductTxt = new JTextField();
			jFilterByProductTxt.setName("jFilterByProductTxt");
			jFilterByProductTxt.setSize(250, 35);
			jFilterByProductTxt.setFont(new Font("Dialog", Font.BOLD, 15));
			jFilterByProductTxt.setLocation(150, 10);
			jFilterByProductTxt.addKeyListener(new KeyListener() {

				public void keyTyped(KeyEvent arg0) {}

				public void keyReleased(KeyEvent arg0) {}

				public void keyPressed(KeyEvent actionEvent) {
					if(actionEvent.getKeyCode() == KeyEvent.VK_ENTER) {
						getLetMessageData((LetSpool) jDeviceNameCombo.getSelectedItem());		
					}
				}
			});
		}
		return jFilterByProductTxt;
	}
	
	protected LabeledComboBox getStatusElement() {
		
		if (statusElement == null) {
			statusElement = new LabeledComboBox("Send Status");
			statusElement.setName("statusComboBox");
			statusElement.getLabel().setFont(Fonts.DIALOG_BOLD_15);
			statusElement.getComponent().setBackground(Color.WHITE);
			statusElement.setInsets(0, 0, 0, 0);
			statusElement.getComponent().addItem("All");
			for (LetTotalStatus status : LetTotalStatus.values()) {
				statusElement.getComponent().addItem(status);
			}
			
			statusElement.getComponent().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					searchByStatus(actionEvent);
				}
			});
		}
		return statusElement;
	}
	
	private JPanel getStatusFilterPane() {
		JPanel filterPane = new JPanel(new GridLayout(0, 1));
		filterPane.add(getStatusElement());
		filterPane.setSize(250, 35);
		filterPane.setLocation(400, 10);
		return filterPane;
	}

	private void searchByStatus(ActionEvent actionEvent) {
		getLetMessageData((LetSpool) jDeviceNameCombo.getSelectedItem());		
	}

	private JLabel getYearLbl() {
		if (jYearLbl == null) {
			jYearLbl = new JLabel();
			jYearLbl.setSize(150, 35);
			jYearLbl.setFont(new Font("Dialog", Font.BOLD, 12));
			jYearLbl.setText(Message.get("DATE"));
			jYearLbl.setLocation(670, 10);
		}
		return jYearLbl;
	}

	private JDatePickerImpl getDateChooser() {
		if (jDatePicker == null) {
			setCurrentDateTime();
			jDatePicker.setLocation(700, 15);
			jDatePicker.setVisible(true);
			jDatePicker.setSize(150, 45);
		}
		return jDatePicker;
	}

	private JButton setCurrentTimeButton(){
		if (jSetCurrentTime == null) {
			jSetCurrentTime = new JButton();
			jSetCurrentTime.setName("currentTime");
			jSetCurrentTime.setSize(120, 26);
			jSetCurrentTime.setFont(new Font("Dialog", Font.BOLD, 12));
			jSetCurrentTime.setText(Message.get("CURRENT_TIME"));
			jSetCurrentTime.setLocation(860, 15);
			jSetCurrentTime.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setCurrentDateTime();
				}

			});
		}
		return jSetCurrentTime;
	}

	private void setCurrentDateTime() {
		isCurrentTimeButtonHit = true;
		UtilDateModel model = new UtilDateModel(new Date());
		model.setSelected(true);
		Properties properties = new Properties();
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");
		properties.put("text.clear", "Clear");
		JDatePanelImpl datePanel = new JDatePanelImpl(model,properties);
		datePanel.setShowYearButtons(true);
		jDatePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter() {
			private String datePattern = "yyyy-MM-dd";
			private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

			@Override
			public Object stringToValue(String text) throws ParseException {
				return dateFormatter.parseObject(text);
			}

			@Override
			public String valueToString(Object value) throws ParseException {
				if (value != null) {
					Calendar cal = (Calendar) value;
					return dateFormatter.format(cal.getTime());
				}

				return "";
			}
		});
		if(null!=jHourChooser){
			jHourChooser.setValue(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		}if(null!=jMinChooser){
			jMinChooser.setValue(Calendar.getInstance().get(Calendar.MINUTE));
		}if(null!=jSecChooser){
			jSecChooser.setValue(Calendar.getInstance().get(Calendar.SECOND));
		}if(null!=jMsecChooser){
			jMsecChooser.setValue(Calendar.getInstance().get(Calendar.MILLISECOND)); 
		}
		jDatePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMessage();
			}
		});
	}

	private JLabel getMonthLbl() {
		if (jMonthLbl == null) {
			jMonthLbl = new JLabel();
			jMonthLbl.setSize(150, 35);
			jMonthLbl.setFont(new Font("Dialog", Font.BOLD, 12));
			jMonthLbl.setText(Message.get("MONTH"));
			jMonthLbl.setLocation(670, 10);
		}
		return jMonthLbl;
	}

	private JLabel getDayLbl() {
		if (jDayLbl == null) {
			jDayLbl = new JLabel();
			jDayLbl.setSize(150, 35);
			jDayLbl.setFont(new Font("Dialog", Font.BOLD, 12));
			jDayLbl.setText(Message.get("DAY"));
			jDayLbl.setLocation(800, 10);
		}
		return jDayLbl;
	}

	private JLabel getHourLbl() {
		if (jHourLbl == null) {
			jHourLbl = new JLabel();
			jHourLbl.setSize(150, 35);
			jHourLbl.setFont(new Font("Dialog", Font.BOLD, 12));
			jHourLbl.setText(Message.get("HOUR"));
			jHourLbl.setLocation(550, 55);
		}
		return jHourLbl;
	}

	private JSpinner getHourChooser() {
		if (jHourChooser == null) {
			SpinnerModel spinnerModel = new SpinnerNumberModel(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), // initial value
					0, // min
					24, // max
					1);// step
			jHourChooser = new JSpinner(spinnerModel);
			jHourChooser.setSize(70, 25);
			jHourChooser.setLocation(580, 60);
			jHourChooser.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if(!isCurrentTimeButtonHit){
						selectMessage();
					}
				}
			});
		}
		return jHourChooser;
	}

	private JLabel getMinLbl() {
		if (jMinLbl == null) {
			jMinLbl = new JLabel();
			jMinLbl.setSize(150, 35);
			jMinLbl.setFont(new Font("Dialog", Font.BOLD, 12));
			jMinLbl.setText(Message.get("MIN"));
			jMinLbl.setLocation(660, 55);
		}
		return jMinLbl;
	}

	private JSpinner getMinChooser() {
		if (jMinChooser == null) {
			SpinnerModel spinnerModel = new SpinnerNumberModel(Calendar.getInstance().get(Calendar.MINUTE), // initial value
					0, // min
					60, // max
					1);// step
			jMinChooser = new JSpinner(spinnerModel);
			jMinChooser.setSize(70, 25);
			jMinChooser.setLocation(685, 60);
			jMinChooser.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if(!isCurrentTimeButtonHit){
						selectMessage();
					}
				}
			});
		}
		return jMinChooser;
	}

	private JLabel getSecLbl() {
		if (jSecLbl == null) {
			jSecLbl = new JLabel();
			jSecLbl.setSize(150, 35);
			jSecLbl.setFont(new Font("Dialog", Font.BOLD, 12));
			jSecLbl.setText(Message.get("SECOND"));
			jSecLbl.setLocation(765, 55);
		}
		return jSecLbl;
	}

	private JSpinner getSecChooser() {
		if (jSecChooser == null) {
			SpinnerModel spinnerModel = new SpinnerNumberModel(Calendar.getInstance().get(Calendar.SECOND), // initial value
					0, // min
					60, // max
					1);// step
			jSecChooser = new JSpinner(spinnerModel);
			jSecChooser.setSize(70, 25);
			jSecChooser.setLocation(790, 60);
			jSecChooser.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if(!isCurrentTimeButtonHit){
						selectMessage();
					}
				}
			});
		}
		return jSecChooser;
	}

	private JLabel getmSecLbl() {
		if (jMseclbl == null) {
			jMseclbl = new JLabel();
			jMseclbl.setSize(150, 35);
			jMseclbl.setFont(new Font("Dialog", Font.BOLD, 12));
			jMseclbl.setText(Message.get("M_SECOND"));
			jMseclbl.setLocation(870, 55);
		}
		return jMseclbl;
	}

	private JSpinner getMsecChooser() {
		if (jMsecChooser == null) {
			SpinnerModel spinnerModel = new SpinnerNumberModel(Calendar.getInstance().get(Calendar.MILLISECOND),// initial value
					0, // min
					1000, // max
					1);// step
			jMsecChooser = new JSpinner(spinnerModel);
			jMsecChooser.setSize(70, 25);
			jMsecChooser.setLocation(910, 60);
			jMsecChooser.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					selectMessage();
				}
			});
		}
		return jMsecChooser;
	}

	private JButton getLoadPointerBtn() {
		if (jLoadPointerBtn == null) {
			jLoadPointerBtn = new JButton();
			jLoadPointerBtn.setSize(120, 40);
			jLoadPointerBtn.setName("jLoadPointerBtn");
			jLoadPointerBtn.setText(Message.get("LOAD_POINTER"));
			jLoadPointerBtn.setLocation(420, 525);
			jLoadPointerBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selectMessage();
				}
			});
		}
		return jLoadPointerBtn;
	}

	private void selectMessage() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						highlightSelectedMsg();
					}
				});
			}
		}, 10);
	}

	private void highlightSelectedMsg() {
		long startTime = System.currentTimeMillis();

		TableColumn column = null;
		int maxRecords =  getLetPropertyBean().getMaxRecords();

		LetSpool letSpool = (LetSpool) jDeviceNameCombo.getSelectedItem();
		Timestamp sqlTimestamp = getSelectedDate();
		String productSearchTxt = jFilterByProductTxt.getText();
		LetMessage letMsg = getDao(LetMessageDao.class).findDataByTimeStamp(sqlTimestamp);

		List<Long> messageIdList = getDao(LetMessageDao.class).findAllMsg(letSpool.getSpoolId(), searchStr, productSearchTxt + "%", maxRecords);
		ListSelectionModel selectionModel = jLetMessageTable.getSelectionModel();

		if (null != letMsg) {

			for (int i = 0; i < messageIdList.size(); i++) {
				Long letMessageMsgId = messageIdList.get(i);
				if (letMsg.getMessageId() == letMessageMsgId) {
					selectedRow = i;
					break;
				}
			}
		}
		getLetMessageData((LetSpool) jDeviceNameCombo.getSelectedItem());
		selectionModel.setSelectionInterval(selectedRow, selectedRow);

		jLetMessageTable.setSelectionBackground(Color.YELLOW);
		long timeTaken = System.currentTimeMillis() - startTime;
		getLogger().info("Search and select records for timestamp "+ sqlTimestamp + " took " + timeTaken + " ms");
	}

	private Timestamp getSelectedDate() {
		Date javaTime = null;
		DateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		int selectedYear =  jDatePicker.getModel().getYear();
		int selectedMonth =  jDatePicker.getModel().getMonth()+1;
		int selectedDay =  jDatePicker.getModel().getDay();
		int selectedHour = (Integer) jHourChooser.getValue();
		int selectedMin = (Integer) jMinChooser.getValue();
		int selectedSec = (Integer) jSecChooser.getValue();
		int selectedMSec = (Integer) jMsecChooser.getValue();
		LetSpool letSpool = (LetSpool) jDeviceNameCombo.getSelectedItem();
		String selectedActualTime = String.valueOf(selectedYear) + "-"
				+ String.valueOf(selectedMonth) + "-"
				+ String.valueOf(selectedDay) + " "
				+ String.valueOf(selectedHour) + ":"
				+ String.valueOf(selectedMin) + ":"
				+ String.valueOf(selectedSec) + "."
				+ String.valueOf(selectedMSec);
		try {
			javaTime = (Date) formatter.parse(selectedActualTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Timestamp sqlTimestamp = new Timestamp(javaTime.getTime());
		return sqlTimestamp;
	}

	private JTable getLetMessageTable() {

		if (jLetMessageTable == null) {
			jLetMessageTable = new JTable();
			jLetMessageTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jLetMessageTable.setColumnSelectionAllowed(false);
			jLetMessageTable.setRowSelectionAllowed(true);

			jLetMessageTable.setName("letMessageTable");
			jLetMessageTable.getTableHeader().setReorderingAllowed(false);
			jLetMessageTable.setFont(new Font("Dialog", Font.PLAIN, 12));

			addRightClickPopup();
		}
		return jLetMessageTable;
	}

	private void addRightClickPopup() {
		JPopupMenu popupMenu = new JPopupMenu();

		ipAddressChkBox = new JCheckBoxMenuItem(LetColumnNames.IP_ADDRESS.toString(), true);
		productIdChkBox = new JCheckBoxMenuItem(LetColumnNames.PRODUCT_ID.toString(), true);
		timestampChkBox = new JCheckBoxMenuItem(LetColumnNames.ACTUAL_TIMESTAMP.toString(), true);
		processingTimeChkBox = new JCheckBoxMenuItem(LetColumnNames.PROCESSING_TIME.toString(), true);
		buildCodeChkBox = new JCheckBoxMenuItem(LetColumnNames.BUILD_CODE.toString(), false);
		macAddrChkBox = new JCheckBoxMenuItem(LetColumnNames.MAC_ADDRESS.toString(), false);
		msgHeaderChkBox = new JCheckBoxMenuItem(LetColumnNames.MESSAGE_HEADER.toString(), false);
		msgTypeChkBox = new JCheckBoxMenuItem(LetColumnNames.MESSAGE_TYPE.toString(), true);
		msgRplyChkBox = new JCheckBoxMenuItem(LetColumnNames.MESSAGE_REPLY.toString(), false);

		ActionListener aListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JCheckBoxMenuItem boxMenuItem = (JCheckBoxMenuItem) event.getSource();

				if (columnsMap != null) {
					TableColumn columnHeader = columnsMap.get(boxMenuItem.getText());
					if (boxMenuItem.isSelected()){
						SortedMap<Integer,TableColumn> sortedMap = new TreeMap();

						Enumeration<TableColumn> enumeration = jLetMessageTable.getColumnModel().getColumns();
						while (enumeration.hasMoreElements()){
							TableColumn column = enumeration.nextElement();
							sortedMap.put(column.getModelIndex(), column);
						}

						for (TableColumn column: sortedMap.values()) {
							jLetMessageTable.getColumnModel().removeColumn(column);
						}

						sortedMap.put(columnHeader.getModelIndex(),columnHeader);

						for (TableColumn column: sortedMap.values()) {
							jLetMessageTable.getColumnModel().addColumn(column);
						}
					}
					else
						jLetMessageTable.getColumnModel().removeColumn(columnHeader);
				}
			}
		};

		ipAddressChkBox.addActionListener(aListener);
		productIdChkBox.addActionListener(aListener);
		timestampChkBox.addActionListener(aListener);
		processingTimeChkBox.addActionListener(aListener);
		buildCodeChkBox.addActionListener(aListener);
		macAddrChkBox.addActionListener(aListener);
		msgHeaderChkBox.addActionListener(aListener);
		msgTypeChkBox.addActionListener(aListener);
		msgRplyChkBox.addActionListener(aListener);

		popupMenu.add(ipAddressChkBox);
		popupMenu.add(productIdChkBox);
		popupMenu.add(timestampChkBox);
		popupMenu.add(processingTimeChkBox);
		popupMenu.add(processingTimeChkBox);
		popupMenu.add(buildCodeChkBox);
		popupMenu.add(macAddrChkBox);
		popupMenu.add(msgHeaderChkBox);
		popupMenu.add(msgTypeChkBox);
		popupMenu.add(msgRplyChkBox);

		jLetMessageTable.setComponentPopupMenu(popupMenu);
	}

	private javax.swing.JScrollPane getLetMessagePane() {
		if (jLetMessageScrollPane == null) {
			jLetMessageScrollPane = new JScrollPane();
			jLetMessageScrollPane.setViewportView(getLetMessageTable());
			jLetMessageScrollPane.setSize(950, 422);
			jLetMessageScrollPane.setLocation(30, 100);
			jLetMessageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		}
		return jLetMessageScrollPane;
	}

	private JButton getRestartButton() {
		if (jRestartBtn == null) {
			jRestartBtn = new JButton();
			jRestartBtn.setSize(100, 40);
			jRestartBtn.setName("restartButton");
			jRestartBtn.setText(Message.get("RESTART"));
			jRestartBtn.setLocation(540, 525);
			jRestartBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onRestart();
				}
			});
		}
		return jRestartBtn;
	}

	private void onRestart() {
		clearErrorMessage();
		int rowCount = jLetMessageTable.getRowCount();
		int rows = jLetMessageTable.getSelectedRowCount();
		if (rowCount > 0 && rows == 1) {
			Date date = null;
			int indexOfTimeStamp = 0;
			BufferedWriter bufOut = null;

			final int selectedRow = jLetMessageTable.getSelectedRow();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
			Enumeration<TableColumn> enumeration = jLetMessageTable.getColumnModel().getColumns();
			while (enumeration.hasMoreElements()) {
				TableColumn column = enumeration.nextElement();
				if(column.getHeaderValue().toString().equalsIgnoreCase("ACTUAL_TIMESTAMP")){
					indexOfTimeStamp = column.getModelIndex();
				}
			}

			String selectedTimeStamp = (String) jLetMessageTable.getValueAt(selectedRow, indexOfTimeStamp);

			try {
				date = (Date) formatter.parse(selectedTimeStamp);
			} catch (ParseException e) {
				setErrorMessage("Date conversion failed");
				getLogger().error(e,"Failed parsing date");
				e.printStackTrace();
			}
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
			String[] reprocessableStatus = {LetTotalStatus.FAIL_PROC.name()};
			List<Long> letMessages = getDao(LetMessageDao.class).findMsgIdsByStatusAndTs(timeStampDate, Arrays.asList(reprocessableStatus));

			if(null != letMessages && !letMessages.isEmpty()){
				setMessage("Please wait ... Resending XML files and processing in progress..... ");
				reProcessMessageInNewThread(selectedRow, letMessages);
			}else{
				setMessage("No NAK records to be reprocessed");
			}
		} else {
			if(!(rowCount == 0)){
				setErrorMessage("Select only one record to restart processing");
			}
		}
	}

	private void reProcessMessageInNewThread(final int selectedRow, final List<Long> letMessages) {	
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						reProcessMessage(letMessages,selectedRow);
					}
				});
			}
		}, 10);
	}

	private void reProcessMessage(List<Long> letMessages, int selectedRow) {
		String xmlData = "";
		int spoolId = 0;
		String header = "";
		LetSpool letSpool = (LetSpool) jDeviceNameCombo.getSelectedItem();
		for (int i = 0; i < letMessages.size(); i++) {
			Long msgId = letMessages.get(i);
			LetMessage letMessage = ServiceFactory.getDao(LetMessageDao.class).findByKey(msgId);
			xmlData = letMessage.getXmlMessageBody();
			spoolId = letMessage.getSpoolId();
			if (xmlData != null) {
				getDao(LetMessageDao.class).removeByKey(letMessage.getMessageId());
				logUserAction(REMOVED, letMessage);
				header = letMessage.getMessageHeader();
				Socket clientSocket = getClientSocket(getLetPropertyBean().getHostName(), letSpool.getPortNumber());
				if (clientSocket != null) {
					sendToServer(xmlData, header, msgId.toString(), clientSocket);
				}
			}
		}
		getLetMessageData((LetSpool) jDeviceNameCombo.getSelectedItem());
		setMessage("Files reprocessing complete");
	}

	private void sendToServer(String xmlData, String header, String msgId, Socket clientSocket) {
		HandHeldDeviceXmlSendThread connectionThread = new HandHeldDeviceXmlSendThread(clientSocket,header.trim(), xmlData.trim(), true);
		connectionThread.start();
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getLogger().info("Processed NAK message " + msgId);
	}

	public Socket getSocket() {
		return _socket;
	}

	private Socket getClientSocket(String hostName, int portNumber) {
		if (_socket == null || _socket.isClosed() || !_socket.isConnected()) {
			try {
				_socket = TCPSocketFactory.getSocket(hostName, portNumber, 20000);

			} catch (IOException ex) {
				ex.printStackTrace();
				getLogger().error(ex, "Unable to create a socket connection");
			}
		}
		return _socket;
	}

	private JButton getDeleteButton() {
		if (jDeleteBtn == null) {
			jDeleteBtn = new JButton();
			jDeleteBtn.setSize(100, 40);
			jDeleteBtn.setName("deleteButton");
			jDeleteBtn.setText(Message.get("DELETE"));
			jDeleteBtn.setLocation(640, 525);
			jDeleteBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onDelete();
				}
			});
		}
		return jDeleteBtn;
	}

	protected void onDelete() {
		clearErrorMessage();
		int indexOfMsgId = 0;
		DefaultTableModel model = (DefaultTableModel) jLetMessageTable.getModel();

		if (jLetMessageTable.getSelectedRow() != -1) {
			if (MessageDialog.confirm(this,"Are you sure you want to delete selected records?")) {

				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								deleteRecords();
							}
						});
					}
				}, 10);
			}

		}else{
			setErrorMessage("Please select a record to delete");
		}
	}

	private void deleteRecords() {
		clearErrorMessage();
		int indexOfMsgId = 0;
		int[] rows = jLetMessageTable.getSelectedRows();
		jLetMessageTable.getColumnModel().addColumn(columnsMap.get(""));
		Enumeration<TableColumn> enumeration = jLetMessageTable.getColumnModel().getColumns();
		while (enumeration.hasMoreElements()){
			TableColumn column = enumeration.nextElement();
			if(column.getHeaderValue().toString().equalsIgnoreCase("")){
				indexOfMsgId = column.getModelIndex();
			}
		}
		for (int i = 0; i < rows.length; i++) {
			String messageId = (String) jLetMessageTable.getModel().getValueAt(rows[i], indexOfMsgId);
			getDao(LetMessageDao.class).removeByKey(Long.parseLong(messageId));
		}
		setMessage("Records deleted successfully");
		getLetMessageData((LetSpool) jDeviceNameCombo.getSelectedItem());
	}

	private JButton getUpLoadButton() {
		if (jUploadBtn == null) {
			jUploadBtn = new JButton();
			jUploadBtn.setSize(100, 40);
			jUploadBtn.setName("loadButton");
			jUploadBtn.setText(Message.get("UPLOAD"));
			jUploadBtn.setLocation(740, 525);
			jUploadBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onUpload();
				}
			});
		}
		return jUploadBtn;
	}

	private void onUpload() {
		try {
			LetSpool letSpool = (LetSpool) jDeviceNameCombo.getSelectedItem();
			clearErrorMessage();
			LoadXmlFileDialog loadXmlFileDialog = new LoadXmlFileDialog(
					this.getMainWindow(), getMainWindow(),letSpool.getPortNumber(),
					"Upload LET XML File ", "Upload LET XML File");

			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			final Dimension screenSize = toolkit.getScreenSize();
			final int x = (screenSize.width - loadXmlFileDialog.getWidth()) / 2;
			final int y = (screenSize.height - loadXmlFileDialog.getHeight()) / 2;
			loadXmlFileDialog.setLocation(x, y);
			loadXmlFileDialog.setModal(true);
			loadXmlFileDialog.setVisible(true);
			getLetMessageData((LetSpool) jDeviceNameCombo.getSelectedItem());
			if(loadXmlFileDialog.getUploadStatus().equalsIgnoreCase(UploadStatusType.PASS.toString())){
				setMessage("........................File Upload finished ...................");
			}else if(loadXmlFileDialog.getUploadStatus().equalsIgnoreCase(UploadStatusType.FAIL.toString())){
				setErrorMessage("An error Occurred while uploading LET xml file");
			}

		} catch (Exception ex) {
			setErrorMessage("An error Occurred while uploading LET xml file");
			ex.printStackTrace();
			getLogger().error(ex, "An error Occurred while uploading LET xml file");
		}
	}

	private JButton getDownloadButton() {
		if (jDownLoadBtn == null) {
			jDownLoadBtn = new JButton();
			jDownLoadBtn.setSize(140, 40);
			jDownLoadBtn.setName("downloadButton");
			jDownLoadBtn.setText(Message.get("DOWNLOAD"));
			jDownLoadBtn.setLocation(840, 525);
			jDownLoadBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onDownload();
				}
			});
		}
		return jDownLoadBtn;
	}

	private JRadioButton getXmlRadioButton() {
		if (jXmlRadioBtn == null) {
			jXmlRadioBtn = new JRadioButton("XML", true);
			jXmlRadioBtn.setSize(50, 40);
			jXmlRadioBtn.setName("xmlRadioButton");
			jXmlRadioBtn.setLocation(880, 565);
		}
		return jXmlRadioBtn;
	}

	private JRadioButton getTextRadioButton() {
		if (jTextRadioBtn == null) {
			jTextRadioBtn = new JRadioButton();
			jTextRadioBtn.setSize(50, 40);
			jTextRadioBtn.setName("textRadioButton");
			jTextRadioBtn.setText("Text");
			jTextRadioBtn.setLocation(930, 565);
		}
		return jTextRadioBtn;
	}

	private void getLetMessageData(final LetSpool letSpool) {
		clearErrorMessage();
		firstPageAction();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						getLetData(letSpool);
					}
				});
			}
		}, 10);
	}

	private void getLetData(LetSpool letSpool) {

		Object selectedItem = getStatusElement().getComponent().getSelectedItem();
		if (selectedItem instanceof LetTotalStatus) {
			searchStr = ((LetTotalStatus) selectedItem).name();
		} else {
			searchStr = "%";
		}

		List<LetMessage> letMsgList = new ArrayList<LetMessage>();
		String productSearchTxt = jFilterByProductTxt.getText();
		
		totalMsgCount = getDao(LetMessageDao.class).getTotalLetMsgCount(letSpool.getSpoolId(), searchStr.equals("%") ? "ALL" : searchStr, productSearchTxt);
		int pageFactor = totalMsgCount % ITEMS_PER_PAGE == 0 ? 0 : 1;
		maxPageIndex = totalMsgCount / ITEMS_PER_PAGE + pageFactor;
		totalPageCount.setText(String.format("/ %d", maxPageIndex));	
		if (maxPageIndex <= 1) {
			nextPage.setEnabled(false);
			lastPage.setEnabled(false);
		}
		List<Object[]> tempMsgList = getDao(LetMessageDao.class).findAllMessages(letSpool.getSpoolId(),searchStr,productSearchTxt.toUpperCase()+"%", startIndex, endIndex);

		ListIterator<Object[]> litr1 = tempMsgList.listIterator();
		while (litr1.hasNext()) {
			Object[] obj = litr1.next();

			if (obj != null && obj.length > 0) {
				LetMessage letMessage = new LetMessage();

				for (int i = 0; i < obj.length; i++) {
					letMessage.setMessageId((Long) obj[0]);
					letMessage.setTotalStatus((String) obj[1]);
					letMessage.setTerminalId((String) obj[2]);
					letMessage.setIpAddress((String) obj[3]);
					letMessage.setProductId((String) obj[4]);
					letMessage.setActualTimestamp((Timestamp) obj[5]);
					letMessage.setDuration((Double.valueOf(obj[6].toString())));
					letMessage.setBuildCode((String) obj[7]);
					letMessage.setMacAddress((String) obj[8]);
					letMessage.setMessageHeader((String) obj[9]);
					letMessage.setMessageType((String) obj[10]);
					letMessage.setMessageReply((String) obj[11]);
				}
				letMsgList.add(letMessage);
			}
		}
		setTableModel(letMsgList);
		ListSelectionModel selectionModel = jLetMessageTable.getSelectionModel();
		selectionModel.setSelectionInterval(selectedRow, selectedRow);
		jLetMessageTable.changeSelection(selectedRow, selectedRow, false, false);
		jLetMessageTable.setSelectionBackground(Color.YELLOW);
		setInitialColumnModel();
	}

	private void setTableModel(List<LetMessage> letMsgList) {
		TableColumn column = null;
		DefaultTableModel letMsgTableModel = createModel();
		for (Object letMsg : letMsgList) {
			letMsgTableModel.addRow(createLetMessageData((LetMessage) letMsg));
		}
		jLetMessageTable.setModel(letMsgTableModel);
		for (int i = 0; i < letMsgTableModel.getColumnCount(); i++) {
			column = jLetMessageTable.getColumnModel().getColumn(i);
			column.setPreferredWidth(120);
		}
	}

	public DefaultTableModel createModel() {
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model.addColumn(LetColumnNames.SEND_STATUS.toString());
		model.addColumn(LetColumnNames.TERMINAL_ID.toString());
		model.addColumn(LetColumnNames.IP_ADDRESS);
		model.addColumn(LetColumnNames.PRODUCT_ID.toString());
		model.addColumn(LetColumnNames.ACTUAL_TIMESTAMP.toString());
		model.addColumn(LetColumnNames.PROCESSING_TIME.toString());
		model.addColumn(LetColumnNames.BUILD_CODE.toString());
		model.addColumn(LetColumnNames.MAC_ADDRESS.toString());
		model.addColumn(LetColumnNames.MESSAGE_HEADER.toString());
		model.addColumn(LetColumnNames.MESSAGE_TYPE.toString());
		model.addColumn(LetColumnNames.MESSAGE_REPLY.toString());
		model.addColumn("");

		return model;
	}

	private Vector<String> createLetMessageData(LetMessage letMsg) {

		Vector<String> lotData = new Vector<String>();

		lotData.addElement(letMsg.getTotalStatus());
		lotData.addElement(letMsg.getTerminalId());
		lotData.addElement(letMsg.getIpAddress());
		lotData.addElement(letMsg.getProductId());
		lotData.addElement(letMsg.getActualTimestamp().toString());
		lotData.addElement(String.valueOf(letMsg.getDuration()));
		lotData.addElement(letMsg.getBuildCode());
		lotData.addElement(letMsg.getMacAddress());
		lotData.addElement(letMsg.getMessageHeader());
		lotData.addElement(letMsg.getMessageType());
		lotData.addElement(letMsg.getMessageReply());
		lotData.addElement(String.valueOf(letMsg.getMessageId()));

		return lotData;
	}

	private void onDownload() {
		long startTime = System.currentTimeMillis();
		clearErrorMessage();
		try {
			if (chkRowSelected()) {
				JFileChooser chooser = new JFileChooser();
				chooser.addChoosableFileFilter(new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						}
						if (jXmlRadioBtn.isSelected()) {
							return f.getName().toLowerCase().endsWith(LETDataTag.XML_FILE_EXTENSION);
						} else {
							return f.getName().toLowerCase().endsWith(LETDataTag.TEXT_FILE_EXTENSION);
						}
					}

					public String getDescription() {
						if (jXmlRadioBtn.isSelected()) {
							return LETDataTag.XML_FILE_EXTENSION;
						} else {
							return LETDataTag.TEXT_FILE_EXTENSION;
						}
					}
				});

				String xmlData = "";
				String fileExtention;
				BufferedOutputStream bufOut = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String nowDate = sdf.format(new Date());
				String fileName = LETDataTag.LET_MESSAGE_FILE_NAME;

				if (jXmlRadioBtn.isSelected()) {
					fileExtention = LETDataTag.XML_FILE_EXTENSION;
				} else {
					fileExtention = LETDataTag.TEXT_FILE_EXTENSION;
				}

				File file = new File(fileName + nowDate + fileExtention);
				chooser.setSelectedFile(file);
				int rtnBtn = chooser.showSaveDialog(this);
				if (rtnBtn == JFileChooser.APPROVE_OPTION) {
					handleApprove(chooser, bufOut);
				} 
			} else {
				int[] rows = jLetMessageTable.getSelectedRows();
				if (rows.length > 1) {
					setErrorMessage("Select only one record to Download");
				} else {
					setErrorMessage("Select a record to Download");
				}
			}

			long timeTaken = System.currentTimeMillis() - startTime;
			getLogger().info("Downloading file took " + timeTaken + " ms");
		} catch (Exception e) {
			getLogger().error(e, "An error Occurred while downloading file");
			e.printStackTrace();
			setErrorMessage("An error Occurred while downloading file");
		}
	}

	private void handleApprove(JFileChooser chooser, BufferedOutputStream bufOut) throws IOException {
		String xmlData;
		int indexOfMsgId = 0;
		int selectedColumn = 5;
		jLetMessageTable.getColumnModel().addColumn(columnsMap.get(""));
		Enumeration<TableColumn> enumeration = jLetMessageTable.getColumnModel().getColumns();
		while (enumeration.hasMoreElements()) {
			TableColumn column = enumeration.nextElement();
			if(column.getHeaderValue().toString().equalsIgnoreCase("")){
				indexOfMsgId = column.getModelIndex();
			}
		}
		int selectedRow = jLetMessageTable.getSelectedRow();
		String messageId = (String) jLetMessageTable.getModel().getValueAt(selectedRow, indexOfMsgId);
		LetMessage letMessage = getDao(LetMessageDao.class).findByKey(Long.parseLong(messageId));

		if (letMessage.getXmlMessageBody() != null) {
			xmlData = letMessage.getXmlMessageBody();
		} else {
			xmlData = letMessage.getExceptionMessageBody();
		}

		try {
			bufOut = new BufferedOutputStream(new FileOutputStream(new File(chooser.getSelectedFile().getPath())));
			bufOut.write(xmlData.getBytes());
			bufOut.flush();
			bufOut.close();
			setMessage("File downloaded successfully.");
			getLogger().info("File downloaded successfully.");
		} catch (java.io.IOException e) {
			setErrorMessage("Failed creating the file. Please confirm whether the file is used by other applications.");
			getLogger().error(e, "Failed creating the file. Please confirm whether the file is used by other applications.");
			e.printStackTrace();
		} finally {
			if (bufOut != null) {
				bufOut.close();
				bufOut = null;
			}
		}
		jLetMessageTable.getColumnModel().removeColumn(columnsMap.get(""));
	}

	private boolean chkRowSelected() {
		int[] rows = jLetMessageTable.getSelectedRows();
		if (rows.length != 1) {
			return false;
		} else {
			return true;
		}
	}

	public void actionPerformed(ActionEvent arg0) {}
	
	public static LetPropertyBean getLetPropertyBean() {
		return PropertyService.getPropertyBean(LetPropertyBean.class);
	}
	
	public Logger getLogger() {
		return Logger.getLogger(this.getApplicationId());
	}
	
	private JPanel getAutoRefreshPane() {
		jAutoRefreshLbl = new JLabel("Auto Refresh");
		jAutoRefreshOn = new JRadioButton("ON");
		jAutoRefreshOff = new JRadioButton("OFF");

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(jAutoRefreshOn);
		buttonGroup.add(jAutoRefreshOff);
		jAutoRefreshOff.setSelected(true);
		// add refresh actions
		jAutoRefreshOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				disableFilterButtons(true);
				autoRefreshLetMessageData();
			}
		});

		jAutoRefreshOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				disableFilterButtons(false);
			}
		});

		JPanel buttonPane = new JPanel(new FlowLayout());
		buttonPane.add(jAutoRefreshLbl);
		buttonPane.add(jAutoRefreshOn);
		buttonPane.add(jAutoRefreshOff);
		buttonPane.setSize(200, 40);
		buttonPane.setLocation(30, 525);
		buttonPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		return buttonPane;
	}

	/**
	 * This method will be used to refresh the Let screen on defined intervals.
	 */
	private void autoRefreshLetMessageData() {
		clearErrorMessage();
		final Timer autoRefreshTimer = new Timer();
		autoRefreshTimer.schedule(new TimerTask() {
			public void run() {
				if (jAutoRefreshOff.isSelected()) {
					autoRefreshTimer.cancel();
				} else {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							firstPageAction();
							getLetData((LetSpool) jDeviceNameCombo.getSelectedItem());
							jLetMessageScrollPane.repaint();
						}
					});
				}
			}
		}, 10, Integer.parseInt(getLetPropertyBean().getDefaultAutoRefreshTime()));
	}
	
	/**
	 * This method will be used to enable/disable buttons based on auto refresh
	 * toggles.
	 * 
	 * @param isAutoRefreshOn
	 */
	private void disableFilterButtons(boolean isAutoRefreshOn) {
		jDatePicker.setVisible(!isAutoRefreshOn);
		jSetCurrentTime.setVisible(!isAutoRefreshOn);
		jHourChooser.setVisible(!isAutoRefreshOn);
		jMinChooser.setVisible(!isAutoRefreshOn);
		jSecChooser.setVisible(!isAutoRefreshOn);
		jMsecChooser.setVisible(!isAutoRefreshOn);

		jHourLbl.setVisible(!isAutoRefreshOn);
		jMinLbl.setVisible(!isAutoRefreshOn);
		jSecLbl.setVisible(!isAutoRefreshOn);
		jMseclbl.setVisible(!isAutoRefreshOn);
		jYearLbl.setVisible(!isAutoRefreshOn);
	}

	private JPanel getButtonPane() {
		firstPage.setEnabled(false);
		prevPage.setEnabled(false);
		currentPage.setText("1");

		JPanel po = new JPanel();
		po.add(currentPage);
		po.add(totalPageCount);
		JPanel buttonPane = new JPanel(new GridLayout(1, 4, 2, 2));
		for (JComponent component : Arrays.asList(firstPage, prevPage, po, nextPage, lastPage)) {
			buttonPane.add(component);
		}
		buttonPane.setSize(250, 40);
		buttonPane.setLocation(260, 525);

		return buttonPane;
	}
	
	private final JButton firstPage = new JButton(new AbstractAction("|<") {
		public void actionPerformed(ActionEvent e) {
			firstPageAction();
			getLetData((LetSpool) jDeviceNameCombo.getSelectedItem());
		}
	});

	private void firstPageAction() {
		startIndex = 1;
		endIndex = ITEMS_PER_PAGE;
		currentPage.setText("1");
		firstPage.setEnabled(false);
		prevPage.setEnabled(false);
		nextPage.setEnabled(true);
		lastPage.setEnabled(true);
	}

	private final JButton prevPage = new JButton(new AbstractAction("<") {
		public void actionPerformed(ActionEvent e) {
			startIndex = startIndex - ITEMS_PER_PAGE;
			endIndex = endIndex - ITEMS_PER_PAGE;
			currentPage.setText(String.valueOf(endIndex / ITEMS_PER_PAGE));
			getLetData((LetSpool) jDeviceNameCombo.getSelectedItem());

			nextPage.setEnabled(true);
			lastPage.setEnabled(true);
			if (startIndex == 1) {
				prevPage.setEnabled(false);
				firstPage.setEnabled(false);
			}
		}
	});

	private final JButton nextPage = new JButton(new AbstractAction(">") {
		public void actionPerformed(ActionEvent e) {
			startIndex = startIndex + ITEMS_PER_PAGE;
			endIndex = endIndex + ITEMS_PER_PAGE;
			currentPage.setText(String.valueOf(endIndex / ITEMS_PER_PAGE));
			getLetData((LetSpool) jDeviceNameCombo.getSelectedItem());

			prevPage.setEnabled(true);
			firstPage.setEnabled(true);
			if (endIndex == ITEMS_PER_PAGE * maxPageIndex) {
				nextPage.setEnabled(false);
				lastPage.setEnabled(false);
			}
		}
	});

	private final JButton lastPage = new JButton(new AbstractAction(">|") {
		public void actionPerformed(ActionEvent e) {
			endIndex = ITEMS_PER_PAGE * maxPageIndex;
			startIndex = (endIndex - ITEMS_PER_PAGE) + 1;
			currentPage.setText(String.valueOf(maxPageIndex));
			getLetData((LetSpool) jDeviceNameCombo.getSelectedItem());

			prevPage.setEnabled(true);
			firstPage.setEnabled(true);
			nextPage.setEnabled(false);
			lastPage.setEnabled(false);
		}
	});

}
