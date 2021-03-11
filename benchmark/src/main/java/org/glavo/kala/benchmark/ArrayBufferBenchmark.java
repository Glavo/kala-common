package org.glavo.kala.benchmark;

import org.glavo.kala.collection.mutable.ArrayBuffer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ArrayBufferBenchmark {

    private static final int N = 500000;

    Integer[] data = new Integer[N];

    {
        var r = new Random(0);
        for (int i = 0; i < N; i++) {
            data[i] = r.nextInt();
        }
    }

    @Benchmark
    @SuppressWarnings("ManualArrayToCollectionCopy")
    public Object testArrayListAdd() {
        ArrayList<Integer> list = new ArrayList<>();
        for (Integer i : data) {
            //noinspection UseBulkOperation
            list.add(i);
        }
        return list;
    }

    @Benchmark
    public Object testArrayBufferAppend() {
        ArrayBuffer<Integer> buffer = new ArrayBuffer<>();

        for (Integer i : data) {
            buffer.append(i);
        }
        return buffer;
    }
}
