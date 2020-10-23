package asia.kala.collection.mutable;

import asia.kala.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class MutableHashSet<E> extends AbstractMutableSet<E> implements Serializable {

    private static final long serialVersionUID = 56207218679792671L;

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private static final MutableHashSet.Factory<?> FACTORY = new Factory<>();

    //region Fields

    private Node<E>[] table;
    private int threshold;
    private double loadFactor;
    private int size = 0;

    //endregion

    //region Constructors

    public MutableHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashSet(@Range(from = 0, to = MAXIMUM_CAPACITY) int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashSet(@Range(from = 0, to = MAXIMUM_CAPACITY) int initialCapacity, double loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        if (loadFactor <= 0 || Double.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        this.loadFactor = loadFactor;
        this.table = (Node<E>[]) new Node<?>[tableSizeFor(initialCapacity)];
        this.threshold = newThreshold(table.length);
    }

    //endregion

    //region Factory methods

    @NotNull
    public static <E> CollectionFactory<E, ?, MutableHashSet<E>> factory() {
        return ((Factory<E>) FACTORY);
    }

    @NotNull
    @Contract(value = "-> new", pure = true)
    public static <E> MutableHashSet<E> of() {
        return new MutableHashSet<>();
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E> MutableHashSet<E> of(E value1) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.add(value1);
        return s;
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <E> MutableHashSet<E> of(E value1, E value2) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.add(value1);
        s.add(value2);
        return s;
    }

    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E> MutableHashSet<E> of(E value1, E value2, E value3) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s;
    }

    @NotNull
    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E> MutableHashSet<E> of(E value1, E value2, E value3, E value4) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s;
    }

    @NotNull
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E> MutableHashSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return s;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E> MutableHashSet<E> of(E... values) {
        return from(values);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E> MutableHashSet<E> from(E @NotNull [] values) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.addAll(values);
        return s;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <E> MutableHashSet<E> from(@NotNull Iterable<? extends E> values) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.addAll(values);
        return s;
    }

    //endregion

    //region HashSet helpers

    private static int improveHash(int originalHash) {
        return originalHash ^ (originalHash >>> 16);
    }

    private static int hash(Object obj) {
        if (obj == null) {
            return 0;
        }
        return improveHash(obj.hashCode());
    }

    private static int tableSizeFor(int capacity) {
        return Math.min(Integer.highestOneBit(Math.max(capacity - 1, 4)) * 2, MAXIMUM_CAPACITY);
    }

    private int newThreshold(int size) {
        return (int) ((double) size * loadFactor);
    }

    private int indexOf(int hash) {
        return hash & (table.length - 1);
    }

    private Node<E> findNode(E value) {
        final int hash = hash(value);
        Node<E> n = table[indexOf(hash)];
        if (n == null) {
            return null;
        }

        if (value == null) {
            while (true) {
                if (n.hash == 0 && n.value == null) {
                    return n;
                }
                if (n.next == null || n.hash > 0) {
                    return null;
                }
                n = n.next;
            }
        } else {
            while (true) {
                if (n.hash == hash && value.equals(n.value)) {
                    return n;
                }
                if (n.next == null || n.hash > hash) {
                    return null;
                }
                n = n.next;
            }
        }
    }

    private void growTable(int newLen) {
        int oldLen = table.length;
        threshold = newThreshold(newLen);
        if (size == 0) {
            table = (Node<E>[]) new Node<?>[newLen];
        } else {
            table = Arrays.copyOf(table, newLen);
            Node<E> preLow = new Node<>(null, 0);
            Node<E> preHigh = new Node<>(null, 0);

            while (oldLen < newLen) {
                int i = 0;
                while (i < oldLen) {
                    Node<E> old = table[i];
                    if (old != null) {
                        preLow.next = null;
                        preHigh.next = null;
                        Node<E> lastLow = preLow;
                        Node<E> lastHigh = preHigh;
                        Node<E> n = old;
                        while (n != null) {
                            Node<E> next = n.next;
                            if ((n.hash & oldLen) == 0) { // keep low
                                lastLow.next = n;
                                lastLow = n;
                            } else { // move to high
                                lastHigh.next = n;
                                lastHigh = n;
                            }
                            n = next;
                        }
                        lastLow.next = null;
                        if (old != preLow.next) {
                            table[i] = preLow.next;
                        }
                        if (preHigh.next != null) {
                            table[i + oldLen] = preHigh.next;
                            lastHigh.next = null;
                        }
                    }
                    ++i;
                }
                oldLen *= 2;
            }
        }
    }

    void sizeHint(int size) {
        int target = tableSizeFor((int) ((double) (size + 1) / loadFactor));
        if (target > table.length) {
            growTable(target);
        }
    }

    //endregion

    //region MutableSet members

    private boolean add(E value, int hash) {
        int idx = indexOf(hash);
        Node<E> n = table[idx];
        if (n == null) {
            table[idx] = new Node<>(value, hash);
        } else {
            final Node<E> old = n;
            Node<E> prev = null;
            while ((n != null) && n.hash <= hash) {
                if (n.hash == hash && Objects.equals(value, n.value)) {
                    return false;
                }
                prev = n;
                n = n.next;
            }
            if (prev == null) {
                table[idx] = new Node<>(value, hash, old);
            } else {
                prev.next = new Node<>(value, hash, prev.next);
            }
        }
        ++size;
        return true;
    }

    @Override
    public final boolean add(E value) {
        if (size + 1 >= threshold) {
            growTable(table.length * 2);
        }
        return add(value, hash(value));
    }

    @Override
    public boolean addAll(E @NotNull [] values) {
        final int s = values.length;
        if (s == 0) {
            return false;
        }
        sizeHint(s);
        for (E value : values) {
            this.add(value);
        }
        return true;
    }

    private boolean remove(E value, int hash) {
        int idx = indexOf(hash);
        Node<E> n = table[idx];
        if (n == null) {
            return false;
        }
        if (n.hash == hash && Objects.equals(n.value, value)) {
            table[idx] = n.next;
            --size;
            return true;
        }
        Node<E> prev = n;
        Node<E> next = n.next;
        while ((next != null) && next.hash <= hash) {
            if (next.hash == hash && Objects.equals(next.value, value)) {
                prev.next = next.next;
                --size;
                return true;
            }
            prev = next;
            next = next.next;
        }
        return false;
    }

    @Override
    public final boolean remove(E value) {
        return remove(value, hash(value));
    }

    @Override
    public final void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    //endregion

    //region MutableCollection members

    @Override
    public final String className() {
        return "MutableHashSet";
    }

    @NotNull
    @Override
    public final <U> CollectionFactory<U, ?, MutableHashSet<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    @Override
    public final MutableSetEditor<E, MutableHashSet<E>> edit() {
        return new MutableSetEditor<>(this);
    }

    @NotNull
    @Override
    public final Iterator<E> iterator() {
        return new Itr();
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

    //region Serialization

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeDouble(loadFactor);
        out.writeInt(size);
        for (E e : this) {
            out.writeObject(e);
        }
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        this.size = 0;
        this.loadFactor = in.readDouble();
        this.table = (Node<E>[]) new Node<?>[tableSizeFor(DEFAULT_INITIAL_CAPACITY)];
        this.threshold = newThreshold(table.length);

        int s = in.readInt();
        if (s == 0) {
            return;
        }
        sizeHint(s);
        for (int i = 0; i < s; i++) {
            this.add((E) in.readObject());
        }
    }

    //endregion

    private final class Itr implements Iterator<E> {
        private int i = 0;
        private Node<E> node = null;
        private int len = table.length;

        @Override
        public final boolean hasNext() {
            if (node != null) {
                return true;
            }

            while (i < len) {
                Node<E> n = table[i];
                ++i;
                if (n != null) {
                    node = n;
                    return true;
                }
            }
            return false;
        }

        @Override
        public final E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E res = node.value;
            node = node.next;
            return res;
        }
    }

    private static final class Node<E> {
        final E value;
        final int hash;

        Node<E> next;

        private Node(E value, int hash) {
            this.value = value;
            this.hash = hash;
        }

        private Node(E value, int hash, Node<E> next) {
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private static final class Factory<E> extends AbstractMutableSetFactory<E, MutableHashSet<E>> {
        @Override
        public final MutableHashSet<E> newBuilder() {
            return new MutableHashSet<>();
        }

        @Override
        public void sizeHint(@NotNull MutableHashSet<E> es, int size) {
            es.sizeHint(size);
        }
    }
}
