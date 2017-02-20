package org.pipbenchmark.standardbenchmarks;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.*;

public class GraphicsBenchmarkSuite extends BenchmarkSuite {
    private final static int MaxLength = 100;
//    private final static int MaxWindowWidth = 480;
//    private final static int MaxWindowHeight = 480;
    private int _width = 480;
    private int _height = 480;
    private Image[] Images = new Image[] { };
    
    private Object _syncRoot = new Object();
    private Random _randomGenerator = new Random();
    private Shell _outputForm;
    private GC _outputGraphics;

    public GraphicsBenchmarkSuite() {
	    super("Graphics", "Benchmark for graphical operations");
	    initializeTests();
	}
	
	private void initializeTests() {
	    addBenchmark("Line", "Measures line drawings", new Runnable() {
	    	public void run() {
	    		executeDrawLine();
	    	}
	    });
	    addBenchmark("Rectangle", "Measures rectangle drawings", new Runnable() {
	    	public void run() {
	    		executeDrawRectangle();
	    	}
	    });
	    addBenchmark("Text", "Measures text drawings", new Runnable() {
	    	public void run() {
	    		executeDrawText();
	    	}
	    });
	    addBenchmark("Bitmap", "Measures bitmap drawings", new Runnable() {
	    	public void run() {
	    		executeDrawBitmap();
	    	}
	    });
	    addBenchmark("BitmapScaled", "Measures scaled bitmap drawings", new Runnable() {
	    	public void run() {
	    		executeDrawBitmapScaled();
	    	}
	    });
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
    
    private void executeDrawLine() {
        synchronized (_syncRoot) {
            int xStart = -MaxLength / 2 + _randomGenerator.nextInt(_width + MaxLength);
            int yStart = -MaxLength / 2 + _randomGenerator.nextInt(_height + MaxLength);
            int xEnd = -MaxLength / 2 + _randomGenerator.nextInt(_width + MaxLength);
            int yEnd = -MaxLength / 2 + _randomGenerator.nextInt(_height + MaxLength);

            Color color = getRandomColor();
            try {
	            _outputGraphics.setForeground(color);
	            _outputGraphics.setLineWidth(1 + _randomGenerator.nextInt(5));
	            _outputGraphics.drawLine(xStart, yStart, xEnd, yEnd);
            } finally {
            	color.dispose();
            }
        }
    }

    private void executeDrawRectangle() {
        synchronized (_syncRoot) {
            int xStart = -MaxLength / 2 + _randomGenerator.nextInt(_width + MaxLength);
            int yStart = -MaxLength / 2 + _randomGenerator.nextInt(_height + MaxLength);
            int xEnd = -MaxLength / 2 + _randomGenerator.nextInt(_width + MaxLength);
            int yEnd = -MaxLength / 2 + _randomGenerator.nextInt(_height + MaxLength);

            Color color = getRandomColor();
            try {
	            _outputGraphics.setBackground(color);
                _outputGraphics.fillRectangle(Math.min(xStart, xEnd),
                    Math.min(yStart, yEnd), Math.abs(xEnd - xStart), Math.abs(yEnd - yStart));               
            } finally {
            	color.dispose();
            }
        }
    }

    private void executeDrawText() {
        synchronized (_syncRoot) {
            int x = -MaxLength / 2 + _randomGenerator.nextInt(_width + MaxLength);
            int y = -MaxLength / 2 + _randomGenerator.nextInt(_height + MaxLength);

            String fontFamily;
            switch (_randomGenerator.nextInt(3)) {
                case 0:
                    fontFamily = "Arial";
                    break;
                case 1:
                    fontFamily = "Times New Roman";
                    break;
                default:
                    fontFamily = "Courier New";
                    break;
            }
            
            Color color = getRandomColor();
            Font font = new Font(_outputGraphics.getDevice(),
            	fontFamily, 6 + _randomGenerator.nextInt(26), SWT.NORMAL);
            try {
            	_outputGraphics.setForeground(color);
            	_outputGraphics.setFont(font);
                _outputGraphics.drawString("Draw Text", x, y, true);
            } finally {
            	color.dispose();
            	font.dispose();
            }
        }
    }

    private void executeDrawBitmap() {
        synchronized (_syncRoot) {
            int x = -MaxLength / 2 + _randomGenerator.nextInt(_width + MaxLength);
            int y = -MaxLength / 2 + _randomGenerator.nextInt(_height + MaxLength);

            _outputGraphics.drawImage(Images[_randomGenerator.nextInt(Images.length)], x, y);
        }
    }

    private void executeDrawBitmapScaled() {
        synchronized (_syncRoot) {
            Image image = Images[_randomGenerator.nextInt(Images.length)];
            int x = -MaxLength / 2 + _randomGenerator.nextInt(_width + MaxLength);
            int y = -MaxLength / 2 + _randomGenerator.nextInt(_height + MaxLength);
            int width = 10 + _randomGenerator.nextInt(image.getImageData().width);
            int height = 10 + _randomGenerator.nextInt(image.getImageData().width);

            _outputGraphics.drawImage(image, 0, 0,
            	image.getImageData().width, image.getImageData().height,
            	x, y, width, height);
        }
    }
    
}
