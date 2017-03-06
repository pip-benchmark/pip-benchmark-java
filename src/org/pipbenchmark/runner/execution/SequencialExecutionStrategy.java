package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.results.*;

public class SequencialExecutionStrategy extends ExecutionStrategy {
    private boolean _running = false;
    private Thread _controlThread = null;
    private Thread[] _threads = null;
    private BenchmarkInstance _runningBenchmark = null;
    private double _ticksPerTransaction = 0;
    private List<BenchmarkResult> _results = new ArrayList<BenchmarkResult>();

    public SequencialExecutionStrategy(ConfigurationManager configuration,
		ExecutionManager process, List<BenchmarkInstance> benchmarks) {
        
    	super(configuration, process, benchmarks);
    }

    @Override
    public void start() {
        if (_configuration.getDuration() <= 0) {
            throw new NullPointerException("Duration was not set");
        }

        if (_configuration.getMeasurementType() == MeasurementType.Peak) {
            _ticksPerTransaction = 0;
        } else {
            _ticksPerTransaction = 1000.0 / _configuration.getNominalRate() 
            	* _configuration.getNumberOfThreads();
        }

        _running = true;

        // Start control thread
        _controlThread = new Thread(
        	new Runnable() {
        		@Override
        		public final void run() { controlBenchmarking(); }
        	});
        _controlThread.setName("Benchmarking Control Thread");
        _controlThread.start();
    }

    @Override
    public void stop() {
        _running = false;

        // Stop control thread
        if (_controlThread != null) {
            _controlThread.interrupt();
            _controlThread = null;
        }

        // Stop benchmarking threads
        stopBenchmarkingThreads();
    }

    @Override
    public List<BenchmarkResult> getResults() {
        return _results;
    }

    private void controlBenchmarking() {
        try {
            notifyResultUpdate(ExecutionState.Starting);

            for (BenchmarkInstance benchmark : getBenchmarks()) {
                initializeMeasurements();
                getCurrentResult().getBenchmarks().add(benchmark);

                benchmark.getSuite().setUp(new ExecutionContext(this, benchmark.getSuite()));
                try {
                    if (!benchmark.isPassive()) {
                        startBenchmarkingThreads(benchmark);
                    }

                    // Wait for set duration (in seconds)
                    try {
                    	Thread.sleep(_configuration.getDuration() * 1000);
                    } catch (InterruptedException ex) {
                    	break;
                    }

                    if (!benchmark.isPassive()) {
                        stopBenchmarkingThreads();
                    }
                } finally {
                    benchmark.getSuite().tearDown();
                }

                _results.add(getCurrentResult());
            }

            _controlThread = null;
            getProcess().stop();
        } finally {
            stopBenchmarkingThreads();
            notifyResultUpdate(ExecutionState.Completed);
        }
    }

    private void startBenchmarkingThreads(BenchmarkInstance test) {
        synchronized (getSyncRoot()) {
            _runningBenchmark = test;

            _threads = new Thread[_configuration.getNumberOfThreads()];
            for (int index = 0; index < _configuration.getNumberOfThreads(); index++) {
                _threads[index] = new Thread(
                	new Runnable() {
                		@Override
                		public final void run() { performBenchmarking(); }
                	});
                _threads[index].setName(String.format("Benchmarking Thread #%d/%d",
                    index, _configuration.getNumberOfThreads()));
                //_threads[index].setPriority(ThreadPriority.Highest);
                _threads[index].start();
            }
        }
    }

    private void stopBenchmarkingThreads() {
        synchronized (getSyncRoot()) {
            _runningBenchmark = null;
            
            if (_threads != null) {
                for (int index = 0; index < _threads.length; index++) {
                    _threads[index].interrupt();
                    _threads[index] = null;
                }
                _threads = null;
            }
        }
    }

    private void performBenchmarking() {
        BenchmarkInstance benchmark = _runningBenchmark;
        long lastExecutedTicks = System.currentTimeMillis();
        long endTicks = System.currentTimeMillis() 
        	+ _configuration.getDuration() * 1000;

        try {
            long currentTicks = System.currentTimeMillis();

            while (_running && benchmark == _runningBenchmark && endTicks > currentTicks) {
                if (_configuration.getMeasurementType() == MeasurementType.Nominal) {
                    double ticksToNextTransaction = _ticksPerTransaction
                        - (currentTicks - lastExecutedTicks);
                    
                    // Wait to ensure nominal transaction rate
                    if (ticksToNextTransaction > 0)
                        Thread.sleep((int)ticksToNextTransaction);
                }

                lastExecutedTicks = currentTicks;
                executeBenchmark(benchmark);
                currentTicks = System.currentTimeMillis();
                incrementCounter(1, lastExecutedTicks);
            }
        } catch (InterruptedException ex) {
            // Ignore the exception...
        }
    }
}
