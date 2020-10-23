package asia.kala.iterator;

import asia.kala.traversable.JavaArray;
import asia.kala.Tuple;
import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import asia.kala.annotations.StaticClass;
import asia.kala.control.Option;
import asia.kala.factory.CollectionFactory;
import asia.kala.function.IndexedConsumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.*;
import java.util.function.Consumer;
import java.util.stream.Collector;

@StaticClass
@SuppressWarnings("unchecked")
public final class Iterators {
    private Iterators() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <E> Iterator<E> narrow(Iterator<? extends E> iterator) {
        return (Iterator<E>) iterator;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Iterator<E> empty() {
        return ((Iterator<E>) EMPTY);
    }

    @NotNull
    public static <E> Iterator<E> of() {
        return empty();
    }

    @NotNull
    public static <E> Iterator<E> of(E value) {
        return new AbstractIterator<E>() {
            private boolean hasNext = true;

            @Override
            public final boolean hasNext() {
                return hasNext;
            }

            @Override
            public final E next() {
                if (!hasNext) {
                    throw new NoSuchElementException();
                }
                hasNext = false;
                return value;
            }
        };
    }

    @NotNull
    @SafeVarargs
    public static <E> Iterator<E> of(E... values) {

        return JavaArray.iterator(values);
    }

    public static <E> Iterator<E> ofEnumeration(@NotNull java.util.Enumeration<? extends E> enumeration) {
        if (!enumeration.hasMoreElements()) {
            return Iterators.empty();
        }

        if (enumeration instanceof Iterator<?>) {
            return (Iterator<E>) enumeration;
        }

        return new AbstractIterator<E>() {
            @Override
            public final boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public final E next() {
                return enumeration.nextElement();
            }
        };
    }

    @NotNull
    public static <E> Iterator<E> from(@NotNull Iterable<? extends E> values) {
        return narrow(values.iterator());
    }

    @NotNull
    public static <E> Iterator<E> from(E @NotNull [] values) {
        return JavaArray.iterator(values);
    }

    @NotNull
    public static <E> Iterator<E> concat(@NotNull Iterator<? extends E>... its) {
        return new Concat<>(JavaArray.iterator(its));
    }

    @NotNull
    public static <E> Iterator<E> concat(@NotNull Iterable<? extends Iterator<? extends E>> its) {
        return new Concat<>(its.iterator());
    }

    @NotNull
    public static <E> Iterator<E> concat(@NotNull Iterator<? extends Iterator<? extends E>> its) {
        Objects.requireNonNull(its);
        return new Concat<>(its);
    }

    @Contract(mutates = "param1")
    public static int hash(@NotNull Iterator<?> it) {
        int ans = 0;
        while (it.hasNext()) {
            ans = ans * 31 + Objects.hashCode(it.next());
        }
        return ans;
    }

    public static int size(@NotNull Iterator<?> it) {
        int i = 0;
        while (it.hasNext()) {
            it.next();
            ++i;
        }
        return i;
    }

    public static boolean contains(Iterator<?> it, Object value) {
        if (value == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (value.equals(it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean sameElements(@NotNull Iterator<?> it1, @NotNull Iterator<?> it2) {
        while (it1.hasNext() && it2.hasNext()) {
            if (!Objects.equals(it1.next(), it2.next())) {
                return false;
            }
        }
        return it1.hasNext() == it2.hasNext();
    }

    public static boolean sameElements(@NotNull Iterator<?> it1, @NotNull Iterator<?> it2, boolean identity) {
        if (!identity) {
            return sameElements(it1, it2);
        }

        while (it1.hasNext() && it2.hasNext()) {
            if (it1.next() != it2.next()) {
                return false;
            }
        }
        return it1.hasNext() == it2.hasNext();
    }

    public static <E> int count(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        int c = 0;
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                ++c;
            }
        }
        return c;
    }

    public static <E> boolean anyMatch(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static <E> boolean allMatch(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        while (it.hasNext()) {
            if (!predicate.test(it.next())) {
                return false;
            }
        }
        return true;
    }

    public static <E> boolean noneMatch(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                return false;
            }
        }
        return true;
    }

    public static <E extends Comparable<E>> E max(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) > 0) {
                e = v;
            }
        }
        return e;
    }

    public static <E> E max(@NotNull Iterator<? extends E> it, @NotNull Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (comparator.compare(v, e) > 0) {
                e = v;
            }
        }
        return e;
    }

    @Nullable
    public static <E extends Comparable<E>> E maxOrNull(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return null;
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) > 0) {
                e = v;
            }
        }
        return e;
    }

    @Nullable
    public static <E> E maxOrNull(@NotNull Iterator<? extends E> it, @NotNull Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            return null;
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (comparator.compare(v, e) > 0) {
                e = v;
            }
        }
        return e;
    }

    public static <E extends Comparable<E>> Option<E> maxOption(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) > 0) {
                e = v;
            }
        }
        return Option.some(e);
    }

    public static <E> Option<E> maxOption(@NotNull Iterator<? extends E> it, @NotNull Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (comparator.compare(v, e) > 0) {
                e = v;
            }
        }
        return Option.some(e);
    }

    public static <E extends Comparable<E>> E min(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) < 0) {
                e = v;
            }
        }
        return e;
    }

    public static <E> E min(@NotNull Iterator<? extends E> it, @NotNull Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (comparator.compare(v, e) < 0) {
                e = v;
            }
        }
        return e;
    }

    @Nullable
    public static <E extends Comparable<E>> E minOrNull(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return null;
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) < 0) {
                e = v;
            }
        }
        return e;
    }

    @Nullable
    public static <E> E minOrNull(@NotNull Iterator<? extends E> it, @NotNull Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            return null;
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (comparator.compare(v, e) < 0) {
                e = v;
            }
        }
        return e;
    }

    public static <E extends Comparable<E>> Option<E> minOption(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (v.compareTo(e) < 0) {
                e = v;
            }
        }
        return Option.some(e);
    }

    public static <E> Option<E> minOption(@NotNull Iterator<? extends E> it, @NotNull Comparator<? super E> comparator) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        while (it.hasNext()) {
            E v = it.next();
            if (comparator.compare(v, e) < 0) {
                e = v;
            }
        }
        return Option.some(e);
    }

    @Contract(mutates = "param1")
    public static <E> Iterator<E> drop(@NotNull Iterator<? extends E> it, int n) {
        while (n > 0 && it.hasNext()) {
            it.next();
            --n;
        }
        return ((Iterator<E>) it);
    }

    @NotNull
    public static <E> Iterator<E> dropWhile(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        if (!it.hasNext()) {
            return (Iterator<E>) it;
        }

        E value = null;
        boolean p = false;
        while (it.hasNext()) {
            if (!predicate.test(value = it.next())) {
                p = true;
                break;
            }
        }

        if (p) {
            return it.hasNext() ? prepended(it, value) : Iterators.of(value);
        } else {
            return (Iterator<E>) it;
        }
    }

    @NotNull
    public static <E> Iterator<E> take(@NotNull Iterator<? extends E> it, int n) {
        if (!it.hasNext() || n <= 0) {
            return empty();
        }

        return new AbstractIterator<E>() {
            int c = n;

            @Override
            public final boolean hasNext() {
                return c > 0 && it.hasNext();
            }

            @Override
            public final E next() {
                if (hasNext()) {
                    --c;
                    return it.next();
                }
                throw new NoSuchElementException(this + ".next()");
            }
        };
    }

    @NotNull
    public static <E> Iterator<E> takeWhile(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        if (!it.hasNext()) {
            return (Iterator<E>) it;
        }
        return new TakeWhile<>(it, predicate);
    }

    @NotNull
    public static <E> Iterator<E> updated(@NotNull Iterator<? extends E> it, int n, E newValue) {
        if (!it.hasNext() || n < 0) {
            return (Iterator<E>) it;
        }

        if (n == 0) {
            it.next();
            return prepended(it, newValue);
        }

        return new AbstractIterator<E>() {
            private int idx = 0;

            @Override
            public final boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public final E next() {
                if (idx++ == n) {
                    it.next();
                    return newValue;
                } else {
                    return it.next();
                }
            }
        };
    }

    @NotNull
    public static <E> Iterator<E> prepended(@NotNull Iterator<? extends E> it, E value) {
        Objects.requireNonNull(it);
        return new AbstractIterator<E>() {
            private boolean flag = true;

            @Override
            public final boolean hasNext() {
                return flag || it.hasNext();
            }

            @Override
            public final E next() {
                if (flag) {
                    flag = false;
                    return value;
                }

                return it.next();
            }
        };
    }

    @NotNull
    public static <E> Iterator<E> appended(@NotNull Iterator<? extends E> it, E value) {
        if (!it.hasNext()) {
            return Iterators.of(value);
        }
        return new AbstractIterator<E>() {
            private boolean flag = true;

            @Override
            public final boolean hasNext() {
                return it.hasNext() || flag;
            }

            @Override
            public final E next() {
                if (it.hasNext()) {
                    return it.next();
                }
                if (flag) {
                    flag = false;
                    return value;
                }
                throw new NoSuchElementException();
            }
        };
    }

    @NotNull
    public static <E> Iterator<E> filter(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        if (!it.hasNext()) {
            return empty();
        }
        return new Filter<>(it, predicate, false);
    }

    @NotNull
    public static <E> Iterator<E> filterNot(@NotNull Iterator<? extends E> it, @NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        if (!it.hasNext()) {
            return empty();
        }
        return new Filter<>(it, predicate, true);
    }

    @NotNull
    public static <E> Iterator<@NotNull E> filterNotNull(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return empty();
        }
        return new FilterNotNull<>(it);
    }

    @NotNull
    public static <E, U> Iterator<U> map(
            @NotNull Iterator<? extends E> it,
            @NotNull Function<? super E, ? extends U> mapper
    ) {
        Objects.requireNonNull(mapper);
        if (!it.hasNext()) {
            return (Iterator<U>) it;
        }
        return new AbstractIterator<U>() {
            @Override
            public final boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public final U next() {
                return mapper.apply(it.next());
            }
        };
    }

    @NotNull
    public static <E, U> Iterator<U> flatMap(
            @NotNull Iterator<? extends E> it,
            @NotNull Function<? super E, ? extends Iterable<? extends U>> mapper
    ) {
        Objects.requireNonNull(mapper);
        if (!it.hasNext()) {
            return empty();
        }

        return new Concat<>(Iterators.map(Iterators.map(it, mapper), Iterable::iterator));
    }

    @NotNull
    public static <E> Tuple2<Iterator<E>, Iterator<E>> span(
            @NotNull Iterator<E> it, @NotNull Predicate<? super E> predicate
    ) {
        if (!it.hasNext()) {
            return Tuple.of(empty(), empty());
        }

        LinkedList<E> list = new LinkedList<>();

        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                list.add(e);
            } else {
                it = prepended(it, e);
                break;
            }
        }

        return new Tuple2<>(list.iterator(), it);
    }

    public static <E> E fold(
            @NotNull Iterator<? extends E> it,
            E zero, @NotNull BiFunction<? super E, ? super E, ? extends E> op
    ) {
        while (it.hasNext()) {
            zero = op.apply(zero, it.next());
        }
        return zero;
    }

    public static <E, U> U foldLeft(
            @NotNull Iterator<? extends E> it,
            U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op
    ) {
        while (it.hasNext()) {
            zero = op.apply(zero, it.next());
        }
        return zero;
    }


    public static <E, U> U foldRight(
            @NotNull Iterator<? extends E> it,
            U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op
    ) {
        if (!it.hasNext()) {
            return zero;
        }
        LinkedList<E> list = new LinkedList<>();
        while (it.hasNext()) {
            list.addFirst(it.next());
        }
        for (E u : list) {
            zero = op.apply(u, zero);
        }
        return zero;
    }

    public static <E> E reduce(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        while (it.hasNext()) {
            e = op.apply(e, it.next());
        }
        return e;
    }

    public static <E> E reduceLeft(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        while (it.hasNext()) {
            e = op.apply(e, it.next());
        }
        return e;
    }

    public static <E> E reduceRight(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        LinkedList<E> list = new LinkedList<>();
        while (it.hasNext()) {
            list.addFirst(it.next());
        }
        it = list.iterator();
        E e = it.next();
        if (it.hasNext()) {
            e = op.apply(it.next(), e);
        }
        return e;
    }

    public static <E> Option<E> reduceOption(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        while (it.hasNext()) {
            e = op.apply(e, it.next());
        }
        return Option.some(e);
    }

    public static <E> Option<E> reduceLeftOption(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        if (!it.hasNext()) {
            return Option.none();
        }
        E e = it.next();
        while (it.hasNext()) {
            e = op.apply(e, it.next());
        }
        return Option.some(e);
    }

    public static <E> Option<E> reduceRightOption(@NotNull Iterator<? extends E> it, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        if (!it.hasNext()) {
            return Option.none();
        }
        LinkedList<E> list = new LinkedList<>();
        while (it.hasNext()) {
            list.addFirst(it.next());
        }
        it = list.iterator();
        E e = it.next();
        if (it.hasNext()) {
            e = op.apply(it.next(), e);
        }
        return Option.some(e);
    }

    public static <E> E[] toArray(@NotNull Iterator<? extends E> it, @NotNull IntFunction<E[]> generator) {
        Objects.requireNonNull(generator);
        if (!it.hasNext()) {
            return generator.apply(0);
        }
        ArrayList<E> buffer = new ArrayList<>();
        while (it.hasNext()) {
            buffer.add(it.next());
        }
        return buffer.toArray(generator.apply(buffer.size()));
    }

    static <E, R, Builder> R collect(
            @NotNull Iterator<? extends E> it,
            @NotNull CollectionFactory<? super E, Builder, ? extends R> factory
    ) {
        if (!it.hasNext()) {
            return factory.empty();
        }
        Builder builder = factory.newBuilder();
        while (it.hasNext()) {
            factory.addToBuilder(builder, it.next());
        }
        return factory.build(builder);
    }

    public static <E, R, Builder> R collect(
            @NotNull Iterator<? extends E> it,
            @NotNull Collector<? super E, Builder, ? extends R> collector
    ) {
        Builder builder = collector.supplier().get();
        final Function<Builder, ? extends R> finisher = collector.finisher();
        if (!it.hasNext()) {
            return finisher.apply(builder);
        }

        final BiConsumer<Builder, ? super E> accumulator = collector.accumulator();
        while (it.hasNext()) {
            accumulator.accept(builder, it.next());
        }
        return finisher.apply(builder);
    }


    @Contract(value = "_, _ -> param2", mutates = "param1, param2")
    public static <A extends Appendable> A joinTo(
            @NotNull Iterator<?> it,
            @NotNull A buffer
    ) {
        return joinTo(it, buffer, ", ", "", "");
    }

    @Contract(value = "_, _, _ -> param2", mutates = "param1, param2")
    public static <A extends Appendable> A joinTo(
            @NotNull Iterator<?> it,
            @NotNull A buffer,
            CharSequence separator
    ) {
        return joinTo(it, buffer, separator, "", "");
    }

    @Contract(value = "_, _, _, _, _ -> param2", mutates = "param1, param2")
    public static <A extends Appendable> A joinTo(
            @NotNull Iterator<?> it,
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        try {
            buffer.append(prefix);
            if (it.hasNext()) {
                buffer.append(Objects.toString(it.next()));
            }
            while (it.hasNext()) {
                buffer.append(separator).append(Objects.toString(it.next()));
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return buffer;
    }

    @NotNull
    public static String joinToString(@NotNull Iterator<?> it) {
        return joinTo(it, new StringBuilder()).toString();
    }

    @NotNull
    public static String joinToString(
            @NotNull Iterator<?> it,
            CharSequence separator
    ) {
        return joinTo(it, new StringBuilder(), separator).toString();
    }

    @NotNull
    public static String joinToString(
            @NotNull Iterator<?> it,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return joinTo(it, new StringBuilder(), separator, prefix, postfix).toString();
    }


    public static <E> void forEach(@NotNull Iterator<? extends E> it, @NotNull Consumer<? super E> action) {
        while (it.hasNext()) {
            action.accept(it.next());
        }
    }

    public static <E> void forEachIndexed(@NotNull Iterator<? extends E> it, @NotNull IndexedConsumer<? super E> action) {
        int idx = 0;
        while (it.hasNext()) {
            action.accept(idx++, it.next());
        }
    }

    private static final Iterator<?> EMPTY = new AbstractIterator<Object>() {
        @Override
        public final boolean hasNext() {
            return false;
        }

        @Override
        public final Object next() {
            throw new NoSuchElementException();
        }

        @Override
        public final String toString() {
            return "Iterator[]";
        }
    };

    static final class Filter<@Covariant E> extends AbstractIterator<E> {
        @NotNull
        private final Iterator<? extends E> source;
        @NotNull
        private final Predicate<? super E> predicate;

        private E nextValue = null;
        private boolean flag = false;

        private final boolean isFlipped;

        Filter(@NotNull Iterator<? extends E> source, @NotNull Predicate<? super E> predicate, boolean isFlipped) {
            this.source = source;
            this.predicate = predicate;
            this.isFlipped = isFlipped;
        }

        @Override
        public final boolean hasNext() {
            if (flag) {
                return true;
            }
            if (!source.hasNext()) {
                return false;
            }
            E v = source.next();
            while (predicate.test(v) == isFlipped) {
                if (!source.hasNext()) {
                    return false;
                }
                v = source.next();
            }

            this.nextValue = v;
            flag = true;
            return true;
        }

        @Override

        public final E next() {
            if (hasNext()) {
                flag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    static final class FilterNotNull<@Covariant E> extends AbstractIterator<E> {
        @NotNull
        private final Iterator<? extends E> source;

        private E nextValue = null;
        private boolean flag = false;

        FilterNotNull(@NotNull Iterator<? extends E> source) {
            this.source = source;
        }

        @Override
        public final boolean hasNext() {
            if (flag) {
                return true;
            }
            if (!source.hasNext()) {
                return false;
            }
            E v = source.next();
            while (v == null) {
                if (!source.hasNext()) {
                    return false;
                }
                v = source.next();
            }

            this.nextValue = v;
            flag = true;
            return true;
        }

        @Override

        public final E next() {
            if (hasNext()) {
                flag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    static final class TakeWhile<@Covariant E> extends AbstractIterator<E> {
        @NotNull
        private Iterator<? extends E> source;

        private final Predicate<? super E> predicate;

        private E nextValue = null;
        private boolean tag = false;

        TakeWhile(@NotNull Iterator<? extends E> source, Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final boolean hasNext() {
            if (tag) {
                return true;
            }

            if (source.hasNext()) {
                E v = nextValue = source.next();
                if (predicate.test(v)) {
                    tag = true;
                    return true;
                } else {
                    source = Iterators.empty();
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public final E next() {
            if (hasNext()) {
                tag = false;
                return nextValue;
            } else {
                throw new NoSuchElementException(this + ".next()");
            }
        }
    }

    static final class Concat<@Covariant E> extends AbstractIterator<E> {
        @NotNull
        private final Iterator<? extends Iterator<? extends E>> iterators;

        private Iterator<? extends E> current = null;

        Concat(@NotNull Iterator<? extends Iterator<? extends E>> iterators) {
            this.iterators = iterators;
        }

        @Override
        public final boolean hasNext() {
            while ((current == null || !current.hasNext()) && iterators.hasNext()) {
                current = iterators.next();
            }
            return current != null && current.hasNext();
        }

        @Override
        public final E next() {
            if (hasNext()) {
                return current.next();
            }
            throw new NoSuchElementException(this + ".next()");
        }
    }
}
