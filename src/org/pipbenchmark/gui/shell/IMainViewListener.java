package org.pipbenchmark.gui.shell;

public interface IMainViewListener {
  void loadSuiteClicked();
  void loadParametersClicked();
  void saveParametersClicked();
  void printReportClicked();
  void saveReportClicked();
  void startBenchmarkingClicked();
  void stopBenchmarkingClicked();
  void benchmarkEnvironmentClicked();
  void aboutClicked();
  void exitClicked();
  void formExited();
}
