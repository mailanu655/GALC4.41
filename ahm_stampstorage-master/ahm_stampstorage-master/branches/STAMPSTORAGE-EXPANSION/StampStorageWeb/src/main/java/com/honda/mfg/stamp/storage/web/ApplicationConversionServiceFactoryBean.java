package com.honda.mfg.stamp.storage.web;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.honda.mfg.stamp.conveyor.domain.AlarmContact;
import com.honda.mfg.stamp.conveyor.domain.AlarmDefinition;
import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.conveyor.domain.AuditErrorLog;
import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierHistory;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.Contact;
import com.honda.mfg.stamp.conveyor.domain.Defect;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Model;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.WeldSchedule;

/**
 * A central place to register application Converters and Formatters.
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	// TODO to verify with MvcConfig
	/*
	 * protected void installFormatters(FormatterRegistry registry) {
	 * super.installFormatters(registry); // Register application converters and
	 * formatters }
	 */

	public void installLabelConverters(FormatterRegistry registry) {
		registry.addConverter(new CarrierConverter());
		registry.addConverter(new DieConverter());
		registry.addConverter(new ModelConverter());
		registry.addConverter(new OrderMgrConverter());
		registry.addConverter(new StopConverter());
		registry.addConverter(new WeldOrderConverter());
		registry.addConverter(new AlarmDefinitionConverter());
		registry.addConverter(new AlarmEventConverter());
		registry.addConverter(new CarrierHistoryConverter());
		registry.addConverter(new DefectConverter());
		// registry.addConverter(new LaneImplConverter());
		registry.addConverter(new OrderFulfillmentConverter());
		registry.addConverter(new JsonToOrderFulfillmentPkConverter());
		registry.addConverter(new OrderFulfillmentPkToJsonConverter());
		registry.addConverter(new WeldScheduleConverter());
		registry.addConverter(new CarrierReleaseConverter());
		// registry.addConverter(new GroupHoldConverter());
		registry.addConverter(new AuditErrorLogConverter());
		registry.addConverter(new ContactConverter());
		registry.addConverter(new AlarmContactConverter());
		registry.addConverter(new StorageRowConverter());
		// registry.addConverter(new ValidDestinationConverter());

	}

	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		installLabelConverters(getObject());
	}

	static class CarrierConverter implements Converter<Carrier, String> {
		public String convert(Carrier carrier) {
			return new StringBuilder().append(carrier.getCarrierNumber()).append(" ").append(carrier.getQuantity())
					.append(" ").append(carrier.getLoadTimestamp()).append(" ").append(carrier.getUnloadTimestamp())
					.toString();
		}

	}

	static class DieConverter implements Converter<Die, String> {
		public String convert(Die die) {
			return new StringBuilder().append(die.getId()).append(" ").append(die.getDescription()).toString();
		}

	}

	static class ModelConverter implements Converter<Model, String> {
		public String convert(Model model) {
			return new StringBuilder().append(model.getName()).append(" ").append(model.getDescription()).toString();
		}

	}

	static class OrderMgrConverter implements Converter<OrderMgr, String> {
		public String convert(OrderMgr orderMgr) {
			return new StringBuilder().append(orderMgr.getLineName()).toString();
		}

	}

	static class StopConverter implements Converter<Stop, String> {
		public String convert(Stop stop) {
			return new StringBuilder().append(stop.getName()).toString();
		}

	}

	static class WeldOrderConverter implements Converter<WeldOrder, String> {
		public String convert(WeldOrder weldOrder) {
			return new StringBuilder().append(weldOrder.getOrderSequence()).append(" ").toString();
		}

	}

	static class AlarmDefinitionConverter implements Converter<AlarmDefinition, String> {
		public String convert(AlarmDefinition alarmDefinition) {
			return new StringBuilder().append(alarmDefinition.getDescription()).toString();
		}

	}

	static class CarrierHistoryConverter implements Converter<CarrierHistory, String> {
		public String convert(CarrierHistory carrierHistory) {
			return new StringBuilder().append(carrierHistory.getBuffer()).append(" ")
					.append(carrierHistory.getCarrierNumber()).append(" ").append(carrierHistory.getCurrentLocation())
					.append(" ").append(carrierHistory.getDestination()).toString();
		}

	}

	static class DefectConverter implements Converter<Defect, String> {
		public String convert(Defect defect) {
			return new StringBuilder().append(defect.getCarrierNumber()).append(" ").append(defect.getProductionRunNo())
					.append(" ").append(defect.getXArea()).append(" ").append(defect.getYArea()).toString();
		}

	}

	static class AlarmEventConverter implements Converter<AlarmEvent, String> {
		public String convert(AlarmEvent alarmEvent) {
			return new StringBuilder().append(alarmEvent.getLocation()).append(" ").append(alarmEvent.getAlarmNumber())
					.toString();
		}
	}

	// static class
	// com.honda.mfg.stamp.storage.web.ApplicationConversionServiceFactoryBean.LaneImplConverter
	// implements Converter<LaneImpl, String> {
