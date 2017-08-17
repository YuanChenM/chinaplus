/**
 * Read File
 *
 * @author liu_yinchuan
 */
package com.chinaplus.batch.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.util.StringUtil;

/**
 * TextReader.
 * 
 * @author yang_jia1
 */
public abstract class TxtReader {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(TxtReader.class);

    /**
     * read File.
     * 
     * @param filePath file Path
     * @return List
     * @throws BatchException e
     */
    public static List<String> readInvoiceFile(String filePath) throws BatchException {

        logger.info(String.format("Read file [%s] start.", filePath));
        
        // get file
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            logger.error(String.format("File patch [%s] is incorrect.", new Object[] { filePath }));
            throw new BatchException();
        }

        // read file
        BufferedReader reader = null;
        FileInputStream inputStream = null;
        List<String> invoiceList = new ArrayList<String>();

        try {

            // file input stream
            inputStream = new FileInputStream(file);

            // reader file // user defulat UTF-8
            reader = new BufferedReader(new FileReader(file));

            // read
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {

                // if blank, next
                if (StringUtil.isNullOrEmpty(tempString)) {
                    continue;
                }

                // set into List
                invoiceList.add(tempString.trim());
            }
        } catch (Exception e) {
            logger.error(String.format("Read File [%s] failtrue.", new Object[] { filePath }));
            logger.error(e.getMessage());
            // change to BatchException
            throw new BatchException();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }

            // end
            logger.info(String.format("Read file [%s] end.", file.getName()));
        }

        return invoiceList;
    }

}
