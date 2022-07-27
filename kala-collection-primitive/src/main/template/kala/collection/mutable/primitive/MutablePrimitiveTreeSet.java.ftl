package kala.collection.mutable.primitive;

import kala.collection.AnySet;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.primitive.${Type}Set;
import kala.collection.primitive.internal.tree.${Type}RedBlackTree;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

public final class Mutable${Type}TreeSet extends ${Type}RedBlackTree<Mutable${Type}TreeSet.Node>
        implements Mutable${Type}Set, Cloneable, Serializable {
    private static final long serialVersionUID = 6211626172352429615L;

    private static final Mutable${Type}TreeSet.Factory FACTORY = new Factory();

    //region Constructors

    public Mutable${Type}TreeSet() {
    }

    //endregion

    //region Static Factories

    public static @NotNull ${Type}CollectionFactory<?, Mutable${Type}TreeSet> factory() {
        return FACTORY;
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet create() {
        return new Mutable${Type}TreeSet();
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet of() {
        return new Mutable${Type}TreeSet();
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet of(${PrimitiveType} value1) {
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.add(value1);
        return s;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.add(value1);
        s.add(value2);
        return s;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s;
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s;
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet of(${PrimitiveType}... values) {
        return from(values);
    }


    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet from(${PrimitiveType} @NotNull [] values) {
        Objects.requireNonNull(values);
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet from(@NotNull ${Type}Traversable values) {
        Objects.requireNonNull(values);

        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Mutable${Type}TreeSet from(@NotNull ${Type}Iterator it) {
        Mutable${Type}TreeSet s = new Mutable${Type}TreeSet();
        while (it.hasNext()) { // implicit null check of it
            s.add(it.next${Type}());
        }
        return s;
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "Mutable${Type}TreeSet";
    }

    @Override
    public @NotNull ${Type}CollectionFactory<?, Mutable${Type}TreeSet> iterableFactory() {
        return FACTORY;
    }

    @Override
    public @NotNull ${Type}Iterator iterator() {
        final Node firstNode = firstNode();
        return firstNode == null ? ${Type}Iterator.empty() : new Mutable${Type}TreeSet.Itr(firstNode);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull Mutable${Type}TreeSet clone() {
        return from(this); // need to optimize
    }

    @Override
    public boolean add(${PrimitiveType} value) {
        Node node = root;

        if (node == null) {
            root = new Node(value, null);
            size = 1;
            return true;
        }

        int c;
        Node parent;

        do {
            parent = node;
            c = ${WrapperType}.compare(value, node.getValue());
            if (c < 0) {
                node = node.left;
            } else if (c > 0) {
                node = node.right;
            } else {
                return false;
            }

        } while (node != null);


        Node n = new Node(value, parent);
        if (c < 0) {
            parent.left = n;
        } else {
            parent.right = n;
        }
        size++;
        fixAfterInsert(n);
        return true;
    }

    @Override
    public boolean remove(${PrimitiveType} value) {
        Node node = getNode(value);
        if (node == null) {
            return false;
        }
        remove0(node);
        return true;
    }

    @Override
    public boolean contains(${PrimitiveType} value) {
        return getNode(value) != null;
    }

    // @Override
    public ${PrimitiveType} first() {
        final Node node = firstNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.getValue();
    }

    // @Override
    public ${PrimitiveType} last() {
        final Node node = lastNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.getValue();
    }

    @Override
    public ${PrimitiveType} @NotNull [] toArray() {
        final int size = this.size;
        final ${PrimitiveType}[] res = new ${PrimitiveType}[size];
        if (size == 0) {
            return res;
        }
        final ${Type}Iterator it = this.iterator();
        for (int i = 0; i < size; i++) {
            res[i] = it.next${Type}();
        }
        return res;
    }

    @Override
    public int hashCode() {
        return ${Type}Set.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnySet && ${Type}Set.equals(this, (AnySet<?>) obj);
    }

    @Override
    public void forEach(@NotNull ${Type}Consumer action) {
        forEachKey0(action);
    }

    @Override
    public String toString() {
        return joinToString(", ", "MutableTreeSet[", "]");
    }

    //region Serialization Operations

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            this.add(in.read${Type}());
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
        out.writeInt(size);
        this.forEachUnchecked(out::write${Type});
    }

    //endregion

    static final class Node extends ${Type}RedBlackTree.Node<Node> {
        Node(${PrimitiveType} value, Node parent) {
            super(value, parent);
        }

        void setValue(${PrimitiveType} newValue) {
            this.key = newValue;
        }

        ${PrimitiveType} getValue() {
            return key;
        }

        @Override
        public String toString() {
            return String.format(
                    "Mutable${Type}TreeSet.Node[value=%s, color=%s, parent=%s, left=%s, right=%s]",
                    key, color == RED ? "RED" : "BLACK", parent, left, right
            );
        }
    }

    private static final class Itr extends Abstract${Type}Iterator {
        private Node node;

        Itr(Node node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            final Node node = this.node;
            if (node == null) {
                throw new NoSuchElementException();
            }

            Node n;
            if (node.right != null) {
                n = node.right;
                while (n.left != null) {
                    n = n.left;
                }
            } else {
                n = node.parent;
                Node c = node;

                while (n != null && c == n.right) {
                    c = n;
                    n = n.parent;
                }
            }
            this.node = n;

            return node.getValue();
        }
    }

    private static final class Factory extends AbstractMutable${Type}SetFactory<Mutable${Type}TreeSet> {
        @Override
        public Mutable${Type}TreeSet newBuilder() {
            return new Mutable${Type}TreeSet();
        }
    }
}
