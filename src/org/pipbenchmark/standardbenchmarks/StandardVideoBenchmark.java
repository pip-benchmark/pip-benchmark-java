package org.pipbenchmark.standardbenchmarks;

import java.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.*;

public class StandardVideoBenchmark extends Benchmark {
    private final static int MaxLength = 100;
//    private final static int MaxWindowWidth = 480;
//    private final static int MaxWindowHeight = 480;
    private int _width = 480;
    private int _height = 480;
    
    private Object _syncRoot = new Object();
    private Random _randomGenerator = new Random();
    private Shell _outputForm;
    private GC _outputGraphics;

    public StandardVideoBenchmark() {
        super("Video", "Measures speed of drawing graphical primitives");
    }

    @Override
    public final void setUp() {
        _outputForm = new Shell(Display.getCurrent(), SWT.ON_TOP);
        _outputForm.setText("Video Benchmarking");
        _outputForm.setMaximized(true);
        _outputForm.setBounds(Display.getCurrent().getBounds());
        
//        _outputForm.setSize(Math.min(MaxWindowWidth, Display.getCurrent().getPrimaryMonitor().getBounds().width),
//        		Math.min(MaxWindowHeight, Display.getCurrent().getPrimaryMonitor().getBounds().height));
//        _outputForm.FormBorderStyle = FormBorderStyle.FixedSingle;
                
//        _outputForm.addDisposeListener(
//    		new DisposeListener() {
//    			public final void widgetDisposed(DisposeEvent e) {
//    		        if (getTestSuite().getContext() != null) {
//    		            getTestSuite().getContext().stopBenchmarking();
//    		        }
//    			}
//    		});
        
        _outputForm.open();        
        _outputGraphics = new GC(_outputForm);

        _width = _outputForm.getSize().x;
        _height = _outputForm.getSize().y;
    }

    @Override
    public final void execute() {
        if (_outputForm == null || _outputForm.isDisposed()) {
            return;
        }

        int xStart = -MaxLength / 2 + _randomGenerator.nextInt(_width + MaxLength);
        int yStart = -MaxLength / 2 + _randomGenerator.nextInt(_height + MaxLength);
        int xEnd = -MaxLength / 2 + _randomGenerator.nextInt(_width + MaxLength);
        int yEnd = -MaxLength / 2 + _randomGenerator.nextInt(_height + MaxLength);
        
        synchronized (_syncRoot) {
        	Color color = getRandomColor();
        	try {
        		_outputGraphics.setForeground(color);
        		_outputGraphics.setBackground(color);
    	        if (_randomGenerator.nextInt(2) == 0) {
    	        	_outputGraphics.setLineWidth(1 + _randomGenerator.nextInt(5));
    	            _outputGraphics.drawLine(xStart, yStart, xEnd, yEnd);
    	        } else {
                    _outputGraphics.fillRectangle(Math.min(xStart, xEnd),
                        Math.min(yStart, yEnd), Math.abs(xEnd - xStart), Math.abs(yEnd - yStart));
    	        }
        	} finally {
        		color.dispose();
        	}
        }
    }

    private Color getRandomColor() {
        return new Color(_outputForm.getDisplay(), _randomGenerator.nextInt(256),
            _randomGenerator.nextInt(256), _randomGenerator.nextInt(256));
    }

    @Override
    public final void tearDown() {
        synchronized (_syncRoot) {
        	if (_outputGraphics != null) {
	            _outputGraphics.dispose();
	            _outputGraphics = null;
        	}

        	if (_outputForm != null) {
	            _outputForm.dispose();
	            _outputForm = null;
        	}
        }
    }
}
