package org.glavo.kala.collection.mutable;

import org.glavo.kala.comparator.Comparators;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.SortedSet;
import org.glavo.kala.collection.base.Iterators;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.glavo.kala.collection.mutable.RedBlackTree.*;

@SuppressWarnings("unchecked")
public final class MutableTreeSet<E> extends AbstractMutableSet<E>
        implements MutableSet<E>, SortedSet<E>, Cloneable, Serializable {

    private static final long serialVersionUID = 6211626172352429615L;

    private static final MutableTreeSet.Factory<? extends Comparable<?>> DEFAULT_FACTORY =
            new Factory<>(Comparator.naturalOrder());

    //region Fields

    private final @NotNull Comparator<? super E> comparator;

    private transient Node<E> root;
    private transient int size = 0;

    //endregion

    //region Constructors

    public MutableTreeSet() {
        this(null);
    }

    public MutableTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator == null ? Comparators.naturalOrder() : comparator;
    }

    //endregion

    //region Static Factories

    public static <E extends Comparable<? super E>> @NotNull CollectionFactory<E, ?, MutableTreeSet<E>> factory() {
        return (Factory<E>) DEFAULT_FACTORY;
    }

    public static <E> @NotNull CollectionFactory<E, ?, MutableTreeSet<E>> factory(Comparator<? super E> comparator) {
        if (comparator == null || comparator == Comparator.naturalOrder()) {
            return (Factory<E>) DEFAULT_FACTORY;
        }
        return new Factory<>(comparator);
    }

    @Contract(value = " -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of() {
        return new MutableTreeSet<>();
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E value1) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        return s;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E value1, E value2) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        return s;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E value1, E value2, E value3) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s;
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E value1, E value2, E value3, E value4) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s;
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E... values) {
        return from(values);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> of(Comparator<? super E> comparator) {
        return new MutableTreeSet<>(comparator);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        return s;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        return s;
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s;
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3, E value4
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s;
    }

    @Contract(value = "_, _, _, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3, E value4, E value5
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return s;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> of(Comparator<? super E> comparator, E... values) {
        return from(comparator, values);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> from(E @NotNull [] values) {
        Objects.requireNonNull(values);
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> from(@NotNull SortedSet<? extends E> values) {
        final Comparator<E> comparator = (Comparator<E>) values.comparator();
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> from(@NotNull java.util.SortedSet<? extends E> values) {
        final Comparator<E> comparator = (Comparator<E>) values.comparator();
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> from(Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        Comparator<? super E> comparator = null;

        if (values instanceof SortedSet<?>) {
            comparator = ((SortedSet<E>) values).comparator();
        } else if (values instanceof java.util.SortedSet<?>) {
            comparator = ((java.util.SortedSet<E>) values).comparator();
        }

        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> from(Comparator<? super E> comparator, E @NotNull [] values) {
        Objects.requireNonNull(values);
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> from(Comparator<? super E> comparator, @NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    //endregion

    //region Red–black tree helpers

    private @Nullable Node<E> getNode(Object value) {
        try {
            final Comparator<? super E> comparator = this.comparator;
            Node<E> n = this.root;
            while (n != null) {
                int c = comparator.compare((E) value, n.value);
                if (c < 0) {
                    n = n.left;
                } else if (c > 0) {
                    n = n.right;
                } else {
                    return n;
                }
            }
        } catch (ClassCastException ignored) {
        }
        return null;
    }

    private @Nullable Node<E> firstNode() {
        Node<E> node = root;
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private @Nullable Node<E> lastNode() {
        Node<E> node = root;
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private void rotateLeft(Node<E> node) {
        if (node == null) {
            return;
        }
        Node<E> r = node.right;
        node.right = r.left;
        if (r.left != null) {
            r.left.parent = node;
        }
        r.parent = node.parent;
        if (node.parent == null) {
            root = r;
        } else if (node.parent.left == node) {
            node.parent.left = r;
        } else {
            node.parent.right = r;
        }
        r.left = node;
        node.parent = r;
    }

    private void rotateRight(Node<E> node) {
        if (node == null) {
            return;
        }
        Node<E> l = node.left;
        node.left = l.right;
        if (l.right != null) {
            l.right.parent = node;
        }
        l.parent = node.parent;
        if (node.parent == null) {
            root = l;
        } else if (node.parent.right == node) {
            node.parent.right = l;
        } else {
            node.parent.left = l;
        }
        l.right = node;
        node.parent = l;
    }

    private void fixAfterInsert(Node<E> x) {
        x.color = RedBlackTree.RED;

        while (x != null && x != root && x.parent.color == RedBlackTree.RED) {
            if (parentOrNull(x) == leftOrNull(parentOrNull(parentOrNull(x)))) {
                Node<E> y = rightOrNull(parentOrNull(parentOrNull(x)));
                if (colorOf(y) == RedBlackTree.RED) {
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(y, RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), RedBlackTree.RED);
                    x = parentOrNull(parentOrNull(x));
                } else {
                    if (x == rightOrNull(parentOrNull(x))) {
                        x = parentOrNull(x);
                        rotateLeft(x);
                    }
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), RedBlackTree.RED);
                    rotateRight(parentOrNull(parentOrNull(x)));
                }
            } else {
                Node<E> y = leftOrNull(parentOrNull(parentOrNull(x)));
                if (colorOf(y) == RedBlackTree.RED) {
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(y, RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), RedBlackTree.RED);
                    x = parentOrNull(parentOrNull(x));
                } else {
                    if (x == leftOrNull(parentOrNull(x))) {
                        x = parentOrNull(x);
                        rotateRight(x);
                    }
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), RedBlackTree.RED);
                    rotateLeft(parentOrNull(parentOrNull(x)));
                }
            }
        }
        root.color = RedBlackTree.BLACK;
    }

    private void fixAfterDelete(Node<E> x) {
        while (x != root && colorOf(x) == RedBlackTree.BLACK) {
            if (x == leftOrNull(parentOrNull(x))) {
                Node<E> sib = rightOrNull(parentOrNull(x));
                if (colorOf(sib) == RedBlackTree.RED) {
                    setColor(sib, RedBlackTree.BLACK);
                    setColor(parentOrNull(x), RedBlackTree.RED);
                    rotateLeft(parentOrNull(x));
                    sib = rightOrNull(parentOrNull(x));
                }

                if (colorOf(leftOrNull(sib)) == RedBlackTree.BLACK &&
                        colorOf(rightOrNull(sib)) == RedBlackTree.BLACK) {
                    setColor(sib, RedBlackTree.RED);
                    x = parentOrNull(x);
                } else {
                    if (colorOf(rightOrNull(sib)) == RedBlackTree.BLACK) {
                        setColor(leftOrNull(sib), RedBlackTree.BLACK);
                        setColor(sib, RedBlackTree.RED);
                        rotateRight(sib);
                        sib = rightOrNull(parentOrNull(x));
                    }
                    setColor(sib, colorOf(parentOrNull(x)));
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(rightOrNull(sib), RedBlackTree.BLACK);
                    rotateLeft(parentOrNull(x));
                    x = root;
                }
            } else {
                Node<E> sib = leftOrNull(parentOrNull(x));

                if (colorOf(sib) == RedBlackTree.RED) {
                    setColor(sib, RedBlackTree.BLACK);
                    setColor(parentOrNull(x), RedBlackTree.RED);
                    rotateRight(parentOrNull(x));
                    sib = leftOrNull(parentOrNull(x));
                }

                if (colorOf(rightOrNull(sib)) == RedBlackTree.BLACK &&
                        colorOf(leftOrNull(sib)) == RedBlackTree.BLACK) {
                    setColor(sib, RedBlackTree.RED);
                    x = parentOrNull(x);
                } else {
                    if (colorOf(leftOrNull(sib)) == RedBlackTree.BLACK) {
                        setColor(rightOrNull(sib), RedBlackTree.BLACK);
                        setColor(sib, RedBlackTree.RED);
                        rotateLeft(sib);
                        sib = leftOrNull(parentOrNull(x));
                    }
                    setColor(sib, colorOf(parentOrNull(x)));
                    setColor(parentOrNull(x), RedBlackTree.BLACK);
                    setColor(leftOrNull(sib), RedBlackTree.BLACK);
                    rotateRight(parentOrNull(x));
                    x = root;
                }
            }
        }

        setColor(x, RedBlackTree.BLACK);
    }

    //endregion

    //region Collection Operations

    @Override
    public final String className() {
        return "MutableTreeSet";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, MutableTreeSet<U>> iterableFactory() {
        return ((Factory<U>) DEFAULT_FACTORY);
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, MutableTreeSet<U>> iterableFactory(Comparator<? super U> comparator) {
        return factory(comparator);
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        final Node<E> firstNode = firstNode();
        return firstNode == null ? Iterators.empty() : new MutableTreeSet.Itr<>(firstNode);
    }

    @Override
    public final @NotNull MutableSetEditor<E, MutableTreeSet<E>> edit() {
        return new MutableSetEditor<>(this);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final @NotNull MutableTreeSet<E> clone() {
        return MutableTreeSet.from(comparator, this); // need to optimize
    }

    //endregion

    //region Size Info

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final int knownSize() {
        return size;
    }

    //endregion

    @Override
    public final boolean add(E value) {
        final Comparator<? super E> comparator = this.comparator;

        Node<E> node = root;

        if (node == null) {
            //noinspection ResultOfMethodCallIgnored,EqualsWithItself
            comparator.compare(value, value);

            root = new Node<>(value, null);
            size = 1;
            return true;
        }

        int c;
        Node<E> parent;

        do {
            parent = node;
            c = comparator.compare(value, node.value);
            if (c < 0) {
                node = node.left;
            } else if (c > 0) {
                node = node.right;
            } else {
                return false;
            }

        } while (node != null);

        Node<E> n = new Node<>(value, parent);
        if (c < 0) {
            parent.left = n;
        } else {
            parent.right = n;
        }
        ++size;
        fixAfterInsert(n);
        return true;
    }

    @Override
    public final boolean remove(Object value) {
        Node<E> node = getNode(value);
        if (node == null) {
            return false;
        }
        if (node.left != null && node.right != null) {
            Node<E> s = successor(node);
            node.value = s.value;
            node = s;
        }

        Node<E> replacement = node.left != null ? node.left : node.right;

        if (replacement != null) {
            replacement.parent = node.parent;
            if (node.parent == null) {
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }
            node.left = node.right = node.parent = null;

            if (node.color == RedBlackTree.BLACK) {
                fixAfterDelete(replacement);
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            if (node.color == RedBlackTree.BLACK) {
                fixAfterDelete(node);
            }

            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null;
                } else if (node == node.parent.right) {
                    node.parent.right = null;
                }
                node.parent = null;
            }
        }
        return true;
    }

    @Override
    public final void clear() {
        root = null;
        size = 0;
    }

    @Override
    public final boolean contains(Object value) {
        return getNode((E) value) != null;
    }

    @Override
    public final Comparator<? super E> comparator() {
        return this.comparator;
    }

    @Override
    public final E first() {
        final Node<E> node = firstNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.value;
    }

    @Override
    public final E last() {
        final Node<E> node = lastNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.value;
    }

    //region Serialization Operations

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            this.add((E) in.readObject());
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
        out.writeInt(size);
        for (E e : this) {
            out.writeObject(e);
        }
    }

    //endregion

    static final class Node<E> extends RedBlackTree.TreeNode<Node<E>> {
        E value;

        Node(E value, Node<E> parent) {
            super(parent);
            this.value = value;
        }
    }

    private static final class Itr<E> implements Iterator<E> {
        private Node<E> node;

        Itr(Node<E> node) {
            this.node = node;
        }

        @Override
        public final boolean hasNext() {
            return node != null;
        }

        @Override
        public final E next() {
            final Node<E> node = this.node;
            if (node == null) {
                throw new NoSuchElementException();
            }

            if (node.right != null) {
                Node<E> n = node.right;
                while (n.left != null) {
                    n = n.left;
                }
                this.node = n;
            } else {
                Node<E> n = node.parent;
                Node<E> c = node;

                while (n != null && c == n.right) {
                    c = n;
                    n = n.parent;
                }
                this.node = n;
            }

            return node.value;
        }
    }

    private static final class Factory<E> extends AbstractMutableSetFactory<E, MutableTreeSet<E>> {

        private final @NotNull Comparator<? super E> comparator;

        Factory(@NotNull Comparator<? super E> comparator) {
            this.comparator = comparator;
        }

        @Override
        public final MutableTreeSet<E> newBuilder() {
            return new MutableTreeSet<>(comparator);
        }
    }
}
