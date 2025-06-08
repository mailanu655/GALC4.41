package com.honda.galc.client.gts.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

public class CommStatusWindow extends JDialog implements ActionListener{
    
   private static final long serialVersionUID = 1L;

   private JPanel mainPanel = new JPanel(new BorderLayout()); 
   private JPanel namePanel = new JPanel();
   private JPanel statusPanel = new JPanel();
    
    
    private Timer timer;
    
    private List<ComStatus> comStatusList;
    
    public CommStatusWindow(JComponent owner, List<ComStatus> comStatusList){
        
        this.comStatusList = comStatusList;
        
        initComponents();
        
        this.pack();
        
        
        this.setLocation(Math.max(2,owner.getLocationOnScreen().x - (this.getWidth() - owner.getWidth())/2),
                        owner.getLocationOnScreen().y - this.getHeight() - 2);
        
        timer = new Timer(5000,this);
        timer.start();
    }
    
    private void initComponents(){
        
        setComStatusList(comStatusList);
        
        mainPanel.add(namePanel,BorderLayout.WEST);
        mainPanel.add(statusPanel,BorderLayout.EAST);
        mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        this.getContentPane().add(mainPanel);
        this.setBackground(Color.red);
        this.setUndecorated(true);
    }

    public void actionPerformed(ActionEvent e) {
        
        close();
    }
    
    public void close(){
        
        this.dispose();
        this.setVisible(false);
        
    }
    
    public void setComStatusList(List<ComStatus> comStatusList) {
         
        int size = comStatusList.size();
        
        if(size == 0) return;
        
        namePanel.setLayout(new GridLayout(size,0));
  
        statusPanel.setLayout(new GridLayout(size,0));
        
        for(ComStatus comStatus : comStatusList) {
            
            JLabel label = new JLabel(comStatus.getName());
            label.setOpaque(true);
            label.setBackground(new Color(180,255,255));
            namePanel.add(label);
            namePanel.setBackground(new Color(180,255,255));
            
            JLabel st = new JLabel(comStatus.isOk()? " OK " : " ERR");
            
            st.setOpaque(true);
            st.setBackground(comStatus.isOk() ? Color.green : Color.red);
            statusPanel.add(st);
            statusPanel.setBackground(new Color(180,255,255));
        }
    }
    
 
    public class StatusComponent extends JComponent {
        
        
        private static final long serialVersionUID = 1L;

        public void paint(Graphics g){
            
            int r = Math.min(this.getHeight(), this.getWidth());
            
            int cx = getLocationOnScreen().x + (getWidth() / 2);
            int cy = getLocationOnScreen().y + (getHeight() / 2);
            
            Ellipse2D.Double e = new Ellipse2D.Double(cx - (r/2), cy-(r/2), r,r);
            g.setColor(this.getBackground());
            ((Graphics2D)g).fill(e);
            ((Graphics2D)g).draw(e);
            
        }
    }
    
    
    
}
