package org.glavo.kala.collection.base;

import org.glavo.kala.Tuple2;
import org.glavo.kala.annotations.DeprecatedReplaceWith;
import org.glavo.kala.control.OptionAny;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.PrimitiveIterator;

public interface PrimIterator<
        T,
        T_ITERATOR extends PrimIterator<T, T_ITERATOR, T_ARRAY, T_OPTION, T_CONSUMER, T_PREDICATE>,
        T_ARRAY,
        T_OPTION extends OptionAny<T>,
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

    //region Size Info

    int size();

    //endregion

    @NotNull T_OPTION find(@NotNull T_PREDICATE predicate);

    //region Element Conditions

    boolean contains(Object value);

    boolean containsAll(@NotNull T_ARRAY values);

    default boolean containsAll(T @NotNull [] values) {
        for (T value : values) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    default boolean containsAll(@NotNull Iterable<?> values) {
        for (Object value : values) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    boolean sameElements(@NotNull T_ITERATOR other);

    boolean sameElements(@NotNull Iterator<?> other);

    boolean anyMatch(@NotNull T_PREDICATE predicate);

    boolean allMatch(@NotNull T_PREDICATE predicate);

    boolean noneMatch(@NotNull T_PREDICATE predicate);

    //endregion

    //region Misc Operations

    @NotNull T_ITERATOR drop(int n);

    @NotNull T_ITERATOR dropWhile(@NotNull T_PREDICATE predicate);

    @NotNull T_ITERATOR take(int n);

    @NotNull T_ITERATOR takeWhile(@NotNull T_PREDICATE predicate);

    @NotNull T_ITERATOR filter(@NotNull T_PREDICATE predicate);

    @NotNull T_ITERATOR filterNot(@NotNull T_PREDICATE predicate);

    @NotNull Tuple2<@NotNull T_ITERATOR, @NotNull T_ITERATOR> span(@NotNull T_PREDICATE predicate);

    //endregion

    //region Aggregate Operations

    int count(@NotNull T_PREDICATE predicate);

    @Nullable T maxOrNull();

    @NotNull T_OPTION maxOption();

    @Nullable T minOrNull();

    @NotNull T_OPTION minOption();

    //endregion

    @NotNull T_ARRAY toArray();

    //region Traverse Operations

    void forEach(@NotNull T_CONSUMER action);

    @Override
    @DeprecatedReplaceWith("forEach(action)")
    default void forEachRemaining(@NotNull T_CONSUMER action) {
        forEach(action);
    }

    //endregion

    //region String Representation

    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer) {
        return joinTo(buffer, ", ");
    }

    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator) {
        return joinTo(buffer, separator, "", "");
    }

    @Contract(value = "_, _, _, _ -> param1", mutates = "param1")
    <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    );

    default @NotNull String joinToString() {
        return joinTo(new StringBuilder()).toString();
    }

    default @NotNull String joinToString(CharSequence separator) {
        return joinTo(new StringBuilder(), separator).toString();
    }

    default @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return joinTo(new StringBuilder(), separator, prefix, postfix).toString();
    }

    //endregion
}
