package com.honda.galc.client.collector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ProductPanel;

/**
 * 
 * <h3>CollectorPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CollectorPanel description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Dec 2, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Dec 2, 2011
 */

public class CollectorPanel extends JPanel implements ICollectorPane{
	private static final long serialVersionUID = 1L;
	protected CollectorClientPropertyBean property;
	private ProductPanel productPane;
	private JEditorPane resultPane;
	private JPanel buttonPane;
	private JButton cancelButton;
	protected JScrollPane resultScrollPane;
	protected MainWindow mainWin;
	

	public CollectorPanel(CollectorClientPropertyBean property, MainWindow mainWin) {
		this.property = property;
		this.mainWin = mainWin;
		init();
	}


	protected void init() {
		setLayout(new BorderLayout());
		
		if(getProperty().isShowProductId())
			add(getProductPane(),BorderLayout.NORTH);
		
		add(getResultScrollPane(), BorderLayout.CENTER);
		add(getButtonPane(), BorderLayout.SOUTH);
	}


	public void refresh(){
		getProductPane().refresh();
		getCancelButton().setVisible(false);
		getResultPane().setVisible(false);
		getProductPane().getProductIdField().requestFocus();
	}
	
	public ProductPanel getProductPane() {
		if(productPane == null)
			productPane = new ProductPanel(mainWin, mainWin.getApplicationContext().getProductTypeData());
		
		return productPane;
	}


	//Getters && Setters
	public JEditorPane getResultPane() {
		if(resultPane == null){
			resultPane = new JEditorPane();
			resultPane.setEditable(false);
			resultPane.setContentType("text/html");
		}
		return resultPane;
	}

	protected JPanel getButtonPane() {
		if(buttonPane == null){
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.TRAILING));
			buttonPane.add(getCancelButton());
			buttonPane.add(Box.createHorizontalStrut(120));
			
		}
		return buttonPane;
	}

	public JButton getCancelButton() {
		if(cancelButton == null){
			cancelButton = new JButton("Cancel");
			cancelButton.setPreferredSize(new Dimension(100, 32));
		}
		return cancelButton;
	}


	public JScrollPane getResultScrollPane() {
		if(resultScrollPane == null){

			resultScrollPane = new JScrollPane(getResultPane());
			resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			resultScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			resultScrollPane.setPreferredSize(new Dimension(1000, 500));
		}
		return resultScrollPane;
	}
	
	protected CollectorClientPropertyBean getProperty() {
		return this.property;
	}
	
	public MainWindow getMainWin() {
		return mainWin;
	}
	

}
