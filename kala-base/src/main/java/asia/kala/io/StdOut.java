package asia.kala.io;

import asia.kala.annotations.StaticClass;
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
            if (x instanceof Object[]) {
                print((Object[]) x);
            } else if (x instanceof char[]) {
                print((char[]) x);
            } else if (x instanceof boolean[]) {
                print((boolean[]) x);
            } else if (x instanceof byte[]) {
                print((byte[]) x);
            } else if (x instanceof short[]) {
                print((short[]) x);
            } else if (x instanceof int[]) {
                print((int[]) x);
            } else if (x instanceof long[]) {
                print((long[]) x);
            } else if (x instanceof float[]) {
                print((float[]) x);
            } else if (x instanceof double[]) {
                print((double[]) x);
            } else {
                throw new AssertionError("Unknown array type: " + x.getClass());
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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
            if (x instanceof Object[]) {
                println((Object[]) x);
            } else if (x instanceof char[]) {
                println((char[]) x);
            } else if (x instanceof boolean[]) {
                println((boolean[]) x);
            } else if (x instanceof byte[]) {
                println((byte[]) x);
            } else if (x instanceof short[]) {
                println((short[]) x);
            } else if (x instanceof int[]) {
                println((int[]) x);
            } else if (x instanceof long[]) {
                println((long[]) x);
            } else if (x instanceof float[]) {
                println((float[]) x);
            } else if (x instanceof double[]) {
                println((double[]) x);
            } else {
                throw new AssertionError("Unknown array type: " + x.getClass());
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
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
