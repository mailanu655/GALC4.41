package com.honda.galc.client.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedPasswordField;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopup;
import com.honda.galc.openprotocol.model.LastTighteningResult;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.util.Duration;


public class CommonUtil {

	/**
	 * Get current date in yyyy-MM-dd format as String
	 * @return
	 */
	public static String getCurrentDate() throws ParseException{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String d1 = dtf.format(localDate);
		return d1;
	}

	/**
	 * Get current Time in HH:MM:SS format as String
	 * @return
	 */
	public static String getCurrentTime() throws ParseException{
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	    Date now = new Date();
	    String strTime = sdfTime.format(now);
		return strTime;
	}
	
	public static Date appendDateTime(String d, String t) {
		String sDate1= d + " " + t;
		Date date = new Date();
	    try {
	    	date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDate1);
	    } catch(Exception e) {
	    	date = new Date();
	    }
	    return date; 
	}

	public String[] listPackedResources(String path) 
	throws UnsupportedEncodingException, IOException, URISyntaxException{
		URL dirURL = Thread.currentThread().getContextClassLoader().getResource(path);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			/* A file path: easy enough */
			return new File(dirURL.toURI()).list(new FilenameFilter(){public boolean accept(File dir, String name) {
				return !name.startsWith(".");
			};});
		} 

		if (dirURL != null && dirURL.getProtocol().equals("jar")) {
			/* A JAR path */
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
			while(entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith(path) && !name.endsWith("/")) { //filter according to the path
					result.add(name.substring(path.length() +1));
				}
			}
			return result.toArray(new String[result.size()]);
		} 

		throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	}
	
	public static String toString(LastTighteningResult result){
		StringBuilder sb = new StringBuilder();
		if (result != null) {
			sb.append(" tighteningId=").append(result.getTighteningId()).append(",");
			sb.append(" productId=").append(result.getProductId().trim()).append(",");
			sb.append(" torque=").append(result.getTorque()).append(",");
			sb.append(" torqueStatus=").append(result.getTorqueStatus()).append(",");
			sb.append(" angle=").append(result.getAngle()).append(",");
			sb.append(" angleStatus=").append(result.getAngleStatus());
		}
		return sb.toString();
	}
	
	public static String format(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat(QiConstant.TIME_STAMP_FORMAT);
		return df.format(date);
	}
	
	/**
	 * Finds the date range for which associate user id will shown.
	 * @param no. of days for which logged in user will be displayed
	 * @return Timestamp
	 */
	public static Timestamp findDateRange(int noOfDays){
		 Date todayDate = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(todayDate);
		cal.add(Calendar.DAY_OF_MONTH, -noOfDays);
		cal.set(Calendar.HOUR_OF_DAY, 0);            
		cal.set(Calendar.MINUTE, 0);                
		cal.set(Calendar.SECOND, 0);                
		cal.set(Calendar.MILLISECOND, 0); 
		Date previousDate = cal.getTime();
		Timestamp oldTimestamp=new Timestamp(previousDate.getTime());
		return oldTimestamp;
	}
	
	public static void setPopupVisible(final boolean togglePopup, final Node n2, final KeyBoardPopup popup) {
		Platform.runLater(new Runnable() {
			private Animation fadeAnimation;
			@Override
			public void run() {
				if (fadeAnimation != null) {
					fadeAnimation.stop();
				}
				if (!togglePopup){
					popup.hide();
					return;
				}
				if (popup.isShowing()){
					return;
				}
				popup.getKeyBoard().setOpacity(0.0);

				FadeTransition fade = new FadeTransition(Duration.seconds(.5), popup.getKeyBoard());
				fade.setToValue(togglePopup ? 1.0 : 0.0);
				fade.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						fadeAnimation = null;
					}
				});

				ScaleTransition scale = new ScaleTransition(Duration.seconds(.5), popup.getKeyBoard());
				scale.setToX(togglePopup ? 1 : 0.8);
				scale.setToY(togglePopup ? 1 : 0.8);

				ParallelTransition tx = new ParallelTransition(fade, scale);
				fadeAnimation = tx;
				tx.play();
				if (togglePopup) {
					if (!popup.isShowing()) {
						if(popup.getOwnerWindow() != null) {
							popup.show(popup.getOwnerWindow());
						} else {
							popup.show(n2.getScene().getWindow());
						}
						
						if (n2 != null) {
							Rectangle2D primarySceneBounds = Screen.getPrimary().getVisualBounds();
							popup.setX((primarySceneBounds.getWidth() - popup.getWidth())/2);
							popup.setY(primarySceneBounds.getHeight() - popup.getHeight() - 10);
						}
					}
				}
			}
		});
	}
	
	/**
	 * validate text field as mandatory or not
	 */
	@SuppressWarnings("unchecked")
	public static boolean isMandatoryFieldEmpty(final Object textFieldName) {
		if(textFieldName instanceof UpperCaseFieldBean)
			return StringUtils.isEmpty(((UpperCaseFieldBean) textFieldName).getText().trim());
		else if(textFieldName instanceof LoggedTextArea)
			return StringUtils.isEmpty(((LoggedTextArea)textFieldName).getText().trim());
		else if (textFieldName instanceof LoggedComboBox<?>)
			return ((LoggedComboBox<String>) textFieldName).getSelectionModel().isEmpty();
		else if(textFieldName instanceof ListView<?>)
			return ((ListView<String>) textFieldName).getSelectionModel().isEmpty();
		else		
			return false;
	}
	
	/**
	 * validate textfield for deleting extra spaces
	 */
	public static String delMultipleSpaces(final Object textFieldName) {
		if(textFieldName instanceof UpperCaseFieldBean){
			String str = StringUtils.trim(((UpperCaseFieldBean) textFieldName).getText());
		    return str.replaceAll("\\s+", " ");
		}
		else if(textFieldName instanceof LoggedTextArea){
			String str = StringUtils.trim(((LoggedTextArea) textFieldName).getText());
			return str.replaceAll("\\s+", " ");
		}
		else
			return null;
		
	}
	
	/**
	 * validate textfield for restricting its length using KeyEvent
	 */
	public static EventHandler<KeyEvent> restrictLengthOfTextFields(final Integer limit) {
		return new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String text = StringUtils.EMPTY;
				int len;
				if(e.getSource() instanceof UpperCaseFieldBean){
					UpperCaseFieldBean textField = (UpperCaseFieldBean) e.getSource(); 
					text = textField.getText();  
					len=text.length();
					if (len>= limit) {                    
						e.consume();
						textField.settext(text.substring(0,limit));
					}
				}
				else if(e.getSource() instanceof LoggedTextArea){
					LoggedTextArea textArea = (LoggedTextArea) e.getSource(); 
					text = textArea.getText();
					len=text.length();
					if (len>= limit) {                    
						e.consume();
						textArea.setText(text.substring(0,limit));
					}
				}
				else if(e.getSource() instanceof LoggedComboBox){
					LoggedComboBox comboBox = (LoggedComboBox) e.getSource(); 
					text = comboBox.getEditor().getText();
					len=text.length();
					if (len>= limit) {                    
						e.consume();
						comboBox.setValue(text.substring(0,limit));
					}
				}

				else if(e.getSource() instanceof LoggedTextField){
					LoggedTextField textField = (LoggedTextField) e.getSource(); 
					text = textField.getText();
					len=text.length();
					if (len>= limit) {                    
						e.consume();
						textField.setText(text.substring(0,limit));
					}
				}
				
				else if(e.getSource() instanceof LoggedPasswordField){
					LoggedPasswordField password = (LoggedPasswordField) e.getSource(); 
					text = password.getText();
					len=text.length();
					if (len>= limit) {                    
						e.consume();
						password.setText(text.substring(0,limit));
					}
				}
			}
		};
	}

	 public String getClientOSName() {
		 String systemOSName = System.getProperty("os.name");
         String clientOS = null;
         systemOSName = systemOSName.toLowerCase(Locale.ENGLISH);
         if (systemOSName.contains("windows")) {
        	 clientOS = "WINDOWS";
         } else if (systemOSName.contains("linux")
                 || systemOSName.contains("mpe/ix")
                 || systemOSName.contains("freebsd")
                 || systemOSName.contains("irix")
                 || systemOSName.contains("digital unix")
                 || systemOSName.contains("unix")) {
        	 clientOS = "UNIX";
         } else if (systemOSName.contains("mac os")) {
        	 clientOS = "MAC";
         } else if (systemOSName.contains("sun os")
                 || systemOSName.contains("sunos")
                 || systemOSName.contains("solaris")) {
        	 clientOS = "POSIX_UNIX";
         } else if (systemOSName.contains("hp-ux") 
                 || systemOSName.contains("aix")) {
        	 clientOS = "POSIX_UNIX";
         } else {
        	 clientOS = "OTHER";
         }
         return clientOS;
     }
	
	 public String getClientOSVersion() {
		 String clientOSVersion = null;
		 
		 clientOSVersion = System.getProperty("os.version");
		 if(clientOSVersion.startsWith("10")) {
			 clientOSVersion = "Windows 10";
		 } else if(clientOSVersion.startsWith("6.3") || clientOSVersion.startsWith("6.2")) {
			 clientOSVersion = "Windows 8";
		 } else if(clientOSVersion.startsWith("6.1")) {
			 clientOSVersion = "Windows 7";
		 }
		 return clientOSVersion;
	 }
}

