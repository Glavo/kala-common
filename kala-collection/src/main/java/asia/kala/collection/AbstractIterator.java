package asia.kala.collection;

import java.util.Iterator;

public abstract class AbstractIterator<E> implements Iterator<E> {
    @Override
    public String toString() {
        if(hasNext()) {
            return "<non-empty iterator>";
        }
        return "<empty iterator>";
    }
}
