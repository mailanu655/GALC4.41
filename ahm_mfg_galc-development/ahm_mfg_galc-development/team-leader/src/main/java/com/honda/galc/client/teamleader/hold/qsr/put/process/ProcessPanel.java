package com.honda.galc.client.teamleader.hold.qsr.put.process;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.client.teamleader.hold.qsr.put.process.listener.ClearAction;
import com.honda.galc.client.teamleader.hold.qsr.put.process.listener.ProcessPointSelectionListener;
import com.honda.galc.client.teamleader.hold.qsr.put.process.listener.ProductNumberListener;
import com.honda.galc.client.teamleader.hold.qsr.put.process.listener.SelectProductAction;
import com.honda.galc.client.teamleader.hold.qsr.put.process.listener.TimeListener;
import com.honda.galc.client.ui.component.Calendar;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.report.TableReport;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessPanel</code> is ... .
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
 */
public class ProcessPanel extends HoldProductPanel {

	private static final long serialVersionUID = 1L;

	private Action selectProductAction;
	private Action clearAction;

	private ProductTypeData productTypeData;
	
	private String dateSelected = "";

	public ProcessPanel(QsrMaintenanceFrame mainWindow) {
		super("Hold By Process", KeyEvent.VK_H, mainWindow);
	}

	@Override
	protected void initView() {
		super.initView();
		getSplitPanel().setDividerLocation(115);
		setSelectProductAction(new SelectProductAction(this));
		setClearAction(new ClearAction(this));
	}

	@Override
	protected void defineProductTypeColumnsMapping() {
		super.defineProductTypeColumnsMapping();
		for (PropertiesMapping mapping : getProductTypeColumnsMapping().values()) {
			mapping.put("Time", "history.actualTimestamp", getDateFormat());
			if(Config.getProperty().isShowTrackingStatus()){
				mapping.put("Current Status","line.lineName");
				mapping.put("Product Spec Code", "product.productSpecCode");
			}
		}
	}

