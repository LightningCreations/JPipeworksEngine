package com.lightning.jpipeworks.things;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

/**
 * A DrawingSpace is is a generalization of the API Provided by Engine,
 *  to allow transformations of various kinds applied to drawing calls
 */
public interface DrawingSpace {
	public final class Point{
		private final float x,y;
		private Point(float x,float y){
			this.x = x;
			this.y = y;
		}
		public static final Point getPoint(float x,float y){
			return new Point(x,y);
		}
		public static final Point ORIGIN = new Point(0,0);
		public float getX(){
			return x;
		}
		public float getY(){
			return y;
		}
		public Point add(Point other){
			return new Point(x+other.x,y+other.y);
		}
		public Point sub(Point other){
			return new Point(x-other.x,y-other.y);
		}
		public Point mul(float factor){
			return new Point(x*factor,y*factor);
		}
		public Point div(float factor){
			return new Point(x/factor,y/factor);
		}

		/**
		 * Multiplies this point by the 2x2 matrix: <br/>
		 * [a b]<br/>
		 * [c d]<br/>
		 */
		public Point mul(float a,float b,float c, float d){
			return new Point(a*x+b*y,c*x+d*y);
		}
	}

	public int getWidth();
	public int getHeight();
	public void plotPixel(int x,int y,int rgb);
	public default void plotPixel(int x,int y,int r,int g,int b) {
		plotPixel(x,y,(r&0xff)<<16|(g&0xff)<<8|b&0xff);
	}
	public default void drawCircle(int x,int y,int r,int rgb) {
		drawEllipse(x,y,r,r,rgb);
	}
	public void drawRect(int x1,int y1,int x2,int y2,int rgb);
	public void drawEllipse(int x,int y,int rx,int ry,int rgb);
	public void drawLine(int x1,int y1,int x2,int y2,int rgb);
	public default void fillCircle(int x,int y,int r,int rgb) {
		fillEllipse(x,y,r,r,rgb);
	}
	public void fillRect(int x1,int y1,int x2,int y2,int rgb);
	public void fillEllipse(int x,int y,int rx,int ry,int rgb);

	/**
	 * Defined for Transformative drawing spaces, returning the point in the drawing space,
	 *  corresponding to the given point in the global drawing space.
	 *
	 * @param abs
	 */
	public default Point getPointIn(Point abs){
		return abs;
	}
}
