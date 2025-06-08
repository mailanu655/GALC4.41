package com.honda.galc.client.common;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.honda.galc.common.logging.Logger;

/**
 * @author Fredrick Yessaian
 * @date Dec 01, 2019
 * 
 *       Util class created to house all common methods related to image.
 */
public class ImageUtils {
	
	public static byte[] scale(byte[] imageData, boolean isBmp, int MAX_IMAGE_SIZE, double ratio) {
		byte[] result = null;
		result = isBmp ? scaleImage(imageData, 1) : imageData;
		
		while(result.length > MAX_IMAGE_SIZE) {
			result = scaleImage(result, ratio);
		}
		return result;
	} 
	
	private static byte[] scaleImage(byte[] imageData, double ratio) {
		byte[] result = null;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
			BufferedImage inputBufferedImage = ImageIO.read(inputStream);
			inputStream.close();

			int newWidth = (int) (inputBufferedImage.getWidth() * ratio);
			int newHeight = (int) (inputBufferedImage.getHeight() * ratio);
			
			Image newImage = inputBufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BufferedImage outputBufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			outputBufferedImage.getGraphics().drawImage(newImage, 0, 0, new Color(0, 0, 0), null);
			ImageIO.write(outputBufferedImage, "jpg", outputStream);
			result = outputStream.toByteArray();
			outputStream.close();

		} catch (Exception e) {
             Logger.getLogger().error("Unable to rescale the image");
             e.printStackTrace();
		}
		return result;
	}

}
