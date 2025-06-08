
package com.honda.galc.client.teamleader.let;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Dec 03, 2013
 */
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import com.honda.galc.client.teamleader.let.util.AsciiDocument;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.NumericDocument;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetInspectionProgramDao;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.message.Message;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import static com.honda.galc.service.ServiceFactory.getDao;

@SuppressWarnings(value = { "all" })
public class LetFaultResultDownloadPanel extends TabbedPanel implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel = null;
	private javax.swing.JLabel dateCommentLabel = null;
	private javax.swing.JLabel dateFormatLabel = null;
	private JLabel dateFromLabel = null;
	private JTextField dateFromText = null;
	private int datePeriod = 0;
	private JLabel dateToLabel = null;
	private JTextField dateToText = null;
	private JButton downloadButton = null;
	private JRadioButton faultResultRadioButton = null;
	private String FONT_NAME = "Dialog";
	private int FONT_SIZE = 18;
	private int iDateRange = -1;
	private JLabel inspectionProgramLabel = null;
	private JComboBox insPgmCombo = null;
	private List<LetInspectionProgram> insPgmList = null;
	private JRadioButton lotOutRadioButton = null;
	private JLabel modelLabel = null;
	private JTextField modelText = null;
	private JLabel mtoLabel = null;
	private JLabel optionLabel = null;
	private JTextField optionText = null;
	private ButtonGroup repButtonGroup = null;
	private JLabel titleLabel = null;
	private JLabel typeLabel = null;
	private JTextField typeText = null;
	private JLabel vinLabel = null;
	private JTextField vinText = null;
	private JLabel yearLabel = null;
	private JTextField yearText = null;
	private LetPropertyBean letPropertyBean=null;
	private String propReportSize=null;
	private String reqDateFrom=null;
	private String reqDateTo=null;
	private Integer reqInsPgmId=null;
	private String reqInsPgmName=null;
	private String reqModel=null;
	private String reqMto=null;
	private String reqOption=null;
	private String reqType=null;
	private String reqYear=null;
	private String resMsgId=null;
	private String sqlDateFrom=null;
	private String sqlDateTo=null;
	private byte[] resXslFoBinary;
	private String productId=null;
	private String printerTemplatePath=null;



	public LetFaultResultDownloadPanel(TabbedMainWindow mainWindow) {
		super("LET Fault Result Download Panel", KeyEvent.VK_H,mainWindow);	
		initialize();
	}

	public LetFaultResultDownloadPanel(String screenName, int keyEvent,TabbedMainWindow mainWindow) {
		super(screenName, keyEvent,mainWindow);
		initialize();
	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		Logger.getLogger(this.getApplicationId()).info("LET Fault Result Download Panel is selected");
	}

	private void changeReport() 
	{
		if (faultResultRadioButton.isSelected()) {
			if (insPgmCombo.getItemCount() > 0) {
				insPgmCombo.setSelectedIndex(0);
			}
			insPgmCombo.setEnabled(false);
			vinText.setBackground(Color.WHITE);
			vinText.setEnabled(true);
			vinText.setName("vinText");
		} else {
			insPgmCombo.setEnabled(true);
			vinText.setText("");
			vinText.setEnabled(false);
			vinText.setBackground(Color.LIGHT_GRAY);
		}
	}

	private boolean chkInputData() 
	{
		boolean inputVin = false;
		boolean selFaultResult = faultResultRadioButton.isSelected();
		if (selFaultResult) {
			if (vinText.getText().length() > 0) {
				inputVin = true;
			}
		}
		if (!inputVin) {
			dateFromText.setText(dateFromText.getText().trim());
			dateToText.setText(dateToText.getText().trim());
			if (vinText.getText().trim().length() == 0 && yearText.getText().length() == 0 && modelText.getText().length() == 0 && typeText.getText().length() == 0 && optionText.getText().length() == 0 && dateFromText.getText().length() == 0 && dateToText.getText().length() == 0) {
				if (selFaultResult) {
					setErrorMessage("Enter VIN or MTO, Date (From - To).");
					Logger.getLogger(getApplicationId()).error("Enter VIN or MTO, Date (From - To).");
				} else {
					setErrorMessage("Enter Inspection Program and Date (From - To).");
					Logger.getLogger(getApplicationId()).error("Enter Inspection Program and Date (From - To).");
				}
				return false;
			}
			if (selFaultResult) {
				if (yearText.getText().length() == 0 || modelText.getText().trim().length() == 0) {
					setErrorMessage("Enter MTO (YEAR, MODEL).");
					Logger.getLogger(getApplicationId()).error("Enter MTO (YEAR, MODEL).");
					return false;
				}
			} else if (insPgmCombo.getSelectedIndex() == 0) {
				setErrorMessage("Enter Inspection program.");
				Logger.getLogger(getApplicationId()).error("Enter Inspection program.");
				return false;
			}
			if(!validateMto()) return false;
			if (dateFromText.getText().length() == 0 || dateToText.getText().length() == 0) {
				setErrorMessage("Enter Date (From - To).");
				Logger.getLogger(getApplicationId()).error("Enter Date (From - To).");
				return false;
			}
			if (dateFromText.getText().length() != 8) {
				setErrorMessage("Invalid From Date.");
				Logger.getLogger(getApplicationId()).error("Invalid To Date.");
				return false;
			}
			if (dateToText.getText().length() != 8) {
				setErrorMessage("Invalid To Date.");
				Logger.getLogger(getApplicationId()).error("Invalid To Date.");
				return false;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			sdf.setLenient(false);
			Date dateFrom = null;
			Date dateTo = null;
			try {
				dateFrom =sdf.parse(dateFromText.getText(), new ParsePosition(0));
			}
			catch (Exception e) {
				setErrorMessage("Invalid From Date.");
				Logger.getLogger(getApplicationId()).error("Invalid From Date.");
				return false;
			}
			if (dateFrom == null) {
				setErrorMessage("Invalid From Date.");
				Logger.getLogger(getApplicationId()).error("Invalid From Date.");
				return false;
			}
			try {
				dateTo = sdf.parse(dateToText.getText(), new ParsePosition(0));
			}
			catch (Exception e) {
				setErrorMessage("Invalid To Date.");
				Logger.getLogger(getApplicationId()).error("Invalid To Date.");
				return false;
			}
			if (dateTo == null) {
				setErrorMessage("Invalid To Date.");
				Logger.getLogger(getApplicationId()).error("Invalid To Date.");
				return false;
			}
			if (dateTo.before(dateFrom)) {
				setErrorMessage("To Date is before From Date.");
				Logger.getLogger(getApplicationId()).error("To Date is before From Date.");
				return false;
			}
			GregorianCalendar calFrom = new GregorianCalendar();
			calFrom.setTime(dateFrom);
			calFrom.add(calFrom.DATE, datePeriod - 1);
			dateFrom = calFrom.getTime();
			if (dateFrom.before(dateTo)) {
				setErrorMessage("The period of Date (From - To) exceeds the maximum days.");
				Logger.getLogger(getApplicationId()).error("The period of Date (From - To) exceeds the maximum days.");
				return false;
			}
		}
		return true;
	}

	private boolean validateMto() 
	{
		if (yearText.getText().length() > 0 || modelText.getText().length() > 0 || typeText.getText().length() > 0 || optionText.getText().length() > 0) {
			if (yearText.getText().length() == 0 || modelText.getText().length() == 0) {
				setErrorMessage("Enter MTO (YEAR, MODEL).");
				Logger.getLogger(getApplicationId()).error("Enter MTO (YEAR, MODEL).");
				return false;
			}
			if (modelText.getText().length() > 0 && modelText.getText().length() != 3) {
				setErrorMessage("Invalid MTO (MODEL).");
				Logger.getLogger(getApplicationId()).error("Invalid MTO (MODEL).");
				return false;
			}
			if (typeText.getText().length() > 0 && typeText.getText().length() != 3) {
				setErrorMessage("Invalid MTO (TYPE).");
				Logger.getLogger(getApplicationId()).error("Invalid MTO (TYPE).");
				return false;
			}
			if (optionText.getText().length() > 0 && optionText.getText().length() != 3) {
				setErrorMessage("Invalid MTO (OPTION).");
				Logger.getLogger(getApplicationId()).error("Invalid MTO (OPTION).");
				return false;
			}
		}
		return true;
	}

	private javax.swing.JLabel getDateCommentLabel() {
		if (dateCommentLabel == null) {
			dateCommentLabel = new javax.swing.JLabel();
			dateCommentLabel.setSize(116, 40);
			dateCommentLabel.setText("");
			dateCommentLabel.setLocation(703, 305);
		}
		return dateCommentLabel;
	}

	private javax.swing.JLabel getDateFormatLabel() {
		if (dateFormatLabel == null) {
			dateFormatLabel = new javax.swing.JLabel();
			dateFormatLabel.setSize(81, 40);
			dateFormatLabel.setText(Message.get("(YYYYMMDD)"));
			dateFormatLabel.setLocation(615, 305);
		}
		return dateFormatLabel;
	}

	private JLabel getDateFromLabel() {
		if (dateFromLabel == null) {
			dateFromLabel = new JLabel();
			dateFromLabel.setSize(77, 40);
			dateFromLabel.setText(Message.get(LETDataTag.RESOURCES_DATE_FROM));
			dateFromLabel.setLocation(160, 305);
		}
		return dateFromLabel;
	}

	private JTextField getDateFromText() {
		if (dateFromText == null) {
			dateFromText = new JTextField();
			dateFromText.setSize(100, 30);
			dateFromText.setFont(new java.awt.Font(FONT_NAME, java.awt.Font.PLAIN, FONT_SIZE));
			dateFromText.setLocation(249, 311);
			dateFromText.setName("dateFromText");
			dateFromText.setDocument(new NumericDocument(8));
			dateFromText.setText("");
		}
		return dateFromText;
	}

	private JLabel getDateToLabel() {
		if (dateToLabel == null) {
			dateToLabel = new JLabel();
			dateToLabel.setSize(70, 40);
			dateToLabel.setText(Message.get(LETDataTag.RESOURCES_DATE_TO));
			dateToLabel.setLocation(431, 305);
		}
		return dateToLabel;
	}

	private JTextField getDateToText() {
		if (dateToText == null) {
			dateToText = new JTextField();
			dateToText.setName("dateToText");
			dateToText.setSize(100, 30);
			dateToText.setFont(new java.awt.Font(FONT_NAME, java.awt.Font.PLAIN, FONT_SIZE));
			dateToText.setLocation(511, 311);
			dateToText.setDocument(new NumericDocument(8));
		}
		return dateToText;
	}

	private JButton getDownloadButton() {
		if (downloadButton == null) {
			downloadButton = new JButton();
			downloadButton.setSize(120, 35);
			downloadButton.setName("downloadButton");
			downloadButton.setText(Message.get(LETDataTag.RESOURCES_DOWNLOAD));
			downloadButton.setLocation(690, 400);
			downloadButton.setEnabled(true);
			downloadButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onDownload();
				}
			});
		}
		return downloadButton;
	}

	private JRadioButton getFaultResultRadioButton() {
		if (faultResultRadioButton == null) {
			faultResultRadioButton =new JRadioButton(Message.get(LETDataTag.RESOURCES_LET_FAULT_RESULT_LIST_REPORT),true);
			faultResultRadioButton.setSize(200, 40);
			faultResultRadioButton.setLocation(320, 80);
			faultResultRadioButton.setName("faultResultRadioButton");
			faultResultRadioButton.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					changeReport();
				}
			});
		}
		return faultResultRadioButton;
	}

	private JComboBox getInspectionProgramCombo() {
		if (insPgmCombo == null) {
			insPgmCombo = new JComboBox();
			insPgmCombo.setSize(500, 30);
			insPgmCombo.setName("insPgmCombo");
			insPgmCombo.setFont( new java.awt.Font(FONT_NAME, java.awt.Font.PLAIN, FONT_SIZE));
			insPgmCombo.setLocation(320, 130);
		}
		return insPgmCombo;
	}

	private JLabel getInspectionProgramLabel() {
		if (inspectionProgramLabel == null) {
			inspectionProgramLabel = new JLabel();
			inspectionProgramLabel.setSize(300, 40);
			inspectionProgramLabel.setText(Message.get(LETDataTag.RESOURCES_INSPECTION_PROGRAM));
			inspectionProgramLabel.setLocation(160, 125);
		}
		return inspectionProgramLabel;
	}

	private JRadioButton getLotOutRadioButton() {
		if (lotOutRadioButton == null) {
			lotOutRadioButton =new JRadioButton( Message.get( LETDataTag.RESOURCES_LET_LOT_OUT_LIST_REPORT));
			lotOutRadioButton.setSize(200, 40);
			lotOutRadioButton.setLocation(575, 80);
			lotOutRadioButton.setName("lotOutRadioButton");
			lotOutRadioButton.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					changeReport();
				}
			});
		}
		return lotOutRadioButton;
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(null);
			mainPanel.add(getTitleLabel(), null);
			mainPanel.add(getFaultResultRadioButton(), null);
			mainPanel.add(getLotOutRadioButton(), null);
			mainPanel.add(getInspectionProgramLabel(), null);
			mainPanel.add(getInspectionProgramCombo(), null);
			mainPanel.add(getVinLabel(), null);
			mainPanel.add(getVinText(), null);
			mainPanel.add(getMtoLabel(), null);
			mainPanel.add(getYearLabel(), null);
			mainPanel.add(getYearText(), null);
			mainPanel.add(getModelLabel(), null);
			mainPanel.add(getModelText(), null);
			mainPanel.add(getTypeLabel(), null);
			mainPanel.add(getTypeText(), null);
			mainPanel.add(getOptionLabel(), null);
			mainPanel.add(getOptionText(), null);
			mainPanel.add(getDateFromLabel(), null);
			mainPanel.add(getDateFromText(), null);
			mainPanel.add(getDateToLabel(), null);
			mainPanel.add(getDateToText(), null);
			mainPanel.add(getDateFormatLabel(), null);
			mainPanel.add(getDateCommentLabel(), null);
			mainPanel.add(getDownloadButton(), null);
			repButtonGroup = new ButtonGroup();
			repButtonGroup.add(faultResultRadioButton);
			repButtonGroup.add(lotOutRadioButton);
		}
		return mainPanel;
	}

	private JLabel getModelLabel() {
		if (modelLabel == null) {
			modelLabel = new JLabel();
			modelLabel.setSize(200, 40);
			modelLabel.setText(Message.get(LETDataTag.RESOURCES_MODEL));
			modelLabel.setLocation(370, 220);
		}
		return modelLabel;
	}

	private JTextField getModelText() {
		if (modelText == null) {
			modelText = new JTextField();
			modelText.setSize(50, 30);
			modelText.setFont(new java.awt.Font(FONT_NAME, java.awt.Font.PLAIN, FONT_SIZE));
			modelText.setLocation(370, 260);
			modelText.setDocument(new AsciiDocument(3));
		}
		return modelText;
	}

	private JLabel getMtoLabel() {
		if (mtoLabel == null) {
			mtoLabel = new JLabel();
			mtoLabel.setSize(200, 40);
			mtoLabel.setText(Message.get(LETDataTag.RESOURCES_MTO));
			mtoLabel.setLocation(160, 255);
		}
		return mtoLabel;
	}

	private JLabel getOptionLabel() {
		if (optionLabel == null) {
			optionLabel = new JLabel();
			optionLabel.setSize(200, 40);
			optionLabel.setText(Message.get(LETDataTag.RESOURCES_OPTION));
			optionLabel.setLocation(510, 220);
		}
		return optionLabel;
	}

	private JTextField getOptionText() {
		if (optionText == null) {
			optionText = new JTextField();
			optionText.setSize(50, 30);
			optionText.setFont( new java.awt.Font(FONT_NAME, java.awt.Font.PLAIN, FONT_SIZE));
			optionText.setLocation(510, 260);
			optionText.setDocument(new AsciiDocument(3));
		}
		return optionText;
	}

	private JLabel getTitleLabel() {
		if (titleLabel == null) {
			titleLabel = new JLabel();
			titleLabel.setBounds(124, 11, 755, 29);
			titleLabel.setText(Message.get(LETDataTag.RESOURCES_TITLE));
			titleLabel.setFont(new Font(FONT_NAME, java.awt.Font.BOLD, 24));
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return titleLabel;
	}

	private JLabel getTypeLabel() {
		if (typeLabel == null) {
			typeLabel = new JLabel();
			typeLabel.setSize(200, 40);
			typeLabel.setText(Message.get(LETDataTag.RESOURCES_TYPE));
			typeLabel.setLocation(440, 220);
		}
		return typeLabel;
	}

	private JTextField getTypeText() {
		if (typeText == null) {
			typeText = new JTextField();
			typeText.setSize(50, 30);
			typeText.setFont(new java.awt.Font(FONT_NAME, java.awt.Font.PLAIN, FONT_SIZE));
			typeText.setLocation(440, 260);
			typeText.setDocument(new AsciiDocument(3));
		}
		return typeText;
	}


	private JLabel getVinLabel() {
		if (vinLabel == null) {
			vinLabel = new JLabel();
			vinLabel.setSize(200, 40);
			vinLabel.setText(Message.get(LETDataTag.RESOURCES_VIN));
			vinLabel.setLocation(160, 175);
		}
		return vinLabel;
	}

	private JTextField getVinText() {
		if (vinText == null) {
			vinText = new JTextField();
			vinText.setSize(250, 30);
			vinText.setFont(new java.awt.Font(FONT_NAME, java.awt.Font.PLAIN, FONT_SIZE));
			vinText.setLocation(320, 180);
			vinText.setDocument(new AsciiDocument(17));
		}
		return vinText;
	}

	private JLabel getYearLabel() {
		if (yearLabel == null) {
			yearLabel = new JLabel();
			yearLabel.setSize(200, 40);
			yearLabel.setText(Message.get(LETDataTag.RESOURCES_YEAR));
			yearLabel.setLocation(320, 220);
		}
		return yearLabel;
	}

	private JTextField getYearText() {
		if (yearText == null) {
			yearText = new JTextField();
			yearText.setSize(30, 30);
			yearText.setFont(new java.awt.Font(FONT_NAME, java.awt.Font.PLAIN, FONT_SIZE));
			yearText.setLocation(320, 260);
			yearText.setDocument(new AsciiDocument(1));
		}
		return yearText;
	}  

	private void initialize() {
		setLayout(new BorderLayout());
		add(getMainPanel(),BorderLayout.CENTER);
		letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, getApplicationId());
		setInspectionProgram();
		insPgmCombo.setEnabled(false);
		try {			
			iDateRange = Integer.valueOf(letPropertyBean.getDownloadDateRange()).intValue();
			printerTemplatePath=letPropertyBean.getPrinterTemplatePath();
			String[] paramStringArray = new String[]{Integer.toString(iDateRange)};
			getDateCommentLabel().setText((String)Message.get("WITHIN_N_DAYS", paramStringArray));			
		}  catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Fault Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Fault Result Download screen");        }
	}

	private void onDownload() 
	{
		setErrorMessage("");		
		try {
			if (chkInputData()) {
				JFileChooser chooser = new JFileChooser();
				chooser.addChoosableFileFilter(new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						}
						return f.getName().toLowerCase().endsWith(LETDataTag.PDF_FILE_EXTENSION);
					}
					public String getDescription() {
						return LETDataTag.PDF_FILE_DESCRIPTION;
					}
				});
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String nowDate = sdf.format(new Date());
				String fileName;
				if (faultResultRadioButton.isSelected()) {
					fileName = LETDataTag.PDF_FILE_NAME_FAULT_RESULT;
				} else {
					fileName = LETDataTag.PDF_FILE_NAME_LOTOUT;
				}
				chooser.setSelectedFile(new File(fileName + nowDate + LETDataTag.PDF_FILE_EXTENSION));
				int rtnBtn = chooser.showSaveDialog(this);
				reqInsPgmId=null;
				reqInsPgmName=null;
				if (rtnBtn == JFileChooser.APPROVE_OPTION) {
					if (insPgmCombo.getSelectedIndex() > 0) {
						LetInspectionProgram tmp = insPgmList.get(insPgmCombo.getSelectedIndex() - 1);
						reqInsPgmId=tmp.getInspectionPgmId();
						reqInsPgmName=(String)insPgmCombo.getSelectedItem();
					} 
					productId = vinText.getText();
					if (!productId.equals("")) {
						while (productId.length() < 17) {
							productId = " " + productId;
						}
					}				
					generateReport();
					if (resMsgId.length() == 0) {
						if(resXslFoBinary==null)
						{
							setErrorMessage("No results to download");
							Logger.getLogger(this.getApplicationId()).error("No results to download");
							return;
						}
						resXslFoBinary = XSLFOPDFConverter.generatePDF(new String(XSLFOPDFConverter.gzipUncompress(resXslFoBinary)));
						BufferedOutputStream bufOut = null;
						try {
							bufOut = new BufferedOutputStream(new FileOutputStream(new File(chooser.getSelectedFile().getPath())));
							bufOut.write(resXslFoBinary);
							bufOut.flush();
							bufOut.close();
							setMessage("PDF file downloaded successfully.");
							Logger.getLogger(this.getApplicationId()).info("PDF file downloaded successfully.");
						}
						catch (java.io.IOException e) {
							setErrorMessage("Failed creating the file. Please confirm whether the file is used by other applications.");
							Logger.getLogger(this.getApplicationId()).error(e,"Failed creating the file. Please confirm whether the file is used by other applications.");
							e.printStackTrace();   
						}
						finally {
							if (bufOut != null) {
								bufOut.close();
								bufOut = null;
							}
						}
					} else {
						setErrorMessage(resMsgId);
						Logger.getLogger(this.getApplicationId()).error(resMsgId);
					}

				} else if (rtnBtn == JFileChooser.CANCEL_OPTION) {
				}
			}
		}catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Fault Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Fault Result Download screen");        
		}
	}

	private void generateReport() 
	{
		resXslFoBinary=null;
		propReportSize = letPropertyBean.getFaultResultReportSize();
		reqMto = "";
		reqDateFrom = "";
		reqDateTo = "";
		if (productId.length() == 0) {
			reqYear = (String) yearText.getText();
			reqModel = (String) modelText.getText();
			reqType = (String) typeText.getText();
			reqOption = (String) optionText.getText();
			reqMto = reqYear + reqModel;
			if (reqMto.length() > 0) {
				if (reqType.length() > 0) {
					reqMto = reqMto + reqType;
				} else {
					reqMto = reqMto + LETDataTag.TEMPLATE_TYPE_OPTION_NO_INPUT;
				}
				if (reqOption.length() > 0) {
					reqMto = reqMto + reqOption;
				} else {
					reqMto = reqMto + LETDataTag.TEMPLATE_TYPE_OPTION_NO_INPUT;
				}
			}
			reqDateFrom =(String) dateFromText.getText();
			reqDateTo =(String) dateToText.getText();
			if (reqDateFrom.length() > 0) {
				sqlDateFrom = reqDateFrom.substring(0, 4)+ "-"+ reqDateFrom.substring(4, 6) + "-"+ reqDateFrom.substring(6, 8)+ " 00:00:00.000000";
				reqDateFrom = reqDateFrom + LETDataTag.TEMPLATE_DATE_MARK;
			}
			if (reqDateTo.length() > 0) {
				sqlDateTo =reqDateTo.substring(0, 4) + "-"+ reqDateTo.substring(4, 6)+ "-"+ reqDateTo.substring(6, 8) + " 23:59:59.999999";
			}
		}
		resMsgId = "";
		if (faultResultRadioButton.isSelected()) {
			executeFaultResultList();
		} else {
			executeLotOutList();
		}
	}

	private void executeFaultResultList()  {
		ArrayList result = null;
		if (productId.length() > 0) {
			result = getVinFaultResultData();
		} else {
			result = getMtoFaultResultData();
		}
		if (result.size() > 0) {
			String xslFo = convertFaultResultDataToXslFo(result);
			resXslFoBinary = gzipCompress(xslFo.getBytes());
		}
	}

	private String convertFaultResultDataToXslFo(ArrayList result)
	{

		String template_form =getFileData(printerTemplatePath+ propReportSize.toLowerCase()+ LETDataTag.TEMPLATE_FAULT_RESULT_FROM);
		String template_page =getFileData(printerTemplatePath+ propReportSize.toLowerCase()+ LETDataTag.TEMPLATE_FAULT_RESULT_PAGE);
		String template_lastpage =getFileData(printerTemplatePath+ propReportSize.toLowerCase()+ LETDataTag.TEMPLATE_FAULT_RESULT_LASTPAGE);
		String template_body_iterate =getFileData(printerTemplatePath+ propReportSize.toLowerCase()+ LETDataTag.TEMPLATE_FAULT_RESULT_BODY_ITERATE);
		StringBuffer bufPage = new StringBuffer();
		int vinsRows = result.size();
		ArrayList vin;
		String strIteVin;
		String strIteMto;
		ArrayList insPgm;
		String page;
		String body_iterate;
		StringBuffer bufTemplate = new StringBuffer();
		for (int vinsRow = 0; vinsRow < vinsRows; vinsRow++) {
			vin = (ArrayList) result.get(vinsRow);
			page = template_page.replaceFirst(LETDataTag.FAULT_RESULT_TAG_VIN, productId);
			page = page.replaceFirst(LETDataTag.FAULT_RESULT_TAG_MTO, reqMto);
			page = page.replaceFirst(LETDataTag.FAULT_RESULT_TAG_DATE_FROM, reqDateFrom);
			page = page.replaceFirst(LETDataTag.FAULT_RESULT_TAG_DATE_TO, reqDateTo);
			strIteVin = (String) vin.get(0);
			strIteMto = (String) vin.get(1);
			page = page.replaceAll(LETDataTag.FAULT_RESULT_TAG_ITE_VIN, strIteVin);
			page = page.replaceAll(LETDataTag.FAULT_RESULT_TAG_ITE_MTO, strIteMto);
			insPgm = (ArrayList) vin.get(2);
			page =page.replaceFirst(LETDataTag.FAULT_RESULT_TAG_BODY_S,convertFaultResultListDataInsPgmToXslFo(template_body_iterate,insPgm));
			insPgm = (ArrayList) vin.get(3);
			page =page.replaceFirst(LETDataTag.FAULT_RESULT_TAG_BODY_C,convertFaultResultListDataInsPgmToXslFo(template_body_iterate,insPgm));
			if (vinsRow == vinsRows - 1) {
				page =page.replaceFirst(LETDataTag.FAULT_RESULT_TAG_LAST_PAGE,template_lastpage);
			} else {
				page = page.replaceFirst(LETDataTag.FAULT_RESULT_TAG_LAST_PAGE, "");
			}
			bufTemplate.append(page);
		}
		return template_form.replaceFirst(LETDataTag.FAULT_RESULT_TAG_PAGE,bufTemplate.toString());
	}

	private String convertFaultResultListDataInsPgmToXslFo(String template, ArrayList insPgms) {
		StringBuffer bufTemplate = new StringBuffer();
		String strRow;
		ArrayList insPgmInfo;
		int insPgmRows = insPgms.size();
		if (insPgmRows > 0) {
			for (int insPgmRow = 0; insPgmRow < insPgmRows; insPgmRow++) {
				insPgmInfo = (ArrayList) insPgms.get(insPgmRow);
				strRow = template.replaceFirst( LETDataTag.FAULT_RESULT_TAG_ITE_PROCESS_STEP, (String) insPgmInfo.get(0));
				strRow = strRow.replaceFirst(LETDataTag.FAULT_RESULT_TAG_ITE_INS_PGM_NAME, (String) insPgmInfo.get(1));
				strRow = strRow.replaceFirst(LETDataTag.FAULT_RESULT_TAG_ITE_INS_PGM_STATUS, LetInspectionStatus.getType(Integer.parseInt(insPgmInfo.get(2).toString().trim())).name());
				bufTemplate.append(strRow);
			}
		}
		return bufTemplate.toString();
	}

	private ArrayList getMtoFaultResultData()  
	{
		ArrayList result = new ArrayList();
		try {
			int vinCnt=getDao(LetResultDao.class).getFaultListCntData(reqYear,reqModel,reqType,reqOption,sqlDateFrom,sqlDateTo,reqInsPgmId);
			int maxVinCnt =Integer.parseInt(letPropertyBean.getFaultResultMaxVinCount());				
			if (vinCnt == 0) {
				Logger.getLogger(this.getApplicationId()).error("No record matched.Aborted download.");
				setErrorMessage("No record matched. Aborted download.");        
				return result;
			} else if (vinCnt > maxVinCnt) {
				Logger.getLogger(this.getApplicationId()).error("There are a lot of pertinent records. Aborted download.");
				setErrorMessage("There are a lot of pertinent records. Aborted download.");        
				return result;
			}
			List<Object[]> faultResultList=getDao(LetResultDao.class).getFaultResultListData(reqYear,reqModel,reqType,reqOption,sqlDateFrom,sqlDateTo,reqInsPgmId);
			result = redistributeFaultResultListData(faultResultList);			         
			if (result.size() == 0) {
				Logger.getLogger(this.getApplicationId()).error("No record matched.Aborted download.");
				setErrorMessage("No record matched. Aborted download.");        
				return result;
			}			
		}
		catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Fault Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Fault Result Download screen");        
		}
		return result;
	}

	private ArrayList getVinFaultResultData()  
	{
		ArrayList result=new ArrayList();
		try {
			List<Object[]> faultResultList=getDao(LetInspectionProgramDao.class).getLetVinFaultResultData(productId);
			if (faultResultList.size() == 0) {
				Logger.getLogger(this.getApplicationId()).error("No record matched. Aborted download.");
				setErrorMessage("No record matched. Aborted download.");
				return result;
			}
			result = redistributeFaultResultListData(faultResultList);			
		}
		catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Fault Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Fault Result Download screen");        
		}
		return result;
	}

	private ArrayList redistributeFaultResultListData(List<Object[]> faultResultList) 
	{
		String previousVin = null;
		String currentVin = null;
		String mto;
		String processStep;
		String insPgm_Name;
		String insPgm_Status;
		ArrayList vins = new ArrayList(faultResultList.size());
		ArrayList vin = null;
		ArrayList pgms = null;
		ArrayList pgms_s = null;
		ArrayList pgms_c = null;
		for(Object[] obj:faultResultList) {
			currentVin =(String) obj[0];
			if (!currentVin.equals(previousVin)) {
				vin = new ArrayList(4);
				pgms_s = new ArrayList();
				pgms_c = new ArrayList();
				mto =(String) obj[4];
				vin.add(0, currentVin);
				vin.add(1, mto);
				vin.add(2, pgms_s);
				vin.add(3, pgms_c);
				vins.add(vin);
			}
			processStep =(String) obj[1];
			insPgm_Name =(String) obj[2];
			if (insPgm_Name.length() > 20) {
				insPgm_Name = insPgm_Name.substring(0, 20);
			}
			insPgm_Status =(String)obj[3];
			pgms = new ArrayList(3);
			pgms.add(0, processStep);
			pgms.add(1, insPgm_Name);
			pgms.add(2, insPgm_Status);
			if (processStep != null && processStep.length() > 0) {
				if (processStep.substring(0, 1).equals(LETDataTag.SEQUENCIAL_MODE)) {
					pgms_s.add(pgms);
				} else if (processStep.substring(0, 1).equals(LETDataTag.CONTINUOUS_MODE)) {
					pgms_c.add(pgms);
				}
			}
			previousVin = currentVin;
		}
		vins.trimToSize();
		return vins;
	}

	private void executeLotOutList()  {
		try {
			String xslFo = null;
			List<Object[]> lotOutList=getDao(LetResultDao.class).getLotOutListData(reqYear,reqModel,reqType,reqOption,sqlDateFrom,sqlDateTo,reqInsPgmId);
			xslFo = convertLotOutListDataToXslFo(lotOutList);
			if (xslFo.length() > 0) {
				resXslFoBinary = gzipCompress(xslFo.getBytes());
			}
		}catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Fault Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Fault Result Download screen");        
		}
	}

	private String convertLotOutListDataToXslFo(List<Object[]> resultList)
	{
		String result = "";
		try {
			String template_form = getFileData(printerTemplatePath+ propReportSize.toLowerCase()+ LETDataTag.TEMPLATE_LOTOUT_FORM);
			String template_body_iterate =getFileData(printerTemplatePath+ propReportSize.toLowerCase()+ LETDataTag.TEMPLATE_LOTOUT_BODY_ITERATE);
			String form = template_form.replaceFirst(LETDataTag.LOTOUT_TAG_MTO, reqMto);
			form = form.replaceFirst(LETDataTag.LOTOUT_TAG_PGM, reqInsPgmName);
			form = form.replaceFirst(LETDataTag.LOTOUT_TAG_DATE_FROM, reqDateFrom);
			form = form.replaceFirst(LETDataTag.LOTOUT_TAG_DATE_TO, reqDateTo);
			StringBuffer bufTemplate = new StringBuffer();
			String body = null;
			int row = 0;
			for(Object[] obj:resultList) 
			{
				body =template_body_iterate.replaceAll(LETDataTag.LOTOUT_TAG_ITE_VIN,(String)obj[0]);
				body =body.replaceAll(LETDataTag.LOTOUT_TAG_ITE_MTO,(String)obj[1]);
				bufTemplate.append(body);
				row = row + 1;
			}
			if (row == 0) {
				Logger.getLogger(this.getApplicationId()).error("No record matched. Aborting download.");
				setErrorMessage("No record matched. Aborting download."); 
				return result;
			} else {
			}
			result = form.replaceFirst(LETDataTag.LOTOUT_TAG_BODY, bufTemplate.toString());
		}
		catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Fault Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Fault Result Download screen");        
		}
		return result;
	}

	private byte[] gzipCompress(byte[] in)  
	{
		byte[] rtn = null;
		BufferedInputStream bufIn = null;
		ByteArrayOutputStream bytOut = null;
		BufferedOutputStream bufOut = null;
		try {
			bufIn = new BufferedInputStream(new ByteArrayInputStream(in));
			bytOut = new ByteArrayOutputStream();
			bufOut = new BufferedOutputStream(new GZIPOutputStream(bytOut), 1024000);
			int chr;
			try {
				while ((chr = bufIn.read()) != -1) {
					bufOut.write((byte) chr);
				}
				bufOut.flush();
			}
			catch (IOException e) {	            	
				Logger.getLogger(this.getApplicationId()).error(e,"Failed GZip Compress process.");
				e.printStackTrace();
				setErrorMessage("Failed GZip Compress process.");        
			}
			try {
				bufOut.close();
				rtn = bytOut.toByteArray();
				bytOut.close();
			}
			catch (IOException e) {
				Logger.getLogger(this.getApplicationId()).error(e,"Failed to close OutputStream.");
				e.printStackTrace();
				setErrorMessage("Failed to close OutputStream.");        
			}
			bufOut = null;
			bytOut = null;
			try {
				bufIn.close();
			}
			catch (IOException e) {
				Logger.getLogger(this.getApplicationId()).error(e,"Failed to close InputStream.");
				e.printStackTrace();
				setErrorMessage("Failed to close InputStream.");        
			}
			bufIn = null;
		}
		finally {
			if (bufIn != null) {
				try {
					bufIn.close();
				}
				catch (IOException e) {
					Logger.getLogger(this.getApplicationId()).error(e,"Failed to close InputStream.");
					e.printStackTrace();
					setErrorMessage("Failed to close InputStream."); 	                
				}
				bufIn = null;
			}
			try {
				if (bufOut != null) {
					bufOut.close();
					bufOut = null;
				}

				if (bytOut != null) {
					bytOut.close();
				}
				bytOut = null;
			}
			catch (IOException e) {
				Logger.getLogger(this.getApplicationId()).error(e,"Failed to close OutputStream.");
				e.printStackTrace();
				setErrorMessage("Failed to close OutputStream."); 
			}
			return rtn;
		}
	}

	private void setInspectionProgram() 
	{
		try {
			insPgmCombo.addItem("");
			insPgmList=getDao(LetInspectionProgramDao.class).findAllLetInspPgmOrderByPgmName();
			datePeriod =Integer.parseInt(letPropertyBean.getFaultResultOutputDtPeriod());
			if (insPgmList.size() > 0) {
				Iterator<LetInspectionProgram> iterator = insPgmList.iterator();
				LetInspectionProgram tmp;
				while (iterator.hasNext()) {
					tmp = (LetInspectionProgram) iterator.next();
					insPgmCombo.addItem(tmp.getInspectionPgmName());
				}
				iterator = null;
			} else {
				setErrorMessage("Acquisition of the Inspection Program failed.");
			}
		}       
		catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Fault Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Fault Result Download screen");        
		}
	}

	public void actionPerformed(ActionEvent arg) {
	}

	private String getFileData(String filePath)  
	{
		BufferedReader bufferedReader = null;
		StringBuffer bufTemplate = new StringBuffer();
		try {
			ClassLoader cl = this.getClass().getClassLoader();
			InputStream in = cl.getResourceAsStream(filePath);
			bufferedReader = new BufferedReader(new InputStreamReader(in, LETDataTag.TEMPLATE_STR_CODE));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				bufTemplate.append(line);
			}
		}
		catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Fault Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Fault Result Download screen");        
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException e) {
					Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Fault Download screen");
					e.printStackTrace();
					setErrorMessage("An error Occurred while processing the LET Fault Result Download screen");        
				}
			}
		}
		return bufTemplate.toString();
	}
}
