package model.util;

import java.awt.Color;

public class Pixel {
    
    private int x;
    private int y;
    private Color color;
          
    /**
     * Creates a new pixel
     * @param x is the coordinate on the x axis
     * @param y is the coordinate on the y axis
     * @param color is the color of the pixel
     */
    public Pixel (int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Returns the color of the pixel
     * @return color
     */
	public Color getColor() {
		return color;
	}

	/**
	 * Retuns the x coordinate of the pixel
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retuns the y coordinate of the pixel
	 * @return y
	 */
	public int getY() {
		return y;
	}
    
}
