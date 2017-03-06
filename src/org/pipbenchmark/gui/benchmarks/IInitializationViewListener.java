package org.pipbenchmark.gui.benchmarks;

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
