package kala.collection;

import kala.control.Option;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractSeq<E> extends AbstractCollection<E> implements Seq<E> {

    public E getFirst() {
        return Seq.super.getFirst();
    }

    public @Nullable E getFirstOrNull() {
        return Seq.super.getFirstOrNull();
    }

    public @NotNull Option<E> getFirstOption() {
        return Seq.super.getFirstOption();
    }

    public E getLast() {
        return Seq.super.getLast();
    }

    public @Nullable E getLastOrNull() {
        return Seq.super.getLastOrNull();
    }

    public @NotNull Option<E> getLastOption() {
        return Seq.super.getLastOption();
    }

    @Override
    public int hashCode() {
        return Seq.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnySeq<?> && Seq.equals(this, ((AnySeq<?>) obj));
    }
}
