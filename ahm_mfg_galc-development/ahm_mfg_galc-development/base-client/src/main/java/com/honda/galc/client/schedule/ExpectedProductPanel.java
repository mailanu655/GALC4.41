package com.honda.galc.client.schedule;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.component.DefaultFieldRender;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.property.PropertyService;

public class ExpectedProductPanel extends JPanel implements KeyListener {
	
	private static final long serialVersionUID = 7641211851169301188L;
	
	public static final int PRODUCT_ID_FIELD_WIDTH = 17;
	
	private BaseProduct expectedProduct;
	private String expectedProductLabel = "Product ID";
	
	private JLabel productIdLabel;
	private LengthFieldBean productIdTextField;
	private JTextField specTextField;
	private Integer reset = null;
	private String processingProductId;
	private boolean checkDuplicateProductId = false;
	
	public ExpectedProductPanel() {
		super();
		initialize();
	}
	
	public ExpectedProductPanel(String prodLabel) {
		super();
		this.expectedProductLabel = prodLabel;
		initialize();
	}
	
	private void initialize() {
		initializePanel();
		if(expectedProduct != null) {
			populateData();
		}
		
		getProductIdTextField().requestFocusInWindow();
	}
	
	public ExpectedProductPanel(Integer reset) {
		this();
		this.reset = reset;
	}
	
	public ExpectedProductPanel(Integer reset,String prodLabel) {
		this(prodLabel);
		this.reset = reset;
	}
	
	public void populateData() {
		productIdTextField.setText(expectedProduct.getProductId());
		productIdLabel.setText(expectedProductLabel);
	}
	
	private void initializePanel() {
		productIdLabel = new JLabel(expectedProductLabel);
		productIdLabel.setHorizontalAlignment(SwingConstants.LEFT);
		productIdLabel.setFont(Fonts.DIALOG_PLAIN_33);
		
		productIdTextField = new LengthFieldBean(new DefaultFieldRender());
		productIdTextField.addKeyListener(this);
		productIdTextField.setColumns(getProductIdFieldWidth() + 3);
		productIdTextField.setMaximumLength(getProductIdFieldWidth());
		productIdTextField.setFont(Fonts.DIALOG_PLAIN_33);
		productIdTextField.setName("productIdTextField");
		
		JPanel panel = new JPanel();
		panel.add(productIdLabel);
		panel.add(productIdTextField);
		
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
	}


	private int getProductIdFieldWidth() {
		
		String maxProductSnLength = PropertyService.getProperty(ApplicationContext.getInstance().getProcessPointId(), TagNames.MAX_PRODUCT_SN_LENGTH.name());
		if(!StringUtils.isEmpty(maxProductSnLength))
			return Integer.valueOf(maxProductSnLength);
		
		return PRODUCT_ID_FIELD_WIDTH;
	}
	
	public BaseProduct getExpectedProduct() {
		return expectedProduct;
	}
	
	public void setExpectedProduct(BaseProduct expectedProduct) {
		this.expectedProduct = expectedProduct;
	}

	public String getExpectedProductLabel() {
		return expectedProductLabel;
	}

	public void setExpectedProductLabel(String expectedProductLabel) {
		this.expectedProductLabel = expectedProductLabel;
	}

	public JLabel getProductIdLabel() {
		return productIdLabel;
	}

	public void setProductIdLabel(JLabel productIdLabel) {
		this.productIdLabel = productIdLabel;
	}

	public LengthFieldBean getProductIdTextField() {
		return productIdTextField;
	}

	public void setProductIdTextField(LengthFieldBean productIdTextField) {
		this.productIdTextField = productIdTextField;
	}

	public JTextField getSpecTextField() {
		return specTextField;
	}

	public void setSpecTextField(JTextField specTextField) {
		this.specTextField = specTextField;
	}

	public void keyReleased(KeyEvent e) {
		if (KeyEvent.VK_ENTER == e.getKeyCode()) {
			if(isCheckDuplicateProductId()){
				processProductIdWithDuplicateCheck(e);
			}else{
				processProductId(e);
			}
		}
	}
	
	private void processProductIdWithDuplicateCheck(KeyEvent e){
		String productId= StringUtils.trimToEmpty(productIdTextField.getText());
		if(StringUtils.equals(processingProductId, productId)){
			//The processing product id equals to the current product id. So ignore the duplicate request
			e.consume();
			return;
		}
		processingProductId = productId;
		final SchedulingEvent schEvent = new SchedulingEvent(productId, SchedulingEventType.PROCESS_PRODUCT);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				EventBus.publish(schEvent);
			};
		});
		
		if (reset != null)  resetLaterForCurrentProductId();
		else productIdTextField.setText("");
		e.consume();
	}
	
	private void processProductId(KeyEvent e){
		final SchedulingEvent schEvent = new SchedulingEvent(productIdTextField.getText(), SchedulingEventType.PROCESS_PRODUCT);
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
            	EventBus.publish(schEvent);
            };
        });
		
		if (reset != null)  resetLater();
		else productIdTextField.setText("");
	}

	private void resetLater() {
		Thread t = new Thread(){
			public void run() {
				try {
					Thread.sleep(reset);
				} catch (Exception e) {
					// TODO: handle exception
				}
				productIdTextField.setText("");
			}
		};
		
		t.start();
		
	}
	
	private void resetLaterForCurrentProductId() {
		final String currentProductId=productIdTextField.getText();
		Thread t = new Thread(){
			public void run() {
				try {
					Thread.sleep(reset);
				} catch (Exception e) {
					// TODO: handle exception
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						//Swing Components must be accessed from AWT Event Dispatch Thread
						if(StringUtils.equals(currentProductId, productIdTextField.getText())){
							//only reset the text field if the product id value doesn't changes.
							productIdTextField.setText("");
						}
					}
				});
				
			}
		};
		
		t.start();
	}
	
	public boolean isCheckDuplicateProductId() {
		return checkDuplicateProductId;
	}


	public void setCheckDuplicateProductId(boolean checkDuplicateProductId) {
		this.checkDuplicateProductId = checkDuplicateProductId;
	}

	public void keyPressed(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
}
