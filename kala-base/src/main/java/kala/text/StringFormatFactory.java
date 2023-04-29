package kala.text;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @see StringFormat
 */
@ApiStatus.Experimental
public final class StringFormatFactory {
    private final Map<String, StringFormatProcessor> formatProcessors;
    private final Locale locale;

    private StringFormatFactory(Map<String, StringFormatProcessor> formatProcessors, Locale locale) {
        this.formatProcessors = formatProcessors;
        this.locale = locale;
    }

    private static final StringFormatFactory DEFAULT = builder()
            .registerProcessor("array", StringFormatProcessor.ARRAY)
            .registerProcessor("lower", StringFormatProcessor.LOWER)
            .registerProcessor("upper", StringFormatProcessor.UPPER)
            .registerProcessor("substring", StringFormatProcessor.SUBSTRING)
            .registerProcessor("printf", StringFormatProcessor.PRINTF)
            .build();

    public static StringFormatFactory getDefault() {
        return DEFAULT;
    }

    public static StringFormatFactory.Builder builder() {
        return new Builder();
    }

    public static StringFormatFactory.Builder builder(@NotNull StringFormatFactory base) {
        Builder builder = new Builder();
        builder.formatTypes.putAll(base.formatProcessors);
        builder.locale = base.locale;
        return builder;
    }

    public Locale getLocale() {
        return locale;
    }

    public StringFormat parse(String format) {
        return new StringFormat(this, format); // TODO: opt
    }

    public String format(String format, Object... arguments) {
        return format(new StringBuilder(), format, arguments).toString();
    }

    public StringBuilder format(StringBuilder out, String format, Object... arguments) {
        final int formatLength = format.length();   // implicit null check of format
        final int argsCount = arguments.length;     // implicit null check of arguments
        int argIndex = 0;

        int lastOffset = 0;

        mainLoop:
        while (lastOffset < formatLength) {
            final int offset = format.indexOf('{', lastOffset);
            if (offset < 0) {
                out.append(format, lastOffset, formatLength);
                return out;
            }

            if (offset == formatLength - 1) {
                throw invalidFormatString(format);
            }

            out.append(format, lastOffset, offset);

            final int nextOffset = offset + 1;
            final char nextChar = format.charAt(nextOffset);

            if (nextChar == '}') {
                processArg(out, arguments, argIndex++);
                lastOffset = nextOffset + 1;
            } else if (nextChar == '\'') {
                int endOffset = nextOffset + 1;
                while (endOffset < formatLength) {
                    endOffset = format.indexOf('}', endOffset);
                    if (endOffset < 0) {
                        throw invalidFormatString(format);
                    }

                    if (format.charAt(endOffset - 1) == '\'' && endOffset > nextOffset + 1) {
                        out.append(format, nextOffset + 1, endOffset - 1);
                        lastOffset = endOffset + 1;
                        continue mainLoop;
                    }

                    endOffset++;
                }

                throw invalidFormatString(format);
            } else {
                int endOffset = format.indexOf('}', nextOffset + 1);
                if (endOffset < 0) {
                    throw invalidFormatString(format);
                }

                int firstColonOffset = findColon(format, nextOffset, endOffset);
                if (firstColonOffset < 0) {
                    // fastpath
                    int idx = parseInt(format, nextOffset, endOffset);
                    if (idx == -1) {
                        throw invalidFormatString(format);
                    }

                    processArg(out, arguments, idx);
                } else {
                    int idx = firstColonOffset == nextOffset ? argIndex++ : parseInt(format, nextOffset, firstColonOffset);
                    if (idx == -1) {
                        throw invalidFormatString(format);
                    }

                    processArgImpl(out, format, firstColonOffset + 1, endOffset, arguments, idx);
                }

                lastOffset = endOffset + 1;
            }
        }

        return out;
    }

    private void processArgImpl(StringBuilder out, String format, int beginIndex, int endIndex, Object[] args, int argIndex) {
        String processorName;
        String style;

        int secondColonOffset = findColon(format, beginIndex, endIndex);
        if (secondColonOffset < 0) {
            processorName = format.substring(beginIndex, endIndex);
            style = "";
        } else {
            processorName = format.substring(beginIndex, secondColonOffset);
            style = format.substring(secondColonOffset + 1, endIndex);
        }

        processArg(out, processorName, args, argIndex, style);
    }

    private static void checkArgIndex(int index, int length) {
        if (index < 0 || index >= length) {
            throw new StringFormatException("Invalid argument index: " + index);
        }
    }

    private static StringFormatException invalidFormatString(String format) {
        return new StringFormatException("Invalid format string: " + format);
    }

    private void processArg(StringBuilder out, Object[] args, int index) {
        checkArgIndex(index, args.length);
        out.append(args[index]);
    }

    private void processArg(StringBuilder out, String processorName, Object[] args, int argIndex, String style) {
        checkArgIndex(argIndex, args.length);

        Object arg = args[argIndex];

        if (processorName.isEmpty()) {
            if (!style.isEmpty()) {
                throw new StringFormatException("Unknown style: " + style);
            }
            out.append(arg);
        } else {
            @SuppressWarnings("unchecked")
            StringFormatProcessor processor = this.formatProcessors.get(processorName);

            if (processor == null) {
                throw new StringFormatException("Unknown format processor: " + processorName);
            }

            if (arg == null && !processor.processNull()) {
                out.append((String) null);
            } else {
                try {
                    processor.accept(this, out, arg, style);
                } catch (StringFormatException e) {
                    throw e;
                } catch (Throwable e) {
                    throw new StringFormatException(e);
                }
            }
        }
    }

    private static int findColon(String str, int beginIndex, int endIndex) {
        for (int i = beginIndex; i < endIndex; i++) {
            if (str.charAt(i) == ':') {
                return i;
            }
        }

        return -1;
    }

    private static int parseInt(String source, int beginIndex, int endIndex) {
        // Don't accept very large numbers
        if (endIndex - beginIndex > 9) {
            return -1;
        }

        int res = 0;
        for (int i = beginIndex; i < endIndex; i++) {
            int n = source.charAt(i) - '0';
            if (n < 0 || n > 9) {
                return -1;
            }

            res *= 10;
            res += n;
        }
        return res;
    }

    public static final class Builder {
        private final Map<String, StringFormatProcessor> formatTypes = new LinkedHashMap<>();
        private Locale locale = Locale.ROOT;

        private boolean built = false;

        Builder() {
        }

        public <T> Builder registerProcessor(String type, @NotNull StringFormatProcessor processor) {
            Objects.requireNonNull(type);
            Objects.requireNonNull(processor);

            if (built) {
                throw new IllegalStateException();
            }

            formatTypes.put(type, processor);
            return this;
        }

        public Builder locale(Locale locale) {
            this.locale = Objects.requireNonNull(locale);
            return this;
        }

        public StringFormatFactory build() {
            this.built = true;
            return new StringFormatFactory(Collections.unmodifiableMap(formatTypes), locale);
        }
    }
}
