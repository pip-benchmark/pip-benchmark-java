package org.pipbenchmark.runner;

import java.util.*;
import java.util.jar.*;

import org.pipbenchmark.*;

import java.io.*;
import java.net.*;

public class BenchmarkSuiteManager {
	private BenchmarkRunner _runner;
    private List<BenchmarkSuiteInstance> _suites = new ArrayList<BenchmarkSuiteInstance>();

    public BenchmarkSuiteManager(BenchmarkRunner runner) {
        _runner = runner;
    }

    public BenchmarkRunner getRunner() {
        return _runner;
    }
 
    public List<BenchmarkSuiteInstance> getSuites() {
        return _suites;
    }

    public List<BenchmarkInstance> getSelectedBenchmarks() {
        List<BenchmarkInstance> benchmarks = new ArrayList<BenchmarkInstance>();

        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
                if (benchmark.isSelected()) {
                    benchmarks.add(benchmark);
                }
            }
        }

        return benchmarks;
    }

    public void selectAllBenchmarks() {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks())
                benchmark.setSelected(true);
        }
    }

    public void selectBenchmarks(String[] benchmarkNames) {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
            	for (String benchmarkName : benchmarkNames) {
            		if (benchmarkName.equals(benchmark.getFullName()))
            			benchmark.setSelected(true);
            	}
            }
        }
    }

    public void selectBenchmarks(BenchmarkInstance[] benchmarks) {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
            	for (BenchmarkInstance anotherBenchmark : benchmarks) {
            		if (benchmark == anotherBenchmark)
            			benchmark.setSelected(true);
            	}
            }
        }
    }

    public void unselectAllBenchmarks() {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks())
                benchmark.setSelected(false);
        }
    }

    public void unselectBenchmarks(String[] benchmarkNames) {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
            	for (String benchmarkName : benchmarkNames) {
            		if (benchmarkName.equals(benchmark.getFullName()))
            			benchmark.setSelected(false);
            	}
            }
        }
    }

    public void unselectBenchmarks(BenchmarkInstance[] benchmarks) {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
            	for (BenchmarkInstance anotherBenchmark : benchmarks) {
            		if (benchmark == anotherBenchmark)
            			benchmark.setSelected(false);
            	}
            }
        }
    }
    
    public void addSuiteFromClass(String suiteClassName)
    	throws ClassNotFoundException, InstantiationException {
    	Class<?> suiteClass = Class.forName(suiteClassName);
    	try {
    		BenchmarkSuite suite = (BenchmarkSuite)suiteClass.newInstance();
    		addSuite(suite);
    	} catch (IllegalAccessException ex) {
    		// Wrap exception since it shall rarely happen
    		throw new RuntimeException(ex);
    	}
    }

    public void addSuite(BenchmarkSuite suite) {
    	BenchmarkSuiteInstance instance = new BenchmarkSuiteInstance(suite);
    	addSuite(instance);
    }
    
    public void addSuite(BenchmarkSuiteInstance suite) {
        getRunner().getProcess().stop();
    	_suites.add(suite);
        getRunner().getConfigurationManager().createParametersForSuite(suite);
    }
    
    public void loadSuitesFromLibrary(String fileName) throws IOException {
        getRunner().getProcess().stop();

        // Load assembly
        URL fileUrl = new URL("jar:file:" + fileName + "!/");
        @SuppressWarnings("resource")
		URLClassLoader classLoader = new URLClassLoader(new URL[] { fileUrl });
        FileInputStream stream = new FileInputStream(fileName);
        try {
        	@SuppressWarnings("resource")
			JarInputStream jar = new JarInputStream(stream);
        	JarEntry entry = jar.getNextJarEntry();
	        // Find benchmark suites
        	while (entry != null) {
        		String entryName = entry.getName().replace('\\', '.').replace('/', '.');
        		if (!entry.isDirectory() && entryName.endsWith(".class") && !entryName.contains("$")) {
        			String className = entryName.substring(0, entryName.length() - 6);
        			try {
        				Class<?> type = classLoader.loadClass(className);
        				if (BenchmarkSuiteInstance.class.isAssignableFrom(type)) {
        					BenchmarkSuiteInstance suite = (BenchmarkSuiteInstance) type.newInstance();
        					_suites.add(suite);
        	                getRunner().getConfigurationManager().createParametersForSuite(suite);
        				}
        			} catch (Exception ex1) {
        				continue;
        			}
        		}
	            entry = jar.getNextJarEntry();
	        }
        } finally {
        	stream.close();
        }
    }

    private BenchmarkSuiteInstance findSuite(String suiteName) {
        for (BenchmarkSuiteInstance suite : _suites) {
            if (suite.getName().equalsIgnoreCase(suiteName)) {
                return suite;
            }
        }
        return null;
    }
    
    public void removeSuite(String suiteName) {
        getRunner().getProcess().stop();
        BenchmarkSuiteInstance suite = findSuite(suiteName);
        if (suite != null) {
            getRunner().getConfigurationManager().removeParametersForSuite(suite);
            _suites.remove(suite);
        }
    }

    public void removeSuite(BenchmarkSuite suite) {
    	for (BenchmarkSuiteInstance instance : _suites) {
    		if (instance.getSuite() == suite) {
    			removeSuite(instance);
    			return;
    		}
    	}
    }
    
    public void removeSuite(BenchmarkSuiteInstance suite) {
        getRunner().getProcess().stop();
        getRunner().getConfigurationManager().removeParametersForSuite(suite);
    	_suites.remove(suite);
    }
 
    public void removeAllSuites() {
        getRunner().getProcess().stop();

        for (BenchmarkSuiteInstance suite : _suites)
            getRunner().getConfigurationManager().removeParametersForSuite(suite);
        
        _suites.clear();
    }

}