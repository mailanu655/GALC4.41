package com.honda.galc.client.datacollection.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.ViewProperty;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.CounterDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Counter;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.service.ServiceFactory;

/*
 * This class is to Show Processed units at one process point.
 * Note: The height of table is fixed, because we have 3 fixed no of rows in the table. 1: shift, 2) plan, and 3) actual
 * Note: The width of column may change based on the no of distinct shifts from GAL226TBX table. The max no of shifts can be 4 in this class as of now.
 * Note: Need to set panel width based on no of rows. It can be done after setting up the process point
 * On click of "Refresh" button and on completion of process ,the table will be refreshed. See ClassicViewManager.java
 */
public class ProcessedCounterTablePanel extends JPanel {

    private DefaultTableModel model = null;
	private JScrollPane scrollPaneListTable = null;
	private int panelhight = 75;
	protected JTable table = null;
	protected CommonTlPropertyBean propertyBean;
	protected ClientContext context;
	String[][] counterDataArray = null;
	String[] columnNames = null;
	private int tableWidth = 0;

    public ProcessedCounterTablePanel() {
    	super();
    }
    
	public void initialize(ViewProperty property)
	{
		try {
			AnnotationProcessor.process(this);
			context = DataCollectionController.getInstance().getClientContext();
			panelhight = 91;
			setLayout(new BorderLayout());
			add(getScrollTablePanel());
			setBounds(property.getProcessedCounterPanelPositionX(), property.getProcessedCounterPanelPositionY(), tableWidth, getPanelhight());
		} catch (java.lang.Throwable ex) {
			handleException(ex);
		} 
	}

	private JScrollPane getScrollTablePanel() {
		if(scrollPaneListTable == null)
		{
			table = getTable();
		    scrollPaneListTable = new JScrollPane();		    
		    scrollPaneListTable.getViewport().add(table);
		    scrollPaneListTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    scrollPaneListTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		    
		    for (int i = 0; i < table.getColumnCount(); i++) {
	            TableColumn column = table.getColumnModel().getColumn(i);
	            tableWidth = tableWidth +  column.getWidth();
	        }
		    scrollPaneListTable.setPreferredSize(new Dimension(tableWidth+1, 90+1));
		}
		return scrollPaneListTable;
	}
	
    public JTable getTable() {
		if (table == null) {
			try {
				createTable();
			} catch (java.lang.Throwable ex) {
				handleException(ex);
			}
		}
		return table;
    }

	protected JTable createTable() {
		counterDataArray = createTableData();
		columnNames = getColumnNames(counterDataArray);
    	model = new DefaultTableModel(counterDataArray, columnNames);
        table = new JTable(model);
        table.setTableHeader(null);
        table.setShowGrid(false);

        table.setRowHeight(30);
        table.getColumn("planActual").setMaxWidth(52);
        for (int i=1; i<counterDataArray[0].length; i++) {
        	table.getColumn(i+"").setMinWidth(80);
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumn("planActual").setCellRenderer(
                new DefaultTableCellRenderer() {
                   @Override
                   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                      Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, 0, column);
                      c.setFont(new Font("dialog", 0, 18));
                      ((DefaultTableCellRenderer)c).setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
                      return c;
                   }
         });

