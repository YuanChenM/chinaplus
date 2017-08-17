package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TNV_EXP_DELIVERY database table.
 * 
 */
@Entity(name="TNV_EXP_DELIVERY")
public class TnvExpDelivery extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNV_EXP_DELIVERY_EXPDELIVERYID_GENERATOR", sequenceName="SEQ_TNV_EXP_DELIVERY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNV_EXP_DELIVERY_EXPDELIVERYID_GENERATOR")
	@Column(name="EXP_DELIVERY_ID")
	private Integer expDeliveryId;

	@Column(name="DELAY_BY_AIR")
	private BigDecimal delayByAir;

	@Column(name="DELAY_BY_SEA")
	private BigDecimal delayBySea;

	@Column(name="FUTURE_BY_AIR")
	private BigDecimal futureByAir;

	@Column(name="FUTURE_BY_SEA")
	private BigDecimal futureBySea;

	@Column(name="ORDER_DETAIL_ID")
	private Integer orderDetailId;

	public TnvExpDelivery() {
	}

	public Integer getExpDeliveryId() {
		return this.expDeliveryId;
	}

	public void setExpDeliveryId(Integer expDeliveryId) {
		this.expDeliveryId = expDeliveryId;
	}

	public BigDecimal getDelayByAir() {
		return this.delayByAir;
	}

	public void setDelayByAir(BigDecimal delayByAir) {
		this.delayByAir = delayByAir;
	}

	public BigDecimal getDelayBySea() {
		return this.delayBySea;
	}

	public void setDelayBySea(BigDecimal delayBySea) {
		this.delayBySea = delayBySea;
	}

	public BigDecimal getFutureByAir() {
		return this.futureByAir;
	}

	public void setFutureByAir(BigDecimal futureByAir) {
		this.futureByAir = futureByAir;
	}

	public BigDecimal getFutureBySea() {
		return this.futureBySea;
	}

	public void setFutureBySea(BigDecimal futureBySea) {
		this.futureBySea = futureBySea;
	}

	public Integer getOrderDetailId() {
		return this.orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

}