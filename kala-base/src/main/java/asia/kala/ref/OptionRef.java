package asia.kala.ref;

import asia.kala.control.Option;
import asia.kala.function.CheckedConsumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class OptionRef<T> implements Serializable {
    private static final long serialVersionUID = 243510304233826246L;

    private static final int HASH_MAGIC = -337852631;

    private @NotNull Option<T> value;

    public OptionRef() {
        this.value = Option.none();
    }

    public OptionRef(T value) {
        this.value = Option.some(value);
    }

    public OptionRef(@NotNull Option<? extends T> value) {
        Objects.requireNonNull(value);
        this.value = Option.narrow(value);
    }

    public final boolean isDefined() {
        return value.isDefined();
    }

    public final boolean isEmpty() {
        return value.isEmpty();
    }

    public final T get() {
        return value.get();
    }

    public final @NotNull Option<T> getOption() {
        return value;
    }

    public final T getOrDefault(T defaultValue) {
        return value.getOrDefault(defaultValue);
    }

    public final T getOrElse(@NotNull Supplier<? extends T> supplier) {
        return value.getOrElse(supplier);
    }

    public final @Nullable T getOrNull() {
        return value.getOrNull();
    }

    public final void set(T newValue) {
        value = Option.some(newValue);
    }

    public final void setOption(@NotNull Option<? extends T> option) {
        this.value = Option.narrow(Objects.requireNonNull(option));
    }

    public final void setIfEmpty(T newValue) {
        if (isEmpty()) {
            this.value = Option.some(newValue);
        }
    }

    public final void setIfEmpty(@NotNull Supplier<? extends T> supplier) {
        if (isEmpty()) {
            this.value = Option.some(supplier.get());
        }
    }

    public final void clear() {
        value = Option.none();
    }

    public final void mapInPlace(@NotNull Function<? super T, ? extends T> mapper) {
        value = value.map(mapper);
    }

    public final void removeIf(@NotNull Predicate<? super T> predicate) {
        filterNotInPlace(predicate);
    }

    public final void removeIfNull() {
        filterNotNullInPlace();
    }

    public final void filterInPlace(@NotNull Predicate<? super T> predicate) {
        value = value.filter(predicate);
    }

    public final void filterNotInPlace(@NotNull Predicate<? super T> predicate) {
        value = value.filterNot(predicate);
    }

    public final void filterNotNullInPlace() {
        value = value.filterNotNull();
    }

    public final void flatMapInPlace(@NotNull Function<? super T, ? extends Option<? extends T>> mapper) {
        value = value.flatMap(mapper);
    }

    public final void forEach(@NotNull Consumer<? super T> action) {
        value.forEach(action);
    }

    public final <Ex extends Throwable> void forEachChecked(@NotNull CheckedConsumer<? super T, ? extends Ex> action) throws Ex {
        forEach(action);
    }

    public final void forEachUnchecked(@NotNull CheckedConsumer<? super T, ?> action) {
        forEach(action);
    }

    public final OptionRef.Editor<T> edit() {
        return new OptionRef.Editor<>(this);
    }

    @Override
    public final int hashCode() {
        return value.hashCode() + HASH_MAGIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionRef<?>)) {
            return false;
        }
        OptionRef<?> other = (OptionRef<?>) o;
        return value.equals(other.value);
    }

    @Override
    public final String toString() {
        return "OptionRef[" + value + "]";
    }

    public static final class Editor<T> {
        private final @NotNull OptionRef<T> ref;

        Editor(@NotNull OptionRef<T> ref) {
            this.ref = ref;
        }

        @Contract(value = "_ -> this")
        public @NotNull OptionRef.Editor<T> set(T newValue) {
            ref.set(newValue);
            return this;
        }

        @Contract(value = "_ -> this")
        public @NotNull OptionRef.Editor<T> setOption(@NotNull Option<? extends T> option) {
            ref.setOption(option);
            return this;
        }

        @Contract(value = "_ -> this")
        public @NotNull OptionRef.Editor<T> setIfEmpty(T newValue) {
            ref.setIfEmpty(newValue);
            return this;
        }

        @Contract(value = "_ -> this")
        public @NotNull OptionRef.Editor<T> setIfEmpty(@NotNull Supplier<? extends T> supplier) {
            ref.setIfEmpty(supplier);
            return this;
        }

        @Contract(value = "-> this")
        public @NotNull OptionRef.Editor<T> clear() {
            ref.clear();
            return this;
        }

        @Contract(value = "_ -> this")
        public @NotNull OptionRef.Editor<T> mapInPlace(@NotNull Function<? super T, ? extends T> mapper) {
            ref.mapInPlace(mapper);
            return this;
        }

        @Contract(value = "_ -> this")
        public @NotNull OptionRef.Editor<T> removeIf(@NotNull Predicate<? super T> predicate) {
            ref.removeIf(predicate);
            return this;
        }

        @Contract(value = "-> this")
        public @NotNull OptionRef.Editor<T> removeIfNull() {
            ref.removeIfNull();
            return this;
        }

        @Contract(value = "_ -> this")
        public @NotNull OptionRef.Editor<T> filterInPlace(@NotNull Predicate<? super T> predicate) {
            ref.filterInPlace(predicate);
            return this;
        }

        @Contract(value = "_ -> this")
        public @NotNull OptionRef.Editor<T> filterNotInPlace(@NotNull Predicate<? super T> predicate) {
            ref.filterNotInPlace(predicate);
            return this;
        }

        @Contract(value = "-> this")
        public @NotNull OptionRef.Editor<T> filterNotNullInPlace() {
            ref.filterNotNullInPlace();
            return this;
        }

        @Contract(value = "_ -> this")
        public @NotNull OptionRef.Editor<T> flatMapInPlace(@NotNull Function<? super T, ? extends Option<? extends T>> mapper) {
            ref.flatMapInPlace(mapper);
            return this;
        }

        public final @NotNull OptionRef<T> done() {
            return ref;
        }
    }
}
