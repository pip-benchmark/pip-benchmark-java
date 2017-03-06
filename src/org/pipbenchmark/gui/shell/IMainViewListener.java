package org.pipbenchmark.gui.shell;

public interface IMainViewListener {
  void loadSuiteClicked();
  void loadConfigurationClicked();
  void saveConfigurationClicked();
  void printReportClicked();
  void saveReportClicked();
  void startBenchmarkingClicked();
  void stopBenchmarkingClicked();
  void benchmarkEnvironmentClicked();
  void aboutClicked();
  void exitClicked();
  void formExited();
}
