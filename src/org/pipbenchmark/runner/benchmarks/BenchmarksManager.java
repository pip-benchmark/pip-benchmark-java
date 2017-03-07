package org.pipbenchmark.runner.benchmarks;

import java.util.*;
import java.util.jar.*;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.params.*;

import java.io.*;
import java.net.*;

public class BenchmarksManager {
	private ParametersManager _parameters;
    private List<BenchmarkSuiteInstance> _suites = new ArrayList<BenchmarkSuiteInstance>();

    public BenchmarksManager(ParametersManager parameters) {
    	_parameters = parameters;
    }

    public List<BenchmarkSuiteInstance> getSuites() {
        return _suites;
    }

    public List<BenchmarkInstance> getSelected() {
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

    public void selectAll() {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks())
                benchmark.setSelected(true);
        }
    }

    public void selectByName(String[] benchmarkNames) {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
            	for (String benchmarkName : benchmarkNames) {
            		if (benchmarkName.equals(benchmark.getFullName()))
            			benchmark.setSelected(true);
            	}
            }
        }
    }

    public void select(BenchmarkInstance[] benchmarks) {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
            	for (BenchmarkInstance anotherBenchmark : benchmarks) {
            		if (benchmark == anotherBenchmark)
            			benchmark.setSelected(true);
            	}
            }
        }
    }

    public void unselectAll() {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks())
                benchmark.setSelected(false);
        }
    }

    public void unselectByName(String[] benchmarkNames) {
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
            	for (String benchmarkName : benchmarkNames) {
            		if (benchmarkName.equals(benchmark.getFullName()))
            			benchmark.setSelected(false);
            	}
            }
        }
    }

    public void unselect(BenchmarkInstance[] benchmarks) {
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
    	_suites.add(suite);
        _parameters.addSuite(suite);
    }
    
    public void addSuitesFromLibrary(String fileName) throws IOException {
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
        	                _parameters.addSuite(suite);
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
    
    public void removeSuite(String suiteName) {
        BenchmarkSuiteInstance suite = null;
        for (BenchmarkSuiteInstance thisSuite : _suites) {
            if (thisSuite.getName().equalsIgnoreCase(suiteName)) {
                suite = thisSuite;
                break;
            }
        }

        if (suite != null) {
            _parameters.removeSuite(suite);
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
        _parameters.removeSuite(suite);
    	_suites.remove(suite);
    }
 
    public void clear() {
        for (BenchmarkSuiteInstance suite : _suites)
            _parameters.removeSuite(suite);
        
        _suites.clear();
    }

}
