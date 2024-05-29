package kala.collection;

import kala.Conditions;
import kala.annotations.Covariant;
import kala.collection.base.Iterators;
import kala.collection.base.OrderedTraversable;
import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableCollection;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.convert.FromJavaConvert;
import kala.collection.internal.view.SeqViews;
import kala.comparator.Comparators;
import kala.function.*;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("unchecked")
public interface Seq<@Covariant E> extends Collection<E>, OrderedTraversable<E>, SeqLike<E>, AnySeq<E> {

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> Seq<E> narrow(Seq<? extends E> seq) {
        return (Seq<E>) seq;
    }

    //endregion

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, Seq<E>> factory() {
        return CollectionFactory.narrow(ImmutableSeq.factory());
    }

    static <E> @NotNull Seq<E> empty() {
        return ImmutableSeq.empty();
    }

    static <E> @NotNull Seq<E> of() {
        return ImmutableSeq.of();
    }

    static <E> @NotNull Seq<E> of(E value1) {
        return ImmutableSeq.of(value1);
    }

    static <E> @NotNull Seq<E> of(E value1, E value2) {
        return ImmutableSeq.of(value1, value2);
    }

    static <E> @NotNull Seq<E> of(E value1, E value2, E value3) {
        return ImmutableSeq.of(value1, value2, value3);
    }

    static <E> @NotNull Seq<E> of(E value1, E value2, E value3, E value4) {
        return ImmutableSeq.of(value1, value2, value3, value4);
    }

    static <E> @NotNull Seq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return ImmutableSeq.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull Seq<E> of(E... values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull Seq<E> from(E @NotNull [] values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull Seq<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull Seq<E> from(@NotNull Iterator<? extends E> it) {
        return ImmutableSeq.from(it);
    }

    static <E> @NotNull Seq<E> fill(int n, E value) {
        return ImmutableSeq.fill(n, value);
    }

    static <E> @NotNull Seq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        return ImmutableSeq.fill(n, supplier);
    }

    static <E> @NotNull Seq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        return ImmutableSeq.fill(n, init);
    }

    static <E> @NotNull Seq<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        return ImmutableSeq.generateUntil(supplier, predicate);
    }

    static <E> @NotNull Seq<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        return ImmutableSeq.generateUntilNull(supplier);
    }

    static <E> @NotNull Seq<E> wrapJava(java.util.@NotNull List<? extends E> source) {
        Objects.requireNonNull(source);
        if (source instanceof AsJavaConvert.SeqAsJava<?, ?>) {
            return ((AsJavaConvert.SeqAsJava<E, ?>) source).source;
        }
        return source instanceof RandomAccess
                ? new FromJavaConvert.IndexedSeqFromJava<>((List<E>) source)
                : new FromJavaConvert.SeqFromJava<>((List<E>) source);
    }

    //endregion

    static int hashCode(@NotNull Seq<?> seq) {
        return Iterators.hash(seq.iterator()) + SEQ_HASH_MAGIC;
    }

    static boolean equals(@NotNull Seq<?> seq1, @NotNull AnySeq<?> seq2) {
        if (seq1 == seq2) return true;
        if (!seq1.canEqual(seq2) || !seq2.canEqual(seq1)) return false;

        return seq1.sameElements(seq2.asGeneric());
    }

    @Override
    default @NotNull String className() {
        return "Seq";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends Seq<U>> iterableFactory() {
        return Seq.factory();
    }

    @Override
    default @NotNull SeqView<E> view() {
        return knownSize() == 0 ? SeqView.empty() : new SeqViews.Of<>(this);
    }

    @Override
    default @NotNull @UnmodifiableView List<E> asJava() {
        return this.supportsFastRandomAccess()
                ? new AsJavaConvert.IndexedSeqAsJava<>(this)
                : new AsJavaConvert.SeqAsJava<>(this);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> slice(int beginIndex, int endIndex) {
        return view().slice(beginIndex, endIndex).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> drop(int n) {
        return view().drop(n).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> dropLast(int n) {
        return view().dropLast(n).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        return view().dropWhile(predicate).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> take(int n) {
        return view().take(n).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> takeLast(int n) {
        return view().takeLast(n).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        return view().takeWhile(predicate).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> updated(int index, E newValue) {
        Conditions.checkElementIndex(index, size());
        return view().updated(index, newValue).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> concat(@NotNull SeqLike<? extends E> other) {
        return view().concat(other).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> concat(@NotNull List<? extends E> other) {
        return view().concat(other).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prepended(E value) {
        return view().prepended(value).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prependedAll(E @NotNull [] values) {
        return view().prependedAll(values).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prependedAll(@NotNull Iterable<? extends E> values) {
        return view().prependedAll(values).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appended(E value) {
        return view().appended(value).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appendedAll(@NotNull Iterable<? extends E> values) {
        return view().appendedAll(values).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appendedAll(E @NotNull [] values) {
        return view().appendedAll(values).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> removedAt(int index) {
        Conditions.checkElementIndex(index, size());
        return view().removedAt(index).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> sorted() {
        return sorted(Comparators.naturalOrder());
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> sorted(Comparator<? super E> comparator) {
        return view().sorted(comparator).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> reversed() {
        return view().reversed().toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> filter(@NotNull Predicate<? super E> predicate) {
        return view().filter(predicate).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull <Ex extends Throwable> ImmutableSeq<E> filterChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) throws Ex {
        return filter(predicate);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> filterUnchecked(
            @NotNull CheckedPredicate<? super E, ?> predicate) {
        return filter(predicate);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return view().filterNot(predicate).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull <Ex extends Throwable> ImmutableSeq<E> filterNotChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) throws Ex {
        return filterNot(predicate);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> filterNotUnchecked(
            @NotNull CheckedPredicate<? super E, ?> predicate) {
        return filterNot(predicate);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<@NotNull E> filterNotNull() {
        return this.filter(Predicates.isNotNull());
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return view().<U>filterIsInstance(clazz).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return view().<U>map(mapper).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableSeq<U> mapChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return map(mapper);
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return map(mapper);
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return view().<U>mapIndexed(mapper).toImmutableSeq();
    }

    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableSeq<U> mapIndexedChecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapIndexed(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapIndexedUnchecked(@NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
        return mapIndexed(mapper);
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return view().<U>mapNotNull(mapper).toImmutableSeq();
    }

    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableSeq<U> mapNotNullChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapNotNull(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return mapNotNull(mapper);
    }


    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<@NotNull U> mapIndexedNotNull(
            @NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        return view().<U>mapIndexedNotNull(mapper).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull <U> ImmutableSeq<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        return view().mapMulti(mapper).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default @NotNull <U> ImmutableSeq<U> mapIndexedMulti(@NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        return view().mapIndexedMulti(mapper).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return view().flatMap(mapper).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default <R> Tuple2<? extends ImmutableSeq<E>, ? extends ImmutableSeq<E>> partition(@NotNull Predicate<? super E> predicate) {
        return partition(ImmutableSeq.factory(), predicate);
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> distinct() {
        return view().distinct().toImmutableSeq();
    }
}
