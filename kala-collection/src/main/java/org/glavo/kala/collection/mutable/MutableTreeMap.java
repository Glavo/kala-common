package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.SortedMap;
import org.glavo.kala.collection.base.AbstractIterator;
import org.glavo.kala.collection.base.AbstractMapIterator;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.base.MapIterator;
import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.control.Option;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;


public final class MutableTreeMap<K, V> extends RedBlackTree<K, MutableTreeMap.Node<K, V>>
        implements MutableMap<K, V>, SortedMap<K, V> {

    public MutableTreeMap() {
        this(null);
    }

    public MutableTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

    //region Static Factories

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> of() {
        return new MutableTreeMap<>();
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> of(K k1, V v1) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>();
        m.set(k1, v1);
        return m;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        return m;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        return m;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        m.set(k4, v4);
        return m;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        m.set(k4, v4);
        m.set(k5, v5);
        return m;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> of(Comparator<? super K> comparator) {
        return new MutableTreeMap<>(comparator);
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> of(Comparator<? super K> comparator, K k1, V v1) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>(comparator);
        m.set(k1, v1);
        return m;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> of(
            Comparator<? super K> comparator,
            K k1, V v1,
            K k2, V v2
    ) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>(comparator);
        m.set(k1, v1);
        m.set(k2, v2);
        return m;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> of(
            Comparator<? super K> comparator,
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>(comparator);
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        return m;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> of(
            Comparator<? super K> comparator,
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>(comparator);
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        m.set(k4, v4);
        return m;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> of(
            Comparator<? super K> comparator,
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>(comparator);
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        m.set(k4, v4);
        m.set(k5, v5);
        return m;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> ofEntries() {
        return new MutableTreeMap<>();
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>();
        res.set(entry1);
        return res;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>();
        res.set(entry1);
        res.set(entry2);
        return res;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        return res;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        res.set(entry4);
        return res;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        res.set(entry4);
        res.set(entry5);
        return res;
    }

    @SafeVarargs
    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            res.set(entry);
        }
        return res;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> ofEntries(Comparator<? super K> comparator) {
        return new MutableTreeMap<>(comparator);
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>(comparator);
        res.set(entry1);
        return res;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>(comparator);
        res.set(entry1);
        res.set(entry2);
        return res;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>(comparator);
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        return res;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>(comparator);
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        res.set(entry4);
        return res;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>(comparator);
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        res.set(entry4);
        res.set(entry5);
        return res;
    }

    @SafeVarargs
    public static <K, V> @NotNull MutableTreeMap<K, V> ofEntries(Comparator<? super K> comparator, Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        MutableTreeMap<K, V> res = new MutableTreeMap<>(comparator);
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            res.set(entry);
        }
        return res;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>();
        m.putAll(values);
        return m;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> from(@NotNull org.glavo.kala.collection.Map<? extends K, ? extends V> values) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>();
        m.putAll(values);
        return m;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> @NotNull MutableTreeMap<K, V> from(java.util.@NotNull SortedMap<? extends K, ? extends V> values) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>((Comparator<K>) values.comparator());
        m.putAll(values);
        return m;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> @NotNull MutableTreeMap<K, V> from(@NotNull SortedMap<? extends K, ? extends V> values) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>((Comparator<K>) values.comparator());
        m.putAll(values);
        return m;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.set(value.getKey(), value.getValue());
        }
        return m;
    }

    public static <K extends Comparable<? super K>, V> @NotNull MutableTreeMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.set(value.getKey(), value.getValue());
        }
        return m;
    }

    public static <K, V> @NotNull MutableTreeMap<K, V> from(
            Comparator<? super K> comparator,
            java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values
            ) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>(comparator);
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.set(value.getKey(), value.getValue());
        }
        return m;
    }

    public static <K , V> @NotNull MutableTreeMap<K, V> from(
            Comparator<? super K> comparator,
            @NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        MutableTreeMap<K, V> m = new MutableTreeMap<>(comparator);
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.set(value.getKey(), value.getValue());
        }
        return m;
    }

    //endregion

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "MutableTreeMap";
    }

    @Override
    public final @NotNull MapIterator<K, V> iterator() {
        Node<K, V> node = firstNode();
        return node == null ? MapIterator.empty() : new Itr<>(node);
    }

    @Override
    public final @NotNull Map<K, V> asJava() {
        return new AsJava<>(this);
    }

    //endregion

    @Override
    public final V get(K key) {
        Node<K, V> node = getNode(key);
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.value;
    }

    @Override
    public final @Nullable V getOrNull(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public final @NotNull Option<V> getOption(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? Option.none() : Option.some(node.value);
    }

    @Override
    public final V getOrDefault(K key, V defaultValue) {
        Node<K, V> node = getNode(key);
        return node == null ? defaultValue : node.value;
    }

    @Override
    public final V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
        Node<K, V> node = getNode(key);
        return node == null ? supplier.get() : node.value;
    }

    @Override
    public final V getOrPut(K key, @NotNull Supplier<? extends V> defaultValue) {
        Node<K, V> node = getNode(key);
        if (node == null) {
            V dv = defaultValue.get();
            set(key, dv);
            return dv;
        }
        return node.value;
    }

    @Override
    public final <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
        Node<K, V> node = getNode(key);
        if (node == null) {
            throw supplier.get();
        }
        return node.value;
    }

    @Override
    public final <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
        Node<K, V> node = getNode(key);
        if (node == null) {
            throw exception;
        }
        return node.value;
    }

    @Override
    public final void set(K key, V value) {
        final Comparator<? super K> comparator = this.comparator;

        Node<K, V> node = root;

        if (node == null) {
            //noinspection ResultOfMethodCallIgnored,EqualsWithItself
            comparator.compare(key, key);

            root = new Node<>(key, value, null);
            size = 1;
            return;
        }

        int c;
        Node<K, V> parent;

        do {
            parent = node;
            c = comparator.compare(key, node.key);
            if (c < 0) {
                node = node.left;
            } else if (c > 0) {
                node = node.right;
            } else {
                node.value = value;
                return;
            }
        } while (node != null);

        Node<K, V> n = new Node<>(key, value, parent);
        if (c < 0) {
            parent.left = n;
        } else {
            parent.right = n;
        }
        ++size;
        fixAfterInsert(n);
    }

    @Override
    public final @NotNull Option<V> put(K key, V value) {
        final Comparator<? super K> comparator = this.comparator;

        Node<K, V> node = root;

        if (node == null) {
            //noinspection ResultOfMethodCallIgnored,EqualsWithItself
            comparator.compare(key, key);

            root = new Node<>(key, value, null);
            size = 1;
            return Option.none();
        }

        int c;
        Node<K, V> parent;

        do {
            parent = node;
            c = comparator.compare(key, node.key);
            if (c < 0) {
                node = node.left;
            } else if (c > 0) {
                node = node.right;
            } else {
                V oldValue = node.value;
                node.value = value;
                return Option.some(oldValue);
            }

        } while (node != null);

        Node<K, V> n = new Node<>(key, value, parent);
        if (c < 0) {
            parent.left = n;
        } else {
            parent.right = n;
        }
        ++size;
        fixAfterInsert(n);
        return Option.none();
    }


    @Override
    public final @NotNull Option<V> remove(K key) {
        Node<K, V> node = getNode(key);
        if (node == null) {
            return Option.none();
        }
        V oldValue = node.value;
        remove0(node);
        return Option.some(oldValue);
    }

    @Override
    public final Option<V> replace(K key, V value) {
        Node<K, V> node = getNode(key);
        if (node == null) {
            return Option.none();
        } else {
            V oldValue = node.value;
            node.value = value;
            return Option.some(oldValue);
        }
    }

    @Override
    public final boolean replace(K key, V oldValue, V newValue) {
        Node<K, V> node = getNode(key);
        if (node == null || !Objects.equals(node.value, oldValue)) {
            return false;
        } else {
            node.value = newValue;
            return true;
        }
    }

    @Override
    public void replaceAll(@NotNull BiFunction<? super K, ? super V, ? extends V> function) {
        Node<K, V> node = this.root;
        while (node != null) {
            node.value = function.apply(node.key, node.value);

            Node<K, V> n;
            if (node.right != null) {
                n = node.right;
                while (n.left != null) {
                    n = n.left;
                }
            } else {
                n = node.parent;
                Node<K, V> c = node;

                while (n != null && c == n.right) {
                    c = n;
                    n = n.parent;
                }
            }
            node = n;
        }
    }

    //region SortedMap

    @Override
    public final Comparator<? super K> comparator() {
        return this.comparator;
    }

    @Override
    public final K firstKey() {
        Node<K, V> node = firstNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public final K lastKey() {
        Node<K, V> node = lastNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.key;
    }

    //endregion

    @Override
    public final void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        Node<K, V> node = this.root;
        while (node != null) {
            consumer.accept(node.key, node.value);

            Node<K, V> n;
            if (node.right != null) {
                n = node.right;
                while (n.left != null) {
                    n = n.left;
                }
            } else {
                n = node.parent;
                Node<K, V> c = node;

                while (n != null && c == n.right) {
                    c = n;
                    n = n.parent;
                }
            }
            node = n;
        }
    }

    static final class Node<K, V> extends RedBlackTree.TreeNode<K, Node<K, V>> implements java.util.Map.Entry<K, V> {
        V value;

        public Node(K key, V value, Node<K, V> parent) {
            super(key, parent);
            this.key = key;
            this.value = value;
        }

        @Override
        public final K getKey() {
            return key;
        }

        @Override
        public final V getValue() {
            return value;
        }

        @Override
        public final V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    static final class Itr<K, V> extends AbstractMapIterator<K, V> {
        private Node<K, V> node;
        private V value;

        Itr(Node<K, V> node) {
            this.node = node;
        }

        @Override
        public final boolean hasNext() {
            return node != null;
        }

        @Override
        public final K nextKey() {
            final Node<K, V> node = this.node;
            if (node == null) {
                throw new NoSuchElementException();
            }

            Node<K, V> n;
            if (node.right != null) {
                n = node.right;
                while (n.left != null) {
                    n = n.left;
                }
            } else {
                n = node.parent;
                Node<K, V> c = node;

                while (n != null && c == n.right) {
                    c = n;
                    n = n.parent;
                }
            }
            this.node = n;
            this.value = node.value;
            return node.key;
        }

        @Override
        public final V getValue() {
            return value;
        }
    }

    static final class NodeItr<K, V> extends AbstractIterator<Node<K, V>> {
        private Node<K, V> node;

        NodeItr(Node<K, V> node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Node<K, V> next() {
            final Node<K, V> node = this.node;
            if (node == null) {
                throw new NoSuchElementException();
            }

            Node<K, V> n;
            if (node.right != null) {
                n = node.right;
                while (n.left != null) {
                    n = n.left;
                }
            } else {
                n = node.parent;
                Node<K, V> c = node;

                while (n != null && c == n.right) {
                    c = n;
                    n = n.parent;
                }
            }
            this.node = n;
            return node;
        }
    }

    static final class AsJava<K, V> extends AsJavaConvert.MutableMapAsJava<K, V, MutableTreeMap<K, V>> {
        AsJava(MutableTreeMap<K, V> source) {
            super(source);
        }

        @Override
        public @NotNull Set<Entry<K, V>> entrySet() {
            return new EntrySet<>(source);
        }

        static final class EntrySet<K, V> extends AsJavaConvert.MapAsJava.EntrySet<K, V, MutableTreeMap<K, V>> {
            EntrySet(MutableTreeMap<K, V> source) {
                super(source);
            }

            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public @NotNull Iterator<Entry<K, V>> iterator() {
                return source.root == null ? Iterators.empty() : (Iterator) new NodeItr<>(source.root);
            }
        }
    }
}
