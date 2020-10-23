package asia.kala.collection.mutable;

import asia.kala.factory.CollectionFactory;
import asia.kala.collection.SortedSet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class MutableTreeSet<E> extends AbstractMutableSet<E>
        implements MutableSet<E>, SortedSet<E>, Cloneable, Serializable {

    private static final long serialVersionUID = 6211626172352429615L;

    private static final MutableTreeSet.Factory<? extends Comparable<?>> DEFAULT_FACTORY =
            new Factory<>(Comparator.naturalOrder());

    //region Fields

    @NotNull
    private final Comparator<? super E> comparator;

    private transient Node<E> root;
    private transient int size = 0;

    //endregion

    //region Constructors

    public MutableTreeSet() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public MutableTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator == null ? (Comparator<? super E>) Comparator.naturalOrder() : comparator;
    }

    //endregion

    //region Factory methods

    @NotNull
    public static <E extends Comparable<? super E>> CollectionFactory<E, ?, MutableTreeSet<E>> factory() {
        return (Factory<E>) DEFAULT_FACTORY;
    }

    @NotNull
    public static <E> CollectionFactory<E, ?, MutableTreeSet<E>> factory(Comparator<? super E> comparator) {
        if (comparator == null || comparator == Comparator.naturalOrder()) {
            return (Factory<E>) DEFAULT_FACTORY;
        }
        return new Factory<>(comparator);
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of() {
        return new MutableTreeSet<>();
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(E value1) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        return s;
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(E value1, E value2) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        return s;
    }

    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(E value1, E value2, E value3) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s;
    }

    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(E value1, E value2, E value3, E value4) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s;
    }

    @NotNull
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return s;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(E... values) {
        return from(values);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E> MutableTreeSet<E> of(Comparator<? super E> comparator) {
        return new MutableTreeSet<>(comparator);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        return s;
    }

    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        return s;
    }

    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s;
    }

    @NotNull
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(
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

    @NotNull
    @Contract(value = "_, _, _, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> of(
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


    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E> MutableTreeSet<E> of(Comparator<? super E> comparator, E... values) {
        return from(comparator, values);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E> MutableTreeSet<E> from(@NotNull SortedSet<? extends E> values) {
        final Comparator<E> comparator = (Comparator<E>) values.comparator();
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E> MutableTreeSet<E> from(@NotNull java.util.SortedSet<? extends E> values) {
        final Comparator<E> comparator = (Comparator<E>) values.comparator();
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> from(Iterable<? extends E> values) {
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

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> MutableTreeSet<E> from(E @NotNull [] values) {
        Objects.requireNonNull(values);
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.addAll(values);
        return s;
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E> MutableTreeSet<E> from(Comparator<? super E> comparator, @NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E> MutableTreeSet<E> from(Comparator<? super E> comparator, E @NotNull [] values) {
        Objects.requireNonNull(values);
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    //endregion

    //region Red–black tree helpers

    @Nullable
    private Node<E> getNode(E value) {
        final Comparator<? super E> comparator = this.comparator;
        Node<E> n = this.root;
        while (n != null) {
            int c = comparator.compare(value, n.value);
            if (c < 0) {
                n = n.left;
            } else if (c > 0) {
                n = n.right;
            } else {
                return n;
            }
        }

        return null;
    }

    @Nullable
    private Node<E> firstNode() {
        Node<E> node = root;
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Nullable
    private Node<E> lastNode() {
        Node<E> node = root;
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private static <E> boolean colorOf(Node<E> p) {
        return (p == null ? Node.BLACK : p.color);
    }

    private static <E> Node<E> parentOrNull(Node<E> p) {
        return (p == null ? null : p.parent);
    }

    private static <E> void setColor(Node<E> p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    private static <E> Node<E> leftOrNull(Node<E> p) {
        return (p == null) ? null : p.left;
    }

    private static <E> Node<E> rightOrNull(Node<E> p) {
        return (p == null) ? null : p.right;
    }

    private static <E> Node<E> minNode(Node<E> node) {
        if (node == null) {
            return null;
        }
        return minNodeNonNull(node);
    }

    private static <E> Node<E> minNodeNonNull(@NotNull Node<E> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private static <E> Node<E> maxNode(Node<E> node) {
        if (node == null) {
            return null;
        }
        return maxNodeNonNull(node);
    }

    private static <E> Node<E> maxNodeNonNull(@NotNull Node<E> node) {
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

    private static <E> Node<E> successor(Node<E> node) {
        if (node == null) {
            return null;
        }
        if (node.right != null) {
            return minNodeNonNull(node.right);
        }
        Node<E> n = node.parent;
        Node<E> ch = node;
        while (n != null && ch == n.right) {
            ch = n;
            n = n.parent;
        }
        return n;
    }

    private static <E> Node<E> predecessor(Node<E> node) {
        if (node == null) {
            return null;
        }
        if (node.left != null) {
            return maxNodeNonNull(node.left);
        }
        Node<E> p = node.parent;
        Node<E> ch = node;
        while (p != null && ch == p.left) {
            ch = p;
            p = p.parent;
        }
        return p;
    }

    private void fixAfterInsert(Node<E> x) {
        x.color = Node.RED;

        while (x != null && x != root && x.parent.color == Node.RED) {
            if (parentOrNull(x) == leftOrNull(parentOrNull(parentOrNull(x)))) {
                Node<E> y = rightOrNull(parentOrNull(parentOrNull(x)));
                if (colorOf(y) == Node.RED) {
                    setColor(parentOrNull(x), Node.BLACK);
                    setColor(y, Node.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), Node.RED);
                    x = parentOrNull(parentOrNull(x));
                } else {
                    if (x == rightOrNull(parentOrNull(x))) {
                        x = parentOrNull(x);
                        rotateLeft(x);
                    }
                    setColor(parentOrNull(x), Node.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), Node.RED);
                    rotateRight(parentOrNull(parentOrNull(x)));
                }
            } else {
                Node<E> y = leftOrNull(parentOrNull(parentOrNull(x)));
                if (colorOf(y) == Node.RED) {
                    setColor(parentOrNull(x), Node.BLACK);
                    setColor(y, Node.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), Node.RED);
                    x = parentOrNull(parentOrNull(x));
                } else {
                    if (x == leftOrNull(parentOrNull(x))) {
                        x = parentOrNull(x);
                        rotateRight(x);
                    }
                    setColor(parentOrNull(x), Node.BLACK);
                    setColor(parentOrNull(parentOrNull(x)), Node.RED);
                    rotateLeft(parentOrNull(parentOrNull(x)));
                }
            }
        }
        root.color = Node.BLACK;
    }

    private void fixAfterDelete(Node<E> x) {
        while (x != root && colorOf(x) == Node.BLACK) {
            if (x == leftOrNull(parentOrNull(x))) {
                Node<E> sib = rightOrNull(parentOrNull(x));
                if (colorOf(sib) == Node.RED) {
                    setColor(sib, Node.BLACK);
                    setColor(parentOrNull(x), Node.RED);
                    rotateLeft(parentOrNull(x));
                    sib = rightOrNull(parentOrNull(x));
                }

                if (colorOf(leftOrNull(sib)) == Node.BLACK &&
                        colorOf(rightOrNull(sib)) == Node.BLACK) {
                    setColor(sib, Node.RED);
                    x = parentOrNull(x);
                } else {
                    if (colorOf(rightOrNull(sib)) == Node.BLACK) {
                        setColor(leftOrNull(sib), Node.BLACK);
                        setColor(sib, Node.RED);
                        rotateRight(sib);
                        sib = rightOrNull(parentOrNull(x));
                    }
                    setColor(sib, colorOf(parentOrNull(x)));
                    setColor(parentOrNull(x), Node.BLACK);
                    setColor(rightOrNull(sib), Node.BLACK);
                    rotateLeft(parentOrNull(x));
                    x = root;
                }
            } else {
                Node<E> sib = leftOrNull(parentOrNull(x));

                if (colorOf(sib) == Node.RED) {
                    setColor(sib, Node.BLACK);
                    setColor(parentOrNull(x), Node.RED);
                    rotateRight(parentOrNull(x));
                    sib = leftOrNull(parentOrNull(x));
                }

                if (colorOf(rightOrNull(sib)) == Node.BLACK &&
                        colorOf(leftOrNull(sib)) == Node.BLACK) {
                    setColor(sib, Node.RED);
                    x = parentOrNull(x);
                } else {
                    if (colorOf(leftOrNull(sib)) == Node.BLACK) {
                        setColor(rightOrNull(sib), Node.BLACK);
                        setColor(sib, Node.RED);
                        rotateLeft(sib);
                        sib = leftOrNull(parentOrNull(x));
                    }
                    setColor(sib, colorOf(parentOrNull(x)));
                    setColor(parentOrNull(x), Node.BLACK);
                    setColor(leftOrNull(sib), Node.BLACK);
                    rotateRight(parentOrNull(x));
                    x = root;
                }
            }
        }

        setColor(x, Node.BLACK);
    }

    //endregion

    //region MutableSet members

    @Override
    public final boolean add(E value) {
        final Comparator<? super E> comparator = this.comparator;

        Node<E> node = root;

        if (node == null) {
            //noinspection ResultOfMethodCallIgnored
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
    public final boolean remove(E value) {
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

            if (node.color == Node.BLACK) {
                fixAfterDelete(replacement);
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            if (node.color == Node.BLACK) {
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

    //endregion

    //region SortedSet members

    @NotNull
    @Override
    public final Comparator<? super E> comparator() {
        return this.comparator;
    }

    @Override
    public final E first() {
        Node<E> node = firstNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.value;
    }

    @Override
    public final E last() {
        Node<E> node = lastNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.value;
    }

    //endregion

    //region MutableCollection members

    @Override
    public final String className() {
        return "MutableTreeSet";
    }

    @NotNull
    @Override
    public final <U> CollectionFactory<U, ?, MutableTreeSet<U>> iterableFactory() {
        return ((Factory<U>) DEFAULT_FACTORY);
    }

    @NotNull
    @Override
    public <U> CollectionFactory<U, ?, MutableTreeSet<U>> iterableFactory(Comparator<? super U> comparator) {
        return factory(comparator);
    }

    @NotNull
    @Override
    public final MutableSetEditor<E, MutableTreeSet<E>> edit() {
        return new MutableSetEditor<>(this);
    }

    @NotNull
    @Override
    public final Iterator<E> iterator() {
        return new MutableTreeSet.Itr<>(firstNode());
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final int knownSize() {
        return size;
    }

    //endregion

    //region Object members

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final @NotNull MutableTreeSet<E> clone() {
        return MutableTreeSet.from(comparator, this); // need to optimize
    }

    //endregion

    //region Serialization

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
        out.writeInt(size);
        for (E e : this) {
            out.writeObject(e);
        }
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            this.add((E) in.readObject());
        }
    }

    //endregion

    private static final class Node<E> {
        static final boolean RED = true;
        static final boolean BLACK = false;

        E value;
        Node<E> left;
        Node<E> right;
        Node<E> parent;

        boolean color = BLACK;

        Node(E value, Node<E> parent) {
            this.value = value;
            this.parent = parent;
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
        @NotNull
        private final Comparator<? super E> comparator;

        Factory(@NotNull Comparator<? super E> comparator) {
            this.comparator = comparator;
        }

        @Override
        public final MutableTreeSet<E> newBuilder() {
            return new MutableTreeSet<>(comparator);
        }
    }
}
