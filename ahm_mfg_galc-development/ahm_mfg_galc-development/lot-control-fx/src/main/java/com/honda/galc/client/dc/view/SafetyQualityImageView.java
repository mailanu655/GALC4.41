package com.honda.galc.client.dc.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;

import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.pdda.UnitSafetyImageDao;
import com.honda.galc.entity.pdda.UnitSafetyImage;
import com.honda.galc.service.ServiceFactory;

public class SafetyQualityImageView {
	
	private static final String confirmImageUrl = "/resource/images/common/confirm.png";


    static class Dialog extends Stage {
        public Dialog( Stage owner, Scene scene) {
            setTitle( "Safety Quality Image" );
            initStyle( StageStyle.UNDECORATED);
            initModality( Modality.APPLICATION_MODAL );
            initOwner( owner );
            setResizable( false );
            setScene( scene );
        }
        public void showDialog() {
            sizeToScene();
            //centerOnScreen();
            setY(155);
            setX(280);
            
            System.out.println("width" + this.getWidth() );
            showAndWait();
        }
    }
    
	
    public static void showImage(Stage stage,String unitno) throws SQLException, IOException{
    	setContentPane(stage,unitno);
    }

    /**
     * This method initializes ContentPane	
     * 	
     * @return javax.swing.JScrollPane	
     * @throws IOException 
     * @throws SQLException 
     */
    private static void setContentPane(Stage stage, String unitno) throws SQLException, IOException {
    	
    	final VBox vboxOuter = new VBox(10);
    	
    	vboxOuter.setStyle("-fx-border-style: solid; -fx-border-color: gray;-fx-border-width: 3px;  -fx-border-radius: 2px;");
    	
    	final HBox hbox = new HBox();
    	//vboxOuter.setPadding(new Insets(0, 0, 10, 0));
    	Scene scene = new Scene( vboxOuter );
    	
    	UnitSafetyImageDao installedPartDao = ServiceFactory.getService(UnitSafetyImageDao.class);
		List<UnitSafetyImage> pddaimage = new ArrayList<UnitSafetyImage>();
		final double width = 255;
		final double maxwidth = 510;
		double maxheight = 340;
		pddaimage = installedPartDao.findAllSafetyImages(Integer.valueOf(unitno));
		
		final TilePane tilePane = new TilePane();
		tilePane.setPrefColumns(2); 
		
		double imageMaxwidth = 0;
		double imageMaxheight = 0;
		if ( null != pddaimage ) {
			
			for(int i = 0;i<pddaimage.size();i++) {
				final int ii = i;
				final Image	image = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(pddaimage.get(i).getImage())),null);
				if (image.getWidth()>imageMaxwidth) imageMaxwidth = image.getWidth();
				if (image.getHeight()>imageMaxheight) imageMaxheight = image.getHeight();
				final ImageView iv = new ImageView();
				
				iv.setPreserveRatio(true);
				iv.setSmooth(true); 
				iv.setCache(true);
				if (pddaimage.size()==1){
					if (image.getWidth()>maxwidth) {
						iv.setFitWidth(maxwidth);
						//maxheight = iv.getFitHeight();
					}
					else iv.setFitWidth(image.getWidth());
				} else {
					iv.setFitWidth(width);
					iv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			            //@Override
			            public void handle(MouseEvent me) {
			            	if (image.getWidth()!=iv.getFitWidth()&&iv.getFitWidth()!=maxwidth) {
			            		if (image.getWidth()>maxwidth) iv.setFitWidth(maxwidth);
			            		else iv.setFitWidth(image.getWidth());
			            		vboxOuter.getChildren().remove(tilePane);
			            		vboxOuter.getChildren().remove(hbox);
			            		vboxOuter.getChildren().add(iv);
			            		vboxOuter.getChildren().add(hbox);
			            	}
			            	else {
			            		iv.setFitWidth(width);
			            		vboxOuter.getChildren().remove(iv);
			            		vboxOuter.getChildren().remove(hbox);
			            		tilePane.getChildren().add(ii,iv);
			            		vboxOuter.getChildren().add(tilePane);
			            		vboxOuter.getChildren().add(hbox);
			            	}
			            }
			        });
				}
				iv.setImage(image);
				iv.setPreserveRatio(true);
				tilePane.getChildren().add(iv);
			}
		}
		
		//tilePane.setAlignment(Pos.CENTER);
		vboxOuter.getChildren().add(tilePane);
		if (imageMaxwidth>maxwidth)imageMaxwidth=maxwidth;
		if (imageMaxheight>maxheight)imageMaxheight=maxheight;
		vboxOuter.setPrefSize(imageMaxwidth+10, imageMaxheight+10);
		vboxOuter.setMaxSize(imageMaxwidth+10, imageMaxheight+10);
    	final Dialog dial = new Dialog( stage, scene );
		hbox.setAlignment( Pos.CENTER );
	    Button buttonClose = UiFactory.createButton("Close");
		ImageView confirmImage;
		confirmImage = new ImageView(confirmImageUrl);
		buttonClose.setGraphic(confirmImage);
	    buttonClose.setStyle("-fx-font-size: 22; ");
	    buttonClose.setOnAction( new EventHandler<ActionEvent>() {
            //@Override 
            public void handle( ActionEvent e ) {
                dial.close();
            }
        } );
	    hbox.getChildren().addAll(buttonClose);
	    HBox.setMargin(buttonClose, new Insets(5,0,5,0));
	    vboxOuter.getChildren().add(hbox);
    	dial.showDialog();
    }



}


