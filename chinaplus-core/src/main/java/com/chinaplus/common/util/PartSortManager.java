/**
 * PartSortManager.java
 * 
 * @screen common
 * @author gu_chengchen
 */
package com.chinaplus.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.StringUtil;

/**
 * Part Sort Manager.
 */
public class PartSortManager {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(PartSortManager.class);

    /**
     * The Constructors Method.
     */
    private PartSortManager() {

    }

    /**
     * Do parts sort.
     * 
     * @param partsList parts list
     * @param partNoName TTC parts No. name
     * @param oldNoName old TTC parts No. name
     */
    public static void sort(List<? extends BaseEntity> partsList, final String partNoName, final String oldNoName) {

        sort(partsList, partNoName, oldNoName, true);
    }

    /**
     * Do parts sort.
     * 
     * @param partsList parts list
     * @param partNoName TTC parts No. name
     * @param oldNoName old TTC parts No. name
     * @param isNeedSort need sort flag
     */
    public static void sort(List<? extends BaseEntity> partsList, final String partNoName, final String oldNoName,
        boolean isNeedSort) {

        try {
            List<List<Object[]>> groupList = new ArrayList<List<Object[]>>();
            // Create group data
            for (int i = 0; i < partsList.size(); i++) {
                BaseEntity partsBean = partsList.get(i);
                String partsNo = BeanUtils.getProperty(partsBean, partNoName);
                String oldPartsNo = BeanUtils.getProperty(partsBean, oldNoName);
                List<Object[]> group = null;
                Integer oldPartsIndex = getOldPartsIndex(oldPartsNo, partsList, partNoName);
                BaseEntity oldPartsBean = null;
                if (oldPartsIndex != null) {
                    oldPartsBean = partsList.get(oldPartsIndex);
                }
                Integer sortGroupIdex = partsBean.getSortGroupIdex();
                if (sortGroupIdex != null) {
                    group = groupList.get(sortGroupIdex);
                } else {
                    List<Integer> processedIndexs = new ArrayList<Integer>();
                    processedIndexs.add(i);
                    sortGroupIdex = getSortGroupIndex(i, oldPartsIndex, partsList, partNoName, oldNoName,
                        processedIndexs);
                    if (sortGroupIdex != null) {
                        group = groupList.get(sortGroupIdex);
                        partsBean.setSortGroupIdex(sortGroupIdex);
                    }
                }
                if (group == null) {
                    group = new ArrayList<Object[]>();
                    groupList.add(group);
                    partsBean.setSortGroupIdex(groupList.size() - 1);
                }
                if (oldPartsBean != null) {
                    oldPartsBean.setSortGroupIdex(partsBean.getSortGroupIdex());
                }
                Object[] curGroupData = new Object[IntDef.INT_THREE];
                curGroupData[IntDef.INT_ZERO] = i;
                curGroupData[IntDef.INT_ONE] = oldPartsIndex;
                curGroupData[IntDef.INT_TWO] = partsNo;
                group.add(curGroupData);
            }

            // Sort for every group
            for (List<Object[]> group : groupList) {
                // Confirm group type
                String separator = "#;!";
                List<String> groupNoList = new ArrayList<String>();
                for (Object[] groupData : group) {
                    int sortGroupType = 0;
                    Integer curIndex = (Integer) groupData[IntDef.INT_ZERO];
                    Integer curOldIndex = (Integer) groupData[IntDef.INT_ONE];
                    String curPartsNo = (String) groupData[IntDef.INT_TWO];
                    if (curOldIndex == null) {
                        sortGroupType = GroupType.HIGH;
                    } else {
                        List<Integer> newParts = getNewParts(curIndex, group);
                        if (newParts.size() == 0) {
                            sortGroupType = GroupType.LOW;
                        } else {
                            // if (isMidHighData(newParts, group)) {
                            // sortGroupType = GroupType.MID_HIGH;
                            // } else {
                            // sortGroupType = GroupType.MID_LOW;
                            // }
                            List<Integer> processedIndexs = new ArrayList<Integer>();
                            processedIndexs.add(curIndex);
                            sortGroupType = GroupType.MID + getOldStep(curOldIndex, group, 1, processedIndexs);
                        }
                    }

                    BaseEntity curPartsBean = partsList.get(curIndex);
                    curPartsBean.setSortGroupType(sortGroupType);
                    groupNoList.add(sortGroupType + separator + curPartsNo);
                }

                // Confirm group part No.
                Collections.sort(groupNoList);
                String sortGroupNo = groupNoList.get(groupNoList.size() - 1).split(separator)[1];
                for (Object[] groupData : group) {
                    Integer curIndex = (Integer) groupData[IntDef.INT_ZERO];
                    BaseEntity curPartsBean = partsList.get(curIndex);
                    curPartsBean.setSortGroupNo(sortGroupNo);
                }
            }

            if (isNeedSort) {
                // Do finally sort
                Collections.sort(partsList, new Comparator<BaseEntity>() {
                    @Override
                    public int compare(BaseEntity bean1, BaseEntity bean2) {
                        int res = bean1.getSortGroupNo().compareTo(bean2.getSortGroupNo());
                        if (res == 0) {
                            res = bean1.getSortGroupType().compareTo(bean2.getSortGroupType());
                            if (res == 0) {
                                try {
                                    String ttcPartsNo1 = BeanUtils.getProperty(bean1, partNoName);
                                    String ttcPartsNo2 = BeanUtils.getProperty(bean2, partNoName);
                                    res = ttcPartsNo1.compareTo(ttcPartsNo2);
                                } catch (Exception e) {
                                    res = 0;
                                }
                            }
                        }
                        return res;
                    }
                });
            }
        } catch (Exception e) {
            // do nothing
            logger.warn("Sort parts error.", e);
        }
    }

