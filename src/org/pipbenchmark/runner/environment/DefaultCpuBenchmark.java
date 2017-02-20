package org.pipbenchmark.runner.environment;

import org.pipbenchmark.*;

public class DefaultCpuBenchmark extends Benchmark {
    private final static int NumberOfAttempts = 20000;

    public DefaultCpuBenchmark() {
        super("CPU", "Measures CPU performance");
    }

    @Override
    public final void execute() {
        // Count increment, comparison and goto for 1 arithmetic operation
        for (double value = 0; value < NumberOfAttempts; value++) {
            // #1
            double result1 = value + value;
            double result2 = result1 - value;
            double result3 = result1 * result2;
            double result4 = result2 / result3;
            Math.log(result4);

            // #2
            result1 = value + value;
            result2 = result1 - value;
            result3 = result1 * result2;
            result4 = result2 / result3;
            Math.log(result4);

            // #3
            result1 = value + value;
            result2 = result1 - value;
            result3 = result1 * result2;
            result4 = result2 / result3;
            Math.log(result4);

            // #4
            result1 = value + value;
            result2 = result1 - value;
            result3 = result1 * result2;
            result4 = result2 / result3;
            Math.log(result4);

            // #5
            result1 = value + value;
            result2 = result1 - value;
            result3 = result1 * result2;
            result4 = result2 / result3;
            Math.log(result4);

            // #6
            result1 = value + value;
            result2 = result1 - value;
            result3 = result1 * result2;
            result4 = result2 / result3;
            Math.log(result4);

            // #7
            result1 = value + value;
            result2 = result1 - value;
            result3 = result1 * result2;
            result4 = result2 / result3;
            Math.log(result4);

            // #8
            result1 = value + value;
            result2 = result1 - value;
            result3 = result1 * result2;
            result4 = result2 / result3;
            Math.log(result4);

            // #9
            result1 = value + value;
            result2 = result1 - value;
            result3 = result1 * result2;
            result4 = result2 / result3;
            Math.log(result4);

            // #10
            //result1 = value + value;
            //result2 = result1 - value;
            //result3 = result1 * result2;
            result4 = result2 / result3;
            Math.log(result4);
        }
    }
}
