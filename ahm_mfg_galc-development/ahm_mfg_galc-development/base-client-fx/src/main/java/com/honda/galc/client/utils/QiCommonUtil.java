package com.honda.galc.client.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedPasswordField;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.entity.conf.UserSecurityGroupId;
import com.honda.galc.util.LDAPService;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>QICommonUtil</h3> <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * QICommonUtil description
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author LnT Infotech May 4, 2016
 * 
 */
public class QiCommonUtil {



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
		else if (textFieldName instanceof LoggedTextField)
			return StringUtils.isEmpty(((LoggedTextField) textFieldName).getText().trim());
		else		
			return false;
	}

	/**
	 * validate textfield for special characters
	 */
	public static boolean hasSpecialCharacters(final Object textFieldName) {

		Pattern regex = Pattern.compile("^[A-Z0-9\\s]*$");
		Matcher matcher = null;
		if(textFieldName instanceof String)  {
			return hasSpecialCharactersString((String)textFieldName);
		}
		if(textFieldName instanceof UpperCaseFieldBean)
			matcher = regex.matcher(StringUtils.trim(((UpperCaseFieldBean) textFieldName).getText()));
		else if(textFieldName instanceof LoggedTextArea)
			matcher = regex.matcher(StringUtils.trim(((LoggedTextArea)textFieldName).getText()));

		if (!matcher.find()) {
			return true;
		}
		return false;

	}

	/**
	 * validate textfield for special characters
	 */
	public static boolean hasSpecialCharactersString(final String inputString) {

		Pattern regex = Pattern.compile("^[A-Z0-9\\s]*$");
		Matcher matcher = null;
		String s = inputString;
		if(s == null)  s = "";
		matcher = regex.matcher(StringUtils.trim(s));
		if (!matcher.find()) {
			return true;
		}
		return false;

	}

	/**
	 * validate textfield for special characters
	 */
	public static void publishError(String message) {

		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.ERROR));

	}

	/**
	 * publish event to clear status message
	 */
	public static void publishClear() {
		EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
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

	/**
	 * validate textfield to check non numeric values
	 */
	public static boolean isNumberTextMaximum(final Object dataType,final LoggedTextField textField){
		String newValue = checkNumericInput(StringUtils.trim(textField.getText()));
		StringUtils.isNumeric(StringUtils.trim(textField.getText()));
		textField.setText(newValue);
		if(dataType instanceof Integer){
			int newIntValue = StringUtils.isEmpty(newValue) ? 0 : Integer.parseInt(newValue);
			if (newIntValue > Short.MAX_VALUE) {
				clearTextComponent(textField);
				return true;
			}
		}else if(dataType instanceof Long){
			long newLongValue = StringUtils.isEmpty(newValue) ? 0 : Long.parseLong(newValue);
			if (newLongValue > Integer.MAX_VALUE) {
				clearTextComponent(textField);
				return true;
			}
		}
		return false;
	}
	/**
	 * This method is used to clear Text Field
	 * @param textField
	 */
	private static void clearTextComponent(LoggedTextField textField) {
		textField.clear();
		textField.setText(String.valueOf(0));
	}

	/**
	 * @param newValue
	 * @return
	 */
	public static String checkNumericInput(String newValue) {
		if (!newValue.matches("\\d+") && newValue.length() > 0) {
			return newValue.substring(0, newValue.length() - 1);
		}
		return newValue;
	}
	
	/**
	 * @param newValue
	 * @return
	 */
	public static Boolean isNumericInput(String newValue) {
		if (!newValue.matches("\\d+") && newValue.length() > 0)	
			return false;
		return true;
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
	 * This method is used to remove duplicate items from an ArrayList
	 * @param arrayList
	 * @return
	 */
	public static <T extends Object>List<T> getUniqueArrayList(List<T> arrayList) {
		List<T> uniqueDtoList = new ArrayList<T>(new HashSet<T>(arrayList));
		return uniqueDtoList;
	}
	
	/**
	 * This method is used to check if the fields are empty. Requires id set for the field to display field specific error message.
	 * @param arrayList
	 * @return
	 */
	public static String checkMandatoryFields(List<Object> checkMandatoryFieldList) {
		for(Object field:checkMandatoryFieldList){
			if((field instanceof UpperCaseFieldBean) && StringUtils.isEmpty(((UpperCaseFieldBean) field).getText().trim()))
				 return ((UpperCaseFieldBean) field).getId()!=null?"Please enter "+((UpperCaseFieldBean) field).getId():"Mandatory Field is empty";
			else if((field instanceof LoggedTextArea) && StringUtils.isEmpty(((LoggedTextArea)field).getText().trim()))
				return ((LoggedTextArea) field).getId()!=null?"Please enter "+((LoggedTextArea) field).getId():"Mandatory Field is empty";
			else if( (field instanceof LoggedComboBox<?>) && ((LoggedComboBox<String>) field).getSelectionModel().isEmpty())
				return ((LoggedComboBox<String>) field).getId()!=null?"Please enter "+((LoggedComboBox<String>) field).getId():"Mandatory Field is empty";
			else if((field instanceof ListView<?>) && ((ListView<String>) field).getSelectionModel().isEmpty())
				return ((ListView<String>) field).getId()!=null?"Please enter "+((ListView<String>) field).getId():"Mandatory Field is empty";
			else if( (field instanceof ComboBox<?>) && ((ComboBox<String>) field).getSelectionModel().isEmpty())
				return ((ComboBox<String>) field).getId()!=null?"Please enter "+((ComboBox<String>) field).getId():"Mandatory Field is empty";
		}
		return "false";
	}
	
	/**
	 * This method creates a  message.
	 * @param pdcCount
	 * @param regionalAttributeCount
	 * @return message
	 */
	public static StringBuilder getMessage(String screenName,int pdcCount,int regionalAttributeCount,int imageSectionCount) {
		StringBuilder message = new StringBuilder(); 
		if(pdcCount>0 || regionalAttributeCount>0||imageSectionCount>0){
			message.append("The "+screenName+" being updated affects ");
			if(pdcCount>0){
				message.append(pdcCount+" Part Defect Combination");
			}
			if(regionalAttributeCount>0){
				if(pdcCount>0)
					message.append(", ");
				message.append(regionalAttributeCount + " Regional Attribute Maintenance(s)");
			}
			if(imageSectionCount>0){
				if(pdcCount>0 || regionalAttributeCount>0)
					message.append(", ");
				message.append(imageSectionCount+ " Image Section(s)");
			}
			message.append(". Do you still want to continue?");
		}
		return message;
	}
	
	/**
	 * validate text field for float values
	 */
	public static boolean isDecimalField(final Object textFieldName) {

		Pattern regex = Pattern.compile("\\d+(?:\\.\\d+)?");
		Matcher matcher = null;
		if(textFieldName instanceof UpperCaseFieldBean)
			matcher = regex.matcher(StringUtils.trim(((UpperCaseFieldBean) textFieldName).getText()));
		else if(textFieldName instanceof LoggedTextArea)
			matcher = regex.matcher(StringUtils.trim(((LoggedTextArea)textFieldName).getText()));

		if (!matcher.find()) {
			return true;
		}
		return false;

	}
	
	public static Date convert(Date date) {
		if (date instanceof Timestamp) {
			Timestamp ts = (Timestamp) date;
			long secs = ts.getTime() / 1000;
			double milsDouble = ts.getNanos() / 1000000d;
			long mils = Math.round(milsDouble);
			long timeInMils = secs * 1000 + mils;
			return new Date(timeInMils);
		} else {
			return date;
		}
	}
	
	public static boolean isUserInDataCorrectionLimitedGroup() {
		boolean sgPartOfGroup = false; 
		QiPropertyBean qiPropertyBean = PropertyService.getPropertyBean(QiPropertyBean.class);
		String secGrp = qiPropertyBean.getDataCorrectionLimited();
		if(!StringUtils.isBlank(secGrp)) {
			String sgUserId = ClientMainFx.getInstance().getAccessControlManager().getUserName();
			List<UserSecurityGroupId> userSecGroup = LDAPService.getInstance().getMemberOf(sgUserId);
			for(UserSecurityGroupId lpGrp : userSecGroup) {
				if(lpGrp.getSecurityGroup().trim().equalsIgnoreCase(secGrp.trim())) {
					sgPartOfGroup = true;
					break;
					}
			}
		}
		return sgPartOfGroup;
	}
}
	
	

