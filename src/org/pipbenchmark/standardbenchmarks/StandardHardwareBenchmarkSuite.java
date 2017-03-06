package org.pipbenchmark.standardbenchmarks;

import org.pipbenchmark.*;

public class StandardHardwareBenchmarkSuite extends BenchmarkSuite {
    private StandardCpuBenchmark _cpuBenchmarkTest;
    private StandardDiskBenchmark _diskBenchmarkTest;
    private StandardVideoBenchmark _videoBenchmarkTest;

    public StandardHardwareBenchmarkSuite() {
        super("StandardBenchmark", "Standard hardware benchmark");

        _cpuBenchmarkTest = new StandardCpuBenchmark();
        addBenchmark(_cpuBenchmarkTest);

        _diskBenchmarkTest = new StandardDiskBenchmark();
        addBenchmark(_diskBenchmarkTest);

        _videoBenchmarkTest = new StandardVideoBenchmark();
        addBenchmark(_videoBenchmarkTest);
    }

    public StandardCpuBenchmark getCpuBenchmarkTest() {
        return _cpuBenchmarkTest;
    }

    public StandardDiskBenchmark getDiskBenchmarkTest() {
        return _diskBenchmarkTest;
    }

    public StandardVideoBenchmark getVideoBenchmarkTest() {
        return _videoBenchmarkTest;
    }

    @Override
    public void setUp() {}

    @Override
    public void tearDown() {}
}
