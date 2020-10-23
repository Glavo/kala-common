package asia.kala.control;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

final class OptionContainerIterator<T> implements Iterator<T>, Spliterator<T> {
    private Object valueRef;

    OptionContainerIterator(Object value) {
        this.valueRef = value;
    }

    @Override
    public boolean hasNext() {
        return valueRef != InternalEmptyTag.INSTANCE;
    }

    @Override
    public T next() {
        if (valueRef == InternalEmptyTag.INSTANCE) {
            throw new NoSuchElementException("OptionIterator.next");
        }
        @SuppressWarnings("unchecked") T v = (T) valueRef;
        valueRef = InternalEmptyTag.INSTANCE;
        return v;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        if (hasNext()) {
            action.accept(next());
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return hasNext() ? 1 : 0;
    }

    @Override
    public int characteristics() {
        return Spliterator.IMMUTABLE | Spliterator.SIZED;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        if (hasNext()) {
            action.accept(next());
        }
    }
}
