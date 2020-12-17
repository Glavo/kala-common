package asia.kala.comparator;

import asia.kala.annotations.StaticClass;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@StaticClass
@SuppressWarnings("unchecked")
public final class Comparators {
    private Comparators() {
    }

    public static <T> @NotNull Comparator<T> naturalOrder() {
        return (Comparator<T>) Comparator.naturalOrder();
    }

    public static <T> @NotNull Comparator<T> reverseOrder() {
        return (Comparator<T>) Comparator.reverseOrder();
    }
}
