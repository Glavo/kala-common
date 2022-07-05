package kala.collection.mutable.primitive;

import kala.annotations.DelegateBy;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.internal.convert.FromJavaConvert;
import kala.collection.primitive.${Type}Seq;
import kala.comparator.primitive.${Type}Comparator;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.stream.Stream;

public interface Mutable${Type}Seq extends MutablePrimitiveSeq<${WrapperType}>, ${Type}Seq, Mutable${Type}Collection {

    //region Static Factories

    static @NotNull ${Type}CollectionFactory<?, Mutable${Type}Seq> factory() {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Mutable${Type}Seq of() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Seq of(${PrimitiveType} value1) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _ -> new")
    static @NotNull Mutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _, _ -> new")
    static @NotNull Mutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _, _, _ -> new")
    static @NotNull Mutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _, _, _, _ -> new")
    static @NotNull Mutable${Type}Seq of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Mutable${Type}Seq of(${PrimitiveType}...values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Mutable${Type}Seq from(${PrimitiveType} @NotNull []values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Mutable${Type}Seq from(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Mutable${Type}Seq from(@NotNull ${Type}Iterator it) {
        throw new UnsupportedOperationException(); // TODO
    }
<#if IsSpecialized>

    /*
    static @NotNull Mutable${Type}Seq from(@NotNull ${Type}Stream stream) {
        throw new UnsupportedOperationException(); // TODO
    }
     */
</#if>

    //endregion

    @Override
    default @NotNull String className() {
        return "Mutable${Type}Seq";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends Mutable${Type}Seq> iterableFactory() {
        return Mutable${Type}Seq.factory();
    }

    @Contract(mutates = "this")
    void set(int index, ${PrimitiveType} newValue);

    default void swap(int index1, int index2) {
        final ${PrimitiveType} old1 =this.get(index1);
        final ${PrimitiveType} old2 =this.get(index2);

        this.set(index1, old2);
        this.set(index2, old1);
    }

    @Contract(mutates = "this")
    default void replaceAll(@NotNull ${Type}UnaryOperator operator) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, operator.applyAs${Type}(this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void replaceAllIndexed(@NotNull Indexed${Type}UnaryOperator operator) {
        int size = size();
        for (int i = 0; i < size; i++) {
            this.set(i, operator.applyAs${Type}(i, this.get(i)));
        }
    }

    @Contract(mutates = "this")
    default void sort() {
<#if Type == "Boolean">
        final int size = this.size();
        int trueCount = this.count(it -> it);
        int falseCount = size - trueCount;

        if (trueCount == 0 || falseCount == 0) return;

        for (int i = 0; i < falseCount; i++) {
            this.set(i, false);
        }

        for (int i = falseCount; i < size; i++) {
            this.set(i, true);
        }
<#else>
        ${PrimitiveType}[] values = toArray();
        Arrays.sort(values);

        for (int i = 0; i < values.length; i++) {
            this.set(i, values[i]);
        }
</#if>
    }

    @Contract(mutates = "this")
    default void reverse() {
        final int size = this.size();
        if (size == 0) {
            return;
        }

        for (int i = 0; i < size / 2; i++) {
            swap(i, size - i - 1);
        }
    }

    @DelegateBy("shuffle(Random)")
    default void shuffle() {
        shuffle(ThreadLocalRandom.current());
    }

    default void shuffle(@NotNull Random random) {
        int ks = this.knownSize();
        if (ks == 0 || ks == 1) {
            return;
        }
        if (supportsFastRandomAccess() || (ks > 0 && ks <= 5)) { // TODO: SHUFFLE_THRESHOLD
            assert ks > 0;
            for (int i = ks; i > 1; i--) {
                swap(i - 1, random.nextInt(i));
            }
        } else {
            final ${PrimitiveType}[]arr = this.toArray();
            ${Type}Arrays.shuffle(arr, random);
            this.replaceAllIndexed((i, v) -> arr[i]);
        }
    }
}