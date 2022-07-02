package kala.collection;

import kala.control.Option;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractSeq<E> extends AbstractCollection<E> implements Seq<E> {

    public E first() {
        return Seq.super.first();
    }

    public @Nullable E firstOrNull() {
        return Seq.super.firstOrNull();
    }

    public @NotNull Option<E> firstOption() {
        return Seq.super.firstOption();
    }

    public E last() {
        return Seq.super.last();
    }

    public @Nullable E lastOrNull() {
        return Seq.super.lastOrNull();
    }

    public @NotNull Option<E> lastOption() {
        return Seq.super.lastOption();
    }

    @Override
    public int hashCode() {
        return Seq.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Seq<?> && Seq.equals(this, ((Seq<?>) obj));
    }
}
