package kala.collection.primitive;

import kala.Conditions;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.function.*;
import kala.internal.Internal${Type}ArrayBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "elements")
public class ${Type}ArraySeq extends Abstract${Type}Seq implements Indexed${Type}Seq, Serializable {
    private static final long serialVersionUID = 4981379062449237945L;

    public static final ${Type}ArraySeq EMPTY = new ${Type}ArraySeq(${Type}Arrays.EMPTY);

    private static final ${Type}ArraySeq.Factory FACTORY = new ${Type}ArraySeq.Factory();

    protected final ${PrimitiveType} @NotNull [] elements;

    protected ${Type}ArraySeq(${PrimitiveType} @NotNull [] elements) {
        this.elements = elements;
    }

    //region Static Factories

    public static @NotNull ${Type}CollectionFactory<?, ? extends ${Type}ArraySeq> factory() {
        return FACTORY;
    }

    @Contract("_ -> new")
    public static @NotNull ${Type}ArraySeq wrap(${PrimitiveType} @NotNull [] array) {
        Objects.requireNonNull(array);
        return new ${Type}ArraySeq(array);
    }

    public static @NotNull ${Type}ArraySeq empty() {
        return EMPTY;
    }

    public static @NotNull ${Type}ArraySeq of() {
        return empty();
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ${Type}ArraySeq of(${PrimitiveType} value1) {
        return new ${Type}ArraySeq(new ${PrimitiveType}[]{value1});
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull ${Type}ArraySeq of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return new ${Type}ArraySeq(new ${PrimitiveType}[]{value1, value2});
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull ${Type}ArraySeq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return new ${Type}ArraySeq(new ${PrimitiveType}[]{value1, value2, value3});
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static @NotNull ${Type}ArraySeq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return new ${Type}ArraySeq(new ${PrimitiveType}[]{value1, value2, value3, value4});
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static @NotNull ${Type}ArraySeq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return new ${Type}ArraySeq(new ${PrimitiveType}[]{value1, value2, value3, value4, value5});
    }

    @Contract(pure = true)
    public static @NotNull ${Type}ArraySeq of(${PrimitiveType}... values) {
        return from(values);
    }

    @Contract(pure = true)
    public static @NotNull ${Type}ArraySeq from(${PrimitiveType} @NotNull [] values) {
        return values.length == 0
                ? empty()
                : new ${Type}ArraySeq(values.clone());
    }

    public static @NotNull ${Type}ArraySeq from(@NotNull ${Type}Traversable values) {
        /* TODO
        if (values instanceof ImmutableArray<?>) {
            return (${Type}ArraySeq) values;
        }
         */

        if (values.isEmpty()) { // implicit null check of values
            return empty();
        }

        ${PrimitiveType}[] arr = values.toArray();
        return arr.length == 0 ? empty() : new ${Type}ArraySeq(arr);
    }


    public static @NotNull ${Type}ArraySeq from(@NotNull ${Type}Iterator it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        return new ${Type}ArraySeq(it.toArray());
    }

    public static @NotNull ${Type}ArraySeq fill(int n, ${PrimitiveType} value) {
        if (n <= 0) {
            return empty();
        }

        ${PrimitiveType}[] ans = new ${PrimitiveType}[n];
        if (value != ${Values.Default}) {
            Arrays.fill(ans, value);
        }
        return new ${Type}ArraySeq(ans);
    }

    public static @NotNull ${Type}ArraySeq fill(int n, @NotNull ${Type}Supplier supplier) {
        if (n <= 0) {
            return empty();
        }

        ${PrimitiveType}[] ans = new ${PrimitiveType}[n];
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.getAs${Type}();
        }
        return new ${Type}ArraySeq(ans);
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "${Type}ArraySeq";
    }

    @Override
    public final @NotNull ${Type}Iterator iterator() {
        return ${Type}Arrays.iterator(elements);
    }

    @Override
    public @NotNull ${Type}Iterator iterator(int beginIndex) {
        return ${Type}Arrays.iterator(elements, beginIndex);
    }

    //region Size Info

    @Override
    public final boolean isEmpty() {
        return elements.length == 0;
    }

    @Override
    public final int size() {
        return elements.length;
    }

