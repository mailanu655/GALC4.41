/**
 * 
 */
package com.honda.galc.client.schedule;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Zack chai
 * @date Jan 14, 2014
 */
public class EngineCureStatusPanel extends ApplicationMainPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2274554063761263120L;


	/**
	 * 
	 */

	public static final String panel_title = "Engine Cure Status";
	
	
	private String _panelName = "";
	
	private String _btnName = "Refresh";
	
	private final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private final String PROPERTY_WAIT_TIMER_KEY = "WAIT_TIMER";
	
	private final String PROPERTY_CHECK_LINE_IDS_KEY = "CHECK_LINE_IDS";
	
	private final String PROPERTY_CURE_TIME_START_PART_NAME_KEY = "CURE_TIME_START_PART_NAME";
	
	private final String PROPERTY_UPDATE_LINE_ID_NAME_KEY = "UPDATE_LINE_ID_NAME";
	
	private EngineCureStatusTableModel engineCueStatusTableModel;
	
	TablePane engineCureStatusTabelPanel;
	
	private DateFormat simpleDateFormat = null;
	
	private Dimension winDimension = null;
	
	public EngineCureStatusPanel(DefaultWindow defaultWin) {
		super(defaultWin);
		winDimension = defaultWin.getPreferredSize();
		initializePanel();
	}
	
	protected void initializePanel() {
		
		JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BorderLayout());
		
		layoutPanel.add(getLabel(), BorderLayout.NORTH);
		layoutPanel.add(getTablePanel(), BorderLayout.CENTER);
		layoutPanel.add(getRefreshButton(), BorderLayout.SOUTH);
		add(layoutPanel);
	}
	
	
	private List<Engine> populateTable() {
		String checkLineIds = getPropertyTrim(PROPERTY_CHECK_LINE_IDS_KEY);
		String cureTimeStartPartName = getPropertyTrim(PROPERTY_CURE_TIME_START_PART_NAME_KEY);
		if(StringUtils.isEmpty(checkLineIds) || StringUtils.isEmpty(cureTimeStartPartName)){
			return new ArrayList<Engine>(0);
		}
		
		String[] checkLineIdsArray = checkLineIds.split(",");
		List<Engine> itemList = ServiceFactory.getDao(EngineDao.class).findEnginesAndBeginCureTimeByLineIdsAndPartName(checkLineIdsArray, cureTimeStartPartName);
		calculateDateForListPanel(itemList);
		return itemList;
	}
	
	
	private void calculateDateForListPanel(List<Engine> itemList){
		String waitTimer = getPropertyTrim(PROPERTY_WAIT_TIMER_KEY);
		if(StringUtils.isEmpty(waitTimer)){
			waitTimer = "0";
		}
		for(Engine engine : itemList){
			engine.setCureWaitTimer(waitTimer);
			if(engine.getCureTimeBegin() != null){
				engine.setCureTimeEnd(calculateCureTimeEnd(engine.getCureTimeBegin().getTime(), waitTimer));
			}else{
				engine.setCureTimeEnd("");
			}
		}
	}
	
	
	
	private String calculateCureTimeEnd(long cureTimeBeginTime, String waitTimer){
		DateFormat df = getSimpleDateFormat(DEFAULT_DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cureTimeBeginTime);
		cal.add(Calendar.MINUTE, Integer.valueOf(waitTimer));
		return df.format(cal.getTime());
	}
	
	private JPanel getRefreshButton(){
		JPanel panel = new JPanel(new BorderLayout());
		JButton btn = new JButton(_btnName);
		btn.setFont(Fonts.DIALOG_PLAIN_18);
		btn.setSize(150, 30);
		btn.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				JButton jbtn = (JButton)e.getSource();
				if(jbtn.getText().equals(_btnName)){
					refresh();
				}
			}
			
		});
		panel.add(btn, BorderLayout.EAST);
		return panel;
	}
	
	public void refresh() {
		// update track status
		updateTrackStatus();
		
		// get newly datas
		getEngineCueStatusTableModel().refresh(populateTable());
	}
	
	
	public void updateTrackStatus(){
		// update track status, default UPDATE_LINE_ID_NAME is 'Cure Complete'
		String updateLineIdName = getPropertyTrim(PROPERTY_UPDATE_LINE_ID_NAME_KEY);
		if(updateLineIdName == null || StringUtils.isEmpty(updateLineIdName)){
			updateLineIdName = "Cure Complete";
		}
		List<String> updateEIDs = filterUpdateStatusEIDFromList(updateLineIdName);
		if(updateEIDs != null && !updateEIDs.isEmpty()){
			String[] updateEIDsArray = new String[updateEIDs.size()];
			ServiceFactory.getDao(EngineDao.class).updateTrackStatusByProductIds(updateEIDs.toArray(updateEIDsArray), updateLineIdName);
		}
	}
	
	public List<String> filterUpdateStatusEIDFromList(String filterLineName){
		List<Engine> itemsList = getEngineCueStatusTableModel().getItems();
		if(itemsList.isEmpty()){
			return null;
		}
		List<String> updateStatusEIDs = new ArrayList<String>();
		
		DateFormat df = getSimpleDateFormat(DEFAULT_DATE_FORMAT);
		long curTime = System.currentTimeMillis();
		long eachEndTime = 0;
		
		try {
			for(Engine engine : itemsList){
				if(StringUtils.isEmpty(engine.getCureTimeEnd())){
					continue;
				}
				if(engine.getTrackingStatus().trim().equals(filterLineName.trim())){
					continue; 
				}
				eachEndTime = df.parse(engine.getCureTimeEnd()).getTime();
				if(curTime - eachEndTime > 0){
					updateStatusEIDs.add(engine.getProductId());
				}
				continue;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return updateStatusEIDs;
	}
	
	private JLabel getLabel(){
		JLabel titleLabel = new JLabel(panel_title);
		titleLabel.setVisible(true);
		Font font = new Font(null, Font.BOLD, 24);
		titleLabel.setFont(font);
		titleLabel.setPreferredSize(new Dimension(100,60));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		return titleLabel;
	}
	
	private TablePane getTablePanel(){
		engineCureStatusTabelPanel = new TablePane(getPanelName());
		engineCureStatusTabelPanel.setPreferredHeight(500);
		engineCureStatusTabelPanel.setPreferredWidth((int)winDimension.getWidth()-50);
		setEngineCueStatusTableModel(new EngineCureStatusTableModel(populateTable(), engineCureStatusTabelPanel.getTable()));
		getEngineCueStatusTableModel().pack();
		return engineCureStatusTabelPanel;
	}
	
	
	private String getPanelName() {
		return _panelName;
	}

	public EngineCureStatusTableModel getEngineCueStatusTableModel() {
		return engineCueStatusTableModel;
	}

	public void setEngineCueStatusTableModel(
			EngineCureStatusTableModel engineCueStatusTableModel) {
		this.engineCueStatusTableModel = engineCueStatusTableModel;
	}
	
	private DateFormat getSimpleDateFormat(String datePattern){
		if(simpleDateFormat != null){
			return simpleDateFormat;
		}
		if(StringUtils.isEmpty(datePattern)){
			datePattern = DEFAULT_DATE_FORMAT;
		}
		simpleDateFormat = new SimpleDateFormat(datePattern);
		return simpleDateFormat;
	}
	
	private String getPropertyTrim(String propertyName){
		if(getProperty(propertyName) != null){
			return getProperty(propertyName).trim();
		}
		return "";
	}
}
