package com.digitalliving.nbenchmark;

import java.util.*;
import java.io.*;

public interface IBenchmarkEngine {
    List<BenchmarkTestSuite> getTestSuites();
    
    void loadTestSuite(String testSuiteClassName)
    	throws ClassNotFoundException, InstantiationException;
    void loadTestSuite(BenchmarkTestSuite testSuite);
    void loadTestSuitesFromLibrary(String fileName) throws IOException;
    void unloadTestSuite(BenchmarkTestSuite testSuite);
    void unloadTestSuite(String testSuiteName);
    void unloadAllTestSuites();

    List<ConfigurationParameter> getConfiguration();

    void loadConfigurationFromFile(String fileName) throws IOException;
    void saveConfigurationToFile(String fileName) throws IOException;
    void setConfigurationToDefault();

    int getNumberOfThreads();
    void setNumberOfThreads(int value);
    
    TestingMode getTestingMode();
    void setTestingMode(TestingMode value);
    
    double getNominalPerformance();
    void setNominalPerformance(double value);
    
    ExecutionType getExecutionType();
    void setExecutionType(ExecutionType value);
    
    int getTestDuration();
    void setTestDuration(int value);

    boolean isBenchmarking();
    List<BenchmarkingResult> getResults();

    void addStatusListener(IBenchmarkStatusListener listener);
    void removeStatusListener(IBenchmarkStatusListener listener);
    
    void startBenchmarking();
    void stopBenchmarking();

    String generateReport();
    void saveReportToFile(String fileName) throws IOException;

    Map<String, String> getSystemInformation();
    double getCpuBenchmark();
    double getVideoBenchmark();
    double getDiskBenchmark();

    void updateSystemBenchmark();
}
