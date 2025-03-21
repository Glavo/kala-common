/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection;

import kala.annotations.Covariant;
import kala.annotations.DelegateBy;
import kala.collection.base.AnyTraversable;
import kala.collection.immutable.*;
import kala.collection.internal.CollectionHelper;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.factory.CollectionFactory;
import kala.collection.internal.view.CollectionViews;
import kala.function.CheckedBiConsumer;
import kala.function.CheckedBiFunction;
import kala.function.CheckedConsumer;
import kala.function.CheckedFunction;
import kala.function.CheckedPredicate;
import kala.function.Predicates;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.*;

public interface Collection<@Covariant E> extends CollectionLike<E>, AnyCollection<E> {

    //region Static Factories

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <E> Collection<E> narrow(Collection<? extends E> collection) {
        return (Collection<E>) collection;
    }

    @Contract(pure = true)
    static <E> @NotNull CollectionFactory<E, ?, Collection<E>> factory() {
        return CollectionFactory.narrow(ImmutableCollection.factory());
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "Collection";
    }

    default <U> @NotNull CollectionFactory<U, ?, ? extends Collection<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull CollectionView<E> view() {
        return isEmpty() ? CollectionView.empty() : new CollectionViews.Of<>(this);
    }

    default java.util.@NotNull @UnmodifiableView Collection<E> asJava() {
        return new AsJavaConvert.CollectionAsJava<>(this);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filter(@NotNull Predicate<? super E> predicate) {
        return filter(CollectionHelper.immutableCollectionFactoryBy(this), predicate);
    }

    @Contract(pure = true)
    default @NotNull <Ex extends Throwable> ImmutableCollection<E> filterChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) throws Ex {
        return filter(predicate);
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filterUnchecked(
            @NotNull CheckedPredicate<? super E, ?> predicate) {
        return filter(predicate);
    }

    @Override
    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default @NotNull ImmutableCollection<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filter(predicate.negate());
    }