        for (int i=1; i<counterDataArray[0].length; i++) {
	        table.getColumn(i+"").setCellRenderer(
	                new DefaultTableCellRenderer() {
	                   @Override
	                   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	                      Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, 0, column);
	                      if(row == 0) {
	                    	  c.setFont(new Font("dialog", 0, 18));
	                    	  c.setBackground(Color.white);
	                      } else {
	                    	  c.setFont(new Font("Dialog", Font.BOLD, 25));
	                    	  c.setBackground(Color.lightGray);
	                      }
	                      ((DefaultTableCellRenderer)c).setOpaque(true);
	                      ((DefaultTableCellRenderer)c).setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
	                      if(column != 2) {
		                      MatteBorder border = new MatteBorder(0, 0, 0, 7, Color.WHITE);
		                      ((DefaultTableCellRenderer)c).setBorder(border);
	                      }
	                      if(table.getColumnCount() == 4 && column == 2) {
	                    	  MatteBorder border = new MatteBorder(0, 0, 0, 7, Color.WHITE);
		                      ((DefaultTableCellRenderer)c).setBorder(border);
	                      }
	                      return c;
                   	}
	         });
        }
        return table;

	}
	
	public String[][] createTableData() {
    	String processPointId = context.getProcessPointId();
    	ProcessPointDao processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		ProcessPoint processPoint = processPointDao.findById(processPointId);
		
		GpcsDivisionDao gpcsDivisionDao = ServiceFactory.getDao(GpcsDivisionDao.class);
		GpcsDivision gpcsDivision = gpcsDivisionDao.findByKey(processPoint.getDivisionId());
		
		String processLocation = gpcsDivision.getGpcsProcessLocation().trim();
		if(processPoint.getDivisionName().contains(" ")) {
			processLocation = processPoint.getDivisionName().substring(0, processPoint.getDivisionName().indexOf(" "));
		}
		
    	Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
    	
    	// 1) Get Current Schedule based on currentTime
    	DailyDepartmentSchedule currentSchedule = getCurrentSchedule(processPointId, processLocation, currentTimeStamp,gpcsDivision);
    	
    	// 2) Get all shifts based on current Schedule
    	List<String> shiftsList = getShiftList(currentSchedule, processPointId, processLocation);
    	
    	// 3) Get Planned units based on shifts, current schedule and current time
    	Map<String, Integer> plannedUnitsAtProcessMap = getPlannedUnitsAtProcessPoint(processPointId, processLocation, shiftsList, currentSchedule, currentTimeStamp,gpcsDivision);

    	// 4) Get Actual units 
    	Map<String, Integer> actualUnitsAtProcessMap = getActualUnitsAtProcessPoint(processPointId, processLocation, currentSchedule, currentTimeStamp, shiftsList);
    	
    	// Create data for counter table to be shown on the screen. i.e. number of rows and columns and data in the table cells
    	String[][] counterDataArray = new String[3][shiftsList.size()+1];
    	counterDataArray[0][0] = "";
    	counterDataArray[1][0] = "Plan";
    	counterDataArray[2][0] = "Actual";
    	   	
    	Integer col = 1;

    	for (String key : shiftsList) {
			int row = 0;
			ShiftsOrdinalEnum shiftsEnum = ShiftsOrdinalEnum.getByValue(key);
			counterDataArray[row][col] = col+shiftsEnum.name()+" Shift";
			counterDataArray[row+1][col] = plannedUnitsAtProcessMap.get(key).toString();
			counterDataArray[row+2][col] = actualUnitsAtProcessMap.get(key).toString();
			col++;
    	}

		return counterDataArray;
	}
	
	public String[] getColumnNames(String[][] counterDataArray) {
		String[] columnNames = new String[counterDataArray[0].length];
    	columnNames[0] = "planActual";
    	for (int i=1; i<columnNames.length; i++) {
			columnNames[i] = i+"";
    	}
    	return columnNames;
	}
	
	private Map<String, Integer> getActualUnitsAtProcessPoint(String processPointId, String processLocation, DailyDepartmentSchedule currentSchedule, Timestamp currentTimeStamp, List<String> shiftsList) {
		Map<String, Integer> actualUnitsAtProcessPoint = new HashMap<String, Integer>();
		CounterDao counterDao = ServiceFactory.getDao(CounterDao.class);

		for(String shift : shiftsList) {
			Integer count = counterDao.getCountByDateShiftAndProcess(currentSchedule.getId().getProductionDate(), shift, processPointId);
			if(count == null) {
				count = 0;
			}
			actualUnitsAtProcessPoint.put(shift, count);
		}
		return actualUnitsAtProcessPoint;
	}
	
	
	private DailyDepartmentSchedule getCurrentSchedule(String processPointId, String processLocation, Timestamp currentTimeStamp,GpcsDivision gpcsDivision) {
		DailyDepartmentScheduleDao dailyDepartmentScheduleDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
		DailyDepartmentSchedule currentSchedule = null;
		try {
			currentSchedule= dailyDepartmentScheduleDao.findByActualTime(gpcsDivision.getGpcsLineNo(), processLocation, gpcsDivision.getGpcsPlantCode(), currentTimeStamp);
		} catch(Exception e) {
			handleException(e);
		}
		return currentSchedule;
	}
	
	private List<String> getShiftList(DailyDepartmentSchedule currentSchedule, String processPointId, String processLocation) {
		DailyDepartmentScheduleDao dailyDepartmentScheduleDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
		List<String> shiftList = null;
		try {
			shiftList = dailyDepartmentScheduleDao.findAllShiftsByDateAndProcessLocation(currentSchedule.getId().getProductionDate(), processLocation);
		} catch(Exception e) {
			handleException(e);
		}
		if(shiftList != null && shiftList.size() > 0) {
			return shiftList;
		} else {
			shiftList.add("00");
			shiftList.add("00");
			Logger.getLogger().error("No shifts found");
		}
		return shiftList;
	}
	
	private Map<String, Integer> getPlannedUnitsAtProcessPoint(String processPointId, String processLocation, List<String> shiftList, DailyDepartmentSchedule currentSchedule, Timestamp currentTimeStamp,GpcsDivision gpcsDivision) {
		DailyDepartmentScheduleDao dailyDepartmentScheduleDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
		Map<String, Integer> plannedUnitsAtProcessPoint = new HashMap<String, Integer>();

		if(shiftList != null && shiftList.size() > 0) {
			long lcurrentTime = System.currentTimeMillis();
			for(String shift : shiftList) {
				int counter = 0;
				List<DailyDepartmentSchedule> schedules = dailyDepartmentScheduleDao.findUptoCurrentTime(gpcsDivision.getGpcsLineNo(), processLocation,gpcsDivision.getGpcsPlantCode(), currentSchedule.getId().getProductionDate(), currentTimeStamp);
				if(schedules != null && schedules.size() > 0) {
					for(DailyDepartmentSchedule schedule : schedules) {
						if (shift.trim().equals(schedule.getId().getShift().trim())) {
							if(currentSchedule.getId().getPeriod() == schedule.getId().getPeriod()) {
								Timestamp startTime = schedule.getStartTimestamp();
								Timestamp endTime = schedule.getEndTimestamp();
								int capacity = schedule.getCapacity();
								if (startTime.before(endTime)) {
									long periodDiff = endTime.getTime() - startTime.getTime();
									if (periodDiff > 0) {
										long past = currentTimeStamp.getTime() - startTime.getTime();
										counter += (1.0 * past / periodDiff) * capacity;
									}
								}
							}  else {
								counter += schedule.getCapacity();
							}
						}
					}
					plannedUnitsAtProcessPoint.put(shift, counter);
				}
			}
		} else {
			Logger.getLogger().error("No shifts found");
		}
		return plannedUnitsAtProcessPoint;
	}
	
	@EventSubscriber(eventClass = Counter.class)
	public void add(Counter event){
		updateTable(event);
	}

	protected void updateTable(Counter event) {
		counterDataArray = createTableData();
		refresh();
	}

	public void refresh() {
	    DefaultTableModel model1 = new DefaultTableModel(counterDataArray, columnNames);
	    table.setAutoCreateColumnsFromModel(false);
	    table.setModel(model1);
	    table.repaint();

	}

	public enum ShiftsOrdinalEnum {
		st("01"), nd("02"), rd("03"), th("04");
		private static final Map<String, ShiftsOrdinalEnum> MY_MAP = new HashMap<String, ShiftsOrdinalEnum>();
		static {
			for (ShiftsOrdinalEnum myEnum : values()) {
				MY_MAP.put(myEnum.getValue(), myEnum);
			}
		}
		private String value;

		private ShiftsOrdinalEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static ShiftsOrdinalEnum getByValue(String value) {
			return MY_MAP.get(value);
		}

		public String toString() {
			return name() + "=" + value;
		}

	}

	private void handleException(java.lang.Throwable e) {
		Logger.getLogger().error(e, "-- UNCAUGHT EXCEPTION --");
	}
	
	public int getPanelhight() {
		return panelhight;
	}

	public void setPanelhight(int panelhight) {
		this.panelhight = panelhight;
	}

}
