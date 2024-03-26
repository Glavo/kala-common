package kala.io;

import kala.annotations.StaticClass;
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

    private static final BufferedReader reader;

    static {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(FileDescriptor.in)));
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


    public static @NotNull String readLine() throws IOException {
        return Objects.requireNonNull(reader, "System.in is null").readLine();
    }

    public static @NotNull String readLine(String prompt) throws IOException {
        System.out.print(prompt);
        System.out.flush();
        return readLine();
    }

    public static char @NotNull [] readPassword() throws IOException, UnsupportedOperationException {
        final java.io.Console console = System.console();
        if (console == null) {
            throw new UnsupportedOperationException("System.console() is null");
        }
        return console.readPassword();
    }

    public static char @NotNull [] readPassword(String prompt) throws IOException, UnsupportedOperationException {
        final java.io.Console console = System.console();
        if (console == null) {
            throw new UnsupportedOperationException("System.console() is null");
        }
        return console.readPassword("%s", prompt);
    }

    public static char readChar() throws IOException, InputMismatchException {
        String s = readLine();
        if (s.length() != 1) {
            throw new InputMismatchException("For input string: \"" + s + '"');
        }
        return s.charAt(0);
    }

    public static char readChar(String prompt) throws IOException, InputMismatchException {
        String s = readLine(prompt);
        if (s.length() != 1) {
            throw new InputMismatchException("For input string: \"" + s + '"');
        }
        return s.charAt(0);
    }

    public static @Nullable Character readCharOrNull() throws IOException {
        String s = readLine();
        if (s.length() != 1) {
            return null;
        }
        return s.charAt(0);
    }

    public static @Nullable Character readCharOrNull(String prompt) throws IOException {
        String s = readLine(prompt);
        if (s.length() != 1) {
            return null;
        }
        return s.charAt(0);
    }

    public static int readInt() throws IOException, InputMismatchException {
        return readInt(10);
    }

    public static int readInt(int radix) throws IOException, InputMismatchException {
        try {
            return Integer.parseInt(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static int readInt(String prompt) throws IOException, InputMismatchException {
        return readInt(prompt, 10);
    }

    public static int readInt(String prompt, int radix) throws IOException, InputMismatchException {
        try {
            return Integer.parseInt(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static @Nullable Integer readIntOrNull() throws IOException {
        return readIntOrNull(10);
    }

    public static @Nullable Integer readIntOrNull(int radix) throws IOException {
        try {
            return Integer.parseInt(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @Nullable Integer readIntOrNull(String prompt) throws IOException {
        return readIntOrNull(prompt, 10);
    }

    public static @Nullable Integer readIntOrNull(String prompt, int radix) throws IOException {
        try {
            return Integer.parseInt(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static long readLong() throws IOException, InputMismatchException {
        return readLong(10);
    }

    public static long readLong(int radix) throws IOException, InputMismatchException {
        try {
            return Long.parseLong(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static long readLong(String prompt) throws IOException, InputMismatchException {
        return readLong(prompt, 10);
    }

    public static long readLong(String prompt, int radix) throws IOException, InputMismatchException {
        try {
            return Long.parseLong(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }


    public static @Nullable Long readLongOrNull() throws IOException {
        return readLongOrNull(10);
    }

    public static @Nullable Long readLongOrNull(int radix) throws IOException {
        try {
            return Long.parseLong(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @Nullable Long readLongOrNull(String prompt) throws IOException {
        return readLongOrNull(prompt, 10);
    }

    public static @Nullable Long readLongOrNull(String prompt, int radix) throws IOException {
        try {
            return Long.parseLong(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @NotNull BigInteger readBigInteger() throws IOException, InputMismatchException {
        return readBigInteger(10);
    }

    public static @NotNull BigInteger readBigInteger(int radix) throws IOException, InputMismatchException {
        try {
            return new BigInteger(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static @NotNull BigInteger readBigInteger(String prompt) throws IOException, InputMismatchException {
        return readBigInteger(prompt, 10);
    }

    public static @NotNull BigInteger readBigInteger(String prompt, int radix) throws IOException, InputMismatchException {
        try {
            return new BigInteger(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static @Nullable BigInteger readBigIntegerOrNull() throws IOException, InputMismatchException {
        return readBigIntegerOrNull(10);
    }

    public static @Nullable BigInteger readBigIntegerOrNull(int radix)  throws IOException, InputMismatchException {
        try {
            return new BigInteger(readLine().trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @Nullable BigInteger readBigIntegerOrNull(String prompt)  throws IOException, InputMismatchException {
        return readBigIntegerOrNull(prompt, 10);
    }

    public static @Nullable BigInteger readBigIntegerOrNull(String prompt, int radix)  throws IOException, InputMismatchException {
        try {
            return new BigInteger(readLine(prompt).trim(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static double readDouble()  throws IOException, InputMismatchException {
        try {
            return Double.parseDouble(readLine().trim());
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static double readDouble(String prompt)  throws IOException, InputMismatchException {
        try {
            return Double.parseDouble(readLine(prompt).trim());
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static @Nullable Double readDoubleOrNull()  throws IOException, InputMismatchException {
        try {
            return Double.parseDouble(readLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @Nullable Double readDoubleOrNull(String prompt)  throws IOException, InputMismatchException {
        try {
            return Double.parseDouble(readLine(prompt).trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @NotNull BigDecimal readBigDecimal()  throws IOException, InputMismatchException {
        try {
            return new BigDecimal(readLine().trim());
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static @NotNull BigDecimal readBigDecimal(String prompt)  throws IOException, InputMismatchException {
        try {
            return new BigDecimal(readLine(prompt).trim());
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static @NotNull BigDecimal readBigDecimal(@NotNull MathContext mc)  throws IOException, InputMismatchException {
        try {
            return new BigDecimal(readLine().trim(), mc);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static @NotNull BigDecimal readBigDecimal(String prompt, @NotNull MathContext mc)  throws IOException, InputMismatchException {
        try {
            return new BigDecimal(readLine(prompt).trim(), mc);
        } catch (NumberFormatException e) {
            throw new InputMismatchException(e.getMessage());
        }
    }

    public static @Nullable BigDecimal readBigDecimalOrNull()  throws IOException, InputMismatchException {
        try {
            return new BigDecimal(readLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @Nullable BigDecimal readBigDecimalOrNull(String prompt)  throws IOException, InputMismatchException {
        try {
            return new BigDecimal(readLine(prompt).trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @Nullable BigDecimal readBigDecimalOrNull(@NotNull MathContext mc)  throws IOException, InputMismatchException {
        try {
            return new BigDecimal(readLine().trim(), mc);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @Nullable BigDecimal readBigDecimalOrNull(String prompt, @NotNull MathContext mc)  throws IOException, InputMismatchException {
        try {
            return new BigDecimal(readLine(prompt).trim(), mc);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private StdIn() {
    }
}
