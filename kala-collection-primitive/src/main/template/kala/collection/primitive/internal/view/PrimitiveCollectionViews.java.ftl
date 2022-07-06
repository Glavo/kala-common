package kala.collection.primitive.internal.view;

import kala.collection.CollectionView;
import kala.collection.base.primitive.*;
import kala.collection.primitive.*;
import kala.control.primitive.${Type}Option;
<#if !IsSpecialized>
import kala.function.*;
</#if>
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
<#if IsSpecialized>
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.${Type}Stream;
</#if>

public final class ${Type}CollectionViews {
    private ${Type}CollectionViews() {
    }

    public static final ${Type}CollectionView EMPTY = new Empty();

    public static class Empty extends Abstract${Type}CollectionView {
        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return ${Type}Iterator.empty();
        }
<#if IsSpecialized>

        @Override
        public @NotNull ${Type}Stream stream() {
            return ${Type}Stream.empty();
        }

        @Override
        public @NotNull ${Type}Stream parallelStream() {
            return ${Type}Stream.empty().parallel();
        }
</#if>

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return true;
        }

        @Override
        public final int size() {
            return 0;
        }

        @Override
        public final int knownSize() {
            return 0;
        }

        //endregion

        @Override
        public @NotNull ${Type}CollectionView filter(@NotNull ${Type}Predicate predicate) {
            return this;
        }

        @Override
        public @NotNull ${Type}CollectionView filterNot(@NotNull ${Type}Predicate predicate) {
            return this;
        }

        @Override
        public @NotNull ${Type}CollectionView map(@NotNull ${Type}UnaryOperator mapper) {
            return this;
        }

        @Override
        public @NotNull <U> CollectionView<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper) {
            return CollectionView.empty();
        }

        @Override
        public @NotNull ${Type}CollectionView flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
            return this;
        }

        @Override
        public final ${PrimitiveType} @NotNull [] toArray() {
            return ${Type}Arrays.EMPTY;
        }

        @Override
        public final String toString() {
            return className() + "[]";
        }
    }

    public static class Single extends Abstract${Type}CollectionView {
        protected final ${PrimitiveType} value;

        public Single(${PrimitiveType} value) {
            this.value = value;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return ${Type}Iterator.of(value);
        }

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return false;
        }

        @Override
        public final int size() {
            return 1;
        }

        @Override
        public final int knownSize() {
            return 1;
        }

        //endregion

        @Override
        public void forEach(@NotNull ${Type}Consumer action) {
            action.accept(value);
        }
    }

    public static class Of<C extends ${Type}CollectionLike> extends Abstract${Type}CollectionView {
        protected final @NotNull C source;

        public Of(@NotNull C source) {
            this.source = source;
        }

        @Override
        public final @NotNull ${Type}Iterator iterator() {
            return source.iterator();
        }
<#if IsSpecialized>

        @Override
        public final @NotNull Spliterator.Of${Type} spliterator() {
            return source.spliterator();
        }

        @Override
        public final @NotNull ${Type}Stream stream() {
            return source.stream();
        }

        @Override
        public final @NotNull ${Type}Stream parallelStream() {
            return source.parallelStream();
        }
</#if>

        //region Size Info

        @Override
        public boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        public int knownSize() {
            return source.knownSize();
        }

        //endregion

        @Override
        public @NotNull ${Type}Option find(@NotNull ${Type}Predicate predicate) {
            return source.find(predicate);
        }

        //region Element Conditions

        @Override
        public final boolean contains(${PrimitiveType} value) {
            return source.contains(value);
        }

        @Override
        public final boolean containsAll(${PrimitiveType} @NotNull [] values) {
            return source.containsAll(values);
        }

        @Override
        public final boolean containsAll(@NotNull ${Type}Traversable values) {
            return source.containsAll(values);
        }

        @Override
        public final boolean sameElements(@NotNull Iterable<?> other) {
            return source.sameElements(other);
        }

        @Override
        public final boolean anyMatch(@NotNull ${Type}Predicate predicate) {
            return source.anyMatch(predicate);
        }

        @Override
        public final boolean allMatch(@NotNull ${Type}Predicate predicate) {
            return source.allMatch(predicate);
        }

        @Override
        public final boolean noneMatch(@NotNull ${Type}Predicate predicate) {
            return source.noneMatch(predicate);
        }

        //endregion

        //region Aggregate

        @Override
        public final int count(@NotNull ${Type}Predicate predicate) {
            return source.count(predicate);
        }

        @Override
        public final ${PrimitiveType} max() {
            return source.max();
        }
        
        @Override
        public final ${PrimitiveType} min() {
            return source.min();
        }
        
        //endregion

        //region Conversion Operations

        @Override
        public final ${PrimitiveType} @NotNull [] toArray() {
            return source.toArray();
        }

        //endregion

        //region Traverse Operations

        @Override
        public final void forEach(@NotNull ${Type}Consumer action) {
            source.forEach(action);
        }

        //endregion

        //region String Representation

        @Override
        public final <A extends Appendable> @NotNull A joinTo(@NotNull A buffer) {
            return source.joinTo(buffer);
        }

        @Override
        public final <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, @NotNull CharSequence separator) {
            return source.joinTo(buffer, separator);
        }

        @Override
        public final <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, @NotNull CharSequence separator, @NotNull CharSequence prefix, @NotNull CharSequence postfix) {
            return source.joinTo(buffer, separator, prefix, postfix);
        }

        @Override
        public final @NotNull String joinToString() {
            return source.joinToString();
        }

        @Override
        public final @NotNull String joinToString(@NotNull CharSequence separator) {
            return source.joinToString(separator);
        }

        @Override
        public final @NotNull String joinToString(@NotNull CharSequence separator, @NotNull CharSequence prefix, @NotNull CharSequence postfix) {
            return source.joinToString(separator, prefix, postfix);
        }
        
        @Override
        public final String toString() {
            return joinToString(", ", className() + "[", "]");
        }

        //endregion

    }

    public static final class Mapped extends Abstract${Type}CollectionView {

        private final @NotNull ${Type}CollectionView source;

        private final @NotNull ${Type}UnaryOperator mapper;

        public Mapped(@NotNull ${Type}CollectionView source, @NotNull ${Type}UnaryOperator mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public @NotNull ${Type}Iterator iterator() {
            return iterator().map(mapper);
        }
<#if IsSpecialized>

        @Override
        public @NotNull Spliterator.Of${Type} spliterator() {
            return new MappedSpliterator(source.spliterator(), mapper);
        }

        @Override
        public @NotNull ${Type}Stream stream() {
            return source.stream().map(mapper);
        }

        @Override
        public @NotNull ${Type}Stream parallelStream() {
            return source.parallelStream().map(mapper);
        }
</#if>

        //region Size Info

        @Override
        public boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        public int knownSize() {
            return source.knownSize();
        }

        //endregion
    }

    public static final class Filter extends Abstract${Type}CollectionView {
        private final @NotNull ${Type}CollectionView source;
        private final @NotNull ${Type}Predicate predicate;

        public Filter(@NotNull ${Type}CollectionView source, @NotNull ${Type}Predicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public @NotNull ${Type}Iterator iterator() {
            return source.iterator().filter(predicate);
        }
<#if IsSpecialized>

        @Override
        public @NotNull ${Type}Stream stream() {
            return source.stream().filter(predicate);
        }

        @Override
        public @NotNull ${Type}Stream parallelStream() {
            return source.stream().filter(predicate).parallel();
        }
</#if>
    }

    public static final class FilterNot extends Abstract${Type}CollectionView {
        private final @NotNull ${Type}CollectionView source;

        private final @NotNull ${Type}Predicate predicate;

        public FilterNot(@NotNull ${Type}CollectionView source, @NotNull ${Type}Predicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public @NotNull ${Type}Iterator iterator() {
            return source.iterator().filterNot(predicate);
        }
<#if IsSpecialized>

        @Override
        public @NotNull ${Type}Stream stream() {
            return source.stream().filter(predicate.negate());
        }

        @Override
        public @NotNull ${Type}Stream parallelStream() {
            return source.stream().filter(predicate.negate()).parallel();
        }
</#if>
    }

    public static final class FlatMapped extends Abstract${Type}CollectionView {
        private final @NotNull ${Type}CollectionView source;
        private final @NotNull ${Type}Function<? extends ${Type}Traversable> mapper;

        public FlatMapped(
                @NotNull ${Type}CollectionView source,
                @NotNull ${Type}Function<? extends ${Type}Traversable> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public @NotNull ${Type}Iterator iterator() {
            return ${Type}Iterator.concat(source.mapToObj(it -> mapper.apply(it).iterator()));
        }
    }
<#if IsSpecialized>

    static final class MappedSpliterator implements Spliterator.Of${Type} {
        private final @NotNull Spliterator.Of${Type} source;
        private final @NotNull ${Type}UnaryOperator mapper;

        MappedSpliterator(@NotNull Spliterator.Of${Type} source, @NotNull ${Type}UnaryOperator mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public boolean tryAdvance(${Type}Consumer action) {
            Objects.requireNonNull(action);
            return source.tryAdvance((${Type}Consumer) (t) -> action.accept(mapper.applyAs${Type}(t)));
        }

        @Override
        public Spliterator.Of${Type} trySplit() {
            final Spliterator.Of${Type} ss = source.trySplit();
            return ss != null ? new MappedSpliterator(ss, mapper) : null;
        }

        @Override
        public long estimateSize() {
            return source.estimateSize();
        }

        @Override
        public int characteristics() {
            return source.characteristics();
        }
    }
</#if>
}
