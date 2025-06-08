package com.honda.galc.client.dc.view;

import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.utils.UiFactory;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class PPEView {

//    private ClientContext context;
    private static boolean canceled = false;

    static class Dialog extends Stage {
        public Dialog( Stage owner, Scene scene) {
            setTitle( "PPE Data" );
            initStyle( StageStyle.UTILITY );
            initModality( Modality.APPLICATION_MODAL );
            initOwner( owner );
            setResizable( true );
            setScene( scene );
        }
        public void showDialog() {
            sizeToScene();
            centerOnScreen();
            showAndWait();
        }
    }
    
	
    public static boolean showPPE(Stage stage){
    	//owner = stage;
    	canceled = false;
    	boolean isCanceled = setContentPane(stage);
    	return isCanceled;
    }

    /**
     * This method initializes labelProductList	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private static boolean setContentPane(Stage stage) {

    	VBox vboxOuter = new VBox(10);
    	vboxOuter.setFillWidth(true);
    	vboxOuter.setSpacing(5);
    	Scene scene = new Scene( vboxOuter );
    	//vboxOuter.setAlignment( Pos.CENTER );
    	vboxOuter.setPrefSize(812, 640);
    	final Dialog dial = new Dialog( stage, scene );

    	Label label1 = UiFactory.createLabel("processMaintHistory", "Process Maintenance history");
    	label1.setStyle("-fx-font-size: 20;");
    	label1.setUnderline(true);
    	Label label2 = UiFactory.createLabel("ppe", "Personal Protection Equipment");
    	label2.setStyle("-fx-font-size: 20;");
    	label2.setUnderline(true);
    	vboxOuter.getChildren().add(label1);
    	vboxOuter.getChildren().add(getProcessMaintenancehistory());
    	vboxOuter.getChildren().add(label2);
    	vboxOuter.getChildren().add(getPPE());
    	//vboxOuter.getChildren().add(getButtonPanel(dial));
    	
    	Label label3 = UiFactory.createLabel("comments", "Comments:");
    	label3.setStyle("-fx-font-size: 20;");
    	label3.setUnderline(true);
    	
    	vboxOuter.getChildren().add(label3);
    	
		HBox hbox = new HBox();
		hbox.setSpacing(80);
		hbox.setAlignment( Pos.CENTER );
		TextArea textArea = UiFactory.createTextArea();
		textArea.setPrefSize(300,150);
		textArea.requestFocus();
		
		VBox vbox = new VBox();
		vbox.setSpacing(50);
	    Button buttonConfirm = UiFactory.createButton("CONFIRM");
	    buttonConfirm.setPrefWidth(300);
	    buttonConfirm.setStyle("-fx-font-size: 20;");
	    buttonConfirm.setOnAction( new EventHandler<ActionEvent>() {
            //@Override 
            public void handle( ActionEvent e ) {
            	//Set Current Unit Index
            	//getCurrentState().setCurrentPartIndex(jumpedToUnitIndex);
            	canceled = false;
                dial.close();
                //buttonSelected = Response.NO;
            }
        } );

	    Button buttonRequest = UiFactory.createButton("Request Clarification");
	    buttonRequest.setPrefWidth(300);
	    buttonRequest.setStyle("-fx-font-size: 20;");
	    buttonRequest.setOnAction( new EventHandler<ActionEvent>() {
            //@Override 
            public void handle( ActionEvent e ) {
            	//Set Current Unit Index
            	//getCurrentState().setCurrentPartIndex(jumpedToUnitIndex);
            	canceled = true;
                dial.close();
                //buttonSelected = Response.NO;
            }
        } );
	    //vbox.setSpacing(80);
	    vbox.getChildren().addAll(buttonConfirm,buttonRequest);
	    
	    hbox.getChildren().addAll(textArea,vbox);
	    
	    vboxOuter.getChildren().add(hbox);

	    
    	
    	dial.showDialog();
    	
    	return canceled;
    }


    private static ObservableList<HistoryData> getTableContent(){    
        ObservableList<HistoryData> tc= FXCollections.observableArrayList();
        tc.add(new HistoryData("MTO3052","12/17/2013", "08:28","EDIT","BODY LOCATION CHANGE/CORRECTION.CHANGE BODY LOCATION FOR INNOVATION.")); // add to ObservableList
        tc.add(new HistoryData("MTO3792","09/17/2013", "01:16","MASS ISSUE","QAD CAR.C/M 12293")); // add to ObservableList
        tc.add(new HistoryData("MTO3792","02/19/2013", "01:27","MASS ISSUE","2013 MDX MASS PRODUCTION 17594")); // add to ObservableList
        tc.add(new HistoryData("MCO29685","12/8/2011", "12:45","ADD","PPE INITIAL SET UP - PPE SET UP MS")); // add to ObservableList
        return tc;  
    }
    
    private static ObservableList<PPEData> getPPETableContent(){
        ObservableList<PPEData> tc= FXCollections.observableArrayList();
        tc.add(new PPEData("ANSI Z 87 SAFETY GLASSES",new PPEImage("ppeicon1.jpg"))); // add to ObservableList
        tc.add(new PPEData("EAR PLUGS",new PPEImage("ppeicon2.png"))); // add to ObservableList
        tc.add(new PPEData("LONG SLEEVE SHIRTS",new PPEImage("ppeicon3.jpg"))); // add to ObservableList
        tc.add(new PPEData("NON CUT RESISTANT GLOVES",new PPEImage("ppeicon4.jpg"))); // add to ObservableList
        tc.add(new PPEData("STEEL TOED SAFETY SHOES(NO STEEL EYELETS",new PPEImage("ppeicon5.jpg"))); // add to ObservableList
        return tc;  
    }

    @SuppressWarnings("unchecked")
	private static TableView<HistoryData> getProcessMaintenancehistory() {

		
        TableColumn<HistoryData,String> userNameCol = UiFactory.createTableColumn(HistoryData.class ,String.class);
        userNameCol.setText("User Name");
        userNameCol.setPrefWidth(100);
        //unitNameCol.setResizable(false);
        //unitNameCol.setMaxWidth(300);
        userNameCol.setCellValueFactory(new PropertyValueFactory<HistoryData,String>("username"));
        
        
        
        TableColumn<HistoryData,String> dateCol = UiFactory.createTableColumn(HistoryData.class,String.class);
        dateCol.setText("date");
        dateCol.setPrefWidth(100);
        dateCol.setCellValueFactory(new PropertyValueFactory<HistoryData,String>("date"));

        
		TableColumn<HistoryData,String> timeCol = UiFactory.createTableColumn(HistoryData.class,String.class);
		timeCol.setText("Time");
		timeCol.setPrefWidth(100);
		timeCol.setCellValueFactory(new PropertyValueFactory<HistoryData,String>("time"));
		
		TableColumn<HistoryData,String> actionCol = UiFactory.createTableColumn(HistoryData.class, String.class);
		actionCol.setText("Action");
		actionCol.setPrefWidth(100);
		actionCol.setCellValueFactory(new PropertyValueFactory<HistoryData,String>("action"));

		TableColumn<HistoryData,String> reasonCol = UiFactory.createTableColumn(HistoryData.class, String.class);
		reasonCol.setText("Reason for change");
		reasonCol.setPrefWidth(400);
		reasonCol.setCellValueFactory(new PropertyValueFactory<HistoryData,String>("reason"));
        //wrap long reason
		reasonCol.setCellFactory(new Callback<TableColumn<HistoryData,String>, TableCell<HistoryData,String>>() {
            //@Override
            public LoggedTableCell<HistoryData, String> call( TableColumn<HistoryData, String> param) {
                 final LoggedTableCell<HistoryData, String> cell = new LoggedTableCell<HistoryData, String>() {
                      private Text text;
                      @Override
                      public void updateItem(String item, boolean empty) {
                           super.updateItem(item, empty);
                           if (!isEmpty()) {
                                text = UiFactory.createText(item);
                                text.setWrappingWidth(400); // Setting the wrapping width to the Text
                                setGraphic(text);
                           }
                      }
                 };
                 return cell;
            }
       });
		
		TableView<HistoryData> tableView = UiFactory.createTableView(HistoryData.class);
		tableView.setPrefWidth(812);
		tableView.setPrefHeight(171);
		tableView.setStyle("-fx-font-size: 16;");
//		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.getColumns().addAll(userNameCol, dateCol,timeCol,actionCol,reasonCol);
        tableView.setItems(getTableContent());
        return tableView;
	}
	

	
	@SuppressWarnings("unchecked")
	private static TableView<PPEData> getPPE() {
		
		

		
        TableColumn<PPEData,String> requireCol = UiFactory.createTableColumn(PPEData.class, String.class);
        requireCol.setText("PPE Required");
        requireCol.setPrefWidth(400);
        //unitNameCol.setResizable(false);
        //unitNameCol.setMaxWidth(300);
        requireCol.setCellValueFactory(new PropertyValueFactory<PPEData,String>("require"));
        
        
        
        TableColumn<PPEData,PPEImage> imageCol = UiFactory.createTableColumn(PPEData.class, PPEImage.class);
        imageCol.setText("Images");
        imageCol.setPrefWidth(400);
        imageCol.setCellValueFactory(new PropertyValueFactory<PPEData,PPEImage>("images"));
        imageCol.setCellFactory(new Callback<TableColumn<PPEData,PPEImage>,TableCell<PPEData,PPEImage>>(){        
            //@Override
            public LoggedTableCell<PPEData,PPEImage> call(TableColumn<PPEData,PPEImage> param) {                
            	LoggedTableCell<PPEData,PPEImage> cell = new LoggedTableCell<PPEData,PPEImage>(){
                    @Override
                    public void updateItem(PPEImage item, boolean empty) {                        
                        if(item!=null){                            
                            HBox box= new HBox();
                            box.setSpacing(10) ;

                            ImageView imageview = new ImageView();
                            imageview.setFitHeight(25);
                            imageview.setFitWidth(25);
                            
                            imageview.setImage(new Image("resource/com/honda/galc/client/fx/"+item.getFilename())); 

                            box.getChildren().addAll(imageview); 
                            //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                            setGraphic(box);
                        }
                    }
                };
                System.out.println(cell.getIndex());               
                return cell;
            }

        });


		TableView<PPEData> tableView = UiFactory.createTableView(PPEData.class);
		//tableView.autosize();
		tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		tableView.setPrefWidth(812);
		tableView.setPrefHeight(197);
		tableView.setStyle("-fx-font-size: 16;");
		
		//tableView.getSelectionModel().clearAndSelect(100,  null );
		//tableView.requestFocus();
		tableView.getColumns().addAll(requireCol, imageCol);
        tableView.setItems(getPPETableContent());
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //tableView.getFocusModel().focus(20);
        //tableView.getSelectionModel().select(20);
        
        //tableView.getFocusModel().focus(10);
		//scrollToIndex(tableView,20);
        //int numRows = tableView.getItems().size();
		//tableView.scrollTo(0);
		//tableView
		//tableView.getSelectionModel().select(10);
		//tableView.getSelectionModel().select(20);
     
        //Platform.runLater();
        return tableView;
	}

	
	public static boolean isCanceled() {
		return canceled;
	}


	public static class HistoryData {  
	    private SimpleStringProperty username;  
	    private SimpleStringProperty date;
	    private SimpleStringProperty time;
	    private SimpleStringProperty action;
	    private SimpleStringProperty reason;
	    
	  
	    public HistoryData(String username, String date, String time, String action, String reason){  
	        this.username=new SimpleStringProperty(username);  
	        this.date=new SimpleStringProperty(date);  
	        this.time=new SimpleStringProperty(time);  
	        this.action=new SimpleStringProperty(action);  
	        this.reason=new SimpleStringProperty(reason);  
	    }  
	      
	    public String getUsername() {  
	        return username.get();  
	    }  
	  
	    public void setUsername(String username) {  
	       this.username=new SimpleStringProperty(username);  
	    }  
	  
	    /** 
	     * @return the date 
	     */  
	    public String getDate() {  
	        return date.get();  
	    }  
	  
	    /** 
	     * @param  the date to set 
	     */  
	    public void setDate(String date) {  
	        this.date=new SimpleStringProperty(date);  
	    }  
	      
	    /** 
	     * @return the time 
	     */  
	    public String getTime() {  
	        return time.get();  
	    }  
	  
	    /** 
	     * @param  the time to set 
	     */  
	    public void setTime(String time) {  
	        this.time=new SimpleStringProperty(time);  
	    }  

	    /** 
	     * @return the action 
	     */  
	    public String getAction() {  
	        return action.get();  
	    }  
	  
	    /** 
	     * @param  the action to set 
	     */  
	    public void setAction(String action) {  
	        this.action=new SimpleStringProperty(action);  
	    }  
	
	    /** 
	     * @return the reason 
	     */  
	    public String getReason() {  
	        return reason.get();  
	    }  
	  
	    /** 
	     * @param  the reason to set 
	     */  
	    public void setReason(String reason) {  
	        this.reason=new SimpleStringProperty(reason);  
	    }  

	}  


	public static class PPEData {  
	    private SimpleStringProperty require = new SimpleStringProperty();
	    private ObjectProperty images = new SimpleObjectProperty();
	  
	    public PPEData(String require, PPEImage images){  
	    	setRequire(require);  
	    	setImages(images);  
	    }  
	      
	    public String getRequire() {  
	        return require.get();  
	    }  
	  
	    public void setRequire(String require) {  
	       this.require.set(require);
	    }  
	  
	    /** 
	     * @return the images 
	     */  
	    public Object getImages() {  
	        return images.get();  
	    }  
	  
	    /** 
	     * @param  the images to set 
	     */  
	    public void setImages(PPEImage images) {  
	        this.images.set(images);  
	    }  

	}  

	
	public static class PPEImage {  
		//Attributes
		 private String filename;
		 
		 public PPEImage(String filename) {
	         this.filename = filename;
	     }

		 public String getFilename() {
	         return filename;
	     }
			 
	     public void setFilename(String filename) {
	         this.filename = filename;
	     }
	}
	
}
