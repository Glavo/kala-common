package kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public class MutableListEditor<E, C extends MutableList<E>> extends MutableSeqEditor<E, C> {
    protected MutableListEditor(@NotNull C source) {
        super(source);
    }

    @Contract(value = "_ -> this")
    public @NotNull MutableListEditor<E, C> append(E value) {
        source.append(value);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableListEditor<E, C> appendAll(@NotNull Iterable<? extends E> collection) {
        source.appendAll(collection);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableListEditor<E, C> appendAll(E @NotNull [] collection) {
        source.appendAll(collection);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableListEditor<E, C> prepend(E value) {
        source.prepend(value);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableListEditor<E, C> prependAll(@NotNull Iterable<? extends E> collection) {
        source.prependAll(collection);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableListEditor<E, C> prependAll(E @NotNull [] collection) {
        source.prependAll(collection);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull MutableListEditor<E, C> insert(int index, E element) {
        source.insert(index, element);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull MutableListEditor<E, C> insertAll(int index, @NotNull Iterable<? extends E> values) {
        source.insertAll(index, values);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull MutableListEditor<E, C> insertAll(int index, E @NotNull [] values) {
        source.insertAll(index, values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableListEditor<E, C> removeAt(int index) {
        source.removeAt(index);
        return this;
    }

    @Contract("-> this")
    public @NotNull MutableListEditor<E, C> removeFirst() {
        source.removeFirst();
        return this;
    }

    @Contract("-> this")
    public @NotNull MutableListEditor<E, C> clear() {
        source.clear();
        return this;
    }

    @Contract("_ -> this")
    public MutableListEditor<E, C> retainAll(@NotNull Predicate<? super E> predicate) {
        source.retainAll(predicate);
        return this;
    }

    @Contract("_ -> this")
    public MutableListEditor<E, C> removeAll(@NotNull Predicate<? super E> predicate) {
        source.removeAll(predicate);
        return this;
    }

    @Contract("_ -> this")
    public MutableListEditor<E, C> dropInPlace(int n) {
        source.dropInPlace(n);
        return this;
    }

    @Contract("_ -> this")
    public MutableListEditor<E, C> takeInPlace(int n) {
        source.takeInPlace(n);
        return this;
    }

    //region MutableSeqEditor members

    @Override
    @Contract("_, _ -> this")
    public @NotNull MutableListEditor<E, C> set(int index, E newValue) {
        source.set(index, newValue);
        return this;
    }

    @Override
    @Contract("_, _ -> this")
    public @NotNull MutableListEditor<E, C> swap(int index1, int index2) {
        source.swap(index1, index2);
        return this;
    }


    @Override
    @Contract("_ -> this")
    public @NotNull MutableListEditor<E, C> replaceAll(@NotNull Function<? super E, ? extends E> mapper) {
        source.replaceAll(mapper);
        return this;
    }

    @Override
    @Contract("-> this")
    public @NotNull MutableListEditor<E, C> sort() {
        source.sort();
        return this;
    }

    @Override
    @Contract("_ -> this")
    public @NotNull MutableListEditor<E, C> sort(@NotNull Comparator<? super E> comparator) {
        source.sort(comparator);
        return this;
    }

    //endregion
}
