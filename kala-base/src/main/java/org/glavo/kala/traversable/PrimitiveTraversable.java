package org.glavo.kala.traversable;

import org.glavo.kala.annotations.DeprecatedReplaceWith;
import org.glavo.kala.control.OptionAny;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface PrimitiveTraversable<
        T,
        T_TRAVERSABLE extends PrimitiveTraversable<T, T_TRAVERSABLE, T_ITERATOR, T_ARRAY, T_OPTION, T_CONSUMER, T_PREDICATE>,
        T_ITERATOR extends PrimIterator<T, T_ITERATOR, T_ARRAY, T_OPTION, T_CONSUMER, T_PREDICATE>,
        T_ARRAY,
        T_OPTION extends OptionAny<T>,
        T_CONSUMER,
        T_PREDICATE
        > extends AnyTraversable<T, T_ITERATOR, T_ARRAY, T_OPTION, T_CONSUMER, T_PREDICATE> {

    //region Size Info

    /**
     * {@inheritDoc}
     */
    @Override
    default int size() {
        return iterator().size();
    }

    //endregion

    //region Element Conditions

    boolean containsAll(@NotNull T_TRAVERSABLE values);

    default boolean containsAll(@NotNull Iterable<?> values) {
        return iterator().containsAll(values);
    }

    default boolean sameElements(@NotNull T_TRAVERSABLE other) {
        return iterator().sameElements(other.iterator());
    }

    @Override
    default boolean sameElements(@NotNull Iterable<?> other) {
        return iterator().sameElements(other.iterator());
    }

    default boolean anyMatch(@NotNull T_PREDICATE predicate) {
        return knownSize() != 0 && iterator().anyMatch(predicate);
    }

    default boolean allMatch(@NotNull T_PREDICATE predicate) {
        return knownSize() == 0 || iterator().allMatch(predicate);
    }

    default boolean noneMatch(@NotNull T_PREDICATE predicate) {
        return knownSize() == 0 || iterator().noneMatch(predicate);
    }

    default boolean containsAll(@NotNull T_ARRAY values) {
        return iterator().containsAll(values);
    }

    //endregion

    //region Search Operations

    @Override
    default @NotNull T_OPTION find(@NotNull T_PREDICATE predicate) {
        return iterator().find(predicate);
    }

    //endregion

    //region Aggregate Operations

    default int count(@NotNull T_PREDICATE predicate) {
        return knownSize() == 0 ? 0 : iterator().count(predicate);
    }

    default @Nullable T maxOrNull() {
        return knownSize() == 0 ? null : iterator().maxOrNull();
    }

    default @NotNull T_OPTION maxOption() {
        return iterator().maxOption();
    }

    default @Nullable T minOrNull() {
        return knownSize() == 0 ? null : iterator().minOrNull();
    }

    default @NotNull T_OPTION minOption() {
        return iterator().minOption();
    }

    //endregion

    //region Conversion Operations

    @NotNull T_ARRAY toArray();

    //endregion

    //region Traverse Operations

    /**
     * @see #forEachPrimitive(Object)
     */
    @Override
    @Deprecated
    @DeprecatedReplaceWith("forEachPrimitive(action::accept)")
    void forEach(@NotNull Consumer<? super T> action);

    void forEachPrimitive(@NotNull T_CONSUMER action);

    //endregion

    //region String Representation

    @Override
    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return iterator().joinTo(buffer, separator, prefix, postfix);
    }

    //endregion
}
