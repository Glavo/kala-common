package asia.kala.collection;

import asia.kala.Tuple;
import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import asia.kala.collection.internal.CollectionHelper;
import asia.kala.comparator.Comparators;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public interface SeqView<@Covariant E> extends Seq<E>, View<E> {

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> SeqView<E> narrow(SeqView<? extends E> view) {
        return (SeqView<E>) view;
    }

    //endregion

    @NotNull
    default SeqView<E> updated(int index, E newValue) {
        return new SeqViews.Updated<>(this, index, newValue);
    }

    @NotNull
    default SeqView<E> drop(int n) {
        return new SeqViews.Drop<>(this, n);
    }

    @NotNull
    default SeqView<E> dropLast(int n) {
        return new SeqViews.DropLast<>(this, n);
    }

    @NotNull
    default SeqView<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        return new SeqViews.DropWhile<>(this, predicate);
    }

    @NotNull
    default SeqView<E> take(int n) {
        return new SeqViews.Take<>(this, n);
    }

    @NotNull
    default SeqView<E> takeLast(int n) {
        return new SeqViews.TakeLast<>(this, n);
    }

    @NotNull
    default SeqView<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        return new SeqViews.TakeWhile<>(this, predicate);
    }

    @NotNull
    default SeqView<E> concat(@NotNull Seq<? extends E> other) {
        Objects.requireNonNull(other);

        return new SeqViews.Concat<>(this, narrow(other.view()));
    }

    @NotNull
    default SeqView<E> prepended(E value) {
        return new SeqViews.Prepended<>(this, value);
    }

    @NotNull
    default SeqView<E> prependedAll(@NotNull Iterable<? extends E> prefix) {
        Objects.requireNonNull(prefix);
        return new SeqViews.Concat<>(CollectionHelper.asSeq(prefix), this);
    }

    @NotNull
    default SeqView<E> prependedAll(E @NotNull [] prefix) {
        Objects.requireNonNull(prefix);
        return new SeqViews.Concat<>(ArraySeq.wrap(prefix), this);
    }

    @NotNull
    default SeqView<E> appended(E value) {
        return new SeqViews.Appended<>(this, value);
    }

    @NotNull
    default SeqView<E> appendedAll(@NotNull Iterable<? extends E> postfix) {
        Objects.requireNonNull(postfix);
        return new SeqViews.Concat<>(this, CollectionHelper.asSeq(postfix));
    }

    @NotNull
    default SeqView<E> appendedAll(E @NotNull [] postfix) {
        Objects.requireNonNull(postfix);
        return new SeqViews.Concat<>(this, ArraySeq.wrap(postfix));
    }

    @NotNull
    default SeqView<E> sorted() {
        return sorted(Comparators.naturalOrder());
    }

    @NotNull
    default SeqView<E> sorted(@NotNull Comparator<? super E> comparator) {
        Objects.requireNonNull(comparator);

        return new SeqViews.Sorted<>(this, comparator);
    }

    @NotNull
    default SeqView<E> reversed() {
        return new SeqViews.Reversed<>(this);
    }

    //region View members

    @NotNull
    @Override
    @Contract(value = "-> this", pure = true)
    default SeqView<E> view() {
        return this;
    }

    @Override
    default String className() {
        return "SeqView";
    }

    @Override
    default boolean canEqual(Object other) {
        return other instanceof SeqView<?>;
    }

    @NotNull
    @Override
    default <U> SeqView<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        return new SeqViews.Mapped<>(this, mapper);
    }

    @Override
    @NotNull
    default SeqView<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SeqViews.Filter<>(this, predicate);
    }

    @Override
    @NotNull
    default SeqView<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SeqViews.Filter<>(this, predicate.negate());
    }

    @Override
    @NotNull
    default <U> SeqView<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        return new SeqViews.FlatMapped<>(this, mapper);
    }

    @NotNull
    @Override
    default Tuple2<? extends SeqView<E>, ? extends SeqView<E>> span(@NotNull Predicate<? super E> predicate) {
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
    }

    //endregion
}
