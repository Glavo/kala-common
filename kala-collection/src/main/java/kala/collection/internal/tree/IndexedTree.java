/*
 * Copyright (c) 2008 Harold Cooper. All rights reserved.
 * Licensed under the MIT License.
 * See LICENSE file in the project root for full license information.
 */

package kala.collection.internal.tree;

import kala.collection.base.AbstractIterator;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// https://github.com/hrldcpr/pcollections/blob/master/src/main/java/org/pcollections/IntTree.java
public final class IndexedTree<V> implements Iterable<V> {

    // marker value:
    private static final IndexedTree<?> EMPTY_NODE = new IndexedTree<>();

    @SuppressWarnings("unchecked")
    public static <E> IndexedTree<E> empty() {
        return (IndexedTree<E>) EMPTY_NODE;
    }

    // we use longs so relative keys can express all ints
    // (e.g. if this has key -10 and right has 'absolute' key MAXINT,
    // then its relative key is MAXINT+10 which overflows)
    // there might be some way to deal with this based on left-verse-right logic,
    // but that sounds like a mess.
    private final long key;

    private final V value;
    private final IndexedTree<V> left, right;
    private final int size;

    private IndexedTree() {
        size = 0;

        key = 0;
        value = null;
        left = this;
        right = this;
    }

    public IndexedTree(final long key, final V value, final IndexedTree<V> left, final IndexedTree<V> right) {
        this.key = key;
        this.value = value;
        this.left = left;
        this.right = right;
        this.size = 1 + left.size + right.size;
    }

    private IndexedTree<V> withKey(final long newKey) {
        if (size == 0 || newKey == key) return this;
        return new IndexedTree<>(newKey, value, left, right);
    }

    @Override
    public @NotNull Iterator<V> iterator() {
        return new Itr<>(this);
    }

    public int size() {
        return size;
    }

    public boolean containsKey(final long key) {
        if (size == 0) return false;

        if (key < this.key) return left.containsKey(key - this.key);
        if (key > this.key) return right.containsKey(key - this.key);
        // otherwise key==this.key:
        return true;
    }

    public V get(final long key) {
        if (size == 0) return null;
        if (key < this.key) return left.get(key - this.key);
        if (key > this.key) return right.get(key - this.key);
        // otherwise key==this.key:
        return value;
    }

    public IndexedTree<V> plus(final long key, final V value) {
        if (size == 0) return new IndexedTree<>(key, value, this, this);
        if (key < this.key) return rebalanced(left.plus(key - this.key, value), right);
        if (key > this.key) return rebalanced(left, right.plus(key - this.key, value));
        // otherwise key==this.key, so we simply replace this, with no effect on balance:
        if (value == this.value) return this;
        return new IndexedTree<>(key, value, left, right);
    }

    public IndexedTree<V> minus(final long key) {
        if (size == 0) return this;
        if (key < this.key) return rebalanced(left.minus(key - this.key), right);
        if (key > this.key) return rebalanced(left, right.minus(key - this.key));

        // otherwise key==this.key, so we are killing this node:

        if (left.size == 0) // we can just become right node
            // make key 'absolute':
            return right.withKey(right.key + this.key);
        if (right.size == 0) // we can just become left node
            return left.withKey(left.key + this.key);

        // otherwise replace this with the next key (i.e. the smallest key to the right):

        long newKey = right.minKey() + this.key;
        // (right.minKey() is relative to this; adding this.key makes it 'absolute'
        //	where 'absolute' really means relative to the parent of this)

        V newValue = right.get(newKey - this.key);
        // now that we've got the new stuff, take it out of the right subtree:
        IndexedTree<V> newRight = right.minus(newKey - this.key);

        // lastly, make the subtree keys relative to newKey (currently they are relative to this.key):
        newRight = newRight.withKey((newRight.key + this.key) - newKey);
        // left is definitely not empty:
        IndexedTree<V> newLeft = left.withKey((left.key + this.key) - newKey);

        return rebalanced(newKey, newValue, newLeft, newRight);
    }

    /**
     * Changes every key k>=key to k+delta.
     *
     * <p>This method will create an _invalid_ tree if delta&lt;0 and the distance between the smallest
     * k&gt;=key in this and the largest j&lt;key in this is |delta| or less.
     *
     * <p>In other words, this method must not result in any change in the order of the keys in this,
     * since the tree structure is not being changed at all.
     */
    public IndexedTree<V> changeKeysAbove(final long key, final int delta) {
        if (size == 0 || delta == 0) return this;

        if (this.key >= key)
            // adding delta to this.key changes the keys of _all_ children of this,
            // so we now need to un-change the children of this smaller than key,
            // all of which are to the left. note that we still use the 'old' relative key...:
            return new IndexedTree<>(
                    this.key + delta, value, left.changeKeysBelow(key - this.key, -delta), right);

        // otherwise, doesn't apply yet, look to the right:
        IndexedTree<V> newRight = right.changeKeysAbove(key - this.key, delta);
        if (newRight == right) return this;
        return new IndexedTree<>(this.key, value, left, newRight);
    }

