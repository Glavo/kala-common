package asia.kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class MutableSetEditor<E, C extends MutableSet<E>> extends MutableCollectionEditor<E, C> {
    public MutableSetEditor(@NotNull C source) {
        super(source);
    }

    @NotNull
    @Contract("_ -> this")
    public MutableSetEditor<E, C> add(E value) {
        source.add(value);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public MutableSetEditor<E, C> addAll(@NotNull Iterable<? extends E> values) {
        source.addAll(values);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public MutableSetEditor<E, C> addAll(E @NotNull [] values) {
        source.addAll(values);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public MutableSetEditor<E, C> remove(E value) {
        source.remove(value);
        return this;
    }

    @NotNull
    @Contract("-> this")
    public MutableSetEditor<E, C> clear() {
        source.clear();
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public MutableSetEditor<E, C> retainIf(@NotNull Predicate<? super E> predicate) {
        source.retainIf(predicate);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public MutableSetEditor<E, C> retainAll(@NotNull Iterable<? super E> values) {
        source.retainAll(values);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public MutableSetEditor<E, C> retainAll(E @NotNull [] values) {
        source.retainAll(values);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public MutableSetEditor<E, C> filterInPlace(@NotNull Predicate<? super E> predicate) {
        source.filterInPlace(predicate);
        return this;
    }
}
