package com.honda.galc.client.gts.figure;

import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.enumtype.GtsCarrierType;
import com.honda.galc.entity.enumtype.GtsOrientation;
import com.honda.galc.entity.gts.GtsColorMap;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsOutlineMap;


public class ImageFactory {
	private static ImageFactory imageFactory = new ImageFactory();
	private  final List<Image> images = new ArrayList<Image>();
    private List<GtsColorMap> colorMaps = new ArrayList<GtsColorMap>();
    private BufferedImage snailImage = getImageFromResource("images/Snail.PNG");
    private BufferedImage ghostImage = getImageFromResource("images/GHOST.PNG");
    private BufferedImage dummyImage = getImageFromResource("images/dummy.PNG");
    private BufferedImage scrapImage = getImageFromResource("images/Scrap.PNG");
    private BufferedImage scrap1Image = getImageFromResource("images/Scrap1.PNG");
    private BufferedImage scrap2Image = getImageFromResource("images/Scrap2.PNG");
    
    private BufferedImage eastDummyImage = flip(cloneImage(dummyImage));
    
	public static ImageFactory getInstance(){
     return imageFactory;   
    }
    
    public BufferedImage getBufferedImage(GtsLaneCarrier laneCarrier){
    	if(StringUtils.isEmpty(laneCarrier.getLaneCarrier())) return ghostImage;
        
        if(laneCarrier.getLaneCarrier().equals("Dummy"))return getDummyImage(laneCarrier.getHorizontalDirection());
        
        if(laneCarrier.getCarrier() != null) {
        	if (laneCarrier.getCarrier().getCarrierType() == GtsCarrierType.SCRAP) return scrapImage;
        	else if (laneCarrier.getCarrier().getCarrierType() == GtsCarrierType.SCRAP1) return scrap1Image;
        	else if (laneCarrier.getCarrier().getCarrierType() == GtsCarrierType.SCRAP2) return scrap2Image;
        }
        
        if(laneCarrier.getProductId() == null || laneCarrier.getProductId().length() == 0) return null;
        
        if(laneCarrier.getProduct() == null) return ghostImage;
            
        Image image = getImage(laneCarrier.getModelCode());
        
        if(image == null || image.getBufferedImage() == null) return getDummyImage(laneCarrier.getHorizontalDirection());
        
        return image.transform(getColorMap(laneCarrier.getExtColorCode()),laneCarrier.getHorizontalDirection(),laneCarrier.getDiscrepancyStatus());

    }
        
    public void initilizeOutlineMap(List<GtsOutlineMap> maps){
        images.clear();
        for(GtsOutlineMap map:maps){
            images.add(new Image(map));
        }
    }
    
    public  void initilizeColorMap(List<GtsColorMap> maps){
       colorMaps = maps;
    }
    
    public void updateColorMap(GtsColorMap map){
        removeColorMap(map.getId().getColorId());
        colorMaps.add(map);
    }
    
    public void removeColorMap(int id){
        GtsColorMap oldMap = findColorMap(id);
        if(oldMap != null){
            colorMaps.remove(oldMap);
        };
    }
    
    public void updateOutlineMap(GtsOutlineMap map){
        removeOutlineMap(map);
        images.add(new Image(map));
    }
    
    public void removeOutlineMap(GtsOutlineMap map){
            images.removeAll(findImages(map));
    }
    
    private GtsColorMap findColorMap(int id){
        for(GtsColorMap map:colorMaps){
            if(map.getId().getColorId() == id) return map;
        }
        return null;
    }
    
  
    private List<Image> findImages(GtsOutlineMap map){
        List<Image> imgs = new ArrayList<Image>();
        for(Image image:images){
            if(image.getOutlineMap().getModelCode().equals(map.getModelCode()))
                imgs.add(image);
        }
        return imgs;
    }
     

    
    private Image getImage(String modelCode){
        for (Image image:images){
            if(image.match(modelCode)) return image;
        }
        return null;
    }
    
    private GtsColorMap getColorMap(String colorCode){
        for(GtsColorMap map:colorMaps){
            if(map.getColorCode().equals(colorCode)) return map;
        }
        return null;
    }
    
    public BufferedImage getSnailImage(){
        return snailImage;
    }
    
    public BufferedImage getDummyImage(){
        return dummyImage;
    }
    
    public BufferedImage getDummyImage(GtsOrientation direction){
        if(direction != GtsOrientation.EAST)return dummyImage;
        else return eastDummyImage;
    }
    
    public static BufferedImage getImageFromResource(String resourceURL){
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(ImageFactory.class.getResource(resourceURL));
        MediaTracker tracker = new MediaTracker(new JLabel());
        tracker.addImage(image, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException ex) {};       
        BufferedImage buf = new BufferedImage(image.getWidth(null), image.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        image.flush();
        return buf;
    }
    
    public static BufferedImage cloneImage(BufferedImage image) 
    {
        if(image == null) return null;
        
        BufferedImage clone = new BufferedImage(image.getColorModel(), 
                        image.getData().createCompatibleWritableRaster(),
                        image.isAlphaPremultiplied(), null);
       clone.setData(image.copyData(clone.getRaster()));
       return clone;     
    }
    
    /**
      * flip the image horizontally around the mid point
      *
      */
     
     public  static BufferedImage flip(BufferedImage bufferedImage){
         int v1,v2;
         if(bufferedImage == null) return null;
         final int width = bufferedImage.getWidth();
         for (int j=0;j<bufferedImage.getHeight();j++){
             for (int i=0;i<(width + 1) /2;i++){
                 v1 = bufferedImage.getRGB(i, j);
                 v2 = bufferedImage.getRGB(width -1 -i, j);
                 bufferedImage.setRGB(i, j, v2);
                 bufferedImage.setRGB(width -1 -i, j, v1);
             }
         }
         return bufferedImage;
     }
}
