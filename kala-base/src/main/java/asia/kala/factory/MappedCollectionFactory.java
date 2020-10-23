package asia.kala.factory;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

class MappedCollectionFactory<F extends CollectionFactory<E, Builder, R>, E, Builder, R, NewR>
        extends MappedFactory<F, Builder, R, NewR> implements CollectionFactory<E, Builder, NewR> {

    MappedCollectionFactory(@NotNull F source, @NotNull Function<? super R, ? extends NewR> mapper) {
        super(source, mapper);
    }

    @Override
    public final void addToBuilder(@NotNull Builder builder, E value) {
        source.addToBuilder(builder, value);
    }

    @Override
    public final Builder mergeBuilder(@NotNull Builder builder1, @NotNull Builder builder2) {
        return source.mergeBuilder(builder1, builder2);
    }

    @Override
    public final void sizeHint(@NotNull Builder builder, int size) {
        source.sizeHint(builder, size);
    }
}
