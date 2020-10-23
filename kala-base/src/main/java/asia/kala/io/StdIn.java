package asia.kala.io;

import asia.kala.annotations.StaticClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.InputMismatchException;
import java.util.Objects;

/**
 * Provide methods to simply input from the console.
 */
@StaticClass
public final class StdIn {
    private static final int BUFFER_SIZE = 128;

    private static InputStream in;

    private static BufferedReader reader;

    private static BufferedReader reader() {
        final InputStream stdin = System.in;

        if (StdIn.in == stdin) {
            return reader;
        }

        synchronized (StdIn.class) {
            if (StdIn.in == stdin) {
                return reader;
            }

            if (stdin == null) {
                StdIn.in = null;
                StdIn.reader = null;
                return null;
            }

            final BufferedReader reader = new BufferedReader(new InputStreamReader(stdin), BUFFER_SIZE);

            StdIn.in = stdin;
            StdIn.reader = reader;

            return reader;
        }
    }

    public static InputStream stdin() {
        return System.in;
    }

    public static PrintStream stdout() {
        return System.out;
    }

    public static PrintStream stderr() {
        return System.err;
    }

    public static void clearStdIOCache() {
        final PrintStream out = System.out;
        if (out != null) {
            out.flush();
        }
        synchronized (StdIn.class) {
            in = null;
            reader = null;
        }
    }

    @NotNull
    public static String readLine() {
        try {
            return Objects.requireNonNull(reader(), "System.in is null")
                    .readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @NotNull
    public static String readLine(String prompt) {
        System.out.print(prompt);
        System.out.flush();
        return readLine();
    }

    @NotNull
    public static char[] readPassword() throws UnsupportedOperationException {
        final java.io.Console console = System.console();
        if (console == null) {
            throw new UnsupportedOperationException("System.console() is null");
        }
        return console.readPassword();
    }

    @NotNull
    public static char[] readPassword(String prompt) throws UnsupportedOperationException {
        final java.io.Console console = System.console();
        if (console == null) {
            throw new UnsupportedOperationException("System.console() is null");
        }
        return console.readPassword("%s", prompt);
    }

    public static char readChar() throws InputMismatchException {
        String s = readLine();
        if (s.length() != 1) {
            throw new InputMismatchException("For input string: \"" + s + '"');
        }
        return s.charAt(0);
    }

    public static char readChar(String prompt) throws InputMismatchException {
        String s = readLine(prompt);
        if (s.length() != 1) {
            throw new InputMismatchException("For input string: \"" + s + '"');
        }
        return s.charAt(0);
    }

    @Nullable
    public static Character readCharOrNull() {
        String s = readLine();
        if (s.length() != 1) {
            return null;
        }
        return s.charAt(0);
    }

    @Nullable
    public static Character readCharOrNull(String prompt) {
        String s = readLine(prompt);
        if (s.length() != 1) {
            return null;
        }
        return s.charAt(0);
    }

    public static int readInt() throws InputMismatchException {
        return readInt(10);
    }

    public static int readInt(int radix) throws InputMismatchException {
        try {
            return Integer.parseInt(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static int readInt(String prompt) throws InputMismatchException {
        return readInt(prompt, 10);
    }

    public static int readInt(String prompt, int radix) throws InputMismatchException {
        try {
            return Integer.parseInt(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    @Nullable
    public static Integer readIntOrNull() {
        return readIntOrNull(10);
    }

    @Nullable
    public static Integer readIntOrNull(int radix) {
        try {
            return Integer.parseInt(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public static Integer readIntOrNull(String prompt) {
        return readIntOrNull(prompt, 10);
    }

    @Nullable
    public static Integer readIntOrNull(String prompt, int radix) {
        try {
            return Integer.parseInt(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static long readLong() throws InputMismatchException {
        return readLong(10);
    }

    public static long readLong(int radix) throws InputMismatchException {
        try {
            return Long.parseLong(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static long readLong(String prompt) throws InputMismatchException {
        return readLong(prompt, 10);
    }

    public static long readLong(String prompt, int radix) throws InputMismatchException {
        try {
            return Long.parseLong(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    @Nullable
    public static Long readLongOrNull() {
        return readLongOrNull(10);
    }

    @Nullable
    public static Long readLongOrNull(int radix) {
        try {
            return Long.parseLong(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public static Long readLongOrNull(String prompt) {
        return readLongOrNull(prompt, 10);
    }

    @Nullable
    public static Long readLongOrNull(String prompt, int radix) {
        try {
            return Long.parseLong(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @NotNull
    public static BigInteger readBigInteger() throws InputMismatchException {
        return readBigInteger(10);
    }

    @NotNull
    public static BigInteger readBigInteger(int radix) throws InputMismatchException {
        try {
            return new BigInteger(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    @NotNull
    public static BigInteger readBigInteger(String prompt) throws InputMismatchException {
        return readBigInteger(prompt, 10);
    }

    @NotNull
    public static BigInteger readBigInteger(String prompt, int radix) throws InputMismatchException {
        try {
            return new BigInteger(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    @Nullable
    public static BigInteger readBigIntegerOrNull() throws InputMismatchException {
        return readBigIntegerOrNull(10);
    }

    @Nullable
    public static BigInteger readBigIntegerOrNull(int radix) throws InputMismatchException {
        try {
            return new BigInteger(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public static BigInteger readBigIntegerOrNull(String prompt) throws InputMismatchException {
        return readBigIntegerOrNull(prompt, 10);
    }

    @Nullable
    public static BigInteger readBigIntegerOrNull(String prompt, int radix) throws InputMismatchException {
        try {
            return new BigInteger(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static double readDouble() throws InputMismatchException {
        try {
            return Double.parseDouble(readLine().trim());
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static double readDouble(String prompt) throws InputMismatchException {
        try {
            return Double.parseDouble(readLine(prompt).trim());
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    @Nullable
    public static Double readDoubleOrNull() throws InputMismatchException {
        try {
            return Double.parseDouble(readLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public static Double readDoubleOrNull(String prompt) throws InputMismatchException {
        try {
            return Double.parseDouble(readLine(prompt).trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @NotNull
    public static BigDecimal readBigDecimal() throws InputMismatchException {
        try {
            return new BigDecimal(readLine().trim());
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    @NotNull
    public static BigDecimal readBigDecimal(String prompt) throws InputMismatchException {
        try {
            return new BigDecimal(readLine(prompt).trim());
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    @NotNull
    public static BigDecimal readBigDecimal(@NotNull MathContext mc) throws InputMismatchException {
        try {
            return new BigDecimal(readLine().trim(), mc);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    @NotNull
    public static BigDecimal readBigDecimal(String prompt, @NotNull MathContext mc) throws InputMismatchException {
        try {
            return new BigDecimal(readLine(prompt).trim(), mc);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    @Nullable
    public static BigDecimal readBigDecimalOrNull() throws InputMismatchException {
        try {
            return new BigDecimal(readLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public static BigDecimal readBigDecimalOrNull(String prompt) throws InputMismatchException {
        try {
            return new BigDecimal(readLine(prompt).trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public static BigDecimal readBigDecimalOrNull(@NotNull MathContext mc) throws InputMismatchException {
        try {
            return new BigDecimal(readLine().trim(), mc);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public static BigDecimal readBigDecimalOrNull(String prompt, @NotNull MathContext mc) throws InputMismatchException {
        try {
            return new BigDecimal(readLine(prompt).trim(), mc);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private StdIn() {
    }
}