    /**
     * Changes every key k&lt;key to k+delta.
     *
     * <p>This method will create an _invalid_ tree if delta&gt;0 and the distance between the largest
     * k<key in this and the smallest j>=key in this is delta or less.
     *
     * <p>In other words, this method must not result in any overlap or change in the order of the
     * keys in this, since the tree _structure_ is not being changed at all.
     */
    public IndexedTree<V> changeKeysBelow(final long key, final int delta) {
        if (size == 0 || delta == 0) return this;

        if (this.key < key)
            // adding delta to this.key changes the keys of _all_ children of this,
            // so we now need to un-change the children of this larger than key,
            // all of which are to the right. note that we still use the 'old' relative key...:
            return new IndexedTree<>(
                    this.key + delta, value, left, right.changeKeysAbove(key - this.key, -delta));

        // otherwise, doesn't apply yet, look to the left:
        IndexedTree<V> newLeft = left.changeKeysBelow(key - this.key, delta);
        if (newLeft == left) return this;
        return new IndexedTree<>(this.key, value, newLeft, right);
    }

    // min key in this:
    private long minKey() {
        if (left.size == 0) return key;
        // make key 'absolute' (i.e. relative to the parent of this):
        return left.minKey() + this.key;
    }

    private IndexedTree<V> rebalanced(final IndexedTree<V> newLeft, final IndexedTree<V> newRight) {
        if (newLeft == left && newRight == right) return this; // already balanced
        return rebalanced(key, value, newLeft, newRight);
    }

    private static final int OMEGA = 5;
    private static final int ALPHA = 2;

    // rebalance a tree that is off-balance by at most 1:
    private static <V> IndexedTree<V> rebalanced(
            final long key, final V value, final IndexedTree<V> left, final IndexedTree<V> right) {
        if (left.size + right.size > 1) {
            if (left.size >= OMEGA * right.size) { // rotate to the right
                IndexedTree<V> ll = left.left, lr = left.right;
                if (lr.size < ALPHA * ll.size) // single rotation
                    return new IndexedTree<>(
                            left.key + key,
                            left.value,
                            ll,
                            new IndexedTree<>(-left.key, value, lr.withKey(lr.key + left.key), right));
                else { // double rotation:
                    IndexedTree<V> lrl = lr.left, lrr = lr.right;
                    return new IndexedTree<>(
                            lr.key + left.key + key,
                            lr.value,
                            new IndexedTree<>(-lr.key, left.value, ll, lrl.withKey(lrl.key + lr.key)),
                            new IndexedTree<>(
                                    -left.key - lr.key, value, lrr.withKey(lrr.key + lr.key + left.key), right));
                }
            } else if (right.size >= OMEGA * left.size) { // rotate to the left
                IndexedTree<V> rl = right.left, rr = right.right;
                if (rl.size < ALPHA * rr.size) // single rotation
                    return new IndexedTree<>(
                            right.key + key,
                            right.value,
                            new IndexedTree<>(-right.key, value, left, rl.withKey(rl.key + right.key)),
                            rr);
                else { // double rotation:
                    IndexedTree<V> rll = rl.left, rlr = rl.right;
                    return new IndexedTree<>(
                            rl.key + right.key + key,
                            rl.value,
                            new IndexedTree<>(
                                    -right.key - rl.key, value, left, rll.withKey(rll.key + rl.key + right.key)),
                            new IndexedTree<>(-rl.key, right.value, rlr.withKey(rlr.key + rl.key), rr));
                }
            }
        }
        // otherwise already balanced enough:
        return new IndexedTree<>(key, value, left, right);
    }

    private static final class Itr<V> extends AbstractIterator<V> {
        private final List<IndexedTree<V>> stack = new ArrayList<>(); // path of nonempty nodes
        private int key = 0; // note we use _int_ here since this is a truly absolute key

        Itr(final IndexedTree<V> root) {
            gotoMinOf(root);
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public V next() {
            IndexedTree<V> node = stack.getLast();
            final V result = node.value;

            // find next node.
            // we've already done everything smaller,
            // so try least larger node:

            if (node.right.size > 0) // we can descend to the right
                gotoMinOf(node.right);
            else // can't descend to the right -- try ascending to the right
                while (true) { // find current node's least larger ancestor, if any
                    key -= node.key; // revert to parent's key
                    stack.removeLast(); // climb up to parent
                    // if parent was larger than child or there was no parent, we're done:
                    if (node.key < 0 || stack.isEmpty()) break;
                    // otherwise parent was smaller -- try its parent:
                    node = stack.getLast();
                }

            return result;
        }

        // extend the stack to its least non-empty node:
        private void gotoMinOf(IndexedTree<V> node) {
            while (node.size > 0) {
                stack.add(node);
                key += node.key;
                node = node.left;
            }
        }
    }
}
