package org.glavo.kala.collection;

import org.glavo.kala.collection.mutable.Growable;
import org.glavo.kala.function.IndexedFunction;
import org.jetbrains.annotations.NotNull;

import java.util.RandomAccess;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IndexedSeqLike<E> extends SeqLike<E>, RandomAccess {

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    default int knownSize() {
        return size();
    }

    @Override
    default <G extends Growable<? super E>> @NotNull G filterTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
        for (int i = 0; i < this.size(); i++) {
            E e = this.get(i);
            if (predicate.test(e)) {
                destination.addValue(e);
            }
        }
        return destination;
    }

    @Override
    default <G extends Growable<? super E>> @NotNull G filterNotTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
        for (int i = 0; i < this.size(); i++) {
            E e = this.get(i);
            if (!predicate.test(e)) {
                destination.addValue(e);
            }
        }
        return destination;
    }

    @Override
    default <G extends Growable<? super E>> @NotNull G filterNotNullTo(@NotNull G destination) {
        for (int i = 0; i < this.size(); i++) {
            E e = this.get(i);
            if (e != null) {
                destination.addValue(e);
            }
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<? super U>> @NotNull G mapTo(@NotNull G destination, @NotNull Function<? super E, ? extends U> mapper) {
        for (int i = 0; i < this.size(); i++) {
            destination.addValue(mapper.apply(this.get(i)));
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<? super U>> @NotNull G mapIndexedTo(@NotNull G destination, @NotNull IndexedFunction<? super E, ? extends U> mapper) {
        for (int i = 0; i < this.size(); i++) {
            destination.addValue(mapper.apply(i, this.get(i)));
        }
        return destination;
    }

}