    @Override
    public final int knownSize() {
        return elements.length;
    }

    //endregion

    @Override
    public final @NotNull ${Type}Iterator reverseIterator() {
        return ${Type}Arrays.reverseIterator(elements);
    }

    //region Positional Access Operations

    public final ${PrimitiveType} get(int index) {
        return elements[index];
    }

    //endregion
<#if Type != "Boolean">

    //region Search Operations

    @Override
    public final int binarySearch(${PrimitiveType} value, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, elements.length);
        return Arrays.binarySearch(elements, beginIndex, endIndex, value);
    }

    //endregion
</#if>

    //region Element Retrieval Operations

    @Override
    public final ${PrimitiveType} first() {
        try {
            return elements[0];
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public final ${PrimitiveType} last() {
        final int size = elements.length;
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[size - 1];
    }

    //endregion

    //region Element Conditions

    @Override
    public final boolean contains(${PrimitiveType} value) {
        if (elements.length == 0) {
            return false;
        }

        for (${PrimitiveType} e : elements) {
            if (${PrimitiveEquals("value", "e")}) {
                return true;
            }
        }

        return false;
    }

    @Override
    public final boolean containsAll(@NotNull ${Type}Traversable values) {
        ${Type}Iterator it = values.iterator();
        while (it.hasNext()) {
            ${PrimitiveType} v = it.next${Type}();
            if (!contains(v)) return false;
        }

        return true;
    }

    @Override
    public final boolean anyMatch(@NotNull ${Type}Predicate predicate) {
        for (${PrimitiveType} e : elements) {
            if (predicate.test(e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final boolean allMatch(@NotNull ${Type}Predicate predicate) {
        for (${PrimitiveType} e : elements) {
            if (!predicate.test(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean noneMatch(@NotNull ${Type}Predicate predicate) {
        for (${PrimitiveType} e : elements) {
            if (predicate.test(e)) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region Search Operations

    @Override
    public final int indexOf(${PrimitiveType} value) {
        return ${Type}Arrays.indexOf(elements, value);
    }

    @Override
    public final int indexOf(${PrimitiveType} value, int from) {
        return ${Type}Arrays.indexOf(elements, value, from);
    }

    @Override
    public final int indexWhere(@NotNull ${Type}Predicate predicate) {
        return ${Type}Arrays.indexWhere(elements, predicate);
    }

    @Override
    public final int indexWhere(@NotNull ${Type}Predicate predicate, int from) {
        return ${Type}Arrays.indexWhere(elements, predicate, from);
    }

    @Override
    public final int lastIndexOf(${PrimitiveType} value) {
        return ${Type}Arrays.lastIndexOf(elements, value);
    }

    @Override
    public final int lastIndexOf(${PrimitiveType} value, int end) {
        return ${Type}Arrays.lastIndexOf(elements, value, end);
    }

    @Override
    public final int lastIndexWhere(@NotNull ${Type}Predicate predicate) {
        return ${Type}Arrays.lastIndexWhere(elements, predicate);
    }

    @Override
    public final int lastIndexWhere(@NotNull ${Type}Predicate predicate, int end) {
        return ${Type}Arrays.lastIndexWhere(elements, predicate, end);
    }

    //endregion

    //region Aggregate Operations

    @Override
    public final int count(@NotNull ${Type}Predicate predicate) {
        int c = 0;
        for (${PrimitiveType} e : this.elements) {
            if (predicate.test(e)) {
                ++c;
            }
        }
        return c;
    }

    @Override
    public final ${PrimitiveType} max() {
        return ${Type}Arrays.max(elements);
    }

    @Override
    public final ${PrimitiveType} min() {
        return ${Type}Arrays.min(elements);
    }


    @Override
    public ${PrimitiveType} foldLeft(${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        return ${Type}Arrays.foldLeft(elements, zero, op);
    }

    @Override
    public <U> U foldLeftToObj(U zero, @NotNull Obj${Type}BiFunction<U, U> op) {
        return ${Type}Arrays.foldLeftToObj(elements, zero, op);
    }

    @Override
    public ${PrimitiveType} foldRight(${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        return ${Type}Arrays.foldRight(elements, zero, op);
    }

    @Override
    public <U> U foldRightToObj(U zero, @NotNull ${Type}ObjBiFunction<U, U> op) {
        return ${Type}Arrays.foldRightToObj(elements, zero, op);
    }

    @Override
    public ${PrimitiveType} reduceLeft(@NotNull ${Type}BinaryOperator op) {
        return ${Type}Arrays.reduceLeft(elements, op);
    }

    @Override
    public ${PrimitiveType} reduceRight(@NotNull ${Type}BinaryOperator op) {
        return ${Type}Arrays.reduceRight(elements, op);
    }

    //endregion

    @Override
    public final int copyToArray(int srcPos, ${PrimitiveType} @NotNull [] dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + destPos + ") < 0");
        }
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        final int dl = dest.length;
        final int size = elements.length;

        if (destPos >= dl || srcPos >= size) {
            return 0;
        }

        final int n = Math.min(Math.min(size - srcPos, dl - destPos), limit);
        System.arraycopy(elements, srcPos, dest, destPos, n);
        return n;
    }

    //region Conversion Operations

    @Override
    public final ${PrimitiveType} @NotNull [] toArray() {
        return elements.clone();
    }

    //endregion

    //region Traverse Operations

    @Override
    public final void forEach(@NotNull ${Type}Consumer action) {
        for (${PrimitiveType} e : this.elements) {
            action.accept(e);
        }
    }

    //endregion

    @Override
    public final int hashCode() {
        int ans = 0;
        for (Object o : elements) {
            ans = ans * 31 + Objects.hashCode(o);
        }
        return ans + SEQ_HASH_MAGIC;
    }

    //region String Representation

    @Override
    public final <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        final int size = elements.length;

        try {
            buffer.append(prefix);
            if (size > 0) {
                buffer.append(Objects.toString(elements[0]));
                for (int i = 1; i < size; i++) {
                    buffer.append(separator);
                    buffer.append(Objects.toString(elements[i]));
                }
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return buffer;
    }

    //endregion

    private static final class Factory implements ${Type}CollectionFactory<Internal${Type}ArrayBuilder, ${Type}ArraySeq> {

        @Override
        public ${Type}ArraySeq empty() {
            return ${Type}ArraySeq.empty();
        }

        @Override
        public ${Type}ArraySeq from(${PrimitiveType} @NotNull [] values) {
            return ${Type}ArraySeq.from(values);
        }

        @Override
        public ${Type}ArraySeq from(@NotNull ${Type}Traversable values) {
            return ${Type}ArraySeq.from(values);
        }

        @Override
        public ${Type}ArraySeq from(@NotNull ${Type}Iterator it) {
            return ${Type}ArraySeq.from(it);
        }

        @Override
        public ${Type}ArraySeq fill(int n, ${PrimitiveType} value) {
            return ${Type}ArraySeq.fill(n, value);
        }

        /*
        @Override
        public ${Type}ArraySeq fill(int n, @NotNull ${Type}Supplier supplier) {
            return ${Type}ArraySeq.fill(n, supplier);
        }

        @Override
        public ${Type}ArraySeq fill(int n, @NotNull IntTo${Type}Function init) {
            return ${Type}ArraySeq.fill(n, init);
        }
         */

        @Override
        public Internal${Type}ArrayBuilder newBuilder() {
            return new Internal${Type}ArrayBuilder();
        }

        @Override
        public void addToBuilder(@NotNull Internal${Type}ArrayBuilder buffer, ${PrimitiveType} value) {
            buffer.append(value);
        }

        @Override
        public Internal${Type}ArrayBuilder mergeBuilder(@NotNull Internal${Type}ArrayBuilder builder1, @NotNull Internal${Type}ArrayBuilder builder2) {
            throw new UnsupportedOperationException(); // TODO
        }

        @Override
        public void sizeHint(@NotNull Internal${Type}ArrayBuilder buffer, int size) {
            // TODO: buffer.sizeHint(size);
        }

        @Override
        public ${Type}ArraySeq build(@NotNull Internal${Type}ArrayBuilder buffer) {
            return new ${Type}ArraySeq(buffer.toArray());
        }
    }
}
