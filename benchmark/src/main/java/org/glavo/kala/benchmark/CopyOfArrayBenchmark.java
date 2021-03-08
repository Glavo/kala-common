package org.glavo.kala.benchmark;

import org.glavo.kala.collection.immutable.ImmutableArray;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@SuppressWarnings("ALL")
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

    //@Benchmark
    public Object[] copyOfHalfGenericArrayTest() {
        return Arrays.copyOf(array, N / 2);
    }

    //@Benchmark
    public Object[] copyOfHalfObjectArrayTest() {
        return copyOfObjectArray(array, N / 2);
    }

    static class TestImmutableArray1<E> {
        private static final TestImmutableArray1<?> EMPTY = new TestImmutableArray1<>(new Object[0]);

        final Object[] elements;

        TestImmutableArray1(Object[] elements) {
            this.elements = elements;
        }

        public final TestImmutableArray1<E> filter(Predicate<? super E> predicate) {
            Objects.requireNonNull(predicate);

            final Object[] elements = this.elements;
            final int size = elements.length;

            if (size == 0) {
                return this;
            }

            Object[] tmp = new Object[size];
            int c = 0;

            for (Object value : elements) {
                E v = (E) value;
                if (predicate.test(v)) {
                    tmp[c++] = v;
                }
            }

            if (c == 0) {
                return (TestImmutableArray1<E>) EMPTY;
            }
            if (c == size) {
                return this;
            }

            Object[] res = new Object[c];
            System.arraycopy(tmp, 0, res, 0, c);
            return new TestImmutableArray1<>(res);
        }

    }

    static class TestImmutableArray2<E> {
        private static final TestImmutableArray2<?> EMPTY = new TestImmutableArray2<>(new Object[0]);

        final Object[] elements;

        TestImmutableArray2(Object[] elements) {
            this.elements = elements;
        }

        public final TestImmutableArray2<E> filter(Predicate<? super E> predicate) {
            Objects.requireNonNull(predicate);

            final Object[] elements = this.elements;
            final int size = elements.length;

            if (size == 0) {
                return this;
            }

            Object[] tmp = new Object[size];
            int c = 0;

            for (Object value : elements) {
                E v = (E) value;
                if (predicate.test(v)) {
                    tmp[c++] = v;
                }
            }

            if (c == 0) {
                return (TestImmutableArray2<E>) EMPTY;
            }
            if (c == size) {
                return this;
            }
            return new TestImmutableArray2<>(Arrays.copyOf(tmp, c));
        }

    }

    @Benchmark
    public TestImmutableArray1<?> testTestImmutableArray1() {
        return new TestImmutableArray1<>(array).filter(obj -> System.identityHashCode(obj) > 0);
    }

    @Benchmark
    public TestImmutableArray2<?> testTestImmutableArray2() {
        return new TestImmutableArray2<>(array).filter(obj -> System.identityHashCode(obj) > 0);
    }

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include(CopyOfArrayBenchmark.class.getSimpleName())
                .build();
        new Runner(options).run();
    }
}
