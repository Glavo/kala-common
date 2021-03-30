package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.base.AbstractIterator;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.internal.enums.SharedEnumConstants;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public abstract class MutableEnumSet<E extends Enum<E>> extends AbstractMutableSet<E> {
    protected final Class<E> enumType;
    protected final E[] constants;

    protected int size = 0;

    MutableEnumSet(Class<E> enumType) {
        this.enumType = enumType;
        this.constants = SharedEnumConstants.getConstants(enumType);
    }

    @Override
    public final @NotNull String className() {
        return "MutableEnumSet";
    }

    @Override
    public final boolean isEmpty() {
        return size == 0;
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final int knownSize() {
        return size;
    }

    static final class OfEmptyEnum<E extends Enum<E>> extends MutableEnumSet<E> {

        OfEmptyEnum(Class<E> enumType) {
            super(enumType);
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.empty();
        }

        @Override
        public final boolean add(E value) {
            Objects.requireNonNull(value);
            throw new ClassCastException();
        }

        @Override
        public final boolean remove(Object value) {
            return false;
        }

        @Override
        public final void clear() {
        }
    }

    static final class OfSmallEnum<E extends Enum<E>> extends MutableEnumSet<E> {
        private long elements = 0L;

        OfSmallEnum(Class<E> enumType) {
            super(enumType);
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            // TODO
        }

        static final class Itr<E extends Enum<E>> extends AbstractIterator<E> {
            private final E[] constants;
            private long elements;

            Itr(E[] constants, long elements) {
                this.constants = constants;
                this.elements = elements;
            }

            @Override
            public final boolean hasNext() {
                return elements != 0;
            }

            @Override
            public final E next() {
                if (elements == 0) {
                    throw new NoSuchElementException();
                }

                final long ordinal = elements & -elements;
                elements -= ordinal;
                return constants[Long.numberOfTrailingZeros(ordinal)];
            }
        }
    }
}
