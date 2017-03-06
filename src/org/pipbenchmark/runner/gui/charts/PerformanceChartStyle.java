package org.pipbenchmark.runner.gui.charts;

import org.eclipse.swt.graphics.*;

public class PerformanceChartStyle {
    private ChartPen _verticalGridPen = new ChartPen(new RGB(0, 0, 0));
    private ChartPen _horizontalGridPen = new ChartPen(new RGB(0, 0, 0));
    private ChartPen _averageLinePen = new ChartPen(new RGB(72, 169, 72));
    private ChartPen _chartLinePen = new ChartPen(new RGB(255, 215, 0));

    private RGB _backgroundColorTop = new RGB(0,100,0);
    private RGB _backgroundColorBottom = new RGB(0,100,0);

    private boolean _showVerticalGridLines = true;
    private boolean _showHorizontalGridLines = true;
    private boolean _showAverageLine = true;
    private boolean _antiAliasing = true;

    public PerformanceChartStyle() {
    }

    public boolean getShowVerticalGridLines() {
        return _showVerticalGridLines;
    }
    
    public void setShowVerticalGridLines(boolean value) {
        _showVerticalGridLines = value;
    }

    public boolean getShowHorizontalGridLines() { 
        return _showHorizontalGridLines; 
    }
    
    public void setShowHorizontalGridLines(boolean value) {
        _showHorizontalGridLines = value;
    }

    public boolean getShowAverageLine() {
        return _showAverageLine; 
    }
    
    public void setShowAverageLine(boolean value) {
        _showAverageLine = value;
    }

    public ChartPen getVerticalGridPen() {
        return _verticalGridPen; 
    }
    
    public void setVerticalGridPen(ChartPen value) {
        _verticalGridPen = value;
    }

    public ChartPen getHorizontalGridPen() { 
        return _horizontalGridPen; 
    }
    
    public void setHorizontalGridPen(ChartPen value) {
        _horizontalGridPen = value;
    }

    public ChartPen getAverageLinePen()  {
        return _averageLinePen; 
    }
    
    public void setAverageLinePen(ChartPen value) {
        _averageLinePen = value;
    }

    public ChartPen getChartLinePen() { 
        return _chartLinePen;
    }
    
    public void setChartLinePen(ChartPen value) {
        _chartLinePen = value;
    }

    public boolean getAntiAliasing() { 
        return _antiAliasing;
    }
    
    public void setAntiAliasing(boolean value) {
        _antiAliasing = value;
    }

    public RGB getBackgroundColorTop() {
        return _backgroundColorTop; 
    }
    
    public void setBackgroundColorTop(RGB value) {
        _backgroundColorTop = value;
    }

    public RGB getBackgroundColorBottom() { 
        return _backgroundColorBottom; 
    }
    
    public void setBackgroundColorBottom(RGB value) {
        _backgroundColorBottom = value;
    }
}
