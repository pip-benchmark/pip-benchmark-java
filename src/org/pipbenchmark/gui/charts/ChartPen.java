package org.pipbenchmark.gui.charts;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class ChartPen {
	private RGB _color = new RGB(0, 0, 0);
	private int _style = SWT.LINE_SOLID;
	private int _width = 1;
	
    public ChartPen() {
    }

    public ChartPen(RGB color, int style, int width) {
    	_color = color;
    	_style = style;
    	_width = width;
    }
    
    public ChartPen(RGB color) {
    	_color = color;
    }
    
    public RGB getColor() {
        return _color; 
    }
    
    public void setColor(RGB color) {
        _color = color;
    }

    public int getWidth() {
        return _width; 
    }
    
    public void setWidth(int value) {
        _width = value;
    }

    public void assign(GC gc) {
    	Color color = new Color(gc.getDevice(), _color);
    	try {
    		gc.setLineStyle(_style);
    		gc.setLineWidth(_width);
    		gc.setForeground(color);
    	} finally {
    		color.dispose();
    	}
    }
}
