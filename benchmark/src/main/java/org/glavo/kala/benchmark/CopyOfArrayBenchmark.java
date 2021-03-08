package org.glavo.kala.benchmark;

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

    public static Object[] copyOfObjectArray(Object[] array, int newLength) {
        Object[] res = new Object[newLength];
        System.arraycopy(array, 0, res, 0, Integer.min(array.length, newLength));
        return res;
    }

    public static Object[] copyOfRange(Object[] array, int beginIndex, int endIndex) {
        int newLength = endIndex - beginIndex;
        if (newLength < 0) {
            throw new IllegalArgumentException("beginIndex(" + beginIndex + ") > endIndex(" + endIndex + ")");
        }
        Object[] res = new Object[newLength];
        System.arraycopy(array, beginIndex, res, 0, Integer.min(array.length - beginIndex, newLength));
        return res;
    }

    @Benchmark
    public Object[] copyOfHalfGenericArrayTest() {
        return Arrays.copyOf(array, N / 2);
    }

    @Benchmark
    public Object[] copyOfHalfObjectArrayTest() {
        return copyOfObjectArray(array, N / 2);
    }

    @Benchmark
    public Object[] copyOfTwiceGenericArrayTest() {
        return Arrays.copyOf(array, N * 2);
    }

    @Benchmark
    public Object[] copyOfTwiceObjectArrayTest() {
        return copyOfObjectArray(array, N * 2);
    }

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include(CopyOfArrayBenchmark.class.getSimpleName())
                .build();
        new Runner(options).run();
    }
}
