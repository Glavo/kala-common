package asia.kala.collection.immutable;

import asia.kala.collection.mutable.ArrayBuffer;
import asia.kala.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

final class ImmutableSeqFactory<E> implements CollectionFactory<E, ArrayBuffer<E>, ImmutableSeq<E>> {
    static final ImmutableSeqFactory<?> INSTANCE = new ImmutableSeqFactory<>();

    private ImmutableSeqFactory() {
    }

    @Override
    public final ArrayBuffer<E> newBuilder() {
        return new ArrayBuffer<>();
    }

    @Override
    public final void addToBuilder(@NotNull ArrayBuffer<E> buffer, E value) {
        buffer.append(value);
    }

    @Override
    public final ArrayBuffer<E> mergeBuilder(@NotNull ArrayBuffer<E> builder1, @NotNull ArrayBuffer<E> builder2) {
        builder1.appendAll(builder2);
        return builder1;
    }

    @Override
    public final void sizeHint(@NotNull ArrayBuffer<E> buffer, int size) {
        buffer.sizeHint(size);
    }

    @Override
    public final ImmutableSeq<E> build(@NotNull ArrayBuffer<E> buffer) {
        switch (buffer.size()) {
            case 0:
                return ImmutableSeq0.instance();
            case 1:
                return new ImmutableSeq1<>(buffer.get(0));
            case 2:
                return new ImmutableSeq2<>(buffer.get(0), buffer.get(1));
            case 3:
                return new ImmutableSeq3<>(buffer.get(0), buffer.get(1), buffer.get(2));
            case 4:
                return new ImmutableSeq4<>(buffer.get(0), buffer.get(1), buffer.get(2), buffer.get(3));
            case 5:
                return new ImmutableSeq5<>(buffer.get(0), buffer.get(1), buffer.get(2), buffer.get(3), buffer.get(4));
            default:
                return ImmutableVector.from(buffer);
        }
    }
}
