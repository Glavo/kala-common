package asia.kala.collection;

import asia.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public interface SetView<@Covariant E> extends View<E>, Set<E> {

    //region View members

    @NotNull
    @Override
    @Contract(value = "-> this", pure = true)
    default SetView<E> view() {
        return this;
    }

    @Override
    default String className() {
        return "SetView";
    }

    @Override
    default boolean canEqual(Object other) {
        return other instanceof SetView<?>;
    }

    @NotNull
    @Override
    default SetView<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SetViews.Filter<>(this, predicate);
    }

    @NotNull
    @Override
    default SetView<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SetViews.Filter<>(this, predicate.negate());
    }

    //endregion
}
