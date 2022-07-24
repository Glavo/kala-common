package kala.value;

import kala.annotations.UnstableName;
import kala.collection.base.Iterators;
import kala.collection.base.Mappable;
import kala.collection.base.Traversable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Value<T> extends AnyValue<T>, Traversable<T>, Mappable<T>, Supplier<T> {

    static <T> @NotNull Value<T> of(T value) {
        return new DefaultValue<>(value);
    }

    @UnstableName
    static <T> @NotNull Value<T> by(@NotNull Supplier<? extends T> getter) {
        Objects.requireNonNull(getter);
        return new DelegateValue<>(getter);
    }

    T get();

    @Override
    default T getValue() {
        return get();
    }

    default <U> @NotNull Value<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return Value.by(() -> mapper.apply(get()));
    }

    default @NotNull MutableValue<T> asMutable(@NotNull Consumer<? super T> setter) {
        Objects.requireNonNull(setter);
        return new DelegateMutableValue<>(this, setter);
    }

    // Traversable

    @Override
    default @NotNull Iterator<T> iterator() {
        return Iterators.of(get());
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default int size() {
        return 1;
    }

    @Override
    default int knownSize() {
        return 1;
    }

    @Override
    default void forEach(@NotNull Consumer<? super T> action) {
        action.accept(get());
    }
}
