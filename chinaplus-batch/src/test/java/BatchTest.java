import com.chinaplus.batch.common.command.StartBatchMain;

/**
 * BatchTest.
 * 
 * @author liu_yinchuan
 */
public class BatchTest {

    /**
     * Main.
     * 
     * 
     * @param args args
     * 
     * @author liu_yinchuan
     */
    public static void main(String[] args) {

        // String[] as = new String[] { "CPIIFB15", "D:\\Project\\Chinaplus\\InterfaceData\\WorkingPath"};
        // String[] as = new String[] { "CPIIFB01", "1" };
        String[] as = new String[] { "CPIIFB02", "CN:TTSH", "20160506" };
        // String[] as = new String[] { "CPIIFB17", "D:\\Project\\Chinaplus\\InterfaceData\\InterfacePath",
        // "D:\\Project\\Chinaplus\\InterfaceData\\WorkingPath", "D:\\Project\\Chinaplus\\InterfaceData\\BackupPath" };
        // String[] as = new String[] { "CPIIFB18", "D:\\Project\\Chinaplus\\InterfaceData\\InterfacePath",
        // "D:\\Project\\Chinaplus\\InterfaceData\\WorkingPath", "D:\\Project\\Chinaplus\\InterfaceData\\BackupPath",
        // "JSP_IP.TW" };

        StartBatchMain.main(as);

        // Date from = new Date(2016, 2, 14);
        // Date to = new Date(2016, 1, 9);
        //
        // long dayDiff = DateTimeUtil.getDayDifferent(to, from);
        //
        // System.out.println(dayDiff);
    }
}
