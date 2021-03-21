package org.glavo.kala.collection.immutable;

import org.glavo.kala.function.IndexedConsumer;
import org.glavo.kala.function.IndexedFunction;
import org.glavo.kala.collection.base.AbstractIterator;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.base.GenericArrays;
import org.glavo.kala.collection.base.Traversable;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.glavo.kala.Conditions.*;
import static java.util.Arrays.copyOf;

@SuppressWarnings({"unchecked", "rawtypes"})
final class ImmutableVectors {
    @Debug.Renderer(hasChildren = "false", childrenArray = "new Object[0]")
    final static class Vector0 extends ImmutableVector<Object> {
        private static final long serialVersionUID = 6286060267578716429L;

        static final Vector0 INSTANCE = new Vector0();

        private Vector0() {
            super(GenericArrays.EMPTY_OBJECT_ARRAY);
        }

        @Override
        final int vectorSliceCount() {
            return 0;
        }

        @Override
        final Object[] vectorSlice(int idx) {
            return null;
        }

        @Override
        final int vectorSlicePrefixLength(int idx) {
            return 0;
        }

        @Override
        public final @NotNull Iterator<Object> iterator() {
            return Iterators.empty();
        }

        @Override
        public final @NotNull Spliterator<Object> spliterator() {
            return Spliterators.emptySpliterator();
        }

        @Override
        public final int size() {
            return 0;
        }

        @Override
        public final Object get(int index) {
            throw new IndexOutOfBoundsException();
        }

        //region Addition Operations

        @Override
        public final @NotNull ImmutableVector<Object> appended(Object value) {
            return new Vector1<>(new Object[]{value});
        }

        @Override
        public final @NotNull ImmutableVector<Object> appendedAll(Object @NotNull [] values) {
            return from(values);
        }

        @Override
        public final @NotNull ImmutableVector<Object> appendedAll(@NotNull ImmutableVector<?> values) {
            return (ImmutableVector<Object>) Objects.requireNonNull(values);
        }

        @Override
        public final @NotNull ImmutableVector<Object> appendedAll(@NotNull Iterable<?> values) {
            return from(values);
        }

        @Override
        public final @NotNull ImmutableVector<Object> prepended(Object value) {
            return new Vector1<>(new Object[]{value});
        }

        @Override
        public final @NotNull ImmutableVector<Object> prependedAll(@NotNull Iterable<?> values) {
            return from(values);
        }

        @Override
        public final @NotNull ImmutableVector<Object> prependedAll(@NotNull ImmutableVector<?> values) {
            return (ImmutableVector<Object>) Objects.requireNonNull(values);
        }

        @Override
        public final @NotNull ImmutableVector<Object> prependedAll(Object @NotNull [] values) {
            return from(values);
        }

        //endregion


        @Override
        final @NotNull ImmutableVector<Object> slice0(int lo, int hi) {
            throw new AssertionError();
        }

        @Override
        final ImmutableVector<Object> filterImpl(Predicate<? super Object> predicate, boolean isFlipped) {
            return this;
        }

        @Override
        public final @NotNull <U> ImmutableVector<U> map(@NotNull Function<? super Object, ? extends U> mapper) {
            return (ImmutableVector<U>) this;
        }

        @Override
        public @NotNull <U> ImmutableVector<U> mapIndexed(@NotNull IndexedFunction<? super Object, ? extends U> mapper) {
            return (ImmutableVector<U>) this;
        }

        @Override
        public final void forEach(@NotNull Consumer<? super Object> action) {
            // do nothing
        }

        @Override
        public final void forEachIndexed(@NotNull IndexedConsumer<? super Object> action) {
            // do nothing
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    @Debug.Renderer(hasChildren = "true", childrenArray = "prefix1")
    final static class Vector1<E> extends ImmutableVector<E> {
        private static final long serialVersionUID = -2956354586637109936L;

        Vector1(Object[] prefix1) {
            super(prefix1);
        }

        @Override
        final int vectorSliceCount() {
            return 1;
        }

        @Override
        final Object[] vectorSlice(int idx) {
            return prefix1;
        }

        @Override
        final int vectorSlicePrefixLength(int idx) {
            return prefix1.length;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return (Iterator<E>) GenericArrays.iterator(prefix1);
        }

        @Override
        public final @NotNull Spliterator<E> spliterator() {
            return (Spliterator<E>) Arrays.spliterator(prefix1);
        }

        @Override
        public final int size() {
            return prefix1.length;
        }

        @Override
        public final E get(int index) {
            try {
                return (E) prefix1[index];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException();
            }
        }

        //region Addition Operations


        @Override
        public final @NotNull ImmutableVector<E> appended(E value) {
            final Object[] prefix1 = this.prefix1;
            final int len1 = prefix1.length;
            if (len1 < WIDTH) {
                return new Vector1(copyAppend1(prefix1, value));
            } else {
                return new Vector2(prefix1, WIDTH, empty2, wrap1(value), WIDTH + 1);
            }
        }

        @Override
        public final @NotNull ImmutableVector<E> appendedAll(E @NotNull [] values) {
            final int vl = values.length;
            if (vl == 0) {
                return this;
            }

            final Object[] prefix1 = this.prefix1;
            final int size = prefix1.length;

            if (size + vl <= WIDTH) {
                Object[] res = new Object[size + vl];
                System.arraycopy(prefix1, 0, res, 0, size);
                System.arraycopy(values, 0, res, size, vl);
                return new Vector1<>(res);
            }

            VectorBuilder<E> builder = new VectorBuilder<>();
            builder.initFrom(this);
            builder.addAll(values);
            return builder.build();
        }

        @Override
        public final @NotNull ImmutableVector<E> prepended(E value) {
            final Object[] prefix1 = this.prefix1;
            final int len1 = prefix1.length;
            if (len1 < WIDTH) {
                return new Vector1(copyPrepend1(value, prefix1));
            } else {
                return new Vector2(wrap1(value), 1, empty2, prefix1, len1 + 1);
            }
        }

        @Override
        public final @NotNull ImmutableVector<E> prependedAll(E @NotNull [] values) {
            int vl = values.length;
            if (vl == 0) {
                return this;
            }
            final Object[] prefix1 = this.prefix1;
            final int size = prefix1.length;

            if (size + vl <= WIDTH) {
                Object[] res = new Object[vl + size];
                System.arraycopy(values, 0, res, 0, vl);
                System.arraycopy(prefix1, 0, res, vl, size);
                return new Vector1<>(res);
            }

            VectorBuilder<E> builder = new VectorBuilder<>();
            builder.addAll(values);
            builder.addVector(this);
            return builder.build();
        }

        //endregion

        @Override
        public final E first() {
            return (E) prefix1[0];
        }

        @Override
        public final E last() {
            return (E) prefix1[prefix1.length - 1];
        }

        @Override
        final @NotNull ImmutableVector<E> slice0(int lo, int hi) {
            final Object[] elements = this.prefix1;
            final int size = elements.length;

            final int newSize = hi - lo;

            Object[] res = new Object[newSize];
            System.arraycopy(elements, lo, res, 0, newSize);
            return new Vector1<>(res);
        }

        @Override
        public final @NotNull ImmutableVector<E> updated(int index, E newValue) {
            final Object[] prefix1 = this.prefix1;
            final int size = prefix1.length;
            checkElementIndex(index, size);
            Object[] res = prefix1.clone();
            res[index] = newValue;
            return new ImmutableVectors.Vector1<>(res);
        }

        @Override
        final ImmutableVector<E> filterImpl(Predicate<? super E> predicate, boolean isFlipped) {
            final Object[] prefix1 = this.prefix1;
            final int size = prefix1.length;

            Object[] temp = new Object[size];
            int c = 0;

            for (Object value : prefix1) {
                E v = (E) value;
                if (predicate.test(v) != isFlipped) {
                    temp[c++] = v;
                }
            }

            if (c == 0) {
                return empty();
            }
            if (c == size) {
                return this;
            }

            return new Vector1<>(Arrays.copyOf(temp, c));
        }

        @Override
        public final @NotNull <U> ImmutableVector<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            final Object[] prefix1 = this.prefix1;
            final int size = prefix1.length;

            Object[] res = new Object[size];
            for (int i = 0; i < size; i++) {
                res[i] = mapper.apply((E) prefix1[i]);
            }
            return new Vector1<>(res);
        }

        @Override
        public final @NotNull <U> ImmutableVector<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            final Object[] prefix1 = this.prefix1;
            final int size = prefix1.length;

            Object[] res = new Object[size];
            for (int i = 0; i < size; i++) {
                res[i] = mapper.apply(i, (E) prefix1[i]);
            }
            return new Vector1<>(res);
        }

        @Override
        public final void forEach(@NotNull Consumer<? super E> action) {
            for (Object element : prefix1) {
                action.accept((E) element);
            }
        }

