package kala.collection.mutable;

import kala.collection.base.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

@SuppressWarnings("unchecked")
public abstract class MutableEnumSet<E extends Enum<E>> extends AbstractMutableSet<E> {
    private static final ClassValue<Enum<?>[]> ENUM_CONSTANTS = new ClassValue<>() {
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
            throw new ClassCastException();
        }

        @Override
        public boolean remove(Object value) {
            return false;
        }

        @Override
        public void clear() {
        }
    }
}
