package org.pipbenchmark.runner.gui.initialization;

import java.util.*;

import org.pipbenchmark.runner.*;

public interface IInitializationView {
    List<BenchmarkSuiteInstance> getAllSuites();
    void setAllSuites(List<BenchmarkSuiteInstance> value);
    
    List<BenchmarkSuiteInstance> getSelectedSuites();
    
    List<BenchmarkInstance> getAllBenchmarks();
    void setAllBenchmarks(List<BenchmarkInstance> value);
    
    List<BenchmarkInstance> getSelectedBenchmarks();

    void setListener(IInitializationViewListener listener);
    
    void refreshData();
}
