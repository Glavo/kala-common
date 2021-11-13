package kala.value.primitive;

import kala.collection.base.primitive.*;
import kala.function.*;
import kala.value.LazyValue;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

public class Lazy${Type}Value implements ${Type}Traversable, ${Type}Value, ${Type}Supplier, Memoized, Serializable {
    private static final long serialVersionUID = ${SerialVersionUID};

    private transient volatile ${Type}Supplier supplier;
    private transient ${PrimitiveType} value;

    private Lazy${Type}Value(${Type}Supplier supplier) {
        this.supplier = supplier;
    }

    private Lazy${Type}Value(${PrimitiveType} value) {
        this.value = value;
    }

    public static Lazy${Type}Value of(@NotNull ${Type}Supplier supplier) {
        Objects.requireNonNull(supplier);
        return new Lazy${Type}Value(supplier);
    }

    public static Lazy${Type}Value ofValue(${PrimitiveType} value) {
        return new Lazy${Type}Value(value);
    }

    public ${PrimitiveType} get() {
        if (supplier != null) {
            synchronized (this) {
                ${Type}Supplier s = supplier;
                if (s != null) {
                    value = s.getAs${Type}();
                    supplier = null;
                }
            }
        }
        return value;
    }

    @Override
    public ${PrimitiveType} getAs${Type}() {
        return get();
    }

    public boolean isReady() {
        return supplier == null;
    }

    public Lazy${Type}Value map(@NotNull ${Type}UnaryOperator mapper) {
        Objects.requireNonNull(mapper);
        return Lazy${Type}Value.of(() -> mapper.applyAs${Type}(get()));
    }

    public <U> LazyValue<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return LazyValue.of(() -> mapper.apply(get()));
    }

    @Override
    public @NotNull ${Type}Iterator iterator() {
        return new Abstract${Type}Iterator() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public ${PrimitiveType} next${Type}() {
                if (!hasNext) {
                    throw new NoSuchElementException();
                }
                hasNext = false;
                return get();
            }
        };
    }

    @Override
    public String toString() {
        if (supplier == null) {
            return "Lazy${Type}Value[" + value + "]";
        } else {
            return "Lazy${Type}Value[<not computed>]";
        }
    }

    //region Serialization Operations

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        value = in.read${Type}();
        supplier = null;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.write${Type}(get());
    }

    //endregion
}
