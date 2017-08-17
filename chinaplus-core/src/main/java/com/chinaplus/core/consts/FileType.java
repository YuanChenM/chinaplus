/**
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.consts;

import java.util.Arrays;

/**
 * The file type enum.
 */
public enum FileType {

    /** all file type */
    ALL(null),
    /** excel */
    EXCEL(new String[] { "xlsx" }),
    /** mapping file type */
    MAPPINGFILETYPE(new String[] { "xls", "csv", "txt" }),
    /** CSV */
    CSV(new String[] { "csv" }),
    /** PDF */
    PDF(new String[] { "pdf" }),
    /** ZIP */
    ZIP(new String[] { "zip" }),
    /** TXT */
    TXT(new String[] { "txt" });

    /** file suffix array of this type */
    private String[] suffixs;

    /**
     * 
     * <p>
     * The Constructors Method.
     * </p>
     * 
     * @param suffixs file suffix array
     */
    FileType(String[] suffixs) {
        this.suffixs = suffixs;
    }

    /**
     * <p>
     * Check the given suffix
     * </p>
     * 
     * @param suffix the file suffix
     * @return if the given suffix is match this type then return true
     */
    public boolean match(String suffix) {
        boolean exits = false;
        if (this.suffixs != null) {
            exits = Arrays.asList(this.suffixs).contains(suffix);
        } else {
            exits = true;
        }
        return exits;
    }

    /**
     * <p>
     * Check the given suffix
     * </p>
     * 
     * @param suffix the file suffix
     * @return if the given suffix is match this type then return true
     */
    public boolean matchIgnoreCase(String suffix) {
        boolean exist = false;
        if (this.suffixs != null) {
            String[] tempSuffix = new String[this.suffixs.length];
            for (int idx = 0; idx < this.suffixs.length; idx++) {
                tempSuffix[idx] = this.suffixs[idx].toLowerCase();
            }
            exist = Arrays.asList(tempSuffix).contains(suffix.toLowerCase());
        } else {
            exist = true;
        }
        return exist;
    }

    /**
     * Get file suffix for this type.
     * If this type has more than one suffix, then return first.
     * 
     * @return the file suffix
     */
    public String getSuffix() {
        String suffix = "";

        if (this.suffixs != null && this.suffixs.length > 0) {
            suffix = this.suffixs[0];
        }

        return suffix;
    }
}
