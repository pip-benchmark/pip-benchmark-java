package org.pipbenchmark.runner.environment;

import org.pipbenchmark.*;

public class StandardBenchmarkSuite extends BenchmarkSuite {
    private DefaultCpuBenchmark _cpuBenchmark;
    private DefaultDiskBenchmark _diskBenchmark;
    private DefaultVideoBenchmark _videoBenchmark;

    public StandardBenchmarkSuite() {
        super("StandardBenchmark", "Measures overall system performance");
        
        _cpuBenchmark = new DefaultCpuBenchmark();
        addBenchmark(_cpuBenchmark);

        _diskBenchmark = new DefaultDiskBenchmark();
        addBenchmark(_diskBenchmark);

        _videoBenchmark = new DefaultVideoBenchmark();
        addBenchmark(_videoBenchmark);

        createParameter("FilePath", "Path where test file is located on disk", "");
        createParameter("FileSize", "Size of the test file", "102400000");
        createParameter("ChunkSize", "Size of a chunk that read or writter from/to test file", "1024000");
        createParameter("OperationTypes", "Types of test operations: read, write or all", "all");
    }

    public DefaultCpuBenchmark getCpuBenchmark() {
        return _cpuBenchmark;
    }

    public DefaultDiskBenchmark getDiskBenchmark() {
        return _diskBenchmark;
    }

    public DefaultVideoBenchmark getVideoBenchmark() {
        return _videoBenchmark;
    }
}
