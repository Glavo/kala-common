/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    public static boolean startsWith(@NotNull String str, @NotNull CharSequence prefix) {
        return startsWith(str, prefix, 0);
    }

    public static boolean startsWith(@NotNull String str, @NotNull CharSequence prefix, int toIndex) {
        return prefix instanceof StringSlice ? startsWith(str, (StringSlice) prefix, toIndex) : startsWith(str, prefix.toString(), toIndex);
    }

    public static boolean endsWith(@NotNull String str, @NotNull String prefix) {
        return str.endsWith(prefix);
    }

    public static boolean endsWith(@NotNull String str, @NotNull StringSlice prefix) {
        if (str.length() < prefix.length()) {
            return false;
        }

        return str.regionMatches(str.length() - prefix.length(), prefix.source(), prefix.sourceOffset(), prefix.length());
    }

    public static boolean endsWith(@NotNull String str, @NotNull CharSequence prefix) {
        return prefix instanceof StringSlice ? endsWith(str, (StringSlice) prefix) : endsWith(str, prefix.toString());
    }

    public static String removePrefix(@NotNull String str, @NotNull String prefix) {
        return str.startsWith(prefix) ? str.substring(prefix.length()) : str;
    }

    public static String removePrefix(@NotNull String str, @NotNull StringSlice prefix) {
        return startsWith(str, prefix) ? str.substring(prefix.length()) : str;
    }

    public static String removePrefix(@NotNull String str, @NotNull CharSequence prefix) {
        return startsWith(str, prefix) ? str.substring(prefix.length()) : str;
    }

    public static String removeSuffix(@NotNull String str, @NotNull String prefix) {
        return str.endsWith(prefix) ? str.substring(0, str.length() - prefix.length()) : str;
    }

    public static String removeSuffix(@NotNull String str, @NotNull StringSlice prefix) {
        return endsWith(str, prefix) ? str.substring(0, str.length() - prefix.length()) : str;
    }

    public static String removeSuffix(@NotNull String str, @NotNull CharSequence prefix) {
        return endsWith(str, prefix) ? str.substring(0, str.length() - prefix.length()) : str;
    }
}
