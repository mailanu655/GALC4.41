package com.honda.mfg.mesproxy;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: vc029195
 * Date: 5/15/12
 * Time: 5:34 AM
 * To change this template use File | Settings | File Templates.
 */

public class SimpleLog {

    private static String logFileName = MesProxyProperties.getLogFileName();
    private static String logFilePath = MesProxyProperties.getLogFilePath();
    private final static DateFormat logDateFormat = MesProxyProperties.getLogDateFormat();
    private final static DateFormat logFileDateFormat = MesProxyProperties.getLogFileDateFormat();

    private SimpleLog() {
    }

    public static void write(Exception e) {
        write(stack2string(e));
    }

    public static void write(String msg) {
        try {
            Date now = new Date();
            String currentTime = SimpleLog.logDateFormat.format(now);
            FileWriter aWriter = getFileWriter();
            aWriter.write(currentTime + " " + msg + System.getProperty("line.separator"));
            System.out.println(currentTime + " " + msg);
            aWriter.flush();
            aWriter.close();
        } catch (Exception e) {
            System.out.println(stack2string(e));
        }
    }

    private static String stack2string(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "------\r\n" + sw.toString() + "------\r\n";
        } catch (Exception e2) {
            return "bad stack2string";
        }
    }

    private static FileWriter getFileWriter() {

        try {
            String logFilePathAndName = logFilePath + logFileName;
            File currentFile = new File(logFilePath + logFileName);
            if (currentFile.length() >= MesProxyProperties.getLogFileMaxSize()) {
                Date now = new Date();
                String newFileName = logFilePath + SimpleLog.logFileDateFormat.format(now) + "_" + logFileName;
                try{
                renameFile(logFilePathAndName, newFileName);
                deleteOldFiles();
                return new FileWriter(logFilePathAndName, false);
                }catch (IOException e){
                   return new FileWriter(logFilePathAndName, true);
                }
            } else {
                return new FileWriter(logFilePathAndName, true);
            }
        } catch (Exception e) {
            System.out.println("SimpleLog: error - " + e.getMessage());
            return null;

        }
    }

    private static void renameFile(String oldName, String newName) throws IOException {
        File srcFile = new File(oldName);
        boolean bSucceeded = false;
        try {
            File newFile = new File(newName);
            if (newFile.exists()) {
                if (!newFile.delete()) {
                    throw new IOException(oldName + " was not successfully renamed to " + newName);
                }
            }
            if (!srcFile.renameTo(newFile)) {
                throw new IOException(oldName + " was not successfully renamed to " + newName);
            } else {
                bSucceeded = true;
            }
        } finally {
            if (bSucceeded) {
                srcFile.delete();
            }
        }
    }

    private static void deleteOldFiles() {
        while (true) {
            File oldest = null;
            long oldestTime = 0;
            File[] list = new File(MesProxyProperties.getLogFilePath()).listFiles();
            if (list.length < MesProxyProperties.getMaxNumberOfLogFiles()) {
                break;
            }
            for (File f : list) {
                long m = f.lastModified();
                if (oldest == null || oldestTime > m) {
                    oldestTime = m;
                    oldest = f;
                }
            }
            write("SimpleLog: deleting file: " + oldest.getName());
            oldest.delete();
        }
    }
}