    /**
     * Get old step.
     * 
     * @param curOldIndex current old index
     * @param group group data
     * @param curSetp current step
     * @param processedIndexs processed index
     * @return old step
     */
    private static int getOldStep(Integer curOldIndex, List<Object[]> group, int curSetp, List<Integer> processedIndexs) {

        int step = curSetp;
        for (Object[] groupData : group) {
            Integer index = (Integer) groupData[0];
            Integer oldIndex = (Integer) groupData[1];
            if (curOldIndex.equals(index)) {
                if (processedIndexs.contains(index)) {
                    return step;
                } else {
                    processedIndexs.add(index);
                    step++;
                    if (oldIndex != null) {
                        return getOldStep(oldIndex, group, step, processedIndexs);
                    } else {
                        return step;
                    }
                }
            }
        }

        return step;
    }

    /**
     * Get sort group index.
     * 
     * @param curIndex current parts index
     * @param oldPartsIndex old parts index
     * @param partsList parts list
     * @param partNoName TTC parts No. name
     * @param oldNoName old TTC parts No. name
     * @param processedIndexs processed index
     * @return sort group index
     * @throws Exception the exception
     */
    private static Integer getSortGroupIndex(Integer curIndex, Integer oldPartsIndex,
        List<? extends BaseEntity> partsList, String partNoName, String oldNoName, List<Integer> processedIndexs)
        throws Exception {

        if (oldPartsIndex == null) {
            return null;
        } else {
            BaseEntity oldPartsBean = partsList.get(oldPartsIndex);
            if (oldPartsBean.getSortGroupIdex() != null) {
                return oldPartsBean.getSortGroupIdex();
            } else if (processedIndexs.contains(oldPartsIndex)) {
                return null;
            } else {
                processedIndexs.add(oldPartsIndex);
                String oldPartsNo = BeanUtils.getProperty(oldPartsBean, oldNoName);
                Integer oldOldPartsIndex = getOldPartsIndex(oldPartsNo, partsList, partNoName);
                return getSortGroupIndex(oldPartsIndex, oldOldPartsIndex, partsList, partNoName, oldNoName,
                    processedIndexs);
            }
        }
    }

    /**
     * Get old parts index.
     * 
     * @param oldTtcPartsNo old TTC parts No.
     * @param partsList parts list
     * @param partNoName TTC parts No. name
     * @return old parts index.
     * @throws Exception the exception
     */
    private static Integer getOldPartsIndex(String oldTtcPartsNo, List<? extends BaseEntity> partsList,
        String partNoName) throws Exception {

        if (StringUtil.isEmpty(oldTtcPartsNo)) {
            return null;
        }

        for (int i = 0; i < partsList.size(); i++) {
            BaseEntity partsBean = partsList.get(i);
            String ttcPartsNo = BeanUtils.getProperty(partsBean, partNoName);
            if (oldTtcPartsNo.equals(ttcPartsNo)) {
                return i;
            }
        }

        return null;
    }

    /**
     * Get new parts.
     * 
     * @param curIndex current index
     * @param group group data
     * @return new parts.
     */
    private static List<Integer> getNewParts(Integer curIndex, List<Object[]> group) {

        List<Integer> newParts = new ArrayList<Integer>();
        for (Object[] groupData : group) {
            Integer index = (Integer) groupData[0];
            Integer oldIndex = (Integer) groupData[1];
            if (oldIndex != null && oldIndex.equals(curIndex)) {
                newParts.add(index);
            }
        }

        return newParts;
    }

    // /**
    // * Check whether mid high data.
    // *
    // * @param newParts new parts
    // * @param group group data
    // * @return check result
    // */
    // private static boolean isMidHighData(List<Integer> newParts, List<Object[]> group) {
    //
    // for (Integer newPart : newParts) {
    // if (getNewParts(newPart, group).size() > 0) {
    // return true;
    // }
    // }
    //
    // return false;
    // }

    /**
     * Group Type.
     */
    private interface GroupType {

        /** High */
        public static final int HIGH = 1;

        /** Mid */
        public static final int MID = 2;

        // /** Mid High */
        // public static final int MID_HIGH = 2;
        //
        // /** Mid Low */
        // public static final int MID_LOW = 3;

        /** Low */
        public static final int LOW = 999;
    }

}
