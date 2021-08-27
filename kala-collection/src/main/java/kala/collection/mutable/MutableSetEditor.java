package kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class MutableSetEditor<E, C extends MutableSet<E>> extends MutableCollectionEditor<E, C> {
    public MutableSetEditor(@NotNull C source) {
        super(source);
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> add(E value) {
        source.add(value);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> addAll(@NotNull Iterable<? extends E> values) {
        source.addAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> addAll(E @NotNull [] values) {
        source.addAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> remove(E value) {
        source.remove(value);
        return this;
    }

    @Contract("-> this")
    public @NotNull MutableSetEditor<E, C> clear() {
        source.clear();
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> removeAll(@NotNull Iterable<? super E> values) {
        source.removeAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> removeAll(E @NotNull [] values) {
        source.removeAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> removeAll(@NotNull Predicate<? super E> predicate) {
        source.removeAll(predicate);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> retainAll(@NotNull Iterable<? super E> values) {
        source.retainAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> retainAll(E @NotNull [] values) {
        source.retainAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> retainAll(@NotNull Predicate<? super E> predicate) {
        source.retainAll(predicate);
        return this;
    }
}