//        public String convert(LaneImpl laneImpl) {
//            return new StringBuilder().append(laneImpl.getLaneName()).toString();
//        }
//
//    }

	static class OrderFulfillmentConverter implements Converter<OrderFulfillment, String> {
		public String convert(OrderFulfillment orderFulfillment) {
			return new StringBuilder().append(orderFulfillment.getQuantity()).toString();
		}

	}

	static class JsonToOrderFulfillmentPkConverter implements Converter<String, OrderFulfillmentPk> {
		public OrderFulfillmentPk convert(String encodedJson) {
			return OrderFulfillmentPk.fromJsonToOrderFulfillmentPk(new String(Base64.decodeBase64(encodedJson)));
		}

	}

	static class OrderFulfillmentPkToJsonConverter implements Converter<OrderFulfillmentPk, String> {
		public String convert(OrderFulfillmentPk orderFulfillmentPk) {
			return Base64.encodeBase64URLSafeString(orderFulfillmentPk.toJson().getBytes());
		}

	}

	static class WeldScheduleConverter implements Converter<WeldSchedule, String> {
		public String convert(WeldSchedule weldSchedule) {
			return new StringBuilder().append(weldSchedule.getWeldLine()).append(" ")
					.append(weldSchedule.getLeftHandProdPlan()).append(" ")
					.append(weldSchedule.getLeftHandProdRemaining()).append(" ")
					.append(weldSchedule.getRightHandProdPlan()).append(" ")
					.append(weldSchedule.getRightHandProdRemaining()).toString();
		}

	}

	static class CarrierReleaseConverter implements Converter<CarrierRelease, String> {
		public String convert(CarrierRelease carrierRelease) {
			return new StringBuilder().append(carrierRelease.getSource()).toString();
		}

	}

	// static class
	// com.honda.mfg.stamp.storage.web.ApplicationConversionServiceFactoryBean.GroupHoldConverter
	// implements Converter<GroupHold, String> {
//        public String convert(GroupHold groupHold) {
//            return new StringBuilder().append(groupHold.getMessage()).toString();
//        }
//
//    }

	static class AuditErrorLogConverter implements Converter<AuditErrorLog, String> {
		public String convert(AuditErrorLog auditErrorLog) {
			return new StringBuilder().append(auditErrorLog.getNodeId()).append(" ").append(auditErrorLog.getSource())
					.append(" ").append(auditErrorLog.getSeverity()).append(" ").append(auditErrorLog.getMessageText())
					.toString();
		}

	}

	static class ContactConverter implements Converter<Contact, String> {
		public String convert(Contact contact) {
			return new StringBuilder().append(contact.getContactName()).toString();
		}

	}

	static class AlarmContactConverter implements Converter<AlarmContact, String> {
		public String convert(AlarmContact alarmContact) {
			return new StringBuilder().append(alarmContact.getAlarm()).append(" ").append(alarmContact.getContact())
					.append(" ").append(alarmContact.getContactType()).toString();
		}

	}

	static class StorageRowConverter implements Converter<StorageRow, String> {
		public String convert(StorageRow storageRow) {
			return new StringBuilder().append(storageRow.getRowName()).toString();
		}

	}
}
