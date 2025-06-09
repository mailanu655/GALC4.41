package com.honda.mfg.stamp.conveyor.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Embeddable
@Configurable
public final class OrderFulfillmentPk implements Serializable {

	@NotNull
	@ManyToOne
	@JoinColumn(name="WELD_ORDER", referencedColumnName="ORDER_ID")
	private WeldOrder weldOrder;

	@Column(name = "CARRIER_NUMBER")
	private Integer carrierNumber;

	@Column(name = "RELEASE_CYCLE")
	private Integer releaseCycle;

	public OrderFulfillmentPk(WeldOrder weldOrder, Integer carrierNumber, Integer releaseCycle) {
		super();
		this.weldOrder = weldOrder;
		this.carrierNumber = carrierNumber;
		this.releaseCycle = releaseCycle;
	}

	private OrderFulfillmentPk() {
		super();
	}

	public WeldOrder getWeldOrder() {
		return this.weldOrder;
	}

	public Integer getCarrierNumber() {
		return this.carrierNumber;
	}

	public Integer getReleaseCycle() {
		return this.releaseCycle;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OrderFulfillmentPk))
			return false;
		OrderFulfillmentPk other = (OrderFulfillmentPk) obj;
		if (weldOrder == null) {
			if (other.weldOrder != null)
				return false;
		} else if (!weldOrder.equals(other.weldOrder))
			return false;
		if (carrierNumber == null) {
			if (other.carrierNumber != null)
				return false;
		} else if (!carrierNumber.equals(other.carrierNumber))
			return false;
		if (releaseCycle == null) {
			if (other.releaseCycle != null)
				return false;
		} else if (!releaseCycle.equals(other.releaseCycle))
			return false;
		return true;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + (weldOrder == null ? 0 : weldOrder.hashCode());
		result = prime * result + (carrierNumber == null ? 0 : carrierNumber.hashCode());
		result = prime * result + (releaseCycle == null ? 0 : releaseCycle.hashCode());
		return result;
	}

	// declare parents: OrderFulfillmentPk implements Serializable;

	private static final long serialVersionUID = 1L;

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public static OrderFulfillmentPk fromJsonToOrderFulfillmentPk(String json) {
		return new JSONDeserializer<OrderFulfillmentPk>().use(null, OrderFulfillmentPk.class).deserialize(json);
	}

	public static String toJsonArray(Collection<OrderFulfillmentPk> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<OrderFulfillmentPk> fromJsonArrayToOrderFulfillmentPks(String json) {
		return new JSONDeserializer<List<OrderFulfillmentPk>>().use(null, ArrayList.class)
				.use("values", OrderFulfillmentPk.class).deserialize(json);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ReleaseCycle: ").append(getReleaseCycle()).append(", ");
		sb.append("CarrierNumber: ").append(getCarrierNumber()).append(", ");
		sb.append("WeldOrder: ").append(getWeldOrder());
		return sb.toString();
	}
}
