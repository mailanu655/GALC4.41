package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;

import com.honda.galc.client.gts.view.GtsDrawingView;
import com.honda.galc.client.gts.view.MessageWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.entity.gts.GtsOutlineImage;
import com.honda.galc.entity.gts.GtsOutlineMap;

public class EditOutlineMapDialog extends JDialog implements  ActionListener,TableModelListener{

    private static final long serialVersionUID = 1L;

    private GtsDrawingView view;
    private JPanel mainPanel = new JPanel();
    private JPanel upperPanel = new JPanel();
    private JPanel lowerPanel = new JPanel();
    private JPanel upperLeftPanel = new JPanel();
    private JPanel upperRightPanel = new JPanel();
    private JButton assignImageButton =    new JButton("<< Assign Image");
    private JButton addOutlineMapButton = new JButton("Add");
    private JButton removeOutlineMapButton = new JButton("Remove");
    private JButton addImageButton =      new JButton("Add Image");
    private JButton removeImageButton = new JButton("Remove Image");
    private JButton closeButton =      new JButton("Close");
    private JTable outlineMapTable = new JTable();
    private JTable imageTable = new JTable();
    private List<GtsOutlineMap> maps;
    private List<GtsOutlineImage> images;
    
    
    public EditOutlineMapDialog(GtsDrawingView view){
        super((JFrame)view.getRootPane().getParent(),true);
        this.view = view;
        setTitle("Edit Outline Map Window");
        maps = view.getDrawing().getModel().fetchAllOutlineMaps();
        images = view.getDrawing().getModel().fetchAllOutlineImages();
        initComponent();
        addActionListeners();
        pack();
        this.setLocationRelativeTo(view);
        if(outlineMapTable.getRowCount()>0) outlineMapTable.setRowSelectionInterval(0, 0);
        if(imageTable.getRowCount()>0) imageTable.setRowSelectionInterval(0, 0);
     }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        setUpperPanel();
        setLowerPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(upperPanel,BorderLayout.NORTH);
        mainPanel.add(lowerPanel,BorderLayout.SOUTH);
   }
    
    private void setUpperPanel(){
        setUpperLeftPanel();
        setUpperRightPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel,BoxLayout.X_AXIS));
        upperPanel.add(upperLeftPanel);
        upperPanel.add(upperRightPanel);
    }
    
    private void setUpperLeftPanel(){
        outlineMapTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        outlineMapTable.setModel(new OutlineMapTableModel(maps));
        outlineMapTable.setRowHeight(50);
        outlineMapTable.getModel().addTableModelListener(this);
        outlineMapTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        
        outlineMapTable.setPreferredScrollableViewportSize(new Dimension(300,400));
        outlineMapTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
       
        upperLeftPanel.setBorder(new TitledBorder("Outline Image List"));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(outlineMapTable);
        JPanel comPanel = new JPanel();
        comPanel.setLayout(new BoxLayout(comPanel,BoxLayout.X_AXIS));
        comPanel.add(addOutlineMapButton);
        comPanel.add(Box.createHorizontalStrut(10));
        comPanel.add(removeOutlineMapButton);
        upperLeftPanel.setLayout(new BoxLayout(upperLeftPanel,BoxLayout.Y_AXIS));
        upperLeftPanel.add(scrollPane);
        upperLeftPanel.add(comPanel);
    }
    
    private void setUpperRightPanel(){
        imageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        imageTable.setModel(new ImageTableModel(images));
        imageTable.getModel().addTableModelListener(this);
        imageTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        
        imageTable.setRowHeight(50);
        imageTable.setPreferredScrollableViewportSize(new Dimension(300,400));
        imageTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        upperRightPanel.setBorder(new TitledBorder("Availabel Image List"));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(imageTable);
        JPanel comPanel = new JPanel();
        comPanel.setLayout(new BoxLayout(comPanel,BoxLayout.X_AXIS));
        comPanel.add(addImageButton);
        comPanel.add(Box.createHorizontalStrut(10));
        comPanel.add(removeImageButton);
        upperRightPanel.setLayout(new BoxLayout(upperRightPanel,BoxLayout.Y_AXIS));
        upperRightPanel.add(scrollPane);
        upperRightPanel.add(comPanel);
    }
    
    private void setLowerPanel(){
        assignImageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addOutlineMapButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addImageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lowerPanel.setLayout(new BoxLayout(lowerPanel,BoxLayout.X_AXIS));
        lowerPanel.add(Box.createHorizontalGlue());
        lowerPanel.add(assignImageButton);
        lowerPanel.add(Box.createHorizontalStrut(20));
        lowerPanel.add(closeButton);
        lowerPanel.add(Box.createHorizontalGlue());
        lowerPanel.setPreferredSize(new Dimension(80,50));
    }
    
    private void addActionListeners(){
        assignImageButton.addActionListener(this);
        addOutlineMapButton.addActionListener(this);
        removeOutlineMapButton.addActionListener(this);
        addImageButton.addActionListener(this);
        removeImageButton.addActionListener(this);
        closeButton.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == assignImageButton){
            assignImage();
        }else if(e.getSource() == addOutlineMapButton){
            addOutlineMap();
        }else if(e.getSource()== removeOutlineMapButton){
            removeOutlineMap();
        } else if(e.getSource() == addImageButton){
            addImage();
        } else if(e.getSource() == removeImageButton){
            removeImage();
        }else if (e.getSource() == closeButton){
            this.setVisible(false);
            this.dispose();
        }
        
    }
    
    private void assignImage(){
        int index1 = outlineMapTable.getSelectedRow();
        if(index1 <0) return;
        int index2 = imageTable.getSelectedRow();
        if(index2 <0) return;
        GtsOutlineMap map = maps.get(index1);
        map.setImageBytes(images.get(index2).getImage().clone());
        view.getDrawing().getModel().updateOutlineMap(map);
        outlineMapTable.updateUI();
    }
    
    private void addOutlineMap(){
 
        String mCode = this.getDummyModelCode();
        GtsOutlineMap map = view.getDrawing().getModel().createOutlineMap(mCode, null);
        maps.add(map);
        outlineMapTable.revalidate();
    }
    
    private void removeOutlineMap(){
        int index = outlineMapTable.getSelectedRow();
        if(index <0) return;
       
        view.getDrawing().getModel().removeOutlineMap(maps.get(index));
 
        maps.remove(index);
        outlineMapTable.updateUI();
    }
    
    private void removeImage(){
        int index = imageTable.getSelectedRow();
        if(index <0) return;
        view.getDrawing().getModel().removeOutlineImage(images.get(index));
        images.remove(index);
        imageTable.updateUI();
        
    }
    
    private String getDummyModelCode(){
        int index = -1;
        if(maps.isEmpty()) return "M1";
        for(GtsOutlineMap map:maps){
            int i = getIndex(map.getModelCode());
            if(i>index) index = i;
        }
        return "M"+Integer.toString(index + 1);
    }
    
    private int getIndex(String str){
        if(str==null || str.length() < 2  || str.charAt(0)!='M') return -1;
        try{
            return Integer.parseInt(str.substring(1)); 
        }catch(NumberFormatException e){
        }
        return -1;
    }
    
    private void addImage(){
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ImageFilter());
        fc.showOpenDialog(this);
        File file = fc.getSelectedFile();
        if(file == null) return;
        byte[] bytes = this.loadImageIntoByteArray(file);
        if(bytes == null) return;
        BufferedImage image = convertImage(bytes);
        if(image == null) return;
        
        GtsOutlineImage outlineImage = view.getDrawing().getModel().createOutlineImage(file.getName(), bytes);
        images.add(outlineImage);
        imageTable.updateUI();
    }
    
    private byte[] loadImageIntoByteArray(File file) {
        try{
            InputStream in = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[512];
            int bytesRead;
            while ((bytesRead = in.read(buf)) > 0) {
                baos.write(buf, 0, bytesRead);
            }
            return baos.toByteArray();
        }catch(Exception e){
            handleException(e);
        }
        return null;
    }
    
    private BufferedImage convertImage(byte[] bytes) {
        try{
            return ImageIO.read(new ByteArrayInputStream(bytes));
        }catch(Exception e){
            handleException(e);
        }
        return null;
    }
    
    public void tableChanged(TableModelEvent e) {
        
        TableModel model = (TableModel)e.getSource();
        if(model instanceof OutlineMapTableModel){
        	GtsOutlineMap map = maps.get(e.getFirstRow());
            this.view.getDrawing().getModel().updateOutlineMap(maps.get(e.getFirstRow()));
        }else if(model instanceof ImageTableModel){
        	this.view.getDrawing().getModel().updateOutlineImage(images.get(e.getFirstRow()));
        }
    }
    
    public void handleException(Exception e) {
        
        MessageWindow.showExceptionDialog(this, e.getMessage());

    }

    
    private class ImageFilter extends FileFilter {

        //Accept all directories and all gif, jpg, tiff, or png files.
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            if(extension == null) return false;
            if (extension.equalsIgnoreCase("bmp")||
                extension.equalsIgnoreCase("png")) return true;
            return false;
        }
        
        private String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 &&  i < s.length() - 1) {
                ext = s.substring(i+1).toLowerCase();
            }
            return ext;
        }

        //The description of this filter
        public String getDescription() {
            return "bmp files";
        }
    }
    
    private abstract class TableModel extends AbstractTableModel{
        
    	private static final long serialVersionUID = 1L;
		
    	protected String[] names;
        
        public String getColumnName(int column) {
            return names[column];
        }    
        public int getColumnCount() {
            if(names.length == 0) return 4;
            return names.length;
        }
    }
    
    private class OutlineMapTableModel extends TableModel {
        
        private static final long serialVersionUID = 1L;
       
        private List<GtsOutlineMap> outlines;
        
        public OutlineMapTableModel(List<GtsOutlineMap> outlines){
            this.names = new String[] {"Model Code","Outline Image"};
            this.outlines = outlines;
        }
        
        public int getRowCount() {
            return outlines.size();
        }
        
        /**
         *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
         *
         *  @param columnIndex  the column being queried
         *  @return the Object.class
         */
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex == 1) return ImageIcon.class;
            else return super.getColumnClass(columnIndex);
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(columnIndex != 1) return true;
            return false;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(rowIndex >= outlines.size()) return null;
            GtsOutlineMap outline = outlines.get(rowIndex);
            switch(columnIndex){
                case 0: return outline.getModelCode();
                case 1: return outline.getImageIcon();
            }
            return null;
        }
        
        public void setValueAt(Object value, int row, int column) {
            if(column<0 || column >= 1) return;
            GtsOutlineMap map= maps.get(row);
            if(column == 0){
            	for(GtsOutlineMap item : maps) {
             	   if(item.equals(map)) continue;
             	   if(item.getModelCode().equalsIgnoreCase((String)value)) {
             		   MessageDialog.showError("Duplicate Model Code not allowed - " + map.getModelCode() );
             		   return;
             	   }
             	}
                map.setModelCode((String)value);
            }
            this.fireTableCellUpdated(row, column);
        }    
   }
    
    private class ImageTableModel extends TableModel{
        
        private static final long serialVersionUID = 1L;
        
        private List<GtsOutlineImage> outlines;
        public ImageTableModel(List<GtsOutlineImage> outlines){
            this.names = new String[] {"Description","Image"};
            this.outlines = outlines;
        }
        
        public int getRowCount() {
            return outlines.size();
        }
        
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(rowIndex >= outlines.size()) return null;
            GtsOutlineImage image = outlines.get(rowIndex);
            switch(columnIndex){
                case 0: return image.getOutlineImageDescription();
                case 1: return image.getImageIcon();
            }
            return null;
        }
        
        public void setValueAt(Object value, int row, int column) {
            if(column!=0) return;
            GtsOutlineImage outline = images.get(row);
            outline.setOutlineImageDescription((String)value);
            this.fireTableCellUpdated(row, column);
        }    
        
        /**
         *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
         *
         *  @param columnIndex  the column being queried
         *  @return the Object.class
         */
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex == 1) return ImageIcon.class;
            else return super.getColumnClass(columnIndex);
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(columnIndex != 1) return true;
            return false;
        }
    }
}
