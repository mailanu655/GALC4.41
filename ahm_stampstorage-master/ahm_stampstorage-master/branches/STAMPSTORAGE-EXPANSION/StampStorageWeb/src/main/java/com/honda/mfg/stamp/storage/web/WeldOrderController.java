package com.honda.mfg.stamp.storage.web;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Model;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.storage.service.CarrierManagementService;
import com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy;

@RequestMapping("/weldorders")

@Controller
public class WeldOrderController {
	private static final Logger LOG = LoggerFactory.getLogger(WeldOrderController.class);

	@Autowired
	CarrierManagementService carrierManagementService;

	@Autowired
	CarrierManagementServiceProxy carrierManagementServiceProxy;

	@ModelAttribute("models")
	public Collection<Model> populateModels() {
		// return Model.findAllModels();

		Collection<Model> models = Model.findAllModels();
		List<Model> activeModels = new ArrayList<Model>();

		for (Model model : models) {
			if (model.getActive()) {
				activeModels.add(model);
			}
		}

		return activeModels;
	}

	public List<OrderFulfillment> getOrderFulfillmentsByOrder(WeldOrder order) {

		List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(order);

		List<OrderFulfillment> fulfillmentList = new ArrayList<OrderFulfillment>();
		for (OrderFulfillment fulfillment : fulfillments) {
			CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(fulfillment.getId().getCarrierNumber());
			Stop currentLocation = carrierMes == null ? null : Stop.findStop(carrierMes.getCurrentLocation());
			fulfillment.setCurrentLocation(currentLocation);

			fulfillmentList.add(fulfillment);
		}
		return fulfillmentList;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid WeldOrder weldOrder, BindingResult bindingResult, org.springframework.ui.Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("weldOrder", weldOrder);
			uiModel.addAttribute("weldorders", getWeldorders());

			ArrayList<OrderStatus> orderstatuses = new ArrayList<OrderStatus>();
			orderstatuses.add(OrderStatus.Initialized);
			uiModel.addAttribute("orderstatuses", orderstatuses);

			ArrayList<OrderStatus> deliverystatuses = new ArrayList<OrderStatus>();
			deliverystatuses.add(OrderStatus.Initialized);
			uiModel.addAttribute("deliverystatuses", deliverystatuses);

			uiModel.addAttribute("filterorderstatuses", populateOrderStatuses());
			uiModel.addAttribute("ordermgrs", getOrderMgrs());
			uiModel.addAttribute("models", populateModels());
			return "weldorders/list";
		}
		uiModel.asMap().clear();

