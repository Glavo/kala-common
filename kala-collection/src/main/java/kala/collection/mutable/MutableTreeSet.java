package kala.collection.mutable;

import kala.collection.Collection;
import kala.collection.SortedSet;
import kala.collection.base.Iterators;
import kala.collection.Set;
import kala.collection.base.AbstractIterator;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Collector;

@SuppressWarnings("unchecked")
public final class MutableTreeSet<E> extends RedBlackTree<E, MutableTreeSet.Node<E>>
        implements MutableSet<E>, SortedSet<E>, Cloneable, Serializable {

    private static final long serialVersionUID = 6211626172352429615L;

    private static final MutableTreeSet.Factory<? extends Comparable<?>> DEFAULT_FACTORY =
            new Factory<>(Comparator.naturalOrder());


    //region Constructors

    public MutableTreeSet() {
        this(null);
    }

    public MutableTreeSet(Comparator<? super E> comparator) {
        super(comparator);
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

    public static <E extends Comparable<? super E>> @NotNull Collector<E, ?, MutableTreeSet<E>> collector() {
        return factory();
    }

    public static <E> @NotNull Collector<E, ?, MutableTreeSet<E>> collector(Comparator<? super E> comparator) {
        return factory(comparator);
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

    //region Collection Operations

    @Override
    public final @NotNull String className() {
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
        return from(comparator, this); // need to optimize
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
            c = comparator.compare(value, node.getValue());
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
            node.setValue(s.getValue());
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
    public final boolean contains(Object value) {
        return getNode(value) != null;
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
        return node.getValue();
    }

    @Override
    public final E last() {
        final Node<E> node = lastNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.getValue();
    }

    @Override
    public final Object @NotNull [] toArray() {
        final int size = this.size;
        final Object[] res = new Object[size];
        if (size == 0) {
            return res;
        }
        final Iterator<E> it = this.iterator();
        for (int i = 0; i < size; i++) {
            res[i] = it.next();
        }
        return res;
    }

    @Override
    public final <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        final int size = this.size;
        final U[] res = generator.apply(size);
        if (size == 0) {
            return res;
        }
        final Iterator<E> it = this.iterator();
        for (int i = 0; i < size; i++) {
            res[i] = (U) it.next();
        }
        return res;
    }

    @Override
    public final int hashCode() {
        return Iterators.hash(iterator()) + Collection.SET_HASH_MAGIC;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Set<?>)
                || !(((Collection<?>) obj).canEqual(this))) {
            return false;
        }
        return sameElements(((Collection<?>) obj));
    }

    @Override
    public final void forEach(@NotNull Consumer<? super E> action) {
        forEachKey0(action);
    }

    @Override
    public final String toString() {
        return joinToString(", ", "MutableTreeSet[", "]");
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

    static final class Node<E> extends RedBlackTree.TreeNode<E, Node<E>> {
        Node(E value, Node<E> parent) {
            super(value, parent);
        }

        final void setValue(E newValue) {
            this.key = newValue;
        }

        final E getValue() {
            return key;
        }

        @Override
        public final String toString() {
            return String.format(
                    "MutableTreeSet.Node[value=%s, color=%s, parent=%s, left=%s, right=%s]",
                    key, color == RED ? "RED" : "BLACK", parent, left, right
            );
        }
    }

    private static final class Itr<E> extends AbstractIterator<E> {
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

            Node<E> n;
            if (node.right != null) {
                n = node.right;
                while (n.left != null) {
                    n = n.left;
                }
            } else {
                n = node.parent;
                Node<E> c = node;

                while (n != null && c == n.right) {
                    c = n;
                    n = n.parent;
                }
            }
            this.node = n;

            return node.getValue();
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
