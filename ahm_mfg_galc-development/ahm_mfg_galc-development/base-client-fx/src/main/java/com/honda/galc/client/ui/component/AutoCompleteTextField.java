package com.honda.galc.client.ui.component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;

/**
 * 
 * <h3>AutoCompleteTextField Class description</h3>
 * An extension to the LoggedTextField class that adds hooks to create AutoComplete Suggestions
 * in TextField
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
 * @author L&T Infotech<br>
 * Apr 20, 2017
 *
 */
public class AutoCompleteTextField extends UpperCaseFieldBean
{
	public static final int DEFAULT_SUGGESTION_LIST_SZ = 30;
	private List<String> suggestionList;
	private ContextMenu suggestionPopup;
	private int maxSuggestions = 10;
	
	public interface ISuggestionList  {
		public LinkedList<String>  getSuggestionList(List<String> sourceList, String newText);
		//isFlag is a general flag to set 
		public boolean isFlag();
	}
	
	private static ISuggestionList defaultSuggest =
		new ISuggestionList()  {
			public boolean isFlag() {return true;}
			public LinkedList<String> getSuggestionList(List<String> sourceList, String newText)  {
				LinkedList<String> searchResult = new LinkedList<String>();
				for (String e : sourceList) {
					if (e.toLowerCase().contains(newText)) {
						searchResult.add(e);
					}
				}
				return searchResult;
			}
		};

		private static ISuggestionList partOnlySuggest =
				new ISuggestionList()  {
					private volatile boolean flag = false;
					public boolean isFlag() {
						return flag;
					}
					public LinkedList<String> getSuggestionList(List<String> sourceList, String newText)  {
						String searchText = StringUtils.trimToEmpty(newText).toLowerCase();
						SortedSet<String> newSet = new TreeSet<String>();
						
						String modifiedFilter = StringUtils.trimToEmpty(newText).toLowerCase().replaceAll("%", ".*");
						Pattern pat = Pattern.compile(modifiedFilter);
						for (String e : sourceList) {
							Matcher mat = pat.matcher(e.toLowerCase());
							if (mat.find()) {
								String[] partDefect = e.split("@");
								newSet.add(partDefect[0].trim());
							}
						}

						return new LinkedList<String>(newSet);
					}
				};

	public AutoCompleteTextField(String id) {
		this(id, defaultSuggest);
	}

	public AutoCompleteTextField(String id, ISuggestionList suggest) {
		super(id);
		suggestionList = new ArrayList<String>();
		suggestionPopup = new ContextMenu();
		textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
				if (getText().length() == 0)
				{
					suggestionPopup.hide();
				} else
				{
					LinkedList<String> searchResult = suggest.getSuggestionList(suggestionList, getText());

					if (suggestionList.size() > 0)
					{
						populatePopup(searchResult);
						if (!suggestionPopup.isShowing())
						{
							suggestionPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
						}
					} else
					{
						suggestionPopup.hide();
					}
				}
			}
		});

		focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
				suggestionPopup.hide();
			}
		});

	}

	/**
	 * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
	 * @param searchResult The set of matching strings.
	 */
	private void populatePopup(List<String> searchResult) {
		List<CustomMenuItem> menuItems = new LinkedList<CustomMenuItem>();
		int count = Math.min(searchResult.size(), maxSuggestions);
	    for (int i = 0; i < count; i++)
		{
			final String result = searchResult.get(i);
			Label entryLabel = new Label(result);
			entryLabel.setPrefWidth(500);
			CustomMenuItem item = new CustomMenuItem(entryLabel, true);
			item.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent actionEvent) {
					setText(result);
					suggestionPopup.hide();
				}
			});
			menuItems.add(item);
		}
		suggestionPopup.getItems().clear();
		suggestionPopup.getItems().addAll(menuItems);
	}
	
	public List<String> getSuggestionList() {
		return suggestionList; 
	}

	public void setSuggestionList(List<String> entries) {
		this.suggestionList = entries;
	}

	public int getMaxSuggestions() {
		return maxSuggestions;
	}

	public void setMaxSuggestions(int maxSuggestions) {
		this.maxSuggestions = maxSuggestions;
	}
	
	public ContextMenu getSuggestionPopup() {
		return suggestionPopup;
	}
}
