package kala.collection.internal.enums;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unchecked")
public final class SharedEnumConstants {
    private SharedEnumConstants() {
    }

    private static final ClassValue<Enum<?>[]> constants = new ClassValue<>() {
        @Override
        protected Enum<?>[] computeValue(Class<?> type) {
            return Objects.requireNonNull((Enum<?>[]) type.getEnumConstants());
        }
    };

    public static <T extends Enum<T>> T[] getConstants(@NotNull Class<T> clazz) {
        return (T[]) constants.get(clazz);
    }

    public static <T extends Enum<T>> T getConstant(@NotNull Class<T> clazz, int ordinal) {
        return (T) constants.get(clazz)[ordinal];
    }
}
