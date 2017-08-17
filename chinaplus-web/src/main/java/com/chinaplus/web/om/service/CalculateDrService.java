/**
 * CalculateDrService.java
 * 
 * @screen COMMON
 * @author shi_yuxi
 */
package com.chinaplus.web.om.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.OrderForecastType;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CalculateDrEntity;

/**
 * Calculate DR Service.(No Combine, Daily)
 */
@Service
public class CalculateDrService extends BaseService {

    /**
     * Calculate DR Logic.
     * 
     * @param partsLst part info
     * @param calendarLst calendar
     * @param allocationType allocation Type: 1(Daily);
     * @return allocation result
     */
    public Map<Integer, List<CalculateDrEntity>> calcDr(List<CalculateDrEntity> partsLst,
        List<CalculateDrEntity> calendarLst, String allocationType) {

        Map<Integer, List<CalculateDrEntity>> rstMap = null;
        if (null != partsLst && !partsLst.isEmpty() && null != calendarLst && !calendarLst.isEmpty()
                && !StringUtil.isEmpty(allocationType)) {

            rstMap = new HashMap<Integer, List<CalculateDrEntity>>();
            // allocation working days
            BigDecimal workingDays = null;

            if (OrderForecastType.DAILY.equals(allocationType)) {
                // Daily
                workingDays = new BigDecimal(calendarLst.size());
            } else {
                // TODO for future
                return rstMap;
            }

            BigDecimal totalBoxes = new BigDecimal(0);
            BigDecimal totalRemainder1 = new BigDecimal(0);
            BigDecimal totalDailyBoxes = new BigDecimal(0);
            BigDecimal completedDailyBoxes = new BigDecimal(0);
            for (int i = 0; i < partsLst.size(); i++) {

                CalculateDrEntity partEntity = partsLst.get(i);

                partEntity.setSelectedCnt(0);
                BigDecimal partQty = partEntity.getPartQty();
                BigDecimal srbq = partEntity.getSrbq();
                // change Qty to boxes by srbq.
                BigDecimal partBoxes = partQty.divide(srbq, BigDecimal.ROUND_UP);
                partEntity.setPartBoxes(partBoxes);
                // total boxes
                totalBoxes = totalBoxes.add(partQty.divide(srbq, BigDecimal.ROUND_UP));

                // 1-1. Divide by number of working days and get remainder 1.
                BigDecimal[] calcBoxes = partBoxes.divideAndRemainder(workingDays);
                BigDecimal partDailyBoxes = calcBoxes[0];
                BigDecimal remainder = calcBoxes[1];
                partEntity.setRemainder1(remainder);
                totalDailyBoxes = totalDailyBoxes.add(partDailyBoxes);
                completedDailyBoxes = completedDailyBoxes.add(partDailyBoxes);
                totalRemainder1 = totalRemainder1.add(remainder);

                // create result
                List<CalculateDrEntity> outCalendarLst = new ArrayList<CalculateDrEntity>();
                for (int j = 0; j < calendarLst.size(); j++) {

                    CalculateDrEntity calEntity = calendarLst.get(j);
                    CalculateDrEntity outCalEntity = new CalculateDrEntity();
                    outCalEntity.setCalendarDate(calEntity.getCalendarDate());
                    outCalEntity.setDrQty(partDailyBoxes.multiply(srbq));
                    outCalEntity.setDailyBoxes(partDailyBoxes);
                    outCalEntity.setSrbq(srbq);
                    outCalEntity.setPartQty(partQty);
                    outCalendarLst.add(outCalEntity);
                }
                rstMap.put(partEntity.getPart(), outCalendarLst);
            }

            // 1-2. Divide remainder 1 by number of working days and get remainder 2.
            if (totalRemainder1.compareTo(new BigDecimal(0)) > 0) {

                BigDecimal[] calcBoxes = totalRemainder1.divideAndRemainder(workingDays);
                BigDecimal partDailyBoxes = calcBoxes[0];
                BigDecimal totalRemainder2 = calcBoxes[1];
                totalDailyBoxes = totalDailyBoxes.add(partDailyBoxes);

                // 2-1. Sum table 1- 2 and 1- 3 to show the daily distribution of Remainder 1.
                List<BigDecimal> unallocatedDailyBoxes = new ArrayList<BigDecimal>();

                if (totalRemainder2.compareTo(new BigDecimal(0)) > 0) {

                    // 1-3. To divide remainder 2 equally for each working days, use the formula below:
                    // ・ Calculate X & Y for each working day
                    // X = (Day N) / total no of working days * remainder2
                    // Y = Round half up value of X
                    // ・ Place Boxes on 1st appearance of each new value Y
                    for (int i = 0; i < calendarLst.size() - 1; i++) {

                        BigDecimal calcRatio = totalRemainder2.multiply(new BigDecimal(i + 1)).divide(workingDays,
                            BigDecimal.ROUND_HALF_UP);
                        BigDecimal calcCompRate = totalRemainder2.multiply(new BigDecimal(i + 2)).divide(workingDays,
                            BigDecimal.ROUND_HALF_UP);

                        // 1-4. Sum results of Table 1- 1, 1- 2, and 1- 3 to get number of boxes per day.
                        // 2-1. Sum table 1- 2 and 1- 3 to show the daily distribution of Remainder 1.
                        if (i == 0) {
                            if (calcRatio.compareTo(new BigDecimal(1)) < 0) {
                                calendarLst.get(i).setDailyBoxes(totalDailyBoxes);
                                unallocatedDailyBoxes.add(partDailyBoxes);
                            } else {
                                calendarLst.get(i).setDailyBoxes(DecimalUtil.add(totalDailyBoxes, new BigDecimal(1)));
                                unallocatedDailyBoxes.add(DecimalUtil.add(partDailyBoxes, new BigDecimal(1)));
                            }
                        }
                        if (calcCompRate.compareTo(calcRatio) > 0) {
                            calendarLst.get(i + 1).setDailyBoxes(DecimalUtil.add(totalDailyBoxes, new BigDecimal(1)));
                            unallocatedDailyBoxes.add(DecimalUtil.add(partDailyBoxes, new BigDecimal(1)));
                        } else {
                            calendarLst.get(i + 1).setDailyBoxes(totalDailyBoxes);
                            unallocatedDailyBoxes.add(partDailyBoxes);
                        }
                    }
                } else {

                    // 1-4. Sum results of Table 1- 1, 1- 2, and 1- 3 to get number of boxes per day.
                    for (int i = 0; i < calendarLst.size(); i++) {
                        calendarLst.get(i).setDailyBoxes(totalDailyBoxes);
                        unallocatedDailyBoxes.add(partDailyBoxes);
                    }
                }

                // sort partLst by boxes DESC,partNo. ASC
                Collections.sort(partsLst, new Comparator<CalculateDrEntity>() {
                    @Override
                    public int compare(CalculateDrEntity entity1, CalculateDrEntity entity2) {

                        int result = entity2.getPartBoxes().compareTo(entity1.getPartBoxes());
                        if (result == 0) {
                            result = entity1.getPartNo().compareTo(entity2.getPartNo());
                        }
                        return result;
                    }
                });

                // 2-2. Sum table 1- 2 and 1- 3 to show the daily distribution of Remainder 1.
                // ・ Calculate appearance ratio = No of boxes per part/ Total no of boxes
                // ・ The first value is equal to the appearance ratio
                // ・ The selected box is the box with the highest ratio in each column.
                // ・ For the next value, the ratio is calculated by:
                // IF the part was selected, Next ratio = Current ratio + Appearance ratio – 1
                // IF the part was NOT selected, Next ratio = Current ratio + Appearance ratio
                List<Integer> calcPartsLst = new ArrayList<Integer>();
                for (int i = 0; i < totalRemainder1.intValue(); i++) {

                    BigDecimal maxRatio = new BigDecimal(0);
                    int pos = 0;
                    for (int j = 0; j < partsLst.size(); j++) {

                        CalculateDrEntity partEntity = partsLst.get(j);
                        BigDecimal calcRatio = partEntity.getRemainder1().multiply(new BigDecimal(i + 1))
                            .divide(totalRemainder1, 5, BigDecimal.ROUND_DOWN)
                            .subtract(new BigDecimal(partEntity.getSelectedCnt()));

                        partEntity.setCalcRatio(calcRatio);
                        if (maxRatio.compareTo(partEntity.getCalcRatio()) < 0) {
                            maxRatio = calcRatio;
                            pos = j;
                        }
                    }
                    partsLst.get(pos).setCalcRatio(partsLst.get(pos).getCalcRatio().subtract(new BigDecimal(1)));
                    partsLst.get(pos).setSelectedCnt(partsLst.get(pos).getSelectedCnt() + 1);
                    calcPartsLst.add(partsLst.get(pos).getPart());
                }

                // 2-3. Allocated parts of Remainder 1 is to the working days from 2 - 1 & 2 - 2
                // 2-4. Sum Table 2- 3 & 1- 1 to get the total part Boxes allocated for each working day.
                int index = 0;
                for (int i = 0; i < unallocatedDailyBoxes.size(); i++) {

                    int dailyBoxes = unallocatedDailyBoxes.get(i).intValue();
                    for (int j = 0 + index; j < dailyBoxes + index; j++) {

                        CalculateDrEntity dailyEntity = rstMap.get(calcPartsLst.get(j)).get(i);
                        BigDecimal allocatedBoxes = dailyEntity.getDailyBoxes();
                        BigDecimal allocatedDrQty = dailyEntity.getDrQty();
                        dailyEntity.setDailyBoxes(allocatedBoxes.add(new BigDecimal(1)));
                        dailyEntity.setDrQty(allocatedDrQty.add(dailyEntity.getSrbq()));
                    }
                    index += dailyBoxes;
                }
            }

            // calculate the Qty of the last day
            for (Map.Entry<Integer, List<CalculateDrEntity>> rst : rstMap.entrySet()) {

                List<CalculateDrEntity> drLst = rst.getValue();
                CalculateDrEntity drEntity = drLst.get(drLst.size() - 1);
                BigDecimal partBoxes = drEntity.getPartQty().divide(drEntity.getSrbq(), BigDecimal.ROUND_UP);
                // TotalQty - (TotalBoxes - 1) * srbq + (boxes of last day - 1) * srbq
                BigDecimal reAllocateQty = drEntity.getPartQty()
                        .subtract(drEntity.getSrbq().multiply(partBoxes.subtract(new BigDecimal(1))))
                        .add(drEntity.getDailyBoxes().subtract(new BigDecimal(1)).multiply(drEntity.getSrbq()));
                for (int i = drLst.size() - 1; i >= 0; i--) {
                    CalculateDrEntity reAllocateEntity = drLst.get(i);
                    if (reAllocateEntity.getDrQty().intValue() > 0) {
                        if (reAllocateQty.intValue() < 0) {
                            reAllocateEntity.setDrQty(reAllocateEntity.getDrQty().add(reAllocateQty));
                        } else {
                            reAllocateEntity.setDrQty(reAllocateQty);
                        }
                        break;
                    }
                }
            }
        }

        return rstMap;
    }
}