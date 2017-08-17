/**
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.io.Serializable;

/**
 * TreeNodeEntity.
 */
public class TreeNode implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private String id;

    private boolean leaf = true;

    private String text;

    private String screenId;

    private String root;

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
     * Get the leaf.
     * 
     * @return leaf
     */
    public boolean isLeaf() {
        return this.leaf;
    }

    /**
     * Set the leaf.
     * 
     * @param leaf leaf
     */
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
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

    /**
     * Get the root.
     *
     * @return root
     */
    public String getRoot() {
        return this.root;
    }

    /**
     * Set the root.
     *
     * @param root root
     */
    public void setRoot(String root) {
        this.root = root;
        
    }

    /**
     * Get the screenId.
     *
     * @return screenId
     */
    public String getScreenId() {
        return this.screenId;
    }

    /**
     * Set the screenId.
     *
     * @param screenId screenId
     */
    public void setScreenId(String screenId) {
        this.screenId = screenId;
        
    }

}
