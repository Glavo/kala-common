package kala.collection;

import kala.control.Option;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractSeq<E> extends AbstractCollection<E> implements Seq<E> {

    public E first() {
        return firstOption().get();
    }

    public @Nullable E firstOrNull() {
        return firstOption().getOrNull();
    }

    public @NotNull Option<E> firstOption() {
        return isNotEmpty() ? Option.some(iterator().next()) : Option.none();
    }

    public E last() {
        return lastOption().get();
    }

    public @Nullable E lastOrNull() {
        return lastOption().getOrNull();
    }

    public @NotNull Option<E> lastOption() {
        return isNotEmpty() ? Option.some(reverseIterator().next()) : Option.none();
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
