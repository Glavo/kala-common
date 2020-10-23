package asia.kala.collection.immutable;

import asia.kala.collection.IndexedSeq;
import asia.kala.iterator.Iterators;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;

abstract class ImmutableSeqN<E> extends AbstractImmutableSeq<E> implements IndexedSeq<E>, Serializable {
    @Override
    public abstract int size();

    @Override
    public abstract E get(int index);

    @Override
    public final String className() {
        return "ImmutableSeq";
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }

        ImmutableSeqN<?> other = (ImmutableSeqN<?>) obj;

        return sizeEquals(other) && this.sameElements(other);
    }
}

final class ImmutableSeq0<E> extends ImmutableSeqN<E> {
    private static final long serialVersionUID = 0L;

    private static final ImmutableSeq0<?> INSTANCE = new ImmutableSeq0<>();

    private ImmutableSeq0() {
    }

    @SuppressWarnings("unchecked")
    static <E> ImmutableSeq0<E> instance() {
        return (ImmutableSeq0<E>) INSTANCE;
    }

    @Override
    public final int size() {
        return 0;
    }

    @Override
    public final E get(int index) {
        throw new IndexOutOfBoundsException("Index out of range: " + index);
    }

    @NotNull
    @Override
    public final ImmutableSeq<E> appended(E element) {
        return new ImmutableSeq1<>(element);
    }

    @NotNull
    @Override
    public final ImmutableSeq<E> prepended(E element) {
        return new ImmutableSeq1<>(element);
    }

    @NotNull
    @Override
    public final Iterator<E> iterator() {
        return Iterators.empty();
    }

    @NotNull
    @Override
    public final Spliterator<E> spliterator() {
        return Spliterators.emptySpliterator();
    }

    @NotNull
    @Override
    public final Stream<E> stream() {
        return Stream.empty();
    }

    @NotNull
    @Override
    public final <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return instance();
    }

    //
    // -- Serializable
    //

    private Object readResolve() {
        return INSTANCE;
    }
}

final class ImmutableSeq1<E> extends ImmutableSeqN<E> {
    private static final long serialVersionUID = 0L;

    private final E $0;

    ImmutableSeq1(E $0) {
        this.$0 = $0;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public final E get(int index) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (index) {
            case 0:
                return $0;
        }
        throw new IndexOutOfBoundsException("Index out of range: " + index);
    }

    @NotNull
    @Override
    public final ImmutableSeq<E> appended(E element) {
        return new ImmutableSeq2<>($0, element);
    }

    @NotNull
    @Override
    public final ImmutableSeq<E> prepended(E element) {
        return new ImmutableSeq2<>(element, $0);
    }

    @NotNull
    @Override
    public final <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new ImmutableSeq1<>(mapper.apply($0));
    }

    @NotNull
    @Override
    public final Stream<E> stream() {
        return Stream.of($0);
    }
}

final class ImmutableSeq2<E> extends ImmutableSeqN<E> {
    private static final long serialVersionUID = 0L;

    private final E $0;
    private final E $1;

    ImmutableSeq2(E $0, E $1) {
        this.$0 = $0;
        this.$1 = $1;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public final E get(int index) {
        switch (index) {
            case 0:
                return $0;
            case 1:
                return $1;
        }
        throw new IndexOutOfBoundsException("Index out of range: " + index);
    }

    @NotNull
    @Override
    public final ImmutableSeq<E> appended(E element) {
        return new ImmutableSeq3<>($0, $1, element);
    }

    @NotNull
    @Override
    public final ImmutableSeq<E> prepended(E element) {
        return new ImmutableSeq3<>(element, $0, $1);
    }


    @NotNull
    @Override
    public final <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return new ImmutableSeq2<>(mapper.apply($0), mapper.apply($1));
    }
}

final class ImmutableSeq3<E> extends ImmutableSeqN<E> {
    private static final long serialVersionUID = 0L;

    private final E $0;
    private final E $1;
    private final E $2;

    ImmutableSeq3(E $0, E $1, E $2) {
        this.$0 = $0;
        this.$1 = $1;
        this.$2 = $2;
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public final E get(int index) {
        switch (index) {
            case 0:
                return $0;
            case 1:
                return $1;
            case 2:
                return $2;
        }
        throw new IndexOutOfBoundsException("Index out of range: " + index);
    }

    @NotNull
    @Override
    public final ImmutableSeq<E> appended(E element) {
        return new ImmutableSeq4<>($0, $1, $2, element);
    }

    @NotNull
    @Override
    public final ImmutableSeq<E> prepended(E element) {
        return new ImmutableSeq4<>(element, $0, $1, $2);
    }


    @NotNull
    @Override
    public final <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return new ImmutableSeq3<>(mapper.apply($0), mapper.apply($1), mapper.apply($2));
    }
}

final class ImmutableSeq4<E> extends ImmutableSeqN<E> {
    private static final long serialVersionUID = 0L;

    private final E $0;
    private final E $1;
    private final E $2;
    private final E $3;

    ImmutableSeq4(E $0, E $1, E $2, E $3) {
        this.$0 = $0;
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public final E get(int index) {
        switch (index) {
            case 0:
                return $0;
            case 1:
                return $1;
            case 2:
                return $2;
            case 3:
                return $3;
        }
        throw new IndexOutOfBoundsException("Index out of range: " + index);
    }

    @NotNull
    @Override
    public final ImmutableSeq<E> appended(E element) {
        return new ImmutableSeq5<>($0, $1, $2, $3, element);
    }

    @NotNull
    @Override
    public final ImmutableSeq<E> prepended(E element) {
        return new ImmutableSeq5<>(element, $0, $1, $2, $3);
    }

    @NotNull
    @Override
    public final <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return new ImmutableSeq4<>(mapper.apply($0), mapper.apply($1), mapper.apply($2), mapper.apply($3));
    }
}

final class ImmutableSeq5<E> extends ImmutableSeqN<E> {
    private static final long serialVersionUID = 0L;

    private final E $0;
    private final E $1;
    private final E $2;
    private final E $3;
    private final E $4;

    ImmutableSeq5(E $0, E $1, E $2, E $3, E $4) {
        this.$0 = $0;
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
    }

    @Override
    public int size() {
        return 5;
    }

    @Override
    public final E get(int index) {
        switch (index) {
            case 0:
                return $0;
            case 1:
                return $1;
            case 2:
                return $2;
            case 3:
                return $3;
            case 4:
                return $4;
        }
        throw new IndexOutOfBoundsException("Index out of range: " + index);
    }

    @NotNull
    @Override
    public final <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return new ImmutableSeq5<>(mapper.apply($0), mapper.apply($1), mapper.apply($2), mapper.apply($3), mapper.apply($4));
    }
}
