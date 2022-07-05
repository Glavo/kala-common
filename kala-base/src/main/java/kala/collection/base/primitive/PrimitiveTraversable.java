package kala.collection.base.primitive;

import kala.collection.base.AnyTraversable;
import kala.collection.base.Traversable;
import kala.control.AnyOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PrimitiveTraversable<T> extends AnyTraversable<T> {

    default @NotNull Traversable<T> asGeneric() {
        return new AsGenericTraversable<>(this);
    }

    @Override
    @NotNull PrimitiveIterator<T, ?> iterator();

    //region Size Info

    /**
     * {@inheritDoc}
     */
    @Override
    default int size() {
        return iterator().size();
    }

    //endregion

    //region Aggregate Operations

    @Nullable T maxOrNull();

    @NotNull AnyOption<T> maxOption();

    @Nullable T minOrNull();

    @NotNull AnyOption<T> minOption();

    //endregion

    //region String Representation

    @Override
    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return iterator().joinTo(buffer, separator, prefix, postfix);
    }

    //endregion
}
