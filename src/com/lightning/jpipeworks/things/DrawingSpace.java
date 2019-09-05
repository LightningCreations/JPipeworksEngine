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
	
	public default DrawingSpace scale(float originX,float originY,float xscale,float yscale) {
		if(xscale==0||yscale==0)
			throw new IllegalArgumentException("Cannot scale Space by 0 on any axis");
		else
			return new DrawingSpace() {

				@Override
				public int getWidth() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int getHeight() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public void plotPixel(int x, int y, int rgb) {
					drawRect(x,y,x+1,y+1,rgb);
				}

				@Override
				public void drawRect(int x1, int y1, int x2, int y2, int rgb) {
					DrawingSpace.this.drawRect((int)(xscale*(x1-originX)+originX),(int)(yscale*(y1-originY)+originY),(int)(xscale*(x2-originX)+originX),(int)(yscale*(y2-originY)+originY),rgb);
				}

				@Override
				public void drawEllipse(int x, int y, int rx, int ry, int rgb) {
					rx = (int)(rx*xscale);
					ry = (int)(ry*yscale);
					x = (int)(xscale*(x-originX)+originX);
					y = (int)(yscale*(y-originY)+originY);
					DrawingSpace.this.drawEllipse(x, y, rx, ry, rgb);
				}

				@Override
				public void drawLine(int x1, int y1, int x2, int y2, int rgb) {
					DrawingSpace.this.drawLine((int)(xscale*(x1-originX)+originX),(int)(yscale*(y1-originY)+originY),(int)(xscale*(x2-originX)+originX),(int)(yscale*(y2-originY)+originY),rgb);
				}

				@Override
				public void fillRect(int x1, int y1, int x2, int y2, int rgb) {
					DrawingSpace.this.fillRect((int)(xscale*(x1-originX)+originX),(int)(yscale*(y1-originY)+originY),(int)(xscale*(x2-originX)+originX),(int)(yscale*(y2-originY)+originY),rgb);
				}

				@Override
				public void fillEllipse(int x, int y, int rx, int ry, int rgb) {
					rx = (int)(rx*xscale);
					ry = (int)(ry*yscale);
					x = (int)(xscale*(x-originX)+originX);
					y = (int)(yscale*(y-originY)+originY);
					DrawingSpace.this.fillEllipse(x, y, rx, ry, rgb);
				}
			};	
	}
	
	public default DrawingSpace changeColorSpace(IntUnaryOperator rgbFunction) {
		Objects.requireNonNull(rgbFunction);
		return new DrawingSpace() {

			@Override
			public int getWidth() {
				// TODO Auto-generated method stub
				return DrawingSpace.this.getWidth();
			}

			@Override
			public int getHeight() {
				// TODO Auto-generated method stub
				return DrawingSpace.this.getHeight();
			}

			@Override
			public void plotPixel(int x, int y, int rgb) {
				DrawingSpace.this.plotPixel(x,y,rgbFunction.applyAsInt(rgb));
			}

			@Override
			public void drawRect(int x1, int y1, int x2, int y2, int rgb) {
				DrawingSpace.this.drawRect(x1,y1,x2,y2,rgbFunction.applyAsInt(rgb));
			}

			@Override
			public void drawEllipse(int x, int y, int rx, int ry, int rgb) {
				DrawingSpace.this.drawEllipse(x,y,rx,ry,rgbFunction.applyAsInt(rgb));
			}

			@Override
			public void drawLine(int x1, int y1, int x2, int y2, int rgb) {
				DrawingSpace.this.drawRect(x1,y1,x2,y2,rgbFunction.applyAsInt(rgb));
			}

			@Override
			public void fillRect(int x1, int y1, int x2, int y2, int rgb) {
				DrawingSpace.this.fillRect(x1,y1,x2,y2,rgbFunction.applyAsInt(rgb));
			}

			@Override
			public void fillEllipse(int x, int y, int rx, int ry, int rgb) {
				DrawingSpace.this.fillEllipse(x,y,rx,ry,rgbFunction.applyAsInt(rgb));
			}
			
		};
	}
}
