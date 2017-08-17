/**
 * Common run shell.
 * 
 * @screen common
 * @author shiyang
 */
package com.chinaplus.common.util;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.core.util.ConfigUtil;

/**
 * Run Shell Utility
 */
public class RunShellManager {

    /**
     * 
     * The Constructors Method.
     *
     */
    public RunShellManager() {}

    /**
     * Main
     * 
     * @param args param
     */
    public static void main(String[] args) {
        try {
            runShell("test.sh", "A", "B", "C");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run shell
     * 
     * @param shellName shell name
     * @param params params
     * @return result
     * @throws Exception Exception
     */
    public static List<String> runShell(String shellName, String... params) throws Exception {
        // Get paramter for running command
        String paramter = "";
        for (String param : params) {
            paramter = paramter + " " + param;
        }

        // Get shell path
        String shellPath = ConfigUtil.get(ChinaPlusConst.Properties.RUN_SHELL_PATH);

        // Get command
        String command = shellPath + shellName + paramter;
        // test source start
        // String command = "E://" + shellName + paramter;
        // String command = "/tmp/runShellTest/" + shellName + paramter;
        // test source end

        // Run shell
        Process p = Runtime.getRuntime().exec(command);
        // Run shell (for Linux only)
        // Process p = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command }, null, null);

        // Get the result
        InputStreamReader ir = new InputStreamReader(p.getInputStream());
        LineNumberReader input = new LineNumberReader(ir);
        String line;
        p.waitFor();
        List<String> resultList = new ArrayList<String>();
        while ((line = input.readLine()) != null) {
            resultList.add(line);
        }

        // Output log
        File file = new File(shellPath + "RunShell.log");
        FileUtils.writeLines(file, resultList);
        return resultList;
    }
}
