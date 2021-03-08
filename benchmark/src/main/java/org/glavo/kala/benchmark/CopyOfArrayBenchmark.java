package org.glavo.kala.benchmark;

import org.glavo.kala.collection.base.ObjectArrays;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CopyOfArrayBenchmark {
    private static final int N = 500000;
    private final Object[] array = new Object[N];

    {
        var r = new Random(0);
        for (int i = 0; i < N; i++) {
            array[i] = String.valueOf(r.nextInt());
        }
    }

    @Benchmark
    public Object[] copyOfHalfGenericArrayTest() {
        return Arrays.copyOf(array, N / 2);
    }

    @Benchmark
    public Object[] copyOfHalfObjectArrayTest() {
        return ObjectArrays.copyOf(array, N / 2);
    }

    @Benchmark
    public Object[] copyOfTwiceGenericArrayTest() {
        return Arrays.copyOf(array, N * 2);
    }

    @Benchmark
    public Object[] copyOfTwiceObjectArrayTest() {
        return ObjectArrays.copyOf(array, N * 2);
    }

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include(CopyOfArrayBenchmark.class.getSimpleName())
                .build();
        new Runner(options).run();
    }
}
