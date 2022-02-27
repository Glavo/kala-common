package kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Consumer;

class BTree<T> {
    static final int M = 8;
    static final int MAX_KEY_NUMBER = M - 1;

    private int size;
    private Node<T> root;

    private Comparator<T> comparator;

    protected int compare(T t0, T t1) {
        return comparator.compare(t0, t1);
    }

    int binarySearch(Node<T> node, T value) {
        int n = node.n;

        int low = 0;
        int high = n - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = node.getKey(mid);

            int c = compare(midVal, value);

            if (c < 0) {
                low = mid + 1;
            } else if (c > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return -(low + 1);
    }

    void forEachKey(@NotNull Node<T> node, Consumer<? super T> action) {
        int n = node.n;

        int i;
        if (node.isLeaf) {
            for (i = 0; i < n; i++) {
                action.accept(node.getKey(i));
            }
        } else {
            for (i = 0; i < n; i++) {
                action.accept(node.getKey(i));
                forEachKey(node.getNode(i), action);
            }
            forEachKey(node.getNode(i), action);
        }
    }

    Node<T> search(Node<T> node, T value) {
        while (true) {
            int idx = binarySearch(node, value);

            if (idx >= 0) {
                return node;
            }

            if (node.isLeaf) {
                return null;
            }

            node = node.getNode(-(idx + 1));
        }
    }


    static final class Node<T> {
        T parent;

        T k0, k1, k2, k3, k4, k5, k6;
        Node<T> n0, n1, n2, n3, n4, n5, n6, n7;

        boolean isLeaf;
        byte n;

        T getKey(int idx) {
            switch (idx) {
                case 0:
                    return k0;
                case 1:
                    return k1;
                case 2:
                    return k2;
                case 3:
                    return k3;
                case 4:
                    return k4;
                case 5:
                    return k5;
                case 6:
                    return k6;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        void setKey(int idx, T newValue) {
            switch (idx) {
                case 0:
                    k0 = newValue;
                    break;
                case 1:
                    k1 = newValue;
                    break;
                case 2:
                    k2 = newValue;
                    break;
                case 3:
                    k3 = newValue;
                    break;
                case 4:
                    k4 = newValue;
                    break;
                case 5:
                    k5 = newValue;
                    break;
                case 6:
                    k6 = newValue;
                    break;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        Node<T> getNode(int idx) {
            switch (idx) {
                case 0:
                    return n0;
                case 1:
                    return n1;
                case 2:
                    return n2;
                case 3:
                    return n3;
                case 4:
                    return n4;
                case 5:
                    return n5;
                case 6:
                    return n6;
                case 7:
                    return n7;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        void setNode(int idx, Node<T> newNode) {
            switch (idx) {
                case 0:
                    n0 = newNode;
                    break;
                case 1:
                    n1 = newNode;
                    break;
                case 2:
                    n2 = newNode;
                    break;
                case 3:
                    n3 = newNode;
                    break;
                case 4:
                    n4 = newNode;
                    break;
                case 5:
                    n5 = newNode;
                    break;
                case 6:
                    n6 = newNode;
                    break;
                case 7:
                    n7 = newNode;
                    break;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }
    }
}
