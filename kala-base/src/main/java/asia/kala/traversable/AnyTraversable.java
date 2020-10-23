package asia.kala.traversable;

import asia.kala.control.OptionAny;
import asia.kala.iterator.Iterators;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public interface AnyTraversable<
        T,
        T_ITERATOR extends Iterator<T>,
        T_ARRAY,
        T_OPTION extends OptionAny<T>,
        T_CONSUMER,
        T_PREDICATE
        > extends Iterable<T> {

    static int knownSize(@NotNull Iterable<?> c) {
        Objects.requireNonNull(c);
        if (c instanceof Collection<?>) {
            return ((Collection<?>) c).size();
        }
        if (c instanceof AnyTraversable<?, ?, ?, ?, ?, ?>) {
            return ((AnyTraversable<?, ?, ?, ?, ?, ?>) c).knownSize();
        }
        return -1;
    }

    @NotNull
    @Override
    T_ITERATOR iterator();

    default boolean isEmpty() {
        final int size = knownSize();
        if (size < 0) {
            return !iterator().hasNext();
        } else {
            return size == 0;
        }
    }

    /**
     * Returns the size of this {@code AnyTraversable} if is a finite structure.
     *
     * @return the number of elements in this {@code AnyTraversable}
     */
    default int size() {
        return Iterators.size(iterator());
    }

    @Range(from = -1, to = Integer.MAX_VALUE)
    default int knownSize() {
        return -1;
    }

    //region Size compare operators

    default int sizeCompare(int otherSize) {
        if (otherSize < 0) {
            return 1;
        }
        final int knownSize = knownSize();
        if (knownSize >= 0) {
            return Integer.compare(knownSize, otherSize);
        }
        int i = 0;
        for (T t : this) {
            if (i == otherSize) {
                return 1;
            }
            ++i;
        }
        return i - otherSize;
    }

    default int sizeCompare(@NotNull Iterable<?> other) {
        final int os = knownSize(other);

        if (os >= 0) {
            return sizeCompare(os);
        }
        int ks = this.knownSize();
        if (ks == 0) {
            return other.iterator().hasNext() ? -1 : 0;
        } else if (ks > 0) {
            Iterator<?> it = other.iterator();
            while (it.hasNext()) {
                it.next();
                --ks;
                if (ks == 0) {
                    return it.hasNext() ? -1 : 0;
                }
            }
            return 1;
        }

        Iterator<?> it1 = this.iterator();
        Iterator<?> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            it1.next();
            it2.next();
        }

        if (it1.hasNext()) {
            return 1;
        }
        if (it2.hasNext()) {
            return -1;
        }
        return 0;
    }

    default boolean sizeIs(int otherSize) {
        return sizeCompare(otherSize) == 0;
    }

    default boolean sizeEquals(int otherSize) {
        return sizeIs(otherSize);
    }

    default boolean sizeEquals(@NotNull Iterable<?> other) {
        return sizeCompare(other) == 0;
    }

    default boolean sizeLessThan(int otherSize) {
        return sizeCompare(otherSize) < 0;
    }

    default boolean sizeLessThan(@NotNull Iterable<?> other) {
        return sizeCompare(other) < 0;
    }

    default boolean sizeLessThanOrEquals(int otherSize) {
        return sizeCompare(otherSize) <= 0;
    }

    default boolean sizeLessThanOrEquals(@NotNull Iterable<?> other) {
        return sizeCompare(other) <= 0;
    }

    default boolean sizeGreaterThan(int otherSize) {
        return sizeCompare(otherSize) > 0;
    }

    default boolean sizeGreaterThan(@NotNull Iterable<?> other) {
        return sizeCompare(other) > 0;
    }

    default boolean sizeGreaterThanOrEquals(int otherSize) {
        return sizeCompare(otherSize) >= 0;
    }

    default boolean sizeGreaterThanOrEquals(@NotNull Iterable<?> other) {
        return sizeCompare(other) >= 0;
    }

    //endregion

    boolean containsAll(@NotNull T_ARRAY values);

    boolean containsAll(@NotNull Iterable<?> values);

    boolean sameElements(@NotNull Iterable<?> other);

    boolean anyMatch(@NotNull T_PREDICATE predicate);

    boolean allMatch(@NotNull T_PREDICATE predicate);

    boolean noneMatch(@NotNull T_PREDICATE predicate);

    int count(@NotNull T_PREDICATE predicate);

    @NotNull
    T_OPTION find(@NotNull T_PREDICATE predicate);

    @Nullable
    T maxOrNull();

    @NotNull
    T_OPTION maxOption();

    @Nullable
    T minOrNull();

    @NotNull
    T_OPTION minOption();

    @NotNull
    T_ARRAY toArray();

    @NotNull
    default <A extends Appendable> A joinTo(@NotNull A buffer) {
        return joinTo(buffer, ", ");
    }

    @NotNull
    default <A extends Appendable> A joinTo(@NotNull A buffer, CharSequence separator) {
        return joinTo(buffer, separator, "", "");
    }

    @NotNull
    @Contract(value = "_, _, _, _ -> param1", mutates = "param1")
    <A extends Appendable> A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    );

    @NotNull
    default String joinToString() {
        return joinTo(new StringBuilder()).toString();
    }

    @NotNull
    default String joinToString(CharSequence separator) {
        return joinTo(new StringBuilder(), separator).toString();
    }

    @NotNull
    default String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return joinTo(new StringBuilder(), separator, prefix, postfix).toString();
    }
}
