package com.honda.galc.client.gts.figure;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import com.honda.galc.entity.enumtype.GtsOrientation;
import com.honda.galc.entity.gts.GtsColorMap;
import com.honda.galc.entity.gts.GtsOutlineMap;


/**
 * 
 * 
 * <h3>Image Class description</h3>
 * <p> Image description </p>
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
 * @author Jeffray Huang<br>
 * May 15, 2015
 *
 *
 */

public class Image {
	private GtsOutlineMap outlineMap; 
	private BufferedImage bufferedImage;
	
	
    public Image(GtsOutlineMap map){
        this.outlineMap = map;
        this.bufferedImage = getBufferedImage(map.getImageBytes());
    }
    
    
    public boolean match(String modelCode){
        return outlineMap.getModelCode().equals(modelCode);
    }
	
	public BufferedImage getBufferedImage(){
		return bufferedImage;
	}
		
     
     public GtsOutlineMap getOutlineMap() {
        return outlineMap;
    }


    public void setOutlineMap(GtsOutlineMap outlineMap) {
        this.outlineMap = outlineMap;
    }


    public BufferedImage transform(GtsColorMap colorMap,GtsOrientation direction,int discrepancyFlag){
        Color color = colorMap == null ? null:colorMap.getColor();
        BufferedImage image = fillColor(color,discrepancyFlag); 
        if (!direction.equals(GtsOrientation.WEST)) ImageFactory.flip(image);
        return image;
         
     }
     
     private BufferedImage fillColor(Color color,int discrepancyFlag){
	     BufferedImage clone = new BufferedImage(convertColorModel(color,discrepancyFlag), 
                             bufferedImage.getData().createCompatibleWritableRaster(),
                             bufferedImage.isAlphaPremultiplied(), null);
         clone.setData(bufferedImage.copyData(clone.getRaster()));
         return clone;
	 }
     
     private BufferedImage getBufferedImage(byte[] image){
         try{
             return ImageIO.read(new ByteArrayInputStream(image)); 
         }catch(Exception e){
         }
         return null;
     }
    
    /**
     * replace the color of the color model 
     * original color - color at point (10,10)
     * replaced by the instance variable color
     * @return
     */ 
     
    private IndexColorModel convertColorModel(Color color,int discrepancyFlag){
        IndexColorModel m = (IndexColorModel)bufferedImage.getColorModel();
        int mapSize = m.getMapSize();
        byte alphas[] = new byte[mapSize];
        byte reds[] = new byte[mapSize];
        byte greens[] = new byte[mapSize];
        byte blues[] = new byte[mapSize];
        Color c = new Color(bufferedImage.getRGB(10, 10));
        Color bk = new Color(bufferedImage.getRGB(0, 0));
        Color newBackColor = discrepancyFlag == 1 ? Color.magenta : Color.red;
        m.getAlphas(alphas);
        m.getReds(reds);
        m.getGreens(greens);
        m.getBlues(blues);
        
        // set product color
        if(color != null){
            for(int i=0; i<mapSize;i++){
                if(reds[i] == (byte)c.getRed() && greens[i] == (byte)c.getGreen() && blues[i] == (byte)c.getBlue()){
                    reds[i] = (byte)color.getRed();
                    greens[i] = (byte)color.getGreen();
                    blues[i] = (byte)color.getBlue();
                    break;
                };
            }
        }
        
        // set dicrepancy color
        if(discrepancyFlag > 0){
           for(int i=0; i<mapSize;i++){
                        
                if(reds[i] == (byte)bk.getRed() && greens[i] == (byte)bk.getGreen() && blues[i] == (byte)bk.getBlue()){
                    reds[i] = (byte)newBackColor.getRed();
                    greens[i] = (byte)newBackColor.getGreen();
                    blues[i] = (byte)newBackColor.getBlue();
                    break;
                }
            }
        }   
        return new IndexColorModel(m.getPixelSize(),mapSize,reds,greens,blues,alphas);
        
    }
         
}
