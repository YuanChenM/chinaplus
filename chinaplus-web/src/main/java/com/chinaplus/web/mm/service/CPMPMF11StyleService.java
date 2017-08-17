/**
 * @screen CPMPMF11
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;

/**
 * CPMPMF11StyleService.
 */
@Service
public class CPMPMF11StyleService extends BaseService {

    /**
     * check Static style
     * 
     * @param sheet this sheet
     * @param type type
     * @return boolean
     */
    public boolean checkStatic(Sheet sheet, int type) {
        // staticcheck
        String disyRealTimeData = MessageManager.getMessage("CPMPMF01_Grid_DisplayRealTimeData",
            Language.CHINESE.getLocale());

        String row6c4 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_FOUR);
        String row6c4in = MessageManager.getMessage("CPMPMF01_Grid_PartsDescriptionEn", Language.CHINESE.getLocale());

        // String row6c6 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_SIX);
        // String row7c6 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SEVEN, IntDef.INT_SIX);

        String row6c7 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_SEVEN);
        String row6c7in = MessageManager.getMessage("CPMPMF01_Grid_UOM", Language.CHINESE.getLocale());

        String row6c8 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_EIGHT);
        String row6c8in = MessageManager.getMessage("CPMPMF01_Grid_ExportCountry", Language.CHINESE.getLocale());

        String row6c11 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_ELEVEN);
        String row6c11in = MessageManager.getMessage("CPMPMF01_Grid_SSMSVendorRouteBr", Language.CHINESE.getLocale());
        row6c11in = row6c11in.replace("<br>", "\n");

        // String row6c12 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_TWELVE);
        // String row7c12 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SEVEN, IntDef.INT_TWELVE);

        String row6c20 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_TWENTY);
        String row6c20in = MessageManager.getMessage("CPMPMF01_Grid_CustPN", Language.CHINESE.getLocale());

        String row6c26 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_TWENTY_SIX);
        String row6c26in = MessageManager.getMessage("CPMPMF01_Grid_OrderLot", Language.CHINESE.getLocale());

        String row6c28 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_TWENTY_EIGHT);
        String row6c28in = MessageManager.getMessage("CPMPMF01_Grid_SPQ", Language.CHINESE.getLocale());

        // String row6c48 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_FOURTY_EIGTH);
        // String row7c48 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SEVEN, IntDef.INT_FOURTY_EIGTH);

        String row6c63 = PoiUtil.getStringCellValue(sheet, IntDef.INT_SIX, IntDef.INT_SIXTYTHREE);
        String row6c63in = MessageManager.getMessage("CPMPMF01_Grid_BuildOutIndicator", Language.CHINESE.getLocale());

        if (type == BusinessPattern.V_V) {
            row6c4in = row6c4in + disyRealTimeData;
            row6c7in = row6c7in + disyRealTimeData;
            row6c8in = row6c8in + disyRealTimeData;
            row6c11in = row6c11in + disyRealTimeData;
            row6c20in = row6c20in + disyRealTimeData;
            row6c26in = row6c26in + disyRealTimeData;
            row6c28in = row6c28in + disyRealTimeData;
            row6c63in = row6c63in + disyRealTimeData;

            // String row6c6in = MessageManager.getMessage("CPMPMF01_Grid_OldTTCPN", Language.CHINESE.getLocale());
            // if (!row6c6in.equals(row6c6)) {
            // return false;
            // }
            // String row7c6in = MessageManager.getMessage("CPMPMF01_Grid_OldTTCPN", Language.ENGLISH.getLocale());
            // if (!row7c6in.equals(row7c6)) {
            // return false;
            // }

            // String row6c12in = MessageManager.getMessage("CPMPMF01_Grid_SSMSSuppCd", Language.CHINESE.getLocale());
            // if (!row6c12in.equals(row6c12)) {
            // return false;
            // }
            // String row7c12in = MessageManager.getMessage("CPMPMF01_Grid_SSMSSuppCd", Language.ENGLISH.getLocale());
            // if (!row7c12in.equals(row7c12)) {
            // return false;
            // }

            // String row6c48in = MessageManager.getMessage("CPMPMF01_Grid_TTCImpWHObFlucPerc",
            // Language.CHINESE.getLocale());
            // if (!row6c48in.equals(row6c48)) {
            // return false;
            // }
            // String row7c48in = MessageManager.getMessage("CPMPMF01_Grid_TTCImpWHObFlucPerc",
            // Language.ENGLISH.getLocale());
            // if (!row7c48in.equals(row7c48)) {
            // return false;
            // }
        }
        // else if (type == BusinessPattern.AISIN) {
        // String row6c6in = MessageManager.getMessage("CPMPMF01_Grid_OldPN", Language.CHINESE.getLocale());
        // if (!row6c6in.equals(row6c6)) {
        // return false;
        // }
        // String row7c6in = MessageManager.getMessage("CPMPMF01_Grid_OldPN", Language.ENGLISH.getLocale());
        // if (!row7c6in.equals(row7c6)) {
        // return false;
        // }

        // String row6c12in = MessageManager.getMessage("CPMPMF01_Grid_TTCSuppCd", Language.CHINESE.getLocale());
        // if (!row6c12in.equals(row6c12)) {
        // return false;
        // }
        // String row7c12in = MessageManager.getMessage("CPMPMF01_Grid_TTCSuppCd", Language.ENGLISH.getLocale());
        // if (!row7c12in.equals(row7c12)) {
        // return false;
        // }

        // String row6c48in = MessageManager.getMessage("CPMPMF01_Grid_ObFlucPerc", Language.CHINESE.getLocale());
        // if (!row6c48in.equals(row6c48)) {
        // return false;
        // }
        // String row7c48in = MessageManager.getMessage("CPMPMF01_Grid_ObFlucPerc", Language.ENGLISH.getLocale());
        // if (!row7c48in.equals(row7c48)) {
        // return false;
        // }
        // }
        if (!row6c4in.equals(row6c4)) {
            return false;
        }
        if (!row6c7in.equals(row6c7)) {
            return false;
        }
        if (!row6c8in.equals(row6c8)) {
            return false;
        }
        if (!row6c11in.equals(row6c11)) {
            return false;
        }
        if (!row6c20in.equals(row6c20)) {
            return false;
        }
        if (!row6c26in.equals(row6c26)) {
            return false;
        }
        if (!row6c28in.equals(row6c28)) {
            return false;
        }
        if (!row6c63in.equals(row6c63)) {
            return false;
        }
        return true;
    }

    /**
     * check must Alphameric
     * 
     * @param value value
     * @param size size
     * @param itemName itemName
     * @param rowNum rowNum
     * @param sheetName sheetName
     * @return messageLists messageLists
     */
    public List<BaseMessage> checkAlphameric(String value, int size, String itemName, String rowNum, String sheetName) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        if (!ValidatorUtils.requiredValidator(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName });
            messageLists.add(message);
        }
        // else if (!ValidatorUtils.isAlphameric(value)) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
        // message.setMessageArgs(new String[] { rowNum, sheetName, itemName, "CPMPMF01_Grid_Alphanumeric" });
        // messageLists.add(message);
        // }
        else if (!ValidatorUtils.maxLengthValidator(value, size)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_144);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, StringUtil.toSafeString(size) });
            messageLists.add(message);
        }
        return messageLists;
    }

    /**
     * check not must Alphameric
     * 
     * @param value value
     * @param size size
     * @param itemName itemName
     * @param rowNum rowNum
     * @param sheetName sheetName
     * @return messageLists messageLists
     */
    public List<BaseMessage> checkAlphamericIsNeed(String value, int size, String itemName, String rowNum,
        String sheetName) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();

        // if (!ValidatorUtils.isAlphameric(value)) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
        // message.setMessageArgs(new String[] { rowNum, sheetName, itemName, "CPMPMF01_Grid_Alphanumeric" });
        // messageLists.add(message);
        // } else
        if (!ValidatorUtils.maxLengthValidator(value, size)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_144);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, StringUtil.toSafeString(size) });
            messageLists.add(message);
        }
        return messageLists;
    }

    /**
     * check date
     * 
     * @param date date
     * @param itemName itemName
     * @param rowNum rowNum
     * @param sheetName sheetName
     * @return messageLists messageLists
     */
    public List<BaseMessage> checkDate(Date date, String itemName, String rowNum, String sheetName) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        if (!ValidatorUtils.requiredValidator(date)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, "CPMSRF11_Grid_Date" });
            messageLists.add(message);
        }
        return messageLists;
    }

    /**
     * check Integer
     * 
     * @param value value
     * @param size size
     * @param itemName itemName
     * @param rowNum rowNum
     * @param sheetName sheetName
     * @return messageLists messageLists
     */
    public List<BaseMessage> checkInteger(String value, int size, String itemName, String rowNum, String sheetName) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        if (!ValidatorUtils.requiredValidator(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName });
            messageLists.add(message);
        } else if (!StringUtil.isNumeric(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, "CPMSRF11_Grid_Integer" });
            messageLists.add(message);
        } else if (!ValidatorUtils.maxLengthValidator(value, size)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_144);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, StringUtil.toSafeString(size) });
            messageLists.add(message);
        }
        return messageLists;
    }

    /**
     * check Number
     * 
     * @param value value
     * @param itemName itemName
     * @param rowNum rowNum
     * @param sheetName sheetName
     * @return messageLists messageLists
     */
    public List<BaseMessage> checkNumber(String value, String itemName, String rowNum, String sheetName) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        if (!ValidatorUtils.requiredValidator(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName });
            messageLists.add(message);
        } else if (!ValidatorUtils.checkDecimal(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, "CPMPMF01_Grid_Number" });
            messageLists.add(message);
        } else if (!ValidatorUtils.checkMaxDecimal(new BigDecimal(value))) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_043);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, StringUtil.toSafeString(IntDef.INT_TEN),
                StringUtil.toSafeString(IntDef.INT_SIX) });
            messageLists.add(message);
        }
        return messageLists;
    }

    /**
     * check Number
     * 
     * @param value value
     * @param itemName itemName
     * @param rowNum rowNum
     * @param sheetName sheetName
     * @return messageLists messageLists
     */
    public List<BaseMessage> checkNumberIsNeed(String value, String itemName, String rowNum, String sheetName) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        if (!ValidatorUtils.checkDecimal(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, "CPMPMF01_Grid_Number" });
            messageLists.add(message);
        } else if (!ValidatorUtils.checkMaxDecimal(new BigDecimal(value))) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_043);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, StringUtil.toSafeString(IntDef.INT_TEN),
                StringUtil.toSafeString(IntDef.INT_SIX) });
            messageLists.add(message);
        }
        return messageLists;
    }

    /**
     * check Percentage
     * 
     * @param value value
     * @param itemName itemName
     * @param rowNum rowNum
     * @param sheetName sheetName
     * @return messageLists messageLists
     */
    public List<BaseMessage> checkPercentage(String value, String itemName, String rowNum, String sheetName) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        if (!ValidatorUtils.requiredValidator(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName });
            messageLists.add(message);
        } else if (!ValidatorUtils.isPercentage(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, "CPMPMF01_Grid_Percentage" });
            messageLists.add(message);
        } else if (value.startsWith(StringConst.MIDDLE_LINE)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, "0" });
            messageLists.add(message);
        } else if (!ValidatorUtils.maxLengthValidator(value, IntDef.INT_FOUR)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_144);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName,
                StringUtil.toSafeString(IntDef.INT_THREE) });
            messageLists.add(message);
        }
        return messageLists;
    }
}
