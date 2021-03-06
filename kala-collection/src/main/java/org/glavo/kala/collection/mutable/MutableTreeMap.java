package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.SortedMap;
import org.glavo.kala.collection.base.AbstractIterator;
import org.glavo.kala.collection.base.AbstractMapIterator;
import org.glavo.kala.collection.base.MapIterator;
import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;


public final class MutableTreeMap<K, V> extends RedBlackTree<K, MutableTreeMap.Node<K, V>>
        implements MutableMap<K, V>, SortedMap<K, V> {
    public MutableTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

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

    }
}