	// === mappings === //
	protected void mapActions() {
		super.mapActions();
		getInputPanel().getProductTypeComboBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Division division = getDivision();
				ProductType productType = getProductType();
				if (division == null || productType == null) {
					getInputPanel().getProcessPointComboBox().setSelectedIndex(-1);
					getInputPanel().getProcessPointComboBox().removeAllItems();
					return;
				}
				getInputPanel().getModelComboBox().removeAllItems();
				List<String> modelList = Config.getModelCodes(productType);
				getInputPanel().getModelComboBox().setModel(new DefaultComboBoxModel(new Vector<String>(modelList)));
				getInputPanel().getModelComboBox().setSelectedIndex(-1);
				List<ProcessPoint> processPoints = Config.getInstance().getProcessPoints(division, productType);
				getInputPanel().getProcessPointComboBox().setModel(new DefaultComboBoxModel(new Vector<ProcessPoint>(processPoints)));
				getInputPanel().getProcessPointComboBox().setSelectedIndex(-1);
				getInputPanel().getStartVinButton().setText(getProductTypeData().getProductIdLabel());
				getInputPanel().getEndVinButton().setText(getProductTypeData().getProductIdLabel());
			}
		});

		getInputPanel().getProcessPointComboBox().addActionListener(new ProcessPointSelectionListener(this));

		getInputPanel().getStartProductInput().addActionListener(new ProductNumberListener(this, getInputPanel().getStartProductInput(), getInputPanel().getStartTimeInput()));
		getInputPanel().getEndProductInput().addActionListener(new ProductNumberListener(this, getInputPanel().getEndProductInput(), getInputPanel().getEndTimeInput()));
		DocumentListener timeListener = new TimeListener(this);
		getInputPanel().getStartTimeInput().getDocument().addDocumentListener(timeListener);
		getInputPanel().getEndTimeInput().getDocument().addDocumentListener(timeListener);

		getInputPanel().getCommandButton().setAction(getSelectProductAction());
		getInputPanel().getCommandButton().setEnabled(false);
		getInputPanel().getStartVinButton().addActionListener(this);
		getInputPanel().getEndVinButton().addActionListener(this);
		getInputPanel().getStartTimeButton().addActionListener(this);
		getInputPanel().getEndTimeButton().addActionListener(this);
		
	}

	public String selectProductFromDialog(){
		String productId = "";
		String productType = getInputPanel().getProductTypeComboBox().getSelectedItem().toString();
		ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(getMainWindow(), productType, ProductNumberDef.getProductNumberDef(ProductType.getType(productType)).get(0).getName());
		manualProductEntry.setModal(true);
		manualProductEntry.setVisible(true);
		productId = manualProductEntry.getResultProductId();
		return productId;
		}

	public String selectDateTime(){
		Calendar calendar = new Calendar();
		calendar.setModal(true);
		calendar.setVisible(true);
		dateSelected = calendar.getFinalTextDate();
		return dateSelected; 
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getInputPanel().getStartVinButton())){
			String selectedProductId = selectProductFromDialog();
			if (!StringUtils.isBlank(selectedProductId)){
				getInputPanel().getStartProductInput().setText(selectedProductId);
				getInputPanel().getStartProductInput().postActionEvent();
			}
			getInputPanel().getStartProductInput().requestFocusInWindow();
		}
		else if(e.getSource().equals(getInputPanel().getEndVinButton())){
			String selectedProductId = selectProductFromDialog();
			if (!StringUtils.isBlank(selectedProductId)) {
				getInputPanel().getEndProductInput().setText(selectedProductId);
				getInputPanel().getEndProductInput().postActionEvent();
			}
			getInputPanel().getEndProductInput().requestFocusInWindow();
		}
		else if(e.getSource().equals(getInputPanel().getStartTimeButton())){
			selectDateTime();
			getInputPanel().getStartTimeInput().setText(!StringUtils.isBlank(dateSelected) ? dateSelected: getInputPanel().getStartTimeInput().getText());
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date;
			try {
				date = format.parse(dateSelected);
				getInputPanel().getStartTimeInput().setValue(date);
			} catch (ParseException e1) {
				Logger.getLogger(getApplicationId()).error(Arrays.toString(e1.getStackTrace()));
			}
			String startDate = getInputPanel().getStartTimeInput().getText();
			String endDate = getInputPanel().getEndTimeInput().getText();
			if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate))
				getInputPanel().getCommandButton().setEnabled(true);
			
		}
		else if(e.getSource().equals(getInputPanel().getEndTimeButton())){
			selectDateTime();
			getInputPanel().getEndTimeInput().setText(!StringUtils.isBlank(dateSelected) ? dateSelected : getInputPanel().getEndTimeInput().getText());
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date;
			try {
				date = format.parse(dateSelected);
				getInputPanel().getEndTimeInput().setValue(date);
			} catch (ParseException e1) {
				Logger.getLogger(getApplicationId()).error(Arrays.toString(e1.getStackTrace()));
			}
			
			String startDate = getInputPanel().getStartTimeInput().getText();
			String endDate = getInputPanel().getEndTimeInput().getText();
			if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate))
				getInputPanel().getCommandButton().setEnabled(true);
		}
	}
	
	// === factory methods === //
	@Override
	public ProcessInputPanel createInputPanel() {
		ProcessInputPanel panel = new ProcessInputPanel(this);
		panel.setInputEnabled(false);
		return panel;
	}

	@Override
	public TableReport getReport() {
		TableReport report = super.getReport();
		Division division = getDivision();
		ProcessPoint processPoint = (ProcessPoint) getInputPanel().getProcessPointComboBox().getSelectedItem();
		String fileName = String.format("QSR-HOLD-%s-%s.xlsx", division.getDivisionId(), processPoint.getProcessPointId());
		String sheetName = String.format("QSR-HOLD-%s-%s", getDivision().getDivisionId(), processPoint.getProcessPointId());
		String headerLine = String.format("QSR-HOLD-%s-%s-%s (%s)", division.getDivisionId(), getProductType(), processPoint.getProcessPointName(), processPoint.getProcessPointId());
		String startTime = getInputPanel().getStartTimeInput().getText();
		String endTime = getInputPanel().getEndTimeInput().getText();
		headerLine = String.format("%s  Time Range  %s to %s", headerLine, startTime, endTime);
		report.setFileName(fileName);
		report.setTitle(headerLine);
		report.setReportName(sheetName);
		return report;
	}

	@Override
	public void defineReports() {
		super.defineReports();
		for (TableReport report : getReports().values()) {
			report.addColumn("history.actualTimestamp", "Time", 7000);
		}
	}

	@Override
	public ProcessInputPanel getInputPanel() {
		return (ProcessInputPanel) super.getInputPanel();
	}

	public Action getClearAction() {
		return clearAction;
	}

	public void setClearAction(Action clearAction) {
		this.clearAction = clearAction;
	}

	public Action getSelectProductAction() {
		return selectProductAction;
	}

	public void setSelectProductAction(Action selectProductAction) {
		this.selectProductAction = selectProductAction;
	}
	
	public ProductTypeData getProductTypeData(){
			for(ProductTypeData type : window.getApplicationContext().getProductTypeDataList()){
				if(type.getProductTypeName().equals(getProductType().name())){
					productTypeData = type;
					break;
				}
			}
		
		return productTypeData;
	}
}
