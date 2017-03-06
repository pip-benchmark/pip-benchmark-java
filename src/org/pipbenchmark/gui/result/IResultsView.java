package org.pipbenchmark.gui.result;

public interface IResultsView {
    String getReportContent();
    void setReportContent(String value);
    
    String getSelectedReportContent();

    void setListener(IResultsViewListener listener);
}
