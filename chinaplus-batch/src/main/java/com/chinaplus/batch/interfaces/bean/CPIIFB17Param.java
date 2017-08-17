/**
 * CPIIFB17Param.java
 * 
 * @screen CPIIFB17
 * @author Cheng xingfei
 */
package com.chinaplus.batch.interfaces.bean;

import com.chinaplus.batch.common.bean.BaseBatchParam;

/**
 * Class for batch parameters.
 * 
 * @author Cheng xingfei
 */
public class CPIIFB17Param extends BaseBatchParam {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** interfaceFilePath */
    private String interfaceFilePath;

    /** workingFilePath */
    private String workingFilePath;

    /** backupFilePath */
    private String backupFilePath;

    /**
     * @return the interfaceFilePath
     */
    public String getInterfaceFilePath() {
        return interfaceFilePath;
    }

    /**
     * @param interfaceFilePath the interfaceFilePath to set
     */
    public void setInterfaceFilePath(String interfaceFilePath) {
        this.interfaceFilePath = interfaceFilePath;
    }

    /**
     * @return the workingFilePath
     */
    public String getWorkingFilePath() {
        return workingFilePath;
    }

    /**
     * @param workingFilePath the workingFilePath to set
     */
    public void setWorkingFilePath(String workingFilePath) {
        this.workingFilePath = workingFilePath;
    }

    /**
     * @return the backupFilePath
     */
    public String getBackupFilePath() {
        return backupFilePath;
    }

    /**
     * @param backupFilePath the backupFilePath to set
     */
    public void setBackupFilePath(String backupFilePath) {
        this.backupFilePath = backupFilePath;
    }
}
