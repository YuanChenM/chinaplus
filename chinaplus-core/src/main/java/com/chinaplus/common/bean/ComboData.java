/**
 * ComboData.java
 * 
 * @screen common
 * @author ma_b
 */
package com.chinaplus.common.bean;

import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.util.StringUtil;

/**
 * The entity for master data combobox.
 * 
 */
public class ComboData extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = -8081214993514385727L;

    /** id */
    private String id;
    /** text */
    private String text;

    /**
     * Get the id.
     * 
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Set the id.
     * 
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the Text.
     * 
     * @return Text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the Text.
     * 
     * @param text text
     */
    public void setText(String text) {
        this.text = text;
    }

    // /**
    // * hashCode
    // *
    // * @return
    // * @see java.lang.Object#hashCode()
    // */
    // @Override
    // public int hashCode() {
    // final int prime = 31;
    // int result = 1;
    // result = prime * result + ((id == null) ? 0 : id.hashCode());
    // result = prime * result + ((text == null) ? 0 : text.hashCode());
    // return result;
    // }

    // /**
    // * equals
    // *
    // * @param obj
    // * @return boolean
    // * @see java.lang.Object#equals(java.lang.Object)
    // */
    // @Override
    // public boolean equals(Object obj) {
    //
    // if (this == obj) {
    // return true;
    // }
    //
    // if (obj == null) {
    // return false;
    // }
    //
    // if (getClass() != obj.getClass()) {
    // return false;
    // }
    //
    // ComboData other = (ComboData) obj;
    //
    // if (id == null) {
    // if (other.id != null) {
    // return false;
    // }
    // } else if (!id.equals(other.id)) {
    // return false;
    // }
    //
    // if (text == null) {
    // if (other.text != null) {
    // return false;
    // }
    // } else if (!text.equals(other.text)) {
    // return false;
    // }
    // return true;
    // }

    /**
     * toString
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return StringUtil.formatMessage("MasterData [id={}, text={}]", this.id, this.text);
    }

}
