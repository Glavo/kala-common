package kala.collection.base.primitive;

import kala.annotations.ReplaceWith;
import kala.control.AnyOption;
import kala.control.primitive.CharOption;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface PrimitiveIterator<T, T_CONSUMER> extends java.util.PrimitiveIterator<T, T_CONSUMER> {

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

    /**
     * Equivalent to calling {@link #next()} and ignoring the return value.
     * <p>
     * Useful when you only need to count the number of Iterator elements and don't care about the value.
     * The difference between this method and {@link #next()} is that boxing the return value can be avoided.
     */
    void nextIgnoreResult();

    //region Size Info

    default boolean isEmpty() {
        return !hasNext();
    }

    @Contract(mutates = "this")
    default int size() {
        int i = 0;
        while (hasNext()) {
            nextIgnoreResult();
            ++i;
        }
        return i;
    }

    default int knownSize() {
        return hasNext() ? -1 : 0;
    }

    //endregion

    //region Element Conditions

    @Contract(mutates = "this")
    boolean contains(Object value);

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

    boolean sameElements(@NotNull Iterator<?> other);

    //endregion

    //region Misc Operations

    @NotNull PrimitiveIterator<T, T_CONSUMER> drop(int n);

    @NotNull PrimitiveIterator<T, T_CONSUMER> take(int n);

    //endregion

    //region Aggregate Operations

    @Nullable T maxOrNull();

    @NotNull AnyOption<T> maxOption();

    @Nullable T minOrNull();

    @NotNull AnyOption<T> minOption();

    //endregion

    @Contract(mutates = "this")
    @NotNull Object toArray();

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
