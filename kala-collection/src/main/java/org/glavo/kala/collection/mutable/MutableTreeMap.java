package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.SortedMap;
import org.glavo.kala.collection.base.AbstractMapIterator;
import org.glavo.kala.collection.base.MapIterator;
import org.glavo.kala.control.Option;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;


public final class MutableTreeMap<K, V> extends RedBlackTree<K, MutableTreeMap.Node<K, V>>
        implements MutableMap<K, V>, SortedMap<K, V> {
    public MutableTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

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
                return Option.some(node.value);
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

    static final class Node<K, V> extends RedBlackTree.TreeNode<K, Node<K, V>> {
        V value;

        public Node(K key, V value, Node<K, V> parent) {
            super(key, parent);
            this.key = key;
            this.value = value;
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
}
