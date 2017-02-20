package org.pipbenchmark.runner.gui.initialization;

public interface IInitializationViewListener {
    void suiteSelectedChanged();
    void loadSuiteClicked();
    void unloadSuiteClicked();
    void unloadAllSuitesClicked();
    void selectBenchmarkClicked();
    void selectAllBenchmarksClicked();
    void unselectBenchmarkClicked();
    void unselectAllBenchmarksClicked();
}
