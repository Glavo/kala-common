package kala.collection.factory;

import kala.annotations.Covariant;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public interface Factory<Builder, @Covariant R> {
    Builder newBuilder();

    R build(Builder builder);

    default <U> @NotNull Factory<Builder, U> mapResult(@NotNull Function<? super R, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        final class MappedFactory implements Factory<Builder, U> {
            @Override
            public final Builder newBuilder() {
                return Factory.this.newBuilder();
            }

            @Override
            public final U build(@NotNull Builder builder) {
                return mapper.apply(Factory.this.build(builder));
            }
        }

        return new MappedFactory();
    }
}
