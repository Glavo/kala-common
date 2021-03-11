package org.glavo.kala.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
    public static void main(String[] args) throws RunnerException {
        var builder = new OptionsBuilder();

        for (String arg : args) {
            builder.include(arg);
        }
        var runner = new Runner(builder.build());
        runner.run();
    }
}
