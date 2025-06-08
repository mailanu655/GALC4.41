package com.honda.galc.client.datacollection.view.info;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter.HighlightPainter;

import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.utils.ColorUtil;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>LogInfoPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LogInfoPanel description </p>
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
 * @author Paul Chou
 * May 7, 2010
 *
 */
public class LogInfoPanel extends JSplitPane 
{
	private static final long serialVersionUID = 1L;
	private LabeledListBox logItemList;
	private String title;
	private JScrollPane logDetailsPanel;
	private JTextArea logDetailTextArea;
	private Highlighter highlighter;
	private String mark = " {";
	private int LENGTH_OF_DATETIME = 31;
	private Map<HighlightKeywords, HighlightPainter> highlightPainters;
	private enum HighlightKeywords{emergency("red"), error("pink"), warning("yellow");
		private String color;
		HighlightKeywords(String color){
			this.color = color;
		}
		public String getColor(){return color;}
	};


	public LogInfoPanel(String title, String logLevel) {
		super();
		this.title = title;
		initialize();
	}

	private void initialize() {
		try {
			this.setPreferredSize(new Dimension(LotControlSystemInfo.WIN_WIDTH,
					LotControlSystemInfo.WIN_HIGHT - LotControlSystemInfo.BOTTOM_PANEL_HIGHT * 3));
			this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			this.setDividerSize(1);
			setLeftComponent(getLogItemList());
			setRightComponent(getLogDetailPanel());
			setDividerLocation(150);
			initHighlighter();
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception initialize LogInfoPanel");
		}
		
	}

	private void initHighlighter() {
		highlightPainters = new HashMap<HighlightKeywords, HighlightPainter>();
		highlighter = new DefaultHighlighter();
		for(HighlightKeywords hk: HighlightKeywords.values()){
			highlightPainters.put(hk, new DefaultHighlightPainter(ColorUtil.getColor(hk.getColor())));
		}
		logDetailTextArea.setHighlighter(highlighter);
	}

	private JScrollPane getLogDetailPanel() {
		 if (logDetailsPanel == null) {
    		 logDetailsPanel = new JScrollPane(getInformationTextArea());
         }
		return logDetailsPanel;
	}

	public JTextArea getInformationTextArea() {
		if (logDetailTextArea == null) {
			logDetailTextArea = new JTextArea(40, 80);
			logDetailTextArea.setLineWrap(true);
			logDetailTextArea.setWrapStyleWord(true);
			logDetailTextArea.setAutoscrolls(true);
			logDetailTextArea.setVisible(true);
		}
		return logDetailTextArea;
	}


	public LabeledListBox getLogItemList() {
		if(logItemList == null){
			logItemList = new LabeledListBox(title);
			logItemList.getLabel().setAlignmentX(CENTER_ALIGNMENT);
			logItemList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			logItemList.getComponent().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);	
		}
		
		return logItemList;
	}

	public void setText(String text) {
		
		logDetailTextArea.setText(text);
		
		try {
			hightlight(text);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to hight client log keywards.");
		}
		
		
	}

	private void hightlight(String text) throws BadLocationException {
		highlighter.removeAllHighlights();
		highlightText(text,  0);
		
	}

	private void highlightText(String text, int pos) throws BadLocationException {
		for(HighlightKeywords keyword : HighlightKeywords.values()){
			int start = text.indexOf(keyword.toString() + mark, pos);
			while(start > 0){
				int end = start + keyword.toString().length();
				int end1 = text.indexOf(System.getProperty("line.separator"), end);
				highlighter.addHighlight(start - LENGTH_OF_DATETIME, end1, getPainter(keyword));
				start =  text.indexOf(keyword.toString() + mark, end1);
			}
		}
	}

	private HighlightPainter getPainter(HighlightKeywords keyword) {
		return highlightPainters.get(keyword);
	}

	public Highlighter getHighlighter() {
		return highlighter;
	}

	public void apend(LogRecord event) {
		try {
			int end = logDetailTextArea.getText().length();
			logDetailTextArea.append(event.getSingleLineMessage() + System.getProperty("line.separator"));
			highlightText(logDetailTextArea.getText(), end);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception on highlight appending text.");
		}
	}


}
