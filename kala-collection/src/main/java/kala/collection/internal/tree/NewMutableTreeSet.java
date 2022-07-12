package kala.collection.internal.tree;

import kala.internal.ComparableUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NewMutableTreeSet<E> {
    protected int compare(E e0, E e1) {
        return ComparableUtils.compare(e0, e1);
    }

    protected Node<E> root;

    protected @Nullable Node<E> search(E value) {
        Node<E> node = root;

        while (true) {
            if (node == null) return null;

            int c = compare(value, node.value0);
            if (c == 0) return node;

            if (c < 0) {
                node = node.node0;
                continue;
            }

            if (node.isTwoNode()) {
                node = node.node1;
                continue;
            }

            c = compare(value, node.value1);
            if (c == 0) return node;

            if (c < 0) {
                node = node.node1;
                continue;
            }

            node = node.node2;
        }

    }

    public boolean contains(Object value) {
        @SuppressWarnings("unchecked")
        E e = (E) value;
        return search(e) != null;
    }

    /*
    public boolean add(E value) {
        if (root == null) {
            root = new Node<>();
            root.value0 = value;
            return true;
        }

        Node<E> node = root;
        int tag = 0;
        while (true) {
            int c = compare(value, node.value0);
            if (c == 0) return false;

            if (c < 0) {
                if (node.isLeaf()) {
                    tag = 0;
                    break;
                } else {
                    node = node.node0;
                    continue;
                }
            }

            if (node.isTwoNode()) {
                if (node.isLeaf()) {
                    tag = 1;
                    break;
                } else {
                    node = node.node1;
                    continue;
                }
            }

            c = compare(value, node.value1);
            if (c == 0) return false;

            if (c > 0) {
                if (node.isLeaf()) {
                    tag = 2;
                    break;
                } else {
                    node = node.node2;
                    continue;
                }
            }

            if (node.isLeaf()) {
                tag = 1;
                break;
            } else {
                node = node.node1;
                continue;
            }
        }

        if (node.isTwoNode()) {
            if (tag == 0) {
                node.value1 = node.value0;
                node.value0 = value;
            } else {
                node.value1 = value;
            }
        } else {
            E v0, v1, v2;

            if (tag == 0) {
                v0 = value;
                v1 = node.value0;
                v2 = node.value1;
            } else if (tag == 1) {
                v0 = node.value0;
                v1 = value;
                v2 = node.value1;
            } else { // tag == 2
                v0 = node.value0;
                v1 = node.value1;
                v2 = value;
            }

            Node<E> newNode = new Node<>();
            newNode.value0 = v2;

            while (true) {
                if (node.parent == null) {
                    // TODO
                    break;
                }

                if (node.parent.isTwoNode()) {
                    node.setAsTwoNode();
                    node.value0 = v0;
                    node.value1 = null;

                    node.parent.setAsThreeNode();
                    node.parent.value1 = v1;


                } else {
                    // TODO
                }
            }

        }
    }
     */

    public void forEach(@NotNull Consumer<? super E> action) {
        forEach(root, action);
    }

    private static <E> void forEach(Node<E> node, Consumer<? super E> action) {
        if (node == null) return;
        if (node.isLeaf()) {
            action.accept(node.value0);
            if (node.isThreeNode())
                action.accept(node.value1);
            return;
        }

        forEach(node.node0, action);
        action.accept(node.value0);
        forEach(node.node1, action);

        if (node.isThreeNode()) {
            action.accept(node.value1);
            forEach(node.node2, action);
        }
    }

    private static class Node<E> {
        E value0, value1;

        Node<E> node0, node1, node2;

        Node<E> parent;
        private boolean isThreeNode;

        boolean isLeaf() {
            return node0 == null;
        }

        boolean isTwoNode() {
            return !isThreeNode;
        }

        boolean isThreeNode() {
            return isThreeNode;
        }

        void setAsTwoNode() {
            isThreeNode = false;
        }

        void setAsThreeNode() {
            isThreeNode = true;
        }
    }

}
