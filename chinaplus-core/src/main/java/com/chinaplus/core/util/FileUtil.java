package com.chinaplus.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * 文件操作辅助类
 * 
 * @author jiang_nan
 * 
 */
public final class FileUtil {

    /** 当前目录 */
    public static final String CURRENT_PATH = "./";
    /** logger */
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    /** 路径分隔符 */
    private static final String PATH_DIV = "/";
    /** 路径分隔符 */
    private static final String PATH_DOT = ".";
    /** csv file type */
    private static final String CSV_TYPE = "csv";

    /**
     * 构造方法
     */
    private FileUtil() {

    }

    /**
     * 
     * <p>
     * 检查目录是否存在
     * </p>
     * 
     * @param dir 目录
     * @return 目录存在返回true,目录不存在返回false
     */
    public static boolean checkDirExist(File dir) {
        boolean exist = false;
        if (dir != null) {
            exist = dir.exists();
        }
        return exist;
    }

    /**
     * 
     * <p>
     * 检查文件是否存在
     * </p>
     * 
     * @param file 文件
     * @return 文件存在返回True,文件不存在返回False
     */
    public static boolean checkFileExist(File file) {
        return file.exists();
    }

    /**
     * 
     * <p>
     * 删除文件
     * </p>
     * 
     * @param fileName 文件名称
     */
    public static void deleteFile(String fileName) {
        deleteFile(new File(fileName));
    }

    /**
     * 
     * <p>
     * 删除文件
     * </p>
     * 
     * @param file 文件
     */
    public static void deleteFile(File file) {
        if (checkFileExist(file)) {
            file.delete();
        }
    }

    /**
     * 获得文件的名称,不包含后缀
     * 
     * @param file 文件
     * @return 文件名称
     */
    public static String getFileName(File file) {
        String fileName = file.getName();
        int indexOf = fileName.indexOf(PATH_DOT);
        String filePrefix = fileName.substring(0, indexOf);
        return filePrefix;
    }

    /**
     * 获得文件名称的后缀
     * 
     * 
     * @param fileName 文件名称
     * @return 后缀
     * 
     * @author jiang_nan
     */
    public static String getFileSuffix(String fileName) {
        int indexOf = fileName.indexOf(PATH_DOT);
        String fileSuffix = fileName.substring(indexOf + 1);
        return fileSuffix;
    }

    /**
     * 创建目录
     * 
     * @param dir 目录
     * @return create success or not
     */
    public static boolean createDirectory(File dir) {
        boolean result = false;
        if (dir != null && !checkDirExist(dir)) {
            result = dir.mkdirs();
            if (!result) {
                logger.warn("Fail to create directory：" + dir.getName());
            }
        }

        return result;
    }

    /**
     * 创建文件
     * 
     * @param file 文件
     */
    public static void createFile(File file) {
        if (!checkFileExist(file)) {
            boolean result = file.mkdirs();
            if (!result) {
                logger.debug("Fail to create file：" + file.getName());
            }
        }
    }

    /**
     * 移动文件
     * 
     * @param srcPath 源路径
     * @param destPath 目标路径
     * @return boolean
     */
    public static boolean moveFile(String srcPath, String destPath) {
        boolean success = false;
        // File (or directory) to be moved
        File file = new File(srcPath);
        if (FileUtil.checkFileExist(file)) {
            // Destination directory
            File dest = new File(destPath);
            if (!checkFileExist(dest)) {
                dest.mkdirs();
            }
            // Move file to new directory
            success = file.renameTo(new File(dest, file.getName()));
        }
        return success;
    }

    /**
     * 获得系统Temp路径
     * 
     * @return Temp路径
     * 
     * @author jiang_nan
     */
    public static String getSystemTempPath() {

        String reportPdfPath = ConfigManager.getSystemTempPath();
        if (StringUtil.isEmpty(reportPdfPath)) {
            logger.error("No config.");
        }
        if (!StringUtil.isEmpty(reportPdfPath) && !reportPdfPath.endsWith(PATH_DIV)) {
            reportPdfPath += PATH_DIV;
        }
        FileUtil.createDirectory(new File(reportPdfPath));

        return reportPdfPath;
    }

