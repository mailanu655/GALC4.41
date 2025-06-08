package com.honda.galc.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
/**
 * <h3>TileListView Class description</h3>
 * <p> TileListView is basically a container which behaves as a control. It is a ListView kind of control which adds 
 * Items in a Tiled manner </p>
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
 * @author L&T Infotech<br>
 * Mar 31, 2017
 *
 */
public class TileListView<T extends Object> extends TilePane{
	
	private Node node;
	private Node selectedNode; 
	private List<T> items;
	private T selectedItem;
	private AbstractTileListViewBehaviour<T> behaviour;
	
	public TileListView() {
		super();
		items = new ArrayList<T>();
	}
	
	/**
	 * This method is used to populate Tile ListView with desired nodes
	 * @param behaviour
	 */
	private void populateTileListView() {
		for(T item : items) {
			behaviour.setBehaviour(item);
			Node clonedNode = node;
			clonedNode.setUserData(item);
			clonedNode.setCursor(Cursor.DEFAULT);
			setSelectedItemListener(clonedNode, item);
			this.getChildren().add(clonedNode);
		}
	}
	/**
	 * This method is used to add listener to the added item
	 * @param node
	 * @param behaviour
	 */
	@SuppressWarnings("unchecked")
	private void setSelectedItemListener(final Node node, final T item) {
		node.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue && selectedNode!=null) {
					clearNodeStyle();
					selectedNode.setStyle("-fx-background-color: silver");
				}
			}
		});
		node.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(selectedNode!=null && node.equals(selectedNode)) {
					clearSelection();
				} else {
					selectedItem = (T) node.getUserData();
					selectedNode = node;
					clearNodeStyle();
					selectedNode.setStyle("-fx-background-color: -fx-focus-color");
				}
				behaviour.addListener(item);
			}
		});
	}
	/**
	 * This method is used to clear the focus from the node
	 */
	private void clearNodeStyle() {
		ObservableList<Node> nodeList = this.getChildren();
		for(Node node : nodeList) {
			node.setStyle("-fx-background-color: white; -fx-text-fill: black");
		}
	}
	/**
	 * This method is used to clear selection from the control
	 */
	public void clearSelection() {
		selectedItem = null;
		selectedNode = null;
		clearNodeStyle();
	}
	/**
	 * This method is used to remove all the items from the control
	 */
	public void clear() {
		ObservableList<Node> children = this.getChildren();
		this.items.clear();
		this.getChildren().removeAll(children);
	}
	
	@SuppressWarnings("unchecked")
	public void selectFirst() {
		ObservableList<Node> nodeList = this.getChildren();
		if(!nodeList.isEmpty()) {
			selectedNode = nodeList.get(0);
			selectedItem = (T) selectedNode.getUserData();
			clearNodeStyle();
			selectedNode.setStyle("-fx-background-color: -fx-focus-color");
			behaviour.addListener(selectedItem);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void select(T item) {
		ObservableList<Node> nodeList = this.getChildren();
		for(Node node : nodeList) {
			T nodeItem = (T)node.getUserData();
			if(nodeItem.equals(item)) {
				selectedNode = node;
				selectedItem = (T) selectedNode.getUserData();
				clearNodeStyle();
				selectedNode.setStyle("-fx-background-color: -fx-focus-color");
				behaviour.addListener(selectedItem);
				break;
			}
		}
	}
	
	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	
	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		clear();
		this.items = items;
		populateTileListView();
	}

	public T getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(T selectedItem) {
		this.selectedItem = selectedItem;
	}

	public Node getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(Node selectedNode) {
		this.selectedNode = selectedNode;
	}

	public void setBehaviour(AbstractTileListViewBehaviour<T> behaviour) {
		this.behaviour = behaviour;
	}
	
	public boolean isSelected() {
		return (this.selectedItem != null && this.selectedNode != null);
	}
}
