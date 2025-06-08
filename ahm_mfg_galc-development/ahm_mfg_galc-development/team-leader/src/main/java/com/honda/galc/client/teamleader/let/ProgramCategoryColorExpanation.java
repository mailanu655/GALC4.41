
package com.honda.galc.client.teamleader.let;

import com.honda.galc.data.LETDataTag;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public class ProgramCategoryColorExpanation extends JPanel {

	private static final long serialVersionUID = 1L;
	private JList jList = null;
    private JLabel jLabel = null;
    private Hashtable colorList = new Hashtable();

    public ProgramCategoryColorExpanation(Vector list) {
        super();
        initialize(list);
        this.setBorder(BorderFactory.createCompoundBorder());
    }


    private void initialize(Vector list) {
        this.setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane(getJList(list));
        pane.setBorder(BorderFactory.createCompoundBorder());
        this.add(pane, BorderLayout.CENTER);
    }

    private JList getJList(Vector list) {
        if (jList == null) {
            int size = list.size();
            String[] listData = new String[size];
            jList = new JList();
            jList.setBackground(this.getBackground());

            for (int i = 0; i < size; i++) {
                Hashtable h = (Hashtable) list.get(i);
                listData[i] = (String) h.get(LETDataTag.PGM_CATEGORY_NAME);
                colorList.put((String) h.get(LETDataTag.PGM_CATEGORY_NAME), (String) h.get(LETDataTag.BG_COLOR));
            }
            jList.setListData(listData);
            jList.setCellRenderer(new MyCellRenderer());
            jList.setBorder(BorderFactory.createCompoundBorder());
        }
        return jList;
    }

    class MyCellRenderer extends JLabel implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {

            String str = value.toString();
            setText(str);
            setBackground(Color.decode((String) colorList.get(str)));
            setForeground(Color.black);
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }
}
