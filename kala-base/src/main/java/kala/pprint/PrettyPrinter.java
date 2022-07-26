package kala.pprint;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.function.Function;

public class PrettyPrinter {
    private final int indent;
    private final int width;
    private final boolean escapeUnicode;
    private final Writer writer;
    private final Function<Object, ?> objectMapper;

    public PrettyPrinter(int indent, int width, boolean escapeUnicode, Writer writer, Function<Object, ?> objectMapper) {
        this.indent = indent;
        this.width = width;
        this.escapeUnicode = escapeUnicode;
        this.writer = writer;
        this.objectMapper = objectMapper;
    }

    private static Object mapObj(Function<Object, ?> objectMapper, Object obj) {
        return objectMapper == null ? obj : objectMapper.apply(obj);
    }

    private static <A extends Appendable> A format(
            A output, int indent, int width, boolean escapeUnicode, Function<Object, ?> objectMapper) throws IOException {
        throw new UnsupportedOperationException(); // TODO
    }

    private static <A extends Appendable> A format(
            A output, int indent, int width, boolean escapeUnicode, Function<Object, ?> objectMapper,
            int indentLevel, Context context) throws IOException {
        throw new UnsupportedOperationException(); // TODO
    }

    private static final class Context {
        private final ArrayList<Object> recursive = new ArrayList<>();
    }
}
