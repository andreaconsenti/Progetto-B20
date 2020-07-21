package model.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import model.entities.Territory;

public class ImageAssets {

	/**
	 * Creates a new HashMap containing all the territories
	 * @param image is the image used for the map
	 * @param list is the array containing all the territories
	 * @return map
	 */
	public static HashMap<Territory, ArrayList<Pixel>> imageProcess(BufferedImage image, ArrayList<Territory> list) {
	    int w = image.getWidth();
	    int h = image.getHeight();
	    Pixel pixel;
	    Color tempColor;
	    ArrayList<Pixel> tempPixels;
	    ArrayList<Pixel> pixelList = new ArrayList<Pixel>();
	    HashMap<Territory, ArrayList<Pixel>> map = new HashMap<Territory, ArrayList<Pixel>>();
		for(Territory t : list) {
			map.put(t, new ArrayList<Pixel>());
		}
	    
	    
	    for (int i = 0; i < w; i++) {
	        for (int j = 0; j < h; j++) {
	        	
	        	tempColor = new Color(image.getRGB(i,j));
	        	pixel = new Pixel(i, j, tempColor);
	            
	        	for(Territory t : list) {
	        		if(t.getRGB().equals(tempColor)) {
	        			tempPixels = map.get(t);
	        			tempPixels.add(pixel);
	        			map.put(t, tempPixels);
	        		}
	        	}
	        }
	    }
	    return map;
	  }
}
	

