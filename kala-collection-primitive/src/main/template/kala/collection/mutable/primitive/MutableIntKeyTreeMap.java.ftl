/*
 * Copyright 2026 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection.mutable.primitive;

import kala.collection.base.primitive.${Name}MapIterator;
import kala.collection.factory.MapBuilder;
import kala.collection.factory.MapFactory;
import kala.collection.primitive.${Name}MapLike;
import kala.collection.primitive.Abstract${Name}Map;
import kala.collection.primitive.Sorted${Name}Map;
import kala.collection.primitive.internal.tree.IntRedBlackTree;
import kala.comparator.primitive.IntComparator;
<#if IsPrimitiveValue>
import kala.control.primitive.IntOption;
<#else>
import kala.control.Option;
</#if>
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/// Stores mappings in a red-black tree with primitive int keys.
/// Lookup, insertion, and removal take O(log n) comparisons. Iteration follows
/// the comparator, or ascending numerical order when no comparator is supplied.
/// Comparator-equivalent keys share a mapping; replacements retain the original key.
/// The comparator requirements are specified by [Sorted${Name}Map].
///
/// This class is not thread-safe. Iterators reject subsequent mutations, including
/// value replacements, on a best-effort basis with [ConcurrentModificationException].
/// Iterator removal is unsupported.
<#if !IsPrimitiveValue>
/// Null values are permitted. Copies share value references, but not tree nodes.
///
/// @param <V> the value type
</#if>
public final class Mutable${Name}TreeMap${Generic} extends Abstract${Name}Map${Generic}
        implements MutableSorted${Name}Map${Generic}, Cloneable {
    /// Privately owned tree storage.
    private final Tree${Generic} tree;
    /// Modification counter checked by iterators.
    private int version;

    /// Creates an empty map using ascending numerical key order.
    public Mutable${Name}TreeMap() {
        this((IntComparator) null);
    }

    /// Creates an empty map using the comparator, or natural order if it is null.
    public Mutable${Name}TreeMap(@Nullable IntComparator comparator) {
        tree = new Tree${Diamond}(comparator);
    }

    /// Takes ownership of an independent tree.
    private Mutable${Name}TreeMap(Tree${Generic} tree) {
        this.tree = tree;
    }

    /// Returns a factory using natural key order.
    public static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, Mutable${Name}TreeMap${Generic}> factory() {
        return Mutable${Name}TreeMap.<#if !IsPrimitiveValue><V></#if>factory(null);
    }

    /// Returns a factory using the given comparator, or natural order if it is null.
    /// Boxed keys<#if IsPrimitiveValue> and values</#if> are unboxed when added.
    public static ${MethodGeneric}@NotNull MapFactory<Integer, ${BoxedValue}, ?, Mutable${Name}TreeMap${Generic}> factory(
            @Nullable IntComparator comparator) {
        return new Factory${Diamond}(comparator);
    }

    /// Creates a builder using natural key order.
    /// The builder returns its mutable accumulation map.
    public static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Mutable${Name}TreeMap${Generic}> newMapBuilder() {
        return Mutable${Name}TreeMap.<#if !IsPrimitiveValue><V></#if>factory().newMapBuilder();
    }

    /// Creates a builder using the given comparator, or natural order if it is null.
    /// The builder returns its mutable accumulation map.
    public static ${MethodGeneric}@NotNull MapBuilder<Integer, ${BoxedValue}, Mutable${Name}TreeMap${Generic}> newMapBuilder(
            @Nullable IntComparator comparator) {
        return Mutable${Name}TreeMap.<#if !IsPrimitiveValue><V></#if>factory(comparator).newMapBuilder();
    }

    /// Creates an empty mutable map using natural key order.
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} create() {
        return of();
    }

    /// Creates an empty mutable map using the comparator, or natural order if it is null.
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} create(@Nullable IntComparator comparator) {
        return of(comparator);
    }

<#list 0..5 as arity>
    /// Creates a map in natural key order; the last value wins for duplicate keys.
<#if arity == 0>
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} of() {
<#else>
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} of(
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
</#if>
        return Mutable${Name}TreeMap.<#if !IsPrimitiveValue><V></#if>of(null<#list 1..arity as index><#if arity gt 0>, key${index}, value${index}</#if></#list>);
    }

    /// Creates a map using the comparator, or natural order if it is null.
    /// Keys comparing as zero share a mapping; the first key and last value are retained.
<#if arity == 0>
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} of(@Nullable IntComparator comparator) {
<#else>
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} of(
            @Nullable IntComparator comparator,
<#list 1..arity as index>
            int key${index}, ${Value} value${index}<#if index != arity>,</#if>
</#list>
    ) {
</#if>
        var result = new Mutable${Name}TreeMap${Generic}(comparator);
<#list 1..arity as index><#if arity gt 0>
        result.set(key${index}, value${index});
</#if></#list>
        return result;
    }

</#list>
    /// Copies parallel arrays into a map using natural key order.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} from(int @NotNull [] keys, ${Value} @NotNull [] values) {
        return from(null, keys, values);
    }

    /// Copies parallel arrays using the comparator, or natural order if it is null.
    /// Comparator-equivalent keys retain the first key and last value.
    /// Subsequent array changes do not affect the map; object values are not cloned.
    ///
    /// @throws IllegalArgumentException if the array lengths differ
    /// @throws NullPointerException if either array is null
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} from(
            @Nullable IntComparator comparator, int @NotNull [] keys, ${Value} @NotNull [] values) {
        Objects.requireNonNull(keys);
        Objects.requireNonNull(values);
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Mismatched array lengths");
        }
        var result = new Mutable${Name}TreeMap${Generic}(comparator);
        for (int i = 0; i < keys.length; i++) {
            result.set(keys[i], values[i]);
        }
        return result;
    }

    /// Copies mappings, preserving the comparator of a [Sorted${Name}Map] source.
    /// Other sources use natural key order. Object values are not cloned.
    ///
    /// @throws NullPointerException if the source is null
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} from(@NotNull ${Name}MapLike${Covariant} source) {
        return from(source instanceof Sorted${Name}Map${Wildcard} sorted ? sorted.comparator() : null, source);
    }

    /// Copies mappings using the comparator, or natural order if it is null.
    /// Comparator-equivalent keys retain the first encountered key and last value.
    /// Object values are not cloned.
    ///
    /// @throws NullPointerException if the source is null
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} from(
            @Nullable IntComparator comparator, @NotNull ${Name}MapLike${Covariant} source) {
        var result = new Mutable${Name}TreeMap${Generic}(comparator);
        result.putAll(source);
        return result;
    }

    /// Copies a Java map using natural key order.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} from(
            java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        return from(null, source);
    }

    /// Copies a Java sorted map, preserving its ordering.
    /// Object values are not cloned.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} from(
            java.util.@NotNull SortedMap<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        var comparator = source.comparator();
        return from(comparator == null ? null
                : comparator instanceof IntComparator primitive ? primitive : comparator::compare, source);
    }

    /// Copies a Java map using the comparator, or natural order if it is null.
    /// Comparator-equivalent keys retain the first encountered key and last value.
    /// Object values are not cloned.
    ///
    /// @throws NullPointerException if the source or a key<#if IsPrimitiveValue> or value</#if> is null
    public static ${MethodGeneric}@NotNull Mutable${Name}TreeMap${Generic} from(
            @Nullable IntComparator comparator,
            java.util.@NotNull Map<Integer, <#if IsPrimitiveValue>Integer<#else>? extends V</#if>> source) {
        var result = new Mutable${Name}TreeMap${Generic}(comparator);
        result.putAll(source);
        return result;
    }

    @Override
    public @Nullable IntComparator comparator() {
        return tree.comparator;
    }

    @Override
    public int firstKey() {
        var node = tree.first();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public int lastKey() {
        var node = tree.last();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public boolean containsKey(int key) {
        return tree.find(key) != null;
    }

    @Override
    public ${Value} get(int key) {
        var node = tree.find(key);
        if (node == null) {
            throw new NoSuchElementException("Missing key: " + key);
        }
        return node.value;
    }

    @Override
    public ${Value} getOrDefault(int key, ${Value} defaultValue) {
        var node = tree.find(key);
        return node == null ? defaultValue : node.value;
    }

    @Override
    public @NotNull ${Option} getOption(int key) {
        var node = tree.find(key);
        return node == null ? ${OptionClass}.none() : ${OptionClass}.some(node.value);
    }

    @Override
    public @NotNull ${Option} put(int key, ${Value} value) {
        var node = tree.find(key);
        if (node != null) {
            var old = ${OptionClass}.some(node.value);
            node.value = value;
            version++;
            return old;
        }
        tree.insert(key, value);
        version++;
        return ${OptionClass}.none();
    }

    @Override
    public void set(int key, ${Value} value) {
        var node = tree.find(key);
        if (node == null) {
            tree.insert(key, value);
        } else {
            node.value = value;
        }
        version++;
    }

    @Override
    public @NotNull ${Option} remove(int key) {
        var node = tree.find(key);
        if (node == null) {
            return ${OptionClass}.none();
        }
        var old = ${OptionClass}.some(node.value);
        tree.delete(node);
        version++;
        return old;
    }

    @Override
    public void clear() {
        if (!tree.isEmpty()) {
            tree.clear();
            version++;
        }
    }

    /// Returns an independent copy with the same comparator in O(n) time.
    /// Object values themselves are not copied.
    @Override
    public @NotNull Mutable${Name}TreeMap${Generic} clone() {
        return new Mutable${Name}TreeMap${Diamond}(tree.copy());
    }

    @Override
    public @NotNull String className() {
        return "Mutable${Name}TreeMap";
    }

    @Override
    public @NotNull ${Name}MapIterator${Generic} iterator() {
        return new EntryIterator();
    }

    /// Visits tree nodes in order and detects mutations to the enclosing map.
    private final class EntryIterator implements ${Name}MapIterator${Generic} {
        /// Modification counter captured at construction.
        private final int expectedVersion = version;
        /// Entry selected by the most recent successful advance.
        private Tree.Node${Generic} current;
        /// Next entry to visit, or null after exhaustion.
        private Tree.Node${Generic} next = tree.first();

        /// Creates an iterator over the current tree.
        private EntryIterator() {
        }

        /// Rejects use after the enclosing map has been modified.
        private void checkVersion() {
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            checkVersion();
            return next != null;
        }

        @Override
        public int nextIntKey() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current = next;
            next = tree.next(current);
            return current.key;
        }

        @Override
        public ${Value} ${ValueGetter}() {
            checkVersion();
            if (current == null) {
                throw new IllegalStateException("No current entry");
            }
            return current.value;
        }
    }

    /// Adapts the shared red-black balancing algorithms to ordered key/value nodes.
<#if !IsPrimitiveValue>
    ///
    /// @param <V> the value type
</#if>
    private static final class Tree${Generic} extends IntRedBlackTree<Tree.Node${Generic}> {
        /// Serialization identifier inherited from the internal tree hierarchy.
        private static final long serialVersionUID = 1L;
        /// Ordering of keys, or null for natural order.
        private final IntComparator comparator;

        /// Creates empty storage using the supplied ordering.
        private Tree(IntComparator comparator) {
            this.comparator = comparator;
        }

        /// Compares two keys without boxing them.
        private int compare(int left, int right) {
            return comparator == null ? Integer.compare(left, right) : comparator.compare(left, right);
        }

        /// Finds the node whose key compares as zero, or returns null if absent.
        private Node${Generic} find(int key) {
            var node = root;
            while (node != null) {
                int comparison = compare(key, node.key);
                if (comparison == 0) {
                    return node;
                }
                node = comparison < 0 ? node.left : node.right;
            }
            return null;
        }

        /// Inserts an absent key; comparator failures leave the tree unchanged.
        private void insert(int key, ${Value} value) {
            Node${Generic} parent = null;
            var node = root;
            int comparison = 0;
            while (node != null) {
                parent = node;
                comparison = compare(key, node.key);
                node = comparison < 0 ? node.left : node.right;
            }
            if (parent == null) {
                compare(key, key);
            }
            var inserted = new Node${Generic}(key, value, parent);
            if (parent == null) {
                root = inserted;
            } else if (comparison < 0) {
                parent.left = inserted;
            } else {
                parent.right = inserted;
            }
            size++;
            fixAfterInsert(inserted);
        }

        /// Removes the selected node and restores red-black balance.
        private void delete(Node${Generic} node) {
            remove0(node);
        }

        /// Returns the first node in comparator order, or null if empty.
        private Node${Generic} first() {
            return firstNode();
        }

        /// Returns the last node in comparator order, or null if empty.
        private Node${Generic} last() {
            return lastNode();
        }

        /// Returns the successor, or null for the last node.
        private Node${Generic} next(Node${Generic} node) {
            return successor(node);
        }

        /// Copies all nodes without invoking the comparator.
        private Tree${Generic} copy() {
            var result = new Tree${Generic}(comparator);
            result.root = copyNode(root, null);
            result.size = size;
            return result;
        }

        /// Recursively copies a subtree, preserving colors and parent links.
        private Node${Generic} copyNode(Node${Generic} source, Node${Generic} parent) {
            if (source == null) {
                return null;
            }
            var copy = new Node${Generic}(source.key, source.value, parent);
            copy.color = source.color;
            copy.left = copyNode(source.left, copy);
            copy.right = copyNode(source.right, copy);
            return copy;
        }

        /// Stores an association and its red-black tree links.
<#if !IsPrimitiveValue>
        ///
        /// @param <V> the value type
</#if>
        private static final class Node${Generic} extends IntRedBlackTree.Node<Node${Generic}> {
            /// Value associated with the inherited key.
            private ${Value} value;

            /// Creates a node attached to the given parent.
            private Node(int key, ${Value} value, Node${Generic} parent) {
                super(key, parent);
                this.value = value;
            }

            /// Copies both key and value when deletion substitutes a successor entry.
            @Override
            public void copyValuesFrom(Node${Generic} other) {
                super.copyValuesFrom(other);
                value = other.value;
            }
        }
    }

    /// Builds maps using a fixed key ordering.
<#if !IsPrimitiveValue>
    ///
    /// @param <V> the value type
</#if>
    private static final class Factory${Generic}
            implements MapFactory<Integer, ${BoxedValue}, Mutable${Name}TreeMap${Generic}, Mutable${Name}TreeMap${Generic}> {
        /// Key ordering, or null for natural order.
        private final IntComparator comparator;

        /// Creates a factory with the supplied key ordering.
        private Factory(IntComparator comparator) {
            this.comparator = comparator;
        }

        @Override
        public Mutable${Name}TreeMap${Generic} newBuilder() {
            return new Mutable${Name}TreeMap${Diamond}(comparator);
        }

        @Override
        public Mutable${Name}TreeMap${Generic} build(Mutable${Name}TreeMap${Generic} builder) {
            return builder;
        }

        @Override
        public void addToBuilder(Mutable${Name}TreeMap${Generic} builder, Integer key, ${BoxedValue} value) {
            builder.set(key, value);
        }

        @Override
        public Mutable${Name}TreeMap${Generic} mergeBuilder(
                Mutable${Name}TreeMap${Generic} builder1, Mutable${Name}TreeMap${Generic} builder2) {
            builder1.putAll(builder2);
            return builder1;
        }
    }
}
