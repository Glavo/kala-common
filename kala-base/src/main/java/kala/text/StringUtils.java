package kala.text;

import kala.annotations.StaticClass;
import org.jetbrains.annotations.NotNull;

@StaticClass
public final class StringUtils {
    private StringUtils() {
    }

    public static boolean startsWith(@NotNull String str, @NotNull String prefix) {
        return str.startsWith(prefix);
    }

    public static boolean startsWith(@NotNull String str, @NotNull String prefix, int toIndex) {
        return str.startsWith(prefix, toIndex);
    }

    public static boolean startsWith(@NotNull String str, @NotNull StringSlice prefix) {
        return startsWith(str, prefix, 0);
    }

    public static boolean startsWith(@NotNull String str, @NotNull StringSlice prefix, int toIndex) {
        if (toIndex < 0 || toIndex > str.length() - prefix.length()) {
            return false;
        }
        return str.regionMatches(toIndex, prefix.source(), prefix.sourceOffset(), prefix.length());
    }

    public static String removePrefix(@NotNull String str, @NotNull String prefix) {
        return str.startsWith(prefix) ? str.substring(prefix.length()) : str;
    }

}
