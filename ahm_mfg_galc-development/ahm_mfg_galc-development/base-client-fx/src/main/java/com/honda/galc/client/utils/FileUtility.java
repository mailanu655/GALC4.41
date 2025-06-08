package com.honda.galc.client.utils;

import java.io.File;
import java.util.prefs.Preferences;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.ApplicationMainPane;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>FileUtility</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Aug 9, 2019
 */
public class FileUtility {

    public static final String FILE_SAVE_LOCATION_KEY = "FILE_SAVE_LOCATION_KEY";

    /**
     * 
     * @param parentPane
     * @param fileName
     * @param extensionFilters
     *            , for example : new ExtensionFilter("CSV Files", "*.csv"), new
     *            ExtensionFilter("Excel Files xls, xlsx", "*.xls", "*xlsx")
     * @return
     */
    public static File popupSaveDialog(ApplicationMainPane parentPane, String fileName, ExtensionFilter... extensionFilters) {
        Preferences preferences = Preferences.userNodeForPackage(parentPane.getClass());
        String key = parentPane.getClass().getSimpleName() + "." + FILE_SAVE_LOCATION_KEY;
        String currentDirectoryPath = preferences.get(key, null);
        if (currentDirectoryPath == null) {
            currentDirectoryPath = "";
        }

        FileChooser fileChooser = new FileChooser();
        if (extensionFilters != null) {
            for (ExtensionFilter ef : extensionFilters) {
                fileChooser.getExtensionFilters().add(ef);
            }
        } 
        fileChooser.setTitle("Select file to save.");
        if (StringUtils.isNotBlank(currentDirectoryPath)) {
            File file = new File(currentDirectoryPath);
            if (file.exists() && file.isDirectory()) {
                fileChooser.setInitialDirectory(file);
            } else if (file.exists()) {
                fileChooser.setInitialDirectory(file.getParentFile());
            }
        }
        if (StringUtils.isNotBlank(fileName)) {
            fileChooser.setInitialFileName(fileName);
        }
        File file = fileChooser.showSaveDialog(parentPane.getMainWindow().getStage());
        if (file == null) {
            return file;
        }
        if (file.isDirectory()) {
            preferences.put(key, file.getAbsolutePath());
        } else if (file.getParentFile() != null && file.getParentFile().isDirectory()) {
            preferences.put(key, file.getParentFile().getAbsolutePath());
        }
        return file;
    }
}
