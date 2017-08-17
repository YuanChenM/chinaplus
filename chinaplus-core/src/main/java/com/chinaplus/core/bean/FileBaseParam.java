/**
 * BaseParam.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import org.springframework.web.multipart.MultipartFile;


/**
 * The parameter from page request.
 */
public class FileBaseParam extends BaseParam {

    /** serialVersionUID */
    private static final long serialVersionUID = -7940245098961717223L;

    private MultipartFile supportDoc1;
    
    private MultipartFile supportDoc2;
    
    private MultipartFile supportDoc3;
    
    private MultipartFile supportDoc4;
    
    private MultipartFile supportDoc5;

    /**
     * Get the supportDoc1.
     *
     * @return supportDoc1
     */
    public MultipartFile getSupportDoc1() {
        return this.supportDoc1;
    }

    /**
     * Set the supportDoc1.
     *
     * @param supportDoc1 supportDoc1
     */
    public void setSupportDoc1(MultipartFile supportDoc1) {
        this.supportDoc1 = supportDoc1;
        
    }

    /**
     * Get the supportDoc2.
     *
     * @return supportDoc2
     */
    public MultipartFile getSupportDoc2() {
        return this.supportDoc2;
    }

    /**
     * Set the supportDoc2.
     *
     * @param supportDoc2 supportDoc2
     */
    public void setSupportDoc2(MultipartFile supportDoc2) {
        this.supportDoc2 = supportDoc2;
        
    }

    /**
     * Get the supportDoc3.
     *
     * @return supportDoc3
     */
    public MultipartFile getSupportDoc3() {
        return this.supportDoc3;
    }

    /**
     * Set the supportDoc3.
     *
     * @param supportDoc3 supportDoc3
     */
    public void setSupportDoc3(MultipartFile supportDoc3) {
        this.supportDoc3 = supportDoc3;
        
    }

    /**
     * Get the supportDoc4.
     *
     * @return supportDoc4
     */
    public MultipartFile getSupportDoc4() {
        return this.supportDoc4;
    }

    /**
     * Set the supportDoc4.
     *
     * @param supportDoc4 supportDoc4
     */
    public void setSupportDoc4(MultipartFile supportDoc4) {
        this.supportDoc4 = supportDoc4;
        
    }

    /**
     * Get the supportDoc5.
     *
     * @return supportDoc5
     */
    public MultipartFile getSupportDoc5() {
        return this.supportDoc5;
    }

    /**
     * Set the supportDoc5.
     *
     * @param supportDoc5 supportDoc5
     */
    public void setSupportDoc5(MultipartFile supportDoc5) {
        this.supportDoc5 = supportDoc5;
        
    }
}