		try {
			weldOrder.setLeftDeliveredQuantity(0);
			weldOrder.setLeftConsumedQuantity(0);
			weldOrder.setRightDeliveredQuantity(0);
			weldOrder.setRightConsumedQuantity(0);
			weldOrder.setLeftQueuedQty(0);
			weldOrder.setRightQueuedQty(0);
			String user = httpServletRequest.getUserPrincipal().getName();
			weldOrder.setCreatedBy(user);
			weldOrder.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			weldOrder.persist();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/weldorders";
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(org.springframework.ui.Model uiModel) {
		uiModel.addAttribute("weldOrder", new WeldOrder());

		ArrayList<OrderStatus> orderstatuses = new ArrayList<OrderStatus>();
		orderstatuses.add(OrderStatus.Initialized);
		uiModel.addAttribute("orderstatuses", orderstatuses);

		ArrayList<OrderStatus> deliverystatuses = new ArrayList<OrderStatus>();
		deliverystatuses.add(OrderStatus.Initialized);
		uiModel.addAttribute("deliverystatuses", deliverystatuses);

		uiModel.addAttribute("filterorderstatuses", populateOrderStatuses());
		uiModel.addAttribute("ordermgrs", getOrderMgrs());
		return "weldorders/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, org.springframework.ui.Model uiModel) {
		WeldOrder order = WeldOrder.findWeldOrder(id);
		uiModel.addAttribute("weldorder", order);
		uiModel.addAttribute("itemId", id);
		List<OrderFulfillment> fulfillments = getOrderFulfillmentsByOrder(order);
		uiModel.addAttribute("orderfulfillments", fulfillments);
		boolean mesHealthy = !carrierManagementService.isDisconnected();
		uiModel.addAttribute("meshealthy", mesHealthy);
		uiModel.addAttribute("alarmevent", carrierManagementService.getAlarmEventToDisplay());
		return "weldorders/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, org.springframework.ui.Model uiModel) {
		List<WeldOrder> weldOrdersInProcess = getWeldorders();
		List<WeldOrder> weldOrdersPending = getWeldordersPending();
		List<WeldOrder> weldOrdersDelivering = getWeldordersDelivering();

		uiModel.addAttribute("weldOrder", new WeldOrder());
		uiModel.addAttribute("weldorders", weldOrdersInProcess);
		uiModel.addAttribute("weldordersPending", weldOrdersPending);
		uiModel.addAttribute("weldordersDelivering", weldOrdersDelivering);

		ArrayList<OrderStatus> orderstatuses = new ArrayList<OrderStatus>();
		orderstatuses.add(OrderStatus.Initialized);
		uiModel.addAttribute("orderstatuses", orderstatuses);

		ArrayList<OrderStatus> deliverystatuses = new ArrayList<OrderStatus>();
		deliverystatuses.add(OrderStatus.Initialized);
		uiModel.addAttribute("deliverystatuses", deliverystatuses);

		uiModel.addAttribute("filterorderstatuses", populateOrderStatuses());
		uiModel.addAttribute("ordermgrs", getOrderMgrs());

		return "weldorders/list";
	}

	private List<WeldOrder> getWeldorders() {
		List<WeldOrder> weldOrders = new ArrayList<WeldOrder>();
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		// change to only run query once in case user has more than one authority - MG
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ROLE_APP-OHCVD-MAP-WEOP1")
					|| authority.getAuthority().equals("ROLE_APP-OHCVP-MAP-WEOP1")
					|| authority.getAuthority().equals("ROLE_APP-OHCVQ-MAP-WEOP1")) {
				// weldOrders =
				// WeldOrder.findAllWeldOrdersByOrderMgr(OrderMgr.findOrderMgr(1L));
				weldOrders = WeldOrder.findWeldOrdersInProcessByOrderMgr(OrderMgr.findOrderMgr(1L));
				return weldOrders;
			} else if (authority.getAuthority().equals("ROLE_APP-OHCVD-MAP-WEOP2")
					|| authority.getAuthority().equals("ROLE_APP-OHCVP-MAP-WEOP2")
					|| authority.getAuthority().equals("ROLE_APP-OHCVQ-MAP-WEOP2")) {
				// weldOrders =
				// WeldOrder.findAllWeldOrdersByOrderMgr(OrderMgr.findOrderMgr(2L));
				weldOrders = WeldOrder.findWeldOrdersInProcessByOrderMgr(OrderMgr.findOrderMgr(2L));
				return weldOrders;
			}
		}
		weldOrders = WeldOrder.findWeldOrdersInProcess();
		return weldOrders;
	}

	private List<WeldOrder> getWeldordersDelivering() {
		List<WeldOrder> weldOrders = new ArrayList<WeldOrder>();
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		// change to only run query once in case user has more than one authority - MG
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ROLE_APP-OHCVD-MAP-WEOP1")
					|| authority.getAuthority().equals("ROLE_APP-OHCVP-MAP-WEOP1")
					|| authority.getAuthority().equals("ROLE_APP-OHCVQ-MAP-WEOP1")) {
				weldOrders = WeldOrder.findWeldOrdersDeliveringByOrderMgr(OrderMgr.findOrderMgr(1L));
				return weldOrders;
			} else if (authority.getAuthority().equals("ROLE_APP-OHCVD-MAP-WEOP2")
					|| authority.getAuthority().equals("ROLE_APP-OHCVP-MAP-WEOP2")
					|| authority.getAuthority().equals("ROLE_APP-OHCVQ-MAP-WEOP2")) {
				weldOrders = WeldOrder.findWeldOrdersDeliveringByOrderMgr(OrderMgr.findOrderMgr(2L));
				return weldOrders;
			}
		}
		weldOrders = WeldOrder.findWeldOrdersDelivering();
		return weldOrders;
	}

	private List<WeldOrder> getWeldordersPending() {
		List<WeldOrder> weldOrders = new ArrayList<WeldOrder>();
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		// change to only run query once in case user has more than one authority - MG
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ROLE_APP-OHCVD-MAP-WEOP1")
					|| authority.getAuthority().equals("ROLE_APP-OHCVP-MAP-WEOP1")
					|| authority.getAuthority().equals("ROLE_APP-OHCVQ-MAP-WEOP1")) {
				weldOrders = WeldOrder.findWeldOrdersPendingByOrderMgr(OrderMgr.findOrderMgr(1L));
				return weldOrders;
			} else if (authority.getAuthority().equals("ROLE_APP-OHCVD-MAP-WEOP2")
					|| authority.getAuthority().equals("ROLE_APP-OHCVP-MAP-WEOP2")
					|| authority.getAuthority().equals("ROLE_APP-OHCVQ-MAP-WEOP2")) {
				weldOrders = WeldOrder.findWeldOrdersPendingByOrderMgr(OrderMgr.findOrderMgr(2L));
				return weldOrders;
			}
		}
		weldOrders = WeldOrder.findWeldOrdersPending();
		return weldOrders;
	}

	private Collection<OrderMgr> getOrderMgrs() {
		Collection<OrderMgr> orderMgrs = new ArrayList<OrderMgr>();
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		// change to only run query once in case user has more than one authority - MG
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ROLE_APP-OHCVD-MAP-WEOP1")
					|| authority.getAuthority().equals("ROLE_APP-OHCVP-MAP-WEOP1")
					|| authority.getAuthority().equals("ROLE_APP-OHCVQ-MAP-WEOP1")) {
				orderMgrs.add(OrderMgr.findOrderMgr(1L));
			} else if (authority.getAuthority().equals("ROLE_APP-OHCVD-MAP-WEOP2")
					|| authority.getAuthority().equals("ROLE_APP-OHCVP-MAP-WEOP2")
					|| authority.getAuthority().equals("ROLE_APP-OHCVQ-MAP-WEOP2")) {
				orderMgrs.add(OrderMgr.findOrderMgr(2L));
			}
		}
		orderMgrs = populateOrderMgrs();
		return orderMgrs;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid WeldOrder weldOrder, BindingResult bindingResult, org.springframework.ui.Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("weldOrder", weldOrder);
			uiModel.addAttribute("ordermgrs", getOrderMgrs());
			return "weldorders/update";
		}
		uiModel.asMap().clear();
		try {
			WeldOrder tempWeldOrder = WeldOrder.findWeldOrder(weldOrder.getId());

			weldOrder.setLeftConsumedQuantity(tempWeldOrder.getLeftConsumedQuantity());
			weldOrder.setLeftDeliveredQuantity(tempWeldOrder.getLeftDeliveredQuantity());
			weldOrder.setRightConsumedQuantity(tempWeldOrder.getRightConsumedQuantity());
			weldOrder.setRightDeliveredQuantity(tempWeldOrder.getRightDeliveredQuantity());
			weldOrder.setCreatedBy(tempWeldOrder.getCreatedBy());
			weldOrder.setCreatedDate(tempWeldOrder.getCreatedDate());
			weldOrder.setComments(tempWeldOrder.getComments());
			weldOrder.setLeftDeliveryComments(tempWeldOrder.getLeftDeliveryComments());
			weldOrder.setRightDeliveryComments(tempWeldOrder.getRightDeliveryComments());
			weldOrder.setLeftFulfillmentComments(tempWeldOrder.getLeftFulfillmentComments());
			weldOrder.setRightFulfillmentComments(tempWeldOrder.getRightFulfillmentComments());
			weldOrder.setLeftQueuedQty(tempWeldOrder.getLeftQueuedQty());
			weldOrder.setRightQueuedQty(tempWeldOrder.getRightQueuedQty());
			if (weldOrder.getOrderStatus() == null) {
				weldOrder.setOrderStatus(tempWeldOrder.getOrderStatus());
			} else {
				weldOrder.setOrderStatus(weldOrder.getOrderStatus());
			}
			if (weldOrder.getDeliveryStatus() == null) {
				weldOrder.setDeliveryStatus(tempWeldOrder.getDeliveryStatus());
			} else {
				weldOrder.setDeliveryStatus(weldOrder.getDeliveryStatus());
			}
			weldOrder.merge();

			String user = httpServletRequest.getUserPrincipal().getName();
			LOG.info("Order Updated " + tempWeldOrder.getId() + " by user " + user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/weldorders";
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, org.springframework.ui.Model uiModel) {

		WeldOrder weldOrder = WeldOrder.findWeldOrder(id);
		List<WeldOrder> weldOrdersInProcess = getWeldorders();
		List<WeldOrder> weldOrdersPending = getWeldordersPending();
		List<WeldOrder> weldOrdersDelivering = getWeldordersDelivering();

		uiModel.addAttribute("weldOrder", weldOrder);
		uiModel.addAttribute("weldorders", weldOrdersInProcess);
		uiModel.addAttribute("weldordersPending", weldOrdersPending);
		uiModel.addAttribute("weldordersDelivering", weldOrdersDelivering);
		uiModel.addAttribute("orderstatuses", getOrderStatusList(weldOrder, false));
		uiModel.addAttribute("deliverystatuses", getDeliveryStatusList(weldOrder, false));
		uiModel.addAttribute("filterorderstatuses", populateOrderStatuses());
		uiModel.addAttribute("ordermgrs", getOrderMgrs());
		uiModel.addAttribute("models", populateModels());
		uiModel.addAttribute("canUpdateOrderStatus", canUpdateOrderStatus(weldOrder));
		uiModel.addAttribute("canUpdateDeliveryStatus", canUpdateDeliveryStatus(weldOrder));
		return "weldorders/list";
	}

	@RequestMapping(params = "orderfulfillment", method = RequestMethod.GET)
	public String updateOrderFulfillment(@RequestParam("itemId") Long id,
			@RequestParam("carrierNumber") Integer carrierNumber, @RequestParam("cycle") Integer releaseCycle,
			org.springframework.ui.Model uiModel) {
		uiModel.asMap().clear();
		LOG.info("removing carrier from order-" + id);
		WeldOrder weldOrder = WeldOrder.findWeldOrder(id);
		OrderFulfillmentPk pk = new OrderFulfillmentPk(weldOrder, carrierNumber, releaseCycle);
		OrderFulfillment orderFulfillment = OrderFulfillment.findOrderFulfillment(pk);

		if (orderFulfillment != null) {

			if (orderFulfillment.getDie().equals(weldOrder.getModel().getLeftDie())) {
				weldOrder.setLeftDeliveredQuantity(Integer.valueOf(
						weldOrder.getLeftDeliveredQuantity().intValue() - orderFulfillment.getQuantity().intValue()));
			} else {
				weldOrder.setRightDeliveredQuantity(Integer.valueOf(
						weldOrder.getRightDeliveredQuantity().intValue() - orderFulfillment.getQuantity().intValue()));
			}
			weldOrder.merge();
			orderFulfillment.remove();
		}

		WeldOrder order = WeldOrder.findWeldOrder(weldOrder.getId());
		uiModel.addAttribute("weldorder", order);
		uiModel.addAttribute("itemId", order.getId());
		List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(order);
		uiModel.addAttribute("orderfulfillments", fulfillments);
		return "weldorders/show";
	}

	@RequestMapping(params = { "find=ByOrderMgrAndByOrderStatus", "form" }, method = RequestMethod.GET)
	public String findWeldOrdersByOrderMgrAndOrderStatusForm(org.springframework.ui.Model uiModel) {
		uiModel.addAttribute("ordermgrs", getOrderMgrs());
		uiModel.addAttribute("filterorderstatuses", populateOrderStatuses());
		return "weldorders/list";
	}

	@RequestMapping(params = "find=ByOrderMgrAndByOrderStatus", method = RequestMethod.GET)
	public String findWeldOrdersByOrderMgrAndOrderStatus(@RequestParam("orderMgr") OrderMgr orderMgr,
			@RequestParam("orderStatus") OrderStatus orderStatus, org.springframework.ui.Model uiModel) {
		uiModel.addAttribute("weldOrder", new WeldOrder());
		uiModel.addAttribute("weldorders", WeldOrder.findWeldOrdersByOrderMgrAndOrderStatus(orderMgr, orderStatus));
		uiModel.addAttribute("ordermgrs", getOrderMgrs());
		uiModel.addAttribute("filterorderstatuses", populateOrderStatuses());
		ArrayList<OrderStatus> orderstatuses = new ArrayList<OrderStatus>();
		orderstatuses.add(OrderStatus.Initialized);
		uiModel.addAttribute("orderstatuses", orderstatuses);

		return "weldorders/list";
	}

	@RequestMapping(params = "find=ByOrderMgrAndByOrderStatusAndDeliveryStatus", method = RequestMethod.GET)
	public String findWeldOrdersByOrderMgrAndOrderStatusAndDeliveryStatus(@RequestParam("orderMgr") OrderMgr orderMgr,
			@RequestParam("orderStatus") OrderStatus orderStatus,
			@RequestParam("deliveryStatus") OrderStatus deliveryStatus, org.springframework.ui.Model uiModel) {

		List<WeldOrder> orders = null;

		if (orderMgr != null) {
			orders = WeldOrder.findWeldOrdersByOrderMgrNotNullAndOrderStatusAndDeliveryStatus(orderMgr, orderStatus,
					deliveryStatus);
		} else {
			orders = WeldOrder.findWeldOrdersByDeliveryStatusAndOrderStatus(deliveryStatus, orderStatus);
		}
		uiModel.addAttribute("weldOrder", new WeldOrder());
		uiModel.addAttribute("weldorders", orders);
		uiModel.addAttribute("ordermgrs", getOrderMgrs());
		uiModel.addAttribute("filterorderstatuses", populateOrderStatuses());

		ArrayList<OrderStatus> orderstatuses = new ArrayList<OrderStatus>();
		orderstatuses.add(OrderStatus.Initialized);
		ArrayList<OrderStatus> deliverystatuses = new ArrayList<OrderStatus>();
		deliverystatuses.add(OrderStatus.Initialized);
		uiModel.addAttribute("deliverystatuses", deliverystatuses);

		uiModel.addAttribute("orderstatuses", orderstatuses);

		return "weldorders/list";
	}

	@RequestMapping(params = "ByOrder", method = RequestMethod.GET)
	public String findWeldOrderForm(org.springframework.ui.Model uiModel) {
		LOG.debug("by order, form");
		uiModel.addAttribute("ordermgrs", getOrderMgrs());
		return "weldorders/manual";
	}

	@RequestMapping(params = "find=ByOrder", method = RequestMethod.GET)
	public String findWeldOrder(@RequestParam("orderMgr") OrderMgr orderMgr, org.springframework.ui.Model uiModel) {
		LOG.debug(orderMgr.getLineName());

		uiModel.addAttribute("weldOrder", new WeldOrder());
		uiModel.addAttribute("weldorders", WeldOrder.findAllWeldOrders());
		uiModel.addAttribute("ordermgrs", getOrderMgrs());
		uiModel.addAttribute("filterorderstatuses", populateOrderStatuses());
		ArrayList<OrderStatus> orderstatuses = new ArrayList<OrderStatus>();
		orderstatuses.add(OrderStatus.Initialized);
		uiModel.addAttribute("orderstatuses", orderstatuses);
		return "weldorders/list";
	}

	@RequestMapping(params = "ByManualComplete", method = RequestMethod.GET)
	public String manuallyCompleteWeldOrderForm(org.springframework.ui.Model uiModel) {
		return "weldorders/manual";
	}

	@RequestMapping(params = "find=ByManualComplete", method = RequestMethod.GET)
	public String manuallyCompleteWeldOrder(@RequestParam("id") Long id, org.springframework.ui.Model uiModel) {
		LOG.info("Complete WeldOrder--" + id);
		WeldOrder order = WeldOrder.findWeldOrder(id);
		order.setOrderStatus(OrderStatus.ManuallyCompleted);
		order.setDeliveryStatus(OrderStatus.ManuallyCompleted);
		order.merge();
		LOG.info(" Refreshing Storage State ");
		carrierManagementServiceProxy.refreshStorageState();

		uiModel.addAttribute("weldorder", WeldOrder.findWeldOrder(id));
		uiModel.addAttribute("itemId", id);
		return "redirect:/weldorders";
	}

	@RequestMapping(params = "find=ByCancel", method = RequestMethod.GET)
	public String cancelWeldOrder(@RequestParam("id") Long id, org.springframework.ui.Model uiModel) {
		LOG.info("Cancel WeldOrder--" + id);
		WeldOrder order = WeldOrder.findWeldOrder(id);
		order.setOrderStatus(OrderStatus.Cancelled);
		order.setDeliveryStatus(OrderStatus.Cancelled);
		order.merge();
		uiModel.addAttribute("weldorder", WeldOrder.findWeldOrder(id));
		uiModel.addAttribute("itemId", id);
		return "redirect:/weldorders";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size, org.springframework.ui.Model uiModel) {
		WeldOrder.findWeldOrder(id).remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/weldorders";
	}

	@ModelAttribute("ordermgrs")
	public Collection<OrderMgr> populateOrderMgrs() {
		return OrderMgr.findAllOrderMgrs();
	}

	@ModelAttribute("orderstatuses")
	public Collection<OrderStatus> populateOrderStatuses() {
		List<OrderStatus> statusList = null;
		statusList = Arrays.asList(OrderStatus.class.getEnumConstants());
		/*
		 * statusList.remove(OrderStatus.Delivered);
		 * statusList.remove(OrderStatus.DeliveringCarriers);
		 */
		return statusList;
	}

	@ModelAttribute("weldorders")
	public Collection<WeldOrder> populateWeldOrders() {
		return WeldOrder.findAllWeldOrders();
	}

	@ModelAttribute("carrierfulfillmentstatuses")
	public Collection<CarrierFulfillmentStatus> populateCarrierFulfillmentStatuses() {
		return Arrays.asList(CarrierFulfillmentStatus.class.getEnumConstants());
	}

	@ModelAttribute("stops")
	public Collection<Stop> populateStops() {
		return Stop.findAllStops();
	}

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest)
			throws UnsupportedEncodingException {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		return pathSegment;
	}

	List<OrderStatus> getOrderStatusList(WeldOrder weldOrder, boolean create) {

		ArrayList<OrderStatus> orderstatuses = new ArrayList<OrderStatus>();
		if (create) {
			orderstatuses.add(OrderStatus.Initialized);
		} else {
			if (weldOrder.getOrderStatus() == OrderStatus.Queued
					|| weldOrder.getOrderStatus() == OrderStatus.Initialized) {
				orderstatuses.add(OrderStatus.InProcess);
				orderstatuses.add(OrderStatus.OnHold);
			} else if (weldOrder.getOrderStatus().equals(OrderStatus.InProcess)) {
				orderstatuses.add(OrderStatus.OnHold);
				orderstatuses.add(OrderStatus.InProcess);
			} else if (weldOrder.getOrderStatus().equals(OrderStatus.OnHold)) {
				orderstatuses.add(OrderStatus.InProcess);
				orderstatuses.add(OrderStatus.OnHold);
			} else {
				orderstatuses.add(weldOrder.getOrderStatus());
			}
		}
		return orderstatuses;
	}

	List<OrderStatus> getDeliveryStatusList(WeldOrder weldOrder, boolean create) {

		ArrayList<OrderStatus> deliverystatuses = new ArrayList<OrderStatus>();
		if (create) {
			deliverystatuses.add(OrderStatus.Initialized);
		} else {
			if (weldOrder.getDeliveryStatus().equals(OrderStatus.Initialized)) {
				deliverystatuses.add(OrderStatus.InProcess);
				deliverystatuses.add(OrderStatus.OnHold);
			} else if (weldOrder.getDeliveryStatus().equals(OrderStatus.InProcess)) {
				deliverystatuses.add(OrderStatus.OnHold);
				deliverystatuses.add(OrderStatus.InProcess);
			} else if (weldOrder.getDeliveryStatus().equals(OrderStatus.OnHold)) {
				deliverystatuses.add(OrderStatus.InProcess);
				deliverystatuses.add(OrderStatus.OnHold);
			} else {
				deliverystatuses.add(weldOrder.getDeliveryStatus());
			}
		}
		return deliverystatuses;
	}

	boolean canUpdateOrderStatus(WeldOrder weldOrder) {
		boolean canUpdateOrderStatus = false;

		List<WeldOrder> inprocessWeldOrders = WeldOrder.findWeldOrdersByOrderMgrAndOrderStatus(weldOrder.getOrderMgr(),
				com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus.InProcess);
		List<WeldOrder> retrievingWeldOrders = WeldOrder.findWeldOrdersByOrderMgrAndOrderStatus(weldOrder.getOrderMgr(),
				com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus.RetrievingCarriers);
		List<WeldOrder> deliveringWeldOrders = WeldOrder.findWeldOrdersByOrderMgrAndOrderStatus(weldOrder.getOrderMgr(),
				com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus.DeliveringCarriers);
		if (inprocessWeldOrders == null && retrievingWeldOrders == null && deliveringWeldOrders == null) {
			canUpdateOrderStatus = true;
		} else if (inprocessWeldOrders.size() == 0 && retrievingWeldOrders.size() == 0
				&& deliveringWeldOrders.size() == 0) {
			canUpdateOrderStatus = true;
		}

		if (weldOrder.getOrderStatus().equals(com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus.InProcess)) {
			canUpdateOrderStatus = true;
		}
		return canUpdateOrderStatus;
	}

	boolean canUpdateDeliveryStatus(WeldOrder weldOrder) {
		boolean canUpdateOrderStatus = false;

		List<WeldOrder> inprocessWeldOrders = WeldOrder.findWeldOrdersByOrderMgrAndDeliveryStatus(
				weldOrder.getOrderMgr(), com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus.InProcess);
		List<WeldOrder> onHoldWeldOrders = WeldOrder.findWeldOrdersByOrderMgrAndDeliveryStatus(weldOrder.getOrderMgr(),
				OrderStatus.OnHold);
		List<WeldOrder> deliveringWeldOrders = WeldOrder.findWeldOrdersByOrderMgrAndDeliveryStatus(
				weldOrder.getOrderMgr(), com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus.DeliveringCarriers);
		if (inprocessWeldOrders == null && deliveringWeldOrders == null && onHoldWeldOrders == null) {
			canUpdateOrderStatus = true;
		} else if (inprocessWeldOrders.size() == 0 && deliveringWeldOrders.size() == 0
				&& onHoldWeldOrders.size() == 0) {
			canUpdateOrderStatus = true;
		}

		if (weldOrder.getDeliveryStatus().equals(com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus.InProcess)
				|| weldOrder.getDeliveryStatus().equals(com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus.OnHold)) {
			canUpdateOrderStatus = true;
		}
		return canUpdateOrderStatus;
	}

	boolean isNextForDelivery(WeldOrder order) {
		Stop qStop = order.getOrderMgr().getLeftQueueStop();
		WeldOrder thisOrder = null;
		boolean isNext = false;
		if (order.isRightOnly()) {
			qStop = order.getOrderMgr().getRightQueueStop();
		}

		List<OrderFulfillment> fulfillmentsInQueue = OrderFulfillment
				.findAllOrderFulfillmentsWithCurrentLocation(qStop);
		// loop through all fulfillments in queue, skipping those that are being
		// delivered
		for (OrderFulfillment f : fulfillmentsInQueue) {
			thisOrder = f.getId().getWeldOrder();
			if (thisOrder.getDeliveryStatus() == OrderStatus.DeliveringCarriers
					|| thisOrder.getDeliveryStatus() == OrderStatus.InProcess) {
				if (thisOrder.getId() == order.getId())
					break; // already being delivered
				else
					continue;
			} else { // first fulfillment that is not being currently delivered
				if (thisOrder.getId() == order.getId())
					isNext = true;
				else
					isNext = false;
			}
		}
		return isNext;
	}
}