        @Override
        public final void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
            final Object[] prefix1 = this.prefix1;
            final int size = prefix1.length;
            for (int i = 0; i < size; i++) {
                action.accept(i, (E) prefix1[i]);
            }
        }
    }

    @Debug.Renderer(hasChildren = "true", childrenArray = "toArray()")
    static abstract class BigVector<E> extends ImmutableVector<E> {
        final Object[] suffix1;
        final int length0;

        BigVector(Object[] prefix1, Object[] suffix1, int length0) {
            super(prefix1);
            this.suffix1 = suffix1;
            this.length0 = length0;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return new Itr<>(this, length0, vectorSliceCount());
        }

        @Override
        public final @NotNull Spliterator<E> spliterator() {
            return new Itr<>(this, length0, vectorSliceCount());
        }

        @Override
        public final int size() {
            return length0;
        }

        @Override
        public final E first() {
            return (E) prefix1[0];
        }

        @Override
        public final E last() {
            return (E) suffix1[suffix1.length - 1];
        }

        @Override
        final ImmutableVector<E> filterImpl(Predicate<? super E> predicate, boolean isFlipped) {
            final Object[] prefix1 = this.prefix1;
            final int len = prefix1.length;
            int i = 0;
            while (i != len) {
                if (predicate.test((E) prefix1[i]) == isFlipped) {
                    // each 1 bit indicates that index passes the filter.
                    // all indices < i are also assumed to pass the filter
                    int bitmap = 0;
                    int j = i + 1;
                    while (j < len) {
                        if (predicate.test((E) prefix1[j]) != isFlipped) {
                            bitmap |= (1 << j);
                        }
                        ++j;
                    }
                    final int newLen = i + Integer.bitCount(bitmap);

                    VectorBuilder<E> b = new VectorBuilder<>();
                    int k = 0;
                    while (k < i) {
                        b.add((E) prefix1[k]);
                        ++k;
                    }
                    k = i + 1;
                    while (i != newLen) {
                        if (((1 << k) & bitmap) != 0) {
                            b.add((E) prefix1[k]);
                            i += 1;
                        }
                        ++k;
                    }
                    this.forEachRest(v -> {
                        if (predicate.test(v) != isFlipped) {
                            b.add(v);
                        }
                    });
                    return b.build();
                }
                ++i;
            }
            VectorBuilder<E> b = new VectorBuilder<>();
            b.initFrom(prefix1);
            this.forEachRest(v -> {
                if (predicate.test(v) != isFlipped) {
                    b.add(v);
                }
            });
            return b.build();
        }

        @Override
        public final void forEach(@NotNull Consumer<? super E> action) {
            int c = vectorSliceCount();
            int i = 0;
            while (i < c) {
                forEachRec(vectorSliceDim(c, i) - 1, vectorSlice(i), action);
                ++i;
            }
        }

        private void forEachRest(@NotNull Consumer<? super E> action) {
            int c = vectorSliceCount();
            int i = 1;
            while (i < c) {
                forEachRec(vectorSliceDim(c, i) - 1, vectorSlice(i), action);
                ++i;
            }
        }
    }

    //region VectorInline

    public static final int BITS = 5;
    public static final int WIDTH = 1 << BITS;
    public static final int MASK = WIDTH - 1;
    public static final int BITS2 = BITS * 2;
    public static final int WIDTH2 = 1 << BITS2;
    public static final int BITS3 = BITS * 3;
    public static final int WIDTH3 = 1 << BITS3;
    public static final int BITS4 = BITS * 4;
    public static final int WIDTH4 = 1 << BITS4;
    public static final int BITS5 = BITS * 5;
    public static final int WIDTH5 = 1 << BITS5;
    public static final int BITS6 = BITS * 6;
    public static final int WIDTH6 = 1 << BITS6;
    public static final int LASTWIDTH = WIDTH << 1; // 1 extra bit in the last level to go up to Int.MaxValue (2^31-1) instead of 2^30:
    public static final int Log2ConcatFaster = 5;

    /**
     * Dimension of the slice at index.
     */
    static int vectorSliceDim(int count, int idx) {
        final int c = count / 2;
        return c + 1 - Math.abs(idx - c);
    }

    @SuppressWarnings("SameParameterValue")
    static <T> T[] copyOrUse(T[] a, int start, int end) {
        return start == 0 && end == a.length
                ? a
                : Arrays.copyOfRange(a, start, end);
    }

    static <T> T[] copyTail(T[] a) {
        return Arrays.copyOfRange(a, 1, a.length);
    }

    static <T> T[] copyInit(T[] a) {
        return Arrays.copyOfRange(a, 0, a.length - 1);
    }

    static <T> T[] copyIfDifferentSize(T[] a, int len) {
        return a.length == len
                ? a
                : copyOf(a, len);
    }

    static Object[] wrap1(Object x) {
        Object[] a = new Object[1];
        a[0] = x;
        return a;
    }

    static Object[][] wrap2(Object[] x) {
        Object[][] a = new Object[1][];
        a[0] = x;
        return a;
    }

    static Object[][][] wrap3(Object[][] x) {
        Object[][][] a = new Object[1][][];
        a[0] = x;
        return a;
    }

    static Object[][][][] wrap4(Object[][][] x) {
        Object[][][][] a = new Object[1][][][];
        a[0] = x;
        return a;
    }

    static Object[][][][][] wrap5(Object[][][][] x) {
        Object[][][][][] a = new Object[1][][][][];
        a[0] = x;
        return a;
    }

    static Object[] copyUpdate(Object[] a1, int idx1, Object elem) {
        Object[] a1c = a1.clone();
        a1c[idx1] = elem;
        return a1c;
    }

    static Object[][] copyUpdate(Object[][] a2, int idx2, int idx1, Object elem) {
        Object[][] a2c = a2.clone();
        a2c[idx2] = copyUpdate(a2c[idx2], idx1, elem);
        return a2c;
    }

    static Object[][][] copyUpdate(Object[][][] a3, int idx3, int idx2, int idx1, Object elem) {
        Object[][][] a3c = a3.clone();
        a3c[idx3] = copyUpdate(a3c[idx3], idx2, idx1, elem);
        return a3c;
    }

    static Object[][][][] copyUpdate(Object[][][][] a4, int idx4, int idx3, int idx2, int idx1, Object elem) {
        Object[][][][] a4c = a4.clone();
        a4c[idx4] = copyUpdate(a4c[idx4], idx3, idx2, idx1, elem);
        return a4c;
    }

    static Object[][][][][] copyUpdate(Object[][][][][] a5, int idx5, int idx4, int idx3, int idx2, int idx1, Object elem) {
        Object[][][][][] a5c = a5.clone();
        a5c[idx5] = copyUpdate(a5c[idx5], idx4, idx3, idx2, idx1, elem);
        return a5c;
    }

    static Object[][][][][][] copyUpdate(Object[][][][][][] a6, int idx6, int idx5, int idx4, int idx3, int idx2, int idx1, Object elem) {
        Object[][][][][][] a6c = a6.clone();
        a6c[idx6] = copyUpdate(a6c[idx6], idx5, idx4, idx3, idx2, idx1, elem);
        return a6c;
    }

    static <T> T[] concatArrays(T[] a, T[] b) {
        T[] dest = copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, dest, a.length, b.length);
        return dest;
    }

    //endregion

    //region VectorStatics

    static Object[] copyAppend1(Object[] a, Object elem) {
        final int alen = a.length;
        Object[] ac = new Object[alen + 1];
        System.arraycopy(a, 0, ac, 0, alen);
        ac[alen] = elem;
        return ac;
    }

    static <T> T[] copyAppend(T[] a, T elem) {
        T[] ac = copyOf(a, a.length + 1);
        ac[ac.length - 1] = elem;
        return ac;
    }

    static Object[] copyPrepend1(Object elem, Object[] a) {
        Object[] ac = new Object[a.length + 1];
        System.arraycopy(a, 0, ac, 1, a.length);
        ac[0] = elem;
        return ac;
    }

    static <T> T[] copyPrepend(T elem, T[] a) {
        T[] ac = (T[]) Array.newInstance(a.getClass().getComponentType(), a.length + 1);
        System.arraycopy(a, 0, ac, 1, a.length);
        ac[0] = elem;
        return ac;
    }

    static final Object[] empty1 = new Object[0];
    static final Object[][] empty2 = new Object[0][];
    static final Object[][][] empty3 = new Object[0][][];
    static final Object[][][][] empty4 = new Object[0][][][];
    static final Object[][][][][] empty5 = new Object[0][][][][];
    static final Object[][][][][][] empty6 = new Object[0][][][][][];

    static void forEachRec(int level, Object[] a, Consumer f) {
        int i = 0;
        final int len = a.length;
        if (level == 0) {
            while (i < len) {
                f.accept(a[i++]);
            }
        } else {
            final int l = level - 1;
            while (i < len) {
                forEachRec(l, (Object[]) a[i++], f);
            }
        }
    }

    static Object[] mapElems1(Object[] a, Function f) {
        int i = 0;
        while (i < a.length) {
            Object v1 = a[i];
            Object v2 = f.apply(v1);
            if (v1 != v2) {
                return mapElems1Rest(a, f, i, v2);
            }
            ++i;
        }
        return a;
    }

    static Object[] mapElems1Rest(Object[] a, Function f, int at, Object v2) {
        Object[] ac = new Object[a.length];
        if (at > 0) {
            System.arraycopy(a, 0, ac, 0, at);
        }
        ac[at] = v2;
        int i = at + 1;
        while (i < a.length) {
            ac[i] = f.apply(a[i]);
            ++i;
        }
        return ac;
    }

    static <T> T[] mapElems(int n, T[] a, Function f) {
        if (n == 1) {
            return (T[]) mapElems1(a, f);
        }

        int i = 0;
        while (i < a.length) {
            T v1 = a[i];
            Object[] v2 = mapElems(n - 1, (Object[]) v1, f);
            if (v1 != v2) {
                return mapElemsRest(n, a, f, i, v2);
            }
            ++i;
        }
        return a;
    }

    static <T> T[] mapElemsRest(int n, T[] a, Function f, int at, Object v2) {
        Object[] ac = (Object[]) Array.newInstance(a.getClass().getComponentType(), a.length);
        if (at > 0) {
            System.arraycopy(a, 0, ac, 0, at);
        }
        ac[at] = v2;
        int i = at + 1;
        while (i < a.length) {
            ac[i] = mapElems(n - 1, ((Object[]) a[i]), f);
            ++i;
        }
        return (T[]) ac;
    }

    static Object[] prepend1IfSpace(Object[] prefix1, Iterable<?> xs) {
        return null; // TODO
    }

    static Object[] append1IfSpace(Object[] suffix1, Iterable<?> xs) {
        if (xs instanceof Traversable<?>) {
            Traversable<?> it = (Traversable<?>) xs;
            if (it.sizeCompare(WIDTH - suffix1.length) <= 0) {
                int s = it.size();
                if (s == 0) {
                    return null;
                }
                if (s == 1) {
                    return copyAppend(suffix1, it.iterator().next()); // TODO: opt
                }

                Object[] suffix1b = copyOf(suffix1, suffix1.length + s);
                it.copyToArray(suffix1.length, suffix1b);
                return suffix1b;
            } else {
                return null;
            }
        }
        return null; // TODO
    }

    //endregion

    static final class Vector2<E> extends BigVector<E> {
        private final int len1;
        private final Object[] @NotNull [] data2;

        Vector2(Object @NotNull [] prefix1, int len1, Object[] @NotNull [] data2, Object @NotNull [] suffix1, int length0) {
            super(prefix1, suffix1, length0);
            this.len1 = len1;
            this.data2 = data2;
        }

        @Override
        final int vectorSliceCount() {
            return 3;
        }

        @Override
        final Object[] vectorSlice(int idx) {
            switch (idx) {
                case 0:
                    return prefix1;
                case 1:
                    return data2;
                case 2:
                    return suffix1;
            }
            throw new AssertionError();
        }

        @Override
        final int vectorSlicePrefixLength(int idx) {
            switch (idx) {
                case 0:
                    return len1;
                case 1:
                    return length0 - suffix1.length;
                case 2:
                    return length0;
            }
            throw new AssertionError();
        }

        @Override
        public final E get(int index) {
            checkElementIndex(index, length0);

            int io = index - len1;
            if (io >= 0) {
                int i2 = io >>> BITS;
                int i1 = io & MASK;
                if (i2 < data2.length) {
                    return (E) data2[i2][i1];
                } else {
                    return (E) suffix1[io & MASK];
                }
            } else {
                return (E) prefix1[index];
            }
        }

        @Override
        final @NotNull ImmutableVector<E> slice0(int lo, int hi) {
            VectorSliceBuilder b = new VectorSliceBuilder(lo, hi);
            b.consider(1, prefix1);
            b.consider(2, data2);
            b.consider(1, suffix1);
            return b.build();
        }
    }

    static final class Vector3<E> extends BigVector<E> {
        final int len1;
        final Object[][] prefix2;
        final int len12;
        final Object[][][] data3;
        final Object[][] suffix2;

        Vector3(Object[] prefix1, int len1, Object[][] prefix2, int len12, Object[][][] data3, Object[][] suffix2, Object[] suffix1, int length0) {
            super(prefix1, suffix1, length0);
            this.len1 = len1;
            this.prefix2 = prefix2;
            this.len12 = len12;
            this.data3 = data3;
            this.suffix2 = suffix2;
        }

        @Override
        final int vectorSliceCount() {
            return 5;
        }

        @Override
        final Object[] vectorSlice(int idx) {
            switch (idx) {
                case 0:
                    return prefix1;
                case 1:
                    return prefix2;
                case 2:
                    return data3;
                case 3:
                    return suffix2;
                case 4:
                    return suffix1;
            }
            throw new AssertionError();
        }

        @Override
        final int vectorSlicePrefixLength(int idx) {
            switch (idx) {
                case 0:
                    return len1;
                case 1:
                    return len12;
                case 2:
                    return len12 + data3.length * WIDTH2;
                case 3:
                    return length0 - suffix1.length;
                case 4:
                    return length0;
            }
            throw new AssertionError();
        }

        @Override
        public final E get(int index) {
            checkElementIndex(index, length0);

            int io = index - len12;
            if (io >= 0) {
                int i3 = io >>> BITS2;
                int i2 = (io >>> BITS) & MASK;
                int i1 = io & MASK;

                if (i3 < data3.length) {
                    return (E) data3[i3][i2][i1];
                } else if (i2 < suffix2.length) {
                    return (E) suffix2[i2][i1];
                } else {
                    return (E) suffix1[i1];
                }
            } else if (index >= len1) {
                io = index - len1;
                return (E) prefix2[io >>> BITS][io & MASK];
            } else {
                return (E) prefix1[index];
            }
        }

        @Override
        final @NotNull ImmutableVector<E> slice0(int lo, int hi) {
            VectorSliceBuilder b = new VectorSliceBuilder(lo, hi);
            b.consider(1, prefix1);
            b.consider(2, prefix2);
            b.consider(3, data3);
            b.consider(2, suffix2);
            b.consider(1, suffix1);
            return b.build();
        }
    }

    static final class Vector4<E> extends BigVector<E> {
        final int len1;
        final Object[][] prefix2;
        final int len12;
        final Object[][][] prefix3;
        final int len123;
        final Object[][][][] data4;
        final Object[][][] suffix3;
        final Object[][] suffix2;

        Vector4(Object[] prefix1, int len1,
                Object[][] prefix2, int len12,
                Object[][][] prefix3, int len123,
                Object[][][][] data4,
                Object[][][] suffix3,
                Object[][] suffix2,
                Object[] suffix1,
                int length0) {
            super(prefix1, suffix1, length0);

            this.len1 = len1;
            this.prefix2 = prefix2;
            this.len12 = len12;
            this.prefix3 = prefix3;
            this.len123 = len123;
            this.data4 = data4;
            this.suffix3 = suffix3;
            this.suffix2 = suffix2;
        }

        @Override
        final int vectorSliceCount() {
            return 7;
        }

        @Override
        final Object[] vectorSlice(int idx) {
            switch (idx) {
                case 0:
                    return prefix1;
                case 1:
                    return prefix2;
                case 2:
                    return prefix3;
                case 3:
                    return data4;
                case 4:
                    return suffix3;
                case 5:
                    return suffix2;
                case 6:
                    return suffix1;
            }
            throw new AssertionError();
        }

        @Override
        final int vectorSlicePrefixLength(int idx) {
            switch (idx) {
                case 0:
                    return len1;
                case 1:
                    return len12;
                case 2:
                    return len123;
                case 3:
                    return len123 + data4.length * WIDTH3;
                case 4:
                    return len123 + data4.length * WIDTH3 + suffix3.length * WIDTH2;
                case 5:
                    return length0 - suffix1.length;
                case 6:
                    return length0;
            }
            throw new AssertionError();
        }

        @Override
        public final E get(int index) {
            checkElementIndex(index, length0);
            int io = index - len123;
            if (io >= 0) {
                int i4 = io >>> BITS3;
                int i3 = (io >>> BITS2) & MASK;
                int i2 = (io >>> BITS) & MASK;
                int i1 = io & MASK;
                if (i4 < data4.length) return (E) data4[i4][i3][i2][i1];
                else if (i3 < suffix3.length) {
                    return (E) suffix3[i3][i2][i1];
                } else if (i2 < suffix2.length) {
                    return (E) suffix2[i2][i1];
                } else {
                    return (E) suffix1[i1];
                }
            } else if (index >= len12) {
                io = index - len12;
                return (E) prefix3[io >>> BITS2][(io >>> BITS) & MASK][io & MASK];
            } else if (index >= len1) {
                io = index - len1;
                return (E) prefix2[io >>> BITS][io & MASK];
            } else {
                return (E) prefix1[index];
            }
        }

        @Override
        final @NotNull ImmutableVector<E> slice0(int lo, int hi) {
            VectorSliceBuilder b = new VectorSliceBuilder(lo, hi);
            b.consider(1, prefix1);
            b.consider(2, prefix2);
            b.consider(3, prefix3);
            b.consider(4, data4);
            b.consider(3, suffix3);
            b.consider(2, suffix2);
            b.consider(1, suffix1);
            return b.build();
        }
    }

    static final class Vector5<E> extends BigVector<E> {
        final int len1;
        final Object[][] prefix2;
        final int len12;
        final Object[][][] prefix3;
        final int len123;
        final Object[][][][] prefix4;
        final int len1234;
        final Object[][][][][] data5;
        final Object[][][][] suffix4;
        final Object[][][] suffix3;
        final Object[][] suffix2;

        Vector5(Object[] prefix1, int len1,
                Object[][] prefix2, int len12,
                Object[][][] prefix3, int len123,
                Object[][][][] prefix4, int len1234,
                Object[][][][][] data5,
                Object[][][][] suffix4,
                Object[][][] suffix3,
                Object[][] suffix2,
                Object[] suffix1,
                int length0) {
            super(prefix1, suffix1, length0);
            this.len1 = len1;
            this.prefix2 = prefix2;
            this.len12 = len12;
            this.prefix3 = prefix3;
            this.len123 = len123;
            this.prefix4 = prefix4;
            this.len1234 = len1234;
            this.data5 = data5;
            this.suffix4 = suffix4;
            this.suffix3 = suffix3;
            this.suffix2 = suffix2;
        }

        @Override
        final int vectorSliceCount() {
            return 9;
        }

        @Override
        final Object[] vectorSlice(int idx) {
            switch (idx) {
                case 0:
                    return prefix1;
                case 1:
                    return prefix2;
                case 2:
                    return prefix3;
                case 3:
                    return prefix4;
                case 4:
                    return data5;
                case 5:
                    return suffix4;
                case 6:
                    return suffix3;
                case 7:
                    return suffix2;
                case 8:
                    return suffix1;
            }
            throw new AssertionError();
        }

        @Override
        final int vectorSlicePrefixLength(int idx) {
            switch (idx) {
                case 0:
                    return len1;
                case 1:
                    return len12;
                case 2:
                    return len123;
                case 3:
                    return len1234;
                case 4:
                    return len1234 + data5.length * WIDTH4;
                case 5:
                    return len1234 + data5.length * WIDTH4 + suffix4.length * WIDTH3;
                case 6:
                    return len1234 + data5.length * WIDTH4 + suffix4.length * WIDTH3 + suffix3.length * WIDTH2;
                case 7:
                    return length0 - suffix1.length;
                case 8:
                    return length0;
            }
            throw new AssertionError();
        }

        @Override
        public final E get(int index) {
            checkElementIndex(index, length0);
            int io = index - len1234;
            if (io >= 0) {
                int i5 = io >>> BITS4;
                int i4 = (io >>> BITS3) & MASK;
                int i3 = (io >>> BITS2) & MASK;
                int i2 = (io >>> BITS) & MASK;
                int i1 = io & MASK;
                if (i5 < data5.length) {
                    return (E) data5[i5][i4][i3][i2][i1];
                } else if (i4 < suffix4.length) {
                    return (E) suffix4[i4][i3][i2][i1];
                } else if (i3 < suffix3.length) {
                    return (E) suffix3[i3][i2][i1];
                } else if (i2 < suffix2.length) {
                    return (E) suffix2[i2][i1];
                } else {
                    return (E) suffix1[i1];
                }
            } else if (index >= len123) {
                io = index - len123;
                return (E) prefix4[io >>> BITS3][(io >>> BITS2) & MASK][(io >>> BITS) & MASK][io & MASK];
            } else if (index >= len12) {
                io = index - len12;
                return (E) prefix3[io >>> BITS2][(io >>> BITS) & MASK][io & MASK];
            } else if (index >= len1) {
                io = index - len1;
                return (E) prefix2[io >>> BITS][io & MASK];
            } else {
                return (E) prefix1[index];
            }
        }

        @Override
        final @NotNull ImmutableVector<E> slice0(int lo, int hi) {
            VectorSliceBuilder b = new VectorSliceBuilder(lo, hi);
            b.consider(1, prefix1);
            b.consider(2, prefix2);
            b.consider(3, prefix3);
            b.consider(4, prefix4);
            b.consider(5, data5);
            b.consider(4, suffix4);
            b.consider(3, suffix3);
            b.consider(2, suffix2);
            b.consider(1, suffix1);
            return b.build();
        }
    }

    static final class Vector6<E> extends BigVector<E> {
        final int len1;
        final Object[][] prefix2;
        final int len12;
        final Object[][][] prefix3;
        final int len123;
        final Object[][][][] prefix4;
        final int len1234;
        final Object[][][][][] prefix5;
        final int len12345;
        final Object[][][][][][] data6;
        final Object[][][][][] suffix5;
        final Object[][][][] suffix4;
        final Object[][][] suffix3;
        final Object[][] suffix2;

        Vector6(Object[] prefix1, int len1,
                Object[][] prefix2, int len12,
                Object[][][] prefix3, int len123,
                Object[][][][] prefix4, int len1234,
                Object[][][][][] prefix5, int len12345,
                Object[][][][][][] data6,
                Object[][][][][] suffix5,
                Object[][][][] suffix4,
                Object[][][] suffix3,
                Object[][] suffix2,
                Object[] suffix1,
                int length0) {
            super(prefix1, suffix1, length0);
            this.len1 = len1;
            this.prefix2 = prefix2;
            this.len12 = len12;
            this.prefix3 = prefix3;
            this.len123 = len123;
            this.prefix4 = prefix4;
            this.len1234 = len1234;
            this.prefix5 = prefix5;
            this.len12345 = len12345;
            this.data6 = data6;
            this.suffix5 = suffix5;
            this.suffix4 = suffix4;
            this.suffix3 = suffix3;
            this.suffix2 = suffix2;
        }

        @Override
        final int vectorSliceCount() {
            return 11;
        }

        @Override
        final Object[] vectorSlice(int idx) {
            switch (idx) {
                case 0:
                    return prefix1;
                case 1:
                    return prefix2;
                case 2:
                    return prefix3;
                case 3:
                    return prefix4;
                case 4:
                    return prefix5;
                case 5:
                    return data6;
                case 6:
                    return suffix5;
                case 7:
                    return suffix4;
                case 8:
                    return suffix3;
                case 9:
                    return suffix2;
                case 10:
                    return suffix1;
            }
            throw new AssertionError();
        }

        @Override
        final int vectorSlicePrefixLength(int idx) {
            switch (idx) {
                case 0:
                    return len1;
                case 1:
                    return len12;
                case 2:
                    return len123;
                case 3:
                    return len1234;
                case 4:
                    return len12345;
                case 5:
                    return len12345 + data6.length * WIDTH5;
                case 6:
                    return len12345 + data6.length * WIDTH5 + suffix5.length * WIDTH4;
                case 7:
                    return len12345 + data6.length * WIDTH5 + suffix5.length * WIDTH4 + suffix4.length * WIDTH3;
                case 8:
                    return len12345 + data6.length * WIDTH5 + suffix5.length * WIDTH4 + suffix4.length * WIDTH3 + suffix3.length * WIDTH2;
                case 9:
                    return length0 - suffix1.length;
                case 10:
                    return length0;
            }
            throw new AssertionError();
        }

        @Override
        public final E get(int index) {
            checkElementIndex(index, length0);
            int io = index - len12345;
            if (io >= 0) {
                int i6 = io >>> BITS5;
                int i5 = (io >>> BITS4) & MASK;
                int i4 = (io >>> BITS3) & MASK;
                int i3 = (io >>> BITS2) & MASK;
                int i2 = (io >>> BITS) & MASK;
                int i1 = io & MASK;
                if (i6 < data6.length) {
                    return (E) data6[i6][i5][i4][i3][i2][i1];
                } else if (i5 < suffix5.length) {
                    return (E) suffix5[i5][i4][i3][i2][i1];
                } else if (i4 < suffix4.length) {
                    return (E) suffix4[i4][i3][i2][i1];
                } else if (i3 < suffix3.length) {
                    return (E) suffix3[i3][i2][i1];
                } else if (i2 < suffix2.length) {
                    return (E) suffix2[i2][i1];
                } else {
                    return (E) suffix1[i1];
                }
            } else if (index >= len1234) {
                io = index - len1234;
                return (E) prefix5[io >>> BITS4][(io >>> BITS3) & MASK][(io >>> BITS2) & MASK][(io >>> BITS) & MASK][io & MASK];
            } else if (index >= len123) {
                io = index - len123;
                return (E) prefix4[io >>> BITS3][(io >>> BITS2) & MASK][(io >>> BITS) & MASK][io & MASK];
            } else if (index >= len12) {
                io = index - len12;
                return (E) prefix3[io >>> BITS2][(io >>> BITS) & MASK][io & MASK];
            } else if (index >= len1) {
                io = index - len1;
                return (E) prefix2[io >>> BITS][io & MASK];
            } else {
                return (E) prefix1[index];
            }
        }

        @Override
        final @NotNull ImmutableVector<E> slice0(int lo, int hi) {
            VectorSliceBuilder b = new VectorSliceBuilder(lo, hi);
            b.consider(1, prefix1);
            b.consider(2, prefix2);
            b.consider(3, prefix3);
            b.consider(4, prefix4);
            b.consider(5, prefix5);
            b.consider(6, data6);
            b.consider(5, suffix5);
            b.consider(4, suffix4);
            b.consider(3, suffix3);
            b.consider(2, suffix2);
            b.consider(1, suffix1);
            return b.build();
        }
    }

    static final class VectorSliceBuilder {
        private final int lo;
        private final int hi;

        private final Object[][] slices = new Object[11][];
        private int len = 0;
        private int pos = 0;
        private int maxDim = 0;

        VectorSliceBuilder(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
        }

        private int prefixIdx(int n) {
            return n - 1;
        }

        private int suffixIdx(int n) {
            return 11 - n;
        }

        public final <T> void consider(int n, T[] a) {
            //println(s"*****   consider($n, /${a.length})")
            final int count = a.length * (1 << (BITS * (n - 1)));
            final int lo0 = Math.max(lo - pos, 0);
            final int hi0 = Math.min(hi - pos, count);
            if (hi0 > lo0) {
                addSlice(n, a, lo0, hi0);
                len += (hi0 - lo0);
            }
            pos += count;
        }

        private <T> void addSlice(int n, T[] a, int lo, int hi) {
            //println(s"*****     addSlice($n, /${a.length}, $lo, $hi)")
            if (n == 1) {
                add(1, copyOrUse(a, lo, hi));
            } else {
                int bitsN = BITS * (n - 1);
                int widthN = 1 << bitsN;
                int loN = lo >>> bitsN;
                int hiN = hi >>> bitsN;
                int loRest = lo & (widthN - 1);
                int hiRest = hi & (widthN - 1);
                //println(s"*****       bitsN=$bitsN, loN=$loN, hiN=$hiN, loRest=$loRest, hiRest=$hiRest")
                if (loRest == 0) {
                    if (hiRest == 0) {
                        add(n, copyOrUse(a, loN, hiN));
                    } else {
                        if (hiN > loN) {
                            add(n, copyOrUse(a, loN, hiN));
                        }
                        addSlice(n - 1, (Object[]) a[hiN], 0, hiRest);
                    }
                } else {
                    if (hiN == loN) {
                        addSlice(n - 1, (Object[]) a[loN], loRest, hiRest);
                    } else {
                        addSlice(n - 1, (Object[]) a[loN], loRest, widthN);
                        if (hiRest == 0) {
                            if (hiN > loN + 1) add(n, copyOrUse(a, loN + 1, hiN));
                        } else {
                            if (hiN > loN + 1) add(n, copyOrUse(a, loN + 1, hiN));
                            addSlice(n - 1, (Object[]) a[hiN], 0, hiRest);
                        }
                    }
                }
            }
        }

        private <T> void add(int n, T[] a) {
            int idx;
            if (n <= maxDim) {
                idx = suffixIdx(n);
            } else {
                maxDim = n;
                idx = prefixIdx(n);
            }
            slices[idx] = a;
        }

        public final <T> ImmutableVector<T> build() {
            //println(s"*****   result: $len, $maxDim")
            if (len <= 32) {
                if (len == 0) {
                    return ImmutableVector.empty();
                } else {
                    Object[] prefix1 = slices[prefixIdx(1)];
                    Object[] suffix1 = slices[suffixIdx(1)];
                    //println(s"*****     prefix1: ${if(prefix1 == null) "null" else prefix1.mkString("[", ",", "]")}, suffix1: ${if(suffix1 == null) "null" else suffix1.mkString("[", ",", "]")}")
                    Object[] a;
                    if (prefix1 != null) {
                        if (suffix1 != null) {
                            a = concatArrays(prefix1, suffix1);
                        } else {
                            a = prefix1;
                        }

                    } else if (suffix1 != null) {
                        a = suffix1;
                    } else {
                        Object[][] prefix2 = (Object[][]) slices[prefixIdx(2)];
                        if (prefix2 != null) {
                            a = prefix2[0];
                        } else {
                            Object[][] suffix2 = (Object[][]) slices[suffixIdx(2)];
                            a = suffix2[0];
                        }
                    }
                    return new Vector1(a);
                }
            } else {
                balancePrefix(1);
                balanceSuffix(1);
                int resultDim = maxDim;
                if (resultDim < 6) {
                    Object[] pre = slices[prefixIdx(maxDim)];
                    Object[] suf = slices[suffixIdx(maxDim)];
                    if ((pre != null) && (suf != null)) {
                        // The highest-dimensional data consists of two slices: concatenate if they fit into the main data array,
                        // otherwise increase the dimension
                        if (pre.length + suf.length <= WIDTH - 2) {
                            slices[prefixIdx(maxDim)] = concatArrays(pre, suf);
                            slices[suffixIdx(maxDim)] = null;
                        } else {
                            resultDim += 1;
                        }
                    } else {
                        // A single highest-dimensional slice could have length WIDTH-1 if it came from a prefix or suffix but we
                        // only allow WIDTH-2 for the main data, so increase the dimension in this case
                        Object[] one = (pre != null) ? pre : suf;
                        if (one.length > WIDTH - 2) {
                            resultDim += 1;
                        }
                    }
                }
                Object[] prefix1 = slices[prefixIdx(1)];
                Object[] suffix1 = slices[suffixIdx(1)];
                int len1 = prefix1.length;
                switch (resultDim) {
                    case 2: {
                        Object[][] data2 = dataOr(2, empty2);
                        return new Vector2<>(prefix1, len1, data2, suffix1, len);
                    }
                    case 3: {
                        Object[][] prefix2 = prefixOr(2, empty2);
                        Object[][][] data3 = dataOr(3, empty3);
                        Object[][] suffix2 = suffixOr(2, empty2);
                        int len12 = len1 + (prefix2.length * WIDTH);
                        return new Vector3<>(prefix1, len1, prefix2, len12, data3, suffix2, suffix1, len);
                    }
                    case 4: {
                        Object[][] prefix2 = prefixOr(2, empty2);
                        Object[][][] prefix3 = prefixOr(3, empty3);
                        Object[][][][] data4 = dataOr(4, empty4);
                        Object[][][] suffix3 = suffixOr(3, empty3);
                        Object[][] suffix2 = suffixOr(2, empty2);
                        int len12 = len1 + (prefix2.length * WIDTH);
                        int len123 = len12 + (prefix3.length * WIDTH2);
                        return new Vector4<>(prefix1, len1, prefix2, len12, prefix3, len123, data4, suffix3, suffix2, suffix1, len);
                    }
                    case 5: {
                        Object[][] prefix2 = prefixOr(2, empty2);
                        Object[][][] prefix3 = prefixOr(3, empty3);
                        Object[][][][] prefix4 = prefixOr(4, empty4);
                        Object[][][][][] data5 = dataOr(5, empty5);
                        Object[][][][] suffix4 = suffixOr(4, empty4);
                        Object[][][] suffix3 = suffixOr(3, empty3);
                        Object[][] suffix2 = suffixOr(2, empty2);
                        int len12 = len1 + (prefix2.length * WIDTH);
                        int len123 = len12 + (prefix3.length * WIDTH2);
                        int len1234 = len123 + (prefix4.length * WIDTH3);
                        return new Vector5<>(prefix1, len1, prefix2, len12, prefix3, len123, prefix4, len1234, data5, suffix4, suffix3, suffix2, suffix1, len);
                    }
                    case 6: {
                        Object[][] prefix2 = prefixOr(2, empty2);
                        Object[][][] prefix3 = prefixOr(3, empty3);
                        Object[][][][] prefix4 = prefixOr(4, empty4);
                        Object[][][][][] prefix5 = prefixOr(5, empty5);
                        Object[][][][][][] data6 = dataOr(6, empty6);
                        Object[][][][][] suffix5 = suffixOr(5, empty5);
                        Object[][][][] suffix4 = suffixOr(4, empty4);
                        Object[][][] suffix3 = suffixOr(3, empty3);
                        Object[][] suffix2 = suffixOr(2, empty2);
                        int len12 = len1 + (prefix2.length * WIDTH);
                        int len123 = len12 + (prefix3.length * WIDTH2);
                        int len1234 = len123 + (prefix4.length * WIDTH3);
                        int len12345 = len1234 + (prefix5.length * WIDTH4);
                        return new Vector6<>(prefix1, len1, prefix2, len12, prefix3, len123, prefix4, len1234, prefix5, len12345, data6, suffix5, suffix4, suffix3, suffix2, suffix1, len);
                    }
                    default:
                        throw new AssertionError();
                }
            }
        }

        private <T> T[] prefixOr(int n, T[] a) {
            Object[] p = slices[prefixIdx(n)];
            if (p != null) {
                return (T[]) p;
            } else {
                return a;
            }
        }

        private <T> T[] suffixOr(int n, T[] a) {
            Object[] s = slices[suffixIdx(n)];
            if (s != null) {
                return (T[]) s;
            } else {
                return a;
            }
        }

        private <T> T[] dataOr(int n, T[] a) {
            Object[] p = slices[prefixIdx(n)];
            if (p != null) return (T[]) p;
            else {
                Object[] s = slices[suffixIdx(n)];
                if (s != null) return (T[]) s;
                else return a;
            }
        }

        /**
         * Ensure prefix is not empty
         */
        private void balancePrefix(int n) {
            if (slices[prefixIdx(n)] == null) {
                if (n == maxDim) {
                    slices[prefixIdx(n)] = slices[suffixIdx(n)];
                    slices[suffixIdx(n)] = null;
                } else {
                    balancePrefix(n + 1);
                    Object[][] preN1 = (Object[][]) slices[prefixIdx(n + 1)];
                    //assert(preN1 ne null)
                    slices[prefixIdx(n)] = preN1[0];
                    if (preN1.length == 1) {
                        slices[prefixIdx(n + 1)] = null;
                        if ((maxDim == n + 1) && (slices[suffixIdx(n + 1)] == null)) {
                            maxDim = n;
                        }
                    } else {
                        slices[prefixIdx(n + 1)] = Arrays.copyOfRange(preN1, 1, preN1.length);
                    }
                }
            }
        }

        /**
         * Ensure suffix is not empty
         */
        private void balanceSuffix(int n) {
            if (slices[suffixIdx(n)] == null) {
                if (n == maxDim) {
                    slices[suffixIdx(n)] = slices[prefixIdx(n)];
                    slices[prefixIdx(n)] = null;
                } else {
                    balanceSuffix(n + 1);
                    Object[][] sufN1 = (Object[][]) slices[suffixIdx(n + 1)];
                    //assert(sufN1 ne null, s"n=$n, maxDim=$maxDim, slices=${slices.mkString(",")}")
                    slices[suffixIdx(n)] = sufN1[sufN1.length - 1];
                    if (sufN1.length == 1) {
                        slices[suffixIdx(n + 1)] = null;
                        if ((maxDim == n + 1) && (slices[prefixIdx(n + 1)] == null)) {
                            maxDim = n;
                        }
                    } else {
                        slices[suffixIdx(n + 1)] = Arrays.copyOfRange(sufN1, 0, sufN1.length - 1);
                    }
                }
            }
        }


    }

    static final class VectorBuilder<E> {
        private Object[][][][][][] a6;
        private Object[][][][][] a5;
        private Object[][][][] a4;
        private Object[][][] a3;
        private Object[][] a2;
        private Object[] a1 = new Object[WIDTH];

        private int len1 = 0;
        private int lenRest = 0;
        private int offset = 0;
        private int depth = 1;

        static <E> VectorBuilder<E> merge(VectorBuilder<E> builder1, VectorBuilder<E> builder2) {
            builder1.addVector(builder2.build());
            return builder1;
        }

        final int size() {
            return len1 + lenRest - offset;
        }

        private void setLen(int i) {
            this.len1 = i & MASK;
            this.lenRest = i - len1;
        }

        void initSparse(int size, E elem) {
            setLen(size);
            Arrays.fill(a1, elem);
            if (size > WIDTH) {
                a2 = new Object[WIDTH][];
                Arrays.fill(a2, a1);
                if (size > WIDTH2) {
                    a3 = new Object[WIDTH][][];
                    Arrays.fill(a3, a2);
                    if (size > WIDTH3) {
                        a4 = new Object[WIDTH][][][];
                        Arrays.fill(a4, a3);
                        if (size > WIDTH4) {
                            a5 = new Object[WIDTH][][][][];
                            Arrays.fill(a5, a4);
                            if (size > WIDTH5) {
                                a6 = new Object[LASTWIDTH][][][][][];
                                Arrays.fill(a6, a5);
                                depth = 6;
                            } else {
                                depth = 5;
                            }
                        } else {
                            depth = 4;
                        }
                    } else {
                        depth = 3;
                    }
                } else {
                    depth = 2;
                }
            } else {
                depth = 1;
            }
        }

        void initFrom(Object[] prefix1) {
            depth = 1;
            setLen(prefix1.length);
            a1 = copyOrUse(prefix1, 0, WIDTH);
            if (len1 == 0 && lenRest > 0) {
                // force advance() on next addition:
                len1 = WIDTH;
                lenRest -= WIDTH;
            }
        }

        void initFrom(ImmutableVector<?> v) {
            switch (v.vectorSliceCount()) { // implicit null check of v
                case 0:
                    break;
                case 1: {
                    Vector1<?> v1 = (Vector1<?>) v;
                    depth = 1;
                    setLen(v1.prefix1.length);
                    a1 = copyOrUse(v1.prefix1, 0, WIDTH);
                    break;
                }
                case 3: {
                    Vector2<?> v2 = (Vector2<?>) v;
                    Object[][] d2 = v2.data2;
                    a1 = copyOrUse(v2.suffix1, 0, WIDTH);
                    depth = 2;
                    offset = WIDTH - v2.len1;
                    setLen(v2.length0 + offset);
                    a2 = new Object[WIDTH][];
                    a2[0] = v2.prefix1;
                    System.arraycopy(d2, 0, a2, 1, d2.length);
                    a2[d2.length + 1] = a1;
                    break;
                }
                case 5: {
                    Vector3<?> v3 = (Vector3<?>) v;
                    Object[][][] d3 = v3.data3;
                    Object[][] s2 = v3.suffix2;
                    a1 = copyOrUse(v3.suffix1, 0, WIDTH);
                    depth = 3;
                    offset = WIDTH2 - v3.len12;
                    setLen(v3.length0 + offset);
                    a3 = new Object[WIDTH][][];
                    a3[0] = copyPrepend(v3.prefix1, v3.prefix2);
                    System.arraycopy(d3, 0, a3, 1, d3.length);
                    a2 = copyOf(s2, WIDTH);
                    a3[d3.length + 1] = a2;
                    a2[s2.length] = a1;
                    break;
                }
                case 7: {
                    Vector4<?> v4 = (Vector4<?>) v;
                    Object[][][][] d4 = v4.data4;
                    Object[][][] s3 = v4.suffix3;
                    Object[][] s2 = v4.suffix2;
                    a1 = copyOrUse(v4.suffix1, 0, WIDTH);
                    depth = 4;
                    offset = WIDTH3 - v4.len123;
                    setLen(v4.length0 + offset);
                    a4 = new Object[WIDTH][][][];
                    a4[0] = copyPrepend(copyPrepend(v4.prefix1, v4.prefix2), v4.prefix3);
                    System.arraycopy(d4, 0, a4, 1, d4.length);
                    a3 = copyOf(s3, WIDTH);
                    a2 = copyOf(s2, WIDTH);
                    a4[d4.length + 1] = a3;
                    a3[s3.length] = a2;
                    a2[s2.length] = a1;
                    break;
                }
                case 9: {
                    Vector5<?> v5 = (Vector5<?>) v;
                    Object[][][][][] d5 = v5.data5;
                    Object[][][][] s4 = v5.suffix4;
                    Object[][][] s3 = v5.suffix3;
                    Object[][] s2 = v5.suffix2;
                    a1 = copyOrUse(v5.suffix1, 0, WIDTH);
                    depth = 5;
                    offset = WIDTH4 - v5.len1234;
                    setLen(v5.length0 + offset);
                    a5 = new Object[WIDTH][][][][];
                    a5[0] = copyPrepend(copyPrepend(copyPrepend(v5.prefix1, v5.prefix2), v5.prefix3), v5.prefix4);
                    System.arraycopy(d5, 0, a5, 1, d5.length);
                    a4 = copyOf(s4, WIDTH);
                    a3 = copyOf(s3, WIDTH);
                    a2 = copyOf(s2, WIDTH);
                    a5[d5.length + 1] = a4;
                    a4[s4.length] = a3;
                    a3[s3.length] = a2;
                    a2[s2.length] = a1;
                    break;
                }
                case 11: {
                    Vector6<?> v6 = (Vector6<?>) v;
                    Object[][][][][][] d6 = v6.data6;
                    Object[][][][][] s5 = v6.suffix5;
                    Object[][][][] s4 = v6.suffix4;
                    Object[][][] s3 = v6.suffix3;
                    Object[][] s2 = v6.suffix2;
                    a1 = copyOrUse(v6.suffix1, 0, WIDTH);
                    depth = 6;
                    offset = WIDTH5 - v6.len12345;
                    setLen(v6.length0 + offset);
                    a6 = new Object[WIDTH][][][][][];
                    a6[0] = copyPrepend(copyPrepend(copyPrepend(copyPrepend(v6.prefix1, v6.prefix2), v6.prefix3), v6.prefix4), v6.prefix5);
                    System.arraycopy(d6, 0, a6, 1, d6.length);
                    a5 = copyOf(s5, WIDTH);
                    a4 = copyOf(s4, WIDTH);
                    a3 = copyOf(s3, WIDTH);
                    a2 = copyOf(s2, WIDTH);
                    a6[d6.length + 1] = a5;
                    a5[s5.length] = a4;
                    a4[s4.length] = a3;
                    a3[s3.length] = a2;
                    a2[s2.length] = a1;
                    break;
                }
                default:
                    throw new AssertionError();
            }
            if (len1 == 0 && lenRest > 0) {
                // force advance() on next addition:
                len1 = WIDTH;
                lenRest -= WIDTH;
            }
        }

        void add(E elem) {
            if (len1 == WIDTH) {
                advance();
            }
            a1[len1++] = elem;
        }

        private void addArr1(Object[] data) {
            final int dl = data.length;
            if (dl > 0) {
                if (len1 == WIDTH) {
                    advance();
                }
                int copy1 = Math.min(WIDTH - len1, dl);
                int copy2 = dl - copy1;
                System.arraycopy(data, 0, a1, len1, copy1);
                len1 += copy1;
                if (copy2 > 0) {
                    advance();
                    System.arraycopy(data, copy1, a1, 0, copy2);
                    len1 += copy2;
                }
            }
        }

        void addVector(ImmutableVector<? extends E> xs) {
            final int sliceCount = xs.vectorSliceCount(); // implicit null check of xs
            int sliceIdx = 0;
            while (sliceIdx < sliceCount) {
                Object[] slice = xs.vectorSlice(sliceIdx);

                int dim = vectorSliceDim(sliceCount, sliceIdx);
                if (dim == 1) {
                    addArr1(slice);
                } else {
                    forEachRec(dim - 2, slice, (Consumer<Object[]>) this::addArr1);
                }
                ++sliceIdx;
            }
        }

        void addAll(Object[] xs) {
            for (Object e : xs) {
                add((E) e);
            }
        }

        void addAll(Iterable<? extends E> xs) {
            for (E e : xs) {
                add(e);
            }
        }

        void addAll(Traversable<? extends E> xs) {
            if (xs instanceof ImmutableVector<?>) {
                ImmutableVector<E> v = (ImmutableVector<E>) xs;
                if (len1 == 0 && lenRest == 0) {
                    initFrom(v);
                } else {
                    addVector(v);
                }
            }
            for (E e : xs) {
                add(e);
            }
        }

        private void advance() {
            int idx = lenRest + WIDTH;
            int xor = idx ^ lenRest;
            lenRest = idx;
            len1 = 0;
            advance1(idx, xor);
        }

        private void advance1(int idx, int xor) {
            if (xor < WIDTH2) { // level = 1
                if (depth == 1) {
                    a2 = new Object[WIDTH][];
                    a2[0] = a1;
                    ++depth;
                }
                a1 = new Object[WIDTH];
                a2[(idx >>> BITS) & MASK] = a1;
            } else if (xor < WIDTH3) { // level = 2
                if (depth == 2) {
                    a3 = new Object[WIDTH][][];
                    a3[0] = a2;
                    ++depth;
                }
                a1 = new Object[WIDTH];
                a2 = new Object[WIDTH][];
                a2[(idx >>> BITS) & MASK] = a1;
                a3[(idx >>> BITS2) & MASK] = a2;
            } else if (xor < WIDTH4) { // level = 3
                if (depth == 3) {
                    a4 = new Object[WIDTH][][][];
                    a4[0] = a3;
                    ++depth;
                }
                a1 = new Object[WIDTH];
                a2 = new Object[WIDTH][];
                a3 = new Object[WIDTH][][];
                a2[(idx >>> BITS) & MASK] = a1;
                a3[(idx >>> BITS2) & MASK] = a2;
                a4[(idx >>> BITS3) & MASK] = a3;
            } else if (xor < WIDTH5) { // level = 4
                if (depth == 4) {
                    a5 = new Object[WIDTH][][][][];
                    a5[0] = a4;
                    depth += 1;
                }
                a1 = new Object[WIDTH];
                a2 = new Object[WIDTH][];
                a3 = new Object[WIDTH][][];
                a4 = new Object[WIDTH][][][];
                a2[(idx >>> BITS) & MASK] = a1;
                a3[(idx >>> BITS2) & MASK] = a2;
                a4[(idx >>> BITS3) & MASK] = a3;
                a5[(idx >>> BITS4) & MASK] = a4;
            } else if (xor < WIDTH6) { // level = 5
                if (depth == 5) {
                    a6 = new Object[LASTWIDTH][][][][][];
                    a6[0] = a5;
                    depth += 1;
                }
                a1 = new Object[WIDTH];
                a2 = new Object[WIDTH][];
                a3 = new Object[WIDTH][][];
                a4 = new Object[WIDTH][][][];
                a5 = new Object[WIDTH][][][][];
                a2[(idx >>> BITS) & MASK] = a1;
                a3[(idx >>> BITS2) & MASK] = a2;
                a4[(idx >>> BITS3) & MASK] = a3;
                a5[(idx >>> BITS4) & MASK] = a4;
                a6[(idx >>> BITS5) & MASK] = a5;
            } else {                      // level = 6
                throw new IllegalArgumentException(
                        String.format("advance1(%d, %d): a1=%s, a2=%s, a3=%s, a4=%s, a5=%s, a6=%s, depth=%d",
                                idx, xor,
                                Arrays.toString(a1), Arrays.deepToString(a2), Arrays.deepToString(a3),
                                Arrays.deepToString(a4), Arrays.deepToString(a5), Arrays.deepToString(a6),
                                depth
                        ));
            }
        }

        final ImmutableVector<E> build() {
            final int len = len1 + lenRest;
            final int realLen = len - offset;
            if (realLen == 0) {
                return ImmutableVector.empty();
            } else if (len <= WIDTH) {
                if (realLen == WIDTH) {
                    return new Vector1(a1);
                } else {
                    return new Vector1(copyOf(a1, realLen));
                }
            } else if (len <= WIDTH2) {
                int i1 = (len - 1) & MASK;
                int i2 = (len - 1) >>> BITS;
                Object[][] data = Arrays.copyOfRange(a2, 1, i2);
                Object[] prefix1 = a2[0];
                Object[] suffix1 = copyIfDifferentSize(a2[i2], i1 + 1);
                return new Vector2(prefix1, WIDTH - offset, data, suffix1, realLen);
            } else if (len <= WIDTH3) {
                int i1 = (len - 1) & MASK;
                int i2 = ((len - 1) >>> BITS) & MASK;
                int i3 = ((len - 1) >>> BITS2);
                Object[][][] data = Arrays.copyOfRange(a3, 1, i3);
                Object[][] prefix2 = copyTail(a3[0]);
                Object[] prefix1 = a3[0][0];
                Object[][] suffix2 = copyOf(a3[i3], i2);
                Object[] suffix1 = copyIfDifferentSize(a3[i3][i2], i1 + 1);
                int len1 = prefix1.length;
                int len12 = len1 + prefix2.length * WIDTH;
                return new Vector3(prefix1, len1, prefix2, len12, data, suffix2, suffix1, realLen);
            } else if (len <= WIDTH4) {
                int i1 = (len - 1) & MASK;
                int i2 = ((len - 1) >>> BITS) & MASK;
                int i3 = ((len - 1) >>> BITS2) & MASK;
                int i4 = ((len - 1) >>> BITS3);
                Object[][][][] data = Arrays.copyOfRange(a4, 1, i4);
                Object[][][] prefix3 = copyTail(a4[0]);
                Object[][] prefix2 = copyTail(a4[0][0]);
                Object[] prefix1 = a4[0][0][0];
                Object[][][] suffix3 = copyOf(a4[i4], i3);
                Object[][] suffix2 = copyOf(a4[i4][i3], i2);
                Object[] suffix1 = copyIfDifferentSize(a4[i4][i3][i2], i1 + 1);
                int len1 = prefix1.length;
                int len12 = len1 + prefix2.length * WIDTH;
                int len123 = len12 + prefix3.length * WIDTH2;
                return new Vector4(prefix1, len1, prefix2, len12, prefix3, len123, data, suffix3, suffix2, suffix1, realLen);
            } else if (len <= WIDTH5) {
                int i1 = (len - 1) & MASK;
                int i2 = ((len - 1) >>> BITS) & MASK;
                int i3 = ((len - 1) >>> BITS2) & MASK;
                int i4 = ((len - 1) >>> BITS3) & MASK;
                int i5 = ((len - 1) >>> BITS4);
                Object[][][][][] data = Arrays.copyOfRange(a5, 1, i5);
                Object[][][][] prefix4 = copyTail(a5[0]);
                Object[][][] prefix3 = copyTail(a5[0][0]);
                Object[][] prefix2 = copyTail(a5[0][0][0]);
                Object[] prefix1 = a5[0][0][0][0];
                Object[][][][] suffix4 = copyOf(a5[i5], i4);
                Object[][][] suffix3 = copyOf(a5[i5][i4], i3);
                Object[][] suffix2 = copyOf(a5[i5][i4][i3], i2);
                Object[] suffix1 = copyIfDifferentSize(a5[i5][i4][i3][i2], i1 + 1);
                int len1 = prefix1.length;
                int len12 = len1 + prefix2.length * WIDTH;
                int len123 = len12 + prefix3.length * WIDTH2;
                int len1234 = len123 + prefix4.length * WIDTH3;
                return new Vector5(prefix1, len1, prefix2, len12, prefix3, len123, prefix4, len1234, data, suffix4, suffix3, suffix2, suffix1, realLen);
            } else {
                int i1 = (len - 1) & MASK;
                int i2 = ((len - 1) >>> BITS) & MASK;
                int i3 = ((len - 1) >>> BITS2) & MASK;
                int i4 = ((len - 1) >>> BITS3) & MASK;
                int i5 = ((len - 1) >>> BITS4) & MASK;
                int i6 = ((len - 1) >>> BITS5);
                Object[][][][][][] data = Arrays.copyOfRange(a6, 1, i6);
                Object[][][][][] prefix5 = copyTail(a6[0]);
                Object[][][][] prefix4 = copyTail(a6[0][0]);
                Object[][][] prefix3 = copyTail(a6[0][0][0]);
                Object[][] prefix2 = copyTail(a6[0][0][0][0]);
                Object[] prefix1 = a6[0][0][0][0][0];
                Object[][][][][] suffix5 = copyOf(a6[i6], i5);
                Object[][][][] suffix4 = copyOf(a6[i6][i5], i4);
                Object[][][] suffix3 = copyOf(a6[i6][i5][i4], i3);
                Object[][] suffix2 = copyOf(a6[i6][i5][i4][i3], i2);
                Object[] suffix1 = copyIfDifferentSize(a6[i6][i5][i4][i3][i2], i1 + 1);
                int len1 = prefix1.length;
                int len12 = len1 + prefix2.length * WIDTH;
                int len123 = len12 + prefix3.length * WIDTH2;
                int len1234 = len123 + prefix4.length * WIDTH3;
                int len12345 = len1234 + prefix5.length * WIDTH4;
                return new Vector6(prefix1, len1, prefix2, len12, prefix3, len123, prefix4, len1234, prefix5, len12345, data, suffix5, suffix4, suffix3, suffix2, suffix1, realLen);
            }
        }

        final ImmutableSeq<E> buildSeq() {
            final int len = len1 + lenRest;
            final int realLen = len - offset;
            if (realLen == 0) {
                return ImmutableSeq.empty();
            } else if (len <= WIDTH) {
                E[] a1 = (E[]) this.a1;
                switch (realLen) {
                    case 1:
                        return ImmutableSeq.of(a1[0]);
                    case 2:
                        return ImmutableSeq.of(a1[0], a1[1]);
                    case 3:
                        return ImmutableSeq.of(a1[0], a1[1], a1[2]);
                    case 4:
                        return ImmutableSeq.of(a1[0], a1[1], a1[2], a1[3]);
                    case 5:
                        return ImmutableSeq.of(a1[0], a1[1], a1[2], a1[3], a1[4]);
                }
                if (realLen == WIDTH) {
                    return new Vector1(a1);
                } else {
                    return new Vector1(copyOf(a1, realLen));
                }
            } else if (len <= WIDTH2) {
                int i1 = (len - 1) & MASK;
                int i2 = (len - 1) >>> BITS;
                Object[][] data = Arrays.copyOfRange(a2, 1, i2);
                Object[] prefix1 = a2[0];
                Object[] suffix1 = copyIfDifferentSize(a2[i2], i1 + 1);
                return new Vector2(prefix1, WIDTH - offset, data, suffix1, realLen);
            } else if (len <= WIDTH3) {
                int i1 = (len - 1) & MASK;
                int i2 = ((len - 1) >>> BITS) & MASK;
                int i3 = ((len - 1) >>> BITS2);
                Object[][][] data = Arrays.copyOfRange(a3, 1, i3);
                Object[][] prefix2 = copyTail(a3[0]);
                Object[] prefix1 = a3[0][0];
                Object[][] suffix2 = copyOf(a3[i3], i2);
                Object[] suffix1 = copyIfDifferentSize(a3[i3][i2], i1 + 1);
                int len1 = prefix1.length;
                int len12 = len1 + prefix2.length * WIDTH;
                return new Vector3(prefix1, len1, prefix2, len12, data, suffix2, suffix1, realLen);
            } else if (len <= WIDTH4) {
                int i1 = (len - 1) & MASK;
                int i2 = ((len - 1) >>> BITS) & MASK;
                int i3 = ((len - 1) >>> BITS2) & MASK;
                int i4 = ((len - 1) >>> BITS3);
                Object[][][][] data = Arrays.copyOfRange(a4, 1, i4);
                Object[][][] prefix3 = copyTail(a4[0]);
                Object[][] prefix2 = copyTail(a4[0][0]);
                Object[] prefix1 = a4[0][0][0];
                Object[][][] suffix3 = copyOf(a4[i4], i3);
                Object[][] suffix2 = copyOf(a4[i4][i3], i2);
                Object[] suffix1 = copyIfDifferentSize(a4[i4][i3][i2], i1 + 1);
                int len1 = prefix1.length;
                int len12 = len1 + prefix2.length * WIDTH;
                int len123 = len12 + prefix3.length * WIDTH2;
                return new Vector4(prefix1, len1, prefix2, len12, prefix3, len123, data, suffix3, suffix2, suffix1, realLen);
            } else if (len <= WIDTH5) {
                int i1 = (len - 1) & MASK;
                int i2 = ((len - 1) >>> BITS) & MASK;
                int i3 = ((len - 1) >>> BITS2) & MASK;
                int i4 = ((len - 1) >>> BITS3) & MASK;
                int i5 = ((len - 1) >>> BITS4);
                Object[][][][][] data = Arrays.copyOfRange(a5, 1, i5);
                Object[][][][] prefix4 = copyTail(a5[0]);
                Object[][][] prefix3 = copyTail(a5[0][0]);
                Object[][] prefix2 = copyTail(a5[0][0][0]);
                Object[] prefix1 = a5[0][0][0][0];
                Object[][][][] suffix4 = copyOf(a5[i5], i4);
                Object[][][] suffix3 = copyOf(a5[i5][i4], i3);
                Object[][] suffix2 = copyOf(a5[i5][i4][i3], i2);
                Object[] suffix1 = copyIfDifferentSize(a5[i5][i4][i3][i2], i1 + 1);
                int len1 = prefix1.length;
                int len12 = len1 + prefix2.length * WIDTH;
                int len123 = len12 + prefix3.length * WIDTH2;
                int len1234 = len123 + prefix4.length * WIDTH3;
                return new Vector5(prefix1, len1, prefix2, len12, prefix3, len123, prefix4, len1234, data, suffix4, suffix3, suffix2, suffix1, realLen);
            } else {
                int i1 = (len - 1) & MASK;
                int i2 = ((len - 1) >>> BITS) & MASK;
                int i3 = ((len - 1) >>> BITS2) & MASK;
                int i4 = ((len - 1) >>> BITS3) & MASK;
                int i5 = ((len - 1) >>> BITS4) & MASK;
                int i6 = ((len - 1) >>> BITS5);
                Object[][][][][][] data = Arrays.copyOfRange(a6, 1, i6);
                Object[][][][][] prefix5 = copyTail(a6[0]);
                Object[][][][] prefix4 = copyTail(a6[0][0]);
                Object[][][] prefix3 = copyTail(a6[0][0][0]);
                Object[][] prefix2 = copyTail(a6[0][0][0][0]);
                Object[] prefix1 = a6[0][0][0][0][0];
                Object[][][][][] suffix5 = copyOf(a6[i6], i5);
                Object[][][][] suffix4 = copyOf(a6[i6][i5], i4);
                Object[][][] suffix3 = copyOf(a6[i6][i5][i4], i3);
                Object[][] suffix2 = copyOf(a6[i6][i5][i4][i3], i2);
                Object[] suffix1 = copyIfDifferentSize(a6[i6][i5][i4][i3][i2], i1 + 1);
                int len1 = prefix1.length;
                int len12 = len1 + prefix2.length * WIDTH;
                int len123 = len12 + prefix3.length * WIDTH2;
                int len1234 = len123 + prefix4.length * WIDTH3;
                int len12345 = len1234 + prefix5.length * WIDTH4;
                return new Vector6(prefix1, len1, prefix2, len12, prefix3, len123, prefix4, len1234, prefix5, len12345, data, suffix5, suffix4, suffix3, suffix2, suffix1, realLen);
            }

        }

        @Override
        public String toString() {
            return String.format(
                    "ImmutableVector.Builder[len1=%d, lenRest=%d, offset=%d, depth=%d]",
                    len1, lenRest, offset, depth
            );
        }
    }

    static final class Itr<E> extends AbstractIterator<E> implements Spliterator<E>, Cloneable {
        private final BigVector<? extends E> v;

        private int totalLength;
        private final int sliceCount;

        private Object[] a1;
        private Object[][] a2;
        private Object[][][] a3;
        private Object[][][][] a4;
        private Object[][][][][] a5;
        private Object[][][][][][] a6;
        private int a1len;
        private int i1 = 0;// current index in a1
        private int oldPos = 0;
        private int len1; // remaining length relative to a1

        private int sliceIdx = 0;
        private int sliceDim = 1;
        private int sliceStart = 0; // absolute position
        private int sliceEnd; // absolute position


        Itr(BigVector<? extends E> v, int totalLength, int sliceCount) {
            this.v = v;
            this.totalLength = totalLength;
            this.sliceCount = sliceCount;

            a1 = v.prefix1;
            a1len = a1.length;
            len1 = totalLength;
            sliceEnd = a1len;
        }

        private void advanceSlice() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            sliceIdx += 1;
            Object[] slice = v.vectorSlice(sliceIdx);
            while (slice.length == 0) {
                sliceIdx += 1;
                slice = v.vectorSlice(sliceIdx);
            }
            sliceStart = sliceEnd;
            sliceDim = vectorSliceDim(sliceCount, sliceIdx);
            switch (sliceDim) {
                case 1:
                    a1 = slice;
                    break;
                case 2:
                    a2 = (Object[][]) slice;
                    break;
                case 3:
                    a3 = (Object[][][]) slice;
                    break;
                case 4:
                    a4 = (Object[][][][]) slice;
                    break;
                case 5:
                    a5 = (Object[][][][][]) slice;
                    break;
                case 6:
                    a6 = (Object[][][][][][]) slice;
                    break;
                default:
                    throw new AssertionError();
            }
            sliceEnd = sliceStart + slice.length * (1 << (BITS * (sliceDim - 1)));
            if (sliceEnd > totalLength) {
                sliceEnd = totalLength;
            }
            if (sliceDim > 1) {
                oldPos = (1 << (BITS * sliceDim)) - 1;
            }
        }

        private void advance() {
            int pos = i1 - len1 + totalLength;
            if (pos == sliceEnd) {
                advanceSlice();
            }
            if (sliceDim > 1) {
                int io = pos - sliceStart;
                int xor = oldPos ^ io;
                advanceA(io, xor);
                oldPos = io;
            }
            len1 -= i1;
            a1len = Math.min(a1.length, len1);
            i1 = 0;
        }

        private void advanceA(int io, int xor) {
            if (xor < WIDTH2) {
                a1 = a2[(io >>> BITS) & MASK];
            } else if (xor < WIDTH3) {
                a2 = a3[(io >>> BITS2) & MASK];
                a1 = a2[0];
            } else if (xor < WIDTH4) {
                a3 = a4[(io >>> BITS3) & MASK];
                a2 = a3[0];
                a1 = a2[0];
            } else if (xor < WIDTH5) {
                a4 = a5[(io >>> BITS4) & MASK];
                a3 = a4[0];
                a2 = a3[0];
                a1 = a2[0];
            } else {
                a5 = a6[io >>> BITS5];
                a4 = a5[0];
                a3 = a4[0];
                a2 = a3[0];
                a1 = a2[0];
            }
        }

        @Override
        public final boolean hasNext() {
            return len1 > i1;
        }

        @Override
        public final E next() {
            if (i1 == a1len) {
                advance();
            }
            return (E) a1[i1++];
        }

        @Override
        public final void forEachRemaining(Consumer<? super E> action) {
            while (hasNext()) {
                action.accept(next());
            }
        }

        @Override
        public final boolean tryAdvance(Consumer<? super E> action) {
            if (hasNext()) {
                action.accept(next());
                return true;
            } else {
                return false;
            }
        }

        private ImmutableVectors.Itr<E> split(int at) {
            Itr<E> it2 = this.clone();
            it2.take(at);
            drop(at);
            return it2;
        }

        @Override
        public final Spliterator<E> trySplit() {
            int len = knownSize();
            if (len > 1) {
                return split(len >>> 1);
            }
            return null;
        }

        final int knownSize() {
            return len1 - i1;
        }

        @Override
        public final long estimateSize() {
            return knownSize();
        }

        @Override
        public final int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }

        private void setA(int io, int xor) {
            if (xor < WIDTH2) {
                a1 = a2[(io >>> BITS) & MASK];
            } else if (xor < WIDTH3) {
                a2 = a3[(io >>> BITS2) & MASK];
                a1 = a2[(io >>> BITS) & MASK];
            } else if (xor < WIDTH4) {
                a3 = a4[(io >>> BITS3) & MASK];
                a2 = a3[(io >>> BITS2) & MASK];
                a1 = a2[(io >>> BITS) & MASK];
            } else if (xor < WIDTH5) {
                a4 = a5[(io >>> BITS4) & MASK];
                a3 = a4[(io >>> BITS3) & MASK];
                a2 = a3[(io >>> BITS2) & MASK];
                a1 = a2[(io >>> BITS) & MASK];
            } else {
                a5 = a6[io >>> BITS5];
                a4 = a5[(io >>> BITS4) & MASK];
                a3 = a4[(io >>> BITS3) & MASK];
                a2 = a3[(io >>> BITS2) & MASK];
                a1 = a2[(io >>> BITS) & MASK];
            }
        }

        private void drop(int n) {
            if (n > 0) {
                int oldpos = i1 - len1 + totalLength;
                int newpos = Math.min(oldpos + n, totalLength);
                if (newpos == totalLength) {
                    i1 = 0;
                    len1 = 0;
                    a1len = 0;
                } else {
                    while (newpos >= sliceEnd) advanceSlice();
                    int io = newpos - sliceStart;
                    if (sliceDim > 1) {
                        int xor = oldPos ^ io;
                        setA(io, xor);
                        oldPos = io;
                    }
                    a1len = a1.length;
                    i1 = io & MASK;
                    len1 = i1 + (totalLength - newpos);
                    if (a1len > len1) {
                        a1len = len1;
                    }
                }
            }
        }

        private void take(int n) {
            final int knownSize = knownSize();
            if (n < knownSize) {
                int trunc = knownSize - Math.max(0, n);
                totalLength -= trunc;
                len1 -= trunc;
                if (len1 < a1len) {
                    a1len = len1;
                }
                if (totalLength < sliceEnd) {
                    sliceEnd = totalLength;
                }
            }
        }

        @Override
        protected final ImmutableVectors.Itr<E> clone() {
            try {
                return (Itr<E>) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }
}