    /**
     * 根据指定的目录和文件名获取文件路径
     * 
     * @param dirPath 目录
     * @param fileName 文件名
     * @return 文件路径
     */
    public static File makeFile(String dirPath, String fileName) {

        // 目录处理
        File dir;
        if (!StringUtil.isEmpty(dirPath)) {
            dir = new File(dirPath);
            // 目录不存在时，创建目录
            createDirectory(dir);
        } else {
            // 未指定时使用当前目录
            dir = new File(CURRENT_PATH);
        }

        return new File(dir, fileName);
    }

    /**
     * Get file from file path.
     * 
     * @param filePath filePath
     * @param filter name filter
     * @return full files
     */
    public static List<File> getCsvFilesFromPath(File filePath, String filter) {
        return getFilesFromPath(filePath, filter, CSV_TYPE);
    }

    /**
     * Get file from file path.
     * 
     * @param filePath filePath
     * @return full files
     */
    public static List<File> getCsvFilesFromPath(File filePath) {
        return getFilesFromPath(filePath, null, CSV_TYPE);
    }

    /**
     * Get file from file path.
     * 
     * @param filePath filePath
     * @param type type
     * @return full files
     */
    public static List<File> getFilesFromPath(File filePath, String type) {
        return getFilesFromPath(filePath, null, type);
    }

    /**
     * Get file from file path.
     * 
     * @param filePath file path
     * @param filter filter
     * @param type type
     * @return full file
     */
    public static List<File> getFilesFromPath(File filePath, String filter, String type) {

        // file list
        List<File> fileLst = new ArrayList<File>();

        // to lower case
        String lowFiler = null;
        if (!StringUtil.isEmpty(filter)) {
            lowFiler = filter.toLowerCase();
        }

        // if exist
        if (filePath.exists() && filePath.isDirectory()) {

            // loop for files
            for (File lstFile : filePath.listFiles()) {

                // if file
                if (lstFile.isFile()) {

                    // file name
                    if (lowFiler != null && lstFile.getName().toLowerCase().indexOf(lowFiler) < 0) {
                        continue;
                    }

                    // file type
                    if (type != null && !lstFile.getName().toLowerCase().endsWith(type)) {
                        continue;
                    }

                    // add into list
                    fileLst.add(lstFile);
                }

                // if directory
                if (lstFile.isDirectory()) {
                    fileLst.addAll(getFilesFromPath(lstFile, filter, type));
                }
            }

        }

        return fileLst;
    }

    /**
     * Get file from file path.
     * 
     * @param filePath file path
     * @param filters filter
     * @return full file
     */
    public static List<File> getBatchFilesFromPath(File filePath, String... filters) {

        // file list
        List<File> fileLst = new ArrayList<File>();

        // if exist
        if (filePath.isDirectory()) {
            // check files exist
            File[] files = filePath.listFiles();
            if (files == null) {
                return fileLst;
            }
            // loop for files
            for (File lstFile : files) {

                // if file
                if (lstFile.isFile()) {
                    for (String filter : filters) {
                        // file name
                        if (!lstFile.getName().toLowerCase().startsWith(filter.toLowerCase())) {
                            continue;
                        }

                        // add into list
                        fileLst.add(lstFile);
                        break;
                    }
                }

                // if directory
                if (lstFile.isDirectory()) {
                    fileLst.addAll(getBatchFilesFromPath(lstFile, filters));
                }
            }
        }

        return fileLst;
    }

    /**
     * Delete file and directory.
     * 
     * @param file file
     * @return delete
     */
    public static boolean deleteAllFile(File file) {
        // if exist
        if (!file.exists()) {
            return true;
        } else {
            // if is file
            if (file.isFile()) {
                // delete
                return file.delete();
            } else {
                // delete son files
                for (File lstFile : file.listFiles()) {
                    if (!deleteAllFile(lstFile)) {
                        return false;
                    }
                }
                // delete directory
                return file.delete();
            }
        }
    }

