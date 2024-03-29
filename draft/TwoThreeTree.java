package kala.collection.internal.tree;

import kala.function.IndexedConsumer;
import kala.internal.ComparableUtils;
import kala.value.primitive.IntVar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public final class TwoThreeTree<E> {
    private int compare(E e0, E e1) {
        return ComparableUtils.compare(e0, e1);
    }

    private Node<E> root;
    private int len = 0;

    public int size() {
        return len;
    }

    private @Nullable Node<E> search(E value) {
        Node<E> node = root;

        while (true) {
            if (node == null) return null;

            int c = compare(value, node.value1);
            if (c == 0) return node;

            if (c < 0) {
                node = node.left;
                continue;
            }

            if (node.isTwoNode()) {
                node = node.right;
                continue;
            }

            c = compare(value, node.value2);
            if (c == 0) return node;

            if (c < 0) {
                node = node.middle;
                continue;
            }

            node = node.right;
        }

    }

    public boolean contains(Object value) {
        if (value == null) return false;
        @SuppressWarnings("unchecked")
        E e = (E) value;
        return search(e) != null;
    }

    public boolean add(@NotNull E value) {
        Objects.requireNonNull(value);

        if (root == null) {
            root = new Node<>(value);
            len = 1;
            return true;
        }

        Node<E> node = root;
        int tag;
        while (true) {
            int c = compare(value, node.value1);
            if (c == 0) return false;

            if (c < 0) {
                if (node.isLeaf()) {
                    tag = 0;
                    break;
                } else {
                    node = node.left;
                    continue;
                }
            }

            if (node.isTwoNode()) {
                if (node.isLeaf()) {
                    tag = 1;
                    break;
                } else {
                    node = node.right;
                    continue;
                }
            }

            c = compare(value, node.value2);
            if (c == 0) return false;

            if (c < 0) {
                if (node.isLeaf()) {
                    tag = 1;
                    break;
                } else {
                    node = node.middle;
                    continue;
                }
            }

            if (node.isLeaf()) {
                tag = 2;
                break;
            } else {
                node = node.right;
                continue;
            }
        }

        if (node.isTwoNode()) {
            if (tag == 0) {
                node.value2 = node.value1;
                node.value1 = value;
            } else {
                node.value2 = value;
            }
        } else {
            final E v0, v1, v2;

            if (tag == 0) {
                v0 = value;
                v1 = node.value1;
                v2 = node.value2;
            } else if (tag == 1) {
                v0 = node.value1;
                v1 = value;
                v2 = node.value2;
            } else { // tag == 2
                v0 = node.value1;
                v1 = node.value2;
                v2 = value;
            }

            Node<E> newNode = new Node<>(v2);
            E pv = v1;

            node.value1 = v0;
            node.value2 = null;

            while (true) {
                if (node.parent == null) {
                    Node<E> newRoot = new Node<>(pv);
                    newRoot.left = node;
                    newRoot.right = newNode;

                    node.parent = newRoot;
                    newNode.parent = newRoot;

                    this.root = newRoot;
                    break;
                }

                if (node.parent.isTwoNode()) {
                    if (compare(pv, node.parent.value1) < 0) {
                        node.parent.value2 = node.parent.value1;
                        node.parent.value1 = pv;
                    } else {
                        node.parent.value2 = pv;
                    }

                    if (node.parent.left == node) {
                        node.parent.middle = newNode;
                    } else {
                        node.parent.middle = node;
                        node.parent.right = newNode;
                    }
                    newNode.parent = node.parent;
                    break;
                } else {
                    final Node<E> nn;
                    final E pp;
                    { // spilt
                        if (compare(pv, node.parent.value1) < 0) {
                            pp = node.parent.value1;
                            node.parent.value1 = pv;
                            nn = new Node<>(node.parent.value2);
                        } else if (compare(pv, node.parent.value2) < 0) {
                            pp = pv;
                            nn = new Node<>(node.parent.value2);
                        } else {
                            pp = node.parent.value2;
                            nn = new Node<>(pv);
                        }
                        node.parent.value2 = null;
                    }

                    if (node.parent.left == node) {
                        nn.left = node.parent.middle;
                        node.parent.middle.parent = nn;

                        nn.right = node.parent.right;
                        node.parent.right.parent = nn;

                        node.parent.right = newNode;
                        newNode.parent = node.parent;
                    } else if (node.parent.middle == node) {
                        nn.left = newNode;
                        newNode.parent = nn;

                        nn.right = node.parent.right;
                        node.parent.right.parent = nn;

                        node.parent.right = node.parent.middle;
                    } else {
                        node.parent.right = node.parent.middle;
                        Node<E> tmp = node.parent.right;

                        nn.left = node;
                        node.parent = nn;

                        nn.right = newNode;
                        newNode.parent = nn;

                        node = tmp;
                    }

                    node.parent.middle = null;
                    node = node.parent;
                    pv = pp;
                    newNode = nn;
                }
            }

        }
        len++;
        return true;
    }

    public Object[] toArray() {
        Object[] res = new Object[len];
        IntVar i = new IntVar();
        forEach(v -> res[i.value++] = v);
        return res;
    }

    public void forEach(@NotNull Consumer<? super E> action) {
        forEach(root, action);
    }

    private static <E> void forEach(Node<E> node, Consumer<? super E> action) {
        if (node == null) return;
        if (node.isLeaf()) {
            action.accept(node.value1);
            if (node.isThreeNode())
                action.accept(node.value2);
            return;
        }

        forEach(node.left, action);
        action.accept(node.value1);
        if (node.isThreeNode()) {
            forEach(node.middle, action);
            action.accept(node.value2);
        }
        forEach(node.right, action);
    }

    public void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        forEachIndexed(root, action, 0);
    }

    private static <E> int forEachIndexed(Node<E> node, IndexedConsumer<? super E> action, int currentIndex) {
        if (node == null) return 0;

        if (node.isLeaf()) {
            if (node.isTwoNode()) {
                action.accept(currentIndex, node.value1);
                return 1;
            } else {
                action.accept(currentIndex, node.value1);
                action.accept(currentIndex + 1, node.value2);
                return 2;
            }
        }

        int n = 0;

        n += forEachIndexed(node.left, action, currentIndex + n);
        action.accept(currentIndex + n++, node.value1);
        if (node.isThreeNode()) {
            n += forEachIndexed(node.middle, action, currentIndex + n);
            action.accept(currentIndex + n++, node.value2);
        }
        n += forEachIndexed(node.right, action, currentIndex);

        return n;
    }


    public void print() {
        System.out.println("root=" + root);
        forEach(System.out::println);
    }

    private static class Node<E> {
        E value1, value2;

        Node<E> left, middle, right;

        Node<E> parent;

        Node() {
        }

        Node(E value1) {
            this.value1 = value1;
        }

        boolean isLeaf() {
            return left == null;
        }

        boolean isTwoNode() {
            return value2 == null;
        }

        boolean isThreeNode() {
            return value2 != null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Node[");
            if (isTwoNode()) {
                builder.append("value1=").append(value1);
            } else {
                builder.append("value1=").append(value1).append(", value2=").append(value2);
            }

            if (!isLeaf()) {
                builder.append(", ");
                if (isTwoNode()) {
                    builder.append("left=").append(left).append(", right=").append(right);
                } else {
                    builder.append("left=").append(left).append(", middle").append(middle).append(", right=").append(right);
                }
            }

            builder.append(']');
            return builder.toString();
        }
    }

}
