package com.lightning.jpipeworks.things;

/**
 * A DrawingSpace which ignores attempts to draw to it.
 * This is a global space, all coordinates within the DrawingSpace are absolute (this property is solely shared with the Engine class)
 */
public final class NullSpace implements DrawingSpace {
    @Override
    public int getWidth() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getHeight() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void plotPixel(int x, int y, int rgb) {

    }

    @Override
    public void plotPixel(int x, int y, int r, int g, int b) {

    }

    @Override
    public void drawCircle(int x, int y, int r, int rgb) {

    }

    @Override
    public void drawRect(int x1, int y1, int x2, int y2, int rgb) {

    }

    @Override
    public void drawEllipse(int x, int y, int rx, int ry, int rgb) {

    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, int rgb) {

    }

    @Override
    public void fillCircle(int x, int y, int r, int rgb) {

    }

    @Override
    public void fillRect(int x1, int y1, int x2, int y2, int rgb) {

    }

    @Override
    public void fillEllipse(int x, int y, int rx, int ry, int rgb) {

    }
}