    /**
     * Get file full path.
     * 
     * @param filePath file path
     * @param fileType file type
     * @return full file path
     * 
     * @author liu_yinchuan
     */
    public static String getBackUpFilePathByFileType(String filePath, String fileType) {

        // temp path
        StringBuffer tempFilePath = new StringBuffer();
        // first folder
        tempFilePath.append(filePath);
        if (!filePath.endsWith(String.valueOf(File.separatorChar))) {
            tempFilePath.append(File.separatorChar);
        }
        // second folder
        tempFilePath.append(fileType);

        return tempFilePath.toString();
    }

    /**
     * copy file from src to dst
     * 
     * @param src File
     * @param dst File
     * @return boolean
     * @throws IOException e
     */
    public static boolean removeAndDeleteFile(File src, File dst) throws IOException {

        // return true
        return removeAndDeleteFile(src, dst, true);
    }

    /**
     * copy file from src to dst
     * 
     * @param src File
     * @param dst File
     * @param delete boolean
     * @return boolean
     * @throws IOException e
     */
    public static boolean removeAndDeleteFile(File src, File dst, boolean delete) throws IOException {

        // byte length
        final int readLength = 1024;

        // if exist
        if (!src.exists()) {
            return true;
        }

        // clear dst file
        if (dst.exists() && dst.isFile()) {
            // if exist, delete
            dst.delete();
        }

        // copy
        InputStream in = null;
        OutputStream out = null;
        try {

            // copy
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);
            byte[] buf = new byte[readLength];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            logger.error("Copy file error. please copy manual.(File:" + src.getName() + ")");
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    // do nothing
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    // do nothing
                }
            }
        }

        // if delete
        if (delete) {
            return src.delete();
        }

        // return true
        return true;
    }

    /**
     * remove the file from one path to back file path.
     * 
     * @param file target file
     * @param backupPath backup Path
     * @return result
     */
    public static boolean removeAndDeleteFile(File file, String backupPath) {

        // byte length
        final int readLength = 1024;

        // make back up file name
        String fileName = file.getName();

        // if not separator
        StringBuffer destPath = new StringBuffer();
        if (!backupPath.endsWith(File.separator)) {
            destPath.append(backupPath).append(File.separator).append(fileName);
        } else {
            destPath.append(backupPath).append(fileName);
        }

        // file check
        File destFile = new File(destPath.toString());
        if (destFile.exists() && destFile.isFile()) {
            // if exist, delete
            destFile.delete();
        }

        // get stream
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {

            // copy
            fis = new FileInputStream(file);
            fos = new FileOutputStream(destFile);
            byte[] buf = new byte[readLength];
            int c;
            while ((c = fis.read(buf)) != -1) {
                fos.write(buf, 0, c);
            }

        } catch (IOException e) {
            logger.error("Copy file error. please copy manual.(File:" + fileName + ")");
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    // do nothing
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    // do nothing
                }
            }
        }

        // after copy do delete
        file.delete();

        return true;
    }

    /**
     * Save file to dest path.
     * 
     * @param file the file
     * @param destPath dest path
     * @param fileName save file name
     */
    public static void saveFileToPath(File file, String destPath, String fileName) {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            saveFileToPath(fis, destPath, fileName);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Save file to dest path.
     * 
     * @param fis file input stream
     * @param destPath dest path
     * @param fileName save file name
     */
    public static void saveFileToPath(InputStream fis, String destPath, String fileName) {

        FileOutputStream fos = null;
        try {
            File destFolder = new File(destPath);
            if (!destFolder.exists()) {
                destFolder.mkdirs();
            }
            File destFile = new File(destPath, fileName);
            fos = new FileOutputStream(destFile);
            final int readLength = 1024;
            byte[] buf = new byte[readLength];
            int c;
            while ((c = fis.read(buf)) != -1) {
                fos.write(buf, 0, c);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Extract zip file to dest path.
     * 
     * @param file the file
     * @param destPath dest path
     */
    public static void extract(File file, String destPath) {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            extract(fis, destPath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Extract zip file to dest path.
     * 
     * @param is file input stream
     * @param saveFileDir dest path
     */
    public static void extract2(InputStream is, String saveFileDir) {

        String destDir = saveFileDir;
        if (!destDir.endsWith("\\") && !destDir.endsWith("/")) {
            destDir += File.separator;
        }
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        ZipArchiveInputStream zais = null;
        try {
            zais = new ZipArchiveInputStream(is);
            ArchiveEntry archiveEntry = null;
            while ((archiveEntry = zais.getNextEntry()) != null) {
                String entryFileName = archiveEntry.getName();
                String entryFilePath = destDir + entryFileName;
                OutputStream os = null;
                try {
                    File entryFile = new File(entryFilePath);
                    if (entryFileName.endsWith("/")) {
                        entryFile.mkdirs();
                    } else {
                        os = new BufferedOutputStream(new FileOutputStream(entryFile));
                        byte[] buffer = new byte[IntDef.CUSTOMER_AUTO_GROW_COLLECTION_LIMIT];
                        int len = -1;
                        while ((len = zais.read(buffer)) != -1) {
                            os.write(buffer, 0, len);
                        }
                    }
                } catch (IOException e) {
                    throw new IOException(e);
                } finally {
                    if (os != null) {
                        try {
                            os.flush();
                            os.close();
                        } catch (IOException ex) {
                            logger.warn("OutputStream close fail.", ex);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (zais != null) {
                    zais.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Extract zip file to dest path.
     * 
     * @param fins file input stream
     * @param destPath dest path
     */
    public static void extract(InputStream fins, String destPath) {

        ZipInputStream zins = new ZipInputStream(fins);
        ZipEntry ze = null;
        try {
            byte[] ch = new byte[IntDef.INT_TWO_HUNDRED_FIFTY_SIX];
            while ((ze = zins.getNextEntry()) != null) {
                File zfile = new File(destPath + File.separator + ze.getName());
                File fpath = new File(zfile.getParentFile().getPath());
                if (ze.isDirectory()) {
                    if (!zfile.exists()) {
                        zfile.mkdirs();
                    }
                    zins.closeEntry();
                } else {
                    if (!fpath.exists()) {
                        fpath.mkdirs();
                    }
                    FileOutputStream fouts = new FileOutputStream(zfile);
                    int i;
                    while ((i = zins.read(ch)) != -1) {
                        fouts.write(ch, 0, i);
                    }
                    zins.closeEntry();
                    fouts.close();
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fins != null) {
                try {
                    fins.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (zins != null) {
                try {
                    zins.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Compress files to zip file.
     * 
     * @param zipFile the zip file
     * @param filesPath the files path
     */
    public static void compress(File zipFile, File filesPath) {

        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            zip(out, filesPath, filesPath.getName());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Compress files to zip file.
     * 
     * @param out zip out stream
     * @param f file
     * @param base base name
     * @throws IOException the IO exception
     */
    private static void zip(ZipOutputStream out, File f, String base) throws IOException {

        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            if (fl.length == 0) {
                out.putNextEntry(new ZipEntry(base + "/"));
            }
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + "/" + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            BufferedInputStream bi = new BufferedInputStream(in);
            int b;
            while ((b = bi.read()) != -1) {
                out.write(b);
            }
            bi.close();
            in.close();
        }
    }

    /**
     * copy file from src to dst
     * 
     * @param src File
     * @param directory File
     * @return boolean
     * @throws IOException e
     */
    public static boolean moveToDirectoy(File src, File directory) throws IOException {

        // if not directory
        if (!directory.isDirectory()) {
            return false;
        }

        // new file
        String newFileName = new StringBuffer(directory.getPath()).append(File.separatorChar).append(src.getName())
            .toString();

        // move and delete
        return removeAndDeleteFile(src, new File(newFileName), false);
    }

    /**
     * copy file from src to dst
     * 
     * @param src File
     * @param directory File
     * @return boolean
     * @throws IOException e
     */
    public static boolean moveToDirectoyAndDelete(File src, File directory) throws IOException {

        // if not directory
        if (!directory.isDirectory()) {
            return false;
        }

        // new file
        String newFileName = new StringBuffer(directory.getPath()).append(File.separatorChar).append(src.getName())
            .toString();

        // move and delete
        return removeAndDeleteFile(src, new File(newFileName));
    }
}
