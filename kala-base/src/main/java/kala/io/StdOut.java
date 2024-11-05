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
package kala.io;

import kala.annotations.StaticClass;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.Locale;

/**
 * Provide methods to simply output to the console.
 */
@StaticClass
public final class StdOut {
    public static void print(Object x) {
        if (x == null) {
            System.out.print("null");
        } else if (x.getClass().isArray()) {
            switch (x) {
                case Object[] objects -> print(objects);
                case char[] chars -> print(chars);
                case boolean[] booleans -> print(booleans);
                case byte[] bytes -> print(bytes);
                case short[] shorts -> print(shorts);
                case int[] ints -> print(ints);
                case long[] longs -> print(longs);
                case float[] floats -> print(floats);
                case double[] doubles -> print(doubles);
                default -> throw new AssertionError("Unknown array type: " + x.getClass());
            }
        } else {
            System.out.print(x);
        }
    }

    public static void print(String x) {
        System.out.print(x);
    }

    public static void print(char x) {
        System.out.print(x);
    }

    public static void print(boolean x) {
        System.out.print(x);
    }

    public static void print(byte x) {
        System.out.print(x);
    }

    public static void print(short x) {
        System.out.print(x);
    }

    public static void print(int x) {
        System.out.print(x);
    }

    public static void print(long x) {
        System.out.print(x);
    }

    public static void print(float x) {
        System.out.print(x);
    }

    public static void print(double x) {
        System.out.print(x);
    }

    public static void print(Object[] array) {
        if (array == null) {
            System.out.print("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    print(array[i]);
                }
            }
            out.print(']');
        }
    }

    public static void print(char[] array) {
        if (array == null) {
            System.out.print("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.print(']');
        }
    }

    public static void print(boolean[] array) {
        if (array == null) {
            System.out.print("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.print(']');
        }
    }

    public static void print(byte[] array) {
        if (array == null) {
            System.out.print("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.print(']');
        }
    }

    public static void print(short[] array) {
        if (array == null) {
            System.out.print("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.print(']');
        }
    }

    public static void print(int[] array) {
        if (array == null) {
            System.out.print("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.print(']');
        }
    }

    public static void print(long[] array) {
        if (array == null) {
            System.out.print("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.print(']');
        }
    }

    public static void print(float[] array) {
        if (array == null) {
            System.out.print("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.print(']');
        }
    }

    public static void print(double[] array) {
        if (array == null) {
            System.out.print("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.print(']');
        }
    }

    public static void println() {
        System.out.println();
    }

    public static void println(Object x) {
        if (x == null) {
            System.out.println("null");
        } else if (x.getClass().isArray()) {
            switch (x) {
                case Object[] objects -> println(objects);
                case char[] chars -> println(chars);
                case boolean[] booleans -> println(booleans);
                case byte[] bytes -> println(bytes);
                case short[] shorts -> println(shorts);
                case int[] ints -> println(ints);
                case long[] longs -> println(longs);
                case float[] floats -> println(floats);
                case double[] doubles -> println(doubles);
                default -> throw new AssertionError("Unknown array type: " + x.getClass());
            }
        } else {
            System.out.println(x);
        }
    }

    public static void println(String x) {
        System.out.println(x);
    }

    public static void println(char x) {
        System.out.println(x);
    }

    public static void println(boolean x) {
        System.out.println(x);
    }

    public static void println(byte x) {
        System.out.println(x);
    }

    public static void println(short x) {
        System.out.println(x);
    }

    public static void println(int x) {
        System.out.println(x);
    }

    public static void println(long x) {
        System.out.println(x);
    }

    public static void println(float x) {
        System.out.println(x);
    }

    public static void println(double x) {
        System.out.println(x);
    }

    public static void println(Object[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    print(array[i]);
                }
            }
            out.println(']');
        }
    }

    public static void println(char[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.println(']');
        }
    }

    public static void println(boolean[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.println(']');
        }
    }

    public static void println(byte[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.println(']');
        }
    }

    public static void println(short[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.println(']');
        }
    }

    public static void println(int[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.println(']');
        }
    }

    public static void println(long[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.println(']');
        }
    }

    public static void println(float[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.println(']');
        }
    }

    public static void println(double[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }

        final int size = array.length;
        final PrintStream out = System.out;

        synchronized (out) {
            out.print('[');
            if (size > 0) {
                out.print(array[0]);
                for (int i = 1; i < size; i++) {
                    out.print(", ");
                    out.print(array[i]);
                }
            }
            out.println(']');
        }
    }

    public static void printf(@NotNull String format, Object... args) {
        System.out.printf(format, args);
    }

    public static void printf(Locale locale, @NotNull String format, Object... args) {
        System.out.printf(locale, format, args);
    }

    public static void flushStdout() {
        System.out.flush();
    }

    private StdOut() {
    }
}
