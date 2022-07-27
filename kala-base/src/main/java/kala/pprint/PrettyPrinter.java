package kala.pprint;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.function.Function;

public class PrettyPrinter {
    private final int indent;
    private final int width;
    private final int maxLevel;
    private final boolean escapeUnicode;
    private final Writer writer;
    private final Function<Object, ?> objectMapper;

    public PrettyPrinter(int indent, int width, int maxLevel, boolean escapeUnicode, Writer writer, Function<Object, ?> objectMapper) {
        this.indent = indent;
        this.width = width;
        this.maxLevel = maxLevel;
        this.escapeUnicode = escapeUnicode;
        this.writer = writer;
        this.objectMapper = objectMapper;
    }

    private static void appendIndent(Appendable a, int indent, int level) throws IOException {
        for (int i = 0; i < level * indent; i++) {
            a.append(' ');
        }
    }

    private static void appendRecursion(Appendable a, Object obj) throws IOException {
        a.append("<Recursion on ")
                .append(obj.getClass().getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(obj)))
                .append('>');
    }

    private void format(Appendable output, Object obj) throws IOException {
        format(output, obj, 0, new Context());
    }

    private void format(Appendable output, Object obj,
                        int indentLevel, Context context) throws IOException {
        if (objectMapper != null) obj = objectMapper.apply(obj);

        appendIndent(output, indent, indentLevel);
        if (obj == null) {
            output.append("null");
            return;
        }

        // Literal
        if (obj instanceof Number) {
            if (obj instanceof Byte || obj instanceof Short || obj instanceof Integer) {
                output.append(obj.toString());

            }
        }

    }

    private static final class Context {
        private final ArrayList<Object> recursive = new ArrayList<>();

        void pushObject(Object obj) {
            recursive.add(obj);
        }

        Object popObject() {
            return recursive.remove(recursive.size() - 1);
        }

        boolean isRecursive(Object obj) {
            for (Object o : recursive) {
                if (obj == o) return true;
            }
            return false;
        }
    }
}
