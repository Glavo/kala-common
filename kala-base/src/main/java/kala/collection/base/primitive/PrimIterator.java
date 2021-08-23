package kala.collection.base.primitive;

import kala.annotations.ReplaceWith;
import kala.control.AnyOption;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.PrimitiveIterator;

public interface PrimIterator<
        T,
        T_ITERATOR extends PrimIterator<T, T_ITERATOR, T_ARRAY, T_OPTION, T_CONSUMER, T_PREDICATE>,
        T_ARRAY,
        T_OPTION extends AnyOption<T>,
        T_CONSUMER,
        T_PREDICATE> extends PrimitiveIterator<T, T_CONSUMER> {

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasNext();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull T next();

    void nextIgnoreResult();

    //region Size Info

    default boolean isEmpty() {
        return !hasNext();
    }

    @Contract(mutates = "this")
    int size();

    default int knownSize() {
        return hasNext() ? -1 : 0;
    }

    //endregion

    @NotNull T_OPTION find(@NotNull T_PREDICATE predicate);

    //region Element Conditions

    @Contract(mutates = "this")
    boolean contains(Object value);

    @Contract(mutates = "this")
    boolean containsAll(@NotNull T_ARRAY values);

    @Contract(mutates = "this")
    default boolean containsAll(T @NotNull [] values) {
        for (T value : values) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    @Contract(mutates = "this")
    default boolean containsAll(@NotNull Iterable<?> values) {
        for (Object value : values) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    @Contract(mutates = "this")
    boolean sameElements(@NotNull T_ITERATOR other);

    @Contract(mutates = "this")
    boolean sameElements(@NotNull Iterator<?> other);

    @Contract(mutates = "this")
    boolean anyMatch(@NotNull T_PREDICATE predicate);

    @Contract(mutates = "this")
    boolean allMatch(@NotNull T_PREDICATE predicate);

    @Contract(mutates = "this")
    boolean noneMatch(@NotNull T_PREDICATE predicate);

    //endregion

    //region Misc Operations

    @Contract(mutates = "this")
    @NotNull T_ITERATOR drop(int n);

    @Contract(mutates = "this")
    @NotNull T_ITERATOR dropWhile(@NotNull T_PREDICATE predicate);

    @Contract(mutates = "this")
    @NotNull T_ITERATOR take(int n);

    @Contract(mutates = "this")
    @NotNull T_ITERATOR takeWhile(@NotNull T_PREDICATE predicate);

    @Contract(mutates = "this")
    @NotNull T_ITERATOR filter(@NotNull T_PREDICATE predicate);

    @Contract(mutates = "this")
    @NotNull T_ITERATOR filterNot(@NotNull T_PREDICATE predicate);

    @Contract(mutates = "this")
    @NotNull Tuple2<@NotNull T_ITERATOR, @NotNull T_ITERATOR> span(@NotNull T_PREDICATE predicate);

    //endregion

    //region Aggregate Operations

    @Contract(mutates = "this")
    int count(@NotNull T_PREDICATE predicate);

    @Contract(mutates = "this")
    @Nullable T maxOrNull();

    @Contract(mutates = "this")
    @NotNull T_OPTION maxOption();

    @Contract(mutates = "this")
    @Nullable T minOrNull();

    @Contract(mutates = "this")
    @NotNull T_OPTION minOption();

    //endregion

    @Contract(mutates = "this")
    @NotNull T_ARRAY toArray();

    //region Traverse Operations

    @Contract(mutates = "this")
    void forEach(@NotNull T_CONSUMER action);

    @Override
    @ReplaceWith("forEach(action)")
    default void forEachRemaining(@NotNull T_CONSUMER action) {
        forEach(action);
    }

    //endregion

    //region String Representation

    @Contract(mutates = "this, param1")
    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer) {
        return joinTo(buffer, ", ");
    }

    @Contract(mutates = "this, param1")
    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator) {
        return joinTo(buffer, separator, "", "");
    }

    @Contract(value = "_, _, _, _ -> param1", mutates = "this, param1")
    <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    );

    @Contract(mutates = "this")
    default @NotNull String joinToString() {
        return joinTo(new StringBuilder()).toString();
    }

    @Contract(mutates = "this")
    default @NotNull String joinToString(CharSequence separator) {
        return joinTo(new StringBuilder(), separator).toString();
    }

    @Contract(mutates = "this")
    default @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return joinTo(new StringBuilder(), separator, prefix, postfix).toString();
    }

    //endregion
}