    @Contract(pure = true)
    default @NotNull <Ex extends Throwable> ImmutableCollection<E> filterNotChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) throws Ex {
        return filterNot(predicate);
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filterNotUnchecked(
            @NotNull CheckedPredicate<? super E, ?> predicate) {
        return filterNot(predicate);
    }

    @Override
    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default @NotNull ImmutableCollection<@NotNull E> filterNotNull() {
        return filter(Predicates.isNotNull());
    }

    @Override
    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default <U> @NotNull ImmutableCollection<U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        @SuppressWarnings("unchecked")
        ImmutableCollection<U> result = (ImmutableCollection<U>) filter(Predicates.isInstance(clazz));
        return result;
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return map(CollectionHelper.<U>immutableCollectionFactoryBy(this), mapper);
    }

    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableCollection<U> mapChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return map(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return map(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return mapNotNull(CollectionHelper.<U>immutableCollectionFactoryBy(this), mapper);
    }

    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableCollection<U> mapNotNullChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapNotNull(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return mapNotNull(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        return mapMulti(CollectionHelper.immutableCollectionFactoryBy(this), mapper);
    }

    @Contract(pure = true)
    @DelegateBy("mapMulti(BiConsumer<E, Consumer<U>>)")
    default <U, Ex extends Throwable> @NotNull ImmutableCollection<U> mapMultiChecked(
            @NotNull CheckedBiConsumer<? super E, ? super Consumer<? super U>, Ex> mapper) throws Ex {
        return mapMulti(mapper);
    }

    @Contract(pure = true)
    @DelegateBy("mapMulti(BiConsumer<E, Consumer<U>>)")
    default <U> @NotNull ImmutableCollection<U> mapMultiUnchecked(
            @NotNull CheckedBiConsumer<? super E, ? super Consumer<? super U>, ?> mapper) {
        return mapMulti(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return flatMap(CollectionHelper.immutableCollectionFactoryBy(this), mapper);
    }

    @Contract(pure = true)
    @DelegateBy("flatMap(Function<E, Iterable<U>>)")
    default <U, Ex extends Throwable> @NotNull ImmutableCollection<U> flatMapChecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ? extends Ex> mapper) throws Ex {
        return flatMap(mapper);
    }

    @Contract(pure = true)
    @DelegateBy("flatMap(Function<E, Iterable<U>>)")
    default <U> @NotNull ImmutableCollection<U> flatMapUnchecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ?> mapper) {
        return flatMap(mapper);
    }

    default <R> Tuple2<? extends ImmutableCollection<E>, ? extends ImmutableCollection<E>> partition(@NotNull Predicate<? super E> predicate) {
        return partition(ImmutableSeq.factory(), predicate);
    }

    @Contract(pure = true)
    @DelegateBy("zip(Iterable<U>, BiFunction<E, U, R>)")
    default <U> @NotNull ImmutableCollection<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return zip(other, Tuple::of);
    }

    @Contract(pure = true)
    default <U, R> @NotNull ImmutableCollection<R> zip(@NotNull Iterable<? extends U> other, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(other);
        Objects.requireNonNull(mapper);

        var factory = CollectionHelper.<R>immutableCollectionFactoryBy(this);

        if (this.isEmpty() || AnyTraversable.knownSize(other) == 0) {
            return factory.empty();
        }

        var builder = factory.newCollectionBuilder();
        Iterator<? extends E> it1 = this.iterator();
        Iterator<? extends U> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            builder.plusAssign(mapper.apply(it1.next(), it2.next()));
        }
        return builder.build();
    }

    @Contract(pure = true)
    @DelegateBy("zip(Iterable<U>, BiFunction<E, U, R>)")
    default <U, R, Ex extends Throwable> @NotNull ImmutableCollection<R> zipChecked(
            @NotNull Iterable<? extends U> other,
            @NotNull CheckedBiFunction<? super E, ? super U, ? extends R, ? extends Ex> mapper) throws Ex {
        return zip(other, mapper);
    }

    @Contract(pure = true)
    @DelegateBy("zip(Iterable<U>, BiFunction<E, U, R>)")
    default <U, R> @NotNull ImmutableCollection<R> zipUnchecked(
            @NotNull Iterable<? extends U> other,
            @NotNull CheckedBiFunction<? super E, ? super U, ? extends R, ?> mapper) {
        return zip(other, mapper);
    }

    @Contract(pure = true)
    default <U, V> @NotNull ImmutableCollection<@NotNull Tuple3<E, U, V>> zip3(@NotNull Iterable<? extends U> other1, @NotNull Iterable<? extends V> other2) {
        Objects.requireNonNull(other1);
        Objects.requireNonNull(other2);

        var factory = CollectionHelper.<Tuple3<E, U, V>>immutableCollectionFactoryBy(this);

        if (this.isEmpty() || AnyTraversable.knownSize(other1) == 0 || AnyTraversable.knownSize(other2) == 0) {
            return factory.empty();
        }

        var builder = factory.newCollectionBuilder();
        Iterator<? extends E> it1 = this.iterator();
        Iterator<? extends U> it2 = other1.iterator();
        Iterator<? extends V> it3 = other2.iterator();

        while (it1.hasNext() && it2.hasNext() && it3.hasNext()) {
            builder.plusAssign(Tuple.of(it1.next(), it2.next(), it3.next()));
        }
        return builder.build();
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> distinct() {
        return distinct(ImmutableSeq.factory());
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull Collection<E> onEach(@NotNull Consumer<? super E> action) {
        forEach(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull Collection<E> onEachChecked(@NotNull CheckedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull Collection<E> onEachUnchecked(@NotNull CheckedConsumer<? super E, ?> action) {
        return onEach(action);
    }

    final class SerializationWrapper<E, C extends Collection<E>> implements Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

        private final CollectionFactory<E, ?, C> factory;
        private transient C value;

        public SerializationWrapper(CollectionFactory<E, ?, C> factory, C value) {
            this.factory = factory;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        private static <E, B, C extends Collection<E>> C readObjectImpl(ObjectInputStream input, CollectionFactory<E, B, C> factory, int size) throws IOException, ClassNotFoundException {
            if (size < 0) {
                throw new IOException("Invalid size: " + size);
            }

            if (size == 0) {
                return factory.empty();
            }

            B builder = factory.newBuilder(size);
            for (int i = 0; i < size; i++) {
                factory.addToBuilder(builder, (E) input.readObject());
            }
            return factory.build(builder);
        }

        @Serial
        private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
            input.defaultReadObject();
            value = readObjectImpl(input, factory, input.readInt());
        }

        @Serial
        private void writeObject(ObjectOutputStream output) throws IOException {
            output.defaultWriteObject();
            int size = value.size();

            output.writeInt(size);
            if (size == 0) {
                return;
            }

            Iterator<E> iterator = value.iterator();
            for (int i = 0; i < size; i++) {
                if (!iterator.hasNext()) {
                    throw new IOException("No more elements");
                }

                output.writeObject(iterator.next());
            }
        }

        @Serial
        private Object readResolve() {
            return value;
        }
    }
}
