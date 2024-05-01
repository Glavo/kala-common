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
package kala.collection.factory.primitive;

import kala.annotations.Covariant;
import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.base.primitive.${Type}Traversable;
import kala.function.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public interface ${Type}CollectionFactory<Builder, @Covariant R> extends PrimitiveCollectionFactory<${WrapperType}, Builder, R> {

    @SuppressWarnings("unchecked")
    static <Builder, R> ${Type}CollectionFactory<Builder, R> narrow(${Type}CollectionFactory<Builder, ? extends R> factory) {
        return (${Type}CollectionFactory<Builder, R>) factory;
    }

    @ApiStatus.Experimental
    static <Builder, R> R buildBy(@NotNull ${Type}CollectionFactory<Builder, R> factory, Consumer<${Type}Consumer> consumer) {
        Builder builder = factory.newBuilder();
        consumer.accept(value -> factory.addToBuilder(builder, value));
        return factory.build(builder);
    }

    @ApiStatus.Experimental
    default @NotNull ${Type}CollectionBuilder<R> newCollectionBuilder(Builder builder) {
        return new ${Type}CollectionBuilder<R>() {
            @Override
            public void plusAssign(${PrimitiveType} value) {
                ${Type}CollectionFactory.this.addToBuilder(builder, value);
            }

            @Override
            public void sizeHint(int size) {
                ${Type}CollectionFactory.this.sizeHint(builder, size);
            }

            @Override
            public R build() {
                return ${Type}CollectionFactory.this.build(builder);
            }
        };
    }

    @ApiStatus.Experimental
    default @NotNull ${Type}CollectionBuilder<R> newCollectionBuilder() {
        return newCollectionBuilder(newBuilder());
    }

    @ApiStatus.Experimental
    default @NotNull ${Type}CollectionBuilder<R> newCollectionBuilder(int capacity) {
        return newCollectionBuilder(newBuilder(capacity));
    }

    void addToBuilder(@NotNull Builder builder, ${PrimitiveType} value);

    @Override
    default void addToBuilder(@NotNull Builder builder, ${WrapperType} value) {
        addToBuilder(builder, value.${PrimitiveType}Value());
    }

    default void addAllToBuilder(@NotNull Builder builder, @NotNull ${Type}Traversable values) {
        addAllToBuilder(builder, values.iterator());
    }

    default void addAllToBuilder(@NotNull Builder builder, @NotNull ${Type}Iterator it) {
        while (it.hasNext()) {
            addToBuilder(builder, it.next${Type}());
        }
    }

    default void addAllToBuilder(@NotNull Builder builder, ${PrimitiveType} @NotNull [] values) {
        Objects.requireNonNull(values);
        for (${PrimitiveType} value : values) {
            addToBuilder(builder, value);
        }
    }

    default R fill(int n, ${PrimitiveType} value) {
        if (n <= 0) {
            return empty();
        }

        Builder builder = newBuilder();
        sizeHint(builder, n);
        for (int i = 0; i < n; i++) {
            addToBuilder(builder, value);
        }
        return build(builder);
    }

    default R fill(int n, ${Type}Supplier supplier) {
        if (n <= 0) {
            return empty();
        }

        Builder builder = newBuilder();
        sizeHint(builder, n);
        for (int i = 0; i < n; i++) {
            addToBuilder(builder, supplier.getAs${Type}());
        }
        return build(builder);
    }

    @Override
    default R fill(int n, ${WrapperType} value) {
        return fill(n, value.${PrimitiveType}Value());
    }

    default R from(${PrimitiveType} @NotNull [] values) {
        if (values.length == 0) {
            return empty();
        }

        Builder builder = newBuilder();
        sizeHint(builder, values.length);
        addAllToBuilder(builder, values);
        return build(builder);
    }

    default R from(@NotNull ${Type}Traversable values) {
        Builder builder = newBuilder();
        sizeHint(builder, values);
        addAllToBuilder(builder, values);
        return build(builder);
    }

    default R from(@NotNull ${Type}Iterator it) {
        if (!it.hasNext()) {
            return empty();
        }
        Builder builder = newBuilder();
        addAllToBuilder(builder, it);
        return build(builder);
    }

    @Override
    default @NotNull <U> ${Type}CollectionFactory<Builder, U> mapResult(@NotNull Function<? super R, ? extends U> mapper) {
        ${Type}CollectionFactory<Builder, R> self = this;
        return new ${Type}CollectionFactory<Builder, U>() {
            @Override
            public void addToBuilder(@NotNull Builder builder, ${PrimitiveType} value) {
                self.addToBuilder(builder, value);
            }

            @Override
            public Builder newBuilder() {
                return self.newBuilder();
            }

            @Override
            public U build(Builder builder) {
                return mapper.apply(self.build(builder));
            }

            @Override
            public Builder mergeBuilder(@NotNull Builder builder1, @NotNull Builder builder2) {
                return self.mergeBuilder(builder1, builder2);
            }
        };
    }
}
