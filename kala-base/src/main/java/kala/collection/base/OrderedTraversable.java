package kala.collection.base;

/**
 * A collection of elements that exist in some order.
 * <p>
 * This order can be insertion order, sort order, or any other meaningful order,
 * its iterators and iteration methods should follow this order.
 */
public interface OrderedTraversable<E> extends Traversable<E> {
}
