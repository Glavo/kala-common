package kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public class DynamicSeqEditor<E, C extends DynamicSeq<E>> extends MutableSeqEditor<E, C> {
    protected DynamicSeqEditor(@NotNull C source) {
        super(source);
    }

    @Contract(value = "_ -> this")
    public @NotNull DynamicSeqEditor<E, C> append(E value) {
        source.append(value);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull DynamicSeqEditor<E, C> appendAll(@NotNull Iterable<? extends E> collection) {
        source.appendAll(collection);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull DynamicSeqEditor<E, C> appendAll(E @NotNull [] collection) {
        source.appendAll(collection);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull DynamicSeqEditor<E, C> prepend(E value) {
        source.prepend(value);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull DynamicSeqEditor<E, C> prependAll(@NotNull Iterable<? extends E> collection) {
        source.prependAll(collection);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull DynamicSeqEditor<E, C> prependAll(E @NotNull [] collection) {
        source.prependAll(collection);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull DynamicSeqEditor<E, C> insert(int index, E element) {
        source.insert(index, element);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull DynamicSeqEditor<E, C> insertAll(int index, @NotNull Iterable<? extends E> values) {
        source.insertAll(index, values);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull DynamicSeqEditor<E, C> insertAll(int index, E @NotNull [] values) {
        source.insertAll(index, values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull DynamicSeqEditor<E, C> removeAt(int index) {
        source.removeAt(index);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull DynamicSeqEditor<E, C> removeAt(int index, int count) {
        source.removeAt(index, count);
        return this;
    }

    @Contract("-> this")
    public @NotNull DynamicSeqEditor<E, C> clear() {
        source.clear();
        return this;
    }

    @Contract("_ -> this")
    public @NotNull DynamicSeqEditor<E, C> dropInPlace(int n) {
        source.dropInPlace(n);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull DynamicSeqEditor<E, C> dropWhileInPlace(@NotNull Predicate<? super E> predicate) {
        source.dropWhileInPlace(predicate);
        return this;
    }

    @Contract("_ -> this")
    public DynamicSeqEditor<E, C> takeInPlace(int n) {
        source.takeInPlace(n);
        return this;
    }

    @Contract("_ -> this")
    public DynamicSeqEditor<E, C> takeWhileInPlace(@NotNull Predicate<? super E> predicate) {
        source.takeWhileInPlace(predicate);
        return this;
    }

    @Contract("_ -> this")
    public DynamicSeqEditor<E, C> filterInPlace(@NotNull Predicate<? super E> predicate) {
        source.filterInPlace(predicate);
        return this;
    }

    @Contract("_ -> this")
    public DynamicSeqEditor<E, C> filterNotInPlace(@NotNull Predicate<? super E> predicate) {
        source.filterNotInPlace(predicate);
        return this;
    }

    //region MutableSeqEditor members

    @Override
    @Contract("_, _ -> this")
    public @NotNull DynamicSeqEditor<E, C> set(int index, E newValue) {
        source.set(index, newValue);
        return this;
    }

    @Override
    @Contract("_, _ -> this")
    public @NotNull DynamicSeqEditor<E, C> swap(int index1, int index2) {
        source.swap(index1, index2);
        return this;
    }


    @Override
    @Contract("_ -> this")
    public @NotNull DynamicSeqEditor<E, C> replaceAll(@NotNull Function<? super E, ? extends E> mapper) {
        source.replaceAll(mapper);
        return this;
    }

    @Override
    @Contract("-> this")
    public @NotNull DynamicSeqEditor<E, C> sort() {
        source.sort();
        return this;
    }

    @Override
    @Contract("_ -> this")
    public @NotNull DynamicSeqEditor<E, C> sort(@NotNull Comparator<? super E> comparator) {
        source.sort(comparator);
        return this;
    }

    //endregion
}
