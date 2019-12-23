package com.lightning.jpipeworks.things;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

public interface DrawingSpace {
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
}
