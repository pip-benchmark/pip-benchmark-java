package org.pipbenchmark.console;

class HelpPrinter {
    public static void print() {
        System.out.println("Pip.Benchmark Console Runner. (c) Conceptual Vision Consulting LLC 2017");
        System.out.println();
        System.out.println("Command Line Parameters:");
        System.out.println("-a <assembly>    - Library (jar) with benchmarks to be loaded. You may include multiple jars");
        System.out.println("-p <param>=<value> - Setting parameter value. You may include multiple parameters");
        System.out.println("-b <benchmark>   - Name of benchmark to be executed. You may include multiple benchmarks");
        System.out.println("-c <config file> - File with parameters to be loaded");
        System.out.println("-r <report file> - File to save benchmarking report");
        System.out.println("-d <seconds>     - Benchmarking time specified in seconds");
        System.out.println("-h               - Display this help screen");
        System.out.println("-B               - Show all available benchmarks");
        System.out.println("-P               - Show all available parameters");
        System.out.println("-e               - Benchmark environment");
        System.out.println("-x [proportional|sequencial] - Execution type: Proportional or Sequencial");
        System.out.println("-m [peak|nominal] - Measurement type: Peak or Nominal");
        System.out.println("-n <rate>        - Nominal rate in transactions per second");
    }
}
