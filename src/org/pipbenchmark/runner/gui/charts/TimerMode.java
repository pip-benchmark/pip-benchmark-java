package org.pipbenchmark.runner.gui.charts;

/// <summary>
/// Chart Refresh Mode Timer Control Mode
/// </summary>
public enum TimerMode {
    /// <summary>
    /// Chart is refreshed when a value is added
    /// </summary>
    Disabled,
    /// <summary>
    /// Chart is refreshed every <c>TimerInterval</c> milliseconds, adding all values
    /// in the queue to the chart. If there are no values in the queue, a 0 (zero) is added
    /// </summary>
    Simple,
    /// <summary>
    /// Chart is refreshed every <c>TimerInterval</c> milliseconds, adding an average of
    /// all values in the queue to the chart. If there are no values in the queue,
    /// 0 (zero) is added
    /// </summary>
    SynchronizedAverage,
    /// <summary>
    /// Chart is refreshed every <c>TimerInterval</c> milliseconds, adding the sum of
    /// all values in the queue to the chart. If there are no values in the queue,
    /// 0 (zero) is added
    /// </summary>
    SynchronizedSum
}
