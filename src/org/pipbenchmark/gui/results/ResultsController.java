package org.pipbenchmark.gui.results;

import java.io.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.gui.shell.*;
import org.pipbenchmark.runner.*;

public class ResultsController extends AbstractChildController
	implements IResultsViewListener {
	
//    private static final Font PrintFont = new Font("Tahoma", 10, FontStyle.Regular);
    private IResultsView _view;
    private BenchmarkRunner _model;
    private FileDialog _saveReportDialog;

//    private string[] _printLines;
//    private int _linesPrinted;
//    private PrintDialog _printReportDialog;
//    private PrintDocument _printDocument;

    public ResultsController(MainController mainController, IResultsView view) {
        super(mainController);

        _view = view;
        _view.setListener(this);
        _model = mainController.getModel();

        initializeDialogs();
    }

    private void initializeDialogs() {
        _saveReportDialog = new FileDialog(getMainView().getHandler(), SWT.SAVE);
        _saveReportDialog.setFileName("BenchmarkReport.txt");
        _saveReportDialog.setFilterExtensions(new String[] {"*.txt", "*.*"});
        _saveReportDialog.setFilterNames(new String[] {"Text Files", "All Files"});
        _saveReportDialog.setFilterIndex(0);
        _saveReportDialog.setText("Save Benchmark Report");

//        _printReportDialog = new PrintDialog();
//        _printReportDialog.AllowSelection = false;
//        _printReportDialog.AllowSomePages = false;
//
//        _printDocument = new PrintDocument();
//        _printDocument.DocumentName = "Benchmark Report";
//        _printDocument.BeginPrint += OnBeginPrint;
//        _printDocument.PrintPage += OnPrintPage;
    }

    public void generateReport() {
        String report = _model.getReport().generate();
        _view.setReportContent(report);
    }

    public void saveReport() {
    	try {
	    	String fileName = _saveReportDialog.open();
	        if (fileName != null) {
	        	FileWriter fileWriter = new FileWriter(fileName);
	        	BufferedWriter writer = new BufferedWriter(fileWriter);
	        	try {
	        		writer.write(_view.getReportContent());
	        		writer.flush();
	        	} finally {
	        		writer.close();
	        		fileWriter.close();
	        	}
	        }
    	} catch (IOException ex) {
    		getMainController().showErrorDialog("Error", "Failed to save report", ex);
    	}
    }

    public void printReport() {
//        if (_printReportDialog.ShowDialog() == DialogResult.OK) {
//            _printDocument.PrinterSettings = _printReportDialog.PrinterSettings;
//            _printDocument.Print();
//        }
    }

    public void saveReportClicked() {
        saveReport();
    }

    public void printReportClicked() {
        printReport();
    }

//    private void OnBeginPrint(object sender, PrintEventArgs e) {
//        char[] param = {'\n'};
//
//        if (_printReportDialog.PrinterSettings.PrintRange == PrintRange.Selection) {
//            _printLines = _view.ReportContent.Split(param);
//        } else {
//            _printLines = _view.ReportContent.Split(param);
//        }
//                
//        char[] trimParam = {'\r'};
//        for (int index = 0; index < _printLines.Length; index++) {
//            _printLines[index] = _printLines[index].TrimEnd(trimParam);
//        }
//        _linesPrinted = 0;
//    }
//
//    private void OnPrintPage(object sender, PrintPageEventArgs e) {
//        int x = e.MarginBounds.Left;
//        int y = e.MarginBounds.Top;
//
//        while (_linesPrinted < _printLines.Length) {
//            e.Graphics.DrawString(_printLines[_linesPrinted++], PrintFont, Brushes.Black, x, y);
//            y += 15;
//            if (y >= e.MarginBounds.Bottom) {
//                e.HasMorePages = true;
//                return;
//            } else {
//                e.HasMorePages = false;
//            }
//        }
//    }

}
