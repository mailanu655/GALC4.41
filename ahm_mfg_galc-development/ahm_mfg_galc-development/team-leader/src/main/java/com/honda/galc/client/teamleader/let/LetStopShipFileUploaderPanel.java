package com.honda.galc.client.teamleader.let;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.let.util.AsciiDocument;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetPassCriteriaDao;
import com.honda.galc.dao.product.LetPassCriteriaMtoDao;
import com.honda.galc.dao.product.LetPassCriteriaProgramDao;
import com.honda.galc.entity.product.LetPassCriteria;
import com.honda.galc.entity.product.LetPassCriteriaId;
import com.honda.galc.entity.product.LetPassCriteriaMto;
import com.honda.galc.entity.product.LetPassCriteriaMtoId;
import com.honda.galc.entity.product.LetPassCriteriaProgram;
import com.honda.galc.message.Message;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ExtensionFileFilter;

/**
 * <h3>LetStopShipFileUploaderPanel</h3>
 * <h3>Class description</h3>
 * <p>
 * This screen will be responsible for uploading YMTO inspection details
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
 * <TR>
 * <TD>vcc01419</TD>
 * <TD>Sept 11, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 *
 * </TABLE>
 * 
 * @see
 * @version 0.1
 * @author vcc01419
 * @since Sept 11, 2017
 */
public class LetStopShipFileUploaderPanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	private static final int MAX_NANO_SECS = 999999000;
	private static final String YMT_HEADER = "YMT";
	private static final String STOP_SHIP_TYPE_HEADER = "Stop Ship Type";
	private static final String INPSECTION_NAME_HEADER = "Inpsection Name";
	
	private static final Font font = new Font("Verdana", Font.BOLD, 12);
	
	private JPanel mainPanel = null;
	private JLabel lblTitle = null;
	private JButton jSaveBtn = null;
	private JButton jValidateBtn = null;
	private JButton jClearBtn = null;
	private File csvFile;
	private JButton jBrowseBtn;

	private LetPropertyBean letPropertyBean;
	private ProgramCategoryColorExpanation programCategoryColorExpanation = null;
	private LetProgramCategoryUtility letProgramCategoryUtility = null;
	private LetUtil letUtil = null;
	private Vector<Hashtable<String, String>> colorExpList = null;
	private Map<String, List<CSVRecord>> csvRecordMap; 
	private Map<String, LetPassCriteriaProgram> passCriteriaMap; 
	private List<LetPassCriteriaMto> mtoCriteriaList;
	private List<LetPassCriteria> criteriaList;
	private Set<String> notFoundInspectionSet;

	private JLabel jfileNameLbl = null;
	private JTextField jFileTxtField = null;
	private JLabel jfileTypeLbl = null;
	@SuppressWarnings("rawtypes")
	private JComboBox jFileTypeComboBox = null;
	
	private JLabel lblEffective = null;
	private JTextField txtEffDate = null;
	private JTextField txtEffTime = null;
	private JLabel lblExpiration = null;
	private JTextField txtExpDate = null;
	private JTextField txtExpTime = null;
	private JLabel lblEffExplainDate = null;
	private JLabel lblEffExplainTime = null;
	private JLabel lblExpExplainDate = null;
	private JLabel lblExpExplainTime = null;
	private JLabel lblEffExplain = null;
	private JLabel lblExpExplain = null;
	private JProgressBar jProgressBar;
	private JLabel jProgressBarLbl;
	
	private String effectiveDateStr = "";
	private String expirationDateStr = "";
	private final SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
	private final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public LetStopShipFileUploaderPanel(String screenName, int keyEvent, MainWindow mainWindow) {
		super(screenName, keyEvent, mainWindow);
		initialize();
	}

	public LetStopShipFileUploaderPanel(TabbedMainWindow mainWindow) {
		super("Let Stop Ship File Uploader Panel", KeyEvent.VK_P, mainWindow);
		initialize();
	}

	private void initialize() {
		letUtil = new LetUtil();
		letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, getApplicationId());
		letProgramCategoryUtility = new LetProgramCategoryUtility(getApplicationId());
		colorExpList = letProgramCategoryUtility.getColorExplanation(letPropertyBean.getLetPgmCategories());
		setLayout(new BorderLayout());
		add(getMainPanel(), BorderLayout.CENTER);
        populatePassCriteriaMap();
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(null);
			mainPanel.add(getLblTitle(), null);

			mainPanel.add(getLblEffective(), null);
			mainPanel.add(getTxtEffDate(), null);
			mainPanel.add(getTxtEffTime(), null);
			mainPanel.add(getLblExpiration(), null);
			mainPanel.add(getTxtExpDate(), null);
			mainPanel.add(getTxtExpTime(), null);
			mainPanel.add(getLblEffExplainDate(), null);
			mainPanel.add(getLblEffExplainTime(), null);
			mainPanel.add(getLblExpExplainDate(), null);
			mainPanel.add(getLblExpExplainTime(), null);
			mainPanel.add(getLblEffExplain(), null);
			mainPanel.add(getLblExpExplain(), null);

			mainPanel.add(getBrowseButton(), null);
			mainPanel.add(getFileNameLbl(), null);
			mainPanel.add(getFilePathTxtBox(), null);
			mainPanel.add(getFileTypeLbl(), null);
			mainPanel.add(getFileTypeComboBox(), null);

			mainPanel.add(getSaveBtn(), null);
			mainPanel.add(getClearBtn(), null);
			mainPanel.add(getValidateBtn(), null);
			mainPanel.add(getProgressBar(), null);
			mainPanel.add(jProgressBarLbl, null);
			
			mainPanel.add(getProgramCategoryColorExpanation(), null);
		}
		return mainPanel;
	}

	private JLabel getLblTitle() {
		if (lblTitle == null) {
			lblTitle = new JLabel();
			lblTitle.setBounds(132, 13, 796, 29);
			lblTitle.setText("Let Stop Ship File Uploader");
			lblTitle.setFont(new Font("Dialog", java.awt.Font.BOLD, 24));
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblTitle;
	}

	private JButton getValidateBtn() {
		if (jValidateBtn == null) {
			jValidateBtn = new JButton();
			jValidateBtn.setEnabled(false);
			jValidateBtn.setSize(90, 35);
			jValidateBtn.setText("Validate");
			jValidateBtn.setLocation(500, 250);
			jValidateBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					validateCsvFile();
				}
			});
		}
		return jValidateBtn;
	}
	
	private JButton getSaveBtn() {
		if (jSaveBtn == null) {
			jSaveBtn = new JButton();
			jSaveBtn.setEnabled(false);
			jSaveBtn.setSize(90, 35);
			jSaveBtn.setText("Save");
			jSaveBtn.setLocation(600, 250);
			jSaveBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onSaveCriteria();
				}
			});
		}
		return jSaveBtn;
	}

	private JButton getClearBtn() {
		if (jClearBtn == null) {
			jClearBtn = new JButton();
			jClearBtn.setSize(90, 35);
			jClearBtn.setText("Clear");
			jClearBtn.setLocation(700, 250);
			jClearBtn.setEnabled(false);
			jClearBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearAllFields();
				}
			});
		}
		return jClearBtn;
	}

	private ProgramCategoryColorExpanation getProgramCategoryColorExpanation() {
		if (programCategoryColorExpanation == null) {
			programCategoryColorExpanation = new ProgramCategoryColorExpanation(colorExpList);
			programCategoryColorExpanation.setBounds(795, 58, 100, 50);
		}
		return programCategoryColorExpanation;
	}

	private JButton getBrowseButton() {
		jBrowseBtn = new JButton("Browse");
		jBrowseBtn.setFont(font);
		jBrowseBtn.setSize(100, 25);
		jBrowseBtn.setLocation(795, 155);

		jBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileSelection();
			}
		});
		return jBrowseBtn;
	}

	private JLabel getFileNameLbl() {
		jfileNameLbl = new JLabel("File Name:");
		jfileNameLbl.setSize(75, 35);
		jfileNameLbl.setLocation(395, 150);
		jfileNameLbl.setFont(font);

		return jfileNameLbl;
	}

	private JTextField getFilePathTxtBox() {
		jFileTxtField = new JTextField();
		jFileTxtField.setSize(295, 25);
		jFileTxtField.setLocation(495, 155);
		jFileTxtField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				enableValidateBtn();
			}
			public void insertUpdate(DocumentEvent e) {
				enableValidateBtn();
			}
			public void changedUpdate(DocumentEvent e) {
				enableValidateBtn();
			}
		});
		return jFileTxtField;
	}
	
	private JLabel getFileTypeLbl() {
		jfileTypeLbl = new JLabel("Files of Type:");
		jfileTypeLbl.setSize(100, 35);
		jfileTypeLbl.setLocation(395, 185);
		jfileTypeLbl.setFont(font);

		return jfileTypeLbl;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox getFileTypeComboBox() {
		jFileTypeComboBox = new JComboBox();
		jFileTypeComboBox.setSize(295, 25);
		jFileTypeComboBox.setLocation(495, 190);
		jFileTypeComboBox.setFont(font);
		jFileTypeComboBox.addItem("CSV File");
		jFileTypeComboBox.setSelectedIndex(0);
		return jFileTypeComboBox;
	}

	private void fileSelection() {
		getMainWindow().clearMessage();
		
		JFileChooser fileChooser = new JFileChooser("");
		FileFilter filter = new ExtensionFileFilter("CSV", "csv");
		fileChooser.setFileFilter(filter);
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.requestFocus();
		fileChooser.requestFocusInWindow();
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			csvFile = fileChooser.getSelectedFile();
			jFileTxtField.setText(csvFile.getAbsolutePath());
			jClearBtn.setEnabled(true);
			jValidateBtn.setEnabled(true);
		}
	}

	private void clearAllFields() {
		jFileTxtField.setText(null);
		jClearBtn.setEnabled(false);
		jSaveBtn.setEnabled(false);
		jValidateBtn.setEnabled(false);
		txtEffDate.setText(null);
		txtEffTime.setText(null);
		txtExpDate.setText(null);
		txtExpTime.setText(null);
		getMainWindow().clearMessage(); 
	}

	private JLabel getLblEffExplainDate() {
		if (lblEffExplainDate == null) {
			lblEffExplainDate = new JLabel();
			lblEffExplainDate.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			lblEffExplainDate.setBounds(150, 145, 70, 15);
			lblEffExplainDate.setText("YYYYMMDD");
		}
		return lblEffExplainDate;
	}

	private JLabel getLblEffExplainTime() {
		if (lblEffExplainTime == null) {
			lblEffExplainTime = new JLabel();
			lblEffExplainTime.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			lblEffExplainTime.setBounds(245, 145, 70, 15);
			lblEffExplainTime.setText("HHMMSS");
		}
		return lblEffExplainTime;
	}

	private JLabel getLblEffective() {
		if (lblEffective == null) {
			lblEffective = new JLabel();
			lblEffective.setFont(font);
			lblEffective.setBounds(40, 155, 110, 30);
			lblEffective.setText(Message.get("EFFECTIVE_DATE"));
		}
		return lblEffective;
	}

	private JTextField getTxtEffDate() {
		if (txtEffDate == null) {
			txtEffDate = new JTextField();
			txtEffDate.setDocument(new AsciiDocument(8));
			txtEffDate.setBounds(150, 160, 90, 25);
		}
		return txtEffDate;
	}

	private JTextField getTxtEffTime() {
		if (txtEffTime == null) {
			txtEffTime = new JTextField();
			txtEffTime.setDocument(new AsciiDocument(6));
			txtEffTime.setBounds(245, 160, 90, 25);
		}
		return txtEffTime;
	}

	private JLabel getLblEffExplain() {
		if (lblEffExplain == null) {
			lblEffExplain = new JLabel();
			lblEffExplain.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			lblEffExplain.setBounds(40, 185, 250, 40);
			lblEffExplain.setText(Message.get("ExplainEff"));
		}
		return lblEffExplain;
	}

	private JLabel getLblExpExplainDate() {
		if (lblExpExplainDate == null) {
			lblExpExplainDate = new JLabel();
			lblExpExplainDate.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			lblExpExplainDate.setBounds(150, 250, 70, 15);
			lblExpExplainDate.setText("YYYYMMDD");
		}
		return lblExpExplainDate;
	}

	private JLabel getLblExpExplainTime() {
		if (lblExpExplainTime == null) {
			lblExpExplainTime = new JLabel();
			lblExpExplainTime.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			lblExpExplainTime.setBounds(245, 250, 70, 15);
			lblExpExplainTime.setText("HHMMSS");
		}
		return lblExpExplainTime;
	}

	private JLabel getLblExpiration() {
		if (lblExpiration == null) {
			lblExpiration = new JLabel();
			lblExpiration.setFont(font);
			lblExpiration.setBounds(40, 260, 110, 30);
			lblExpiration.setText(Message.get("EXPIRATION_DATE"));
		}
		return lblExpiration;
	}

	private JTextField getTxtExpDate() {
		if (txtExpDate == null) {
			txtExpDate = new JTextField();
			txtExpDate.setDocument(new AsciiDocument(8));
			txtExpDate.setBounds(150, 265, 90, 25);
		}
		return txtExpDate;
	}

	private JTextField getTxtExpTime() {
		if (txtExpTime == null) {
			txtExpTime = new JTextField();
			txtExpTime.setDocument(new AsciiDocument(6));
			txtExpTime.setBounds(245, 265, 90, 25);
		}
		return txtExpTime;
	}

	private JLabel getLblExpExplain() {
		if (lblExpExplain == null) {
			lblExpExplain = new JLabel();
			lblExpExplain.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			lblExpExplain.setBounds(40, 290, 250, 40);
			lblExpExplain.setText(Message.get("ExplainExp"));
		}
		return lblExpExplain;
	}
	
	private JProgressBar getProgressBar() {
		jProgressBarLbl = new JLabel("File Upload in Progress.......");
		jProgressBarLbl.setFont(font);
		jProgressBarLbl.setSize(390, 25);
		jProgressBarLbl.setLocation(500, 320);
		jProgressBarLbl.setVisible(false);

		jProgressBar = new JProgressBar();
		jProgressBar.setSize(390, 25);
		jProgressBar.setLocation(500, 350);
		jProgressBar.setOpaque(true);
		jProgressBar.setVisible(false);
		jProgressBar.setStringPainted(true);
		jProgressBar.setForeground(Color.GREEN);
		jProgressBar.setFont(new Font("Dialog", Font.BOLD, 15));

		return jProgressBar;
	}

	public void actionPerformed(ActionEvent e) {
		
	}
	
	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		Logger.getLogger(getApplicationId()).info("LetStopShipFileUploaderPanel is selected");
	}
	
	
	/**
	 * This method will be used to validate & parse CSV file
	 */
	private void validateCsvFile() {

		getMainWindow().clearMessage();
		CSVParser csvParser = null;
		try {
			csvParser = new CSVParser(new FileReader(StringUtils.trim(jFileTxtField.getText())), CSVFormat.EXCEL
					.withHeader(YMT_HEADER, STOP_SHIP_TYPE_HEADER, INPSECTION_NAME_HEADER).withIgnoreHeaderCase());

			csvRecordMap = new HashMap<String, List<CSVRecord>>();
			int headerCounter = 0;
			for (CSVRecord csvRecord : csvParser) {

				if (headerCounter == 0) { // Ignore header
					headerCounter++;
					continue;
				}

				if (StringUtils.isBlank(csvRecord.get(YMT_HEADER))
						|| StringUtils.isBlank(csvRecord.get(STOP_SHIP_TYPE_HEADER))
						|| StringUtils.isBlank(csvRecord.get(INPSECTION_NAME_HEADER))) {
					logErrorMsg("Selected CSV file is not valid");
					jSaveBtn.setEnabled(false);
					return;
				}

				String ymt = csvRecord.get(YMT_HEADER).trim();
				if (csvRecordMap.containsKey(ymt)) {
					for(CSVRecord record : csvRecordMap.get(ymt)) {
						if(StringUtils.equals(record.get(INPSECTION_NAME_HEADER), csvRecord.get(INPSECTION_NAME_HEADER))) {
							logErrorMsg("Duplicate record found for YMT = " + ymt + "     INSPECTION NAME = " + csvRecord.get(INPSECTION_NAME_HEADER));
							jSaveBtn.setEnabled(false);
							return;
						}
					}
					csvRecordMap.get(ymt).add(csvRecord);
				} else {
					List<CSVRecord> recordList = new ArrayList<CSVRecord>();
					recordList.add(csvRecord);
					csvRecordMap.put(ymt, recordList);
				}
			}
			// check if file is blank
			if (csvRecordMap.isEmpty()) {
				logErrorMsg("Selected CSV file is blank");
				jSaveBtn.setEnabled(false);
				return;
			}

			JOptionPane.showMessageDialog(this, "Selected CSV file is valid, Proceed to upload");
			jSaveBtn.setEnabled(true);
			jProgressBar.setMaximum(getTotelFileSize());
		} catch (FileNotFoundException e) {
			logException(e, "CSV file not found");
		} catch (Exception e) {
			logException(e, "Selected CSV file is not valid");
		} finally {
			try {
				csvParser.close();
			} catch (IOException e) {
				logException(e, "Failed to close CSV Parser");
			}
		}
	}
	
	/**
	 * This method will be used to persist pass criteria details
	 */
	private void onSaveCriteria() {
		getMainWindow().clearMessage(); 
		try {
			if (!validateUserInput())
				return;

			if (!validateExpEffDateForMtoAddition())
				return;

			int itDialog = JOptionPane.showConfirmDialog(this, getMappedMessage(), Message.get("SaveConfirmation"), JOptionPane.YES_NO_OPTION);
			if (itDialog != JOptionPane.YES_OPTION)
				return;
			
			mtoCriteriaList = new ArrayList<LetPassCriteriaMto>();
			criteriaList = new ArrayList<LetPassCriteria>();
			notFoundInspectionSet = new TreeSet<String>();
			
			for (Map.Entry<String, List<CSVRecord>> mapEntry : csvRecordMap.entrySet()) {
				String ymt = mapEntry.getKey();
				String modelYear = ymt.substring(0, 1);
				String modelCode = ymt.substring(1, 4);
				String modelType = ymt.substring(4);
				String modelOption = "*";

				int conditionWeight = 0;
				if (!"*".equals(modelYear)) {
					conditionWeight += 4;
				}
				if (!"*".equals(modelCode)) {
					conditionWeight += 8;
				}
				if (!"*".equals(modelType)) {
					conditionWeight += 2;
				}
				// call to populate data
				if (validateYmtoExpiration(modelYear, modelCode, modelType, modelOption)) {
					prepareMtoCriteriaDetails(modelYear, modelCode, modelType, modelOption, conditionWeight,
							mapEntry.getValue());
				}
			}
			
			// call to persist data
			persistMtoCriteriaDetails();
			showProgressBar(getTotelFileSize());
		} catch (Exception e) {
			logException(e, "An error Occurred while saving MTO pass criteria.");
		}
	}
	
	private boolean validateYmtoExpiration(String modelYear, String modelCode, String modelType, String modelOption) {
		try {
			String effecDateStr = dateFormat2.format(dateFormat1.parse(effectiveDateStr, new ParsePosition(0))) + ".000000";
			Timestamp effecDate = new Timestamp(dateFormat2.parse(effecDateStr).getTime());
			
			List<LetPassCriteriaMto> letPassCriteriaPgmList = getDao(LetPassCriteriaMtoDao.class).findLetPassCriteriaGreaterEqualEffecTime(modelYear, modelCode, modelType, modelOption, effecDate);
			
			if (letPassCriteriaPgmList != null && !letPassCriteriaPgmList.isEmpty()) {
				
				Timestamp effectiveTimestamp = letUtil.createTimestamp(effectiveDateStr);
				Timestamp expirationTimestamp = letUtil.createTimestamp(expirationDateStr, MAX_NANO_SECS);
				
				for (LetPassCriteriaMto letPassCriteriaPgm : letPassCriteriaPgmList) {

					Timestamp getEff = letPassCriteriaPgm.getId().getEffectiveTime();
					Timestamp getExp = letPassCriteriaPgm.getEndTimestamp();
					
					if (getEff.compareTo(effectiveTimestamp) < 0 && getExp.compareTo(expirationTimestamp) > 0) {
						String errorMessage = "The enforcement period of the standard which it is going to register is contained during the enforcement of the already registered standard.";
						Logger.getLogger().error(errorMessage);
						notFoundInspectionSet.add(modelYear + modelCode + modelType + "  Reason- " + errorMessage);
						return false;
					} else if (getEff.compareTo(effectiveTimestamp) < 0 && getExp.compareTo(effectiveTimestamp) > 0) {
						Calendar calExp = letUtil.createCalendar(effectiveDateStr);
						calExp.add(Calendar.SECOND, -1);
						updateExpirationTime(dateFormat2.format(calExp.getTime()) + ".999999", modelYear, modelCode, modelType, modelOption, getEff.toString(), getExp.toString());
					} else if (getEff.compareTo(expirationTimestamp) < 0 && getExp.compareTo(expirationTimestamp) > 0) {
						Calendar calEff = letUtil.createCalendar(expirationDateStr);
						calEff.add(Calendar.SECOND, +1);
						updateEffectiveTime(dateFormat2.format(calEff.getTime()) + ".000000", modelYear, modelCode, modelType, modelOption, getEff.toString(), getExp.toString());
					} else if (getEff.compareTo(effectiveTimestamp) >= 0 && getExp.compareTo(expirationTimestamp) <= 0) {
						deletePassCriteria(modelYear, modelCode, modelType, modelOption, getEff.toString());
						}
					}
			}
			return true;
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e, "An error Occurred while processing the Let Stop Ship File Uploader Panel screen");
			return false;
		}
	}
	
	private void updateEffectiveTime(String newEffecDate, String modelYear, String modelCode, String modelType,
			String modelOption, String oldEffecDate, String oldExpTime) throws ParseException {

			Timestamp newEffTimeStamp = new Timestamp(dateFormat2.parse(newEffecDate).getTime());
			Timestamp oldEffTimeStamp = new Timestamp(dateFormat2.parse(oldEffecDate).getTime());
			Timestamp oldExpTimeStamp = new Timestamp(dateFormat2.parse(oldExpTime).getTime());
			getDao(LetPassCriteriaMtoDao.class).updateLetPassCriteriaEffTime(newEffTimeStamp, modelYear, modelCode,	modelType, modelOption, oldEffTimeStamp, oldExpTimeStamp);
			getDao(LetPassCriteriaDao.class).updateEffectiveTime(newEffTimeStamp, modelYear, modelCode, modelType, modelOption,oldEffTimeStamp);
	}

	public void updateExpirationTime(String newExpTime, String modelYear, String modelCode, String modelType,
			String modelOption, String effectiveTime, String oldExpTime) throws ParseException {
			
		    Timestamp newExpTimeStamp = new Timestamp(dateFormat2.parse(newExpTime).getTime());
			Timestamp effectiveTimeStamp = new Timestamp(dateFormat2.parse(effectiveTime).getTime());
			Timestamp oldExpTimeStamp = new Timestamp(dateFormat2.parse(oldExpTime).getTime());
			getDao(LetPassCriteriaMtoDao.class).updateLetPassCriteriaExpTime(newExpTimeStamp, modelYear, modelCode,	modelType, modelOption, effectiveTimeStamp, oldExpTimeStamp);
	}

	public void deletePassCriteria(String modelYear, String modelCode, String modelType, String modelOption, String effecDate) throws ParseException {
		Timestamp effTimeStamp = new Timestamp(dateFormat2.parse(effecDate).getTime());
		getDao(LetPassCriteriaMtoDao.class).deleteLetPassCriteriaMto(modelYear, modelCode, modelType, modelOption, effTimeStamp);
		getDao(LetPassCriteriaDao.class).deleteLetPassCriteria(modelYear, modelCode, modelType, modelOption, effTimeStamp);
	}
	
	/**
	 * This method will be used to persist mto criteria details.
	 */
	private void persistMtoCriteriaDetails() {
		new Thread(new Runnable() {
			public void run() {
				getDao(LetPassCriteriaMtoDao.class).saveAll(mtoCriteriaList);
				logUserAction(SAVED, mtoCriteriaList);
				getDao(LetPassCriteriaDao.class).saveAll(criteriaList);
				logUserAction(SAVED, criteriaList);
			}
		}).start();
	}
	
	/**
	 * This method will derive total length of CSV file records
	 * 
	 * @return
	 */
	private int getTotelFileSize() {
		int totelSize = csvRecordMap.size();
		for (Map.Entry<String, List<CSVRecord>> mapEntry : csvRecordMap.entrySet()) {
			totelSize = totelSize + mapEntry.getValue().size();
		}
		return totelSize;
	}
	
	private void disableButtons(boolean disableFlag) {
		jProgressBarLbl.setVisible(disableFlag);
		jProgressBar.setVisible(disableFlag);

		jValidateBtn.setEnabled(!disableFlag);
		jBrowseBtn.setEnabled(!disableFlag);
		jClearBtn.setEnabled(!disableFlag);
		jSaveBtn.setEnabled(false);
	}
	
	/**
	 * Method will show progress bar in different thread execution.
	 * 
	 * @param maxLength
	 */
	private void showProgressBar(final int maxLength) {
		disableButtons(true);
		new Thread(new Runnable() {
			public void run() {
				int progressCounter = 0;
				while (progressCounter < maxLength) {
					progressCounter++;
					jProgressBar.setValue(progressCounter);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						logException(e, "An error Occurred while saving MTO pass criteria.");
					}
					if (progressCounter == maxLength) {
						disableButtons(false);
						getMainWindow().setMessage("File Uploaded Successfully...");
						if (!notFoundInspectionSet.isEmpty()) {
							showUnparsedInspectionSet();
						}
					}
				}
			}
		}).start();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void showUnparsedInspectionSet() {
		JScrollPane scrollpane = new JScrollPane();
		List<String> inspectionList = new ArrayList<String>();
		inspectionList.add("File parsed successfully except below YMTO/Inspections-  ");

		int counter = 1;
		for (String inspectionName : notFoundInspectionSet) {
			inspectionList.add(counter + ". " + inspectionName);
			counter++;
		}
		JList list = new JList(inspectionList.toArray());
		list.setForeground(Color.GRAY);
		scrollpane = new JScrollPane(list);
		JPanel panel = new JPanel();
		panel.add(scrollpane);
		scrollpane.getViewport().add(list);
		JOptionPane.showMessageDialog(LetStopShipFileUploaderPanel.this, scrollpane, "Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * This method will be used to populate list of {@link LetPassCriteriaMto} &
	 * {@link LetPassCriteria} to be persisted from given information.
	 * 
	 * @param modelYear
	 * @param modelCode
	 * @param modelType
	 * @param modelOption
	 * @param conditionWeight
	 * @param passCriteriaList
	 */
	private void prepareMtoCriteriaDetails(String modelYear, String modelCode, String modelType, String modelOption,
			int conditionWeight, List<CSVRecord> passCriteriaList) {
		try {
			String effDate = dateFormat2.format(letUtil.createCalendar(effectiveDateStr).getTime()) + ".000000";
			String expDate = dateFormat2.format(letUtil.createCalendar(expirationDateStr).getTime()) + ".999999";

			Timestamp effTimeStamp = new Timestamp(dateFormat2.parse(effDate).getTime());
			Timestamp expTimeStamp = new Timestamp(dateFormat2.parse(expDate).getTime());

			LetPassCriteriaMtoId letPassCriteriaMtoId = new LetPassCriteriaMtoId(modelYear, modelCode, modelType, modelOption, effTimeStamp);
			LetPassCriteriaMto letPassCriteriaMto = new LetPassCriteriaMto(letPassCriteriaMtoId, expTimeStamp, conditionWeight);
			mtoCriteriaList.add(letPassCriteriaMto); 

			for (CSVRecord csvRecord : passCriteriaList) {
				LetPassCriteriaProgram criteriaProgram = getPassCriteriaProgram(csvRecord.get(INPSECTION_NAME_HEADER));
				if (criteriaProgram == null) {
					notFoundInspectionSet.add(csvRecord.get(INPSECTION_NAME_HEADER) + "  Reason- Inspection not created");
					continue;
				}
				LetPassCriteriaId letPassCriteriaId = new LetPassCriteriaId(modelYear, modelCode, modelType, modelOption, effTimeStamp, criteriaProgram.getCriteriaPgmId());
				LetPassCriteria letPassCriteria = new LetPassCriteria(letPassCriteriaId, criteriaProgram.getInspectionDeviceType(), criteriaProgram.getCriteriaPgmAttr());
				criteriaList.add(letPassCriteria);
			}
		} catch (Exception e) {
			logException(e, "An error Occurred while saving MTO pass criteria for Product :" + modelYear + modelCode + modelType);
		}
	}
	
	/**
	 * Method will be used to validate user input details
	 * 
	 * @return
	 */
	private boolean validateUserInput() {
		// Effective Date Validation
		if ("".equals(txtEffDate.getText()) && !"".equals(txtEffTime.getText())) {
			logErrorMsg("Effective date invalid.");
			return false;
		} else if (!"".equals(txtEffDate.getText()) && "".equals(txtEffTime.getText())) {
			if (!isValidDate(txtEffDate.getText())) {
				logErrorMsg("Effective date invalid.");
				return false;
			}
		} else if (!"".equals(txtEffDate.getText()) && !"".equals(txtEffTime.getText())) {
			if (!isValidDate(txtEffDate.getText() + txtEffTime.getText())) {
				logErrorMsg("Effective time invalid.");
				return false;
			}
		}
		// Expiration Date Validation
		if ("".equals(txtExpDate.getText()) && !"".equals(txtExpTime.getText())) {
			logErrorMsg("Expiration date invalid.");
			return false;
		} else if (!"".equals(txtExpDate.getText()) && "".equals(txtExpTime.getText())) {
			if (!isValidDate(txtExpDate.getText())) {
				logErrorMsg("Expiration date invalid.");
				return false;
			}
		} else if (!"".equals(txtExpDate.getText()) && !"".equals(txtExpTime.getText())) {
			if (!isValidDate(txtExpDate.getText() + txtExpTime.getText())) {
				logErrorMsg("Expiration time invalid.");
				return false;
			}
		}
		return true;
	}

	private boolean isValidDate(String date) {
		String strFormat = new String();
		if (date == null) {
			return false;
		}
		if (date.length() == 8) {
			strFormat = "yyyyMMdd";
		} else if (date.length() == 14) {
			strFormat = "yyyyMMddHHmmss";
		} else {
			return false;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
		formatter.setLenient(false);
		ParsePosition position = new ParsePosition(0);
		Date formattedDate = formatter.parse(date, position);
		if (formattedDate == null) {
			return false;
		}
		return true;
	}
	
	private boolean validateExpEffDateForMtoAddition() 
	{
		Calendar cal = Calendar.getInstance();
		String strEffDate = txtEffDate.getText();
		String strEffTime = txtEffTime.getText();
		String strExpDate = txtExpDate.getText();
		String strExpTime = txtExpTime.getText();
		
		if ("".equals(strEffDate) && "".equals(strEffTime)) {
			effectiveDateStr = dateFormat1.format(cal.getTime());
		} else if (!"".equals(strEffDate) && "".equals(strEffTime)) {
			try {
				try {
					effectiveDateStr = dateFormat1.format(dateFormat2.parse(letUtil.getStartTime(strEffDate, letPropertyBean.getProcessLocation())));
				} catch (Exception e) {
					logException(e, "Schedule data not found");
					return false;
				}
				if (dateFormat1.parse(effectiveDateStr).before(cal.getTime())) {
					logErrorMsg("Effective time should not precede current time.");
					return false;
				}
			} catch (ParseException e) {
				logException(e, "Effective Date format not correct");
				return false;
			}
		} else {
			try {
				effectiveDateStr = strEffDate + strEffTime;
				if (dateFormat1.parse(effectiveDateStr).before(cal.getTime())) {
					logErrorMsg("Effective time should not precede current time.");
					return false;
				}
			} catch (ParseException e) {
				logException(e, "Effective Date format not correct");
				return false;
			}
		}
		
		if ("".equals(strExpDate) && "".equals(strExpTime)) {
			expirationDateStr = "99991231235959";
		} else if (!"".equals(strExpDate) && "".equals(strExpTime)) {
			try {
				expirationDateStr =dateFormat1.format(dateFormat2.parse(letUtil.getEndTime(strExpDate,letPropertyBean.getProcessLocation())));
			} catch (ParseException e) {
				logException(e, "Schedule data not found");
				return false;
			}
			if (letUtil.createCalendar(effectiveDateStr).after(letUtil.createCalendar(expirationDateStr))) {
				logErrorMsg("Effective time should not exceed expiration time.");
				return false;
			} else if (effectiveDateStr.equals(expirationDateStr)) {
				logErrorMsg("Effective time should not exceed expiration time.");
				return false;
			}
		} else {
			expirationDateStr = strExpDate + strExpTime;
			if (letUtil.createCalendar(effectiveDateStr).after(letUtil.createCalendar(expirationDateStr))) {
				logErrorMsg("Effective time should not exceed expiration time.");
				return false;
			} else if (effectiveDateStr.equals(expirationDateStr)) {
				logErrorMsg("Effective time should not exceed expiration time.");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method will map different warning code with descriptions.
	 * 
	 * @return
	 */
	private String[] getMappedMessage() {
		String[] strDialogMsg = null;
		String effectiveDate = null;
		String expirationDate = null;

		if (StringUtils.isBlank(txtEffDate.getText()) && StringUtils.isBlank(txtExpDate.getText())) {
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG0");
		} else if (StringUtils.isNotBlank(txtEffDate.getText()) && StringUtils.isBlank(txtExpDate.getText())) {
			effectiveDate = dateFormat2.format(letUtil.createCalendar(effectiveDateStr).getTime());
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG1", effectiveDate);
		} else if (StringUtils.isBlank(txtEffDate.getText()) && StringUtils.isNotBlank(txtExpDate.getText())) {
			expirationDate = dateFormat2.format(letUtil.createCalendar(expirationDateStr).getTime());
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG2", expirationDate);
		} else if (StringUtils.isNotBlank(txtEffDate.getText()) && StringUtils.isNotBlank(txtExpDate.getText())) {
			effectiveDate = dateFormat2.format(letUtil.createCalendar(effectiveDateStr).getTime());
			expirationDate = dateFormat2.format(letUtil.createCalendar(expirationDateStr).getTime());
			strDialogMsg = Message.getStrArrayMultipleLineMessage("CRITERIA_MSG3", effectiveDate, expirationDate);
		}
		return strDialogMsg;
	}
	
	/**
	 * Method will be used to Enable/Disable validate button based on condition.
	 */
	private void enableValidateBtn() {
		getMainWindow().clearMessage();
		if (StringUtils.isNotBlank(jFileTxtField.getText())) {
			jValidateBtn.setEnabled(true);
		} else {
			jValidateBtn.setEnabled(false);
		}
	}

	/**
	 * This method will be used to cache pass criteria program details.
	 */
	private void populatePassCriteriaMap() {
		passCriteriaMap = new HashMap<String, LetPassCriteriaProgram>();
		List<LetPassCriteriaProgram> criteriaPrograms = getDao(LetPassCriteriaProgramDao.class).findAll();
		for (LetPassCriteriaProgram letPassCriteriaProgram : criteriaPrograms) {
			passCriteriaMap.put(letPassCriteriaProgram.getCriteriaPgmName(), letPassCriteriaProgram);
		}
	}

	private LetPassCriteriaProgram getPassCriteriaProgram(String inspectionName) {
		return passCriteriaMap.get(inspectionName);
	}

	private void logErrorMsg(String message) {
		Logger.getLogger(getApplicationId()).error(message);
		getMainWindow().setErrorMessage(message);
	}

	private void logException(Exception e, String message) {
		Logger.getLogger(getApplicationId()).error(e, "An error Occurred while processing the Let Stop Ship File Uploader Panel screen");
		getMainWindow().setErrorMessage(message);
	}

}
