package com.honda.galc.client.ui.component;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.honda.galc.common.logging.Logger;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>XmlTreePanel</code> is ... .
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
public class XmlTreePanel extends BorderPane {

    private TreeView<String> treeView;

    public XmlTreePanel() {
        initUi();
    }

    protected void initUi() {
        this.treeView = new TreeView<String>();
        setCenter(getTreeView());
    }

    // === data === //
    public void clearData() {
        getTreeView().setRoot(new TreeItem<String>());
    }

    public void setData(String xml) {
        TreeItem<String> rootItem = parseXml(xml);
        if (rootItem != null) {
            getTreeView().setRoot(rootItem);
        }
    }

    protected TreeItem<String> parseXml(String xml) {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = null;
        XMLReader reader = null;
        TreeItemCreationContentHandler contentHandler = new TreeItemCreationContentHandler();
        try {
            parser = parserFactory.newSAXParser();
            reader = parser.getXMLReader();
            reader.setContentHandler(contentHandler);
            reader.parse(new InputSource(new ByteArrayInputStream(xml.getBytes())));
        } catch (Exception e) {
            Logger.getLogger().error(e, "Failed to parse xml:" + xml);
            return new TreeItem<String>("Invalid Xml !");
        }
        TreeItem<String> item = contentHandler.getItem().getChildren().get(0);
        contentHandler.getItem().getChildren().clear();
        return item;
    }

    // === operations === //
    public void expandAll() {
        expandBranch(getTreeView().getRoot());
    }

    public void collapseAll() {
        collapseBranch(getTreeView().getRoot());
    }

    public void expandItem(TreeItem<String> item) {
        expandItem(item, true);
    }

    public void collapseItem(TreeItem<String> item) {
        expandItem(item, false);
    }

    public void expandItem(TreeItem<String> item, boolean expand) {
        if (item != null) {
            item.setExpanded(expand);
        }
    }

    public void expandBranch(TreeItem<String> item) {
        expandBranch(item, true);
    }

    public void collapseBranch(TreeItem<String> item) {
        expandBranch(item, false);
    }

    public void expandBranch(TreeItem<String> item, boolean expand) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(expand);
            for (TreeItem<String> child : item.getChildren()) {
                expandBranch(child, expand);
            }
        }
    }

    public void expandToRoot(TreeItem<String> item) {
        expandToRoot(item, true);
    }

    public void collapseToRoot(TreeItem<String> item) {
        expandToRoot(item, false);
    }

    public void expandToRoot(TreeItem<String> item, boolean expand) {
        if (item == null || item.getParent() == null) {
            return;
        }
        item.getParent().setExpanded(expand);
        expandToRoot(item.getParent(), expand);
    }

    public int getIndex(TreeItem<String> item) {
        int ix = 0;
        TreeItem<String> parent = null;
        while ((parent = item.getParent()) != null) {
            if (parent.isExpanded()) {
                ix++;
                ix = ix + parent.getChildren().indexOf(item);
            }
            item = parent;
        }
        return ix;
    }

    public TreeItem<String> findItem(String value) {
        return findItem(getTreeView().getRoot(), value);
    }

    public TreeItem<String> findItem(TreeItem<String> item, String value) {
        if (item == null) {
            return null;
        }
        if (item.getValue().equals(value)) {
            return item;
        }
        for (TreeItem<String> child : item.getChildren()) {
            TreeItem<String> s = findItem(child, value);
            if (s != null) {
                return s;
            }
        }
        return null;
    }

    public List<TreeItem<String>> searchItems(String filter) {
        List<TreeItem<String>> foundItems = new ArrayList<TreeItem<String>>();
        searchItems(getTreeView().getRoot(), filter, foundItems);
        return foundItems;
    }

    public List<TreeItem<String>> searchItems(TreeItem<String> item, String filter) {
        List<TreeItem<String>> foundItems = new ArrayList<TreeItem<String>>();
        searchItems(item, filter, foundItems);
        return foundItems;
    }

    public void searchItems(TreeItem<String> item, String filter, List<TreeItem<String>> foundItems) {
        if (item == null) {
            return;
        }
        if (filter == null) {
            return;
        }

        if (StringUtils.containsIgnoreCase(item.getValue(), filter)) {
            foundItems.add(item);
        }

        if (item.getChildren() == null || item.getChildren().size() == 0) {
            return;
        }
        for (TreeItem<String> child : item.getChildren()) {
            searchItems(child, filter, foundItems);
        }
    }

    // === get/set === //
    public TreeView<String> getTreeView() {
        return treeView;
    }

    // === content handler === //
    class TreeItemCreationContentHandler extends DefaultHandler {

        private TreeItem<String> item;

        public TreeItemCreationContentHandler() {
            item = new TreeItem<String>();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            this.item = this.item.getParent();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            String str = qName + processAttributes(attributes);
            TreeItem<String> item = new TreeItem<String>(str);
            this.item.getChildren().add(item);
            this.item = item;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String s = String.valueOf(ch, start, length).trim();
            if (!s.isEmpty()) {
                this.item.getChildren().add(new TreeItem<String>(s));
            }
        }

        protected String processAttributes(Attributes attributes) {
            if (attributes == null || attributes.getLength() == 0) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < attributes.getLength(); i++) {
                sb.append(" ").append(attributes.getQName(i)).append("=\"").append(attributes.getValue(i)).append("\"");
            }
            return "{" + sb.toString() + "}";
        }

        public TreeItem<String> getItem() {
            return item;
        }
    }
}