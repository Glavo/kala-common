package kala.function;

import kala.annotations.StaticClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@StaticClass
@SuppressWarnings("unchecked")
public final class Consumers {
    private Consumers() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Consumer<T> narrow(Consumer<? super T> supplier) {
        return (Consumer<T>) supplier;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Consumer<T> of(Consumer<? super T> supplier) {
        return (Consumer<T>) supplier;
    }

    public static <T> @NotNull Consumer<T> noop() {
        return (Consumer<T>) Noop.INSTANCE;
    }

    enum Noop implements Consumer<Object> {
        INSTANCE;

        @Override
        public void accept(Object o) {
            // noop
        }

        @Override
        public @NotNull Consumer<Object> andThen(@NotNull Consumer<? super Object> after) {
            return narrow(after);
        }

        @Override
        public final String toString() {
            return "Consumers.Noop";
        }
    }
}
