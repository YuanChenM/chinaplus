/**
 * @screen CPCUMS02
 * @author zhang_chi
 */
package com.chinaplus.web.com.entity;

import com.chinaplus.core.base.BaseEntity;

/** 
 * CPCUMS02ComboEntity.
 */ 
public class CPCUMS02ComboEntity  extends BaseEntity{

    /** serialVersionUID */
    private static final long serialVersionUID = -1674731349706045588L;


    /** id. */
    private Integer  id;
    
    /** text. */ 
    private String  text;
    
    
    /**
     * Get the id.
     *
     * @return id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Set the id.
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the text.
     *
     * @return text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the text.
     *
     * @param text text
     */
    public void setText(String text) {
        this.text = text;
    }

}
