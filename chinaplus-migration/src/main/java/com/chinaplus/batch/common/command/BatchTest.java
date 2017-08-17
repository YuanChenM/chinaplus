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

        //String[] as = new String[] { "ImpIpSync", "CN:TTTJ"};
        String[] as = new String[] { "InvoiceSyn", "CN:TTTJ"};

        //String[] as = new String[] { "IPReader", "D:\\test\\IP", "CN:TTGC" };
        //String[] as = new String[] { "InvReader", "D:\\test\\IP", "CN:TTTJ" };

        StartBatchMain.main(as);
    }
}
