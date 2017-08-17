package com.chinaplus.batch.common.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Base class for batch parameters.
 */
public class BaseBatchParam implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Office ID */
    private Integer officeId;

    /** Office Code */
    private String officeCode;

    /** Process date */
    private Timestamp processDate;

    /** The Run Model */
    private String model;

    /** The Process Mode */
    private String processMode;

    /**
     * Get the office Code.
     * 
     * @return office Code
     */
    public String getOfficeCode() {
        return this.officeCode;
    }

    /**
     * Set the office Code.
     * 
     * @param officeCode office Code
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    /**
     * Get the office ID.
     * 
     * @return office ID
     */
    public Integer getOfficeId() {
        return this.officeId;
    }

    /**
     * Set the office ID.
     * 
     * @param officeId office ID
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    /**
     * Get the process date.
     * 
     * @return process date
     */
    public Timestamp getProcessDate() {
        return this.processDate;
    }

    /**
     * Set the process date.
     * 
     * @param processDate process date
     */
    public void setProcessDate(Timestamp processDate) {
        this.processDate = processDate;
    }

    /**
     * Get the model.
     *
     * @return model
     */
    public String getModel() {
        return this.model;
    }

    /**
     * Set the model.
     *
     * @param model model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Get the Process Mode.
     *
     * @return Process Mode
     */
    public String getProcessMode() {
        return this.processMode;
    }

    /**
     * Set the Process Mode.
     *
     * @param processMode processMode
     */
    public void setProcessMode(String processMode) {
        this.processMode = processMode;
    }
}
