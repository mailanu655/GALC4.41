
package com.honda.galc.client.teamleader.let;


import static com.honda.galc.service.ServiceFactory.getDao;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.let.util.AsciiDocument;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.NumericDocument;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetInspectionProgramDao;
import com.honda.galc.dao.product.LetProgramResultDao;
import com.honda.galc.data.LetInspectionDownloadDto;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.message.Message;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 25, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetInspectionResultDownloadPanel extends TabbedPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel = null;
	private JLabel titleLabel = null;
	private JPanel searchPanel = null;
	private JPanel conditionPanel = null;
	private JPanel conditionFrameNoPanel = null;
	private JLabel frameNoLabel = null;
	private JTextField frameNoTextField = null;
	private JLabel productIdLabel = null;
	private JTextField productIdTxtField = null;
	private JPanel conditionPeriodPanel = null;
	private JLabel startLabel = null;
	private JTextField start = null;
	private JLabel endLabel = null;
	private JTextField end = null;
	private JPanel conditionMtoPanel = null;
	private JLabel modelLabel = null;
	private JTextField modelTxtField = null;
	private JLabel typeLabel = null;
	private JTextField typeTxtField = null;
	private JLabel optionLabel = null;
	private JTextField optionTxtField = null;
	private JLabel lineNoLabel = null;
	private JTextField lineNoTxtField = null;
	private JButton searchButton = null;
	private javax.swing.JLabel jLabel1 = null;
	private JLabel programLabel = null;
	private JScrollPane programScrollPane = null;
	private JTable programTable = null;
	private javax.swing.JLabel jLabel2 = null;
	private int iDateRange = -1;
	private javax.swing.JLabel jLabel4 = null;
	private String frameNoErrMsg = null;
	private JComponent iniFocusComp = null;
	private DateFormat dfTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
	private final String CSV_CON_COLUMN = "DownloadTimestamp,VIN,LineNo,Year,Model,Type,Option,StartDate,EndDate,Production,Program\n";
	private final String CSV_CON_COLUMN_JP = "DownloadTimestamp,F.No,VIN,LineNo,Year,Model,Type,Option,StartDate,EndDate,Production,Program\n";
	private final String CSV_RESULT_HEADER = "VIN,Year,Model,Type,Option,TestProcess,StepFile,EndTimestamp,Process,Cal,Cell,Status,Production";	
	private javax.swing.JLabel yearLabel = null;
	private javax.swing.JTextField yearTxtField = null;
	private javax.swing.JLabel jLabel3 = null;
	private javax.swing.JLabel productionLabel = null;
	private javax.swing.JTextField productionTextField = null;
	private Vector programsVector=null;
	private LetPropertyBean letPropertyBean=null;
	private String useFrameNo=null;
	private LetUtil letUtil=null;
	private boolean withWildcard = false;
	private GregorianCalendar gcStartDate = null;
	private GregorianCalendar gcEndDate = null;
	private Set paramKindSet = null;
	private String csvResults = null;
	private Boolean resultsExist=null;
	private LetInspectionDownloadDto dto =null;
	private File dataFile = null;
	private BufferedWriter bw = null;
	private StringBuffer csvResult=null;
	private HashMap<String, String> lowLimitMap = null;
	private HashMap<String, String> highLimitMap = null;



	public LetInspectionResultDownloadPanel(TabbedMainWindow mainWindow) {
		super("LET Inspection Result Download Panel", KeyEvent.VK_Z,mainWindow);	
		initialize();
	}

	public LetInspectionResultDownloadPanel(String screenName, int keyEvent,TabbedMainWindow mainWindow) {
		super(screenName, keyEvent,mainWindow);
		initialize();
	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		Logger.getLogger(this.getApplicationId()).info("LET Inspection Result Download Panel is selected");
	}

	private void initialize() {
		setLayout(new BorderLayout());
		add(getMainPanel(),BorderLayout.CENTER);
		try {
			letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, getApplicationId());
			letUtil=new LetUtil();
			paramKindSet = new LinkedHashSet();
			lowLimitMap = new HashMap<String, String>();
			highLimitMap = new HashMap<String, String>();
			csvResults = "";
			resultsExist=Boolean.FALSE;
			useFrameNo =Message.get("USE_FRAME");
			if (useFrameNo.equals("true")) {
				conditionFrameNoPanel.add(getFrameNoLabel(), 0);
				conditionFrameNoPanel.add(getFrameNo(), 1);
				iniFocusComp = getFrameNo();
				frameNoErrMsg = "Enter at least one of VIN, frame number or date range.";
			} else {
				getFrameNoLabel();
				getFrameNo();
				iniFocusComp = getProductId();
				frameNoErrMsg = "Enter at least VIN or date range.";
			}
			this.getMainWindow().addWindowListener(new WindowAdapter() {
				public void windowActivated(WindowEvent e) {
					iniFocusComp.requestFocusInWindow();
				}
			});
			TableColumnModel columnModel = getProgramTable().getColumnModel();
			columnModel.getColumn(0).setPreferredWidth(200);
			try {
				iDateRange = Integer.valueOf(letPropertyBean.getDownloadDateRange()).intValue();
			} catch (Exception nfe) {
				Logger.getLogger(this.getApplicationId()).error("Date range property not found.");
				setErrorMessage("Date range property not found.");
				return;
			}			
			DefaultTableModel model = (DefaultTableModel) getProgramTable().getModel();
			fetchAllLetPrograms();
			model.setColumnCount(2);
			model.setRowCount(0);
			if (programsVector != null) {
				if (programsVector.size() != 0) {
					for (int i = 0; i < programsVector.size(); i++) {
						model.addRow((Vector) programsVector.get(i));
					}
				}
			}
			getProgramTable().removeColumn(getProgramTable().getColumnModel().getColumn(1));
			columnModel.getColumn(0).setPreferredWidth(200);
			String[] paramStringArray = new String[]{Integer.toString(iDateRange)};
			getJLabel2().setText((String)Message.get("WITHIN_N_DAYS", paramStringArray));
		}  catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");        }
	}



	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(null);
			mainPanel.add(getTitleLabel(), null);
			mainPanel.add(getSearchPanel(), null);
			mainPanel.add(getProgramLabel(), null);
			mainPanel.add(getProgramScrollPane(), null);
			mainPanel.add(getSearchButton(), null);
		}
		return mainPanel;
	}

	private JLabel getTitleLabel() {
		if (titleLabel == null) {
			titleLabel = new JLabel();
			titleLabel.setBounds(132, 13, 755, 29);
			titleLabel.setText(Message.get("LETInspectionResultDownload"));
			titleLabel.setFont(new Font("Dialog", java.awt.Font.BOLD, 24));
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return titleLabel;
	}

	private JPanel getSearchPanel() {
		if (searchPanel == null) {
			searchPanel = new JPanel();
			searchPanel.setLayout(null);
			searchPanel.add(getConditionPanel(), null);
			searchPanel.add(getSearchButton(), null);
			searchPanel.setBounds(150, 91, 751, 148);
			searchPanel.setBorder(BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
		}
		return searchPanel;
	}

	private JPanel getConditionPanel() {
		if (conditionPanel == null) {
			conditionPanel = new JPanel();
			conditionPanel.setLayout(new BoxLayout(conditionPanel, javax.swing.BoxLayout.Y_AXIS));
			conditionPanel.add(getConditionFrameNoPanel(), null);
			conditionPanel.add(getConditionPeriodPanel(), null);
			conditionPanel.add(getConditionMtoPanel(), null);
			conditionPanel.setBounds(8, 9, 735, 137);
		}
		return conditionPanel;
	}

	private JPanel getConditionFrameNoPanel() {
		if (conditionFrameNoPanel == null) {
			conditionFrameNoPanel = new JPanel();
			FlowLayout layFlowLayout3 = new FlowLayout();
			layFlowLayout3.setAlignment(FlowLayout.LEFT);
			conditionFrameNoPanel.setLayout(layFlowLayout3);
			conditionFrameNoPanel.add(getJLabel4(), null);
			conditionFrameNoPanel.add(getProductIdLabel(), null);
			conditionFrameNoPanel.add(getProductId(), null);
		}
		return conditionFrameNoPanel;
	}

	private JLabel getFrameNoLabel() {
		if (frameNoLabel == null) {
			frameNoLabel = new JLabel();
			frameNoLabel.setText(Message.get("SeriesFrameNo"));
		}
		return frameNoLabel;
	}

	private JTextField getFrameNo() {
		if (frameNoTextField == null) {
			frameNoTextField = new JTextField();
			frameNoTextField.setColumns(8);
			frameNoTextField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
			frameNoTextField.setDocument(new AsciiDocument(10));
		}
		return frameNoTextField;
	}

	private JLabel getProductIdLabel() {
		if (productIdLabel == null) {
			productIdLabel = new JLabel();
			productIdLabel.setText(Message.get("VIN"));
		}
		return productIdLabel;
	}

	private JTextField getProductId() {
		if (productIdTxtField == null) {
			productIdTxtField = new JTextField();
			productIdTxtField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
			productIdTxtField.setColumns(15);
			productIdTxtField.setName("productIdTxtField");
			productIdTxtField.setDocument(new AsciiDocument(17));
		}
		return productIdTxtField;
	}

	private JPanel getConditionPeriodPanel() {
		if (conditionPeriodPanel == null) {
			conditionPeriodPanel = new JPanel();
			FlowLayout layFlowLayout2 = new FlowLayout();
			layFlowLayout2.setAlignment(FlowLayout.LEFT);
			conditionPeriodPanel.setLayout(layFlowLayout2);
			conditionPeriodPanel.add(getStartLabel(), null);
			conditionPeriodPanel.add(getStart(), null);
			conditionPeriodPanel.add(getEndLabel(), null);
			conditionPeriodPanel.add(getEnd(), null);
			conditionPeriodPanel.add(getJLabel1(), null);
			conditionPeriodPanel.add(getJLabel2(), null);
		}
		return conditionPeriodPanel;
	}

	private JLabel getStartLabel() {
		if (startLabel == null) {
			startLabel = new JLabel();
			startLabel.setText(Message.get("StartDate"));
		}
		return startLabel;
	}

	private JTextField getStart() {
		if (start == null) {
			start = new JTextField();
			start.setColumns(7);
			start.setDocument(new NumericDocument(8));
			start.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
		}
		return start;
	}

	private JLabel getEndLabel() {
		if (endLabel == null) {
			endLabel = new JLabel();
			endLabel.setText(Message.get("EndDate"));
		}
		return endLabel;
	}

	private JTextField getEnd() {
		if (end == null) {
			end = new JTextField();
			end.setColumns(7);
			end.setDocument(new NumericDocument(8));
			end.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
		}
		return end;
	}

	private JPanel getConditionMtoPanel() {
		if (conditionMtoPanel == null) {
			conditionMtoPanel = new JPanel();
			FlowLayout layFlowLayout = new FlowLayout();
			layFlowLayout.setAlignment(FlowLayout.LEFT);
			conditionMtoPanel.setLayout(layFlowLayout);
			conditionMtoPanel.add(getYearLabel(), null);
			conditionMtoPanel.add(getYear(), null);
			conditionMtoPanel.add(getModelLabel(), null);
			conditionMtoPanel.add(getModel(), null);
			conditionMtoPanel.add(getTypeLabel(), null);
			conditionMtoPanel.add(getType(), null);
			conditionMtoPanel.add(getOptionLabel(), null);
			conditionMtoPanel.add(getOption(), null);
			conditionMtoPanel.add(getLineNoLabel(), null);
			conditionMtoPanel.add(getLineNo(), null);
			conditionMtoPanel.add(getProductionLabel(), null);
			conditionMtoPanel.add(getProduction(), null);
			conditionMtoPanel.add(getJLabel3(), null);
		}
		return conditionMtoPanel;
	}

	private javax.swing.JLabel getYearLabel() {
		if (yearLabel == null) {
			yearLabel = new javax.swing.JLabel();
			yearLabel.setText(Message.get("YEAR_CODE"));
		}
		return yearLabel;
	}

	private javax.swing.JTextField getYear() {
		if (yearTxtField == null) {
			yearTxtField = new JTextField();
			yearTxtField.setColumns(2);
			yearTxtField.setDocument(new AsciiDocument(1));
			yearTxtField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
		}
		return yearTxtField;
	}

	private JLabel getModelLabel() {
		if (modelLabel == null) {
			modelLabel = new JLabel();
			modelLabel.setText(Message.get("MODEL"));
		}
		return modelLabel;
	}

	private JTextField getModel() {
		if (modelTxtField == null) {
			modelTxtField = new JTextField();
			modelTxtField.setColumns(4);
			modelTxtField.setDocument(new AsciiDocument(3));
			modelTxtField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
		}
		return modelTxtField;
	}

	private JLabel getTypeLabel() {
		if (typeLabel == null) {
			typeLabel = new JLabel();
			typeLabel.setText(Message.get("TYPE"));
		}
		return typeLabel;
	}

	private JTextField getType() {
		if (typeTxtField == null) {
			typeTxtField = new JTextField();
			typeTxtField.setColumns(4);
			typeTxtField.setDocument(new AsciiDocument(3));
			typeTxtField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
		}
		return typeTxtField;
	}

	private JLabel getOptionLabel() {
		if (optionLabel == null) {
			optionLabel = new JLabel();
			optionLabel.setText(Message.get("OPTION"));
		}
		return optionLabel;
	}

	private JTextField getOption() {
		if (optionTxtField == null) {
			optionTxtField = new JTextField();
			optionTxtField.setColumns(4);
			optionTxtField.setDocument(new AsciiDocument(3));
			optionTxtField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
		}
		return optionTxtField;
	}

	private JLabel getLineNoLabel() {
		if (lineNoLabel == null) {
			lineNoLabel = new javax.swing.JLabel();
			lineNoLabel.setText(Message.get("LINE_NO"));
		}
		return lineNoLabel;
	}

	private JTextField getLineNo() {
		if (lineNoTxtField == null) {
			lineNoTxtField = new JTextField();
			lineNoTxtField.setColumns(2);
			lineNoTxtField.setDocument(new AsciiDocument(1));
			lineNoTxtField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
		}
		return lineNoTxtField;
	}

	private JButton getSearchButton() {
		if (searchButton == null) {
			searchButton = new JButton();
			searchButton.setName("searchButton");
			searchButton.setSize(120, 35);
			searchButton.setText(Message.get("DOWNLOAD"));
			searchButton.setLocation(450, 500);
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onDownload();
				}
			});
		}
		return searchButton;
	}

	private void onDownload() 
	{
		setErrorMessage("");
		withWildcard = false;
		gcStartDate = null;
		gcEndDate = null;
		 dataFile = null;
		 bw = null;
		try {
			if(!verifyEmptyProductIdStartEndDate())return;
			String productId = getProductId().getText();
			String frameNo = getFrameNo().getText();					
			if(!verifyProgramTableSelection()) return;			
			String production = getProduction().getText();			
			if(!validateProduction(production)) return;
			if (!productId.equals("")) {
				if(!validateProductId(productId)) return;
			}
			if (!frameNo.equals("")) {
				if(!validateFrameNo(frameNo)) return;
			}			
			if(!fetchStartEndDate()) return;
			JFileChooser chooser = new JFileChooser();
			chooser.addChoosableFileFilter(new FileFilter() {
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}
					return f.getName().toLowerCase().endsWith(".csv");
				}
				public String getDescription() {
					return "CSV File";
				}
			});
			chooser.setSelectedFile(new File("LET_Inspection_Result.csv"));
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			dataFile = chooser.getSelectedFile();
			dataFile = new File(dataFile.getAbsolutePath() + ".rcv");			
			int selectedRow = getProgramTable().getSelectedRow();
			StringBuffer csvFile=createInitialCsvFile(frameNo,productId,production,selectedRow);
			 dto = new LetInspectionDownloadDto( null,  frameNo,productId,  getLineNo().getText(),  getYear().getText(),getModel().getText(),  getOption().getText(),  getType().getText(),null,  null,null,  null,production,  (String)getProgramTable().getModel().getValueAt(selectedRow, 0),  (Integer)getProgramTable().getModel().getValueAt(selectedRow, 1),null,  null);
			 csvResult = new StringBuffer("");
			if (((!productId.equals("") || !frameNo.equals("")) && !withWildcard) || gcStartDate == null || gcEndDate == null) {
				processEmptyProductStartEndDate();
			} else {
				processNotEmptyProductStartEndtDate();
			}
			if (((Boolean)getResultsExist()).booleanValue()) {
				File file = chooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".csv")) {
					file = new File(file.getAbsolutePath() + ".csv");
				}
				try {
					Set setParamKind = getParamKindSet();
					for (Iterator itrParamKind = setParamKind.iterator(); itrParamKind.hasNext();) {
						
						String paramKind = ((String) itrParamKind.next()).substring(2);
						boolean lowLimitFlag = isLowLimitAvailable(paramKind);
						boolean highLimitFlag = isHighLimitAvailable(paramKind);
						
						if (lowLimitFlag && highLimitFlag) {
							csvFile.append("," + StringUtil.csvCommaCheckAddQuotes(paramKind) + ",LoLimit,HiLimit,Unit");
						} else if (lowLimitFlag) {
							csvFile.append("," + StringUtil.csvCommaCheckAddQuotes(paramKind) + ",LoLimit,Unit");
						} else if (highLimitFlag) {
							csvFile.append("," + StringUtil.csvCommaCheckAddQuotes(paramKind) + ",HiLimit,Unit");
						} else {
							csvFile.append("," + StringUtil.csvCommaCheckAddQuotes(paramKind) + ",Unit");
						}
					}
					bw = new BufferedWriter(new FileWriter(file));
					bw.write(csvFile.toString() + "\n");
					BufferedReader br = new BufferedReader(new FileReader(dataFile));
					String in_rec = null;
					while ((in_rec = br.readLine()) != null) {
						bw.write(in_rec + "\n");
					}
					br.close();
					bw.close();
					dataFile.delete();
					Logger.getLogger(this.getApplicationId()).info("Download completed successfully.");
					setMessage("Download completed successfully.");  
				} catch (IOException e) {
					Logger.getLogger(this.getApplicationId()).error("Failed to create file. Confirm not opened by another application.");
					setErrorMessage("Failed to create file. Confirm not opened by another application.");               
					dataFile.delete();
				}
			} else {
				Logger.getLogger(this.getApplicationId()).error("No record matched. Aborted download.");
				setErrorMessage("No record matched. Aborted download.");  
				dataFile.delete();
			}

		}  catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");
		} finally {
			if (dataFile != null) {
				dataFile.delete();
			}
		}
	}
	
	/**
	 * Method will be used to check if low limit value available for given
	 * parameter.
	 * 
	 * @param param
	 * @return
	 */
	private boolean isLowLimitAvailable(String param) {
		return lowLimitMap.containsKey(param);
	}

	/**
	 * Method will be used to check if high limit value available for given
	 * parameter.
	 * 
	 * @param param
	 * @return
	 */
	private boolean isHighLimitAvailable(String param) {
		return highLimitMap.containsKey(param);
	}

	private void processNotEmptyProductStartEndtDate() {

		try {
			dto.setStartDate(gcStartDate);
			dto.setEndDate(gcEndDate);
			GregorianCalendar gcProStartDate = (GregorianCalendar) gcStartDate.clone();
			GregorianCalendar gcProEndDate = (GregorianCalendar) gcEndDate.clone();
			bw = new BufferedWriter(new FileWriter(dataFile));
			dto.setProStartDate(gcProStartDate);
			dto.setProEndDate(gcProEndDate);
				
			try {
				dto.setEndTimestampStartDate(letUtil.getStartTime(format2.format(gcStartDate.getTime()),letPropertyBean.getProcessLocation()));
				dto.setEndTimestampEndDate(letUtil.getEndTime(format2.format(gcEndDate.getTime()),letPropertyBean.getProcessLocation()));
			} catch (Exception e) {
				Logger.getLogger(getApplicationId()).error(e,"Schedule data not found");
				e.printStackTrace();
				setErrorMessage("Schedule data not found"); 
				return;
			}
				
			fetchResults(dto);
			csvResult.append(getCsvResults());
			setCsvResults("");
			try {
				bw.write(csvResult.toString());
				csvResult = new StringBuffer("");
			} catch (IOException e) {
				Logger.getLogger(this.getApplicationId()).error("Failed to create file. Confirm not opened by another application.");
				setErrorMessage("Failed to create file. Confirm not opened by another application.");               
			}
			bw.close();
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");
		}		
	}

	private void processEmptyProductStartEndDate() {

		try {
			if (gcStartDate != null) {
				dto.setStartDate(gcStartDate);
				dto.setProStartDate(gcStartDate);
				try {
					dto.setEndTimestampStartDate(letUtil.getStartTime(format2.format(gcStartDate.getTime()),letPropertyBean.getProcessLocation()));
				} catch (Exception e) {
					Logger.getLogger(getApplicationId()).error(e,"Schedule data not found");
					e.printStackTrace();
					setErrorMessage("Schedule data not found"); 
					return;
				}
			}
			if (gcEndDate != null) {
				dto.setEndDate(gcEndDate);
				dto.setProEndDate(gcEndDate);
				try {
					dto.setEndTimestampEndDate(letUtil.getEndTime(format2.format(gcEndDate.getTime()),letPropertyBean.getProcessLocation()));
				} catch (Exception e) {
					Logger.getLogger(getApplicationId()).error(e,"Schedule data not found");
					e.printStackTrace();
					setErrorMessage("Schedule data not found"); 
					return;
				}
			}
			fetchResults(dto);
			csvResult.append(getCsvResults());
			setCsvResults("");
			try {
				bw = new BufferedWriter(new FileWriter(dataFile));
				bw.write("\n" + csvResult.toString());
				bw.close();
				csvResult = new StringBuffer("");

			} catch (IOException e) {
				Logger.getLogger(this.getApplicationId()).error("Failed to create file. Confirm not opened by another application.");
				setErrorMessage("Failed to create file. Confirm not opened by another application.");               
			}               
			Logger.getLogger(this.getApplicationId()).info("Download completed successfully.");
			setMessage("Download completed successfully.");
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");
		}  		
	}

	private StringBuffer createInitialCsvFile(String frameNo, String productId, String production,Integer selectedRow) {
		StringBuffer csvFile=null;
		try {
			if (useFrameNo.equals("true")) {
				csvFile = new StringBuffer(CSV_CON_COLUMN_JP+ dfTimestamp.format(new Date()) + ",");
				csvFile.append(StringUtil.csvCommaCheckAddQuotes(frameNo) + ",");
			} else {
				csvFile = new StringBuffer(CSV_CON_COLUMN+ dfTimestamp.format(new Date()) + ",");
			}
			csvFile.append(StringUtil.csvCommaCheckAddQuotes(productId) + ",");
			csvFile.append(StringUtil.csvCommaCheckAddQuotes(getLineNo().getText()) + ",");
			csvFile.append(StringUtil.csvCommaCheckAddQuotes(getYear().getText()) + ",");
			csvFile.append(StringUtil.csvCommaCheckAddQuotes(getModel().getText()) + ",");
			csvFile.append(StringUtil.csvCommaCheckAddQuotes(getType().getText()) + ",");
			csvFile.append(StringUtil.csvCommaCheckAddQuotes(getOption().getText()) + ",");
			if (gcStartDate == null) {
				csvFile.append(",");
			} else {
				csvFile.append(format1.format(gcStartDate.getTime()) + ",");
			}
			if (gcEndDate == null) {
				csvFile.append(",");
			} else {
				csvFile.append(format1.format(gcEndDate.getTime()) + ",");
			}
			csvFile.append(StringUtil.csvCommaCheckAddQuotes(production) + ",");
			csvFile.append(StringUtil.csvCommaCheckAddQuotes((String) getProgramTable().getModel().getValueAt(selectedRow, 0)) + "\n\n");
			csvFile.append(CSV_RESULT_HEADER);
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");
		}
		return csvFile;
	}

	private boolean verifyEmptyProductIdStartEndDate() {
		try {
			if (getProductId().getText().equals("")&& getFrameNo().getText().equals("")&& (getStart().getText().equals("") || getEnd().getText().equals(""))) {
				Logger.getLogger(this.getApplicationId()).error(frameNoErrMsg);
				setErrorMessage(frameNoErrMsg);
				return false;
			}
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");
			return false;
		}
		return true;
	}

	private boolean verifyProgramTableSelection() {
		try {
			if (getProgramTable().getSelectedRow() == -1) {
				Logger.getLogger(this.getApplicationId()).error("Choose a Program");
				setErrorMessage("Choose a Program");  
				return false;
			}
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");
			return false;
		}
		return true;
	}

	private boolean validateProduction(String production) {
		try {
			if (production != null && !production.equals("")) {
				if (!production.equals("0") && !production.equals("1")) {
					Logger.getLogger(this.getApplicationId()).error("Please input either of 0 or 1 to MassPro/Event division.");
					setErrorMessage("Please input either of 0 or 1 to MassPro/Event division.");  
					return false;
				}
			}
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");
			return false;
		}
		return true;
	}

	private boolean fetchStartEndDate() {
		try {
			if (!getStart().getText().equals("")) {
				try {
					gcStartDate = new GregorianCalendar();
					gcStartDate.setLenient(false);
					gcStartDate.set(Integer.parseInt(getStart().getText().substring(0, 4)),Integer.parseInt(getStart().getText().substring(4, 6)) - 1,Integer.parseInt(getStart().getText().substring(6, 8)));
					gcStartDate.getTime();
				} catch (Exception e) {
					Logger.getLogger(this.getApplicationId()).error("Invalid start date.");
					setErrorMessage("Invalid start date.");  
					return false;
				}
			}
			if (!getEnd().getText().equals("")) {
				try {
					gcEndDate = new GregorianCalendar();
					gcEndDate.setLenient(false);
					gcEndDate.set(Integer.parseInt(getEnd().getText().substring(0, 4)),Integer.parseInt(getEnd().getText().substring(4, 6)) - 1,Integer.parseInt(getEnd().getText().substring(6, 8)));
					gcEndDate.getTime();
				} catch (Exception e) {
					Logger.getLogger(this.getApplicationId()).error("Invalid end date.");
					setErrorMessage("Invalid end date.");  
					return false;
				}
			}
			if (gcStartDate != null && gcEndDate != null) {
				GregorianCalendar gcCompStartDate = (GregorianCalendar) gcStartDate.clone();
				gcCompStartDate.add(Calendar.DATE, iDateRange);
				if (gcStartDate.after(gcEndDate)) {                    	
					Logger.getLogger(this.getApplicationId()).error("Start date should not exceed end date.");
					setErrorMessage("Invalid end date.");  
					return false;
				} else if (gcCompStartDate.before(gcEndDate)) {
					Logger.getLogger(this.getApplicationId()).error("Date range exceeds limit.");
					setErrorMessage("Date range exceeds limit.");  
					return false;
				}
			}
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error("Invalid date.");
			setErrorMessage("Invalid date.");  
			return false;
		}
      return true;
	}

	private boolean validateFrameNo(String frameNo) {

		try {
			int questPos = frameNo.indexOf("?");
			int asterPos = frameNo.indexOf("*");
			if (asterPos == -1) {
				if (frameNo.length() != 10) {
					Logger.getLogger(this.getApplicationId()).error("Series frame number must be 10 figures.");
					setErrorMessage("Series frame number must be 10 figures.");  
					return false;
				}
			} else {
				if (asterPos != frameNo.length() - 1) {
					Logger.getLogger(this.getApplicationId()).error("'*' must be the last character.");
					setErrorMessage("'*' must be the last character.");  
					return false;
				}
			}
			if (questPos != -1 || asterPos != -1) {
				withWildcard = true;

				if (getStart().getText().equals("") || getEnd().getText().equals("")) {
					Logger.getLogger(this.getApplicationId()).error("Period specification is indispensable when (?, *) are contained in VIN or the series frame No.");
					setErrorMessage("Period specification is indispensable when (?, *) are contained in VIN or the series frame No.");  
					return false;
				}
			}
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");
			return false;
		}

		return true;
	}

	private boolean validateProductId(String productId) 
	{	
		try {
			int questPos = productId.indexOf("?");
			int asterPos = productId.indexOf("*");
			if (asterPos == -1) {
				if (productId.length() != 11 && productId.length() != 17) {
					Logger.getLogger(this.getApplicationId()).error("VIN must be 11 or 17 figures.");
					setErrorMessage("VIN must be 11 or 17 figures.");  
					return false;
				}
				while (productId.length() < 17) {
					productId = " " + productId;
				}

			} else {
				if (asterPos != productId.length() - 1) {
					Logger.getLogger(this.getApplicationId()).error("'*' must be the last character.");
					setErrorMessage("'*' must be the last character.");  
					return false;
				}
			}
			if (questPos != -1 || asterPos != -1) {
				withWildcard = true;

				if (getStart().getText().equals("") || getEnd().getText().equals("")) {
					Logger.getLogger(this.getApplicationId()).error("Period specification is indispensable when (?, *) are contained in VIN or the series frame No.");
					setErrorMessage("Period specification is indispensable when (?, *) are contained in VIN or the series frame No.");  
					return false;
				}
			}
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");
			return false;
		}

		return true;
	}

	private JLabel getProgramLabel() {
		if (programLabel == null) {
			programLabel = new JLabel();
			programLabel.setSize(210, 33);
			programLabel.setText(Message.get("InspectionProgram"));
			programLabel.setLocation(413, 248);
		}
		return programLabel;
	}

	private JScrollPane getProgramScrollPane() {
		if (programScrollPane == null) {
			programScrollPane = new JScrollPane();
			programScrollPane.setViewportView(getProgramTable());
			programScrollPane.setBounds(410, 278, 218, 200);
			programScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return programScrollPane;
	}

	private JTable getProgramTable() {
		if (programTable == null) {
			programTable = new JTable();
			programTable.setName("programTable");
			programTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			programTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			model.addColumn(Message.get("Program"));
			programTable.setModel(model);
		}
		return programTable;
	}

	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("(YYYYMMDD)");
		}
		return jLabel1;
	}

	private javax.swing.JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new javax.swing.JLabel();
		}
		return jLabel2;
	}

	private javax.swing.JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("      ");
		}
		return jLabel4;
	}

	private javax.swing.JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new javax.swing.JLabel();
			jLabel3.setText("");
		}
		return jLabel3;
	}

	private javax.swing.JLabel getProductionLabel() {
		if (productionLabel == null) {
			productionLabel = new javax.swing.JLabel();
			productionLabel.setText(Message.get("PRODUCTION"));
		}
		return productionLabel;
	}

	private javax.swing.JTextField getProduction() {
		if (productionTextField == null) {
			productionTextField = new javax.swing.JTextField();
			productionTextField.setColumns(2);
			productionTextField.setDocument(new AsciiDocument(1));
			productionTextField.setFont(new Font("Dialog", java.awt.Font.PLAIN, 18));
		}
		return productionTextField;
	}

	public void actionPerformed(ActionEvent arg) {

	}

	private Vector fetchAllLetPrograms()
	{
		try {
			programsVector = new Vector();
			List<LetInspectionProgram> letInspectionProgramList = getDao(LetInspectionProgramDao.class).findAllLetInspPgmOrderByPgmName();		
			for(LetInspectionProgram letInspectionProgram : letInspectionProgramList){
				Vector rowData = new Vector();
				if ((Integer) letInspectionProgram.getInspectionPgmId() != null) {
					rowData.addElement(letInspectionProgram.getInspectionPgmName());
					rowData.addElement(letInspectionProgram.getInspectionPgmId());

				}
				programsVector.add(rowData);
			}
		} catch (Exception e) {			
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Inspection Result Download screen");
		}
		return programsVector;
	}

	public Set getParamKindSet() {
		return paramKindSet;
	}

	public void setParamKindSet(Set paramKindSet) {
		this.paramKindSet = paramKindSet;
	}

	public String getCsvResults() {
		return csvResults;
	}

	public void setCsvResults(String csvResults) {
		this.csvResults = csvResults;
	}

	public Boolean getResultsExist() {
		return resultsExist;
	}

	public void setResultsExist(Boolean resultsExist) {
		this.resultsExist = resultsExist;
	}

	public void fetchResults(LetInspectionDownloadDto dto)
	{
		try {
			List<Object[]> programResultData=getDao(LetProgramResultDao.class).fetchLetProgramResultDownloadData(dto);
			Vector seqIndex =new Vector();
			for (Object[] letProgramResult:programResultData)
			{
				Vector rowData = new Vector(Arrays.asList(letProgramResult)); 			 
				seqIndex.add(rowData);
			}
			if (seqIndex.size() < 1) {
				return ;
			}
			resultsExist=Boolean.TRUE;
			List<Object[]> programResultValueData=getDao(LetProgramResultDao.class).fetchLetProgramResultValueDownloadData(dto);
			Map valueIndex = new HashMap();     
			paramKindSet.clear();
			lowLimitMap.clear();
			highLimitMap.clear();
			for (Object[] letProgramResultValue:programResultValueData)
			{          				
				String productId = (String)letProgramResultValue[0];
				Integer testSeq = (Integer)letProgramResultValue[1];
				String paramName = (String)letProgramResultValue[2];
				String paramType = (String)letProgramResultValue[5];
				String lowLimit = (String) letProgramResultValue[6];
				String highLimit = (String)letProgramResultValue[7];
				String[] value = { StringUtils.defaultIfEmpty((String) letProgramResultValue[3], ""),
						StringUtils.defaultIfEmpty(lowLimit, ""), StringUtils.defaultIfEmpty(highLimit, ""),
						StringUtils.defaultIfEmpty((String) letProgramResultValue[4], "") };
				valueIndex.put(productId + "-" + testSeq + "-" + paramType + "-" + paramName, value);
				paramKindSet.add(paramType + "-" + paramName);
				if (StringUtils.isNotBlank(lowLimit))
					lowLimitMap.put(paramName, lowLimit);
				if (StringUtils.isNotBlank(highLimit))
					highLimitMap.put(paramName, highLimit);
			}
			StringBuffer sbfCSV = new StringBuffer("");
			sbfCSV = allData_added_toCSV(seqIndex, paramKindSet, valueIndex, sbfCSV);
			setCsvResults( sbfCSV.toString());
		} catch (Exception e) {
			Logger.getLogger().error(e,"An error Occurred while processing the LET Download Results screen");
			e.printStackTrace();
		}
	} 

	protected StringBuffer getProgramResult( Vector vecPhy703TblName,StringBuffer sbfCreateSQL1) {

		try {
			for (Iterator itrPhy703TblName = vecPhy703TblName.iterator(); itrPhy703TblName.hasNext();) 
			{
				sbfCreateSQL1.append("SELECT END_TIMESTAMP, "
						+ "PRODUCT_ID, "
						+ "TEST_SEQ, "
						+ "INSPECTION_PGM_ID, "
						+ "INSPECTION_PGM_STATUS, "
						+ "PROCESS_STEP, "
						+ "PROCESS_END_TIMESTAMP,"
						+ "LET_TERMINAL_ID, "
						+ "LET_RESULT_CAL "
						+ "FROM " + itrPhy703TblName.next());
				if (itrPhy703TblName.hasNext()) 
				{
					sbfCreateSQL1.append(" UNION ALL ");
				}
			}
		} catch (Exception e) {
			Logger.getLogger().error(e,"An error Occurred while processing the LET Download Results screen");
			e.printStackTrace();
		}
		return sbfCreateSQL1;
	}

	protected StringBuffer allData_added_toCSV(Vector seqIndex,Set setParamKind, Map valueIndex,StringBuffer sbfCSV)
	{
		try {
			for (Iterator itrSeqIndex = seqIndex.iterator(); itrSeqIndex.hasNext();) {
				Vector value = (Vector) itrSeqIndex.next();
				if (value == null) {
					continue;
				}
				String status = (String) value.get(9);
				if (status == null) {
					continue;
				}
				String statusValue = LetInspectionStatus.getType(Integer.parseInt(status)).name();
				String processStep = (String) value.get(10);
				String processTimestamp = "";
				if (value.get(11) != null) {
					processTimestamp = dfTimestamp.format((Timestamp) value.get(11));
				}
				String base_release = (String) value.get(14);
				if (base_release == null) {
					base_release = "";
				}
				String stepFile = "";
				if (processStep == null) {
					processStep = "";
				} else if (processStep.substring(0, 1).equals("S")) {
					stepFile = (String) value.get(6);
				} else if (processStep.substring(0, 1).equals("C")) {
					stepFile = (String) value.get(7);
				}
				if (stepFile == null) {
					stepFile = "";
				}
				String cal = (String) value.get(13);
				if (cal == null) {
					cal = "";
				}
				String cell = (String) value.get(12);
				if (cell == null) {
					cell = "";
				}
				String production = (String) value.get(15);
				if (production == null) {
					production = "";
				}
				sbfCSV.append(StringUtil.csvCommaCheckAddQuotes((String) value.get(0)) + ","
						+ StringUtil.csvCommaCheckAddQuotes((String) value.get(1)) + ","
						+ StringUtil.csvCommaCheckAddQuotes((String) value.get(2)) + ","
						+ StringUtil.csvCommaCheckAddQuotes((String) value.get(3)) + ","
						+ StringUtil.csvCommaCheckAddQuotes((String) value.get(4)) + ","
						+ (Integer) value.get(5) + ","
						+ StringUtil.csvCommaCheckAddQuotes(stepFile) + ","
						+ processTimestamp + ","
						+ StringUtil.csvCommaCheckAddQuotes(processStep) + ","
						+ StringUtil.csvCommaCheckAddQuotes(base_release) + ","
						+ StringUtil.csvCommaCheckAddQuotes(cell) + ","
						+ statusValue + ","
						+ StringUtil.csvCommaCheckAddQuotes(production) + ",");

				for (Iterator iteParamKind = setParamKind.iterator(); iteParamKind.hasNext();) {
					String paramKind = (String) iteParamKind.next();
					String paramKey = paramKind.substring(2);
					String[] paramValue = (String[]) valueIndex.get(value.get(0) + "-" + value.get(5) + "-" + paramKind);
					if (paramValue == null) {
						sbfCSV.append(",");
						if (lowLimitMap.containsKey(paramKey)) {
							sbfCSV.append(",");
						}
						if (highLimitMap.containsKey(paramKey)) {
							sbfCSV.append(",");
						}
					} else if (paramValue[0] != null) {
						sbfCSV.append(StringUtil.csvCommaCheckAddQuotes(paramValue[0]) + ",");
						if (lowLimitMap.containsKey(paramKey)) {
							sbfCSV.append(paramValue[1] + ",");
						}
						if (highLimitMap.containsKey(paramKey)) {
							sbfCSV.append(paramValue[2] + ",");
						}
						if (paramValue[3] != null) {
							sbfCSV.append(StringUtil.csvCommaCheckAddQuotes(paramValue[3]));
						}
					}
					if (iteParamKind.hasNext()) {
						sbfCSV.append(",");
					}
				}

				sbfCSV.append("\n");
			}
		} catch (Exception e) {
			Logger.getLogger().error(e,"An error Occurred while processing the LET Download Results screen");
			e.printStackTrace();

		}
		return sbfCSV;
	}



}
