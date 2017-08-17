/**
 * CPSRDS01Service.java
 * 
 * @screen CPSRDS01
 * @author zhang_chi
 */
package com.chinaplus.web.sa.service;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.RundownType;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.web.sa.entity.CPSRDS01Entity;

/**
 * CPSRDS01Service
 */
@Service
public class CPSRDS01Service extends BaseService {

    /**
     * get From To Date
     * 
     * @param officeTimezone officeTimezone
     * @param lang lang
     * @return login user
     * @throws Exception Exception
     * 
     */
    public CPSRDS01Entity getFromToDate(String officeTimezone, Integer lang) throws Exception {
        
        // get db date time
        Timestamp officeTime = super.getDBDateTime(officeTimezone);

        CPSRDS01Entity cpsrds01Entity = new CPSRDS01Entity();

        SimpleDateFormat sdfE = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        SimpleDateFormat sdfZ = new SimpleDateFormat("yyyy/MM/dd");

        Calendar cds = Calendar.getInstance();
        cds.setTime(officeTime);
        cds.add(Calendar.DAY_OF_YEAR, -1);

        Calendar cde = Calendar.getInstance();
        cde.setTime(officeTime);
        cde.add(Calendar.MONTH, -1);
        cde.set(Calendar.DAY_OF_MONTH, 1);

        if (lang == IntDef.INT_ONE) {
            cpsrds01Entity.setEnd(sdfZ.format(cds.getTime()));
            cpsrds01Entity.setStart(sdfZ.format(cde.getTime()));
        } else {
            cpsrds01Entity.setEnd(sdfE.format(cds.getTime()));
            cpsrds01Entity.setStart(sdfE.format(cde.getTime()));
        }

        return cpsrds01Entity;
    }

    /**
     * append File Name
     * 
     * @param officeCode officeCode
     * @param busPattern busPattern
     * @param byMultiParts byMultiParts
     * @param bySinglePart bySinglePart
     * @param fromDate fromDate
     * @param toDate toDate
     * @param lang Language
     * @return strList strList
     */
    public List<String> appendFileName(String officeCode, Integer busPattern, boolean byMultiParts,
        boolean bySinglePart, Date fromDate, Date toDate, Language lang) {

        // define file names
        List<String> filesName = new ArrayList<String>();
        
        // for from date to end date
        Date loopDate = fromDate;
        while (!loopDate.after(toDate)) {
            
            // prepare stock date
            Date stockDate = DateTimeUtil.addDay(loopDate, IntDef.INT_N_ONE);
            
            // if has all parts
            if (byMultiParts) {
                
                // add all parts file 
                filesName.add(this.getZippedFileName(officeCode, busPattern, stockDate, RundownType.ALL_PARTS, lang));
            }
            
            // if has all parts
            if (bySinglePart) {
                // add all parts file 
                filesName.add(this.getZippedFileName(officeCode, busPattern, stockDate, RundownType.SINGLE_PARTS, lang));
            }
            
            // next date
            loopDate = DateTimeUtil.addDay(loopDate, IntDef.INT_ONE);
        }
        
        // return all names
        return filesName;
    }

    /**
     * Prepare file save path.
     * 
     * @param officeCode the office
     * @param businessPattern BusinessPattern
     * @param stockDate stockDate
     * @param rdType rudnown type
     * @param lang Language
     * @return zip file path
     */
    private String getZippedFileName(String officeCode, Integer businessPattern, Date stockDate, String rdType,
        Language lang) {
        // do nothing in base
        // get file path
        StringBuffer fullName = new StringBuffer();
        fullName.append(ConfigManager.getBatchFileSavePath());
        fullName.append(File.separatorChar);
        fullName.append(officeCode.replaceAll(StringConst.COLON, StringConst.EMPTY));
        fullName.append(StringConst.UNDERLINE);
        fullName.append(CodeCategoryManager.getCodeName(Language.ENGLISH.getCode(),
            CodeMasterCategory.BUSINESS_PATTERN, businessPattern));
        fullName.append(StringConst.UNDERLINE);
        fullName.append(rdType);
        fullName.append(StringConst.UNDERLINE);
        fullName.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, stockDate));
        fullName.append(StringConst.UNDERLINE);
        fullName.append(lang.getName());
        fullName.append(StringConst.DOT);
        fullName.append(FileType.ZIP.getSuffix());

        // return full name
        return fullName.toString();
    }

}
