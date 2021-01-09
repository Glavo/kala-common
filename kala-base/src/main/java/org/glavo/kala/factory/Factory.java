package org.glavo.kala.factory;

import org.glavo.kala.annotations.Covariant;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public interface Factory<Builder, @Covariant R> {
    Builder newBuilder();

    R build(@NotNull Builder builder);

    default <U> @NotNull Factory<Builder, U> mapResult(@NotNull Function<? super R, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new MappedFactory<>(this, mapper);
    }
}