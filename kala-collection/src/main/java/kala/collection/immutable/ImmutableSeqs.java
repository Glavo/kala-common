package kala.collection.immutable;

import kala.collection.*;
import kala.collection.base.Iterators;
import kala.control.Option;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.Conditions;
import org.glavo.kala.collection.*;
import kala.collection.factory.CollectionFactory;
import kala.collection.internal.view.IndexedSeqViews;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.Serializable;
import java.util.*;
import java.util.Map;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final class ImmutableSeqs {
    private ImmutableSeqs() {
    }

    static abstract class SeqN<E> extends AbstractImmutableSeq<E> implements IndexedSeq<E> {

    }

    static final class Seq0<E> extends SeqN<E> {
        static final Seq0<?> INSTANCE = new Seq0<>();

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> reversed() {
            return this;
        }

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return Iterators.empty();
        }

        @Override
        public final @NotNull Spliterator<E> spliterator() {
            return Spliterators.emptySpliterator();
        }

        @Override
        public final @NotNull IndexedSeqView<E> view() {
            return IndexedSeqView.empty();
        }

        @Override
        public final int size() {
            return 0;
        }

        @Override
        public final E get(int index) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public final @NotNull ImmutableSeq<E> prepended(E value) {
            return ImmutableSeq.of(value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> appended(E value) {
            return ImmutableSeq.of(value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> take(int n) {
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeLast(int n) {
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> drop(int n) {
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropLast(int n) {
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> updated(int index, E newValue) {
            throw new IndexOutOfBoundsException("index: " + index);
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return empty();
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return empty();
        }

        @Override
        public final @NotNull <U> ImmutableSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
            return empty();
        }

        @Override
        public final @NotNull <U> ImmutableSeq<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
            return empty();
        }
    }

    static final class Seq1<E> extends SeqN<E> {
        private final E value1;

        Seq1(E value1) {
            this.value1 = value1;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.of(value1);
        }

        @Override
        public final @NotNull ImmutableSeq<E> reversed() {
            return this;
        }

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return Iterators.of(value1);
        }

        @Override
        public final int size() {
            return 1;
        }

        @Override
        public final E get(int index) {
            if (index != 0) {
                throw new IndexOutOfBoundsException();
            }
            return value1;
        }

        @Override
        public final @NotNull ImmutableSeq<E> prepended(E value) {
            return ImmutableSeq.of(value, value1);
        }

        @Override
        public final @NotNull ImmutableSeq<E> appended(E value) {
            return ImmutableSeq.of(value1, value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> take(int n) {
            if (n <= 0) {
                return empty();
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeLast(int n) {
            if (n <= 0) {
                return empty();
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return empty();
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> drop(int n) {
            if (n <= 0) {
                return this;
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropLast(int n) {
            if (n <= 0) {
                return this;
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            return predicate.test(value1) ? empty() : this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> updated(int index, E newValue) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("index: " + index);
            }
            return ImmutableSeq.of(newValue);
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(value1)
            );
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(0, value1)
            );
        }

        @Override
        public final @NotNull <U> ImmutableSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
            final U u = mapper.apply(value1);
            return u == null ? empty() : ImmutableSeq.of(u);
        }

        @Override
        public final @NotNull <U> ImmutableSeq<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
            final U u = mapper.apply(0, value1);
            return u == null ? empty() : ImmutableSeq.of(u);
        }
    }

    static final class Seq2<E> extends SeqN<E> {
        private final E value1;
        private final E value2;

        Seq2(E value1, E value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.of(value1, value2);
        }

        @Override
        public final @NotNull ImmutableSeq<E> reversed() {
            return new Seq2<>(value2, value1);
        }

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return Iterators.of(value2, value1);
        }

        @Override
        public final int size() {
            return 2;
        }

        @Override
        public final E get(int index) {
            switch (index) {
                case 0:
                    return value1;
                case 1:
                    return value2;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public final @NotNull ImmutableSeq<E> prepended(E value) {
            return ImmutableSeq.of(value, value1, value2);
        }

        @Override
        public final @NotNull ImmutableSeq<E> appended(E value) {
            return ImmutableSeq.of(value1, value2, value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> take(int n) {
            if (n <= 0) {
                return empty();
            }
            if (n == 1) {
                return ImmutableSeq.of(value1);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeLast(int n) {
            if (n <= 0) {
                return empty();
            }
            if (n == 1) {
                return ImmutableSeq.of(value2);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return empty();
            }
            if (!predicate.test(value2)) {
                return ImmutableSeq.of(value1);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> drop(int n) {
            if (n <= 0) {
                return this;
            }
            if (n == 1) {
                return ImmutableSeq.of(value2);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropLast(int n) {
            if (n <= 0) {
                return this;
            }
            if (n == 1) {
                return ImmutableSeq.of(value1);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return this;
            }
            if (!predicate.test(value2)) {
                return ImmutableSeq.of(value2);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> updated(int index, E newValue) {
            switch (index) {
                case 0:
                    return ImmutableSeq.of(newValue, value2);
                case 1:
                    return ImmutableSeq.of(value1, newValue);
            }
            throw new IndexOutOfBoundsException("index: " + index);
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(value1),
                    mapper.apply(value2)
            );
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(0, value1),
                    mapper.apply(1, value2)
            );
        }
    }

    static final class Seq3<E> extends SeqN<E> {
        private final E value1;
        private final E value2;
        private final E value3;

        Seq3(E value1, E value2, E value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.of(value1, value2, value3);
        }

        @Override
        public final @NotNull ImmutableSeq<E> reversed() {
            return new Seq3<>(value3, value2, value1);
        }

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return Iterators.of(value3, value2, value1);
        }

        @Override
        public final int size() {
            return 3;
        }

        @Override
        public final E get(int index) {
            switch (index) {
                case 0:
                    return value1;
                case 1:
                    return value2;
                case 2:
                    return value3;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public final @NotNull ImmutableSeq<E> prepended(E value) {
            return ImmutableSeq.of(value, value1, value2, value3);
        }

        @Override
        public final @NotNull ImmutableSeq<E> appended(E value) {
            return ImmutableSeq.of(value1, value2, value3, value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> take(int n) {
            if (n <= 0) {
                return empty();
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value1);
                case 2:
                    return ImmutableSeq.of(value1, value2);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeLast(int n) {
            if (n <= 0) {
                return empty();
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value3);
                case 2:
                    return ImmutableSeq.of(value2, value3);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return empty();
            }
            if (!predicate.test(value2)) {
                return ImmutableSeq.of(value1);
            }
            if (!predicate.test(value3)) {
                return ImmutableSeq.of(value1, value2);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> drop(int n) {
            if (n <= 0) {
                return this;
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value2, value3);
                case 2:
                    return ImmutableSeq.of(value3);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropLast(int n) {
            if (n <= 0) {
                return this;
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value1, value2);
                case 2:
                    return ImmutableSeq.of(value2);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return this;
            }
            if (!predicate.test(value2)) {
                return ImmutableSeq.of(value2, value3);
            }
            if (!predicate.test(value3)) {
                return ImmutableSeq.of(value3);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> updated(int index, E newValue) {
            switch (index) {
                case 0:
                    return ImmutableSeq.of(newValue, value2, value3);
                case 1:
                    return ImmutableSeq.of(value1, newValue, value3);
                case 2:
                    return ImmutableSeq.of(value1, value2, newValue);
            }
            throw new IndexOutOfBoundsException("index: " + index);
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(value1),
                    mapper.apply(value2),
                    mapper.apply(value3)
            );
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(0, value1),
                    mapper.apply(1, value2),
                    mapper.apply(2, value3)
            );
        }
    }

    static final class Seq4<E> extends SeqN<E> {
        private final E value1;
        private final E value2;
        private final E value3;
        private final E value4;

        Seq4(E value1, E value2, E value3, E value4) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.value4 = value4;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.of(value1, value2, value3, value4);
        }

        @Override
        public final @NotNull ImmutableSeq<E> reversed() {
            return new Seq4<>(value4, value3, value2, value1);
        }

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return Iterators.of(value4, value3, value2, value1);
        }

        @Override
        public final int size() {
            return 4;
        }

        @Override
        public final E get(int index) {
            switch (index) {
                case 0:
                    return value1;
                case 1:
                    return value2;
                case 2:
                    return value3;
                case 3:
                    return value4;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public final @NotNull ImmutableSeq<E> prepended(E value) {
            return ImmutableSeq.of(value, value1, value2, value3, value4);
        }

        @Override
        public final @NotNull ImmutableSeq<E> appended(E value) {
            return ImmutableSeq.of(value1, value2, value3, value4, value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> take(int n) {
            if (n <= 0) {
                return empty();
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value1);
                case 2:
                    return ImmutableSeq.of(value1, value2);
                case 3:
                    return ImmutableSeq.of(value1, value2, value3);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeLast(int n) {
            if (n <= 0) {
                return empty();
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value4);
                case 2:
                    return ImmutableSeq.of(value3, value4);
                case 3:
                    return ImmutableSeq.of(value2, value3, value4);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return empty();
            }
            if (!predicate.test(value2)) {
                return ImmutableSeq.of(value1);
            }
            if (!predicate.test(value3)) {
                return ImmutableSeq.of(value1, value2);
            }
            if (!predicate.test(value4)) {
                return ImmutableSeq.of(value1, value2, value3);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> drop(int n) {
            if (n <= 0) {
                return this;
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value2, value3, value4);
                case 2:
                    return ImmutableSeq.of(value3, value4);
                case 3:
                    return ImmutableSeq.of(value4);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropLast(int n) {
            if (n <= 0) {
                return this;
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value1, value2, value3);
                case 2:
                    return ImmutableSeq.of(value1, value2);
                case 3:
                    return ImmutableSeq.of(value1);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return this;
            }
            if (!predicate.test(value2)) {
                return ImmutableSeq.of(value2, value3, value4);
            }
            if (!predicate.test(value3)) {
                return ImmutableSeq.of(value3, value4);
            }
            if (!predicate.test(value4)) {
                return ImmutableSeq.of(value4);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> updated(int index, E newValue) {
            switch (index) {
                case 0:
                    return ImmutableSeq.of(newValue, value2, value3, value4);
                case 1:
                    return ImmutableSeq.of(value1, newValue, value3, value4);
                case 2:
                    return ImmutableSeq.of(value1, value2, newValue, value4);
                case 3:
                    return ImmutableSeq.of(value1, value2, value3, newValue);
            }
            throw new IndexOutOfBoundsException("index: " + index);
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(value1),
                    mapper.apply(value2),
                    mapper.apply(value3),
                    mapper.apply(value4)
            );
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(0, value1),
                    mapper.apply(1, value2),
                    mapper.apply(2, value3),
                    mapper.apply(3, value4)
            );
        }
    }

    static final class Seq5<E> extends SeqN<E> {
        private final E value1;
        private final E value2;
        private final E value3;
        private final E value4;
        private final E value5;

        Seq5(E value1, E value2, E value3, E value4, E value5) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.value4 = value4;
            this.value5 = value5;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.of(value1, value2, value3, value4, value5);
        }

        @Override
        public final @NotNull ImmutableSeq<E> reversed() {
            return new Seq5<>(value5, value4, value3, value2, value1);
        }

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return Iterators.of(value5, value4, value3, value2, value1);
        }

        @Override
        public final int size() {
            return 5;
        }

        @Override
        public final E get(int index) {
            switch (index) {
                case 0:
                    return value1;
                case 1:
                    return value2;
                case 2:
                    return value3;
                case 3:
                    return value4;
                case 4:
                    return value5;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public final @NotNull ImmutableSeq<E> prepended(E value) {
            return new ImmutableVectors.Vector1<>(new Object[]{value, value1, value2, value3, value4, value5});
        }

        @Override
        public final @NotNull ImmutableSeq<E> appended(E value) {
            return new ImmutableVectors.Vector1<>(new Object[]{value1, value2, value3, value4, value5, value});
        }

        @Override
        public final @NotNull ImmutableSeq<E> take(int n) {
            if (n <= 0) {
                return empty();
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value1);
                case 2:
                    return ImmutableSeq.of(value1, value2);
                case 3:
                    return ImmutableSeq.of(value1, value2, value3);
                case 4:
                    return ImmutableSeq.of(value1, value2, value3, value4);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeLast(int n) {
            if (n <= 0) {
                return empty();
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value5);
                case 2:
                    return ImmutableSeq.of(value4, value5);
                case 3:
                    return ImmutableSeq.of(value3, value4, value5);
                case 4:
                    return ImmutableSeq.of(value2, value3, value4, value5);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return empty();
            }
            if (!predicate.test(value2)) {
                return ImmutableSeq.of(value1);
            }
            if (!predicate.test(value3)) {
                return ImmutableSeq.of(value1, value2);
            }
            if (!predicate.test(value4)) {
                return ImmutableSeq.of(value1, value2, value3);
            }
            if (!predicate.test(value5)) {
                return ImmutableSeq.of(value1, value2, value3, value4);
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> drop(int n) {
            if (n <= 0) {
                return this;
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value2, value3, value4, value5);
                case 2:
                    return ImmutableSeq.of(value3, value4, value5);
                case 3:
                    return ImmutableSeq.of(value4, value5);
                case 4:
                    return ImmutableSeq.of(value5);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropLast(int n) {
            if (n <= 0) {
                return this;
            }
            switch (n) {
                case 1:
                    return ImmutableSeq.of(value1, value2, value3, value4);
                case 2:
                    return ImmutableSeq.of(value1, value2, value3);
                case 3:
                    return ImmutableSeq.of(value1, value2);
                case 4:
                    return ImmutableSeq.of(value1);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return this;
            }
            if (!predicate.test(value2)) {
                return ImmutableSeq.of(value2, value3, value4, value5);
            }
            if (!predicate.test(value3)) {
                return ImmutableSeq.of(value3, value4, value5);
            }
            if (!predicate.test(value4)) {
                return ImmutableSeq.of(value4, value5);
            }
            if (!predicate.test(value5)) {
                return ImmutableSeq.of(value5);
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> updated(int index, E newValue) {
            switch (index) {
                case 0:
                    return ImmutableSeq.of(newValue, value2, value3, value4, value5);
                case 1:
                    return ImmutableSeq.of(value1, newValue, value3, value4, value5);
                case 2:
                    return ImmutableSeq.of(value1, value2, newValue, value4, value5);
                case 3:
                    return ImmutableSeq.of(value1, value2, value3, newValue, value5);
                case 4:
                    return ImmutableSeq.of(value1, value2, value3, value4, newValue);
            }
            throw new IndexOutOfBoundsException("index: " + index);
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(value1),
                    mapper.apply(value2),
                    mapper.apply(value3),
                    mapper.apply(value4),
                    mapper.apply(value5)
            );
        }

        @Override
        public final @NotNull <U> ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(0, value1),
                    mapper.apply(1, value2),
                    mapper.apply(2, value3),
                    mapper.apply(3, value4),
                    mapper.apply(4, value5)
            );
        }
    }

    @SuppressWarnings("unchecked")
    static abstract class CopiesSeqBase<E>
            implements IndexedSeqLike<E> {
        protected final @Range(from = 1, to = Integer.MAX_VALUE) int size;
        protected final E value;

        protected CopiesSeqBase(@Range(from = 1, to = Integer.MAX_VALUE) int size, E value) {
            this.size = size;
            this.value = value;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.fill(size, value);
        }

        @Override
        public final @NotNull Spliterator<E> spliterator() {
            return stream().spliterator();
        }

        @Override
        public final @NotNull Stream<E> stream() {
            return size == 1 ? Stream.of(value) : IntStream.range(0, size).mapToObj(i -> value);
        }

        @Override
        public final @NotNull Stream<E> parallelStream() {
            return size == 1 ? Stream.of(value).parallel() : IntStream.range(0, size).parallel().mapToObj(i -> value);
        }

        @Override
        public final int size() {
            return size;
        }

        //region Positional Access Operations

        @Override
        public final E get(int index) {
            Conditions.checkElementIndex(index, size);
            return value;
        }

        @Override
        public final @Nullable E getOrNull(int index) {
            return index >= 0 && index < size ? value : null;
        }

        @Override
        public final @NotNull Option<E> getOption(int index) {
            return index >= 0 && index < size ? Option.some(value) : Option.none();
        }

        //endregion

        //region Reversal Operations

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return iterator();
        }

        //endregion

        //region Element Retrieval Operations

        @Override
        public final E first() {
            return value;
        }

        @Override
        public final @Nullable E firstOrNull() {
            return value;
        }

        @Override
        public final @NotNull Option<E> firstOption() {
            return Option.some(value);
        }

        @Override
        public final E last() {
            return value;
        }

        @Override
        public final @Nullable E lastOrNull() {
            return value;
        }

        @Override
        public final @NotNull Option<E> lastOption() {
            return Option.some(value);
        }

        //endregion

        //region Element Conditions

        @Override
        public final boolean contains(Object value) {
            return Objects.equals(value, this.size);
        }

        @Override
        public final boolean containsAll(Object @NotNull [] values) {
            final E value = this.value;
            if (value == null) {
                for (Object v : values) {
                    if (null != v) {
                        return false;
                    }
                }
            } else {
                for (Object v : values) {
                    if (!value.equals(v)) {
                        return false;
                    }
                }
            }
            return true;
        }

        //endregion

        //region Search Operations

        @Override
        public final int indexOf(Object value) {
            return Objects.equals(value, this.value) ? 0 : -1;
        }

        @Override
        public final int indexOf(Object value, int from) {
            if (from >= size) {
                return -1;
            } else {
                return Objects.equals(value, this.value) ? Integer.max(from, 0) : -1;
            }
        }

        @Override
        public final int lastIndexOf(Object value) {
            return Objects.equals(value, this.value) ? size - 1 : -1;
        }

        @Override
        public final int lastIndexOf(Object value, int end) {
            if (end < 0) {
                return -1;
            } else {
                return Objects.equals(value, this.value) ? Integer.min(size - 1, end) : -1;
            }
        }

        //endregion

        //region Aggregate Operations

        @Override
        public final E max() {
            return value;
        }

        @Override
        public E max(Comparator<? super E> comparator) {
            return max();
        }

        @Override
        public final @Nullable E maxOrNull() {
            return value;
        }

        @Override
        public final @Nullable E maxOrNull(@NotNull Comparator<? super E> comparator) {
            return maxOrNull();
        }

        @Override
        public final @NotNull Option<E> maxOption() {
            return Option.some(value);
        }

        @Override
        public final @NotNull Option<E> maxOption(Comparator<? super E> comparator) {
            return maxOption();
        }

        @Override
        public final E min() {
            return value;
        }

        @Override
        public E min(Comparator<? super E> comparator) {
            return min();
        }

        @Override
        public final @Nullable E minOrNull() {
            return value;
        }

        @Override
        public final @Nullable E minOrNull(@NotNull Comparator<? super E> comparator) {
            return minOrNull();
        }

        @Override
        public final @NotNull Option<E> minOption() {
            return Option.some(value);
        }

        @Override
        public final @NotNull Option<E> minOption(Comparator<? super E> comparator) {
            return minOption();
        }

        //endregion

        //region Copy Operations

        @Override
        public int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
            if (srcPos < 0) {
                throw new IllegalArgumentException("srcPos(" + destPos + ") < 0");
            }
            if (destPos < 0) {
                throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
            }

            final int dl = dest.length;
            final int size = this.size;

            if (destPos >= dl || srcPos >= size) {
                return 0;
            }

            final int n = Math.min(Math.min(size - srcPos, dl - destPos), limit);
            final E value = this.value;

            final int end = n + destPos;
            for (int i = destPos; i < end; i++) {
                dest[i] = value;
            }

            return n;
        }

        //endregion

        //region Conversion Operations

        @Override
        public final Object @NotNull [] toArray() {
            final Object[] res = new Object[size];
            final E value = this.value;
            if (value != null) {
                Arrays.fill(res, value);
            }
            return res;
        }

        @Override
        public final <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
            final U[] res = generator.apply(size);
            final E value = this.value;
            if (value != null) {
                Arrays.fill(res, value);
            }
            return res;
        }

        @Override
        public final @NotNull <K, V> ImmutableMap<K, V> toImmutableMap() {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) value;
            return ImmutableMap.of(entry.getKey(), entry.getValue());
        }

        //endregion

        //region Traverse Operations

        @Override
        public final void forEach(@NotNull Consumer<? super E> action) {
            for (int i = 0; i < size; i++) {
                action.accept(value);
            }
        }

        @Override
        public final void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
            for (int i = 0; i < size; i++) {
                action.accept(i, value);
            }
        }

        //endregion

        //region String Representation

        @Override
        public final String toString() {
            final String cn = this.className();
            final int size = this.size;
            final String vs = value.toString();

            final StringBuilder builder = new StringBuilder(cn.length() + (vs.length() + 2) * size);
            builder.append(cn).append('[').append(vs);
            for (int i = 1; i < size; i++) {
                builder.append(", ").append(vs);
            }
            builder.append(']');
            return builder.toString();
        }

        //endregion
    }

    static final class CopiesSeqView<E> extends CopiesSeqBase<E> implements IndexedSeqView<E> {
        public CopiesSeqView(@Range(from = 1, to = Integer.MAX_VALUE) int size, E value) {
            super(size, value);
        }

        @Override
        public final @NotNull String className() {
            return "CopiesSeqView";
        }

        @Override
        public final @NotNull IndexedSeqView<E> prepended(E value) {
            if (value == this.value) {
                return new CopiesSeqView<>(size + 1, value);
            }
            return IndexedSeqView.super.prepended(value);
        }

        @Override
        public final @NotNull IndexedSeqView<E> appended(E value) {
            if (value == this.value) {
                return new CopiesSeqView<>(size + 1, value);
            }
            return IndexedSeqView.super.appended(value);
        }

        @Override
        public final @NotNull ImmutableSeqs.CopiesSeqView<E> reversed() {
            return this;
        }

        //region Misc Operations

        @Override
        public final @NotNull IndexedSeqView<E> slice(int beginIndex, int endIndex) {
            Conditions.checkPositionIndices(beginIndex, endIndex, size);
            final int ns = endIndex - beginIndex;
            if (ns == 0) {
                return IndexedSeqView.empty();
            }
            if (ns == 1) {
                return new IndexedSeqViews.Single<>(value);
            }
            return new CopiesSeqView<>(ns, value);
        }

        @Override
        public final @NotNull IndexedSeqView<E> drop(int n) {
            if (n <= 0) {
                return this;
            }
            if (n >= size) {
                return IndexedSeqView.empty();
            }
            final int ns = size - n;
            if (ns == 1) {
                return new IndexedSeqViews.Single<>(value);
            }
            return new CopiesSeqView<>(ns, value);
        }

        @Override
        public final @NotNull IndexedSeqView<E> dropLast(int n) {
            return drop(n);
        }

        @Override
        public final @NotNull IndexedSeqView<E> take(int n) {
            if (n <= 0) {
                return IndexedSeqView.empty();
            }
            if (n >= size) {
                return this;
            }
            if (n == 1) {
                return new IndexedSeqViews.Single<>(value);
            }
            return new CopiesSeqView<>(n, value);
        }

        @Override
        public final @NotNull IndexedSeqView<E> takeLast(int n) {
            return take(n);
        }

        @Override
        public final @NotNull IndexedSeqView<@NotNull E> filterNotNull() {
            return value == null ? IndexedSeqView.empty() : this;
        }

        @Override
        public final @NotNull IndexedSeqView<E> sorted() {
            return this;
        }

        @Override
        public final @NotNull IndexedSeqView<E> sorted(Comparator<? super E> comparator) {
            return this;
        }

        //endregion

    }

    @SuppressWarnings("unchecked")
    static final class CopiesSeq<E> extends CopiesSeqBase<E>
            implements ImmutableSeq<E>, IndexedSeq<E>, Serializable {
        private static final long serialVersionUID = 6615175156982747837L;

        CopiesSeq(@Range(from = 1, to = Integer.MAX_VALUE) int size, E value) {
            super(size, value);
        }

        public static <E> @NotNull ImmutableSeq<E> fill(int n, E value) {
            if (n <= 0) {
                return empty();
            }
            return new CopiesSeq<>(n, value);
        }

        @Override
        public final @NotNull IndexedSeqView<E> view() {
            return size == 1 ? new IndexedSeqViews.Single<>(value) : new CopiesSeqView<>(size, value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> reversed() {
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> prepended(E value) {
            final int size = this.size;
            if (value == this.value) {
                return new CopiesSeq<>(size + 1, value);
            }
            final E oldValue = this.value;
            if (size < ImmutableVectors.WIDTH) {
                final Object[] arr = new Object[size + 1];
                Arrays.fill(arr, oldValue);
                arr[0] = value;
                return new ImmutableVectors.Vector1<>(arr);
            }
            final ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
            builder.add(value);
            for (int i = 0; i < size; i++) {
                builder.add(oldValue);
            }
            return builder.build();
        }

        @Override
        public final @NotNull ImmutableSeq<E> appended(E value) {
            final int size = this.size;
            if (value == this.value) {
                assert size != Integer.MAX_VALUE;
                return new CopiesSeq<>(size + 1, value);
            }
            final E oldValue = this.value;
            if (size < ImmutableVectors.WIDTH) {
                final Object[] arr = new Object[size + 1];
                Arrays.fill(arr, oldValue);
                arr[size] = value;
                return new ImmutableVectors.Vector1<>(arr);
            }
            final ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
            for (int i = 0; i < size; i++) {
                builder.add(oldValue);
            }
            builder.add(value);
            return builder.build();
        }

        //region Misc Operations

        @Override
        public final @NotNull ImmutableSeq<E> slice(int beginIndex, int endIndex) {
            Conditions.checkPositionIndices(beginIndex, endIndex, size);
            final int ns = endIndex - beginIndex;
            return ns == 0 ? empty() : new CopiesSeq<>(ns, value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> drop(int n) {
            if (n <= 0) {
                return this;
            }
            if (n >= size) {
                return empty();
            }
            return new CopiesSeq<>(size - n, value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropLast(int n) {
            return drop(n);
        }

        @Override
        public final @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            final int size = this.size;
            final E value = this.value;
            for (int i = 0; i < size; i++) {
                if (!predicate.test(value)) {
                    return drop(i);
                }
            }
            return empty();
        }

        @Override
        public final @NotNull ImmutableSeq<E> take(int n) {
            if (n <= 0) {
                return empty();
            }
            if (n >= size) {
                return this;
            }
            return new CopiesSeq<>(n, value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeLast(int n) {
            return take(n);
        }

        @Override
        public final @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            final int size = this.size;
            final E value = this.value;
            for (int i = 0; i < size; i++) {
                if (!predicate.test(value)) {
                    return take(i);
                }
            }
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> concat(@NotNull SeqLike<? extends E> other) {
            if (other instanceof CopiesSeq) {
                CopiesSeq<E> ics = (CopiesSeq<E>) other;
                if (ics.value == this.value) {
                    return new CopiesSeq<>(this.size + ics.size, value);
                }
            }
            Objects.requireNonNull(other);
            final ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
            for (int i = 0; i < size; i++) {
                builder.add(value);
            }
            builder.addAll(other);
            return builder.build();
        }

        @Override
        public final @NotNull ImmutableSeq<E> filter(@NotNull Predicate<? super E> predicate) {
            final int size = this.size;
            final E value = this.value;

            int c = 0;
            for (int i = 0; i < size; i++) {
                if (predicate.test(value)) {
                    c++;
                }
            }

            if (c == 0) {
                return empty();
            }
            if (c == 1) {
                return ImmutableSeq.of(value);
            }
            if (c == size) {
                return this;
            }
            return new CopiesSeq<>(c, value);
        }

        @Override
        public final @NotNull ImmutableSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
            final int size = this.size;
            final E value = this.value;

            int c = 0;
            for (int i = 0; i < size; i++) {
                if (!predicate.test(value)) {
                    c++;
                }
            }

            if (c == 0) {
                return empty();
            }
            if (c == 1) {
                return ImmutableSeq.of(value);
            }
            if (c == size) {
                return this;
            }
            return new CopiesSeq<>(c, value);
        }

        @Override
        public final @NotNull ImmutableSeq<@NotNull E> filterNotNull() {
            return value == null ? empty() : this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> sorted() {
            return this;
        }

        @Override
        public final @NotNull ImmutableSeq<E> sorted(Comparator<? super E> comparator) {
            return this;
        }

        //endregion

        @Override
        public final int hashCode() {
            if (value == null) {
                return SEQ_HASH_MAGIC;
            }
            final int vh = value.hashCode();
            if (vh == 0) {
                return SEQ_HASH_MAGIC;
            }

            int h = 0;
            for (int i = 0; i < size; i++) {
                h = h * 31 + vh;
            }
            return h + SEQ_HASH_MAGIC;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Seq)) {
                return false;
            }
            return Seq.equals(this, ((Seq<?>) obj));
        }
    }

    static final class Factory<E> implements CollectionFactory<E, ImmutableVectors.VectorBuilder<E>, ImmutableSeq<E>> {

        @Override
        public final ImmutableSeq<E> empty() {
            return ImmutableSeq.empty();
        }

        @Override
        public final ImmutableVectors.VectorBuilder<E> newBuilder() {
            return new ImmutableVectors.VectorBuilder<>();
        }

        @Override
        public final void addToBuilder(ImmutableVectors.@NotNull VectorBuilder<E> builder, E value) {
            builder.add(value);
        }

        @Override
        public final ImmutableVectors.VectorBuilder<E> mergeBuilder(ImmutableVectors.@NotNull VectorBuilder<E> builder1, ImmutableVectors.@NotNull VectorBuilder<E> builder2) {
            builder1.addVector(builder2.build());
            return builder1;
        }

        @Override
        public final ImmutableSeq<E> build(ImmutableVectors.@NotNull VectorBuilder<E> builder) {
            return builder.buildSeq();
        }

        @Override
        public final ImmutableSeq<E> from(E @NotNull [] values) {
            return ImmutableSeq.from(values);
        }

        @Override
        public final ImmutableSeq<E> from(@NotNull Iterable<? extends E> values) {
            return ImmutableSeq.from(values);
        }

        @Override
        public final ImmutableSeq<E> from(@NotNull Iterator<? extends E> it) {
            return ImmutableSeq.from(it);
        }

        @Override
        public final ImmutableSeq<E> fill(int n, E value) {
            return ImmutableSeq.fill(n, value);
        }

        @Override
        public final ImmutableSeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return ImmutableSeq.fill(n, supplier);
        }

        @Override
        public final ImmutableSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ImmutableSeq.fill(n, init);
        }
    }

    static final Factory<?> FACTORY = new Factory<>();
}
