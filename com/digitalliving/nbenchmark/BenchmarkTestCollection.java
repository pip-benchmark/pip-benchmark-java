package com.digitalliving.nbenchmark;

import java.util.*;

public class BenchmarkTestCollection extends ArrayList<BenchmarkTest> {
	private static final long serialVersionUID = 636550589659180982L;

	public BenchmarkTest findByName(String name) {
        for (BenchmarkTest test : this) {
            if (test.getName().equalsIgnoreCase(name)) {
                return test;
            }
        }
        return null;
    }
}
