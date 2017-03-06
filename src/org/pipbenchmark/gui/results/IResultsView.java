package org.pipbenchmark.gui.results;

public interface IResultsView {
    String getReportContent();
    void setReportContent(String value);
    
    String getSelectedReportContent();

    void setListener(IResultsViewListener listener);
}
