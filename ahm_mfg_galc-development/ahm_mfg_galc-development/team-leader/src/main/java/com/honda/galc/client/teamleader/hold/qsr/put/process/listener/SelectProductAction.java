package com.honda.galc.client.teamleader.hold.qsr.put.process.listener;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.put.process.ProcessPanel;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ObjectComparator;
import com.honda.galc.util.ProductCheckUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QsrSelectProductAction</code> is ... .
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
public class SelectProductAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private BaseListener<ProcessPanel> action;

	public SelectProductAction(ProcessPanel parentPanel) {
		super();
		putValue(Action.NAME, "Select");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		action = new BaseListener<ProcessPanel>(parentPanel) {
			@Override
			public void executeActionPerformed(ActionEvent e) {

				Division division = getView().getDivision();
				ProcessPoint processPoint = (ProcessPoint) getView().getInputPanel().getProcessPointComboBox().getSelectedItem();
				
				String modelCode = (String) getView().getInputPanel().getModelComboBox().getSelectedItem();
				
				Date start = getView().getInputPanel().getStartTime();
				Date end = getView().getInputPanel().getEndTime();
				if (division == null || processPoint == null || start == null || end == null) {
					getView().getInputPanel().getCommandButton().setEnabled(false);
					return;
				}

				Timestamp startTime = new Timestamp(start.getTime());
				Timestamp endTime = new Timestamp(end.getTime());

				long duration = endTime.getTime() - startTime.getTime();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date(duration));
				if (duration < 0) {
					JOptionPane.showMessageDialog(getView(), "Make sure that Start Time is before End Time", "Invalid Input", JOptionPane.WARNING_MESSAGE);
					return;
				}

				long maxQsrTimeRangeInMinutes = Config.getProperty().getMaxQsrTimeRange() * 60 * 1000L;
				if (calendar.getTimeInMillis() > maxQsrTimeRangeInMinutes) {
					int days = Config.getProperty().getMaxQsrTimeRange() / (24 * 60);
					int hours = (Config.getProperty().getMaxQsrTimeRange() - days * 24 * 60) / 60;
					int minutest = Config.getProperty().getMaxQsrTimeRange() - days * 24 * 60 - hours * 60;
					StringBuilder sb = new StringBuilder();
					sb.append("Time range exceeds maximum value of ");
					sb.append(days).append(" days ").append(hours).append(" hours and ").append(minutest).append(" minutes ");
					JOptionPane.showMessageDialog(getView(), sb, "Invalid Input", JOptionPane.WARNING_MESSAGE);
					return;
				}

				selectProducts(division, processPoint, startTime, endTime, modelCode);

				if (getView().getProductPanel().getTable().getRowCount() > 0) {
					getView().getInputPanel().getCommandButton().setAction(getView().getClearAction());
				} else {
					return;
				}
			}
		};
	}

	public void actionPerformed(ActionEvent ae) {
		getAction().actionPerformed(ae);
	}

	protected BaseListener<ProcessPanel> getAction() {
		return action;
	}

	protected void selectProducts(Division division, ProcessPoint processPoint, Timestamp startTime, Timestamp endTime, String modelCode) {

		List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
		ProductType productType = getAction().getView().getProductType();
		List<? extends BaseProduct> products = new ArrayList<BaseProduct>();
		List<? extends ProductHistory> historyList = new ArrayList<ProductHistory>();
		
		if(modelCode != null && modelCode.length()>0){
			modelCode = "%"+modelCode+"%";
			products = getAction().getProductDao(productType).findAllByProcessPointAndModel(processPoint.getProcessPointId(), startTime, endTime, modelCode, productType);
			historyList = ProductTypeUtil.getProductHistoryDao(productType).findAllByProcessPointAndModel(processPoint.getProcessPointId(), startTime, endTime, modelCode,productType);
		}
		else{
			products = getAction().getProductDao(productType).findAllByProcessPoint(processPoint.getProcessPointId(), startTime, endTime);
			historyList = ProductTypeUtil.getProductHistoryDao(productType).findAllByProcessPoint(processPoint.getProcessPointId(), startTime, endTime);
		}
		
		List<Line> lines = ServiceFactory.getDao(LineDao.class).findAll();
		Map<String , Line>  linesIx = new HashMap<String, Line>();

		for(Line line : lines){
			linesIx.put(line.getLineId(), line);
		}

		String shipLineId = Config.getProperty().getShipLineId();
		List<String> lineIdList = Arrays.asList(shipLineId.split(Delimiter.COMMA));
		Map<String, BaseProduct> productIx = new HashMap<String, BaseProduct>();
		for (BaseProduct product : products) {
			productIx.put(product.getProductId(), product);
		}

		List<String> processedLatestHistory = new ArrayList<String>();
		Collections.sort(historyList, new ObjectComparator<ProductHistory>("id.actualTimestamp"));
		Collections.reverse(historyList);
		for (ProductHistory history : historyList) {
			if (processedLatestHistory.contains(history.getProductId()) || !productIx.containsKey(history.getProductId())) {
				continue;
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("history", history);
			map.put("product", productIx.get(history.getProductId()));
			map.put("line",(linesIx.get(productIx.get(history.getProductId()).getTrackingStatus())));
			map.put("ship" ,lineIdList.contains(productIx.get(history.getProductId()).getTrackingStatus()) ? true : false);
			map.put("lastProcessPointName", productIx.get(history.getProductId()).getLastPassingProcessPointId() == null ? "" :getProcessPointName(productIx.get(history.getProductId()).getLastPassingProcessPointId()));
			String deviceId = history.getDeviceId();
			map.put("deviceId", deviceId == null ? "" : deviceId);
			tableData.add(map);
			processedLatestHistory.add(history.getProductId());
		}
		getAction().getView().getProductPanel().reloadData(tableData);
	}
	
	private Object getProcessPointName(String processPoint) {
		return  ServiceFactory.getDao(ProcessPointDao.class).findById(processPoint).getProcessPointName();
	}
}
