# Portable Benchmarking Framework in .NET Changelog

## <a name="1.0.1"></a> 1.0.1 (2017-01-14)

Added ability to track errors

### Features
* **errors** Reporting errors during execution
* **example** Added examples


## <a name="1.0.0"></a> 1.0.0 (2017-01-13)

Initial public release

### Features
* **benchmarks** Code is structured as Benchmark and BenchmarkSuite classes
* **passive** Passive benchmarks that perform and report their own measurements
* **sequencial** Sequencial execution of benchmarks. Each benchmark runs for specified duration
* **parallel** Parallel execution of benchmarks. Each benchmark is called proportioned to its frequency
* **peak** Peak performance makes maximum number of calls, one after another without wait
* **nominal** Nominal performance tries to maintain specified call frequency while measuring system load
* **environment** Taking key environment benchmarks (CPU, Video, Disk) to report for objective interpretation of results
* **console** Console runner to execute benchmarks
* **gui** GUI runner to execute benchmarks

### Breaking Changes
No breaking changes since this is the first version

### Bug Fixes
No fixes in this version

