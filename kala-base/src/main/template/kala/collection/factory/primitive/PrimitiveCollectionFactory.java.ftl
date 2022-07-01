package kala.collection.factory.primitive;

import kala.annotations.Covariant;
import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.base.primitive.${Type}Traversable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public interface ${Type}CollectionFactory<Builder, @Covariant R> extends PrimitiveCollectionFactory<${WrapperType}, Builder, R> {
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
