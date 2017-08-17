/**
 * CodeCategoryManager.java
 *
 * @screen common.
 * @author zhang_pingwu
 */
package com.chinaplus.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.common.entity.TnmCodeCategory;
import com.chinaplus.common.service.TnmCodeCategoryService;

/**
 * CodeCategoryManager.
 *
 */
@Component
public class CodeCategoryManager {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CodeCategoryManager.class);

    private static Map<Integer, Map<String, Map<Integer, String>>> codeCategoryMap;

    @Autowired
    TnmCodeCategoryService tnmCodeCategoryService;

    /**
     * The Constructors Method.
     *
     */
    public CodeCategoryManager() {}

    /**
     * Initializer method. Get TnmCodeCategory information from database and store it in codeCategoryMap for further
     * use.
     *
     */
    @PostConstruct
    public void initCodeCategoryMap() {
        logger.debug("method initCodeCategoryMap is start");

        if (codeCategoryMap == null) {
            codeCategoryMap = new HashMap<Integer, Map<String, Map<Integer, String>>>();
        }

        List<TnmCodeCategory> codeCategoryList = tnmCodeCategoryService.findAll();

        for (TnmCodeCategory tnmCodeCategory : codeCategoryList) {

            Integer langFlag = tnmCodeCategory.getLanguageFlag();
            if (!codeCategoryMap.containsKey(langFlag)) {
                codeCategoryMap.put(langFlag, new HashMap<String, Map<Integer, String>>());
            }
            Map<String, Map<Integer, String>> tmpCodeInfoLanMap = codeCategoryMap.get(langFlag);
            String tmpCodeCategory = tnmCodeCategory.getCodeCategory();

            if (!tmpCodeInfoLanMap.containsKey(tmpCodeCategory)) {
                tmpCodeInfoLanMap.put(tmpCodeCategory, new HashMap<Integer, String>());
            }
            Map<Integer, String> tmpCodeInfoMap = tmpCodeInfoLanMap.get(tmpCodeCategory);
            Integer tmpCodeValue = tnmCodeCategory.getCodeValue();
            String tmpCodeName = tnmCodeCategory.getCodeName();
            tmpCodeInfoMap.put(tmpCodeValue, tmpCodeName);
        }

        logger.debug("method initCodeCategoryMap is end");
    }

    /**
     * Get code name by cade category and code value.
     *
     * @param lang Integer
     * @param codeCategory String
     * @param codeValue Integer
     * @return String
     *
     */
    public static String getCodeName(Integer lang, String codeCategory, Integer codeValue) {
        logger.debug("method getCodeName is start");
        Map<String, Map<Integer, String>> tmpCodeInfoLanMap = codeCategoryMap.get(lang);
        if (tmpCodeInfoLanMap == null) {
            logger.debug("method getCodeName is end");
            return null;
        }
        Map<Integer, String> codeInfoMap = tmpCodeInfoLanMap.get(codeCategory);
        if (codeInfoMap == null) {
            logger.debug("method getCodeName is end");
            return null;
        }
        String codeName = codeInfoMap.get(codeValue);
        logger.debug("method getCodeName is end");
        return codeName;
    }

    /**
     * Get code name by cade category
     *
     * @param lang Integer
     * @param codeCategory String
     * @return String
     *
     */
    public static String[] getCodeName(Integer lang, String codeCategory) {
        logger.debug("method getCodeName is start");
        Map<String, Map<Integer, String>> tmpCodeInfoLanMap = codeCategoryMap.get(lang);
        if (tmpCodeInfoLanMap == null) {
            logger.debug("method getCodeName is end");
            return null;
        }
        Map<Integer, String> codeInfoMap = tmpCodeInfoLanMap.get(codeCategory);
        if (codeInfoMap == null) {
            logger.debug("method getCodeName is end");
            return null;
        }
        String[] pos = new String[codeInfoMap.size()] ;
        int i = 0;
        for (Map.Entry<Integer, String> entry : codeInfoMap.entrySet()) {
            pos[i] = entry.getValue();
            i++;
        }
        logger.debug("method getCodeName is end");
        return pos;
    }

    /**
     * Get code value by code category and code name.
     *
     * @param lang Integer
     * @param codeCategory String
     * @param codeName String
     * @return Integer
     *
     */
    public static Integer getCodeValue(Integer lang, String codeCategory, String codeName) {
        logger.debug("method getCodeValue is start");
        if (codeName == null || codeName.length() == 0) {
            logger.debug("method getCodeValue is end");
            return null;
        }

        Map<String, Map<Integer, String>> tmpCodeInfoLanMap = codeCategoryMap.get(lang);
        if (tmpCodeInfoLanMap == null) {
            logger.debug("method getCodeValue is end");
            return null;
        }
        Map<Integer, String> codeInfoMap = tmpCodeInfoLanMap.get(codeCategory);
        if (codeInfoMap == null) {
            logger.debug("method getCodeValue is end");
            return null;
        }

        Integer codeValue = null;
        for (Map.Entry<Integer, String> entry : codeInfoMap.entrySet()) {
            if (codeName.equalsIgnoreCase(entry.getValue())) {
                logger.debug("method getCodeValue is end");
                codeValue = entry.getKey();
            }
        }

        return codeValue;
    }

    /**
     * Get CodeCategary By Language.
     * 
     * @param lang Integer
     * @return Map<String, Map<Integer, String>>
     */
    public static Map<String, Map<Integer, String>> getCodeCategaryByLang(Integer lang) {
        logger.debug("method getCodeCategaryByLang is start");
        Map<String, Map<Integer, String>> tmpCodeInfoLanMap = codeCategoryMap.get(lang);
        logger.debug("method getCodeCategaryByLang is end");
        return tmpCodeInfoLanMap;
    }

    /**
     * Get CodeCategary By Language.
     * 
     * @param lang Integer
     * @param codeCategory code Category
     * @return Map<String, Map<Integer, String>>
     */
    public static Map<Integer, String> getCodeCategaryByLang(Integer lang, String codeCategory) {
        logger.debug("method getCodeCategaryByLang is start");
        Map<String, Map<Integer, String>> tmpCodeInfoLanMap = codeCategoryMap.get(lang);
        if (tmpCodeInfoLanMap == null) {
            logger.debug("method getCodeName is end");
            return null;
        }
        // get from code map
        Map<Integer, String> codeInfoMap = tmpCodeInfoLanMap.get(codeCategory);
        logger.debug("method getCodeCategaryByLang is end");
        return codeInfoMap;
    }

}
