package org.glavo.kala.factory;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

class MappedFactory<F extends Factory<Builder, R>, Builder, R, NewR>
        implements Factory<Builder, NewR> {
    protected final @NotNull F source;

    protected final @NotNull Function<? super R, ? extends NewR> mapper;

    MappedFactory(@NotNull F source, @NotNull Function<? super R, ? extends NewR> mapper) {
        this.source = source;
        this.mapper = mapper;
    }


    @Override
    public final Builder newBuilder() {
        return source.newBuilder();
    }

    @Override
    public final NewR build(@NotNull Builder builder) {
        return mapper.apply(source.build(builder));
    }
}