/**
 * CPOOHS01Entity.java
 * 
 * @screen CPOOHS01
 * @author Xiang_chao
 */
package com.chinaplus.web.om.entity;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * CPOOHS01Entity.
 */
public class CPOOHF01Entity extends CPOOHS01Entity {
	
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
	
	/** On shipping qty Array */
	private BigDecimal[] onShippingQtyArray;		
	
	/** ETD */
	private Date etd;		
	
	/** ETA */
	private Date eta;		
	
	/** on shipping plan qty */
	private BigDecimal qty;

	/**
	 * @return the onShippingQtyArray
	 */
	public BigDecimal[] getOnShippingQtyArray() {
		return onShippingQtyArray;
	}

	/**
	 * @param onShippingQtyArray the onShippingQtyArray to set
	 */
	public void setOnShippingQtyArray(BigDecimal[] onShippingQtyArray) {
		this.onShippingQtyArray = onShippingQtyArray;
	}

	/**
	 * @return the etd
	 */
	public Date getEtd() {
		return etd;
	}

	/**
	 * @param etd the etd to set
	 */
	public void setEtd(Date etd) {
		this.etd = etd;
	}

	/**
	 * @return the eta
	 */
	public Date getEta() {
		return eta;
	}

	/**
	 * @param eta the eta to set
	 */
	public void setEta(Date eta) {
		this.eta = eta;
	}

	/**
	 * @return the qty
	 */
	public BigDecimal getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	
}
