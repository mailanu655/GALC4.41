package com.honda.galc.client.teamleader.let;

import static com.honda.galc.service.ServiceFactory.getDao;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.teamleader.let.util.AsciiDocument;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.NumericDocument;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetDiagResultDao;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.product.LetDiagResult;
import com.honda.galc.message.Message;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 04, 2013
 */
@SuppressWarnings(value = { "all" })
public class LetSelfDiagResultDownloadPanel extends TabbedPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel = null;
	private JLabel startDateLabel = null;
	private JLabel endDateLabel = null;
	private JTextField startDateTextField = null;
	private JTextField endDateTextField = null;
	private JLabel dateFormatLabel = null;
	private JLabel withinDaysLabel = null;
	private JTextField terminalIdTextField = null;
	private JButton downloadButton = null;
	private JLabel terminalIdLabel = null;
	private JLabel titleLabel = null;
	private LetPropertyBean letPropertyBean=null;
	private int iDateRange = 0;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public LetSelfDiagResultDownloadPanel(TabbedMainWindow mainWindow) {
		super("LET Self Diagnosis Result Download Panel", KeyEvent.VK_S,mainWindow);	
		initialize();
	}

	public LetSelfDiagResultDownloadPanel(String screenName, int keyEvent,TabbedMainWindow mainWindow) {
		super(screenName, keyEvent,mainWindow);
	}

	@Override
	public void onTabSelected() {
		setErrorMessage(null);
		Logger.getLogger(this.getApplicationId()).info("LET Self Diagnosis Result Download Panel is selected");
	}

	private void initialize() {		
		try {
			setLayout(new BorderLayout());
			letPropertyBean = PropertyService.getPropertyBean(LetPropertyBean.class, getApplicationId());
			iDateRange = Integer.valueOf(letPropertyBean.getDownloadDateRange()).intValue();
			add(getMainPanel(),BorderLayout.CENTER);
		}  catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Inspection Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Self Diagnosis Result Download screen");      
		}
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(null);
			mainPanel.add(getTitleLabel(), null);
			mainPanel.add(getStartDateLabel(), null);
			mainPanel.add(getStartDateTextField(), null);
			mainPanel.add(getEndDateLabel(), null);
			mainPanel.add(getEndDateTextField(), null);
			mainPanel.add(getDateFormatLabel(), null);
			mainPanel.add(getWithinDaysLabel(), null);
			mainPanel.add(getTerminalIdLabel(), null);
			mainPanel.add(getTerminalIdTextField(), null);
			mainPanel.add(getDownloadButton(), null);

		}
		return mainPanel;
	}

	private JLabel getTitleLabel() {
		if (titleLabel == null) {
			titleLabel = new JLabel();
			titleLabel.setSize(755, 30);
			titleLabel.setText(Message.get("SelfDiagnosisResultDownload"));
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
			titleLabel.setLocation(130, 15);
			titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		}
		return titleLabel;
	}

	private JLabel getStartDateLabel() {
		if (startDateLabel == null) {
			startDateLabel = new JLabel();
			startDateLabel.setSize(60, 35);
			startDateLabel.setText(Message.get("StartDate"));
			startDateLabel.setLocation(225, 100);
		}
		return startDateLabel;
	}

	private JTextField getStartDateTextField() {
		if (startDateTextField == null) {
			startDateTextField = new JTextField();
			startDateTextField.setSize(110, 35);
			startDateTextField.setText("");
			startDateTextField.setName("startDateTextField");
			startDateTextField.setFont(new Font("Dialog", Font.PLAIN, 18));
			startDateTextField.setColumns(7);
			startDateTextField.setLocation(295, 100);
			startDateTextField.setDocument(new NumericDocument(8));
		}
		return startDateTextField;
	}

	private JLabel getEndDateLabel() {
		if (endDateLabel == null) {
			endDateLabel = new JLabel();
			endDateLabel.setSize(55, 35);
			endDateLabel.setText(Message.get("EndDate"));
			endDateLabel.setLocation(435, 100);
		}
		return endDateLabel;
	}

	private JTextField getEndDateTextField() {
		if (endDateTextField == null) {
			endDateTextField = new JTextField();
			endDateTextField.setSize(110, 35);
			endDateTextField.setText("");
			endDateTextField.setName("endDateTextField");
			endDateTextField.setFont(new Font("Dialog", Font.PLAIN, 18));
			endDateTextField.setColumns(7);
			endDateTextField.setLocation(500, 100);
			endDateTextField.setDocument(new NumericDocument(8));
		}
		return endDateTextField;
	}

	private JLabel getDateFormatLabel() {
		if (dateFormatLabel == null) {
			dateFormatLabel = new JLabel();
			dateFormatLabel.setSize(75, 35);
			dateFormatLabel.setText("(YYYYMMDD)");
			dateFormatLabel.setLocation(615, 100);
		}
		return dateFormatLabel;
	}

	private JLabel getWithinDaysLabel() {
		if (withinDaysLabel == null) {
			withinDaysLabel = new JLabel();
			withinDaysLabel.setSize(90, 35);
			withinDaysLabel.setLocation(710, 100);			
			String[] paramStringArray = new String[]{Integer.toString(iDateRange)};
			withinDaysLabel.setText((String)Message.get("WITHIN_N_DAYS", paramStringArray));
		}
		return withinDaysLabel;
	}

	private JLabel getTerminalIdLabel() {
		if (terminalIdLabel == null) {
			terminalIdLabel = new JLabel();
			terminalIdLabel.setSize(75, 35);
			terminalIdLabel.setText(Message.get("TERMINAL_ID"));
			terminalIdLabel.setLocation(220, 160);
		}
		return terminalIdLabel;
	}

	private JTextField getTerminalIdTextField() {
		if (terminalIdTextField == null) {
			terminalIdTextField = new JTextField();
			terminalIdTextField.setSize(55, 35);
			terminalIdTextField.setText("");
			terminalIdTextField.setFont(new Font("Dialog", Font.PLAIN, 18));
			terminalIdTextField.setColumns(4);
			terminalIdTextField.setLocation(295, 160);
			terminalIdTextField.setDocument(new AsciiDocument(3));
		}
		return terminalIdTextField;
	}

	private JButton getDownloadButton() {
		if (downloadButton == null) {
			downloadButton = new JButton();
			downloadButton.setSize(140, 35);
			downloadButton.setName("downloadButton");
			downloadButton.setText(Message.get("DOWNLOAD"));
			downloadButton.setLocation(666, 160);
			downloadButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onDownload();
				}
			});
		}
		return downloadButton;
	}

	private void onDownload() 
	{
		setErrorMessage("");
		String startDate = startDateTextField.getText();
		String endDate = endDateTextField.getText();
		String terminalId = terminalIdTextField.getText();
		if (!validateInput(startDate,endDate,terminalId)) return;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setName("fileChooser");
		fileChooser.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				}
				return file.getName().toLowerCase().endsWith(".csv");
			}
			public String getDescription() {
				return "CSV File";
			}
		});
		File csvFile = new File("Self_Diagnosis_Result.csv");
		fileChooser.setSelectedFile(csvFile);
		int returnVal = fileChooser.showSaveDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}
		String csvData = "";
		try {
			csvData=fetchResults(startDate,endDate,terminalId);
			if (csvData.equals("")) {
				Logger.getLogger(this.getApplicationId()).error("No data matched. Aborted download.");
				setErrorMessage("No data matched. Aborted download.");
				return;
			}
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Self Diagnosis Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Self Diagnosis Result Download screen");
		} 
		String filePath = fileChooser.getSelectedFile().getPath();
		if (!filePath.toLowerCase().endsWith(".csv")) {
			filePath = filePath + ".csv";
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(csvData);
			Logger.getLogger(this.getApplicationId()).info("Download completed successfully.");
			setMessage("Download completed successfully.");			
		} catch (IOException e) {
			Logger.getLogger(this.getApplicationId()).error(e,"Failed to create file. Confirm not opened by another application.");
			e.printStackTrace();
			setErrorMessage("Failed to create file. Confirm not opened by another application.");
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					Logger.getLogger(this.getApplicationId()).error(e,"Failed to create file. Confirm not opened by another application.");
					e.printStackTrace();
					setErrorMessage("Failed to create file. Confirm not opened by another application.");
				}
			}
		}
	}



	private boolean validateInput(String startDate, String endDate, String terminalId) 
	{
		if (startDate.equals("")) {
			Logger.getLogger(this.getApplicationId()).error("Enter start date.");
			setErrorMessage("Enter start date.");
			return false;
		}
		if (endDate.equals("")) {
			Logger.getLogger(this.getApplicationId()).error("Enter end date.");
			setErrorMessage("Enter end date.");
			return false;
		}
		GregorianCalendar gcStartDate = new GregorianCalendar();
		try {
			gcStartDate.setLenient(false);
			gcStartDate.set(Integer.parseInt(startDate.substring(0, 4)),Integer.parseInt(startDate.substring(4, 6)) - 1,Integer.parseInt(startDate.substring(6, 8)));
			gcStartDate.getTime();
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error("Invalid start date.");
			setErrorMessage("Invalid start date.");
			return false;
		}
		GregorianCalendar gcEndDate = new GregorianCalendar();
		try {
			gcEndDate.setLenient(false);
			gcEndDate.set(Integer.parseInt(endDate.substring(0, 4)),Integer.parseInt(endDate.substring(4, 6)) - 1,Integer.parseInt(endDate.substring(6, 8)));
			gcEndDate.getTime();
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error("Invalid end date.");
			setErrorMessage("Invalid end date.");
			return false;
		}
		GregorianCalendar gcCompStartDate =(GregorianCalendar) gcStartDate.clone();
		gcCompStartDate.add(Calendar.DATE, iDateRange);
		if (gcStartDate.after(gcEndDate)) {
			Logger.getLogger(this.getApplicationId()).error("Start date should not exceed end date.");
			setErrorMessage("Start date should not exceed end date.");
			return false;
		} else if (gcCompStartDate.before(gcEndDate)) {
			Logger.getLogger(this.getApplicationId()).error("Date range exceeds the limit.");
			setErrorMessage("Date range exceeds the limit.");			
			return false;
		}
		if (terminalId != null&& !"".equals(terminalId) && terminalId.length() < 3) {
			Logger.getLogger(this.getApplicationId()).error("Terminal ID must be 3 figures");
			setErrorMessage("Terminal ID must be 3 figures");			
			return false;
		}

		return true;
	}



	public void actionPerformed(ActionEvent arg) {
	}

	private String fetchResults(String startDateStr, String endDateStr,String terminalId){
		LetUtil letUtil=new LetUtil();
		String startDate=null;
		try {
			startDate = letUtil.getStartTime(startDateStr,letPropertyBean.getProcessLocation());
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"Schedule data not found");
			e.printStackTrace();
			setErrorMessage("Schedule data not found"); 
			return "";
		}
		String endDate=null;
		try {
			endDate = letUtil.getEndTime(endDateStr,letPropertyBean.getProcessLocation());
		} catch (Exception e) {
			Logger.getLogger(getApplicationId()).error(e,"Schedule data not found");
			e.printStackTrace();
			setErrorMessage("Schedule data not found"); 
			return "";
		}
		Vector csvDatas =getDiagResult(startDate, endDate, terminalId);
		if (csvDatas == null) {
			return "";
		}
		TreeMap paramValueMap = new TreeMap();
		HashMap[] testStatusMap = getDiagDetail(csvDatas, paramValueMap);
		HashMap faultCountMap = getDiagFaultCount(startDate, endDate, terminalId, paramValueMap);
		getDiagFaultDetail(csvDatas,paramValueMap,testStatusMap,faultCountMap);
		StringBuffer stb = formatData(csvDatas);
		return stb.toString();
	}


	private Vector getDiagResult(String startDate,String endDate,String terminalId)
	{
		Vector result = new Vector();
		try {
			java.util.Date parsedStartDate = dateFormat.parse(startDate);
			java.sql.Timestamp startTimestamp = new java.sql.Timestamp(parsedStartDate.getTime());
			java.util.Date parsedEndDate = dateFormat.parse(endDate);
			java.sql.Timestamp endTimestamp = new java.sql.Timestamp(parsedEndDate.getTime());
			List<LetDiagResult> letDiagResultList=getDao(LetDiagResultDao.class).getLetDiagRsltBtwStartEndTmp(startTimestamp, endTimestamp, terminalId);
			if(letDiagResultList.size()==0)
			{
				Logger.getLogger(this.getApplicationId()).error("Diag result not found.");
				setErrorMessage("Diag result not found.");
				return null;
			}

			Vector title = new Vector();
			title.add("Finish Time");
			title.add("Cell");
			title.add("Total Status");
			title.add("Cal");
			title.add("SWVer");
			List<Object[]> letDiagColumn = getDao(LetDiagResultDao.class).getLetDiagColumns(startTimestamp, endTimestamp, terminalId);
			for (Object[] columnHeader : letDiagColumn) {
				title.add(((String)columnHeader[0]).trim());
			}
			result.add(title);
			for (LetDiagResult diagResult:letDiagResultList) {
				Vector line = new Vector();
				Map<String, Integer> letDiagDetailMap = new HashMap<String, Integer>();
				List<Object[]> letDiagResultListView = getDao(LetDiagResultDao.class).getLetDiagRsltView(diagResult.getId().getEndTimestamp(), diagResult.getId().getLetTerminalId());
				for (Object[] ob : letDiagResultListView) {
					letDiagDetailMap.put(((String)ob[0]).trim(), Integer.valueOf((String) ob[2]));
				}
				result.add(line);
				line.add(dateFormat.format(diagResult.getId().getEndTimestamp()));
				line.add(diagResult.getId().getLetTerminalId());
				Integer value =Integer.parseInt(diagResult.getTotalResultStatus()) ;
				line.add(LetInspectionStatus.getType(value).name());
				line.add(StringUtils.trimToEmpty(diagResult.getBaseRelease()));
				line.add(StringUtils.trimToEmpty(diagResult.getSoftwareVersion()));
				
				Vector vec = (Vector) result.elementAt(0);
				for(int i = 5; i < vec.size(); i++) {
					String columnName = ((String) vec.elementAt(i)).trim();
					if (letDiagDetailMap.containsKey(columnName)){
						Integer value1 = letDiagDetailMap.get(columnName) ;
						line.add(LetInspectionStatus.getType(value1).name());
					} else {
						line.add(StringUtils.trimToEmpty(""));
					}
				}
			}

		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Self Diagnosis Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Self Diagnosis Result Download screen");			
		} 
		return result;
	}


	private HashMap[] getDiagDetail(Vector csvDatas,TreeMap paramValueMap)
	{
		HashMap[] testStatusMap = new HashMap[csvDatas.size() - 1];
		try {		
			for (int i = 1; i < csvDatas.size(); i++) {
				Vector line = (Vector) csvDatas.get(i);
				String finishTime = (line.get(0)).toString();
				String cell = (String) line.get(1);
				List<Object[]> letDiagNamesDetailsList = getDao(LetDiagResultDao.class).getLetDiagNamesWithDetails(finishTime, cell);
				if(letDiagNamesDetailsList.size()==0)
				{
					Logger.getLogger(this.getApplicationId()).error("Diag detail not found.");
					setErrorMessage("Diag detail not found.");
					return null;
				}
				testStatusMap[i - 1] = new HashMap();
				for(Object[] diagNameDetail:letDiagNamesDetailsList)
				{
					paramValueMap.put(diagNameDetail[0],diagNameDetail[1]);
					Integer value =Integer.parseInt((String) diagNameDetail[2]) ;
					testStatusMap[i - 1].put(diagNameDetail[0],LetInspectionStatus.getType(value).name());
				}
			}			
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Self Diagnosis Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Self Diagnosis Result Download screen");
		} 
		return testStatusMap;
	}


	private HashMap getDiagFaultCount(String startDate,String endDate,String terminalId,TreeMap paramValueMap)
	{
		HashMap faultCountMap = new HashMap();
		try {
			Iterator it = paramValueMap.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				faultCountMap.put(key, "0");
			}	
			List<Object[]> maxFaultCodeCountWithTestIdList = getDao(LetDiagResultDao.class).getMaxFaultCodeCountWithTestId(startDate, endDate, terminalId);			
			for(Object[] maxFaultCodeCountWithTestId:maxFaultCodeCountWithTestIdList)
			{
				Integer diagTestID = (Integer) maxFaultCodeCountWithTestId[0];
				int count = Integer.parseInt((String) faultCountMap.get(diagTestID));
				if (count < ((Integer)maxFaultCodeCountWithTestId[1]).intValue()) {
					faultCountMap.put(diagTestID, ((Integer) maxFaultCodeCountWithTestId[1]).toString());
				}
			}		
		} catch (Exception e) {			
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Self Diagnosis Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Self Diagnosis Result Download screen");		
		} 
		return faultCountMap;
	}


	private void getDiagFaultDetail(Vector csvDatas,TreeMap paramValueMap,HashMap[] testStatusMap,HashMap faultCountMap)
	{
		try {
			Vector title = (Vector) csvDatas.get(0);
			Object[] keys = paramValueMap.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				title.add(paramValueMap.get(keys[i]));
				int faultCount = Integer.parseInt((String) faultCountMap.get(keys[i]));
				for (int j = 0; j < faultCount; j++) {
					title.add("Fault Code");
					title.add("Short Desc");
				}
			}
			for (int i = 1; i < csvDatas.size(); i++) {
				Vector line = (Vector) csvDatas.get(i);
				String finishTime = (line.get(0)).toString();
				String cell = (String) line.get(1);
				List<Object[]> letDiagParamValuesList = getDao(LetDiagResultDao.class).getLetDiagParamValues(finishTime,cell);			
				HashMap faultValueMap = new HashMap();
				HashMap faultCntMap = new HashMap();

				Integer beforeDiagTestID = null;
				Vector faultValue = null;
				int faultCnt = 0;

				for (Object[] letDiagParamValue:letDiagParamValuesList) {
					Integer diagTestID = (Integer) letDiagParamValue[0];
					if (diagTestID.equals(beforeDiagTestID)) {
						faultValue.add((String) letDiagParamValue[1]);
						faultValue.add((String) letDiagParamValue[2]);
						faultCnt++;
					} else {
						faultValue = new Vector();
						faultValue.add((String) letDiagParamValue[1]);
						faultValue.add((String) letDiagParamValue[2]);
						beforeDiagTestID = diagTestID;
						faultCnt = 1;
					}
					faultValueMap.put(diagTestID, faultValue);
					faultCntMap.put(diagTestID, new Integer(faultCnt));
				}

				for (int j = 0; j < keys.length; j++) {
					int totalFaultCount = Integer.parseInt((String) faultCountMap.get(keys[j]));
					faultCnt = 0;
					if (testStatusMap[i - 1].containsKey(keys[j]))
						line.add(testStatusMap[i - 1].get(keys[j]));
					else
						line.add("");
					if (faultValueMap.containsKey(keys[j])) {
						Vector vecFaultValue = (Vector) faultValueMap.get(keys[j]);
						for (Iterator ite = vecFaultValue.iterator(); ite.hasNext();) {
							line.add(ite.next());
						}
						faultCnt = ((Integer) faultCntMap.get(keys[j])).intValue();
					}
					for (int k = 0; k < totalFaultCount - faultCnt; k++) {
						line.add("");
						line.add("");
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger(this.getApplicationId()).error(e,"An error Occurred while processing the LET Self Diagnosis Result Download screen");
			e.printStackTrace();
			setErrorMessage("An error Occurred while processing the LET Self Diagnosis Result Download screen");

		} 
	}

	private StringBuffer formatData(Vector csvDatas) 
	{
		StringBuffer stb = new StringBuffer();
		for (Iterator i = csvDatas.iterator(); i.hasNext();) 
		{
			Vector line = (Vector) i.next();
			for (Iterator j = line.iterator(); j.hasNext();) 
			{
				stb.append(StringUtil.csvCommaCheckAddQuotes((j.next()).toString()) + ",");
			}
			stb.replace(stb.length() - 1, stb.length(), "\n");
		}
		return stb;
	}
}