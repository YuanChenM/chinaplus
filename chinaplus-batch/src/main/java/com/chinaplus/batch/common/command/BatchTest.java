package com.chinaplus.batch.common.command;


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
        
//         String[] as1 = new String[] { "CPIIFB17", "D:\\Project\\Chinaplus\\InterfaceData\\InterfacePath",
//         "D:\\Project\\Chinaplus\\InterfaceData\\WorkingPath", "D:\\Project\\Chinaplus\\InterfaceData\\BackupPath" };
//         StartBatchMain.main(as1);
//        
//         String[] as2 = new String[] { "CPIIFB15", "D:\\Project\\Chinaplus\\InterfaceData\\WorkingPath" };
//         StartBatchMain.main(as2);
//        
//         String[] as3 = new String[] { "CPIIFB01", "1" };
//         StartBatchMain.main(as3);

//         String[] as4 = new String[] { "CPIIFB18", "D:\\Project\\Chinaplus\\InterfaceData\\InterfacePath",
//         "D:\\Project\\Chinaplus\\InterfaceData\\WorkingPath", "D:\\Project\\Chinaplus\\InterfaceData\\BackupPath",
//         "MM15X4V1" };
//         StartBatchMain.main(as4);
//        
//         String[] as5 = new String[] { "CPIIFB16", "D:\\Project\\Chinaplus\\InterfaceData\\WorkingPath" };
//         StartBatchMain.main(as5);
//        
//         String[] as6 = new String[] { "CPIIFB02", "1,2", "CN:TTTJ", "20160712" };
//         StartBatchMain.main(as6);

        String[] as7 = new String[] { "CPIIFB11", "20160712", "CN:TTTJ", "CPIIFB12,CPIIFB13,CPIIFB14" };
        StartBatchMain.main(as7);

        String[] as8 = new String[] { "CPSRDB01", "1", "20160712", "CN:TTTJ" };
        StartBatchMain.main(as8);

        String[] as9 = new String[] { "CPSSSB01", "1", "20160712", "CN:TTTJ" };
        StartBatchMain.main(as9);
    }
}
