package asia.kala.comparator;

import asia.kala.annotations.StaticClass;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@StaticClass
@SuppressWarnings("unchecked")
public final class Comparators {
    private Comparators() {
    }

    @NotNull
    public static <T> Comparator<T> naturalOrder() {
        return (Comparator<T>) Comparator.naturalOrder();
    }

    @NotNull
    public static <T> Comparator<T> reverseOrder() {
        return (Comparator<T>) Comparator.reverseOrder();
    }
}
