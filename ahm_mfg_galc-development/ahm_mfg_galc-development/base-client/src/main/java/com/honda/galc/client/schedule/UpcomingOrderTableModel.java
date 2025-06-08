/**
 * 
 */
package com.honda.galc.client.schedule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.schedule.mbpn.PaintMbpnProductProcessor;
import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.dao.product.OrderDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Order;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.tracking.MbpnProductHelper;

/**
 * @author Subu Kathiresan
 * @date Jan 29, 2013
 */
public class UpcomingOrderTableModel extends BaseTableModel<Order> implements ListSelectionListener, ActionListener {

	private static final long serialVersionUID = 346320332599441800L;
	private static final String POP_UP_MENU_SET_CURRENT = "SET_CURRENT";
	private static final String POP_UP_MENU_SET_COMPLETE = "SET_COMPLETE";
	private static final String POP_UP_MENU_GENERATE_SN = "GENERATE_SN";
	private static final String MENU_TEXT_SET_CURRENT = "Set Current";
	private static final String MENU_TEXT_SET_COMPLETE = "Set Complete";
	private static final String MENU_TEXT_GENERATE_SN = "Generate SN";

	private final static String[] columns = { "Order Number      ", "Plan Code    ", "Product Spec Code       ", "Priority Date     ", "Seq", "Plan",
			"Actual" };
	private OrderDao _orderDao;
	private String _lineId;
	private Hashtable<String, Integer> _actualCountMap = new Hashtable<String, Integer>();
	private String _processPointId;

	public UpcomingOrderTableModel(List<Order> items, JTable table, String lineId, String processPointId) {
		super(items, columns, table);
		_lineId = lineId;
		_processPointId = processPointId;
		setupTable();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= getRowCount())
			return null;
		Order order = getItems().get(rowIndex);

		if (rowIndex == 0)
			_actualCountMap = getOrderDao().getOrderFilledQty(getLineId());

		switch (columnIndex) {
		case 0:
			return order.getId().getOrderNo();
		case 1:
			return order.getId().getPlanCode();
		case 2:
			return order.getProductSpecCode();
		case 3:
			return order.getPriorityDate();
		case 4:
			return order.getPrioritySeq();
		case 5:
			return order.getProdOrderQty();
		case 6:
			return getActualCountMap().get(StringUtils.trimToEmpty(order.getId().getOrderNo()));
		default:
			return super.getValueAt(rowIndex, columnIndex);
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnNames[columnIndex].equalsIgnoreCase("PLAN")) {
			return true;
		} else
			return false;

	}

	public void setValueAt(Object value, int row, int column) {
		try {
			super.setValueAt(value, row, column);
			if (!isCellEditable(row, column)) {
				return;
			}
			Order order = items.get(row);
			String valueString = value.toString().trim();
			switch (column) {
			case 5:
				order.setProdOrderQty(Integer.parseInt(valueString));
				getOrderDao().save(order);
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			return;
		}
		this.fireTableCellUpdated(row, column);
	}

	public void valueChanged(ListSelectionEvent e) {
	}

	public String getLineId() {
		return _lineId;
	}

	private Hashtable<String, Integer> getActualCountMap() {
		return _actualCountMap;
	}

	private void setupTable() {
		getTable().getSelectionModel().addListSelectionListener(this);
		getTable().addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent event) {
				if (event.isPopupTrigger()) {
					showPopupMenu(event);
				}
			}

			public void mouseReleased(MouseEvent event) {
				if (event.isPopupTrigger()) {
					showPopupMenu(event);
				}
			}
		});
	}

	public void showPopupMenu(MouseEvent event) {
		createPopupMenu().show(event.getComponent(), event.getX(), event.getY());
	}

	public JPopupMenu createPopupMenu() {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem item = new JMenuItem();
		item.setName(POP_UP_MENU_SET_CURRENT);
		item.setText(MENU_TEXT_SET_CURRENT);
		item.setEnabled(true);
		item.addActionListener(this);
		menu.add(item);

		item = new JMenuItem();
		item.setName(POP_UP_MENU_SET_COMPLETE);
		item.setText(MENU_TEXT_SET_COMPLETE);
		item.setEnabled(true);
		item.addActionListener(this);
		menu.add(item);

		if (shallPopupGenerateSNMenu()) {
			item = new JMenuItem();
			item.setName(POP_UP_MENU_GENERATE_SN);
			item.setText(MENU_TEXT_GENERATE_SN);
			item.setEnabled(true);
			item.addActionListener(this);
			menu.add(item);
		}
		return menu;
	}

	private boolean shallPopupGenerateSNMenu() {
		// Popup the "Generate SN" menu item only if the selected order is the current order and the product type is "SUPPLIER".
		Order selectedOrder = getSelectedItems().get(0);
		Order currentOrder = MbpnProductHelper.getCurrentOrder(_processPointId);
		if (null == selectedOrder || null == currentOrder || !selectedOrder.getId().getOrderNo().equals(currentOrder.getId().getOrderNo())) {
			return false;
		}
		return false;
		// FIXME since the Order table will no longer be used, so this code will no longer be maintained.
//		return PaintMbpnProductProcessor.isSupplierProduct(selectedOrder);
	}

	public JTable getTable() {
		return this.table;
	}

	public OrderDao getOrderDao() {
		if (_orderDao == null)
			_orderDao = ServiceFactory.getDao(OrderDao.class);

		return _orderDao;
	}

	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals(MENU_TEXT_SET_CURRENT)) {
			setCurrent();
		} else if (command.equals(MENU_TEXT_SET_COMPLETE)) {
			setComplete();
		} else if (command.equals(MENU_TEXT_GENERATE_SN)) {
			generateSN();
		}
	}

	protected void setCurrent() {
		List<Order> selectedItems = getSelectedItems();
		SchedulingEvent schedulingEvent = new SchedulingEvent(selectedItems.get(0), SchedulingEventType.CURRENT_ORDER_CHANGED);
		EventBus.publish(schedulingEvent);
	}

	protected void setComplete() {
		List<Order> selectedItems = getSelectedItems();
		SchedulingEvent schedulingEvent = new SchedulingEvent(selectedItems.get(0), SchedulingEventType.ORDER_COMPLETED);
		EventBus.publish(schedulingEvent);
	}

	protected void generateSN() {
		List<Order> selectedItems = getSelectedItems();
		SchedulingEvent schedulingEvent = new SchedulingEvent(selectedItems.get(0), SchedulingEventType.GENERATE_SN);
		EventBus.publish(schedulingEvent);
	}
}
