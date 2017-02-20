package org.pipbenchmark.runner.console;

import java.util.*;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.ExecutionType;
import org.pipbenchmark.runner.MeasurementType;

public class CommandLineArgs {
    private List<String> _libraries = new ArrayList<String>();
    private List<String> _classes = new ArrayList<String>();
    private List<String> _benchmarks = new ArrayList<String>();
    private Map<String, String> _parameters = new HashMap<String, String>();
    private String _configurationFile;
    private String _reportFile = String.format("BenchmarkReport.txt");
    private long _duration = 30000;
    private boolean _showHelp = false;
    private boolean _showBenchmarks = false;
    private boolean _showParameters = false;
    private boolean _batchMode = false;
    private boolean _benchmarkEnvironment = false;
    private MeasurementType _measurementType = MeasurementType.Peak;
    private ExecutionType _executionType = ExecutionType.Proportional;
    private double _nominalRate = 1;

    public CommandLineArgs(String[] args) {
        processArguments(args);
    }

    private void processArguments(String[] args) {
        for (int index = 0; index < args.length; index++) {
        	String arg = args[index];
        	boolean moreArgs = index < args.length - 1;
        	
            if ((arg.equals("-a") || arg.equals("-j") || args.equals("--jar")) && moreArgs) {
                String library = args[++index];
                _libraries.add(library);
            } else if ((arg.equals("-l") || args.equals("--class")) && moreArgs) {
                    String clazz = args[++index];
                    _classes.add(clazz);
            } else if ((arg.equals("-b") || arg.equals("--benchmark")) && moreArgs) {
                String benchmark = args[++index];
                _benchmarks.add(benchmark);
            } else if ((arg.equals("-p") || arg.equals("--param")) && moreArgs) {
                String param = args[++index];
                int pos = param.indexOf('=');
                String key = pos > 0 ? param.substring(0, pos - 1) : param;
                String value = pos > 0 ? param.substring(pos + 1) : null;
                _parameters.put(key, value);
            } else if ((arg.equals("-c") || arg.equals("--config")) && moreArgs) {
                _configurationFile = args[++index];
            } else if ((arg.equals("-r") || arg.equals("--report")) && moreArgs) {
                _reportFile = args[++index];
            } else if ((arg.equals("-d") || arg.equals("--duration")) && moreArgs) {
                _duration = SimpleTypeConverter.stringToLong(args[++index], 30000);
            } else if ((arg.equals("-m") || arg.equals("--measure")) && moreArgs) {
                _measurementType = args[++index].toLowerCase().equals("nominal")
                    ? MeasurementType.Nominal : MeasurementType.Peak;
            } else if ((arg.equals("-e") || arg.equals("--execute")) && moreArgs) {
                String execution = args[++index].toLowerCase();
                _executionType = execution.equals("seq") || execution.equals("sequential")
                    ? ExecutionType.Sequential : ExecutionType.Proportional;
            } else if ((arg.equals("-n") || arg.equals("--nominal")) && moreArgs) {
                _nominalRate = SimpleTypeConverter.stringToDouble(args[++index], 1);
            } else if (arg.equals("-h") || arg.equals("--help")) {
                _showHelp = true;
            } else if (arg.equals("-B") || arg.equals("--show-benchmarks")) {
                _showBenchmarks = true;
            } else if (arg.equals("-P") || arg.equals("--show-params")) {
                _showParameters = true;
            } else if (arg.equals("-e") || arg.equals("--environment")) {
                _benchmarkEnvironment = true;
            } else if (arg.equals("--batch")) {
            	_batchMode = true;
            }
        }
    }

    public List<String> getLibraries() {
        return _libraries;
    }

    public List<String> getClasses() {
    	return _classes;
    }
    
    public List<String> getBenchmarks() {
        return _benchmarks;
    }

    public Map<String, String> getParameters() {
        return _parameters;
    }

    public String getConfigurationFile() {
        return _configurationFile;
    }

    public String getReportFile() {
        return _reportFile;
    }

    public long getDuration() {
        return _duration;
    }

    public boolean isBatchMode() {
    	return _batchMode;
    }
    
    public boolean getShowHelp() {
        return _showHelp;
    }

    public boolean isShowBanchmarks() {
        return _showBenchmarks;
    }

    public boolean isShowParameters() {
        return _showParameters;
    }

    public boolean getBenchmarkEnvironment() {
        return _benchmarkEnvironment;
    }

    public MeasurementType getMeasurementType() {
        return _measurementType;
    }

    public double getNominalRate() {
        return _nominalRate;
    }

    public ExecutionType getExecutionType() {
        return _executionType;
    }
}
