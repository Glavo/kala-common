package kala.collection.mutable;

import kala.collection.base.AbstractIterator;
import kala.collection.base.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

@SuppressWarnings("unchecked")
public abstract class MutableEnumSet<E extends Enum<E>> extends AbstractMutableSet<E> {
    private static final ClassValue<Enum<?>[]> ENUM_CONSTANTS = new ClassValue<Enum<?>[]>() {
        @Override
        protected Enum<?>[] computeValue(Class<?> type) {
            return (Enum<?>[]) type.getEnumConstants();
        }
    };

    protected final Class<E> enumType;
    protected final E[] constants;

    protected int size = 0;

    private MutableEnumSet(Class<E> enumType) {
        if (!enumType.isEnum()) {
            throw new IllegalArgumentException("Class " + enumType.getName() + " is not an enum");
        }

        this.enumType = enumType;
        this.constants = (E[]) ENUM_CONSTANTS.get(enumType);
    }

    protected void checkValue(E value) throws ClassCastException, NullPointerException {
        Class<?> valueType = value.getClass(); //implicit null check of value

        if (!enumType.isAssignableFrom(valueType)) {
            throw new ClassCastException("Value " + value + " is not an instance of " + enumType.getName());
        }
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

    private static final class OfEmptyEnum<E extends Enum<E>> extends MutableEnumSet<E> {

        OfEmptyEnum(Class<E> enumType) {
            super(enumType);
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.empty();
        }

        @Override
        public boolean add(E value) {
            Objects.requireNonNull(value);
            throw new AssertionError("");
        }

        @Override
        public boolean remove(Object value) {
            return false;
        }

        @Override
        public void clear() {
        }
    }

    private static final class OfSmallEnum<E extends Enum<E>> extends MutableEnumSet<E> {

        private long elements;

        private OfSmallEnum(Class<E> enumType) {
            super(enumType);
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            if (isEmpty()) {
                return Iterators.empty();
            } else {
                return new Itr<>(constants, elements);
            }
        }

        @Override
        public boolean add(E value) {
            checkValue(value);

            int ordinal = value.ordinal();

            long oldElements = elements;
            this.elements = oldElements | (1L << value.ordinal());

            if (this.elements != oldElements) {
                this.size++;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean addAll(@NotNull Iterable<? extends E> values) {
            long oldElements = this.elements;
            long newElements = oldElements;

            for (E value : values) {
                if (enumType.isInstance(value)) {
                    newElements |= (1L << value.ordinal());
                }
            }

            if (oldElements != newElements) {
                this.size = Long.bitCount(newElements);
                this.elements = newElements;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean remove(Object value) {
            if (!enumType.isInstance(value)) {
                return false;
            }

            int ordinal = ((E) value).ordinal();

            long oldElements = elements;
            this.elements = oldElements & ~(1L << ordinal);
            if (this.elements != oldElements) {
                size--;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean removeAll(@NotNull Iterable<?> values) {
            long oldElements = this.elements;
            long newElements = oldElements;

            for (Object value : values) {
                if (enumType.isInstance(value)) {
                    newElements &= ~(1L << ((E) value).ordinal());
                }
            }

            if (oldElements != newElements) {
                this.size = Long.bitCount(newElements);
                this.elements = newElements;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void clear() {
            elements = 0L;
            size = 0;
        }

        private static final class Itr<E> extends AbstractIterator<E> {
            private final E[] constants;
            private final long elements;

            private int current = 0;

            private Itr(E[] constants, long elements) {
                this.constants = constants;
                this.elements = elements;
            }

            @Override
            public boolean hasNext() {
                while (current < constants.length) {
                    if ((elements & (1L << current)) != 0) {
                        return true;
                    }
                    current++;
                }
                return false;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return constants[current++];
            }
        }
    }
}
