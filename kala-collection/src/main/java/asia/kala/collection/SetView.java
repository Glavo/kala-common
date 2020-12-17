package asia.kala.collection;

import asia.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public interface SetView<@Covariant E> extends View<E>, Set<E> {

    @Override
    default String className() {
        return "SetView";
    }

    @Override
    @Contract(value = "-> this", pure = true)
    default @NotNull SetView<E> view() {
        return this;
    }

    @Override
    default @NotNull SetView<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SetViews.Filter<>(this, predicate);
    }

    @Override
    default @NotNull SetView<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SetViews.Filter<>(this, predicate.negate());
    }

    @Override
    default boolean canEqual(Object other) {
        return other instanceof SetView<?>;
    }
}
