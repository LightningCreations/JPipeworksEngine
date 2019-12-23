package com.lightning.jpipeworks.things;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

public class RecoloringSpace implements DrawingSpace {

    private DrawingSpace parent;
    private IntUnaryOperator colorFn;

    public RecoloringSpace(DrawingSpace parent, IntUnaryOperator colorFn) {
        this.parent = Objects.requireNonNull(parent);
        this.colorFn = Objects.requireNonNull(colorFn);
    }

    @Override
    public int getWidth() {
        return parent.getWidth();
    }

    @Override
    public int getHeight() {
        return parent.getHeight();
    }

    @Override
    public void plotPixel(int x, int y, int rgb) {
        parent.plotPixel(x,y,colorFn.applyAsInt(rgb));
    }


    @Override
    public void drawCircle(int x, int y, int r, int rgb) {
        parent.drawCircle(x,y,r,colorFn.applyAsInt(rgb));
    }

    @Override
    public void drawRect(int x1, int y1, int x2, int y2, int rgb) {
        parent.drawRect(x1,y1,x2,y2,colorFn.applyAsInt(rgb));
    }

    @Override
    public void drawEllipse(int x, int y, int rx, int ry, int rgb) {
        parent.drawEllipse(x,y,rx,ry,colorFn.applyAsInt(rgb));
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, int rgb) {
        parent.drawLine(x1,y1,x2,y2,colorFn.applyAsInt(rgb));
    }

    @Override
    public void fillCircle(int x, int y, int r, int rgb) {
        parent.fillCircle(x,y,r,colorFn.applyAsInt(rgb));
    }

    @Override
    public void fillRect(int x1, int y1, int x2, int y2, int rgb) {
        parent.fillRect(x1,y1,x2,y2,colorFn.applyAsInt(rgb));
    }

    @Override
    public void fillEllipse(int x, int y, int rx, int ry, int rgb) {
        parent.fillEllipse(x,y,rx,ry,colorFn.applyAsInt(rgb));
    }
}
