/**
 * ZipCompressorUtil.java
 * 
 * @screen ZipCompressorUtil
 * @author zhang_chi
 */
package com.chinaplus.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

/**
 * ZipCompressorUtil.
 */
@Service
public class ZipCompressorUtil {

    static final int BUFFER = 8192;

    /**
     * compress file
     * 
     * @param fileLists fileLists
     * @param out OutputStream
     * @throws IOException IOException
     */
    public static void compress(File fileLists, OutputStream out) throws IOException {

        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(out);
            String basedir = "";
            if (!fileLists.exists()) {
                return;
            }
            File[] files = fileLists.listFiles();
            for (int i = 0; i < files.length; i++) {
                compress(files[i], zipOut, basedir);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (zipOut != null) {
                try {
                    zipOut.flush();
                    zipOut.close();
                } catch (IOException ex) {
                }
            }
        }
    }
    
    /**
     * compress file list
     * 
     * @param fileList fileList
     * @param out OutputStream
     * @throws IOException IOException
     */
    public static void compressList(List<String> fileList, OutputStream out) throws IOException {

        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(out);
            String basedir = "";
            for (int i = 0; i < fileList.size(); i++) {
                compress(new File(fileList.get(i)), zipOut, basedir);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (zipOut != null) {
                try {
                    zipOut.flush();
                    zipOut.close();
                } catch (IOException ex) {}
            }
        }
    }
    
    /**
     * compress file list
     * 
     * @param fileList fileList
     * @param out OutputStream
     * @throws IOException IOException
     */
    public static void compressFileList(List<File> fileList, OutputStream out) throws IOException {

        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(out);
            String basedir = "";
            for (int i = 0; i < fileList.size(); i++) {
                compress(fileList.get(i), zipOut, basedir);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (zipOut != null) {
                try {
                    zipOut.flush();
                    zipOut.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * compress
     * 
     * @param file file
     * @param out out
     * @param basedir basedir
     */
    private static void compress(File file, ZipOutputStream out, String basedir) {
        if (file.isDirectory()) {
            compressDirectory(file, out, basedir);
        } else {
            compressFile(file, out, basedir);
        }
    }

    /**
     * compress one directory
     * 
     * @param dir dir
     * @param out out
     * @param basedir basedir
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * compress one file
     * 
     * @param file file
     * @param out out
     * @param basedir basedir
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        BufferedInputStream bis =  null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte[] data = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {}
            }
        }
    }

}
