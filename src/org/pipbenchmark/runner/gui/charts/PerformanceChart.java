package org.pipbenchmark.runner.gui.charts;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class PerformanceChart extends Canvas implements PaintListener {
    // Keep only a maximum MAX_VALUE_COUNT amount of values; This will allow
    private final static int MAX_VALUE_COUNT = 512;
    // Draw a background grid with a fixed line spacing
    private final static int GRID_SPACING = 16;

    // Amount of currently visible values (calculated from control width and value spacing)
    private int _visibleValues = 0;
    // Horizontal value space in Pixels
    private int _valueSpacing = 5;
    // The currently highest displayed value, required for Relative Scale Mode
    private double _currentMaxValue = 0;
    // Offset value for the scrolling grid
    private int _gridScrollOffset = 0;
    // The current average value
    private double _averageValue = 0;
    // Scale mode for value aspect ratio
    private ScaleMode _scaleMode = ScaleMode.Absolute;
    // Timer Mode
    private TimerMode _timerMode = TimerMode.Disabled;
    // List of stored values
    private List<Double> _drawValues = new ArrayList<Double>(MAX_VALUE_COUNT);
    // Value queue for Timer Modes
    private Queue<Double> _waitingValues = new LinkedList<Double>();
    // Style and Design
    private PerformanceChartStyle _perfChartStyle;
    // Timer 
    private Timer _refreshTimer = new Timer();
    private int _timerInterval = 500;
    
    public PerformanceChart(Composite parent, int style) {
    	super(parent, style | SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND);
    	super.addPaintListener(this);
        // Initialize Variables
        _perfChartStyle = new PerformanceChartStyle();
    }
    
    public PerformanceChartStyle getPerfChartStyle() {
        return _perfChartStyle; 
    }
    
    public void setPerfChartStyle(PerformanceChartStyle value) {
        _perfChartStyle = value;
    }

    public ScaleMode getScaleMode() {
        return _scaleMode; 
    }
    
    public void setScaleMode(ScaleMode value) {
        _scaleMode = value;
    }

    public TimerMode getTimerMode() {
        return _timerMode; 
    }
    
    public void setTimerMode(TimerMode value) {
        if (value == TimerMode.Disabled) {
            // Stop and append only when changed
            if (_timerMode != TimerMode.Disabled) {
                _timerMode = value;

                // If there are any values in the queue, append them
                chartAppendFromQueue();
            }
        } else {
            _timerMode = value;
        }
    	resetTimer();
    }

    public int getTimerInterval() {
        return _timerInterval; 
    }
       
    public void setTimerInterval(int value) {
        if (value < 15) {
            throw new RuntimeException("The Timer interval must be greater then 15");
        } else {
            _timerInterval = value;
            resetTimer();
        }
    }

    private void resetTimer() {
    	_refreshTimer.cancel();
    	_refreshTimer.purge();
    	
        if (_timerMode != TimerMode.Disabled) {        	
    		_refreshTimer.schedule(new TimerTask() {
    			public void run() {
                    chartAppendFromQueue();
    			}
    		}, _timerInterval, _timerInterval);            	
        }
    }
    
    /// <summary>
    /// Clears the whole chart
    /// </summary>
    public void clear() {
        _drawValues.clear();
        super.redraw();
    }

    /// <summary>
    /// Adds a value to the Chart Line
    /// </summary>
    /// <param name="value">progress value</param>
    public void addValue(double value) {
        if (_scaleMode == ScaleMode.Absolute && value > 100.) {
            throw new RuntimeException(
            	String.format("Values greater then 100 not allowed in ScaleMode: Absolute (%f)", value));
        }

        if (_timerMode == TimerMode.Disabled) {
        	chartAppend(value);
            super.redraw();
        } else if (_timerMode == TimerMode.Simple 
        	|| _timerMode == TimerMode.SynchronizedAverage
        	|| _timerMode == TimerMode.SynchronizedSum) {
            // For all Timer Modes, the Values are stored in the Queue
            addValueToQueue(value);
        } else {
            throw new RuntimeException(String.format(
            	"Unsupported TimerMode: %s", _timerMode));
        }
    }

    /// <summary>
    /// Add value to the queue for a timed refresh
    /// </summary>
    /// <param name="value"></param>
    private void addValueToQueue(double value) {
        _waitingValues.add(value);
    }


    /// <summary>
    /// Appends value <paramref name="value"/> to the chart (without redrawing)
    /// </summary>
    /// <param name="value">performance value</param>
    private void chartAppend(double value) {
        // Insert at first position; Negative values are flatten to 0 (zero)
        _drawValues.add(0, Math.max(value, 0));

        // Remove last item if maximum value count is reached
        if (_drawValues.size() > MAX_VALUE_COUNT) {
            _drawValues.remove(MAX_VALUE_COUNT);
        }

        // Calculate horizontal grid offset for "scrolling" effect
        _gridScrollOffset += _valueSpacing;
        if (_gridScrollOffset > GRID_SPACING) {
            _gridScrollOffset = _gridScrollOffset % GRID_SPACING;
        }
    }

    /// <summary>
    /// Appends Values from queue
    /// </summary>
    private void chartAppendFromQueue() {
        // Proceed only if there are values at all
        if (_waitingValues.size() > 0) {
            if (_timerMode == TimerMode.Simple) {
                for (Double value = _waitingValues.poll();
                	value != null; value = _waitingValues.poll()) {
                    chartAppend(value.doubleValue());
                }
            }
            else if (_timerMode == TimerMode.SynchronizedAverage 
            	|| _timerMode == TimerMode.SynchronizedSum) 
            {
                // appendValue variable is used for calculating the average or sum value
                double appendValue = 0;
                int valueCount = 0;

                for (Double value = _waitingValues.poll();
                	value != null; value = _waitingValues.poll()) {
                    appendValue += value.doubleValue();
                    valueCount++;
                }

                // Calculate Average value in SynchronizedAverage Mode
                if (_timerMode == TimerMode.SynchronizedAverage) {
                    appendValue = appendValue / valueCount;
                }

                // Finally append the value
                chartAppend(appendValue);
            }
        } else {
            // Always add 0 (Zero) if there are no values in the queue
            chartAppend(0);
        }

        // Refresh the Chart
        super.redraw();
    }

    /// <summary>
    /// Calculates the vertical Position of a value in relation the chart size,
    /// Scale Mode and, if ScaleMode is Relative, to the current maximum value
    /// </summary>
    /// <param name="value">performance value</param>
    /// <returns>vertical Point position in Pixels</returns>
    private int calcVerticalPosition(double value) 
    {
        double result = 0;

        if (_scaleMode == ScaleMode.Absolute) {
            result = value * this.getSize().y / 100;
        } else if (_scaleMode == ScaleMode.Relative) {
            result = (_currentMaxValue > 0) ? (value * this.getSize().y / _currentMaxValue) : 0;
        }

        result = this.getSize().y - result;

        return (int)Math.round(result);
    }

    /// <summary>
    /// Returns the currently highest (displayed) value, for Relative ScaleMode
    /// </summary>
    /// <returns></returns>
    private double getHighestValueForRelativeMode() {
        double maxValue = 0;
        for (int i = 0; i < _visibleValues; i++) {
            // Set if higher then previous max value
            if (_drawValues.get(i) > maxValue) {
                maxValue = _drawValues.get(i);
            }
        }
        return maxValue;
    }

    /// <summary>
    /// Draws the chart (w/o background or grid, but with border) to the Graphics canvas
    /// </summary>
    /// <param name="g">Graphics</param>
    private void drawChart(GC gc) {
    	int width = getSize().x;
    	int height = getSize().y;

    	_visibleValues = Math.min(width / _valueSpacing, _drawValues.size());

        if (_scaleMode == ScaleMode.Relative) {
            _currentMaxValue = getHighestValueForRelativeMode();
        }

        // Dirty little "trick": initialize the first previous Point outside the bounds
        Point previousPoint = new Point(width + _valueSpacing, height);
        Point currentPoint = new Point(0, 0);

        // Only draw average line when possible (visibleValues) and needed (style setting)
        if (_visibleValues > 0 && _perfChartStyle.getShowAverageLine()) {
            _averageValue = 0;
            drawAverageLine(gc);
        }

        // Connect all visible values with lines
        _perfChartStyle.getChartLinePen().assign(gc);
        for (int i = 0; i < _visibleValues; i++)  {
            currentPoint = new Point(previousPoint.x - _valueSpacing,
            	calcVerticalPosition(_drawValues.get(i)));

            // Actually draw the line
            gc.drawLine(previousPoint.x, previousPoint.y,
                currentPoint.x, currentPoint.y);

            previousPoint = currentPoint;
        }

        // Draw current relative maximum value stirng
        if (_scaleMode == ScaleMode.Relative)  {
        	_perfChartStyle.getChartLinePen().assign(gc);
            gc.drawText(Double.toString(_currentMaxValue), 4, 2, true);
        }
    }

    private void drawAverageLine(GC gc) {
        for (int i = 0; i < _visibleValues; i++) {
            _averageValue += _drawValues.get(i);
        }

        _averageValue = _averageValue / _visibleValues;

        int verticalPosition = calcVerticalPosition(_averageValue);
        _perfChartStyle.getAverageLinePen().assign(gc);
        gc.drawLine(0, verticalPosition, getSize().x, verticalPosition);
    }

    /// <summary>
    /// Draws the background gradient and the grid into Graphics <paramref name="g"/>
    /// </summary>
    /// <param name="g">Graphic</param>
    private void drawBackgroundAndGrid(GC gc) {
    	int width = getSize().x;
    	int height = getSize().y;
    	
        // Draw the background Gradient rectangle
        Color gradientColor1 = new Color(gc.getDevice(), _perfChartStyle.getBackgroundColorTop());
        Color gradientColor2 = new Color(gc.getDevice(), _perfChartStyle.getBackgroundColorBottom());
        Pattern gradientPattern = new Pattern(gc.getDevice(),
        	0, 0, this.getSize().x, this.getSize().y, gradientColor1, gradientColor2);

        try {
	        gc.setBackground(gradientColor2);
	        gc.setBackgroundPattern(gradientPattern);
	        gc.fillRectangle(0, 0, width, height);
        } finally {
        	gradientColor1.dispose();
        	gradientColor2.dispose();
        	gradientPattern.dispose();
        }

        // Draw all visible, vertical gridlines (if wanted)
        if (_perfChartStyle.getShowVerticalGridLines()) {
        	_perfChartStyle.getVerticalGridPen().assign(gc);
            for (int i = width - _gridScrollOffset; i >= 0; i -= GRID_SPACING) {
                gc.drawLine(i, 0, i, height);
            }
        }

        // Draw all visible, horizontal gridlines (if wanted)
        if (_perfChartStyle.getShowHorizontalGridLines()) {
        	_perfChartStyle.getHorizontalGridPen().assign(gc);
            for (int i = 0; i < height; i += GRID_SPACING) {
                gc.drawLine(0, i, width, i);
            }
        }
    }

    /// Override OnPaint method
    public void paintControl(PaintEvent e) {
        // Enable AntiAliasing, if needed
        if (_perfChartStyle.getAntiAliasing()) {
        	e.gc.setAntialias(SWT.ON);
        }

        drawBackgroundAndGrid(e.gc);
        drawChart(e.gc);
    }
    
}
