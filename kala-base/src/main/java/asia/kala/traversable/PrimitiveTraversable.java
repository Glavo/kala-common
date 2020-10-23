package asia.kala.traversable;

import asia.kala.control.OptionAny;
import asia.kala.iterator.PrimIterator;
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

    /**
     * {@inheritDoc}
     */
    @Override
    default int size() {
        return iterator().size();
    }

    default boolean containsAll(@NotNull T_ARRAY values) {
        return iterator().containsAll(values);
    }

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

    default int count(@NotNull T_PREDICATE predicate) {
        return knownSize() == 0 ? 0 : iterator().count(predicate);
    }

    @NotNull
    @Override
    default T_OPTION find(@NotNull T_PREDICATE predicate) {
        return iterator().find(predicate);
    }

    @Nullable
    default T maxOrNull() {
        return knownSize() == 0 ? null : iterator().maxOrNull();
    }

    @NotNull
    default T_OPTION maxOption() {
        return iterator().maxOption();
    }

    @Nullable
    default T minOrNull() {
        return knownSize() == 0 ? null : iterator().minOrNull();
    }

    @NotNull
    default T_OPTION minOption() {
        return iterator().minOption();
    }

    @NotNull
    T_ARRAY toArray();

    @Override
    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return iterator().joinTo(buffer, separator, prefix, postfix);
    }

    void forEachPrimitive(@NotNull T_CONSUMER action);

    /**
     * @see #forEachPrimitive(Object)
     */
    @Override
    @Deprecated
    void forEach(@NotNull Consumer<? super T> action);
}
