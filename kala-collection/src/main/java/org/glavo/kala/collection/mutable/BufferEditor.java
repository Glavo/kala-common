package org.glavo.kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public class BufferEditor<E, C extends Buffer<E>> extends MutableSeqEditor<E, C> {
    protected BufferEditor(@NotNull C source) {
        super(source);
    }

    @Contract(value = "_ -> this")
    public @NotNull BufferEditor<E, C> append(E value) {
        source.append(value);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull BufferEditor<E, C> appendAll(@NotNull Iterable<? extends E> collection) {
        source.appendAll(collection);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull BufferEditor<E, C> appendAll(E @NotNull [] collection) {
        source.appendAll(collection);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull BufferEditor<E, C> prepend(E value) {
        source.prepend(value);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull BufferEditor<E, C> prependAll(@NotNull Iterable<? extends E> collection) {
        source.prependAll(collection);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull BufferEditor<E, C> prependAll(E @NotNull [] collection) {
        source.prependAll(collection);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull BufferEditor<E, C> insert(int index, E element) {
        source.insert(index, element);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull BufferEditor<E, C> insertAll(int index, @NotNull Iterable<? extends E> values) {
        source.insertAll(index, values);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull BufferEditor<E, C> insertAll(int index, E @NotNull [] values) {
        source.insertAll(index, values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull BufferEditor<E, C> removeAt(int index) {
        source.removeAt(index);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull BufferEditor<E, C> removeAt(int index, int count) {
        source.removeAt(index, count);
        return this;
    }

    @Contract("-> this")
    public @NotNull BufferEditor<E, C> clear() {
        source.clear();
        return this;
    }

    @Contract("_ -> this")
    public @NotNull BufferEditor<E, C> dropInPlace(int n) {
        source.dropInPlace(n);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull BufferEditor<E, C> dropWhileInPlace(@NotNull Predicate<? super E> predicate) {
        source.dropWhileInPlace(predicate);
        return this;
    }

    @Contract("_ -> this")
    public BufferEditor<E, C> takeInPlace(int n) {
        source.takeInPlace(n);
        return this;
    }

    @Contract("_ -> this")
    public BufferEditor<E, C> takeWhileInPlace(@NotNull Predicate<? super E> predicate) {
        source.takeWhileInPlace(predicate);
        return this;
    }

    //region MutableSeqEditor members

    @NotNull
    @Override
    @Contract("_, _ -> this")
    public BufferEditor<E, C> set(int index, E newValue) {
        source.set(index, newValue);
        return this;
    }

    @NotNull
    @Override
    @Contract("_ -> this")
    public BufferEditor<E, C> mapInPlace(@NotNull Function<? super E, ? extends E> mapper) {
        source.mapInPlace(mapper);
        return this;
    }

    @NotNull
    @Override
    @Contract("-> this")
    public BufferEditor<E, C> sort() {
        source.sort();
        return this;
    }

    @Override
    @NotNull
    @Contract("_ -> this")
    public BufferEditor<E, C> sort(@NotNull Comparator<? super E> comparator) {
        source.sort(comparator);
        return this;
    }

    //endregion
}