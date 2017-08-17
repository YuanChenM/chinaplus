/**
 * BatchUtils.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * MainService.
 * 
 * @author yang_jia1
 */
public class BatchUtils {

    /**
     * distinctList
     * 
     * @param T T
     * 
     */
    public static void sortList(List<Timestamp> T) {

        for (int i = 0; i < T.size(); i++) {
            for (int j = i; j < T.size(); j++) {
                if (T.get(i).after(T.get(j))) {
                    Timestamp temp = T.get(i);
                    T.set(i, T.get(j));
                    T.set(j, temp);
                }
            }
        }

    }

    /**
     * distinctList
     * 
     * @param T T
     * @return List<String>
     */
    public static List<Timestamp> distinctList(List<Timestamp> T) {
        List<Timestamp> listTemp = new ArrayList<Timestamp>();
        Iterator<Timestamp> it = T.iterator();
        while (it.hasNext()) {
            Timestamp a = it.next();
            if (listTemp.contains(a)) {
                it.remove();
            } else {
                listTemp.add(a);
            }
        }
        return listTemp;
    }

    /**
     * distinctList
     * 
     * @param T T
     * @return boolean
     */
    public static boolean listContainsRepeat(List<String> T) {
        List<String> listTemp = new ArrayList<String>();
        Iterator<String> it = T.iterator();
        while (it.hasNext()) {
            String a = it.next();
            if (listTemp.contains(a)) {
                return true;
            } else {
                listTemp.add(a);
            }
        }
        return false;
    }

    /**
     * ifContainsDateTime
     * 
     * @param T T
     * @return boolean
     */
    public static boolean ifContainsDateTime(List<String> T) {

        List<String> listTemp = new ArrayList<String>();
        Iterator<String> it = T.iterator();
        while (it.hasNext()) {
            String a = it.next();
            if (listTemp.contains(a)) {
                return true;
            } else {
                listTemp.add(a);
            }
        }
        return false;
    }

}
