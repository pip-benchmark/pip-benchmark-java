package org.pipbenchmark;

import java.util.*;

public interface IExecutionContext {
	Map<String, Parameter> getParameters();
	
    void incrementCounter();
    void incrementCounter(int increment);
    
    void sendMessage(String message);
    void reportError(String errorMessage);
    
    void stop();
}
